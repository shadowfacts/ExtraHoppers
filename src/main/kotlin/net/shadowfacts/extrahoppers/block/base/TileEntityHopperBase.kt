package net.shadowfacts.extrahoppers.block.base

import net.minecraft.util.EnumFacing
import net.shadowfacts.shadowmc.tileentity.BaseTileEntity

/**
 * @author shadowfacts
 */
abstract class TileEntityHopperBase: BaseTileEntity() {

	fun getHopperFacing(): EnumFacing {
		return world.getBlockState(pos).getValue(BlockHopperBase.FACING)
	}

}