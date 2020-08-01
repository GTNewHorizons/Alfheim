package alfheim.common.core.asm.hook.extender

import alexsocol.asjlib.*
import gloomyfolken.hooklib.asm.*
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.world.World
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.common.block.subtile.SubTilePureDaisy

object PureDaisyExtender {
	
	@JvmStatic
	@Hook(createMethod = true, returnCondition = ReturnCondition.ALWAYS)
	fun onWanded(daisy: SubTilePureDaisy, player: EntityPlayer, wand: ItemStack): Boolean {
		val world = daisy.supertile.worldObj
		val x = daisy.supertile.xCoord
		val y = daisy.supertile.yCoord
		val z = daisy.supertile.zCoord
		
		val next = player.inventory.currentItem + 1
		if (next == 9) return false // not a hotbar
		
		val stack = player.inventory[next] ?: return false
		val itemBlock = stack.item as? ItemBlock ?: return false
		val block = itemBlock.field_150939_a
		
		for (i in -1..1)
			for (k in -1..1) {
				val blockAt = world.getBlock(x + i, y, z + k)
				if (!blockAt.isReplaceable(world, x + i, y, z + k) || !blockAt.isAir(world, x + i, y, z + k) || !block.canPlaceBlockAt(world, x + i, y, z + k)) continue
				if (!matches(world, x + i, y, z + k, daisy, block, stack.meta)) continue
				itemBlock.placeBlockAt(stack, player, world, x + i, y, z + k, 1, 0f, 0f, 0f, stack.meta)
				--stack.stackSize
				
				return true
			}
		
		return false
	}
	
	fun matches(world: World, x: Int, y: Int, z: Int, daisy: SubTilePureDaisy, block: Block, meta: Int): Boolean {
		for (recipe in BotaniaAPI.pureDaisyRecipes) {
			if (recipe.matches(world, x, y, z, daisy, block, meta)) {
				return true
			}
		}
		
		return false
	}
}