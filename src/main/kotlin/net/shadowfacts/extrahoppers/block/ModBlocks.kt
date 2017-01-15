package net.shadowfacts.extrahoppers.block

import net.minecraftforge.fml.common.registry.GameRegistry
import net.shadowfacts.extrahoppers.block.fluid.BlockFluidHopper
import net.shadowfacts.extrahoppers.block.fluid.TileEntityFluidHopper
import net.shadowfacts.extrahoppers.block.inverted.BlockInvertedHopper
import net.shadowfacts.extrahoppers.block.inverted.TileEntityInvertedHopper
import net.shadowfacts.extrahoppers.block.wooden.BlockWoodenHopper
import net.shadowfacts.extrahoppers.block.wooden.TileEntityWoodenHopper
import net.shadowfacts.extrahoppers.block.wooden_fluid.BlockWoodenFluidHopper
import net.shadowfacts.extrahoppers.block.wooden_fluid.TileEntityWoodenFluidHopper
import net.shadowfacts.shadowmc.block.ModBlocks

/**
 * @author shadowfacts
 */
object ModBlocks: ModBlocks() {

	val invertedHopper = BlockInvertedHopper()
	val fluidHopper = BlockFluidHopper(false)
	val invertedFluidHopper = BlockFluidHopper(true)
	val woodenHopper = BlockWoodenHopper(false)
	val invertedWoodenHopper = BlockWoodenHopper(true)
	val woodenFluidHopper = BlockWoodenFluidHopper(false)
	val invertedWoodenFluidHopper = BlockWoodenFluidHopper(true)

	override fun init() {
		register(invertedHopper)
		register(fluidHopper)
		register(invertedFluidHopper)
		register(woodenHopper)
		register(invertedWoodenHopper)
		register(woodenFluidHopper)
		register(invertedWoodenFluidHopper)

		GameRegistry.registerTileEntity(TileEntityInvertedHopper::class.java, invertedHopper.registryName.toString())
		GameRegistry.registerTileEntityWithAlternatives(TileEntityFluidHopper::class.java, fluidHopper.registryName.toString(), "funnels:funnel")
		GameRegistry.registerTileEntity(TileEntityWoodenHopper::class.java, woodenHopper.registryName.toString())
		GameRegistry.registerTileEntity(TileEntityWoodenFluidHopper::class.java, woodenFluidHopper.registryName.toString())
	}

}