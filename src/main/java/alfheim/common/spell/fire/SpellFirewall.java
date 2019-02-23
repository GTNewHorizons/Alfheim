package alfheim.common.spell.fire;

import static org.lwjgl.opengl.GL11.*;

import alexsocol.asjlib.ASJUtilities;
import alfheim.api.entity.EnumRace;
import alfheim.api.lib.LibResourceLocations;
import alfheim.api.spell.SpellBase;
import alfheim.common.entity.spell.EntitySpellFirewall;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;

public final class SpellFirewall extends SpellBase {
	
	public SpellFirewall() {
		super("firewall", EnumRace.SALAMANDER, 4000, 200, 15);
	}
	
	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCastOver(caster);
		if (result == SpellCastResult.OK) 
			caster.worldObj.spawnEntityInWorld(new EntitySpellFirewall(caster.worldObj, caster));
		return result;
	}
	
	@Override
	public void render(EntityLivingBase caster) {
		int s = 3;
		//glDisable(GL_ALPHA_TEST);
		glAlphaFunc(GL_GREATER, 0.003921569F);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		ASJUtilities.interpolatedTranslation(caster);
		glRotated(-caster.rotationYaw, 0, 1, 0);
		glTranslated(0, -1.62, 5);
		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.targetq);
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.addVertexWithUV(-s, 0.1, -1, 0, 0);
		Tessellator.instance.addVertexWithUV(-s, 0.1,  1, 0, 1);
		Tessellator.instance.addVertexWithUV( s, 0.1,  1, 1, 1);
		Tessellator.instance.addVertexWithUV( s, 0.1, -1, 1, 0);
		Tessellator.instance.draw();
		glDisable(GL_BLEND);
		glAlphaFunc(GL_GREATER, 0.1F);
		//glEnable(GL_ALPHA_TEST);
	}
}