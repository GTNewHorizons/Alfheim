package alfheim.common.item.equipment.bauble;

import org.lwjgl.opengl.GL11;

import alfheim.AlfheimCore;
import baubles.api.BaubleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.RenderPlayerEvent;
import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;

public class ItemPendant extends ItemBauble implements IBaubleRender {

	IIcon icon;
	String name;
	
	public ItemPendant(String name) {
		super(name);
		this.name = name;
		this.setCreativeTab(AlfheimCore.alfheimTab);
	}
	
	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		super.registerIcons(par1IconRegister);
		icon = IconHelper.forItem(par1IconRegister, this, "Gem");
	}

	@Override
	public BaubleType getBaubleType(ItemStack stack) {
		return BaubleType.AMULET;
	}

	@Override
	public void onPlayerBaubleRender(ItemStack stack, RenderPlayerEvent event, RenderType type) {
		if (type == RenderType.BODY) {
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
			Helper.rotateIfSneaking(event.entityPlayer);
			boolean armor = event.entityPlayer.getCurrentArmor(2) != null;
			GL11.glRotatef(180F, 1F, 0F, 0F);
			GL11.glTranslatef(-0.25F, -0.4F, armor ? 0.2F : 0.15F);
			GL11.glScalef(0.5F, 0.5F, 0.5F);
			ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 1F / 32F);
		}
	}
}