package alfheim.client.core.handler;

import static org.lwjgl.opengl.GL11.*;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.AlfheimCore;
import alfheim.api.AlfheimAPI;
import alfheim.api.entity.EnumRace;
import alfheim.api.event.EntityUpdateEvent;
import alfheim.api.event.TileUpdateEvent;
import alfheim.api.lib.LibResourceLocations;
import alfheim.api.spell.SpellBase;
import alfheim.client.core.handler.CardinalSystemClient.SpellCastingSystemClient;
import alfheim.client.core.handler.CardinalSystemClient.TimeStopSystemClient;
import alfheim.client.render.entity.ParticleRenderDispatcherBound;
import alfheim.client.render.entity.RenderContributors;
import alfheim.client.render.entity.RenderWings;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.core.util.AlfheimConfig;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import vazkii.botania.client.render.world.SkyblockSkyRenderer;

public class EventHandlerClient {

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onDisconnect(ClientDisconnectionFromServerEvent e) {
		TimeStopSystemClient.tsAreas.clear();
		CardinalSystemClient.segment = null;
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTileUpdate(TileUpdateEvent e) {
		if (!ASJUtilities.isServer() && TimeStopSystemClient.affected(e.tile)) e.setCanceled(true);
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onEntityUpdate(EntityUpdateEvent e) {
		if (!ASJUtilities.isServer() && TimeStopSystemClient.affected(e.entity)) e.setCanceled(true);
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onEntityUpdate(LivingUpdateEvent e) {
		if (!ASJUtilities.isServer() && TimeStopSystemClient.affected(e.entity)) e.setCanceled(true);
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onClientTick(ClientTickEvent e) {
		WorldClient world = Minecraft.getMinecraft().theWorld;
		if (world != null && world.provider.dimensionId == AlfheimConfig.dimensionIDAlfheim && world.provider.getSkyRenderer() == null)
			world.provider.setSkyRenderer(new SkyblockSkyRenderer());
		
		if (CardinalSystemClient.segment != null && Minecraft.getMinecraft().thePlayer == null) CardinalSystemClient.segment.target = null;
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
    public void onPlayerPreRender(RenderPlayerEvent.Pre e) {
		if (e.entityPlayer.isPotionActive(AlfheimRegistry.leftFlame.id)) e.setCanceled(true);
    }
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onPlayerSpecialPostRender(RenderPlayerEvent.Specials.Post e) {
		RenderWings.render(e, e.entityPlayer);
		
		if (e.entityPlayer.getCommandSenderName().equals("AlexSocol"))
			((AbstractClientPlayer) e.entityPlayer).func_152121_a(Type.SKIN, LibResourceLocations.skin);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onPlayerTick(PlayerTickEvent e) {
		if (e.phase == TickEvent.Phase.START && e.side == Side.CLIENT && !Minecraft.getMinecraft().isGamePaused()) {
			KeyBindingHandlerClient.parseKeybindings(e.player);
			SpellCastingSystemClient.tick();
			
			if (CardinalSystemClient.segment.target != null && Minecraft.getMinecraft() != null && Minecraft.getMinecraft().thePlayer != null)
				if (!CardinalSystemClient.segment.target.isEntityAlive() || Vector3.entityDistance(Minecraft.getMinecraft().thePlayer, CardinalSystemClient.segment.target) > (CardinalSystemClient.segment.target instanceof IBossDisplayData ? 128 : 32)) CardinalSystemClient.segment.target = null;
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onBlockOverlay(RenderBlockOverlayEvent e) {
		if (e.overlayType != OverlayType.FIRE) e.setCanceled(e.player.isPotionActive(AlfheimRegistry.noclip));
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onWorldLastRender(RenderWorldLastEvent e) {
		RenderContributors.render(e);
		
		Minecraft.getMinecraft().mcProfiler.startSection("alf-particles");
		ParticleRenderDispatcherBound.dispatch();
		
		if (!AlfheimCore.enableElvenStory) return;
		
		spell: {
			SpellBase spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID);
			if (SpellCastingSystemClient.getCoolDown(spell) > 0) break spell;
			
			glPushMatrix();
			ASJUtilities.interpolatedTranslationReverse(Minecraft.getMinecraft().thePlayer);
    		spell.render(Minecraft.getMinecraft().thePlayer);
    		glPopMatrix();
		}
		
		target: if (CardinalSystemClient.segment().target != null) {
			if (CardinalSystemClient.segment.target.equals(Minecraft.getMinecraft().thePlayer) && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) break target;
			glPushMatrix();
			glDisable(GL_CULL_FACE);
			//glDisable(GL_ALPHA_TEST);
	        glAlphaFunc(GL_GREATER, 0.003921569F);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			if (!CardinalSystemClient.segment.target.equals(Minecraft.getMinecraft().thePlayer)) {
				ASJUtilities.interpolatedTranslationReverse(Minecraft.getMinecraft().thePlayer);
				ASJUtilities.interpolatedTranslation(CardinalSystemClient.segment.target);
			} else {
				glTranslated(0, -(1.5 + Minecraft.getMinecraft().thePlayer.eyeHeight), 0);
			}
			glRotated(Minecraft.getMinecraft().theWorld.getTotalWorldTime() + e.partialTicks, 0, 1, 0);
			glScalef(CardinalSystemClient.segment.target.width, CardinalSystemClient.segment.target.width, CardinalSystemClient.segment.target.width);
			ASJUtilities.glColor1u(CardinalSystemClient.segment.isParty ? 0xFF00FF00 : 0xFFFF0000);
			Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.cross);
			Tessellator.instance.startDrawingQuads();
			Tessellator.instance.addVertexWithUV(-1, 0.1, -1, 0, 0);
			Tessellator.instance.addVertexWithUV(-1, 0.1,  1, 0, 1);
			Tessellator.instance.addVertexWithUV( 1, 0.1,  1, 1, 1);
			Tessellator.instance.addVertexWithUV( 1, 0.1, -1, 1, 0);
			Tessellator.instance.draw();
			glDisable(GL_BLEND);
	        glAlphaFunc(GL_GREATER, 0.1F);
			//glEnable(GL_ALPHA_TEST);
			glEnable(GL_CULL_FACE);
			glColor4d(1, 1, 1, 1);
			glPopMatrix();
		}
		
		TimeStopSystemClient.render();
		
//		glPushMatrix();
//		ASJUtilities.interpolatedTranslationReverse(Minecraft.getMinecraft().thePlayer, e.partialTicks);
//		glTranslated(0, 4, 0);
//		// render
//		glPopMatrix();
	}		

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onClonePlayer(PlayerEvent.Clone e) {
		if (!AlfheimCore.enableElvenStory) return;
		EnumRace r = EnumRace.getByID(((EntityPlayer) e.original).getEntityAttribute(AlfheimAPI.RACE).getAttributeValue());
		((EntityPlayer) e.entityPlayer).getEntityAttribute(AlfheimAPI.RACE).setBaseValue(r.ordinal());
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onItemTooltip(ItemTooltipEvent e) {
		if (GuiScreen.isShiftKeyDown() && e.itemStack.hasTagCompound() && e.showAdvancedItemTooltips) {
			e.toolTip.add("");
			e.toolTip.add("NBT Data:");
			for (String s : ASJUtilities.toString(e.itemStack.getTagCompound()).split("\n")) e.toolTip.add(s);
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onFOV(FOVUpdateEvent e) {
		if (e.entity.getActivePotionEffect(AlfheimRegistry.icelens) != null) e.newfov = 0.1F;
	}
}