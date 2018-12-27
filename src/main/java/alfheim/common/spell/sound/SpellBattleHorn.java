package alfheim.common.spell.sound;

import alexsocol.asjlib.math.Vector3;
import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.client.render.world.SpellEffectHandlerClient.Spells;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party;
import alfheim.common.core.handler.SpellEffectHandler;
import alfheim.common.network.MessageEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SpellBattleHorn extends SpellBase {

	public SpellBattleHorn() {
		super("battlehorn", EnumRace.POOKA, 5000, 600, 15);
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
				living.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 36000, 0, true));
				AlfheimCore.network.sendToAll(new MessageEffect(living.getEntityId(), Potion.damageBoost.id, 36000, 0));
				living.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 36000, 0, true));
				AlfheimCore.network.sendToAll(new MessageEffect(living.getEntityId(), Potion.moveSpeed.id, 36000, 0));
				living.addPotionEffect(new PotionEffect(Potion.resistance.id, 36000, 0, true));
				AlfheimCore.network.sendToAll(new MessageEffect(living.getEntityId(), Potion.resistance.id, 36000, 0));
			}
		}
		
		caster.worldObj.playSound(caster.posX, caster.posY, caster.posZ, ModInfo.MODID + ":horn.bhorn", 100.0F, 0.8F + caster.worldObj.rand.nextFloat() * 0.2F, false);
		return result;
	}
}