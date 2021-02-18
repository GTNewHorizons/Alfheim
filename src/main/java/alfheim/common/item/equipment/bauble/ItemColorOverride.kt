package alfheim.common.item.equipment.bauble

import alfheim.api.ModInfo
import alfheim.api.item.IPriestColorOverride
import alfheim.client.core.helper.IconHelper
import alfheim.common.core.util.AlfheimTab
import baubles.api.BaubleType
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.*
import net.minecraftforge.client.event.RenderPlayerEvent
import vazkii.botania.api.item.*
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.item.equipment.bauble.ItemBauble

class ItemColorOverride: ItemBauble("colorOverride"), ICosmeticBauble, IPriestColorOverride {
	
	lateinit var overlayIcon: IIcon
	
	init {
		creativeTab = AlfheimTab
		setHasSubtypes(true)
	}
	
	override fun registerIcons(par1IconRegister: IIconRegister) {
		itemIcon = IconHelper.forItem(par1IconRegister, this)
		overlayIcon = IconHelper.forItem(par1IconRegister, this, "Overlay")
	}
	
	override fun colorOverride(stack: ItemStack): Int {
		if (ItemNBTHelper.detectNBT(stack)) {
			val comp = ItemNBTHelper.getCompound(stack, "display", false)
			if (comp.hasKey("color", 3))
				return comp.getInteger("color")
		}
		return -1
	}
	
	@SideOnly(Side.CLIENT)
	override fun requiresMultipleRenderPasses() = true
	
	@SideOnly(Side.CLIENT)
	override fun getColorFromItemStack(stack: ItemStack, pass: Int) =
		if (pass > 0) 0xFFFFFF else getColor(stack)
	
	@SideOnly(Side.CLIENT)
	override fun getIconFromDamageForRenderPass(meta: Int, pass: Int): IIcon =
		if (pass > 0) overlayIcon else super.getIconFromDamageForRenderPass(meta, pass)
	
	fun setColor(stack: ItemStack, color: Int) {
		if (!ItemNBTHelper.verifyExistance(stack, "display"))
			ItemNBTHelper.setCompound(stack, "display", NBTTagCompound())
		ItemNBTHelper.getCompound(stack, "display", true).setInteger("color", color)
	}
	
	fun removeColor(stack: ItemStack) {
		if (ItemNBTHelper.detectNBT(stack)) {
			val comp = ItemNBTHelper.getCompound(stack, "display", false)
			if (comp.hasKey("color", 3))
				comp.removeTag("color")
		}
	}
	
	fun getColor(stack: ItemStack): Int {
		if (ItemNBTHelper.detectNBT(stack)) {
			val comp = ItemNBTHelper.getCompound(stack, "display", false)
			if (comp.hasKey("color", 3))
				return comp.getInteger("color")
		}
		return 0xB0B0B0
	}
	
	fun hasColor(stack: ItemStack) =
		ItemNBTHelper.detectNBT(stack) && ItemNBTHelper.getCompound(stack, "display", false).hasKey("color", 3)
	
	override fun getUnlocalizedNameInefficiently(par1ItemStack: ItemStack) =
		super.getUnlocalizedNameInefficiently(par1ItemStack).replace("item\\.botania:".toRegex(), "item.${ModInfo.MODID}:")
	
	override fun addInformation(par1ItemStack: ItemStack?, par2EntityPlayer: EntityPlayer?, par3List: MutableList<Any?>, par4: Boolean) {
		if (par1ItemStack == null) return
		if (hasColor(par1ItemStack))
			addStringToTooltip("&7" + StatCollector.translateToLocal("item.dyed") + "&r", par3List)
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4)
	}
	
	override fun addHiddenTooltip(par1ItemStack: ItemStack, par2EntityPlayer: EntityPlayer, par3List: MutableList<Any?>, par4: Boolean) {
		addStringToTooltip(StatCollector.translateToLocal("botaniamisc.cosmeticBauble"), par3List)
		super.addHiddenTooltip(par1ItemStack, par2EntityPlayer, par3List, par4)
	}
	
	fun addStringToTooltip(s: String, tooltip: MutableList<Any?>) =
		tooltip.add(s.replace("&".toRegex(), "\u00a7"))
	
	override fun getBaubleType(arg0: ItemStack) = BaubleType.RING
	
	override fun onPlayerBaubleRender(stack: ItemStack, event: RenderPlayerEvent, type: IBaubleRender.RenderType) = Unit
}
