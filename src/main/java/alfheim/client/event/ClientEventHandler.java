package alfheim.client.event;

import alfheim.common.world.dim.WorldProviderAlfheim;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import vazkii.botania.client.render.world.SkyblockSkyRenderer;

public class ClientEventHandler {

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	/** Someone told me that's this is the best way... */
	public void onClientTick(ClientTickEvent event) {
		WorldClient world = Minecraft.getMinecraft().theWorld;
		if (world != null && world.provider instanceof WorldProviderAlfheim && world.provider.getSkyRenderer() == null) world.provider.setSkyRenderer(new SkyblockSkyRenderer());
	}
}