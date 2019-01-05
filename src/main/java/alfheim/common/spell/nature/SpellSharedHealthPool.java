package alfheim.common.spell.nature;

import java.util.UUID;

import alexsocol.asjlib.math.Vector3;
import alfheim.AlfheimCore;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.api.spell.SpellBase.SpellCastResult;
import alfheim.client.render.world.SpellEffectHandlerClient.Spells;
import alfheim.common.core.handler.SpellEffectHandler;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.network.MessageEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;

public class SpellSharedHealthPool extends SpellBase {

	public SpellSharedHealthPool() {
		super("sharedhp", EnumRace.CAITSITH, 256000, 72000, 100, true);
	}

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		Party pt = caster instanceof EntityPlayer ? PartySystem.getParty((EntityPlayer) caster) : PartySystem.getMobParty(caster);
		if (pt == null) return SpellCastResult.NOTARGET;
		
		SpellCastResult result = checkCast(caster);
		if (result != SpellCastResult.OK) return result;
		
		for (int i = 0; i < pt.count; i++) {
			EntityLivingBase mr = pt.get(i);
			if (mr != null && mr.isPotionActive(AlfheimRegistry.sharedHP.id)) {
				mr.removePotionEffect(AlfheimRegistry.sharedHP.id);
				AlfheimCore.network.sendToAll(new MessageEffect(mr.getEntityId(), AlfheimRegistry.sharedHP.id, 0, 0));
			}
		}
		
		float total = 0, max = 0;
		for (int i = 0; i < pt.count; i++) {
			EntityLivingBase mr = pt.get(i);
			if (mr != null && Vector3.entityDistance(mr, caster) < 32) {
				total += mr.getHealth();
				max += mr.getMaxHealth();
			}
		}
		
		for (int i = 0; i < pt.count; i++) {
			EntityLivingBase mr = pt.get(i);
			if (mr != null && Vector3.entityDistance(mr, caster) < 32) {
				mr.addPotionEffect(new PotionEffect(AlfheimRegistry.sharedHP.id, 36000, (int) max, true));
				AlfheimCore.network.sendToAll(new MessageEffect(mr.getEntityId(), AlfheimRegistry.sharedHP.id, 36000, (int) max));
				SpellEffectHandler.sendPacket(Spells.UPHEAL, mr);
			}
		}
		
		caster.setHealth(total);
		return result;
	}
}