package alfheim.common.integration.thaumcraft

import alexsocol.asjlib.ASJUtilities.*
import alfheim.api.lib.LibOreDict.*
import cpw.mods.fml.client.registry.RenderingRegistry.*
import cpw.mods.fml.common.registry.GameRegistry.*
import net.minecraftforge.oredict.OreDictionary.*
import thaumcraft.api.ThaumcraftApi.*
import thaumcraft.common.lib.utils.Utils.*
import vazkii.botania.common.lib.LibOreDict.*

import alexsocol.asjlib.ASJReflectionHelper
import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.client.render.block.RenderBlockAlfheimThaumOre
import alfheim.common.block.compat.thaumcraft.BlockAlfheimThaumOre
import alfheim.common.core.asm.AlfheimASMData
import alfheim.common.core.registry.AlfheimBlocks
import alfheim.common.item.compat.thaumcraft.ItemAlfheimWandCap
import alfheim.common.item.compat.thaumcraft.ItemAlfheimWandRod
import alfheim.common.item.compat.thaumcraft.NaturalWandRodOnUpdate
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.item.crafting.IRecipe
import net.minecraftforge.oredict.ShapedOreRecipe
import thaumcraft.api.aspects.Aspect
import thaumcraft.api.aspects.AspectList
import thaumcraft.api.crafting.CrucibleRecipe
import thaumcraft.api.crafting.IArcaneRecipe
import thaumcraft.api.research.ResearchItem
import thaumcraft.api.research.ResearchPage
import thaumcraft.api.wands.StaffRod
import thaumcraft.api.wands.WandCap
import thaumcraft.api.wands.WandRod
import thaumcraft.common.blocks.BlockCustomOreItem
import thaumcraft.common.config.ConfigItems
import thaumcraft.common.config.ConfigResearch
import vazkii.botania.common.item.ModItems

object ThaumcraftAlfheimModule {
	
	var alfheimThaumOre: Block
	
	var naturalWandCap: Item
	var naturalWandRod: Item
	
	var recipeElementiumWandCap: IRecipe
	
	var renderIDOre = -1
	
	val tcnTab = object: CreativeTabs("NTC") {
		override fun getTabIconItem(): Item {
			return naturalWandCap
		}
		
		override fun func_151243_f(): Int {
			return 1
		}
	}.setNoTitle().setBackgroundImageName("NTC.png")
	
	val capManasteelName = ModInfo.MODID + "Manasteel"
	val capManasteelRecipe = ModInfo.MODID + "WandCapManasteel"
	val capManasteelResearch = "CAP_$capManasteelName"
	
	val capTerrasteelName = ModInfo.MODID + "Terrasteel"
	val capTerrasteelRecipe = ModInfo.MODID + "WandCapTerrasteel"
	val capTerrasteelResearch = "CAP_$capTerrasteelName"
	
	val capElementiumName = ModInfo.MODID + "Elementium"
	val capElementiumRecipe = ModInfo.MODID + "WandCapElementium"
	val capElementiumResearch = "CAP_$capElementiumName"
	
	val capElvoriumName = ModInfo.MODID + "Elvorium"
	val capElvoriumRecipe = ModInfo.MODID + "WandCapElvorium"
	val capElvoriumResearch = "CAP_$capElvoriumName"
	
	val capMauftriumName = ModInfo.MODID + "Mauftrium"
	val capMauftriumRecipe = ModInfo.MODID + "WandCapMauftrium"
	val capMauftriumResearch = "CAP_$capMauftriumName"
	
	val rodLivingwoodName = ModInfo.MODID + "Livingwood"
	val rodLivingwoodRecipe = ModInfo.MODID + "WandRodLivingwood"
	val rodLivingwoodResearch = "ROD_$rodLivingwoodName"
	
	val rodDreamwoodName = ModInfo.MODID + "Dreamwood"
	val rodDreamwoodRecipe = ModInfo.MODID + "WandRodDreamwood"
	val rodDreamwoodResearch = "ROD_$rodDreamwoodName"
	
	val rodSpiritualName = ModInfo.MODID + "Spiritual"
	val rodSpiritualStaff = rodSpiritualName + "_staff"
	val rodSpiritualRecipe = ModInfo.MODID + "WandRodSpiritual"
	val rodSpiritualResearch = "ROD_$rodSpiritualStaff"
	
	val pureElementiumRecipe = ModInfo.MODID + "PUREELEMENTIUM"
	val pureElementiumResearch = ModInfo.MODID + "PureElementium"
	
	val transElementiumRecipe = ModInfo.MODID + "TRANSELEMENTIUM"
	val transElementiumResearch = ModInfo.MODID + "TransElementium"
	
	fun preInit() {
		constructBlocks()
		constructItems()
		registerBlocks()
		registerItems()
		if (!ASJUtilities.isServer) registerRenders()
	}
	
	fun constructBlocks() {
		alfheimThaumOre = BlockAlfheimThaumOre()
	}
	
	fun constructItems() {
		naturalWandCap = ItemAlfheimWandCap()
		naturalWandRod = ItemAlfheimWandRod()
		
		WandCap(capManasteelName, 0.95f, ItemStack(naturalWandCap, 1, 0), 5)
		WandCap(capTerrasteelName, 0.85f, ItemStack(naturalWandCap, 1, 1), 8)
		WandCap(capElementiumName, 0.95f, ItemStack(naturalWandCap, 1, 2), 5)
		WandCap(capElvoriumName, 0.85f, ItemStack(naturalWandCap, 1, 3), 8)
		WandCap(capMauftriumName, 0.75f, ItemStack(naturalWandCap, 1, 4), 11)
		
		WandRod(rodLivingwoodName, 35, ItemStack(naturalWandRod, 1, 0), 2)
		WandRod(rodDreamwoodName, 65, ItemStack(naturalWandRod, 1, 1), 5)
		StaffRod(rodSpiritualName, 85, ItemStack(naturalWandRod, 1, 2), 12, NaturalWandRodOnUpdate()).isGlowing = true
	}
	
	fun registerBlocks() {
		registerBlock(alfheimThaumOre, BlockCustomOreItem::class.java, "AlfheimThaumOre")
	}
	
	fun registerItems() {
		register(naturalWandCap)
		register(naturalWandRod)
	}
	
	fun registerRenders() {
		renderIDOre = getNextAvailableRenderId()
		registerBlockHandler(RenderBlockAlfheimThaumOre())
	}
	
	fun postInit() {
		registerRecipes()
		reigsterResearches()
		registerOreDict()
		
		addSpecialMiningResult(ItemStack(alfheimThaumOre, 1, 0), ItemStack(ConfigItems.itemNugget, 1, 21), 0.9f)
		addSpecialMiningResult(ItemStack(AlfheimBlocks.elvenOres, 1, 1), ItemStack(ConfigItems.itemNugget, 1, AlfheimASMData.elementiumClusterMeta()), 1.0f)
	}
	
	fun registerRecipes() {
		ConfigResearch.recipes[capManasteelRecipe] = addArcaneCraftingRecipe(capManasteelResearch,
																			 ItemStack(naturalWandCap, 1, 0),
																			 AspectList()
																				 .add(Aspect.AIR, WandCap.caps[capManasteelName].getCraftCost())
																				 .add(Aspect.FIRE, WandCap.caps[capManasteelName].getCraftCost())
																				 .add(Aspect.ORDER, WandCap.caps[capManasteelName].getCraftCost()),
																			 "NNN", "N N",
																			 'N', MANASTEEL_NUGGET
		)
		
		ConfigResearch.recipes[capTerrasteelRecipe] = addArcaneCraftingRecipe(capTerrasteelResearch,
																			  ItemStack(naturalWandCap, 1, 1),
																			  AspectList()
																				  .add(Aspect.AIR, WandCap.caps[capTerrasteelName].getCraftCost())
																				  .add(Aspect.FIRE, WandCap.caps[capTerrasteelName].getCraftCost())
																				  .add(Aspect.ORDER, WandCap.caps[capTerrasteelName].getCraftCost()),
																			  "NNN", "N N",
																			  'N', TERRASTEEL_NUGGET
		)
		
		ConfigResearch.recipes[capElementiumRecipe] = addArcaneCraftingRecipe(capElementiumResearch,
																			  ItemStack(naturalWandCap, 1, 2),
																			  AspectList()
																				  .add(Aspect.AIR, WandCap.caps[capElementiumName].getCraftCost())
																				  .add(Aspect.FIRE, WandCap.caps[capElementiumName].getCraftCost())
																				  .add(Aspect.ORDER, WandCap.caps[capElementiumName].getCraftCost()),
																			  "NNN", "N N",
																			  'N', ELEMENTIUM_NUGGET
		)
		
		ConfigResearch.recipes[capElvoriumRecipe] = addArcaneCraftingRecipe(capElvoriumResearch,
																			ItemStack(naturalWandCap, 1, 3),
																			AspectList()
																				.add(Aspect.AIR, WandCap.caps[capElvoriumName].getCraftCost())
																				.add(Aspect.FIRE, WandCap.caps[capElvoriumName].getCraftCost())
																				.add(Aspect.ORDER, WandCap.caps[capElvoriumName].getCraftCost()),
																			"NNN", "N N",
																			'N', ELVORIUM_NUGGET
		)
		
		ConfigResearch.recipes[capMauftriumRecipe] = addArcaneCraftingRecipe(capMauftriumResearch,
																			 ItemStack(naturalWandCap, 1, 4),
																			 AspectList()
																				 .add(Aspect.AIR, WandCap.caps[capMauftriumName].getCraftCost())
																				 .add(Aspect.FIRE, WandCap.caps[capMauftriumName].getCraftCost())
																				 .add(Aspect.WATER, WandCap.caps[capMauftriumName].getCraftCost())
																				 .add(Aspect.EARTH, WandCap.caps[capMauftriumName].getCraftCost())
																				 .add(Aspect.ORDER, WandCap.caps[capMauftriumName].getCraftCost())
																				 .add(Aspect.ENTROPY, WandCap.caps[capMauftriumName].getCraftCost()),
																			 "NNN", "N N",
																			 'N', MAUFTRIUM_NUGGET
		)
		
		ConfigResearch.recipes[rodLivingwoodRecipe] = addArcaneCraftingRecipe(rodLivingwoodResearch,
																			  ItemStack(naturalWandRod, 1, 0),
																			  AspectList()
																				  .add(Aspect.AIR, WandRod.rods[rodLivingwoodName].getCraftCost())
																				  .add(Aspect.EARTH, WandRod.rods[rodLivingwoodName].getCraftCost()),
																			  "  T", " T ", "T  ",
																			  'T', LIVINGWOOD_TWIG
		)
		
		ConfigResearch.recipes[rodDreamwoodRecipe] = addArcaneCraftingRecipe(rodDreamwoodResearch,
																			 ItemStack(naturalWandRod, 1, 1),
																			 AspectList()
																				 .add(Aspect.AIR, WandRod.rods[rodDreamwoodName].getCraftCost())
																				 .add(Aspect.EARTH, WandRod.rods[rodDreamwoodName].getCraftCost())
																				 .add(Aspect.ORDER, WandRod.rods[rodDreamwoodName].getCraftCost()),
																			 "  I", " I ", "I  ",
																			 'I', INFUSED_DREAM_TWIG
		)
		
		ConfigResearch.recipes[rodSpiritualRecipe] = addArcaneCraftingRecipe("",
																			 ItemStack(naturalWandRod, 1, 2),
																			 AspectList()
																				 .add(Aspect.AIR, WandRod.rods[rodSpiritualStaff].getCraftCost())
																				 .add(Aspect.FIRE, WandRod.rods[rodSpiritualStaff].getCraftCost())
																				 .add(Aspect.WATER, WandRod.rods[rodSpiritualStaff].getCraftCost())
																				 .add(Aspect.EARTH, WandRod.rods[rodSpiritualStaff].getCraftCost())
																				 .add(Aspect.ORDER, WandRod.rods[rodSpiritualStaff].getCraftCost())
																				 .add(Aspect.ENTROPY, WandRod.rods[rodSpiritualStaff].getCraftCost()),
																			 "DSP", " RS", "R D",
																			 'R', ItemStack(naturalWandRod, 1, 1),
																			 'S', LIFE_ESSENCE,
																			 'D', DRAGONSTONE,
																			 'P', ItemStack(ConfigItems.itemResource, 1, 15)
		)
		
		ConfigResearch.recipes[pureElementiumRecipe] = addCrucibleRecipe(pureElementiumResearch,
																		 ItemStack(ConfigItems.itemNugget, 1, AlfheimASMData.elementiumClusterMeta()),
																		 ELEMENTIUM_ORE,
																		 AspectList()
																			 .merge(Aspect.METAL, 1)
																			 .merge(Aspect.ORDER, 1)
		)
		
		ConfigResearch.recipes[transElementiumRecipe] = addCrucibleRecipe(transElementiumResearch,
																		  ItemStack(ModItems.manaResource, 3, 19),
																		  ELEMENTIUM_NUGGET,
																		  AspectList()
																			  .merge(Aspect.METAL, 2)
																			  .merge(Aspect.MAGIC, 2)
		)
		
		recipeElementiumWandCap = ShapedOreRecipe(ItemStack(naturalWandCap, 1, 2),
												  "NNN", "NIN",
												  'N', ELEMENTIUM_NUGGET,
												  'I', IFFESAL_DUST)
		
		if (AlfheimCore.enableElvenStory) addESMRecipes()
		
		addSmelting(ItemStack(alfheimThaumOre, 1, 0),
					ItemStack(ConfigItems.itemResource, 1, 3), // Cinnabar
					1.0f
		)
		
		addSmelting(ItemStack(alfheimThaumOre, 1, 7),
					ItemStack(ConfigItems.itemResource, 1, 6), // Amber
					1.0f
		)
		
		addSmelting(ItemStack(ConfigItems.itemNugget, 1, AlfheimASMData.elementiumClusterMeta()),
					ItemStack(ModItems.manaResource, 2, 7), // Elementium
					1.0f
		)
		
		addSmeltingBonus(ELEMENTIUM_ORE,
						 ItemStack(ModItems.manaResource, 0, 19)        // from ore
		)
		
		addSmeltingBonus(ItemStack(ConfigItems.itemNugget, 1, AlfheimASMData.elementiumClusterMeta()),
						 ItemStack(ModItems.manaResource, 0, 19)        // from cluster
		)
	}
	
	fun addESMRecipes() {
		CraftingManager.getInstance().recipeList.add(recipeElementiumWandCap)
		
		var cap = WandCap.caps[capElementiumName]
		ASJReflectionHelper.setValue(cap, 0.95f, "baseCostModifier")
		cap.setCraftCost(5)
		
		cap = WandCap.caps[capElvoriumName]
		ASJReflectionHelper.setValue(cap, 0.85f, "baseCostModifier")
		cap.setCraftCost(8)
	}
	
	fun removeESMRecipes() {
		CraftingManager.getInstance().recipeList.remove(recipeElementiumWandCap)
		
		var cap = WandCap.caps[capElementiumName]
		ASJReflectionHelper.setValue(cap, 0.9f, "baseCostModifier")
		cap.setCraftCost(6)
		
		cap = WandCap.caps[capElvoriumName]
		ASJReflectionHelper.setValue(cap, 0.8f, "baseCostModifier")
		cap.setCraftCost(9)
	}
	
	fun reigsterResearches() {
		ResearchItem(capManasteelResearch, "THAUMATURGY",
					 AspectList().add(Aspect.METAL, 3).add(Aspect.EXCHANGE, 3).add(Aspect.TOOL, 3),
					 4, 0, 1,
					 ItemStack(naturalWandCap, 1, 0))
			
			.setPages(ResearchPage("tc.research_page.$capManasteelResearch.1"),
					  ResearchPage(ConfigResearch.recipes[capManasteelRecipe] as IArcaneRecipe))
			
			.setParents("CAP_gold").registerResearchItem()
		
		
		
		ResearchItem(capTerrasteelResearch, "THAUMATURGY",
					 AspectList().add(Aspect.METAL, 6).add(Aspect.MAGIC, 6).add(Aspect.TOOL, 3).add(Aspect.AURA, 3),
					 7, 4, 2,
					 ItemStack(naturalWandCap, 1, 1))
			
			.setPages(ResearchPage("tc.research_page.$capTerrasteelResearch.1"),
					  ResearchPage(ConfigResearch.recipes[capTerrasteelRecipe] as IArcaneRecipe))
			
			.setParents("CAP_thaumium").registerResearchItem()
		
		
		
		ResearchItem(capElementiumResearch, "THAUMATURGY",
					 AspectList().add(Aspect.METAL, 3).add(Aspect.EXCHANGE, 3).add(Aspect.TOOL, 3),
					 6, 2, 1,
					 ItemStack(naturalWandCap, 1, 2))
			
			.setPages(ResearchPage("tc.research_page.$capElementiumResearch.1"),
					  ResearchPage(ConfigResearch.recipes[capElementiumRecipe] as IArcaneRecipe))
			
			.setParents("CAP_gold").registerResearchItem()
		
		
		
		ResearchItem(capElvoriumResearch, "THAUMATURGY",
					 AspectList().add(Aspect.METAL, 6).add(Aspect.MAGIC, 6).add(Aspect.TOOL, 3).add(Aspect.AURA, 3),
					 5, 6, 2,
					 ItemStack(naturalWandCap, 1, 3))
			
			.setPages(ResearchPage("tc.research_page.$capElvoriumResearch.1"),
					  ResearchPage(ConfigResearch.recipes[capElvoriumRecipe] as IArcaneRecipe))
			
			.setParents("CAP_thaumium").registerResearchItem()
		
		
		
		ResearchItem(capMauftriumResearch, "THAUMATURGY",
					 AspectList().add(Aspect.VOID, 5).add(Aspect.ELDRITCH, 5).add(Aspect.TOOL, 3).add(Aspect.MAGIC, 3).add(Aspect.AURA, 3),
					 7, 6, 3,
					 ItemStack(naturalWandCap, 1, 4))
			
			.setPages(ResearchPage("tc.research_page.$capMauftriumResearch.1"),
					  ResearchPage(ConfigResearch.recipes[capMauftriumRecipe] as IArcaneRecipe))
			
			.setParents("CAP_void").registerResearchItem()
		
		ResearchItem(rodLivingwoodResearch, "THAUMATURGY",
					 AspectList().add(Aspect.TOOL, 3).add(Aspect.TREE, 6).add(Aspect.MAGIC, 3),
					 -2, 2, 1,
					 ItemStack(naturalWandRod, 1, 0))
			
			.setPages(ResearchPage("tc.research_page.$rodLivingwoodResearch.1"),
					  ResearchPage(ConfigResearch.recipes[rodLivingwoodRecipe] as IArcaneRecipe))
			
			.setParents("ROD_greatwood").registerResearchItem()
		
		
		
		ResearchItem(rodDreamwoodResearch, "THAUMATURGY",
					 AspectList().add(Aspect.TOOL, 4).add(Aspect.TREE, 6).add(Aspect.MAGIC, 5),
					 -4, 4, 2,
					 ItemStack(naturalWandRod, 1, 1))
			
			.setPages(ResearchPage("tc.research_page.$rodDreamwoodResearch.1"),
					  ResearchPage(ConfigResearch.recipes[rodDreamwoodRecipe] as IArcaneRecipe))
			
			.setParents("ROD_greatwood").registerResearchItem()
		
		
		
		ResearchItem(rodSpiritualResearch, "THAUMATURGY",
					 AspectList().add(Aspect.TOOL, 6).add(Aspect.TREE, 6).add(Aspect.MAGIC, 12),
					 -3, 6, 2,
					 ItemStack(naturalWandRod, 1, 2))
			
			.setPages(ResearchPage("tc.research_page.$rodSpiritualResearch.1"),
					  ResearchPage(ConfigResearch.recipes[rodSpiritualRecipe] as IArcaneRecipe))
			
			.setParents("ROD_silverwood").registerResearchItem()
		
		
		
		ResearchItem(pureElementiumResearch, "ALCHEMY",
					 AspectList().add(Aspect.METAL, 3).add(Aspect.ORDER, 2).add(Aspect.MAGIC, 1),
					 -3, 2, 1, ItemStack(ConfigItems.itemNugget, 1, AlfheimASMData.elementiumClusterMeta()))
			
			.setPages(ResearchPage("tc.research_page.$pureElementiumResearch.1"),
					  ResearchPage(ConfigResearch.recipes[pureElementiumRecipe] as CrucibleRecipe))
			
			.setConcealed().setSecondary().setParents("PUREIRON").registerResearchItem()
		
		
		
		ResearchItem(transElementiumResearch, "ALCHEMY",
					 AspectList().add(Aspect.METAL, 3).add(Aspect.EXCHANGE, 3),
					 1, 2, 1, ItemStack(ModItems.manaResource, 1, 19))
			
			.setPages(ResearchPage("tc.research_page.$transElementiumResearch.1"),
					  ResearchPage(ConfigResearch.recipes[transElementiumRecipe] as CrucibleRecipe))
			
			.setConcealed().setSecondary().setParents("TRANSIRON").registerResearchItem()
		
		addWarpToResearch(capMauftriumResearch, 2)
	}
	
	fun registerOreDict() {
		registerOre("oreCinnabar", ItemStack(alfheimThaumOre, 1, 0))
		registerOre("oreInfusedAir", ItemStack(alfheimThaumOre, 1, 1))
		registerOre("oreInfusedFire", ItemStack(alfheimThaumOre, 1, 2))
		registerOre("oreInfusedWater", ItemStack(alfheimThaumOre, 1, 3))
		registerOre("oreInfusedEarth", ItemStack(alfheimThaumOre, 1, 4))
		registerOre("oreInfusedOrder", ItemStack(alfheimThaumOre, 1, 5))
		registerOre("oreInfusedEntropy", ItemStack(alfheimThaumOre, 1, 6))
		registerOre("oreAmber", ItemStack(alfheimThaumOre, 1, 7))
	}
}
