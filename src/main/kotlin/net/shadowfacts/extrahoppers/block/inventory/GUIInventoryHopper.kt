package net.shadowfacts.extrahoppers.block.inventory

import net.minecraft.inventory.Container
import net.minecraft.inventory.Slot
import net.minecraft.util.ResourceLocation
import net.shadowfacts.extrahoppers.ExtraHoppers
import net.shadowfacts.extrahoppers.MOD_ID
import net.shadowfacts.extrahoppers.block.advanced.TileEntityAdvancedHopper
import net.shadowfacts.extrahoppers.gui.element.UIFilterButton
import net.shadowfacts.extrahoppers.network.PacketSetHopperFilterMode
import net.shadowfacts.extrahoppers.network.PacketSetHopperRedstoneMode
import net.shadowfacts.extrahoppers.util.filter.FilterMode
import net.shadowfacts.extrahoppers.util.filter.ItemFilter
import net.shadowfacts.shadowmc.ui.element.UIRect
import net.shadowfacts.shadowmc.ui.element.button.UIButtonEnum
import net.shadowfacts.shadowmc.ui.element.button.UIButtonRedstoneMode
import net.shadowfacts.shadowmc.ui.element.button.UIImage
import net.shadowfacts.shadowmc.ui.element.view.UIFixedView
import net.shadowfacts.shadowmc.ui.element.view.UIStackView
import net.shadowfacts.shadowmc.ui.mcwrapper.UIContainerWrapper
import net.shadowfacts.shadowmc.ui.style.UIAttribute
import net.shadowfacts.shadowmc.ui.style.UIHorizontalLayoutMode
import net.shadowfacts.shadowmc.ui.style.UIVerticalLayoutMode
import net.shadowfacts.shadowmc.ui.style.stylesheet.StylesheetParser
import java.util.function.Consumer
import java.util.function.Function

/**
 * @author shadowfacts
 */
class GUIInventoryHopper(container: Container, tile: TileEntityInventoryHopper): UIContainerWrapper(container) {

	private val BG = ResourceLocation("minecraft", "textures/gui/container/hopper.png")
	private val FILTER_BG = ResourceLocation(MOD_ID, "textures/gui/filter.png")

	init {
		add(UIFixedView(176 + 83 * 2, 133, "root").apply {
			add(UIImage(BG, 176, 133, "bg"))

			if (tile.advanced && tile is TileEntityAdvancedHopper) {
				add(UIFixedView(176, 60, "top").apply {
					add(UIFilterButton({
						// TODO
					}, "filter"))

					add(UIButtonRedstoneMode(tile.mode, Consumer {
						tile.mode = it
						ExtraHoppers.network.sendToServer(PacketSetHopperRedstoneMode(tile))
					}, "mode"))
				})

				add(UIFixedView(83, 86, "filter-container").apply {
					add(UIImage(FILTER_BG, 83, 86, "filter-bg"))

					add(UIFixedView(83, 34, "filter-bottom").apply {
						add(UIButtonEnum<FilterMode>(tile.filterMode, Function(FilterMode::localize), Consumer {
							tile.filterMode = it.value
							ExtraHoppers.network.sendToServer(PacketSetHopperFilterMode(tile))
						}, "filter-mode"))
					})
				})
			}
		})

		val style = StylesheetParser.load("$MOD_ID:inventory_hopper")
		val sheet = StylesheetParser.parse(style)
		children.forEach(sheet::style)

		layout()

		xSize = 176
		ySize = 133
	}

}