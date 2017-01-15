package net.shadowfacts.extrahoppers.block.base

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.shadowfacts.shadowmc.tileentity.BaseTileEntity

/**
 * @author shadowfacts
 */
abstract class TileEntityHopperBase(var inverted: Boolean): BaseTileEntity() {

	constructor(): this(false)

	fun getHopperFacing(): EnumFacing {
		return world.getBlockState(pos).getValue(BlockHopperBase.FACING)
	}

	override fun writeToNBT(tag: NBTTagCompound): NBTTagCompound {
		tag.setBoolean("inverted", inverted)
		return super.writeToNBT(tag)
	}

	override fun readFromNBT(tag: NBTTagCompound) {
		inverted = tag.getBoolean("inverted")
		super.readFromNBT(tag)
	}

}