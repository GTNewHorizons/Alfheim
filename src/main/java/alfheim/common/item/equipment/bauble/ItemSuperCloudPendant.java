package alfheim.common.item.equipment.bauble;

import org.lwjgl.opengl.GL11;

import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.RenderPlayerEvent;
import vazkii.botania.api.item.IBaubleRender.Helper;
import vazkii.botania.api.item.IBaubleRender.RenderType;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.lib.LibItemNames;

public class ItemSuperCloudPendant extends ItemCloudPendant {

	private static IIcon nimbusGem;
	
	public ItemSuperCloudPendant() {
		super("SuperCloudPendant");
		setCreativeTab(AlfheimCore.alfheimTab);
	}

	@Override
	public void registerIcons(IIconRegister reg) {
		super.registerIcons(reg);
		nimbusGem = reg.registerIcon(ModInfo.MODID + ":NimbusGem");
	}
	
	@Override
	public int getMaxAllowedJumps() {
		return 3;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, RenderPlayerEvent event, RenderType type) {
		if(type == RenderType.BODY) {
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
			Helper.rotateIfSneaking(event.entityPlayer);
			boolean armor = event.entityPlayer.getEquipmentInSlot(3) != null;
			GL11.glRotated(180, 1, 0, 0);
			GL11.glTranslated(-0.2, -0.45, armor ? 0.2 : 0.15);
			GL11.glScaled(0.5, 0.5, 0.5);

			ItemRenderer.renderItemIn2D(Tessellator.instance, nimbusGem.getMaxU(), nimbusGem.getMinV(), nimbusGem.getMinU(), nimbusGem.getMaxV(), nimbusGem.getIconWidth(), nimbusGem.getIconHeight(), 1F / 32F);
		}
	}
}