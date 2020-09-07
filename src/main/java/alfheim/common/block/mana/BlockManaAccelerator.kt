package alfheim.common.block.mana

import alexsocol.asjlib.extendables.TileItemContainer
import alexsocol.asjlib.mfloor
import alfheim.api.lib.LibRenderIDs
import alfheim.common.block.base.BlockContainerMod
import alfheim.common.block.tile.TileManaAccelerator
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.*
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.mana.IManaItem
import vazkii.botania.client.lib.LibResources
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.block.mana.BlockPool
import kotlin.math.*

class BlockManaAccelerator: BlockContainerMod(Material.rock), ILexiconable {

	init {
		setBlockBounds(0f, -0.5f, 0f, 1f, -0.125f, 1f)
		setBlockName("ManaAccelerator")
		setBlockTextureName(LibResources.PREFIX_MOD + "livingrock0")
		setHardness(1f)
	}
	
	override fun getIcon(side: Int, meta: Int) = ModBlocks.livingrock.getIcon(0, 0)!!
	override fun registerBlockIcons(reg: IIconRegister) = Unit
	override fun renderAsNormalBlock() = false
	override fun isOpaqueCube() = false
	override fun getRenderType() = LibRenderIDs.idManaAccelerator
	override fun canPlaceBlockAt(world: World, x: Int, y: Int, z: Int) = world.getBlock(x, y - 1, z) is BlockPool
	
	override fun onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		val te = world.getTileEntity(x, y, z) as TileItemContainer
		val stack = player.inventory.getCurrentItem()
		if (player.isSneaking) return false
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
		
		world.updateLightByType(EnumSkyBlock.Sky, x, y, z)
		
		return true
	}
	
	@SideOnly(Side.CLIENT)
	override fun getMixedBrightnessForBlock(world: IBlockAccess, x: Int, y: Int, z: Int): Int {
		return world.getLightBrightnessForSkyBlocks(x, y, z, world.getBlock(x, y, z).getLightValue(world, x, y - 1, z))
	}
	
	override fun breakBlock(world: World, x: Int, y: Int, z: Int, block: Block?, meta: Int) {
		run {
			val te = world.getTileEntity(x, y, z) as? TileItemContainer ?: return@run
			if (te.item != null) {
				world.spawnEntityInWorld(EntityItem(world, x + 0.5, y + 0.5, z + 0.5, te.item!!))
				te.item = null
			}
		}
		
		super.breakBlock(world, x, y, z, block, meta)
	}
	
	override fun hasComparatorInputOverride() = true
	
	override fun getComparatorInputOverride(world: World, x: Int, y: Int, z: Int, side: Int): Int {
		val te = world.getTileEntity(x, y, z) as TileItemContainer
		
		if (te.item != null && te.item!!.item is IManaItem) {
			val stack = te.item
			val mana = te.item!!.item as IManaItem
			
			if (mana.getMana(stack) == mana.getMaxMana(stack)) return 15
			return if (mana.getMana(stack) == 0) 0 else min(max(0.0, mana.getMana(stack) * 15.0 / mana.getMaxMana(stack)), 15.0).mfloor()
			
		}
		
		return 0
	}
	
	override fun createNewTileEntity(world: World, meta: Int) = TileManaAccelerator()
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack) = AlfheimLexiconData.manaAccelerator
}