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

	@Override
	public EnumRace getRace() {
		return EnumRace.SALAMANDER;
	}

	@Override
	public String getName() {
		return "sun";
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