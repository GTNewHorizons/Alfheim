package alfheim.common.item.equipment.tool

import alexsocol.asjlib.render.ASJRenderHelper
import alfheim.api.*
import alfheim.client.core.helper.IconHelper
import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.equipment.armor.fenrir.ItemFenrirArmor
import com.google.common.collect.*
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.*
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraft.world.World
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword
import vazkii.botania.common.lib.LibMisc

class ItemFenrirClaws: ItemManasteelSword(AlfheimAPI.FENRIR, "FenrirClaws") {
	
	val MANA_PER_DAMAGE = 40
	val attackDamage = 3.0
	
	lateinit var overlay: IIcon
	
	init {
		creativeTab = AlfheimTab
	}
	
	override fun isFull3D() = false
	
	override fun onUpdate(stack: ItemStack, world: World, player: Entity, itemSlot: Int, isSelected: Boolean) {
		super.onUpdate(stack, world, player, itemSlot, isSelected)
		if (player !is EntityPlayer) return
		ItemNBTHelper.setBoolean(stack, "SET", ItemFenrirArmor.hasSet(player))
	}
	
	override fun getIsRepairable(stack: ItemStack?, material: ItemStack?) = false // TODO make repairable
	
	override fun getAttributeModifiers(stack: ItemStack): Multimap<String, AttributeModifier> {
		val set = ItemNBTHelper.getBoolean(stack, "SET", false)
		val multimap = HashMultimap.create<String, AttributeModifier>()
		multimap.put(SharedMonsterAttributes.attackDamage.attributeUnlocalizedName, AttributeModifier(field_111210_e, "Weapon modifier", attackDamage + if (set) (13.75 / 1.5 - 9 + attackDamage) else 0.0, 0))
		return multimap
	}
	
	override fun getItemEnchantability() = 14
	
	override fun getManaPerDamage() = MANA_PER_DAMAGE
	
	override fun getItemStackDisplayName(stack: ItemStack) =
		super.getItemStackDisplayName(stack).replace("&", "\u00a7")
	
	override fun getUnlocalizedNameInefficiently(stack: ItemStack) =
		super.getUnlocalizedNameInefficiently(stack).replace(LibMisc.MOD_ID, ModInfo.MODID)
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		itemIcon = IconHelper.forItem(reg, this, "0")
		overlay = IconHelper.forItem(reg, this, "1")
	}
	
	override fun requiresMultipleRenderPasses() = true
	
	override fun getRenderPasses(metadata: Int) = 3
	
	override fun getIcon(stack: ItemStack?, pass: Int): IIcon? {
		return when (pass) {
			0    -> getIconIndex(stack)
			
			1    -> {
				ASJRenderHelper.setGlow()
				overlay
			}
			
			else -> { // without that part armor will glow :(
				ASJRenderHelper.discard()
				
				// crutch because RenderItem#renderIcon has no null check :(
				// and some other places maybe too...
				// why not just use @Nullable ?
				getIconIndex(stack)
			}
		}
	}
	
	override fun getIconIndex(stack: ItemStack?) = itemIcon!! // no elucidator
}
