package net.shadowfacts.extrahoppers

import net.minecraft.init.Items
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.ShapedOreRecipe

/**
 * @author shadowfacts
 */
object ModRecipes {

	fun init() {
		GameRegistry.addRecipe(ShapedOreRecipe(ExtraHoppers.blocks.fluidHopper, "I I", "I I", " B ", 'I', "ingotIron", 'B', Items.BUCKET))
		GameRegistry.addRecipe(ShapedOreRecipe(ExtraHoppers.blocks.woodenHopper, "W W", "W W", " W ", 'W', "plankWood"))
	}

}