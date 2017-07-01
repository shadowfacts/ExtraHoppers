package net.shadowfacts.extrahoppers.gui.element

import net.minecraft.client.Minecraft
import net.shadowfacts.shadowmc.ui.UIDimensions
import net.shadowfacts.shadowmc.ui.element.button.UIButtonBase
import net.shadowfacts.shadowmc.ui.util.UIHelper
import net.shadowfacts.shadowmc.util.MouseButton

/**
 * @author shadowfacts
 */
//TODO: move me to ShadowMC
class UIFilterButton(val callback: (UIFilterButton) -> Unit, id: String, vararg classes: String): UIButtonBase("", id, *classes) {

	override fun getMinDimensions() = preferredDimensions

	override fun getPreferredDimensions() = UIDimensions(20, 20)

	override fun handlePress(mouseX: Int, mouseY: Int, button: MouseButton?): Boolean {
		callback(this)
		return true
	}

	override fun drawButton(mouseX: Int, mouseY: Int) {
		val sprite = Minecraft.getMinecraft().textureMapBlocks.getAtlasSprite("minecraft:items/paper")
		UIHelper.drawFluidQuad(x + 3, y + 2, 16, 16, sprite.minU, sprite.minV, sprite.maxU, sprite.maxV)
//		Minecraft.getMinecraft().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
//		UIHelper.drawTexturedRect(x + 2, y + 2, sprite.getInterpolatedU(0.0), sprite.getInterpolatedV(0.0), 16, 16)
	}

}