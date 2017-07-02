package net.shadowfacts.extrahoppers.block.base

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.shadowfacts.extrahoppers.util.filter.Filter
import net.shadowfacts.extrahoppers.util.filter.FilterMode
import net.shadowfacts.shadowmc.ShadowMC
import net.shadowfacts.shadowmc.network.PacketRequestTEUpdate
import net.shadowfacts.shadowmc.tileentity.BaseTileEntity
import net.shadowfacts.shadowmc.util.RedstoneMode

/**
 * @author shadowfacts
 */
abstract class TileEntityHopperBase<T>(var inverted: Boolean, var advanced: Boolean): BaseTileEntity() {

	var mode = RedstoneMode.LOW
	private var prevPowered = false

	var filterMode = FilterMode.BLACKLIST
	abstract var filter: Filter<T>

	constructor(): this(false, false)

	override fun onLoad() {
		if (world.isRemote) {
			ShadowMC.network.sendToServer(PacketRequestTEUpdate(this))
		}
	}

	protected open fun checkRedstone(): Boolean {
		val powered = world.isBlockPowered(pos)
		if (advanced) {
			val res = when (mode) {
				RedstoneMode.LOW -> !powered
				RedstoneMode.HIGH -> powered
				RedstoneMode.PULSE -> !prevPowered && powered
				RedstoneMode.NEVER -> false
				else -> true
			}
			prevPowered = powered
			return res
		} else {
			return !powered
		}
	}

	fun getHopperFacing(): EnumFacing {
		return world.getBlockState(pos).getValue(BlockHopperBase.FACING)
	}

	fun filterAccepts(it: T): Boolean {
		if (!advanced) return true
		return when (filterMode) {
			FilterMode.WHITELIST -> filter.accepts(it)
			FilterMode.BLACKLIST -> !filter.accepts(it)
		}
	}

	override fun writeToNBT(tag: NBTTagCompound): NBTTagCompound {
		tag.setBoolean("inverted", inverted)
		tag.setBoolean("advanced", advanced)
		tag.setString("mode", mode.name)
		tag.setString("filterMode", filterMode.name)
		filter.writeToNBT(tag)
		return super.writeToNBT(tag)
	}

	override fun readFromNBT(tag: NBTTagCompound) {
		inverted = tag.getBoolean("inverted")
		advanced = tag.getBoolean("advanced")
		if (tag.hasKey("mode")) mode = RedstoneMode.valueOf(tag.getString("mode"))
		if (tag.hasKey("filterMode")) filterMode = FilterMode.valueOf(tag.getString("filterMode"))
		filter.readFromNBT(tag)
		super.readFromNBT(tag)
	}

}