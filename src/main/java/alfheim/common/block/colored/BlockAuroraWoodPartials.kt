package alfheim.common.block.colored

import alexsocol.asjlib.toBlock
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.base.*
import alfheim.common.item.block.ItemColoredSlabMod
import cpw.mods.fml.common.IFuelHandler
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.*
import vazkii.botania.api.lexicon.ILexiconable

class BlockAuroraWoodSlab(full: Boolean, source: Block = AlfheimBlocks.auroraPlanks): BlockSlabMod(full, 0, source, source.unlocalizedName.replace("tile.".toRegex(), "") + "Slab" + (if (full) "Full" else "") + "17"), IFuelHandler {
	
	init {
		setResistance(10f)
		GameRegistry.registerFuelHandler(this)
	}
	
	override fun register() {
		GameRegistry.registerBlock(this, ItemColoredSlabMod::class.java, name)
	}
	
	@SideOnly(Side.CLIENT)
	override fun colorMultiplier(world: IBlockAccess, x: Int, y: Int, z: Int) = BlockAuroraDirt.getBlockColor(x, y, z)
	
	override fun isToolEffective(type: String?, metadata: Int) = (type != null && type == "axe")
	
	override fun getHarvestTool(metadata: Int) = "axe"
	
	override fun getFullBlock() = AlfheimBlocks.auroraSlabFull as BlockSlab
	
	override fun getSingleBlock() = AlfheimBlocks.auroraSlab as BlockSlab
	
	override fun getEntry(world: World?, x: Int, y: Int, z: Int, player: EntityPlayer?, lexicon: ItemStack?) = (source as ILexiconable).getEntry(world, x, y, z, player, lexicon)!!
	
	override fun getBurnTime(fuel: ItemStack) = if (fuel.item.toBlock() === this) if (field_150004_a) 300 else 150 else 0
}

class BlockAuroraWoodStairs(source: Block = AlfheimBlocks.auroraPlanks):
	BlockStairsMod(source, 0, source.unlocalizedName.replace("tile.".toRegex(), "") + "Stairs17"), ILexiconable {
	
	@SideOnly(Side.CLIENT)
	override fun colorMultiplier(world: IBlockAccess, x: Int, y: Int, z: Int) = BlockAuroraDirt.getBlockColor(x, y, z)
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = (source as ILexiconable).getEntry(p0, p1, p2, p3, p4, p5)!!
	
	override fun isToolEffective(type: String?, metadata: Int) = (type != null && type == "axe")
	
	override fun getHarvestTool(metadata: Int) = "axe"
}
