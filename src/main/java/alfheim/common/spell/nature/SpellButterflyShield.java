package alfheim.common.spell.nature;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.common.core.handler.CardinalSystem.TargetingSystem;
import alfheim.common.core.handler.CardinalSystem.TargetingSystem.Target;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.network.MessageEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;

public class SpellButterflyShield extends SpellBase {

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		if (!(caster instanceof EntityPlayer)) return SpellCastResult.NOTARGET; // TODO add targets for mobs
		
		Target tg = TargetingSystem.getTarget((EntityPlayer) caster);
		if (tg == null || tg.target == null) 
			return SpellCastResult.NOTARGET;
			
		if (!tg.isParty)
			return SpellCastResult.WRONGTGT;
		
		if (tg.target != caster && !ASJUtilities.isInFieldOfVision(tg.target, caster)) return SpellCastResult.NOTSEEING;
		
		SpellCastResult result = checkCast(caster);
		if (result == SpellCastResult.OK) {
			tg.target.addPotionEffect(new PotionEffect(AlfheimRegistry.butterShield.id, 6000, 3, true));
			AlfheimCore.network.sendToAll(new MessageEffect(tg.target.getEntityId(), AlfheimRegistry.butterShield.id, 6000, 3));
		}
		
		return result;
	}

	@Override
	public EnumRace getRace() {
		return EnumRace.CAITSITH;
	}

	@Override
	public String getName() {
		return "butterflyshield";
	}

	@Override
	public int getManaCost() {
		return 8000;
	}

	@Override
	public int getCooldown() {
		return 12000;
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