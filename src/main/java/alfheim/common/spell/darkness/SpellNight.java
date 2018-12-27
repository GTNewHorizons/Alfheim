package alfheim.common.spell.darkness;

import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class SpellNight extends SpellBase {

	public SpellNight() {
		super("night", EnumRace.IMP, 30000, 6000, 50);
	}

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCast(caster);
		if (result != SpellCastResult.OK) return result;
		
		for (WorldServer world : MinecraftServer.getServer().worldServers) {
			long time = world.getWorldTime() % 24000;
			world.setWorldTime(world.getWorldTime() + ((time < 18000L ? 18000L : 42000L) - time));
		}
		
		return result;
	}
}