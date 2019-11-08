package alfheim.common.spell.tech

import alexsocol.asjlib.math.*
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.network.MessageEffect
import net.minecraft.entity.*
import net.minecraft.potion.PotionEffect
import kotlin.math.*

class SpellForceShield: SpellBase("shield", EnumRace.LEPRECHAUN, 8000, 1200, 30) {
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val result = checkCast(caster)
		if (result != SpellCastResult.OK) return result
		
		caster.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDShield, 36000, 0, true))
		AlfheimCore.network.sendToAll(MessageEffect(caster.entityId, AlfheimConfigHandler.potionIDShield, 36000, 0))
		
		return result
	}
	
	companion object {
		fun formShield(entity: Entity, x: Double, y: Double, z: Double, ticks: Float, magic: (obb: OrientedBB) -> Unit) {
			val xSide = sin(Math.toRadians(60.0)) * 0.8
			
			Hexagon.body[0].fromParams(0.3, 0.3 * sqrt(3.0), 0.1)
			Hexagon.body[1].fromParams(0.3, 0.3 * sqrt(3.0), 0.1)
			Hexagon.body[2].fromParams(0.3, 0.3 * sqrt(3.0), 0.1)
			
			Hexagon.translate(x, y, z)
			
			for (i in 0..2) {
				val yaw = (120.0 * i) + entity.ticksExisted * 2 % 360 + ticks
				
				Hexagon.reposition(0.0, 0.0, 1.5, 0.0, 0.0, yaw, magic)			// front
				
				Hexagon.reposition(0.0, -0.8, 1.3, 30.0, 0.0, yaw, magic)		// bottom
				Hexagon.reposition(0.0, 0.8, 1.3, -30.0, 0.0, yaw, magic)		// top
				
				Hexagon.reposition(xSide, 0.4, 1.2, -15.0, 30.0, yaw, magic)		// top left
				Hexagon.reposition(-xSide, 0.4, 1.2, -15.0, -30.0, yaw, magic)	// top right
				
				Hexagon.reposition(xSide, -0.4, 1.2, 15.0, 30.0, yaw, magic)		// bottom left
				Hexagon.reposition(-xSide, -0.4, 1.2, 15.0, -30.0, yaw, magic)	// bottom right
			}
			
			Hexagon.translate(-x, -y, -z)
		}
	}
}

object Hexagon {
	
	val body = arrayOf(OrientedBB(0.3, 0.3 * sqrt(3.0), 0.1), OrientedBB(0.3, 0.3 * sqrt(3.0), 0.1), OrientedBB(0.3, 0.3 * sqrt(3.0), 0.1))
	
	fun translate(x: Double, y: Double, z: Double) {
		body.forEach { it.translate(x, y, z) }
	}
	
	fun rotateOX(angle: Double) {
		body.forEach { it.rotateOX(angle) }
	}
	
	fun rotateOY(angle: Double) {
		body.forEach { it.rotateOY(angle) }
	}
	
	fun rotateOZ(angle: Double) {
		body.forEach { it.rotateOZ(angle) }
	}
	
	fun draw() {
		body.forEach { it.drawFaces() }
	}
	
	fun reposition(x: Double, y: Double, z: Double, oX: Double, oY: Double, yaw: Double, magic: (obb: OrientedBB) -> Unit) {
		val (a, _, c) = Vector3(x, 0.0, z).rotate(yaw, Vector3.oY)
		
		body.forEachIndexed { id, it ->
			it.translate(a, y, c)
			it.rotateOZ(120.0 * id)
			it.rotateOX(oX)
			it.rotateOY(oY)
			it.rotateOY(yaw)
			magic.invoke(it)
			it.rotateOY(-yaw)
			it.rotateOY(-oY)
			it.rotateOX(-oX)
			it.rotateOZ(-120.0 * id)
			it.translate(-a, -y, -c)
		}
	}
}