package alfheim.client.event;

import java.lang.reflect.Field;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import alfheim.AlfheimCore;
import alfheim.Constants;
import alfheim.client.entity.render.RenderWings;
import alfheim.client.utils.KeyBindingsUtils;
import alfheim.common.utils.AlfheimConfig;
import alfheim.common.world.dim.alfheim.WorldProviderAlfheim;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import vazkii.botania.client.render.world.SkyblockSkyRenderer;

public class ClientEventHandler {

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	/** Someone told me that this is the best way... */
	public void onClientTick(ClientTickEvent e) {
		WorldClient world = Minecraft.getMinecraft().theWorld;
		if (world != null && world.provider instanceof WorldProviderAlfheim && world.provider.getSkyRenderer() == null) world.provider.setSkyRenderer(new SkyblockSkyRenderer());
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onPlayerSpecialPostRender(RenderPlayerEvent.Specials.Post e) {
		EntityPlayer player = e.entityPlayer;
		if (player.getActivePotionEffect(Potion.invisibility) != null) return;
		if (AlfheimCore.enableElvenStory) RenderWings.render(e, player);
		
		ClientOnEvents.onContributorsRendered(e);
	}
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
		if (e.phase == TickEvent.Phase.START && e.side == Side.CLIENT) {
			KeyBindingsUtils.parseKeybindings(e.player);
		}
	}
	
	@SubscribeEvent
	public void onGUIOpened(GuiOpenEvent e) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		if (AlfheimCore.enableElvenStory && e.gui instanceof GuiGameOver && AlfheimConfig.prolongDeathScreen) ClientOnEvents.onGameOver((GuiGameOver) e.gui);
		
	}
}