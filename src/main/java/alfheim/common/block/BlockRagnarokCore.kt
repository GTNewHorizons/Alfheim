package alfheim.common.block

import alfheim.client.core.helper.IconHelper
import alfheim.common.block.base.BlockContainerMod
import alfheim.common.block.tile.TileRagnarokCore
import alfheim.common.item.equipment.bauble.faith.ItemRagnarokEmblem
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.IIcon
import net.minecraft.world.World

class BlockRagnarokCore: BlockContainerMod(Material.rock) {
	
	lateinit var iconActive: IIcon
	
	init {
		setBlockName("RagnarokCore")
	}
	
	override fun createNewTileEntity(world: World?, meta: Int) = TileRagnarokCore()
	
	override fun onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		if (ItemRagnarokEmblem.getEmblem(player) == null) return false
		val tile = world.getTileEntity(x, y, z) as? TileRagnarokCore ?: return false
		if (!tile.checkStructure(false)) return false
		tile.started = true
		return true
	}
	
	override fun registerBlockIcons(reg: IIconRegister) {
		super.registerBlockIcons(reg)
		iconActive = IconHelper.forBlock(reg, this, "Active")
	}
	
	override fun getIcon(side: Int, meta: Int) = if (meta == 1) iconActive else super.getIcon(side, meta)!!
}
