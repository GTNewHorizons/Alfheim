package alfheim.common.spell.wind;

import alfheim.AlfheimCore;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.common.network.Message3d;
import alfheim.common.network.Message3d.m3d;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class SpellThunder extends SpellBase {

	public SpellThunder() {
		super("thunder", EnumRace.SYLPH, 30000, 6000, 50);
	}

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCast(caster);
		if (result != SpellCastResult.OK) return result;
		
		for (WorldServer world : MinecraftServer.getServer().worldServers) {
			int time = caster.worldObj.rand.nextInt(12000) + 3600;
			world.getWorldInfo().setRaining(true);
			world.getWorldInfo().setRainTime(time);
			world.getWorldInfo().setThundering(true);
			world.getWorldInfo().setThunderTime(time);
			AlfheimCore.network.sendToDimension(new Message3d(m3d.WAETHER, 2, time, time), world.provider.dimensionId);
		}
		
		return result;
	}
}