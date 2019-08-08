package alfheim.common.block

import alexsocol.asjlib.ASJUtilities.Companion.getBlockName
import alexsocol.asjlib.ASJUtilities.Companion.register
import alexsocol.asjlib.extendables.block.*
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.api.lib.LibOreDict
import alfheim.common.block.mana.BlockManaInfuser
import alfheim.common.item.block.*
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.common.registry.GameRegistry.registerBlock
import net.minecraft.block.Block
import net.minecraft.block.Block.*
import net.minecraft.block.material.Material
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary.registerOre
import vazkii.botania.client.lib.LibResources
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.block.decor.slabs.BlockModSlab
import vazkii.botania.common.block.decor.walls.BlockModWall
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
	lateinit var raceSelector: Block
	lateinit var redFlame: Block
	lateinit var tradePortal: Block
	//lateinit var transferer: Block BACK
	
	lateinit var dreamwoodFence: Block
	lateinit var dreamwoodFenceGate: Block
	lateinit var dreamwoodBarkFence: Block
	lateinit var dreamwoodBarkFenceGate: Block
	lateinit var elvenSandstone: Block
	lateinit var elvenSandstoneStairs: Block
	lateinit var elvenSandstoneSlab: Block
	lateinit var elvenSandstoneSlabFull: Block
	lateinit var livingcobbleStairs: Block
	lateinit var livingcobbleSlab: Block
	lateinit var livingcobbleSlabFull: Block
	lateinit var livingcobbleWall: Block
	lateinit var livingrockBrickWall: Block
	lateinit var livingrockTileSlab: Block
	lateinit var livingrockTileSlabFull: Block
	lateinit var livingwoodFence: Block
	lateinit var livingwoodFenceGate: Block
	lateinit var livingwoodBarkFence: Block
	lateinit var livingwoodBarkFenceGate: Block
	lateinit var shrineLight: Block
	lateinit var shrineGlass: Block
	lateinit var shrinePanel: Block
	lateinit var shrinePillar: Block
	lateinit var shrineRock: Block
	lateinit var waterSlab: Block
	
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
		livingcobble = BlockModMeta(Material.rock, 3, ModInfo.MODID).setBlockName("Livingsubstone").setCreativeTab(AlfheimCore.alfheimTab).setHardness(2f).setResistance(60f).setStepSound(soundTypeStone).setHarvestLevelI("pickaxe", 1)
		mauftriumBlock = BlockPatternLexicon(ModInfo.MODID, Material.iron, "MauftriumBlock", AlfheimCore.alfheimTab, 0f, 255, 5f, "pickaxe", 1, 60f, soundTypeMetal, true, true, false, AlfheimLexiconData.essences)
		manaInfuser = BlockManaInfuser()
		poisonIce = BlockPoisonIce()
		powerStone = BlockPowerStone()
		raceSelector = BlockRaceSelector()
		redFlame = BlockRedFlame()
		tradePortal = BlockTradePortal()
		//transferer = new BlockTransferer(); BACK
		
		shrineRock = BlockModMeta(Material.rock, 16, ModInfo.MODID, "shrines")	.setBlockName("ShrineRock")
																.setCreativeTab(AlfheimCore.alfheimTab)
																.setHardness(10f)
																.setResistance(10000f)
																.setStepSound(soundTypeStone)
																.setHarvestLevelI("pickaxe", 2)
		
		shrineLight = BlockModMeta(Material.glass, 4, ModInfo.MODID, "shrines").setBlockName("ShrineLight")
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
		
		elvenSandstone = BlockElvenSandstone()
		
		elvenSandstoneStairs = BlockModStairs(elvenSandstone, 0).setBlockName("ElvenSandstoneStairs")
																.setCreativeTab(AlfheimCore.alfheimTab)
		
		elvenSandstoneSlab = BlockElvenSandstoneSlab(false)
		elvenSandstoneSlabFull = BlockElvenSandstoneSlab(true)
		elvenSandstoneSlab.setCreativeTab(AlfheimCore.alfheimTab)
		(elvenSandstoneSlab as BlockModSlab).register()
		(elvenSandstoneSlabFull as BlockModSlab).register()
		
		livingcobbleStairs = BlockModStairs(livingcobble, 0).setBlockName("LivingCobbleStairs")
															.setCreativeTab(AlfheimCore.alfheimTab)
		
		livingcobbleSlab = BlockLivingCobbleSlab(false)
		livingcobbleSlabFull = BlockLivingCobbleSlab(true)
		livingcobbleSlab.setCreativeTab(AlfheimCore.alfheimTab)
		(livingcobbleSlab as BlockModSlab).register()
		(livingcobbleSlabFull as BlockModSlab).register()
		
		livingrockTileSlab = BlockLivingRockTileSlab(false)
		livingrockTileSlabFull = BlockLivingRockTileSlab(true)
		livingrockTileSlab.setCreativeTab(AlfheimCore.alfheimTab)
		(livingrockTileSlab as BlockModSlab).register()
		(livingrockTileSlabFull as BlockModSlab).register()
		
		livingcobbleWall = BlockModWall(livingcobble, 0).setCreativeTab(AlfheimCore.alfheimTab)
														.setHardness(5f)
														.setResistance(8000f)
														.setStepSound(soundTypeStone)
														.setHarvestLevelI("pickaxe", 2)
		
		livingrockBrickWall = BlockModWall(ModBlocks.livingrock, 1).setCreativeTab(AlfheimCore.alfheimTab)
																	.setHardness(5f)
																	.setResistance(8000f)
																	.setStepSound(soundTypeStone)
																	.setHarvestLevelI("pickaxe", 2)
		
		
		
		livingwoodBarkFenceGate = BlockModFenceGate(ModBlocks.livingwood, 0)	.setBlockName("LivingwoodBarkFenceGate")
																				.setCreativeTab(AlfheimCore.alfheimTab)
																				.setHardness(2F)
																				.setResistance(5F)
																				.setStepSound(soundTypeWood)
		
		livingwoodBarkFence = BlockModFence(LibResources.PREFIX_MOD + "livingwood0", Material.wood, livingwoodBarkFenceGate)	.setBlockName("LivingwoodBarkFence")
																																.setCreativeTab(AlfheimCore.alfheimTab)
																																.setHardness(2F)
																																.setResistance(5F)
																																.setStepSound(soundTypeWood)
		
		dreamwoodBarkFenceGate = BlockModFenceGate(ModBlocks.dreamwood, 0)	.setBlockName("DreamwoodBarkFenceGate")
																			.setCreativeTab(AlfheimCore.alfheimTab)
																			.setHardness(2F)
																			.setResistance(5F)
																			.setStepSound(soundTypeWood)
		
		dreamwoodBarkFence = BlockModFence(LibResources.PREFIX_MOD + "dreamwood0", Material.wood, dreamwoodBarkFenceGate)	.setBlockName("DreamwoodBarkFence")
																															.setCreativeTab(AlfheimCore.alfheimTab)
																															.setHardness(2F)
																															.setResistance(5F)
																															.setStepSound(soundTypeWood)
		
		livingwoodFenceGate = BlockModFenceGate(ModBlocks.livingwood, 1)	.setBlockName("LivingwoodFenceGate")
																			.setCreativeTab(AlfheimCore.alfheimTab)
																			.setHardness(2F)
																			.setResistance(5F)
																			.setStepSound(soundTypeWood)
		
		livingwoodFence = BlockModFence(LibResources.PREFIX_MOD + "livingwood1", Material.wood, livingwoodFenceGate)	.setBlockName("LivingwoodFence")
																														.setCreativeTab(AlfheimCore.alfheimTab)
																														.setHardness(2F)
																														.setResistance(5F)
																														.setStepSound(soundTypeWood)
		
		dreamwoodFenceGate = BlockModFenceGate(ModBlocks.dreamwood, 1)	.setBlockName("DreamwoodFenceGate")
																		.setCreativeTab(AlfheimCore.alfheimTab)
																		.setHardness(2F)
																		.setResistance(5F)
																		.setStepSound(soundTypeWood)
		
		dreamwoodFence = BlockModFence(LibResources.PREFIX_MOD + "dreamwood1", Material.wood, dreamwoodFenceGate)	.setBlockName("DreamwoodFence")
																													.setCreativeTab(AlfheimCore.alfheimTab)
																													.setHardness(2F)
																													.setResistance(5F)
																													.setStepSound(soundTypeWood)
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
		
		register(livingwoodBarkFence)
		register(livingwoodFence)
		register(dreamwoodBarkFence)
		register(dreamwoodFence)
		register(livingwoodBarkFenceGate)
		register(livingwoodFenceGate)
		register(dreamwoodBarkFenceGate)
		register(dreamwoodFenceGate)
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
