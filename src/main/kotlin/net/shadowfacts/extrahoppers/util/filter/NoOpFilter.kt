package net.shadowfacts.extrahoppers.util.filter

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

/**
 * @author shadowfacts
 */
object NoOpFilter: Filter<Any>() {

	override fun setFromStack(i: Int, stack: ItemStack) {
	}

	override fun accepts(stack: Any) = true

	override fun writeToNBT(tag: NBTTagCompound) = tag

	override fun readFromNBT(tag: NBTTagCompound) {
	}

}