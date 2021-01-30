package alfheim.common.item.lens

import alexsocol.asjlib.*
import alexsocol.asjlib.security.InteractionSecurity
import net.minecraft.entity.projectile.EntityThrowable
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.FurnaceRecipes
import net.minecraft.util.*
import net.minecraft.world.World
import vazkii.botania.api.internal.IManaBurst
import vazkii.botania.api.mana.IManaBlock
import vazkii.botania.common.core.handler.ConfigHandler
import vazkii.botania.common.item.lens.Lens

class LensSmelt: Lens() {
	
	override fun collideBurst(burst: IManaBurst, entity: EntityThrowable, pos: MovingObjectPosition?, isManaBlock: Boolean, dead: Boolean, stack: ItemStack?): Boolean {
		var dead = dead
		val world: World = entity.worldObj
		
		if (world.isRemote || pos == null || pos.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) return false
		
		val x = pos.blockX
		val y = pos.blockY
		val z = pos.blockZ
		
		if (!InteractionSecurity.canDoSomethingHere(entity.thrower, x, y, z)) return false
		
		val block = world.getBlock(x, y, z)
		val meta = world.getBlockMetadata(x, y, z)
		val harvestLevel: Int = ConfigHandler.harvestLevelBore
		val tile = world.getTileEntity(x, y, z)
		val hardness = block.getBlockHardness(world, x, y, z)
		val neededHarvestLevel: Int = block.getHarvestLevel(meta)
		val mana = burst.mana
		val source = burst.burstSourceChunkCoordinates
		
		if (source != ChunkCoordinates(x, y, z) && tile !is IManaBlock && neededHarvestLevel <= harvestLevel && hardness != -1f && hardness < 50f && (burst.isFake || mana >= 24)) {
			if (!burst.hasAlreadyCollidedAt(x, y, z)) {
				if (!burst.isFake) {
					val itemstack = FurnaceRecipes.smelting().getSmeltingResult(ItemStack(block, 1, meta))?.copy()
					
					if (itemstack != null) {
						if (!entity.worldObj.isRemote) {
							val xp = FurnaceRecipes.smelting().func_151398_b(itemstack)
							if (xp >= 1f)
								entity.worldObj.getBlock(x, y, z).dropXpOnBlockBreak(entity.worldObj, x, y, z, xp.I)
							
							entity.worldObj.func_147480_a(x, y, z, false)
							entity.entityDropItem(itemstack, 0f)
						} else {
							if (ConfigHandler.blockBreakParticles) {
								world.playAuxSFX(2001, x, y, z, block.id + (meta shl 12))
								for (i in 0..2) entity.worldObj.spawnParticle("flame", x + Math.random() - 0.5f, y + Math.random() - 0.5f, z + Math.random() - 0.5f, 0.0, 0.0, 0.0)
							}
						}
						burst.mana = mana - 40
					}
				}
			}
			dead = false
		}
		return dead
	}
}
