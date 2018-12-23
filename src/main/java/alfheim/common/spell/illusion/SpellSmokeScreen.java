package alfheim.common.spell.illusion;

import java.util.List;

import alexsocol.asjlib.math.Vector3;
import alfheim.AlfheimCore;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.client.render.world.SpellEffectHandlerClient.Spells;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.core.handler.SpellEffectHandler;
import alfheim.common.network.MessageEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SpellSmokeScreen extends SpellBase {

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCast(caster);
		if (result != SpellCastResult.OK) return result;
		
		List<EntityLivingBase> list = caster.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, caster.boundingBox.expand(16, 16, 16));
		for (EntityLivingBase living : list) {
			if (PartySystem.mobsSameParty(caster, living) || Vector3.entityDistance(living, caster) > 16) continue;
			living.addPotionEffect(new PotionEffect(Potion.blindness.id, 200, -1, true));
			AlfheimCore.network.sendToAll(new MessageEffect(living.getEntityId(), Potion.blindness.id, 200, -1));
		}
		SpellEffectHandler.sendPacket(Spells.SMOKE, caster);
		return result;
	}

	@Override
	public EnumRace getRace() {
		return EnumRace.SPRIGGAN;
	}

	@Override
	public String getName() {
		return "smokescreen";
	}

	@Override
	public int getManaCost() {
		return 5000;
	}

	@Override
	public int getCooldown() {
		return 600;
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