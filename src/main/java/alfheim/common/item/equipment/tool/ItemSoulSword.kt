package alfheim.common.item.equipment.tool

import alexsocol.asjlib.*
import alfheim.api.*
import alfheim.client.core.helper.IconHelper
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.AlfheimTab
import alfheim.common.entity.boss.EntityFlugel
import alfheim.common.item.AlfheimItems
import com.google.common.collect.*
import cpw.mods.fml.common.eventhandler.*
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.world.World
import net.minecraftforge.event.entity.living.LivingDeathEvent
import vazkii.botania.api.mana.*
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.item.ModItems
import kotlin.math.*

class ItemSoulSword: ItemSword(AlfheimAPI.SOUL), IManaUsingItem {
	
	init {
		creativeTab = AlfheimTab
		unlocalizedName = "SoulSword"
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World?, player: EntityPlayer): ItemStack {
		if (player.isSneaking && stack.getItemDamage() > 0 && ASJUtilities.consumeItemStack(player.inventory, ItemStack(ModItems.manaResource, 1, 5)))
			repair(stack, 100)
		
		return super.onItemRightClick(stack, world, player)
	}
	
	override fun getAttributeModifiers(stack: ItemStack): Multimap<String, AttributeModifier>? {
		val multimap = HashMultimap.create<String, AttributeModifier>()
		multimap.put(SharedMonsterAttributes.attackDamage.attributeUnlocalizedName, AttributeModifier(Item.field_111210_e, "Weapon modifier", getDamageFromLevel(stack), 0))
		return multimap
	}
	
	override fun getMaxDamage(stack: ItemStack) = getMaxUsesFromLevel(stack)
	
	override fun usesMana(stack: ItemStack) = stack.level < AlfheimConfigHandler.soulSwordMaxLvl
	
	override fun setUnlocalizedName(name: String?): Item? {
		GameRegistry.registerItem(this, name)
		return super.setUnlocalizedName(name)
	}
	
	override fun getUnlocalizedNameInefficiently(stack: ItemStack?): String {
		return super.getUnlocalizedNameInefficiently(stack).replace("item.".toRegex(), "item.${ModInfo.MODID}:")
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		itemIcon = IconHelper.forItem(reg, this)
	}
	
	companion object {
		
		private const val TAG_SOUL_LEVEL = "soulLevel"
		
		private var ItemStack.level: Int
			get() = ItemNBTHelper.getInt(this, TAG_SOUL_LEVEL, 0)
			set(lvl) = ItemNBTHelper.setInt(this, TAG_SOUL_LEVEL, lvl)
		
		fun setLevelP(stack: ItemStack, lvl: Int) {
			stack.level = lvl
		}
		
		fun getLevelP(stack: ItemStack) = stack.level
		
		private fun getDamageFromLevel(stack: ItemStack) = stack.level / 100.0
		
		private fun getMaxUsesFromLevel(stack: ItemStack) = max(100, stack.level / 10)
		
		private fun repair(stack: ItemStack, amount: Int) {
			stack.meta = max(0, stack.meta - amount)
		}
		
		init {
			eventForge()
		}
		
		@SubscribeEvent(priority = EventPriority.LOWEST)
		fun onLivingDeath(e: LivingDeathEvent) {
			val attacker = e.source.entity
			if (!EntityFlugel.isTruePlayer(attacker)) return
			attacker as EntityPlayer // checked above
			val stack = attacker.heldItem ?: return
			if (stack.item !== AlfheimItems.soulSword) return
			if (!ManaItemHandler.requestManaExact(stack, attacker, 1, true)) return
			stack.level = min(AlfheimConfigHandler.soulSwordMaxLvl, stack.level + 1)
			repair(stack, 1)
		}
	}
}
