package alfheim.common.item.equipment.tool

import alexsocol.asjlib.D
import alfheim.common.item.ItemMod
import com.google.common.collect.Multimap
import net.minecraft.entity.*
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import vazkii.botania.common.entity.EntityDoppleganger

/**
 * @author WireSegal
 * Created at 5:34 PM on 4/17/16.
 */
class ItemGaiaSlayer: ItemMod("GaiaSlayer") {
	
	init {
		setFull3D()
		setMaxStackSize(1)
	}
	
	override fun hitEntity(stack: ItemStack, target: EntityLivingBase, attacker: EntityLivingBase): Boolean {
		if (target is EntityDoppleganger) {
			if (target.health >= 0.5f)
				target.health = 0.5f
			
			target.mobSpawnTicks = 0
			target.tpDelay = 10000
			target.onDeath(DamageSource.outOfWorld)
			target.setDead()
		}
		return super.hitEntity(stack, target, attacker)
	}
	
	override fun getAttributeModifiers(stack: ItemStack): Multimap<String, AttributeModifier> {
		val multimap = super.getAttributeModifiers(stack) as Multimap<String, AttributeModifier>
		multimap.put(SharedMonsterAttributes.attackDamage.attributeUnlocalizedName, AttributeModifier(field_111210_e, "Weapon modifier", Int.MAX_VALUE.D, 0))
		return multimap
	}
}