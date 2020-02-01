package alfheim.client.render.item

import alfheim.api.AlfheimAPI
import alfheim.api.block.tile.SubTileEntity
import alfheim.api.lib.LibResourceLocations
import alfheim.client.core.util.mc
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.util.*
import alfheim.common.item.block.ItemBlockAnomaly
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraftforge.client.IItemRenderer
import net.minecraftforge.client.IItemRenderer.ItemRenderType
import org.lwjgl.opengl.GL11.*

//Render from Thaumcraft nodes by Azanor
object RenderItemAnomaly: IItemRenderer {
	
	override fun handleRenderType(item: ItemStack?, type: ItemRenderType) =
		item != null && item.item === Item.getItemFromBlock(AlfheimBlocks.anomaly) && ItemBlockAnomaly.getType(item) != ItemBlockAnomaly.TYPE_UNDEFINED
	
	override fun shouldUseRenderHelper(type: ItemRenderType, item: ItemStack, helper: IItemRenderer.ItemRendererHelper) =
		helper != IItemRenderer.ItemRendererHelper.EQUIPPED_BLOCK
	
	override fun renderItem(type: ItemRenderType, item: ItemStack, vararg data: Any) {
		if (type == ItemRenderType.ENTITY) {
			glScaled(2.0, 2.0, 2.0)
			glTranslated(-0.5, -0.25, -0.5)
		} else if (type == ItemRenderType.EQUIPPED && data[1] is EntityPlayer) {
			glTranslated(0.0, 0.0, -0.5)
		} else if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
			glRotated(93.2, 1.0, 0.0, 0.0)
			glTranslated(0.0, -0.5, -1.0)
		}
		
		renderItemAnomaly(AlfheimAPI.getAnomalyInstance(ItemBlockAnomaly.getType(item)))
	}
	
	fun renderItemAnomaly(subtile: SubTileEntity) {
		glPushMatrix()
		glAlphaFunc(GL_GREATER, 0.003921569f)
		glDepthMask(false)
		glDisable(GL_CULL_FACE)
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		glColor4d(1.0, 1.0, 1.0, 1.0)
		glPushMatrix()
		
		}		
			}		}
	}
}
