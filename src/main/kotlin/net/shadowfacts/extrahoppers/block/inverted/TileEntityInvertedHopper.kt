package net.shadowfacts.extrahoppers.block.inverted

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
import net.shadowfacts.forgelin.extensions.isEmpty

/**
 * @author shadowfacts
 */
class TileEntityInvertedHopper: TileEntityHopperBase(true), ITickable {

	companion object {
		val COOLDOWN = 8
	}

	val inventory = ItemStackHandler(5)

	var cooldown = COOLDOWN

	override fun update() {
		if (!world.isRemote) {
			if (isPowered()) return

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
		if (!inventory.isEmpty) {
			val facing = getHopperFacing()
			val tile = world.getTileEntity(pos.offset(facing))

			if (tile is ISidedInventory) {
				val slots = tile.getSlotsForFace(facing.opposite)
				for (i in slots) {
					for (slot in 0.until(inventory.slots)) {
						val remainder = tile.insert(inventory.extractItem(slot, 1, true), i)
						if (remainder.isEmpty) {
							inventory.extractItem(slot, 1, false)
							return true
						}
					}
				}
			} else if (tile is IInventory) {
				for (i in 0.until(tile.sizeInventory)) {
					for (slot in 0.until(inventory.slots)) {
						val remainder = tile.insert(inventory.extractItem(slot, 1, true), i)
						if (remainder.isEmpty) {
							inventory.extractItem(slot, 1, false)
							return true
						}
					}
				}
			} else if (tile != null && tile.hasCapability(ITEM_HANDLER_CAPABILITY, facing.opposite)) {
				val handler = tile.getCapability(ITEM_HANDLER_CAPABILITY, facing.opposite)!!
				for (i in 0.until(handler.slots)) {
					for (slot in 0.until(inventory.slots)) {
						val remainder = handler.insertItem(i, inventory.extractItem(slot, 1, true), false)
						if (remainder.isEmpty) {
							inventory.extractItem(slot, 1, false)
							return true
						}
					}
				}
			}
		}
		return false
	}

	private fun pull(): Boolean {
		val items = world.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB(pos.x.toDouble(), pos.y - 0.5, pos.z.toDouble(), pos.x + 1.0, pos.y + 0.5, pos.z + 1.0))
		for (item in items) {
			for (slot in 0.until(inventory.slots)) {
				val result = inventory.insertItem(slot, item.entityItem, true)
				if (result.count != item.entityItem.count) {
					inventory.insertItem(slot, item.entityItem, false)
					if (result.isEmpty) {
						item.setDead()
					} else {
						item.setEntityItemStack(result)
					}
					return true
				}
			}
		}

		val tile = world.getTileEntity(pos.down())

		if (tile is ISidedInventory) {
			val slots = tile.getSlotsForFace(EnumFacing.UP)
			for (i in slots) {
				for (slot in 0.until(inventory.slots)) {
					val current = tile[i]
					if (!current.isEmpty) {
						val copy = current.copy()
						copy.count = 1
						val remainder = inventory.insertItem(slot, copy, false)
						if (remainder.isEmpty) {
							current.shrink(1)
							return true
						}
					}
				}
			}
		} else if (tile is IInventory) {
			for (i in 0.until(tile.sizeInventory)) {
				for (slot in 0.until(inventory.slots)) {
					val current = tile[i]
					if (!current.isEmpty) {
						val copy = current.copy()
						copy.count = 1
						val remainder = inventory.insertItem(slot, copy, false)
						if (remainder.isEmpty) {
							current.shrink(1)
							return true
						}
					}
				}
			}
		} else if (tile != null && tile.hasCapability(ITEM_HANDLER_CAPABILITY, EnumFacing.UP)) {
			val handler = tile.getCapability(ITEM_HANDLER_CAPABILITY, EnumFacing.UP)!!
			for (i in 0.until(handler.slots)) {
				for (slot in 0.until(inventory.slots)) {
					val remainder = inventory.insertItem(slot, handler.extractItem(i, 1, true), false)
					if (remainder.isEmpty) {
						handler.extractItem(i, 1, false)
						return true
					}
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
			return facing == EnumFacing.DOWN || facing == getHopperFacing()
		}
		return super.hasCapability(capability, facing)
	}

	override fun <T: Any?> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
		if (capability == ITEM_HANDLER_CAPABILITY) {
			if (facing == EnumFacing.DOWN || facing == getHopperFacing()) {
				return inventory as T
			}
		}
		return super.getCapability(capability, facing)
	}

}