package alfheim.common.core.registry

import alexsocol.asjlib.ASJUtilities.*
import cpw.mods.fml.common.registry.GameRegistry.*
import net.minecraft.block.Block.*
import net.minecraftforge.oredict.OreDictionary.*

import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.api.lib.LibOreDict
import alfheim.common.block.*
import alfheim.common.block.mana.*
import alfheim.common.item.block.*
import alfheim.common.lexicon.AlfheimLexiconData
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.item.ItemStack
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName

object AlfheimBlocks {
	
	var alfheimPortal: Block
	var alfheimPylon: Block
	var animatedTorch: Block
	var anomaly: Block
	var anyavil: Block
	var dreamLeaves: Block
	var dreamLog: Block
	var dreamSapling: Block
	var elvenOres: Block
	var elvenSand: Block
	var elvoriumBlock: Block
	var flugelHead: Block
	var itemHolder: Block
	var livingcobble: Block
	var mauftriumBlock: Block
	var manaInfuser: Block
	var poisonIce: Block
	var powerStone: Block
	var redFlame: Block
	var tradePortal: Block
	//public static Block transferer; BACK
	
	fun init() {
		construct()
		reg()
		regOreDict()
	}
	
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
		flugelHead = BlockFlugelHead()
		itemHolder = BlockItemHolder()
		livingcobble = BlockPatternLexicon(ModInfo.MODID, Material.rock, "LivingCobble", AlfheimCore.alfheimTab, 0f, 255, 2f, "pickaxe", 0, 60f, soundTypeStone, true, false, false, AlfheimLexiconData.worldgen)
		mauftriumBlock = BlockPatternLexicon(ModInfo.MODID, Material.iron, "MauftriumBlock", AlfheimCore.alfheimTab, 0f, 255, 5f, "pickaxe", 1, 60f, soundTypeMetal, true, true, false, AlfheimLexiconData.essences)
		manaInfuser = BlockManaInfuser()
		poisonIce = BlockPoisonIce()
		powerStone = BlockPowerStone()
		redFlame = BlockRedFlame()
		tradePortal = BlockTradePortal()
		//transferer = new BlockTransferer(); BACK
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
