package alfheim.common.core.util

import alfheim.common.entity.spell.*
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.*

open class DamageSourceSpell(type: String): DamageSource(type) {
	
	open class EntityDamageSourceSpell(source: String, protected val attacker: Entity?): DamageSourceSpell(source) {
		
		override fun getEntity(): Entity? {
			return this.attacker
		}
		
		override fun func_151519_b(target: EntityLivingBase): IChatComponent {
			val itemstack = if (this.attacker is EntityLivingBase) this.attacker.heldItem else null
			val s = "death.attack." + this.damageType
			val s1 = "$s.item"
			return if (itemstack != null && itemstack.hasDisplayName() && StatCollector.canTranslate(s1)) ChatComponentTranslation(s1, target.func_145748_c_(), this.attacker!!.func_145748_c_(), itemstack.func_151000_E()) else ChatComponentTranslation(s, target.func_145748_c_(), this.attacker!!.func_145748_c_())
		}
		
		override fun isDifficultyScaled(): Boolean {
			return this.attacker != null && this.attacker is EntityLivingBase && this.attacker !is EntityPlayer
		}
	}
	
	class EntityDamageSourceIndirectSpell(type: String, attacker: Entity?, private val indirectEntity: Entity?): EntityDamageSourceSpell(type, attacker) {
		
		override fun getSourceOfDamage(): Entity? {
			return this.indirectEntity
		}
		
		override fun func_151519_b(target: EntityLivingBase): IChatComponent {
			val ichatcomponent = if (this.indirectEntity == null) this.attacker!!.func_145748_c_() else this.indirectEntity.func_145748_c_()
			val itemstack = if (this.indirectEntity is EntityLivingBase) this.indirectEntity.heldItem else null
			val s = "death.attack." + this.damageType
			val s1 = "$s.item"
			return if (itemstack != null && itemstack.hasDisplayName() && StatCollector.canTranslate(s1)) ChatComponentTranslation(s1, target.func_145748_c_(), ichatcomponent, itemstack.func_151000_E()) else ChatComponentTranslation(s, target.func_145748_c_(), ichatcomponent)
		}
	}
	
	companion object {
		
		val bleeding = DamageSource("bleeding").setDamageBypassesArmor().setDamageIsAbsolute()!!
		val corruption = DamageSource("corruption").setDamageBypassesArmor().setDamageIsAbsolute().setMagicDamage()!!
		val gravity = DamageSourceSpell("gravity").setDamageBypassesArmor().setDifficultyScaled()!!
		val mark = DamageSourceSpell("mark").setDamageBypassesArmor().setDamageIsAbsolute().setMagicDamage()!!
		val poison = DamageSourceSpell("poison").setDamageBypassesArmor()!!
		val possession = DamageSourceSpell("possession").setDamageBypassesArmor().setDamageIsAbsolute().setMagicDamage()!!
		val sacrifice = DamageSourceSpell("sacrifice").setDamageBypassesArmor().setDamageIsAbsolute().setMagicDamage()!!
		val soulburn = DamageSourceSpell("soulburn").setDamageBypassesArmor().setMagicDamage()!!
		
		fun blades(wb: EntitySpellWindBlade, caster: EntityLivingBase?): DamageSource {
			return EntityDamageSourceIndirectSpell("windblade", caster, wb).setDamageBypassesArmor()
		}
		
		/** Sacrifice type of damage to attack other mobs  */
		fun darkness(attacker: EntityLivingBase?): DamageSource {
			return EntityDamageSourceSpell("darkness_FF", attacker).setDamageBypassesArmor().setDamageIsAbsolute().setMagicDamage()
		}
		
		fun explosion(dm: EntitySpellDriftingMine, caster: EntityLivingBase?): DamageSource {
			return EntityDamageSourceIndirectSpell("explosion.player", caster, dm).setDifficultyScaled().setExplosion()
		}
		
		fun fireball(fb: EntitySpellFireball, caster: EntityLivingBase?): DamageSource {
			return EntityDamageSourceIndirectSpell("fireball", caster, fb).setFireDamage().setExplosion().setProjectile()
		}
		
		fun firewall(fw: EntitySpellFirewall, caster: EntityLivingBase?): DamageSource {
			return EntityDamageSourceIndirectSpell("firewall", caster, fw).setFireDamage()
		}
		
		/** Fenrir Storm type of damage  */
		fun lightning(st: EntitySpellFenrirStorm, caster: EntityLivingBase?): DamageSource {
			return EntityDamageSourceIndirectSpell("lightning", caster, st).setDamageBypassesArmor().setFireDamage()
		}
		
		fun missile(im: EntitySpellIsaacMissile, caster: EntityLivingBase?): DamageSource {
			return EntityDamageSourceIndirectSpell("missile", caster, im).setMagicDamage()
		}
		
		fun mortar(mt: EntitySpellMortar, caster: EntityLivingBase?): DamageSource {
			return EntityDamageSourceIndirectSpell("fallingBlock", caster, mt).setDifficultyScaled().setProjectile()
		}
		
		/** Some water blades (?) type of damage  */
		fun water(caster: EntityLivingBase?): DamageSource {
			return EntityDamageSourceSpell("water", caster).setDamageBypassesArmor()
		}
	}
}