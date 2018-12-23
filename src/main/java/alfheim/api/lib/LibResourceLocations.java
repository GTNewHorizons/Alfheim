package alfheim.api.lib;

import java.util.HashMap;

import alfheim.api.ModInfo;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.lib.LibResources;

public class LibResourceLocations {
	
	public static ResourceLocation arfa = new ResourceLocation(ModInfo.MODID, "textures/model/entity/arfa.png"); 
	public static ResourceLocation aura = new ResourceLocation(ModInfo.MODID, "textures/model/entity/KAIIIAK.png"); 
	public static ResourceLocation babylon = new ResourceLocation(LibResources.MISC_BABYLON);
	public static ResourceLocation butterfly = new ResourceLocation(ModInfo.MODID, "textures/misc/Butterfly.png");
	public static ResourceLocation cross = new ResourceLocation(ModInfo.MODID, "textures/misc/crosshair.png");
	public static ResourceLocation deathTimer = new ResourceLocation(ModInfo.MODID, "textures/gui/DeathTimer.png");
	public static ResourceLocation deathTimerBG = new ResourceLocation(ModInfo.MODID, "textures/gui/DeathTimerBack.png");
	public static ResourceLocation elementiumBlock = new ResourceLocation("botania:textures/blocks/storage2.png");
	public static ResourceLocation elf = new ResourceLocation(ModInfo.MODID, "textures/model/entity/Elf.png");
	public static ResourceLocation elvenPylon = new ResourceLocation(LibResources.MODEL_PYLON_PINK);
	public static ResourceLocation elvenPylonOld = new ResourceLocation(LibResources.MODEL_PYLON_PINK_OLD);
	public static ResourceLocation elvoriumArmor = new ResourceLocation(ModInfo.MODID, "textures/model/armor/ElvoriumArmor.png");
	public static ResourceLocation gaiaPylon = new ResourceLocation(ModInfo.MODID, "textures/model/block/GaiaPylon.png");
	public static ResourceLocation gaiaPylonOld = new ResourceLocation(ModInfo.MODID, "textures/model/block/GaiaPylonOld.png");
	public static ResourceLocation glow = new ResourceLocation(ModInfo.MODID, "textures/misc/glow.png");
	public static ResourceLocation glowCyan = new ResourceLocation(LibResources.MISC_GLOW_CYAN);
	public static ResourceLocation gravity = new ResourceLocation(ModInfo.MODID, "textures/model/entity/Gravity.png");
	public static ResourceLocation halo = new ResourceLocation(LibResources.MISC_HALO);
	public static ResourceLocation health = new ResourceLocation(ModInfo.MODID, "textures/gui/health.png");
	public static ResourceLocation hotSpells = new ResourceLocation(ModInfo.MODID, "textures/gui/HotSpells.png");
	public static ResourceLocation iceLens = new ResourceLocation(ModInfo.MODID, "textures/misc/IceLens.png");
	public static ResourceLocation jibril = new ResourceLocation(ModInfo.MODID, "textures/model/entity/Jibril.png");
	public static ResourceLocation lexica = new ResourceLocation(LibResources.MODEL_LEXICA);
	public static ResourceLocation manaInfuserOverlay = new ResourceLocation(LibResources.GUI_MANA_INFUSION_OVERLAY);
	public static ResourceLocation mark = new ResourceLocation(ModInfo.MODID, "textures/model/entity/Mark.png");
	public static ResourceLocation mine1 = new ResourceLocation(ModInfo.MODID, "textures/model/entity/1.png");
	public static ResourceLocation mine2 = new ResourceLocation(ModInfo.MODID, "textures/model/entity/2.png");
	public static ResourceLocation mine3 = new ResourceLocation(ModInfo.MODID, "textures/model/entity/3.png");
	public static ResourceLocation petalOverlay = new ResourceLocation(LibResources.GUI_PETAL_OVERLAY);
	public static ResourceLocation pixie = new ResourceLocation(LibResources.MODEL_PIXIE);	
	public static ResourceLocation potions = new ResourceLocation(ModInfo.MODID, "textures/gui/Potions.png");
	public static ResourceLocation skin = new ResourceLocation(ModInfo.MODID, "textures/model/entity/AlexSocol.png");
	public static ResourceLocation spellFrame = new ResourceLocation(ModInfo.MODID, "textures/gui/spellframe.png");
	public static ResourceLocation spellFrameEpic = new ResourceLocation(ModInfo.MODID, "textures/gui/spellframeepic.png");
	public static ResourceLocation spellRace = new ResourceLocation(ModInfo.MODID, "textures/gui/SpellRace.png");
	public static ResourceLocation spreader = new ResourceLocation(LibResources.MODEL_SPREADER);
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
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/male/SALAMANDER.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/male/SYLPH.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/male/CAITSITH.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/male/POOKA.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/male/GNOME.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/male/LEPRECHAUN.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/male/SPRIGGAN.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/male/UNDINE.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/male/IMP.png"),
	};
	
	public static final ResourceLocation[] female = {
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/female/SALAMANDER.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/female/SYLPH.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/female/CAITSITH.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/female/POOKA.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/female/GNOME.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/female/LEPRECHAUN.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/female/SPRIGGAN.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/female/UNDINE.png"),
			new ResourceLocation(ModInfo.MODID, "textures/model/entity/female/IMP.png"),
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