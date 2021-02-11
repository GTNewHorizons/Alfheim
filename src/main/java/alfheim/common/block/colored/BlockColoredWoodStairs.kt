package alfheim.common.block.colored

import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.base.BlockStairsMod
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.entity.passive.EntitySheep
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.*
import vazkii.botania.api.lexicon.ILexiconable
import java.awt.Color

class BlockColoredWoodStairs(meta: Int, source: Block = AlfheimBlocks.irisPlanks):
	BlockStairsMod(source, meta, source.unlocalizedName.replace("tile.".toRegex(), "") + "Stairs" + meta), ILexiconable {
	
	@SideOnly(Side.CLIENT)
	override fun getRenderColor(m: Int): Int {
		if (meta >= EntitySheep.fleeceColorTable.size)
			return 0xFFFFFF
		
		val color = EntitySheep.fleeceColorTable[meta]
		return Color(color[0], color[1], color[2]).rgb
	}
	
	override fun isToolEffective(type: String?, metadata: Int) = (type != null && type == "axe")
	
	override fun getHarvestTool(metadata: Int) = "axe"
	
	@SideOnly(Side.CLIENT)
	override fun colorMultiplier(world: IBlockAccess?, x: Int, y: Int, z: Int) = getRenderColor(meta)
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = AlfheimLexiconData.irisSapling
}
