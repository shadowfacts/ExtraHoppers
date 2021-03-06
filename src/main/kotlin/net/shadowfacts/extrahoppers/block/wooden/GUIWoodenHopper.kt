package net.shadowfacts.extrahoppers.block.wooden

import net.minecraft.inventory.Container
import net.minecraft.util.ResourceLocation
import net.shadowfacts.extrahoppers.MOD_ID
import net.shadowfacts.shadowmc.ui.element.button.UIImage
import net.shadowfacts.shadowmc.ui.element.view.UIFixedView
import net.shadowfacts.shadowmc.ui.mcwrapper.UIContainerWrapper

/**
 * @author shadowfacts
 */
class GUIWoodenHopper(container: Container): UIContainerWrapper(container) {

	private val BG = ResourceLocation(MOD_ID, "textures/gui/wooden_hopper.png")

	init {
		add(UIFixedView(176, 133, "root").apply {
			add(UIImage(BG, 176, 133, "bg"))
		})
		layout()

		xSize = 176
		ySize = 133
	}

}