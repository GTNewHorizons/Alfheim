package alfmod.common.item.equipment.armor

import alfheim.common.core.util.AlfheimTab
import alfmod.AlfheimModularCore
import alfmod.common.core.helper.IconHelper
import alfmod.common.item.AlfheimModularItems
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import net.minecraftforge.common.util.EnumHelper
import vazkii.botania.common.Botania
import vazkii.botania.common.core.handler.ConfigHandler
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor

open class ItemSnowArmor(type: Int, name: String): ItemManasteelArmor(type, name, snow) {
	
	companion object {
		val snow = EnumHelper.addArmorMaterial("snow", 25, intArrayOf(2, 6, 5, 2), 16)!!
	}
	
	init {
		creativeTab = AlfheimTab
	}
	
	override fun getArmorTextureAfterInk(stack: ItemStack?, slot: Int) =
		"${AlfheimModularCore.MODID}:textures/model/snow${if (ConfigHandler.enableArmorModels) "New" else if (slot == 2) "1" else "0"}.png"
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		itemIcon = IconHelper.forItem(reg, this)
	}
	
	override fun getIsRepairable(par1ItemStack: ItemStack?, par2ItemStack: ItemStack?) = false
	
	val armorSet = arrayOf(ItemStack(AlfheimModularItems.snowHelmet), ItemStack(AlfheimModularItems.snowChest), ItemStack(AlfheimModularItems.snowLeggings), ItemStack(AlfheimModularItems.snowBoots))
	override fun getArmorSetStacks() = armorSet
	
	override fun hasArmorSetItem(player: EntityPlayer, i: Int): Boolean {
		val stack = player.inventory.armorInventory[3 - i] ?: return false
		when (i) {
			0 -> return stack.item === AlfheimModularItems.snowHelmet || (Botania.thaumcraftLoaded && stack.item === AlfheimModularItems.snowHelmetRevealing)
			1 -> return stack.item === AlfheimModularItems.snowChest
			2 -> return stack.item === AlfheimModularItems.snowLeggings
			3 -> return stack.item === AlfheimModularItems.snowBoots
		}
		return false
	}
	
	override fun getArmorSetName() = StatCollector.translateToLocal("alfmod.armorset.snow.name")!!
	
	override fun getArmorSetTitle(player: EntityPlayer?) =
		"${StatCollector.translateToLocal("botaniamisc.armorset")} $armorSetName (${getSetPiecesEquipped(player)}/${armorSetStacks.size})"
}
