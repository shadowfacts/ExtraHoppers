package net.shadowfacts.extrahoppers

import net.minecraft.item.Item
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.shadowfacts.extrahoppers.block.ModBlocks
import net.shadowfacts.extrahoppers.block.fluid.TileEntityFluidHopper
import net.shadowfacts.extrahoppers.block.fluid.TESRFluidHopper
import net.shadowfacts.extrahoppers.block.wooden_fluid.TileEntityWoodenFluidHopper
import net.shadowfacts.extrahoppers.event.ClientEventHandler
import net.shadowfacts.extrahoppers.gui.GUIHandler

/**
 * @author shadowfacts
 */
@Mod(modid = MOD_ID, name = NAME, version = VERSION, dependencies = "required-after:shadowmc;", modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter", guiFactory = "net.shadowfacts.extrahoppers.gui.EHGUIFactory")
object ExtraHoppers {

//	Content
	val blocks = ModBlocks

	@Mod.EventHandler
	fun preInit(event: FMLPreInitializationEvent) {
		EHConfig.init(event.modConfigurationDirectory)
		EHConfig.save()

		blocks.init()

		ModRecipes.init()

		NetworkRegistry.INSTANCE.registerGuiHandler(ExtraHoppers, GUIHandler)
	}

	@Mod.EventHandler
	@SideOnly(Side.CLIENT)
	fun preInitClient(event: FMLPreInitializationEvent) {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidHopper::class.java, TESRFluidHopper)
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWoodenFluidHopper::class.java, TESRFluidHopper)

		MinecraftForge.EVENT_BUS.register(ClientEventHandler)
	}

	@Mod.EventHandler
	fun missingMappings(event: FMLMissingMappingsEvent) {
		event.get().forEach {
			if (it.name == "funnels:funnel") {
				if (it.type == GameRegistry.Type.BLOCK) {
					it.remap(blocks.fluidHopper)
				} else {
					it.remap(Item.getItemFromBlock(blocks.fluidHopper))
				}
			}
		}
	}

}