package alfheim.common.spell.water;

import static org.lwjgl.opengl.GL11.*;

import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.AlfheimCore;
import alfheim.api.entity.EnumRace;
import alfheim.api.lib.LibResourceLocations;
import alfheim.api.spell.SpellBase;
import alfheim.client.render.world.SpellEffectHandlerClient.Spells;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.network.MessageEffect;
import alfheim.common.core.handler.SpellEffectHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public class SpellAquaBind extends SpellBase {

	public SpellAquaBind() {
		super("aquabind", EnumRace.UNDINE, 4000, 600, 15);
	}

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		MovingObjectPosition mop = ASJUtilities.getSelectedBlock(caster, 15, false);
		if (mop == null || mop.typeOfHit == MovingObjectType.MISS) return SpellCastResult.WRONGTGT;
		Vector3 hit = new Vector3(mop.hitVec);
		if (mop.typeOfHit == MovingObjectType.BLOCK) {
			if (mop.sideHit == 0) hit.y -= 0.1;
	        if (mop.sideHit == 1) hit.y += 0.1;
		}
		
		SpellCastResult result = checkCast(caster);
		if (result != SpellCastResult.OK) return result;
		
		List<EntityLivingBase> l = caster.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(hit.x, hit.y, hit.z, hit.x, hit.y, hit.z).expand(3.5, 0.5, 3.5));
		for (EntityLivingBase e : l) {
			if (PartySystem.mobsSameParty(caster, e)) continue;
			Vector3 mob = Vector3.fromEntityCenter(e);
			mob.y = hit.y;
			if (hit.copy().sub(mob).length() <= 3.5) {
				e.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 100, 1, true));
				AlfheimCore.network.sendToAll(new MessageEffect(e.getEntityId(), Potion.moveSlowdown.id, 100, 4));
			}
		}

		SpellEffectHandler.sendPacket(Spells.AQUABIND, caster.dimension, hit.x, hit.y, hit.z);
		return result;
	}

	@Override
	public void render(EntityLivingBase caster) {
		MovingObjectPosition mop = ASJUtilities.getSelectedBlock(caster, 15, false);
		if (mop == null || mop.typeOfHit == MovingObjectType.MISS) return;
		double y = mop.typeOfHit == MovingObjectType.BLOCK ? 0.1 * (mop.sideHit == 0 ? -1 : 1) : 0;
		double s = 3.5;
		glDisable(GL_CULL_FACE);
		//glDisable(GL_ALPHA_TEST);
        glAlphaFunc(GL_GREATER, 0.003921569F);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.target);
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.addVertexWithUV(mop.hitVec.xCoord - s, mop.hitVec.yCoord + y, mop.hitVec.zCoord - s, 0, 0);
		Tessellator.instance.addVertexWithUV(mop.hitVec.xCoord - s, mop.hitVec.yCoord + y, mop.hitVec.zCoord + s, 0, 1);
		Tessellator.instance.addVertexWithUV(mop.hitVec.xCoord + s, mop.hitVec.yCoord + y, mop.hitVec.zCoord + s, 1, 1);
		Tessellator.instance.addVertexWithUV(mop.hitVec.xCoord + s, mop.hitVec.yCoord + y, mop.hitVec.zCoord - s, 1, 0);
		Tessellator.instance.draw();
		glDisable(GL_BLEND);
        glAlphaFunc(GL_GREATER, 0.1F);
		//glEnable(GL_ALPHA_TEST);
		glEnable(GL_CULL_FACE);
	}
}