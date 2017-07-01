package net.shadowfacts.extrahoppers.block.wooden_fluid

import net.minecraftforge.fluids.FluidStack
import net.shadowfacts.extrahoppers.EHConfig
import net.shadowfacts.extrahoppers.block.fluid.TileEntityFluidHopper
import net.shadowfacts.extrahoppers.util.filter.Filter
import net.shadowfacts.extrahoppers.util.filter.NoOpFilter

/**
 * @author shadowfacts
 */
class TileEntityWoodenFluidHopper(inverted: Boolean): TileEntityFluidHopper(inverted, false) {

	override var filter: Filter<FluidStack> = NoOpFilter

	override val fluidValiator: (FluidStack) -> Boolean = { it.fluid.temperature <= EHConfig.wfhMaxTemperature }

	constructor(): this(false)

}