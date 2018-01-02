package alfheim.common.registry;

import static alfheim.common.registry.AlfheimBlocks.*;
import static alfheim.common.registry.AlfheimItems.*;
import static cpw.mods.fml.common.registry.GameRegistry.*;
import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;
import static vazkii.botania.common.block.ModBlocks.*;
import static vazkii.botania.common.item.ModItems.*;

import alfheim.AlfheimCore;
import alfheim.Constants;
import alfheim.client.integration.nei.NEIAlfheimConfig;
import alfheim.common.crafting.HelmRevealingAlfheimRecipe;
import alfheim.common.crafting.IManaInfusionRecipe;
import alfheim.common.crafting.ManaInfusionRecipies;
import alfheim.common.registry.AlfheimItems.ElvenResourcesMetas;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.api.recipe.RecipePureDaisy;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.mana.TilePool;

public class AlfheimRecipes {
	
	public static IRecipe recipeAlfheimPortal;
	public static IRecipe recipeEmentalHelmet;
	public static IRecipe recipeEmentalChestplate;
	public static IRecipe recipeEmentalLeggings;
	public static IRecipe recipeEmentalBoots;
	public static IRecipe recipeElvoriumHelmet;
	public static IRecipe recipeElvoriumChestplate;
	public static IRecipe recipeElvoriumLeggings;
	public static IRecipe recipeElvoriumBoots;
	public static IRecipe recipeElfFirePendant;
	public static IRecipe recipeElfIcePendant;
	public static IRecipe recipeManaInfusionCore;
	public static IRecipe recipeManaInfuser;
	public static IRecipe recipeMuspelheimPowerIngot;
	public static IRecipe recipeNiflheimPowerIngot;

	public static RecipePureDaisy recipeDreamwood;
	public static IRecipe recipeGlowstone;
	public static IRecipe recipeLivingcobble;
	public static IRecipe recipeLivingrockPickaxe;
	
	public static RecipeElvenTrade recipeInterdimensional;
	
	public static IManaInfusionRecipe recipeElvorium;
	public static IManaInfusionRecipe recipeMuspelheimEssence;
	public static IManaInfusionRecipe recipeNiflheimEssence;
	public static IManaInfusionRecipe recipeTerrasteel;
	public static IManaInfusionRecipe recipeMauftrium;
	public static IManaInfusionRecipe recipeManaStone;
	public static IManaInfusionRecipe recipeManaStoneGreater;
	public static RecipeRuneAltar recipeMuspelheimRune;
	public static RecipeRuneAltar recipeNiflheimRune;
	public static RecipeRuneAltar recipeRealityRune;
	public static IRecipe recipeManaElvenRing;
	public static IRecipe recipeManaElvenRingGreater1;
	public static IRecipe recipeManaElvenRingGreater2;
	public static IRecipe recipeMuspelheimRod;
	public static IRecipe recipeNiflheimRod;
	
	public static void init() {
		registerCraftingRecipes();
		registerShapelessRecipes();
		registerSmeltingRecipes();
		registerManaInfusionRecipes();
		registerRecipies();
		if (Constants.DEV) (new NEIAlfheimConfig()).loadConfig();
	}

	public static void registerCraftingRecipes() {
		addShapedRecipe(new ItemStack(alfheimPortal, 1),
		(AlfheimCore.enableElvenStory) ?
			new Object[] {"DPD", "GSG", "DTD",
			'D', dreamwood,
			'G', glowstone_dust,
			'P', new ItemStack(pylon, 1, 2),
			'S', rainbowRod,
			'T', manaInfuser
		} :
			new Object[] {"DPD", "GSG", "DTD",
			'D', dreamwood,
			'G', glowstone_dust,
			'P', new ItemStack(manaResource, 1, 14),	// Gaia Ingot
			'S', rainbowRod,
			'T', terraPlate
		});
		recipeAlfheimPortal = BotaniaAPI.getLatestAddedRecipe();
		
		addShapedRecipe(new ItemStack(elementalHelmet),
			new Object[] {"RTR", "DPD", " M ",
			'R', new ItemStack(rune, 1, 0),
			'T', new ItemStack(manaResource, 1, 13),
			'D', new ItemStack(elvenResource, 1, ElvenResourcesMetas.IffesalDust),
			'P', elementiumHelm,
			'M', new ItemStack(rune, 1, 8),
		});
		recipeEmentalHelmet = BotaniaAPI.getLatestAddedRecipe();
		
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
		recipeEmentalChestplate = BotaniaAPI.getLatestAddedRecipe();
		
		addShapedRecipe(new ItemStack(elementalLeggings),
			new Object[] {"RTR", "DPD", " M ",
			'R', new ItemStack(rune, 1, 1),
			'T', new ItemStack(manaResource, 1, 13),
			'D', new ItemStack(elvenResource, 1, ElvenResourcesMetas.IffesalDust),
			'P', elementiumLegs,
			'M', new ItemStack(rune, 1, 8),
		});
		recipeEmentalLeggings = BotaniaAPI.getLatestAddedRecipe();
		
		addShapedRecipe(new ItemStack(elementalBoots),
			new Object[] {"RTR", "DPD", " M ",
			'R', new ItemStack(rune, 1, 3),
			'T', new ItemStack(manaResource, 1, 13),
			'D', new ItemStack(elvenResource, 1, ElvenResourcesMetas.IffesalDust),
			'P', elementiumBoots,
			'M', new ItemStack(rune, 1, 8),
		});
		recipeEmentalBoots = BotaniaAPI.getLatestAddedRecipe();
		
		addShapedRecipe(new ItemStack(elfFirePendant),
			new Object[] {" N ", "NPN", "RN ",
			'N', new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumNugget),
			'R', new ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimRune),
			'P', lavaPendant
		});
		recipeElfFirePendant = BotaniaAPI.getLatestAddedRecipe();
		
		addShapedRecipe(new ItemStack(elfIcePendant),
			new Object[] {" N ", "NPN", "RN ",
			'N', new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumNugget),
			'R', new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimRune),
			'P', icePendant
		});
		recipeElfIcePendant = BotaniaAPI.getLatestAddedRecipe();
		
		addShapedRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
			new Object[] {"PGP", "GDG", "PGP",
			'D', new ItemStack(manaResource, 1, 8),		// Pixie Dust
			'G', gold_ingot,
			'P', new ItemStack(elvenResource, 1, ElvenResourcesMetas.IffesalDust)
		});
		recipeManaInfusionCore = BotaniaAPI.getLatestAddedRecipe();
		
		addShapedRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimPowerIngot),
			new Object[] {" S ", "SIS", " S ",
			'S', new ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimEssence),
			'I', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot)
		});
		recipeMuspelheimPowerIngot = BotaniaAPI.getLatestAddedRecipe();
		
		addShapedRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimPowerIngot),
			new Object[] {" S ", "SIS", " S ",
			'S', new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimEssence),
			'I', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot)
		});
		recipeNiflheimPowerIngot = BotaniaAPI.getLatestAddedRecipe();
		
		addShapedRecipe(new ItemStack(elvoriumHelmet),
			new Object[] {"TRT", "EPE", "CMC",
			'T', new ItemStack(manaResource, 1, 13),
			'R', new ItemStack(elvenResource, 1, ElvenResourcesMetas.PrimalRune),
			'E', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot),
			'P', terrasteelHelm,
			'C', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
			'M', new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot)
		});
		recipeElvoriumHelmet = BotaniaAPI.getLatestAddedRecipe();
		
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
		recipeElvoriumChestplate = BotaniaAPI.getLatestAddedRecipe();
		
		addShapedRecipe(new ItemStack(elvoriumLeggings),
			new Object[] {"TRT", "EPE", "CMC",
			'T', new ItemStack(manaResource, 1, 13),
			'R', new ItemStack(elvenResource, 1, ElvenResourcesMetas.PrimalRune),
			'E', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot),
			'P', terrasteelLegs,
			'C', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
			'M', new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot)
		});
		recipeElvoriumLeggings = BotaniaAPI.getLatestAddedRecipe();
		
		addShapedRecipe(new ItemStack(elvoriumBoots),
			new Object[] {"TRT", "EPE", "CMC",
			'T', new ItemStack(manaResource, 1, 13),
			'R', new ItemStack(elvenResource, 1, ElvenResourcesMetas.PrimalRune),
			'E', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot),
			'P', terrasteelBoots,
			'C', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
			'M', new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot)
		});
		recipeElvoriumBoots = BotaniaAPI.getLatestAddedRecipe();
		
		addShapedRecipe(new ItemStack(furnace),
			new Object[] {"SSS", "S S", "SSS",
			'S', livingcobble,
		});
		
		// IDK whether this is good source of glowstone or not
		if (AlfheimCore.enableElvenStory) {
			addShapedRecipe(new ItemStack(glowstone_dust),
			
				new Object[] {"LLL", "LDL", "LLL",
				'L', dreamLeaves,
				'D', new ItemStack(manaResource, 1, 9)		// Dragonstone
			});
			recipeGlowstone = BotaniaAPI.getLatestAddedRecipe(); 
		}
		
		addShapedRecipe(new ItemStack(livingrockPickaxe),
			new Object[] {"LLL", " S ", " S ",
			'L', livingcobble,
			'S', stick
		});
		recipeLivingrockPickaxe = BotaniaAPI.getLatestAddedRecipe();
		
		addShapedRecipe(new ItemStack(manaElvenRing),
			new Object[] {"IS ", "S S", " S ",
			'S', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot),
			'I', new ItemStack(manaStone)
		});
		recipeManaElvenRing = BotaniaAPI.getLatestAddedRecipe();
		
		addShapedRecipe(new ItemStack(manaElvenRingGreater),
			new Object[] {"IS ", "S S", " S ",
			'S', new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot),
			'I', new ItemStack(manaStoneGreater)
		});
		recipeManaElvenRingGreater1 = BotaniaAPI.getLatestAddedRecipe();
		
		addShapedRecipe(new ItemStack(manaElvenRingGreater), 
			new Object[] {"SI", "IR",
			'S', manaStoneGreater,
			'R', manaElvenRing,
			'I', new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot)
		});
		recipeManaElvenRingGreater2 = BotaniaAPI.getLatestAddedRecipe();
		
		addShapedRecipe(new ItemStack(manaInfuser),
			new Object[] {"DCD", "IRI", "SSS",
			'C', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
			'D', new ItemStack(manaResource, 1, 9),		// Dragonstone
			'I', new ItemStack(manaResource, 1, 7),		// Elementium
			'R', rainbowRod,							// TODO seriously???
			'S', new ItemStack(livingrock, 1, 4),
		});
		recipeManaInfuser = BotaniaAPI.getLatestAddedRecipe();
		
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
		recipeMuspelheimRod = BotaniaAPI.getLatestAddedRecipe();
		
		addShapedRecipe(new ItemStack(rod, 1, 1),
			new Object[] {" MR", " BM", "B  ",
			'M', new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot),
			'R', new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimRune),
			'B', blaze_rod
		});
		recipeNiflheimRod = BotaniaAPI.getLatestAddedRecipe();
	}

	public static void registerShapelessRecipes() {
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
		recipeLivingcobble = BotaniaAPI.getLatestAddedRecipe(); 
	}
	
	public static void registerSmeltingRecipes() {
		addSmelting(new ItemStack(elvenOres, 1, 1), new ItemStack(manaResource, 1, 7), 1.0F);
		addSmelting(new ItemStack(elvenOres, 1, 3), new ItemStack(gold_ingot, 1, 0), 1.0F);
		addSmelting(elvenSand, new ItemStack(elfGlass), 1.0F);
	}

	public static void registerManaInfusionRecipes() {
		// Why is this here?
		/*ManaInfusionRecipies.addRecipe(new ItemStack(elfGlass), 100,
			new ItemStack[] {new ItemStack(ModItems.quartz, 1, 5), new ItemStack(elvenGlass)});*/
		
		recipeMuspelheimEssence = ManaInfusionRecipies.addRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimEssence), TilePool.MAX_MANA / 10,
			new ItemStack[] {new ItemStack(manaResource, 1, 5), new ItemStack(lava_bucket)});
		recipeNiflheimEssence = ManaInfusionRecipies.addRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimEssence), TilePool.MAX_MANA / 10,
			new ItemStack[] {new ItemStack(manaResource, 1, 5), new ItemStack(ice)});
		recipeTerrasteel = ManaInfusionRecipies.addRecipe(new ItemStack(manaResource, 1, 4), TilePool.MAX_MANA / 2,
			new ItemStack[] {new ItemStack(manaResource, 1, 0), new ItemStack(manaResource, 1, 1), new ItemStack(manaResource, 1, 2)});
		recipeElvorium = ManaInfusionRecipies.addRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot), TilePool.MAX_MANA / 2,
			new ItemStack[] {new ItemStack(manaResource, 1, 7), new ItemStack(manaResource, 1, 8), new ItemStack(manaResource, 1, 9)});
		recipeMauftrium = ManaInfusionRecipies.addRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot), TilePool.MAX_MANA,
			new ItemStack[] {new ItemStack(manaResource, 1, 14), new ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimPowerIngot), new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimPowerIngot)});
		recipeManaStone = ManaInfusionRecipies.addRecipe(new ItemStack(manaStone, 1, 1000) , TilePool.MAX_MANA,
			new ItemStack[] {new ItemStack(elvenResource, 4, ElvenResourcesMetas.IffesalDust), new ItemStack(manaResource, 1, 9)});
		recipeManaStoneGreater = ManaInfusionRecipies.addRecipe(new ItemStack(manaStoneGreater, 1, 1000), TilePool.MAX_MANA * 4,
			new ItemStack[] {new ItemStack(manaStone, 1, 1000), new ItemStack(elvenResource, 2, ElvenResourcesMetas.MuspelheimEssence), new ItemStack(elvenResource, 2, ElvenResourcesMetas.NiflheimEssence)});
	}
	
	public static void registerRecipies() {
		final int costTier1 = 5200;
		final int costTier2 = 8000;
		final int costTier3 = 12000;
		
		recipeRealityRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.PrimalRune), costTier3,
				new Object[] {new ItemStack(rune, 1, 0), new ItemStack(rune, 1, 1), new ItemStack(rune, 1, 2), new ItemStack(rune, 1, 3), new ItemStack(rune, 1, 8), new ItemStack(manaResource, 1, 15), new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot)});
		recipeMuspelheimRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimRune), costTier3,
				new Object[] {new ItemStack(rune, 1, 1), new ItemStack(rune, 1, 2), new ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimEssence), new ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimEssence), new ItemStack(elvenResource, 1, ElvenResourcesMetas.IffesalDust)});
		recipeNiflheimRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimRune), costTier3,
				new Object[] {new ItemStack(rune, 1, 0), new ItemStack(rune, 1, 3), new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimEssence), new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimEssence), new ItemStack(elvenResource, 1, ElvenResourcesMetas.IffesalDust)});
		
		recipeInterdimensional = BotaniaAPI.registerElvenTradeRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.InterdimensionalGatewayCore), new ItemStack(nether_star));
		recipeDreamwood = BotaniaAPI.registerPureDaisyRecipe(dreamlog, dreamwood, 0);
		
		addRecipe(new HelmRevealingAlfheimRecipe());
	}
}
