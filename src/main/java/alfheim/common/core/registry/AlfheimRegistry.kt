package alfheim.common.core.registry

import alexsocol.asjlib.ASJReflectionHelper
import alexsocol.asjlib.ASJUtilities.Companion.registerEntity
import alexsocol.asjlib.ASJUtilities.Companion.registerEntityEgg
import alfheim.AlfheimCore
import alfheim.api.AlfheimAPI.addPink
import alfheim.api.AlfheimAPI.registerAnomaly
import alfheim.api.AlfheimAPI.registerSpell
import alfheim.api.ModInfo
import alfheim.api.spell.SpellBase
import alfheim.common.block.tile.*
import alfheim.common.block.tile.sub.*
import alfheim.common.core.asm.AlfheimHookLoader
import alfheim.common.core.util.AlfheimConfig
import alfheim.common.entity.*
import alfheim.common.entity.boss.*
import alfheim.common.entity.spell.*
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
import cpw.mods.fml.common.registry.GameRegistry.*
import net.minecraft.entity.ai.attributes.*
import net.minecraft.init.*
import net.minecraft.item.ItemStack
import net.minecraft.potion.Potion
import net.minecraft.tileentity.TileEntity
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.brew.ModPotions
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.item.block.ItemBlockSpecialFlower
import vazkii.botania.common.lib.LibBlockNames
import kotlin.math.*

object AlfheimRegistry {
	
	val FLIGHT: IAttribute = object: BaseAttribute(ModInfo.MODID.toUpperCase() + ":FLIGHT", AlfheimConfig.flightTime.toDouble()) {
		
		override fun clampValue(d: Double): Double {
			return max(0.0, min(AlfheimConfig.flightTime.toDouble(), d))
		}
	}.setShouldWatch(true)
	
	
	
	lateinit var berserk: Potion
	lateinit var bleeding: Potion
	lateinit var butterShield: Potion
	lateinit var deathMark: Potion
	lateinit var decay: Potion
	lateinit var goldRush: Potion
	lateinit var icelens: Potion
	lateinit var leftFlame: Potion
	fun leftFlameIsInitialized() = ::leftFlame.isInitialized
	lateinit var nineLifes: Potion
	lateinit var ninja: Potion
	lateinit var noclip: Potion
	lateinit var overmage: Potion
	lateinit var possession: Potion
	lateinit var quadDamage: Potion
	lateinit var sacrifice: Potion
	lateinit var sharedHP: Potion
	fun sharedHPIsInitialized() = ::leftFlame.isInitialized
	lateinit var showMana: Potion
	lateinit var soulburn: Potion
	lateinit var stoneSkin: Potion
	lateinit var tank: Potion
	lateinit var tHrOw: Potion
	lateinit var wellOLife: Potion
	
	lateinit var worldGen: IWorldGenerator
	
	fun preInit() {
		if (Potion.potionTypes.size < 256) ASJReflectionHelper.invokeStatic<ModPotions, Any>(ModPotions::class.java, null, "extendPotionArray")
		berserk = PotionBerserk()
		bleeding = PotionBleeding()
		butterShield = PotionAlfheim(AlfheimConfig.potionIDButterShield, "butterShield", false, 0x00FFFF)
		deathMark = PotionDeathMark()
		decay = PotionAlfheim(AlfheimConfig.potionIDDecay, "decay", true, 0x553355)
		goldRush = PotionGoldRush()
		icelens = PotionAlfheim(AlfheimConfig.potionIDIceLens, "icelens", false, 0xDDFFFF)
		leftFlame = PotionLeftFlame()
		nineLifes = PotionAlfheim(AlfheimConfig.potionIDNineLifes, "nineLifes", false, 0xDD2222)
		ninja = PotionNinja()
		noclip = PotionNoclip()
		overmage = PotionAlfheim(AlfheimConfig.potionIDOvermage, "overmage", false, 0x88FFFF)
		possession = PotionAlfheim(AlfheimConfig.potionIDPossession, "possession", true, 0xCC0000)
		quadDamage = PotionQuadDamage()
		sacrifice = PotionSacrifice()
		sharedHP = PotionSharedHP()
		showMana = PotionShowMana()
		soulburn = PotionSoulburn()
		stoneSkin = PotionAlfheim(AlfheimConfig.potionIDStoneSkin, "stoneSkin", false, 0x593C1F)
		tank = PotionTank()
		tHrOw = PotionThrow()
		wellOLife = PotionWellOLife()
		registerEntities()
		registerTileEntities()
		
		worldGen = WorldGenAlfheim()
		SpellBase.overmag = overmage
	}
	
	fun init() {
		registerWorldGenerator(worldGen, 1)
		registerSpells()
	}
	
	fun postInit() {
		loadAllPinkStuff()
		if (AlfheimConfig.looniumOverseed) BotaniaAPI.looniumBlacklist.remove(ModItems.overgrowthSeed)
	}
	
	private fun registerEntities() {
		registerEntity(EntityCharge::class.java, "Charge", AlfheimCore.instance)
		registerEntityEgg(EntityElf::class.java, "Elf", 0x1A660A, 0x4D3422, AlfheimCore.instance)
		registerEntity(EntityFlugel::class.java, "Flugel", AlfheimCore.instance)
		registerEntity(EntityLightningMark::class.java, "LightningMark", AlfheimCore.instance)
		registerEntityEgg(EntityAlfheimPixie::class.java, "Pixie", 0xFF76D6, 0xFFE3FF, AlfheimCore.instance)
		registerEntity(EntityRook::class.java, "Rook", AlfheimCore.instance)
		
		registerEntity(EntitySpellAcidMyst::class.java, "SpellAcidMyst", AlfheimCore.instance)
		registerEntity(EntitySpellHarp::class.java, "SpellArfa", AlfheimCore.instance)
		registerEntity(EntitySpellAquaStream::class.java, "SpellAquaStream", AlfheimCore.instance)
		registerEntity(EntitySpellDriftingMine::class.java, "SpellDriftingMine", AlfheimCore.instance)
		registerEntity(EntitySpellFenrirStorm::class.java, "SpellFenrirStorm", AlfheimCore.instance)
		registerEntity(EntitySpellFireball::class.java, "SpellFireball", AlfheimCore.instance)
		registerEntity(EntitySpellFirewall::class.java, "SpellFirewall", AlfheimCore.instance)
		registerEntity(EntitySpellGravityTrap::class.java, "SpellGravityTrap", AlfheimCore.instance)
		registerEntity(EntitySpellIsaacMissile::class.java, "SpellIsaacMissile", AlfheimCore.instance)
		registerEntity(EntitySpellMortar::class.java, "SpellMortar", AlfheimCore.instance)
		registerEntity(EntitySpellWindBlade::class.java, "SpellWindBlade", AlfheimCore.instance)
	}
	
	private fun registerTileEntities() {
		registerTile(TileAlfheimPortal::class.java, "AlfheimPortal")
		registerTile(TileAlfheimPylon::class.java, "AlfheimPylon")
		registerTile(TileAnimatedTorch::class.java, "AnimatedTorch")
		registerTile(TileAnomaly::class.java, "Anomaly")
		registerTile(TileAnyavil::class.java, "Anyavil")
		registerTile(TileFlugelHead::class.java, "FlugelHead")
		registerTile(TileItemHolder::class.java, "ItemHolder")
		registerTile(TileManaInfuser::class.java, "ManaInfuser")
		registerTile(TileTradePortal::class.java, "TradePortal")
		//registerTileEntity(TileTransferer.class, "Transferer"); BACK
		
		registerAnomalies()
	}
	
	private fun registerTile(tileEntityClass: Class<out TileEntity>, id: String) {
		registerTileEntity(tileEntityClass, ModInfo.MODID + ":" + id)
	}
	
	private fun registerAnomalies() {
		registerAnomaly("Antigrav", SubTileAntigrav::class.java)
		registerAnomaly("Gravity", SubTileGravity::class.java)
		registerAnomaly("Lightning", SubTileLightning::class.java)
		registerAnomaly("ManaTornado", SubTileManaTornado::class.java)
		registerAnomaly("ManaVoid", SubTileManaVoid::class.java)
		registerAnomaly("SpeedDown", SubTileSpeedDown::class.java)
		registerAnomaly("SpeedUp", SubTileSpeedUp::class.java)
		registerAnomaly("Warp", SubTileWarp::class.java)
	}
	
	private fun registerSpells() {
		registerSpell(SpellAcidMyst())
		registerSpell(SpellAquaBind())
		registerSpell(SpellAquaStream())
		registerSpell(SpellBattleHorn())
		registerSpell(SpellBlink())
		registerSpell(SpellBunnyHop())
		registerSpell(SpellButterflyShield())
		registerSpell(SpellConfusion())
		registerSpell(SpellDay())
		registerSpell(SpellDeathMark())
		registerSpell(SpellDecay())
		registerSpell(SpellDispel())
		registerSpell(SpellDriftingMine())
		registerSpell(SpellDragonGrowl())
		registerSpell(SpellEcho())
		registerSpell(SpellFenrirStorm())
		registerSpell(SpellFireball())
		registerSpell(SpellFirewall())
		registerSpell(SpellGravityTrap())
		registerSpell(SpellGoldRush())
		registerSpell(SpellHammerfall())
		registerSpell(SpellHarp())
		registerSpell(SpellHealing())
		registerSpell(SpellHollowBody())
		registerSpell(SpellIceLens())
		registerSpell(SpellIgnition())
		registerSpell(SpellIsaacStorm())
		registerSpell(SpellMortar())
		registerSpell(SpellNight())
		registerSpell(SpellNightVision())
		registerSpell(SpellNineLifes())
		registerSpell(SpellNoclip())
		registerSpell(SpellOutdare())
		registerSpell(SpellPoisonRoots())
		registerSpell(SpellPurifyingSurface())
		registerSpell(SpellRain())
		registerSpell(SpellResurrect())
		registerSpell(SpellSacrifice())
		if (AlfheimHookLoader.hpSpells) registerSpell(SpellSharedHealthPool())
		registerSpell(SpellSmokeScreen())
		registerSpell(SpellStoneSkin())
		registerSpell(SpellSun())
		registerSpell(SpellThor())
		registerSpell(SpellThrow())
		registerSpell(SpellThunder())
		registerSpell(SpellTimeStop())
		registerSpell(SpellTitanHit())
		registerSpell(SpellTrueSigh())
		registerSpell(SpellUphealth())
		registerSpell(SpellWallWarp())
		registerSpell(SpellWaterBreathing())
		registerSpell(SpellWellOLife())
		registerSpell(SpellWindBlade())
	}
	
	fun loadAllPinkStuff() {
		addPink(ItemStack(Items.dye, 1, 9), 1)
		addPink(ItemStack(Blocks.wool, 1, 6), 1)
		addPink(ItemStack(Items.potionitem, 1, 8193), 2)
		addPink(ItemStack(Items.potionitem, 1, 8225), 3)
		addPink(ItemStack(Items.potionitem, 1, 8257), 3)
		addPink(ItemStack(Items.potionitem, 1, 16385), 2)
		addPink(ItemStack(Items.potionitem, 1, 16417), 3)
		addPink(ItemStack(Items.potionitem, 1, 16449), 3)
		addPink(ItemStack(Blocks.red_flower, 1, 7), 1)
		addPink(ItemStack(Blocks.stained_hardened_clay, 1, 6), 1)
		addPink(ItemStack(Blocks.stained_glass, 1, 6), 1)
		addPink(ItemStack(Blocks.stained_glass_pane, 1, 6), 1)
		addPink(ItemStack(Blocks.carpet, 1, 6), 1)
		addPink(ItemStack(ModBlocks.mushroom, 1, 6), 4)
		addPink(ItemStack(ModBlocks.flower, 1, 6), 2)
		addPink(ItemStack(ModBlocks.floatingFlower, 1, 6), 2)
		addPink(ItemStack(ModBlocks.shinyFlower, 1, 6), 2)
		addPink(ItemStack(ModBlocks.doubleFlower1, 1, 6), 4)
		addPink(ItemStack(ModBlocks.doubleFlower2, 1, 6), 4)
		addPink(ItemStack(ModBlocks.petalBlock, 1, 6), 9)
		addPink(ItemStack(ModItems.petal, 1, 6), 1)
		addPink(ItemStack(ModItems.dye, 1, 6), 1)
		addPink(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ARCANE_ROSE), 4)
		addPink(ItemStack(ModItems.manaResource, 1, 7), 9)
		addPink(ItemStack(ModItems.manaResource, 1, 8), 9)
		addPink(ItemStack(ModItems.manaResource, 1, 9), 9)
		addPink(ItemStack(ModItems.manaResource, 1, 19), 1)
		addPink(ItemStack(ModBlocks.storage, 1, 2), 81)
		addPink(ItemStack(ModBlocks.storage, 1, 4), 81)
		addPink(ItemStack(ModItems.slimeBottle), 45)
		addPink(ItemStack(ModItems.openBucket), 27)
		addPink(ItemStack(ModItems.elementiumAxe), 27)
		addPink(ItemStack(ModItems.elementiumBoots), 36)
		addPink(ItemStack(ModItems.elementiumChest), 72)
		addPink(ItemStack(ModItems.elementiumHelm), 45)
		addPink(ItemStack(ModItems.elementiumHelmRevealing), 45)
		addPink(ItemStack(ModItems.elementiumLegs), 56)
		addPink(ItemStack(ModItems.elementiumPick), 27)
		addPink(ItemStack(ModItems.elementiumShears), 18)
		addPink(ItemStack(ModItems.elementiumShovel), 9)
		addPink(ItemStack(ModItems.elementiumSword), 18)
		addPink(ItemStack(ModItems.starSword), 20)
		addPink(ItemStack(ModItems.thorRing), 1000)
		addPink(ItemStack(ModItems.odinRing), 1000)
		addPink(ItemStack(ModItems.lokiRing), 1000)
		addPink(ItemStack(ModItems.aesirRing), 3000)
		addPink(ItemStack(ModBlocks.unstableBlock, 1, 6), 2)
		addPink(ItemStack(ModBlocks.manaBeacon, 1, 6), 8)
		addPink(ItemStack(ModItems.pinkinator), 100)
		addPink(ItemStack(ModItems.rune, 1, 4), 10)
		addPink(ItemStack(ModItems.baubleBox), 5)
		addPink(ItemStack(ModItems.cosmetic, 1, 8), 8)
		addPink(ItemStack(ModItems.reachRing, 1, 8), 36)
		addPink(ItemStack(ModItems.pixieRing, 1, 8), 45)
		addPink(ItemStack(ModItems.superTravelBelt, 1, 8), 38)
		addPink(ItemStack(ModItems.rainbowRod, 1, 8), 45)
		for (i in 0..9) addPink(ItemStack(ModItems.flightTiara, 1, i), 88)
		addPink(ItemStack(AlfheimItems.elementalBoots), 36)
		addPink(ItemStack(AlfheimItems.elementalChestplate), 72)
		addPink(ItemStack(AlfheimItems.elementalHelmet), 45)
		addPink(ItemStack(AlfheimItems.elementalHelmetRevealing), 45)
		addPink(ItemStack(AlfheimItems.elementalLeggings), 56)
		addPink(ItemStack(AlfheimItems.pixieAttractor), 36)
		//addPink(new ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore), 9);
		addPink(ItemStack(AlfheimBlocks.manaInfuser), 90)
		addPink(ItemStack(AlfheimBlocks.alfheimPylon), 45)
		addPink(ItemStack(AlfheimBlocks.elvenOres), 9)
		addPink(ItemStack(AlfheimBlocks.elvenOres, 1, 1), 9)
		addPink(ItemStack(AlfheimBlocks.anyavil), 297)
	}
}