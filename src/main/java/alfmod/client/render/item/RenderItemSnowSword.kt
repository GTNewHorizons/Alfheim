package alfmod.client.render.item

import alfheim.client.core.util.*
import alfmod.AlfheimModularCore
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.IItemRenderer
import net.minecraftforge.client.IItemRenderer.ItemRenderType
import net.minecraftforge.client.IItemRenderer.ItemRenderType.*
import net.minecraftforge.client.model.AdvancedModelLoader
import org.lwjgl.opengl.GL11.*
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.item.equipment.bauble.ItemIcePendant

object RenderItemSnowSword: IItemRenderer {
	
	val model = AdvancedModelLoader.loadModel(ResourceLocation(AlfheimModularCore.MODID, "model/Katana.obj"))
	val texture = ResourceLocation(AlfheimModularCore.MODID, "textures/model/item/SnowKatana.png")
	
	override fun renderItem(type: ItemRenderType, item: ItemStack, vararg data: Any?) {
		glPushMatrix()
		
		if (type == EQUIPPED_FIRST_PERSON || type == EQUIPPED) {
			glRotatef(135f, 0f, 1f, 0f)
			glTranslatef(-0.7f, 0.5f, -0.15f)
			glRotatef(if (type == EQUIPPED_FIRST_PERSON) -5f else -15f, 1f, 0f, 0f)
			
			data.firstOrNull { it is EntityPlayer }?.let { it as EntityPlayer
				if (it.isBlocking) glRotatef(-90f, 0f, 1f, 0f)
			}
		}
		
		if (type == INVENTORY) {
			glRotatef(-45f, 1f, 1f, 1f)
			glTranslatef(0f, -1f, 0f)
			glScaled(0.75)
		}
		
		mc.renderEngine.bindTexture(texture)
		model.renderAll()
		
		glRotatef(90f, 1f, 0f, 0f)
		glRotatef(45f, 0f, 0f, 1f)
		glTranslated(-0.5, -0.5, -0.4 + 1/16f)
		
		if (type == INVENTORY) glEnable(GL_BLEND)
		
		val icon = (ModItems.icePendant as ItemIcePendant).gemIcon
		mc.renderEngine.bindTexture(TextureMap.locationItemsTexture)
		ItemRenderer.renderItemIn2D(Tessellator.instance, icon.maxU, icon.minV, icon.minU, icon.maxV, icon.iconWidth, icon.iconHeight, 1f / 16f)
		
		if (type == INVENTORY) glDisable(GL_BLEND)
		glPopMatrix()
	}
	
	override fun handleRenderType(item: ItemStack?, type: ItemRenderType?) = true
	override fun shouldUseRenderHelper(type: ItemRenderType?, item: ItemStack?, helper: IItemRenderer.ItemRendererHelper?) = helper != IItemRenderer.ItemRendererHelper.BLOCK_3D
}