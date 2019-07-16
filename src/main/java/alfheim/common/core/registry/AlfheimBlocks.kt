package alfheim.common.core.registry

import alexsocol.asjlib.ASJUtilities.Companion.getBlockName
import alexsocol.asjlib.ASJUtilities.Companion.register
import alexsocol.asjlib.extendables.block.BlockModMeta
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.api.lib.LibOreDict
import alfheim.common.block.*
import alfheim.common.block.mana.BlockManaInfuser
import alfheim.common.item.block.*
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.common.registry.GameRegistry.registerBlock
import net.minecraft.block.Block
import net.minecraft.block.Block.*
import net.minecraft.block.material.Material
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary.registerOre
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName

object AlfheimBlocks {
	
	lateinit var alfheimPortal: Block
	lateinit var alfheimPylon: Block
	lateinit var animatedTorch: Block
	lateinit var anomaly: Block
	lateinit var anyavil: Block
	lateinit var dreamLeaves: Block
	lateinit var dreamLog: Block
	lateinit var dreamSapling: Block
	lateinit var elvenOres: Block
	lateinit var elvenSand: Block
	lateinit var elvoriumBlock: Block
	lateinit var flugelHead: Block
	lateinit var flugelHead2: Block
	lateinit var itemHolder: Block
	lateinit var livingcobble: Block
	lateinit var mauftriumBlock: Block
	lateinit var manaInfuser: Block
	lateinit var poisonIce: Block
	lateinit var powerStone: Block
	lateinit var redFlame: Block
	lateinit var tradePortal: Block
	//lateinit var transferer: Block BACK
	
	lateinit var shrineLight: Block
	lateinit var shrineRock: Block
	lateinit var shrinePillar: Block
	lateinit var shrineGlass: Block
	lateinit var shrinePanel: Block
	
	fun init() {
		construct()
		reg()
		regOreDict()
	}
	
	fun Block.setHarvestLevelI(toolClass: String, level: Int) = also { it.setHarvestLevel(toolClass, level) }
	
	private fun construct() {
		alfheimPortal = BlockAlfheimPortal()
		alfheimPylon = BlockAlfheimPylon()
		animatedTorch = BlockAnimatedTorch()
		anomaly = BlockAnomaly()
		anyavil = BlockAnyavil()
		dreamLeaves = BlockDreamLeaves()
		dreamLog = BlockDreamLog()
		dreamSapling = BlockDreamSapling()
		elvenOres = BlockElvenOres()
		elvenSand = BlockPatternLexicon(ModInfo.MODID, Material.sand, "ElvenSand", AlfheimCore.alfheimTab, 0f, 255, 1f, "shovel", 0, 5f, soundTypeGravel, true, false, true, AlfheimLexiconData.worldgen)
		elvoriumBlock = BlockPatternLexicon(ModInfo.MODID, Material.iron, "ElvoriumBlock", AlfheimCore.alfheimTab, 0f, 255, 5f, "pickaxe", 1, 60f, soundTypeMetal, true, true, false, AlfheimLexiconData.elvorium)
		flugelHead = BlockHeadFlugel()
		flugelHead2 = BlockHeadMiku()
		itemHolder = BlockItemHolder()
		livingcobble = BlockPatternLexicon(ModInfo.MODID, Material.rock, "LivingCobble", AlfheimCore.alfheimTab, 0f, 255, 2f, "pickaxe", 0, 60f, soundTypeStone, true, false, false, AlfheimLexiconData.worldgen)
		mauftriumBlock = BlockPatternLexicon(ModInfo.MODID, Material.iron, "MauftriumBlock", AlfheimCore.alfheimTab, 0f, 255, 5f, "pickaxe", 1, 60f, soundTypeMetal, true, true, false, AlfheimLexiconData.essences)
		manaInfuser = BlockManaInfuser()
		poisonIce = BlockPoisonIce()
		powerStone = BlockPowerStone()
		redFlame = BlockRedFlame()
		tradePortal = BlockTradePortal()
		//transferer = new BlockTransferer(); BACK
		
		shrineRock = BlockModMeta(Material.rock, 16, "shrines")	.setBlockName("ShrineRock")
																.setCreativeTab(AlfheimCore.alfheimTab)
																.setHardness(10f)
																.setResistance(10000f)
																.setStepSound(soundTypeStone)
																.setHarvestLevelI("pickaxe", 2)
		
		shrineLight = BlockModMeta(Material.glass, 4, "shrines").setBlockName("ShrineLight")
																.setCreativeTab(AlfheimCore.alfheimTab)
																.setLightLevel(1f)
																.setLightOpacity(0)
																.setHardness(1f)
																.setResistance(6000f)
																.setStepSound(soundTypeGlass)
																.setHarvestLevelI("pickaxe", 1)
		
		shrinePillar = BlockShrinePillar()
		shrineGlass = BlockShrineGlass()
		shrinePanel = BlockPaneMeta(Material.glass, 4, "ShrinePanel", "shrines").setBlockName("ShrinePanel")
																.setCreativeTab(AlfheimCore.alfheimTab)
																.setLightOpacity(0)
																.setHardness(1f)
																.setResistance(600f)
																.setStepSound(soundTypeGlass)
																.setHarvestLevelI("pickaxe", 1)
		
		
	}
	
	private fun reg() {
		register(manaInfuser)
		register(alfheimPortal)
		register(tradePortal)
		//register(transferer); BACK
		register(itemHolder)
		registerBlock(alfheimPylon, ItemBlockWithMetadataAndName::class.java, getBlockName(alfheimPylon))
		registerBlock(powerStone, ItemBlockWithMetadataAndName::class.java, getBlockName(powerStone))
		register(anyavil)
		register(elvoriumBlock)
		register(mauftriumBlock)
		registerBlock(elvenOres, ItemBlockElvenOres::class.java, getBlockName(elvenOres))
		register(livingcobble)
		register(elvenSand)
		register(dreamLog)
		register(dreamLeaves)
		register(dreamSapling)
		register(animatedTorch)
		register(flugelHead)
		register(flugelHead2)
		registerBlock(anomaly, ItemBlockAnomaly::class.java, getBlockName(anomaly))
		register(poisonIce)
		register(redFlame)
	}
	
	private fun regOreDict() {
		registerOre(LibOreDict.DRAGON_ORE, ItemStack(elvenOres, 1, 0))
		registerOre(LibOreDict.ELEMENTIUM_ORE, ItemStack(elvenOres, 1, 1))
		registerOre(LibOreDict.ELVEN_QUARTZ_ORE, ItemStack(elvenOres, 1, 2))
		registerOre(LibOreDict.GOLD_ORE, ItemStack(elvenOres, 1, 3))
		registerOre(LibOreDict.IFFESAL_ORE, ItemStack(elvenOres, 1, 4))
		
		registerOre("sand", ItemStack(elvenSand))
	}
}
