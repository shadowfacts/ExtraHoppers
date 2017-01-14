package net.shadowfacts.extrahoppers

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack

/**
 * @author shadowfacts
 */
object EHCreativeTab: CreativeTabs(MOD_ID) {

	override fun getTabIconItem(): ItemStack {
		return ItemStack(ExtraHoppers.blocks.fluidHopper)
//		TODO: change me to the wooden hopper
	}

}