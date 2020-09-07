package alfheim.common.crafting.recipe

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.ASJUtilities.addOreDictRecipe
import alexsocol.asjlib.ASJUtilities.addShapelessOreDictRecipe
import alfheim.AlfheimCore
import alfheim.api.*
import alfheim.api.AlfheimAPI.addInfuserRecipe
import alfheim.api.crafting.recipe.*
import alfheim.api.lib.LibOreDict.ALT_TYPES
import alfheim.api.lib.LibOreDict.ARUNE
import alfheim.api.lib.LibOreDict.COAL_NETHERWOOD
import alfheim.api.lib.LibOreDict.DREAM_WOOD_LOG
import alfheim.api.lib.LibOreDict.DYES
import alfheim.api.lib.LibOreDict.ELVORIUM_INGOT
import alfheim.api.lib.LibOreDict.ELVORIUM_NUGGET
import alfheim.api.lib.LibOreDict.EMERALD
import alfheim.api.lib.LibOreDict.FLORAL_POWDER
import alfheim.api.lib.LibOreDict.HOLY_PENDANT
import alfheim.api.lib.LibOreDict.IFFESAL_DUST
import alfheim.api.lib.LibOreDict.INFUSED_DREAM_TWIG
import alfheim.api.lib.LibOreDict.IRIS_DIRT
import alfheim.api.lib.LibOreDict.LEAVES
import alfheim.api.lib.LibOreDict.MAUFTRIUM_INGOT
import alfheim.api.lib.LibOreDict.MAUFTRIUM_NUGGET
import alfheim.api.lib.LibOreDict.MUSHROOM
import alfheim.api.lib.LibOreDict.MUSPELHEIM_ESSENCE
import alfheim.api.lib.LibOreDict.MUSPELHEIM_POWER_INGOT
import alfheim.api.lib.LibOreDict.NIFLHEIM_ESSENCE
import alfheim.api.lib.LibOreDict.NIFLHEIM_POWER_INGOT
import alfheim.api.lib.LibOreDict.PETAL_ANY
import alfheim.api.lib.LibOreDict.RAINBOW_DOUBLE_FLOWER
import alfheim.api.lib.LibOreDict.RAINBOW_FLOWER
import alfheim.api.lib.LibOreDict.RAINBOW_PETAL
import alfheim.api.lib.LibOreDict.RAINBOW_QUARTZ
import alfheim.api.lib.LibOreDict.SPLINTERS_NETHERWOOD
import alfheim.api.lib.LibOreDict.SPLINTERS_THUNDERWOOD
import alfheim.api.lib.LibOreDict.TWIG_NETHERWOOD
import alfheim.api.lib.LibOreDict.TWIG_THUNDERWOOD
import alfheim.api.lib.LibOreDict.WOOD
import alfheim.common.block.AlfheimBlocks.alfStorage
import alfheim.common.block.AlfheimBlocks.alfheimPortal
import alfheim.common.block.AlfheimBlocks.alfheimPylon
import alfheim.common.block.AlfheimBlocks.altPlanks
import alfheim.common.block.AlfheimBlocks.altSlabs
import alfheim.common.block.AlfheimBlocks.altStairs
import alfheim.common.block.AlfheimBlocks.altWood0
import alfheim.common.block.AlfheimBlocks.altWood1
import alfheim.common.block.AlfheimBlocks.amplifier
import alfheim.common.block.AlfheimBlocks.animatedTorch
import alfheim.common.block.AlfheimBlocks.anyavil
import alfheim.common.block.AlfheimBlocks.auroraDirt
import alfheim.common.block.AlfheimBlocks.auroraPlanks
import alfheim.common.block.AlfheimBlocks.auroraSlab
import alfheim.common.block.AlfheimBlocks.auroraStairs
import alfheim.common.block.AlfheimBlocks.auroraWood
import alfheim.common.block.AlfheimBlocks.barrel
import alfheim.common.block.AlfheimBlocks.calicoPlanks
import alfheim.common.block.AlfheimBlocks.calicoSapling
import alfheim.common.block.AlfheimBlocks.calicoSlabs
import alfheim.common.block.AlfheimBlocks.calicoStairs
import alfheim.common.block.AlfheimBlocks.calicoWood
import alfheim.common.block.AlfheimBlocks.circuitPlanks
import alfheim.common.block.AlfheimBlocks.circuitSapling
import alfheim.common.block.AlfheimBlocks.circuitSlabs
import alfheim.common.block.AlfheimBlocks.circuitStairs
import alfheim.common.block.AlfheimBlocks.circuitWood
import alfheim.common.block.AlfheimBlocks.corporeaAutocrafter
import alfheim.common.block.AlfheimBlocks.corporeaInjector
import alfheim.common.block.AlfheimBlocks.elvenOre
import alfheim.common.block.AlfheimBlocks.elvenSand
import alfheim.common.block.AlfheimBlocks.enderActuator
import alfheim.common.block.AlfheimBlocks.irisDirt
import alfheim.common.block.AlfheimBlocks.irisGrass
import alfheim.common.block.AlfheimBlocks.irisLamp
import alfheim.common.block.AlfheimBlocks.irisPlanks
import alfheim.common.block.AlfheimBlocks.irisSapling
import alfheim.common.block.AlfheimBlocks.irisSlabs
import alfheim.common.block.AlfheimBlocks.irisStairs
import alfheim.common.block.AlfheimBlocks.irisWood0
import alfheim.common.block.AlfheimBlocks.irisWood1
import alfheim.common.block.AlfheimBlocks.irisWood2
import alfheim.common.block.AlfheimBlocks.irisWood3
import alfheim.common.block.AlfheimBlocks.itemDisplay
import alfheim.common.block.AlfheimBlocks.kindling
import alfheim.common.block.AlfheimBlocks.lightningPlanks
import alfheim.common.block.AlfheimBlocks.lightningSapling
import alfheim.common.block.AlfheimBlocks.lightningSlabs
import alfheim.common.block.AlfheimBlocks.lightningStairs
import alfheim.common.block.AlfheimBlocks.lightningWood
import alfheim.common.block.AlfheimBlocks.livingcobble
import alfheim.common.block.AlfheimBlocks.livingwoodFunnel
import alfheim.common.block.AlfheimBlocks.manaAccelerator
import alfheim.common.block.AlfheimBlocks.manaInfuser
import alfheim.common.block.AlfheimBlocks.netherPlanks
import alfheim.common.block.AlfheimBlocks.netherSapling
import alfheim.common.block.AlfheimBlocks.netherSlabs
import alfheim.common.block.AlfheimBlocks.netherStairs
import alfheim.common.block.AlfheimBlocks.netherWood
import alfheim.common.block.AlfheimBlocks.rainbowDirt
import alfheim.common.block.AlfheimBlocks.rainbowGrass
import alfheim.common.block.AlfheimBlocks.rainbowMushroom
import alfheim.common.block.AlfheimBlocks.rainbowPetalBlock
import alfheim.common.block.AlfheimBlocks.rainbowPlanks
import alfheim.common.block.AlfheimBlocks.rainbowSlab
import alfheim.common.block.AlfheimBlocks.rainbowStairs
import alfheim.common.block.AlfheimBlocks.rainbowWood
import alfheim.common.block.AlfheimBlocks.sealingPlanks
import alfheim.common.block.AlfheimBlocks.sealingSapling
import alfheim.common.block.AlfheimBlocks.sealingSlabs
import alfheim.common.block.AlfheimBlocks.sealingStairs
import alfheim.common.block.AlfheimBlocks.sealingWood
import alfheim.common.block.AlfheimBlocks.shimmerQuartz
import alfheim.common.block.AlfheimBlocks.shimmerQuartzSlab
import alfheim.common.block.AlfheimBlocks.shimmerQuartzStairs
import alfheim.common.block.AlfheimBlocks.tradePortal
import alfheim.common.block.AlfheimFluffBlocks.dreamwoodBarkFence
import alfheim.common.block.AlfheimFluffBlocks.dreamwoodBarkFenceGate
import alfheim.common.block.AlfheimFluffBlocks.dreamwoodFence
import alfheim.common.block.AlfheimFluffBlocks.dreamwoodFenceGate
import alfheim.common.block.AlfheimFluffBlocks.dwarfLantern
import alfheim.common.block.AlfheimFluffBlocks.dwarfPlanks
import alfheim.common.block.AlfheimFluffBlocks.dwarfTrapDoor
import alfheim.common.block.AlfheimFluffBlocks.elfQuartzWall
import alfheim.common.block.AlfheimFluffBlocks.elvenSandstone
import alfheim.common.block.AlfheimFluffBlocks.elvenSandstoneSlab
import alfheim.common.block.AlfheimFluffBlocks.elvenSandstoneSlab2
import alfheim.common.block.AlfheimFluffBlocks.elvenSandstoneStairs
import alfheim.common.block.AlfheimFluffBlocks.elvenSandstoneWalls
import alfheim.common.block.AlfheimFluffBlocks.livingMountain
import alfheim.common.block.AlfheimFluffBlocks.livingcobbleSlab
import alfheim.common.block.AlfheimFluffBlocks.livingcobbleSlab1
import alfheim.common.block.AlfheimFluffBlocks.livingcobbleSlab2
import alfheim.common.block.AlfheimFluffBlocks.livingcobbleStairs
import alfheim.common.block.AlfheimFluffBlocks.livingcobbleStairs1
import alfheim.common.block.AlfheimFluffBlocks.livingcobbleStairs2
import alfheim.common.block.AlfheimFluffBlocks.livingcobbleWall
import alfheim.common.block.AlfheimFluffBlocks.livingrockBrickWall
import alfheim.common.block.AlfheimFluffBlocks.livingrockDark
import alfheim.common.block.AlfheimFluffBlocks.livingrockDarkSlabs
import alfheim.common.block.AlfheimFluffBlocks.livingrockDarkStairs
import alfheim.common.block.AlfheimFluffBlocks.livingrockDarkWalls
import alfheim.common.block.AlfheimFluffBlocks.livingwoodBarkFence
import alfheim.common.block.AlfheimFluffBlocks.livingwoodBarkFenceGate
import alfheim.common.block.AlfheimFluffBlocks.livingwoodFence
import alfheim.common.block.AlfheimFluffBlocks.livingwoodFenceGate
import alfheim.common.block.AlfheimFluffBlocks.roofTile
import alfheim.common.block.AlfheimFluffBlocks.roofTileSlabs
import alfheim.common.block.AlfheimFluffBlocks.roofTileStairs
import alfheim.common.block.AlfheimFluffBlocks.shrineGlass
import alfheim.common.block.AlfheimFluffBlocks.shrineLight
import alfheim.common.block.AlfheimFluffBlocks.shrinePanel
import alfheim.common.block.AlfheimFluffBlocks.shrinePillar
import alfheim.common.block.AlfheimFluffBlocks.shrineRock
import alfheim.common.block.AlfheimFluffBlocks.shrineRockWhiteSlab
import alfheim.common.block.AlfheimFluffBlocks.shrineRockWhiteStairs
import alfheim.common.core.asm.hook.extender.ItemTwigWandExtender
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.integration.thaumcraft.ThaumcraftSuffusionRecipes
import alfheim.common.integration.tinkersconstruct.TinkersConstructAlfheimConfig
import alfheim.common.item.AlfheimItems.astrolabe
import alfheim.common.item.AlfheimItems.attributionBauble
import alfheim.common.item.AlfheimItems.auraRingElven
import alfheim.common.item.AlfheimItems.auraRingGod
import alfheim.common.item.AlfheimItems.balanceCloak
import alfheim.common.item.AlfheimItems.cloudPendant
import alfheim.common.item.AlfheimItems.cloudPendantSuper
import alfheim.common.item.AlfheimItems.coatOfArms
import alfheim.common.item.AlfheimItems.colorOverride
import alfheim.common.item.AlfheimItems.crescentMoonAmulet
import alfheim.common.item.AlfheimItems.dodgeRing
import alfheim.common.item.AlfheimItems.elementalBoots
import alfheim.common.item.AlfheimItems.elementalChestplate
import alfheim.common.item.AlfheimItems.elementalHelmet
import alfheim.common.item.AlfheimItems.elementalHelmetRevealing
import alfheim.common.item.AlfheimItems.elementalLeggings
import alfheim.common.item.AlfheimItems.elementiumHoe
import alfheim.common.item.AlfheimItems.elfFirePendant
import alfheim.common.item.AlfheimItems.elfIcePendant
import alfheim.common.item.AlfheimItems.elvenFood
import alfheim.common.item.AlfheimItems.elvenResource
import alfheim.common.item.AlfheimItems.elvoriumBoots
import alfheim.common.item.AlfheimItems.elvoriumChestplate
import alfheim.common.item.AlfheimItems.elvoriumHelmet
import alfheim.common.item.AlfheimItems.elvoriumHelmetRevealing
import alfheim.common.item.AlfheimItems.elvoriumLeggings
import alfheim.common.item.AlfheimItems.enlighter
import alfheim.common.item.AlfheimItems.fireGrenade
import alfheim.common.item.AlfheimItems.hyperBucket
import alfheim.common.item.AlfheimItems.invisibilityCloak
import alfheim.common.item.AlfheimItems.invisibleFlameLens
import alfheim.common.item.AlfheimItems.irisSeeds
import alfheim.common.item.AlfheimItems.livingrockPickaxe
import alfheim.common.item.AlfheimItems.lootInterceptor
import alfheim.common.item.AlfheimItems.manaGlove
import alfheim.common.item.AlfheimItems.manaMirrorImba
import alfheim.common.item.AlfheimItems.manaRingElven
import alfheim.common.item.AlfheimItems.manaRingGod
import alfheim.common.item.AlfheimItems.manaStone
import alfheim.common.item.AlfheimItems.manaStoneGreater
import alfheim.common.item.AlfheimItems.manasteelHoe
import alfheim.common.item.AlfheimItems.multibauble
import alfheim.common.item.AlfheimItems.paperBreak
import alfheim.common.item.AlfheimItems.peacePipe
import alfheim.common.item.AlfheimItems.pendantSuperIce
import alfheim.common.item.AlfheimItems.pixieAttractor
import alfheim.common.item.AlfheimItems.priestCloak
import alfheim.common.item.AlfheimItems.priestEmblem
import alfheim.common.item.AlfheimItems.rationBelt
import alfheim.common.item.AlfheimItems.realitySword
import alfheim.common.item.AlfheimItems.ringFeedFlower
import alfheim.common.item.AlfheimItems.ringSpider
import alfheim.common.item.AlfheimItems.rodClicker
import alfheim.common.item.AlfheimItems.rodColorfulSkyDirt
import alfheim.common.item.AlfheimItems.rodFlameStar
import alfheim.common.item.AlfheimItems.rodGrass
import alfheim.common.item.AlfheimItems.rodInterdiction
import alfheim.common.item.AlfheimItems.rodLightning
import alfheim.common.item.AlfheimItems.rodMuspelheim
import alfheim.common.item.AlfheimItems.rodNiflheim
import alfheim.common.item.AlfheimItems.rodPortal
import alfheim.common.item.AlfheimItems.rodPrismatic
import alfheim.common.item.AlfheimItems.soulHorn
import alfheim.common.item.AlfheimItems.spatiotemporalRing
import alfheim.common.item.AlfheimItems.splashPotion
import alfheim.common.item.AlfheimItems.starPlacer2
import alfheim.common.item.AlfheimItems.thinkingHand
import alfheim.common.item.AlfheimItems.triquetrum
import alfheim.common.item.AlfheimItems.wiltedLotus
import alfheim.common.item.block.*
import alfheim.common.item.material.ElvenFoodMetas.Lembas
import alfheim.common.item.material.ElvenFoodMetas.Nectar
import alfheim.common.item.material.ElvenResourcesMetas.DasRheingold
import alfheim.common.item.material.ElvenResourcesMetas.ElvoriumIngot
import alfheim.common.item.material.ElvenResourcesMetas.ElvoriumNugget
import alfheim.common.item.material.ElvenResourcesMetas.GrapeLeaf
import alfheim.common.item.material.ElvenResourcesMetas.IffesalDust
import alfheim.common.item.material.ElvenResourcesMetas.InfusedDreamwoodTwig
import alfheim.common.item.material.ElvenResourcesMetas.InterdimensionalGatewayCore
import alfheim.common.item.material.ElvenResourcesMetas.Jug
import alfheim.common.item.material.ElvenResourcesMetas.ManaInfusionCore
import alfheim.common.item.material.ElvenResourcesMetas.MauftriumIngot
import alfheim.common.item.material.ElvenResourcesMetas.MauftriumNugget
import alfheim.common.item.material.ElvenResourcesMetas.MuspelheimEssence
import alfheim.common.item.material.ElvenResourcesMetas.MuspelheimPowerIngot
import alfheim.common.item.material.ElvenResourcesMetas.MuspelheimRune
import alfheim.common.item.material.ElvenResourcesMetas.NetherwoodCoal
import alfheim.common.item.material.ElvenResourcesMetas.NetherwoodSplinters
import alfheim.common.item.material.ElvenResourcesMetas.NetherwoodTwig
import alfheim.common.item.material.ElvenResourcesMetas.NiflheimEssence
import alfheim.common.item.material.ElvenResourcesMetas.NiflheimPowerIngot
import alfheim.common.item.material.ElvenResourcesMetas.NiflheimRune
import alfheim.common.item.material.ElvenResourcesMetas.PrimalRune
import alfheim.common.item.material.ElvenResourcesMetas.RainbowDust
import alfheim.common.item.material.ElvenResourcesMetas.RainbowPetal
import alfheim.common.item.material.ElvenResourcesMetas.RainbowQuartz
import alfheim.common.item.material.ElvenResourcesMetas.ThunderwoodSplinters
import alfheim.common.item.material.ElvenResourcesMetas.ThunderwoodTwig
import cpw.mods.fml.common.registry.GameRegistry.*
import net.minecraft.block.Block
import net.minecraft.init.Blocks.*
import net.minecraft.init.Items.*
import net.minecraft.item.*
import net.minecraft.item.crafting.*
import net.minecraftforge.oredict.*
import net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.recipe.*
import vazkii.botania.common.Botania
import vazkii.botania.common.block.ModBlocks.*
import vazkii.botania.common.block.ModFluffBlocks.*
import vazkii.botania.common.block.tile.mana.TilePool
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.crafting.*
import vazkii.botania.common.crafting.ModCraftingRecipes.*
import vazkii.botania.common.item.ModItems.*
import vazkii.botania.common.lib.LibOreDict.*
import java.util.*
import net.minecraft.init.Items.dye as justDye
import net.minecraft.init.Items.skull as skullPlacer
import vazkii.botania.common.crafting.ModCraftingRecipes.recipeGaiaPylon as recipeGaiaPylonOld
import vazkii.botania.common.crafting.ModCraftingRecipes.recipesApothecary as recipesApothecaryVanilla
import vazkii.botania.common.item.ModItems.quartz as manaquartz

object AlfheimRecipes {
	
	lateinit var recipeElvorium: RecipeManaInfuser
	lateinit var recipeMauftrium: RecipeManaInfuser
	lateinit var recipeManaStone: RecipeManaInfuser
	lateinit var recipeManaStoneGreater: RecipeManaInfuser
	lateinit var recipeTerrasteel: RecipeManaInfuser
	
	lateinit var recipeAlfheimPortal: IRecipe
	lateinit var recipesAltPlanks: List<IRecipe>
	lateinit var recipesAltSlabs: List<IRecipe>
	lateinit var recipesAltStairs: List<IRecipe>
	lateinit var recipeAmplifier: IRecipe
	lateinit var recipeAnimatedTorch: IRecipe
	lateinit var recipeAnyavil: IRecipe
	lateinit var recipesApothecary: List<IRecipe>
	lateinit var recipeAstrolabe: IRecipe
	lateinit var recipeAttribution: IRecipe
	lateinit var recipeAuraRingElven: IRecipe
	lateinit var recipeAuraRingGod: IRecipe
	lateinit var recipeAuroraDirt: IRecipe
	lateinit var recipeAuroraPlanks: IRecipe
	lateinit var recipeAuroraPlanksFromSlabs: IRecipe
	lateinit var recipeAuroraSlabs: IRecipe
	lateinit var recipeAuroraStairs: IRecipe
	lateinit var recipeAutocrafter: IRecipe
	lateinit var recipeBalanceCloak: IRecipe
	lateinit var recipeBarrel: IRecipe
	lateinit var recipeCalicoPlanks: IRecipe
	lateinit var recipeCalicoSlabs: IRecipe
	lateinit var recipeCalicoStairs: IRecipe
	lateinit var recipeCircuitPlanks: IRecipe
	lateinit var recipeCircuitSlabs: IRecipe
	lateinit var recipeCircuitStairs: IRecipe
	lateinit var recipeCleanPylon: IRecipe
	lateinit var recipeCloakHeimdall: IRecipe
	lateinit var recipeCloakLoki: IRecipe
	lateinit var recipeCloakNjord: IRecipe
	lateinit var recipeCloakOdin: IRecipe
	lateinit var recipeCloakSif: IRecipe
	lateinit var recipeCloakThor: IRecipe
	lateinit var recipeCloudPendant: IRecipe
	lateinit var recipeCloudPendantSuper: IRecipe
	lateinit var recipesCoatOfArms: List<IRecipe>
	lateinit var recipeColorOverride: IRecipe
	lateinit var recipesColoredDirt: List<IRecipe>
	lateinit var recipesColoredPlanks: List<IRecipe>
	lateinit var recipesColoredPlanksFromSlabs: List<IRecipe>
	lateinit var recipesColoredSlabs: List<IRecipe>
	lateinit var recipesColoredStairs: List<IRecipe>
	lateinit var recipeCrescentAmulet: IRecipe
	lateinit var recipeDodgeRing: IRecipe
	lateinit var recipeElementalBoots: IRecipe
	lateinit var recipeElementalChestplate: IRecipe
	lateinit var recipeElementalHelmet: IRecipe
	lateinit var recipeElementalLeggings: IRecipe
	lateinit var recipeElementiumHoe: IRecipe
	lateinit var recipeElvenPylon: IRecipe
	lateinit var recipesElvenWand: List<IRecipe>
	lateinit var recipeElvoriumBoots: IRecipe
	lateinit var recipeElvoriumChestplate: IRecipe
	lateinit var recipeElvoriumHelmet: IRecipe
	lateinit var recipeElvoriumLeggings: IRecipe
	lateinit var recipeElvoriumPylon: IRecipe
	lateinit var recipeEnderActuator: IRecipe
	lateinit var recipeEnlighter: IRecipe
	lateinit var recipeFurnace: IRecipe
	lateinit var recipeGaiaPylon: IRecipe
	lateinit var recipeHyperBucket: IRecipe
	lateinit var recipeInfernalPlanks: IRecipe
	lateinit var recipeInfernalSlabs: IRecipe
	lateinit var recipeInfernalStairs: IRecipe
	lateinit var recipeInfernalTwig: IRecipe
	lateinit var recipeInjector: IRecipe
	lateinit var recipeInvisibilityCloak: IRecipe
	lateinit var recipeInvisibleLens: IRecipe
	lateinit var recipeInvisibleLensUndo: IRecipe
	lateinit var recipesItemDisplay: List<IRecipe>
	lateinit var recipeItemDisplayElven: IRecipe
	lateinit var recipeItemHolder: IRecipe
	lateinit var recipeJug: IRecipe
	lateinit var recipeKindling: IRecipe
	lateinit var recipeLamp: IRecipe
	lateinit var recipesLeafDyes: List<IRecipe>
	lateinit var recipeLembas: IRecipe
	lateinit var recipeLensMessenger: IRecipe
	lateinit var recipeLensPush: IRecipe
	lateinit var recipeLensSmelt: IRecipe
	lateinit var recipeLensSuperconductor: IRecipe
	lateinit var recipeLensTrack: IRecipe
	lateinit var recipeLensTripwire: IRecipe
	lateinit var recipeLivingcobble: IRecipe
	lateinit var recipeLivingrockPickaxe: IRecipe
	lateinit var recipeLivingwoodFunnel: IRecipe
	lateinit var recipeLootInterceptor: IRecipe
	lateinit var recipeManaInfuser: IRecipe
	lateinit var recipeManaInfusionCore: IRecipe
	lateinit var recipeManaMirrorImba: IRecipe
	lateinit var recipeManaRingElven: IRecipe
	lateinit var recipeManaRingGod: IRecipe
	lateinit var recipeManasteelHoe: IRecipe
	lateinit var recipeManaweaveGlove: IRecipe
	lateinit var recipeMultibauble: IRecipe
	lateinit var recipeMuspelheimPendant: IRecipe
	lateinit var recipeMuspelheimPowerIngot: IRecipe
	lateinit var recipeNiflheimPendant: IRecipe
	lateinit var recipeNiflheimPowerIngot: IRecipe
	lateinit var recipePaperBreak: IRecipe
	lateinit var recipePeacePipe: IRecipe
	lateinit var recipePendantSuperIce: IRecipe
	lateinit var recipePixieAttractor: IRecipe
	lateinit var recipePriestOfHeimdall: IRecipe
	lateinit var recipePriestOfLoki: IRecipe
	lateinit var recipePriestOfNjord: IRecipe
	lateinit var recipePriestOfOdin: IRecipe
	lateinit var recipePriestOfSif: IRecipe
	lateinit var recipePriestOfThor: IRecipe
	lateinit var recipesRainbowPetal: List<IRecipe>
	lateinit var recipeRainbowPetalBlock: IRecipe
	lateinit var recipeRainbowPetalGrinding: IRecipe
	lateinit var recipeRationBelt: IRecipe
	lateinit var recipesRedstoneRoot: List<IRecipe>
	lateinit var recipeRelicCleaner: IRecipe
	lateinit var recipeRingFeedFlower: IRecipe
	lateinit var recipeRingSpider: IRecipe
	lateinit var recipeRodClicker: IRecipe
	lateinit var recipesRodColoredSkyDirt: List<IRecipe>
	lateinit var recipeRodFlame: IRecipe
	lateinit var recipeRodGreen: IRecipe
	lateinit var recipeRodInterdiction: IRecipe
	lateinit var recipeRodLightning: IRecipe
	lateinit var recipeRodMuspelheim: IRecipe
	lateinit var recipeRodNiflheim: IRecipe
	lateinit var recipeRodPortal: IRecipe
	lateinit var recipeRodPrismatic: IRecipe
	lateinit var recipeSealingPlanks: IRecipe
	lateinit var recipeSealingSlabs: IRecipe
	lateinit var recipeSealingStairs: IRecipe
	lateinit var recipeShimmerQuartz: IRecipe
	lateinit var recipeSixTorches: IRecipe
	lateinit var recipeSoulHorn: IRecipe
	lateinit var recipesSpark: MutableList<IRecipe>
	lateinit var recipeSpatiotemporal: IRecipe
	lateinit var recipeSplashPotions: IRecipe
	lateinit var recipesStar2: List<IRecipe>
	lateinit var recipesStar: List<IRecipe>
	lateinit var recipeSuperLavaPendantNew: IRecipe
	lateinit var recipeSword: IRecipe
	lateinit var recipeThinkingHand: IRecipe
	lateinit var recipeThunderousPlanks: IRecipe
	lateinit var recipeThunderousSlabs: IRecipe
	lateinit var recipeThunderousStairs: IRecipe
	lateinit var recipeThunderousTwig: IRecipe
	lateinit var recipeTradePortal: IRecipe
	lateinit var recipeTriquetrum: IRecipe
	lateinit var recipeUberSpreader: IRecipe
	
	lateinit var recipesPastoralSeeds: List<RecipeManaInfusion>
	
	lateinit var recipesAttributionHeads: MutableList<RecipePetals>
	
	lateinit var recipeCrysanthermum: RecipePetals
	lateinit var recipeOrechidEndium: RecipePetals
	lateinit var recipePetronia: RecipePetals
	lateinit var recipeRainFlower: RecipePetals
	lateinit var recipeSnowFlower: RecipePetals
	lateinit var recipeStormFlower: RecipePetals
	lateinit var recipeWindFlower: RecipePetals
	
	lateinit var recipeInterdimensional: RecipeElvenTrade
	
	lateinit var recipeDreamwood: RecipePureDaisy
	lateinit var recipeIrisSapling: RecipePureDaisyExclusion
	lateinit var recipePlainDirt: RecipePureDaisy
	
	lateinit var recipeMuspelheimRune: RecipeRuneAltar
	lateinit var recipeNiflheimRune: RecipeRuneAltar
	lateinit var recipeRealityRune: RecipeRuneAltar
	
	lateinit var recipeLightningTree: RecipeTreeCrafting
	lateinit var recipeInfernalTree: RecipeTreeCrafting
	lateinit var recipeSealingTree: RecipeTreeCrafting
	lateinit var recipeCalicoTree: RecipeTreeCrafting
	lateinit var recipeCircuitTree: RecipeTreeCrafting
	
	init {
		registerCraftingRecipes()
		registerShapelessRecipes()
		registerSmeltingRecipes()
		registerManaInfusionRecipes()
		registerDendrology()
		registerRecipies()
		banRetrades()
	}
	
	private fun registerCraftingRecipes() {
		ASJUtilities.removeRecipe(ModCraftingRecipes.recipeGaiaPylon.recipeOutput)
		
		addOreDictRecipe(ItemStack(alfheimPortal, 1),
						 "DPD", "GSG", "DTD",
						 'D', DREAM_WOOD,
						 'G', spark,
						 'P', RUNE[8],
						 'S', rainbowRod,
						 'T', ItemStack(lens, 1, 18))
		recipeAlfheimPortal = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(alfheimPylon),
						 " P ", "EDE", " P ",
						 'P', PIXIE_DUST,
						 'E', ELEMENTIUM,
						 'D', DRAGONSTONE)
		recipeElvenPylon = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(alfheimPylon, 1, 1),
						 " E ", "EPE", "III",
						 'E', ELVORIUM_NUGGET,
						 'P', ItemStack(alfheimPylon),
						 'I', IFFESAL_DUST)
		recipeElvoriumPylon = BotaniaAPI.getLatestAddedRecipe()
		
		for (p in PETAL + RAINBOW_PETAL)
			addOreDictRecipe(ItemStack(altar, 1, 9),
							 "SPS", " C ", "CCC",
							 'S', livingcobbleSlab,
							 'P', p,
							 'C', ItemStack(livingcobble))
		recipesApothecary = BotaniaAPI.getLatestAddedRecipes(17)
		
		addOreDictRecipe(ItemStack(altar),
						 "SPS", " C ", "CCC",
						 'S', "slabCobblestone",
						 'P', RAINBOW_PETAL,
						 'C', "cobblestone")
		recipesApothecaryVanilla.add(BotaniaAPI.getLatestAddedRecipe())
		
		// TODO Ygg & Dream planks ?
		val woods = Array(4) { ItemStack(altWood0, 1, it) } + Array(2) { ItemStack(altWood1, 1, it) }
		woods.forEachIndexed { id, it -> addShapelessOreDictRecipe(ItemStack(altPlanks, 4, id), it) }
		recipesAltPlanks = BotaniaAPI.getLatestAddedRecipes(6)
		
		for (i in 0 until ALT_TYPES.size - 1)
			addRecipe(ItemStack(altSlabs, 6, i),
					  "PPP",
					  'P', ItemStack(altPlanks, 1, i))
		recipesAltSlabs = BotaniaAPI.getLatestAddedRecipes(6)
		
		for (i in 0 until ALT_TYPES.size - 1)
			addOreDictRecipe(ItemStack(altStairs[i], 4), true,
							 "P  ", "PP ", "PPP",
							 'P', ItemStack(altPlanks, 1, i))
		recipesAltStairs = BotaniaAPI.getLatestAddedRecipes(6)
		
		addRecipe(ItemStack(amplifier),
				  " N ", "NRN", " N ",
				  'N', ItemStack(noteblock),
				  'R', ItemStack(sealingPlanks))
		recipeAmplifier = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(animatedTorch),
						 "P", "T",
						 'T', redstone_torch,
						 'P', MANA_POWDER)
		recipeAnimatedTorch = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(anyavil),
						 "BGB", " P ", "EDE",
						 'P', PIXIE_DUST,
						 'E', ELEMENTIUM,
						 'D', DRAGONSTONE,
						 'B', ItemStack(storage, 1, 2),
						 'G', ItemStack(storage, 1, 4))
		recipeAnyavil = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(astrolabe),
						 " ES", "EEE", "SED",
						 'E', ELEMENTIUM,
						 'S', LIFE_ESSENCE,
						 'D', DREAM_WOOD)
		recipeAstrolabe = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(attributionBauble),
						 "S S", "Q Q", " G ",
						 'G', "ingotGold",
						 'Q', "gemQuartz",
						 'S', MANA_STRING)
		recipeAttribution = BotaniaAPI.getLatestAddedRecipe()
		
		addRecipe(ItemStack(auroraSlab, 6),
				  "PPP",
				  'P', ItemStack(auroraPlanks))
		recipeAuroraSlabs = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(auroraStairs, 4), true,
						 "P  ", "PP ", "PPP",
						 'P', ItemStack(auroraPlanks))
		recipeAuroraStairs = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(balanceCloak),
						 "WWW", "EWE", "ESE",
						 'W', ItemStack(wool, 1, 8),
						 'E', EMERALD,
						 'S', LIFE_ESSENCE)
		recipeBalanceCloak = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(barrel),
						 "PSP", "P P", "PPP",
						 'P', ItemStack(planks, 1, 5),
						 'S', ItemStack(wooden_slab, 1, 5))
		recipeBarrel = BotaniaAPI.getLatestAddedRecipe()
		
		addRecipe(ItemStack(calicoSlabs, 6),
				  "PPP",
				  'P', ItemStack(calicoPlanks))
		recipeCalicoSlabs = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(calicoStairs, 4), true,
				  "P  ", "PP ", "PPP",
				  'P', ItemStack(calicoPlanks))
		recipeCalicoStairs = BotaniaAPI.getLatestAddedRecipe()
		
		addRecipe(ItemStack(circuitSlabs, 6),
				  "PPP",
				  'P', ItemStack(circuitPlanks))
		recipeCircuitSlabs = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(circuitStairs, 4), true,
						 "P  ", "PP ", "PPP",
						 'P', ItemStack(circuitPlanks))
		recipeCircuitStairs = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(pylon, 1, 2),
						 "EEE", "EPE", "EEE",
						 'E', LIFE_ESSENCE,
						 'P', ItemStack(alfheimPylon, 1, 2))
		recipeCleanPylon = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(cloudPendant),
						 "US ", "S S", "MSA",
						 'U', RUNE[6],
						 'S', MANA_STRING,
						 'M', MANA_STEEL,
						 'A', RUNE[3])
		recipeCloudPendant = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(cloudPendantSuper),
						 "GEG", "GPG", "WSW",
						 'G', ghast_tear,
						 'E', ELEMENTIUM,
						 'P', ItemStack(cloudPendant),
						 'W', ItemStack(wool),
						 'S', LIFE_ESSENCE)
		recipeCloudPendantSuper = BotaniaAPI.getLatestAddedRecipe()
		
		LEAVES.forEachIndexed { id, it ->
			addOreDictRecipe(ItemStack(coatOfArms, 1, id),
							 "LLL", "LSL", "LLL",
							 'L', it,
							 'S', MANA_STRING)
		}
		recipesCoatOfArms = BotaniaAPI.getLatestAddedRecipes(18)
		
		addOreDictRecipe(ItemStack(colorOverride),
						 "PE ", "E E", " E ",
						 'P', ItemStack(lens, 1, 14), // Paintslinger's Lens
						 'E', ELEMENTIUM)
		recipeColorOverride = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(corporeaAutocrafter),
						 " H ", "RSI", " F ",
						 'H', corporeaInjector,
						 'R', corporeaRetainer,
						 'I', corporeaInterceptor,
						 'F', corporeaFunnel,
						 'S', ItemStack(corporeaSpark, 1, 1))
		recipeAutocrafter = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(crescentMoonAmulet),
						 "  M", "MS ", "RM ",
						 'M', MAUFTRIUM_NUGGET,
						 'R', RUNE[13],
						 'S', ItemStack(manaResource, 1, 12))
		recipeCrescentAmulet = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(dodgeRing),
						 "EM ", "M M", " MR",
						 'E', EMERALD,
						 'M', MANA_STEEL,
						 'R', RUNE[3])
		recipeDodgeRing = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elementalHelmet),
						 "RTR", "DPD", " M ",
						 'R', RUNE[0],
						 'T', INFUSED_DREAM_TWIG,
						 'D', IFFESAL_DUST,
						 'P', elementiumHelm,
						 'M', RUNE[8])
		recipeElementalHelmet = BotaniaAPI.getLatestAddedRecipe()
		
		if (Botania.thaumcraftLoaded)
			addOreDictRecipe(ItemStack(elementalHelmetRevealing),
							 "RTR", "DPD", " M ",
							 'R', RUNE[0],
							 'T', INFUSED_DREAM_TWIG,
							 'D', IFFESAL_DUST,
							 'P', elementiumHelmRevealing,
							 'M', RUNE[8])
		
		addOreDictRecipe(ItemStack(elementalChestplate),
						 "RTR", "DPD", " M ",
						 'R', RUNE[2],
						 'T', INFUSED_DREAM_TWIG,
						 'D', IFFESAL_DUST,
						 'P', elementiumChest,
						 'M', RUNE[8])
		recipeElementalChestplate = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elementalLeggings),
						 "RTR", "DPD", " M ",
						 'R', RUNE[1],
						 'T', INFUSED_DREAM_TWIG,
						 'D', IFFESAL_DUST,
						 'P', elementiumLegs,
						 'M', RUNE[8])
		recipeElementalLeggings = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elementalBoots),
						 "RTR", "DPD", " M ",
						 'R', RUNE[3],
						 'T', INFUSED_DREAM_TWIG,
						 'D', IFFESAL_DUST,
						 'P', elementiumBoots,
						 'M', RUNE[8])
		recipeElementalBoots = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elementiumHoe),
						 "EE", " T", " T",
						 'E', ELEMENTIUM,
						 'T', DREAMWOOD_TWIG)
		recipeElementiumHoe = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elfFirePendant),
						 "  N", "NP ", "RN ",
						 'N', MAUFTRIUM_NUGGET,
						 'R', ARUNE[1],
						 'P', lavaPendant)
		recipeMuspelheimPendant = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elfIcePendant),
						 "  N", "NP ", "RN ",
						 'N', MAUFTRIUM_NUGGET,
						 'R', ARUNE[2],
						 'P', icePendant)
		recipeNiflheimPendant = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elvenResource, 1, DasRheingold),
						 "SCS", "CGC", "SCS",
						 'G', "ingotGold",
						 'S', LIFE_ESSENCE,
						 'C', spellCloth)
		recipeRelicCleaner = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elvenResource, 1, ManaInfusionCore),
						 "PGP", "GDG", "PGP",
						 'D', PIXIE_DUST,
						 'G', "ingotGold",
						 'P', IFFESAL_DUST)
		recipeManaInfusionCore = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elvenResource, 1, MuspelheimPowerIngot),
						 " S ", "SIS", " S ",
						 'S', MUSPELHEIM_ESSENCE,
						 'I', ELVORIUM_INGOT)
		recipeMuspelheimPowerIngot = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elvenResource, 1, NiflheimPowerIngot),
						 " S ", "SIS", " S ",
						 'S', NIFLHEIM_ESSENCE,
						 'I', ELVORIUM_INGOT)
		recipeNiflheimPowerIngot = BotaniaAPI.getLatestAddedRecipe()
		
		for (i in 0..15)
			for (j in 0..15) {
				addOreDictRecipe(ItemTwigWandExtender.forColors(i, j, true),
								 " AS", " SB", "S  ",
								 'A', PETAL[i],
								 'B', PETAL[j],
								 'S', DREAMWOOD_TWIG)
			}
		recipesElvenWand = BotaniaAPI.getLatestAddedRecipes(256)
		
		addOreDictRecipe(ItemStack(elvoriumChestplate),
						 "TRT", "EPE", "CMC",
						 'T', INFUSED_DREAM_TWIG,
						 'R', ARUNE[0],
						 'E', ELVORIUM_INGOT,
						 'P', terrasteelChest,
						 'C', ItemStack(elvenResource, 1, ManaInfusionCore),
						 'M', MAUFTRIUM_INGOT)
		recipeElvoriumChestplate = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elvoriumLeggings),
						 "TRT", "EPE", "CMC",
						 'T', INFUSED_DREAM_TWIG,
						 'R', ARUNE[0],
						 'E', ELVORIUM_INGOT,
						 'P', terrasteelLegs,
						 'C', ItemStack(elvenResource, 1, ManaInfusionCore),
						 'M', MAUFTRIUM_INGOT)
		recipeElvoriumLeggings = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elvoriumBoots),
						 "TRT", "EPE", "CMC",
						 'T', INFUSED_DREAM_TWIG,
						 'R', ARUNE[0],
						 'E', ELVORIUM_INGOT,
						 'P', terrasteelBoots,
						 'C', ItemStack(elvenResource, 1, ManaInfusionCore),
						 'M', MAUFTRIUM_INGOT)
		recipeElvoriumBoots = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(enderActuator),
						 "PBE", "BCB", "ELP",
						 'P', MANA_PEARL,
						 'B', DYES[15],
						 'E', ender_eye,
						 'L', ItemStack(wiltedLotus, 1, 1),
						 'C', ender_chest)
		recipeEnderActuator = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(enlighter),
						 "GGG", "GSG", "EEE",
						 'G', managlassPane,
						 'S', starPlacer2,
						 'E', ELVORIUM_NUGGET)
		recipeEnlighter = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(flowerBag),
						 "WPW", "W W", " W ",
						 'P', PETAL_ANY,
						 'W', ItemStack(wool, 1, 32767))
		CraftingManager.getInstance().recipeList.remove(recipeFlowerBag)
		recipeFlowerBag = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(furnace, 1, 8),
						 "SSS", "S S", "SSS",
						 'S', ItemStack(livingcobble))
		recipeFurnace = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(hyperBucket),
						 "III", "EBE", "MMM",
						 'B', openBucket,
						 'E', ELVORIUM_INGOT,
						 'I', IFFESAL_DUST,
						 'M', MAUFTRIUM_NUGGET)
		recipeHyperBucket = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(invisibilityCloak),
						 "PWP", "GWG", "GJG",
						 'P', PRISMARINE_SHARD,
						 'W', ItemStack(wool),
						 'G', manaGlass,
						 'J', MANA_PEARL)
		recipeInvisibilityCloak = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(irisLamp),
						 " B ", "BLB", " B ",
						 'L', ItemStack(redstone_lamp),
						 'B', DYES[16])
		recipeLamp = BotaniaAPI.getLatestAddedRecipe()
		
		for (i in 0..15)
			addRecipe(ItemStack(irisPlanks, 1, i), "P", "P", 'P', ItemStack(irisSlabs[i], 1))
		addRecipe(ItemStack(rainbowPlanks), "P", "P", 'P', ItemStack(rainbowSlab))
		recipesColoredPlanksFromSlabs = BotaniaAPI.getLatestAddedRecipes(17)
		
		addRecipe(ItemStack(auroraPlanks), "P", "P", 'P', ItemStack(auroraSlab))
		recipeAuroraPlanksFromSlabs = BotaniaAPI.getLatestAddedRecipe()
		
		for (i in 0..15)
			addRecipe(ItemStack(irisSlabs[i], 6),
					  "PPP",
					  'P', ItemStack(irisPlanks, 1, i))
		addRecipe(ItemStack(rainbowSlab, 6),
				  "PPP",
				  'P', ItemStack(rainbowPlanks))
		recipesColoredSlabs = BotaniaAPI.getLatestAddedRecipes(17)
		
		for (i in 0..15)
			addOreDictRecipe(ItemStack(irisStairs[i], 4), true,
							 "P  ", "PP ", "PPP",
							 'P', ItemStack(irisPlanks, 1, i))
		addOreDictRecipe(ItemStack(rainbowStairs, 4), true,
						 "P  ", "PP ", "PPP",
						 'P', ItemStack(rainbowPlanks))
		recipesColoredStairs = BotaniaAPI.getLatestAddedRecipes(17)
		
		arrayOf(MANASTEEL_NUGGET, TERRASTEEL_NUGGET).forEachIndexed { id, it ->
			addOreDictRecipe(ItemStack(itemDisplay, 1, id),
							 "N", "W",
							 'N', it,
							 'W', ItemStack(livingwoodSlab))
		}
		recipesItemDisplay = BotaniaAPI.getLatestAddedRecipes(2)
		
		addOreDictRecipe(ItemStack(itemDisplay, 1, 2),
						 "N", "W",
						 'N', ELEMENTIUM_NUGGET,
						 'W', ItemStack(dreamwoodSlab))
		recipeItemDisplayElven = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elvenResource, 1, Jug),
						 "B B", "B B", " B ",
						 'B', brick)
		recipeJug = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(lens, 1, 22),
						 " P ", "PLP", " P ",
						 'P', paper,
						 'L', ItemStack(lens))
		recipeLensMessenger = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(lens, 1, 26),
						 "IWI", "RLR", "IWI",
						 'L', ItemStack(lens),
						 'W', RUNE[0], // water
						 'R', RUNE[13], // wrath
						 'I', IFFESAL_DUST)
		recipeLensSuperconductor = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elvenFood, 1, Lembas),
						 " LB", "NBN", "BL ",
						 'N', ItemStack(elvenFood, 1, Nectar),
						 'L', ItemStack(elvenResource, 1, GrapeLeaf),
						 'B', bread)
		recipeLembas = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(kindling),
						 " S ", "SBS", " S ",
						 'B', "powderBlaze",
						 'S', MANA_STRING)
		recipeKindling = BotaniaAPI.getLatestAddedRecipe()
		
		addRecipe(ItemStack(lightningSlabs, 6),
				  "PPP",
				  'P', ItemStack(lightningPlanks))
		recipeThunderousSlabs = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(lightningStairs, 4), true,
						 "P  ", "PP ", "PPP",
						 'P', ItemStack(lightningPlanks))
		recipeThunderousStairs = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(livingrockPickaxe),
						 "LLL", " S ", " S ",
						 'L', ItemStack(livingcobble),
						 'S', "stickWood")
		recipeLivingrockPickaxe = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(livingwoodFunnel),
						 "L L", "LCL", " L ",
						 'L', LIVING_WOOD, 'C', ItemStack(chest))
		
		recipeLivingwoodFunnel = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(lootInterceptor),
						 "IHI", "DID",
						 'I', IFFESAL_DUST,
						 'H', blackHoleTalisman,
						 'D', DREAM_WOOD)
		recipeLootInterceptor = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(manaAccelerator),
						 "MLM", "LDL",
						 'D', MANA_DIAMOND,
						 'L', LIVING_ROCK,
						 'M', MANA_PEARL)
		recipeItemHolder = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(manaInfuser),
						 "DCD", "IRI", "SSS",
						 'C', ItemStack(elvenResource, 1, ManaInfusionCore),
						 'D', DRAGONSTONE,
						 'I', ELEMENTIUM,
						 'R', rainbowRod,
						 'S', ItemStack(livingrock, 1, 4))
		recipeManaInfuser = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(manaMirrorImba),
						 "IMI", "EWE", "IMI",
						 'M', MAUFTRIUM_INGOT,
						 'E', ELVORIUM_INGOT,
						 'I', IFFESAL_DUST,
						 'W', ItemStack(lens, 1, 18))
		recipeManaMirrorImba = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(manaRingElven),
						 "IS ", "S S", " S ",
						 'S', ELVORIUM_INGOT,
						 'I', manaStone)
		recipeManaRingElven = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(manasteelHoe),
						 "SS", " T", " T",
						 'S', MANA_STEEL,
						 'T', LIVINGWOOD_TWIG)
		recipeManasteelHoe = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(manaGlove),
						 "MM ", "MPM", " M ",
						 'M', MANAWEAVE_CLOTH,
						 'P', MANA_PEARL)
		recipeManaweaveGlove = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(multibauble),
						 "QT ", "T E", " E ",
						 'E', ELEMENTIUM,
						 'T', TERRA_STEEL,
						 'Q', ItemStack(manaquartz, 1, 5))
		recipeMultibauble = BotaniaAPI.getLatestAddedRecipe()
		
		addRecipe(ItemStack(netherSlabs, 6),
				  "PPP",
				  'P', ItemStack(netherPlanks))
		recipeInfernalSlabs = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(netherStairs, 4), true,
						 "P  ", "PP ", "PPP",
						 'P', ItemStack(netherPlanks))
		recipeInfernalStairs = BotaniaAPI.getLatestAddedRecipe()
		
		addRecipe(ItemStack(elvenResource, 1, NetherwoodTwig),
				  "P", "P",
				  'P', ItemStack(netherWood))
		recipeInfernalTwig = BotaniaAPI.getLatestAddedRecipe()
		
		recipePaperBreak = ShapelessOreRecipe(ItemStack(paperBreak, 4), leather, ItemStack(wooden_sword, 1, WILDCARD_VALUE))
		
		recipePeacePipe = ShapedOreRecipe(ItemStack(peacePipe),
										  "  P", " SD", "S  ",
										  'S', stick,
										  'D', DYES[1],
										  'P', ItemStack(planks, 1, 5))
		
		if (AlfheimConfigHandler.enableMMO) addMMORecipes()
		
		ASJUtilities.removeRecipe(superLavaPendant)
		addOreDictRecipe(ItemStack(superLavaPendant),
						 "MMM", "MPM", "ISI",
						 'M', blaze_rod,
						 'P', lavaPendant,
						 'I', nether_brick,
						 'S', MUSPELHEIM_ESSENCE)
		recipeSuperLavaPendantNew = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(pendantSuperIce),
						 "MMM", "MPM", "ISI",
						 'M', MANA_STEEL,
						 'P', icePendant,
						 'I', packed_ice,
						 'S', NIFLHEIM_ESSENCE)
		recipePendantSuperIce = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(pixieAttractor),
						 "EDE", "EPE", " S ",
						 'D', DRAGONSTONE,
						 'E', ELEMENTIUM,
						 'P', PIXIE_DUST,
						 'S', RUNE[2])
		recipePixieAttractor = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(priestCloak),
						 "WGW", "TWT", "ITI",
						 'W', ItemStack(wool, 1, 15),
						 'G', ItemStack(wool, 1, 5),
						 'T', TERRASTEEL_NUGGET,
						 'I', TWIG_THUNDERWOOD)
		recipeCloakThor = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(priestCloak, 1, 1),
						 "WGW", "NLN", "TOT",
						 'W', ItemStack(wool, 1, 15),
						 'G', ItemStack(wool, 1, 4),
						 'N', "nuggetGold",
						 'L', ItemStack(livingwood, 1, 5),
						 'O', overgrowthSeed,
						 'T', LIVINGWOOD_TWIG)
		recipeCloakSif = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(priestCloak, 1, 2),
						 "WGW", "MPM", "TST",
						 'W', ItemStack(wool),
						 'G', ItemStack(wool, 1, 3),
						 'P', PRISMARINE_SHARD,
						 'M', MANASTEEL_NUGGET,
						 'S', ItemStack(potionitem, 1, 16418),
						 'T', INFUSED_DREAM_TWIG)
		recipeCloakNjord = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(priestCloak, 1, 3),
						 "WGW", "NKN", "TNT",
						 'W', ItemStack(wool, 1, 15),
						 'G', ItemStack(wool, 1, 1),
						 'N', "nuggetGold",
						 'K', kindling,
						 'T', TWIG_NETHERWOOD)
		recipeCloakLoki = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(priestCloak, 1, 4),
						 "RGB", "QPQ", "IQI",
						 'R', ItemStack(wool, 1, 14),
						 'G', ItemStack(wool, 1, 13),
						 'B', ItemStack(wool, 1, 11),
						 'P', ender_eye,
						 'Q', RAINBOW_QUARTZ,
						 'I', bifrostPerm)
		recipeCloakHeimdall = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(priestCloak, 1, 5),
						 "WWW", "MGM", "IMI",
						 'W', ItemStack(wool, 1, 14),
						 'G', ItemStack(elvenResource, 1, DasRheingold),
						 'M', MAUFTRIUM_NUGGET,
						 'I', IFFESAL_DUST)
		recipeCloakOdin = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(priestEmblem),
						 "EGE", "TAT", " W ",
						 'E', ENDER_AIR_BOTTLE,
						 'T', TERRASTEEL_NUGGET,
						 'G', LIFE_ESSENCE,
						 'W', RUNE[13], // Wrath
						 'A', HOLY_PENDANT)
		recipePriestOfThor = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(priestEmblem, 1, 1),
						 "DGD", "NAN", " P ",
						 'D', DRAGONSTONE,
						 'N', "nuggetGold",
						 'G', LIFE_ESSENCE,
						 'P', RUNE[2], // Earth
						 'A', HOLY_PENDANT)
		recipePriestOfSif = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(priestEmblem, 1, 2),
						 "RGR", "NAN", " P ",
						 'P', RUNE[15], // Pride
						 'N', MANASTEEL_NUGGET,
						 'G', LIFE_ESSENCE,
						 'R', RUNE[3], // Air
						 'A', HOLY_PENDANT)
		recipePriestOfNjord = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(priestEmblem, 1, 3),
						 "WGO", "NAN", " P ",
						 'O', RUNE[6],
						 'W', RUNE[7],
						 'N', ELEMENTIUM_NUGGET,
						 'G', LIFE_ESSENCE,
						 'P', RUNE[8], // Mana
						 'A', HOLY_PENDANT)
		recipePriestOfLoki = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(priestEmblem, 1, 4),
						 "BGB", "NAN", " P ",
						 'B', bifrostPerm,
						 'G', LIFE_ESSENCE,
						 'N', ELVORIUM_NUGGET,
						 'P', RUNE[14], // Envy
						 'A', HOLY_PENDANT)
		recipePriestOfHeimdall = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(priestEmblem, 1, 5),
						 "RGR", "NAN", " P ",
						 'R', ItemStack(elvenResource, 1, DasRheingold),
						 'G', LIFE_ESSENCE,
						 'N', MAUFTRIUM_NUGGET,
						 'P', RUNE[9], // Lust
						 'A', HOLY_PENDANT)
		recipePriestOfOdin = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(pylon, 1, 2),
						 " E ", "TPT", " E ",
						 'T', TERRASTEEL_NUGGET,
						 'E', overgrowthSeed,
						 'P', ItemStack(alfheimPylon))
		recipeGaiaPylon = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(rationBelt),
						 "GL ", "L L", "ELS",
						 'G', RUNE[10],
						 'L', leather,
						 'E', ELEMENTIUM,
						 'S', RUNE[12])
		recipeRationBelt = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(realitySword),
						 " M ", "MRM", " S ",
						 'M', MAUFTRIUM_INGOT,
						 'R', ARUNE[0],
						 'S', ItemStack(manaResource, 1, 3))
		recipeSword = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(ringFeedFlower),
						 "D  ", " R ", "  P",
						 'D', distributor,
						 'R', manaRing,
						 'P', pump)
		recipeRingFeedFlower = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(ringSpider),
						 "RMW", "M M", "WM ",
						 'R', RUNE[11], // greed
						 'M', MANA_STEEL,
						 'W', web)
		recipeRingSpider = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(rodClicker),
						 "GEG", " TE", "T G",
						 'G', DRAGONSTONE,
						 'T', INFUSED_DREAM_TWIG,
						 'E', ELEMENTIUM_NUGGET)
		recipeRodClicker = BotaniaAPI.getLatestAddedRecipe()
		
		val dirts = Array(16) { ItemStack(irisDirt, 1, it) } + ItemStack(rainbowDirt) + ItemStack(auroraDirt)
		dirts.forEachIndexed { id, it ->
			addOreDictRecipe(ItemStack(rodColorfulSkyDirt, 1, id),
							 " PD", " RP", "S  ",
							 'D', it,
							 'R', ItemStack(skyDirtRod, 1),
							 'P', PIXIE_DUST,
							 'S', DRAGONSTONE)
		}
		recipesRodColoredSkyDirt = BotaniaAPI.getLatestAddedRecipes(18)
		
		addOreDictRecipe(ItemStack(rodInterdiction),
						 " AS", " DA", "P  ",
						 'P', RUNE[15], // Pride
						 'A', RUNE[3], // Air
						 'S', ItemStack(tornadoRod),
						 'D', DREAMWOOD_TWIG)
		recipeRodInterdiction = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(rodMuspelheim),
						 " MR", " BM", "B  ",
						 'M', MAUFTRIUM_INGOT,
						 'R', ARUNE[1],
						 'B', blaze_rod)
		recipeRodMuspelheim = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(rodFlameStar, 1),
						 " EW", " SD", "S  ",
						 'E', SPLINTERS_NETHERWOOD,
						 'D', COAL_NETHERWOOD,
						 'S', TWIG_NETHERWOOD,
						 'W', RUNE[1]) // Fire
		
		recipeRodFlame = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(rodGrass),
						 "  R", " D ", "S  ",
						 'D', dirtRod,
						 'R', RUNE[4],
						 'S', grassSeeds)
		recipeRodGreen = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(rodNiflheim),
						 " MR", " BM", "B  ",
						 'M', MAUFTRIUM_INGOT,
						 'R', ARUNE[2],
						 'B', blaze_rod)
		recipeRodNiflheim = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(rodLightning, 1),
						 " EW", " SD", "S  ",
						 'E', SPLINTERS_THUNDERWOOD,
						 'D', DRAGONSTONE,
						 'S', TWIG_THUNDERWOOD,
						 'W', RUNE[13]) // Wrath
		
		recipeRodLightning = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(rodPortal),
						 "IER", " FN", "B I",
						 'E', end_stone,
						 'R', ARUNE[0],
						 'F', rainbowRod,
						 'N', netherrack,
						 'I', IFFESAL_DUST,
						 'B', blaze_rod)
		recipeRodPortal = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(rodPrismatic),
						 " GB", " DG", "D  ",
						 'G', "glowstone",
						 'B', DYES[16],
						 'D', DREAMWOOD_TWIG)
		recipeRodPrismatic = BotaniaAPI.getLatestAddedRecipe()
		
		addRecipe(ItemStack(sealingSlabs, 6),
				  "PPP",
				  'P', ItemStack(sealingPlanks))
		recipeSealingSlabs = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(sealingStairs, 4), true,
						 "P  ", "PP ", "PPP",
						 'P', ItemStack(sealingPlanks))
		recipeSealingStairs = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(soulHorn),
						 "MIM", "EIE", " E ",
						 'M', MAUFTRIUM_INGOT,
						 'E', ELVORIUM_INGOT,
						 'I', IFFESAL_DUST)
		recipeSoulHorn = BotaniaAPI.getLatestAddedRecipe()
		
		for (i in 0..15)
			addOreDictRecipe(ItemStack(spark),
							 " P ", "BNB", " P ",
							 'B', PIXIE_DUST,
							 'P', PETAL[i],
							 'N', "nuggetGold")
		recipesSpark = BotaniaAPI.getLatestAddedRecipes(16)
		recipesSpark.addAll(ModCraftingRecipes.recipesSpark)
		
		addOreDictRecipe(ItemStack(spatiotemporalRing),
						 "GES", "E E", "SE ",
						 'G', hourglass,
						 'E', ELEMENTIUM,
						 'S', LIFE_ESSENCE)
		recipeSpatiotemporal = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(spreader),
						 "WWW", "GP ", "WWW",
						 'W', LIVING_WOOD,
						 'P', RAINBOW_PETAL,
						 'G', if (Botania.gardenOfGlassLoaded) LIVING_WOOD else "ingotGold")
		recipesSpreader.add(BotaniaAPI.getLatestAddedRecipe())
		
		addOreDictRecipe(ItemStack(spreader, 1, 2),
						 "WWW", "EP ", "WWW",
						 'W', DREAM_WOOD,
						 'P', RAINBOW_PETAL,
						 'E', ELEMENTIUM)
		recipesDreamwoodSpreader.add(BotaniaAPI.getLatestAddedRecipe())
		
		for (i in 0..16) {
			val stack = ItemStarPlacer.forColor(i)
			stack.stackSize = 3
			addOreDictRecipe(stack,
							 " E ", "GDG", " G ",
							 'E', ENDER_AIR_BOTTLE,
							 'G', "dustGlowstone",
							 'D', DYES[i])
		}
		recipesStar = BotaniaAPI.getLatestAddedRecipes(17)
		
		for (i in 0..16) {
			val stack = ItemStarPlacer2.forColor(i)
			stack.stackSize = 6
			addOreDictRecipe(stack,
							 " E ", "GDG", " G ",
							 'E', ENDER_AIR_BOTTLE,
							 'G', MANA_PEARL,
							 'D', DYES[i])
		}
		recipesStar2 = BotaniaAPI.getLatestAddedRecipes(17)
		
		addOreDictRecipe(ItemStack(thinkingHand),
						 "PPP", "PSP", "PPP",
						 'P', tinyPotato,
						 'S', MANA_STRING)
		recipeThinkingHand = BotaniaAPI.getLatestAddedRecipe()
		
		addRecipe(ItemStack(elvenResource, 1, ThunderwoodTwig),
				  "P", "P",
				  'P', ItemStack(lightningWood))
		recipeThunderousTwig = BotaniaAPI.getLatestAddedRecipe()
		
		addRecipe(ItemStack(torch, 6),
				  "C", "S",
				  'C', ItemStack(elvenResource, 1, NetherwoodCoal),
				  'S', ItemStack(stick))
		recipeSixTorches = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(tradePortal),
						 "LEL", "LEL", "LEL",
						 'L', LIVING_ROCK,
						 'E', ELVORIUM_NUGGET)
		recipeTradePortal = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(triquetrum),
						 "NLN", " NL", " II",
						 'N', TERRASTEEL_NUGGET,
						 'L', LIVINGWOOD_TWIG,
						 'I', TERRA_STEEL)
		
		recipeTriquetrum = BotaniaAPI.getLatestAddedRecipe()
		
		val s = AlfheimCore.stupidMode
		
		// if no TiC || if Avaritia loaded || if molten Mauftrium is disabled
		if (!AlfheimCore.TiCLoaded || s || AlfheimConfigHandler.materialIDs[TinkersConstructAlfheimConfig.MAUFTRIUM] == -1) {
			addOreDictRecipe(ItemStack(spreader, 1, 4),
							 "MMM", "ESI", "MMM",
							 'M', if (s) ItemStack(alfStorage, 1, 1) else MAUFTRIUM_INGOT,
							 'E', if (s) ItemStack(alfStorage) else ELVORIUM_INGOT,
							 'S', ItemStack(spreader, 1, 3),
							 'I', if (s) ItemStack(elvenResource, 1, ManaInfusionCore) else IFFESAL_DUST)
			recipeUberSpreader = BotaniaAPI.getLatestAddedRecipe()
		}
		
		// #############################################################################################################
		// ################################################ DECO BLOCKS ################################################
		// #############################################################################################################
		
		addRecipe(ItemStack(elvenSandstone), "SS", "SS", 'S', elvenSand)
		
		//addRecipe(ItemStack(elvenSandstone), "S", "S", 'S', elvenSandstoneSlab)
		
		addRecipe(ItemStack(elvenSandstone, 2, 1), "S", "S", 'S', elvenSandstoneSlab)
		
		//addRecipe(ItemStack(elvenSandstone, 1, 2), "S", "S", 'S', elvenSandstoneSlab2)
		
		addRecipe(ItemStack(elvenSandstone, 4, 2), "SS", "SS", 'S', ItemStack(elvenSandstone))
		
		addRecipe(ItemStack(elvenSandstone, 4, 3), "SS", "SS", 'S', ItemStack(elvenSandstone, 1, 2))
		
		addOreDictRecipe(ItemStack(elvenSandstoneStairs[0], 4), true, "S  ", "SS ", "SSS", 'S', ItemStack(elvenSandstone))
		
		addOreDictRecipe(ItemStack(elvenSandstoneStairs[1], 4), true, "S  ", "SS ", "SSS", 'S', ItemStack(elvenSandstone, 1, 2))
		
		addOreDictRecipe(ItemStack(elvenSandstoneSlab, 6), "SSS", 'S', ItemStack(elvenSandstone))
		
		addOreDictRecipe(ItemStack(elvenSandstoneSlab2, 6), "SSS", 'S', ItemStack(elvenSandstone, 1, 2))
		
		addOreDictRecipe(ItemStack(elvenSandstoneWalls[0], 6), "SSS", "SSS", 'S', ItemStack(elvenSandstone))
		
		addOreDictRecipe(ItemStack(elvenSandstoneWalls[1], 6), "SSS", "SSS", 'S', ItemStack(elvenSandstone, 1, 2))
		
		for (i in 0..15)
			addOreDictRecipe(ItemStack(irisDirt, 8, i), "DDD", "DPD", "DDD", 'P', DYES[i], 'D', ItemStack(dirt, 1))
		
		addOreDictRecipe(ItemStack(rainbowDirt, 8), "DDD", "DPD", "DDD", 'P', DYES[16], 'D', ItemStack(dirt, 1))
		
		recipesColoredDirt = BotaniaAPI.getLatestAddedRecipes(17)
		
		addOreDictRecipe(ItemStack(auroraDirt, 8), "DDD", "DPD", "DDD", 'P', MANA_PEARL, 'D', ItemStack(dirt, 1))
		
		recipeAuroraDirt = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(livingcobbleStairs, 4), true, "L  ", "LL ", "LLL", 'L', ItemStack(livingcobble))
		
		addOreDictRecipe(ItemStack(livingcobbleStairs1, 4), true, "L  ", "LL ", "LLL", 'L', ItemStack(livingcobble, 1, 1))
		
		addOreDictRecipe(ItemStack(livingcobbleStairs2, 4), true, "L  ", "LL ", "LLL", 'L', ItemStack(livingcobble, 1, 2))
		
		addRecipe(ItemStack(livingcobbleSlab, 6), "LLL", 'L', ItemStack(livingcobble))
		
		addRecipe(ItemStack(livingcobbleSlab1, 6), "LLL", 'L', ItemStack(livingcobble, 1, 1))
		
		addRecipe(ItemStack(livingcobbleSlab2, 6), "LLL", 'L', ItemStack(livingcobble, 1, 2))
		
		addRecipe(ItemStack(livingcobble), "L", "L", 'L', ItemStack(livingcobbleSlab))
		
		addRecipe(ItemStack(livingcobble, 1, 1), "L", "L", 'L', ItemStack(livingcobbleSlab1))
		
		addRecipe(ItemStack(livingcobble, 1, 2), "L", "L", 'L', ItemStack(livingcobbleSlab2))
		
		addRecipe(ItemStack(livingcobbleWall, 6), "LLL", "LLL", 'L', ItemStack(livingcobble))
		
		addRecipe(ItemStack(livingrockBrickWall, 6), "LLL", "LLL", 'L', ItemStack(livingrock, 1, 1))
		
		addRecipe(ItemStack(livingwoodFence, 6), "LLL", "LLL", 'L', ItemStack(livingwood, 1, 1))
		
		addOreDictRecipe(ItemStack(livingwoodFenceGate, 1), "LPL", "LPL", 'L', LIVINGWOOD_TWIG, 'P', ItemStack(livingwood, 1, 1))
		
		addOreDictRecipe(ItemStack(livingwoodBarkFence, 6), "LLL", "LLL", 'L', LIVINGWOOD_TWIG)
		
		addOreDictRecipe(ItemStack(livingwoodBarkFenceGate, 1), "LPL", "LPL", 'L', LIVINGWOOD_TWIG, 'P', ItemStack(livingwood))
		
		addRecipe(ItemStack(dreamwoodFence, 6), "LLL", "LLL", 'L', ItemStack(dreamwood, 1, 1))
		
		addOreDictRecipe(ItemStack(dreamwoodFenceGate, 1), "LPL", "LPL", 'L', DREAMWOOD_TWIG, 'P', ItemStack(dreamwood, 1, 1))
		
		addOreDictRecipe(ItemStack(dreamwoodBarkFence, 6), "LLL", "LLL", 'L', DREAMWOOD_TWIG)
		
		addOreDictRecipe(ItemStack(dreamwoodBarkFenceGate, 1), "LPL", "LPL", 'L', DREAMWOOD_TWIG, 'P', ItemStack(dreamwood))
		
		addOreDictRecipe(ItemStack(dwarfPlanks, 4), " P ", "PMP", " P ", 'P', ItemStack(planks, 1, 5), 'M', MANA_POWDER)
		
		addOreDictRecipe(ItemStack(dwarfLantern, 8),
						 "LCL", "CSC", "LCL",
						 'L', ItemStack(livingrock, 1, 1),
						 'C', ItemStack(livingrock, 1, 4),
						 'S', ItemStack(shrineLight, 1, 1))
		
		for (i in (0..15) - 5 - 9 - 10 - 11 - 13) {
			addOreDictRecipe(ItemStack(shrineRock, 8, i),
							 "LLL", "LDL", "LLL",
							 'L', LIVING_ROCK,
							 'D', DYES[i])
		}
		
		addOreDictRecipe(ItemStack(shrineRock, 8, 5),
						 "LLL", "LSL", "LLL",
						 'L', LIVING_ROCK,
						 'S', sugar)
		
		addOreDictRecipe(ItemStack(shrineRock, 8, 9),
						 "LL", "LL",
						 'L', ItemStack(shrineRock, 8, 0))
		
		addOreDictRecipe(ItemStack(shrineRock, 8, 10),
						 "LLL", "LML", "LLL",
						 'L', LIVING_ROCK,
						 'M', ItemStack(mushroom))
		
		addOreDictRecipe(ItemStack(shrineRock, 8, 11),
						 "LLL", "LML", "LLL",
						 'L', LIVING_ROCK,
						 'M', ItemStack(mushroom, 1, 14))
		
		addOreDictRecipe(ItemStack(shrineRock, 8, 13),
						 "LLL", "LDL", "LLL",
						 'L', LIVING_ROCK,
						 'D', DYES[16])
		
		// ################################################################
		
		addRecipe(ItemStack(livingrockDark, 4, 1),
				  "LL", "LL",
				  'L', ItemStack(livingrockDark))
		
		addRecipe(ItemStack(livingrockDark, 4, 2),
				  "LL", "LL",
				  'L', ItemStack(livingrockDark, 1, 1))
		
		addRecipe(ItemStack(livingrockDark, 4, 3),
				  "LL", "LL",
				  'L', ItemStack(livingrockDark, 1, 2))
		
		addRecipe(ItemStack(livingrockDark),
				  "L", "L",
				  'L', ItemStack(livingrockDarkSlabs[0]))
		
		addRecipe(ItemStack(livingrockDark, 1, 1),
				  "L", "L",
				  'L', ItemStack(livingrockDarkSlabs[1]))
		
		addRecipe(ItemStack(livingrockDark, 1, 3),
				  "L", "L",
				  'L', ItemStack(livingrockDarkSlabs[2]))
		
		addRecipe(ItemStack(livingrockDarkStairs[0], 4),
				  "L  ", "LL ", "LLL",
				  'L', ItemStack(livingrockDark))
		
		addRecipe(ItemStack(livingrockDarkStairs[1], 4),
				  "L  ", "LL ", "LLL",
				  'L', ItemStack(livingrockDark, 1, 1))
		
		addRecipe(ItemStack(livingrockDarkStairs[2], 4),
				  "L  ", "LL ", "LLL",
				  'L', ItemStack(livingrockDark, 1, 3))
		
		addRecipe(ItemStack(livingrockDarkSlabs[0], 6),
				  "LLL",
				  'L', ItemStack(livingrockDark))
		
		addRecipe(ItemStack(livingrockDarkSlabs[1], 6),
				  "LLL",
				  'L', ItemStack(livingrockDark, 1, 1))
		
		addRecipe(ItemStack(livingrockDarkSlabs[2], 6),
				  "LLL",
				  'L', ItemStack(livingrockDark, 1, 3))
		
		addRecipe(ItemStack(livingrockDarkWalls[0], 6),
				  "LLL", "LLL",
				  'L', ItemStack(livingrockDark))
		
		addRecipe(ItemStack(livingrockDarkWalls[1], 6),
				  "LLL", "LLL",
				  'L', ItemStack(livingrockDark, 1, 1))
		
		// ################################################################
		
		addRecipe(ItemStack(shrineRockWhiteStairs, 4),
				  "L  ", "LL ", "LLL",
				  'L', ItemStack(shrineRock))
		
		addRecipe(ItemStack(shrineRockWhiteSlab, 6),
				  "LLL",
				  'L', ItemStack(shrineRock))
		
		for (i in 0..5) {
			addOreDictRecipe(ItemStack(shrineLight, 8, i),
							 "LLL", "LDL", "LLL",
							 'L', "glowstone",
							 'D', DYES[if (i == 0) 14 else i])
		}
		
		addRecipe(ItemStack(shrinePillar, 2), "S", "S", 'S', ItemStack(shrineRock))
		
		addOreDictRecipe(ItemStack(shrineGlass, 8, 0),
						 "GGG", "GDG", "GGG",
						 'G', elfGlass,
						 'D', DYES[0])
		
		addOreDictRecipe(ItemStack(shrineGlass, 8, 1),
						 "GGG", "GDG", "GGG",
						 'G', elfGlass,
						 'D', DYES[14])
		
		addOreDictRecipe(ItemStack(shrineGlass, 8, 2),
						 "GGG", "GDG", "GGG",
						 'G', ItemStack(shrineGlass, 8, 0),
						 'D', DYES[9])
		
		addOreDictRecipe(ItemStack(shrineGlass, 8, 3),
						 "GGG", "GDG", "GGG",
						 'G', ItemStack(shrineGlass, 8, 0),
						 'D', DYES[5])
		
		addOreDictRecipe(ItemStack(shrineGlass, 8, 4),
						 "GGG", "GDG", "GGG",
						 'G', ItemStack(shrineGlass, 8, 0),
						 'D', DYES[14])
		
		addRecipe(ItemStack(livingcobble, 4, 1),
				  "LL", "LL",
				  'L', ItemStack(livingcobble, 1, 2))
		
		addOreDictRecipe(ItemStack(livingcobble, 8, 2),
						 "LLL", "L L", "LLL",
						 'L', LIVING_ROCK)
		
		addShapelessOreDictRecipe(ItemStack(livingcobble, 1, 3), ItemStack(livingcobble), vineBall)
		
		addRecipe(ItemStack(livingMountain, 9),
				  "CRC", "RCR", "CRC",
				  'C', ItemStack(livingcobble),
				  'R', ItemStack(livingrock))
		
		addShapelessOreDictRecipe(ItemStack(roofTile), ItemStack(customBrick, 1, 3), DYES[10], DYES[7])
		addShapelessOreDictRecipe(ItemStack(roofTile, 1, 1), ItemStack(customBrick, 1, 3), DYES[13], DYES[11], DYES[7])
		addShapelessOreDictRecipe(ItemStack(roofTile, 1, 2), ItemStack(customBrick, 1, 3), DYES[13])
		
		roofTileSlabs.forEachIndexed { meta, slab ->
			addRecipe(ItemStack(slab, 6), "RRR", 'R', ItemStack(roofTile, 1, meta))
			addRecipe(ItemStack(roofTile, 1, meta), "R", "R", 'R', ItemStack(slab))
		}
		
		roofTileStairs.forEachIndexed { meta, stair ->
			addRecipe(ItemStack(stair, 4), "R  ", "RR ", "RRR", 'R', ItemStack(roofTile, 1, meta))
		}
		
		val dyes = arrayOf(4, 1, 14, 11)
		for (i in 0..3) {
			addOreDictRecipe(ItemStack(shrinePanel, 16, i),
							 "GGG", "DDD", "GGG",
							 'G', ItemStack(shrineGlass),
							 'D', DYES[dyes[i]])
		}
		
		addRecipe(ItemStack(dwarfTrapDoor),
				  "WWW", "WWW",
				  'W', ItemStack(dwarfPlanks))
		
		val quartzs = arrayOf(quartz_block, blazeQuartz, darkQuartz, elfQuartz, lavenderQuartz, manaQuartz, redQuartz, sunnyQuartz, shimmerQuartz)
		
		for (q in quartzs) {
			addShapelessOreDictRecipe(ItemStack(q, 1, 5), ItemStack(q), if (q === darkQuartz) DYES[0] else DYES[15])
			addOreDictRecipe(ItemStack(q, 4, 6), "QQ", "QQ", 'Q', ItemStack(q))
		}
		
		addShapelessOreDictRecipe(ItemStack(elfQuartz, 1, 1), ItemStack(elfQuartz, 1, 7))
		addShapelessOreDictRecipe(ItemStack(elfQuartz, 1, 7), ItemStack(elfQuartz, 1, 1))
		addOreDictRecipe(ItemStack(elfQuartz, 2, 8), "S", "P", "S", 'S', elfQuartzSlab, 'P', ItemStack(elfQuartz, 1, 9))
		addShapelessOreDictRecipe(ItemStack(elfQuartz, 1, 9), ItemStack(elfQuartz))
		addOreDictRecipe(ItemStack(elfQuartz, 1, 10), "PP", "PP", 'P', ItemStack(elfQuartz, 1, 9))
		addOreDictRecipe(ItemStack(elfQuartzWall, 16), "QQQ", "QQQ", 'Q', ItemStack(elfQuartz))
	}
	
	private fun registerShapelessRecipes() {
		arrayOf(ELVORIUM_INGOT, MAUFTRIUM_INGOT, MUSPELHEIM_POWER_INGOT, NIFLHEIM_POWER_INGOT).forEachIndexed { id, ingot ->
			addShapelessOreDictRecipe(ItemStack(alfStorage, 1, id), *Array(9) { ingot })
			addShapelessOreDictRecipe(ItemStack(alfStorage, 1, id), *Array(9) { ingot })
			addShapelessOreDictRecipe(ItemStack(alfStorage, 1, id), *Array(9) { ingot })
			addShapelessOreDictRecipe(ItemStack(alfStorage, 1, id), *Array(9) { ingot })
		}
		
		addShapelessOreDictRecipe(ItemStack(auraRingElven), ELVORIUM_INGOT, auraRingGreater)
		recipeAuraRingElven = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(auraRingGod), MAUFTRIUM_INGOT, auraRingElven)
		recipeAuraRingGod = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(auroraPlanks, 4), auroraWood)
		recipeAuroraPlanks = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(calicoPlanks, 4), calicoWood)
		recipeCalicoPlanks = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(circuitPlanks, 4), circuitWood)
		recipeCircuitPlanks = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(corporeaInjector), hopper, corporeaSpark)
		recipeInjector = BotaniaAPI.getLatestAddedRecipe()
		
		for (i in 0..15)
			addShapelessOreDictRecipe(ItemStack(dye, 1, i), LEAVES[i], PESTLE_AND_MORTAR)
		addShapelessOreDictRecipe(ItemStack(elvenResource, 1, RainbowDust), LEAVES[16], PESTLE_AND_MORTAR)
		recipesLeafDyes = BotaniaAPI.getLatestAddedRecipes(17)
		
		if (Botania.thaumcraftLoaded) {
			val goggles = Item.itemRegistry.getObject("Thaumcraft:ItemGoggles") as Item
			addShapelessRecipe(ItemStack(elementalHelmetRevealing), ItemStack(elementalHelmet), goggles)
			addShapelessRecipe(ItemStack(elvoriumHelmetRevealing), ItemStack(elvoriumHelmet), goggles)
		}
		
		addShapelessOreDictRecipe(ItemStack(elvenResource, 9, ElvoriumNugget), ELVORIUM_INGOT)
		addShapelessOreDictRecipe(ItemStack(elvenResource, 9, MauftriumNugget), MAUFTRIUM_INGOT)
		
		addShapelessRecipe(ItemStack(elvenResource, 9, ElvoriumIngot), ItemStack(alfStorage))
		addShapelessRecipe(ItemStack(elvenResource, 9, MauftriumIngot), ItemStack(alfStorage, 1, 1))
		addShapelessRecipe(ItemStack(elvenResource, 9, MuspelheimPowerIngot), ItemStack(alfStorage, 1, 2))
		addShapelessRecipe(ItemStack(elvenResource, 9, NiflheimPowerIngot), ItemStack(alfStorage, 1, 3))
		
		addShapelessOreDictRecipe(ItemStack(elvenResource, 1, ElvoriumIngot), *Array(9) { ELVORIUM_NUGGET })
		addShapelessOreDictRecipe(ItemStack(elvenResource, 1, MauftriumIngot), *Array(9) { MAUFTRIUM_NUGGET })
		
		addShapelessOreDictRecipe(ItemStack(fertilizer, if (Botania.gardenOfGlassLoaded) 3 else 1), ItemStack(justDye, 1, 15), FLORAL_POWDER, FLORAL_POWDER, FLORAL_POWDER, FLORAL_POWDER)
		CraftingManager.getInstance().recipeList.remove(recipeFertilizerPowder)
		recipeFertilizerPowder = BotaniaAPI.getLatestAddedRecipe()
		
		for (i in 0..5) {
			val enh: Any = if (i < 3) MAUFTRIUM_INGOT else ItemStack(alfStorage, 1, 1)
			addShapelessOreDictRecipe(ItemStack(hyperBucket, 1, i + 1), ItemStack(hyperBucket, 1, i), enh)
		}
		
		addShapelessOreDictRecipe(ItemStack(invisibleFlameLens),
								  ItemStack(lens, 1, 17), phantomInk)
		recipeInvisibleLens = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(lens, 1, 17),
								  ItemStack(invisibleFlameLens), phantomInk)
		recipeInvisibleLensUndo = BotaniaAPI.getLatestAddedRecipe()
		
		for (i in 0..15)
			addShapelessOreDictRecipe(ItemStack(irisPlanks, 4, i), WOOD[i])
		addShapelessOreDictRecipe(ItemStack(rainbowPlanks, 4), rainbowWood)
		recipesColoredPlanks = BotaniaAPI.getLatestAddedRecipes(17)
		
		addShapelessOreDictRecipe(ItemStack(lens, 1, 23), ItemStack(lens), tripwire_hook, ELEMENTIUM)
		recipeLensTripwire = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(lens, 1, 24), ItemStack(lens), RUNE[2], MANA_POWDER)
		recipeLensPush = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(lens, 1, 25), ItemStack(lens), RUNE[1], MANA_POWDER)
		recipeLensSmelt = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(lens, 1, 27), ItemStack(lens), RUNE[11], MANA_POWDER)
		recipeLensTrack = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(lightningPlanks, 4), lightningWood)
		recipeThunderousPlanks = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(livingcobble), LIVING_ROCK)
		recipeLivingcobble = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(livingrockDark), livingrock, "coal")
		addShapelessOreDictRecipe(ItemStack(livingrockDark, 1, 1), ItemStack(livingrock, 1, 1), "coal")
		addShapelessOreDictRecipe(ItemStack(livingrockDark, 1, 2), ItemStack(livingrock, 1, 4), "coal")
		
		addShapelessOreDictRecipe(ItemStack(livingrockDarkStairs[0]), ItemStack(livingrockStairs), "coal")
		addShapelessOreDictRecipe(ItemStack(livingrockDarkStairs[1]), ItemStack(livingrockBrickStairs), "coal")
		
		addShapelessOreDictRecipe(ItemStack(livingrockDarkSlabs[0]), ItemStack(livingrockSlab), "coal")
		addShapelessOreDictRecipe(ItemStack(livingrockDarkSlabs[1]), ItemStack(livingrockBrickSlab), "coal")
		
		addShapelessOreDictRecipe(ItemStack(livingrockDarkWalls[0]), ItemStack(livingrockWall), "coal")
		addShapelessOreDictRecipe(ItemStack(livingrockDarkWalls[1]), ItemStack(livingrockBrickWall), "coal")
		
		for (i in 0..5)
			addShapelessOreDictRecipe(ItemStack(manaResource, 4, 5), ItemStack(ancientWill, 1, i))
		
		val grasses = Array(16) { ItemStack(irisGrass, 1, it) } + Array(2) { ItemStack(rainbowGrass, 1, it) }
		grasses.forEach { addShapelessOreDictRecipe(ItemStack(manaResource, 1, 6), "dustRedstone", it) }
		recipesRedstoneRoot = BotaniaAPI.getLatestAddedRecipes(18)
		
		addShapelessOreDictRecipe(ItemStack(manaRingGod), MAUFTRIUM_INGOT, manaStoneGreater)
		recipeManaRingGod = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(mushroom_stew), MUSHROOM, MUSHROOM, ItemStack(bowl))
		
		addShapelessOreDictRecipe(ItemStack(netherPlanks, 4), netherWood)
		recipeInfernalPlanks = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(elvenResource, 1, RainbowDust), RAINBOW_PETAL, PESTLE_AND_MORTAR)
		recipeRainbowPetalGrinding = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(elvenResource, 2, RainbowPetal), RAINBOW_FLOWER)
		addShapelessOreDictRecipe(ItemStack(elvenResource, 4, RainbowPetal), RAINBOW_DOUBLE_FLOWER)
		recipesRainbowPetal = BotaniaAPI.getLatestAddedRecipes(2)
		
		addShapelessOreDictRecipe(ItemStack(fireGrenade), vial, fire_charge, gunpowder)
		
		addShapelessOreDictRecipe(ItemStack(rainbowGrass, 1, 3), "dustGlowstone", "dustGlowstone", ItemStack(rainbowGrass, 1, 2))
		recipesShinyFlowers.add(BotaniaAPI.getLatestAddedRecipe())
		
		addShapelessRecipe(ItemStack(rainbowMushroom), ItemStack(red_mushroom), ItemStack(elvenResource, 1, RainbowDust))
		addShapelessRecipe(ItemStack(rainbowMushroom), ItemStack(brown_mushroom), ItemStack(elvenResource, 1, RainbowDust))
		recipesMushrooms.addAll(BotaniaAPI.getLatestAddedRecipes(2))
		
		addOreDictRecipe(ItemStack(rainbowPetalBlock), "PPP", "PPP", "PPP", 'P', ItemStack(elvenResource, 1, RainbowPetal))
		recipeRainbowPetalBlock = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(sealingPlanks, 4), sealingWood)
		recipeSealingPlanks = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(shimmerrock), "livingrock", DYES[16])
		CraftingManager.getInstance().recipeList.remove(recipeShimmerrock)
		recipeShimmerrock = BotaniaAPI.getLatestAddedRecipe()
		
		recipeShimmerQuartz = addQuartzRecipes(shimmerQuartz, shimmerQuartzStairs, shimmerQuartzSlab)
		
		addShapelessOreDictRecipe(ItemStack(shimmerwoodPlanks), ItemStack(dreamwood, 1, 1), DYES[16])
		CraftingManager.getInstance().recipeList.remove(recipeShimmerwoodPlanks)
		recipeShimmerwoodPlanks = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessRecipe(ItemStack(brown_mushroom), mushroom)
		addShapelessRecipe(ItemStack(red_mushroom), mushroom)
	}
	
	private fun registerSmeltingRecipes() {
		for (i in 0..3) {
			addSmelting(ItemStack(altWood0, 1, i), ItemStack(coal, 1, 1), 0.15F)
			
			if (i != 2)
			addSmelting(ItemStack(altWood1, 1, i), ItemStack(coal, 1, 1), 0.15F)
		}
		
		addSmelting(irisWood0, ItemStack(coal, 1, 1), 0.15F)
		addSmelting(irisWood1, ItemStack(coal, 1, 1), 0.15F)
		addSmelting(irisWood2, ItemStack(coal, 1, 1), 0.15F)
		addSmelting(irisWood3, ItemStack(coal, 1, 1), 0.15F)
		addSmelting(rainbowWood, ItemStack(coal, 1, 1), 0.15F)
		addSmelting(auroraWood, ItemStack(coal, 1, 1), 0.15F)
		addSmelting(lightningWood, ItemStack(coal, 1, 1), 0.15F)
		addSmelting(sealingWood, ItemStack(coal, 1, 1), 0.15F)
		addSmelting(netherWood, ItemStack(elvenResource, 1, NetherwoodCoal), 0.15F)
		addSmelting(calicoWood, ItemStack(coal, 1, 1), 0.15F)
		addSmelting(circuitWood, ItemStack(coal, 1, 1), 0.15F)
		addSmelting(lightningPlanks, ItemStack(elvenResource, 2, ThunderwoodSplinters), 0.1F)
		addSmelting(netherPlanks, ItemStack(elvenResource, 2, NetherwoodSplinters), 0.1F)
		
		addSmelting(ItemStack(elvenOre), ItemStack(manaResource, 1, 9), 1f)
		addSmelting(ItemStack(elvenOre, 1, 1), ItemStack(manaResource, 1, 7), 1f)
		addSmelting(ItemStack(elvenOre, 1, 2), ItemStack(manaquartz, 1, 5), 1f)
		addSmelting(ItemStack(elvenOre, 1, 3), ItemStack(gold_ingot), 1f)
		addSmelting(ItemStack(elvenOre, 1, 4), ItemStack(elvenResource, 1, IffesalDust), 1f)
		addSmelting(ItemStack(elvenOre, 1, 5), ItemStack(justDye, 1, 4), 0.2f)
		
		
		
		addSmelting(elvenSand, ItemStack(elfGlass), 1f)
		addSmelting(elvenSandstone, ItemStack(elvenSandstone, 1, 4), 1f)
		addSmelting(manaGlass, ItemStack(glass), 0f)
	}
	
	private fun registerManaInfusionRecipes() {
		// Why is this here?
		/*addRecipe(new ItemStack(elfGlass), 100,
			new ItemStack[] {new ItemStack(Modquartz, 1, 5), new ItemStack(elvenGlass)});*/
		
		recipeTerrasteel = addInfuserRecipe(ItemStack(manaResource, 1, 4),
											TilePool.MAX_MANA / 2,
											MANA_STEEL,
											MANA_PEARL,
											MANA_DIAMOND)
		
		recipeElvorium = addInfuserRecipe(ItemStack(elvenResource, 1, ElvoriumIngot),
										  TilePool.MAX_MANA / 2,
										  ELEMENTIUM,
										  PIXIE_DUST,
										  DRAGONSTONE)
		
		recipeMauftrium = addInfuserRecipe(ItemStack(elvenResource, 1, MauftriumIngot),
										   TilePool.MAX_MANA,
										   GAIA_INGOT,
										   MUSPELHEIM_POWER_INGOT,
										   NIFLHEIM_POWER_INGOT)
		
		recipeManaStone = addInfuserRecipe(ItemStack(manaStone, 1, 1000),
										   TilePool.MAX_MANA,
										   DRAGONSTONE,
										   ItemStack(elvenResource, 4, IffesalDust))
		
		recipeManaStoneGreater = addInfuserRecipe(ItemStack(manaStoneGreater, 1, 1000),
												  TilePool.MAX_MANA * 4,
												  ItemStack(manaStone, 1, WILDCARD_VALUE).also { ItemNBTHelper.setBoolean(it, ASJUtilities.TAG_ASJIGNORENBT, true) },
												  ItemStack(manaResource, 4, 5),
												  ItemStack(elvenResource, 1, MuspelheimEssence),
												  ItemStack(elvenResource, 1, NiflheimEssence))
	}
	
	private fun registerDendrology() {
		recipeLightningTree = AlfheimAPI.addTreeRecipe(50000,
														 ItemStack(lightningSapling),
														 ItemStack(irisSapling),
														 350,
														 MANA_STEEL, MANA_STEEL, MANA_STEEL,
														 RUNE[13], // Wrath
														 LEAVES[10], LEAVES[10], LEAVES[10], // Purple
														 ItemStack(teruTeruBozu))
		
		recipeInfernalTree = AlfheimAPI.addTreeRecipe(10000,
														ItemStack(netherSapling),
														ItemStack(irisSapling),
														70,
														"ingotBrickNether", "ingotBrickNether", "ingotBrickNether",
														RUNE[1], // Fire
														LEAVES[1], LEAVES[1], LEAVES[1], // orange
														BLAZE_BLOCK)
		
		recipeCalicoTree = AlfheimAPI.addTreeRecipe(50000,
													  ItemStack(calicoSapling),
													  ItemStack(irisSapling),
													  70,
													  ItemStack(soul_sand), ItemStack(soul_sand), ItemStack(soul_sand),
													  RUNE[11], // Greed
													  LEAVES[1], LEAVES[0], LEAVES[12], // Orange White Brown
													  ItemStack(obsidian))
		
		recipeCircuitTree = AlfheimAPI.addTreeRecipe(10000,
													   ItemStack(circuitSapling),
													   ItemStack(irisSapling),
													   70,
													   ItemStack(repeater), ItemStack(comparator), ItemStack(repeater),
													   RUNE[9], // Lust
													   LEAVES[14], LEAVES[14], LEAVES[14], // Red
													   "blockRedstone")
		
		recipeSealingTree = AlfheimAPI.addTreeRecipe(50000,
													   ItemStack(sealingSapling),
													   ItemStack(irisSapling),
													   350,
													   ELEMENTIUM, ELEMENTIUM, ELEMENTIUM,
													   RUNE[7], // Winter
													   LEAVES[0], LEAVES[0], LEAVES[0], // White
													   ItemStack(wool, 1, WILDCARD_VALUE))
		
		if (Botania.thaumcraftLoaded && ((Botania.gardenOfGlassLoaded && AlfheimConfigHandler.thaumTreeSuffusion) || ModInfo.DEV))
			ThaumcraftSuffusionRecipes.initRecipes()
	}
	
	private fun registerRecipies() {
		val costTier1 = 5200
		val costTier2 = 8000
		val costTier3 = 12000
		
		recipeCrysanthermum = BotaniaAPI.registerPetalRecipe(BotaniaAPI.internalHandler.getSubTileAsStack("crysanthermum"),
															 PETAL[1], PETAL[1], // Orange
															 PETAL[15], // Black
															 PETAL[3], PETAL[3], // Light Blue
															 RUNE[7], // Winter
															 RUNE[5]) // Summer
		
		recipeOrechidEndium = BotaniaAPI.registerPetalRecipe(BotaniaAPI.internalHandler.getSubTileAsStack("orechidEndium"),
															 PETAL[4],  // Yellow
															 PETAL[10], // Purple
															 ItemStack(manaResource, 1, 15), // Ender Air
															 PETAL[10], // Purple
															 PETAL[6],  // Pink
															 RUNE[15],  // Pride
															 RUNE[11],  // Greed
															 REDSTONE_ROOT,
															 PIXIE_DUST)
		
		recipePetronia = BotaniaAPI.registerPetalRecipe(BotaniaAPI.internalHandler.getSubTileAsStack("petronia"),
														REDSTONE_ROOT,
														RUNE[0],   // Water
														RUNE[1],   // Fire
														PETAL[1],  // Orange
														PETAL[15], // Black
														PETAL[12], // Brown
														DRAGONSTONE)
		
		recipeRainFlower = BotaniaAPI.registerPetalRecipe(BotaniaAPI.internalHandler.getSubTileAsStack("rainFlower"),
														  *Array(4) { PETAL[11] }, // Blue
														  PETAL[3], PETAL[3], // Light Blue
														  PETAL[4]) // Yellow
		
		recipeSnowFlower = BotaniaAPI.registerPetalRecipe(BotaniaAPI.internalHandler.getSubTileAsStack("snowFlower"),
														  *Array(4) { PETAL[3] }, // Light Blue
														  *Array(3) { PETAL[0] }) // White
		
		recipeStormFlower = BotaniaAPI.registerPetalRecipe(BotaniaAPI.internalHandler.getSubTileAsStack("stormFlower"),
														   *Array(4) { PETAL[3] }, // Light Blue
														   PETAL[11], // Blue
														   RUNE[13])  // Wrath
		
		recipeWindFlower = BotaniaAPI.registerPetalRecipe(BotaniaAPI.internalHandler.getSubTileAsStack("windFlower"),
														  PETAL[4], PETAL[4], // Yellow
														  PETAL[5], PETAL[5], // Lime
														  RUNE[6]) // Autumn
		
		recipeRealityRune = BotaniaAPI.registerRuneAltarRecipe(ItemStack(elvenResource, 1, PrimalRune), costTier3,
															   RUNE[0], RUNE[1], RUNE[2], RUNE[3], RUNE[8], ItemStack(manaResource, 1, 15), MAUFTRIUM_INGOT)
		recipeMuspelheimRune = BotaniaAPI.registerRuneAltarRecipe(ItemStack(elvenResource, 1, MuspelheimRune), costTier3,
																  RUNE[1], RUNE[2], ItemStack(elvenResource, 1, MuspelheimEssence), ItemStack(elvenResource, 1, MuspelheimEssence), IFFESAL_DUST)
		recipeNiflheimRune = BotaniaAPI.registerRuneAltarRecipe(ItemStack(elvenResource, 1, NiflheimRune), costTier3,
																RUNE[0], RUNE[3], ItemStack(elvenResource, 1, NiflheimEssence), ItemStack(elvenResource, 1, NiflheimEssence), IFFESAL_DUST)
		
		BotaniaAPI.runeAltarRecipes.remove(ModRuneRecipes.recipeSummerRune)
		ModRuneRecipes.recipeSummerRune = BotaniaAPI.registerRuneAltarRecipe(ItemStack(rune, 1, 5), costTier2, RUNE[2], RUNE[3], "sand", "sand", ItemStack(slime_ball), ItemStack(melon))
		
		ModRuneRecipes.recipesEarthRune.add(BotaniaAPI.registerRuneAltarRecipe(ItemStack(rune, 2, 2), costTier1, MANA_POWDER, MANA_STEEL, ItemStack(livingcobble), ItemStack(obsidian), ItemStack(brown_mushroom)))
		ModRuneRecipes.recipesEarthRune.add(BotaniaAPI.registerRuneAltarRecipe(ItemStack(rune, 2, 2), costTier1, MANA_POWDER, MANA_STEEL, ItemStack(livingcobble), ItemStack(obsidian), ItemStack(red_mushroom)))
		
		recipeInterdimensional = BotaniaAPI.registerElvenTradeRecipe(ItemStack(elvenResource, 1, InterdimensionalGatewayCore), ItemStack(nether_star))
		//recipeStoryToken = BotaniaAPI.registerElvenTradeRecipe(ItemStack(storyToken, 1, 1), ItemStack(storyToken))
		
		recipeDreamwood = BotaniaAPI.registerPureDaisyRecipe(DREAM_WOOD_LOG, dreamwood, 0)
		BotaniaAPI.registerPureDaisyRecipe("cobblestone", livingcobble, 0)
		recipePlainDirt = BotaniaAPI.registerPureDaisyRecipe(IRIS_DIRT, dirt, 0)
		
		recipeIrisSapling = RecipePureDaisyExclusion("treeSapling", irisSapling, 0)
		BotaniaAPI.pureDaisyRecipes.add(recipeIrisSapling)
		
		BotaniaAPI.registerManaInfusionRecipe(ItemStack(elvenResource, 1, InfusedDreamwoodTwig), ItemStack(manaResource, 1, 13), 10000)
		ModManaInfusionRecipes.manaPowderRecipes.add(BotaniaAPI.registerManaInfusionRecipe(ItemStack(manaResource, 1, 23), ItemStack(elvenResource, 1, RainbowDust), 400))
		
		val grasses = Array(16) { ItemStack(irisGrass, 1, it) } + Array(2) { ItemStack(rainbowGrass, 1, it) }
		recipesPastoralSeeds = grasses.mapIndexed { id, it -> BotaniaAPI.registerManaInfusionRecipe(ItemStack(irisSeeds, 1, id), it, 2500) }
		
		addRecipe(RecipeHelmetElvorium(elvoriumHelmet, terrasteelHelm))
		recipeElvoriumHelmet = BotaniaAPI.getLatestAddedRecipe()
		
		if (Botania.thaumcraftLoaded) {
			addRecipe(RecipeHelmetElvorium(elvoriumHelmetRevealing!!, terrasteelHelmRevealing))
			addRecipe(RecipeHelmRevealingAlfheim())
		}
		
		recipesAttributionHeads = ArrayList()
		recipesAttributionHeads.add(attributionSkull("yrsegal", irisSeeds, 16)) // Bifrost Seeds
		// Wire - I just love rainbows, what can I say?
		recipesAttributionHeads.add(attributionSkull("l0nekitsune", elvenResource, NetherwoodCoal))
		// L0ne - "hot stuff" (because I'm classy like that)
		recipesAttributionHeads.add(attributionSkull("Tristaric", coatOfArms, 6)) // Irish Shield
		// Tris - The only item that remotely fits me.
		
		recipeSplashPotions = ShapelessOreRecipe(ItemStack(splashPotion), brewVial, gunpowder)
		
		RecipeSorter.register("${ModInfo.MODID}:elvhelm", RecipeHelmetElvorium::class.java, RecipeSorter.Category.SHAPED, "after:forge:shapedore")
		addRecipe(RecipeRingDyes())
		RecipeSorter.register("${ModInfo.MODID}:ringdye", RecipeRingDyes::class.java, RecipeSorter.Category.SHAPELESS, "")
		addRecipe(RecipeRainbowLensDye())
		RecipeSorter.register("${ModInfo.MODID}:lensdye", RecipeRainbowLensDye::class.java, RecipeSorter.Category.SHAPELESS, "")
		addRecipe(RecipeLootInterceptor())
		RecipeSorter.register("${ModInfo.MODID}:looter", RecipeLootInterceptor::class.java, RecipeSorter.Category.SHAPELESS, "")
		addRecipe(RecipeLootInterceptorClear())
		RecipeSorter.register("${ModInfo.MODID}:looterclean", RecipeLootInterceptorClear::class.java, RecipeSorter.Category.SHAPELESS, "")
		addRecipe(RecipeCleanRelic())
		RecipeSorter.register("${ModInfo.MODID}:cleanrelic", RecipeCleanRelic::class.java, RecipeSorter.Category.SHAPELESS, "")
		addRecipe(RecipeThrowablePotion())
		RecipeSorter.register("${ModInfo.MODID}:throwpotion", RecipeThrowablePotion::class.java, RecipeSorter.Category.SHAPELESS, "")
		
		addRecipe(RecipeElvenWeed())
	}
	
	private fun banRetrades() {
		AlfheimAPI.banRetrade(recipeInterdimensional.output)
		//AlfheimAPI.banRetrade(recipeStoryToken.output)
		AlfheimAPI.banRetrade(ItemStack(iron_ingot))
		AlfheimAPI.banRetrade(ItemStack(iron_block))
		AlfheimAPI.banRetrade(ItemStack(ender_pearl))
		AlfheimAPI.banRetrade(ItemStack(diamond))
		AlfheimAPI.banRetrade(ItemStack(diamond_block))
	}
	
	fun postInit() {
		recipeGaiaPylonOld = recipeGaiaPylon
		recipeSuperLavaPendant = recipeSuperLavaPendantNew
	}
	
	fun addMMORecipes() {
		CraftingManager.getInstance().recipeList.add(recipePaperBreak)
		CraftingManager.getInstance().recipeList.add(recipePeacePipe)
	}
	
	fun removeMMORecipes() {
		ASJUtilities.removeRecipe(paperBreak, 4)
		ASJUtilities.removeRecipe(peacePipe)
	}
	
	private fun addQuartzRecipes(block: Block, stairs: Block, slab: Block): IRecipe {
		addRecipe(ItemStack(block),
							   "QQ",
							   "QQ",
							   'Q', ItemStack(elvenResource, 1, RainbowQuartz))
		
		BotaniaAPI.registerManaAlchemyRecipe(ItemStack(elvenResource, 4, RainbowQuartz), ItemStack(block, 1, 32767), 25)
		
		addRecipe(ItemStack(block, 2, 2),
							   "Q",
							   "Q",
							   'Q', block)
		
		addRecipe(ItemStack(block, 1, 1),
							   "Q",
							   "Q",
							   'Q', slab)
		addStairsAndSlabs(block, 0, stairs, slab)
		
		addRecipe(ShapedOreRecipe(ItemStack(elvenResource, 8, RainbowQuartz),
											   "QQQ",
											   "QCQ",
											   "QQQ",
											   'Q', "gemQuartz",
											   'C', DYES[16]))
		return BotaniaAPI.getLatestAddedRecipe()
	}
	
	private fun addStairsAndSlabs(block: Block, meta: Int, stairs: Block, slab: Block) {
		addRecipe(ItemStack(slab, 6), "QQQ", 'Q', ItemStack(block, 1, meta))
		addRecipe(ItemStack(stairs, 4), "Q  ", "QQ ", "QQQ", 'Q', ItemStack(block, 1, meta))
	}
	
	fun skullStack(name: String): ItemStack {
		val stack = ItemStack(skullPlacer, 1, 3)
		ItemNBTHelper.setString(stack, "SkullOwner", name)
		return stack
	}
	
	private fun attributionSkull(name: String, item: Item, meta: Int) =
		BotaniaAPI.registerPetalRecipe(skullStack(name), *Array(16) { ItemStack(item, 1, meta) })
}