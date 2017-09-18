package alfheim.common.registry;

import static alfheim.common.registry.AlfheimBlocks.*;
import static alfheim.common.registry.AlfheimItems.*;
import static cpw.mods.fml.common.registry.GameRegistry.*;
import static vazkii.botania.common.block.ModBlocks.*;
import static vazkii.botania.common.item.ModItems.*;

import alfheim.Constants;
import alfheim.client.integration.nei.NEIAlfheimConfig;
import alfheim.common.crafting.HelmRevealingAlfheimRecipe;
import alfheim.common.crafting.ManaInfusionRecipies;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.crafting.recipe.HelmRevealingRecipe;
import vazkii.botania.common.item.ModItems;

public class AlfheimRecipes {
	
	public static void init() {
		registerTempRecipes();
		registerCraftingRecipes();
		registerShapelessRecipes();
		registerSmeltingRecipes();
		registerManaInfusionRecipes();
		registerRecipies();
		if (Constants.DEV) (new NEIAlfheimConfig()).loadConfig();
	}

	/** 
	 * Registers temporal recipes for resources yet unavailable in survival
	 * TODO Remove ASAP!!! */
	private static void registerTempRecipes() {
		addShapedRecipe(new ItemStack(elvenResource, 1, 7),
			new Object[] {"GLG", "LGL", "GLG",
			'G', new ItemStack(manaResource, 1, 5),	// Gaia Spirit
			'L', Items.lava_bucket
		});
		
		addShapedRecipe(new ItemStack(elvenResource, 1, 8),
			new Object[] {"GIG", "IGI", "GIG",
			'G', new ItemStack(manaResource, 1, 5),	// Gaia Spirit
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
			'D', new ItemStack(manaResource, 1, 8),		// Pixie Dust
			'G', Items.gold_ingot,
			'P', new ItemStack(elvenResource, 1, 9)
		});
		
		addShapedRecipe(new ItemStack(manaInfuser),
			new Object[] {"DCD", "IRI", "SSS",
			'C', new ItemStack(elvenResource, 1, 0),
			'D', new ItemStack(manaResource, 1, 9),		// Dragonstone
			'I', new ItemStack(manaResource, 1, 7),		// Elementium
			'R', rainbowRod,							// TODO seriously???
			'S', new ItemStack(ModBlocks.livingrock, 1, 4),
			//'T', ModBlocks.terraPlate
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
		
		addShapedRecipe(new ItemStack(elvoriumHelmet),
			new Object[] {"TRT", "EPE", "CMC",
			'T', new ItemStack(manaResource, 1, 13),
			'R', new ItemStack(elvenResource, 1, 10),
			'E', new ItemStack(elvenResource, 1, 1),
			'P', terrasteelHelm,
			'C', new ItemStack(elvenResource, 1, 0),
			'M', new ItemStack(elvenResource, 1, 2),
		});
		
		addShapedRecipe(new ItemStack(elvoriumHelmetRevealing),
			new Object[] {"TRT", "EPE", "CMC",
			'T', new ItemStack(manaResource, 1, 13),
			'R', new ItemStack(elvenResource, 1, 10),
			'E', new ItemStack(elvenResource, 1, 1),
			'P', terrasteelHelmRevealing,
			'C', new ItemStack(elvenResource, 1, 0),
			'M', new ItemStack(elvenResource, 1, 2),
		});
		
		addShapedRecipe(new ItemStack(elvoriumChestplate),
			new Object[] {"TRT", "EPE", "CMC",
			'T', new ItemStack(manaResource, 1, 13),
			'R', new ItemStack(elvenResource, 1, 10),
			'E', new ItemStack(elvenResource, 1, 1),
			'P', terrasteelChest,
			'C', new ItemStack(elvenResource, 1, 0),
			'M', new ItemStack(elvenResource, 1, 2),
		});
		
		addShapedRecipe(new ItemStack(elvoriumLeggings),
			new Object[] {"TRT", "EPE", "CMC",
			'T', new ItemStack(manaResource, 1, 13),
			'R', new ItemStack(elvenResource, 1, 10),
			'E', new ItemStack(elvenResource, 1, 1),
			'P', terrasteelLegs,
			'C', new ItemStack(elvenResource, 1, 0),
			'M', new ItemStack(elvenResource, 1, 2),
		});
		
		addShapedRecipe(new ItemStack(elvoriumBoots),
			new Object[] {"TRT", "EPE", "CMC",
			'T', new ItemStack(manaResource, 1, 13),
			'R', new ItemStack(elvenResource, 1, 10),
			'E', new ItemStack(elvenResource, 1, 1),
			'P', terrasteelBoots,
			'C', new ItemStack(elvenResource, 1, 0),
			'M', new ItemStack(elvenResource, 1, 2),
		});
		
		addShapedRecipe(new ItemStack(elementalHelmet),
			new Object[] {"RTR", "DPD", " M ",
			'R', new ItemStack(rune, 1, 0),
			'T', new ItemStack(manaResource, 1, 13),
			'D', new ItemStack(elvenResource, 1, 9),
			'P', elementiumHelm,
			'M', new ItemStack(rune, 1, 8),
		});
		
		addShapedRecipe(new ItemStack(elementalHelmetRevealing),
			new Object[] {"RTR", "DPD", " M ",
			'R', new ItemStack(rune, 1, 0),
			'T', new ItemStack(manaResource, 1, 13),
			'D', new ItemStack(elvenResource, 1, 9),
			'P', elementiumHelmRevealing,
			'M', new ItemStack(rune, 1, 8),
		});
		
		addShapedRecipe(new ItemStack(elementalChestplate),
			new Object[] {"RTR", "DPD", " M ",
			'R', new ItemStack(rune, 1, 2),
			'T', new ItemStack(manaResource, 1, 13),
			'D', new ItemStack(elvenResource, 1, 9),
			'P', elementiumChest,
			'M', new ItemStack(rune, 1, 8),
		});
		
		addShapedRecipe(new ItemStack(elementalLeggings),
			new Object[] {"RTR", "DPD", " M ",
			'R', new ItemStack(rune, 1, 1),
			'T', new ItemStack(manaResource, 1, 13),
			'D', new ItemStack(elvenResource, 1, 9),
			'P', elementiumLegs,
			'M', new ItemStack(rune, 1, 8),
		});
		
		addShapedRecipe(new ItemStack(elementalBoots),
			new Object[] {"RTR", "DPD", " M ",
			'R', new ItemStack(rune, 1, 3),
			'T', new ItemStack(manaResource, 1, 13),
			'D', new ItemStack(elvenResource, 1, 9),
			'P', elementiumBoots,
			'M', new ItemStack(rune, 1, 8),
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
			'S', new ItemStack(manaResource, 1, 3)
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

	public static void registerShapelessRecipes() {
		addShapelessRecipe(new ItemStack(ModBlocks.dreamwood), dreamLog);
		
		if (Botania.thaumcraftLoaded) {
			Item goggles = (Item) Item.itemRegistry.getObject("Thaumcraft:ItemGoggles");
			addShapelessRecipe(new ItemStack(elementalHelmetRevealing), new ItemStack(elementalHelmet), goggles);
			addShapelessRecipe(new ItemStack(elvoriumHelmetRevealing), new ItemStack(elvoriumHelmet), goggles);
		}
		addRecipe(new HelmRevealingAlfheimRecipe());
		
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
		addSmelting(new ItemStack(elvenOres, 1, 1), new ItemStack(manaResource, 1, 7), 1.0F);
		addSmelting(new ItemStack(elvenOres, 1, 3), new ItemStack(Items.gold_ingot, 1, 0), 1.0F);
	}

	public static void registerManaInfusionRecipes() {
		ManaInfusionRecipies.addRecipe(new ItemStack(manaResource, 1, 4), TilePool.MAX_MANA / 2,
			new ItemStack[] {new ItemStack(manaResource, 1, 0), new ItemStack(manaResource, 1, 1), new ItemStack(manaResource, 1, 2)});
		ManaInfusionRecipies.addRecipe(new ItemStack(elvenResource, 1, 1), TilePool.MAX_MANA / 2,
			new ItemStack[] {new ItemStack(manaResource, 1, 7), new ItemStack(manaResource, 1, 8), new ItemStack(manaResource, 1, 9)});
		ManaInfusionRecipies.addRecipe(new ItemStack(manaStone, 1, 1000) , TilePool.MAX_MANA,
			new ItemStack[] {new ItemStack(elvenResource, 4, 9), new ItemStack(manaResource, 1, 9)});
		ManaInfusionRecipies.addRecipe(new ItemStack(manaStoneGreater, 1, 1000), TilePool.MAX_MANA * 4,
			new ItemStack[] {new ItemStack(elvenResource, 2, 7), new ItemStack(elvenResource, 2, 8), new ItemStack(manaStone, 1, 1000)});
		ManaInfusionRecipies.addRecipe(new ItemStack(elvenResource, 1, 2), TilePool.MAX_MANA,
			new ItemStack[] {new ItemStack(elvenResource, 1, 3), new ItemStack(elvenResource, 1, 4), new ItemStack(manaResource, 1, 14)});
	}
	
	public static void registerRecipies() {
		final int costTier1 = 5200;
		final int costTier2 = 8000;
		final int costTier3 = 12000;
		
		BotaniaAPI.registerRuneAltarRecipe(new ItemStack(elvenResource, 1, 10), costTier3,
				new Object[] {new ItemStack(rune, 1, 0), new ItemStack(rune, 1, 1), new ItemStack(rune, 1, 2), new ItemStack(rune, 1, 3), new ItemStack(rune, 1, 8), new ItemStack(manaResource, 1, 15), new ItemStack(elvenResource, 1, 2)});
		BotaniaAPI.registerRuneAltarRecipe(new ItemStack(elvenResource, 1, 11), costTier3,
				new Object[] {new ItemStack(rune, 1, 1), new ItemStack(rune, 1, 2), new ItemStack(elvenResource, 1, 7), new ItemStack(elvenResource, 1, 7), new ItemStack(elvenResource, 1, 9)});
		BotaniaAPI.registerRuneAltarRecipe(new ItemStack(elvenResource, 1, 12), costTier3,
				new Object[] {new ItemStack(rune, 1, 0), new ItemStack(rune, 1, 3), new ItemStack(elvenResource, 1, 8), new ItemStack(elvenResource, 1, 8), new ItemStack(elvenResource, 1, 9)});
	}
}
