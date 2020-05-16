package alfheim.common.integration.thaumcraft

import alexsocol.asjlib.*
import alexsocol.asjlib.ASJUtilities.register
import alfheim.api.ModInfo
import alfheim.api.lib.LibOreDict.ELEMENTIUM_ORE
import alfheim.api.lib.LibOreDict.ELVORIUM_NUGGET
import alfheim.api.lib.LibOreDict.IFFESAL_DUST
import alfheim.api.lib.LibOreDict.INFUSED_DREAM_TWIG
import alfheim.api.lib.LibOreDict.MAUFTRIUM_NUGGET
import alfheim.client.render.block.RenderBlockAlfheimThaumOre
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.compat.thaumcraft.BlockAlfheimThaumOre
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.item.compat.thaumcraft.*
import cpw.mods.fml.client.registry.RenderingRegistry.*
import cpw.mods.fml.common.registry.GameRegistry.*
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.*
import net.minecraft.item.crafting.*
import net.minecraftforge.oredict.OreDictionary.registerOre
import net.minecraftforge.oredict.ShapedOreRecipe
import thaumcraft.api.ThaumcraftApi.*
import thaumcraft.api.aspects.*
import thaumcraft.api.crafting.*
import thaumcraft.api.research.*
import thaumcraft.api.wands.*
import thaumcraft.common.blocks.BlockCustomOreItem
import thaumcraft.common.config.*
import thaumcraft.common.lib.utils.Utils.addSpecialMiningResult
import vazkii.botania.common.Botania
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.lib.LibOreDict.*

object ThaumcraftAlfheimModule {
	
	lateinit var alfheimThaumOre: Block
	
	lateinit var naturalWandCap: Item
	lateinit var naturalWandRod: Item
	
	lateinit var recipeElementiumWandCap: IRecipe
	
	var renderIDOre = -1
	
	val tcnTab = object: CreativeTabs("NTC") {
		override fun getTabIconItem(): Item {
			return naturalWandCap
		}
		
		override fun func_151243_f(): Int {
			return 1
		}
	}.setNoTitle().setBackgroundImageName("NTC.png")!!
	
	const val capManasteelName = ModInfo.MODID + "Manasteel"
	const val capManasteelRecipe = ModInfo.MODID + "WandCapManasteel"
	const val capManasteelResearch = "CAP_$capManasteelName"
	
	const val capTerrasteelName = ModInfo.MODID + "Terrasteel"
	const val capTerrasteelRecipe = ModInfo.MODID + "WandCapTerrasteel"
	const val capTerrasteelResearch = "CAP_$capTerrasteelName"
	
	const val capElementiumName = ModInfo.MODID + "Elementium"
	const val capElementiumRecipe = ModInfo.MODID + "WandCapElementium"
	const val capElementiumResearch = "CAP_$capElementiumName"
	
	const val capElvoriumName = ModInfo.MODID + "Elvorium"
	const val capElvoriumRecipe = ModInfo.MODID + "WandCapElvorium"
	const val capElvoriumResearch = "CAP_$capElvoriumName"
	
	const val capMauftriumName = ModInfo.MODID + "Mauftrium"
	const val capMauftriumRecipe = ModInfo.MODID + "WandCapMauftrium"
	const val capMauftriumResearch = "CAP_$capMauftriumName"
	
	const val rodLivingwoodName = ModInfo.MODID + "Livingwood"
	const val rodLivingwoodRecipe = ModInfo.MODID + "WandRodLivingwood"
	const val rodLivingwoodResearch = "ROD_$rodLivingwoodName"
	
	const val rodDreamwoodName = ModInfo.MODID + "Dreamwood"
	const val rodDreamwoodRecipe = ModInfo.MODID + "WandRodDreamwood"
	const val rodDreamwoodResearch = "ROD_$rodDreamwoodName"
	
	const val rodSpiritualName = ModInfo.MODID + "Spiritual"
	const val rodSpiritualStaff = rodSpiritualName + "_staff"
	const val rodSpiritualRecipe = ModInfo.MODID + "WandRodSpiritual"
	const val rodSpiritualResearch = "ROD_$rodSpiritualStaff"
	
	const val pureElementiumRecipe = ModInfo.MODID + "PUREELEMENTIUM"
	const val pureElementiumResearch = ModInfo.MODID + "PureElementium"
	
	const val transElementiumRecipe = ModInfo.MODID + "TRANSELEMENTIUM"
	const val transElementiumResearch = ModInfo.MODID + "TransElementium"
	
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

		if (Botania.thaumcraftLoaded) {
			ModItems.elementiumHelmRevealing.creativeTab = tcnTab
			ModItems.manasteelHelmRevealing.creativeTab = tcnTab
			ModItems.terrasteelHelmRevealing.creativeTab = tcnTab
		}

		addSpecialMiningResult(ItemStack(alfheimThaumOre, 1, 0), ItemStack(ConfigItems.itemNugget, 1, 21), 0.9f)
		addSpecialMiningResult(ItemStack(AlfheimBlocks.elvenOre, 1, 1), ItemStack(ConfigItems.itemNugget, 1, AlfheimConfigHandler.elementiumClusterMeta), 1f)
	}
	
	fun registerRecipes() {
		ConfigResearch.recipes[capManasteelRecipe] = addArcaneCraftingRecipe(capManasteelResearch,
																			 ItemStack(naturalWandCap, 1, 0),
																			 AspectList()
																				 .add(Aspect.AIR, WandCap.caps[capManasteelName]!!.craftCost)
																				 .add(Aspect.FIRE, WandCap.caps[capManasteelName]!!.craftCost)
																				 .add(Aspect.ORDER, WandCap.caps[capManasteelName]!!.craftCost),
																			 "NNN", "N N",
																			 'N', MANASTEEL_NUGGET
		)
		
		ConfigResearch.recipes[capTerrasteelRecipe] = addArcaneCraftingRecipe(capTerrasteelResearch,
																			  ItemStack(naturalWandCap, 1, 1),
																			  AspectList()
																				  .add(Aspect.AIR, WandCap.caps[capTerrasteelName]!!.craftCost)
																				  .add(Aspect.FIRE, WandCap.caps[capTerrasteelName]!!.craftCost)
																				  .add(Aspect.ORDER, WandCap.caps[capTerrasteelName]!!.craftCost),
																			  "NNN", "N N",
																			  'N', TERRASTEEL_NUGGET
		)
		
		ConfigResearch.recipes[capElementiumRecipe] = addArcaneCraftingRecipe(capElementiumResearch,
																			  ItemStack(naturalWandCap, 1, 2),
																			  AspectList()
																				  .add(Aspect.AIR, WandCap.caps[capElementiumName]!!.craftCost)
																				  .add(Aspect.FIRE, WandCap.caps[capElementiumName]!!.craftCost)
																				  .add(Aspect.ORDER, WandCap.caps[capElementiumName]!!.craftCost),
																			  "NNN", "N N",
																			  'N', ELEMENTIUM_NUGGET
		)
		
		ConfigResearch.recipes[capElvoriumRecipe] = addArcaneCraftingRecipe(capElvoriumResearch,
																			ItemStack(naturalWandCap, 1, 3),
																			AspectList()
																				.add(Aspect.AIR, WandCap.caps[capElvoriumName]!!.craftCost)
																				.add(Aspect.FIRE, WandCap.caps[capElvoriumName]!!.craftCost)
																				.add(Aspect.ORDER, WandCap.caps[capElvoriumName]!!.craftCost),
																			"NNN", "N N",
																			'N', ELVORIUM_NUGGET
		)
		
		ConfigResearch.recipes[capMauftriumRecipe] = addArcaneCraftingRecipe(capMauftriumResearch,
																			 ItemStack(naturalWandCap, 1, 4),
																			 AspectList()
																				 .add(Aspect.AIR, WandCap.caps[capMauftriumName]!!.craftCost)
																				 .add(Aspect.FIRE, WandCap.caps[capMauftriumName]!!.craftCost)
																				 .add(Aspect.WATER, WandCap.caps[capMauftriumName]!!.craftCost)
																				 .add(Aspect.EARTH, WandCap.caps[capMauftriumName]!!.craftCost)
																				 .add(Aspect.ORDER, WandCap.caps[capMauftriumName]!!.craftCost)
																				 .add(Aspect.ENTROPY, WandCap.caps[capMauftriumName]!!.craftCost),
																			 "NNN", "N N",
																			 'N', MAUFTRIUM_NUGGET
		)
		
		ConfigResearch.recipes[rodLivingwoodRecipe] = addArcaneCraftingRecipe(rodLivingwoodResearch,
																			  ItemStack(naturalWandRod, 1, 0),
																			  AspectList()
																				  .add(Aspect.AIR, WandRod.rods[rodLivingwoodName]!!.craftCost)
																				  .add(Aspect.EARTH, WandRod.rods[rodLivingwoodName]!!.craftCost),
																			  "  T", " T ", "T  ",
																			  'T', LIVINGWOOD_TWIG
		)
		
		ConfigResearch.recipes[rodDreamwoodRecipe] = addArcaneCraftingRecipe(rodDreamwoodResearch,
																			 ItemStack(naturalWandRod, 1, 1),
																			 AspectList()
																				 .add(Aspect.AIR, WandRod.rods[rodDreamwoodName]!!.craftCost)
																				 .add(Aspect.EARTH, WandRod.rods[rodDreamwoodName]!!.craftCost)
																				 .add(Aspect.ORDER, WandRod.rods[rodDreamwoodName]!!.craftCost),
																			 "  I", " I ", "I  ",
																			 'I', INFUSED_DREAM_TWIG
		)
		
		ConfigResearch.recipes[rodSpiritualRecipe] = addArcaneCraftingRecipe("",
																			 ItemStack(naturalWandRod, 1, 2),
																			 AspectList()
																				 .add(Aspect.AIR, WandRod.rods[rodSpiritualStaff]!!.craftCost)
																				 .add(Aspect.FIRE, WandRod.rods[rodSpiritualStaff]!!.craftCost)
																				 .add(Aspect.WATER, WandRod.rods[rodSpiritualStaff]!!.craftCost)
																				 .add(Aspect.EARTH, WandRod.rods[rodSpiritualStaff]!!.craftCost)
																				 .add(Aspect.ORDER, WandRod.rods[rodSpiritualStaff]!!.craftCost)
																				 .add(Aspect.ENTROPY, WandRod.rods[rodSpiritualStaff]!!.craftCost),
																			 "DSP", " RS", "R D",
																			 'R', ItemStack(naturalWandRod, 1, 1),
																			 'S', LIFE_ESSENCE,
																			 'D', DRAGONSTONE,
																			 'P', ItemStack(ConfigItems.itemResource, 1, 15)
		)
		
		ConfigResearch.recipes[pureElementiumRecipe] = addCrucibleRecipe(pureElementiumResearch,
																		 ItemStack(ConfigItems.itemNugget, 1, AlfheimConfigHandler.elementiumClusterMeta),
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
		
		if (AlfheimConfigHandler.enableElvenStory) addESMRecipes()
		
		addSmelting(ItemStack(alfheimThaumOre, 1, 0),
					ItemStack(ConfigItems.itemResource, 1, 3), // Cinnabar
					1f
		)
		
		addSmelting(ItemStack(alfheimThaumOre, 1, 7),
					ItemStack(ConfigItems.itemResource, 1, 6), // Amber
					1f
		)
		
		addSmelting(ItemStack(ConfigItems.itemNugget, 1, AlfheimConfigHandler.elementiumClusterMeta),
					ItemStack(ModItems.manaResource, 2, 7), // Elementium
					1f
		)
		
		addSmeltingBonus(ELEMENTIUM_ORE,
						 ItemStack(ModItems.manaResource, 0, 19)        // from ore
		)
		
		addSmeltingBonus(ItemStack(ConfigItems.itemNugget, 1, AlfheimConfigHandler.elementiumClusterMeta),
						 ItemStack(ModItems.manaResource, 0, 19)        // from cluster
		)
	}
	
	fun addESMRecipes() {
		CraftingManager.getInstance().recipeList.add(recipeElementiumWandCap)
		
		var cap = WandCap.caps[capElementiumName]!!
		ASJReflectionHelper.setValue(cap, 0.95f, "baseCostModifier")
		cap.craftCost = 5
		
		cap = WandCap.caps[capElvoriumName]!!
		ASJReflectionHelper.setValue(cap, 0.85f, "baseCostModifier")
		cap.craftCost = 8
	}
	
	fun removeESMRecipes() {
		CraftingManager.getInstance().recipeList.remove(recipeElementiumWandCap)
		
		var cap = WandCap.caps[capElementiumName]!!
		ASJReflectionHelper.setValue(cap, 0.9f, "baseCostModifier")
		cap.craftCost = 6
		
		cap = WandCap.caps[capElvoriumName]!!
		ASJReflectionHelper.setValue(cap, 0.8f, "baseCostModifier")
		cap.craftCost = 9
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
					 -3, 2, 1, ItemStack(ConfigItems.itemNugget, 1, AlfheimConfigHandler.elementiumClusterMeta))
			
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
