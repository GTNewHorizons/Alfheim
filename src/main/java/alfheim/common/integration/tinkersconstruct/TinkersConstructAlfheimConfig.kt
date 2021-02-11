package alfheim.common.integration.tinkersconstruct

import alexsocol.asjlib.*
import alfheim.AlfheimCore
import alfheim.api.AlfheimAPI
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.item.AlfheimItems
import alfheim.common.item.material.ElvenResourcesMetas
import cpw.mods.fml.common.event.FMLInterModComms
import net.minecraft.block.Block
import net.minecraft.init.Items
import net.minecraft.item.*
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.fluids.*
import tconstruct.TConstruct
import tconstruct.library.TConstructRegistry
import tconstruct.library.crafting.Smeltery
import tconstruct.smeltery.TinkerSmeltery
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.item.ModItems
import java.awt.Color

object TinkersConstructAlfheimConfig {
	
	fun loadConfig() {
		TinkersConstructAlfheimModule
		
		// Smelting materials
		if (AlfheimConfigHandler.materialIDs[ELEMENTIUM] != -1) addMaterial(AlfheimConfigHandler.materialIDs[ELEMENTIUM], "Elementium", BotaniaAPI.elementiumToolMaterial, 1.4f, Color(0xDC6DF2).rgb, EnumChatFormatting.LIGHT_PURPLE.toString(), 52, 5.3f, 3.1f, 0.9f, 1)
		if (AlfheimConfigHandler.materialIDs[ELVORIUM] != -1) addMaterial(AlfheimConfigHandler.materialIDs[ELVORIUM], "Elvorium", AlfheimAPI.elvoriumToolMaterial, 2.4f, Color(0xE58A2E).rgb, EnumChatFormatting.GOLD.toString(), 58, 5.5f, 3.5f, 2.8f, 2)
		if (AlfheimConfigHandler.materialIDs[MANASTEEL] != -1) addMaterial(AlfheimConfigHandler.materialIDs[MANASTEEL], "Manasteel", BotaniaAPI.manasteelToolMaterial, 1.2f, Color(0x4C9ED9).rgb, EnumChatFormatting.AQUA.toString(), 56, 5.1f, 3.0f, 0.7f, 1)
		if (AlfheimConfigHandler.materialIDs[MAUFTRIUM] != -1) addMaterial(AlfheimConfigHandler.materialIDs[MAUFTRIUM], "Mauftrium", AlfheimAPI.mauftriumToolmaterial, 4.5f, Color(0xFFE359).rgb, EnumChatFormatting.YELLOW.toString(), 64, 5.6f, 4.0f, 3.6f, 3)
		if (AlfheimConfigHandler.materialIDs[TERRASTEEL] != -1) addMaterial(AlfheimConfigHandler.materialIDs[TERRASTEEL], "Terrasteel", BotaniaAPI.terrasteelToolMaterial, 2.2f, Color(0x52CC29).rgb, EnumChatFormatting.GREEN.toString(), 60, 5.4f, 3.8f, 2.5f, 1)
		
		if (AlfheimConfigHandler.materialIDs[ELEMENTIUM] != -1) addSmelteryMeltCastGroup(TinkersConstructAlfheimModule.liquidElementium, ModBlocks.storage, 2, ModItems.manaResource, 7, ModItems.manaResource, 19)
		if (AlfheimConfigHandler.materialIDs[ELVORIUM] != -1) addSmelteryMeltCastGroup(TinkersConstructAlfheimModule.liquidElvorium, AlfheimBlocks.alfStorage, 0, AlfheimItems.elvenResource, ElvenResourcesMetas.ElvoriumIngot, AlfheimItems.elvenResource, ElvenResourcesMetas.ElvoriumNugget)
		if (AlfheimConfigHandler.materialIDs[MANASTEEL] != -1) addSmelteryMeltCastGroup(TinkersConstructAlfheimModule.liquidManasteel, ModBlocks.storage, 0, ModItems.manaResource, 0, ModItems.manaResource, 17)
		if (AlfheimConfigHandler.materialIDs[MAUFTRIUM] != -1) addSmelteryMeltCastGroup(TinkersConstructAlfheimModule.liquidMauftrium, AlfheimBlocks.alfStorage, 1, AlfheimItems.elvenResource, ElvenResourcesMetas.MauftriumIngot, AlfheimItems.elvenResource, ElvenResourcesMetas.MauftriumNugget)
		if (AlfheimConfigHandler.materialIDs[TERRASTEEL] != -1) addSmelteryMeltCastGroup(TinkersConstructAlfheimModule.liquidTerrasteel, ModBlocks.storage, 1, ModItems.manaResource, 4, ModItems.manaResource, 18)
		
		// Casting liquid to buckets
		TinkersConstructAlfheimModule.naturalFluids.forEachIndexed { id, it ->
			if (id == -1) return@forEachIndexed
			
			addPartCastingMaterial(AlfheimConfigHandler.materialIDs[id], it.name)
			TConstructRegistry.getTableCasting().addCastingRecipe(ItemStack(TinkersConstructAlfheimModule.naturalBucket, 1, id), FluidStack(it, 1000), ItemStack(Items.bucket), true, 50)
			FluidContainerRegistry.registerFluidContainer(FluidStack(it, 1000), ItemStack(TinkersConstructAlfheimModule.naturalBucket, 1, id), ItemStack(Items.bucket))
		}
		
		// Building materials
		if (AlfheimConfigHandler.materialIDs[LIVIVNGWOOD] != -1) addMaterial(AlfheimConfigHandler.materialIDs[LIVIVNGWOOD], "Livingwood", 1, 175, 550, 3, 1.1f, Color(0x4D2113).rgb, EnumChatFormatting.DARK_RED.toString(), 16, 3.4f, 0.7F, 0.8F)
		if (AlfheimConfigHandler.materialIDs[DREAMWOOD] != -1) addMaterial(AlfheimConfigHandler.materialIDs[DREAMWOOD], "Dreamwood", 1, 200, 600, 3, 1.2f, Color(0xCED9D9).rgb, EnumChatFormatting.GRAY.toString(), 24, 3.2f, 0.6F, 1.1F)
		if (AlfheimConfigHandler.materialIDs[LIVINGROCK] != -1) addMaterial(AlfheimConfigHandler.materialIDs[LIVINGROCK], "Livingrock", 1, 131, 460, 1, 0.5f, Color(0xFFFEE8).rgb, EnumChatFormatting.GRAY.toString(), 80, 1.1f, 2.0F, 1.0F, stoneBound = 1f)
		
		if (AlfheimConfigHandler.materialIDs[LIVIVNGWOOD] != -1) ModBlocks.livingwood.toItem()?.let { addPartBuilderMaterial(AlfheimConfigHandler.materialIDs[LIVIVNGWOOD], ModItems.manaResource, 3, it, 0, 4) }
		if (AlfheimConfigHandler.materialIDs[DREAMWOOD] != -1) ModBlocks.dreamwood.toItem()?.let { addPartBuilderMaterial(AlfheimConfigHandler.materialIDs[DREAMWOOD], ModItems.manaResource, 13, it, 0, 4) }
		if (AlfheimConfigHandler.materialIDs[LIVINGROCK] != -1) ModBlocks.livingrock.toItem()?.let { addPartBuilderMaterial(AlfheimConfigHandler.materialIDs[LIVINGROCK], it, 0, null, 0, 4) }
		
		// Bowstring
		if (AlfheimConfigHandler.materialIDs[REDSTRING] != -1) TConstructRegistry.addBowstringMaterial(AlfheimConfigHandler.materialIDs[REDSTRING], 2, ItemStack(ModItems.manaResource, 1, 12), ItemStack(TinkersConstructAlfheimModule.naturalMaterial), 0.9f, 0.8f, 1.2f, Color(0xE52222).rgb)
		if (AlfheimConfigHandler.materialIDs[MANASTRING] != -1) TConstructRegistry.addBowstringMaterial(AlfheimConfigHandler.materialIDs[MANASTRING], 2, ItemStack(ModItems.manaResource, 1, 16), ItemStack(TinkersConstructAlfheimModule.naturalMaterial, 1, 1), 0.9f, 1.1f, 1.1f, Color(0xCCFFF2).rgb)
		
		if (!AlfheimCore.stupidMode && AlfheimConfigHandler.materialIDs[MAUFTRIUM] != -1)
			TConstructRegistry.getBasinCasting().addCastingRecipe(ItemStack(ModBlocks.spreader, 1, 4), FluidStack(TinkersConstructAlfheimModule.liquidMauftrium, TConstruct.ingotLiquidValue * 8), ItemStack(ModBlocks.spreader, 1, 3), true, 360)
	}
	
	/**
	 * Main [Tool Material][tconstruct.library.tools.ToolMaterial] registration function
	 *
	 * Inherits main data from [material]
	 *
	 * [bowSpeed] and [projSpeed] are for [Bow Material][tconstruct.library.tools.BowMaterial]
	 *
	 * [projMass] and [projFrag] are for [Arrow Material][tconstruct.library.tools.ArrowMaterial]
	 */
	fun addMaterial(id: Int, name: String, material: Item.ToolMaterial, handle: Float, color: Int, style: String? = null, bowSpeed: Int? = null, projSpeed: Float? = null, projMass: Float? = null, projFrag: Float? = null, reinforced: Int = 0, stoneBound: Float = 0f, tooltip: String = "material.$name") = addMaterial(id, name, material.harvestLevel, material.maxUses, material.efficiencyOnProperMaterial.I * 100, material.damageVsEntity.I, handle, color, style, bowSpeed, projSpeed, projMass, projFrag, reinforced, stoneBound, tooltip)
	
	/**
	 * Main [Tool Material][tconstruct.library.tools.ToolMaterial] registration function
	 *
	 * [bowSpeed] and [projSpeed] are for [Bow Material][tconstruct.library.tools.BowMaterial]
	 *
	 * [projMass] and [projFrag] are for [Arrow Material][tconstruct.library.tools.ArrowMaterial]
	 */
	fun addMaterial(id: Int, name: String, harvest: Int, durability: Int, efficiency: Int, damage: Int, handle: Float, color: Int, style: String? = null, bowSpeed: Int? = null, projSpeed: Float? = null, projMass: Float? = null, projFrag: Float? = null, reinforced: Int = 0, stoneBound: Float = 0f, tooltip: String = "material.$name") {
		TConstructRegistry.addToolMaterial(id, name, tooltip, harvest, durability, efficiency, damage, handle, reinforced, stoneBound, style, color)
		
		if ((bowSpeed == null) xor (projSpeed == null)) throw IllegalArgumentException("Both bowSpeed ($bowSpeed) and projSpeed ($projSpeed) must be present")
		if (bowSpeed != null && projSpeed != null) TConstructRegistry.addBowMaterial(id, bowSpeed, projSpeed)
		
		if ((projMass == null) xor (projFrag == null)) throw IllegalArgumentException("Both projMass ($projMass) and projFrag ($projFrag) must be present")
		if (projMass != null && projFrag != null) TConstructRegistry.addArrowMaterial(id, projMass, projFrag)
	}
	
	/**
	 * Adds an [item] with [meta] to be able to repair [id]th material
	 */
	fun addMaterialItem(id: Int, cost: Int, item: Item, meta: Int) {
		val tag = NBTTagCompound()
		tag.setInteger("MaterialId", id)
		
		val it = NBTTagCompound()
		ItemStack(item, 1, meta).writeToNBT(it)
		tag.setTag("Item", it)
		
		tag.setInteger("Value", cost)
		
		FMLInterModComms.sendMessage("TConstruct", "addMaterialItem", tag)
	}
	
	/**
	 * Add [fullItem] with [fullMeta] as full part and [shardItem] with [shardMeta] as shard part for [id] material
	 * to become usable in Part Builder
	 *
	 * Note: DO NOT use with [addPartCastingMaterial]
	 */
	fun addPartBuilderMaterial(id: Int, fullItem: Item, fullMeta: Int, shardItem: Item? = null, shardMeta: Int = 0, cost: Int) {
		val tag = NBTTagCompound()
		tag.setInteger("MaterialId", id)
		
		val full = NBTTagCompound()
		ItemStack(fullItem, 1, fullMeta).writeToNBT(full)
		tag.setTag("Item", full)
		
		if (shardItem != null) {
			val shard = NBTTagCompound()
			ItemStack(shardItem, 1, shardMeta).writeToNBT(shard)
			tag.setTag("Shard", shard)
		}
		
		tag.setInteger("Value", cost)
		FMLInterModComms.sendMessage("TConstruct", "addPartBuilderMaterial", tag)
	}
	
	/**
	 * Adds [id] material to be casted out of [name] fluid
	 *
	 * Note: DO NOT use with [addPartBuilderMaterial]
	 */
	fun addPartCastingMaterial(id: Int, name: String) {
		val tag = NBTTagCompound()
		tag.setInteger("MaterialId", id)
		tag.setString("FluidName", name)
		
		FMLInterModComms.sendMessage("TConstruct", "addPartCastingMaterial", tag)
	}
	
	/**
	 * Group function to instantly add [block], [ingot] and [nugget] to be smeltable and castable in appropriate form
	 *
	 * Note: [ingot] and [nugget] are optional params while [block] is required because of render
	 */
	fun addSmelteryMeltCastGroup(fluid: Fluid, block: Block, blockMeta: Int = 0, ingot: Item? = null, ingotMeta: Int = 0, nugget: Item? = null, nuggetMeta: Int = 0, blockOut: Int = TConstruct.blockLiquidValue, ingotOut: Int = TConstruct.ingotLiquidValue, nuggetOut: Int = TConstruct.nuggetLiquidValue) {
		addSmelteryMelting(block, blockMeta, fluid, blockOut)
		TConstructRegistry.getBasinCasting().addCastingRecipe(ItemStack(block, 1, blockMeta), FluidStack(fluid, blockOut), null, true, 100)
		
		if (ingot != null) {
			addSmelteryMelting(ingot, ingotMeta, block, blockMeta, fluid, ingotOut)
			TConstructRegistry.getTableCasting().addCastingRecipe(ItemStack(ingot, 1, ingotMeta), FluidStack(fluid, ingotOut), ItemStack(TinkerSmeltery.metalPattern, 1, 0), false, 50)
		}
		
		if (nugget != null) {
			addSmelteryMelting(nugget, nuggetMeta, block, blockMeta, fluid, nuggetOut)
			TConstructRegistry.getTableCasting().addCastingRecipe(ItemStack(nugget, 1, nuggetMeta), FluidStack(fluid, nuggetOut), ItemStack(TinkerSmeltery.metalPattern, 1, 27), false, 50)
		}
	}
	
	/**
	 * Add [block] with [blockMeta] to become smeltable to [cost] mB of [fluid]
	 *
	 * Renders as is
	 */
	fun addSmelteryMelting(block: Block, blockMeta: Int, fluid: Fluid, cost: Int) = block.toItem()?.let { addSmelteryMelting(it, blockMeta, block, blockMeta, fluid, cost) }
	
	/**
	 * Add [item] with [itemMeta] to become smeltable to [cost] mB of [fluid]
	 *
	 * Renders in smeltery as [renderBlock] with [renderBlockMeta]
	 */
	fun addSmelteryMelting(item: Item, itemMeta: Int, renderBlock: Block, renderBlockMeta: Int, fluid: Fluid, cost: Int) {
		Smeltery.addMelting(ItemStack(item, 1, itemMeta), renderBlock, renderBlockMeta, fluid.temperature, FluidStack(fluid, cost))
	}
	
	const val ELEMENTIUM = 0
	const val ELVORIUM = 1
	const val MANASTEEL = 2
	const val MAUFTRIUM = 3
	const val TERRASTEEL = 4
	
	const val DREAMWOOD = 5
	const val LIVIVNGWOOD = 6
	const val LIVINGROCK = 7
	
	const val REDSTRING = 8
	const val MANASTRING = 9
}