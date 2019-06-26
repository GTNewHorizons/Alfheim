package alfheim.client.render.world

import org.lwjgl.opengl.GL11.*

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.common.item.ItemAstrolabe
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.client.event.RenderWorldLastEvent

object AstrolabePreviewHandler {
	
	@SubscribeEvent
	fun onWorldRenderLast(event: RenderWorldLastEvent) {
		val world = Minecraft.getMinecraft().theWorld
		val playerEntities = world.playerEntities
		for (player in playerEntities) {
			val currentStack = player.getHeldItem()
			if (currentStack != null && currentStack!!.getItem() is ItemAstrolabe) {
				val block = ItemAstrolabe.getBlock(currentStack)
				if (block !== Blocks.air)
					renderPlayerLook(player, currentStack)
			}
		}
	}
	
	private fun renderPlayerLook(player: EntityPlayer, stack: ItemStack) {
		val coords = ItemAstrolabe.getBlocksToPlace(stack, player)
		if (ItemAstrolabe.hasBlocks(stack, player, coords)) {
			val block = ItemAstrolabe.getBlock(stack)
			val meta = ItemAstrolabe.getBlockMeta(stack)
			
			if (block == null) return
			
			glPushMatrix()
			glDisable(GL_CULL_FACE)
			glEnable(GL_BLEND)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			
			glColor4d(1.0, 1.0, 1.0, 0.4)
			
			for (coord in coords)
				renderBlockAt(player, block, meta, coord)
			
			glColor4d(1.0, 1.0, 1.0, 1.0)
			glDisable(GL_BLEND)
			glEnable(GL_CULL_FACE)
			glPopMatrix()
		}
	}
	
	private fun renderBlockAt(player: EntityPlayer, block: Block, meta: Int, pos: Vector3) {
		glPushMatrix()
		ASJUtilities.interpolatedTranslationReverse(player)
		glDisable(GL_DEPTH_TEST)
		
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture)
		
		Tessellator.instance.startDrawingQuads()
		Tessellator.instance.disableColor()
		RenderBlocks.getInstance().blockAccess = player.worldObj
		RenderBlocks.getInstance().renderAllFaces = true
		RenderBlocks.getInstance().renderBlockByRenderType(block, pos.x.toInt(), pos.y.toInt(), pos.z.toInt())
		Tessellator.instance.draw()
		
		glEnable(GL_DEPTH_TEST)
		glPopMatrix()
	}
}