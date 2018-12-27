package alfheim.common.spell.fire;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

public class SpellDispel extends SpellBase {

	public SpellDispel() {
		super("dispel", EnumRace.SALAMANDER, 1000, 600, 25);
	}

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		if (!(caster instanceof EntityPlayer)) return SpellCastResult.NOTARGET; // TODO add targets for mobs
		
		Target tg = TargetingSystem.getTarget((EntityPlayer) caster);
		if (tg == null || tg.target == null) 
			return SpellCastResult.NOTARGET;
		
		if (tg.target != caster && !ASJUtilities.isInFieldOfVision(tg.target, caster)) return SpellCastResult.NOTSEEING;
			
		SpellCastResult result = checkCast(caster);
		if (result == SpellCastResult.OK) {
			List<PotionEffect> l = new ArrayList<PotionEffect>();
			for (Object o : tg.target.getActivePotionEffects())  if (Potion.potionTypes[((PotionEffect) o).getPotionID()].isBadEffect == tg.isParty) l.add((PotionEffect) o);
						
			if (l.isEmpty()) {
				tg.target.addPotionEffect(new PotionEffect(Potion.confusion.id, 300, 0, true));
				AlfheimCore.network.sendToAll(new MessageEffect(tg.target.getEntityId(), Potion.confusion.id, 300, 0));
			} else {
				for (PotionEffect pe : l) {
					tg.target.removePotionEffect(pe.potionID);
					AlfheimCore.network.sendToAll(new MessageEffect(tg.target.getEntityId(), pe.getPotionID(), 0, 0));
				}
			}
			SpellEffectHandler.sendPacket(Spells.DISPEL, tg.target);
		}
		
		return result;
	}
}