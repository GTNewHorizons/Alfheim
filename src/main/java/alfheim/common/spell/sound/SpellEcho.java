package alfheim.common.spell.sound;

import java.util.List;

import alexsocol.asjlib.math.Vector3;
import alfheim.AlfheimCore;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.api.spell.SpellBase.SpellCastResult;
import alfheim.client.render.world.SpellEffectHandlerClient.Spells;
import alfheim.common.core.handler.SpellEffectHandler;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.network.MessageEffect;
import alfheim.common.network.MessageParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SpellEcho extends SpellBase {

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCast(caster);
		if (result != SpellCastResult.OK) return result;
		
		List<Entity> list = caster.worldObj.getEntitiesWithinAABB(Entity.class, caster.boundingBox.expand(16, 16, 16));
		for (Entity e: list) {
			if (Vector3.entityDistance(e, caster) > 16) continue;
			if (e instanceof EntityItem) SpellEffectHandler.sendPacket(Spells.ECHO_ITEM, e);
			else if (e instanceof IMob) SpellEffectHandler.sendPacket(Spells.ECHO_MOB, e);
			else if (e instanceof EntityPlayer) SpellEffectHandler.sendPacket(Spells.ECHO_PLAYER, e);
			else if (e instanceof EntityLivingBase) SpellEffectHandler.sendPacket(Spells.ECHO_ENTITY, e);
		}
		
		if (caster instanceof EntityPlayerMP) AlfheimCore.network.sendTo(new MessageParticles(Spells.ECHO.ordinal(), caster.posX, caster.posY, caster.posZ), (EntityPlayerMP) caster);
		return result;
	}

	@Override
	public EnumRace getRace() {
		return EnumRace.POOKA;
	}

	@Override
	public String getName() {
		return "echo";
	}

	@Override
	public int getManaCost() {
		return 400;
	}

	@Override
	public int getCooldown() {
		return 1500;
	}

	@Override
	public int castTime() {
		return 5;
	}

	@Override
	public boolean isHard() {
		return false;
	}

	@Override
	public void render(EntityLivingBase caster) {}
}