package alfheim.client.render.entity

import alexsocol.asjlib.mc
import alfheim.api.ModInfo
import alfheim.common.entity.EntitySubspace
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11.*
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
		glPushMatrix()
		glTranslated(par2, par4, par6)
		glRotatef(weapon.rotation, 0f, 1f, 0f)
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		
		mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture)
		//GL11.glPushMatrix();
		
		//GL11.glPopMatrix();
		glDisable(GL_CULL_FACE)
		glShadeModel(GL_SMOOTH)
		glColor4f(1f, 1f, 1f, 1f)
		
		mc.renderEngine.bindTexture(getEntityTexture(weapon))
		
		val tes = Tessellator.instance
		ShaderHelper.useShader(ShaderHelper.halo)
		glRotatef(-90f, 1f, 0f, 0f)
		alexsocol.asjlib.glScalef(if (weapon.ticksExisted < weapon.liveTicks) min(weapon.size, max(0f, (weapon.ticksExisted - weapon.delay) / 10f)) else max(0f, weapon.size - (weapon.ticksExisted - weapon.liveTicks) / 5f))
		
		tes.startDrawingQuads()
		tes.addVertexWithUV(-1.0, 0.0, -1.0, 0.0, 0.0)
		tes.addVertexWithUV(-1.0, 0.0, 1.0, 0.0, 1.0)
		tes.addVertexWithUV(1.0, 0.0, 1.0, 1.0, 1.0)
		tes.addVertexWithUV(1.0, 0.0, -1.0, 1.0, 0.0)
		tes.draw()
		
		ShaderHelper.releaseShader()
		
		glEnable(GL_LIGHTING)
		glShadeModel(GL_FLAT)
		glEnable(GL_CULL_FACE)
		glPopMatrix()
	}
}