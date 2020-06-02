package alfheim.client.model.armor

import alexsocol.asjlib.mc
import alexsocol.asjlib.render.AdvancedArmorModel
import alfheim.api.ModInfo
import alfheim.api.lib.LibResourceLocations
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.helper.ContributorsPrivacyHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.AdvancedModelLoader
import org.lwjgl.opengl.GL11.*
import vazkii.botania.api.item.IPhantomInkable
import vazkii.botania.client.core.helper.ShaderHelper

class ModelElvoriumArmor
/**armorType: 0 - head, 1 - body and arms, 2 - legs, 3 - feet. */
(private val partType: Int): AdvancedArmorModel() {
	
	val sobakaSutula = arrayOf("GedeonGrays", "Gedeon_Grays")
	
	override fun pre(entity: Entity) {
		mc.renderEngine.bindTexture(LibResourceLocations.elvoriumArmor)
		if (entity is EntityPlayer && ContributorsPrivacyHelper.isCorrect(entity.commandSenderName, "GedeonGrays")) {
			glEnable(GL_BLEND)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			if (ContributorsPrivacyHelper.isCorrect(mc.thePlayer.commandSenderName, "GedeonGrays") && !AlfheimConfigHandler.fancies) return
			ShaderHelper.useShader(ShaderHelper.halo)
		}
	}
	
	override fun post(entity: Entity) {
		if (entity is EntityPlayer && ContributorsPrivacyHelper.isCorrect(entity.commandSenderName, "GedeonGrays")) {
			glDisable(GL_BLEND)
			ShaderHelper.releaseShader()
		}
	}
	
	override fun partHead(entity: Entity) {
		if (ink(entity, partType)) return
		if (partType == 0) {
			val s = 0.01
			glTranslatef(0f, -0.75f, 0f)
			alexsocol.asjlib.glScaled(s)
			model.renderPart("Head")
		}
	}
	
	override fun partBody(entity: Entity) {
		if (ink(entity, partType)) return
		if (partType == 1) {
			val s = 0.01
			glTranslated(0.0, -0.75, 0.0)
			alexsocol.asjlib.glScaled(s)
			model.renderPart("Body")
		} else if (partType == 2) {
			val s = 0.01
			glTranslated(0.0, -0.73, 0.0)
			alexsocol.asjlib.glScaled(s)
			model.renderPart("Belt")
		}
	}
	
	override fun partRightArm(entity: Entity) {
		if (ink(entity, partType)) return
		if (partType == 1) {
			val s = 0.01
			glTranslated(0.31, -0.55, 0.0)
			alexsocol.asjlib.glScaled(s)
			model.renderPart("ArmO")
		}
	}
	
	override fun partLeftArm(entity: Entity) {
		if (ink(entity, partType)) return
		if (partType == 1) {
			val s = 0.01
			glTranslated(-0.31, -0.55, 0.0)
			alexsocol.asjlib.glScaled(s)
			model.renderPart("ArmT")
		}
	}
	
	override fun partRightLeg(entity: Entity) {
		if (ink(entity, partType)) return
		if (partType == 2) {
			val s = 0.01
			glTranslated(0.125, 0.01, 0.0)
			alexsocol.asjlib.glScaled(s)
			model.renderPart("pantsO")
		} else if (partType == 3) {
			val s = 0.01
			glTranslated(0.125, 0.0, 0.0)
			alexsocol.asjlib.glScaled(s)
			model.renderPart("BootO")
		}
	}
	
	override fun partLeftLeg(entity: Entity) {
		if (ink(entity, partType)) return
		if (partType == 2) {
			val s = 0.01
			glTranslated(-0.125, 0.01, 0.0)
			alexsocol.asjlib.glScaled(s)
			model.renderPart("PantsT")
		} else if (partType == 3) {
			val s = 0.01
			glTranslated(-0.125, 0.0, 0.0)
			alexsocol.asjlib.glScaled(s)
			model.renderPart("BootT")
		}
	}
	
	override fun hasOffhand(entity: Entity) = false // entity is EntityPlayer && entity.commandSenderName == "AlexSocol" && entity.heldItem?.item !== AlfheimItems.royalStaff
	
	companion object {
		
		val model = AdvancedModelLoader.loadModel(ResourceLocation(ModInfo.MODID, "model/ElvoriumArmor.obj"))!!
		
		fun ink(e: Entity, slot: Int): Boolean {
			return e is EntityPlayer && (e.inventory.armorItemInSlot(3 - slot)?.item as? IPhantomInkable)?.hasPhantomInk(e.inventory.armorItemInSlot(3 - slot)) == true
		}
	}
}