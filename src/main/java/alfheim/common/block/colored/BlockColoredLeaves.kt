package alfheim.common.block.colored

import alexsocol.asjlib.toItem
import alfheim.api.lib.LibOreDict.LEAVES
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.base.BlockLeavesMod
import alfheim.common.item.block.ItemIridescentLeavesMod
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.passive.EntitySheep
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.world.*
import java.awt.Color
import java.util.*

class BlockColoredLeaves(val colorSet: Int): BlockLeavesMod() {
	
	val TYPES: Int = 8
	
	init {
		setBlockName("irisLeaves$colorSet")
	}
	
	override fun quantityDropped(random: Random) = if (random.nextInt(20) == 0) 1 else 0
	
	override fun register(name: String) {
		GameRegistry.registerBlock(this, ItemIridescentLeavesMod::class.java, name)
	}
	
	@SideOnly(Side.CLIENT)
	override fun getRenderColor(meta: Int): Int {
		val shiftedMeta = meta % TYPES + colorSet * TYPES
		if (shiftedMeta >= EntitySheep.fleeceColorTable.size)
			return 0xFFFFFF
		
		val color = EntitySheep.fleeceColorTable[shiftedMeta]
		return Color(color[0], color[1], color[2]).rgb
	}
	
	@SideOnly(Side.CLIENT)
	override fun colorMultiplier(world: IBlockAccess?, x: Int, y: Int, z: Int): Int {
		val meta = world!!.getBlockMetadata(x, y, z)
		return getRenderColor(meta)
	}
	
	override fun getSubBlocks(item: Item?, tab: CreativeTabs?, list: MutableList<Any?>?) {
		if (list != null && item != null)
			for (i in 0 until TYPES) {
				list.add(ItemStack(item, 1, i))
			}
	}
	
	override fun getItemDropped(meta: Int, random: Random, fortune: Int) = AlfheimBlocks.irisSapling.toItem()
	
	override fun createStackedBlock(meta: Int) = ItemStack(this, 1, meta % 8)
	
	override fun func_150125_e() = LEAVES.sliceArray(colorSet * 8 until (colorSet + 1) * 8)
	
	override fun decayBit(): Int = 0x8
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = AlfheimLexiconData.irisSapling
}
