package net.shadowfacts.extrahoppers.block.advanced

import net.minecraft.block.SoundType
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.items.ItemHandlerHelper
import net.shadowfacts.extrahoppers.ExtraHoppers
import net.shadowfacts.extrahoppers.block.base.BlockHopperBase
import net.shadowfacts.extrahoppers.gui.GUIHandler

/**
 * @author shadowfacts
 */
class BlockAdvancedHopper(inverted: Boolean): BlockHopperBase<TileEntityAdvancedHopper>(inverted, true, "hopper") {

	init {
		setHardness(3.5f)
		setResistance(8f)
		soundType = SoundType.METAL
	}

	@Deprecated("")
	override fun hasComparatorInputOverride(state: IBlockState?): Boolean {
		return true
	}

	@Deprecated("")
	override fun getComparatorInputOverride(state: IBlockState, world: World, pos: BlockPos): Int {
		return ItemHandlerHelper.calcRedstoneFromInventory(getTileEntity(world, pos).inventory)
	}

	override fun onBlockActivated(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		player.openGui(ExtraHoppers, GUIHandler.ADVANCED_HOPPER, world, pos.x, pos.y, pos.z)
		return true
	}

	override fun createTileEntity(world: World, state: IBlockState): TileEntityAdvancedHopper {
		return TileEntityAdvancedHopper(inverted)
	}

}