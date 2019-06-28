package alfheim.common.item.equipment.tool.elementuim

import alfheim.common.item.equipment.tool.manasteel.ItemManasteelHoe
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import vazkii.botania.api.BotaniaAPI

class ItemElementiumHoe: ItemManasteelHoe(BotaniaAPI.elementiumToolMaterial, "ElementiumHoe") {
	
	override fun onItemUse(stack: ItemStack?, player: EntityPlayer, world: World?, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		val _do = super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ)
		if (!player.isSneaking && _do) {
			for (i in -1..1) {
				for (k in -1..1) {
					if (i == 0 && k == 0) continue
					if (!world!!.isAirBlock(x + i, y, z + k)) super.onItemUse(stack, player, world, x + i, y, z + k, side, hitX, hitY, hitZ)
				}
			}
		}
		return _do
	}
	
	override fun addInformation(stack: ItemStack?, player: EntityPlayer?, info: MutableList<Any?>, extra: Boolean) {
		info.add(StatCollector.translateToLocal("item.ElementiumHoe.desc"))
	}
}
