package alfheim.common.block

import alexsocol.asjlib.extendables.block.BlockModMeta
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.api.lib.LibOreDict
import alfheim.common.block.mana.*
import alfheim.common.lexicon.AlfheimLexiconData
import net.minecraft.block.Block
import net.minecraft.block.Block.soundTypeGravel
import net.minecraft.block.material.Material
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary.registerOre

object AlfheimBlocks {
	
	val alfheimPortal: Block
	val alfheimPylon: Block
	val alfStorage: Block
	val animatedTorch: Block
	val anomaly: Block
	val anyavil: Block
	val dreamLeaves: Block
	val dreamLog: Block
	val dreamSapling: Block
	val elvenOres: Block
	val elvenSand: Block
	val flugelHead: Block
	val flugelHead2: Block
	val livingcobble: Block
	val manaAccelerator: Block
	val manaInfuser: Block
	val poisonIce: Block
	val powerStone: Block
	val raceSelector: Block
	val redFlame: Block
	val tradePortal: Block
	//val transferer: Block BACK
	
	init {
		alfheimPortal = BlockAlfheimPortal()
		alfheimPylon = BlockAlfheimPylon()
		alfStorage = BlockModMeta(Material.iron, 2, ModInfo.MODID, "alfStorage", AlfheimCore.alfheimTab, 5f, resist = 60f)
		animatedTorch = BlockAnimatedTorch()
		anomaly = BlockAnomaly()
		anyavil = BlockAnyavil()
		dreamLeaves = BlockDreamLeaves()
		dreamLog = BlockDreamLog()
		dreamSapling = BlockDreamSapling()
		elvenOres = BlockElvenOres()
		elvenSand = BlockPatternLexicon(ModInfo.MODID, Material.sand, "ElvenSand", AlfheimCore.alfheimTab, 0f, 255, 1f, "shovel", 0, 5f, soundTypeGravel, true, false, true, AlfheimLexiconData.worldgen)
		flugelHead = BlockHeadFlugel()
		flugelHead2 = BlockHeadMiku()
		livingcobble = BlockModMeta(Material.rock, 3, ModInfo.MODID, "LivingCobble", AlfheimCore.alfheimTab, 2f, resist = 60f)
		manaAccelerator = BlockManaAccelerator()
		manaInfuser = BlockManaInfuser()
		poisonIce = BlockPoisonIce()
		powerStone = BlockPowerStone()
		raceSelector = BlockRaceSelector()
		redFlame = BlockRedFlame()
		tradePortal = BlockTradePortal()
		//transferer = BlockTransferer() BACK
		
		regOreDict()
	}
	
	private fun regOreDict() {
		registerOre(LibOreDict.DRAGON_ORE, ItemStack(elvenOres, 1, 0))
		registerOre(LibOreDict.ELEMENTIUM_ORE, ItemStack(elvenOres, 1, 1))
		registerOre(LibOreDict.ELVEN_QUARTZ_ORE, ItemStack(elvenOres, 1, 2))
		registerOre(LibOreDict.GOLD_ORE, ItemStack(elvenOres, 1, 3))
		registerOre(LibOreDict.IFFESAL_ORE, ItemStack(elvenOres, 1, 4))
		
		registerOre("sand", ItemStack(elvenSand))
	}
	
	fun Block.setHarvestLevelI(toolClass: String, level: Int) = also { it.setHarvestLevel(toolClass, level) }
}
