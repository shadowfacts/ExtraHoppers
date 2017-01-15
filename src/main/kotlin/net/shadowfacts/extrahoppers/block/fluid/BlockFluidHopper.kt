package net.shadowfacts.extrahoppers.block.fluid

import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import net.minecraftforge.fluids.FluidUtil
import net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
import net.shadowfacts.extrahoppers.ExtraHoppers
import net.shadowfacts.extrahoppers.block.base.BlockHopperBase
import net.shadowfacts.extrahoppers.gui.GUIHandler

/**
 * @author shadowfacts
 */
open class BlockFluidHopper(inverted: Boolean, name: String = "fluid_hopper", material: Material = Material.IRON): BlockHopperBase<TileEntityFluidHopper>(inverted, name, material = material) {

	init {
		setHardness(3.5f)
		setResistance(8f)
		soundType = SoundType.METAL
	}

	@Deprecated("")
	override fun hasComparatorInputOverride(state: IBlockState): Boolean {
		return true
	}

	@Deprecated("")
	override fun getComparatorInputOverride(blockState: IBlockState, world: World, pos: BlockPos): Int {
		val tank = getTileEntity(world, pos).tank
		if (tank.fluidAmount == 0) {
			return 0
		} else {
			return MathHelper.floor((tank.fluidAmount / tank.capacity) * 14.0) + 1
		}
	}

	override fun onBlockActivated(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer, hand: EnumHand, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		if (player.isSneaking) {
			player.openGui(ExtraHoppers, GUIHandler.FLUID_HOPPER, world, pos.x, pos.y, pos.z)
		} else {
			val te = getTileEntity(world, pos)
			val stack = player.getHeldItem(hand)
			val result = FluidUtil.interactWithFluidHandler(stack, te.getCapability(FLUID_HANDLER_CAPABILITY, EnumFacing.NORTH), player)
			if (result.isSuccess) {
				player.setHeldItem(hand, result.getResult())
				te.save()
			}
		}
		return true
	}

	override fun addInformation(stack: ItemStack, player: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
		tooltip.add(I18n.format("tile.extrahoppers:fluid_hopper.tooltip"))
	}

	override fun createTileEntity(world: World, state: IBlockState): TileEntityFluidHopper {
		return TileEntityFluidHopper(inverted)
	}

}
