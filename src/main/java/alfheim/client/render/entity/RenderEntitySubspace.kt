package alfheim.client.render.entity

import alfheim.api.ModInfo
import alfheim.client.core.util.mc
import alfheim.common.entity.EntitySubspace
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import vazkii.botania.client.core.helper.ShaderHelper
import kotlin.math.*

/**
 * @author ExtraMeteorP, CKATEPTb
 */
object RenderEntitySubspace: Render() {
	
	val textures = Array(6) { ResourceLocation(ModInfo.MODID, "textures/model/entity/subspace/subspace_$it.png") }
	
	override fun getEntityTexture(entity: Entity) = textures[Math.floorMod(entity.ticksExisted, 6)]
	
	override fun doRender(weapon: Entity, par2: Double, par4: Double, par6: Double, par8: Float, par9: Float) {
		weapon as EntitySubspace
		GL11.glPushMatrix()
		GL11.glTranslated(par2, par4, par6)
		GL11.glRotatef(weapon.rotation, 0f, 1f, 0f)
		GL11.glEnable(GL11.GL_BLEND)
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
		
		mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture)
		//GL11.glPushMatrix();
		
		//GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE)
		GL11.glShadeModel(GL11.GL_SMOOTH)
		GL11.glColor4f(1f, 1f, 1f, 1f)
		
		mc.renderEngine.bindTexture(getEntityTexture(weapon))
		
		val tes = Tessellator.instance
		ShaderHelper.useShader(ShaderHelper.halo)
		GL11.glRotatef(-90f, 1f, 0f, 0f)
		if (weapon.ticksExisted < weapon.liveTicks) {
			val s = min(weapon.size, max(0f, (weapon.ticksExisted - weapon.delay) / 10f))
			GL11.glScalef(s, s, s)
		} else {
			val s = max(0f, weapon.size - (weapon.ticksExisted - weapon.liveTicks) / 5f)
			GL11.glScalef(s, s, s)
		}
		
		tes.startDrawingQuads()
		tes.addVertexWithUV(-1.0, 0.0, -1.0, 0.0, 0.0)
		tes.addVertexWithUV(-1.0, 0.0, 1.0, 0.0, 1.0)
		tes.addVertexWithUV(1.0, 0.0, 1.0, 1.0, 1.0)
		tes.addVertexWithUV(1.0, 0.0, -1.0, 1.0, 0.0)
		tes.draw()
		
		ShaderHelper.releaseShader()
		
		GL11.glEnable(GL11.GL_LIGHTING)
		GL11.glShadeModel(GL11.GL_FLAT)
		GL11.glEnable(GL11.GL_CULL_FACE)
		GL11.glPopMatrix()
	}
}