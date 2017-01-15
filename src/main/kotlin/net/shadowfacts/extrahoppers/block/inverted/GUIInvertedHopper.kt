package net.shadowfacts.extrahoppers.block.inverted

import net.minecraft.inventory.Container
import net.minecraft.util.ResourceLocation
import net.shadowfacts.shadowmc.ui.element.button.UIImage
import net.shadowfacts.shadowmc.ui.element.view.UIFixedView
import net.shadowfacts.shadowmc.ui.mcwrapper.UIContainerWrapper

/**
 * @author shadowfacts
 */
class GUIInvertedHopper(container: Container): UIContainerWrapper(container) {

	private val BG = ResourceLocation("minecraft", "textures/gui/container/hopper.png")

	init {
		add(UIFixedView(176, 133, "root").apply {
			add(UIImage(BG, 176, 133, "bg"))
		})
		layout()

		xSize = 176
		ySize = 133
	}

}