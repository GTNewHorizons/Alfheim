package alfheim.common.spell.tech;

import static org.lwjgl.opengl.GL11.*;

import alexsocol.asjlib.ASJUtilities;
import alfheim.api.entity.EnumRace;
import alfheim.api.lib.LibResourceLocations;
import alfheim.api.spell.SpellBase;
import alfheim.common.entity.spell.EntitySpellGravityTrap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public class SpellGravityTrap extends SpellBase {
	
	public SpellGravityTrap() {
		super("gravitytrap", EnumRace.LEPRECHAUN, 10000, 600, 20);
	}
	
	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		MovingObjectPosition mop = ASJUtilities.getSelectedBlock(caster, 15, true);
		if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
			SpellCastResult result = checkCastOver(caster);
			if (result == SpellCastResult.OK) 
				caster.worldObj.spawnEntityInWorld(new EntitySpellGravityTrap(caster.worldObj, caster, mop.hitVec.xCoord, mop.hitVec.yCoord + 0.1, mop.hitVec.zCoord));
			return result;
		}
		return SpellCastResult.WRONGTGT;
	}
	
	@Override
	public void render(EntityLivingBase caster) {
		MovingObjectPosition mop = ASJUtilities.getSelectedBlock(caster, 15, true);
		if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
			int s = 4;
			//glDisable(GL_ALPHA_TEST);
			glAlphaFunc(GL_GREATER, 0.003921569F);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.target);
			Tessellator.instance.startDrawingQuads();
			Tessellator.instance.addVertexWithUV(mop.hitVec.xCoord - s, mop.hitVec.yCoord + 0.1, mop.hitVec.zCoord - s, 0, 0);
			Tessellator.instance.addVertexWithUV(mop.hitVec.xCoord - s, mop.hitVec.yCoord + 0.1, mop.hitVec.zCoord + s, 0, 1);
			Tessellator.instance.addVertexWithUV(mop.hitVec.xCoord + s, mop.hitVec.yCoord + 0.1, mop.hitVec.zCoord + s, 1, 1);
			Tessellator.instance.addVertexWithUV(mop.hitVec.xCoord + s, mop.hitVec.yCoord + 0.1, mop.hitVec.zCoord - s, 1, 0);
			Tessellator.instance.draw();
			glDisable(GL_BLEND);
			glAlphaFunc(GL_GREATER, 0.1F);
			//glEnable(GL_ALPHA_TEST);
		}
	}
}