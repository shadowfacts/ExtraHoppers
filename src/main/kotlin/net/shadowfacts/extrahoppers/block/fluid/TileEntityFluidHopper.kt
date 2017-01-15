package net.shadowfacts.extrahoppers.block.fluid

import net.minecraft.block.BlockLiquid
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.fluids.Fluid.BUCKET_VOLUME
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidTank
import net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.shadowfacts.extrahoppers.EHConfig
import net.shadowfacts.extrahoppers.block.base.TileEntityHopperBase
import net.shadowfacts.extrahoppers.util.FluidUtils
import net.shadowfacts.shadowmc.ShadowMC
import net.shadowfacts.shadowmc.network.PacketRequestTEUpdate
import net.shadowfacts.shadowmc.network.PacketUpdateTE

/**
 * @author shadowfacts
 */
open class TileEntityFluidHopper(inverted: Boolean): TileEntityHopperBase(inverted), ITickable {

	constructor(): this(false)

	companion object {
		val HANDLER_COOLDOWN = 8
		val WORLD_COOLDOWN = 40
	}

	internal var tank = object: FluidTank(EHConfig.fhSize) {
		override fun onContentsChanged() {
			save()
		}

		override fun canFillFluidType(fluid: FluidStack?): Boolean {
			return fluid == null || fluidValiator(fluid)
		}
	}

	protected open val fluidValiator: (FluidStack) -> Boolean = { true }

	private var handlerCooldown: Int = HANDLER_COOLDOWN
	private var worldCooldown: Int = WORLD_COOLDOWN

	internal fun save() {
		markDirty()
		ShadowMC.network.sendToAllAround(PacketUpdateTE(this), NetworkRegistry.TargetPoint(world.provider.dimension, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), 64.0))
	}

	override fun onLoad() {
		if (world.isRemote) {
			ShadowMC.network.sendToServer(PacketRequestTEUpdate(this))
		}
	}

	override fun update() {
		if (!world.isRemote) {
//			TODO: redstone me
			handlerCooldown--
			if (handlerCooldown <= 0) {
				handleFluidHandlers()
			}

			worldCooldown--
			if (worldCooldown <= 0) {
				handleWorld()
			}
		}
	}

	private fun handleFluidHandlers() {
		val transferOut = transferOut()
		val transferIn = transferIn()

		if (transferOut || transferIn) {
			handlerCooldown = HANDLER_COOLDOWN
			save()
		}
	}

	private fun transferIn(): Boolean {
		if (tank.fluidAmount < tank.capacity) {
			val handlerPos = if (inverted) pos.down() else pos.up()
			val te = world.getTileEntity(handlerPos)
			if (te != null && te.hasCapability(FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN)) {
				val handler = te.getCapability(FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN)
				tank.fill(handler!!.drain(tank.capacity - tank.fluidAmount, true), true)
				return true
			}
		}
		return false
	}

	private fun transferOut(): Boolean {
		if (tank.fluidAmount > 0) {
			val facing = getHopperFacing()
			val handlerPos = pos.offset(facing)
			val te = world.getTileEntity(handlerPos)
			if (te != null && te.hasCapability(FLUID_HANDLER_CAPABILITY, facing.opposite)) {
				val handler = te.getCapability(FLUID_HANDLER_CAPABILITY, facing.opposite)
				tank.drain(handler!!.fill(tank.drain(20, false), true), true)
				return true
			}
		}
		return false
	}

	private fun handleWorld() {
		val pickup = pickupFromWorld()
		val place = placeInWorld()

		if (pickup || place) {
			worldCooldown = WORLD_COOLDOWN
			save()
		}
	}

	private fun pickupFromWorld(): Boolean {
		if (EHConfig.fhPickupWorldFluids && tank.fluidAmount <= tank.capacity - BUCKET_VOLUME) {
			val pickupPos = if (inverted) pos.down() else pos.up()
			if (FluidUtils.isFluidBlock(world, pickupPos)) {
				val toDrain = FluidUtils.drainFluidBlock(world, pickupPos, false)!!
				if (toDrain.amount <= tank.capacity - tank.fluidAmount && tank.fill(toDrain, false) === 1000) {
					tank.fill(FluidUtils.drainFluidBlock(world, pickupPos, true), true)
					return true
				}
			}
		}
		return false
	}

	private fun placeInWorld(): Boolean {
		if (EHConfig.fhPlaceFluidsInWorld && tank.fluidAmount >= BUCKET_VOLUME) {
			val fluid = tank.fluid
			if (fluid!!.fluid.canBePlacedInWorld()) {
				var fluidBlock = fluid.fluid.block
				if (fluidBlock is BlockLiquid) fluidBlock = BlockLiquid.getFlowingBlock(fluidBlock.getDefaultState().material)

				val newPos = pos.offset(getHopperFacing())
				if (fluidBlock.canPlaceBlockAt(world, newPos)) {
					tank.drain(BUCKET_VOLUME, true)
					world.setBlockState(newPos, fluidBlock.defaultState)
					return true
				}
			}
		}
		return false
	}

	override fun writeToNBT(tag: NBTTagCompound): NBTTagCompound {
		tank.writeToNBT(tag)
		tag.setInteger("handlerCooldown", handlerCooldown)
		tag.setInteger("worldCooldown", worldCooldown)
		return super.writeToNBT(tag)
	}

	override fun readFromNBT(tag: NBTTagCompound) {
		tank.readFromNBT(tag)
		handlerCooldown = tag.getInteger("handlerCooldown")
		worldCooldown = tag.getInteger("worldCooldown")
		super.readFromNBT(tag)
	}

	override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
		if (capability == FLUID_HANDLER_CAPABILITY) return true
		else return super.hasCapability(capability, facing)
	}

	override fun <T: Any?> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
		if (capability == FLUID_HANDLER_CAPABILITY) return tank as T
		else return super.getCapability(capability, facing)
	}

}
