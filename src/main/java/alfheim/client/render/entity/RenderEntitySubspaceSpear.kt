package alfheim.client.render.entity

import alfheim.api.ModInfo
import alfheim.client.core.util.mc
import alfheim.client.model.entity.ModelSubspaceSpear
import alfheim.common.entity.EntitySubspaceSpear
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12

object RenderEntitySubspaceSpear: Render() {
	
	val model = ModelSubspaceSpear()
	val SPEAR_TEXTURES = ResourceLocation(ModInfo.MODID, "textures/model/entity/subspace/spearsubspace.png")
	
	override fun doRender(weapon: Entity, d0: Double, d1: Double, d2: Double, par8: Float, par9: Float) {
		weapon as EntitySubspaceSpear
		glPushMatrix()
		glEnable(GL12.GL_RESCALE_NORMAL)
		glColor4f(1f, 1f, 1f, 1f)
		glTranslated(d0, d1, d2)
		glTranslatef(0f, -1f, 0f)
		glTranslatef(0f, 1f, 0f)
		glRotatef(weapon.rotation, 0f, 1f, 0f)
		glRotatef(90f, 1f, 0f, 0f)
		glRotatef(weapon.pitch, 1f, 0f, 0f)
		mc.renderEngine.bindTexture(SPEAR_TEXTURES)
		glScalef(1f, -1f, -1f)
		model.render(0.07f)
		glColor3f(1f, 1f, 1f)
		glScalef(1f, -1f, -1f)
		glEnable(GL12.GL_RESCALE_NORMAL)
		glPopMatrix()
	}
	
	override fun getEntityTexture(entity: Entity) = TextureMap.locationBlocksTexture!!
}