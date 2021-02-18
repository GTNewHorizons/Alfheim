package alfheim.common.block.colored

import alexsocol.asjlib.toItem
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.base.BlockSlabMod
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.common.IFuelHandler
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.*
import net.minecraft.entity.passive.EntitySheep
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.*
import vazkii.botania.api.lexicon.ILexiconable
import java.awt.Color

class BlockColoredWoodSlab(full: Boolean, meta: Int, source: Block = AlfheimBlocks.irisPlanks):
	BlockSlabMod(full, meta, source, source.unlocalizedName.replace("tile.".toRegex(), "") + "Slab" + (if (full) "Full" else "") + meta), ILexiconable, IFuelHandler {
	
	init {
		setResistance(10f)
		GameRegistry.registerFuelHandler(this)
	}
	
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
	
	override fun getFullBlock() = AlfheimBlocks.irisSlabsFull[meta] as BlockSlab
	
	override fun getSingleBlock() = AlfheimBlocks.irisSlabs[meta] as BlockSlab
	
	override fun getEntry(world: World?, x: Int, y: Int, z: Int, player: EntityPlayer?, lexicon: ItemStack?) = AlfheimLexiconData.irisSapling
	
	override fun getBurnTime(fuel: ItemStack) = if (fuel.item === this.toItem()) if (field_150004_a) 300 else 150 else 0
}
