package alfheim.common.block

import alfheim.AlfheimCore
import alfheim.common.block.base.BlockModRotatedPillar
import alfheim.common.core.helper.IconHelper
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraft.world.*
import vazkii.botania.api.lexicon.ILexiconable

class BlockDreamLog: BlockModRotatedPillar(Material.wood), ILexiconable {
	
	lateinit var textures: Array<IIcon>
	
	init {
		setBlockName("DreamLog")
		setCreativeTab(AlfheimCore.alfheimTab)
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerBlockIcons(reg: IIconRegister) {
		textures = Array(2) { IconHelper.forBlock(reg, this, it) }
	}
	
	override fun breakBlock(world: World, x: Int, y: Int, z: Int, block: Block, fortune: Int) {
		val b0: Byte = 4
		val i1: Int = b0 + 1
		
		if (world.checkChunksExist(x - i1, y - i1, z - i1, x + i1, y + i1, z + i1)) {
			for (j1 in -b0..b0) for (k1 in -b0..b0)
				for (l1 in -b0..b0) {
					val blockInWorld: Block = world.getBlock(x + j1, y + k1, z + l1)
					if (blockInWorld.isLeaves(world, x + j1, y + k1, z + l1)) {
						blockInWorld.beginLeavesDecay(world, x + j1, y + k1, z + l1)
					}
				}
		}
		super.breakBlock(world, x, y, z, block, fortune)
	}
	
	override fun canSustainLeaves(world: IBlockAccess, x: Int, y: Int, z: Int) = true
	override fun isWood(world: IBlockAccess?, x: Int, y: Int, z: Int) = true
	@SideOnly(Side.CLIENT)
	override fun getSideIcon(meta: Int) = textures[1]
	@SideOnly(Side.CLIENT)
	override fun getTopIcon(meta: Int) = textures[0]
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack) = AlfheimLexiconData.worldgen
}
