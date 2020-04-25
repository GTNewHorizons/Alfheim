package alfheim.common.spell.sound

import alexsocol.asjlib.*
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.core.handler.CardinalSystem
import alfheim.common.entity.spell.EntitySpellIsaacMissile
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.monster.IMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.AxisAlignedBB

object SpellIsaacStorm: SpellBase("isaacstorm", EnumRace.POOKA, 256000, 72000, 100, true) {
	
	override var damage = 10f
	override var duration = 60
	override var efficiency = 300.0
	
	override val usableParams
		get() = arrayOf(damage, duration, efficiency, radius)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		if (caster !is EntityPlayer) return SpellCastResult.NOTARGET
		
		val tg = CardinalSystem.TargetingSystem.getTarget(caster)
		val tgt = if (tg.isParty) null else tg.target
		
		if (tgt == null && caster.worldObj.getEntitiesWithinAABB(IMob::class.java, AxisAlignedBB.getBoundingBox(caster.posX, caster.posY + 2, caster.posZ, caster.posX, caster.posY + 2, caster.posZ).expand(radius)).isEmpty())
			return SpellCastResult.NOTARGET
		
		val tgc = if (!caster.isSneaking && tgt != null) tgt::class.java else null
		
		val result = checkCast(caster)
		if (result == SpellCastResult.OK) {
			for (i in 1..efficiency.I) {
				EntitySpellIsaacMissile(caster).also {
					it.setPosition(caster.posX + (Math.random() - 0.5) * 0.1, caster.posY + 2.4 + (Math.random() - 0.5) * 0.1, caster.posZ + (Math.random() - 0.5) * 0.1)
					if (tgc != null) it.targetClass = tgc
					else if (tgt != null) {
						it.targetEntity = tgt
						it.userSelected = true
					}
					
					caster.worldObj.spawnEntityInWorld(it)
				}
			}
			
			for (i in 0..3)
				caster.worldObj.playSoundEffect(caster.posX + (Math.random() - 0.5) * 0.1, caster.posY + 2.4 + (Math.random() - 0.5) * 0.1, caster.posZ + (Math.random() - 0.5) * 0.1, "botania:missile", 0.6f, 0.8f + Math.random().F * 0.2f)
		}
		
		return result
	}
}
