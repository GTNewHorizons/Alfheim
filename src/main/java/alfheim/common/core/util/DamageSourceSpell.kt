package alfheim.common.core.util

import alfheim.common.entity.spell.*
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.*

open class DamageSourceSpell(type: String): DamageSource(type) {
	
	companion object {
		
		/** Any anomaly */
		val anomaly = DamageSource("anomaly").setDamageBypassesArmor().setDamageIsAbsolute().setMagicDamage()!!
		/** Decay Spell */
		val bleeding = DamageSourceSpell("bleeding").setDamageBypassesArmor().setDamageIsAbsolute()!!
		/** Gravity Trap Spell */
		val gravity = DamageSourceSpell("gravity").setDamageBypassesArmor()!!
		/** Death Mark Spell */
		val mark = DamageSourceSpell("mark").setDamageBypassesArmor().setDamageIsAbsolute().setMagicDamage()!!
		/** Regular poison */
		val poison = DamageSource("poison").setDamageBypassesArmor()!!
		/** Magical poison */
		val poisonMagic = DamageSourceSpell("poison").setDamageBypassesArmor()!!
		/** Tank Mask */
		val possession = DamageSource("possession").setDamageBypassesArmor().setDamageIsAbsolute().setMagicDamage()!!
		/** Sacrifice Spell */
		val sacrifice = DamageSourceSpell("sacrifice").setDamageBypassesArmor().setDamageIsAbsolute().setMagicDamage()!!
		/** Red Flame */
		val soulburn = DamageSource("soulburn").setDamageBypassesArmor().setMagicDamage()!!
		
		/** Sacrifice type of damage to attack other mobs  */
		fun darkness(caster: EntityLivingBase?) =
			EntityDamageSourceSpell("darkness_FF", caster).setDamageBypassesArmor().setDamageIsAbsolute().setMagicDamage()!!
		
		fun explosion(dm: EntitySpellDriftingMine, caster: EntityLivingBase?) =
			EntityDamageSourceIndirectSpell("explosion.player", caster, dm).setExplosion()!!
		
		fun fireball(fb: EntitySpellFireball, caster: EntityLivingBase?) =
			EntityDamageSourceIndirectSpell("fireball", caster, fb).setFireDamage().setExplosion().setProjectile()!!
		
		fun firewall(fw: EntitySpellFirewall, caster: EntityLivingBase?) =
			EntityDamageSourceIndirectSpell("firewall", caster, fw).setFireDamage()!!
		
		fun hammerfall(caster: EntityLivingBase?) =
			EntityDamageSourceSpell("hammerfall", caster).setDamageBypassesArmor().setProjectile()!!
		
		/** Fenrir Storm type of damage  */
		fun lightning(st: EntitySpellFenrirStorm, caster: EntityLivingBase?) =
			EntityDamageSourceIndirectSpell("lightning", caster, st).setDamageBypassesArmor().setFireDamage()!!
		
		fun missile(im: EntitySpellIsaacMissile, caster: EntityLivingBase?) =
			EntityDamageSourceIndirectSpell("missile", caster, im).setMagicDamage()!!
		
		fun mortar(mt: EntitySpellMortar, caster: EntityLivingBase?) =
			EntityDamageSourceIndirectSpell("mortar", caster, mt).setProjectile()!!
		
		fun shadow(caster: EntityLivingBase?) =
			EntityDamageSource("shadow", caster).setDamageBypassesArmor().setMagicDamage()!!
		
		/** Shadow vortex type of damage */
		fun shadowSpell(caster: EntityLivingBase?) =
			EntityDamageSourceSpell("shadow", caster).setDamageBypassesArmor().setMagicDamage()!!
		
		/** Some water blades (?) type of damage  */
		fun water(caster: EntityLivingBase?) =
			EntityDamageSourceSpell("water", caster).setDamageBypassesArmor()!!
		
		fun windblade(wb: EntitySpellWindBlade, caster: EntityLivingBase?) =
			EntityDamageSourceIndirectSpell("windblade", caster, wb).setDamageBypassesArmor()!!
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

class EntityDamageSourceIndirectSpell(type: String, attacker: Entity?, private val projectile: Entity?): EntityDamageSourceSpell(type, attacker) {
	
	override fun getSourceOfDamage() = projectile
	
	override fun func_151519_b(target: EntityLivingBase): IChatComponent {
		val ichatcomponent = if (attacker == null) projectile!!.func_145748_c_() else attacker.func_145748_c_()
		val itemstack = if (attacker is EntityLivingBase) attacker.heldItem else null
		val s = "death.attack.$damageType"
		val s1 = "$s.item"
		return if (itemstack != null && itemstack.hasDisplayName() && StatCollector.canTranslate(s1)) ChatComponentTranslation(s1, target.func_145748_c_(), ichatcomponent, itemstack.func_151000_E()) else ChatComponentTranslation(s, target.func_145748_c_(), ichatcomponent)
	}
}