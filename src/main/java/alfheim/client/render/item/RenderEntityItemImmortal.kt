package alfheim.client.render.item

import alexsocol.asjlib.*
import alfheim.common.entity.item.EntityItemImmortal
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.entity.*
import net.minecraft.client.renderer.texture.*
import net.minecraft.entity.Entity
import net.minecraft.item.*
import net.minecraft.util.*
import net.minecraftforge.client.IItemRenderer.*
import net.minecraftforge.client.MinecraftForgeClient
import org.lwjgl.opengl.*
import java.util.*

@SideOnly(Side.CLIENT)
object RenderEntityItemImmortal: Render() {
	
	private val RES_ITEM_GLINT = ResourceLocation("textures/misc/enchanted_item_glint.png")
	private val renderBlocksRi = RenderBlocks()
	private val random = Random()
	
	init {
		shadowSize = 0.15f
		shadowOpaque = 0.75f
	}
	
	override fun doRender(entity: Entity, x: Double, y: Double, z: Double, noop: Float, ticks: Float) {
		if (entity !is EntityItemImmortal) return
		
		val stack = entity.stack ?: return
		
		if (stack.item != null) {
			bindEntityTexture(entity)
			TextureUtil.func_152777_a(false, false, 1f)
			random.setSeed(187L)
			GL11.glPushMatrix()
			val f2 = MathHelper.sin((entity.age.F + ticks) / 10f + entity.hoverStart) * 0.1f + 0.1f
			val f3 = ((entity.age + ticks) / 20f + entity.hoverStart) * (180 / Math.PI.F)
			val b0 = when {
				stack.stackSize > 1  -> 2
				stack.stackSize > 5  -> 3
				stack.stackSize > 20 -> 4
				stack.stackSize > 40 -> 5
				else                 -> 1
			}
			GL11.glTranslated(x, y + f2, z)
			GL11.glEnable(GL12.GL_RESCALE_NORMAL)
			var f6: Float
			var f7: Float
			var k: Int
			if (aForgeHookForCustomRender(entity, stack, f2, f3, random, renderManager.renderEngine, field_147909_c, b0)) {
				// Forge rendered everything
			} else if (stack.itemSpriteNumber == 0 && stack.item is ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(stack.item).renderType)) {
				val block = Block.getBlockFromItem(stack.item)
				GL11.glRotatef(f3, 0f, 1f, 0f)
				var f9 = 0.25f
				k = block.renderType
				if (k == 1 || k == 19 || k == 12 || k == 2) {
					f9 = 0.5f
				}
				if (block.renderBlockPass > 0) {
					GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f)
					GL11.glEnable(GL11.GL_BLEND)
					OpenGlHelper.glBlendFunc(770, 771, 1, 0)
				}
				GL11.glScalef(f9, f9, f9)
				for (l in 0 until b0) {
					GL11.glPushMatrix()
					if (l > 0) {
						f6 = (random.nextFloat() * 2f - 1f) * 0.2f / f9
						f7 = (random.nextFloat() * 2f - 1f) * 0.2f / f9
						val f8 = (random.nextFloat() * 2f - 1f) * 0.2f / f9
						GL11.glTranslatef(f6, f7, f8)
					}
					renderBlocksRi.renderBlockAsItem(block, stack.getItemDamage(), 1f)
					GL11.glPopMatrix()
				}
				if (block.renderBlockPass > 0) {
					GL11.glDisable(GL11.GL_BLEND)
				}
			} else {
				var f5: Float
				if ( /*itemstack.getItemSpriteNumber() == 1 &&*/stack.item.requiresMultipleRenderPasses()) {
					GL11.glScalef(0.5f, 0.5f, 0.5f)
					for (j in 0 until stack.item.getRenderPasses(stack.getItemDamage())) {
						random.setSeed(187L)
						val iicon1 = stack.item.getIcon(stack, j)
						k = stack.item.getColorFromItemStack(stack, j)
						f5 = (k shr 16 and 255).F / 255f
						f6 = (k shr 8 and 255).F / 255f
						f7 = (k and 255).F / 255f
						GL11.glColor4f(f5, f6, f7, 1f)
						renderDroppedItem(entity, iicon1, b0, ticks, f5, f6, f7, j)
					}
				} else {
					if (stack.item is ItemCloth) {
						GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f)
						GL11.glEnable(GL11.GL_BLEND)
						OpenGlHelper.glBlendFunc(770, 771, 1, 0)
					}
					GL11.glScalef(0.5f, 0.5f, 0.5f)
					val iicon = stack.iconIndex
					val i = stack.item.getColorFromItemStack(stack, 0)
					val f4 = (i shr 16 and 255).F / 255f
					f5 = (i shr 8 and 255).F / 255f
					f6 = (i and 255).F / 255f
					renderDroppedItem(entity, iicon, b0, ticks, f4, f5, f6)
					if (stack.item is ItemCloth) {
						GL11.glDisable(GL11.GL_BLEND)
					}
				}
			}
			
			GL11.glDisable(GL12.GL_RESCALE_NORMAL)
			GL11.glPopMatrix()
			bindEntityTexture(entity)
			TextureUtil.func_147945_b()
		}
	}
	
	fun aForgeHookForCustomRender(entity: EntityItemImmortal, item: ItemStack, bobing: Float, rotation: Float, random: Random, engine: TextureManager, renderBlocks: RenderBlocks?, count: Int): Boolean {
		val customRenderer = MinecraftForgeClient.getItemRenderer(item, ItemRenderType.ENTITY) ?: return false
		if (customRenderer.shouldUseRenderHelper(ItemRenderType.ENTITY, item, ItemRendererHelper.ENTITY_ROTATION)) {
			GL11.glRotatef(rotation, 0f, 1f, 0f)
		}
		if (!customRenderer.shouldUseRenderHelper(ItemRenderType.ENTITY, item, ItemRendererHelper.ENTITY_BOBBING)) {
			GL11.glTranslatef(0f, -bobing, 0f)
		}
		val is3D = customRenderer.shouldUseRenderHelper(ItemRenderType.ENTITY, item, ItemRendererHelper.BLOCK_3D)
		engine.bindTexture(if (item.itemSpriteNumber == 0) TextureMap.locationBlocksTexture else TextureMap.locationItemsTexture)
		val block = if (item.item is ItemBlock) Block.getBlockFromItem(item.item) else null
		if (is3D || block != null && RenderBlocks.renderItemIn3d(block.renderType)) {
			val renderType = block?.renderType ?: 1
			val scale = if (renderType == 1 || renderType == 19 || renderType == 12 || renderType == 2) 0.5f else 0.25f
			val blend = block != null && block.renderBlockPass > 0
			if (RenderItem.renderInFrame) {
				GL11.glScalef(1.25f, 1.25f, 1.25f)
				GL11.glTranslatef(0f, 0.05f, 0f)
				GL11.glRotatef(-90f, 0f, 1f, 0f)
			}
			if (blend) {
				GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f)
				GL11.glEnable(GL11.GL_BLEND)
				OpenGlHelper.glBlendFunc(770, 771, 1, 0)
			}
			GL11.glScalef(scale, scale, scale)
			for (j in 0 until count) {
				GL11.glPushMatrix()
				if (j > 0) {
					GL11.glTranslatef(
						(random.nextFloat() * 2f - 1f) * 0.2f / scale,
						(random.nextFloat() * 2f - 1f) * 0.2f / scale,
						(random.nextFloat() * 2f - 1f) * 0.2f / scale)
				}
				customRenderer.renderItem(ItemRenderType.ENTITY, item, renderBlocks, entity)
				GL11.glPopMatrix()
			}
			if (blend) {
				GL11.glDisable(GL11.GL_BLEND)
			}
		} else {
			GL11.glScalef(0.5f, 0.5f, 0.5f)
			customRenderer.renderItem(ItemRenderType.ENTITY, item, renderBlocks, entity)
		}
		return true
	}
	
	/**
	 * Renders a dropped item
	 */
	fun renderDroppedItem(entity: EntityItemImmortal, dIcon: IIcon?, itemCount: Int, hower: Float, red: Float, green: Float, blue: Float, pass: Int = 0) {
		val tessellator = Tessellator.instance
		
		val icon: IIcon = dIcon ?: (mc.textureManager.getTexture(getEntityTexture(entity)) as TextureMap).getAtlasSprite("missingno")
		
		val f14 = icon.minU
		val f15 = icon.maxU
		val f4 = icon.minV
		val f5 = icon.maxV
		val f6 = 1f
		val f7 = 0.5
		val f8 = 0.25
		var f10: Float
		
		if (renderManager.options.fancyGraphics) {
			GL11.glPushMatrix()
			GL11.glRotatef(((entity.age.F + hower) / 20f + entity.hoverStart) * (180f / Math.PI.F), 0f, 1f, 0f)
			val f9 = 0.0625f
			f10 = 0.021875f
			val stack = entity.stack
			val j = stack?.stackSize ?: 1
			
			val b0 = when {
				j < 2  -> 1
				j < 16 -> 2
				j < 32 -> 3
				else   -> 4
			}
			
			GL11.glTranslated(-f7, -f8, -((f9 + f10) * b0.D / 2))
			for (k in 0 until b0) {
				if (k > 0) {
					val x = (random.nextFloat() * 2f - 1f) * 0.3f / 0.5f
					val y = (random.nextFloat() * 2f - 1f) * 0.3f / 0.5f
//					val z = (random.nextFloat() * 2f - 1f) * 0.3f / 0.5f
					GL11.glTranslatef(x, y, f9 + f10)
				} else {
					GL11.glTranslatef(0f, 0f, f9 + f10)
				}
				if (stack?.itemSpriteNumber ?: 1 == 0) {
					bindTexture(TextureMap.locationBlocksTexture)
				} else {
					bindTexture(TextureMap.locationItemsTexture)
				}
				GL11.glColor4f(red, green, blue, 1f)
				ItemRenderer.renderItemIn2D(tessellator, f15, f4, f14, f5, icon.iconWidth, icon.iconHeight, f9)
				if (stack?.hasEffect(pass) == true) {
					GL11.glDepthFunc(GL11.GL_EQUAL)
					GL11.glDisable(GL11.GL_LIGHTING)
					renderManager.renderEngine.bindTexture(RES_ITEM_GLINT)
					GL11.glEnable(GL11.GL_BLEND)
					GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE)
					val f11 = 0.76f
					GL11.glColor4f(0.5f * f11, 0.25f * f11, 0.8f * f11, 1f)
					GL11.glMatrixMode(GL11.GL_TEXTURE)
					GL11.glPushMatrix()
					val f12 = 0.125f
					GL11.glScalef(f12, f12, f12)
					var f13 = (Minecraft.getSystemTime() % 3000L).F / 3000f * 8f
					GL11.glTranslatef(f13, 0f, 0f)
					GL11.glRotatef(-50f, 0f, 0f, 1f)
					ItemRenderer.renderItemIn2D(tessellator, 0f, 0f, 1f, 1f, 255, 255, f9)
					GL11.glPopMatrix()
					GL11.glPushMatrix()
					GL11.glScalef(f12, f12, f12)
					f13 = (Minecraft.getSystemTime() % 4873L).F / 4873f * 8f
					GL11.glTranslatef(-f13, 0f, 0f)
					GL11.glRotatef(10f, 0f, 0f, 1f)
					ItemRenderer.renderItemIn2D(tessellator, 0f, 0f, 1f, 1f, 255, 255, f9)
					GL11.glPopMatrix()
					GL11.glMatrixMode(GL11.GL_MODELVIEW)
					GL11.glDisable(GL11.GL_BLEND)
					GL11.glEnable(GL11.GL_LIGHTING)
					GL11.glDepthFunc(GL11.GL_LEQUAL)
				}
			}
			GL11.glPopMatrix()
		} else {
			for (l in 0 until itemCount) {
				GL11.glPushMatrix()
				if (l > 0) {
					f10 = (random.nextFloat() * 2f - 1f) * 0.3f
					val f16 = (random.nextFloat() * 2f - 1f) * 0.3f
					val f17 = (random.nextFloat() * 2f - 1f) * 0.3f
					GL11.glTranslatef(f10, f16, f17)
				}
				GL11.glRotatef(180f - renderManager.playerViewY, 0f, 1f, 0f)
				GL11.glColor4f(red, green, blue, 1f)
				tessellator.startDrawingQuads()
				tessellator.setNormal(0f, 1f, 0f)
				tessellator.addVertexWithUV(-f7, -f8, 0.0, f14.D, f5.D)
				tessellator.addVertexWithUV(f6 - f7, -f8, 0.0, f15.D, f5.D)
				tessellator.addVertexWithUV(f6 - f7, 1 - f8, 0.0, f15.D, f4.D)
				tessellator.addVertexWithUV(-f7, 1 - f8, 0.0, f14.D, f4.D)
				tessellator.draw()
				GL11.glPopMatrix()
			}
		}
	}
	
	override fun getEntityTexture(entity: Entity) = getEntityTexture(entity as EntityItemImmortal)
	
	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	fun getEntityTexture(entity: EntityItemImmortal): ResourceLocation {
		return renderManager.renderEngine.getResourceLocation(entity.stack?.itemSpriteNumber ?: 1)
	}
}