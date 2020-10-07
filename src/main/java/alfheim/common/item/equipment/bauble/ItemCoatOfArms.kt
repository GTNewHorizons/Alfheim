package alfheim.common.item.equipment.bauble

import alexsocol.asjlib.*
import alfheim.api.ModInfo
import alfheim.api.item.IPriestColorOverride
import alfheim.client.core.helper.IconHelper
import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.ItemIridescent
import baubles.api.BaubleType
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.texture.*
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.*
import net.minecraft.util.*
import net.minecraftforge.client.event.RenderPlayerEvent
import org.lwjgl.opengl.GL11.*
import vazkii.botania.api.item.*
import vazkii.botania.api.recipe.IFlowerComponent
import vazkii.botania.client.core.helper.ShaderHelper
import vazkii.botania.common.item.equipment.bauble.ItemBauble

class ItemCoatOfArms: ItemBauble("coatOfArms"), ICosmeticBauble, IPriestColorOverride, IFlowerComponent {
	
	init {
		creativeTab = AlfheimTab
		setHasSubtypes(true)
	}
	
	override fun canFit(stack: ItemStack, inventory: IInventory) = stack.meta == 6
	
	override fun getParticleColor(stack: ItemStack) = colorMap[stack.meta]
	
	override fun registerIcons(par1IconRegister: IIconRegister) {
		icons = Array(TYPES) { IconHelper.forItem(par1IconRegister, this, it, "coatofarms") }
	}
	
	override fun colorOverride(stack: ItemStack): Int {
		if (stack.meta < TYPES - 1 && stack.meta >= 0 && stack.meta != 16)
			return colorMap[stack.meta]
		else if (stack.meta == 16)
			return ItemIridescent.rainbowColor()
		return -1
	}
	
	override fun getSubItems(item: Item, tab: CreativeTabs?, list: MutableList<Any?>) {
		for (i in 0 until TYPES)
			list.add(ItemStack(item, 1, i))
	}
	
	override fun getIconFromDamage(meta: Int) = icons.safeGet(meta)
	
	override fun onEquipped(stack: ItemStack, player: EntityLivingBase) {
		super.onEquipped(stack, player)
		if (stack.meta == 1 && "paris".toRegex().find(stack.displayName.toLowerCase()) != null) {
			stack.meta = TYPES - 1
			stack.tagCompound.removeTag("display")
		}
	}
	
	override fun getUnlocalizedNameInefficiently(par1ItemStack: ItemStack) =
		super.getUnlocalizedNameInefficiently(par1ItemStack).replace("item\\.botania:".toRegex(), "item.${ModInfo.MODID}:") + par1ItemStack.meta
	
	override fun addHiddenTooltip(par1ItemStack: ItemStack, par2EntityPlayer: EntityPlayer, par3List: MutableList<Any?>, par4: Boolean) {
		addStringToTooltip(StatCollector.translateToLocal("botaniamisc.cosmeticBauble"), par3List)
		super.addHiddenTooltip(par1ItemStack, par2EntityPlayer, par3List, par4)
	}
	
	fun addStringToTooltip(s: String, tooltip: MutableList<Any?>?) {
		tooltip!!.add(s.replace("&".toRegex(), "\u00a7"))
	}
	
	override fun getBaubleType(arg0: ItemStack) = BaubleType.AMULET
	
	override fun onPlayerBaubleRender(stack: ItemStack, event: RenderPlayerEvent, type: IBaubleRender.RenderType) {
		if (type == IBaubleRender.RenderType.BODY) {
			mc.renderEngine.bindTexture(TextureMap.locationItemsTexture)
			IBaubleRender.Helper.rotateIfSneaking(event.entityPlayer)
			chestTranslate()
			glScaled(0.8)
			glTranslatef(0.2F, -0.2F, -0.35F)
			glRotatef(10F, 0F, 0F, 1F)
			if (stack.meta == 16) {
				glEnable(GL_BLEND)
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
				glAlphaFunc(GL_GREATER, 0.9F)
				ShaderHelper.useShader(ShaderHelper.halo)
			}
			renderIcon(stack.meta)
			if (stack.meta == 16) {
				ShaderHelper.releaseShader()
				glAlphaFunc(GL_GREATER, 0.1F)
				glDisable(GL_BLEND)
			}
		}
	}
	
	fun chestTranslate() {
		glRotatef(180F, 1F, 0F, 0F)
		glTranslatef(-0.5F, -0.7F, 0.15F)
	}
	
	fun renderIcon(i: Int) {
		val icon = icons.safeGet(i)
		ItemRenderer.renderItemIn2D(Tessellator.instance, icon.maxU, icon.minV, icon.minU, icon.maxV, icon.iconWidth, icon.iconHeight, 1F / 16F)
	}
	
	companion object {
		
		const val TYPES = 19
		
		lateinit var icons: Array<IIcon>
		
		val colorMap = intArrayOf(
			0x00137F, 0x0043FF, 0xFF0037, 0xFFD800,
			0x002EFF, 0x001A8E, 0x009944, 0x003BFF,
			0x00FF3B, 0xFF003B, 0x603A20, 0xFFFF00,
			0xFF0015, 0x0048FF, 0xFFD400, 0xFFFFFF,
			0xFFFFFF, 0xDD0000, 0xFF0037
		)
	}
}
