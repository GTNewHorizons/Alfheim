package alfheim.common.item.relic

import vazkii.botania.common.core.helper.ItemNBTHelper.*

import java.awt.Color

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumAction
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import net.minecraft.world.World
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.client.core.helper.IconHelper
import vazkii.botania.common.Botania
import vazkii.botania.common.item.relic.ItemRelic

@Deprecated("")
class ItemMjolnir: ItemRelic("Mjolnir") {
	
	init {
		creativeTab = AlfheimCore.alfheimTab
		setHasSubtypes(true)
		setFull3D()
	}
	
	@SideOnly(Side.CLIENT)
	override fun getColorFromItemStack(stack: ItemStack?, pass: Int): Int {
		var pass = pass
		pass = if (pass == 1 && getCharge(stack) >= MAX_CHARGE) 1 else 0
		return if (pass == 1) Color.HSBtoRGB((200 + (Math.sin(Botania.proxy.worldElapsedTicks / 10.0 % 20) * 20).toFloat()) / 360f, 0.5f, 1f) else -0x1
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		for (i in icons.indices) icons[i] = IconHelper.forItem(reg, this, i)
	}
	
	@SideOnly(Side.CLIENT)
	override fun getIcon(stack: ItemStack, pass: Int): IIcon {
		return icons[pass]
	}
	
	override fun requiresMultipleRenderPasses(): Boolean {
		return true
	}
	
	override fun getRenderPasses(meta: Int): Int {
		return 2
	}
	
	override fun getSubItems(item: Item, tab: CreativeTabs?, list: MutableList<*>) {
		list.add(ItemStack(item))
		
		val creative = ItemStack(item)
		setBoolean(creative, TAG_CREATIVE, true)
		setCharge(creative, MAX_CHARGE)
		list.add(creative)
	}
	
	override fun onPlayerStoppedUsing(stack: ItemStack?, world: World?, player: EntityPlayer?, itemInUseCount: Int) {
		if (getCharge(stack) >= MAX_CHARGE && !world!!.isRemote) {
			val mop = ASJUtilities.getSelectedBlock(player, 256.0, true)
			if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK) world.addWeatherEffect(EntityLightningBolt(world, mop.blockX.toDouble(), (mop.blockY + 1).toDouble(), mop.blockZ.toDouble()))
		}
		if (!getBoolean(stack, TAG_CREATIVE, false)) setCharge(stack, 0)
	}
	
	override fun onUsingTick(stack: ItemStack?, player: EntityPlayer, coitemInUseCountunt: Int) {
		if (player.worldObj.isRemote) return
		if (getCharge(stack) < MAX_CHARGE) addCharge(stack, if (player.capabilities.isCreativeMode) CHARGE_PER_TICK else ManaItemHandler.requestMana(stack, player, CHARGE_PER_TICK, true))
	}
	
	override fun getMaxItemUseDuration(stack: ItemStack?): Int {
		return 72000
	}
	
	override fun getItemUseAction(stack: ItemStack?): EnumAction {
		return EnumAction.bow
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World?, player: EntityPlayer): ItemStack? {
		if (player.capabilities.isCreativeMode || ManaItemHandler.requestManaExact(stack, player, MAX_CHARGE, false)) player.setItemInUse(stack, this.getMaxItemUseDuration(stack))
		return stack
	}
	
	override fun onUpdate(stack: ItemStack?, world: World?, entity: Entity?, slotID: Int, inHand: Boolean) {
		super.onUpdate(stack, world, entity, slotID, inHand)
		if (entity is EntityPlayer && !world!!.isRemote && getCharge(stack) >= MAX_CHARGE && !getBoolean(stack, TAG_CREATIVE, false) && !inHand || getBoolean(stack, TAG_CREATIVE, false) && inHand && stack!!.displayName.toLowerCase().trim({ it <= ' ' }) == "banhammer") onPlayerStoppedUsing(stack, world, entity as EntityPlayer?, 0)
	}
	
	companion object {
		
		val TAG_CHARGE = "charge"
		val TAG_CREATIVE = "creative"
		val MAX_CHARGE = 10000
		val CHARGE_PER_TICK = 1000
		val icons = arrayOfNulls<IIcon>(2)
		
		fun addCharge(stack: ItemStack?, charge: Int) {
			setInt(stack!!, TAG_CHARGE, getCharge(stack) + charge)
		}
		
		fun setCharge(stack: ItemStack?, charge: Int) {
			setInt(stack!!, TAG_CHARGE, charge)
		}
		
		fun getCharge(stack: ItemStack?): Int {
			return getInt(stack, TAG_CHARGE, 0)
		}
	}
}