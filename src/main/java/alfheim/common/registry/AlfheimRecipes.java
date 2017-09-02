package alfheim.common.registry;

import static cpw.mods.fml.common.registry.GameRegistry.addShapedRecipe;
import static cpw.mods.fml.common.registry.GameRegistry.addShapelessRecipe;
import static cpw.mods.fml.common.registry.GameRegistry.addSmelting;
import static vazkii.botania.common.block.ModBlocks.dreamwood;
import static vazkii.botania.common.item.ModItems.manaResource;

import alfheim.common.utils.ManaInfusionRecipies;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.item.ModItems;

public class AlfheimRecipes {
	
	public static void init() {
		registerCraftingRecipes();
		registerShapelessRecipies();
		registerSmeltingRecipes();
		registerManaInfusionRecipies();
		registerRecipies();
	}

	public static void registerCraftingRecipes() {
		addShapedRecipe(new ItemStack(AlfheimBlocks.alfheimPortal),
			new Object[] {"DPD", "GSG", "DPD",
			'D', dreamwood,
			'G', Items.glowstone_dust,
			'P', new ItemStack(manaResource, 1, 14),	// Gaia Ingot
			'S', new ItemStack(manaResource, 1, 9)		// Dragonstone
		});
		
		addShapedRecipe(new ItemStack(AlfheimItems.elvenResource, 1, 0),
			new Object[] {"PGP", "GDG", "PGP",
			'D', new ItemStack(manaResource, 1, 8),		// Pisie Dust
			'G', Items.gold_ingot,
			'P', new ItemStack(AlfheimItems.elvenResource, 1, 9)
		});
		
		addShapedRecipe(new ItemStack(AlfheimBlocks.manaInfuser),
			new Object[] {"DCD", "IRI", "STS",
			'C', new ItemStack(AlfheimItems.elvenResource, 1, 0),
			'D', new ItemStack(manaResource, 1, 9),		// Dragonstone
			'I', new ItemStack(manaResource, 1, 7),		// Elementium
			'R', ModItems.rainbowRod,
			'S', new ItemStack(ModBlocks.livingrock, 1, 4),
			'T', ModBlocks.terraPlate
		});
		
		addShapedRecipe(new ItemStack(AlfheimItems.elvenResource, 1, 3),
			new Object[] {" S ", "SIS", " S ",
			'S', new ItemStack(AlfheimItems.elvenResource, 1, 7),
			'I', new ItemStack(AlfheimItems.elvenResource, 1, 1)
		});
		
		addShapedRecipe(new ItemStack(AlfheimItems.elvenResource, 1, 4),
			new Object[] {" S ", "SIS", " S ",
			'S', new ItemStack(AlfheimItems.elvenResource, 1, 8),
			'I', new ItemStack(AlfheimItems.elvenResource, 1, 1)
		});
		
		addShapedRecipe(new ItemStack(AlfheimItems.manaElvenRing),
			new Object[] {"IS ", "S S", " S ",
			'S', new ItemStack(AlfheimItems.elvenResource, 1, 1),
			'I', new ItemStack(AlfheimItems.manaStone)
		});
		
		addShapedRecipe(new ItemStack(AlfheimItems.manaElvenRingGreater),
			new Object[] {"IS ", "S S", " S ",
			'S', new ItemStack(AlfheimItems.elvenResource, 1, 2),
			'I', new ItemStack(AlfheimItems.manaStoneGreater)
		});
		
		addShapedRecipe(new ItemStack(AlfheimItems.manaElvenRingGreater), 
			new Object[] {"SI", "IR",
			'S', AlfheimItems.manaStoneGreater,
			'R', AlfheimItems.manaElvenRing,
			'I', new ItemStack(AlfheimItems.elvenResource, 1, 2)});
		
		addShapedRecipe(new ItemStack(AlfheimItems.realitySword),
			new Object[] {" M ", "MRM", " S ",
			'M', new ItemStack(AlfheimItems.elvenResource, 1, 2),
			'R', new ItemStack(AlfheimItems.elvenResource, 1, 10),
			'S', new ItemStack(ModItems.manaResource, 1, 3)});
	}

	public static void registerShapelessRecipies() {
		addShapelessRecipe(new ItemStack(AlfheimBlocks.elvoriumBlock),	 new ItemStack(AlfheimItems.elvenResource, 1, 1), new ItemStack(AlfheimItems.elvenResource, 1, 1), new ItemStack(AlfheimItems.elvenResource, 1, 1), new ItemStack(AlfheimItems.elvenResource, 1, 1), new ItemStack(AlfheimItems.elvenResource, 1, 1), new ItemStack(AlfheimItems.elvenResource, 1, 1), new ItemStack(AlfheimItems.elvenResource, 1, 1), new ItemStack(AlfheimItems.elvenResource, 1, 1), new ItemStack(AlfheimItems.elvenResource, 1, 1));
		addShapelessRecipe(new ItemStack(AlfheimBlocks.mauftriumBlock),	 new ItemStack(AlfheimItems.elvenResource, 1, 2), new ItemStack(AlfheimItems.elvenResource, 1, 2), new ItemStack(AlfheimItems.elvenResource, 1, 2), new ItemStack(AlfheimItems.elvenResource, 1, 2), new ItemStack(AlfheimItems.elvenResource, 1, 2), new ItemStack(AlfheimItems.elvenResource, 1, 2), new ItemStack(AlfheimItems.elvenResource, 1, 2), new ItemStack(AlfheimItems.elvenResource, 1, 2), new ItemStack(AlfheimItems.elvenResource, 1, 2));
		addShapelessRecipe(new ItemStack(AlfheimItems.elvenResource, 1, 1), new ItemStack(AlfheimItems.elvenResource, 1, 5), new ItemStack(AlfheimItems.elvenResource, 1, 5), new ItemStack(AlfheimItems.elvenResource, 1, 5), new ItemStack(AlfheimItems.elvenResource, 1, 5), new ItemStack(AlfheimItems.elvenResource, 1, 5), new ItemStack(AlfheimItems.elvenResource, 1, 5), new ItemStack(AlfheimItems.elvenResource, 1, 5), new ItemStack(AlfheimItems.elvenResource, 1, 5), new ItemStack(AlfheimItems.elvenResource, 1, 5));
		addShapelessRecipe(new ItemStack(AlfheimItems.elvenResource, 1, 2), new ItemStack(AlfheimItems.elvenResource, 1, 6), new ItemStack(AlfheimItems.elvenResource, 1, 6), new ItemStack(AlfheimItems.elvenResource, 1, 6), new ItemStack(AlfheimItems.elvenResource, 1, 6), new ItemStack(AlfheimItems.elvenResource, 1, 6), new ItemStack(AlfheimItems.elvenResource, 1, 6), new ItemStack(AlfheimItems.elvenResource, 1, 6), new ItemStack(AlfheimItems.elvenResource, 1, 6), new ItemStack(AlfheimItems.elvenResource, 1, 6)); 

		addShapelessRecipe(new ItemStack(AlfheimItems.elvenResource, 9, 1), AlfheimBlocks.elvoriumBlock);
		addShapelessRecipe(new ItemStack(AlfheimItems.elvenResource, 9, 2), AlfheimBlocks.mauftriumBlock);
		addShapelessRecipe(new ItemStack(AlfheimItems.elvenResource, 9, 5), new ItemStack(AlfheimItems.elvenResource, 1, 1));
		addShapelessRecipe(new ItemStack(AlfheimItems.elvenResource, 9, 6), new ItemStack(AlfheimItems.elvenResource, 1, 2));
	}
	
	public static void registerSmeltingRecipes() {
		addSmelting(new ItemStack(AlfheimBlocks.elvenOres, 1, 1), new ItemStack(ModItems.manaResource, 1, 7), 1.0F);
		addSmelting(new ItemStack(AlfheimBlocks.elvenOres, 1, 3), new ItemStack(Items.gold_ingot, 1, 0), 1.0F);
	}

	public static void registerManaInfusionRecipies() {
		ManaInfusionRecipies.addRecipe(new ItemStack(ModItems.manaResource, 1, 4), TilePool.MAX_MANA / 2,
			new ItemStack[] {new ItemStack(ModItems.manaResource, 1, 0), new ItemStack(ModItems.manaResource, 1, 1), new ItemStack(ModItems.manaResource, 1, 2)});
		ManaInfusionRecipies.addRecipe(new ItemStack(AlfheimItems.elvenResource, 1, 1), TilePool.MAX_MANA / 2,
			new ItemStack[] {new ItemStack(ModItems.manaResource, 1, 7), new ItemStack(ModItems.manaResource, 1, 8), new ItemStack(ModItems.manaResource, 1, 9)});
		ManaInfusionRecipies.addRecipe(new ItemStack(AlfheimItems.manaStone, 1, 1000) , TilePool.MAX_MANA,
			new ItemStack[] {new ItemStack(AlfheimItems.elvenResource, 4, 9), new ItemStack(ModItems.manaResource, 1, 9)});
		ManaInfusionRecipies.addRecipe(new ItemStack(AlfheimItems.manaStoneGreater, 1, 1000), TilePool.MAX_MANA * 4,
			new ItemStack[] {new ItemStack(AlfheimItems.elvenResource, 2, 7), new ItemStack(AlfheimItems.elvenResource, 2, 8), new ItemStack(AlfheimItems.manaStone, 1, 1000)});
		ManaInfusionRecipies.addRecipe(new ItemStack(AlfheimItems.elvenResource, 1, 2), TilePool.MAX_MANA,
			new ItemStack[] {new ItemStack(AlfheimItems.elvenResource, 1, 4), new ItemStack(AlfheimItems.elvenResource, 1, 5), new ItemStack(ModItems.manaResource, 1, 14)});
	}
	
	public static void registerRecipies() {
		final int costTier1 = 5200;
		final int costTier2 = 8000;
		final int costTier3 = 12000;
		
		BotaniaAPI.registerRuneAltarRecipe(new ItemStack(AlfheimItems.elvenResource, 1, 10), costTier3,
				new Object[] {new ItemStack(ModItems.rune, 1, 0), new ItemStack(ModItems.rune, 1, 1), new ItemStack(ModItems.rune, 1, 2), new ItemStack(ModItems.rune, 1, 3), new ItemStack(ModItems.rune, 1, 8), new ItemStack(ModItems.manaResource, 1, 15), new ItemStack(AlfheimItems.elvenResource, 1, 2)});
		BotaniaAPI.registerRuneAltarRecipe(new ItemStack(AlfheimItems.elvenResource, 1, 11), costTier3,
				new Object[] {new ItemStack(ModItems.rune, 1, 1), new ItemStack(AlfheimItems.elvenResource, 1, 7), new ItemStack(AlfheimItems.elvenResource, 1, 7)});
		BotaniaAPI.registerRuneAltarRecipe(new ItemStack(AlfheimItems.elvenResource, 1, 12), costTier3,
				new Object[] {new ItemStack(ModItems.rune, 1, 0), new ItemStack(AlfheimItems.elvenResource, 1, 8), new ItemStack(AlfheimItems.elvenResource, 1, 8)});
	}
}
