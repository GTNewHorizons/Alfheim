package alfheim.common.item.equipment.bauble;

import static org.lwjgl.opengl.GL11.*;

import java.util.List;

import alfheim.AlfheimCore;
import baubles.api.BaubleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.RenderPlayerEvent;
import vazkii.botania.api.item.ICosmeticBauble;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;

public class ItemThinkingHand extends ItemBauble implements ICosmeticBauble {

	public ItemThinkingHand() {
		super("ThinkingHand");
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.AMULET;
	}

	@Override
	public void onPlayerBaubleRender(ItemStack stack, RenderPlayerEvent event, RenderType type) {
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
		
		if (type == RenderType.HEAD) {
			glPushMatrix();
			glTranslated(0, (event.entityPlayer != Minecraft.getMinecraft().thePlayer ? 1.68 : 0) - event.entityPlayer.getDefaultEyeHeight() + (event.entityPlayer.isSneaking() ? 0.0625 : 0), 0);
			glRotated(90, 0, 1, 0);
			glRotated(180, 1, 0, 0);
			glTranslated(-0.4, 0.1, -0.25);
			glScaled(0.495, 0.495, 0.495);
			glTranslated(0.4, -0.7, -0.27);
			glRotated(15, 0, 0, -1);
			ItemRenderer.renderItemIn2D(Tessellator.instance, itemIcon.getMaxU(), itemIcon.getMinV(), itemIcon.getMinU(), itemIcon.getMaxV(), itemIcon.getIconWidth(), itemIcon.getIconHeight(), 1F / 16F);
			glPopMatrix();
		}
	}

	public void addHiddenTooltip(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		par3List.add(StatCollector.translateToLocal("botaniamisc.cosmeticBauble").replaceAll("&", "\u00a7"));
		super.addHiddenTooltip(par1ItemStack, par2EntityPlayer, par3List, par4);
	}
}