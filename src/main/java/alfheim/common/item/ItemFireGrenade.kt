package alfheim.common.item

import alexsocol.asjlib.ASJUtilities
import alfheim.client.core.helper.InterpolatedIconHelper
import alfheim.common.entity.EntityThrowableItem
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.MinecraftForge

class ItemFireGrenade: ItemMod("fireGrenade") {
	
	init {
		if (ASJUtilities.isClient)
			MinecraftForge.EVENT_BUS.register(this)
	}
	
	override fun onItemRightClick(stack: ItemStack?, world: World?, player: EntityPlayer?): ItemStack? {
		if (stack != null && world != null && player != null) {
			if (!world.isRemote) {
				val potion = EntityThrowableItem(player)
				world.spawnEntityInWorld(potion)
				stack.stackSize--
			}
		}
		
		return stack
	}
	
	override fun registerIcons(reg: IIconRegister) = Unit // NO-OP
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun loadTextures(event: TextureStitchEvent.Pre) {
		if (event.map.textureType == 1) {
			itemIcon = InterpolatedIconHelper.forItem(event.map, this)
		}
	}
}