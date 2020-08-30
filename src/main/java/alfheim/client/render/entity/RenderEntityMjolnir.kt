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
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12
import kotlin.math.*

class RenderEntityMjolnir: Render() {
	
	override fun doRender(entity: Entity, x: Double, y: Double, z: Double, yawOld: Float, ticks: Float) {
		glPushMatrix()
		glTranslated(x, y, z)
		glEnable(GL12.GL_RESCALE_NORMAL)
		
		val v = Vector3(entity.motionX, entity.motionY, entity.motionZ).normalize()
		val (x1, _, z1) = v
		val (x2, _, z2) = Vector3.oZ
		
		val yaw = Math.toDegrees(-atan2(x1 * z2 - z1 * x2, x1 * x2 + z1 * z2)).F // Good DO NOT REMOVE
		val pitch = Math.toDegrees(atan(sqrt(v.x * v.x + v.z * v.z) / v.y)).F - 90f + if (v.y < 0) 180f else 0f // Working but maybe change...
		
		glRotatef(90f, 1f, 0f, 0f)
		glRotatef(yaw, 0f, 0f, 1f)
		glRotatef(pitch, 1f, 0f, 0f)
		
		glDisable(GL_CULL_FACE)
		RenderItem.renderInFrame = true
		RenderManager.instance.renderEntityWithPosYaw(EntityItem(entity.worldObj, 0.0, 0.0, 0.0, ItemStack(AlfheimItems.mjolnir)).also { it.hoverStart = 0f }, 0.0, -0.2501, 0.0, 0f, 0f)
		RenderItem.renderInFrame = false
		glEnable(GL_CULL_FACE)
		
		glDisable(GL12.GL_RESCALE_NORMAL)
		glPopMatrix()
	}
	
	override fun getEntityTexture(entity: Entity): ResourceLocation {
		return TextureMap.locationItemsTexture
	}
}