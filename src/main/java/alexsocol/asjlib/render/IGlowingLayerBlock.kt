package alexsocol.asjlib.render

import net.minecraft.util.IIcon

interface IGlowingLayerBlock {
	
	fun getGlowIcon(side: Int, meta: Int): IIcon?
}