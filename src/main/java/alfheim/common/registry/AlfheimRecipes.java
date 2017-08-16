package alfheim.common.registry;

import static vazkii.botania.common.block.ModBlocks.*;
import static vazkii.botania.common.item.ModItems.*;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class AlfheimRecipes {
	
	public static void init() {
		GameRegistry.addShapedRecipe(new ItemStack(AlfheimBlocks.alfheimPortal),
		new Object[] {"DPD", "GSG", "DPD",
		'D', dreamwood,
		'G', Items.glowstone_dust,
		'P', new ItemStack(manaResource, 1, 14),	// Gaia Ingot
		'S', new ItemStack(manaResource, 1, 9)		// Dragonstone
		});
	}
}
