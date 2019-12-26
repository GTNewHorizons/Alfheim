package alfmod.client.render.model

import net.minecraft.client.model.ModelSkeleton
import net.minecraft.entity.EntityLivingBase

class ModelDedMoroz: ModelSkeleton() {
	
	override fun setLivingAnimations(p_78086_1_: EntityLivingBase?, p_78086_2_: Float, p_78086_3_: Float, p_78086_4_: Float) {
		aimedBow = true
	}
}