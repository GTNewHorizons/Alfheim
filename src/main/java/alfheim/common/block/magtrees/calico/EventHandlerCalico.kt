package alfheim.common.block.magtrees.calico

import alexsocol.asjlib.math.Vector3
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.util.MathHelper
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.world.ExplosionEvent
import vazkii.botania.common.core.helper.MathHelper as VMathHelper

class EventHandlerCalico {
	
	companion object {
		val instance = EventHandlerCalico()
		
		fun register() {
			MinecraftForge.EVENT_BUS.register(instance)
		}
	}
	
	val MAXRANGE = 8
	
	@SubscribeEvent
	fun catchExplosionPre(e: ExplosionEvent.Start) {
		val explosiondampeners = mutableListOf<Vector3>()
		
		val eX = MathHelper.floor_double(e.explosion.explosionX)
		val eY = MathHelper.floor_double(e.explosion.explosionY)
		val eZ = MathHelper.floor_double(e.explosion.explosionZ)
		
		if (e.world.getBlock(eX, eY, eZ) is IExplosionDampener) return
		
		for (x in (eX-MAXRANGE)..(eX+MAXRANGE)) {
			for (y in (eY - MAXRANGE)..(eY + MAXRANGE)) {
				for (z in (eZ - MAXRANGE)..(eZ + MAXRANGE)) {
					if (VMathHelper.pointDistanceSpace(x.toDouble(), y.toDouble(), z.toDouble(), eX.toDouble(), eY.toDouble(), eZ.toDouble()) <= 8) {
						val block = e.world.getBlock(x, y, z)
						if (block is IExplosionDampener)
							explosiondampeners.add(Vector3(x.toDouble(), y.toDouble(), z.toDouble()))
					}
				}
			}
		}
			
		if (explosiondampeners.size == 0) return
		
		val dampener = explosiondampeners[e.world.rand.nextInt(explosiondampeners.size)]
		
		e.world.newExplosion(null, dampener.x, dampener.y, dampener.z, e.explosion.explosionSize, false, false)
		e.isCanceled = true
	}
	
	@SubscribeEvent
	fun catchExplosionAfter(e: ExplosionEvent.Detonate) {
		val block = e.world.getBlock(MathHelper.floor_double(e.explosion.explosionX), MathHelper.floor_double(e.explosion.explosionY), MathHelper.floor_double(e.explosion.explosionZ))
		if (block is IExplosionDampener) {
			e.affectedEntities.clear()
			e.affectedBlocks.clear()
		}
	}
}