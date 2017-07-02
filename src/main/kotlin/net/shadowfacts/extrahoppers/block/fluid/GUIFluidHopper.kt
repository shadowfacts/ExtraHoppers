package net.shadowfacts.extrahoppers.block.fluid

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Container
import net.minecraft.util.ResourceLocation
import net.shadowfacts.extrahoppers.MOD_ID
import net.shadowfacts.extrahoppers.gui.element.UIFilterButton
import net.shadowfacts.extrahoppers.gui.element.UIFluidIndicator
import net.shadowfacts.shadowmc.ui.dsl.container

/**
 * @author shadowfacts
 */
object GUIFluidHopper {

	private val BG = ResourceLocation(MOD_ID, "textures/gui/fluid_hopper.png")

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

					if (hopper.advanced) {
						buttonRedstoneMode {
							id = "mode"
							mode = hopper.mode
							callback = {
								hopper.mode = it
								hopper.sync()
							}
						}

						add(UIFilterButton({
							// TODO
						}, "filter"))
					}
				}
			}

			style("$MOD_ID:fluid_hopper")
		}
	}

}