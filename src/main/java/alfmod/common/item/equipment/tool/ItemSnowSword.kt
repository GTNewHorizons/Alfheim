package alfmod.common.item.equipment.tool

import alexsocol.asjlib.meta
import alfheim.common.item.AlfheimItems
import alfheim.common.item.material.ElvenResourcesMetas
import alfmod.common.core.helper.IconHelper
import alfmod.common.core.util.AlfheimModularTab
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.potion.*
import net.minecraft.util.IIcon
import net.minecraftforge.common.util.EnumHelper
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword

class ItemSnowSword: ItemManasteelSword(snow, "SnowSword") {
	
	lateinit var katanaIcon: IIcon
	
	companion object {
		
		val snow = EnumHelper.addToolMaterial("Snow", 0, 860, 15f, 4f, 16)!!
		
		lateinit var snice: IIcon
	}
	
	init {
		creativeTab = AlfheimModularTab
	}
	
	override fun getIsRepairable(stack: ItemStack?, material: ItemStack) =
		material.item === AlfheimItems.elvenResource && material.meta == ElvenResourcesMetas.NiflheimPowerIngot
	
	override fun hitEntity(stack: ItemStack, target: EntityLivingBase, attacker: EntityLivingBase): Boolean {
		target.addPotionEffect(PotionEffect(Potion.moveSlowdown.id, 160, 1, true))
		
		return super.hitEntity(stack, target, attacker)
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		itemIcon = IconHelper.forName(reg, "SnowKatana")
		katanaIcon = IconHelper.forName(reg, "Katana")
		
		snice = IconHelper.forName(reg, "misc/snice")
	}
	
	override fun getIconIndex(stack: ItemStack) = if (stack.displayName.trim().equals("chunchunmaru", true)) katanaIcon else itemIcon
}
