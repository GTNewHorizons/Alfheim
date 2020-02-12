package alfheim.common.integration.tinkersconstruct

import alfheim.AlfheimCore
import alfheim.api.AlfheimAPI
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.I
import alfheim.common.integration.tinkersconstruct.TinkersConstructAlfheimModule.liquidMauftrium
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
		addMaterial(AlfheimConfigHandler.materialIDs[0], "Elementium",	BotaniaAPI.elementiumToolMaterial,	1.4f,	Color(0xDC6DF2).rgb, EnumChatFormatting.LIGHT_PURPLE.toString(),	52, 5.3f, 3.1f, 0.9f, 1)
		addMaterial(AlfheimConfigHandler.materialIDs[1], "Elvorium",	AlfheimAPI.elvoriumToolMaterial,	2.4f,	Color(0xE58A2E).rgb, EnumChatFormatting.GOLD.toString(),			58, 5.5f, 3.5f, 2.8f, 2)
		addMaterial(AlfheimConfigHandler.materialIDs[2], "Manasteel",	BotaniaAPI.manasteelToolMaterial,	1.2f,	Color(0x4C9ED9).rgb, EnumChatFormatting.AQUA.toString(),			56, 5.1f, 3.0f, 0.7f, 1)
		addMaterial(AlfheimConfigHandler.materialIDs[3], "Mauftrium",	AlfheimAPI.mauftriumToolmaterial,	4.5f,	Color(0xFFE359).rgb, EnumChatFormatting.YELLOW.toString(),			64, 5.6f, 4.0f, 3.6f, 3)
		addMaterial(AlfheimConfigHandler.materialIDs[4], "Terrasteel",	BotaniaAPI.terrasteelToolMaterial,	2.2f,	Color(0x52CC29).rgb, EnumChatFormatting.GREEN.toString(),			60, 5.4f, 3.8f, 2.5f, 1)
		
		addSmelteryMeltCastGroup(TinkersConstructAlfheimModule.liquidElementium, ModBlocks.storage, 2, ModItems.manaResource, 7, ModItems.manaResource, 19)
		addSmelteryMeltCastGroup(TinkersConstructAlfheimModule.liquidElvorium, AlfheimBlocks.alfStorage, 0, AlfheimItems.elvenResource, ElvenResourcesMetas.ElvoriumIngot, AlfheimItems.elvenResource, ElvenResourcesMetas.ElvoriumNugget)
		addSmelteryMeltCastGroup(TinkersConstructAlfheimModule.liquidManasteel, ModBlocks.storage, 0, ModItems.manaResource, 0, ModItems.manaResource, 17)
		addSmelteryMeltCastGroup(liquidMauftrium, AlfheimBlocks.alfStorage, 1, AlfheimItems.elvenResource, ElvenResourcesMetas.MauftriumIngot, AlfheimItems.elvenResource, ElvenResourcesMetas.MauftriumNugget)
		addSmelteryMeltCastGroup(TinkersConstructAlfheimModule.liquidTerrasteel, ModBlocks.storage, 1, ModItems.manaResource, 4, ModItems.manaResource, 18)
		
		// Casting liquid to buckets
		TinkersConstructAlfheimModule.naturalFluids.forEachIndexed { id, it ->
			addPartCastingMaterial(AlfheimConfigHandler.materialIDs[id], it.name)
			TConstructRegistry.getTableCasting().addCastingRecipe(ItemStack(TinkersConstructAlfheimModule.naturalBucket, 1, id), FluidStack(it, 1000), ItemStack(Items.bucket), true, 50)
		}
		
		// Building materials
		addMaterial(AlfheimConfigHandler.materialIDs[5], "Livingwood",	1, 175, 550, 3,						1.1f,	Color(0x4D2113).rgb, EnumChatFormatting.DARK_RED.toString(),	16, 3.4f, 0.7F, 0.8F)
		addMaterial(AlfheimConfigHandler.materialIDs[6], "Dreamwood",	1, 200, 600, 3,						1.2f,	Color(0xCED9D9).rgb, EnumChatFormatting.GRAY.toString(),		24, 3.2f, 0.6F, 1.1F)
		
		addPartBuilderMaterial(AlfheimConfigHandler.materialIDs[5], ModItems.manaResource,	3,	Item.getItemFromBlock(ModBlocks.livingwood),	0, 4)
		addPartBuilderMaterial(AlfheimConfigHandler.materialIDs[6], ModItems.manaResource,	13,	Item.getItemFromBlock(ModBlocks.dreamwood),		0, 4)
		
		// Bowstring
		TConstructRegistry.addBowstringMaterial(AlfheimConfigHandler.materialIDs[7], 2, ItemStack(ModItems.manaResource, 1, 12), ItemStack(TinkersConstructAlfheimModule.naturalMaterial),			0.9f, 0.8f, 1.2f, Color(0xE52222).rgb)
		TConstructRegistry.addBowstringMaterial(AlfheimConfigHandler.materialIDs[8], 2, ItemStack(ModItems.manaResource, 1, 16), ItemStack(TinkersConstructAlfheimModule.naturalMaterial, 1, 1),	0.9f, 1.1f, 1.1f, Color(0xCCFFF2).rgb)
		
		if (!AlfheimCore.stupidMode)
			TConstructRegistry.getBasinCasting().addCastingRecipe(ItemStack(ModBlocks.spreader, 1, 4), FluidStack(liquidMauftrium, TConstruct.ingotLiquidValue * 8), ItemStack(ModBlocks.spreader, 1, 3), true, 360)
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
	fun addMaterial(id: Int, name: String, material: Item.ToolMaterial, handle: Float, color: Int, style: String? = null, bowSpeed: Int? = null, projSpeed: Float? = null, projMass: Float? = null, projFrag: Float? = null, reinforced: Int = 0, stoneBound: Float = 0f, tooltip: String = "material.$name")
		= addMaterial(id, name, material.harvestLevel, material.maxUses, material.efficiencyOnProperMaterial.I * 100, material.damageVsEntity.I, handle, color, style, bowSpeed, projSpeed, projMass, projFrag, reinforced, stoneBound, tooltip)
	
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
	fun addPartBuilderMaterial(id: Int, fullItem: Item, fullMeta: Int, shardItem: Item, shardMeta: Int, cost: Int) {
		val tag = NBTTagCompound()
		tag.setInteger("MaterialId", id)
		
		val full = NBTTagCompound()
		ItemStack(fullItem, 1, fullMeta).writeToNBT(full)
		tag.setTag("Item", full)
		
		val shard = NBTTagCompound()
		ItemStack(shardItem, 1, shardMeta).writeToNBT(shard)
		tag.setTag("Shard", shard)
		
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
	fun addSmelteryMelting(block: Block, blockMeta: Int, fluid: Fluid, cost: Int)
		= addSmelteryMelting(Item.getItemFromBlock(block), blockMeta, block, blockMeta, fluid, cost)
	
	/**
	 * Add [item] with [itemMeta] to become smeltable to [cost] mB of [fluid]
	 *
	 * Renders in smeltery as [renderBlock] with [renderBlockMeta]
	 */
	fun addSmelteryMelting(item: Item, itemMeta: Int, renderBlock: Block, renderBlockMeta: Int, fluid: Fluid, cost: Int) {
		Smeltery.addMelting(ItemStack(item, 1, itemMeta), renderBlock, renderBlockMeta, fluid.temperature, FluidStack(fluid, cost))
	}
}