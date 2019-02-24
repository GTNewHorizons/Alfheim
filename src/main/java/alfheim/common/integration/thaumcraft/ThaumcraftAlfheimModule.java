package alfheim.common.integration.thaumcraft;

import static alfheim.api.lib.LibOreDict.*;
import static vazkii.botania.common.lib.LibOreDict.*;

import alexsocol.asjlib.ASJUtilities;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.item.compat.thaumcraft.ItemAlfheimWandCap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.api.wands.WandCap;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.config.ConfigResearch;
import vazkii.botania.common.item.ModItems;

public class ThaumcraftAlfheimModule {
	
	public static Item naturalWandCap;
	
	public static void preInit() {
		constructItems();
		registerItems();
	}
	
	public static void constructItems() {
		naturalWandCap = new ItemAlfheimWandCap();
	
		new WandCap("manasteel", 1, new ItemStack(naturalWandCap, 1, 0), 3);
		new WandCap("terrasteel", 0.85F, new ItemStack(naturalWandCap, 1, 1), 7);
		new WandCap("elementium", 1, new ItemStack(naturalWandCap, 1, 2), 3);
		new WandCap("elvorium", 0.85F, new ItemStack(naturalWandCap, 1, 3), 7);
		new WandCap("mauftrium", 0.7F, new ItemStack(naturalWandCap, 1, 4), 11);
	}
	
	public static void registerItems() {
		ASJUtilities.register(naturalWandCap);
	}
	
	public static void postInit() {
		registerRecipes();
		reigsterResearches();
	}
	
	public static void registerRecipes() {
		ConfigResearch.recipes.put("WandCapManasteel",
			ThaumcraftApi.addArcaneCraftingRecipe("CAP_manasteel",
			new ItemStack(naturalWandCap, 1, 0),
			new AspectList()
			.add(Aspect.ORDER, ((WandCap) WandCap.caps.get("manasteel")).getCraftCost())
			.add(Aspect.FIRE, ((WandCap) WandCap.caps.get("manasteel")).getCraftCost())
			.add(Aspect.AIR, ((WandCap) WandCap.caps.get("manasteel")).getCraftCost()),
			"NNN", "N N",
			'N', MANASTEEL_NUGGET
		));
		
		ConfigResearch.recipes.put("WandCapTerrasteel",
			ThaumcraftApi.addArcaneCraftingRecipe("CAP_terrasteel",
			new ItemStack(naturalWandCap, 1, 1),
			new AspectList()
			.add(Aspect.ORDER, ((WandCap) WandCap.caps.get("terrasteel")).getCraftCost())
			.add(Aspect.FIRE, ((WandCap) WandCap.caps.get("terrasteel")).getCraftCost())
			.add(Aspect.AIR, ((WandCap) WandCap.caps.get("terrasteel")).getCraftCost()),
			"NNN", "N N",
			'N', TERRASTEEL_NUGGET
		));
		
		ConfigResearch.recipes.put("WandCapElementium",
			ThaumcraftApi.addArcaneCraftingRecipe("CAP_elementium",
			new ItemStack(naturalWandCap, 1, 2),
			new AspectList()
			.add(Aspect.ORDER, ((WandCap) WandCap.caps.get("elementium")).getCraftCost())
			.add(Aspect.FIRE, ((WandCap) WandCap.caps.get("elementium")).getCraftCost())
			.add(Aspect.AIR, ((WandCap) WandCap.caps.get("elementium")).getCraftCost()),
			"NNN", "N N",
			'N', ELEMENTIUM_NUGGET
		));
		
		ConfigResearch.recipes.put("WandCapElvorium",
			ThaumcraftApi.addArcaneCraftingRecipe("CAP_elvorium",
			new ItemStack(naturalWandCap, 1, 3),
			new AspectList()
			.add(Aspect.ORDER, ((WandCap) WandCap.caps.get("elvorium")).getCraftCost())
			.add(Aspect.FIRE, ((WandCap) WandCap.caps.get("elvorium")).getCraftCost())
			.add(Aspect.AIR, ((WandCap) WandCap.caps.get("elvorium")).getCraftCost()),
			"NNN", "N N",
			'N', ELVORIUM_NUGGET
		));
		
		ConfigResearch.recipes.put("WandCapMauftrium",
			ThaumcraftApi.addArcaneCraftingRecipe("CAP_mauftrium",
			new ItemStack(naturalWandCap, 1, 4),
			new AspectList()
			.add(Aspect.ORDER, ((WandCap) WandCap.caps.get("mauftrium")).getCraftCost())
			.add(Aspect.FIRE, ((WandCap) WandCap.caps.get("mauftrium")).getCraftCost())
			.add(Aspect.AIR, ((WandCap) WandCap.caps.get("mauftrium")).getCraftCost()),
			"NNN", "N N",
			'N', MAUFTRIUM_NUGGET
		));
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
