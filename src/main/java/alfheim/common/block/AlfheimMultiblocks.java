package alfheim.common.block;

import alfheim.common.block.tile.TileAlfheimPortal;
import alfheim.common.block.tile.TileManaInfuser;
import alfheim.common.block.tile.TileTradePortal;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;

public class AlfheimMultiblocks {

	public static MultiblockSet infuser;
	public static MultiblockSet infuserU;
	public static MultiblockSet portal;
	public static MultiblockSet soul;
	public static MultiblockSet yordin;

	public static void init() {
		infuser = TileManaInfuser.makeMultiblockSet();
		infuserU = TileManaInfuser.makeMultiblockSetUnknown();
		portal = TileAlfheimPortal.makeMultiblockSet();
		soul = TileManaInfuser.makeMultiblockSetSoul();
		yordin = TileTradePortal.makeMultiblockSet();
	}
}
