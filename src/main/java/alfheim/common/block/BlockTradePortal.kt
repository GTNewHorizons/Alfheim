package alfheim.common.block

import alfheim.api.ModInfo
import alfheim.client.core.helper.*
import alfheim.common.block.base.BlockContainerMod
import alfheim.common.block.tile.TileTradePortal
import alfheim.common.lexicon.AlfheimLexiconData
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraft.world.*
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.common.item.ModItems

class BlockTradePortal: BlockContainerMod(Material.rock), ILexiconable {
	
	init {
		setBlockName("TradePortal")
		setBlockTextureName(ModInfo.MODID + ":TradePortal")
		setHarvestLevel("pickaxe", 0)
		setHardness(10f)
		setResistance(600f)
		setStepSound(soundTypeStone)
	}
	
	override fun loadTextures(map: TextureMap) {
		textures = Array(2) { InterpolatedIconHelper.forBlock(map, this, it) }
	}
	
	override fun registerBlockIcons(reg: IIconRegister) {
		blockIcon = IconHelper.forBlock(reg, this)
	}
	
	override fun onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer?, side: Int, hitX: Float, hitY: Float, hitZ: Float) =
		if (!world.isRemote && player?.heldItem?.item === ModItems.twigWand) (world.getTileEntity(x, y, z) as TileTradePortal).onWanded() else false
	
	override fun isInterpolated() = true
	override fun getIcon(side: Int, meta: Int) = if (meta == 0) blockIcon else textures[0]
	override fun createNewTileEntity(world: World, meta: Int) = TileTradePortal()
	override fun getLightValue(world: IBlockAccess, x: Int, y: Int, z: Int) = if (world.getBlockMetadata(x, y, z) == 0) 0 else 15
	override fun hasComparatorInputOverride() = true
	override fun getComparatorInputOverride(world: World, x: Int, y: Int, z: Int, side: Int) = if ((world.getTileEntity(x, y, z) as TileTradePortal).isTradeOn) 15 else 0
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack) = AlfheimLexiconData.trade
	
	companion object {
		
		lateinit var textures: Array<IIcon?>
	}
}