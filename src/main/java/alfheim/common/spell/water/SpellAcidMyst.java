package alfheim.common.spell.water;

import static org.lwjgl.opengl.GL11.*;

import alfheim.api.entity.EnumRace;
import alfheim.api.lib.LibResourceLocations;
import alfheim.api.spell.SpellBase;
import alfheim.common.entity.spell.EntitySpellAcidMyst;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;

public class SpellAcidMyst extends SpellBase {

	public SpellAcidMyst() {
		super("acidmyst", EnumRace.UNDINE, 8000, 400, 20);
	}

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCast(caster);
		if (result == SpellCastResult.OK) caster.worldObj.spawnEntityInWorld(new EntitySpellAcidMyst(caster.worldObj, caster));
		return result;
	}

	@Override
	public void render(EntityLivingBase caster) {
		double s = 8;
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