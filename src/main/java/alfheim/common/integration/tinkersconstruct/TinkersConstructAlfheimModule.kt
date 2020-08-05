package alfheim.common.integration.tinkersconstruct

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.integration.tinkersconstruct.TinkersConstructAlfheimConfig.ELEMENTIUM
import alfheim.common.integration.tinkersconstruct.TinkersConstructAlfheimConfig.ELVORIUM
import alfheim.common.integration.tinkersconstruct.TinkersConstructAlfheimConfig.MANASTEEL
import alfheim.common.integration.tinkersconstruct.TinkersConstructAlfheimConfig.MAUFTRIUM
import alfheim.common.integration.tinkersconstruct.TinkersConstructAlfheimConfig.TERRASTEEL
import alfheim.common.integration.tinkersconstruct.modifier.*
import alfheim.common.item.compat.tinkersconstruct.*
import gloomyfolken.hooklib.asm.*
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.init.*
import net.minecraft.item.*
import net.minecraft.util.StatCollector
import net.minecraftforge.fluids.*
import net.minecraftforge.oredict.OreDictionary
import tconstruct.library.TConstructRegistry
import tconstruct.library.client.TConstructClientRegistry
import tconstruct.library.crafting.*
import tconstruct.library.modifier.IModifyable
import tconstruct.library.weaponry.IAmmo
import tconstruct.modifiers.tools.ModInteger
import tconstruct.smeltery.TinkerSmeltery
import tconstruct.tools.TinkerTools
import thaumcraft.common.config.ConfigItems
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.core.handler.PixieHandler
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.lib.LibOreDict

object TinkersConstructAlfheimModule {
	
	lateinit var liquidElvorium: Fluid
	lateinit var liquidElvoriumBlock: Block
	
	lateinit var liquidElementium: Fluid
	lateinit var liquidElementiumBlock: Block
	
	lateinit var liquidManasteel: Fluid
	lateinit var liquidManasteelBlock: Block
	
	lateinit var liquidMauftrium: Fluid
	lateinit var liquidMauftriumBlock: Block
	
	lateinit var liquidTerrasteel: Fluid
	lateinit var liquidTerrasteelBlock: Block
	
	val naturalFluids: Array<Fluid>
	val naturalFluidBlocks: Array<Block>
	
	val naturalBucket: Item
	val naturalMaterial: Item
	
	val manaGenMaterials = intArrayOf(AlfheimConfigHandler.materialIDs[ELVORIUM], AlfheimConfigHandler.materialIDs[MAUFTRIUM], AlfheimConfigHandler.materialIDs[TERRASTEEL])
	val manaGenDelay = hashMapOf(manaGenMaterials[0] to 10, manaGenMaterials[1] to 5, manaGenMaterials[2] to 12)
	
	val manaRepairMaterials = IntArray(5) { AlfheimConfigHandler.materialIDs[it] }
	val manaRepairCost = hashMapOf(manaRepairMaterials[0] to 4, manaRepairMaterials[1] to 5, manaRepairMaterials[2] to 4, manaRepairMaterials[3] to 7, manaRepairMaterials[4] to 6)
	
	init {
		// fuck you Forge and your fluid names lowercasing
		
		val nfs = ArrayList<Fluid>()
		val nfbs = ArrayList<Block>()
		
		if (AlfheimConfigHandler.materialIDs[ELEMENTIUM] != -1) {
			liquidElementium = registerSmelteryFluid("elementium", ModBlocks.storage, 2).also { nfs.add(it) }
			liquidElementiumBlock = liquidElementium.block.also { nfbs.add(it) }
		} else {
			nfs.add(FluidRegistry.WATER)
			nfbs.add(Blocks.flowing_water)
		}
		
		if (AlfheimConfigHandler.materialIDs[ELVORIUM] != -1) {
			liquidElvorium = registerSmelteryFluid("elvorium", AlfheimBlocks.alfStorage, 0).also { nfs.add(it) }
			liquidElvoriumBlock = liquidElvorium.block.also { nfbs.add(it) }
		} else {
			nfs.add(FluidRegistry.WATER)
			nfbs.add(Blocks.flowing_water)
		}
		
		if (AlfheimConfigHandler.materialIDs[MANASTEEL] != -1) {
			liquidManasteel = registerSmelteryFluid("manasteel", ModBlocks.storage, 0).also { nfs.add(it) }
			liquidManasteelBlock = liquidManasteel.block.also { nfbs.add(it) }
		} else {
			nfs.add(FluidRegistry.WATER)
			nfbs.add(Blocks.flowing_water)
		}
		
		if (AlfheimConfigHandler.materialIDs[MAUFTRIUM] != -1) {
			liquidMauftrium = registerSmelteryFluid("mauftrium", AlfheimBlocks.alfStorage, 1).also { nfs.add(it) }
			liquidMauftriumBlock = liquidMauftrium.block.also { nfbs.add(it) }
		} else {
			nfs.add(FluidRegistry.WATER)
			nfbs.add(Blocks.flowing_water)
		}
		
		if (AlfheimConfigHandler.materialIDs[TERRASTEEL] != -1) {
			liquidTerrasteel = registerSmelteryFluid("terrasteel", ModBlocks.storage, 1).also { nfs.add(it) }
			liquidTerrasteelBlock = liquidTerrasteel.block.also { nfbs.add(it) }
		} else {
			nfs.add(FluidRegistry.WATER)
			nfbs.add(Blocks.flowing_water)
		}
		
		naturalFluids = nfs.toTypedArray()
		naturalFluidBlocks = nfbs.toTypedArray()
		
		naturalBucket = ItemNaturalBucket()
		naturalMaterial = ItemNaturalMaterial()
		
		ModifiersExtender
		
		ModifyBuilder.registerModifier(ModManaRepair)
		TConstructRegistry.registerActiveToolMod(AModNatural)
		
		if (ASJUtilities.isClient)
			TConstructRegistry.getToolMapping().forEach {
				if (it is IAmmo) return@forEach
				
				TConstructClientRegistry.addEffectRenderMapping(it, AlfheimConfigHandler.modifierIDs[0], "tinker", "modifiers/ManaCore/mana_core", false)
			}
	}
	
	fun registerSmelteryFluid(name: String, renderBlock: Block, renderMeta: Int, texture: String = "liquids/liquid_$name", fluidName: String = "$name.molten", blockName: String = "fluid.molten.$name", density: Int = 3000, viscosity: Int = 6000, temperature: Int = 1300, material: Material = Material.lava) =
		TinkerSmeltery.registerFluid(name, fluidName, blockName, texture, density, viscosity, temperature, material).also {
			FluidType.registerFluidType(name, renderBlock, renderMeta, it.temperature, it, false)
		}!!
}

object ModifiersExtender {
	
	val sharpnessOres = arrayOf<String>(LibOreDict.PRISMARINE_SHARD, *LibOreDict.QUARTZ, alfheim.api.lib.LibOreDict.RAINBOW_QUARTZ)
	val sharpnessOreBlocks = arrayOf(LibOreDict.PRISMARINE_BLOCK, *(LibOreDict.QUARTZ.map { "block${it.capitalize()}" }.toTypedArray()), alfheim.api.lib.LibOreDict.RAINBOW_QUARTZ_BLOCK)
	
	init {
		ModifyBuilder.registerModifier(ModInteger(arrayOf(ItemStack(ModItems.vineBall)), 4, "Moss", 3, "\u00a72", StatCollector.translateToLocal("modifier.tool.moss")))
		
		sharpnessOreBlocks.forEach { ore -> OreDictionary.getOres(ore).forEach { stack -> TinkerTools.modAttack.addStackToMatchList(stack, 8) } }
		sharpnessOres.forEach { ore -> OreDictionary.getOres(ore).forEach { stack -> TinkerTools.modAttack.addStackToMatchList(stack, 2) } }
	}
}

object TraitFairySpawner {
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ON_TRUE, floatReturnConstant = 0.1f)
	fun getChance(handler: PixieHandler, stack: ItemStack?): Boolean {
		if (!AlfheimCore.TiCLoaded) return false
		
		if (stack == null) return false
		val tool = stack.item as? IModifyable ?: return false
		val tag = ItemNBTHelper.getCompound(stack, tool.baseTagName, true) ?: return false
		val headMaterial = tag.getInteger("Head")
		if (headMaterial == AlfheimConfigHandler.materialIDs[ELEMENTIUM]) return true
		return false
	}
}