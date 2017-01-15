package net.shadowfacts.extrahoppers.block.base

import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Slot
import net.minecraft.util.math.BlockPos
import net.minecraftforge.items.SlotItemHandler
import net.shadowfacts.extrahoppers.block.inverted.TileEntityInvertedHopper
import net.shadowfacts.shadowmc.inventory.ContainerBase

/**
 * @author shadowfacts
 */
class ContainerInvertedHopper(hopper: TileEntityInvertedHopper, playerInv: InventoryPlayer, pos: BlockPos): ContainerBase(pos) {

	init {
		for (i in 0.until(hopper.inventory.slots)) {
			addSlotToContainer(SlotHopper(hopper, i, 44 + i * 18, 21))
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

	private class SlotHopper(val hopper: TileEntityInvertedHopper, index: Int, x: Int, y: Int): SlotItemHandler(hopper.inventory, index, x, y) {
		override fun onSlotChanged() {
			hopper.markDirty()
		}
	}

}