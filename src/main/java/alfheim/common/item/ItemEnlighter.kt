package alfheim.common.item

import alexsocol.asjlib.*
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.tile.TileRainbowManaFlame
import alfheim.common.item.rod.ItemRodPrismatic
import alfheim.common.security.InteractionSecurity
import net.minecraft.block.material.Material
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.fluids.BlockFluidClassic
import vazkii.botania.api.mana.*
import vazkii.botania.common.core.helper.ItemNBTHelper

class ItemEnlighter: ItemMod("Enlighter"), IManaUsingItem {
	
	init {
		maxStackSize = 1
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		if (player.isSneaking)
			ItemNBTHelper.setBoolean(stack, TAG_ACTIVE, !ItemNBTHelper.getBoolean(stack, TAG_ACTIVE, false))
		
		return super.onItemRightClick(stack, world, player)
	}
	
	override fun onUpdate(stack: ItemStack?, world: World, entity: Entity?, slot: Int, inHand: Boolean) {
		super.onUpdate(stack, world, entity, slot, inHand)
		
		if (!ItemNBTHelper.getBoolean(stack, TAG_ACTIVE, false)) return
		
		if (entity is EntityPlayer) {
			val x = entity.posX.mfloor()
			val y = entity.posY.mfloor()
			val z = entity.posZ.mfloor()
			
			if (!InteractionSecurity.canDoSomethingHere(entity, x, y, z, world)) return
			
			if (world.getLightBrightness(x, y, z) < 0.25F) {
				val block = world.getBlock(x, y, z)
				val below = world.getBlock(x, y - 1, z)
				
				if (below.isAir(world, x, y, z) || !block.isAir(world, x, y, z)) return
				if (below is BlockFluidClassic || below is BlockFluidClassic) return
				if (block.material == Material.water || below.material == Material.water) return
				if (block.material == Material.lava || below.material == Material.lava) return
				
				val toPlace = ItemStack(AlfheimBlocks.rainbowFlame)
				if (ManaItemHandler.requestManaExactForTool(stack, entity, ItemRodPrismatic.COST, false)) {
					toPlace.tryPlaceItemIntoWorld(entity, world, x, y, z, 1, 0f, 0f, 0f)
					if (toPlace.stackSize == 0) {
						world.playSoundEffect(x.D + 0.5, y.D + 0.5, z.D + 0.5, "fire.ignite", 0.3F, Math.random().F * 0.4F + 0.8F)
						ManaItemHandler.requestManaExactForTool(stack, entity, ItemRodPrismatic.COST, true)
						val tile = world.getTileEntity(x, y, z)
						if (tile is TileRainbowManaFlame)
							tile.invisible = true
					}
				}
			}
		}
	}
	
	override fun usesMana(stack: ItemStack) = ItemNBTHelper.getBoolean(stack, TAG_ACTIVE, false)
	
	companion object {
		const val TAG_ACTIVE = "active"
	}
}
