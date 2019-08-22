package alfheim.common.block

import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.common.block.tile.TileTradePortal
import alfheim.common.lexicon.AlfheimLexiconData
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.IIcon
import net.minecraft.world.*
import vazkii.botania.api.lexicon.*

class BlockTradePortal: BlockContainer(Material.rock), ILexiconable {
	init {
		this.setBlockName("TradePortal")
		this.setBlockTextureName(ModInfo.MODID + ":TradePortal")
		this.setCreativeTab(AlfheimCore.alfheimTab)
		this.setHarvestLevel("pickaxe", 0)
		this.setHardness(10.0f)
		this.setResistance(600.0f)
		this.setStepSound(soundTypeStone)
	}
	
	override fun registerBlockIcons(reg: IIconRegister) {
		textures[0] = reg.registerIcon(ModInfo.MODID + ":TradePortal")
		textures[1] = reg.registerIcon(ModInfo.MODID + ":TradePortalActive")
		textures[2] = reg.registerIcon(ModInfo.MODID + ":TradePortalInside")
	}
	
	override fun getIcon(side: Int, meta: Int): IIcon {
		return if (meta == 0) textures[0]!! else textures[1]!!
	}
	
	override fun createNewTileEntity(world: World, meta: Int): TileEntity {
		return TileTradePortal()
	}
	
	override fun onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer?, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		return if (!world.isRemote) (world.getTileEntity(x, y, z) as TileTradePortal).onWanded() else false
	}
	
	override fun getLightValue(world: IBlockAccess, x: Int, y: Int, z: Int): Int {
		return if (world.getBlockMetadata(x, y, z) == 0) 0 else 15
	}
	
	override fun hasComparatorInputOverride(): Boolean {
		return true
	}
	
	override fun getComparatorInputOverride(world: World, x: Int, y: Int, z: Int, side: Int): Int {
		return if ((world.getTileEntity(x, y, z) as TileTradePortal).isTradeOn) 15 else 0
	}
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack): LexiconEntry {
		return AlfheimLexiconData.trade
	}
	
	companion object {
		
		val textures = arrayOfNulls<IIcon>(3)
	}
}