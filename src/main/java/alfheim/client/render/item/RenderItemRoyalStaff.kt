package alfheim.client.render.item

import alexsocol.asjlib.*
import alfheim.client.model.item.ModelCreatorStaff
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.client.IItemRenderer
import org.lwjgl.opengl.GL11.*
import kotlin.math.min

object RenderItemRoyalStaff: IItemRenderer {
	
	override fun renderItem(type: IItemRenderer.ItemRenderType, item: ItemStack, vararg data: Any?) {
		var wielder: EntityLivingBase? = null
		if (type === IItemRenderer.ItemRenderType.EQUIPPED || type === IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
			wielder = data[1] as EntityLivingBase
		}
		
		glPushMatrix()
		glTranslated(0.0, 0.5, 0.0)
		
		if (type !== IItemRenderer.ItemRenderType.INVENTORY) {
			if (type === IItemRenderer.ItemRenderType.ENTITY) {
				glTranslated(0.0, 1.5, 0.0)
				glScaled(0.9)
			} else {
				glTranslated(0.5, 1.5, 0.5)
				if (type === IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
					glScaled(1.0, 1.1, 1.0)
				}
			}
		} else {
			glScaled(0.8)
			glRotatef(66f, 0f, 0f, 1f)
			glTranslated(0.0, 0.6, 0.0)
			glTranslated(-0.7, 0.6, 0.0)
		}
		
		glRotatef(180.0f, 1.0f, 0.0f, 0.0f)
		if (wielder != null && wielder is EntityPlayer && wielder.itemInUse != null) {
			val t = min(3f, wielder.itemInUseDuration.F + mc.timer.renderPartialTicks)
			
			glTranslatef(0f, 1f, 0f)
			if (type !== IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
				glRotatef(33f, 0f, 0f, 1f)
			} else {
				glRotatef(10f, 1f, 0f, 0f)
				glRotatef(10f, 0f, 0f, 1f)
			}
			
			glRotatef(60f * (t / 3f), -1f, 0f, 0f)
			
			glTranslated(0.0, -1.0, 0.0)
		}
		
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		ModelCreatorStaff.render()
		glDisable(GL_BLEND)
		glPopMatrix()
	}
	
	override fun handleRenderType(item: ItemStack, type: IItemRenderer.ItemRenderType) = true
	
	override fun shouldUseRenderHelper(type: IItemRenderer.ItemRenderType, item: ItemStack, helper: IItemRenderer.ItemRendererHelper) = helper != IItemRenderer.ItemRendererHelper.BLOCK_3D
}
