package advtitles

import alfheim.AlfheimCore
import alfheim.client.core.proxy.ClientProxy
import alfheim.client.core.util.mc
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.relauncher.Side
import net.minecraft.item.Item
import net.minecraft.world.World

@Mod(modid = "advtitles", name = "AdvancedTitles", version = "1")
class AdvTitlesCore {
	
	lateinit var advTitle: Item
	
	@Mod.EventHandler
	fun preInit(e: FMLPreInitializationEvent) {
		advTitle = ItemAdvancedTitle()
		
		if (e.side == Side.CLIENT) {
			RenderAdvancedTitle
			RenderParticles
		}
	}
	
	companion object {
		fun coinFX(world: World, x: Double, y: Double, z: Double, lifetime: Int = 200, size: Float = 1f, gravity: Float = 1f) {
			if (!world.isRemote) return
			
			if (mc.renderViewEntity != null && mc.effectRenderer != null && (AlfheimCore.proxy as ClientProxy).doParticle(world)) {
				mc.effectRenderer.addEffect(EntityCoinFx(world, x, y, z, size, lifetime, gravity))
			}
		}
	}
}