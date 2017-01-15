package net.shadowfacts.extrahoppers.gui

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler
import net.shadowfacts.extrahoppers.block.fluid.GUIFluidHopper
import net.shadowfacts.extrahoppers.block.fluid.TileEntityFluidHopper
import net.shadowfacts.extrahoppers.block.wooden.ContainerWoodenHopper
import net.shadowfacts.extrahoppers.block.wooden.GUIWoodenHopper
import net.shadowfacts.extrahoppers.block.wooden.TileEntityWoodenHopper
import net.shadowfacts.shadowmc.inventory.ContainerPlayerInv

/**
 * @author shadowfacts
 */
object GUIHandler: IGuiHandler {

	val FLUID_HOPPER = 0
	val WOODEN_HOPPER = 1

	override fun getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
		val pos = BlockPos(x, y, z)
		return when (ID) {
			FLUID_HOPPER -> GUIFluidHopper.create(world.getTileEntity(pos) as TileEntityFluidHopper, getServerGuiElement(ID, player, world, x, y, z)!!)
			WOODEN_HOPPER -> GUIWoodenHopper(getServerGuiElement(ID, player, world, x, y, z)!!)
			else -> null
		}
	}

	override fun getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Container? {
		val pos = BlockPos(x, y, z)
		return when (ID) {
			FLUID_HOPPER -> ContainerPlayerInv(pos, player.inventory)
			WOODEN_HOPPER -> ContainerWoodenHopper(world.getTileEntity(pos) as TileEntityWoodenHopper, player.inventory, pos)
			else -> null
		}
	}

}