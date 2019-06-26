package alfheim.common.item.equipment.armor.elemental

import alfheim.AlfheimCore
import alfheim.common.core.registry.AlfheimItems
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import vazkii.botania.api.mana.ManaItemHandler

class ItemElementalFireLeggings: ElementalArmor(2, "ElementalFireLeggings") {
	init {
		this.creativeTab = AlfheimCore.alfheimTab
	}
	
	override fun getPixieChance(stack: ItemStack): Float {
		return 0.15f
	}
	
	override fun onArmorTick(world: World, player: EntityPlayer?, stack: ItemStack?) {
		if (armorType == 2 && player!!.getCurrentArmor(1) != null && player.getCurrentArmor(1).item === AlfheimItems.elementalLeggings) {
			if ((player.motionX != 0.0 || player.motionZ != 0.0) && ManaItemHandler.requestManaExact(player.getCurrentArmor(1), player, 1, !world.isRemote)) player.addPotionEffect(PotionEffect(Potion.moveSpeed.id, 1, 1))
			if (player.isBurning) player.extinguish()
		}
		
	}
	
	@SideOnly(Side.CLIENT)
	override fun addInformation(stack: ItemStack?, player: EntityPlayer?, list: MutableList<*>, b: Boolean) {
		list.add(StatCollector.translateToLocal("item.ElementalArmor.desc2"))
		super.addInformation(stack, player, list, b)
	}
}
