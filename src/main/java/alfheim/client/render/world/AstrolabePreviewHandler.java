/**
 * This class was created by <Vazkii>, backported to 1.7.10 by <AlexSocol>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [27/10/2016, 17:55:20 (GMT)]
 */
package alfheim.client.render.world;

import static org.lwjgl.opengl.GL11.*;

import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.common.item.ItemAstrolabe;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public final class AstrolabePreviewHandler {

	@SubscribeEvent
	public static void onWorldRenderLast(RenderWorldLastEvent event) {
		World world = Minecraft.getMinecraft().theWorld;
		List<EntityPlayer> playerEntities = world.playerEntities;
		for (EntityPlayer player : playerEntities) {
			ItemStack currentStack = player.getHeldItem();
			if(currentStack != null && currentStack.getItem() instanceof ItemAstrolabe) {
				Block block = ItemAstrolabe.getBlock(currentStack);
				if(block != Blocks.air)
					renderPlayerLook(player, currentStack);
			}
		}
	}

	private static void renderPlayerLook(EntityPlayer player, ItemStack stack) {
		List<Vector3> coords = ItemAstrolabe.getBlocksToPlace(stack, player);
		if (ItemAstrolabe.hasBlocks(stack, player, coords)) {
			Block block = ItemAstrolabe.getBlock(stack);
			int meta = ItemAstrolabe.getBlockMeta(stack);

			if (block == null) return;
			
			glPushMatrix();
			glDisable(GL_CULL_FACE);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			
			glColor4d(1, 1, 1, 0.4);
			
			for(Vector3 coord : coords)
				renderBlockAt(player, block, meta, coord);
			
			glColor4d(1, 1, 1, 1);
			glDisable(GL_BLEND);
			glEnable(GL_CULL_FACE);
			glPopMatrix();
		}
	}

	private static void renderBlockAt(EntityPlayer player, Block block, int meta, Vector3 pos) {
		glPushMatrix();
		ASJUtilities.interpolatedTranslationReverse(player);
		glDisable(GL_DEPTH_TEST);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.disableColor();
		RenderBlocks.getInstance().blockAccess = player.worldObj;
		RenderBlocks.getInstance().renderAllFaces = true;
		RenderBlocks.getInstance().renderBlockByRenderType(block, (int) pos.x, (int) pos.y, (int) pos.z);
		Tessellator.instance.draw();
		
		glEnable(GL_DEPTH_TEST);
		glPopMatrix();
	}
}