package alfheim.common.registry;

import static alfheim.common.registry.AlfheimBlocks.*;
import static alfheim.common.registry.AlfheimItems.*;
import static cpw.mods.fml.common.registry.GameRegistry.*;
import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;
import static vazkii.botania.common.block.ModBlocks.*;
import static vazkii.botania.common.item.ModItems.*;

import alfheim.Constants;
import alfheim.client.integration.nei.NEIAlfheimConfig;
import alfheim.common.crafting.HelmRevealingAlfheimRecipe;
import alfheim.common.crafting.ManaInfusionRecipies;
import cpw.mods.fml.common.registry.GameRegistry;
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
		addShapedRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimEssence),
			new Object[] {"GLG", "LGL", "GLG",
			'G', new ItemStack(manaResource, 1, 5),	// Gaia Spirit
			'L', lava_bucket
		});
		
		addShapedRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimEssence),
			new Object[] {"GIG", "IGI", "GIG",
			'G', new ItemStack(manaResource, 1, 5),	// Gaia Spirit
			'I', ice
		});
	}

	public static void registerCraftingRecipes() {
		addShapedRecipe(new ItemStack(alfheimPortal, 2),
			new Object[] {"DPD", "GSG", "DTD",
			'D', dreamwood,
			'G', glowstone_dust,
			'P', new ItemStack(manaResource, 1, 14),	// Gaia Ingot
			'S', rainbowRod,
			'T', ModBlocks.terraPlate
		});
		
		addShapedRecipe(new ItemStack(elementalHelmet),
			new Object[] {"RTR", "DPD", " M ",
			'R', new ItemStack(rune, 1, 0),
			'T', new ItemStack(manaResource, 1, 13),
			'D', new ItemStack(elvenResource, 1, ElvenResourcesMetas.IffesalDust),
			'P', elementiumHelm,
			'M', new ItemStack(rune, 1, 8),
		});
		
		addShapedRecipe(new ItemStack(elementalHelmetRevealing),
			new Object[] {"RTR", "DPD", " M ",
			'R', new ItemStack(rune, 1, 0),
			'T', new ItemStack(manaResource, 1, 13),
			'D', new ItemStack(elvenResource, 1, ElvenResourcesMetas.IffesalDust),
			'P', elementiumHelmRevealing,
			'M', new ItemStack(rune, 1, 8),
		});
		
		addShapedRecipe(new ItemStack(elementalChestplate),
			new Object[] {"RTR", "DPD", " M ",
			'R', new ItemStack(rune, 1, 2),
			'T', new ItemStack(manaResource, 1, 13),
			'D', new ItemStack(elvenResource, 1, ElvenResourcesMetas.IffesalDust),
			'P', elementiumChest,
			'M', new ItemStack(rune, 1, 8),
		});
		
		addShapedRecipe(new ItemStack(elementalLeggings),
			new Object[] {"RTR", "DPD", " M ",
			'R', new ItemStack(rune, 1, 1),
			'T', new ItemStack(manaResource, 1, 13),
			'D', new ItemStack(elvenResource, 1, ElvenResourcesMetas.IffesalDust),
			'P', elementiumLegs,
			'M', new ItemStack(rune, 1, 8),
		});
		
		addShapedRecipe(new ItemStack(elementalBoots),
			new Object[] {"RTR", "DPD", " M ",
			'R', new ItemStack(rune, 1, 3),
			'T', new ItemStack(manaResource, 1, 13),
			'D', new ItemStack(elvenResource, 1, ElvenResourcesMetas.IffesalDust),
			'P', elementiumBoots,
			'M', new ItemStack(rune, 1, 8),
		});
		
		addShapedRecipe(new ItemStack(elfFirePendant),
			new Object[] {" N ", "NPN", "RN ",
			'N', new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumNugget),
			'R', new ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimRune),
			'P', lavaPendant
		});
		
		addShapedRecipe(new ItemStack(elfIcePendant),
			new Object[] {" N ", "NPN", "RN ",
			'N', new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumNugget),
			'R', new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimRune),
			'P', icePendant
		});
		
		addShapedRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
			new Object[] {"PGP", "GDG", "PGP",
			'D', new ItemStack(manaResource, 1, 8),		// Pixie Dust
			'G', gold_ingot,
			'P', new ItemStack(elvenResource, 1, ElvenResourcesMetas.IffesalDust)
		});
		
		addShapedRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimPowerIngot),
			new Object[] {" S ", "SIS", " S ",
			'S', new ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimEssence),
			'I', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot)
		});
		
		addShapedRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimPowerIngot),
			new Object[] {" S ", "SIS", " S ",
			'S', new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimEssence),
			'I', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot)
		});
		
		addShapedRecipe(new ItemStack(elvoriumHelmet),
			new Object[] {"TRT", "EPE", "CMC",
			'T', new ItemStack(manaResource, 1, 13),
			'R', new ItemStack(elvenResource, 1, ElvenResourcesMetas.PrimalRune),
			'E', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot),
			'P', terrasteelHelm,
			'C', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
			'M', new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot)
		});
		
		addShapedRecipe(new ItemStack(elvoriumHelmetRevealing),
			new Object[] {"TRT", "EPE", "CMC",
			'T', new ItemStack(manaResource, 1, 13),
			'R', new ItemStack(elvenResource, 1, ElvenResourcesMetas.PrimalRune),
			'E', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot),
			'P', terrasteelHelmRevealing,
			'C', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
			'M', new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot)
		});
		
		addShapedRecipe(new ItemStack(elvoriumChestplate),
			new Object[] {"TRT", "EPE", "CMC",
			'T', new ItemStack(manaResource, 1, 13),
			'R', new ItemStack(elvenResource, 1, ElvenResourcesMetas.PrimalRune),
			'E', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot),
			'P', terrasteelChest,
			'C', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
			'M', new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot)
		});
		
		addShapedRecipe(new ItemStack(elvoriumLeggings),
			new Object[] {"TRT", "EPE", "CMC",
			'T', new ItemStack(manaResource, 1, 13),
			'R', new ItemStack(elvenResource, 1, ElvenResourcesMetas.PrimalRune),
			'E', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot),
			'P', terrasteelLegs,
			'C', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
			'M', new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot)
		});
		
		addShapedRecipe(new ItemStack(elvoriumBoots),
			new Object[] {"TRT", "EPE", "CMC",
			'T', new ItemStack(manaResource, 1, 13),
			'R', new ItemStack(elvenResource, 1, ElvenResourcesMetas.PrimalRune),
			'E', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot),
			'P', terrasteelBoots,
			'C', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
			'M', new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot)
		});
		
		addShapedRecipe(new ItemStack(furnace),
				new Object[] {"SSS", "S S", "SSS",
				'S', livingcobble,
			});
		
		addShapedRecipe(new ItemStack(livingrockPickaxe),
			new Object[] {"LLL", " S ", " S ",
			'L', livingcobble,
			'S', stick
		});
		
		addShapedRecipe(new ItemStack(manaElvenRing),
			new Object[] {"IS ", "S S", " S ",
			'S', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot),
			'I', new ItemStack(manaStone)
		});
		
		addShapedRecipe(new ItemStack(manaElvenRingGreater),
			new Object[] {"IS ", "S S", " S ",
			'S', new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot),
			'I', new ItemStack(manaStoneGreater)
		});
		
		addShapedRecipe(new ItemStack(manaElvenRingGreater), 
			new Object[] {"SI", "IR",
			'S', manaStoneGreater,
			'R', manaElvenRing,
			'I', new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot)
		});
		
		addShapedRecipe(new ItemStack(manaInfuser),
			new Object[] {"DCD", "IRI", "SSS",
			'C', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
			'D', new ItemStack(manaResource, 1, 9),		// Dragonstone
			'I', new ItemStack(manaResource, 1, 7),		// Elementium
			'R', rainbowRod,							// TODO seriously???
			'S', new ItemStack(ModBlocks.livingrock, 1, 4),
		});
		
		addShapedRecipe(new ItemStack(realitySword),
			new Object[] {" M ", "MRM", " S ",
			'M', new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot),
			'R', new ItemStack(elvenResource, 1, ElvenResourcesMetas.PrimalRune),
			'S', new ItemStack(manaResource, 1, 3)
		});
		
		addShapedRecipe(new ItemStack(rod, 1, 0),
			new Object[] {" MR", " BM", "B  ",
			'M', new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot),
			'R', new ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimRune),
			'B', blaze_rod
		});
		
		addShapedRecipe(new ItemStack(rod, 1, 1),
			new Object[] {" MR", " BM", "B  ",
			'M', new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot),
			'R', new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimRune),
			'B', blaze_rod
		});
	}

	public static void registerShapelessRecipes() {
		addShapelessRecipe(new ItemStack(ModBlocks.dreamwood), dreamLog);
		
		if (Botania.thaumcraftLoaded) {
			Item goggles = (Item) Item.itemRegistry.getObject("Thaumcraft:ItemGoggles");
			addShapelessRecipe(new ItemStack(elementalHelmetRevealing), new ItemStack(elementalHelmet), goggles);
			addShapelessRecipe(new ItemStack(elvoriumHelmetRevealing), new ItemStack(elvoriumHelmet), goggles);
		}

		addShapelessRecipe(new ItemStack(elvenResource, 9, ElvenResourcesMetas.ElvoriumNugget), new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot));
		addShapelessRecipe(new ItemStack(elvenResource, 9, ElvenResourcesMetas.MauftriumNugget), new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot));
		addShapelessRecipe(new ItemStack(elvenResource, 9, ElvenResourcesMetas.ElvoriumIngot), elvoriumBlock);
		addShapelessRecipe(new ItemStack(elvenResource, 9, ElvenResourcesMetas.MauftriumIngot), mauftriumBlock);
		
		addShapelessRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot), new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumNugget), new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumNugget), new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumNugget), new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumNugget), new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumNugget), new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumNugget), new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumNugget), new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumNugget), new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumNugget));
		addShapelessRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot), new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumNugget), new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumNugget), new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumNugget), new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumNugget), new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumNugget), new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumNugget), new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumNugget), new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumNugget), new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumNugget)); 
		addShapelessRecipe(new ItemStack(elvoriumBlock),	 new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot), new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot), new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot), new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot), new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot), new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot), new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot), new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot), new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot));
		addShapelessRecipe(new ItemStack(mauftriumBlock),	 new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot), new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot), new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot), new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot), new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot), new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot), new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot), new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot), new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot));
		
		addShapelessRecipe(new ItemStack(livingcobble), livingrock);
	}
	
	public static void registerSmeltingRecipes() {
		addSmelting(new ItemStack(elvenOres, 1, 1), new ItemStack(manaResource, 1, 7), 1.0F);
		addSmelting(new ItemStack(elvenOres, 1, 3), new ItemStack(gold_ingot, 1, 0), 1.0F);
		addSmelting(elvenSand, new ItemStack(elvenGlass), 1.0F);
	}

	public static void registerManaInfusionRecipes() {
		ManaInfusionRecipies.addRecipe(new ItemStack(elfGlass), 100,
			new ItemStack[] {new ItemStack(ModItems.quartz, 1, 5), new ItemStack(elvenGlass)});
		ManaInfusionRecipies.addRecipe(new ItemStack(manaResource, 1, 4), TilePool.MAX_MANA / 2,
			new ItemStack[] {new ItemStack(manaResource, 1, 0), new ItemStack(manaResource, 1, 1), new ItemStack(manaResource, 1, 2)});
		ManaInfusionRecipies.addRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot), TilePool.MAX_MANA / 2,
			new ItemStack[] {new ItemStack(manaResource, 1, 7), new ItemStack(manaResource, 1, 8), new ItemStack(manaResource, 1, 9)});
		ManaInfusionRecipies.addRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot), TilePool.MAX_MANA,
			new ItemStack[] {new ItemStack(manaResource, 1, 14), new ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimPowerIngot), new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimPowerIngot)});
		ManaInfusionRecipies.addRecipe(new ItemStack(manaStone, 1, 1000) , TilePool.MAX_MANA,
			new ItemStack[] {new ItemStack(elvenResource, 4, ElvenResourcesMetas.IffesalDust), new ItemStack(manaResource, 1, 9)});
		ManaInfusionRecipies.addRecipe(new ItemStack(manaStoneGreater, 1, 1000), TilePool.MAX_MANA * 4,
			new ItemStack[] {new ItemStack(manaStone, 1, 1000), new ItemStack(elvenResource, 2, ElvenResourcesMetas.MuspelheimEssence), new ItemStack(elvenResource, 2, ElvenResourcesMetas.NiflheimEssence)});
	}
	
	public static void registerRecipies() {
		final int costTier1 = 5200;
		final int costTier2 = 8000;
		final int costTier3 = 12000;
		
		BotaniaAPI.registerRuneAltarRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.PrimalRune), costTier3,
				new Object[] {new ItemStack(rune, 1, 0), new ItemStack(rune, 1, 1), new ItemStack(rune, 1, 2), new ItemStack(rune, 1, 3), new ItemStack(rune, 1, 8), new ItemStack(manaResource, 1, 15), new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot)});
		BotaniaAPI.registerRuneAltarRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimRune), costTier3,
				new Object[] {new ItemStack(rune, 1, 1), new ItemStack(rune, 1, 2), new ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimEssence), new ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimEssence), new ItemStack(elvenResource, 1, ElvenResourcesMetas.IffesalDust)});
		BotaniaAPI.registerRuneAltarRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimRune), costTier3,
				new Object[] {new ItemStack(rune, 1, 0), new ItemStack(rune, 1, 3), new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimEssence), new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimEssence), new ItemStack(elvenResource, 1, ElvenResourcesMetas.IffesalDust)});
		
		BotaniaAPI.registerElvenTradeRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.InterdimensionalGatewayCore), new ItemStack(alfheimPortal));
		
		addRecipe(new HelmRevealingAlfheimRecipe());
	}
}
