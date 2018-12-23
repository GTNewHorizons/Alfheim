package alfheim.common.spell.illusion;

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

public class SpellHollowBody extends SpellBase {

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
			tg.target.addPotionEffect(new PotionEffect(Potion.invisibility.id, 3600, 0, true));
			AlfheimCore.network.sendToAll(new MessageEffect(tg.target.getEntityId(), Potion.invisibility.id, 3600, 0));
			SpellEffectHandler.sendPacket(Spells.PURE, tg.target);
		}
		
		return result;
	}

	@Override
	public EnumRace getRace() {
		return EnumRace.SPRIGGAN;
	}

	@Override
	public String getName() {
		return "hollowbody";
	}

	@Override
	public int getManaCost() {
		return 10000;
	}

	@Override
	public int getCooldown() {
		return 1200;
	}

	@Override
	public int castTime() {
		return 20;
	}

	@Override
	public boolean isHard() {
		return false;
	}

	@Override
	public void render(EntityLivingBase caster) {}
}