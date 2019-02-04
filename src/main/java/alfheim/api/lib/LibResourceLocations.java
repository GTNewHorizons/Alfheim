package alfheim.api.lib;

import java.util.HashMap;

import alfheim.api.ModInfo;
import net.minecraft.util.ResourceLocation;

public class LibResourceLocations {
	
	public static ResourceLocation antiPylon = new ResourceLocation(ModInfo.MODID, "textures/model/block/AntiPylon.png");
	public static ResourceLocation antiPylonOld = new ResourceLocation(ModInfo.MODID, "textures/model/block/AntiPylonOld.png");
	public static ResourceLocation anyavil = new ResourceLocation(ModInfo.MODID, "textures/model/block/Anyavil.png");
	public static ResourceLocation aura = new ResourceLocation(ModInfo.MODID, "textures/model/entity/KAIIIAK.png"); 
	public static ResourceLocation babylon = new ResourceLocation("botania:textures/misc/babylon.png");
	public static ResourceLocation balanceCloak = new ResourceLocation(ModInfo.MODID, "textures/model/armor/BalanceCloak.png");
	public static ResourceLocation butterfly = new ResourceLocation(ModInfo.MODID, "textures/misc/Butterfly.png");
	public static ResourceLocation cross = new ResourceLocation(ModInfo.MODID, "textures/misc/crosshair.png");
	public static ResourceLocation deathTimer = new ResourceLocation(ModInfo.MODID, "textures/gui/DeathTimer.png");
	public static ResourceLocation deathTimerBG = new ResourceLocation(ModInfo.MODID, "textures/gui/DeathTimerBack.png");
	public static ResourceLocation elementiumBlock = new ResourceLocation("botania:textures/blocks/storage2.png");
	public static ResourceLocation elf = new ResourceLocation(ModInfo.MODID, "textures/model/entity/Elf.png");
	public static ResourceLocation elvenPylon = new ResourceLocation("botania:textures/model/pylon2.png");
	public static ResourceLocation elvenPylonOld = new ResourceLocation("botania:textures/model/pylonOld2.png");
	public static ResourceLocation elvoriumArmor = new ResourceLocation(ModInfo.MODID, "textures/model/armor/ElvoriumArmor.png");
	public static ResourceLocation explosion = new ResourceLocation(ModInfo.MODID, "textures/misc/explosion.png");
	public static ResourceLocation gaiaPylon = new ResourceLocation(ModInfo.MODID, "textures/model/block/GaiaPylon.png");
	public static ResourceLocation gaiaPylonOld = new ResourceLocation(ModInfo.MODID, "textures/model/block/GaiaPylonOld.png");
	public static ResourceLocation glow = new ResourceLocation(ModInfo.MODID, "textures/misc/glow.png");
	public static ResourceLocation glowCyan = new ResourceLocation("botania:textures/misc/glow1.png");
	public static ResourceLocation gravel = new ResourceLocation(ModInfo.MODID, "textures/misc/gravel.png");
	public static ResourceLocation gravity = new ResourceLocation(ModInfo.MODID, "textures/model/entity/Gravity.png");
	public static ResourceLocation halo = new ResourceLocation("botania:textures/misc/halo.png");
	public static ResourceLocation harp = new ResourceLocation(ModInfo.MODID, "textures/model/entity/harp.png"); 
	public static ResourceLocation health = new ResourceLocation(ModInfo.MODID, "textures/gui/health.png");
	public static ResourceLocation hotSpells = new ResourceLocation(ModInfo.MODID, "textures/gui/HotSpells.png");
	public static ResourceLocation iceLens = new ResourceLocation(ModInfo.MODID, "textures/misc/IceLens.png");
	public static ResourceLocation jibril = new ResourceLocation(ModInfo.MODID, "textures/model/entity/Jibril.png");
	public static ResourceLocation lexica = new ResourceLocation("botania:textures/model/lexica.png");
	public static ResourceLocation livingrock = new ResourceLocation("botania:textures/blocks/livingrock0.png");
	public static ResourceLocation manaInfuserOverlay = new ResourceLocation("botania:textures/gui/manaInfusionOverlay.png");
	public static ResourceLocation mark = new ResourceLocation(ModInfo.MODID, "textures/model/entity/Mark.png");
	public static ResourceLocation miku0 = new ResourceLocation(ModInfo.MODID, "textures/model/entity/Miku0.png");
	public static ResourceLocation miku1 = new ResourceLocation(ModInfo.MODID, "textures/model/entity/Miku1.png");
	public static ResourceLocation mine1 = new ResourceLocation(ModInfo.MODID, "textures/model/entity/1.png");
	public static ResourceLocation mine2 = new ResourceLocation(ModInfo.MODID, "textures/model/entity/2.png");
	public static ResourceLocation mine3 = new ResourceLocation(ModInfo.MODID, "textures/model/entity/3.png");
	public static ResourceLocation petalOverlay = new ResourceLocation("botania:textures/gui/petalOverlay.png");
	public static ResourceLocation pixie = new ResourceLocation("botania:textures/model/pixie.png");
	public static ResourceLocation poolBlue = new ResourceLocation(ModInfo.MODID, "textures/blocks/PoolBlue.png");	
	public static ResourceLocation poolPink = new ResourceLocation(ModInfo.MODID, "textures/blocks/PoolPink.png");	
	public static ResourceLocation potions = new ResourceLocation(ModInfo.MODID, "textures/gui/Potions.png");
	public static ResourceLocation rook = new ResourceLocation(ModInfo.MODID, "textures/model/entity/Rook.png");
	public static ResourceLocation skin = new ResourceLocation(ModInfo.MODID, "textures/model/entity/AlexSocol.png");
	public static ResourceLocation spellFrame = new ResourceLocation(ModInfo.MODID, "textures/gui/spellframe.png");
	public static ResourceLocation spellFrameEpic = new ResourceLocation(ModInfo.MODID, "textures/gui/spellframeepic.png");
	public static ResourceLocation spellRace = new ResourceLocation(ModInfo.MODID, "textures/gui/SpellRace.png");
	public static ResourceLocation spreader = new ResourceLocation("botania:textures/model/spreader.png");
	public static ResourceLocation target = new ResourceLocation(ModInfo.MODID, "textures/misc/target.png");
	public static ResourceLocation targetq = new ResourceLocation(ModInfo.MODID, "textures/misc/targetq.png");
	public static ResourceLocation wind = new ResourceLocation(ModInfo.MODID, "textures/model/entity/wind.png");
	public static ResourceLocation yordinPylon = new ResourceLocation(ModInfo.MODID, "textures/model/block/ElvenPylon.png");
	public static ResourceLocation yordinPylonOld = new ResourceLocation(ModInfo.MODID, "textures/model/block/ElvenPylonOld.png");
	
	public static final ResourceLocation[] wings = {
		null,
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/SALAMANDER_wing.png"),
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/SYLPH_wing.png"),
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/CAITSITH_wing.png"),
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/POOKA_wing.png"),
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/GNOME_wing.png"),
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/LEPRECHAUN_wing.png"),
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/SPRIGGAN_wing.png"),
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/UNDINE_wing.png"),
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/IMP_wing.png"),
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/ALV_wing.png"),
	};
	
	public static final ResourceLocation[] icons = {
		new ResourceLocation(ModInfo.MODID, "textures/misc/icons/HUMAN.png"),
		new ResourceLocation(ModInfo.MODID, "textures/misc/icons/SALAMANDER.png"),
		new ResourceLocation(ModInfo.MODID, "textures/misc/icons/SYLPH.png"),
		new ResourceLocation(ModInfo.MODID, "textures/misc/icons/CAITSITH.png"),
		new ResourceLocation(ModInfo.MODID, "textures/misc/icons/POOKA.png"),
		new ResourceLocation(ModInfo.MODID, "textures/misc/icons/GNOME.png"),
		new ResourceLocation(ModInfo.MODID, "textures/misc/icons/LEPRECHAUN.png"),
		new ResourceLocation(ModInfo.MODID, "textures/misc/icons/SPRIGGAN.png"),
		new ResourceLocation(ModInfo.MODID, "textures/misc/icons/UNDINE.png"),
		new ResourceLocation(ModInfo.MODID, "textures/misc/icons/IMP.png"),
		new ResourceLocation(ModInfo.MODID, "textures/misc/icons/ALV.png"),
		new ResourceLocation(ModInfo.MODID, "textures/misc/icons/MOB.png"),
		new ResourceLocation(ModInfo.MODID, "textures/misc/icons/NPC.png"),
		new ResourceLocation(ModInfo.MODID, "textures/misc/icons/BOSS.png")
	};
	
	public static final ResourceLocation[] affinities = {
		new ResourceLocation("Omg dat's weird"),
		new ResourceLocation(ModInfo.MODID, "textures/gui/spells/affinities/SALAMANDER.png"),
		new ResourceLocation(ModInfo.MODID, "textures/gui/spells/affinities/SYLPH.png"),
		new ResourceLocation(ModInfo.MODID, "textures/gui/spells/affinities/CAITSITH.png"),
		new ResourceLocation(ModInfo.MODID, "textures/gui/spells/affinities/POOKA.png"),
		new ResourceLocation(ModInfo.MODID, "textures/gui/spells/affinities/GNOME.png"),
		new ResourceLocation(ModInfo.MODID, "textures/gui/spells/affinities/LEPRECHAUN.png"),
		new ResourceLocation(ModInfo.MODID, "textures/gui/spells/affinities/SPRIGGAN.png"),
		new ResourceLocation(ModInfo.MODID, "textures/gui/spells/affinities/UNDINE.png"),
		new ResourceLocation(ModInfo.MODID, "textures/gui/spells/affinities/IMP.png"),
	};
	
	public static int MOB = 11, NPC = 12, BOSS = 13;
	
	public static final ResourceLocation[] male = {
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/male/Salamander.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/male/Sylph.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/male/CaitSith.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/male/Pooka.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/male/Gnome.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/male/Leprechaun.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/male/Spriggan.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/male/Undine.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/male/Imp.png"),
	};
	
	public static final ResourceLocation[] female = {
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/female/Salamander.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/female/Sylph.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/female/CaitSith.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/female/Pooka.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/female/Gnome.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/female/Leprechaun.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/female/Spriggan.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/female/Undine.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/female/Imp.png"),
	};
	
	private static HashMap<String, ResourceLocation> spells = new HashMap<String, ResourceLocation>();
	
	public static ResourceLocation add(String name) {
		return spells.put(name, new ResourceLocation(ModInfo.MODID, "textures/gui/spells/" + name + ".png"));
	}
	
	public static ResourceLocation spell(String name) {
		ResourceLocation r = spells.get(name);
		return r == null ? affinities[0] : r;
	}
	
	public static ResourceLocation inventory = new ResourceLocation("textures/gui/container/inventory.png");
    public static ResourceLocation widgets = new ResourceLocation("textures/gui/widgets.png");
}