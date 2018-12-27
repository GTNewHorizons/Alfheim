package alfheim.common.spell.wind;

import alexsocol.asjlib.math.Vector3;
import alfheim.AlfheimCore;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.api.spell.SpellBase.SpellCastResult;
import alfheim.client.render.world.SpellEffectHandlerClient.Spells;
import alfheim.common.core.handler.SpellEffectHandler;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.entity.spell.EntitySpellFireball;
import alfheim.common.network.MessageEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Vec3;

public class SpellThrow extends SpellBase {

	public SpellThrow() {
		super("throw", EnumRace.SYLPH, 8000, 600, 10);
	}

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCast(caster);
		if (result == SpellCastResult.OK) {
			caster.addPotionEffect(new PotionEffect(AlfheimRegistry.tHrOw.id, 10, 0, true));
			AlfheimCore.network.sendToAll(new MessageEffect(caster.getEntityId(), AlfheimRegistry.tHrOw.id, 10, 0));
			Vector3 v = new Vector3(caster.getLookVec()).negate().mul(0.5);
			SpellEffectHandler.sendPacket(Spells.THROW, caster.dimension, caster.posX, caster.posY, caster.posZ, v.x, v.y, v.z);
		}
		return result;
	}
}