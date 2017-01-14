package net.shadowfacts.extrahoppers.gui

import net.minecraft.client.gui.GuiScreen
import net.shadowfacts.extrahoppers.EHConfig
import net.shadowfacts.extrahoppers.MOD_ID
import net.shadowfacts.shadowmc.config.GUIConfig

/**
 * @author shadowfacts
 */
class EHConfigGUI(parent: GuiScreen): GUIConfig(parent, MOD_ID, EHConfig.config)
