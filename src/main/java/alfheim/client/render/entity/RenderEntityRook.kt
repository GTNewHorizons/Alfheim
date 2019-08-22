package alfheim.client.render.entity

import alfheim.api.lib.LibResourceLocations
import net.minecraft.client.model.ModelBase
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation

class RenderEntityRook(model: ModelBase, shadowSize: Float): RenderLiving(model, shadowSize) {
	
	public override fun getEntityTexture(par1Entity: Entity): ResourceLocation {
		return LibResourceLocations.rook
	}
}