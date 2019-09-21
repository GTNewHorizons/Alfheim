package alfheim.common.block

import alexsocol.asjlib.ASJUtilities
import alfheim.common.block.base.BlockContainerMod
import alfheim.common.block.tile.TileRaceSelector
import alfheim.common.core.helper.IconHelper
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.IIcon
import net.minecraft.world.World

class BlockRaceSelector: BlockContainerMod(Material.glass) {
	
	lateinit var icons: Array<IIcon>
	
	init {
		setBlockBounds(0f, 0f, 3f / 16, 1f, 3f / 16, 13f / 16)
		setBlockName("RaceSelector")
		setBlockUnbreakable()
		setCreativeTab(null)
	}
	
	override fun onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		val ret = onBlockActivated2(world, x, y, z, player, side, hitX, hitZ)
		if (ret) ASJUtilities.dispatchTEToNearbyPlayers(world, x, y, z)
		return ret
	}
	
	fun onBlockActivated2(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, hitX: Float, hitZ: Float): Boolean {
		if (side != 1) return false
		
		val meta = world.getBlockMetadata(x, y, z)
		val tile = (world.getTileEntity(x, y, z) ?: return false) as? TileRaceSelector ?: return false
		
		if (tile.activeRotation != 0) return false
		
		fun within(hZ: Float) = hZ in 0.25f..0.75f
		fun isLeft(hX: Float, hZ: Float) = hX in 0.0625f..0.5f && within(hZ)
		fun isRight(hX: Float, hZ: Float) = hX in 0.5f..0.9375f && within(hZ)
		fun isMid(hX: Float, hZ: Float): Boolean {
			val leftest = hX in (5f / 16)..(6f / 16)
			val rightest = hX in (10f / 16)..(11f / 16)
			val height = hZ in (6f / 16)..(10f / 16)
			
			// left/right button side
			if ((leftest || rightest) && height) return true
			
			// button middle
			return hX in (6f / 16)..(10f / 16) && hZ in (5f / 16)..(11f / 16)
		}
		
		if (meta == 0) {
			if (isMid(hitX, hitZ)) {
				tile.custom = !tile.custom
				ASJUtilities.say(player, "alfheimmisc.skintoggle${ if(tile.custom) "1" else "0" }")
				return true
			}
			
			tile.female = if (isLeft(hitX, hitZ)) false else if (isRight(hitX, hitZ)) true else return false
			
			world.setBlockMetadataWithNotify(x, y, z, 1, 3)
			
			return true
		} else if (meta == 1) {
			if (isMid(hitX, hitZ)) {
				return tile.giveRaceAndReset(player)
			}
			
			if (isLeft(hitX, hitZ)) {
				--tile.rotation
				tile.activeRotation = 20
				return true
			}
			
			if (isRight(hitX, hitZ)) {
				++tile.rotation
				tile.activeRotation = -20
				return true
			}
			
			return false
		}
		
		return false
	}
	
	override fun registerBlockIcons(reg: IIconRegister) {
		icons = Array(5) { IconHelper.forBlock(reg, this, it) }
	}
	
	override fun getIcon(side: Int, meta: Int): IIcon {
		return when (side) {
			1    -> icons[if (meta == 1) 2 else 1]
			2, 3 -> icons[3]
			4, 5 -> icons[4]
			else -> icons[0]
		}
	}
	
	override fun createNewTileEntity(world: World?, meta: Int) = TileRaceSelector()
	override fun getRenderBlockPass() = 1
	override fun isOpaqueCube() = false
	override fun renderAsNormalBlock() = false
}
