package net.shadowfacts.extrahoppers.block

import net.shadowfacts.extrahoppers.block.advanced.BlockAdvancedHopper
import net.shadowfacts.extrahoppers.block.fluid.BlockFluidHopper
import net.shadowfacts.extrahoppers.block.inverted.BlockInvertedHopper
import net.shadowfacts.extrahoppers.block.wooden.BlockWoodenHopper
import net.shadowfacts.extrahoppers.block.wooden_fluid.BlockWoodenFluidHopper

/**
 * @author shadowfacts
 */
object ModBlocks {

	val invertedHopper = BlockInvertedHopper()
	val fluidHopper = BlockFluidHopper(false, false)
	val invertedFluidHopper = BlockFluidHopper(true, false)
	val advancedFluidHopper = BlockFluidHopper(false, true)
	val invertedAdvancedFluidHopper = BlockFluidHopper(true, true)
	val woodenHopper = BlockWoodenHopper(false)
	val invertedWoodenHopper = BlockWoodenHopper(true)
	val woodenFluidHopper = BlockWoodenFluidHopper(false)
	val invertedWoodenFluidHopper = BlockWoodenFluidHopper(true)
	val advancedHopper = BlockAdvancedHopper(false)
	val invertedAdvancedHopper = BlockAdvancedHopper(true)

}