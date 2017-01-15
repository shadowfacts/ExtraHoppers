package net.shadowfacts.extrahoppers.block.wooden_fluid

import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.shadowfacts.extrahoppers.ExtraHoppers
import net.shadowfacts.extrahoppers.block.fluid.BlockFluidHopper
import net.shadowfacts.extrahoppers.block.fluid.TileEntityFluidHopper
import net.shadowfacts.extrahoppers.gui.GUIHandler

/**
 * @author shadowfacts
 */
class BlockWoodenFluidHopper(inverted: Boolean): BlockFluidHopper(inverted, name = "wooden_fluid_hopper", material = Material.WOOD) {

	init {
		setHardness(1.5f)
		setResistance(4f)
		soundType = SoundType.WOOD
	}

	override fun onBlockActivated(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer, hand: EnumHand, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		if (player.isSneaking) {
			GUIHandler.woodenFluidHopperOpen = true
			player.openGui(ExtraHoppers, GUIHandler.WOODEN_FLUID_HOPPER, world, pos.x, pos.y, pos.z)
			return true
		} else {
			return super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ)
		}
	}

	override fun addInformation(stack: ItemStack, player: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
	}

	override fun createTileEntity(world: World, state: IBlockState): TileEntityFluidHopper {
		return TileEntityWoodenFluidHopper()
	}

}