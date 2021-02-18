package alfheim.client.render.entity

import alexsocol.asjlib.glScaled
import alexsocol.asjlib.math.Vector3
import alfheim.api.lib.LibResourceLocations
import alfheim.client.model.entity.ModelFenrir
import alfheim.common.entity.boss.EntityFenrir
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.entity.*
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import vazkii.botania.client.core.handler.BossBarHandler

object RenderEntityFenrir: RenderLiving(ModelFenrir, 2f) {
	
	init {
		setRenderPassModel(ModelFenrir)
	}
	
	override fun shouldRenderPass(wolf: EntityLivingBase, pass: Int, ticks: Float): Int {
		return if (pass == 0 && wolf is EntityFenrir && wolf.getWolfShaking()) {
			val f1 = wolf.getBrightness(ticks) * wolf.getShadingWhileShaking(ticks)
			bindTexture(getEntityTexture(wolf))
			GL11.glColor3f(f1, 0f, 0f)
			1
		} else {
			-1
		}
	}
	
	override fun handleRotationFloat(entity: EntityLivingBase, ticks: Float) = (entity as? EntityFenrir)?.getTailRotation() ?: 0f
	
	override fun getEntityTexture(entity: Entity?): ResourceLocation {
		if (entity is EntityFenrir && Vector3.fromEntity(entity) != Vector3.zero) BossBarHandler.setCurrentBoss(entity)
		
		glScaled(10.0)
		GL11.glTranslatef(0f, -1.35f, 0f)
		
		return LibResourceLocations.fenrir
	}
}
