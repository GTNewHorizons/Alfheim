package alfheim.common.block.tile.sub.flower

import alfheim.api.AlfheimAPI
import net.minecraft.init.Blocks
import net.minecraft.util.IIcon
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.lexicon.LexiconEntry
import vazkii.botania.common.block.subtile.functional.SubTileOrechid

class SubTileOrechidEndium: SubTileOrechid() {
	
	private val COST = 20000
	
	override fun canOperate(): Boolean {
		return supertile.worldObj.provider.dimensionId == 1
	}
	
	override fun getOreMap() = AlfheimAPI.oreWeightsEnd
	
	override fun getSourceBlock() = Blocks.end_stone
	
	override fun getCost() = COST
	
	val colors = intArrayOf(
		0xFFE354, 0xFF1FBB, 0xB533FF
	)
	
	override fun getColor() = colors.random()
	
	override fun getEntry(): LexiconEntry? {
		return null // OrechidEndiumAPI.orechidEndiumLexiconEntry TODO
	}
	
	override fun getIcon(): IIcon? = BotaniaAPI.getSignatureForName("orechidEndium").getIconForStack(null)
}
