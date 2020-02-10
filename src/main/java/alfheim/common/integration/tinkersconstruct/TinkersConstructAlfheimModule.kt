package alfheim.common.integration.tinkersconstruct

import alfheim.common.block.AlfheimBlocks
import alfheim.common.integration.tinkersconstruct.TinkersConstructAPI.registerSmelteryFluid
import alfheim.common.item.compat.tinkersconstruct.ItemNaturalBucket
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.item.Item
import net.minecraftforge.fluids.Fluid
import tconstruct.library.crafting.FluidType
import tconstruct.smeltery.TinkerSmeltery
import vazkii.botania.common.block.ModBlocks

object TinkersConstructAlfheimModule {
	
	val liquidElvorium: Fluid
	val liquidElvoriumBlock: Block
	
	val liquidElementium: Fluid
	val liquidElementiumBlock: Block
	
	val liquidManasteel: Fluid
	val liquidManasteelBlock: Block
	
	val liquidMauftrium: Fluid
	val liquidMauftriumBlock: Block
	
	val liquidTerrasteel: Fluid
	val liquidTerrasteelBlock: Block
	
	val naturalFluids: Array<Fluid>
	val naturalFluidBlocks: Array<Block>
	
	val naturalBucket: Item
	
	init {
		liquidElementium = registerSmelteryFluid("Elementium", ModBlocks.storage, 2)
		liquidElementiumBlock = liquidElementium.block
		
		liquidElvorium = registerSmelteryFluid("Elvorium", AlfheimBlocks.alfStorage, 0)
		liquidElvoriumBlock = liquidElvorium.block
		
		liquidManasteel = registerSmelteryFluid("Manasteel", ModBlocks.storage, 0)
		liquidManasteelBlock = liquidManasteel.block
		
		liquidMauftrium = registerSmelteryFluid("Mauftrium", AlfheimBlocks.alfStorage, 1)
		liquidMauftriumBlock = liquidMauftrium.block
		
		liquidTerrasteel = registerSmelteryFluid("Terrasteel", ModBlocks.storage, 1)
		liquidTerrasteelBlock = liquidTerrasteel.block
		
		naturalFluids = arrayOf(liquidElementium, liquidElvorium, liquidManasteel, liquidMauftrium, liquidTerrasteel)
		naturalFluidBlocks = arrayOf(liquidElementiumBlock, liquidElvoriumBlock, liquidManasteelBlock, liquidMauftriumBlock, liquidTerrasteelBlock)
		
		naturalBucket = ItemNaturalBucket()
	}
}

object TinkersConstructAPI {
	
	fun registerSmelteryFluid(name: String, renderBlock: Block, renderMeta: Int, texture: String = "liquid_$name", fluidName: String = "$name.molten", blockName: String = "fluid.molten.$name", density: Int = 3000, viscosity: Int = 6000, temperature: Int = 1300, material: Material = Material.lava) =
		TinkerSmeltery.registerFluid(name, fluidName, blockName, texture, density, viscosity, temperature, material).also {
			FluidType.registerFluidType(name, renderBlock, renderMeta, it.temperature, it, false)
		}!!
}