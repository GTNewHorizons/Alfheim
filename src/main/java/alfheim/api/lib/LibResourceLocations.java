package alfheim.api.lib;

import java.util.HashMap;

import alfheim.api.ModInfo;
import net.minecraft.util.ResourceLocation;

public class LibResourceLocations {
	
	public static final ResourceLocation anomalies = new ResourceLocation(ModInfo.MODID, "textures/misc/anomalies.png");
	public static final ResourceLocation antiPylon = new ResourceLocation(ModInfo.MODID, "textures/model/block/AntiPylon.png");
	public static final ResourceLocation antiPylonOld = new ResourceLocation(ModInfo.MODID, "textures/model/block/AntiPylonOld.png");
	public static final ResourceLocation anyavil = new ResourceLocation(ModInfo.MODID, "textures/model/block/Anyavil.png");
	public static final ResourceLocation aura = new ResourceLocation(ModInfo.MODID, "textures/model/entity/KAIIIAK.png");
	public static ResourceLocation babylon = new ResourceLocation("botania:textures/misc/babylon.png");
	public static final ResourceLocation balanceCloak = new ResourceLocation(ModInfo.MODID, "textures/model/armor/BalanceCloak.png");
	public static final ResourceLocation butterfly = new ResourceLocation(ModInfo.MODID, "textures/misc/Butterfly.png");
	public static final ResourceLocation cross = new ResourceLocation(ModInfo.MODID, "textures/misc/crosshair.png");
	public static final ResourceLocation deathTimer = new ResourceLocation(ModInfo.MODID, "textures/gui/DeathTimer.png");
	public static final ResourceLocation deathTimerBG = new ResourceLocation(ModInfo.MODID, "textures/gui/DeathTimerBack.png");
	public static final ResourceLocation elementiumBlock = new ResourceLocation("botania:textures/blocks/storage2.png");
	public static final ResourceLocation elf = new ResourceLocation(ModInfo.MODID, "textures/model/entity/Elf.png");
	public static ResourceLocation elvenPylon = new ResourceLocation("botania:textures/model/pylon2.png");
	public static ResourceLocation elvenPylonOld = new ResourceLocation("botania:textures/model/pylonOld2.png");
	public static final ResourceLocation elvoriumArmor = new ResourceLocation(ModInfo.MODID, "textures/model/armor/ElvoriumArmor.png");
	public static final ResourceLocation explosion = new ResourceLocation(ModInfo.MODID, "textures/misc/explosion.png");
	public static final ResourceLocation gaiaPylon = new ResourceLocation(ModInfo.MODID, "textures/model/block/GaiaPylon.png");
	public static final ResourceLocation gaiaPylonOld = new ResourceLocation(ModInfo.MODID, "textures/model/block/GaiaPylonOld.png");
	public static final ResourceLocation glow = new ResourceLocation(ModInfo.MODID, "textures/misc/glow.png");
	public static ResourceLocation glowCyan = new ResourceLocation("botania:textures/misc/glow1.png");
	public static final ResourceLocation gravel = new ResourceLocation(ModInfo.MODID, "textures/misc/gravel.png");
	public static final ResourceLocation gravity = new ResourceLocation(ModInfo.MODID, "textures/model/entity/Gravity.png");
	public static ResourceLocation halo = new ResourceLocation("botania:textures/misc/halo.png");
	public static final ResourceLocation harp = new ResourceLocation(ModInfo.MODID, "textures/model/entity/harp.png");
	public static final ResourceLocation health = new ResourceLocation(ModInfo.MODID, "textures/gui/health.png");
	public static final ResourceLocation hotSpells = new ResourceLocation(ModInfo.MODID, "textures/gui/HotSpells.png");
	public static final ResourceLocation iceLens = new ResourceLocation(ModInfo.MODID, "textures/misc/IceLens.png");
	public static final ResourceLocation jibril = new ResourceLocation(ModInfo.MODID, "textures/model/entity/Jibril.png");
	public static ResourceLocation lexica = new ResourceLocation("botania:textures/model/lexica.png");
	public static final ResourceLocation livingrock = new ResourceLocation("botania:textures/blocks/livingrock0.png");
	public static ResourceLocation manaInfuserOverlay = new ResourceLocation("botania:textures/gui/manaInfusionOverlay.png");
	public static final ResourceLocation mark = new ResourceLocation(ModInfo.MODID, "textures/model/entity/Mark.png");
	public static final ResourceLocation miku0 = new ResourceLocation(ModInfo.MODID, "textures/model/entity/Miku0.png");
	public static final ResourceLocation miku1 = new ResourceLocation(ModInfo.MODID, "textures/model/entity/Miku1.png");
	public static final ResourceLocation mine1 = new ResourceLocation(ModInfo.MODID, "textures/model/entity/1.png");
	public static final ResourceLocation mine2 = new ResourceLocation(ModInfo.MODID, "textures/model/entity/2.png");
	public static final ResourceLocation mine3 = new ResourceLocation(ModInfo.MODID, "textures/model/entity/3.png");
	public static ResourceLocation petalOverlay = new ResourceLocation("botania:textures/gui/petalOverlay.png");
	public static ResourceLocation pixie = new ResourceLocation("botania:textures/model/pixie.png");
	public static final ResourceLocation poolBlue = new ResourceLocation(ModInfo.MODID, "textures/blocks/PoolBlue.png");
	public static final ResourceLocation poolPink = new ResourceLocation(ModInfo.MODID, "textures/blocks/PoolPink.png");
	public static final ResourceLocation potions = new ResourceLocation(ModInfo.MODID, "textures/gui/Potions.png");
	public static final ResourceLocation rook = new ResourceLocation(ModInfo.MODID, "textures/model/entity/Rook.png");
	public static final ResourceLocation skin = new ResourceLocation(ModInfo.MODID, "textures/model/entity/AlexSocol.png");
	public static final ResourceLocation spellFrame = new ResourceLocation(ModInfo.MODID, "textures/gui/spellframe.png");
	public static final ResourceLocation spellFrameEpic = new ResourceLocation(ModInfo.MODID, "textures/gui/spellframeepic.png");
	public static final ResourceLocation spellRace = new ResourceLocation(ModInfo.MODID, "textures/gui/SpellRace.png");
	public static ResourceLocation spreader = new ResourceLocation("botania:textures/model/spreader.png");
	public static final ResourceLocation target = new ResourceLocation(ModInfo.MODID, "textures/misc/target.png");
	public static final ResourceLocation targetq = new ResourceLocation(ModInfo.MODID, "textures/misc/targetq.png");
	public static final ResourceLocation wind = new ResourceLocation(ModInfo.MODID, "textures/model/entity/wind.png");
	public static final ResourceLocation yordinPylon = new ResourceLocation(ModInfo.MODID, "textures/model/block/ElvenPylon.png");
	public static final ResourceLocation yordinPylonOld = new ResourceLocation(ModInfo.MODID, "textures/model/block/ElvenPylonOld.png");
	
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
	
	public static final int MOB = 11;
	public static final int NPC = 12;
	public static final int BOSS = 13;
	
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
	
	private static final HashMap<String, ResourceLocation> spells = new HashMap<String, ResourceLocation>();
	
	public static ResourceLocation add(String name) {
		return spells.put(name, new ResourceLocation(ModInfo.MODID, "textures/gui/spells/" + name + ".png"));
	}
	
	public static ResourceLocation spell(String name) {
		ResourceLocation r = spells.get(name);
		return r == null ? affinities[0] : r;
	}
	
	public static final ResourceLocation inventory = new ResourceLocation("textures/gui/container/inventory.png");
	public static final ResourceLocation widgets = new ResourceLocation("textures/gui/widgets.png");
}