package net.shadowfacts.extrahoppers.util.filter

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

/**
 * @author shadowfacts
 */
abstract class Filter<in T> {

	abstract fun setFromStack(i: Int, stack: ItemStack)

	abstract fun accepts(stack: T): Boolean

	abstract fun writeToNBT(tag: NBTTagCompound): NBTTagCompound

	abstract fun readFromNBT(tag: NBTTagCompound)

}