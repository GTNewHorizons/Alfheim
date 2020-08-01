package alfheim.client.render.tile

import alexsocol.asjlib.*
import alfheim.common.block.tile.TileItemDisplay
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemBlock
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.client.ForgeHooksClient
import org.lwjgl.opengl.GL11.*
import vazkii.botania.client.core.handler.ClientTickHandler
import java.awt.Color
import kotlin.math.sin

@SideOnly(Side.CLIENT)
object RenderTileItemDisplay: TileEntitySpecialRenderer() {
	
	override fun renderTileEntityAt(tile: TileEntity, x: Double, y: Double, z: Double, ticks: Float) {
		if (tile !is TileItemDisplay) return
		
		if (tile.worldObj != null) {
			
			glPushMatrix()
			glColor4f(1f, 1f, 1f, 1f)
			glTranslated(x, y, z)
			
			val var27 = (ClientTickHandler.ticksInGame.F + ticks).D
			
			glPushMatrix()
			glScaled(0.5)
			glTranslatef(1f, 1.25f, 1f)
			glRotatef(360f + var27.F, 0f, 1f, 0f)
			glTranslatef(0f, 0f, 0.5f)
			glRotatef(90f, 0f, 1f, 0f)
			glTranslated(0.0, 0.15 * sin(var27 / 7.5), 0.0)
			val scale = tile.get(0)
			
			if (scale != null) {
				mc.renderEngine.bindTexture(if (scale.item is ItemBlock) TextureMap.locationBlocksTexture else TextureMap.locationItemsTexture)
				glScalef(2f)
				glTranslatef(0.25f, 0f, 0f)
				if (!ForgeHooksClient.renderEntityItem(EntityItem(tile.worldObj, tile.xCoord.D, tile.yCoord.D, tile.zCoord.D, scale), scale, 0f, 0f, tile.worldObj.rand, mc.renderEngine, renderBlocks, 1)) {
					glTranslatef(-0.25f, 0f, 0f)
					glScaled(0.5)
					if (scale.item is ItemBlock && RenderBlocks.renderItemIn3d(scale.item.toBlock()?.renderType ?: 0)) {
						glScaled(0.5)
						glTranslatef(1f, 1.1f, 0f)
						renderBlocks.renderBlockAsItem(scale.item.toBlock(), scale.meta, 1f)
						glTranslatef(-1f, -1.1f, 0f)
						glScalef(2f)
					} else if (scale.item is ItemBlock && !RenderBlocks.renderItemIn3d(scale.item.toBlock()?.renderType ?: 0)) {
						val entityitem: EntityItem?
						glPushMatrix()
						
						glScalef(2f)
						glTranslatef(.25f, .275f, 0f)
						
						val `is` = scale.copy()
						`is`.stackSize = 1
						entityitem = EntityItem(tile.worldObj, 0.0, 0.0, 0.0, `is`)
						entityitem.hoverStart = 0f
						RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0, 0.0, 0.0, 0f, 0f)
						
						glTranslatef(-.25f, -.275f, 0f)
						
						glPopMatrix()
					} else {
						var renderPass = 0
						
						do {
							val icon = scale.item.getIcon(scale, renderPass)
							if (icon != null) {
								val color = Color(scale.item.getColorFromItemStack(scale, renderPass))
								glColor3ub(color.red.toByte(), color.green.toByte(), color.blue.toByte())
								val f = icon.minU
								val f1 = icon.maxU
								val f2 = icon.minV
								val f3 = icon.maxV
								ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.iconWidth, icon.iconHeight, 0.0625f)
								glColor3f(1f, 1f, 1f)
							}
							
							++renderPass
						} while (renderPass < scale.item.getRenderPasses(scale.meta))
					}
				}
			}
			
			glPopMatrix()
			
			glDisable(3008)
			glPushMatrix()
			glTranslatef(0.5f, 1.8f, 0.5f)
			glRotatef(180f, 1f, 0f, 1f)
			glPopMatrix()
			glTranslatef(0f, 0.2f, 0f)
			
			glEnable(3008)
			glPopMatrix()
		}
	}
}
