package net.shadowfacts.extrahoppers.block.advanced

import net.minecraft.item.ItemStack
import net.shadowfacts.extrahoppers.block.inventory.TileEntityInventoryHopper
import net.shadowfacts.extrahoppers.util.filter.Filter
import net.shadowfacts.extrahoppers.util.filter.ItemFilter

/**
 * @author shadowfacts
 */
class TileEntityAdvancedHopper(inverted: Boolean): TileEntityInventoryHopper(inverted, true, 5)  {

	override var filter: Filter<ItemStack> = ItemFilter(6)

	constructor(): this(false)

}