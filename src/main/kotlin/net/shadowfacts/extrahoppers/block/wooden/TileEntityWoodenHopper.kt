package net.shadowfacts.extrahoppers.block.wooden

import net.minecraft.inventory.IInventory
import net.minecraft.inventory.ISidedInventory
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
import net.minecraftforge.items.ItemStackHandler
import net.shadowfacts.extrahoppers.block.base.TileEntityHopperBase
import net.shadowfacts.extrahoppers.util.insert
import net.shadowfacts.forgelin.extensions.get

/**
 * @author shadowfacts
 */
class TileEntityWoodenHopper: TileEntityHopperBase(), ITickable {

	companion object {
		val COOLDOWN = 24
	}

	val inventory = ItemStackHandler(1)

	var cooldown: Int = COOLDOWN

	override fun update() {
		if (!world.isRemote) {
//			TODO: redstone
			cooldown--
			if (cooldown <= 0) {
				val pulled = pull()
				val pushed = push()

				if (pulled || pushed) {
					cooldown = COOLDOWN
					markDirty()
				}
			}
		}
	}

	private fun push(): Boolean {
		if (!inventory[0].isEmpty) {
			val facing = getHopperFacing()
			val tile = world.getTileEntity(pos.offset(facing))

			if (tile is ISidedInventory) {
				val slots = tile.getSlotsForFace(facing.opposite)
				for (i in slots) {
					val remainder = tile.insert(inventory.extractItem(0, 1, true), i)
					if (remainder.isEmpty) {
						inventory.extractItem(0, 1, false)
						break
					}
				}
				return true
			} else if (tile is IInventory) {
				for (i in 0.until(tile.sizeInventory)) {
					val remainder = tile.insert(inventory.extractItem(0, 1, true), i)
					if (remainder.isEmpty) {
						inventory.extractItem(0, 1, false)
						break
					}
				}
				return true
			} else if (tile != null && tile.hasCapability(ITEM_HANDLER_CAPABILITY, facing.opposite)) {
				val handler = tile.getCapability(ITEM_HANDLER_CAPABILITY, facing.opposite)!!
				for (i in 0.until(handler.slots)) {
					val remainder = handler.insertItem(i, inventory.extractItem(0, 1, true), false)
					if (remainder.isEmpty) {
						inventory.extractItem(0, 1, false)
						break
					}
				}
				return true
			}
		}
		return false
	}

	private fun pull(): Boolean {
		val tile = world.getTileEntity(pos.up())

		if (tile is ISidedInventory) {
			val slots = tile.getSlotsForFace(EnumFacing.DOWN)
			for (i in slots) {
				val current = tile[i]
				if (!current.isEmpty) {
					val copy = current.copy()
					copy.count = 1
					val remainder = inventory.insertItem(0, copy, false)
					if (remainder.isEmpty) {
						current.shrink(1)
						break
					}
				}
			}
		} else if (tile is IInventory) {
			for (i in 0.until(tile.sizeInventory)) {
				val current = tile[i]
				if (!current.isEmpty) {
					val copy = current.copy()
					copy.count = 1
					val remainder = inventory.insertItem(0, copy, false)
					if (remainder.isEmpty) {
						current.shrink(1)
						break
					}
				}
			}
		} else if (tile != null && tile.hasCapability(ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN)) {
			val handler = tile.getCapability(ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN)!!
			for (i in 0.until(handler.slots)) {
				val remainder = inventory.insertItem(0, handler.extractItem(i, 1, true), false)
				if (remainder.isEmpty) {
					handler.extractItem(i, 1, false)
					break
				}
			}
		}

		return false
	}

	override fun writeToNBT(tag: NBTTagCompound): NBTTagCompound {
		tag.setTag("inventory", inventory.serializeNBT())
		tag.setInteger("cooldown", cooldown)
		return super.writeToNBT(tag)
	}

	override fun readFromNBT(tag: NBTTagCompound) {
		inventory.deserializeNBT(tag.getCompoundTag("inventory"))
		cooldown = tag.getInteger("cooldown")
		super.readFromNBT(tag)
	}

	override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
		if (capability == ITEM_HANDLER_CAPABILITY) {
			return facing == EnumFacing.UP || facing == getHopperFacing()
		}
		return super.hasCapability(capability, facing)
	}

	override fun <T: Any?> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
		if (capability == ITEM_HANDLER_CAPABILITY) {
			if (facing == EnumFacing.UP || facing == getHopperFacing()) {
				return inventory as T
			}
		}
		return super.getCapability(capability, facing)
	}

}