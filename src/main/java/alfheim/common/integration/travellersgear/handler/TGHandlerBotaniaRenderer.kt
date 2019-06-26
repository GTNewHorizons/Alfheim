package alfheim.common.integration.travellersgear.handler

import org.lwjgl.opengl.GL11.*

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.potion.Potion
import net.minecraftforge.client.event.RenderPlayerEvent
import travellersgear.api.TravellersGearAPI
import vazkii.botania.api.item.IBaubleRender
import vazkii.botania.api.item.IBaubleRender.RenderType
import vazkii.botania.api.item.ICosmeticAttachable
import vazkii.botania.api.item.IPhantomInkable

class TGHandlerBotaniaRenderer {
	
	@SubscribeEvent
	fun onPlayerRenderPost(event: RenderPlayerEvent.Specials.Post) {
		if (event.entityLiving.getActivePotionEffect(Potion.invisibility) != null) return
		
		val player = event.entityPlayer
		val tgInv = TravellersGearAPI.getExtendedInventory(player)
		
		for (stack in tgInv) {
			if (stack != null) {
				val item = stack.item
				
				if (item is IPhantomInkable) {
					val inkable = item as IPhantomInkable
					if (inkable.hasPhantomInk(stack)) continue
				}
				
				if (item is ICosmeticAttachable) {
					val attachable = item as ICosmeticAttachable
					val cosmetic = attachable.getCosmeticItem(stack)
					if (cosmetic != null) {
						glPushMatrix()
						glColor4f(1f, 1f, 1f, 1f)
						(cosmetic.item as IBaubleRender).onPlayerBaubleRender(cosmetic, event, RenderType.BODY)
						glPopMatrix()
						continue
					}
				}
				
				if (item is IBaubleRender) {
					glPushMatrix()
					glColor4f(1f, 1f, 1f, 1f)
					(stack.item as IBaubleRender).onPlayerBaubleRender(stack, event, RenderType.BODY)
					glPopMatrix()
				}
			}
		}
	}
}
