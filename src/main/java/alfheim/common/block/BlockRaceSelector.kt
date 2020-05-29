package alfheim.common.block

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.common.block.base.BlockContainerMod
import alfheim.common.block.container.ContainerRaceSelector
import alfheim.common.block.tile.TileRaceSelector
import alfheim.common.core.helper.IconHelper
import alfheim.common.network.MessageRaceSelection
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
		val tile = world.getTileEntity(x, y, z) as? TileRaceSelector ?: return false
		
		if (!world.isRemote) {
			player.openContainer = ContainerRaceSelector(tile)
			return false
		}
		
		val res = onBlockActivated2(world, x, y, z, player, side, hitX, hitZ, tile)
		
		if (res.first) {
			tile.activeRotation = res.second.actRot
			tile.rotation = res.second.rotation
			tile.custom = res.second.custom
			tile.female = res.second.female
			tile.timer = res.second.timer
			
			AlfheimCore.network.sendToServer(MessageRaceSelection(res.second.meta.first, res.second.custom, res.second.female, res.second.giveRace, res.second.meta.second, res.second.rotation, res.second.actRot, res.second.timer, world.provider.dimensionId))
		}
		
		return res.first
	}
	
	private fun onBlockActivated2(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, hitX: Float, hitZ: Float, tile: TileRaceSelector): Pair<Boolean, ActivationResult> {
		val res = ActivationResult()
		
		res.actRot = tile.activeRotation
		res.rotation = tile.rotation
		res.custom = tile.custom
		res.female = tile.female
		res.timer = tile.timer
		
		if (side != 1) return false to res
		
		val meta = world.getBlockMetadata(x, y, z)
		
		if (tile.activeRotation != 0) return false to res
		
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
				res.custom = !tile.custom
				ASJUtilities.say(player, "alfheimmisc.skintoggle${ if(res.custom) "1" else "0" }")
				return true to res
			}
			
			res.female = if (isLeft(hitX, hitZ)) false else if (isRight(hitX, hitZ)) true else return false to res
			
			res.meta = true to 1
			res.timer = 600
			
			return true to res
		} else if (meta == 1) {
			if (isMid(hitX, hitZ)) {
				res.giveRace = true
				return true to res
			}
			
			if (isLeft(hitX, hitZ)) {
				res.rotation = --tile.rotation
				res.actRot = 20
				res.timer = 600
				return true to res
			}
			
			if (isRight(hitX, hitZ)) {
				res.rotation = ++tile.rotation
				res.actRot = -20
				res.timer = 600
				return true to res
			}
			
			return false to res
		}
		
		return false to res
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

private data class ActivationResult(var meta: Pair<Boolean, Int>, var custom: Boolean, var female: Boolean, var rotation: Int, var actRot: Int, var timer: Int, var giveRace: Boolean) {
	constructor(): this(false to 0, false, false, 0, 0, 0, false)
}