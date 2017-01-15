package net.shadowfacts.extrahoppers

import net.minecraftforge.common.config.Configuration
import net.minecraftforge.common.config.Property
import java.io.File
import kotlin.reflect.KProperty

/**
 * @author shadowfacts
 */
object EHConfig {

	internal lateinit var config: Configuration
		private set

//	Config values
//	Fluid Hopper
	var fhSize: Int by ConfigInt("fluidHopper", "size", 1000, 1000, 64000, "The size (in millibuckets) of the Fluid Hopper")

	var fhPickupWorldFluids: Boolean by ConfigBool("fluidHopper", "pickupWorldFluids", true, "If the Fluid Hopper should pickup fluids placed in the world directly above it.")

	var fhPlaceFluidsInWorld: Boolean by ConfigBool("fluidHopper", "placeFluidsInWorld", true, "If the Fluid Hopper should place fluids in the world directly in front of it.")

	var wfhMaxTemperature: Int by ConfigInt("woodenFluidHopper", "maxTemperature", 400, 0, Integer.MAX_VALUE, "The maximum temperature (in Kelvin) of the fluid that can be help by the Wooden Fluid Hopper.")

	fun init(configDir: File) {
		config = Configuration(File(configDir, "shadowfacts/ExtraHoppers.cfg"))

		val legacy = File(configDir, "shadowfacts/Funnels.cfg")
		if (legacy.exists()) {
			migrateLegacy(Configuration(legacy))
		}
	}

	private fun migrateLegacy(legacy: Configuration) {
		fhSize = legacy.get("general", "size", 1000, "").int
		fhPickupWorldFluids = legacy.get("general", "pickupWorldFluids", true, "").boolean
		fhPlaceFluidsInWorld = legacy.get("general", "placeFluidsInWorld", true, "").boolean
	}

	fun save() {
		if (config.hasChanged()) {
			config.save()
		}
	}

	private class ConfigInt(val category: String, val name: String, val default: Int, val minValue: Int, val maxValue: Int, val comment: String) {
		private val prop: Property
			get() = config.get(category, name, default, comment, minValue, maxValue)

		operator fun getValue(instance: Any, property: KProperty<*>): Int {
			return prop.int
		}

		operator fun setValue(instance: Any, property: KProperty<*>, value: Int) {
			prop.set(value)
		}
	}

	private class ConfigBool(val category: String, val name: String, val default: Boolean, val comment: String) {
		private val prop: Property
			get() = config.get(category, name, default, comment)

		operator fun getValue(instance: Any, property: KProperty<*>): Boolean {
			return prop.boolean
		}

		operator fun setValue(instance: Any, property: KProperty<*>, value: Boolean) {
			prop.set(value)
		}
	}

}