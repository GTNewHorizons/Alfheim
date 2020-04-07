package alfheim.common.integration.travellersgear

import alexsocol.asjlib.getActivePotionEffect
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.potion.Potion
import net.minecraftforge.client.event.RenderPlayerEvent
import org.lwjgl.opengl.GL11.*
import travellersgear.api.TravellersGearAPI
import vazkii.botania.api.item.*
import vazkii.botania.api.item.IBaubleRender.RenderType

object TGHandlerBotaniaRenderer {
	
	@SubscribeEvent
	fun onPlayerRenderPost(event: RenderPlayerEvent.Specials.Post) {
		if (event.entityLiving.getActivePotionEffect(Potion.invisibility.id) != null) return
		
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
