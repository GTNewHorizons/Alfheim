package alfmod.client.render.entity

import alfmod.AlfheimModularCore
import alfmod.client.render.model.ModelBipedEyes
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.entity.RenderBiped
import net.minecraft.entity.*
import net.minecraft.init.Items
import net.minecraft.item.*
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11.*

@SideOnly(Side.CLIENT)
class RenderEntityDedMoroz: RenderBiped(ModelBipedEyes(), 0.5f) {
	
	val texture = ResourceLocation("${AlfheimModularCore.MODID}:textures/entity/DedMoroz.png")
	
	override fun getEntityTexture(entity: Entity?) = texture
	override fun getEntityTexture(entity: EntityLiving?) = texture
	override fun preRenderCallback(entity: EntityLivingBase?, ticks: Float) = glScaled(3.0, 3.0, 3.0)
	
	override fun renderEquippedItems(entity: EntityLiving, ticks: Float) {
		glColor3f(1f, 1f, 1f)
		val itemstack = entity.heldItem
		val item: Item
		var f1: Float
		
		if (itemstack?.item != null) {
			item = itemstack.item
			glPushMatrix()
			
			if (mainModel.isChild) {
				f1 = 0.5f
				glTranslatef(0f, 0.625f, 0f)
				glRotatef(-20f, -1f, 0f, 0f)
				glScalef(f1, f1, f1)
			}
			
			modelBipedMain.bipedRightArm.postRender(0.0625f)
			glTranslatef(-0.0625f, 0.4375f, 0.0625f)
			
			val customRenderer = net.minecraftforge.client.MinecraftForgeClient.getItemRenderer(itemstack, net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED)
			val is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED, itemstack, net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D)
			
			if (item is ItemBlock && (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(item).renderType))) {
				f1 = 0.5f
				glTranslatef(0f, 0.1875f, -0.3125f)
				f1 *= 0.75f
				glRotatef(20f, 1f, 0f, 0f)
				glRotatef(45f, 0f, 1f, 0f)
				glScalef(-f1, -f1, f1)
			} else if (item === Items.bow) {
				f1 = 0.625f
				glTranslatef(0f, 0.125f, 0.3125f)
				glRotatef(-20f, 0f, 1f, 0f)
				glScalef(f1, -f1, f1)
				glRotatef(-100f, 1f, 0f, 0f)
				glRotatef(45f, 0f, 1f, 0f)
			} else if (item.isFull3D) {
				f1 = 0.625f
				
				if (item.shouldRotateAroundWhenRendering()) {
					glRotatef(180f, 0f, 0f, 1f)
					glTranslatef(0f, -0.125f, 0f)
				}
				
				func_82422_c()
				glScalef(f1, -f1, f1)
				glRotatef(-100f, 1f, 0f, 0f)
				glRotatef(45f, 0f, 1f, 0f)
			} else {
				f1 = 0.375f
				glTranslatef(0.25f, 0.1875f, -0.1875f)
				glScalef(f1, f1, f1)
				glRotatef(60f, 0f, 0f, 1f)
				glRotatef(-90f, 1f, 0f, 0f)
				glRotatef(20f, 0f, 0f, 1f)
			}
			
			var f2: Float
			var i: Int
			var f5: Float
			
			if (itemstack.item.requiresMultipleRenderPasses()) {
				i = 0
				while (i < itemstack.item.getRenderPasses(itemstack.itemDamage)) {
					val j = itemstack.item.getColorFromItemStack(itemstack, i)
					f5 = (j shr 16 and 255).toFloat() / 255f
					f2 = (j shr 8 and 255).toFloat() / 255f
					val f3 = (j and 255).toFloat() / 255f
					glColor4f(f5, f2, f3, 1f)
					glScaled(1.25, 1.25, 1.25)
					renderManager.itemRenderer.renderItem(entity, itemstack, i)
					glScaled(0.8, 0.8, 0.8)
					++i
				}
			} else {
				i = itemstack.item.getColorFromItemStack(itemstack, 0)
				val f4 = (i shr 16 and 255).toFloat() / 255f
				f5 = (i shr 8 and 255).toFloat() / 255f
				f2 = (i and 255).toFloat() / 255f
				glColor4f(f4, f5, f2, 1f)
				glScaled(1.5, 1.5, 1.5)
				renderManager.itemRenderer.renderItem(entity, itemstack, 0)
				glScaled(0.8, 0.8, 0.8)
			}
			
			glPopMatrix()
		}
	}
}
