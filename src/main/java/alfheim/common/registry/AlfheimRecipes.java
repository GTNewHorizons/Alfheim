package alfheim.common.registry;

import static vazkii.botania.common.block.ModBlocks.*;
import static vazkii.botania.common.item.ModItems.*;
import static cpw.mods.fml.common.registry.GameRegistry.*;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.ModItems;

public class AlfheimRecipes {
	
	public static void init() {
		registerCraftingRecipes();
		registerSmeltingRecipes();
	}

	public static void registerCraftingRecipes() {
		addShapedRecipe(new ItemStack(AlfheimBlocks.alfheimPortal),
			new Object[] {"DPD", "GSG", "DPD",
			'D', dreamwood,
			'G', Items.glowstone_dust,
			'P', new ItemStack(manaResource, 1, 14),	// Gaia Ingot
			'S', new ItemStack(manaResource, 1, 9)		// Dragonstone
		});
		
		
	}
	
	public static void registerSmeltingRecipes() {
		addSmelting(new ItemStack(AlfheimBlocks.elvenOres, 1, 1), new ItemStack(ModItems.manaResource, 1, 7), 1.0F);
		addSmelting(new ItemStack(AlfheimBlocks.elvenOres, 1, 3), new ItemStack(Items.gold_ingot, 1, 0), 1.0F);
	}
}
