package net.shadowfacts.extrahoppers.gui

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.client.IModGuiFactory
import net.shadowfacts.extrahoppers.gui.EHConfigGUI

/**
 * @author shadowfacts
 */
class EHGUIFactory: IModGuiFactory {

	override fun initialize(minecraftInstance: Minecraft) {

	}

	override fun mainConfigGuiClass(): Class<out GuiScreen> {
		return EHConfigGUI::class.java
	}

	override fun runtimeGuiCategories(): Set<IModGuiFactory.RuntimeOptionCategoryElement>? {
		return null
	}

	override fun getHandlerFor(element: IModGuiFactory.RuntimeOptionCategoryElement): IModGuiFactory.RuntimeOptionGuiHandler? {
		return null
	}

}
