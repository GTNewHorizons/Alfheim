package alfheim.common.spell.nature;

import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class SpellDay extends SpellBase {

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

	@Override
	public EnumRace getRace() {
		return EnumRace.CAITSITH;
	}

	@Override
	public String getName() {
		return "day";
	}

	@Override
	public int getManaCost() {
		return 30000;
	}

	@Override
	public int getCooldown() {
		return 6000;
	}

	@Override
	public int castTime() {
		return 50;
	}

	@Override
	public boolean isHard() {
		return false;
	}

	@Override
	public void render(EntityLivingBase caster) {}
}