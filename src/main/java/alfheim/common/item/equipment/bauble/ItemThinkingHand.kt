package alfheim.common.item.equipment.bauble

import alexsocol.asjlib.mc
import baubles.api.BaubleType
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import net.minecraftforge.client.event.RenderPlayerEvent
import org.lwjgl.opengl.GL11.*
import vazkii.botania.api.item.*
import vazkii.botania.common.core.BotaniaCreativeTab
import vazkii.botania.common.item.equipment.bauble.ItemBauble

class ItemThinkingHand: ItemBauble("ThinkingHand"), ICosmeticBauble {
	
	init {
		creativeTab = BotaniaCreativeTab.INSTANCE
	}
	
	override fun getBaubleType(itemstack: ItemStack) = BaubleType.AMULET
	
	override fun onPlayerBaubleRender(stack: ItemStack, event: RenderPlayerEvent, type: IBaubleRender.RenderType) {
		mc.renderEngine.bindTexture(TextureMap.locationItemsTexture)
		
		if (type == IBaubleRender.RenderType.HEAD) {
			glPushMatrix()
			glTranslated(0.0, (if (event.entityPlayer !== mc.thePlayer) 1.68 else 0.0) - event.entityPlayer.defaultEyeHeight + if (event.entityPlayer.isSneaking) 0.0625 else 0.0, 0.0)
			glRotated(90.0, 0.0, 1.0, 0.0)
			glRotated(180.0, 1.0, 0.0, 0.0)
			glTranslated(-0.4, 0.1, -0.1175)
			alexsocol.asjlib.glScaled(0.495)
			glTranslated(0.4, -0.7, -0.27)
			glRotated(15.0, 0.0, 0.0, -1.0)
			ItemRenderer.renderItemIn2D(Tessellator.instance, itemIcon.maxU, itemIcon.minV, itemIcon.minU, itemIcon.maxV, itemIcon.iconWidth, itemIcon.iconHeight, 1f / 16f)
			glPopMatrix()
		}
	}
	
	override fun addHiddenTooltip(par1ItemStack: ItemStack, par2EntityPlayer: EntityPlayer?, par3List: MutableList<Any?>, par4: Boolean) {
		par3List.add(StatCollector.translateToLocal("botaniamisc.cosmeticBauble").replace("&".toRegex(), "\u00a7"))
		super.addHiddenTooltip(par1ItemStack, par2EntityPlayer, par3List, par4)
	}
}