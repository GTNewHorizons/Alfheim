package alfheim.common.item.equipment.armor.elvoruim

import alfheim.AlfheimCore
import alfheim.api.*
import alfheim.client.model.armor.ModelElvoriumArmor
import alfheim.common.item.AlfheimItems
import alfheim.common.item.AlfheimItems.ElvenResourcesMetas
import com.google.common.collect.*
import cpw.mods.fml.common.Optional
import cpw.mods.fml.relauncher.*
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.*
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.*
import net.minecraft.world.World
import thaumcraft.api.IVisDiscountGear
import thaumcraft.api.aspects.Aspect
import vazkii.botania.api.item.IManaProficiencyArmor
import vazkii.botania.api.mana.IManaDiscountArmor
import vazkii.botania.common.Botania
import vazkii.botania.common.core.handler.ConfigHandler
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor

import java.util.*

@Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.IVisDiscountGear", striprefs = true)
open class ItemElvoriumArmor(type: Int, name: String): ItemManasteelArmor(type, name, AlfheimAPI.ELVORIUM), IManaDiscountArmor, IManaProficiencyArmor, IVisDiscountGear {
	
	init {
		this.creativeTab = AlfheimCore.alfheimTab
	}
	
	@SideOnly(Side.CLIENT)
	override fun provideArmorModelForSlot(stack: ItemStack?, slot: Int): ModelBiped {
		models[slot] = ModelElvoriumArmor(slot)
		return models[slot]
	}
	
	override fun getArmorTextureAfterInk(stack: ItemStack?, slot: Int): String {
		return if (ConfigHandler.enableArmorModels) ModInfo.MODID + ":textures/model/armor/ElvoriumArmor.png" else if (slot == 2) ModInfo.MODID + ":textures/model/armor/ElvoriumArmor1.png" else ModInfo.MODID + ":textures/model/armor/ElvoriumArmor0.png"
	}
	
	override fun getIsRepairable(armor: ItemStack?, material: ItemStack): Boolean {
		return material.item === AlfheimItems.elvenResource && material.itemDamage == ElvenResourcesMetas.ElvoriumIngot || super.getIsRepairable(armor, material)
	}
	
	override fun getAttributeModifiers(stack: ItemStack): Multimap<*, *> {
		val multimap = HashMultimap.create<String, AttributeModifier>()
		val uuid = UUID(unlocalizedName.hashCode().toLong(), 0)
		multimap.put(SharedMonsterAttributes.knockbackResistance.attributeUnlocalizedName, AttributeModifier(uuid, "Terrasteel modifier $type", getArmorDisplay(null, ItemStack(this), type).toDouble() / 20, 0))
		return multimap
	}
	
	override fun getArmorSetStacks(): Array<ItemStack> {
		if (armorset == null)
			armorset = arrayOf(ItemStack(AlfheimItems.elvoriumHelmet), ItemStack(AlfheimItems.elvoriumChestplate), ItemStack(AlfheimItems.elvoriumLeggings), ItemStack(AlfheimItems.elvoriumBoots))
		
		return armorset!!
	}
	
	override fun hasArmorSetItem(player: EntityPlayer, i: Int): Boolean {
		val stack = player.inventory.armorInventory[3 - i] ?: return false
		
		when (i) {
			0 -> return stack.item === AlfheimItems.elvoriumHelmet || (AlfheimItems.elvoriumHelmetRevealingIsInitialized() && stack.item === AlfheimItems.elvoriumHelmetRevealing)
			1 -> return stack.item === AlfheimItems.elvoriumChestplate
			2 -> return stack.item === AlfheimItems.elvoriumLeggings
			3 -> return stack.item === AlfheimItems.elvoriumBoots
		}
		
		return false
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		itemIcon = reg.registerIcon(ModInfo.MODID + ':'.toString() + this.unlocalizedName.substring(5))
	}
	
	override fun getUnlocalizedNameInefficiently(stack: ItemStack): String {
		val s = this.getUnlocalizedName(stack)
		return if (s == null) "" else StatCollector.translateToLocal(s)
	}
	
	override fun getArmorSetName(): String {
		return StatCollector.translateToLocal("alfheim.armorset.elvorium.name")
	}
	
	override fun addArmorSetDescription(stack: ItemStack?, list: List<String>) {
		addStringToTooltip(StatCollector.translateToLocal("alfheim.armorset.elvorium.desc0"), list)    // -30% mana cost
		addStringToTooltip(StatCollector.translateToLocal("alfheim.armorset.elvorium.desc1"), list)    // Powerful rods
		if (Botania.thaumcraftLoaded) addStringToTooltip(EnumChatFormatting.DARK_PURPLE.toString() + StatCollector.translateToLocal("alfheim.armorset.elvorium.desc2"), list)    // -20% vis discount
		if (Botania.thaumcraftLoaded) addStringToTooltip(EnumChatFormatting.GOLD.toString() + StatCollector.translateToLocal("alfheim.armorset.elvorium.desc3"), list)    // 8 pts of runic shield
		addStringToTooltip(StatCollector.translateToLocal("botania.armorset.terrasteel.desc1"), list)    // Regen w/o full hungerbar
		addStringToTooltip(StatCollector.translateToLocal("botania.armorset.terrasteel.desc2"), list)    // Passive mana regen
	}
	
	override fun onUpdate(stack: ItemStack?, world: World?, entity: Entity?, slotID: Int, inHand: Boolean) {
		if (entity is EntityPlayer)
			onArmorTick(world!!, entity as EntityPlayer?, stack)
	}
	
	override fun onArmorTick(world: World, player: EntityPlayer?, stack: ItemStack?) {
		super.onArmorTick(world, player, stack)
		if (!stack!!.hasTagCompound()) stack.stackTagCompound = NBTTagCompound()
		if (player != null) stack.stackTagCompound.setBoolean("SET", hasArmorSet(player))
	}
	
	@Optional.Method(modid = "Thaumcraft")
	override fun getRunicCharge(stack: ItemStack): Int {
		if (!stack.hasTagCompound()) stack.stackTagCompound = NBTTagCompound()
		return if (stack.stackTagCompound.getBoolean("SET")) 2 else 0
	}
	
	override fun getDiscount(stack: ItemStack, slot: Int, player: EntityPlayer): Float {
		return if (hasArmorSet(player)) 0.3f / 4.0f else 0f
	}
	
	override fun shouldGiveProficiency(stack: ItemStack, slot: Int, player: EntityPlayer): Boolean {
		return hasArmorSet(player)
	}
	
	@Optional.Method(modid = "Thaumcraft")
	override fun getVisDiscount(stack: ItemStack, player: EntityPlayer, aspect: Aspect): Int {
		return if (hasArmorSet(player)) 5 else 0
	}
	
	companion object {
		
		internal var armorset: Array<ItemStack>? = null
	}
}
