package alfheim.common.block

import alexsocol.asjlib.*
import alexsocol.asjlib.extendables.block.BlockModFence
import alfheim.api.lib.LibRenderIDs
import alfheim.common.item.AlfheimItems
import alfheim.common.item.material.ElvenFoodMetas
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraft.world.*
import net.minecraftforge.common.IPlantable
import net.minecraftforge.common.util.ForgeDirection
import java.util.*
import kotlin.math.*

class BlockGrapeRedPlanted: BlockModFence("planks_oak", Material.wood, null) {
	
	init {
		setUnlocalizedName("GrapeRegPlanted")
		setHardness(2.0F)
		setLightOpacity(0)
		setResistance(5.0F)
		setStepSound(soundTypeGrass)
		tickRandomly = true
	}
	
	override fun getItemDropped(meta: Int, random: Random?, fortune: Int) = Blocks.fence.toItem()
	
	override fun setBlockBoundsBasedOnState(world: IBlockAccess, x: Int, y: Int, z: Int) {
		super.setBlockBoundsBasedOnState(world, x, y, z)
		
		val s = when (world.getBlockMetadata(x, y, z)) {
					0    -> 5f
					1    -> 3f
					else -> 1f
				} / 16
		
		var h = 1 - s + 1f / 16
		if (world.getBlock(x, y + 1, z) === this) h = 1f
		
		val minX = if (world.getBlock(x - 1, y, z) === this) 0f else s
		val minZ = if (world.getBlock(x, y, z - 1) === this) 0f else s
		val maxX = if (world.getBlock(x + 1, y, z) === this) 0f else s
		val maxZ = if (world.getBlock(x, y, z + 1) === this) 0f else s
		
		setBlockBounds(min(minX, blockBoundsMinX.F), 0f, min(minZ, blockBoundsMinZ.F), max(1 - maxX, blockBoundsMaxX.F), max(h, blockBoundsMaxY.F), max(1 - maxZ, blockBoundsMaxZ.F))
	}
	
	override fun registerIcons(reg: IIconRegister) {
		blockIcon = AlfheimBlocks.grapesRed[0].getIcon(0, 0)
	}
	
	override fun getIcon(world: IBlockAccess, x: Int, y: Int, z: Int, side: Int): IIcon {
		return AlfheimBlocks.grapesRed.safeGet(world.getBlockMetadata(x, y, z) - 2).getIcon(world, x, y, z, side)
	}
	
	override fun updateTick(world: World, x: Int, y: Int, z: Int, random: Random) {
		if (!canBlockStay(world, x, y, z)) {
			world.setBlock(x, y, z, Blocks.fence, 0, 3)
			return
		}
		
		val meta = world.getBlockMetadata(x, y, z)
		if (meta < 4 && random.nextInt(if (meta < 2) 50 else 10) == 0) {
			if (world.getBlock(x, y - 1, z) === this && world.getBlockMetadata(x, y - 1, z) <= meta)
				return
			
			world.setBlockMetadataWithNotify(x, y, z, meta + 1, 3)
			return
		}
		
		if (meta > 0 && random.nextInt(100) == 0) {
			for (d in ForgeDirection.VALID_DIRECTIONS.shuffled()) {
				if (world.getBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ) === Blocks.fence) {
					world.setBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ, this, 0, 3)
					return
				}
			}
		}
	}
	
	override fun onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, hx: Float, hy: Float, hz: Float): Boolean {
		if (world.getBlockMetadata(x, y, z) < 4) return false
		if (player.heldItem != null) return false
		
		player.inventory.addItemStackToInventory(ItemStack(AlfheimItems.elvenFood, world.rand.nextInt(2) + 1, ElvenFoodMetas.RedGrapes))
		world.setBlockMetadataWithNotify(x, y, z, 2, 3)
		
		world.markBlockRangeForRenderUpdate(x, y, z, x, y, z)
		
		return true
	}
	
	override fun canBlockStay(world: World, x: Int, y: Int, z: Int): Boolean {
		for (d in ForgeDirection.VALID_DIRECTIONS) {
			if (d == ForgeDirection.UP) continue
			
			val blockAt = world.getBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ)
			
			if (blockAt === this)
				return true
			else if (d == ForgeDirection.DOWN && blockAt.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, Blocks.sapling as IPlantable))
				return true
		}
		
		return false
	}
	
	override fun getRenderType() = LibRenderIDs.idGrapeRedPlanted
	override fun isOpaqueCube() = false
	override fun renderAsNormalBlock() = false
	
	override fun isLadder(world: IBlockAccess?, x: Int, y: Int, z: Int, entity: EntityLivingBase?) = true
}