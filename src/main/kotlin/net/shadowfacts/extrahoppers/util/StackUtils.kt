package net.shadowfacts.extrahoppers.util

import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
import net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY
import net.minecraftforge.fluids.capability.IFluidHandler

/**
 * @author shadowfacts
 */
fun ItemStack.hasFluidHandler(): Boolean {
	return hasCapability(FLUID_HANDLER_CAPABILITY, null) || hasCapability(FLUID_HANDLER_ITEM_CAPABILITY, null)
}

fun ItemStack.getFluidHandler(): IFluidHandler {
	return getCapability(FLUID_HANDLER_CAPABILITY, null) ?: getCapability(FLUID_HANDLER_ITEM_CAPABILITY, null) ?: throw RuntimeException("Expected fluid handler or fluid handler item capability on $this")
}