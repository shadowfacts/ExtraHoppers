package net.shadowfacts.extrahoppers.util.filter

import net.minecraft.client.resources.I18n

/**
 * @author shadowfacts
 */
enum class FilterMode {

	WHITELIST,
	BLACKLIST;

	fun localize(): String {
		return I18n.format("extrahoppers.filtermode.${name.toLowerCase()}")
	}

}