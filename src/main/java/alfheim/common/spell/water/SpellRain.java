package alfheim.common.spell.water;

import alfheim.AlfheimCore;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.common.network.Message3d;
import alfheim.common.network.Message3d.m3d;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class SpellRain extends SpellBase {

	public SpellRain() {
		super("rain", EnumRace.UNDINE, 30000, 6000, 50);
	}

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCast(caster);
		if (result != SpellCastResult.OK) return result;
		
		for (WorldServer world : MinecraftServer.getServer().worldServers) {
			int r = caster.worldObj.rand.nextInt(12000) + 3600, t = caster.worldObj.rand.nextInt(168000) + 12000;
			world.getWorldInfo().setRaining(true);
			world.getWorldInfo().setRainTime(r);
			world.getWorldInfo().setThundering(false);
			world.getWorldInfo().setThunderTime(t);
			AlfheimCore.network.sendToDimension(new Message3d(m3d.WAETHER, 1, r, t), world.provider.dimensionId);
		}
		
		return result;
	}
}