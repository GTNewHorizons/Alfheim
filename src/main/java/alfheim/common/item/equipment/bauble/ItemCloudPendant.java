package alfheim.common.item.equipment.bauble;

import java.util.UUID;

import org.lwjgl.opengl.GL11;

import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.common.network.Message0d;
import alfheim.common.network.Message0d.m0d;
import baubles.api.BaubleType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.RenderPlayerEvent;
import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.lib.LibItemNames;

public class ItemCloudPendant extends CloudPendantShim implements IBaubleRender {

	private static int timesJumped;
	private static boolean jumpDown;
	
	private static IIcon cirrusGem;
	
	public ItemCloudPendant(String name) {
		super(name);
		setCreativeTab(AlfheimCore.alfheimTab);
	}
	
	public ItemCloudPendant() {
		this("CloudPendant");
	}

	@Override
	public void registerIcons(IIconRegister reg) {
		super.registerIcons(reg);
		cirrusGem = reg.registerIcon(ModInfo.MODID + ":CirrusGem");
	}
	
	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.AMULET;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void clientWornTick(ItemStack stack, EntityLivingBase player) {
		if(player instanceof EntityPlayerSP && player == Minecraft.getMinecraft().thePlayer) {
			EntityPlayerSP playerSp = (EntityPlayerSP) player;
			UUID uuid = playerSp.getUniqueID();

			if(playerSp.onGround)
				timesJumped = 0;
			else {
				if(playerSp.movementInput.jump) {
					if(!jumpDown && timesJumped < getMaxAllowedJumps()) {
						playerSp.jump();
						AlfheimCore.network.sendToServer(new Message0d(m0d.JUMP));
						timesJumped++;
					}
					jumpDown = true;
				} else jumpDown = false;
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, RenderPlayerEvent event, RenderType type) {
		if(type == RenderType.BODY) {
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
			Helper.rotateIfSneaking(event.entityPlayer);
			boolean armor = event.entityPlayer.getEquipmentInSlot(3) != null;
			GL11.glRotated(180, 1, 0, 0);
			GL11.glTranslated(-0.2, -0.3, armor ? 0.2 : 0.15);
			GL11.glScaled(0.5, 0.5, 0.5);

			ItemRenderer.renderItemIn2D(Tessellator.instance, cirrusGem.getMaxU(), cirrusGem.getMinV(), cirrusGem.getMinU(), cirrusGem.getMaxV(), cirrusGem.getIconWidth(), cirrusGem.getIconHeight(), 1F / 32F);
		}
	}
}