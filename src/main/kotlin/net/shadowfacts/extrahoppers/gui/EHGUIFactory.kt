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

	@Deprecated("")
	override fun mainConfigGuiClass() = EHConfigGUI::class.java

	override fun runtimeGuiCategories() = null

	override fun getHandlerFor(element: IModGuiFactory.RuntimeOptionCategoryElement) = null

}
