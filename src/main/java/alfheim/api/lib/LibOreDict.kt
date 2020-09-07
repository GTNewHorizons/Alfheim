package alfheim.api.lib

import vazkii.botania.common.lib.LibOreDict

object LibOreDict {
	
	const val DRAGON_ORE = "oreDragonstone"
	const val ELEMENTIUM_ORE = "oreElementium"
	const val ELVEN_QUARTZ_ORE = "oreQuartzElven"
	const val GOLD_ORE = "oreGold"
	const val IFFESAL_ORE = "oreIffesal"
	const val LAPIS_ORE = "oreLapis"
	const val ELVORIUM_INGOT = "ingotElvorium"
	const val MAUFTRIUM_INGOT = "ingotMauftrium"
	const val MUSPELHEIM_POWER_INGOT = "ingotMuspelheimPower"
	const val NIFLHEIM_POWER_INGOT = "ingotNiflheimPower"
	const val ELVORIUM_NUGGET = "nuggetElvorium"
	const val MAUFTRIUM_NUGGET = "nuggetMauftrium"
	const val MUSPELHEIM_ESSENCE = "essenceMuspelheim"
	const val NIFLHEIM_ESSENCE = "essenceNiflheim"
	const val IFFESAL_DUST = "dustIffesal"
	val ARUNE = arrayOf("runePrimalA", "runeMuspelheimA", "runeNiflheimA")
	const val INFUSED_DREAM_TWIG = "twig${LibOreDict.DREAM_WOOD}Insufed"
	const val DREAM_WOOD_LOG = "log${LibOreDict.DREAM_WOOD}"
	
	// Iridescence
	const val TWIG_THUNDERWOOD = "twigThunderwood"
	const val TWIG_NETHERWOOD = "twigNetherwood"
	const val SPLINTERS_THUNDERWOOD = "splinterThunderwood"
	const val SPLINTERS_NETHERWOOD = "splinterNetherwood"
	const val COAL_NETHERWOOD = "coalFlame"
	const val HOLY_PENDANT = "holyPendant"
	val COLORS = arrayOf("White", "Orange", "Magenta", "LightBlue", "Yellow", "Lime", "Pink", "Gray", "LightGray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black", "Rainbow", "Aurora")
	const val IRIS_WOOD = "irisWood"
	const val IRIS_LEAVES = "irisLeaves"
	const val IRIS_DIRT = "irisDirt"
	val WOOD = Array(COLORS.size) { i -> "$IRIS_WOOD${COLORS[i]}" }
	val LEAVES = Array(COLORS.size) { i -> "$IRIS_LEAVES${COLORS[i]}" }
	val DIRT = Array(COLORS.size) { i -> "$IRIS_DIRT${COLORS[i]}" }
	val DYES = Array(COLORS.size - 1) { i -> "dye${COLORS[i]}" }
	const val FLORAL_POWDER = "dyeFloralPowder"
	const val PETAL_ANY = "petalMystic"
	const val MUSHROOM = "mushroomShimmer"
	const val RAINBOW_PETAL = "petalRainbow"
	const val RAINBOW_FLOWER = "mysticFlowerRainbow"
	const val RAINBOW_DOUBLE_FLOWER = "${RAINBOW_FLOWER}Double"
	const val RAINBOW_QUARTZ = "quartzRainbow"
	const val RAINBOW_QUARTZ_BLOCK = "blockQuartzRainbow"
	
	val ALT_TYPES = arrayOf("Dry", "Golden", "Vivid", "Scorched", "Infused", "Mutated", "Wisdom", "Dreamwood")
	
	val EMERALD = "gemEmerald"
	val GLOWSTONE_DUST = "dustGlowstone"
	val REDSTONE_DUST = "dustRedstone"
}