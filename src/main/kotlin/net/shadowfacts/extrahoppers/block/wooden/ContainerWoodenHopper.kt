package net.shadowfacts.extrahoppers.block.wooden

import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Slot
import net.minecraft.util.math.BlockPos
import net.minecraftforge.items.SlotItemHandler
import net.shadowfacts.shadowmc.inventory.ContainerBase

/**
 * @author shadowfacts
 */
class ContainerWoodenHopper(hopper: TileEntityWoodenHopper, playerInv: InventoryPlayer, pos: BlockPos): ContainerBase(pos) {

	init {
		addSlotToContainer(object: SlotItemHandler(hopper.inventory, 0, 80, 21) {
			override fun onSlotChanged() {
				hopper.markDirty()
			}
		})

		for (l in 0..2) {
			for (k in 0..8) {
				this.addSlotToContainer(Slot(playerInv, k + l * 9 + 9, 8 + k * 18, l * 18 + 52))
			}
		}

		for (i1 in 0..8) {
			this.addSlotToContainer(Slot(playerInv, i1, 8 + i1 * 18, 110))
		}
	}

}