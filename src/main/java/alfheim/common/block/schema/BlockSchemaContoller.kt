package alfheim.common.block.schema

import alfheim.common.block.base.BlockContainerMod
import alfheim.common.block.tile.TileSchemaController
import alfheim.common.core.helper.IconHelper
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.IIcon
import net.minecraft.world.World
import vazkii.botania.api.wand.IWandable
import java.util.*

class BlockSchemaContoller: BlockContainerMod(Material.wood), IWandable {
	
	lateinit var icon1: IIcon
	lateinit var icon2: IIcon
	lateinit var icon3: IIcon
	
	init {
		setBlockName("schemaController")
		setBlockUnbreakable()
	}
	
	override fun getItemDropped(meta: Int, rand: Random, fortune: Int) = null
	
	override fun getIcon(side: Int, meta: Int) =
		when (side) {
			0, 1 -> icon1
			2, 3 -> icon2
			else -> icon3
		}
	
	override fun createNewTileEntity(world: World?, meta: Int) = TileSchemaController()
	
	override fun registerBlockIcons(reg: IIconRegister) {
		icon1 = IconHelper.forName(reg, "schema1")
		icon2 = IconHelper.forName(reg, "schema2")
		icon3 = IconHelper.forName(reg, "schema3")
	}
	
	override fun onUsedByWand(p0: EntityPlayer?, p1: ItemStack?, p2: World?, p3: Int, p4: Int, p5: Int, p6: Int): Boolean {
		if (p2 != null) {
			val tile: TileEntity? = p2.getTileEntity(p3, p4, p5)
			if (tile is TileSchemaController)
				tile.blockActivated(p0)
		}
		return true
	}
}
