package net.shadowfacts.extrahoppers.util.filter

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.items.ItemStackHandler
import net.shadowfacts.forgelin.extensions.forEach
import net.shadowfacts.forgelin.extensions.set

/**
 * @author shadowfacts
 */
class ItemFilter(size: Int): Filter<ItemStack>() {

	val inventory = ItemStackHandler(size)

	override fun setFromStack(i: Int, stack: ItemStack) {
		inventory[i] = stack.copy().apply {
			count = 1
		}
	}

	override fun accepts(stack: ItemStack): Boolean {
		inventory.forEach {
			if (!it.isEmpty && ItemStack.areItemStacksEqual(it, stack)) {
				return true
			}
		}
		return false
	}

	override fun writeToNBT(tag: NBTTagCompound): NBTTagCompound {
		tag.setTag("filter", inventory.serializeNBT())
		return tag
	}

	override fun readFromNBT(tag: NBTTagCompound) {
		inventory.deserializeNBT(tag.getCompoundTag("filter"))
	}

}