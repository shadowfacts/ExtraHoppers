package net.shadowfacts.extrahoppers

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.shadowfacts.extrahoppers.block.ModBlocks
import net.shadowfacts.extrahoppers.block.advanced.TileEntityAdvancedHopper
import net.shadowfacts.extrahoppers.block.fluid.TESRFluidHopper
import net.shadowfacts.extrahoppers.block.fluid.TileEntityFluidHopper
import net.shadowfacts.extrahoppers.block.inverted.TileEntityInvertedHopper
import net.shadowfacts.extrahoppers.block.wooden.TileEntityWoodenHopper
import net.shadowfacts.extrahoppers.block.wooden_fluid.TileEntityWoodenFluidHopper
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

		NetworkRegistry.INSTANCE.registerGuiHandler(ExtraHoppers, GUIHandler)
	}

	@Mod.EventHandler
	@SideOnly(Side.CLIENT)
	fun preInitClient(event: FMLPreInitializationEvent) {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidHopper::class.java, TESRFluidHopper)
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWoodenFluidHopper::class.java, TESRFluidHopper)
	}

	@Mod.EventBusSubscriber
	object RegistrationHandler {

		@JvmStatic
		@SubscribeEvent
		fun registerBlocks(event: RegistryEvent.Register<Block>) {
			event.registry.registerAll(
					blocks.invertedHopper,
					blocks.fluidHopper,
					blocks.invertedFluidHopper,
					blocks.advancedFluidHopper,
					blocks.invertedAdvancedFluidHopper,
					blocks.woodenHopper,
					blocks.invertedWoodenHopper,
					blocks.woodenFluidHopper,
					blocks.invertedWoodenFluidHopper,
					blocks.advancedHopper,
					blocks.invertedAdvancedHopper
			)

			GameRegistry.registerTileEntity(TileEntityInvertedHopper::class.java, blocks.invertedHopper.registryName.toString())
			GameRegistry.registerTileEntity(TileEntityFluidHopper::class.java, blocks.fluidHopper.registryName.toString())
			GameRegistry.registerTileEntity(TileEntityWoodenHopper::class.java, blocks.woodenHopper.registryName.toString())
			GameRegistry.registerTileEntity(TileEntityWoodenFluidHopper::class.java, blocks.woodenFluidHopper.registryName.toString())
			GameRegistry.registerTileEntity(TileEntityAdvancedHopper::class.java, blocks.advancedHopper.registryName.toString())
		}

		@JvmStatic
		@SubscribeEvent
		fun registerItems(event: RegistryEvent.Register<Item>) {
			event.registry.registerAll(
					blocks.invertedHopper.createItemBlock(),
					blocks.fluidHopper.createItemBlock(),
					blocks.invertedFluidHopper.createItemBlock(),
					blocks.advancedFluidHopper.createItemBlock(),
					blocks.invertedAdvancedFluidHopper.createItemBlock(),
					blocks.woodenHopper.createItemBlock(),
					blocks.invertedWoodenHopper.createItemBlock(),
					blocks.woodenFluidHopper.createItemBlock(),
					blocks.invertedWoodenFluidHopper.createItemBlock(),
					blocks.advancedHopper.createItemBlock(),
					blocks.invertedAdvancedHopper.createItemBlock()
			)
		}

		@JvmStatic
		@SubscribeEvent
		fun registerModels(event: ModelRegistryEvent) {
			blocks.invertedHopper.initItemModel()
			blocks.fluidHopper.initItemModel()
			blocks.invertedFluidHopper.initItemModel()
			blocks.advancedFluidHopper.initItemModel()
			blocks.invertedAdvancedFluidHopper.initItemModel()
			blocks.woodenHopper.initItemModel()
			blocks.invertedWoodenHopper.initItemModel()
			blocks.woodenFluidHopper.initItemModel()
			blocks.invertedWoodenFluidHopper.initItemModel()
			blocks.advancedHopper.initItemModel()
			blocks.invertedAdvancedHopper.initItemModel()
		}

	}

}