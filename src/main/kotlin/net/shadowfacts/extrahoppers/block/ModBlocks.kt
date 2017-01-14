package net.shadowfacts.extrahoppers.block

import net.minecraft.block.Block
import net.shadowfacts.extrahoppers.block.fluid.BlockFluidHopper
import net.shadowfacts.shadowmc.block.ModBlocks

/**
 * @author shadowfacts
 */
object ModBlocks: ModBlocks() {

	val fluidHopper = BlockFluidHopper()

	override fun init() {
		register(fluidHopper)
	}

	override fun <T: Block> register(block: T): T {
		if (block is BlockTE<*>) {
			block.registerTileEntity()
		}
		return super.register(block)
	}

}