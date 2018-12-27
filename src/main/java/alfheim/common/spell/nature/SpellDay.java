package alfheim.common.spell.nature;

import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class SpellDay extends SpellBase {

	public SpellDay() {
		super("day", EnumRace.CAITSITH, 30000, 6000, 50);
	}

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCast(caster);
		if (result != SpellCastResult.OK) return result;
		
		for (WorldServer world : MinecraftServer.getServer().worldServers) {
			long time = world.getWorldTime() % 24000;
			world.setWorldTime(world.getWorldTime() + ((time < 6000L ? 6000L : 30000L) - time));
		}
		
		return result;
	}
}