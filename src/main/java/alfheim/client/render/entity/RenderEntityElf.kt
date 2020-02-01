package alfheim.client.render.entity

import alfheim.api.lib.LibResourceLocations
import alfheim.client.model.entity.ModelEntityElf
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.entity.Entity

object RenderEntityElf: RenderLiving(ModelEntityElf(), 0.25f) {
	override fun getEntityTexture(par1Entity: Entity) = LibResourceLocations.elf
}
