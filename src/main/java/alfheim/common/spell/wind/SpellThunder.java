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

	@Override
	public EnumRace getRace() {
		return EnumRace.SYLPH;
	}

	@Override
	public String getName() {
		return "thunder";
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