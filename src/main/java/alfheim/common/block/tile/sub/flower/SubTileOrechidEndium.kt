package alfheim.common.block.tile.sub.flower

import alfheim.api.AlfheimAPI
import alfheim.client.render.world.VisualEffectHandlerClient
import alfheim.common.core.handler.VisualEffectHandler
import alfheim.common.lexicon.AlfheimLexiconData
import net.minecraft.init.Blocks
import net.minecraft.util.IIcon
import net.minecraftforge.oredict.OreDictionary
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.common.block.subtile.functional.SubTileOrechid

class SubTileOrechidEndium: SubTileOrechid() {
	
	private val COST = 20000
	
	override fun canOperate(): Boolean {
		if (!searched) {
			searched = true
			for (e in oreMap)
				if (OreDictionary.getOres(e.key).isNotEmpty()) {
					anyOres = true
					break
				}
		}
		
		if (!anyOres) {
			VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.WISP, supertile.worldObj.provider.dimensionId, supertile.xCoord + Math.random(), supertile.yCoord + Math.random(), supertile.zCoord + Math.random(), 1.0, 0.0, 0.0, 0.25, 0.0, 0.0, 0.0, 0.5)
			return false
		}
		
		return supertile.worldObj.provider.dimensionId == 1
	}
	
	override fun getOreMap() = AlfheimAPI.oreWeightsEnd
	
	override fun getSourceBlock() = Blocks.end_stone
	
	override fun getCost() = COST
	
	override fun getColor() = 0xB533FF
	
	override fun getEntry() = AlfheimLexiconData.flowerEnderchid
	
	override fun getIcon(): IIcon? = BotaniaAPI.getSignatureForName("orechidEndium").getIconForStack(null)
	
	companion object {
		
		var searched = false
		var anyOres = false
	}
}
