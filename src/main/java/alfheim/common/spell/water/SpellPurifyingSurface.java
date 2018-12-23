package alfheim.common.spell.water;

import static org.lwjgl.opengl.GL11.*;

import java.util.List;

import alexsocol.asjlib.math.Vector3;
import alfheim.api.entity.EnumRace;
import alfheim.api.lib.LibResourceLocations;
import alfheim.api.spell.SpellBase;
import alfheim.client.render.world.SpellEffectHandlerClient.Spells;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.core.handler.SpellEffectHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SpellPurifyingSurface extends SpellBase {

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCast(caster);
		if (result != SpellCastResult.OK) return result;
		
		SpellEffectHandler.sendPacket(Spells.PURE_AREA, caster);

		List<EntityLivingBase> list = caster.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, caster.boundingBox.expand(5, 0.5, 5));
		for (EntityLivingBase living : list) {
			if (!PartySystem.mobsSameParty(caster, living) || Vector3.entityDistancePlane(living, caster) > 5) continue;
			
			living.extinguish();
			living.removePotionEffect(Potion.poison.id);
			living.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 3600, 0, true));
				
			SpellEffectHandler.sendPacket(Spells.PURE, living);
		}
		return result;
	}

	@Override
	public EnumRace getRace() {
		return EnumRace.UNDINE;
	}

	@Override
	public String getName() {
		return "purifyingsurface";
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
	public void render(EntityLivingBase caster) {
		double s = 5;
		glDisable(GL_CULL_FACE);
        glAlphaFunc(GL_GREATER, 0.003921569F);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glTranslated(0, -1.61, 0);
		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.target);
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.addVertexWithUV(caster.posX - s, caster.posY, caster.posZ - s, 0, 0);
		Tessellator.instance.addVertexWithUV(caster.posX - s, caster.posY, caster.posZ + s, 0, 1);
		Tessellator.instance.addVertexWithUV(caster.posX + s, caster.posY, caster.posZ + s, 1, 1);
		Tessellator.instance.addVertexWithUV(caster.posX + s, caster.posY, caster.posZ - s, 1, 0);
		Tessellator.instance.draw();
		glDisable(GL_BLEND);
        glAlphaFunc(GL_GREATER, 0.1F);
		glEnable(GL_CULL_FACE);
	}
}