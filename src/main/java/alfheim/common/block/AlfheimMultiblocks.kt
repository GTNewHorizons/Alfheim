package alfheim.common.block

import alfheim.common.block.tile.TileAlfheimPortal
import alfheim.common.block.tile.TileManaInfuser
import alfheim.common.block.tile.TileTradePortal
import vazkii.botania.api.lexicon.multiblock.MultiblockSet

object AlfheimMultiblocks {
	
	lateinit var infuser: MultiblockSet
	lateinit var infuserU: MultiblockSet
	lateinit var portal: MultiblockSet
	lateinit var soul: MultiblockSet
	lateinit var yordin: MultiblockSet
	
	fun init() {
		infuser = TileManaInfuser.makeMultiblockSet()
		infuserU = TileManaInfuser.makeMultiblockSetUnknown()
		portal = TileAlfheimPortal.makeMultiblockSet()
		soul = TileManaInfuser.makeMultiblockSetSoul()
		yordin = TileTradePortal.makeMultiblockSet()
	}
}
