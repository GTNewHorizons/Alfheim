package alfheim.client.render.block;

import org.lwjgl.opengl.GL11;

import alexsocol.asjlib.ASJUtilities;
import alfheim.ModInfo;
import alfheim.common.block.tile.TileAnyavil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureCompass;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class RenderTileAnyavil extends TileEntitySpecialRenderer {

	public static final ResourceLocation texture = new ResourceLocation("botania:textures/blocks/storage2.png");
	public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(ModInfo.MODID, "model/Anyavil.obj"));
	
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
		renderTE((TileAnyavil)tile, x, y, z, partialTicks);		
	}
	
	private void renderTE(TileAnyavil tile, double x, double y, double z, float partialTicks) {
		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5, y + 0.425, z + 0.5);
		GL11.glRotatef(90 * (tile.blockMetadata + 1), 0.0F, 1.0F, 0.0F);

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		model.renderAll();

		GL11.glRotated(90, 1, 0, 0);
		GL11.glRotated(135, 0, 0, 1);
		GL11.glTranslated(0, 0.07, -0.6);
		GL11.glScaled(1.5, 1.5, 1.5);

		renderItem(tile);
		GL11.glPopMatrix();
	}
	
	public void renderItem(TileAnyavil tile) {
		ItemStack itemstack = tile.getItem();
		if (itemstack != null) {
			GL11.glDisable(GL11.GL_CULL_FACE);
			EntityItem entityitem = new EntityItem(tile.getWorldObj(), 0.0D, 0.0D, 0.0D, itemstack);
			Item item = entityitem.getEntityItem().getItem();
			entityitem.getEntityItem().stackSize = tile.getItem().stackSize;
			entityitem.hoverStart = 0.0F;
			GL11.glPushMatrix();
	        Tessellator.instance.setBrightness(tile.getBlockType().getMixedBrightnessForBlock(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord));

			if (item == Items.compass) {
				TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
				texturemanager.bindTexture(TextureMap.locationItemsTexture);
				TextureAtlasSprite textureatlassprite1 = ((TextureMap) texturemanager.getTexture(TextureMap.locationItemsTexture)).getAtlasSprite(Items.compass.getIconIndex(entityitem.getEntityItem()).getIconName());

				if (textureatlassprite1 instanceof TextureCompass) {
					TextureCompass texturecompass = (TextureCompass) textureatlassprite1;
					double d0 = texturecompass.currentAngle;
					double d1 = texturecompass.angleDelta;
					texturecompass.currentAngle = 0.0D;
					texturecompass.angleDelta = 0.0D;
					texturecompass.updateCompass(tile.getWorldObj(), tile.xCoord, tile.zCoord, (double) MathHelper.wrapAngleTo180_float((float) (180 + tile.blockMetadata * 90)), false, true);
					texturecompass.currentAngle = d0;
					texturecompass.angleDelta = d1;
				}
				
				TextureAtlasSprite textureatlassprite = ((TextureMap) Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationItemsTexture)).getAtlasSprite(Items.compass.getIconIndex(entityitem.getEntityItem()).getIconName());

				if (textureatlassprite.getFrameCount() > 0) {
					textureatlassprite.updateAnimation();
				}
			}
			RenderItem.renderInFrame = true;
			RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0D, -0.2501D, 0.0D, 0.0F, 0.0F);
			RenderItem.renderInFrame = false;

			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glPopMatrix();
        }
    }
}