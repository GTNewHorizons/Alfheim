package alfmod.client.render.entity

import alexsocol.asjlib.glScaled
import alexsocol.asjlib.render.ResourceLocationAnimated
import alfmod.AlfheimModularCore
import alfmod.client.model.entity.ModelBipedGlowing
import net.minecraft.client.renderer.entity.RenderBiped
import net.minecraft.entity.*
import net.minecraft.util.ResourceLocation

object RenderEntityMuspellsun: RenderBiped(ModelBipedGlowing(), 0.5f) {
	
	val texture = ResourceLocationAnimated.local(AlfheimModularCore.MODID, "textures/model/entity/Muspellsun.png")
	
	override fun getEntityTexture(entity: Entity?) = texture
	
	override fun getEntityTexture(entity: EntityLiving?) = getEntityTexture(entity as Entity?)
	
	override fun bindTexture(loc: ResourceLocation?) {
		if (loc is ResourceLocationAnimated) loc.bind() else super.bindTexture(loc)
	}
	
	override fun preRenderCallback(entity: EntityLivingBase?, ticks: Float) {
		glScaled(1.5)
	}
}
