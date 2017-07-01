package net.shadowfacts.extrahoppers.block.inverted

import net.minecraft.item.ItemStack
import net.shadowfacts.extrahoppers.block.inventory.TileEntityInventoryHopper
import net.shadowfacts.extrahoppers.util.filter.Filter
import net.shadowfacts.extrahoppers.util.filter.NoOpFilter

/**
 * @author shadowfacts
 */
class TileEntityInvertedHopper: TileEntityInventoryHopper(true, false, 5) {

	override var filter: Filter<ItemStack> = NoOpFilter

}