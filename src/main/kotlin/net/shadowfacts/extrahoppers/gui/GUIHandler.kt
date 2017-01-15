package net.shadowfacts.extrahoppers.gui

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler
import net.shadowfacts.extrahoppers.block.base.ContainerInvertedHopper
import net.shadowfacts.extrahoppers.block.fluid.GUIFluidHopper
import net.shadowfacts.extrahoppers.block.fluid.TileEntityFluidHopper
import net.shadowfacts.extrahoppers.block.inverted.GUIInvertedHopper
import net.shadowfacts.extrahoppers.block.inverted.TileEntityInvertedHopper
import net.shadowfacts.extrahoppers.block.wooden.ContainerWoodenHopper
import net.shadowfacts.extrahoppers.block.wooden.GUIWoodenHopper
import net.shadowfacts.extrahoppers.block.wooden.TileEntityWoodenHopper
import net.shadowfacts.extrahoppers.block.wooden_fluid.GUIWoodenFluidHopper
import net.shadowfacts.shadowmc.inventory.ContainerPlayerInv

/**
 * @author shadowfacts
 */
object GUIHandler: IGuiHandler {

	val INVERTED_HOPPER = 0
	val FLUID_HOPPER = 1
	val WOODEN_HOPPER = 2
	val WOODEN_FLUID_HOPPER = 3

	var woodenFluidHopperOpen = false

	override fun getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
		val pos = BlockPos(x, y, z)
		return when (ID) {
			INVERTED_HOPPER -> GUIInvertedHopper(getServerGuiElement(ID, player, world, x, y, z)!!)
			FLUID_HOPPER -> GUIFluidHopper.create(world.getTileEntity(pos) as TileEntityFluidHopper, getServerGuiElement(ID, player, world, x, y, z)!!)
			WOODEN_HOPPER -> GUIWoodenHopper(getServerGuiElement(ID, player, world, x, y, z)!!)
			WOODEN_FLUID_HOPPER -> GUIWoodenFluidHopper.create(world.getTileEntity(pos) as TileEntityFluidHopper, getServerGuiElement(ID, player, world, x, y, z)!!)
			else -> null
		}
	}

	override fun getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Container? {
		val pos = BlockPos(x, y, z)
		return when (ID) {
			INVERTED_HOPPER -> ContainerInvertedHopper(world.getTileEntity(pos) as TileEntityInvertedHopper, player.inventory, pos)
			FLUID_HOPPER, WOODEN_FLUID_HOPPER -> ContainerPlayerInv(pos, player.inventory)
			WOODEN_HOPPER -> ContainerWoodenHopper(world.getTileEntity(pos) as TileEntityWoodenHopper, player.inventory, pos)
			else -> null
		}
	}

}