package net.shadowfacts.extrahoppers.block

import net.minecraft.block.Block
import net.shadowfacts.extrahoppers.block.fluid.BlockFluidHopper
import net.shadowfacts.extrahoppers.block.wooden.BlockWoodenHopper
import net.shadowfacts.extrahoppers.block.wooden_fluid.BlockWoodenFluidHopper
import net.shadowfacts.shadowmc.block.ModBlocks

/**
 * @author shadowfacts
 */
object ModBlocks: ModBlocks() {

	val fluidHopper = BlockFluidHopper()
	val woodenHopper = BlockWoodenHopper()
	val woodenFluidHopper = BlockWoodenFluidHopper()

	override fun init() {
		register(fluidHopper)
		register(woodenHopper)
		register(woodenFluidHopper)
	}

	override fun <T: Block> register(block: T): T {
		if (block is BlockTE<*>) {
			block.registerTileEntity()
		}
		return super.register(block)
	}

}