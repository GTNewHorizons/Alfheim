package alfheim.client.render.entity

import alexsocol.asjlib.*
import alfheim.api.lib.LibResourceLocations
import alfheim.client.model.entity.ModelSubspaceSpear
import alfheim.common.entity.EntitySubspaceSpear
import alfheim.common.item.AlfheimItems
import net.minecraft.client.renderer.entity.*
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12

object RenderEntitySubspaceSpear: Render() {
	
	override fun doRender(weapon: Entity, d0: Double, d1: Double, d2: Double, par8: Float, par9: Float) {
		weapon as EntitySubspaceSpear
		
		glPushMatrix()
		glEnable(GL12.GL_RESCALE_NORMAL)
		glTranslated(d0, d1, d2)
		if (weapon.type == 0) {
			glTranslatef(0f, -1f, 0f)
			glTranslatef(0f, 1f, 0f)
			glRotatef(weapon.rotation, 0f, 1f, 0f)
			glRotatef(90f, 1f, 0f, 0f)
			glRotatef(weapon.pitch, 1f, 0f, 0f)
			glScalef(1f, -1f, -1f)
			mc.renderEngine.bindTexture(getEntityTexture(weapon))
			ModelSubspaceSpear.render(0.0625f)
		} else {
			glPushMatrix()
			glDisable(GL_CULL_FACE)
			glRotatef(-90f, 0f, 1f, 0f)
			glRotatef(45f, 0f, 0f, 1f)
			glRotatef(weapon.rotation, 1f, 1f, 0f)
			glRotatef(-weapon.pitch, 0f, 0f, 1f)
			glScalef(4f)
			RenderItem.renderInFrame = true
			RenderManager.instance.renderEntityWithPosYaw(EntityItem(weapon.worldObj, 0.0, 0.0, 0.0, ItemStack(AlfheimItems.gungnir)).also { it.hoverStart = 0f }, 0.0, -0.2501, 0.0, 0f, 0f)
			RenderItem.renderInFrame = false
			glEnable(GL_CULL_FACE)
			glPopMatrix()
		}
		glEnable(GL12.GL_RESCALE_NORMAL)
		glPopMatrix()
	}
	
	override fun getEntityTexture(entity: Entity) = if ((entity as? EntitySubspaceSpear)?.type == 1) TextureMap.locationItemsTexture else LibResourceLocations.spearSubspace
	
}