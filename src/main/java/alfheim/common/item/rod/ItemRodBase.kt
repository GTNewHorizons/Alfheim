package alfheim.common.item.rod

import alfheim.AlfheimCore
import alfheim.api.ModInfo
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.world.World
import vazkii.botania.api.mana.IManaUsingItem

abstract class ItemRodBase(name: String): Item(), IManaUsingItem {
	
	init {
		creativeTab = AlfheimCore.alfheimTab
		setTextureName(ModInfo.MODID + ":" + name)
		unlocalizedName = name
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World?, player: EntityPlayer): ItemStack {
		player.setItemInUse(stack, getMaxItemUseDuration(stack))
		return stack
	}
	
	override fun getMaxItemUseDuration(stack: ItemStack?): Int {
		return 40
	}
	
	override fun onEaten(stack: ItemStack, world: World?, player: EntityPlayer?): ItemStack {
		cast(stack, world, player)
		return stack
	}
	
	abstract fun cast(stack: ItemStack, world: World?, player: EntityPlayer?)
	
	override fun usesMana(stack: ItemStack): Boolean {
		return true
	}
}