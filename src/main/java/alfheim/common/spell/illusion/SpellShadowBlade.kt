package alfheim.common.spell.illusion

import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.VisualEffectHandlerClient
import alfheim.common.core.handler.VisualEffectHandler
import alfheim.common.core.util.*
import net.minecraft.block.Block
import net.minecraft.entity.*
import net.minecraft.util.Vec3
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.EnderTeleportEvent

object SpellShadowBlade: SpellBase("shadowblade", EnumRace.SPRIGGAN, 2000, 80, 10) {
	
	override var damage = 6f
	override var radius = 2.0
	
	override val usableParams = arrayOf(damage, radius)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val res = checkCast(caster)
		if (res != SpellCastResult.OK) return res
		
		val list = caster.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, caster.boundingBox.expand(radius, 0.0, radius)) as MutableList<EntityLivingBase>
		for (e in list)
			for (i in 1..50) if (teleportRandomly(e) {
					it.attackEntityFrom(DamageSourceSpell.shadow(caster), damage)
					VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.SHADOW, e)
				})
				break
		
		return res
	}
	
	fun teleportRandomly(entity: EntityLivingBase, attack: (target: EntityLivingBase) -> Unit = {}): Boolean {
		val d0 = entity.posX + (entity.worldObj.rand.nextDouble() - 0.5) * 64.0
		val d1 = entity.posY + (entity.worldObj.rand.nextInt(64) - 32)
		val d2 = entity.posZ + (entity.worldObj.rand.nextDouble() - 0.5) * 64.0
		return teleportTo(entity, d0, d1, d2, attack)
	}
	
	fun teleportToEntity(entity: EntityLivingBase, target: Entity, attack: (target: EntityLivingBase) -> Unit = {}): Boolean {
		var vec3 = Vec3.createVectorHelper(entity.posX - target.posX, entity.boundingBox.minY + (entity.height / 2.0f) - target.posY + target.eyeHeight.toDouble(), entity.posZ - target.posZ)
		vec3 = vec3.normalize()
		val d0 = 16.0
		val d1 = entity.posX + (entity.worldObj.rand.nextDouble() - 0.5) * 8.0 - vec3.xCoord * d0
		val d2 = entity.posY + (entity.worldObj.rand.nextInt(16) - 8) - vec3.yCoord * d0
		val d3 = entity.posZ + (entity.worldObj.rand.nextDouble() - 0.5) * 8.0 - vec3.zCoord * d0
		return teleportTo(entity, d1, d2, d3, attack)
	}
	
	fun teleportTo(entity: EntityLivingBase, x: Double, y: Double, z: Double, attack: (target: EntityLivingBase) -> Unit = {}): Boolean {
		val event = EnderTeleportEvent(entity, x, y, z, damage)
		if (MinecraftForge.EVENT_BUS.post(event)) return false
		
		val d3 = entity.posX
		val d4 = entity.posY
		val d5 = entity.posZ
		entity.posX = event.targetX
		entity.posY = event.targetY
		entity.posZ = event.targetZ
		var flag = false
		val i = entity.posX.mfloor()
		var j = entity.posY.mfloor()
		val k = entity.posZ.mfloor()
		if (entity.worldObj.blockExists(i, j, k)) {
			var flag1 = false
			while (!flag1 && j > 0) {
				val block: Block = entity.worldObj.getBlock(i, j - 1, k)
				if (block.material.blocksMovement()) {
					flag1 = true
				} else {
					--entity.posY
					--j
				}
			}
			if (flag1) {
				entity.setPosition(entity.posX, entity.posY, entity.posZ)
				if (entity.worldObj.getCollidingBoundingBoxes(entity, entity.boundingBox).isEmpty() && !entity.worldObj.isAnyLiquid(entity.boundingBox)) {
					flag = true
				}
			}
		}
		return if (!flag) {
			entity.setPosition(d3, d4, d5)
			false
		} else {
			val short1: Short = 128
			for (l in 0 until short1) {
				val d6 = l.toDouble() / (short1.toDouble() - 1.0)
				val f = (entity.worldObj.rand.nextFloat() - 0.5f) * 0.2f
				val f1 = (entity.worldObj.rand.nextFloat() - 0.5f) * 0.2f
				val f2 = (entity.worldObj.rand.nextFloat() - 0.5f) * 0.2f
				val d7 = d3 + (entity.posX - d3) * d6 + (entity.worldObj.rand.nextDouble() - 0.5) * entity.width * 2.0
				val d8 = d4 + (entity.posY - d4) * d6 + entity.worldObj.rand.nextDouble() * entity.height
				val d9 = d5 + (entity.posZ - d5) * d6 + (entity.worldObj.rand.nextDouble() - 0.5) * entity.width * 2.0
				entity.worldObj.spawnParticle("portal", d7, d8, d9, f.toDouble(), f1.toDouble(), f2.toDouble())
			}
			entity.worldObj.playSoundEffect(d3, d4, d5, "mob.endermen.portal", 1.0f, 1.0f)
			entity.playSound("mob.endermen.portal", 1.0f, 1.0f)
			true.also { attack.invoke(entity) }
		}
	}
}