package alfheim.client.render.item

import alfheim.client.core.util.mc
import alfheim.common.core.util.meta
import cpw.mods.fml.relauncher.ReflectionHelper
import net.minecraft.client.renderer.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.client.IItemRenderer
import net.minecraftforge.client.IItemRenderer.ItemRenderType
import org.lwjgl.opengl.GL11.*
import vazkii.botania.common.lib.LibObfuscation

object RenderMoonBow: IItemRenderer {
	
	override fun handleRenderType(item: ItemStack, type: ItemRenderType) =
		type != ItemRenderType.INVENTORY
	
	override fun shouldUseRenderHelper(type: ItemRenderType, item: ItemStack, helper: IItemRenderer.ItemRendererHelper) =
		helper == IItemRenderer.ItemRendererHelper.ENTITY_ROTATION || helper == IItemRenderer.ItemRendererHelper.ENTITY_BOBBING
	
	override fun renderItem(type: ItemRenderType, stack: ItemStack, vararg data: Any) {
		glPushMatrix()
		when (type) {
			ItemRenderType.ENTITY                -> {
				glTranslatef(-0.5f, 0f, 0f)
				if (stack.isOnItemFrame)
					glTranslatef(0f, -0.3f, 0.01f)
				render(stack, null, false)
			}
			
			ItemRenderType.EQUIPPED              -> {
				if (stack.displayName.toLowerCase().trim { it <= ' ' } == "i'm a banana")
					glTranslated(0.25, 0.25, 0.0)
				
				render(stack, if (data[1] is EntityPlayer) data[1] as EntityPlayer else null, true)
			}
			
			ItemRenderType.EQUIPPED_FIRST_PERSON -> {
				if (stack.displayName.toLowerCase().trim { it <= ' ' } == "i'm a banana" && mc.thePlayer.itemInUse !== stack) {
					glTranslated(0.1, 0.1, 0.0)
				}
				
				render(stack, if (data[1] is EntityPlayer) data[1] as EntityPlayer else null, false)
			}
			
			else                                 -> Unit
		}
		glPopMatrix()
	}
	
	fun render(item: ItemStack, player: EntityPlayer?, transform: Boolean) {
		val dmg = item.meta
		var icon = item.item.getIconFromDamageForRenderPass(dmg, 0)
		if (player != null) {
			val using = ReflectionHelper.getPrivateValue<ItemStack, EntityPlayer>(EntityPlayer::class.java, player, *LibObfuscation.ITEM_IN_USE)
			val time = ReflectionHelper.getPrivateValue<Int, EntityPlayer>(EntityPlayer::class.java, player, *LibObfuscation.ITEM_IN_USE_COUNT)
			icon = item.item.getIcon(item, 0, player, using, time)
			if (transform) {
				glTranslatef(0.2f, -0.3f, 0.1f)
				//GL11.glRotatef(20f, 0f, 1f, 0f);
				//GL11.glRotatef(-100f, 1f, 0f, 0f);
			}
		}
		
		val f = icon.minU
		val f1 = icon.maxU
		val f2 = icon.minV
		val f3 = icon.maxV
		val scale = 1f / 16f
		
		glPushMatrix()
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		glColor4f(1f, 1f, 1f, 1f)
		ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.iconWidth, icon.iconHeight, scale)
		
		glDisable(GL_BLEND)
		glPopMatrix()
		
		glColor4f(1f, 1f, 1f, 1f)
	}
}