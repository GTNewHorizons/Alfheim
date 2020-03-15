package alfheim.common.spell.sound

import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.core.util.*
import alfheim.common.entity.spell.EntitySpellIsaacMissile
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.monster.IMob
import net.minecraft.util.AxisAlignedBB

object SpellIsaacStorm: SpellBase("isaacstorm", EnumRace.POOKA, 256000, 72000, 100, true) {
	
	override var damage = 10f
	override var duration = 60
	override var efficiency = 300.0
	
	override val usableParams
		get() = arrayOf(damage, duration, efficiency, radius)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		if (caster.worldObj.getEntitiesWithinAABB(IMob::class.java, AxisAlignedBB.getBoundingBox(caster.posX, caster.posY + 2, caster.posZ, caster.posX, caster.posY + 2, caster.posZ).expand(radius)).isEmpty()) return SpellCastResult.WRONGTGT
		
		val result = checkCast(caster)
		if (result == SpellCastResult.OK) {
			var missile: EntitySpellIsaacMissile
			for (i in 1..efficiency.I) {
				missile = EntitySpellIsaacMissile(caster, false)
				missile.setPosition(caster.posX + (Math.random() - 0.5) * 0.1, caster.posY + 2.4 + (Math.random() - 0.5) * 0.1, caster.posZ + (Math.random() - 0.5) * 0.1)
				caster.worldObj.spawnEntityInWorld(missile)
			}
			
			for (i in 0..3)
				caster.worldObj.playSoundEffect(caster.posX + (Math.random() - 0.5) * 0.1, caster.posY + 2.4 + (Math.random() - 0.5) * 0.1, caster.posZ + (Math.random() - 0.5) * 0.1, "botania:missile", 0.6f, 0.8f + Math.random().F * 0.2f)
		}
		
		return result
	}
}
