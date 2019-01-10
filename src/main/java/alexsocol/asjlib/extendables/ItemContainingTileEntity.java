package alexsocol.asjlib.extendables;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureCompass;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

public class ItemContainingTileEntity extends TileEntity {

	private ItemStack item;

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack it) {
		item = it;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		writeCustomNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		readCustomNBT(nbt);
	}

	public void writeCustomNBT(NBTTagCompound nbt) {
		NBTTagCompound compound = new NBTTagCompound();
		if (getItem() != null) getItem().writeToNBT(compound);
		nbt.setTag("item", compound);
	}

	public void readCustomNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("item")) setItem(ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("item")));
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeCustomNBT(nbt);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
		readCustomNBT(packet.func_148857_g());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}
	
	public static void renderItem(ItemContainingTileEntity tile) {
		ItemStack itemstack = tile.getItem();
		if (itemstack != null) {
			GL11.glDisable(GL11.GL_CULL_FACE);
			EntityItem entityitem = new EntityItem(tile.getWorldObj(), 0.0, 0.0, 0.0, itemstack);
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
					texturecompass.currentAngle = 0.0;
					texturecompass.angleDelta = 0.0;
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
			RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0, -0.2501, 0.0, 0.0F, 0.0F);
			RenderItem.renderInFrame = false;

			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glPopMatrix();
		}
	}
}
