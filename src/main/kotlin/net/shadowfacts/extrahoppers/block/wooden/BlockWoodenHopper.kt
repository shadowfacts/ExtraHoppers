package net.shadowfacts.extrahoppers.block.wooden

import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.registry.GameRegistry
import net.shadowfacts.extrahoppers.ExtraHoppers
import net.shadowfacts.extrahoppers.block.base.BlockHopperBase
import net.shadowfacts.extrahoppers.gui.GUIHandler

/**
 * @author shadowfacts
 */
class BlockWoodenHopper: BlockHopperBase<TileEntityWoodenHopper>("wooden_hopper", Material.WOOD) {

	init {
		setHardness(1.5f)
		setResistance(4f)
		soundType = SoundType.WOOD
	}

	override fun onBlockActivated(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		player.openGui(ExtraHoppers, GUIHandler.WOODEN_HOPPER, world, pos.x, pos.y, pos.z)
		return true
	}

	override fun registerTileEntity() {
		GameRegistry.registerTileEntity(TileEntityWoodenHopper::class.java, registryName.toString())
	}

	override fun createTileEntity(world: World, state: IBlockState): TileEntityWoodenHopper {
		return TileEntityWoodenHopper()
	}

}