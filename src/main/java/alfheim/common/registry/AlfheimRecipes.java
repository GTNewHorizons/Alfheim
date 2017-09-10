package alfheim.common.registry;

import static alfheim.common.registry.AlfheimBlocks.*;
import static alfheim.common.registry.AlfheimItems.*;
import static cpw.mods.fml.common.registry.GameRegistry.*;
import static vazkii.botania.common.block.ModBlocks.*;
import static vazkii.botania.common.item.ModItems.*;

import alfheim.common.utils.ManaInfusionRecipies;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.item.ModItems;

public class AlfheimRecipes {
	
	public static void init() {
		registerTempRecipies();
		registerCraftingRecipes();
		registerShapelessRecipies();
		registerSmeltingRecipes();
		registerManaInfusionRecipies();
		registerRecipies();
	}

	private static void registerTempRecipies() {
		addShapedRecipe(new ItemStack(elvenResource, 1, 7),
			new Object[] {"GLG", "LGL", "GLG",
			'G', new ItemStack(manaResource, 1, 14),	// Gaia Ingot
			'L', Items.lava_bucket
		});
		
		addShapedRecipe(new ItemStack(elvenResource, 1, 8),
			new Object[] {"GIG", "IGI", "GIG",
			'G', new ItemStack(manaResource, 1, 14),	// Gaia Ingot
			'I', Blocks.ice
		});
	}

	public static void registerCraftingRecipes() {
		addShapedRecipe(new ItemStack(alfheimPortal),
			new Object[] {"DPD", "GSG", "DPD",
			'D', dreamwood,
			'G', Items.glowstone_dust,
			'P', new ItemStack(manaResource, 1, 14),	// Gaia Ingot
			'S', rainbowRod
		});
		
		addShapedRecipe(new ItemStack(elvenResource, 1, 0),
			new Object[] {"PGP", "GDG", "PGP",
			'D', new ItemStack(manaResource, 1, 8),		// Pisie Dust
			'G', Items.gold_ingot,
			'P', new ItemStack(elvenResource, 1, 9)
		});
		
		addShapedRecipe(new ItemStack(manaInfuser),
			new Object[] {"DCD", "IRI", "STS",
			'C', new ItemStack(elvenResource, 1, 0),
			'D', new ItemStack(manaResource, 1, 9),		// Dragonstone
			'I', new ItemStack(manaResource, 1, 7),		// Elementium
			'R', ModItems.rainbowRod,
			'S', new ItemStack(ModBlocks.livingrock, 1, 4),
			'T', ModBlocks.terraPlate
		});
		
		addShapedRecipe(new ItemStack(elvenResource, 1, 3),
			new Object[] {" S ", "SIS", " S ",
			'S', new ItemStack(elvenResource, 1, 7),
			'I', new ItemStack(elvenResource, 1, 1)
		});
		
		addShapedRecipe(new ItemStack(elvenResource, 1, 4),
			new Object[] {" S ", "SIS", " S ",
			'S', new ItemStack(elvenResource, 1, 8),
			'I', new ItemStack(elvenResource, 1, 1)
		});
		
		addShapedRecipe(new ItemStack(manaElvenRing),
			new Object[] {"IS ", "S S", " S ",
			'S', new ItemStack(elvenResource, 1, 1),
			'I', new ItemStack(manaStone)
		});
		
		addShapedRecipe(new ItemStack(manaElvenRingGreater),
			new Object[] {"IS ", "S S", " S ",
			'S', new ItemStack(elvenResource, 1, 2),
			'I', new ItemStack(manaStoneGreater)
		});
		
		addShapedRecipe(new ItemStack(manaElvenRingGreater), 
			new Object[] {"SI", "IR",
			'S', manaStoneGreater,
			'R', manaElvenRing,
			'I', new ItemStack(elvenResource, 1, 2)
		});
		
		addShapedRecipe(new ItemStack(realitySword),
			new Object[] {" M ", "MRM", " S ",
			'M', new ItemStack(elvenResource, 1, 2),
			'R', new ItemStack(elvenResource, 1, 10),
			'S', new ItemStack(ModItems.manaResource, 1, 3)
		});
		
		addShapedRecipe(new ItemStack(elfFirePendant),
			new Object[] {" N ", "NPN", "RN ",
			'N', new ItemStack(elvenResource, 1, 6),
			'R', new ItemStack(elvenResource, 1, 11),
			'P', lavaPendant
		});
		
		addShapedRecipe(new ItemStack(elfIcePendant),
			new Object[] {" N ", "NPN", "RN ",
			'N', new ItemStack(elvenResource, 1, 6),
			'R', new ItemStack(elvenResource, 1, 12),
			'P', icePendant
		});
		
		addShapedRecipe(new ItemStack(rod, 1, 0),
			new Object[] {" MR", " BM", "B  ",
			'M', new ItemStack(elvenResource, 1, 2),
			'R', new ItemStack(elvenResource, 1, 11),
			'B', Items.blaze_rod
		});
		
		addShapedRecipe(new ItemStack(rod, 1, 1),
			new Object[] {" MR", " BM", "B  ",
			'M', new ItemStack(elvenResource, 1, 2),
			'R', new ItemStack(elvenResource, 1, 12),
			'B', Items.blaze_rod
		});
	}

	public static void registerShapelessRecipies() {
		addShapelessRecipe(new ItemStack(ModBlocks.dreamwood), dreamLog);
		
		addShapelessRecipe(new ItemStack(elvoriumBlock),	 new ItemStack(elvenResource, 1, 1), new ItemStack(elvenResource, 1, 1), new ItemStack(elvenResource, 1, 1), new ItemStack(elvenResource, 1, 1), new ItemStack(elvenResource, 1, 1), new ItemStack(elvenResource, 1, 1), new ItemStack(elvenResource, 1, 1), new ItemStack(elvenResource, 1, 1), new ItemStack(elvenResource, 1, 1));
		addShapelessRecipe(new ItemStack(mauftriumBlock),	 new ItemStack(elvenResource, 1, 2), new ItemStack(elvenResource, 1, 2), new ItemStack(elvenResource, 1, 2), new ItemStack(elvenResource, 1, 2), new ItemStack(elvenResource, 1, 2), new ItemStack(elvenResource, 1, 2), new ItemStack(elvenResource, 1, 2), new ItemStack(elvenResource, 1, 2), new ItemStack(elvenResource, 1, 2));
		addShapelessRecipe(new ItemStack(elvenResource, 1, 1), new ItemStack(elvenResource, 1, 5), new ItemStack(elvenResource, 1, 5), new ItemStack(elvenResource, 1, 5), new ItemStack(elvenResource, 1, 5), new ItemStack(elvenResource, 1, 5), new ItemStack(elvenResource, 1, 5), new ItemStack(elvenResource, 1, 5), new ItemStack(elvenResource, 1, 5), new ItemStack(elvenResource, 1, 5));
		addShapelessRecipe(new ItemStack(elvenResource, 1, 2), new ItemStack(elvenResource, 1, 6), new ItemStack(elvenResource, 1, 6), new ItemStack(elvenResource, 1, 6), new ItemStack(elvenResource, 1, 6), new ItemStack(elvenResource, 1, 6), new ItemStack(elvenResource, 1, 6), new ItemStack(elvenResource, 1, 6), new ItemStack(elvenResource, 1, 6), new ItemStack(elvenResource, 1, 6)); 

		addShapelessRecipe(new ItemStack(elvenResource, 9, 1), elvoriumBlock);
		addShapelessRecipe(new ItemStack(elvenResource, 9, 2), mauftriumBlock);
		addShapelessRecipe(new ItemStack(elvenResource, 9, 5), new ItemStack(elvenResource, 1, 1));
		addShapelessRecipe(new ItemStack(elvenResource, 9, 6), new ItemStack(elvenResource, 1, 2));
	}
	
	public static void registerSmeltingRecipes() {
		addSmelting(new ItemStack(elvenOres, 1, 1), new ItemStack(ModItems.manaResource, 1, 7), 1.0F);
		addSmelting(new ItemStack(elvenOres, 1, 3), new ItemStack(Items.gold_ingot, 1, 0), 1.0F);
	}

	public static void registerManaInfusionRecipies() {
		ManaInfusionRecipies.addRecipe(new ItemStack(ModItems.manaResource, 1, 4), TilePool.MAX_MANA / 2,
			new ItemStack[] {new ItemStack(ModItems.manaResource, 1, 0), new ItemStack(ModItems.manaResource, 1, 1), new ItemStack(ModItems.manaResource, 1, 2)});
		ManaInfusionRecipies.addRecipe(new ItemStack(elvenResource, 1, 1), TilePool.MAX_MANA / 2,
			new ItemStack[] {new ItemStack(ModItems.manaResource, 1, 7), new ItemStack(ModItems.manaResource, 1, 8), new ItemStack(ModItems.manaResource, 1, 9)});
		ManaInfusionRecipies.addRecipe(new ItemStack(manaStone, 1, 1000) , TilePool.MAX_MANA,
			new ItemStack[] {new ItemStack(elvenResource, 4, 9), new ItemStack(ModItems.manaResource, 1, 9)});
		ManaInfusionRecipies.addRecipe(new ItemStack(manaStoneGreater, 1, 1000), TilePool.MAX_MANA * 4,
			new ItemStack[] {new ItemStack(elvenResource, 2, 7), new ItemStack(elvenResource, 2, 8), new ItemStack(manaStone, 1, 1000)});
		ManaInfusionRecipies.addRecipe(new ItemStack(elvenResource, 1, 2), TilePool.MAX_MANA,
			new ItemStack[] {new ItemStack(elvenResource, 1, 4), new ItemStack(elvenResource, 1, 5), new ItemStack(ModItems.manaResource, 1, 14)});
	}
	
	public static void registerRecipies() {
		final int costTier1 = 5200;
		final int costTier2 = 8000;
		final int costTier3 = 12000;
		
		BotaniaAPI.registerRuneAltarRecipe(new ItemStack(elvenResource, 1, 10), costTier3,
				new Object[] {new ItemStack(ModItems.rune, 1, 0), new ItemStack(ModItems.rune, 1, 1), new ItemStack(ModItems.rune, 1, 2), new ItemStack(ModItems.rune, 1, 3), new ItemStack(ModItems.rune, 1, 8), new ItemStack(ModItems.manaResource, 1, 15), new ItemStack(elvenResource, 1, 2)});
		BotaniaAPI.registerRuneAltarRecipe(new ItemStack(elvenResource, 1, 11), costTier3,
				new Object[] {new ItemStack(ModItems.rune, 1, 1), new ItemStack(elvenResource, 1, 7), new ItemStack(elvenResource, 1, 7)});
		BotaniaAPI.registerRuneAltarRecipe(new ItemStack(elvenResource, 1, 12), costTier3,
				new Object[] {new ItemStack(ModItems.rune, 1, 0), new ItemStack(elvenResource, 1, 8), new ItemStack(elvenResource, 1, 8)});
	}
}
