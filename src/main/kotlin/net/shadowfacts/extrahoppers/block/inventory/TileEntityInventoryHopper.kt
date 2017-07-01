package net.shadowfacts.extrahoppers.block.inventory

import net.minecraft.entity.item.EntityItem
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.ISidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraft.util.math.AxisAlignedBB
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
import net.minecraftforge.items.ItemStackHandler
import net.shadowfacts.extrahoppers.block.base.TileEntityHopperBase
import net.shadowfacts.extrahoppers.util.filter.Filter
import net.shadowfacts.extrahoppers.util.filter.ItemFilter
import net.shadowfacts.extrahoppers.util.insert
import net.shadowfacts.forgelin.extensions.get
import net.shadowfacts.forgelin.extensions.isEmpty

/**
 * @author shadowfacts
 */
abstract class TileEntityInventoryHopper(inverted: Boolean, advanced: Boolean, size: Int): TileEntityHopperBase<ItemStack>(inverted, advanced), ITickable {

	companion object {
		val COOLDOWN = 8
	}

	val inventory = object: ItemStackHandler(size) {
		override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
			if (!filterAccepts(stack)) return stack
			return super.insertItem(slot, stack, simulate)
		}
	}

	var cooldown = COOLDOWN

	val box by lazy {
		if (inverted) {
			AxisAlignedBB(pos.x - 0.5, pos.y - 1.5, pos.z - 0.5, pos.x + 0.5, pos.y.toDouble(), pos.z + 0.5)
		} else {
			AxisAlignedBB(pos.x - 0.5, pos.y.toDouble(), pos.z - 0.5, pos.x + 0.5, pos.y + 1.5, pos.z + 0.5)
		}
	}

	override fun writeToNBT(tag: NBTTagCompound): NBTTagCompound {
		tag.setTag("inventory", inventory.serializeNBT())
		return super.writeToNBT(tag)
	}

	override fun readFromNBT(tag: NBTTagCompound) {
		inventory.deserializeNBT(tag.getCompoundTag("inventory"))
		super.readFromNBT(tag)
	}

	override fun update() {
		if (!world.isRemote) {
			cooldown--

			if (!checkRedstone()) return

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
						if (inventory[slot].isEmpty) continue
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
						if (inventory[slot].isEmpty) continue
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
						if (inventory[slot].isEmpty) continue
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
		val items = world.getEntitiesWithinAABB(EntityItem::class.java, box)
		for (item in items) {
			for (slot in 0.until(inventory.slots)) {
				val result = inventory.insertItem(slot, item.item, true)
				if (result.count != item.item.count) {
					inventory.insertItem(slot, item.item, false)
					if (result.isEmpty) {
						item.setDead()
					} else {
						item.item = result
					}
					return true
				}
			}
		}

//		val facing = getHopperFacing()
		val side = if (inverted) EnumFacing.DOWN else EnumFacing.UP
		val tile = world.getTileEntity(pos.offset(side))

		if (tile is ISidedInventory) {
			val slots = tile.getSlotsForFace(side.opposite)
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
		} else if (tile != null && tile.hasCapability(ITEM_HANDLER_CAPABILITY, side.opposite)) {
			val handler = tile.getCapability(ITEM_HANDLER_CAPABILITY, side.opposite)!!
			for (i in 0.until(handler.slots)) {
				for (slot in 0.until(inventory.slots)) {
					val remainder = inventory.insertItem(slot, handler.extractItem(i, 1, true), false)
					if (remainder.isEmpty()) {
						handler.extractItem(i, 1, false)
						return true
					}
				}
			}
		}

		return false
	}

	override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
		if (capability == ITEM_HANDLER_CAPABILITY) {
			val side = if (inverted) EnumFacing.DOWN else EnumFacing.UP
			return facing == side || facing == getHopperFacing()
		}
		return super.hasCapability(capability, facing)
	}

	override fun <T: Any?> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
		if (capability == ITEM_HANDLER_CAPABILITY) {
			val side = if (inverted) EnumFacing.DOWN else EnumFacing.UP
			if (facing == side || facing == getHopperFacing()) {
				return inventory as T
			}
		}
		return super.getCapability(capability, facing)
	}

}