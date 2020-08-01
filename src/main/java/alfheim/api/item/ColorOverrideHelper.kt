package alfheim.api.item

import alexsocol.asjlib.get
import baubles.common.lib.PlayerHandler
import net.minecraft.entity.player.EntityPlayer
import vazkii.botania.api.item.ICosmeticAttachable
import java.awt.Color

object ColorOverrideHelper {
	
	fun getColor(player: EntityPlayer?): Int {
		var r = 0
		var b = 0
		var g = 0
		var colors = 0
		if (player != null) {
			val baubles = PlayerHandler.getPlayerBaubles(player)
			for (i in 0..3) {
				val stack = baubles[i]
				if (stack != null && stack.item is IPriestColorOverride) {
					val stackColor = (stack.item as IPriestColorOverride).colorOverride(stack)
					if (stackColor != -1) {
						val color = Color(stackColor)
						r += color.red
						g += color.green
						b += color.blue
						colors++
					}
				} else if (stack != null && stack.item is ICosmeticAttachable && (stack.item as ICosmeticAttachable).getCosmeticItem(stack) != null) {
					val cosmeticStack = (stack.item as ICosmeticAttachable).getCosmeticItem(stack)
					if (cosmeticStack != null && cosmeticStack.item is IPriestColorOverride) {
						val stackColor = (cosmeticStack.item as IPriestColorOverride).colorOverride(cosmeticStack)
						if (stackColor != -1) {
							val color = Color(stackColor)
							r += color.red
							g += color.green
							b += color.blue
							colors++
						}
					}
				}
			}
		}
		return if (colors == 0) -1 else Color(r / colors, g / colors, b / colors).rgb
	}
	
	fun getColor(player: EntityPlayer, fallback: Int): Int {
		val color = getColor(player)
		return if (color != -1) color else fallback
	}
}
