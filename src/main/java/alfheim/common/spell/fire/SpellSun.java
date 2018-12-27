package alfheim.common.spell.fire;

import alfheim.AlfheimCore;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.common.network.Message3d;
import alfheim.common.network.Message3d.m3d;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class SpellSun extends SpellBase {

	public SpellSun() {
		super("sun", EnumRace.SALAMANDER, 30000, 6000, 50);
	}

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCast(caster);
		if (result != SpellCastResult.OK) return result;
		
		for (WorldServer world : MinecraftServer.getServer().worldServers) {
			int time = caster.worldObj.rand.nextInt(168000) + 12000;
			world.getWorldInfo().setRaining(false);
			world.getWorldInfo().setRainTime(time);
			world.getWorldInfo().setThundering(false);
			world.getWorldInfo().setThunderTime(time);
			AlfheimCore.network.sendToDimension(new Message3d(m3d.WAETHER, 0, time, time), world.provider.dimensionId);
		}
		
		return result;
	}
}