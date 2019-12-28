package alfmod.client.render.entity

import alfmod.AlfheimModularCore
import cpw.mods.fml.relauncher.*
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.entity.RenderBiped
import net.minecraft.entity.*
import net.minecraft.util.ResourceLocation

@SideOnly(Side.CLIENT)
class RenderEntityDedMoroz: RenderBiped(ModelBiped(), 0.5f) {
	
	val texture = ResourceLocation("${AlfheimModularCore.MODID}:textures/entity/DedMoroz.png")
	
	override fun getEntityTexture(entity: Entity?) = texture
	override fun getEntityTexture(entity: EntityLiving?) = texture
}
