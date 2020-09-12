package alfheim.common.item.equipment.tool

import alexsocol.asjlib.render.ASJRenderHelper
import alfheim.common.core.helper.IconHelper
import alfheim.common.item.ItemMod
import com.google.common.collect.Multimap
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.*
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.IIcon
import net.minecraft.world.World
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.item.equipment.tool.ToolCommons

class ItemFenrirClaws: ItemMod("FenrirClaws") {
	
	val MANA_PER_DAMAGE = 40
	
	lateinit var overlay: IIcon
	
	init {
		setMaxStackSize(1)
		maxDamage = 1561
	}
	
	override fun hitEntity(stack: ItemStack, target: EntityLivingBase?, attacker: EntityLivingBase?): Boolean {
		ToolCommons.damageItem(stack, 1, attacker, MANA_PER_DAMAGE)
		return true
	}
	
	override fun onBlockDestroyed(stack: ItemStack?, world: World?, block: Block, x: Int, y: Int, z: Int, entity: EntityLivingBase?): Boolean {
		if (block.getBlockHardness(world, x, y, z) != 0f)
			ToolCommons.damageItem(stack, 1, entity, MANA_PER_DAMAGE)
		return true
	}
	
	val attackDamage = 1.5
	
	override fun onUpdate(stack: ItemStack, world: World, player: Entity, itemSlot: Int, isSelected: Boolean) {
		if (!world.isRemote && player is EntityPlayer && stack.itemDamage > 0 && ManaItemHandler.requestManaExactForTool(stack, player, MANA_PER_DAMAGE * 2, true))
			stack.itemDamage = stack.itemDamage - 1
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World?, player: EntityPlayer): ItemStack {
		player.setItemInUse(stack, getMaxItemUseDuration(stack))
		return super.onItemRightClick(stack, world, player)
	}
	
	override fun getItemUseAction(stack: ItemStack) = EnumAction.block
	
	override fun getMaxItemUseDuration(stack: ItemStack) = 72000
	
	override fun getAttributeModifiers(stack: ItemStack): Multimap<String, AttributeModifier> {
		val multimap = super.getAttributeModifiers(stack) as Multimap<String, AttributeModifier>
		multimap.put(SharedMonsterAttributes.attackDamage.attributeUnlocalizedName, AttributeModifier(field_111210_e, "Weapon modifier", attackDamage, 0))
		return multimap
	}
	
	override fun getItemEnchantability(): Int = 14
	
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
}
