package net.shadowfacts.extrahoppers.block.wooden

import net.minecraft.entity.item.EntityItem
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.ISidedInventory
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraft.util.math.AxisAlignedBB
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

	var cooldown = COOLDOWN

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
						return true
					}
				}
			} else if (tile is IInventory) {
				for (i in 0.until(tile.sizeInventory)) {
					val remainder = tile.insert(inventory.extractItem(0, 1, true), i)
					if (remainder.isEmpty) {
						inventory.extractItem(0, 1, false)
						return true
					}
				}
			} else if (tile != null && tile.hasCapability(ITEM_HANDLER_CAPABILITY, facing.opposite)) {
				val handler = tile.getCapability(ITEM_HANDLER_CAPABILITY, facing.opposite)!!
				for (i in 0.until(handler.slots)) {
					val remainder = handler.insertItem(i, inventory.extractItem(0, 1, true), false)
					if (remainder.isEmpty) {
						inventory.extractItem(0, 1, false)
						return true
					}
				}
			}
		}
		return false
	}

	private fun pull(): Boolean {
		val yOffset = if (inverted) -1 else 0
		val items = world.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB(pos.x.toDouble(), pos.y + 0.5 + yOffset, pos.z.toDouble(), pos.x + 1.0, pos.y + 1.5 + yOffset, pos.z + 1.0))
		for (item in items) {
			val result = inventory.insertItem(0, item.entityItem, true)
			if (result.count != item.entityItem.count) {
				inventory.insertItem(0, item.entityItem, false)
				if (result.isEmpty) {
					item.setDead()
				} else {
					item.setEntityItemStack(result)
				}
				return true
			}
		}

		val tile = world.getTileEntity(if (inverted) pos.down() else pos.up())
		val side = if (inverted) EnumFacing.UP else EnumFacing.DOWN

		if (tile is ISidedInventory) {
			val slots = tile.getSlotsForFace(side)
			for (i in slots) {
				val current = tile[i]
				if (!current.isEmpty) {
					val copy = current.copy()
					copy.count = 1
					val remainder = inventory.insertItem(0, copy, false)
					if (remainder.isEmpty) {
						current.shrink(1)
						return true
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
						return true
					}
				}
			}
		} else if (tile != null && tile.hasCapability(ITEM_HANDLER_CAPABILITY, side)) {
			val handler = tile.getCapability(ITEM_HANDLER_CAPABILITY, side)!!
			for (i in 0.until(handler.slots)) {
				val remainder = inventory.insertItem(0, handler.extractItem(i, 1, true), false)
				if (remainder.isEmpty) {
					handler.extractItem(i, 1, false)
					return true
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
			return facing == (if (inverted) EnumFacing.DOWN else EnumFacing.UP) || facing == getHopperFacing()
		}
		return super.hasCapability(capability, facing)
	}

	override fun <T: Any?> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
		if (capability == ITEM_HANDLER_CAPABILITY) {
			if (facing == (if (inverted) EnumFacing.DOWN else EnumFacing.UP) || facing == getHopperFacing()) {
				return inventory as T
			}
		}
		return super.getCapability(capability, facing)
	}

}