package net.shadowfacts.extrahoppers.block

import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.shadowfacts.extrahoppers.EHCreativeTab
import net.shadowfacts.shadowmc.block.BlockBase
import net.shadowfacts.shadowmc.tileentity.BaseTileEntity

/**
 * @author shadowfacts
 */
abstract class BlockTE<out TE: BaseTileEntity>(name: String, material: Material = Material.ROCK): BlockBase(material, name) {

	init {
		unlocalizedName = registryName.toString()
		setCreativeTab(EHCreativeTab)
	}

	abstract fun registerTileEntity()

	abstract override fun createTileEntity(world: World, state: IBlockState): TE

	override fun hasTileEntity(state: IBlockState): Boolean {
		return true
	}

	fun getTileEntity(world: IBlockAccess, pos: BlockPos): TE {
		return world.getTileEntity(pos) as TE
	}

}