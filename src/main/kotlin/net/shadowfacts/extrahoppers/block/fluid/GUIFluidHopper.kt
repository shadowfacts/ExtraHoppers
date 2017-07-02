package net.shadowfacts.extrahoppers.block.fluid

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Container
import net.minecraft.util.ResourceLocation
import net.shadowfacts.extrahoppers.ExtraHoppers
import net.shadowfacts.extrahoppers.MOD_ID
import net.shadowfacts.extrahoppers.gui.element.UIFilterButton
import net.shadowfacts.extrahoppers.gui.element.UIFluidIndicator
import net.shadowfacts.extrahoppers.network.PacketSetHopperFilterMode
import net.shadowfacts.extrahoppers.network.PacketSetHopperRedstoneMode
import net.shadowfacts.extrahoppers.util.filter.FilterMode
import net.shadowfacts.shadowmc.ui.dsl.container

/**
 * @author shadowfacts
 */
object GUIFluidHopper {

	private val BG = ResourceLocation(MOD_ID, "textures/gui/fluid_hopper.png")
	private val FILTER_BG = ResourceLocation(MOD_ID, "textures/gui/filter.png")

	fun create(tile: TileEntityFluidHopper, container: Container): GuiContainer {
		return container(container) {
			fixed {
				id = "root"
				width = 176 + 83 * 2
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

					add(UIFluidIndicator(tile.tank, "fluidIndicator"))

					if (tile.advanced) {
						add(UIFilterButton({
							// TODO
						}, "filter"))

						buttonRedstoneMode {
							id = "mode"
							mode = tile.mode
							callback = {
								tile.mode = it
								ExtraHoppers.network.sendToServer(PacketSetHopperRedstoneMode(tile))
							}
						}
					}
				}

				if (tile.advanced) {
					fixed {
						id = "filter-container"
						width = 83
						height = 86

						image {
							id = "filter-bg"
							width = 83
							height = 86
							texture = FILTER_BG
						}

						fixed {
							id = "filter-bottom"
							width = 83
							height = 34

							buttonEnum(FilterMode::class.java) {
								id = "filter-mode"
								value = tile.filterMode
								localizer = FilterMode::localize
								clickHandler = {
									tile.filterMode = it.value
									ExtraHoppers.network.sendToServer(PacketSetHopperFilterMode(tile))
								}
							}
						}
					}
				}
			}

			style("$MOD_ID:fluid_hopper")
		}
	}

}