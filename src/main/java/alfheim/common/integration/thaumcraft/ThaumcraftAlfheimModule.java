package alfheim.common.integration.thaumcraft;

import static alexsocol.asjlib.ASJUtilities.*;
import static alfheim.api.lib.LibOreDict.*;
import static cpw.mods.fml.client.registry.RenderingRegistry.*;
import static cpw.mods.fml.common.registry.GameRegistry.*;
import static net.minecraftforge.oredict.OreDictionary.*;
import static thaumcraft.api.ThaumcraftApi.*;
import static thaumcraft.common.lib.utils.Utils.*;
import static vazkii.botania.common.lib.LibOreDict.*;

import alexsocol.asjlib.ASJReflectionHelper;
import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.client.render.block.RenderBlockAlfheimThaumOre;
import alfheim.common.block.compat.thaumcraft.BlockAlfheimThaumOre;
import alfheim.common.core.asm.AlfheimASMData;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.item.compat.thaumcraft.ItemAlfheimWandCap;
import alfheim.common.item.compat.thaumcraft.ItemAlfheimWandRod;
import alfheim.common.item.compat.thaumcraft.NaturalWandRodOnUpdate;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.api.wands.StaffRod;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;
import thaumcraft.common.blocks.BlockCustomOreItem;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.config.ConfigResearch;
import vazkii.botania.common.item.ModItems;

public class ThaumcraftAlfheimModule {
	
	public static Block alfheimThaumOre;
	
	public static Item naturalWandCap;
	public static Item naturalWandRod;
	
	public static IRecipe recipeElementiumWandCap;
	
	public static int renderIDOre = -1;
	
	public static void preInit() {
		constructBlocks();
		constructItems();
		registerBlocks();
		registerItems();
		if (!ASJUtilities.isServer()) registerRenders(); 
	}
	
	public static void constructBlocks() {
		alfheimThaumOre = new BlockAlfheimThaumOre();
	}
	
	public static void constructItems() {
		naturalWandCap = new ItemAlfheimWandCap();
		naturalWandRod = new ItemAlfheimWandRod();
	
		new WandCap(capManasteelName,	0.95F, new ItemStack(naturalWandCap, 1, 0), 5	);
		new WandCap(capTerrasteelName,	0.85F, new ItemStack(naturalWandCap, 1, 1), 8	);
		new WandCap(capElementiumName,	0.95F, new ItemStack(naturalWandCap, 1, 2), 5	);
		new WandCap(capElvoriumName,	0.85F, new ItemStack(naturalWandCap, 1, 3), 8	);
		new WandCap(capMauftriumName,	0.75F, new ItemStack(naturalWandCap, 1, 4), 11	);
		
		new WandRod(rodLivingwoodName,	35, new ItemStack(naturalWandRod, 1, 0), 2	);
		new WandRod(rodDreamwoodName,	65, new ItemStack(naturalWandRod, 1, 1), 5	);
		new StaffRod(rodSpiritualName,	85, new ItemStack(naturalWandRod, 1, 2), 12	, new NaturalWandRodOnUpdate()).setGlowing(true);
	}
	
	public static void registerBlocks() {
		registerBlock(alfheimThaumOre, BlockCustomOreItem.class, "AlfheimThaumOre");
	}
	
	public static void registerItems() {
		register(naturalWandCap);
		register(naturalWandRod);
	}
	
	public static void registerRenders() {
		renderIDOre = getNextAvailableRenderId();
		registerBlockHandler(new RenderBlockAlfheimThaumOre());
	}
	
	public static void postInit() {
		registerRecipes();
		reigsterResearches();
		registerOreDict();
		
		addSpecialMiningResult(new ItemStack(alfheimThaumOre, 1, 0), new ItemStack(ConfigItems.itemNugget, 1, 21), 0.9F);
		addSpecialMiningResult(new ItemStack(AlfheimBlocks.elvenOres, 1, 1), new ItemStack(ConfigItems.itemNugget, 1, AlfheimASMData.elementiumClusterMeta()), 1.0F);
	}
	
	public static void registerRecipes() {
		ConfigResearch.recipes.put(capManasteelRecipe,
			addArcaneCraftingRecipe(capManasteelResearch,
				new ItemStack(naturalWandCap, 1, 0),
				new AspectList()
				.add(Aspect.AIR,	WandCap.caps.get(capManasteelName).getCraftCost())
				.add(Aspect.FIRE,	WandCap.caps.get(capManasteelName).getCraftCost())
				.add(Aspect.ORDER,	WandCap.caps.get(capManasteelName).getCraftCost()),
				"NNN", "N N",
				'N', MANASTEEL_NUGGET
			)
		);
		
		ConfigResearch.recipes.put(capTerrasteelRecipe,
			addArcaneCraftingRecipe(capTerrasteelResearch,
				new ItemStack(naturalWandCap, 1, 1),
				new AspectList()
				.add(Aspect.AIR,	WandCap.caps.get(capTerrasteelName).getCraftCost())
				.add(Aspect.FIRE,	WandCap.caps.get(capTerrasteelName).getCraftCost())
				.add(Aspect.ORDER,	WandCap.caps.get(capTerrasteelName).getCraftCost()),
				"NNN", "N N",
				'N', TERRASTEEL_NUGGET
			)
		);
		
		ConfigResearch.recipes.put(capElementiumRecipe,
			addArcaneCraftingRecipe(capElementiumResearch,
				new ItemStack(naturalWandCap, 1, 2),
				new AspectList()
				.add(Aspect.AIR,	WandCap.caps.get(capElementiumName).getCraftCost())
				.add(Aspect.FIRE,	WandCap.caps.get(capElementiumName).getCraftCost())
				.add(Aspect.ORDER,	WandCap.caps.get(capElementiumName).getCraftCost()),
				"NNN", "N N",
				'N', ELEMENTIUM_NUGGET
			)
		);
		
		ConfigResearch.recipes.put(capElvoriumRecipe,
			addArcaneCraftingRecipe(capElvoriumResearch,
				new ItemStack(naturalWandCap, 1, 3),
				new AspectList()
				.add(Aspect.AIR,	WandCap.caps.get(capElvoriumName).getCraftCost())
				.add(Aspect.FIRE,	WandCap.caps.get(capElvoriumName).getCraftCost())
				.add(Aspect.ORDER,	WandCap.caps.get(capElvoriumName).getCraftCost()),
				"NNN", "N N",
				'N', ELVORIUM_NUGGET
			)
		);
		
		ConfigResearch.recipes.put(capMauftriumRecipe,
			addArcaneCraftingRecipe(capMauftriumResearch,
				new ItemStack(naturalWandCap, 1, 4),
				new AspectList()
				.add(Aspect.AIR,	WandCap.caps.get(capMauftriumName).getCraftCost())
				.add(Aspect.FIRE,	WandCap.caps.get(capMauftriumName).getCraftCost())
				.add(Aspect.WATER,	WandCap.caps.get(capMauftriumName).getCraftCost())
				.add(Aspect.EARTH,	WandCap.caps.get(capMauftriumName).getCraftCost())
				.add(Aspect.ORDER,	WandCap.caps.get(capMauftriumName).getCraftCost())
				.add(Aspect.ENTROPY, WandCap.caps.get(capMauftriumName).getCraftCost()),
				"NNN", "N N",
				'N', MAUFTRIUM_NUGGET
			)
		);
		
		ConfigResearch.recipes.put(rodLivingwoodRecipe,
			addArcaneCraftingRecipe(rodLivingwoodResearch,
				new ItemStack(naturalWandRod, 1, 0),
				new AspectList()
				.add(Aspect.AIR,	WandRod.rods.get(rodLivingwoodName).getCraftCost())
				.add(Aspect.EARTH,	WandRod.rods.get(rodLivingwoodName).getCraftCost()),
				"  T", " T ", "T  ",
				'T', LIVINGWOOD_TWIG
			)
		);
		
		ConfigResearch.recipes.put(rodDreamwoodRecipe,
			addArcaneCraftingRecipe(rodDreamwoodResearch,
				new ItemStack(naturalWandRod, 1, 1),
				new AspectList()
				.add(Aspect.AIR,	WandRod.rods.get(rodDreamwoodName).getCraftCost())
				.add(Aspect.EARTH,	WandRod.rods.get(rodDreamwoodName).getCraftCost())
				.add(Aspect.ORDER,	WandRod.rods.get(rodDreamwoodName).getCraftCost()),
				"  I", " I ", "I  ",
				'I', INFUSED_DREAM_TWIG
			)
		);
		
		ConfigResearch.recipes.put(rodSpiritualRecipe,
			addArcaneCraftingRecipe("",
				new ItemStack(naturalWandRod, 1, 2),
				new AspectList()
				.add(Aspect.AIR,	WandRod.rods.get(rodSpiritualStaff).getCraftCost())
				.add(Aspect.FIRE,	WandRod.rods.get(rodSpiritualStaff).getCraftCost())
				.add(Aspect.WATER,	WandRod.rods.get(rodSpiritualStaff).getCraftCost())
				.add(Aspect.EARTH,	WandRod.rods.get(rodSpiritualStaff).getCraftCost())
				.add(Aspect.ORDER,	WandRod.rods.get(rodSpiritualStaff).getCraftCost())
				.add(Aspect.ENTROPY, WandRod.rods.get(rodSpiritualStaff).getCraftCost()),
				"DSP", " RS", "R D",
				'R', new ItemStack(naturalWandRod, 1, 1),
				'S', LIFE_ESSENCE,
				'D', DRAGONSTONE,
				'P', new ItemStack(ConfigItems.itemResource, 1, 15)
			)
		);
		
		ConfigResearch.recipes.put(pureElementiumRecipe,
			addCrucibleRecipe(pureElementiumResearch,
				new ItemStack(ConfigItems.itemNugget, 1, AlfheimASMData.elementiumClusterMeta()),
				ELEMENTIUM_ORE,
				new AspectList()
				.merge(Aspect.METAL, 1)
				.merge(Aspect.ORDER, 1)
			)
		);
		
		ConfigResearch.recipes.put(transElementiumRecipe,
			addCrucibleRecipe(transElementiumResearch,
				new ItemStack(ModItems.manaResource, 3, 19),
				ELEMENTIUM_NUGGET,
				new AspectList()
				.merge(Aspect.METAL, 2)
				.merge(Aspect.MAGIC, 2)
			)
		);
		
		recipeElementiumWandCap = new ShapedOreRecipe(new ItemStack(naturalWandCap, 1, 2),
			"NNN", "NIN",
			'N', ELEMENTIUM_NUGGET,
			'I', IFFESAL_DUST);
		
		if (AlfheimCore.enableElvenStory) addESMRecipes();
		
		addSmelting(new ItemStack(alfheimThaumOre, 1, 0),
			new ItemStack(ConfigItems.itemResource, 1, 3),	// Cinnabar
			1.0F
		);
		
		addSmelting(new ItemStack(alfheimThaumOre, 1, 7),
			new ItemStack(ConfigItems.itemResource, 1, 6),	// Amber
			1.0F
		);
		
		addSmelting(new ItemStack(ConfigItems.itemNugget, 1, AlfheimASMData.elementiumClusterMeta()),
			new ItemStack(ModItems.manaResource, 2, 7),		// Elementium
			1.0F
		);
		
		addSmeltingBonus(ELEMENTIUM_ORE,
			new ItemStack(ModItems.manaResource, 0, 19)		// from ore
		);
		
		addSmeltingBonus(new ItemStack(ConfigItems.itemNugget, 1, AlfheimASMData.elementiumClusterMeta()),
			new ItemStack(ModItems.manaResource, 0, 19)		// from cluster
		);
	}
	
	public static void addESMRecipes() {
		CraftingManager.getInstance().getRecipeList().add(recipeElementiumWandCap);
		
		WandCap cap = WandCap.caps.get(capElementiumName);
		ASJReflectionHelper.setValue(cap, 0.95F, "baseCostModifier");
		cap.setCraftCost(5);
		
		cap = WandCap.caps.get(capElvoriumName);
		ASJReflectionHelper.setValue(cap, 0.85F, "baseCostModifier");
		cap.setCraftCost(8);
	}
	
	public static void removeESMRecipes() {
		CraftingManager.getInstance().getRecipeList().remove(recipeElementiumWandCap);
		
		WandCap cap = WandCap.caps.get(capElementiumName);
		ASJReflectionHelper.setValue(cap, 0.9F, "baseCostModifier");
		cap.setCraftCost(6);
		
		cap = WandCap.caps.get(capElvoriumName);
		ASJReflectionHelper.setValue(cap, 0.8F, "baseCostModifier");
		cap.setCraftCost(9);
	}
	
	public static void reigsterResearches() {
		new ResearchItem(capManasteelResearch, "THAUMATURGY",
			new AspectList().add(Aspect.METAL, 3).add(Aspect.EXCHANGE, 3).add(Aspect.TOOL, 3),
			4, 0, 1,
			new ItemStack(naturalWandCap, 1, 0))
			
			.setPages(	new ResearchPage("tc.research_page." + capManasteelResearch + ".1"),
						new ResearchPage((IArcaneRecipe) ConfigResearch.recipes.get(capManasteelRecipe)))
			
			.setParents("CAP_gold").registerResearchItem();
		
		
		
		new ResearchItem(capTerrasteelResearch, "THAUMATURGY",
			new AspectList().add(Aspect.METAL, 6).add(Aspect.MAGIC, 6).add(Aspect.TOOL, 3).add(Aspect.AURA, 3),
			7, 4, 2,
			new ItemStack(naturalWandCap, 1, 1))
			
			.setPages(	new ResearchPage("tc.research_page." + capTerrasteelResearch + ".1"),
						new ResearchPage((IArcaneRecipe) ConfigResearch.recipes.get(capTerrasteelRecipe)))
			
			.setParents("CAP_thaumium").registerResearchItem();
		
		
		
		new ResearchItem(capElementiumResearch, "THAUMATURGY",
			new AspectList().add(Aspect.METAL, 3).add(Aspect.EXCHANGE, 3).add(Aspect.TOOL, 3),
			6, 2, 1,
			new ItemStack(naturalWandCap, 1, 2))
			
			.setPages(	new ResearchPage("tc.research_page." + capElementiumResearch + ".1"),
						new ResearchPage((IArcaneRecipe) ConfigResearch.recipes.get(capElementiumRecipe)))
			
			.setParents("CAP_gold").registerResearchItem();
		
		
		
		new ResearchItem(capElvoriumResearch, "THAUMATURGY",
			new AspectList().add(Aspect.METAL, 6).add(Aspect.MAGIC, 6).add(Aspect.TOOL, 3).add(Aspect.AURA, 3),
			5, 6, 2,
			new ItemStack(naturalWandCap, 1, 3))
			
			.setPages(	new ResearchPage("tc.research_page." + capElvoriumResearch + ".1"),
						new ResearchPage((IArcaneRecipe) ConfigResearch.recipes.get(capElvoriumRecipe)))
			
			.setParents("CAP_thaumium").registerResearchItem();
		
		
		
		new ResearchItem(capMauftriumResearch, "THAUMATURGY",
			new AspectList().add(Aspect.VOID, 5).add(Aspect.ELDRITCH, 5).add(Aspect.TOOL, 3).add(Aspect.MAGIC, 3).add(Aspect.AURA, 3),
			7, 6, 3,
			new ItemStack(naturalWandCap, 1, 4))
			
			.setPages(	new ResearchPage("tc.research_page." + capMauftriumResearch + ".1"),
						new ResearchPage((IArcaneRecipe) ConfigResearch.recipes.get(capMauftriumRecipe)))
			
			.setParents("CAP_void").registerResearchItem();
		
		new ResearchItem(rodLivingwoodResearch, "THAUMATURGY",
				new AspectList().add(Aspect.TOOL, 3).add(Aspect.TREE, 6).add(Aspect.MAGIC, 3),
				-2, 2, 1,
				new ItemStack(naturalWandRod, 1, 0))
				
				.setPages(	new ResearchPage("tc.research_page." + rodLivingwoodResearch + ".1"),
							new ResearchPage((IArcaneRecipe) ConfigResearch.recipes.get(rodLivingwoodRecipe)))
				
				.setParents("ROD_greatwood").registerResearchItem();
		
		
		
		new ResearchItem(rodDreamwoodResearch, "THAUMATURGY",
				new AspectList().add(Aspect.TOOL, 4).add(Aspect.TREE, 6).add(Aspect.MAGIC, 5),
				-4, 4, 2,
				new ItemStack(naturalWandRod, 1, 1))
				
				.setPages(	new ResearchPage("tc.research_page." + rodDreamwoodResearch + ".1"),
							new ResearchPage((IArcaneRecipe) ConfigResearch.recipes.get(rodDreamwoodRecipe)))
				
				.setParents("ROD_greatwood").registerResearchItem();
		
		
		
		new ResearchItem(rodSpiritualResearch, "THAUMATURGY",
				new AspectList().add(Aspect.TOOL, 6).add(Aspect.TREE, 6).add(Aspect.MAGIC, 12),
				-3, 6, 2,
				new ItemStack(naturalWandRod, 1, 2))
				
				.setPages(	new ResearchPage("tc.research_page." + rodSpiritualResearch + ".1"),
							new ResearchPage((IArcaneRecipe) ConfigResearch.recipes.get(rodSpiritualRecipe)))
				
				.setParents("ROD_silverwood").registerResearchItem();
		
		
		
		new ResearchItem(pureElementiumResearch, "ALCHEMY",
			new AspectList().add(Aspect.METAL, 3).add(Aspect.ORDER, 2).add(Aspect.MAGIC, 1),
			-3, 2, 1, new ItemStack(ConfigItems.itemNugget, 1, AlfheimASMData.elementiumClusterMeta()))
		
			.setPages(	new ResearchPage("tc.research_page." + pureElementiumResearch + ".1"),
						new ResearchPage((CrucibleRecipe) ConfigResearch.recipes.get(pureElementiumRecipe)))
			
			.setConcealed().setSecondary().setParents("PUREIRON").registerResearchItem();
		
		
		
		new ResearchItem(transElementiumResearch, "ALCHEMY",
			new AspectList().add(Aspect.METAL, 3).add(Aspect.EXCHANGE, 3),
			1, 2, 1, new ItemStack(ModItems.manaResource, 1, 19))
			
			.setPages(	new ResearchPage("tc.research_page." + transElementiumResearch + ".1"),
						new ResearchPage((CrucibleRecipe) ConfigResearch.recipes.get(transElementiumRecipe)))
			
			.setConcealed().setSecondary().setParents("TRANSIRON").registerResearchItem();
		
		addWarpToResearch(capMauftriumResearch, 2);
	}
	
	public static void registerOreDict() {
		registerOre("oreCinnabar", new ItemStack(alfheimThaumOre, 1, 0));
		registerOre("oreInfusedAir", new ItemStack(alfheimThaumOre, 1, 1));
		registerOre("oreInfusedFire", new ItemStack(alfheimThaumOre, 1, 2));
		registerOre("oreInfusedWater", new ItemStack(alfheimThaumOre, 1, 3));
		registerOre("oreInfusedEarth", new ItemStack(alfheimThaumOre, 1, 4));
		registerOre("oreInfusedOrder", new ItemStack(alfheimThaumOre, 1, 5));
		registerOre("oreInfusedEntropy", new ItemStack(alfheimThaumOre, 1, 6));
		registerOre("oreAmber", new ItemStack(alfheimThaumOre, 1, 7));
	}
	
	public static final CreativeTabs tcnTab = new CreativeTabs("NTC") {
		@Override
		public Item getTabIconItem() {
			return naturalWandCap;
		}
		
		public int func_151243_f() {
			return 1;
		}
	}.setNoTitle().setBackgroundImageName("NTC.png");
	
	public static final String
								capManasteelName		= ModInfo.MODID + "Manasteel",
								capManasteelRecipe		= ModInfo.MODID + "WandCapManasteel",
								capManasteelResearch	= "CAP_" + capManasteelName,
								
								capTerrasteelName		= ModInfo.MODID + "Terrasteel",
								capTerrasteelRecipe		= ModInfo.MODID + "WandCapTerrasteel",
								capTerrasteelResearch	= "CAP_" + capTerrasteelName,
								
								capElementiumName		= ModInfo.MODID + "Elementium",
								capElementiumRecipe		= ModInfo.MODID + "WandCapElementium",
								capElementiumResearch	= "CAP_" + capElementiumName,
								
								capElvoriumName			= ModInfo.MODID + "Elvorium",
								capElvoriumRecipe		= ModInfo.MODID + "WandCapElvorium",
								capElvoriumResearch		= "CAP_" + capElvoriumName,
								
								capMauftriumName		= ModInfo.MODID + "Mauftrium",
								capMauftriumRecipe		= ModInfo.MODID + "WandCapMauftrium",
								capMauftriumResearch	= "CAP_" + capMauftriumName,
								
								rodLivingwoodName		= ModInfo.MODID + "Livingwood",
								rodLivingwoodRecipe		= ModInfo.MODID + "WandRodLivingwood",
								rodLivingwoodResearch	= "ROD_" + rodLivingwoodName,
										
								rodDreamwoodName		= ModInfo.MODID + "Dreamwood",
								rodDreamwoodRecipe		= ModInfo.MODID + "WandRodDreamwood",
								rodDreamwoodResearch	= "ROD_" + rodDreamwoodName,
										
								rodSpiritualName		= ModInfo.MODID + "Spiritual",
								rodSpiritualStaff		= rodSpiritualName + "_staff",
								rodSpiritualRecipe		= ModInfo.MODID + "WandRodSpiritual",
								rodSpiritualResearch	= "ROD_" + rodSpiritualStaff,
								
								pureElementiumRecipe	= ModInfo.MODID + "PUREELEMENTIUM",
								pureElementiumResearch	= ModInfo.MODID + "PureElementium",
								
								transElementiumRecipe	= ModInfo.MODID + "TRANSELEMENTIUM",
								transElementiumResearch	= ModInfo.MODID + "TransElementium";
}
