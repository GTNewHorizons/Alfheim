package alfheim.common.block.colored

import alfheim.common.block.base.BlockModRotatedPillar
import alfheim.common.item.block.ItemIridescentWoodMod
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.passive.EntitySheep
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.world.*
import java.awt.Color

class BlockColoredWood(val colorSet: Int): BlockModRotatedPillar(Material.wood) {
	
	private val name = "irisWood$colorSet"
	
	init {
		blockHardness = 2F
		setLightLevel(0f)
		stepSound = soundTypeWood
		
		setBlockName(name)
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
	}
	
	override fun register(name: String) {
		GameRegistry.registerBlock(this, ItemIridescentWoodMod::class.java, name)
	}
	
	@SideOnly(Side.CLIENT)
	override fun getBlockColor() = 0xFFFFFF
	
	@SideOnly(Side.CLIENT)
	fun colorMeta(meta: Int) = meta + (colorSet * 4)
	
	/**
	 * Returns the color this block should be rendered. Used by leaves.
	 */
	@SideOnly(Side.CLIENT)
	override fun getRenderColor(meta: Int): Int {
		if (colorMeta(meta and 3) >= EntitySheep.fleeceColorTable.size)
			return 0xFFFFFF
		
		val color = EntitySheep.fleeceColorTable[colorMeta(meta and 3)]
		return Color(color[0], color[1], color[2]).rgb
	}
	
	@SideOnly(Side.CLIENT)
	override fun colorMultiplier(world: IBlockAccess?, x: Int, y: Int, z: Int): Int {
		val meta = world!!.getBlockMetadata(x, y, z)
		return getRenderColor(meta)
	}
	
	override fun canSustainLeaves(world: IBlockAccess, x: Int, y: Int, z: Int) = true
	
	override fun isWood(world: IBlockAccess, x: Int, y: Int, z: Int) = true
	
	override fun getSubBlocks(item: Item?, tab: CreativeTabs?, list: MutableList<Any?>?) {
		if (list != null && item != null) {
			list.add(ItemStack(this, 1, 0))
			list.add(ItemStack(this, 1, 1))
			list.add(ItemStack(this, 1, 2))
			list.add(ItemStack(this, 1, 3))
		}
	}
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) =
		AlfheimLexiconData.irisSapling
}
