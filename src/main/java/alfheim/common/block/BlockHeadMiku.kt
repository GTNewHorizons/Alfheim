package alfheim.common.block

import alfheim.common.block.tile.TileHeadMiku
import alfheim.common.core.registry.AlfheimItems
import cpw.mods.fml.relauncher.*
import net.minecraft.block.BlockSkull
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.init.Blocks
import net.minecraft.item.*
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.IIcon
import net.minecraft.world.World
import java.util.*

class BlockHeadMiku: BlockSkull() {
	
	init {
		setBlockName("MikuHeadBlock")
		setHardness(1.0f)
	}
	
	@SideOnly(Side.CLIENT)
	override fun getItem(p_149694_1_: World?, p_149694_2_: Int, p_149694_3_: Int, p_149694_4_: Int): Item {
		return AlfheimItems.flugelHead2
	}
	
	override fun registerBlockIcons(p_149651_1_: IIconRegister?) {
		// NO-OP
	}
	
	override fun getDrops(world: World, x: Int, y: Int, z: Int, meta: Int, fortune: Int): ArrayList<ItemStack> {
		val ret = ArrayList<ItemStack>()
		if (meta and 8 == 0) {
			val itemstack = ItemStack(AlfheimItems.flugelHead2, 1)
			world.getTileEntity(x, y, z) ?: return ret
			
			ret.add(itemstack)
		}
		
		return ret
	}
	
	override fun getItemDropped(p_149650_1_: Int, p_149650_2_: Random?, p_149650_3_: Int): Item {
		return AlfheimItems.flugelHead2
	}
	
	override fun getDamageValue(p_149643_1_: World, p_149643_2_: Int, p_149643_3_: Int, p_149643_4_: Int): Int {
		return 0
	}
	
	override fun damageDropped(p_149692_1_: Int): Int {
		return 0
	}
	
	override fun createNewTileEntity(p_149915_1_: World?, p_149915_2_: Int): TileEntity {
		return TileHeadMiku()
	}
	
	@SideOnly(Side.CLIENT)
	override fun getIcon(p_149691_1_: Int, p_149691_2_: Int): IIcon {
		return Blocks.coal_block.getBlockTextureFromSide(p_149691_1_)
	}
}