package net.shadowfacts.extrahoppers.block.fluid

import net.minecraft.block.BlockLiquid
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.fluids.Fluid.BUCKET_VOLUME
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidTank
import net.minecraftforge.fluids.FluidUtil
import net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
import net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY
import net.minecraftforge.fluids.capability.IFluidHandlerItem
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.items.ItemStackHandler
import net.shadowfacts.extrahoppers.EHConfig
import net.shadowfacts.extrahoppers.block.base.TileEntityHopperBase
import net.shadowfacts.extrahoppers.util.FluidUtils
import net.shadowfacts.extrahoppers.util.filter.Filter
import net.shadowfacts.extrahoppers.util.filter.FluidFilter
import net.shadowfacts.extrahoppers.util.getFluidHandler
import net.shadowfacts.extrahoppers.util.hasFluidHandler
import net.shadowfacts.forgelin.extensions.get
import net.shadowfacts.forgelin.extensions.set
import net.shadowfacts.shadowmc.ShadowMC
import net.shadowfacts.shadowmc.network.PacketRequestTEUpdate
import net.shadowfacts.shadowmc.network.PacketUpdateTE

/**
 * @author shadowfacts
 */
open class TileEntityFluidHopper(inverted: Boolean, advanced: Boolean): TileEntityHopperBase<FluidStack>(inverted, advanced), ITickable {

	companion object {
		val HANDLER_COOLDOWN = 8
		val WORLD_COOLDOWN = 40
	}

	override var filter: Filter<FluidStack> = FluidFilter(6)

	constructor(): this(false, false)

	internal var tank = object: FluidTank(EHConfig.fhSize) {
		override fun onContentsChanged() {
			save()
		}

		override fun canFillFluidType(fluid: FluidStack?): Boolean {
//			return fluid == null || (fluidValiator(fluid) && filterAccepts(fluid))
			return fluid == null || fluidValiator(fluid)
		}
	}
	val ioInventory = object: ItemStackHandler(2) {
		override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
			if (!stack.hasFluidHandler()) return stack
//			val stackHandler = stack.getFluidHandler()
//			when (slot) {
//				0 -> { // insert fluid
//					if (stackHandler.drain(tank.fill(stackHandler.drain(tank.capacity - tank.fluidAmount, false), false), false) == null) return stack
//				}
//				1 -> { // extract fluid
//					if (tank.drain(stackHandler.fill(tank.drain(tank.fluidAmount, false), false), false) == null) return stack
//				}
//			}
			return super.insertItem(slot, stack, simulate)
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
			handlerCooldown--
			worldCooldown--

			if (!checkRedstone()) return

			extractFromItem()
			insertIntoItem()

			if (handlerCooldown <= 0) {
				handleFluidHandlers()
			}
			if (worldCooldown <= 0) {
				handleWorld()
			}
		}
	}

	private fun extractFromItem() {
		val stack = ioInventory[0]
		if (!stack.isEmpty) {
			val res = FluidUtil.tryEmptyContainer(stack, tank, tank.capacity - tank.fluidAmount, null, true)
			if (res.isSuccess) {
				ioInventory[0] = res.result
				save()
			}
		}
	}

	private fun insertIntoItem() {
		val stack = ioInventory[1]
		if (!stack.isEmpty) {
			val res = FluidUtil.tryFillContainer(stack, tank, tank.fluidAmount, null, true)
			if (res.isSuccess) {
				ioInventory[1] = res.result
				save()
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
				if (toDrain.amount <= tank.capacity - tank.fluidAmount && tank.fill(toDrain, false) == 1000) {
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
		tag.setTag("tank", tank.writeToNBT(NBTTagCompound()))
		tag.setTag("ioInventory", ioInventory.serializeNBT())
		tag.setInteger("handlerCooldown", handlerCooldown)
		tag.setInteger("worldCooldown", worldCooldown)
		return super.writeToNBT(tag)
	}

	override fun readFromNBT(tag: NBTTagCompound) {
		tank.readFromNBT(tag.getCompoundTag("tank"))
		ioInventory.deserializeNBT(tag.getCompoundTag("ioInventory"))
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
