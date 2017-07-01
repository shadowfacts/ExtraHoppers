package net.shadowfacts.extrahoppers

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack

/**
 * @author shadowfacts
 */
object EHCreativeTab: CreativeTabs(MOD_ID) {

	override fun getTabIconItem() = ItemStack(ExtraHoppers.blocks.woodenHopper)

}