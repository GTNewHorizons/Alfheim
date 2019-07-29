package alfheim.common.block.schema

import alfheim.common.block.base.BlockMod
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.IIcon
import net.minecraft.world.World
import alfheim.common.block.tile.TileSchematicAnnihilator
import alfheim.common.core.helper.IconHelper
import vazkii.botania.api.wand.IWandable

/**
 * Created by l0nekitsune on 1/3/16.
 */
class BlockSchematicAnnihilator: BlockMod(Material.wood), IWandable, ITileEntityProvider {
	
	lateinit var icon1: IIcon
	lateinit var icon2: IIcon
	lateinit var icon3: IIcon
	
	init {
		setBlockName("schematicAnnihilator")
		isBlockContainer = true
	}
	
	override fun getIcon(side: Int, meta: Int) =
		when (side) {
			0, 1 -> icon1
			2, 3 -> icon2
			else -> icon3
		}
	
	override fun hasTileEntity(metadata: Int) = true
	
	override fun createNewTileEntity(p_149915_1_: World?, p_149915_2_: Int) = TileSchematicAnnihilator()
	
	override fun breakBlock(p_149749_1_: World, p_149749_2_: Int, p_149749_3_: Int, p_149749_4_: Int, p_149749_5_: Block?, p_149749_6_: Int) {
		super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_)
		p_149749_1_.removeTileEntity(p_149749_2_, p_149749_3_, p_149749_4_)
	}
	
	override fun onBlockEventReceived(p_149696_1_: World?, p_149696_2_: Int, p_149696_3_: Int, p_149696_4_: Int, p_149696_5_: Int, p_149696_6_: Int): Boolean {
		super.onBlockEventReceived(p_149696_1_, p_149696_2_, p_149696_3_, p_149696_4_, p_149696_5_, p_149696_6_)
		val tileentity = p_149696_1_!!.getTileEntity(p_149696_2_, p_149696_3_, p_149696_4_)
		return tileentity?.receiveClientEvent(p_149696_5_, p_149696_6_) ?: false
	}
	
	override fun registerBlockIcons(par1IconRegister: IIconRegister) {
		icon1 = IconHelper.forName(par1IconRegister, "schema1")
		icon2 = IconHelper.forName(par1IconRegister, "schema2")
		icon3 = IconHelper.forName(par1IconRegister, "schema3")
	}
	
	override fun onUsedByWand(p0: EntityPlayer?, p1: ItemStack?, p2: World?, p3: Int, p4: Int, p5: Int, p6: Int): Boolean {
		if (p2 != null && p0 != null) {
			val tile: TileEntity? = p2.getTileEntity(p3, p4, p5)
			if (tile is TileSchematicAnnihilator)
				tile.blockActivated(p0)
		}
		return true
	}
}
