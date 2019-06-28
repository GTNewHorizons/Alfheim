package alfheim.client.model.armor

import alexsocol.asjlib.render.AdvancedArmorModel
import alfheim.api.ModInfo
import alfheim.api.lib.LibResourceLocations
import alfheim.common.core.util.AlfheimConfig
import net.minecraft.client.Minecraft
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
	
	override fun pre(entity: Entity) {
		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.elvoriumArmor)
		if (entity is EntityPlayer && entity.getCommandSenderName() == "GedeonGrays") {
			glEnable(GL_BLEND)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			if (Minecraft.getMinecraft().thePlayer.commandSenderName == "GedeonGrays" && !AlfheimConfig.fancies) return
			ShaderHelper.useShader(ShaderHelper.halo)
		}
	}
	
	override fun post(entity: Entity) {
		if (entity is EntityPlayer && entity.getCommandSenderName() == "GedeonGrays") {
			glDisable(GL_BLEND)
			ShaderHelper.releaseShader()
		}
	}
	
	override fun partHead(e: Entity) {
		if (ink(e, partType)) return
		if (partType == 0) {
			val s = 0.01
			glTranslatef(0f, -0.75f, 0f)
			glScaled(s, s, s)
			model.renderPart("Head")
		}
	}
	
	override fun partBody(e: Entity) {
		if (ink(e, partType)) return
		if (partType == 1) {
			val s = 0.01
			glTranslated(0.0, -0.75, 0.0)
			glScaled(s, s, s)
			model.renderPart("Body")
		} else if (partType == 2) {
			val s = 0.01
			glTranslated(0.0, -0.73, 0.0)
			glScaled(s, s, s)
			model.renderPart("Belt")
		}
	}
	
	override fun partRightArm(e: Entity) {
		if (ink(e, partType)) return
		if (partType == 1) {
			val s = 0.01
			glTranslated(0.31, -0.55, 0.0)
			glScaled(s, s, s)
			model.renderPart("ArmO")
		}
	}
	
	override fun partLeftArm(e: Entity) {
		if (ink(e, partType)) return
		if (partType == 1) {
			val s = 0.01
			glTranslated(-0.31, -0.55, 0.0)
			glScaled(s, s, s)
			model.renderPart("ArmT")
		}
	}
	
	override fun partRightLeg(e: Entity) {
		if (ink(e, partType)) return
		if (partType == 2) {
			val s = 0.01
			glTranslated(0.125, 0.01, 0.0)
			glScaled(s, s, s)
			model.renderPart("pantsO")
		} else if (partType == 3) {
			val s = 0.01
			glTranslated(0.125, 0.0, 0.0)
			glScaled(s, s, s)
			model.renderPart("BootO")
		}
	}
	
	override fun partLeftLeg(e: Entity) {
		if (ink(e, partType)) return
		if (partType == 2) {
			val s = 0.01
			glTranslated(-0.125, 0.01, 0.0)
			glScaled(s, s, s)
			model.renderPart("PantsT")
		} else if (partType == 3) {
			val s = 0.01
			glTranslated(-0.125, 0.0, 0.0)
			glScaled(s, s, s)
			model.renderPart("BootT")
		}
	}
	
	companion object {
		
		val model = AdvancedModelLoader.loadModel(ResourceLocation(ModInfo.MODID, "model/ElvoriumArmor.obj"))
		
		fun ink(e: Entity, slot: Int): Boolean {
			var slot = slot
			slot = 3 - slot
			return e is EntityPlayer && e.inventory.armorItemInSlot(slot) != null && e.inventory.armorItemInSlot(slot).item is IPhantomInkable && (e.inventory.armorItemInSlot(slot).item as IPhantomInkable).hasPhantomInk(e.inventory.armorItemInSlot(slot))
		}
	}
}