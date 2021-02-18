package alfheim.common.block.magtrees.nether

import alexsocol.asjlib.toItem
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.colored.rainbow.*
import alfheim.common.item.block.*
import cpw.mods.fml.common.IFuelHandler
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.*
import net.minecraft.item.ItemStack
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.util.ForgeDirection

class BlockNetherWoodSlab(full: Boolean, source: Block = AlfheimBlocks.netherPlanks): BlockRainbowWoodSlab(full, source) {
	
	init {
		setLightLevel(0.5f)
	}
	
	override fun getFullBlock() = AlfheimBlocks.netherSlabsFull as BlockSlab
	
	override fun register() {
		GameRegistry.registerBlock(this, ItemSlabMod::class.java, name)
	}
	
	override fun getSingleBlock() = AlfheimBlocks.netherSlabs as BlockSlab
	
	override fun isFlammable(world: IBlockAccess?, x: Int, y: Int, z: Int, face: ForgeDirection?) = false
	
	override fun getFireSpreadSpeed(world: IBlockAccess?, x: Int, y: Int, z: Int, face: ForgeDirection?) = 0
	
	override fun getBurnTime(fuel: ItemStack) = if (fuel.item === this.toItem()) if (field_150004_a) 2000 else 1000 else 0
}

class BlockNetherWoodStairs(source: Block = AlfheimBlocks.netherPlanks): BlockRainbowWoodStairs(source), IFuelHandler {
	
	init {
		setLightLevel(0.5f)
		
		GameRegistry.registerFuelHandler(this)
	}
	
	override fun register() {
		GameRegistry.registerBlock(this, ItemBlockLeavesMod::class.java, name)
	}
	
	override fun isFlammable(world: IBlockAccess?, x: Int, y: Int, z: Int, face: ForgeDirection?) = false
	
	override fun getFireSpreadSpeed(world: IBlockAccess?, x: Int, y: Int, z: Int, face: ForgeDirection?) = 0
	
	override fun getBurnTime(fuel: ItemStack) = if (fuel.item === this.toItem()) 2000 else 0
}
