package alfheim.common.integration.travellersgear

import alexsocol.asjlib.*
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.potion.Potion
import net.minecraftforge.client.event.RenderPlayerEvent
import org.lwjgl.opengl.GL11.*
import travellersgear.api.TravellersGearAPI
import vazkii.botania.api.item.*
import vazkii.botania.api.item.IBaubleRender.RenderType

object TGHandlerBotaniaRenderer {
	
	init {
		eventForge()
	}
	
	@SubscribeEvent
	fun onPlayerRenderPost(event: RenderPlayerEvent.Specials.Post) {
		if (event.entityLiving.getActivePotionEffect(Potion.invisibility.id) != null) return
		
		val player = event.entityPlayer
		val tgInv = TravellersGearAPI.getExtendedInventory(player)
		
		for (stack in tgInv) {
			val item = stack?.item ?: continue
			
			if (item is IPhantomInkable && item.hasPhantomInk(stack)) continue
			
			if (item is ICosmeticAttachable) {
				val cosmetic = item.getCosmeticItem(stack)
				glPushMatrix()
				glColor4f(1f, 1f, 1f, 1f)
				(cosmetic?.item as? IBaubleRender)?.onPlayerBaubleRender(cosmetic, event, RenderType.BODY)
				glPopMatrix()
			}
			
			if (item is IBaubleRender) {
				glPushMatrix()
				glColor4f(1f, 1f, 1f, 1f)
				item.onPlayerBaubleRender(stack, event, RenderType.BODY)
				glPopMatrix()
			}
		}
	}
}
