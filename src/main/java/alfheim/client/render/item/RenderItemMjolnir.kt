package alfheim.client.render.item

import alexsocol.asjlib.*
import alfheim.api.ModInfo
import alfheim.common.core.handler.AlfheimConfigHandler
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.IItemRenderer
import net.minecraftforge.client.model.AdvancedModelLoader
import org.lwjgl.opengl.GL11.*

object RenderItemMjolnir: IItemRenderer {
	
	val model = if (AlfheimConfigHandler.minimalGraphics) null else AdvancedModelLoader.loadModel(ResourceLocation(ModInfo.MODID, "model/Mjolnir.obj"))
	val texture = ResourceLocation(ModInfo.MODID, "textures/model/item/Mjolnir.png")
	
	override fun renderItem(type: IItemRenderer.ItemRenderType, stack: ItemStack, vararg data: Any?) {
		if (model == null) return // renderer won't be registered at all
		
		glPushMatrix()
		
		if (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON || type == IItemRenderer.ItemRenderType.EQUIPPED) {
			glRotatef(135f, 0f, 1f, 0f)
			glTranslatef(-0.7f, 0.5f, -0.15f)
			glRotatef(if (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) -5f else -15f, 1f, 0f, 0f)
			glScaled(0.75)
		}
		
		if (type == IItemRenderer.ItemRenderType.INVENTORY) {
			glRotatef(-45f, 1f, 1f, 1f)
			glRotatef(-67.5f, 0f, 1f, 0f)
			glScaled(0.5)
			glTranslatef(0f, -1f, 0f)
		}
		
		if (type == IItemRenderer.ItemRenderType.ENTITY) {
			glScaled(0.5)
			glTranslatef(0f, 1f, 0f)
		}
		
		mc.renderEngine.bindTexture(texture)
		
		model.renderAll()
		
		glPopMatrix()
	}
	
	override fun handleRenderType(item: ItemStack?, type: IItemRenderer.ItemRenderType?) = true
	override fun shouldUseRenderHelper(type: IItemRenderer.ItemRenderType?, item: ItemStack?, helper: IItemRenderer.ItemRendererHelper?) = helper != IItemRenderer.ItemRendererHelper.BLOCK_3D
}