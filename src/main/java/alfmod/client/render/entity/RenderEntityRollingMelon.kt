package alfmod.client.render.entity

import alexsocol.asjlib.render.ResourceLocationAnimated
import alfmod.AlfheimModularCore
import alfmod.client.model.entity.ModelRollingMelon
import alfmod.common.entity.EntityRollingMelon
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.entity.*
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11.glTranslatef

object RenderEntityRollingMelon: RenderLiving(ModelRollingMelon(), 0.5f) {
	
	val water = ResourceLocation(AlfheimModularCore.MODID, "textures/model/entity/WaterMelon.png")
	val lava = ResourceLocationAnimated.local(AlfheimModularCore.MODID, "textures/model/entity/LavaMelon.png")
	
	override fun getEntityTexture(entity: Entity?) = if ((entity as? EntityRollingMelon)?.isLava == true) lava else water
	
	override fun bindTexture(loc: ResourceLocation?) {
		if (loc is ResourceLocationAnimated) loc.bind() else super.bindTexture(loc)
	}
	
	override fun preRenderCallback(entity: EntityLivingBase?, ticks: Float) {
		glTranslatef(0f, 1f, 0f)
	}
}
