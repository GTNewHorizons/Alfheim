package alfheim.common.spell.sound;

import java.util.List;

import alexsocol.asjlib.math.Vector3;
import alfheim.AlfheimCore;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.network.MessageEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SpellDragonGrowl extends SpellBase {

	public SpellDragonGrowl() {
		super("dragongrowl", EnumRace.POOKA, 12000, 2400, 20);
	}

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCast(caster);
		if (result != SpellCastResult.OK) return result;
		
		List<EntityLivingBase> list = caster.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, caster.boundingBox.expand(8, 8, 8));
		for (EntityLivingBase living : list) {
			if (PartySystem.mobsSameParty(caster, living) || Vector3.entityDistance(living, caster) > 16) continue;
			living.addPotionEffect(new PotionEffect(Potion.blindness.id, 100, 0, true));
			AlfheimCore.network.sendToAll(new MessageEffect(living.getEntityId(), Potion.blindness.id, 100, 0));
			living.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 100, 5, true));
			AlfheimCore.network.sendToAll(new MessageEffect(living.getEntityId(), Potion.moveSlowdown.id, 100, 5));
			living.addPotionEffect(new PotionEffect(Potion.weakness.id, 100, 2, true));
			AlfheimCore.network.sendToAll(new MessageEffect(living.getEntityId(), Potion.moveSlowdown.id, 100, 2));
		}
		caster.worldObj.playSoundEffect(caster.posX, caster.posY, caster.posZ, "mob.enderdragon.growl", 100.0F, 0.8F + caster.worldObj.rand.nextFloat() * 0.2F);
		return result;
	}
}