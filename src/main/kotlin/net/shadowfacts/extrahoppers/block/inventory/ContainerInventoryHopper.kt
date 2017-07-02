package net.shadowfacts.extrahoppers.block.inventory

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.ClickType
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraftforge.items.SlotItemHandler
import net.shadowfacts.extrahoppers.util.SlotItemFilter
import net.shadowfacts.shadowmc.inventory.ContainerBase

/**
 * @author shadowfacts
 */
class ContainerInventoryHopper(val hopper: TileEntityInventoryHopper, playerInv: InventoryPlayer, pos: BlockPos): ContainerBase(pos) {

	init {
		for (i in 0.until(hopper.inventory.slots)) {
			addSlotToContainer(SlotHopper(hopper, i, 44 + i * 18, 21))
		}

		if (hopper.advanced) {
			for (i in 0..2) {
				addSlotToContainer(SlotItemFilter(hopper, i, -62 + i * 18, 12))
			}
			for (i in 0..2) {
				addSlotToContainer(SlotItemFilter(hopper, i + 3, -62 + i * 18, 33))
			}
		}

		for (l in 0..2) {
			for (k in 0..8) {
				addSlotToContainer(Slot(playerInv, k + l * 9 + 9, 8 + k * 18, l * 18 + 52))
			}
		}

		for (i1 in 0..8) {
			addSlotToContainer(Slot(playerInv, i1, 8 + i1 * 18, 110))
		}
	}

	override fun slotClick(id: Int, dragType: Int, type: ClickType, player: EntityPlayer): ItemStack {
		if (id in hopper.inventory.slots..hopper.inventory.slots + 5) {
			if (type == ClickType.PICKUP || type == ClickType.PICKUP_ALL || type == ClickType.SWAP) {
				hopper.filter.setFromStack(id - hopper.inventory.slots, player.inventory.itemStack)
				hopper.markDirty()
			}
			return ItemStack.EMPTY
		}
		return super.slotClick(id, dragType, type, player)
	}

	override fun transferStackInSlot(player: EntityPlayer?, index: Int): ItemStack {
		if (index in hopper.inventory.slots..hopper.inventory.slots + 5) { // if the slot is a filter slot
			return ItemStack.EMPTY
		}

		var itemstack = ItemStack.EMPTY
		val slot = inventorySlots[index]

		if (slot != null && slot.hasStack) {
			val itemstack1 = slot.stack
			itemstack = itemstack1.copy()

			if (index < hopper.inventory.slots) {
				if (!this.mergeItemStack(itemstack1, hopper.inventory.slots + 6, inventorySlots.size, true)) {
					return ItemStack.EMPTY
				}
			} else if (!this.mergeItemStack(itemstack1, 0, hopper.inventory.slots, false)) {
				return ItemStack.EMPTY
			}

			if (itemstack1.count == 0) {
				slot.putStack(ItemStack.EMPTY)
			} else {
				slot.onSlotChanged()
			}

			if (itemstack1.count == itemstack.count) {
				return ItemStack.EMPTY
			}

			slot.onTake(player, itemstack1)
		}

		return itemstack
	}

	private class SlotHopper(val hopper: TileEntityInventoryHopper, index: Int, x: Int, y: Int): SlotItemHandler(hopper.inventory, index, x, y) {
		override fun onSlotChanged() {
			hopper.markDirty()
		}
	}

}