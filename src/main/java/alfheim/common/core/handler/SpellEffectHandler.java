package alfheim.common.core.handler;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.client.render.world.SpellEffectHandlerClient.Spells;
import alfheim.common.network.MessageParticles;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class SpellEffectHandler {
	
	public static void sendPacket(Spells s, Entity e) {
		sendPacket(s, e.dimension, e.posX, e.posY, e.posZ);
	}
	
	public static void sendPacket(Spells s, int dimension, double x, double y, double z) {
		AlfheimCore.network.sendToDimension(new MessageParticles(s.ordinal(), x, y, z), dimension);
	}
	
	public static void sendPacket(Spells s, int dimension, double x, double y, double z, double x2, double y2, double z2) {
		AlfheimCore.network.sendToDimension(new MessageParticles(s.ordinal(), x, y, z, x2, y2, z2), dimension);
	}
}