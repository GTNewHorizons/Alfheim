package alexsocol.asjlib.extendables.block

import alexsocol.asjlib.*
import cpw.mods.fml.relauncher.*
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.*
import net.minecraft.client.renderer.texture.*
import net.minecraft.entity.item.EntityItem
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.*
import org.lwjgl.opengl.GL11.*

open class TileItemContainer: ASJTile() {
	
	open var item: ItemStack? = null
		set(stack) {
			field = stack
			if (ASJUtilities.isServer && worldObj != null) {
				ASJUtilities.dispatchTEToNearbyPlayers(this)
			}
		}
	
	override fun writeCustomNBT(nbt: NBTTagCompound) {
		if (item == null) return
		
		val compound = NBTTagCompound()
		item!!.writeToNBT(compound)
		nbt.setTag("item", compound)
	}
	
	override fun readCustomNBT(nbt: NBTTagCompound) {
		if (nbt.hasKey("item")) item = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("item"))
	}
	
	@SideOnly(Side.CLIENT)
	override fun getRenderBoundingBox(): AxisAlignedBB {
		return INFINITE_EXTENT_AABB
	}
	
	companion object {
		
		fun renderItem(tile: TileItemContainer) {
			val itemstack = tile.item
			if (itemstack != null) {
				glDisable(GL_CULL_FACE)
				val entityitem = EntityItem(tile.worldObj, 0.0, 0.0, 0.0, itemstack)
				val item = entityitem.entityItem.item
				entityitem.entityItem.stackSize = tile.item!!.stackSize
				entityitem.hoverStart = 0f
				glPushMatrix()
				Tessellator.instance.setBrightness(tile.getBlockType().getMixedBrightnessForBlock(tile.worldObj, tile.xCoord, tile.yCoord, tile.zCoord))
				
				if (item === Items.compass) {
					val texturemanager = Minecraft.getMinecraft().textureManager
					texturemanager.bindTexture(TextureMap.locationItemsTexture)
					val textureatlassprite1 = (texturemanager.getTexture(TextureMap.locationItemsTexture) as TextureMap).getAtlasSprite(Items.compass.getIconIndex(entityitem.entityItem).iconName)
					
					if (textureatlassprite1 is TextureCompass) {
						val d0 = textureatlassprite1.currentAngle
						val d1 = textureatlassprite1.angleDelta
						textureatlassprite1.currentAngle = 0.0
						textureatlassprite1.angleDelta = 0.0
						textureatlassprite1.updateCompass(tile.worldObj, tile.xCoord.D, tile.zCoord.D, MathHelper.wrapAngleTo180_double(180 + tile.blockMetadata * 90.0), false, true)
						textureatlassprite1.currentAngle = d0
						textureatlassprite1.angleDelta = d1
					}
					
					val textureatlassprite = (Minecraft.getMinecraft().textureManager.getTexture(TextureMap.locationItemsTexture) as TextureMap).getAtlasSprite(Items.compass.getIconIndex(entityitem.entityItem).iconName)
					
					if (textureatlassprite.frameCount > 0) {
						textureatlassprite.updateAnimation()
					}
				}
				RenderItem.renderInFrame = true
				RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0, -0.2501, 0.0, 0f, 0f)
				RenderItem.renderInFrame = false
				
				glEnable(GL_CULL_FACE)
				glPopMatrix()
			}
		}
	}
}
