package alfheim.client.render.entity

import alfheim.api.lib.LibResourceLocations
import net.minecraft.client.model.ModelBase
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.entity.Entity

class RenderEntityElf(model: ModelBase, shadowSize: Float): RenderLiving(model, shadowSize) {
	override fun getEntityTexture(par1Entity: Entity) = LibResourceLocations.elf
}
