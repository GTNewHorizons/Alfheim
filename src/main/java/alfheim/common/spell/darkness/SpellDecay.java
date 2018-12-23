package alfheim.common.spell.darkness;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.api.spell.SpellBase.SpellCastResult;
import alfheim.client.render.world.SpellEffectHandlerClient.Spells;
import alfheim.common.core.handler.SpellEffectHandler;
import alfheim.common.core.handler.CardinalSystem.TargetingSystem;
import alfheim.common.core.handler.CardinalSystem.TargetingSystem.Target;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.network.MessageEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SpellDecay extends SpellBase {

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		if (!(caster instanceof EntityPlayer)) return SpellCastResult.NOTARGET; // TODO add targets for mobs
		
		Target tg = TargetingSystem.getTarget((EntityPlayer) caster);
		if (tg == null || tg.target == null) 
			return SpellCastResult.NOTARGET;
			
		if (tg.target == caster || tg.isParty)
			return SpellCastResult.WRONGTGT;
		
		if (!ASJUtilities.isInFieldOfVision(tg.target, caster)) return SpellCastResult.NOTSEEING;
		
		SpellCastResult result = checkCast(caster);
		if (result == SpellCastResult.OK) {
			tg.target.addPotionEffect(new PotionEffect(AlfheimRegistry.decay.id, 600, 0, true));
			AlfheimCore.network.sendToAll(new MessageEffect(tg.target.getEntityId(), AlfheimRegistry.decay.id, 600, 0));
			SpellEffectHandler.sendPacket(Spells.DISPEL, tg.target);
		}
		
		return result;
	}

	@Override
	public EnumRace getRace() {
		return EnumRace.IMP;
	}

	@Override
	public String getName() {
		return "decay";
	}

	@Override
	public int getManaCost() {
		return 12000;
	}

	@Override
	public int getCooldown() {
		return 2400;
	}

	@Override
	public int castTime() {
		return 25;
	}

	@Override
	public boolean isHard() {
		return false;
	}

	@Override
	public void render(EntityLivingBase caster) {}
}