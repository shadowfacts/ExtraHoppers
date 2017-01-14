package net.shadowfacts.extrahoppers.block.wooden

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Container
import net.minecraft.util.ResourceLocation
import net.shadowfacts.extrahoppers.MOD_ID
import net.shadowfacts.shadowmc.ui.dsl.*
import net.shadowfacts.shadowmc.ui.element.button.UIImage
import net.shadowfacts.shadowmc.ui.element.view.UIFixedView
import net.shadowfacts.shadowmc.ui.mcwrapper.UIContainerWrapper
import net.shadowfacts.shadowmc.ui.style.UIAttribute
import net.shadowfacts.shadowmc.ui.style.UIHorizontalLayoutMode
import net.shadowfacts.shadowmc.ui.style.UIVerticalLayoutMode

/**
 * @author shadowfacts
 */
class GUIWoodenHopper(container: Container): UIContainerWrapper(container) {

	private val BG = ResourceLocation(MOD_ID, "textures/gui/wooden_hopper.png")

	init {
		val fixed = UIFixedView(176, 133, "root")
		val bg = UIImage(BG, 176, 133, "bg")
		fixed.add(bg)
		add(fixed)
		layout()

		xSize = 176
		ySize = 133
	}

//	private fun container(): GuiContainer

//	fun create(container: Container): GuiContainer {
//		return container(container) {
//			fixed {
//				id = "root"
//				width = 176
//				height = 133
//
//				image {
//					id = "bg"
//					width = 176
//					height = 133
//					texture = BG
//				}
//			}
//		}
//	}

}