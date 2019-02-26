package alfheim.common.integration.thaumcraft;

import static alexsocol.asjlib.ASJUtilities.*;
import static alfheim.api.lib.LibOreDict.*;
import static cpw.mods.fml.client.registry.RenderingRegistry.*;
import static cpw.mods.fml.common.registry.GameRegistry.*;
import static net.minecraftforge.oredict.OreDictionary.*;
import static thaumcraft.api.ThaumcraftApi.*;
import static thaumcraft.common.lib.utils.Utils.*;
import static vazkii.botania.common.lib.LibOreDict.*;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.client.render.block.RenderBlockAlfheimThaumOre;
import alfheim.common.block.compat.thaumcraft.BlockAlfheimThaumOre;
import alfheim.common.core.asm.AlfheimASMData;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.core.registry.AlfheimItems;
import alfheim.common.core.registry.AlfheimItems.ElvenResourcesMetas;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.item.compat.thaumcraft.ItemAlfheimWandCap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
import thaumcraft.api.wands.WandCap;
import thaumcraft.common.blocks.BlockCustomOreItem;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.config.ConfigResearch;
import thaumcraft.common.items.equipment.ItemElementalPickaxe;
import vazkii.botania.common.item.ModItems;

public class ThaumcraftAlfheimModule {
	
	public static Block alfheimThaumOre;
	
	public static Item naturalWandCap;
	
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
	
		new WandCap("manasteel", 1, new ItemStack(naturalWandCap, 1, 0), 3);
		new WandCap("terrasteel", 0.85F, new ItemStack(naturalWandCap, 1, 1), 7);
		new WandCap("elementium", 1, new ItemStack(naturalWandCap, 1, 2), 3);
		new WandCap("elvorium", 0.85F, new ItemStack(naturalWandCap, 1, 3), 7);
		new WandCap("mauftrium", 0.7F, new ItemStack(naturalWandCap, 1, 4), 11);
	}
	
	public static void registerBlocks() {
		registerBlock(alfheimThaumOre, BlockCustomOreItem.class, "AlfheimThaumOre");
	}
	
	public static void registerItems() {
		register(naturalWandCap);
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
		ConfigResearch.recipes.put("WandCapManasteel",
			addArcaneCraftingRecipe("CAP_manasteel",
				new ItemStack(naturalWandCap, 1, 0),
				new AspectList()
				.add(Aspect.ORDER, ((WandCap) WandCap.caps.get("manasteel")).getCraftCost())
				.add(Aspect.FIRE, ((WandCap) WandCap.caps.get("manasteel")).getCraftCost())
				.add(Aspect.AIR, ((WandCap) WandCap.caps.get("manasteel")).getCraftCost()),
				"NNN", "N N",
				'N', MANASTEEL_NUGGET
			)
		);
		
		ConfigResearch.recipes.put("WandCapTerrasteel",
			addArcaneCraftingRecipe("CAP_terrasteel",
				new ItemStack(naturalWandCap, 1, 1),
				new AspectList()
				.add(Aspect.ORDER, ((WandCap) WandCap.caps.get("terrasteel")).getCraftCost())
				.add(Aspect.FIRE, ((WandCap) WandCap.caps.get("terrasteel")).getCraftCost())
				.add(Aspect.AIR, ((WandCap) WandCap.caps.get("terrasteel")).getCraftCost()),
				"NNN", "N N",
				'N', TERRASTEEL_NUGGET
			)
		);
		
		ConfigResearch.recipes.put("WandCapElementium",
			addArcaneCraftingRecipe("CAP_elementium",
				new ItemStack(naturalWandCap, 1, 2),
				new AspectList()
				.add(Aspect.ORDER, ((WandCap) WandCap.caps.get("elementium")).getCraftCost())
				.add(Aspect.FIRE, ((WandCap) WandCap.caps.get("elementium")).getCraftCost())
				.add(Aspect.AIR, ((WandCap) WandCap.caps.get("elementium")).getCraftCost()),
				"NNN", "N N",
				'N', ELEMENTIUM_NUGGET
			)
		);
		
		ConfigResearch.recipes.put("WandCapElvorium",
			addArcaneCraftingRecipe("CAP_elvorium",
				new ItemStack(naturalWandCap, 1, 3),
				new AspectList()
				.add(Aspect.ORDER, ((WandCap) WandCap.caps.get("elvorium")).getCraftCost())
				.add(Aspect.FIRE, ((WandCap) WandCap.caps.get("elvorium")).getCraftCost())
				.add(Aspect.AIR, ((WandCap) WandCap.caps.get("elvorium")).getCraftCost()),
				"NNN", "N N",
				'N', ELVORIUM_NUGGET
			)
		);
		
		ConfigResearch.recipes.put("WandCapMauftrium",
			addArcaneCraftingRecipe("CAP_mauftrium",
				new ItemStack(naturalWandCap, 1, 4),
				new AspectList()
				.add(Aspect.ORDER, ((WandCap) WandCap.caps.get("mauftrium")).getCraftCost())
				.add(Aspect.FIRE, ((WandCap) WandCap.caps.get("mauftrium")).getCraftCost())
				.add(Aspect.AIR, ((WandCap) WandCap.caps.get("mauftrium")).getCraftCost()),
				"NNN", "N N",
				'N', MAUFTRIUM_NUGGET
			)
		);
		
		ConfigResearch.recipes.put("PureElementium",
			addCrucibleRecipe("PUREELEMENTIUM",
				new ItemStack(ConfigItems.itemNugget, 1, AlfheimASMData.elementiumClusterMeta()),
				ELEMENTIUM_ORE,
				new AspectList()
				.merge(Aspect.METAL, 1)
				.merge(Aspect.ORDER, 1)
			)
		);
		
		ConfigResearch.recipes.put("TransElementium",
			addCrucibleRecipe("TRANSELEMENTIUM",
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
	}
	
	public static void removeESMRecipes() {
		CraftingManager.getInstance().getRecipeList().remove(recipeElementiumWandCap);
	}
	
	public static void reigsterResearches() {
		new ResearchItem("CAP_manasteel", "THAUMATURGY",
			new AspectList().add(Aspect.METAL, 3).add(Aspect.EXCHANGE, 3).add(Aspect.TOOL, 3),
			4, 0, 1,
			new ItemStack(naturalWandCap, 1, 0))
			
			.setPages(	new ResearchPage("tc.research_page.CAP_manasteel.1"),
						new ResearchPage((IArcaneRecipe) ConfigResearch.recipes.get("WandCapManasteel")))
			
			.setParents("CAP_gold").registerResearchItem();
		
		
		
		new ResearchItem("CAP_terrasteel", "THAUMATURGY",
			new AspectList().add(Aspect.METAL, 6).add(Aspect.MAGIC, 6).add(Aspect.TOOL, 3).add(Aspect.AURA, 3),
			7, 4, 2,
			new ItemStack(naturalWandCap, 1, 1))
			
			.setPages(	new ResearchPage("tc.research_page.CAP_terrasteel.1"),
						new ResearchPage((IArcaneRecipe) ConfigResearch.recipes.get("WandCapTerrasteel")))
			
			.setParents("CAP_thaumium").registerResearchItem();
		
		
		
		new ResearchItem("CAP_elementium", "THAUMATURGY",
			new AspectList().add(Aspect.METAL, 3).add(Aspect.EXCHANGE, 3).add(Aspect.TOOL, 3),
			6, 2, 1,
			new ItemStack(naturalWandCap, 1, 2))
			
			.setPages(	new ResearchPage("tc.research_page.CAP_elementium.1"),
						new ResearchPage((IArcaneRecipe) ConfigResearch.recipes.get("WandCapElementium")))
			
			.setParents("CAP_gold").registerResearchItem();
		
		
		
		new ResearchItem("CAP_elvorium", "THAUMATURGY",
			new AspectList().add(Aspect.METAL, 6).add(Aspect.MAGIC, 6).add(Aspect.TOOL, 3).add(Aspect.AURA, 3),
			5, 6, 2,
			new ItemStack(naturalWandCap, 1, 3))
			
			.setPages(	new ResearchPage("tc.research_page.CAP_elvorium.1"),
						new ResearchPage((IArcaneRecipe) ConfigResearch.recipes.get("WandCapElvorium")))
			
			.setParents("CAP_thaumium").registerResearchItem();
		
		
		
		new ResearchItem("CAP_mauftrium", "THAUMATURGY",
			new AspectList().add(Aspect.VOID, 5).add(Aspect.ELDRITCH, 5).add(Aspect.TOOL, 3).add(Aspect.MAGIC, 3).add(Aspect.AURA, 3),
			7, 6, 3,
			new ItemStack(naturalWandCap, 1, 4))
			
			.setPages(	new ResearchPage("tc.research_page.CAP_mauftrium.1"),
						new ResearchPage((IArcaneRecipe) ConfigResearch.recipes.get("WandCapMauftrium")))
			
			.setParents("CAP_void").registerResearchItem();
		
		new ResearchItem("PUREELEMENTIUM", "ALCHEMY",
			new AspectList().add(Aspect.METAL, 3).add(Aspect.ORDER, 2).add(Aspect.MAGIC, 1),
			-3, 2, 1, new ItemStack(ConfigItems.itemNugget, 1, AlfheimASMData.elementiumClusterMeta()))
		
			.setPages(	new ResearchPage("tc.research_page.PUREELEMENTIUM.1"),
						new ResearchPage((CrucibleRecipe) ConfigResearch.recipes.get("PureElementium")))
			
			.setConcealed().setSecondary().setParents("PUREIRON").registerResearchItem();
		
		new ResearchItem("TRANSELEMENTIUM", "ALCHEMY",
			new AspectList().add(Aspect.METAL, 3).add(Aspect.EXCHANGE, 3),
			1, 2, 1, new ItemStack(ModItems.manaResource, 1, 19))
			
			.setPages(	new ResearchPage("tc.research_page.TRANSELEMENTIUM.1"),
						new ResearchPage((CrucibleRecipe) ConfigResearch.recipes.get("TransElementium")))
			
			.setConcealed().setSecondary().setParents("TRANSIRON").registerResearchItem();
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
	
	public static CreativeTabs tcnTab = new CreativeTabs("NTC") {
		@Override
		public Item getTabIconItem() {
			return naturalWandCap;
		}
		
		public int func_151243_f() {
			return 1;
		}
	}.setNoTitle().setBackgroundImageName("NTC.png");
}
