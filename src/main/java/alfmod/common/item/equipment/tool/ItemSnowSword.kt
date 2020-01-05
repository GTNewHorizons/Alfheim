package alfmod.common.item.equipment.tool

import alfheim.common.core.util.AlfheimTab
import alfmod.common.core.helper.IconHelper
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.potion.*
import net.minecraftforge.common.util.EnumHelper
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword

class ItemSnowSword: ItemManasteelSword(snow, "SnowSword") {
	
	companion object {
		val snow = EnumHelper.addToolMaterial("Snow", 0, 860, 15f, 4f, 16)!!
	}
	
	init {
		creativeTab = AlfheimTab
	}
	
	override fun hitEntity(stack: ItemStack, target: EntityLivingBase, attacker: EntityLivingBase): Boolean {
		target.addPotionEffect(PotionEffect(Potion.moveSlowdown.id, 160, 1, true))
		
		return super.hitEntity(stack, target, attacker)
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		itemIcon = IconHelper.forItem(reg, this)
	}
}
