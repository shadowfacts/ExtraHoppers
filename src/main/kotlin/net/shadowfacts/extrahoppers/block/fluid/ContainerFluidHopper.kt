package net.shadowfacts.extrahoppers.block.fluid

import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Slot
import net.minecraft.util.math.BlockPos
import net.minecraftforge.items.SlotItemHandler
import net.shadowfacts.shadowmc.inventory.ContainerBase

/**
 * @author shadowfacts
 */
class ContainerFluidHopper(val hopper: TileEntityFluidHopper, playerInv: InventoryPlayer, pos: BlockPos): ContainerBase(pos) {

	init {
		addSlotToContainer(SlotHopperIO(hopper, 0, 26, 35))
		addSlotToContainer(SlotHopperIO(hopper, 1, 134, 35))

		for (i in 0..2) {
			for (j in 0..8) {
				addSlotToContainer(Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18))
			}
		}

		for (k in 0..8) {
			this.addSlotToContainer(Slot(playerInv, k, 8 + k * 18, 142))
		}
	}

	private class SlotHopperIO(val hopper: TileEntityFluidHopper, index: Int, x: Int, y: Int): SlotItemHandler(hopper.ioInventory, index, x, y) {
		override fun onSlotChanged() {
			hopper.markDirty()
		}
	}

}