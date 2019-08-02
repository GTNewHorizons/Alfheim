package alfheim.common.item

import alfheim.api.lib.LibOreDict.COAL_NETHERWOOD
import alfheim.api.lib.LibOreDict.DYES
import alfheim.api.lib.LibOreDict.FLORAL_POWDER
import alfheim.api.lib.LibOreDict.HOLY_PENDANT
import alfheim.api.lib.LibOreDict.PETAL
import alfheim.api.lib.LibOreDict.RAINBOW_PETAL
import alfheim.api.lib.LibOreDict.SPLINTERS_NETHERWOOD
import alfheim.api.lib.LibOreDict.SPLINTERS_THUNDERWOOD
import alfheim.api.lib.LibOreDict.TWIG_NETHERWOOD
import alfheim.api.lib.LibOreDict.TWIG_THUNDERWOOD
import alfheim.common.item.block.*
import alfheim.common.item.creator.*
import alfheim.common.item.equipment.bauble.*
import alfheim.common.item.rod.*
import net.minecraft.item.*
import net.minecraftforge.oredict.OreDictionary
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.item.ModItems

object ShadowFoxItems {
	
	val irisSeeds: Item
	val colorfulSkyDirtRod: Item
	val rainbowRod: Item
	val lightningRod: Item
	val interdictionRod: Item
	val emblem: Item
	val aesirEmblem: Item
	val coatOfArms: Item
	val invisibleFlameLens: Item
	val colorOverride: Item
	val attributionBauble: Item
	val wiltedLotus: Item
	val resource: Item
	val toolbelt: Item
	val flameRod: Item
	val wireAxe: Item
	val star: Item
	val star2: Item
	val trisDagger: Item
	val fireGrenade: Item
	val splashPotion: Item
	
	init {
		irisSeeds = ItemColorSeeds()
		colorfulSkyDirtRod = ItemIridescentRod()
		rainbowRod = ItemRainbowLightRod()
		lightningRod = ItemLightningRod()
		interdictionRod = ItemInterdictionRod()
		emblem = ItemPriestEmblem()
		aesirEmblem = ItemAesirEmblem()
		coatOfArms = ItemCoatOfArms()
		invisibleFlameLens = ItemLensFlashInvisible()
		colorOverride = ItemColorOverride()
		attributionBauble = ItemAttributionBauble()
		wiltedLotus = ItemWiltedLotus()
		resource = ItemResource()
		toolbelt = ItemToolbelt()
		flameRod = ItemFlameRod()
		wireAxe = ItemWireAxe()
		star = ItemStarPlacer()
		star2 = ItemStarPlacer2()
		trisDagger = ItemTrisDagger()
		splashPotion = ItemSplashPotion()
		fireGrenade = ItemFireGrenade()
		initOreDict()
	}
	
	fun initOreDict() {
		
		OreDictionary.registerOre(TWIG_THUNDERWOOD, ItemStack(resource, 1, 0))
		OreDictionary.registerOre(SPLINTERS_THUNDERWOOD, ItemStack(resource, 1, 1))
		OreDictionary.registerOre(TWIG_NETHERWOOD, ItemStack(resource, 1, 2))
		OreDictionary.registerOre(SPLINTERS_NETHERWOOD, ItemStack(resource, 1, 3))
		OreDictionary.registerOre(COAL_NETHERWOOD, ItemStack(resource, 1, 4))
		OreDictionary.registerOre(DYES[16], ItemStack(resource, 1, 6))
		OreDictionary.registerOre(FLORAL_POWDER, ItemStack(resource, 1, 6))
		OreDictionary.registerOre(RAINBOW_PETAL, ItemStack(resource, 1, 7))
		OreDictionary.registerOre(PETAL, ItemStack(resource, 1, 7))
		
		OreDictionary.registerOre(HOLY_PENDANT, ItemStack(attributionBauble, 1, OreDictionary.WILDCARD_VALUE))
		
		OreDictionary.registerOre(DYES[16], ItemStack(ModBlocks.bifrostPerm))
		OreDictionary.registerOre(FLORAL_POWDER, ItemStack(ModItems.dye, 1, OreDictionary.WILDCARD_VALUE))
		OreDictionary.registerOre(PETAL, ItemStack(ModItems.petal, 1, OreDictionary.WILDCARD_VALUE))
	}
}
