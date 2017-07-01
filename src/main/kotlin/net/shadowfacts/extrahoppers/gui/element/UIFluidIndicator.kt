package net.shadowfacts.extrahoppers.gui.element

import net.minecraftforge.fluids.IFluidTank
import net.shadowfacts.shadowmc.ui.UIDimensions
import net.shadowfacts.shadowmc.ui.element.UIFluidIndicator

/**
 * @author shadowfacts
 */
class UIFluidIndicator(tank: IFluidTank, id: String, vararg classes: String): UIFluidIndicator(tank, id, *classes) {

	override fun getPreferredDimensions() = UIDimensions(20, 73)

}