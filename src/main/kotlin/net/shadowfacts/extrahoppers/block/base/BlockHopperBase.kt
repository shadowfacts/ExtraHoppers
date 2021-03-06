package net.shadowfacts.extrahoppers.block.base

import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.shadowfacts.extrahoppers.block.BlockTE
import net.shadowfacts.shadowmc.tileentity.BaseTileEntity

/**
 * @author shadowfacts
 */
abstract class BlockHopperBase<out TE: BaseTileEntity>(val inverted: Boolean, val advanced: Boolean, name: String, material: Material = Material.ROCK): BlockTE<TE>(getName(inverted, advanced, name), material = material) {

	companion object {
		val FACING: PropertyDirection = PropertyDirection.create("facing")

		val BASE_AABB = AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.625, 1.0)
		val SOUTH_AABB = AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.125)
		val NORTH_AABB = AxisAlignedBB(0.0, 0.0, 0.875, 1.0, 1.0, 1.0)
		val WEST_AABB = AxisAlignedBB(0.875, 0.0, 0.0, 1.0, 1.0, 1.0)
		val EAST_AABB = AxisAlignedBB(0.0, 0.0, 0.0, 0.125, 1.0, 1.0)

		private fun getName(inverted: Boolean, advanced: Boolean, name: String): String {
			var name = name
			if (advanced) name = "advanced_$name"
			if (inverted) name = "inverted_$name"
			return name
		}
	}

	init {
		defaultState = defaultState.withProperty(FACING, EnumFacing.DOWN)
	}

	@Deprecated("")
	override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB {
		return FULL_BLOCK_AABB
	}

	@Deprecated("")
	override fun addCollisionBoxToList(state: IBlockState, world: World, pos: BlockPos, entityBox: AxisAlignedBB, collidingBoxes: MutableList<AxisAlignedBB>, entity: Entity?, p_185477_7_: Boolean) {
		addCollisionBoxToList(pos, entityBox, collidingBoxes, BASE_AABB)
		addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB)
		addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB)
		addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB)
		addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB)
	}

	override fun createBlockState(): BlockStateContainer {
		return BlockStateContainer(this, FACING)
	}

	override fun getMetaFromState(state: IBlockState): Int {
		return state.getValue(FACING).index
	}

	@Deprecated("")
	override fun getStateFromMeta(meta: Int): IBlockState {
		return defaultState.withProperty(FACING, EnumFacing.getFront(meta))
	}

	override fun getStateForPlacement(world: World, pos: BlockPos, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase, hand: EnumHand): IBlockState {
		var side = facing.opposite
		if (inverted) {
			if (side == EnumFacing.DOWN) side = EnumFacing.UP
		} else {
			if (side == EnumFacing.UP) side = EnumFacing.DOWN
		}
		return defaultState.withProperty(FACING, side)
	}

	@Deprecated("")
	override fun isOpaqueCube(state: IBlockState) = false

	@Deprecated("")
	override fun isFullCube(state: IBlockState?) = false


}