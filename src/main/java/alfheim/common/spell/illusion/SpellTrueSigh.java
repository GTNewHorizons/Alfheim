package alfheim.common.spell.illusion;

import java.util.ArrayList;
import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.api.spell.SpellBase.SpellCastResult;
import alfheim.client.render.world.SpellEffectHandlerClient.Spells;
import alfheim.common.core.handler.SpellEffectHandler;
import alfheim.common.core.handler.CardinalSystem;
import alfheim.common.core.handler.CardinalSystem.SpellCastingSystem;
import alfheim.common.core.handler.CardinalSystem.TargetingSystem;
import alfheim.common.core.handler.CardinalSystem.TargetingSystem.Target;
import alfheim.common.network.MessageEffect;
import alfheim.common.network.MessageParticles;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SpellTrueSigh extends SpellBase {

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		if (!(caster instanceof EntityPlayerMP)) return SpellCastResult.NOTARGET; // TODO add targets for mobs
		
		Target tg = TargetingSystem.getTarget((EntityPlayer) caster);
		if (tg == null || tg.target == null) 
			return SpellCastResult.NOTARGET;
		
		if (tg.isParty || !(tg.target instanceof EntityPlayer)) return SpellCastResult.WRONGTGT;
		
		if (tg.target != caster && !ASJUtilities.isInFieldOfVision(tg.target, caster)) return SpellCastResult.NOTSEEING;
			
		SpellCastResult result = checkCast(caster);
		if (result == SpellCastResult.OK) {
			int mana = CardinalSystem.ManaSystem.getMana(tg.target);
			AlfheimCore.network.sendTo(new MessageParticles(Spells.MANA.ordinal(), tg.target.getEntityId(), mana, 0), (EntityPlayerMP) caster);
		}
		
		return result;
	}

	@Override
	public EnumRace getRace() {
		return EnumRace.SPRIGGAN;
	}

	@Override
	public String getName() {
		return "truesigh";
	}

	@Override
	public int getManaCost() {
		return 2000;
	}

	@Override
	public int getCooldown() {
		return 2500;
	}

	@Override
	public int castTime() {
		return 40;
	}

	@Override
	public boolean isHard() {
		return false;
	}

	@Override
	public void render(EntityLivingBase caster) {}
}