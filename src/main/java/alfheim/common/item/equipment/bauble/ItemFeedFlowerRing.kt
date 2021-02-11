package alfheim.common.item.equipment.bauble

import alexsocol.asjlib.getTileEntity
import baubles.api.BaubleType
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import vazkii.botania.api.mana.*
import vazkii.botania.api.subtile.SubTileFunctional
import vazkii.botania.common.block.tile.TileSpecialFlower
import vazkii.botania.common.item.equipment.bauble.ItemBauble

class ItemFeedFlowerRing: ItemBauble("FeedFlower"), IManaUsingItem {
	
	override fun onWornTick(stack: ItemStack?, entity: EntityLivingBase?) {
		super.onWornTick(stack, entity)
		
		if (entity !is EntityPlayer) return
		
		val range = 7
		for (x in -range until range)
			for (y in -range until range)
				for (z in -range until range) {
					val te = entity.entityWorld.getTileEntity(entity, x, y, z)
					if (te is TileSpecialFlower) {
						if (te.subTile is SubTileFunctional) {
							val subt = te.subTile as SubTileFunctional
							val manaToUse = subt.maxMana - subt.mana
							
							if (ManaItemHandler.requestManaExact(stack, entity, manaToUse, true))
								subt.addMana(manaToUse)
						}
					}
				}
	}
	
	override fun getBaubleType(stack: ItemStack?): BaubleType? {
		return BaubleType.RING
	}
	
	override fun usesMana(stack: ItemStack?): Boolean {
		return true
	}
}
