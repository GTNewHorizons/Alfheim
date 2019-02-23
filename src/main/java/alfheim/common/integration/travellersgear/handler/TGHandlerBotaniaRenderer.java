package alfheim.common.integration.travellersgear.handler;

import static org.lwjgl.opengl.GL11.*;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.client.event.RenderPlayerEvent;
import travellersgear.api.TravellersGearAPI;
import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.api.item.IBaubleRender.RenderType;
import vazkii.botania.api.item.ICosmeticAttachable;
import vazkii.botania.api.item.IPhantomInkable;

public class TGHandlerBotaniaRenderer {

	@SubscribeEvent
	public void onPlayerRenderPost(RenderPlayerEvent.Specials.Post event) {
		if(event.entityLiving.getActivePotionEffect(Potion.invisibility) != null) return;
		
		EntityPlayer player = event.entityPlayer;
		ItemStack[] tgInv = TravellersGearAPI.getExtendedInventory(player);
		
		for(int i = 0; i < tgInv.length; i++) {
			ItemStack stack = tgInv[i];
			if(stack != null) {
				Item item = stack.getItem();
				
				if(item instanceof IPhantomInkable) {
					IPhantomInkable inkable = (IPhantomInkable) item;
					if(inkable.hasPhantomInk(stack)) continue;
				}
				
				if(item instanceof ICosmeticAttachable) {
					ICosmeticAttachable attachable = (ICosmeticAttachable) item;
					ItemStack cosmetic = attachable.getCosmeticItem(stack);
					if(cosmetic != null) {
						glPushMatrix();
						glColor4f(1F, 1F, 1F, 1F);
						((IBaubleRender) cosmetic.getItem()).onPlayerBaubleRender(cosmetic, event, RenderType.BODY);
						glPopMatrix();
						continue;
					}
				}
				
				if(item instanceof IBaubleRender) {
					glPushMatrix();
					glColor4f(1F, 1F, 1F, 1F);
					((IBaubleRender) stack.getItem()).onPlayerBaubleRender(stack, event, RenderType.BODY);
					glPopMatrix();
				}
			}
		}
	}
}
