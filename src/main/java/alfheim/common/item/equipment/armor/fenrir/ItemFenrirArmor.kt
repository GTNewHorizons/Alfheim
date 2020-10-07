package alfheim.common.item.equipment.armor.fenrir

import alexsocol.asjlib.*
import alexsocol.asjlib.render.ASJRenderHelper
import alfheim.api.ModInfo
import alfheim.client.model.armor.ModelFenrirArmor
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.client.core.helper.IconHelper
import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.AlfheimItems
import alfheim.common.item.equipment.tool.ItemFenrirClaws
import alfheim.common.item.material.ElvenResourcesMetas
import com.google.common.collect.*
import cpw.mods.fml.common.eventhandler.*
import cpw.mods.fml.relauncher.*
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.*
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraftforge.event.entity.living.LivingAttackEvent
import vazkii.botania.api.mana.IManaDiscountArmor
import vazkii.botania.client.lib.LibResources
import vazkii.botania.common.core.handler.ConfigHandler
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor
import java.util.*

open class ItemFenrirArmor(slot: Int, name: String): ItemManasteelArmor(slot, name), IManaDiscountArmor {
	
	lateinit var overlay: IIcon
	
	init {
		creativeTab = AlfheimTab
	}
	
	@SideOnly(Side.CLIENT)
	override fun provideArmorModelForSlot(stack: ItemStack?, slot: Int): ModelBiped {
		models[slot] = if (AlfheimConfigHandler.minimalGraphics) ModelBiped() else ModelFenrirArmor(slot)
		return models[slot]
	}
	
	override fun getArmorTexture(stack: ItemStack?, entity: Entity?, slot: Int, type: String?): String {
		return if (hasPhantomInk(stack)) LibResources.MODEL_INVISIBLE_ARMOR else getArmorTextureAfterInk(stack, slot, type)
	}
	
	open fun getArmorTextureAfterInk(stack: ItemStack?, slot: Int, type: String?): String {
		val t = type?.capitalize() ?: ""
		if (t == "Overlay")
			ASJRenderHelper.setGlow()
		
		return if (ConfigHandler.enableArmorModels && !AlfheimConfigHandler.minimalGraphics) ModInfo.MODID + ":textures/model/armor/FenrirArmor$t.png" else if (slot == 2) ModInfo.MODID + ":textures/model/armor/FenrirArmor1$t.png" else ModInfo.MODID + ":textures/model/armor/FenrirArmor0$t.png"
	}
	
	override fun getColor(stack: ItemStack) = 0xFFFFFF
	
	override fun getIsRepairable(armor: ItemStack?, material: ItemStack): Boolean {
		return material.item === AlfheimItems.elvenResource && material.meta == ElvenResourcesMetas.FenrirFur
	}
	
	override fun getAttributeModifiers(stack: ItemStack): Multimap<String, AttributeModifier> {
		val multimap = HashMultimap.create<String, AttributeModifier>()
		val uuid = UUID(unlocalizedName.hashCode().toLong(), 0)
		multimap.put(SharedMonsterAttributes.attackDamage.attributeUnlocalizedName, AttributeModifier(uuid, "Fenrir modifier $type", 0.5, 0))
		return multimap
	}
	
	override fun getArmorSetStacks(): Array<ItemStack> {
		if (armorset == null)
			armorset = arrayOf(ItemStack(AlfheimItems.fenrirHelmet), ItemStack(AlfheimItems.fenrirChestplate), ItemStack(AlfheimItems.fenrirLeggings), ItemStack(AlfheimItems.fenrirBoots))
		
		return armorset!!
	}
	
	override fun hasArmorSetItem(player: EntityPlayer, i: Int): Boolean {
		val stack = player.inventory.armorInventory[3 - i] ?: return false
		
		when (i) {
			0 -> return stack.item === AlfheimItems.fenrirHelmet || AlfheimItems.fenrirHelmetRevealing?.let { stack.item === it } ?: false
			1 -> return stack.item === AlfheimItems.fenrirChestplate
			2 -> return stack.item === AlfheimItems.fenrirLeggings
			3 -> return stack.item === AlfheimItems.fenrirBoots
		}
		
		return false
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		itemIcon = IconHelper.forItem(reg, this, "0")
		overlay = IconHelper.forItem(reg, this, "1")
	}
	
	override fun requiresMultipleRenderPasses() = true
	
	override fun getRenderPasses(metadata: Int) = 3
	
	override fun getIcon(stack: ItemStack?, pass: Int): IIcon? {
		return when (pass) {
			0    -> super.getIcon(stack, pass)
			1    -> {
				ASJRenderHelper.setGlow()
				overlay
			}
			else -> { // without that part armor will glow :(
				ASJRenderHelper.discard()
				
				// crutch because RenderItem#renderIcon has no null check :(
				// and some other places maybe too...
				// why not just use @Nullable ?
				super.getIcon(stack, pass)
			}
		}
	}
	
	override fun getUnlocalizedNameInefficiently(stack: ItemStack): String {
		val s = this.getUnlocalizedName(stack)
		return if (s == null) "" else StatCollector.translateToLocal(s)
	}
	
	override fun getArmorSetName(): String {
		return StatCollector.translateToLocal("alfheim.armorset.fenrir.name")
	}
	
	override fun addArmorSetDescription(stack: ItemStack?, list: List<String>) {
		addStringToTooltip(StatCollector.translateToLocal("alfheim.armorset.fenrir.desc0"), list)
		addStringToTooltip(StatCollector.translateToLocal("alfheim.armorset.fenrir.desc1"), list)
	}
	
//	override fun onUpdate(stack: ItemStack, world: World, entity: Entity?, slotID: Int, inHand: Boolean) {
//		if (entity is EntityPlayer)
//			onArmorTick(world, entity, stack)
//	}
//
//	override fun onArmorTick(world: World, player: EntityPlayer, stack: ItemStack) {
//		super.onArmorTick(world, player, stack)
//		if (!stack.hasTagCompound()) stack.stackTagCompound = NBTTagCompound()
//		stack.stackTagCompound.setBoolean("SET", hasArmorSet(player))
//	}
	
	override fun getDiscount(stack: ItemStack, slot: Int, player: EntityPlayer): Float {
		return if (hasArmorSet(player)) 0.2f / 4f else 0f
	}
	
	companion object {
		
		internal var armorset: Array<ItemStack>? = null
		
		init {
			eventForge()
		}
		
		fun hasSet(player: EntityPlayer?) = (AlfheimItems.fenrirChestplate as ItemFenrirArmor).hasArmorSet(player)
		
		@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
		fun onLivingAttack(e: LivingAttackEvent) {
			val attacker = e.source.entity as? EntityPlayer ?: return
			val flagSet = hasSet(attacker)
			val flagClaws = attacker.heldItem?.item is ItemFenrirClaws
			if (e.source.damageType == "player" && ((flagSet && attacker.heldItem == null) || flagClaws)) {
				e.source.setDamageBypassesArmor()
				e.isCanceled = false
			}
		}
	}
}
