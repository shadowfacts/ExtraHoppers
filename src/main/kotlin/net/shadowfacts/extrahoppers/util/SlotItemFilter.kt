package net.shadowfacts.extrahoppers.util

import net.minecraftforge.items.SlotItemHandler
import net.shadowfacts.extrahoppers.block.inventory.TileEntityInventoryHopper
import net.shadowfacts.extrahoppers.util.filter.ItemFilter

/**
 * @author shadowfacts
 */
class SlotItemFilter(hopper: TileEntityInventoryHopper, id: Int, x: Int, y: Int): SlotItemHandler((hopper.filter as ItemFilter).inventory, id, x, y)