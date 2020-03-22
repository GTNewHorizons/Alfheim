package alfheim.client.render.entity

import alfheim.client.core.util.*
import alfheim.client.model.item.ModelCreatorStaff
import alfheim.common.core.helper.ContributorsPrivacyHelper
import alfheim.common.core.util.F
import alfheim.common.item.AlfheimItems
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.item.ItemStack
import net.minecraft.potion.Potion
import net.minecraftforge.client.event.RenderPlayerEvent
import org.lwjgl.opengl.GL11.*
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword

object RenderEntitysLeftHand {
	
	fun render(e: RenderPlayerEvent.Specials.Pre) {
		if (e.entityPlayer.isInvisible || e.entityPlayer.isInvisibleToPlayer(mc.thePlayer) || e.entityPlayer.isPotionActive(Potion.invisibility)) return
		
		val name = e.entityPlayer.commandSenderName
		
		renderLeftArm(e) {
			when {
				ContributorsPrivacyHelper.isCorrect(name, "AlexSocol") -> renderRoyalStaff(e)
				name == "Kirito"                                       -> renderDualSwords(e)
			}
		}
	}
	
	private fun renderLeftArm(e: RenderPlayerEvent.Specials.Pre, render: (RenderPlayerEvent.Specials.Pre) -> Unit) {
		glPushMatrix()
		
		if (e.renderer.mainModel.isChild) {
			glTranslatef(0f, 0.625f, 0f)
			glRotatef(-20f, -1f, 0f, 0f)
			glScaled(0.5)
		}
		
		e.renderer.modelBipedMain.bipedLeftArm.postRender(0.0625f)
		glTranslatef(-0.0625f, 0.4375f, 0.0625f)
		
		val f1 = 0.625f
		
		glTranslatef(0f, 0.1875f, 0f)
		glScalef(f1, -f1, f1)
		glRotatef(-80f, 1f, 0f, 0f)
		glRotatef(45f, 0f, 1f, 0f)
		
		val f2: Float
		val i = 0xFFFFFF
		val f5: Float
		
		val f4 = (i shr 16 and 255).F / 255f
		f5 = (i shr 8 and 255).F / 255f
		f2 = (i and 255).F / 255f
		glColor4f(f4, f5, f2, 1f)
		
		glPushMatrix()
		render.invoke(e)
		glPopMatrix()
		
		glPopMatrix()
	}
	
	private fun renderRoyalStaff(e: RenderPlayerEvent.Specials.Pre) {
		if (e.entityPlayer.heldItem?.item !== AlfheimItems.royalStaff) {
			glTranslated(0.1, 1.5, 0.15)
			glRotatef(180f, 1f, 0f, 0f)
			
			glEnable(GL_BLEND)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			ModelCreatorStaff.render()
			glDisable(GL_BLEND)
		}
	}
	
	private fun renderDualSwords(e: RenderPlayerEvent.Specials.Pre) {
		val player = e.entityPlayer
		val stack = player.heldItem ?: return
		val item = stack.item
		val exc = item === AlfheimItems.excaliber
		val ell = stack.displayName.toLowerCase().trim() == "the elucidator"
		
		if (item is ItemManasteelSword && (exc || ell)) {
			glPushMatrix()
			glRotatef(-20f, 1f, 0f, 1f)
			glRotatef(-10f, 0f, 1f, 0f)
			glTranslatef(0.25f, 0f, 0.15f)
			
			RenderManager.instance.itemRenderer.renderItem(player, if (exc && !ell) {
				val els = ItemStack(ModItems.manasteelSword); els.setStackDisplayName("the elucidator")
			} else ItemStack(AlfheimItems.excaliber), 0)
			glPopMatrix()
		}
	}
}