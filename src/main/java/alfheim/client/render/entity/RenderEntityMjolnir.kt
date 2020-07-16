package alfheim.client.render.entity

import alexsocol.asjlib.F
import alexsocol.asjlib.math.Vector3
import alfheim.common.item.AlfheimItems
import net.minecraft.client.renderer.entity.*
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.*
import kotlin.math.*

class RenderEntityMjolnir: Render() {
	
	override fun doRender(entity: Entity, x: Double, y: Double, z: Double, p_76986_8_: Float, ticks: Float) {
		GL11.glPushMatrix()
		GL11.glTranslated(x, y, z)
		GL11.glEnable(GL12.GL_RESCALE_NORMAL)
		
		val v = Vector3(entity.motionX, entity.motionY, entity.motionZ).normalize()
		
		val yaw = atan(sqrt(v.x * v.x + v.z * v.z) / v.y)
		val pitch = atan(v.z / v.x)
		
		GL11.glDisable(GL11.GL_CULL_FACE)
		GL11.glRotatef(-90f, 0f, 1f, 0f)
		GL11.glRotatef(45f, 0f, 0f, 1f)
		GL11.glRotatef(Math.toDegrees(yaw).F, 1f, 1f, 0f)
		GL11.glRotatef(Math.toDegrees(pitch).F, 0f, 0f, 1f)
		
		RenderItem.renderInFrame = true
		RenderManager.instance.renderEntityWithPosYaw(EntityItem(entity.worldObj, 0.0, 0.0, 0.0, ItemStack(AlfheimItems.mjolnir)).also { it.hoverStart = 0f }, 0.0, -0.2501, 0.0, 0f, 0f)
		RenderItem.renderInFrame = false
		GL11.glEnable(GL11.GL_CULL_FACE)
		
		GL11.glDisable(GL12.GL_RESCALE_NORMAL)
		GL11.glPopMatrix()
	}
	
	override fun getEntityTexture(entity: Entity): ResourceLocation {
		return TextureMap.locationItemsTexture
	}
}