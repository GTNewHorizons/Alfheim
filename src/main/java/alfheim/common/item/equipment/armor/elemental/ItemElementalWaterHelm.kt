package alfheim.common.item.equipment.armor.elemental

import alfheim.common.core.util.mfloor
import cpw.mods.fml.relauncher.*
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.potion.*
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import vazkii.botania.api.mana.*

open class ItemElementalWaterHelm: ElementalArmor, IManaDiscountArmor {
	
	constructor(): super(0, "ElementalWaterHelm")
	constructor(name: String): super(0, name)
	
	override fun getPixieChance(stack: ItemStack): Float {
		return 0.11f
	}
	
	override fun getDiscount(stack: ItemStack, slot: Int, player: EntityPlayer): Float {
		return if (hasArmorSet(player)) 0.1f else 0f
	}
	
	override fun onArmorTick(world: World, player: EntityPlayer, stack: ItemStack?) {
		if (!world.isRemote && armorType == 0 && player.getCurrentArmor(3) != null && player.getCurrentArmor(3).item === this) {
			if (world.getBlock(player.posX.mfloor(), player.posY.mfloor() + 1, player.posZ.mfloor()).material == Material.water && ManaItemHandler.requestManaExact(player.getCurrentArmor(3), player, 1, !world.isRemote)) {
				player.addPotionEffect(PotionEffect(Potion.waterBreathing.id, 5, -1))
				player.addPotionEffect(PotionEffect(Potion.nightVision.id, 5, -1))
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	override fun addInformation(stack: ItemStack?, player: EntityPlayer?, list: MutableList<Any?>, b: Boolean) {
		list.add(StatCollector.translateToLocal("item.ElementalArmor.desc4"))
		super.addInformation(stack, player, list, b)
	}
}
