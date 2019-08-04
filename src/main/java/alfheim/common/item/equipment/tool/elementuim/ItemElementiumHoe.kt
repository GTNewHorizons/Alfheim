package alfheim.common.item.equipment.tool.elementuim

import alfheim.common.item.equipment.tool.manasteel.ItemManasteelHoe
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.common.Botania

class ItemElementiumHoe: ItemManasteelHoe(BotaniaAPI.elementiumToolMaterial, "ElementiumHoe") {
	
	override fun onItemUse(stack: ItemStack?, player: EntityPlayer, world: World?, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		if (player.isSneaking)
			return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ)
		
		var did = false
		for (xOffset in -1..1) {
			for (zOffset in -1..1) {
				if (super.onItemUse(stack, player, world, x + xOffset, y, z + zOffset, side, hitX, hitY, hitZ)) {
					for (i in 0..2)
						Botania.proxy.sparkleFX(world, x + xOffset - 0.1 + Math.random() * 1.2, y - 0.1 + Math.random() * 1.2, z + zOffset - 0.1 + Math.random() * 1.2, 0.5f, 0.2f, 0f, 1f, 1)
					if (!did)
						did = true
				}
			}
		}
		
		return did
	}
	
	override fun addInformation(stack: ItemStack?, player: EntityPlayer?, info: MutableList<Any?>, extra: Boolean) {
		info.add(StatCollector.translateToLocal("item.ElementiumHoe.desc"))
	}
}
