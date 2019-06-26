package alfheim.common.block

import java.util.ArrayList
import java.util.Random

import alfheim.common.block.tile.TileFlugelHead
import alfheim.common.core.registry.AlfheimItems
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.block.BlockSkull
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntitySkull
import net.minecraft.util.IIcon
import net.minecraft.world.World

class BlockFlugelHead: BlockSkull() {
	init {
		setBlockName("FlugelHeadBlock")
		setHardness(1.0f)
	}
	
	@SideOnly(Side.CLIENT)
	override fun getItem(p_149694_1_: World?, p_149694_2_: Int, p_149694_3_: Int, p_149694_4_: Int): Item {
		return AlfheimItems.flugelHead
	}
	
	override fun registerBlockIcons(p_149651_1_: IIconRegister?) {
		// NO-OP
	}
	
	override fun getDrops(p_149749_1_: World, p_149749_2_: Int, p_149749_3_: Int, p_149749_4_: Int, p_149749_6_: Int, fortune: Int): ArrayList<ItemStack> {
		val ret = ArrayList<ItemStack>()
		
		if (p_149749_6_ and 8 == 0) {
			val itemstack = ItemStack(AlfheimItems.flugelHead, 1)
			val tileentityskull = p_149749_1_.getTileEntity(p_149749_2_, p_149749_3_, p_149749_4_) as TileEntitySkull
								  ?: return ret
			
			ret.add(itemstack)
		}
		return ret
	}
	
	override fun getItemDropped(p_149650_1_: Int, p_149650_2_: Random?, p_149650_3_: Int): Item {
		return AlfheimItems.flugelHead
	}
	
	override fun getDamageValue(p_149643_1_: World, p_149643_2_: Int, p_149643_3_: Int, p_149643_4_: Int): Int {
		return 0
	}
	
	override fun damageDropped(p_149692_1_: Int): Int {
		return 0
	}
	
	override fun createNewTileEntity(p_149915_1_: World?, p_149915_2_: Int): TileEntity {
		return TileFlugelHead()
	}
	
	@SideOnly(Side.CLIENT)
	override fun getIcon(p_149691_1_: Int, p_149691_2_: Int): IIcon {
		return Blocks.coal_block.getBlockTextureFromSide(p_149691_1_)
	}
}