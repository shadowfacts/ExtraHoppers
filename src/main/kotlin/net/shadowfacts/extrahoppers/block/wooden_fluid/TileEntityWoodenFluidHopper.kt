package net.shadowfacts.extrahoppers.block.wooden_fluid

import net.minecraftforge.fluids.FluidStack
import net.shadowfacts.extrahoppers.EHConfig
import net.shadowfacts.extrahoppers.block.fluid.TileEntityFluidHopper

/**
 * @author shadowfacts
 */
class TileEntityWoodenFluidHopper(inverted: Boolean): TileEntityFluidHopper(inverted) {

	constructor(): this(false)

	override val fluidValiator: (FluidStack) -> Boolean = { it.fluid.temperature <= EHConfig.wfhMaxTemperature }

}