package alfheim.common.item.equipment.bauble

import alexsocol.asjlib.ASJUtilities
import alfheim.common.core.util.DamageSourceSpell
import alexsocol.asjlib.security.InteractionSecurity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.potion.*
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.item.equipment.bauble.ItemIcePendant

class ItemSuperIcePendant: ItemIcePendant() {
	
	override fun setUnlocalizedName(name: String): Item {
		return super.setUnlocalizedName("Super${name.capitalize()}")
	}
	
	override fun onWornTick(stack: ItemStack?, entity: EntityLivingBase?) {
		super.onWornTick(stack, entity)
		if (entity !is EntityPlayer) return
		
		val e = ASJUtilities.getMouseOver(entity, 16.0, true)?.entityHit as? EntityLivingBase ?: return
		if (!InteractionSecurity.canHurtEntity(entity, e)) return
		
		if (!ManaItemHandler.requestManaExact(stack, entity, 100, false)) return
		
		if (e.attackEntityFrom(DamageSourceSpell.frost(entity), 1f)) {
			e.addPotionEffect(PotionEffect(Potion.moveSlowdown.id, 100, 1))
			e.addPotionEffect(PotionEffect(Potion.weakness.id, 100, 1))
			
			ManaItemHandler.requestManaExact(stack, entity, 100, true)
		}
	}
}
