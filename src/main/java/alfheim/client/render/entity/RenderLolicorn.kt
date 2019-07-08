package alfheim.client.render.entity

import alfheim.api.lib.LibResourceLocations
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.model.ModelBase
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase

@SideOnly(Side.CLIENT)
class RenderLolicorn(modelBase: ModelBase, shadowSize: Float): RenderLiving(modelBase, shadowSize) {
	
	override fun renderModel(entity: EntityLivingBase, f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
		if (entity.isInvisible) {
			mainModel.setRotationAngles(f, f1, f2, f3, f4, f5, entity)
		} else {
			bindEntityTexture(entity)
			mainModel.render(entity, f, f1, f2, f3, f4, f5)
		}
	}
	
	override fun getEntityTexture(entity: Entity) = LibResourceLocations.lolicorn
}