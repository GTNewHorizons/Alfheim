package alfheim.common.spell.nature;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.client.render.world.SpellEffectHandlerClient.Spells;
import alfheim.common.core.handler.CardinalSystem.TargetingSystem;
import alfheim.common.core.handler.CardinalSystem.TargetingSystem.Target;
import alfheim.common.core.handler.SpellEffectHandler;
import alfheim.common.network.MessageEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SpellUphealth extends SpellBase {

	public SpellUphealth() {
		super("uphealth", EnumRace.CAITSITH, 10000, 1200, 30);
	}

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
			tg.target.addPotionEffect(new PotionEffect(Potion.field_76434_w.id, 36000, 1, true));
			AlfheimCore.network.sendToAll(new MessageEffect(tg.target.getEntityId(), Potion.field_76434_w.id, 36000, 1));
			SpellEffectHandler.sendPacket(Spells.UPHEAL, tg.target);
		}
		
		return result;
	}
}