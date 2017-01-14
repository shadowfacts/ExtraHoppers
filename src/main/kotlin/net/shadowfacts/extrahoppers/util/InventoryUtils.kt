package net.shadowfacts.extrahoppers.util

import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraftforge.items.ItemHandlerHelper
import net.shadowfacts.forgelin.extensions.get
import net.shadowfacts.forgelin.extensions.set

/**
 * @author shadowfacts
 */

/**
 * Attempts to insert the given [ItemStack] into this [IInventory]
 * @param stack The stack to insert
 * @param slot The slot to insert into
 * @return The remained of the stack that couldn't been inserted
 */
fun IInventory.insert(stack: ItemStack, slot: Int): ItemStack {
	if (stack.isEmpty) {
		return stack
	}
	val existing = this[slot]
	var limit = inventoryStackLimit
	if (!existing.isEmpty) {
		if (!ItemHandlerHelper.canItemStacksStack(stack, existing)) {
			return stack
		}
		limit -= existing.count
	}

	if (limit <= 0) {
		return stack
	}

	val reachedLimit = stack.count > limit

	if (existing.isEmpty) {
		this[slot] = if (reachedLimit) ItemHandlerHelper.copyStackWithSize(stack, limit) else stack
	} else {
		existing.grow(if (reachedLimit) limit else stack.count)
	}
	return if (reachedLimit) ItemHandlerHelper.copyStackWithSize(stack, stack.count - limit) else ItemStack.EMPTY
}