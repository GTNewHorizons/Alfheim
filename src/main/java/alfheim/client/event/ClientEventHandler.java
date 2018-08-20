package alfheim.client.event;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.client.core.utils.KeyBindingsUtils;
import alfheim.client.render.entity.RenderContributors;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.entity.EnumRace;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.render.world.SkyblockSkyRenderer;

public class ClientEventHandler {

	private static final ResourceLocation skin = new ResourceLocation(ModInfo.MODID,
			"textures/model/entity/AlexSocol.png");
	private static final ResourceLocation babylon = new ResourceLocation(LibResources.MISC_BABYLON);

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	/** Someone told me that this is the best way... */
	public void onClientTick(ClientTickEvent e) {
		WorldClient world = Minecraft.getMinecraft().theWorld;
		if (world != null && world.provider.dimensionId == AlfheimConfig.dimensionIDAlfheim && world.provider.getSkyRenderer() == null)
			world.provider.setSkyRenderer(new SkyblockSkyRenderer());
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onPlayerSpecialPostRender(RenderPlayerEvent.Specials.Post e) {
		if (e.entityPlayer.getCommandSenderName().equals("AlexSocol"))
			((AbstractClientPlayer) e.entityPlayer).func_152121_a(Type.SKIN, skin);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onPlayerTick(PlayerTickEvent e) {
		if (e.phase == TickEvent.Phase.START && e.side == Side.CLIENT) {
			KeyBindingsUtils.parseKeybindings(e.player);
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onGUIOpened(GuiOpenEvent e)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		if (AlfheimCore.enableElvenStory && e.gui instanceof GuiGameOver && AlfheimConfig.prolongDeathScreen)
			ClientOnEvents.onGameOver((GuiGameOver) e.gui);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onWorldLastRender(RenderWorldLastEvent e) {
		RenderContributors.render(e);
		
//		{
//			GL11.glPushMatrix();
//			GL11.glDisable(GL11.GL_CULL_FACE);
//			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
//			ASJUtilities.interpolatedTranslationReverse(player, e.partialTicks);
//			GL11.glTranslated(0.5, 85.53, -14.5);
//			GL11.glRotated(180, 1, 0, 0);
//			Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(ModInfo.MODID, "textures/model/entity/Jibril.png"));
//			//(new ModelBipedNew()).render(null, 0, MathHelper.sin(player.ticksExisted / 10F) / 2F, player.ticksExisted, player.rotationYawHead + 180, -player.rotationPitch, 0.0625F);
//			(new ModelBipedNew()).render(null, 0, 0, player.ticksExisted, 0, 0, 0.0625F);
//			GL11.glEnable(GL11.GL_CULL_FACE);
//			GL11.glPopMatrix();
//		}
		
//		glPushMatrix();
//		ASJUtilities.interpolatedTranslationReverse(Minecraft.getMinecraft().thePlayer, e.partialTicks);
//		glTranslated(0, 100, 0);
//		//render
//		glPopMatrix();
		
		
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onClonePlayer(PlayerEvent.Clone e) {
		if (!AlfheimCore.enableElvenStory)
			return;
		EnumRace r = EnumRace
				.fromID(((EntityPlayer) e.original).getEntityAttribute(AlfheimRegistry.RACE).getAttributeValue());
		((EntityPlayer) e.entityPlayer).getEntityAttribute(AlfheimRegistry.RACE).setBaseValue(r.ordinal());
	}
	
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onItemTooltip(ItemTooltipEvent e) {
		if (GuiScreen.isShiftKeyDown() && e.itemStack.hasTagCompound() && e.showAdvancedItemTooltips) {
			e.toolTip.add("");
			e.toolTip.add("NBT Data:");
			for (String s : e.itemStack.getTagCompound().toString().split("\n")) e.toolTip.add(s);
		}
	}
}