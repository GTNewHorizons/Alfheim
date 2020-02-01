package alfheim.client.render.entity

import alfheim.api.lib.LibResourceLocations
import alfheim.client.model.entity.ModelEntityRook
import alfheim.common.entity.boss.EntityRook
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import vazkii.botania.client.core.handler.BossBarHandler

object RenderEntityRook: RenderLiving(ModelEntityRook(), 1.5f) {
	
	override fun getEntityTexture(entity: Entity): ResourceLocation {
		if (entity is EntityRook) BossBarHandler.setCurrentBoss(entity)
		
		return LibResourceLocations.rook
	}
}