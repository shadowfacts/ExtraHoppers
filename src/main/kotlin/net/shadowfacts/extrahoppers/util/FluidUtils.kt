package net.shadowfacts.extrahoppers.util

import net.minecraft.block.BlockLiquid
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.IFluidBlock

/**
 * @author shadowfacts
 */
object FluidUtils {

	fun isFluidBlock(world: World, pos: BlockPos): Boolean {
		val state = world.getBlockState(pos)
		if (state.block is BlockLiquid) {
			return state.getValue(BlockLiquid.LEVEL) == 0
		}
		if (state.block is IFluidBlock) {
			return (state.block as IFluidBlock).canDrain(world, pos)
		}
		return false
	}

	fun drainFluidBlock(world: World, pos: BlockPos, doDrain: Boolean): FluidStack? {
		var stack: FluidStack? = null

		val state = world.getBlockState(pos)

		if (state.block is BlockLiquid && state.getValue(BlockLiquid.LEVEL) == 0) {
			if (state.block === Blocks.WATER || state.block === Blocks.FLOWING_WATER) {
				stack = FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME)
				if (doDrain) world.setBlockToAir(pos)
			} else if (state.block === Blocks.LAVA || state.block === Blocks.FLOWING_LAVA) {
				stack = FluidStack(FluidRegistry.LAVA, Fluid.BUCKET_VOLUME)
				if (doDrain) world.setBlockToAir(pos)
			}
		} else if (state.block is IFluidBlock) {
			stack = (state.block as IFluidBlock).drain(world, pos, doDrain)
		}

		return stack
	}

}