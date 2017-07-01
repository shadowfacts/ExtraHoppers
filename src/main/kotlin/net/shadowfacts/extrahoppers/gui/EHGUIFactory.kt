package net.shadowfacts.extrahoppers.gui

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.client.IModGuiFactory

/**
 * @author shadowfacts
 */
class EHGUIFactory: IModGuiFactory {

	override fun initialize(minecraftInstance: Minecraft) {}

	override fun hasConfigGui() = true

	override fun createConfigGui(parentScreen: GuiScreen) = EHConfigGUI(parentScreen)

	override fun runtimeGuiCategories() = null

}
