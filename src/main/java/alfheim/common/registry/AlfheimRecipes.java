package alfheim.common.registry;

import static vazkii.botania.common.block.ModBlocks.*;
import static vazkii.botania.common.item.ModItems.*;

import alfheim.common.items.ElvenItems;

import static cpw.mods.fml.common.registry.GameRegistry.*;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.block.ModBlocks;
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
		
		addShapedRecipe(new ItemStack(AlfheimItems.elvenItems, 1, 0),
			new Object[] {"PGP", "GDG", "PGP",
			'D', new ItemStack(manaResource, 1, 8),		// Pisie Dust
			'G', Items.gold_ingot,
			'P', new ItemStack(AlfheimItems.elvenItems, 1, 9)
		});
		
		addShapedRecipe(new ItemStack(AlfheimBlocks.manaInfuser),
			new Object[] {"DCD", "IRI", "SSS",
			'C', new ItemStack(AlfheimItems.elvenItems, 1, 0),
			'D', new ItemStack(manaResource, 1, 9),		// Dragonstone
			'I', new ItemStack(manaResource, 1, 7),		// Elementium
			'R', ModItems.rainbowRod,
			'S', new ItemStack(ModBlocks.livingrock, 1, 4)
		});
		
		addShapedRecipe(new ItemStack(AlfheimItems.elvenItems, 1, 3),
			new Object[] {" S ", "SIS", " S ",
			'S', new ItemStack(AlfheimItems.elvenItems, 1, 7),
			'I', new ItemStack(AlfheimItems.elvenItems, 1, 1)
		});
		
		addShapedRecipe(new ItemStack(AlfheimItems.elvenItems, 1, 4),
			new Object[] {" S ", "SIS", " S ",
			'S', new ItemStack(AlfheimItems.elvenItems, 1, 8),
			'I', new ItemStack(AlfheimItems.elvenItems, 1, 1)
		});
		
		addShapedRecipe(new ItemStack(AlfheimItems.manaElvenRing),
			new Object[] {"IS ", "S S", " S ",
			'S', new ItemStack(AlfheimItems.elvenItems, 1, 1),
			'I', new ItemStack(AlfheimItems.manaStone)
		});
		
		addShapedRecipe(new ItemStack(AlfheimItems.manaElvenRingGreater),
			new Object[] {"IS ", "S S", " S ",
			'S', new ItemStack(AlfheimItems.elvenItems, 1, 2),
			'I', new ItemStack(AlfheimItems.manaStoneGreater)
		});
		
		addShapedRecipe(new ItemStack(AlfheimItems.manaElvenRingGreater), 
			new Object[] {"SI", "IR",
			'S', AlfheimItems.manaStoneGreater,
			'R', AlfheimItems.manaElvenRing,
			'I', new ItemStack(AlfheimItems.elvenItems, 1, 2)});
		
		addShapelessRecipe(new ItemStack(AlfheimBlocks.elvoriumBlock),	 new ItemStack(AlfheimItems.elvenItems, 1, 1), new ItemStack(AlfheimItems.elvenItems, 1, 1), new ItemStack(AlfheimItems.elvenItems, 1, 1), new ItemStack(AlfheimItems.elvenItems, 1, 1), new ItemStack(AlfheimItems.elvenItems, 1, 1), new ItemStack(AlfheimItems.elvenItems, 1, 1), new ItemStack(AlfheimItems.elvenItems, 1, 1), new ItemStack(AlfheimItems.elvenItems, 1, 1), new ItemStack(AlfheimItems.elvenItems, 1, 1));
		addShapelessRecipe(new ItemStack(AlfheimBlocks.mauftriumBlock),	 new ItemStack(AlfheimItems.elvenItems, 1, 2), new ItemStack(AlfheimItems.elvenItems, 1, 2), new ItemStack(AlfheimItems.elvenItems, 1, 2), new ItemStack(AlfheimItems.elvenItems, 1, 2), new ItemStack(AlfheimItems.elvenItems, 1, 2), new ItemStack(AlfheimItems.elvenItems, 1, 2), new ItemStack(AlfheimItems.elvenItems, 1, 2), new ItemStack(AlfheimItems.elvenItems, 1, 2), new ItemStack(AlfheimItems.elvenItems, 1, 2));
		addShapelessRecipe(new ItemStack(AlfheimItems.elvenItems, 1, 1), new ItemStack(AlfheimItems.elvenItems, 1, 5), new ItemStack(AlfheimItems.elvenItems, 1, 5), new ItemStack(AlfheimItems.elvenItems, 1, 5), new ItemStack(AlfheimItems.elvenItems, 1, 5), new ItemStack(AlfheimItems.elvenItems, 1, 5), new ItemStack(AlfheimItems.elvenItems, 1, 5), new ItemStack(AlfheimItems.elvenItems, 1, 5), new ItemStack(AlfheimItems.elvenItems, 1, 5), new ItemStack(AlfheimItems.elvenItems, 1, 5));
		addShapelessRecipe(new ItemStack(AlfheimItems.elvenItems, 1, 2), new ItemStack(AlfheimItems.elvenItems, 1, 6), new ItemStack(AlfheimItems.elvenItems, 1, 6), new ItemStack(AlfheimItems.elvenItems, 1, 6), new ItemStack(AlfheimItems.elvenItems, 1, 6), new ItemStack(AlfheimItems.elvenItems, 1, 6), new ItemStack(AlfheimItems.elvenItems, 1, 6), new ItemStack(AlfheimItems.elvenItems, 1, 6), new ItemStack(AlfheimItems.elvenItems, 1, 6), new ItemStack(AlfheimItems.elvenItems, 1, 6)); 

		addShapelessRecipe(new ItemStack(AlfheimItems.elvenItems, 9, 1), AlfheimBlocks.elvoriumBlock);
		addShapelessRecipe(new ItemStack(AlfheimItems.elvenItems, 9, 2), AlfheimBlocks.mauftriumBlock);
		addShapelessRecipe(new ItemStack(AlfheimItems.elvenItems, 9, 5), new ItemStack(AlfheimItems.elvenItems, 1, 1));
		addShapelessRecipe(new ItemStack(AlfheimItems.elvenItems, 9, 6), new ItemStack(AlfheimItems.elvenItems, 1, 2));

	}
	
	public static void registerSmeltingRecipes() {
		addSmelting(new ItemStack(AlfheimBlocks.elvenOres, 1, 1), new ItemStack(ModItems.manaResource, 1, 7), 1.0F);
		addSmelting(new ItemStack(AlfheimBlocks.elvenOres, 1, 3), new ItemStack(Items.gold_ingot, 1, 0), 1.0F);
	}
}
