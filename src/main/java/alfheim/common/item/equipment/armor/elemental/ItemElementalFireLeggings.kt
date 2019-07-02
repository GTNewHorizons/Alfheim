package alfheim.common.item.equipment.armor.elemental

import alfheim.AlfheimCore
import alfheim.common.core.registry.AlfheimItems
import com.google.common.collect.*
import cpw.mods.fml.relauncher.*
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword
import java.util.*

class ItemElementalFireLeggings: ElementalArmor(2, "ElementalFireLeggings") {
	
	companion object {
		val uuid = UUID.fromString("d162f3e4-87f5-43cc-b33e-85b8b4f4cb75")!!
	}
	
	init {
		this.creativeTab = AlfheimCore.alfheimTab
	}
	
	override fun getPixieChance(stack: ItemStack): Float {
		return 0.15f
	}
	
	override fun onArmorTick(world: World, player: EntityPlayer, stack: ItemStack) {
		if (!world.isRemote && armorType == 2 && player.getCurrentArmor(1) != null && player.getCurrentArmor(1).item === AlfheimItems.elementalLeggings) {
			//if (player.isSprinting && ManaItemHandler.requestManaExact(player.getCurrentArmor(1), player, 1, !world.isRemote)) player.addPotionEffect(PotionEffect(Potion.moveSpeed.id, 1, 1))
			if (player.isBurning) player.extinguish()
		}
	}
	
	override fun getAttributeModifiers(stack: ItemStack?): Multimap<*, *> {
		val multimap = HashMultimap.create<String, AttributeModifier>()
		multimap.put(SharedMonsterAttributes.movementSpeed.attributeUnlocalizedName, AttributeModifier(uuid, "Leggings modifier", 0.4, 2))
		return multimap
	}
	
	@SideOnly(Side.CLIENT)
	override fun addInformation(stack: ItemStack?, player: EntityPlayer?, list: MutableList<Any?>, b: Boolean) {
		list.add(StatCollector.translateToLocal("item.ElementalArmor.desc2"))
		super.addInformation(stack, player, list, b)
	}
}
