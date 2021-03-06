package alfheim.common.spell.illusion

import alexsocol.asjlib.*
import alexsocol.asjlib.security.InteractionSecurity
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.VisualEffectHandlerClient
import alfheim.common.core.handler.*
import alfheim.common.core.util.DamageSourceSpell
import net.minecraft.block.Block
import net.minecraft.entity.EntityLivingBase

object SpellShadowVortex: SpellBase("shadowvortex", EnumRace.SPRIGGAN, 2000, 80, 10) {
	
	override var damage = 6f
	override var radius = 3.0
	
	override val usableParams
		get() = arrayOf(damage, radius)
	
	@Suppress("UNCHECKED_CAST")
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val res = checkCastOver(caster)
		if (res != SpellCastResult.OK) return res
		
		val list = caster.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, caster.boundingBox.expand(radius, 0.0, radius)) as MutableList<EntityLivingBase>
		list.forEach {
			if (it != caster && !CardinalSystem.PartySystem.mobsSameParty(caster, it) && InteractionSecurity.canHurtEntity(caster, it))
				for (i in 1..50)
					if (teleportRandomly(it)) {
						VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.SHADOW, it)
						it.attackEntityFrom(DamageSourceSpell.shadowSpell(caster), over(caster, damage.D))
						VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.SHADOW, it)
						break
					}
			
		}
		
		return res
	}
	
	fun teleportRandomly(entity: EntityLivingBase): Boolean {
		val d0 = entity.posX + (entity.worldObj.rand.nextDouble() - 0.5) * 16.0
		val d1 = entity.posY + (entity.worldObj.rand.nextInt(16) - 8)
		val d2 = entity.posZ + (entity.worldObj.rand.nextDouble() - 0.5) * 16.0
		return teleportTo(entity, d0, d1, d2)
	}
	
	fun teleportTo(entity: EntityLivingBase, x: Double, y: Double, z: Double): Boolean {
		val prevX = entity.posX
		val prevY = entity.posY
		val prevZ = entity.posZ
		entity.posX = x
		entity.posY = y
		entity.posZ = z
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
			entity.setPosition(prevX, prevY, prevZ)
			false
		} else {
			for (l in 0 until 128) {
				val mod = l.D / 127
				
				val px = prevX + (entity.posX - prevX) * mod + (entity.worldObj.rand.nextDouble() - 0.5) * entity.width * 2.0
				val py = prevY + (entity.posY - prevY) * mod + entity.worldObj.rand.nextDouble() * entity.height
				val pz = prevZ + (entity.posZ - prevZ) * mod + (entity.worldObj.rand.nextDouble() - 0.5) * entity.width * 2.0
				
				val mx = (entity.worldObj.rand.nextDouble() - 0.5) * 0.2
				val my = (entity.worldObj.rand.nextDouble() - 0.5) * 0.2
				val mz = (entity.worldObj.rand.nextDouble() - 0.5) * 0.2
				entity.worldObj.spawnParticle("portal", px, py, pz, mx, my, mz)
			}
			entity.worldObj.playSoundEffect(prevX, prevY, prevZ, "mob.endermen.portal", 1f, 1f)
			entity.playSound("mob.endermen.portal", 1f, 1f)
			true
		}
	}
}