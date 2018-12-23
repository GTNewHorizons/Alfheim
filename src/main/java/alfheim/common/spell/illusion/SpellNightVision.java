package alfheim.common.spell.illusion;

import alexsocol.asjlib.math.Vector3;
import alfheim.AlfheimCore;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.client.render.world.SpellEffectHandlerClient.Spells;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party;
import alfheim.common.core.handler.SpellEffectHandler;
import alfheim.common.network.MessageEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SpellNightVision extends SpellBase {

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		Party pt = caster instanceof EntityPlayer ? PartySystem.getParty((EntityPlayer) caster) : PartySystem.getMobParty(caster);
		if (pt == null) return SpellCastResult.NOTARGET;
		
		SpellCastResult result = checkCast(caster);
		if (result != SpellCastResult.OK) return result;
		
		for (int i = 0; i < pt.count; i++) {
			EntityLivingBase living = pt.get(i);
			if (living != null && Vector3.entityDistance(living, caster) < 32) {
				living.addPotionEffect(new PotionEffect(Potion.nightVision.id, 36000, -1, true));
				AlfheimCore.network.sendToAll(new MessageEffect(living.getEntityId(), Potion.nightVision.id, 36000, -1));
				SpellEffectHandler.sendPacket(Spells.NVISION, living);
			}
		}
		
		return result;
	}

	@Override
	public EnumRace getRace() {
		return EnumRace.SPRIGGAN;
	}

	@Override
	public String getName() {
		return "nightvision";
	}

	@Override
	public int getManaCost() {
		return 6000;
	}

	@Override
	public int getCooldown() {
		return 1200;
	}

	@Override
	public int castTime() {
		return 30;
	}

	@Override
	public boolean isHard() {
		return false;
	}

	@Override
	public void render(EntityLivingBase caster) {}
}