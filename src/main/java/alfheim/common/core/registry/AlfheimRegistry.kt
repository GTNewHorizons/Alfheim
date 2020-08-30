package alfheim.common.core.registry

import alexsocol.asjlib.ASJUtilities.registerEntity
import alfheim.AlfheimCore
import alfheim.api.AlfheimAPI.addPink
import alfheim.api.AlfheimAPI.registerAnomaly
import alfheim.api.AlfheimAPI.registerSpell
import alfheim.api.ModInfo
import alfheim.common.block.*
import alfheim.common.block.tile.*
import alfheim.common.block.tile.corporea.*
import alfheim.common.block.tile.sub.anomaly.*
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.entity.*
import alfheim.common.entity.boss.*
import alfheim.common.entity.item.*
import alfheim.common.entity.spell.*
import alfheim.common.item.AlfheimItems
import alfheim.common.item.material.ElvenResourcesMetas
import alfheim.common.potion.*
import alfheim.common.spell.darkness.*
import alfheim.common.spell.earth.*
import alfheim.common.spell.fire.*
import alfheim.common.spell.illusion.*
import alfheim.common.spell.nature.*
import alfheim.common.spell.sound.*
import alfheim.common.spell.tech.*
import alfheim.common.spell.water.*
import alfheim.common.spell.wind.*
import alfheim.common.world.dim.alfheim.customgens.WorldGenAlfheim
import cpw.mods.fml.common.IWorldGenerator
import cpw.mods.fml.common.registry.EntityRegistry
import cpw.mods.fml.common.registry.GameRegistry.*
import net.minecraft.entity.EnumCreatureType
import net.minecraft.init.*
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.biome.BiomeGenBase
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.common.Botania
import vazkii.botania.common.block.*
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.item.block.ItemBlockSpecialFlower
import vazkii.botania.common.lib.LibBlockNames

// TODO decentralize
object AlfheimRegistry {
	
	private lateinit var worldGen: IWorldGenerator
	
	fun preInit() {
		registerPotions()
		registerEntities()
		registerTileEntities()
		
		worldGen = WorldGenAlfheim()
	}
	
	fun init() {
		registerWorldGenerator(worldGen, 1)
		registerSpells()
		loadAllPinkStuff()
	}
	
	fun postInit() {
		if (AlfheimConfigHandler.looniumOverseed)
			BotaniaAPI.looniumBlacklist.remove(ModItems.overgrowthSeed)
		
		val (w, n, x) = AlfheimConfigHandler.voidCreeper
		
		for (i in BiomeGenBase.getBiomeGenArray()) {
			if (i != null && !AlfheimConfigHandler.voidCreepBiomeBlackList.contains(i.biomeID))
				EntityRegistry.addSpawn(EntityVoidCreeper::class.java, w, n, x, EnumCreatureType.monster, i)
		}
	}
	
	fun registerPotions() {
		PotionBerserk()
		PotionBleeding()
		PotionButterShield()
		PotionDeathMark()
		PotionAlfheim(AlfheimConfigHandler.potionIDDecay, "decay", true, 0x553355)
		PotionEternity()
		PotionGoldRush()
		PotionAlfheim(AlfheimConfigHandler.potionIDIceLens, "icelens", false, 0xDDFFFF)
		PotionLeftFlame()
		PotionLightningShield()
		PotionManaVoid()
		PotionAlfheim(AlfheimConfigHandler.potionIDNineLifes, "nineLifes", false, 0xDD2222)
		PotionNinja()
		PotionNoclip()
		PotionAlfheim(AlfheimConfigHandler.potionIDOvermage, "overmage", false, 0x88FFFF)
		PotionAlfheim(AlfheimConfigHandler.potionIDPossession, "possession", true, 0xCC0000)
		PotionQuadDamage()
		PotionSacrifice()
		PotionShowMana()
		PotionSoulburn()
		PotionAlfheim(AlfheimConfigHandler.potionIDStoneSkin, "stoneSkin", false, 0x593C1F)
		PotionTank()
		PotionThrow()
		PotionWellOLife()
	}
	
	var id = 0
		get() = field.also { field++ }
	
	private fun registerEntities() {
		registerEntity(EntityAlfheimPixie::class.java, "Pixie", AlfheimCore.instance, id)
		registerEntity(EntityButterfly::class.java, "Butterfly", AlfheimCore.instance, id)
		registerEntity(EntityCharge::class.java, "Charge", AlfheimCore.instance, id)
		registerEntity(EntityElf::class.java, "Elf", AlfheimCore.instance, id)
		registerEntity(EntityFireAura::class.java, "FireAura", AlfheimCore.instance, id)
		registerEntity(EntityFlugel::class.java, "Flugel", AlfheimCore.instance, id)
		registerEntity(EntityItemImmortal::class.java, "ImmortalItem", AlfheimCore.instance, id)
		registerEntity(EntityItemImmortalRelic::class.java, "ImmortalRelicItem", AlfheimCore.instance, id)
		registerEntity(EntityLightningMark::class.java, "LightningMark", AlfheimCore.instance, id)
		registerEntity(EntityLolicorn::class.java, "Lolicorn", AlfheimCore.instance, id)
		registerEntity(EntityRook::class.java, "Rook", AlfheimCore.instance, id)
		
		registerEntity(EntityGrieferCreeper::class.java, "GrieferCreeper", AlfheimCore.instance, id)
		registerEntity(EntityVoidCreeper::class.java, "VoidCreeper", AlfheimCore.instance, id)
		
		registerEntity(EntityThrowableItem::class.java, "ThrownItem", AlfheimCore.instance, id)
		registerEntity(EntityThrownPotion::class.java, "ThrownPotion", AlfheimCore.instance, id)
		
		registerEntity(EntityGleipnir::class.java, "Gleipnir", AlfheimCore.instance, id)
		registerEntity(EntityMjolnir::class.java, "Mjolnir", AlfheimCore.instance, id)
		
		registerEntity(EntityMagicArrow::class.java, "MagicArrow", AlfheimCore.instance, id)
		registerEntity(EntitySubspace::class.java, "Subspace", AlfheimCore.instance, id)
		registerEntity(EntitySubspaceSpear::class.java, "SubspaceSpear", AlfheimCore.instance, id)
		registerEntity(FakeLightning::class.java, "FakeLightning", AlfheimCore.instance, id)
		
		registerEntity(EntitySpellAcidMyst::class.java, "SpellAcidMyst", AlfheimCore.instance, id)
		registerEntity(EntitySpellAquaStream::class.java, "SpellAquaStream", AlfheimCore.instance, id)
		registerEntity(EntitySpellDriftingMine::class.java, "SpellDriftingMine", AlfheimCore.instance, id)
		registerEntity(EntitySpellFenrirStorm::class.java, "SpellFenrirStorm", AlfheimCore.instance, id)
		registerEntity(EntitySpellFireball::class.java, "SpellFireball", AlfheimCore.instance, id)
		registerEntity(EntitySpellFirewall::class.java, "SpellFirewall", AlfheimCore.instance, id)
		registerEntity(EntitySpellGravityTrap::class.java, "SpellGravityTrap", AlfheimCore.instance, id)
		registerEntity(EntitySpellHarp::class.java, "SpellHarp", AlfheimCore.instance, id)
		registerEntity(EntitySpellIsaacMissile::class.java, "SpellIsaacMissile", AlfheimCore.instance, id)
		registerEntity(EntitySpellMortar::class.java, "SpellMortar", AlfheimCore.instance, id)
		registerEntity(EntitySpellWindBlade::class.java, "SpellWindBlade", AlfheimCore.instance, id)
	}
	
	private fun registerTileEntities() {
		registerTile(TileAlfheimPortal::class.java, "AlfheimPortal")
		registerTile(TileAlfheimPylon::class.java, "AlfheimPylon")
		registerTile(TileAnimatedTorch::class.java, "AnimatedTorch")
		registerTile(TileAnomaly::class.java, "Anomaly")
		registerTile(TileAnomalyHarvester::class.java, "AnomalyHarvester")
		registerTile(TileAnyavil::class.java, "Anyavil")
		registerTile(TileBarrel::class.java, "Barrel")
		registerTile(TileCorporeaAutocrafter::class.java, "CorporeaAutocrafter")
		registerTile(TileCorporeaInjector::class.java, "CorporeaInjector")
		registerTile(TileEnderActuator::class.java, "EnderActuator")
		registerTile(TileHeadFlugel::class.java, "HeadFlugel")
		registerTile(TileHeadMiku::class.java, "HeadMiku")
		registerTile(TileManaAccelerator::class.java, "ItemHolder")
		registerTile(TileManaInfuser::class.java, "ManaInfuser")
		registerTile(TilePowerStone::class.java, "PowerStone")
		registerTile(TileRaceSelector::class.java, "RaceSelector")
		registerTile(TileTradePortal::class.java, "TradePortal")
		
		registerAnomalies()
		
		registerTile(TileCracklingStar::class.java, "StarPlacer2")
		registerTile(TileEntityStar::class.java, "StarPlacer")
		registerTile(TileInvisibleManaFlame::class.java, "ManaInvisibleFlame")
		registerTile(TileItemDisplay::class.java, "ItemDisplay")
		registerTile(TileLightningRod::class.java, "RodLightning")
		registerTile(TileLivingwoodFunnel::class.java, "LivingwoodFunnel")
		registerTile(TileRainbowManaFlame::class.java, "ManaRainbowFlame")
		registerTile(TileSchemaController::class.java, "SchemaController")
		registerTile(TileSchemaAnnihilator::class.java, "SchemaAnnihilator")
		registerTile(TileTreeCook::class.java, "TreeCook")
		registerTile(TileTreeCrafter::class.java, "TreeCrafter")
	}
	
	private fun registerTile(tileEntityClass: Class<out TileEntity>, id: String) {
		registerTileEntity(tileEntityClass, "${ModInfo.MODID}:$id")
	}
	
	private fun registerAnomalies() {
		registerAnomaly("Antigrav", SubTileAntigrav::class.java)
		registerAnomaly("Gravity", SubTileGravity::class.java)
		registerAnomaly("Lightning", SubTileLightning::class.java)
		registerAnomaly("ManaTornado", SubTileManaTornado::class.java)
		registerAnomaly("ManaVoid", SubTileManaVoid::class.java)
		registerAnomaly("SpeedUp", SubTileSpeedUp::class.java)
		registerAnomaly("Warp", SubTileWarp::class.java)
	}
	
	private fun registerSpells() {
		registerSpell(SpellAcidMyst)
		registerSpell(SpellAquaBind)
		registerSpell(SpellAquaStream)
		registerSpell(SpellBattleHorn)
		registerSpell(SpellBlink)
		registerSpell(SpellBunnyHop)
		registerSpell(SpellButterflyShield)
		registerSpell(SpellCall)
		registerSpell(SpellConfusion)
		registerSpell(SpellDay)
		registerSpell(SpellDeathMark)
		registerSpell(SpellDecay)
		registerSpell(SpellDispel)
		registerSpell(SpellDriftingMine)
		registerSpell(SpellDragonGrowl)
		registerSpell(SpellEcho)
		registerSpell(SpellFenrirStorm)
		registerSpell(SpellFireball)
		registerSpell(SpellFirewall)
		registerSpell(SpellGravityTrap)
		registerSpell(SpellGoldRush)
		registerSpell(SpellHammerfall)
		registerSpell(SpellHarp)
		registerSpell(SpellHealing)
		registerSpell(SpellHollowBody)
		registerSpell(SpellIceLens)
		registerSpell(SpellIgnition)
		registerSpell(SpellIsaacStorm)
		registerSpell(SpellJoin)
		registerSpell(SpellLiquification)
		registerSpell(SpellMortar)
		registerSpell(SpellNight)
		registerSpell(SpellNightVision)
		registerSpell(SpellNineLifes)
		registerSpell(SpellNoclip)
		registerSpell(SpellOutdare)
		registerSpell(SpellPoisonRoots)
		registerSpell(SpellPurifyingSurface)
		registerSpell(SpellRain)
		registerSpell(SpellResurrect)
		registerSpell(SpellSacrifice)
		registerSpell(SpellShadowVortex)
		registerSpell(SpellSmokeScreen)
		registerSpell(SpellStoneSkin)
		registerSpell(SpellSun)
		registerSpell(SpellSwap)
		registerSpell(SpellThor)
		registerSpell(SpellThrow)
		registerSpell(SpellThunder)
		registerSpell(SpellTimeStop)
		registerSpell(SpellTitanHit)
		registerSpell(SpellTrueSight)
		registerSpell(SpellUphealth)
		registerSpell(SpellWallWarp)
		registerSpell(SpellWarhood)
		registerSpell(SpellWaterBreathing)
		registerSpell(SpellWellOLife)
		registerSpell(SpellWindBlades)
	}
	
	fun loadAllPinkStuff() {
		addPink(ItemStack(Blocks.wool, 1, 6), 1)
		addPink(ItemStack(Blocks.red_flower, 1, 7), 1)
		addPink(ItemStack(Blocks.stained_hardened_clay, 1, 6), 1)
		addPink(ItemStack(Blocks.stained_glass, 1, 6), 1)
		addPink(ItemStack(Blocks.stained_glass_pane, 1, 6), 1)
		addPink(ItemStack(Blocks.carpet, 1, 6), 1)
		addPink(ItemStack(Blocks.double_plant, 1, 5), 2)
		
		addPink(ItemStack(Items.dye, 1, 9), 1)
		addPink(ItemStack(Items.potionitem, 1, 8193), 2)
		addPink(ItemStack(Items.potionitem, 1, 8225), 3)
		addPink(ItemStack(Items.potionitem, 1, 8257), 3)
		addPink(ItemStack(Items.potionitem, 1, 16385), 2)
		addPink(ItemStack(Items.potionitem, 1, 16417), 3)
		addPink(ItemStack(Items.potionitem, 1, 16449), 3)
		addPink(ItemStack(Items.porkchop), 1)
		
		
		
		addPink(ItemStack(ModBlocks.corporeaCrystalCube), 9)
		addPink(ItemStack(ModBlocks.corporeaFunnel), 9)
		addPink(ItemStack(ModBlocks.corporeaIndex), 27)
		addPink(ItemStack(ModBlocks.corporeaInterceptor), 9)
		addPink(ItemStack(ModBlocks.corporeaRetainer), 9)
		addPink(ItemStack(ModBlocks.flower, 1, 6), 2)
		addPink(ItemStack(ModBlocks.floatingFlower, 1, 6), 2)
		addPink(ItemStack(ModBlocks.doubleFlower1, 1, 6), 4) // upper part
		addPink(ItemStack(ModBlocks.doubleFlower1, 1, 14), 4) // bottom part just in case
		addPink(ItemStack(ModBlocks.manaBeacon, 1, 6), 8)
		addPink(ItemStack(ModBlocks.mushroom, 1, 6), 4)
		addPink(ItemStack(ModBlocks.petalBlock, 1, 6), 9)
		addPink(ItemStack(ModBlocks.shinyFlower, 1, 6), 2)
		addPink(ItemStack(ModBlocks.spawnerClaw), 18)
		addPink(ItemStack(ModBlocks.spreader, 1, 3), 18)
		addPink(ItemStack(ModBlocks.starfield), 45)
		addPink(ItemStack(ModBlocks.storage, 1, 2), 81)
		addPink(ItemStack(ModBlocks.storage, 1, 4), 81)
		addPink(ItemStack(ModBlocks.tinyPotato), 1)
		addPink(ItemStack(ModBlocks.unstableBlock, 1, 6), 2)
		addPink(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ARCANE_ROSE), 2) // was 4
		
		addPink(ItemStack(ModFluffBlocks.lavenderQuartz), 4)
		addPink(ItemStack(ModFluffBlocks.lavenderQuartz, 1, 1), 4)
		addPink(ItemStack(ModFluffBlocks.lavenderQuartz, 1, 2), 4)
		addPink(ItemStack(ModFluffBlocks.lavenderQuartzSlab), 2)
		addPink(ItemStack(ModFluffBlocks.lavenderQuartzStairs), 4)
		
		addPink(ItemStack(ModItems.aesirRing), 6000)
		addPink(ItemStack(ModItems.baubleBox), 5)
		addPink(ItemStack(ModItems.blackHoleTalisman), 36)
		addPink(ItemStack(ModItems.corporeaSpark), 9)
		addPink(ItemStack(ModItems.cosmetic, 1, 8), 4) // was 8
		addPink(ItemStack(ModItems.cosmetic, 1, 30), 1)
		addPink(ItemStack(ModItems.dye, 1, 6), 1)
		for (i in 0..9) addPink(ItemStack(ModItems.flightTiara, 1, i), 88)
		addPink(ItemStack(ModItems.manaResource, 1, 7), 9)
		addPink(ItemStack(ModItems.manaResource, 1, 8), 9)
		addPink(ItemStack(ModItems.manaResource, 1, 9), 9)
		addPink(ItemStack(ModItems.manaResource, 1, 19), 1)
		addPink(ItemStack(ModItems.elementiumAxe), 27)
		addPink(ItemStack(ModItems.elementiumBoots), 36)
		addPink(ItemStack(ModItems.elementiumChest), 72)
		addPink(ItemStack(ModItems.elementiumHelm), 45)
		if (Botania.thaumcraftLoaded) addPink(ItemStack(ModItems.elementiumHelmRevealing), 45)
		addPink(ItemStack(ModItems.elementiumLegs), 63)
		addPink(ItemStack(ModItems.elementiumPick), 27)
		addPink(ItemStack(ModItems.elementiumShears), 18)
		addPink(ItemStack(ModItems.elementiumShovel), 9)
		addPink(ItemStack(ModItems.elementiumSword), 18)
		addPink(ItemStack(ModItems.lens, 1, 14), 18)
		addPink(ItemStack(ModItems.lokiRing), 1000)
		addPink(ItemStack(ModItems.odinRing), 1000)
		addPink(ItemStack(ModItems.openBucket), 27)
		addPink(ItemStack(ModItems.petal, 1, 6), 1)
		addPink(ItemStack(ModItems.pinkinator), 100)
		addPink(ItemStack(ModItems.pixieRing), 45)
		addPink(ItemStack(ModItems.quartz, 1, 3), 1)
		addPink(ItemStack(ModItems.rainbowRod), 45)
		addPink(ItemStack(ModItems.reachRing), 36)
		addPink(ItemStack(ModItems.rune, 1, 4), 10)
		addPink(ItemStack(ModItems.spawnerMover), 63)
		addPink(ItemStack(ModItems.slimeBottle), 45)
		addPink(ItemStack(ModItems.starSword), 20)
		addPink(ItemStack(ModItems.superTravelBelt), 27) // was 38
		addPink(ItemStack(ModItems.thorRing), 1000)
		
		
		
		addPink(ItemStack(AlfheimBlocks.anyavil), 297)
		addPink(ItemStack(AlfheimBlocks.alfheimPylon), 45)
		addPink(ItemStack(AlfheimBlocks.elvenOre), 9)
		addPink(ItemStack(AlfheimBlocks.elvenOre, 1, 1), 9)
		addPink(ItemStack(AlfheimBlocks.irisDirt, 1, 6), 2)
		addPink(ItemStack(AlfheimBlocks.irisTallGrass0, 1, 6), 2)
		addPink(ItemStack(AlfheimBlocks.irisGrass, 1, 6), 1)
		addPink(ItemStack(AlfheimBlocks.irisLeaves0, 1, 6), 1)
		addPink(ItemStack(AlfheimBlocks.irisLeaves0, 1, 14), 1)
		addPink(ItemStack(AlfheimBlocks.irisPlanks, 1, 6), 2)
		addPink(ItemStack(AlfheimBlocks.irisSlabs[6]), 1)
		addPink(ItemStack(AlfheimBlocks.irisStairs[6]), 2)
		addPink(ItemStack(AlfheimBlocks.irisWood1, 1, 2), 2)
		addPink(ItemStack(AlfheimBlocks.itemDisplay, 1, 2), 1)
		addPink(ItemStack(AlfheimBlocks.manaInfuser), 90)
		
		addPink(ItemStack(AlfheimFluffBlocks.shrineRock, 1, 6), 1)
		
		addPink(ItemStack(AlfheimItems.aesirEmblem), 18)
		addPink(ItemStack(AlfheimItems.astrolabe), 54)
		addPink(ItemStack(AlfheimItems.colorOverride), 54)
		addPink(ItemStack(AlfheimItems.cloudPendantSuper), 18)
		addPink(ItemStack(AlfheimItems.elementalBoots), 36)
		addPink(ItemStack(AlfheimItems.elementalChestplate), 72)
		addPink(ItemStack(AlfheimItems.elementalHelmet), 45)
		if (Botania.thaumcraftLoaded) addPink(ItemStack(AlfheimItems.elementalHelmetRevealing), 45)
		addPink(ItemStack(AlfheimItems.elementalLeggings), 63)
		addPink(ItemStack(AlfheimItems.elementiumHoe), 18)
		addPink(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore), 9)
		addPink(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.ElvenWeed), 8)
		addPink(ItemStack(AlfheimItems.flugelDisc), 13)
		addPink(ItemStack(AlfheimItems.flugelHead), 5)
		for (i in 0..6) addPink(ItemStack(AlfheimItems.hyperBucket, 1, i), 27)
		addPink(ItemStack(AlfheimItems.irisSeeds, 1, 6), 2)
		addPink(ItemStack(AlfheimItems.multibauble), 18)
		addPink(ItemStack(AlfheimItems.pixieAttractor), 54)
		addPink(ItemStack(AlfheimItems.priestEmblem, 1, 3), 18)
		addPink(ItemStack(AlfheimItems.priestRingHeimdall), 1000)
		addPink(ItemStack(AlfheimItems.priestRingNjord), 1000)
		addPink(ItemStack(AlfheimItems.priestRingSif), 1000)
		addPink(ItemStack(AlfheimItems.rodClicker), 29)
		addPink(ItemStack(AlfheimItems.rodColorfulSkyDirt), 27)
		addPink(ItemStack(AlfheimItems.spatiotemporalRing), 54)
		addPink(ItemStack(AlfheimItems.trisDagger), 36)
		addPink(ItemStack(AlfheimItems.wireAxe), 81)
	}
}