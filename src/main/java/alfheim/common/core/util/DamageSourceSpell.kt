package alfheim.common.core.util

import alfheim.common.entity.spell.*
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.*

open class DamageSourceSpell(type: String): DamageSource(type) {
	
	companion object {
		
		val anomaly = DamageSource("anomaly").setDamageBypassesArmor().setDamageIsAbsolute().setMagicDamage()!!
		val bleeding = DamageSource("bleeding").setDamageBypassesArmor().setDamageIsAbsolute()!!
		val gravity = DamageSourceSpell("gravity").setDamageBypassesArmor().setDifficultyScaled()!!
		val mark = DamageSourceSpell("mark").setDamageBypassesArmor().setDamageIsAbsolute().setMagicDamage()!!
		val poison = DamageSourceSpell("poison").setDamageBypassesArmor()!!
		val possession = DamageSourceSpell("possession").setDamageBypassesArmor().setDamageIsAbsolute().setMagicDamage()!!
		val sacrifice = DamageSourceSpell("sacrifice").setDamageBypassesArmor().setDamageIsAbsolute().setMagicDamage()!!
		val soulburn = DamageSourceSpell("soulburn").setDamageBypassesArmor().setMagicDamage()!!
		
		fun blades(wb: EntitySpellWindBlade, caster: EntityLivingBase?) =
			EntityDamageSourceIndirectSpell("windblade", caster, wb).setDamageBypassesArmor()!!
		
		/** Sacrifice type of damage to attack other mobs  */
		fun darkness(attacker: EntityLivingBase?) =
			EntityDamageSourceSpell("darkness_FF", attacker).setDamageBypassesArmor().setDamageIsAbsolute().setMagicDamage()!!
		
		fun explosion(dm: EntitySpellDriftingMine, caster: EntityLivingBase?) =
			EntityDamageSourceIndirectSpell("explosion.player", caster, dm).setDifficultyScaled().setExplosion()!!
		
		fun fireball(fb: EntitySpellFireball, caster: EntityLivingBase?) =
			EntityDamageSourceIndirectSpell("fireball", caster, fb).setFireDamage().setExplosion().setProjectile()!!
		
		fun firewall(fw: EntitySpellFirewall, caster: EntityLivingBase?) =
			EntityDamageSourceIndirectSpell("firewall", caster, fw).setFireDamage()!!
		
		/** Fenrir Storm type of damage  */
		fun lightning(st: EntitySpellFenrirStorm, caster: EntityLivingBase?) =
			EntityDamageSourceIndirectSpell("lightning", caster, st).setDamageBypassesArmor().setFireDamage()!!
		
		fun missile(im: EntitySpellIsaacMissile, caster: EntityLivingBase?) =
			EntityDamageSourceIndirectSpell("missile", caster, im).setMagicDamage()!!
		
		fun mortar(mt: EntitySpellMortar, caster: EntityLivingBase?) =
			EntityDamageSourceIndirectSpell("fallingBlock", caster, mt).setDifficultyScaled().setProjectile()!!
		
		/** Some water blades (?) type of damage  */
		fun water(caster: EntityLivingBase?) = EntityDamageSourceSpell("water", caster).setDamageBypassesArmor()!!
	}
}

open class EntityDamageSourceSpell(source: String, protected val attacker: Entity?): DamageSourceSpell(source) {
	
	override fun getEntity() = attacker
	
	override fun func_151519_b(target: EntityLivingBase): IChatComponent {
		val itemstack = if (attacker is EntityLivingBase) attacker.heldItem else null
		val s = "death.attack.$damageType"
		val s1 = "$s.item"
		return if (itemstack != null && itemstack.hasDisplayName() && StatCollector.canTranslate(s1)) ChatComponentTranslation(s1, target.func_145748_c_(), attacker!!.func_145748_c_(), itemstack.func_151000_E()) else ChatComponentTranslation(s, target.func_145748_c_(), attacker!!.func_145748_c_())
	}
	
	override fun isDifficultyScaled() =
		attacker != null && attacker is EntityLivingBase && attacker !is EntityPlayer
}

class EntityDamageSourceIndirectSpell(type: String, attacker: Entity?, private val directEntity: Entity?): EntityDamageSourceSpell(type, attacker) {
	
	override fun getSourceOfDamage() = directEntity
	
	override fun func_151519_b(target: EntityLivingBase): IChatComponent {
		val ichatcomponent = if (directEntity == null) attacker!!.func_145748_c_() else directEntity.func_145748_c_()
		val itemstack = if (directEntity is EntityLivingBase) directEntity.heldItem else null
		val s = "death.attack.$damageType"
		val s1 = "$s.item"
		return if (itemstack != null && itemstack.hasDisplayName() && StatCollector.canTranslate(s1)) ChatComponentTranslation(s1, target.func_145748_c_(), ichatcomponent, itemstack.func_151000_E()) else ChatComponentTranslation(s, target.func_145748_c_(), ichatcomponent)
	}
}