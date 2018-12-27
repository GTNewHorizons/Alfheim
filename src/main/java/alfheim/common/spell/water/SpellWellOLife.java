package alfheim.common.spell.water;

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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SpellWellOLife extends SpellBase {

	public SpellWellOLife() {
		super("wellolife", EnumRace.UNDINE, 7000, 600, 30);
	}

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		Party pt = caster instanceof EntityPlayer ? PartySystem.getParty((EntityPlayer) caster) : PartySystem.getMobParty(caster);
		if (pt == null) return SpellCastResult.NOTARGET;
		
		SpellCastResult result = checkCast(caster);
		if (result != SpellCastResult.OK) return result;
		
		for (int i = 0; i < pt.count; i++) {
			EntityLivingBase living = pt.get(i);
			if (living != null && Vector3.entityDistance(living, caster) < 32) {
				living.addPotionEffect(new PotionEffect(AlfheimRegistry.wellOLife.id, 1200, 0, true));
				AlfheimCore.network.sendToAll(new MessageEffect(living.getEntityId(), AlfheimRegistry.wellOLife.id, 1200, 0));
			}
		}
		
		SpellEffectHandler.sendPacket(Spells.PURE, caster);
		return result;
	}
}