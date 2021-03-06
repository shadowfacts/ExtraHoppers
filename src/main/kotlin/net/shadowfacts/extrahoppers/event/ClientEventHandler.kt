package net.shadowfacts.extrahoppers.event

import net.minecraft.client.resources.I18n
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.shadowfacts.extrahoppers.EHConfig
import net.shadowfacts.extrahoppers.MOD_ID
import net.shadowfacts.extrahoppers.gui.GUIHandler

/**
 * @author shadowfacts
 */
@Mod.EventBusSubscriber(Side.CLIENT, modid = MOD_ID)
object ClientEventHandler {

	@JvmStatic
	@SubscribeEvent
	fun itemTooltip(event: ItemTooltipEvent) {
		if (GUIHandler.woodenFluidHopperOpen) {
			val stack = event.itemStack
			if (stack.hasCapability(FLUID_HANDLER_ITEM_CAPABILITY, null)) {
				val handler = stack.getCapability(FLUID_HANDLER_ITEM_CAPABILITY, null)!!
				val props = handler.tankProperties
				val fluid = props[0].contents
				if (fluid != null) {
					val color = if (fluid.fluid.temperature <= EHConfig.wfhMaxTemperature) TextFormatting.GREEN else TextFormatting.RED
					event.toolTip.add(I18n.format("extrahoppers.wooden_fluid_hopper.temperature", color, fluid.fluid.temperature))
				}
			}
		}
	}

}