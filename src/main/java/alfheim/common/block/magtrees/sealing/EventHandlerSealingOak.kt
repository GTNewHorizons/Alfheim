package alfheim.common.block.magtrees.sealing

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.client.Minecraft
import net.minecraft.client.audio.*
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.sound.PlaySoundEvent17
import net.minecraftforge.common.MinecraftForge
import kotlin.math.*

/**
 * @author WireSegal
 * Created at 8:49 AM on 1/27/16.
 */
class EventHandlerSealingOak {
	
	companion object {
		val instance = EventHandlerSealingOak()
		
		fun register() {
			MinecraftForge.EVENT_BUS.register(instance)
		}
	}
	
	val MAXRANGE = 16
	
	@SubscribeEvent
	fun onSound(event: PlaySoundEvent17) {
		if (Minecraft.getMinecraft().theWorld != null && event.result != null) {
			val world = Minecraft.getMinecraft().theWorld
			if (event.result !is ITickableSound) {
				
				val x = event.result.xPosF
				val y = event.result.yPosF
				val z = event.result.zPosF
				
				var volumeMultiplier = 1f
				
				for (dx in (x - MAXRANGE).toInt()..(x + MAXRANGE).toInt()) {
					for (dy in (y - MAXRANGE).toInt()..(y + MAXRANGE).toInt()) {
						for (dz in (z - MAXRANGE).toInt()..(z + MAXRANGE).toInt()) {
							val block = world.getBlock(dx, dy, dz)
							if (block is ISoundSilencer) {
								val distance = dist(dx, dy, dz, x, y, z)
								if (distance <= MAXRANGE && block.canSilence(world, dx, dy, dz, distance, event)) {
									volumeMultiplier *= block.getVolumeMultiplier(world, dx, dy, dz, distance, event)
								}
							}
						}
					}
				}
				if (volumeMultiplier != 1f) {
					event.result = VolumeModSound(event.result, volumeMultiplier)
				}
			}
		}
		
	}
	
	inner class VolumeModSound(val sound: ISound, val volumeMult: Float): ISound {
		override fun getPositionedSoundLocation(): ResourceLocation = sound.positionedSoundLocation
		override fun canRepeat(): Boolean = sound.canRepeat()
		override fun getRepeatDelay(): Int = sound.repeatDelay
		override fun getVolume(): Float = sound.volume * volumeMult
		override fun getPitch(): Float = sound.pitch
		override fun getXPosF(): Float = sound.xPosF
		override fun getYPosF(): Float = sound.yPosF
		override fun getZPosF(): Float = sound.zPosF
		override fun getAttenuationType(): ISound.AttenuationType = sound.attenuationType
	}
	
	private fun dist(dx: Int, dy: Int, dz: Int, x: Float, y: Float, z: Float): Double {
		val xd = (dx + 0.5 - x).pow(2.0)
		val yd = (dy + 0.5 - y).pow(2.0)
		val zd = (dz + 0.5 - z).pow(2.0)
		return sqrt(xd + yd + zd)
	}
}
