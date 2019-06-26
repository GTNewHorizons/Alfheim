package alfheim.common.block

import alexsocol.asjlib.extendables.ItemContainingTileEntity
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.api.lib.LibRenderIDs
import alfheim.common.block.tile.TileItemHolder
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.block.Block
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.MathHelper
import net.minecraft.world.EnumSkyBlock
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry
import vazkii.botania.api.mana.IManaItem
import vazkii.botania.client.lib.LibResources
import vazkii.botania.common.block.mana.BlockPool

class BlockItemHolder: BlockContainer(Material.iron), ILexiconable {
	init {
		setBlockBounds(0f, -0.5f, 0f, 1f, -0.125f, 1f)
		setBlockName("ItemHolder")
		setBlockTextureName(ModInfo.MODID + ":ItemHolder")
		setCreativeTab(AlfheimCore.alfheimTab)
		setBlockTextureName(LibResources.PREFIX_MOD + "livingrock0")
	}
	
	override fun renderAsNormalBlock(): Boolean {
		return false
	}
	
	override fun isOpaqueCube(): Boolean {
		return false
	}
	
	override fun getRenderType(): Int {
		return LibRenderIDs.idItemHolder
	}
	
	override fun canPlaceBlockAt(world: World, x: Int, y: Int, z: Int): Boolean {
		return world.getBlock(x, y - 1, z) is BlockPool
	}
	
	override fun onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		val te = world.getTileEntity(x, y, z) as ItemContainingTileEntity
		val stack = player.inventory.getCurrentItem()
		if (player.isSneaking) return false
		if (te != null) {
			if (te.item != null) {
				if (!world.isRemote) {
					val entityitem = EntityItem(world, x + 0.5, y + 0.5, z + 0.5, te.item!!)
					world.spawnEntityInWorld(entityitem)
				}
				te.item = null
			}
			if (stack != null && stack.stackSize == 1 && stack.item.isDamageable) {
				te.item = stack.copy()
				te.item!!.stackSize = stack.stackSize
				stack.stackSize = 0
			}
			
			world.setTileEntity(x, y, z, te)
			world.updateLightByType(EnumSkyBlock.Sky, x, y, z)
			world.markTileEntityChunkModified(x, y, z, te)
		}
		return true
	}
	
	@SideOnly(Side.CLIENT)
	override fun getMixedBrightnessForBlock(world: IBlockAccess, x: Int, y: Int, z: Int): Int {
		return world.getLightBrightnessForSkyBlocks(x, y, z, world.getBlock(x, y, z).getLightValue(world, x, y - 1, z))
	}
	
	override fun breakBlock(world: World, x: Int, y: Int, z: Int, block: Block?, meta: Int) {
		val te = world.getTileEntity(x, y, z) as ItemContainingTileEntity
		if (te != null && te.item != null) {
			world.spawnEntityInWorld(EntityItem(world, x + 0.5, y + 0.5, z + 0.5, te.item!!))
			te.item = null
		}
		
		super.breakBlock(world, x, y, z, block, meta)
	}
	
	override fun hasComparatorInputOverride(): Boolean {
		return true
	}
	
	override fun getComparatorInputOverride(world: World, x: Int, y: Int, z: Int, side: Int): Int {
		val te = world.getTileEntity(x, y, z) as ItemContainingTileEntity
		
		if (te != null && te.item != null && te.item!!.item is IManaItem) {
			val stack = te.item
			val mana = te.item!!.item as IManaItem
			
			if (mana.getMana(stack) == mana.getMaxMana(stack)) return 15
			return if (mana.getMana(stack) == 0) 0 else MathHelper.floor_double(Math.min(Math.max(0.0, mana.getMana(stack) * 15.0 / mana.getMaxMana(stack)), 15.0))
			
		}
		
		return 0
	}
	
	override fun createNewTileEntity(world: World, meta: Int): TileEntity {
		return TileItemHolder()
	}
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack): LexiconEntry {
		return AlfheimLexiconData.itemHold
	}
}