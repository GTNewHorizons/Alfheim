package alfheim.common.registry;

import alexsocol.asjlib.ASJUtilities;
import alfheim.common.blocks.AlfheimPortal;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

public class AlfheimBlocks {

	public static Block alfheimPortal = new AlfheimPortal();
	
	public static void init() {
		registration();
		oreDictRegistration();
	}

	private static void registration() {
		GameRegistry.registerBlock(alfheimPortal, ASJUtilities.getBlockName(alfheimPortal));
	}

	private static void oreDictRegistration() {

	}
}
