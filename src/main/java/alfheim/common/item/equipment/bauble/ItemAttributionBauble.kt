package alfheim.common.item.equipment.bauble

import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.common.core.helper.IconHelper
import baubles.api.BaubleType
import cpw.mods.fml.relauncher.*
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.texture.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.common.MinecraftForge
import org.lwjgl.opengl.GL11
import vazkii.botania.api.item.*
import vazkii.botania.client.core.helper.ShaderHelper
import vazkii.botania.client.core.proxy.ClientProxy
import vazkii.botania.client.lib.LibResources
import vazkii.botania.client.model.ModelTinyPotato
import vazkii.botania.common.item.equipment.bauble.ItemBauble
import kotlin.properties.Delegates

class ItemAttributionBauble: ItemBauble("attributionBauble"), ICosmeticBauble {
	
	// private val kitsuneTexture = ResourceLocation("${ModInfo.MODID}:textures/items/kitsunesTail.png")
	private lateinit var potatoTexture: ResourceLocation
	
	var defaultIcon: IIcon by Delegates.notNull()
	var wireIcon: IIcon by Delegates.notNull()
	var kitsuneIcon: IIcon by Delegates.notNull()
	var trisIcon: IIcon by Delegates.notNull()
	
	init {
		setHasSubtypes(true)
		creativeTab = AlfheimCore.baTab
		if (FMLLaunchHandler.side().isClient) {
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
	
	fun addStringToTooltip(s: String, tooltip: MutableList<Any?>?) {
		tooltip!!.add(s.replace("&".toRegex(), "\u00a7"))
	}
	
	override fun getIconFromDamage(dmg: Int) = itemIcon!!
	
	override fun getBaubleType(stack: ItemStack) = BaubleType.AMULET
	
	override fun getUnlocalizedName(par1ItemStack: ItemStack) =
		super.getUnlocalizedName(par1ItemStack) + par1ItemStack.itemDamage
	
	fun faceTranslate() {
		GL11.glRotatef(90F, 0F, 1F, 0F)
		GL11.glRotatef(180F, 1F, 0F, 0F)
		GL11.glTranslatef(-0.4F, 0.1F, -0.25F)
	}
	
	fun chestTranslate() {
		GL11.glRotatef(180F, 1F, 0F, 0F)
		GL11.glTranslatef(-0.5F, -0.7F, 0.15F)
	}
	
	fun scale(f: Float) {
		GL11.glScalef(f, f, f)
	}
	
	override fun onEquipped(stack: ItemStack, player: EntityLivingBase) {
		super.onEquipped(stack, player)
		if (stack.itemDamage != 1 && stack.displayName.toLowerCase().trim() == "vazkii is bae") {
			stack.itemDamage = 1
			stack.tagCompound.removeTag("display")
		}
	}
	
	//    @SideOnly(Side.CLIENT)
	//    fun renderTail(event: RenderPlayerEvent) {
	//        GL11.glEnable(GL11.GL_BLEND)
	//        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
	//        GL11.glShadeModel(GL11.GL_SMOOTH)
	//        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)
	//        GL11.glDisable(GL11.GL_LIGHTING)
	//        GL11.glDisable(GL11.GL_CULL_FACE)
	//
	//        Minecraft.getMinecraft().renderEngine.bindTexture(kitsuneTexture)
	//
	//        IBaubleRender.Helper.rotateIfSneaking(event.entityPlayer)
	//        chestTranslate()
	//
	//        GL11.glRotatef(-90F, 1F, 0F, 0F)
	//
	//        //        GL11.glTranslatef(-1F, -0.2F, -.50F)
	////        GL11.glRotatef(180F, 0F, 1F, 1F)
	////        GL11.glRotatef(-90F, 0F, 1F, 0F)
	//        //        GL11.glScalef(1F, 1F, 1F)
	//
	//        val tes = Tessellator.instance
	//        ShaderHelper.useShader(ShaderHelper.halo)
	//        tes.startDrawingQuads()
	//        tes.addVertexWithUV(-0.75, 0.0, -0.75, 0.0, 0.0)
	//        tes.addVertexWithUV(-0.75, 0.0, 0.75, 0.0, 1.0)
	//        tes.addVertexWithUV(0.75, 0.0, 0.75, 1.0, 1.0)
	//        tes.addVertexWithUV(0.75, 0.0, -0.75, 1.0, 0.0)
	//        tes.draw()
	//        ShaderHelper.releaseShader()
	//
	//        GL11.glEnable(GL11.GL_LIGHTING)
	//        GL11.glShadeModel(GL11.GL_FLAT)
	//        GL11.glEnable(GL11.GL_CULL_FACE)
	//    }
	
	@SideOnly(Side.CLIENT)
	override fun onPlayerBaubleRender(stack: ItemStack, event: RenderPlayerEvent, type: IBaubleRender.RenderType) {
		val name = event.entityPlayer.commandSenderName
		if (type == IBaubleRender.RenderType.HEAD) {
			if (stack.itemDamage != 0) {
				// Render the Tiny Potato on your head... a tiny headtato, if you will.
				Minecraft.getMinecraft().renderEngine.bindTexture(potatoTexture)
				val model = ModelTinyPotato()
				GL11.glTranslatef(0.0F, -2.0F, 0.0F)
				GL11.glRotatef(-90F, 0F, 1F, 0F)
				model.render()
			} else {
				if (name == "yrsegal" || name == "theLorist") {
					// Render the Blueflare
					Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture)
					IBaubleRender.Helper.translateToHeadLevel(event.entityPlayer)
					GL11.glEnable(GL11.GL_BLEND)
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
					GL11.glAlphaFunc(GL11.GL_EQUAL, 1F)
					ShaderHelper.useShader(ShaderHelper.halo)
					faceTranslate()
					scale(0.35F)
					GL11.glTranslatef(0.9F, -0.505F, -0.4F)
					ItemRenderer.renderItemIn2D(Tessellator.instance, wireIcon.maxU, wireIcon.minV, wireIcon.minU, wireIcon.maxV, wireIcon.iconWidth, wireIcon.iconHeight, 1F / 32F)
					ShaderHelper.releaseShader()
					GL11.glAlphaFunc(GL11.GL_ALWAYS, 1F)
					GL11.glDisable(GL11.GL_BLEND)
				} else if (name == "Tristaric") {
					// Render the Ezic Star
					Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture)
					IBaubleRender.Helper.translateToHeadLevel(event.entityPlayer)
					faceTranslate()
					scale(0.5F)
					GL11.glTranslatef(0.3F, -0.45F, 0F)
					ItemRenderer.renderItemIn2D(Tessellator.instance, trisIcon.maxU, trisIcon.minV, trisIcon.minU, trisIcon.maxV, trisIcon.iconWidth, trisIcon.iconHeight, 1F / 32F)
				}
			}
		} else
			if (type == IBaubleRender.RenderType.BODY && stack.itemDamage == 0) {
				Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture)
				IBaubleRender.Helper.rotateIfSneaking(event.entityPlayer)
				if (name == "l0nekitsune") {
					// Render a fox tail
					chestTranslate()
					GL11.glRotatef(-90F, 0F, 1F, 0F)
					GL11.glScalef(1F, 1F, 1F)
					GL11.glTranslatef(-1F, -0.2F, -.50F)
					ItemRenderer.renderItemIn2D(Tessellator.instance, kitsuneIcon.maxU, kitsuneIcon.minV, kitsuneIcon.minU, kitsuneIcon.maxV, kitsuneIcon.iconWidth, kitsuneIcon.iconHeight, 1F / 32F)
					GL11.glTranslatef(0F, 0F, 0.025F)
					ItemRenderer.renderItemIn2D(Tessellator.instance, kitsuneIcon.maxU, kitsuneIcon.minV, kitsuneIcon.minU, kitsuneIcon.maxV, kitsuneIcon.iconWidth, kitsuneIcon.iconHeight, 1F / 32F)
				} else if (name != "yrsegal" && name != "theLorist" && name != "Tristaric") {
					// Render the Holy Symbol
					val armor = event.entityPlayer.getCurrentArmor(2) != null
					GL11.glRotatef(180F, 1F, 0F, 0F)
					GL11.glTranslatef(-0.26F, -0.4F, if (armor) 0.21F else 0.15F)
					scale(0.5F)
					ItemRenderer.renderItemIn2D(Tessellator.instance, defaultIcon.maxU, defaultIcon.minV, defaultIcon.minU, defaultIcon.maxV, defaultIcon.iconWidth, defaultIcon.iconHeight, 1F / 32F)
				}
			}
	}
}
