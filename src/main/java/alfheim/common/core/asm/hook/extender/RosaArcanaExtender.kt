package alfheim.common.core.asm.hook.extender

import alexsocol.asjlib.*
import alfheim.common.block.AlfheimBlocks
import cpw.mods.fml.relauncher.*
import gloomyfolken.hooklib.asm.*
import net.minecraft.block.Block
import net.minecraft.entity.item.EntityXPOrb
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import vazkii.botania.api.subtile.RadiusDescriptor
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.block.decor.IFloatingFlower
import vazkii.botania.common.block.subtile.generating.SubTileArcaneRose

object RosaArcanaExtender {
	
	@JvmStatic
	@Hook
	fun onUpdate(tile: SubTileArcaneRose) {
		if (tile.mana >= tile.maxMana || getSuperRadius(tile.supertile) == 0 || tile.ticksExisted % 5 != 0) return
		
		val list = tile.supertile.worldObj.getEntitiesWithinAABB(EntityXPOrb::class.java, getNewActiveBB(tile)) as MutableList<EntityXPOrb>
		for (xp in list) {
			val new = tile.mana + (xp.xpValue * 50)
			if (new > tile.maxMana) continue
			
			tile.mana = new
			xp.xpValue = 0
			xp.worldObj.playSoundAtEntity(xp, "random.orb", 0.1f, 0.5f * ((xp.worldObj.rand.nextFloat() - xp.worldObj.rand.nextFloat()) * 0.7f + 1.8f))
			xp.setDead()
			
			break
		}
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ON_NOT_NULL)
	@SideOnly(Side.CLIENT)
	fun getRadius(tile: SubTileArcaneRose): RadiusDescriptor? {
		return if (getSuperRadius(tile.supertile) > 0 && mc.thePlayer.isSneaking) RadiusDescriptor.Rectangle(tile.toChunkCoordinates(), getNewActiveBB(tile)) else null
	}
	
	fun getNewActiveBB(tile: SubTileArcaneRose) = getSuperRadius(tile.supertile).let { tile.supertile.boundingBox().expand(it.D, 0.0, it.D)!! }
	
	fun getSuperRadius(supertile: TileEntity): Int {
		val base = BlockData(supertile.worldObj, supertile.xCoord, supertile.yCoord - if (supertile is IFloatingFlower) 1 else 2, supertile.zCoord)
		
		return when {
			base.equals(AlfheimBlocks.alfStorage, 1) -> 3
			base.equals(AlfheimBlocks.alfStorage, 0) -> 2
			base.equals(ModBlocks.storage, 4)        -> 1
			else -> 0
		}
	}
	
	class BlockData(var block: Block, var meta: Int) {
		
		constructor(world: World, x: Int, y: Int, z: Int): this(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z))
		
		fun equals(block: Block, meta: Int): Boolean {
			return this.block === block && this.meta == meta
		}
	}
}