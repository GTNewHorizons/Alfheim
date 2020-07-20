package alfmod.client.model.entity

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfmod.common.entity.EntityRollingMelon
import net.minecraft.client.model.*
import net.minecraft.entity.Entity

class ModelRollingMelon: ModelBase() {
	
	val melon = ModelRenderer(this, 64, 32).apply {
		addBox(-8f, -8f, -8f, 16, 16, 16)
		setRotationPoint(0f, 0f, 0f)
	}
	
	override fun render(entity: Entity?, f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
		setRotationAngles(f, f1, f2, f3, f4, f5, entity)
		melon.render(f5)
	}
	
	override fun setRotationAngles(f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float, entity: Entity?) {
		if (entity !is EntityRollingMelon) return
		
		val ticks = mc.timer.renderPartialTicks
		entity.rotation += Vector3(entity.motionX * ticks, 0, entity.motionZ * ticks).length().F
		melon.rotateAngleY = entity.rotation
	}
}
