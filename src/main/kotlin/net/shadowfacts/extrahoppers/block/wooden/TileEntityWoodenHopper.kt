package net.shadowfacts.extrahoppers.block.wooden

import net.minecraft.item.ItemStack
import net.minecraft.util.ITickable
import net.shadowfacts.extrahoppers.block.inventory.TileEntityInventoryHopper
import net.shadowfacts.extrahoppers.util.filter.Filter
import net.shadowfacts.extrahoppers.util.filter.NoOpFilter

/**
 * @author shadowfacts
 */
class TileEntityWoodenHopper(inverted: Boolean): TileEntityInventoryHopper(inverted, false, 1), ITickable {

	override var filter: Filter<ItemStack> = NoOpFilter

	constructor(): this(false)

}