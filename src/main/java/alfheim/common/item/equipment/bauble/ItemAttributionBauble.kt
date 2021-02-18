package alfheim.common.item.equipment.bauble

import alexsocol.asjlib.*
import alfheim.api.ModInfo
import alfheim.client.core.helper.IconHelper
import alfheim.common.core.util.AlfheimTab
import baubles.api.BaubleType
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.texture.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.common.MinecraftForge
import org.lwjgl.opengl.GL11.*
import vazkii.botania.api.item.*
import vazkii.botania.client.core.helper.ShaderHelper
import vazkii.botania.client.core.proxy.ClientProxy
import vazkii.botania.client.lib.LibResources
import vazkii.botania.client.model.ModelTinyPotato
import vazkii.botania.common.item.equipment.bauble.ItemBauble

class ItemAttributionBauble: ItemBauble("attributionBauble"), ICosmeticBauble {
	
	lateinit var potatoTexture: ResourceLocation
	
	lateinit var defaultIcon: IIcon
	lateinit var wireIcon: IIcon
	lateinit var kitsuneIcon: IIcon
	lateinit var trisIcon: IIcon
	
	init {
		creativeTab = AlfheimTab
		setHasSubtypes(true)
		if (ASJUtilities.isClient) {
			MinecraftForge.EVENT_BUS.register(this)
			potatoTexture = ResourceLocation(if (ClientProxy.dootDoot) LibResources.MODEL_TINY_POTATO_HALLOWEEN else LibResources.MODEL_TINY_POTATO)
		}
	}
	
	override fun getUnlocalizedNameInefficiently(par1ItemStack: ItemStack) =
		super.getUnlocalizedNameInefficiently(par1ItemStack).replace("item\\.botania:".toRegex(), "item.${ModInfo.MODID}:")
	
	override fun getItemStackDisplayName(stack: ItemStack) =
		super.getItemStackDisplayName(stack).replace("&".toRegex(), "\u00a7")
	
	override fun registerIcons(par1IconRegister: IIconRegister) {
		itemIcon = IconHelper.forItem(par1IconRegister, this)
		defaultIcon = IconHelper.forItem(par1IconRegister, this, "Render")
		wireIcon = IconHelper.forItem(par1IconRegister, this, "-WireSegal")
		kitsuneIcon = IconHelper.forItem(par1IconRegister, this, "-L0neKitsune")
		trisIcon = IconHelper.forItem(par1IconRegister, this, "-Tristaric")
	}
	
	override fun addHiddenTooltip(par1ItemStack: ItemStack, par2EntityPlayer: EntityPlayer, par3List: MutableList<Any?>, par4: Boolean) {
		addStringToTooltip(StatCollector.translateToLocal("botaniamisc.cosmeticBauble"), par3List)
		super.addHiddenTooltip(par1ItemStack, par2EntityPlayer, par3List, par4)
	}
	
	fun addStringToTooltip(s: String, tooltip: MutableList<Any?>) {
		tooltip.add(s.replace("&".toRegex(), "\u00a7"))
	}
	
	override fun getIconFromDamage(dmg: Int) = itemIcon!!
	
	override fun getBaubleType(stack: ItemStack) = BaubleType.AMULET
	
	override fun getUnlocalizedName(par1ItemStack: ItemStack) =
		super.getUnlocalizedName(par1ItemStack) + par1ItemStack.meta
	
	fun faceTranslate() {
		glRotatef(90F, 0F, 1F, 0F)
		glRotatef(180F, 1F, 0F, 0F)
		glTranslatef(-0.4F, 0.1F, -0.25F)
	}
	
	fun chestTranslate() {
		glRotatef(180F, 1F, 0F, 0F)
		glTranslatef(-0.5F, -0.7F, 0.15F)
	}
	
	override fun onEquipped(stack: ItemStack, player: EntityLivingBase) {
		super.onEquipped(stack, player)
		if (stack.meta != 1 && stack.displayName.toLowerCase().trim() == "vazkii is bae") {
			stack.meta = 1
			stack.tagCompound.removeTag("display")
		}
	}
	
	@SideOnly(Side.CLIENT)
	override fun onPlayerBaubleRender(stack: ItemStack, event: RenderPlayerEvent, type: IBaubleRender.RenderType) {
		val name = event.entityPlayer.commandSenderName
		if (type == IBaubleRender.RenderType.HEAD) {
			if (stack.meta != 0) {
				// Render the Tiny Potato on your head... a tiny headtato, if you will.
				mc.renderEngine.bindTexture(potatoTexture)
				val model = ModelTinyPotato()
				glTranslatef(0f, -2f, 0f)
				glRotatef(-90F, 0F, 1F, 0F)
				model.render()
			} else {
				if (name == "yrsegal" || name == "theLorist") {
					// Render the Blueflare
					mc.renderEngine.bindTexture(TextureMap.locationItemsTexture)
					IBaubleRender.Helper.translateToHeadLevel(event.entityPlayer)
					glEnable(GL_BLEND)
					glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
					glAlphaFunc(GL_EQUAL, 1F)
					ShaderHelper.useShader(ShaderHelper.halo)
					faceTranslate()
					glScaled(0.35)
					glTranslatef(0.9F, -0.505F, -0.4F)
					ItemRenderer.renderItemIn2D(Tessellator.instance, wireIcon.maxU, wireIcon.minV, wireIcon.minU, wireIcon.maxV, wireIcon.iconWidth, wireIcon.iconHeight, 1F / 32F)
					ShaderHelper.releaseShader()
					glAlphaFunc(GL_ALWAYS, 1F)
					glDisable(GL_BLEND)
				} else if (name == "Tristaric") {
					// Render the Ezic Star
					mc.renderEngine.bindTexture(TextureMap.locationItemsTexture)
					IBaubleRender.Helper.translateToHeadLevel(event.entityPlayer)
					faceTranslate()
					glScaled(0.5)
					glTranslatef(0.3F, -0.45F, 0F)
					ItemRenderer.renderItemIn2D(Tessellator.instance, trisIcon.maxU, trisIcon.minV, trisIcon.minU, trisIcon.maxV, trisIcon.iconWidth, trisIcon.iconHeight, 1F / 32F)
				}
			}
		} else
			if (type == IBaubleRender.RenderType.BODY && stack.meta == 0) {
				mc.renderEngine.bindTexture(TextureMap.locationItemsTexture)
				IBaubleRender.Helper.rotateIfSneaking(event.entityPlayer)
				if (name == "l0nekitsune") {
					// Render a fox tail
					chestTranslate()
					glRotatef(-90F, 0F, 1F, 0F)
					glScalef(1F, 1F, 1F)
					glTranslatef(-1F, -0.2F, -.50F)
					ItemRenderer.renderItemIn2D(Tessellator.instance, kitsuneIcon.maxU, kitsuneIcon.minV, kitsuneIcon.minU, kitsuneIcon.maxV, kitsuneIcon.iconWidth, kitsuneIcon.iconHeight, 1F / 32F)
					glTranslatef(0F, 0F, 0.025F)
					ItemRenderer.renderItemIn2D(Tessellator.instance, kitsuneIcon.maxU, kitsuneIcon.minV, kitsuneIcon.minU, kitsuneIcon.maxV, kitsuneIcon.iconWidth, kitsuneIcon.iconHeight, 1F / 32F)
				} else if (name != "yrsegal" && name != "theLorist" && name != "Tristaric") {
					// Render the Holy Symbol
					val armor = event.entityPlayer.getCurrentArmor(2) != null
					glRotatef(180F, 1F, 0F, 0F)
					glTranslatef(-0.26F, -0.4F, if (armor) 0.21F else 0.15F)
					glScaled(0.5)
					ItemRenderer.renderItemIn2D(Tessellator.instance, defaultIcon.maxU, defaultIcon.minV, defaultIcon.minU, defaultIcon.maxV, defaultIcon.iconWidth, defaultIcon.iconHeight, 1F / 32F)
				}
			}
	}
}
