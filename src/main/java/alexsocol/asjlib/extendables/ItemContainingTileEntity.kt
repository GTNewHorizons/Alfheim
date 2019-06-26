package alexsocol.asjlib.extendables

import org.lwjgl.opengl.GL11.*

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.texture.TextureCompass
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.item.EntityItem
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.Packet
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.MathHelper

open class ItemContainingTileEntity: TileEntity() {
	
	open var item: ItemStack? = null
	
	override fun writeToNBT(nbt: NBTTagCompound) {
		super.writeToNBT(nbt)
		writeCustomNBT(nbt)
	}
	
	override fun readFromNBT(nbt: NBTTagCompound) {
		super.readFromNBT(nbt)
		readCustomNBT(nbt)
	}
	
	open fun writeCustomNBT(nbt: NBTTagCompound) {
		val compound = NBTTagCompound()
		if (item != null) item!!.writeToNBT(compound)
		nbt.setTag("item", compound)
	}
	
	open fun readCustomNBT(nbt: NBTTagCompound) {
		if (nbt.hasKey("item")) item = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("item"))
	}
	
	override fun getDescriptionPacket(): Packet {
		val nbt = NBTTagCompound()
		writeCustomNBT(nbt)
		return S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, nbt)
	}
	
	override fun onDataPacket(net: NetworkManager?, packet: S35PacketUpdateTileEntity?) {
		super.onDataPacket(net, packet)
		readCustomNBT(packet!!.func_148857_g())
	}
	
	@SideOnly(Side.CLIENT)
	override fun getRenderBoundingBox(): AxisAlignedBB {
		return TileEntity.INFINITE_EXTENT_AABB
	}
	
	companion object {
		
		fun renderItem(tile: ItemContainingTileEntity) {
			val itemstack = tile.item
			if (itemstack != null) {
				glDisable(GL_CULL_FACE)
				val entityitem = EntityItem(tile.getWorldObj(), 0.0, 0.0, 0.0, itemstack)
				val item = entityitem.entityItem.item
				entityitem.entityItem.stackSize = tile.item!!.stackSize
				entityitem.hoverStart = 0.0f
				glPushMatrix()
				Tessellator.instance.setBrightness(tile.getBlockType().getMixedBrightnessForBlock(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord))
				
				if (item === Items.compass) {
					val texturemanager = Minecraft.getMinecraft().textureManager
					texturemanager.bindTexture(TextureMap.locationItemsTexture)
					val textureatlassprite1 = (texturemanager.getTexture(TextureMap.locationItemsTexture) as TextureMap).getAtlasSprite(Items.compass.getIconIndex(entityitem.entityItem).iconName)
					
					if (textureatlassprite1 is TextureCompass) {
						val d0 = textureatlassprite1.currentAngle
						val d1 = textureatlassprite1.angleDelta
						textureatlassprite1.currentAngle = 0.0
						textureatlassprite1.angleDelta = 0.0
						textureatlassprite1.updateCompass(tile.getWorldObj(), tile.xCoord.toDouble(), tile.zCoord.toDouble(), MathHelper.wrapAngleTo180_float((180 + tile.blockMetadata * 90).toFloat()).toDouble(), false, true)
						textureatlassprite1.currentAngle = d0
						textureatlassprite1.angleDelta = d1
					}
					
					val textureatlassprite = (Minecraft.getMinecraft().textureManager.getTexture(TextureMap.locationItemsTexture) as TextureMap).getAtlasSprite(Items.compass.getIconIndex(entityitem.entityItem).iconName)
					
					if (textureatlassprite.frameCount > 0) {
						textureatlassprite.updateAnimation()
					}
				}
				RenderItem.renderInFrame = true
				RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0, -0.2501, 0.0, 0.0f, 0.0f)
				RenderItem.renderInFrame = false
				
				glEnable(GL_CULL_FACE)
				glPopMatrix()
			}
		}
	}
}
