package net.shadowfacts.extrahoppers.gui

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler

/**
 * @author shadowfacts
 */
object GUIHandler: IGuiHandler {

	val FLUID_HOPPER = 0

	override fun getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
		return when (ID) {
			else -> null
		}
	}

	override fun getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Container? {
		val pos = BlockPos(x, y, z)
		return when (ID) {
			else -> null
		}
	}

}