package alfmod.client.render.entity

import alfmod.AlfheimModularCore
import alfmod.client.render.model.ModelDedMoroz
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.entity.RenderBiped
import net.minecraft.entity.*
import net.minecraft.util.ResourceLocation

@SideOnly(Side.CLIENT)
class RenderEntityDedMoroz: RenderBiped(ModelDedMoroz(), 0.5f) {
	
	val texture = ResourceLocation("${AlfheimModularCore.MODID}:textures/entity/DedMoroz.png")
	
	override fun getEntityTexture(entity: Entity?) = texture
	override fun getEntityTexture(entity: EntityLiving?) = texture
}
