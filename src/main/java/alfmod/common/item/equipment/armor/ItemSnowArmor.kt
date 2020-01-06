package alfmod.common.item.equipment.armor

import alfheim.common.core.util.AlfheimTab
import alfmod.AlfheimModularCore
import alfmod.client.render.model.ModelSnowArmor
import alfmod.common.core.helper.IconHelper
import alfmod.common.item.AlfheimModularItems
import cpw.mods.fml.relauncher.*
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.StatCollector
import net.minecraftforge.common.util.EnumHelper
import vazkii.botania.common.Botania
import vazkii.botania.common.core.handler.ConfigHandler
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor

open class ItemSnowArmor(type: Int, name: String): ItemManasteelArmor(type, name, snow) {
	
	companion object {
		val snow = EnumHelper.addArmorMaterial("snow", 25, intArrayOf(2, 6, 5, 2), 16)!!
		
		var model1: ModelBiped? = null
		var model2: ModelBiped? = null
		var model3: ModelBiped? = null
		
		var model: ModelBiped? = null
	}
	
	init {
		creativeTab = AlfheimTab
	}
	
	override fun getArmorTextureAfterInk(stack: ItemStack?, slot: Int) =
		"${AlfheimModularCore.MODID}:textures/model/snow${if (slot == 3) "0" else if (ConfigHandler.enableArmorModels) "New" else if (slot == 2) "1" else "0"}.png"
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		itemIcon = IconHelper.forItem(reg, this)
	}
	
	override fun getIsRepairable(par1ItemStack: ItemStack?, par2ItemStack: ItemStack?) = false
	
	lateinit var armorSet: Array<ItemStack>
	override fun getArmorSetStacks(): Array<ItemStack> {
		if (!::armorSet.isInitialized) armorSet = arrayOf(ItemStack(AlfheimModularItems.snowHelmet), ItemStack(AlfheimModularItems.snowChest), ItemStack(AlfheimModularItems.snowLeggings), ItemStack(AlfheimModularItems.snowBoots))
		return armorSet
	}
	
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
	
	@SideOnly(Side.CLIENT)
	override fun getArmorModel(living: EntityLivingBase, stack: ItemStack, slot: Int): ModelBiped? {
		if (!ConfigHandler.enableArmorModels) return super.getArmorModel(living, stack, slot)
		
		if (model1 == null) model1 = ModelSnowArmor(1f)
		if (model2 == null) model2 = ModelSnowArmor(0.5f)
		if (model3 == null) model3 = ModelBiped()
		
		model = when (slot) {
			0 -> model2
			1 -> model1
			2 -> model2
			3 -> model3
			else -> model
		}
		
		model!!.bipedHead.showModel = slot == 0
		model!!.bipedHeadwear.showModel = slot == 0
		model!!.bipedBody.showModel = slot == 1 || slot == 2
		model!!.bipedRightArm.showModel = slot == 1
		model!!.bipedLeftArm.showModel = slot == 1
		model!!.bipedRightLeg.showModel = slot == 2 || slot == 3
		model!!.bipedLeftLeg.showModel = slot == 2 || slot == 3
		model!!.isSneak = living.isSneaking
		model!!.isRiding = living.isRiding
		model!!.isChild = living.isChild
		model!!.aimedBow = false
		model!!.heldItemRight = if (living.heldItem != null) 1 else 0
		
		if (living is EntityPlayer && living.itemInUseDuration > 0) {
			val enumaction = living.getItemInUse().itemUseAction
			if (enumaction == EnumAction.block) {
				model!!.heldItemRight = 3
			} else if (enumaction == EnumAction.bow) {
				model!!.aimedBow = true
			}
		}
		
		return model
	}
}
