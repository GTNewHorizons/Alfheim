package alfheim.common.block

import alexsocol.asjlib.ASJUtilities.setBurnable
import alexsocol.asjlib.extendables.block.BlockModMeta
import alfheim.api.*
import alfheim.api.lib.LibOreDict
import alfheim.common.block.alt.*
import alfheim.common.block.base.*
import alfheim.common.block.colored.*
import alfheim.common.block.colored.rainbow.*
import alfheim.common.block.magtrees.calico.*
import alfheim.common.block.magtrees.circuit.*
import alfheim.common.block.magtrees.lightning.*
import alfheim.common.block.magtrees.nether.*
import alfheim.common.block.magtrees.sealing.*
import alfheim.common.block.mana.*
import alfheim.common.block.schema.*
import alfheim.common.block.tile.*
import alfheim.common.block.tile.sub.flower.*
import alfheim.common.core.util.AlfheimTab
import alfheim.common.lexicon.AlfheimLexiconData
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.item.ItemStack
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.*
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.oredict.OreDictionary.registerOre
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.common.block.*

object AlfheimBlocks {
	
	val alfheimPortal: Block
	val alfheimPylon: Block
	val alfStorage: Block
	val amplifier: Block
	val animatedTorch: Block
	val anomaly: Block
	val anomalyHarvester: Block
	val anyavil: Block
	val auroraDirt: Block
	val auroraLeaves: Block
	val auroraPlanks: Block
	val auroraSlab: Block
	val auroraSlabFull: Block
	val auroraStairs: Block
	val auroraWood: Block
	val barrier: Block
	val dreamSapling: Block
	val elvenOres: Block
	val elvenSand: Block
	val flugelHeadBlock: Block
	val flugelHead2Block: Block
	val itemDisplay: Block
	val invisibleFlame: Block
	val irisDirt: Block
	val irisGrass: Block
	val irisLamp: Block
	val irisLeaves0: Block
	val irisLeaves1: Block
	val irisPlanks: Block
	val irisSapling: Block
	val irisSlabs: Array<Block>
	val irisSlabsFull: Array<Block>
	val irisStairs: Array<Block>
	val irisTallGrass0: Block
	val irisTallGrass1: Block
	val irisWood0: Block
	val irisWood1: Block
	val irisWood2: Block
	val irisWood3: Block
	val livingcobble: Block
	val livingwoodFunnel: Block
	val kindling: Block
	val manaAccelerator: Block
	val manaInfuser: Block
	val poisonIce: Block
	val powerStone: Block
	val raceSelector: Block
	val rainbowDirt: Block
	val rainbowFlame: Block
	val rainbowGrass: Block
	val rainbowLeaves: Block
	val rainbowMushroom: Block
	val rainbowPetalBlock: Block
	val rainbowPlanks: Block
	val rainbowSlab: Block
	val rainbowSlabFull: Block
	val rainbowStairs: Block
	val rainbowTallGrass: Block
	val rainbowTallFlower: Block
	val rainbowWood: Block
	val redFlame: Block
	val schemaAnnihilator: Block
	val schemaController: Block
	val schemaFiller: Block
	val schemaGenerator: Block
	val schemaMarker: Block
	val shimmerQuartz: Block
	val shimmerQuartzSlab: Block
	val shimmerQuartzSlabFull: Block
	val shimmerQuartzStairs: Block
	val snowGrass: Block
	val snowLayer: Block
	val starBlock: Block
	val starBlock2: Block
	val tradePortal: Block
	//val transferer: Block BACK
	val treeCrafterBlock: Block
	val treeCrafterBlockRB: Block
	val treeCrafterBlockAU: Block
	
	// DENDROLOGY
	
	val altLeaves: Block
	val altPlanks: Block
	val altSlabs: Block
	val altSlabsFull: Block
	val altStairs: Array<Block>
	val altWood0: Block
	val altWood1: Block
	
	val calicoLeaves: Block
	val calicoPlanks: Block
	val calicoSapling: Block
	val calicoSlabs: Block
	val calicoSlabsFull: Block
	val calicoStairs: Block
	val calicoWood: Block
	
	val circuitLeaves: Block
	val circuitPlanks: Block
	val circuitSapling: Block
	val circuitSlabs: Block
	val circuitSlabsFull: Block
	val circuitStairs: Block
	val circuitWood: Block
	
	val lightningLeaves: Block
	val lightningPlanks: Block
	val lightningSapling: Block
	val lightningSlabs: Block
	val lightningSlabsFull: Block
	val lightningStairs: Block
	val lightningWood: Block
	
	val netherLeaves: Block
	val netherPlanks: Block
	val netherSapling: Block
	val netherSlabs: Block
	val netherSlabsFull: Block
	val netherStairs: Block
	val netherWood: Block
	
	val sealingLeaves: Block
	val sealingPlanks: Block
	val sealingSapling: Block
	val sealingSlabs: Block
	val sealingSlabsFull: Block
	val sealingStairs: Block
	val sealingWood: Block
	
	init {
		alfheimPortal = BlockAlfheimPortal()
		alfheimPylon = BlockAlfheimPylon()
		alfStorage = object: BlockModMeta(Material.iron, 2, ModInfo.MODID, "alfStorage", AlfheimTab, 5f, resist = 60f) {
			override fun isBeaconBase(worldObj: IBlockAccess?, x: Int, y: Int, z: Int, beaconX: Int, beaconY: Int, beaconZ: Int) = true
		}
		amplifier = BlockAmplifier()
		animatedTorch = BlockAnimatedTorch()
		anomaly = BlockAnomaly()
		anomalyHarvester = BlockAnomalyHarvester()
		anyavil = BlockAnyavil()
		auroraDirt = BlockAuroraDirt()
		auroraLeaves = BlockAuroraLeaves()
		auroraPlanks = BlockAuroraPlanks()
		auroraSlab = BlockAuroraWoodSlab(false)
		auroraSlabFull = BlockAuroraWoodSlab(true)
		auroraSlab.register()
		auroraSlabFull.register()
		auroraStairs = BlockAuroraWoodStairs()
		auroraWood = BlockAuroraWood()
		barrier = BlockBarrier()
		dreamSapling = BlockDreamSapling()
		elvenOres = BlockElvenOres()
		elvenSand = object: BlockPatternLexicon(ModInfo.MODID, Material.sand, "ElvenSand", AlfheimTab, harvTool = "shovel", harvLvl = 0, isFalling = true, entry = AlfheimLexiconData.worldgen) {
			override fun canSustainPlant(world: IBlockAccess, x: Int, y: Int, z: Int, direction: ForgeDirection?, plantable: IPlantable) = when (plantable.getPlantType(world, x, y, z)) {
				EnumPlantType.Desert -> true
				EnumPlantType.Beach -> world.getBlock(x - 1, y, z).material === Material.water || world.getBlock(x + 1, y, z).material === Material.water || world.getBlock(x, y, z - 1).material === Material.water || world.getBlock(x, y, z + 1).material === Material.water
				else -> super.canSustainPlant(world, x, y, z, direction, plantable)
			}
		}
		flugelHeadBlock = BlockHeadFlugel()
		flugelHead2Block = BlockHeadMiku()
		itemDisplay = BlockItemDisplay()
		invisibleFlame = BlockManaFlame("invisibleFlame", TileInvisibleManaFlame::class.java)
		irisDirt = BlockColoredDirt()
		irisLamp = BlockColoredLamp()
		irisLeaves0 = BlockColoredLeaves(0)
		irisLeaves1 = BlockColoredLeaves(1)
		irisGrass = BlockColoredGrass()
		irisPlanks = BlockColoredPlanks()
		irisSapling = BlockColoredSapling()
		irisSlabs = Array(16) { BlockColoredWoodSlab(false, it) }
		irisSlabsFull = Array(16) { BlockColoredWoodSlab(true, it) }
		irisSlabs.forEach { (it as BlockSlabMod).register() }
		irisSlabsFull.forEach { (it as BlockSlabMod).register() }
		irisStairs = Array(16) { BlockColoredWoodStairs(it) }
		irisTallGrass0 = BlockColoredDoubleGrass(0)
		irisTallGrass1 = BlockColoredDoubleGrass(1)
		irisWood0 = BlockColoredWood(0)
		irisWood1 = BlockColoredWood(1)
		irisWood2 = BlockColoredWood(2)
		irisWood3 = BlockColoredWood(3)
		kindling = BlockKindling()
		livingcobble = BlockModMeta(Material.rock, 3, ModInfo.MODID, "LivingCobble", AlfheimTab, 2f, resist = 60f)
		livingwoodFunnel = BlockFunnel()
		manaAccelerator = BlockManaAccelerator()
		manaInfuser = BlockManaInfuser()
		poisonIce = BlockPoisonIce()
		powerStone = BlockPowerStone()
		raceSelector = BlockRaceSelector()
		rainbowDirt = BlockRainbowDirt()
		rainbowFlame = BlockManaFlame("rainbowFlame", TileRainbowManaFlame::class.java)
		rainbowLeaves = BlockRainbowLeaves()
		rainbowGrass = BlockRainbowGrass()
		rainbowMushroom = BlockRainbowMushroom()
		rainbowPetalBlock = BlockRainbowPetalBlock()
		rainbowPlanks = BlockRainbowPlanks()
		rainbowSlab = BlockRainbowWoodSlab(false)
		rainbowSlabFull = BlockRainbowWoodSlab(true)
		rainbowSlab.register()
		rainbowSlabFull.register()
		rainbowStairs = BlockRainbowWoodStairs()
		rainbowTallGrass = BlockRainbowDoubleGrass()
		rainbowTallFlower = BlockRainbowDoubleFlower()
		rainbowWood = BlockRainbowWood()
		redFlame = BlockRedFlame()
		schemaAnnihilator = BlockSchemaAnnihilator()
		schemaController = BlockSchemaContoller()
		schemaFiller = BlockSchemaFiller()
		schemaGenerator = BlockSchemaGenerator()
		schemaMarker = BlockShemaMarker()
		shimmerQuartz = BlockShimmerQuartz()
		shimmerQuartzSlab = BlockShimmerQuartzSlab(shimmerQuartz, false)
		shimmerQuartzSlabFull = BlockShimmerQuartzSlab(shimmerQuartz, true)
		shimmerQuartzSlab.register()
		shimmerQuartzSlabFull.register()
		shimmerQuartzStairs = BlockShimmerQuartzStairs(shimmerQuartz)
		snowGrass = BlockSnowGrass()
		snowLayer = BlockSnowLayer()
		starBlock = BlockStar()
		starBlock2 = BlockCracklingStar()
		tradePortal = BlockTradePortal()
		//transferer = BlockTransferer() BACK
		treeCrafterBlock = BlockTreeCrafter("treeCrafter", irisPlanks)
		treeCrafterBlockRB = BlockTreeCrafter("treeCrafterRB", rainbowPlanks)
		treeCrafterBlockAU = BlockTreeCrafter("treeCrafterAU", auroraPlanks)
		
		// DENDOROLOGY
		
		altLeaves = BlockAltLeaves()
		altPlanks = BlockAltPlanks()
		altSlabs = BlockAltWoodSlab(false)
		altSlabsFull = BlockAltWoodSlab(true)
		(altSlabs as BlockSlabMod).register()
		(altSlabsFull as BlockSlabMod).register()
		altStairs = Array(LibOreDict.ALT_TYPES.size - 1) { if (it == BlockAltLeaves.yggMeta) BlockYggStairs() else BlockAltWoodStairs(it) }
		altWood0 = BlockAltWood(0)
		altWood1 = BlockAltWood(1)
		
		calicoLeaves = BlockCalicoLeaves()
		calicoPlanks = BlockCalicoPlanks()
		calicoSapling = BlockCalicoSapling()
		calicoSlabs = BlockCalicoWoodSlab(false)
		calicoSlabsFull = BlockCalicoWoodSlab(true)
		calicoSlabs.register()
		calicoSlabsFull.register()
		calicoStairs = BlockCalicoWoodStairs()
		calicoWood = BlockCalicoWood()
		
		circuitLeaves = BlockCircuitLeaves()
		circuitPlanks = BlockCircuitPlanks()
		circuitSapling = BlockCircuitSapling()
		circuitSlabs = BlockCircuitWoodSlab(false)
		circuitSlabsFull = BlockCircuitWoodSlab(true)
		circuitSlabs.register()
		circuitSlabsFull.register()
		circuitStairs = BlockCircuitWoodStairs()
		circuitWood = BlockCircuitWood()
		
		lightningLeaves = BlockLightningLeaves()
		lightningPlanks = BlockLightningPlanks()
		lightningSapling = BlockLightningSapling()
		lightningSlabs = BlockLightningWoodSlab(false)
		lightningSlabsFull = BlockLightningWoodSlab(true)
		lightningSlabs.register()
		lightningSlabsFull.register()
		lightningStairs = BlockLightningWoodStairs()
		lightningWood = BlockLightningWood()
		
		netherLeaves = BlockNetherLeaves()
		netherPlanks = BlockNetherPlanks()
		netherSapling = BlockNetherSapling()
		netherSlabs = BlockNetherWoodSlab(false)
		netherSlabsFull = BlockNetherWoodSlab(true)
		netherSlabs.register()
		netherSlabsFull.register()
		netherStairs = BlockNetherWoodStairs()
		netherWood = BlockNetherWood()
		
		sealingLeaves = BlockSealingLeaves()
		sealingPlanks = BlockSealingPlanks()
		sealingSapling = BlockSealingSapling()
		sealingSlabs = BlockSealingWoodSlab(false)
		sealingSlabsFull = BlockSealingWoodSlab(true)
		sealingSlabs.register()
		sealingSlabsFull.register()
		sealingStairs = BlockSealingWoodStairs()
		sealingWood = BlockSealingWood()
		
		registerBurnables()
		registerFlora()
	}
	
	fun regOreDict() {
		registerOre(LibOreDict.DRAGON_ORE, ItemStack(elvenOres))
		registerOre(LibOreDict.ELEMENTIUM_ORE, ItemStack(elvenOres, 1, 1))
		registerOre(LibOreDict.ELVEN_QUARTZ_ORE, ItemStack(elvenOres, 1, 2))
		registerOre(LibOreDict.GOLD_ORE, ItemStack(elvenOres, 1, 3))
		registerOre(LibOreDict.IFFESAL_ORE, ItemStack(elvenOres, 1, 4))
		
		val quartzs = arrayOf(ModFluffBlocks.darkQuartz, ModFluffBlocks.manaQuartz, ModFluffBlocks.blazeQuartz, ModFluffBlocks.lavenderQuartz, ModFluffBlocks.redQuartz, ModFluffBlocks.elfQuartz, ModFluffBlocks.sunnyQuartz)
		
		vazkii.botania.common.lib.LibOreDict.QUARTZ.forEachIndexed { id, it ->
			registerOre("block${it.capitalize()}", ItemStack(quartzs[id]))
		}
		registerOre(LibOreDict.RAINBOW_QUARTZ_BLOCK, ItemStack(shimmerQuartz))
		
		registerOre("sand", ItemStack(elvenSand))
		
		registerOre(LibOreDict.DREAM_WOOD_LOG, ItemStack(altWood1, 1, 3))
		
		// ################
		
		registerOre(LibOreDict.RAINBOW_FLOWER, ItemStack(rainbowGrass, 1, 2))
		registerOre(LibOreDict.RAINBOW_DOUBLE_FLOWER, ItemStack(rainbowTallFlower))
		
		registerOre(LibOreDict.MUSHROOM, ItemStack(ModBlocks.mushroom, 1, OreDictionary.WILDCARD_VALUE))
		registerOre(LibOreDict.MUSHROOM, ItemStack(rainbowMushroom))
		
		registerOre("treeSapling", irisSapling)
		
		var t: ItemStack
		
		registerOre(LibOreDict.WOOD[16], ItemStack(rainbowWood))
		registerOre(LibOreDict.WOOD[17], ItemStack(auroraWood))
		
		registerOre(LibOreDict.DIRT[16], ItemStack(rainbowDirt))
		registerOre(LibOreDict.IRIS_DIRT, ItemStack(rainbowDirt))
		registerOre(LibOreDict.DIRT[17], ItemStack(auroraDirt))
		registerOre(LibOreDict.IRIS_DIRT, ItemStack(auroraDirt))
		
		registerOre("treeLeaves", ItemStack(lightningLeaves))
		registerOre("plankWood", ItemStack(lightningPlanks))
		registerOre("treeSapling", ItemStack(lightningSapling))
		
		registerOre("slabWood", ItemStack(lightningSlabs))
		registerOre("stairWood", ItemStack(lightningStairs))
		
		registerOre("treeLeaves", ItemStack(calicoLeaves))
		registerOre("plankWood", ItemStack(calicoPlanks))
		registerOre("treeSapling", ItemStack(calicoSapling))
		
		registerOre("slabWood", ItemStack(calicoSlabs))
		registerOre("stairWood", ItemStack(calicoStairs))
		
		registerOre("treeLeaves", ItemStack(circuitLeaves))
		registerOre("plankWood", ItemStack(circuitPlanks))
		registerOre("treeSapling", ItemStack(circuitSapling))
		
		registerOre("slabWood", ItemStack(circuitSlabs))
		registerOre("stairWood", ItemStack(circuitStairs))
		
		registerOre("treeLeaves", ItemStack(netherLeaves))
		registerOre("plankWood", ItemStack(netherPlanks))
		registerOre("treeSapling", ItemStack(netherSapling))
		
		registerOre("slabWood", ItemStack(netherSlabs))
		registerOre("stairWood", ItemStack(netherStairs))
		
		registerOre("treeLeaves", ItemStack(sealingLeaves))
		registerOre("plankWood", ItemStack(sealingPlanks))
		registerOre("treeSapling", ItemStack(sealingSapling))
		
		registerOre("slabWood", ItemStack(sealingSlabs))
		registerOre("stairWood", ItemStack(sealingStairs))
		
		
		for (i in 0..3) {
			registerOre(LibOreDict.WOOD[i], ItemStack(irisWood0, 1, i))
			
			registerOre(LibOreDict.WOOD[i + 4], ItemStack(irisWood1, 1, i))
			
			registerOre(LibOreDict.WOOD[i + 8], ItemStack(irisWood2, 1, i))
			
			registerOre(LibOreDict.WOOD[i + 12], ItemStack(irisWood3, 1, i))
		}
		
		for (i in 0..7) {
			registerOre(LibOreDict.LEAVES[i], ItemStack(irisLeaves0, 1, i))
			registerOre(LibOreDict.LEAVES[i + 8], ItemStack(irisLeaves1, 1, i))
		}
		
		for (i in 0..5) {
			registerOre("stairWood", ItemStack(altStairs[i], 1))
			
			registerOre("treeLeaves", ItemStack(altLeaves, 1, i))
		}
		
		for (i in 0 until LibOreDict.ALT_TYPES.size - 1) {
			registerOre("slabWood", ItemStack(altSlabs, 1, i))
			
			registerOre("slabWood", ItemStack(altSlabsFull, 1, i))
		}
		
		for (i in 0..15) {
			registerOre(LibOreDict.IRIS_DIRT, ItemStack(irisDirt, 1, i))
			registerOre(LibOreDict.DIRT[i], ItemStack(irisDirt, 1, i))
			
			registerOre("logWood", ItemStack(lightningWood, 1, i))
			registerOre("logWood", ItemStack(netherWood, 1, i))
			registerOre("logWood", ItemStack(sealingWood, 1, i))
			registerOre("logWood", ItemStack(calicoWood, 1, i))
			registerOre("logWood", ItemStack(circuitWood, 1, i))
			
			t = ItemStack(rainbowWood, 1, i)
			registerOre("logWood", t)
			registerOre(LibOreDict.IRIS_WOOD, t)
			
			t = ItemStack(irisWood0, 1, i)
			registerOre("logWood", t)
			registerOre(LibOreDict.IRIS_WOOD, t)
			
			t = ItemStack(irisWood1, 1, i)
			registerOre("logWood", t)
			registerOre(LibOreDict.IRIS_WOOD, t)
			
			t = ItemStack(irisWood2, 1, i)
			registerOre("logWood", t)
			registerOre(LibOreDict.IRIS_WOOD, t)
			
			t = ItemStack(irisWood3, 1, i)
			registerOre("logWood", t)
			registerOre(LibOreDict.IRIS_WOOD, t)
			
			t = ItemStack(altWood0, 1, i)
			registerOre("logWood", t)
			
			t = ItemStack(altWood1, 1, i)
			registerOre("logWood", t)
			
			t = ItemStack(irisLeaves0, 1, i)
			registerOre("treeLeaves", t)
			registerOre(LibOreDict.IRIS_LEAVES, t)
			
			t = ItemStack(irisLeaves1, 1, i)
			registerOre("treeLeaves", t)
			registerOre(LibOreDict.IRIS_LEAVES, t)
			
			t = ItemStack(irisPlanks, 1, i)
			registerOre("plankWood", t)
			
			t = ItemStack(altPlanks, 1, i)
			registerOre("plankWood", t)
			
			t = ItemStack(irisStairs[i], 1)
			registerOre("stairWood", t)
			
			t = ItemStack(irisSlabs[i], 1)
			registerOre("slabWood", t)
			
			t = ItemStack(irisSlabsFull[i], 1)
			registerOre("slabWood", t)
		}
		
		t = ItemStack(rainbowLeaves)
		registerOre("treeLeaves", t)
		registerOre(LibOreDict.IRIS_LEAVES, t)
		registerOre(LibOreDict.LEAVES[16], t)
		
		t = ItemStack(auroraLeaves)
		registerOre("treeLeaves", t)
		registerOre(LibOreDict.IRIS_LEAVES, t)
		registerOre(LibOreDict.LEAVES[17], t)
		
		t = ItemStack(rainbowPlanks)
		registerOre("plankWood", t)
		
		t = ItemStack(rainbowStairs)
		registerOre("stairWood", t)
		
		t = ItemStack(rainbowSlab)
		registerOre("slabWood", t)
	}
	
	fun registerBurnables() {
		setBurnable(altLeaves, 30, 60)
		setBurnable(altPlanks, 5, 20)
		setBurnable(altSlabs, 5, 20)
		setBurnable(altSlabsFull, 5, 20)
		altStairs.forEach { setBurnable(it, 5, 20) }
		setBurnable(altWood0, 5, 5)
		setBurnable(altWood1, 5, 5)
		
		setBurnable(amplifier, 5, 20)
		
		setBurnable(auroraPlanks, 5, 20)
		setBurnable(auroraSlab, 5, 20)
		setBurnable(auroraSlabFull, 5, 20)
		setBurnable(auroraStairs, 5, 20)
		setBurnable(auroraWood, 5, 5)
		
		setBurnable(calicoLeaves, 30, 60)
		setBurnable(calicoPlanks, 5, 20)
		setBurnable(calicoSlabs, 5, 20)
		setBurnable(calicoSlabsFull, 5, 20)
		setBurnable(calicoStairs, 5, 20)
		setBurnable(calicoWood, 5, 5)
		
		setBurnable(circuitLeaves, 30, 60)
		setBurnable(circuitPlanks, 5, 20)
		setBurnable(circuitSlabs, 5, 20)
		setBurnable(circuitSlabsFull, 5, 20)
		setBurnable(circuitStairs, 5, 20)
		setBurnable(circuitWood, 5, 5)
		
		setBurnable(irisGrass, 60, 100)
		setBurnable(irisLeaves0, 30, 60)
		setBurnable(irisLeaves1, 30, 60)
		setBurnable(irisPlanks, 5, 20)
		irisSlabs.forEach { setBurnable(it, 5, 20) }
		irisSlabsFull.forEach { setBurnable(it, 5, 20) }
		irisStairs.forEach { setBurnable(it, 5, 20) }
		setBurnable(irisTallGrass0, 60, 100)
		setBurnable(irisTallGrass1, 60, 100)
		setBurnable(irisWood0, 5, 5)
		setBurnable(irisWood1, 5, 5)
		setBurnable(irisWood2, 5, 5)
		setBurnable(irisWood3, 5, 5)
		
		setBurnable(lightningLeaves, 30, 60)
		setBurnable(lightningPlanks, 5, 20)
		setBurnable(lightningSlabs, 5, 20)
		setBurnable(lightningSlabsFull, 5, 20)
		setBurnable(lightningStairs, 5, 20)
		setBurnable(lightningWood, 5, 5)
		
		setBurnable(rainbowGrass, 60, 100)
		setBurnable(rainbowLeaves, 30, 60)
		setBurnable(rainbowPlanks, 5, 20)
		setBurnable(rainbowSlab, 5, 20)
		setBurnable(rainbowSlabFull, 5, 20)
		setBurnable(rainbowStairs, 5, 20)
		setBurnable(rainbowTallGrass, 60, 100)
		setBurnable(rainbowWood, 5, 5)
		
		setBurnable(sealingLeaves, 30, 60)
		setBurnable(sealingPlanks, 5, 20)
		setBurnable(sealingSlabs, 5, 20)
		setBurnable(sealingSlabsFull, 5, 20)
		setBurnable(sealingStairs, 5, 20)
		setBurnable(sealingWood, 5, 5)
	}
	
	fun registerFlora() {
		BotaniaAPI.registerSubTile("crysanthermum", SubTileCrysanthermum::class.java)
		BotaniaAPI.registerSubTileSignature(SubTileCrysanthermum::class.java, ShadowFoxSignature("crysanthermum"))
		
		BotaniaAPI.subTileMods["crysanthermum"] = "Iridescense"
		BotaniaAPI.addSubTileToCreativeMenu("crysanthermum")
		
		ShadowFoxAPI.addTreeVariant(irisDirt, irisWood0, irisLeaves0, 0, 3)
		ShadowFoxAPI.addTreeVariant(irisDirt, irisWood1, irisLeaves0, 4, 7)
		ShadowFoxAPI.addTreeVariant(irisDirt, irisWood2, irisLeaves1, 8, 11, 8)
		ShadowFoxAPI.addTreeVariant(irisDirt, irisWood3, irisLeaves1, 12, 15, 8)
		ShadowFoxAPI.addTreeVariant(rainbowDirt, rainbowWood, rainbowLeaves)
		ShadowFoxAPI.addTreeVariant(auroraDirt, auroraWood, auroraLeaves)
		ShadowFoxAPI.addTreeVariant(ModBlocks.altGrass, altWood0, altLeaves, 0, 3)
		ShadowFoxAPI.addTreeVariant(ModBlocks.altGrass, altWood1, altLeaves, 4, 5)
	}
	
	fun Block.setHarvestLevelI(toolClass: String, level: Int) = also { it.setHarvestLevel(toolClass, level) }
}
