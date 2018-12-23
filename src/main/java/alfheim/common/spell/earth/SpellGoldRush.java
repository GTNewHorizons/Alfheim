package alfheim.common.spell.earth;

import alexsocol.asjlib.math.Vector3;
import alfheim.AlfheimCore;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.client.render.world.SpellEffectHandlerClient.Spells;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.core.handler.SpellEffectHandler;
import alfheim.common.network.MessageEffect;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SpellGoldRush extends SpellBase {

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		Party pt = caster instanceof EntityPlayer ? PartySystem.getParty((EntityPlayer) caster) : PartySystem.getMobParty(caster);
		if (pt == null) return SpellCastResult.NOTARGET;
		
		SpellCastResult result = checkCast(caster);
		if (result != SpellCastResult.OK) return result;
		
		for (int i = 0; i < pt.count; i++) {
			EntityLivingBase living = pt.get(i);
			if (living != null && Vector3.entityDistance(living, caster) < 32) {
				living.addPotionEffect(new PotionEffect(AlfheimRegistry.goldRush.id, 1200, 0, true));
				AlfheimCore.network.sendToAll(new MessageEffect(living.getEntityId(), AlfheimRegistry.goldRush.id, 1200, 0));
				SpellEffectHandler.sendPacket(Spells.UPHEAL, living);
			}
		}
		
		return result;
	}

	@Override
	public EnumRace getRace() {
		return EnumRace.GNOME;
	}

	@Override
	public String getName() {
		return "goldrush";
	}

	@Override
	public int getManaCost() {
		return 7000;
	}

	@Override
	public int getCooldown() {
		return 3000;
	}

	@Override
	public int castTime() {
		return 30;
	}

	@Override
	public boolean isHard() {
		return false;
	}

	@Override
	public void render(EntityLivingBase caster) {}
}