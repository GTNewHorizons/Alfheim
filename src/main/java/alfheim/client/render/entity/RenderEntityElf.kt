package alfheim.client.render.entity

import alfheim.api.lib.LibResourceLocations
import alfheim.client.model.entity.ModelEntityElf
import net.minecraft.client.renderer.entity.RenderBiped
import net.minecraft.entity.Entity

object RenderEntityElf: RenderBiped(ModelEntityElf, 0.25f) {
	
	override fun getEntityTexture(par1Entity: Entity?) = LibResourceLocations.elf
}
