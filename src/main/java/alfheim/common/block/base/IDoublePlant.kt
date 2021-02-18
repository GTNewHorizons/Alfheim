package alfheim.common.block.base

import net.minecraft.util.IIcon

/**
 * @author WireSegal
 * Created at 9:54 PM on 2/14/16.
 */
interface IDoublePlant {
	
	fun getTopIcon(lowerMeta: Int): IIcon
	fun getBottomIcon(lowerMeta: Int): IIcon
}
