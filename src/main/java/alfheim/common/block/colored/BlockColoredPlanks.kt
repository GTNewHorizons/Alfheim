package alfheim.common.block.colored

import alexsocol.asjlib.*
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.base.BlockMod
import alfheim.common.block.tile.TileTreeCrafter
import alfheim.common.item.block.ItemSubtypedBlockMod
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.passive.EntitySheep
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.*
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.wand.IWandable
import java.awt.Color
import java.util.*

class BlockColoredPlanks: BlockMod(Material.wood), ILexiconable, IWandable {
	
	private val name = "irisPlanks"
	private val TYPES = 16
	
	init {
		blockHardness = 2F
		setLightLevel(0f)
		stepSound = soundTypeWood
		
		setBlockName(name)
	}
	
	@SideOnly(Side.CLIENT)
	override fun getBlockColor() = 0xFFFFFF
	
	override fun isToolEffective(type: String?, metadata: Int) = (type != null && type == "axe")
	
	override fun getHarvestTool(metadata: Int) = "axe"
	
	override fun onUsedByWand(p0: EntityPlayer?, p1: ItemStack?, p2: World?, p3: Int, p4: Int, p5: Int, p6: Int): Boolean {
		if (p2 != null) {
			if (TileTreeCrafter.canEnchanterExist(p2, p3, p4, p5)) {
				val meta = p2.getBlockMetadata(p3, p4, p5)
				p2.setBlock(p3, p4, p5, AlfheimBlocks.treeCrafterBlock, meta, 3)
				p2.playSoundEffect(p3.D, p4.D, p5.D, "botania:enchanterBlock", 0.5F, 0.6F)
				
				return true
			}
		}
		
		return false
	}
	
	/**
	 * Returns the color this block should be rendered. Used by leaves.
	 */
	@SideOnly(Side.CLIENT)
	override fun getRenderColor(meta: Int): Int {
		if (meta >= EntitySheep.fleeceColorTable.size)
			return 0xFFFFFF
		
		val color = EntitySheep.fleeceColorTable[meta]
		return Color(color[0], color[1], color[2]).rgb
	}
	
	@SideOnly(Side.CLIENT)
	override fun colorMultiplier(world: IBlockAccess?, x: Int, y: Int, z: Int): Int {
		val meta = world!!.getBlockMetadata(x, y, z)
		return getRenderColor(meta)
	}
	
	override fun shouldRegisterInNameSet() = false
	
	override fun damageDropped(par1: Int) = par1
	
	override fun setBlockName(name: String): Block {
		register(name)
		return super.setBlockName(name)
	}
	
	override fun quantityDropped(random: Random) = 1
	
	override fun getItemDropped(meta: Int, random: Random, fortune: Int) = this.toItem()
	
	internal fun register(name: String) {
		GameRegistry.registerBlock(this, ItemSubtypedBlockMod::class.java, name)
	}
	
	override fun getPickBlock(target: MovingObjectPosition?, world: World, x: Int, y: Int, z: Int): ItemStack {
		val meta = world.getBlockMetadata(x, y, z)
		return ItemStack(this, 1, meta)
	}
	
	override fun getSubBlocks(item: Item?, tab: CreativeTabs?, list: MutableList<Any?>?) {
		if (list != null && item != null)
			for (i in 0 until TYPES) {
				list.add(ItemStack(item, 1, i))
			}
	}
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = AlfheimLexiconData.irisSapling
}
