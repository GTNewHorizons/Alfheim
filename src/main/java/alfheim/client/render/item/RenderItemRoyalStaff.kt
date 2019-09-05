package alfheim.client.render.item

import alfheim.client.model.item.ModelCreatorStaff
import net.minecraft.client.Minecraft
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.client.IItemRenderer
import org.lwjgl.opengl.GL11

class RenderItemRoyalStaff: IItemRenderer {
	
	override fun renderItem(type: IItemRenderer.ItemRenderType?, item: ItemStack?, vararg data: Any?) {
		if (item != null) {
			val pt = Minecraft.getMinecraft().timer.renderPartialTicks
			var wielder: EntityLivingBase? = null
			if (type === IItemRenderer.ItemRenderType.EQUIPPED || type === IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
				wielder = data[1] as EntityLivingBase
			}
			
			GL11.glPushMatrix()
			GL11.glTranslated(0.0, 0.5, 0.0)
			
			if (type !== IItemRenderer.ItemRenderType.INVENTORY) {
				if (type === IItemRenderer.ItemRenderType.ENTITY) {
					GL11.glTranslated(0.0, 1.5, 0.0)
					GL11.glScaled(0.9, 0.9, 0.9)
				} else {
					GL11.glTranslated(0.5, 1.5, 0.5)
					if (type === IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
						GL11.glScaled(1.0, 1.1, 1.0)
					}
				}
			} else {
				GL11.glScaled(0.8, 0.8, 0.8)
				GL11.glRotatef(66.0f, 0.0f, 0.0f, 1.0f)
				GL11.glTranslated(0.0, 0.6, 0.0)
				GL11.glTranslated(-0.7, 0.6, 0.0)
			}
			
			GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f)
			if (wielder != null && wielder is EntityPlayer && wielder.itemInUse != null) {
				var t = wielder.itemInUseDuration.toFloat() + pt
				if (t > 3.0f) {
					t = 3.0f
				}
				
				GL11.glTranslated(0.0, 1.0, 0.0)
				if (type !== IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
					GL11.glRotatef(33.0f, 0.0f, 0.0f, 1.0f)
				} else {
					GL11.glRotatef(10.0f, 1.0f, 0.0f, 0.0f)
					GL11.glRotatef(10.0f, 0.0f, 0.0f, 1.0f)
				}
				
				GL11.glRotatef(60.0f * (t / 3.0f), -1.0f, 0.0f, 0.0f)
				
				GL11.glTranslated(0.0, -1.0, 0.0)
			}
			
			GL11.glEnable(GL11.GL_BLEND)
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
			ModelCreatorStaff.render()
			GL11.glDisable(GL11.GL_BLEND)
			GL11.glPopMatrix()
		}
	}
	
	override fun handleRenderType(item: ItemStack, type: IItemRenderer.ItemRenderType) = true
	
	override fun shouldUseRenderHelper(type: IItemRenderer.ItemRenderType, item: ItemStack, helper: IItemRenderer.ItemRendererHelper) = helper != IItemRenderer.ItemRendererHelper.BLOCK_3D
}
