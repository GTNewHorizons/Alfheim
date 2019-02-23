package alfheim.client.core.proxy;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.render.ASJShaderHelper;
import alfheim.AlfheimCore;
import alfheim.api.lib.*;
import alfheim.client.core.handler.CardinalSystemClient.TimeStopSystemClient;
import alfheim.client.core.handler.EventHandlerClient;
import alfheim.client.core.util.AlfheimBotaniaModifiersClient;
import alfheim.client.gui.*;
import alfheim.client.lib.LibResourceLocationsActual;
import alfheim.client.model.entity.*;
import alfheim.client.render.block.*;
import alfheim.client.render.entity.*;
import alfheim.client.render.item.*;
import alfheim.client.render.tile.*;
import alfheim.common.block.tile.*;
import alfheim.common.core.handler.EventHandler;
import alfheim.common.core.proxy.CommonProxy;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.entity.*;
import alfheim.common.entity.boss.*;
import alfheim.common.entity.spell.*;
import alfheim.common.integration.travellersgear.handler.TGHandlerBotaniaRenderer;
import alfheim.common.lexicon.AlfheimLexiconData;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
	
	public static final KeyBinding keyCast = new KeyBinding("key.cast.desc", Keyboard.KEY_C, "key.categories.gameplay");
	public static final KeyBinding keyUnCast = new KeyBinding("key.uncast.desc", Keyboard.KEY_X, "key.categories.gameplay");
	public static final KeyBinding keyFlight = new KeyBinding("key.flight.desc", Keyboard.KEY_F, "key.categories.movement");
	public static final KeyBinding keySelMob = new KeyBinding("key.selmob.desc", Keyboard.KEY_R, "key.categories.gameplay");
	public static final KeyBinding keySelTeam = new KeyBinding("key.selteam.desc", Keyboard.KEY_T, "key.categories.gameplay");
	
	static {
		removeKeyBinding(keyCast);
		removeKeyBinding(keyFlight);
		removeKeyBinding(keySelMob);
		removeKeyBinding(keySelTeam);
	}
	
	private static final Gui guiIceLens = new GUIIceLens(Minecraft.getMinecraft());
	private static final Gui guiParty = new GUIParty(Minecraft.getMinecraft());
	private static final Gui guiRace = new GUIRace(Minecraft.getMinecraft());
	private static final Gui guiSpells = new GUISpells(Minecraft.getMinecraft());
	
	@Override
	public void preInit() {
		super.preInit();
		LibResourceLocationsActual.init();
	}
	
	@Override
	public void registerRenderThings() {
		LibRenderIDs.idAnyavil = RenderingRegistry.getNextAvailableRenderId();
		LibRenderIDs.idItemHolder = RenderingRegistry.getNextAvailableRenderId();
		LibRenderIDs.idPylon = RenderingRegistry.getNextAvailableRenderId();
		LibRenderIDs.idTransferer = RenderingRegistry.getNextAvailableRenderId();
		
		LibShaderIDs.idFire = ASJShaderHelper.createProgram(null, "shaders/fire.frag");
		LibShaderIDs.idGravity = ASJShaderHelper.createProgram(null, "shaders/gravity.frag");
		LibShaderIDs.idNoise = ASJShaderHelper.createProgram("shaders/position.vert", "shaders/noise4d.frag");
		LibShaderIDs.idShadow = ASJShaderHelper.createProgram(null, "shaders/shadow.frag");
		
	    MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(AlfheimBlocks.anomaly), new RenderItemAnomaly());
		
		RenderingRegistry.registerBlockHandler(LibRenderIDs.idAnyavil, new RenderBlockAnyavil());
		RenderingRegistry.registerBlockHandler(LibRenderIDs.idItemHolder, new RenderBlockItemHolder());
		RenderingRegistry.registerBlockHandler(LibRenderIDs.idPylon, new RenderBlockAlfheimPylons());
		RenderingRegistry.registerBlockHandler(LibRenderIDs.idTransferer, new RenderBlockTransferer());
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileAlfheimPortal.class, new RenderTileAlfheimPortal());
		ClientRegistry.bindTileEntitySpecialRenderer(TileAlfheimPylons.class, new RenderTileAlfheimPylons());
		ClientRegistry.bindTileEntitySpecialRenderer(TileAnimatedTorch.class, new RenderTileAnimatedTorch());
		ClientRegistry.bindTileEntitySpecialRenderer(TileAnomaly.class, new RenderTileAnomaly());
		ClientRegistry.bindTileEntitySpecialRenderer(TileAnyavil.class, new RenderTileAnyavil());
		ClientRegistry.bindTileEntitySpecialRenderer(TileFlugelHead.class, new RenderTileFlugelHead());
		ClientRegistry.bindTileEntitySpecialRenderer(TileItemHolder.class, new RenderTileItemHolder());
		ClientRegistry.bindTileEntitySpecialRenderer(TileTradePortal.class, new RenderTileTradePortal());
		ClientRegistry.bindTileEntitySpecialRenderer(TileTransferer.class, new RenderTileTransferer());
		
		RenderingRegistry.registerEntityRenderingHandler(EntityAlfheimPixie.class, new RenderEntityAlfheimPixie());
		RenderingRegistry.registerEntityRenderingHandler(EntityElf.class, new RenderEntityElf(new ModelEntityElf(), 0.25F));
		RenderingRegistry.registerEntityRenderingHandler(EntityFlugel.class, new RenderEntityFlugel(new ModelEntityFlugel(), 0.25F));
		RenderingRegistry.registerEntityRenderingHandler(EntityRook.class, new RenderEntityRook(new ModelEntityRook(), 1.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityLightningMark.class, new RenderEntityLightningMark());
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellHarp.class, new RenderEntityHarp());
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellDriftingMine.class, new RenderEntityDriftingMine());
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellGravityTrap.class, new RenderEntityGravityTrap());
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellFenrirStorm.class, new RenderEntityFenrirStorm());
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellMortar.class, new RenderEntityMortar());
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellWindBlade.class, new RenderEntityWindBlade());
	}
	
	@Override
	public void registerKeyBinds() {
		if (AlfheimCore.enableElvenStory) addESMKeyBinds();
		if (AlfheimCore.enableMMO) addMMOKeyBinds();
	}
	
	@Override
	public void initializeAndRegisterHandlers() {
		super.initializeAndRegisterHandlers();
		MinecraftForge.EVENT_BUS.register(new EventHandlerClient());
		FMLCommonHandler.instance().bus().register(new EventHandlerClient());
		if (AlfheimCore.TravellersGearLoaded) MinecraftForge.EVENT_BUS.register(new TGHandlerBotaniaRenderer());
		if (AlfheimCore.enableElvenStory) enableESMGUIs();
		if (AlfheimCore.enableMMO) enableMMOGUIs();
	}
	
	@Override
	public void postInit() {
		super.postInit();
		AlfheimBotaniaModifiersClient.postInit();
	}
	
	public static void toggelModes(boolean b, boolean esm, boolean mmo, boolean esmOld, boolean mmoOld) {
		if (b) toggleESM(esm, mmo, esmOld, mmoOld);
		else toggleMMO(esm, mmo, esmOld, mmoOld);
	}
	
	public static void enableESM() {
		if (AlfheimCore.enableElvenStory) return;
		AlfheimCore.enableElvenStory = true;
		AlfheimLexiconData.reEnableESM();
		enableESMGUIs();
		addESMKeyBinds();
		EventHandler.checkAddAttrs();
	}
	
	public static void disableESM() {
		if (!AlfheimCore.enableElvenStory) return;
		AlfheimCore.enableElvenStory = false;
		AlfheimLexiconData.disableESM();
		disableESMGUIs();
		removeESMKeyBinds();
		disableMMO();
	}
	
	public static void enableMMO() {
		if (AlfheimCore.enableMMO) return;
		AlfheimCore.enableMMO = true;
		AlfheimLexiconData.reEnableMMO();
		enableMMOGUIs();
		addMMOKeyBinds();
		enableESM();
	}
	
	public static void disableMMO() {
		if (!AlfheimCore.enableMMO) return;
		AlfheimCore.enableMMO = false;
		AlfheimLexiconData.disableMMO();
		disableMMOGUIs();
		removeMMOKeyBinds();
		TimeStopSystemClient.tsAreas.clear();
	}
	
	private static void toggleESM(boolean esm, boolean mmo, boolean esmOld, boolean mmoOld) {
		if (esmOld == esm) return;
		AlfheimCore.enableElvenStory = esm;
		
		if (esm) {
			AlfheimLexiconData.reEnableESM();
			addESMKeyBinds();
		} else {
			AlfheimLexiconData.disableESM();
			removeESMKeyBinds();
			if (mmoOld != mmo) toggleMMO(esm, mmo, esmOld, mmoOld);
		}
	}
	
	private static void toggleMMO(boolean esm, boolean mmo, boolean esmOld, boolean mmoOld) {
		if (mmoOld == mmo) return;
		AlfheimCore.enableMMO = mmo;
		
		if (mmo) {
			AlfheimLexiconData.reEnableMMO();
			enableMMOGUIs();
			addMMOKeyBinds();
			if (mmoOld != esm) toggleESM(esm, mmo, esmOld, mmoOld);
		} else {
			AlfheimLexiconData.disableMMO();
			disableMMOGUIs();
			removeMMOKeyBinds();
			TimeStopSystemClient.tsAreas.clear();
		}
	}
	
	private static void enableESMGUIs() {
		ASJUtilities.log("Registering ESM GUIs");
		MinecraftForge.EVENT_BUS.register(guiRace);
	}
	
	private static void disableESMGUIs() {
		ASJUtilities.log("Unregistering ESM GUIs");
		MinecraftForge.EVENT_BUS.unregister(guiRace);
	}
	
	private static void enableMMOGUIs() {
		ASJUtilities.log("Registering MMO GUIs");
		MinecraftForge.EVENT_BUS.register(guiIceLens);
		MinecraftForge.EVENT_BUS.register(guiParty);
		MinecraftForge.EVENT_BUS.register(guiSpells);
	}
	
	private static void disableMMOGUIs() {
		ASJUtilities.log("Unregistering MMO GUIs");
		MinecraftForge.EVENT_BUS.unregister(guiIceLens);
		MinecraftForge.EVENT_BUS.unregister(guiParty);
		MinecraftForge.EVENT_BUS.unregister(guiSpells);
	}
	
	private static void addESMKeyBinds() {
		addKeyBinding(keyFlight);
	}
	
	private static void removeESMKeyBinds() {
		unregisterKeyBinding(keyFlight);
	}
	
	private static void addMMOKeyBinds() {
		addKeyBinding(keyCast);
		addKeyBinding(keyUnCast);
		addKeyBinding(keySelMob);
		addKeyBinding(keySelTeam);
	}
	
	private static void removeMMOKeyBinds() {
		unregisterKeyBinding(keyCast);
		unregisterKeyBinding(keyUnCast);
		unregisterKeyBinding(keySelMob);
		unregisterKeyBinding(keySelTeam);
	}
	
	private static void unregisterKeyBinding(KeyBinding key) {
		removeKeyBinding(key);
		int id = ASJUtilities.indexOfComparableArray(Minecraft.getMinecraft().gameSettings.keyBindings, key);
		if (id < 0 || id > Minecraft.getMinecraft().gameSettings.keyBindings.length) return;
		Minecraft.getMinecraft().gameSettings.keyBindings = ArrayUtils.remove(Minecraft.getMinecraft().gameSettings.keyBindings, id);
	}
	
	private static void addKeyBinding(KeyBinding key) {
		key.setKeyCode(key.getKeyCodeDefault());
		ClientRegistry.registerKeyBinding(key);
	}
	
	private static void removeKeyBinding(KeyBinding key) {
		key.setKeyCode(0);
	}
}