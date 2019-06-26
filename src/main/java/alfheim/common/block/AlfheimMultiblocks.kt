package alfheim.common.block

import alfheim.common.block.tile.TileAlfheimPortal
import alfheim.common.block.tile.TileManaInfuser
import alfheim.common.block.tile.TileTradePortal
import vazkii.botania.api.lexicon.multiblock.MultiblockSet

object AlfheimMultiblocks {
	
	var infuser: MultiblockSet
	var infuserU: MultiblockSet
	var portal: MultiblockSet
	var soul: MultiblockSet
	var yordin: MultiblockSet
	
	fun init() {
		infuser = TileManaInfuser.makeMultiblockSet()
		infuserU = TileManaInfuser.makeMultiblockSetUnknown()
		portal = TileAlfheimPortal.makeMultiblockSet()
		soul = TileManaInfuser.makeMultiblockSetSoul()
		yordin = TileTradePortal.makeMultiblockSet()
	}
}
