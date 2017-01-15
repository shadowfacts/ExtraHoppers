package net.shadowfacts.extrahoppers

import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.ShapedOreRecipe

/**
 * @author shadowfacts
 */
object ModRecipes {

	fun init() {
		GameRegistry.addRecipe(ShapedOreRecipe(ExtraHoppers.blocks.fluidHopper, "I I", "I I", " B ", 'I', "ingotIron", 'B', Items.BUCKET))
		GameRegistry.addRecipe(ShapedOreRecipe(ExtraHoppers.blocks.woodenHopper, "W W", "W W", " W ", 'W', "plankWood"))
		GameRegistry.addRecipe(ShapedOreRecipe(ExtraHoppers.blocks.woodenFluidHopper, "W W", "W W", " B ", 'W', "plankWood", 'B', Items.BUCKET))

		GameRegistry.addShapelessRecipe(ItemStack(ExtraHoppers.blocks.invertedHopper), Blocks.HOPPER)
		GameRegistry.addShapelessRecipe(ItemStack(Blocks.HOPPER), ExtraHoppers.blocks.invertedHopper)

		GameRegistry.addShapelessRecipe(ItemStack(ExtraHoppers.blocks.invertedFluidHopper), ExtraHoppers.blocks.fluidHopper)
		GameRegistry.addShapelessRecipe(ItemStack(ExtraHoppers.blocks.fluidHopper), ExtraHoppers.blocks.invertedFluidHopper)

		GameRegistry.addShapelessRecipe(ItemStack(ExtraHoppers.blocks.invertedWoodenHopper), ExtraHoppers.blocks.woodenHopper)
		GameRegistry.addShapelessRecipe(ItemStack(ExtraHoppers.blocks.woodenHopper), ExtraHoppers.blocks.invertedWoodenHopper)

		GameRegistry.addShapelessRecipe(ItemStack(ExtraHoppers.blocks.invertedWoodenFluidHopper), ExtraHoppers.blocks.woodenFluidHopper)
		GameRegistry.addShapelessRecipe(ItemStack(ExtraHoppers.blocks.woodenFluidHopper), ExtraHoppers.blocks.invertedWoodenFluidHopper)
	}

}