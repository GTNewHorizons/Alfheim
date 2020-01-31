package alfheim.client.render.entity

import alfheim.api.ModInfo
import alfheim.client.model.entity.ModelSubspaceSpear
import alfheim.common.entity.EntitySubspaceSpear
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.*

object RenderEntitySubspaceSpear: Render() {
	
	val model = ModelSubspaceSpear()
	val SPEAR_TEXTURES = ResourceLocation(ModInfo.MODID, "textures/model/entity/subspace/spearsubspace.png")
	
	override fun doRender(weapon: Entity, d0: Double, d1: Double, d2: Double, par8: Float, par9: Float) {
		weapon as EntitySubspaceSpear
		GL11.glPushMatrix()
		GL11.glEnable(GL12.GL_RESCALE_NORMAL)
		GL11.glColor4f(1f, 1f, 1f, 1f)
		GL11.glTranslated(d0, d1, d2)
		GL11.glTranslatef(0f, -1f, 0f)
		GL11.glTranslatef(0f, 1f, 0f)
		GL11.glRotatef(weapon.rotation, 0f, 1f, 0f)
		GL11.glRotatef(90f, 1f, 0f, 0f)
		GL11.glRotatef(weapon.pitch, 1f, 0f, 0f)
		Minecraft.getMinecraft().renderEngine.bindTexture(SPEAR_TEXTURES)
		GL11.glScalef(1f, -1f, -1f)
		model.render(0.07f)
		GL11.glColor3f(1f, 1f, 1f)
		GL11.glScalef(1f, -1f, -1f)
		GL11.glEnable(GL12.GL_RESCALE_NORMAL)
		GL11.glPopMatrix()
	}
	
	override fun getEntityTexture(entity: Entity) = TextureMap.locationBlocksTexture!!
}