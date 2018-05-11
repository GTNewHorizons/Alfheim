package alfheim.common.potion;

import alfheim.api.ModInfo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.brew.potion.PotionMod;

public class PotionAlfheim extends PotionMod {
	
	private static final ResourceLocation texture = new ResourceLocation(ModInfo.MODID, "textures/gui/Potions.png");

	public PotionAlfheim(int id, String name, boolean badEffect, int color, int iconIndex) {
		super(id, name, badEffect, color, iconIndex);
		setPotionName("alfheim.potion." + name);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getStatusIconIndex() {
		int id = super.getStatusIconIndex();
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		return id;
	}
}