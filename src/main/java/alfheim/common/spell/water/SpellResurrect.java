package alfheim.common.spell.water;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.api.spell.SpellBase.SpellCastResult;
import alfheim.client.render.world.SpellEffectHandlerClient.Spells;
import alfheim.common.core.handler.SpellEffectHandler;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.core.handler.CardinalSystem.TargetingSystem;
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party;
import alfheim.common.core.handler.CardinalSystem.TargetingSystem.Target;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.network.MessageEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SpellResurrect extends SpellBase {

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		if (!(caster instanceof EntityPlayer)) return SpellCastResult.NOTARGET; // TODO add targets for mobs
		
		Target tg = TargetingSystem.getTarget((EntityPlayer) caster);
		if (tg == null || tg.target == null) 
			return SpellCastResult.NOTARGET;
			
		if (!tg.isParty || !tg.target.isPotionActive(AlfheimRegistry.leftFlame))
			return SpellCastResult.WRONGTGT;
		
		if (tg.target != caster && !ASJUtilities.isInFieldOfVision(tg.target, caster)) return SpellCastResult.NOTSEEING;
		
		SpellCastResult result = checkCast(caster);
		if (result == SpellCastResult.OK) {
			tg.target.addPotionEffect(new PotionEffect(AlfheimRegistry.leftFlame.id, 0, 10, true));
			AlfheimCore.network.sendToAll(new MessageEffect(tg.target.getEntityId(), Potion.field_76434_w.id, 0, 10));
			SpellEffectHandler.sendPacket(Spells.UPHEAL, tg.target);
			Party pt = PartySystem.getMobParty(caster);
			if (pt != null) pt.setDead(tg.target, false);
		}
		
		return result;
	}

	@Override
	public EnumRace getRace() {
		return EnumRace.UNDINE;
	}

	@Override
	public String getName() {
		return "resurrect";
	}

	@Override
	public int getManaCost() {
		return 256000;
	}

	@Override
	public int getCooldown() {
		return 72000;
	}

	@Override
	public int castTime() {
		return 100;
	}

	@Override
	public boolean isHard() {
		return true;
	}

	@Override
	public void render(EntityLivingBase caster) {}
}