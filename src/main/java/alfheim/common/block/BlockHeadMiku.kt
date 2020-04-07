package alfheim.common.block

import alfheim.api.ModInfo
import alfheim.common.block.tile.TileHeadMiku
import alfheim.common.item.AlfheimItems
import alfheim.common.item.block.ItemBlockLeavesMod
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import java.util.*

class BlockHeadMiku: BlockSkull() {
	
	init {
		setBlockName("MikuHeadBlock")
		setCreativeTab(null)
		setHardness(1f)
	}
	
	override fun setBlockName(name: String): Block {
		GameRegistry.registerBlock(this, ItemBlockLeavesMod::class.java, name)
		return super.setBlockName(name)
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
	
	override fun getItem(world: World, x: Int, y: Int, z: Int) = AlfheimItems.flugelHead2
	override fun registerBlockIcons(reg: IIconRegister) = Unit
	override fun getItemDropped(meta: Int, rand: Random, fortune: Int) = AlfheimItems.flugelHead2
	override fun getDamageValue(world: World, x: Int, y: Int, z: Int) = 0
	override fun damageDropped(meta: Int) = 0
	override fun createNewTileEntity(world: World, meta: Int) = TileHeadMiku()
	override fun getIcon(side: Int, meta: Int) = Blocks.wool.getIcon(0, 9)
	override fun getItemIconName() = "${ModInfo.MODID}:MikuHead"
}