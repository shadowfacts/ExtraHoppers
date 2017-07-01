package net.shadowfacts.extrahoppers.util.filter

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraftforge.common.util.Constants
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
import net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY
import net.minecraftforge.fluids.capability.IFluidHandler
import net.shadowfacts.forgelin.extensions.forEach

/**
 * @author shadowfacts
 */
class FluidFilter(size: Int): Filter<FluidStack>() {

	private val filter: Array<FluidStack?> = arrayOfNulls(size)

	override fun setFromStack(i: Int, stack: ItemStack) {
		if (stack.isEmpty) {
			filter[i] = null
		} else {
			val handler: IFluidHandler = if (stack.hasCapability(FLUID_HANDLER_CAPABILITY, null)) {
				stack.getCapability(FLUID_HANDLER_CAPABILITY, null)!!
			} else if (stack.hasCapability(FLUID_HANDLER_ITEM_CAPABILITY, null)) {
				stack.getCapability(FLUID_HANDLER_ITEM_CAPABILITY, null)!!
			} else {
				return
			}
			filter[i] = handler.tankProperties[0].contents?.copy()
		}
	}

	override fun accepts(stack: FluidStack): Boolean {
		return filter.contains(stack)
	}

	override fun writeToNBT(tag: NBTTagCompound): NBTTagCompound {
		val list = NBTTagList()
		filter.forEach {
			if (it != null) {
				list.appendTag(it.writeToNBT(NBTTagCompound()))
			}
		}
		tag.setTag("filter", list)
		return tag
	}

	override fun readFromNBT(tag: NBTTagCompound) {
		val list = tag.getTagList("filter", Constants.NBT.TAG_COMPOUND)
		var i = 0
		list.forEach {
			if (it is NBTTagCompound) {
				filter[i] = FluidStack.loadFluidStackFromNBT(it)
				i++
			}
		}
	}

}