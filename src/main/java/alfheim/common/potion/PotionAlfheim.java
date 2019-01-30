package alfheim.common.potion;

import static org.lwjgl.opengl.GL11.*;

import alfheim.api.lib.LibResourceLocations;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import vazkii.botania.common.brew.potion.PotionMod;

public class PotionAlfheim extends PotionMod {
	
	private static int iconID = 0;
	
	public PotionAlfheim(int id, String name, boolean badEffect, int color) {
		super(id, name, badEffect, color, iconID++);
		setPotionName("alfheim.potion." + name);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getStatusIconIndex() {
		glEnable(GL_BLEND);
		int id = super.getStatusIconIndex();
		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.potions);
		return id;
	}
}