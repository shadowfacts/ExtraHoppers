package net.shadowfacts.extrahoppers.block.wooden_fluid

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Container
import net.minecraft.util.ResourceLocation
import net.shadowfacts.extrahoppers.MOD_ID
import net.shadowfacts.extrahoppers.block.fluid.TileEntityFluidHopper
import net.shadowfacts.extrahoppers.gui.GUIHandler
import net.shadowfacts.extrahoppers.gui.element.UIFluidIndicator
import net.shadowfacts.shadowmc.ui.dsl.container

/**
 * @author shadowfacts
 */
object GUIWoodenFluidHopper {

	private val BG = ResourceLocation("shadowmc", "textures/gui/blank.png")

	fun create(hopper: TileEntityFluidHopper, container: Container): GuiContainer {
		return container(container) {
			fixed {
				id = "root"
				width = 176
				height = 166

				image {
					id = "bg"
					width = 176
					height = 166
					texture = BG
				}

				fixed {
					id = "top"
					width = 176
					height = 166 / 2

					add(UIFluidIndicator(hopper.tank, "fluidIndicator"))
				}
			}

			style("$MOD_ID:fluid_hopper")
			updateHandler {
				GUIHandler.woodenFluidHopperOpen = true
			}
			closeHandler {
				GUIHandler.woodenFluidHopperOpen = false
			}
		}
	}

}