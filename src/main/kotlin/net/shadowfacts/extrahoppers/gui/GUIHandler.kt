package net.shadowfacts.extrahoppers.gui

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler
import net.shadowfacts.extrahoppers.block.fluid.ContainerFluidHopper
import net.shadowfacts.extrahoppers.block.inventory.ContainerInventoryHopper
import net.shadowfacts.extrahoppers.block.fluid.GUIFluidHopper
import net.shadowfacts.extrahoppers.block.fluid.TileEntityFluidHopper
import net.shadowfacts.extrahoppers.block.inventory.GUIInventoryHopper
import net.shadowfacts.extrahoppers.block.inventory.TileEntityInventoryHopper
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
	val ADVANCED_HOPPER = 4

	var woodenFluidHopperOpen = false

	override fun getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
		val pos = BlockPos(x, y, z)
		val tile = world.getTileEntity(pos)
		return when (ID) {
			INVERTED_HOPPER, ADVANCED_HOPPER -> GUIInventoryHopper(getServerGuiElement(ID, player, world, x, y, z)!!, tile as TileEntityInventoryHopper)
			FLUID_HOPPER -> GUIFluidHopper.create(tile as TileEntityFluidHopper, getServerGuiElement(ID, player, world, x, y, z)!!)
			WOODEN_FLUID_HOPPER -> GUIWoodenFluidHopper.create(tile as TileEntityFluidHopper, getServerGuiElement(ID, player, world, x, y, z)!!)
			WOODEN_HOPPER -> GUIWoodenHopper(getServerGuiElement(ID, player, world, x, y, z)!!)
			else -> null
		}
	}

	override fun getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Container? {
		val pos = BlockPos(x, y, z)
		val tile = world.getTileEntity(pos)
		return when (ID) {
			INVERTED_HOPPER, ADVANCED_HOPPER -> ContainerInventoryHopper(tile as TileEntityInventoryHopper, player.inventory, pos)
			FLUID_HOPPER, WOODEN_FLUID_HOPPER -> ContainerFluidHopper(tile as TileEntityFluidHopper, player.inventory, pos)
			WOODEN_HOPPER -> ContainerWoodenHopper(tile as TileEntityWoodenHopper, player.inventory, pos)
			else -> null
		}
	}

}