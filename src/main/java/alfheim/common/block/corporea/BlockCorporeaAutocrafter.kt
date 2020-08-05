package alfheim.common.block.corporea

import alexsocol.asjlib.ASJUtilities
import alfheim.common.block.base.BlockContainerMod
import alfheim.common.block.tile.corporea.TileCorporeaAutocrafter
import alfheim.common.core.helper.IconHelper
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraft.world.World
import vazkii.botania.api.wand.IWandable
import kotlin.math.max

class BlockCorporeaAutocrafter: BlockContainerMod(Material.iron), IWandable {
	
	lateinit var iconSide: IIcon
	
	init {
		setBlockName("CorporeaAutocrafter")
		setHardness(5.5f)
		setStepSound(Block.soundTypeMetal)
	}
	
	override fun registerBlockIcons(reg: IIconRegister) {
		super.registerBlockIcons(reg)
		iconSide = IconHelper.forBlock(reg, this, "Side")
	}
	
	override fun getIcon(side: Int, meta: Int) = if (side < 2) blockIcon!! else iconSide
	
	override fun breakBlock(world: World, x: Int, y: Int, z: Int, block: Block?, meta: Int) {
		onUsedByWand(null, null, world, x, y, z, 0)
		world.func_147453_f(x, y, z, block)
		super.breakBlock(world, x, y, z, block, meta)
	}
	
	override fun createNewTileEntity(world: World?, meta: Int) = TileCorporeaAutocrafter()
	
	override fun onBlockClicked(world: World, x: Int, y: Int, z: Int, player: EntityPlayer) {
		(world.getTileEntity(x, y, z) as? TileCorporeaAutocrafter)?.let {
			it.craftResult = max(1, it.craftResult + if (player.isSneaking) -1 else 1)
			ASJUtilities.say(player, "alfheimmisc.craftresult", it.craftResult)
		}
	}
	
	override fun onUsedByWand(player: EntityPlayer?, stack: ItemStack?, world: World, x: Int, y: Int, z: Int, side: Int): Boolean {
		return (world.getTileEntity(x, y, z) as? TileCorporeaAutocrafter)?.onWanded() == true
	}
}
