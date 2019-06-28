package alfheim.common.item.equipment.armor.elemental

import alfheim.AlfheimCore
import alfheim.common.core.registry.AlfheimItems
import cpw.mods.fml.relauncher.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.potion.*
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import vazkii.botania.api.mana.ManaItemHandler

class ItemElementalEarthChest: ElementalArmor(1, "ElementalEarthChest") {
	init {
		this.creativeTab = AlfheimCore.alfheimTab
	}
	
	override fun getPixieChance(stack: ItemStack): Float {
		return 0.17f
	}
	
	override fun onArmorTick(world: World, player: EntityPlayer?, stack: ItemStack?) {
		if (armorType == 1 && player!!.getCurrentArmor(2) != null && player.getCurrentArmor(2).item === AlfheimItems.elementalChestplate && ManaItemHandler.requestManaExact(player.getCurrentArmor(2), player, 1, !world.isRemote)) player.addPotionEffect(PotionEffect(Potion.resistance.id, 1, 1))
	}
	
	@SideOnly(Side.CLIENT)
	override fun addInformation(stack: ItemStack?, player: EntityPlayer?, list: MutableList<Any?>, b: Boolean) {
		list.add(StatCollector.translateToLocal("item.ElementalArmor.desc3"))
		super.addInformation(stack, player, list, b)
	}
}
