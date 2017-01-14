package net.shadowfacts.extrahoppers.block.fluid

import net.minecraft.block.BlockLiquid
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraftforge.fluids.Fluid.BUCKET_VOLUME
import net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.shadowfacts.extrahoppers.util.FluidUtils
import net.shadowfacts.extrahoppers.EHConfig
import net.shadowfacts.extrahoppers.block.base.TileEntityHopperBase
import net.shadowfacts.shadowmc.ShadowMC
import net.shadowfacts.shadowmc.capability.CapHolder
import net.shadowfacts.shadowmc.fluid.FluidTank
import net.shadowfacts.shadowmc.nbt.AutoSerializeNBT
import net.shadowfacts.shadowmc.network.PacketUpdateTE

/**
 * @author shadowfacts
 */
class TileEntityFluidHopper: TileEntityHopperBase(), ITickable {

	companion object {
		val HANDLER_COOLDOWN = 8
		val WORLD_COOLDOWN = 40
	}

	@AutoSerializeNBT
	@CapHolder(capabilities = arrayOf(IFluidHandler::class))
	internal var tank = FluidTank(EHConfig.fhSize)

	private var handlerCooldown: Int = HANDLER_COOLDOWN
	private var worldCooldown: Int = WORLD_COOLDOWN

	internal fun save() {
		markDirty()
		ShadowMC.network.sendToAllAround(PacketUpdateTE(this), NetworkRegistry.TargetPoint(world.provider.dimension, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), 64.0))
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
			val handlerPos = pos.up()
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
			if (FluidUtils.isFluidBlock(world, pos.up())) {
				val toDrain = FluidUtils.drainFluidBlock(world, pos.up(), false)!!
				if (toDrain.amount <= tank.capacity - tank.fluidAmount) {
					tank.fill(FluidUtils.drainFluidBlock(world, pos.up(), true), true)
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

}
