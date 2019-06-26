package alexsocol.asjlib.render;

import net.minecraft.util.IIcon;

public interface IGlowingLayerBlock {
	
	IIcon getGlowIcon(int side, int meta);
}