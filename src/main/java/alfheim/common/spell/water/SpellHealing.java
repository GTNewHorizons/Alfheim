package alfheim.common.spell.water;

import alexsocol.asjlib.math.Vector3;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.client.render.world.SpellEffectHandlerClient.Spells;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party;
import alfheim.common.core.handler.SpellEffectHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class SpellHealing extends SpellBase {

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		Party pt = caster instanceof EntityPlayer ? PartySystem.getParty((EntityPlayer) caster) : PartySystem.getMobParty(caster);
		if (pt == null) return SpellCastResult.NOTARGET;
		
		SpellCastResult result = checkCast(caster);
		if (result != SpellCastResult.OK) return result;
		
		for (int i = 0; i < pt.count; i++) {
			EntityLivingBase living = pt.get(i);
			if (living != null && Vector3.entityDistance(living, caster) < 32) {
				living.heal(5.0F);
				SpellEffectHandler.sendPacket(Spells.HEAL, living);
			}
		}
		
		return result;
	}

	@Override
	public EnumRace getRace() {
		return EnumRace.UNDINE;
	}

	@Override
	public String getName() {
		return "healing";
	}

	@Override
	public int getManaCost() {
		return 2000;
	}

	@Override
	public int getCooldown() {
		return 200;
	}

	@Override
	public int castTime() {
		return 10;
	}

	@Override
	public boolean isHard() {
		return false;
	}

	@Override
	public void render(EntityLivingBase caster) {}
}