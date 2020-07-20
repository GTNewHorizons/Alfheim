package alfmod.common.item.equipment.armor

import alexsocol.asjlib.meta
import alfmod.AlfheimModularCore
import alfmod.client.model.armor.ModelArmorVolcano
import alfmod.common.core.helper.IconHelper
import alfmod.common.core.util.AlfheimModularTab
import alfmod.common.item.AlfheimModularItems
import cpw.mods.fml.relauncher.*
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import net.minecraftforge.common.util.EnumHelper
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.Botania
import vazkii.botania.common.core.handler.ConfigHandler
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor

open class ItemVolcanoArmor(type: Int, name: String): ItemManasteelArmor(type, name, volcano) {
	
	companion object {
		private const val MANA_PER_DAMAGE = 70
		
		val volcano = EnumHelper.addArmorMaterial("Volcano", 26, intArrayOf(3, 7, 6, 3), 6)!!
		
		var model: ModelBiped? = null
	}
	
	init {
		creativeTab = AlfheimModularTab
	}
	
	fun repair(stack: ItemStack, world: World, player: EntityPlayer) {
		if (stack.meta > 0 && ManaItemHandler.requestManaExact(stack, player, MANA_PER_DAMAGE * 2, world.isRemote))
			stack.meta = stack.meta - 1
	}
	
	override fun onUpdate(stack: ItemStack, world: World, player: Entity, slot: Int, inHand: Boolean) {
		repair(stack, world, player as? EntityPlayer ?: return)
	}
	
	override fun onArmorTick(world: World, player: EntityPlayer, stack: ItemStack) {
		repair(stack, world, player)
	}
	
	override fun getArmorTextureAfterInk(stack: ItemStack?, slot: Int) =
		"${AlfheimModularCore.MODID}:textures/model/armor/volcano${if (ConfigHandler.enableArmorModels) "New" else if (slot == 2) "1" else "0"}.png"
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		itemIcon = IconHelper.forItem(reg, this)
	}
	
	override fun getIsRepairable(par1ItemStack: ItemStack?, par2ItemStack: ItemStack?) = false
	
	var armorSet: Array<ItemStack>? = null
	
	override fun getArmorSetStacks(): Array<ItemStack> {
		if (armorSet == null)
			armorSet = arrayOf(ItemStack(AlfheimModularItems.volcanoHelmet), ItemStack(AlfheimModularItems.volcanoChest), ItemStack(AlfheimModularItems.volcanoLeggings), ItemStack(AlfheimModularItems.volcanoBoots))
		
		return armorSet!!
	}
	
	override fun hasArmorSetItem(player: EntityPlayer, i: Int): Boolean {
		val stack = player.inventory.armorInventory[3 - i] ?: return false
		when (i) {
			0 -> return stack.item === AlfheimModularItems.volcanoHelmet || (Botania.thaumcraftLoaded && stack.item === AlfheimModularItems.volcanoHelmetRevealing)
			1 -> return stack.item === AlfheimModularItems.volcanoChest
			2 -> return stack.item === AlfheimModularItems.volcanoLeggings
			3 -> return stack.item === AlfheimModularItems.volcanoBoots
		}
		return false
	}
	
	override fun getArmorSetName() = StatCollector.translateToLocal("alfmod.armorset.volcano.name")!!
	
	override fun getArmorSetTitle(player: EntityPlayer?) =
		"${StatCollector.translateToLocal("botaniamisc.armorset")} $armorSetName (${getSetPiecesEquipped(player)}/${armorSetStacks.size})"
	
	override fun addArmorSetDescription(stack: ItemStack?, list: MutableList<String>?) {
		addStringToTooltip(StatCollector.translateToLocal("alfmod.armorset.volcano.desc"), list)
	}
	
	@SideOnly(Side.CLIENT)
	override fun provideArmorModelForSlot(stack: ItemStack?, slot: Int): ModelBiped? {
		models[slot] = ModelArmorVolcano(slot)
		return models[slot]
	}
}