package alfheim.common.block.colored

import alfheim.common.block.ShadowFoxBlocks
import alfheim.common.block.base.*
import cpw.mods.fml.common.IFuelHandler
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.world.*
import vazkii.botania.api.lexicon.ILexiconable

open class BlockAuroraWoodSlab(full: Boolean, source: Block = ShadowFoxBlocks.auroraPlanks): BlockSlabMod(full, 0, source, source.unlocalizedName.replace("tile.".toRegex(), "") + "Slab" + (if (full) "Full" else "")), IFuelHandler {
    
    init {
        setResistance(10.0f)
        GameRegistry.registerFuelHandler(this)
    }
	
	@SideOnly(Side.CLIENT)
	override fun colorMultiplier(world: IBlockAccess, x: Int, y: Int, z: Int) = BlockAuroraDirt.getBlockColor(x, y, z)
	
    override fun isToolEffective(type: String?, metadata: Int) = (type != null && type == "axe")
    
    override fun getHarvestTool(metadata: Int) = "axe"
    
    override fun getFullBlock() = ShadowFoxBlocks.auroraSlabsFull as BlockSlab
    
    override fun getSingleBlock() = ShadowFoxBlocks.auroraSlabs as BlockSlab
    
    override fun getEntry(world: World?, x: Int, y: Int, z: Int, player: EntityPlayer?, lexicon: ItemStack?) = (source as ILexiconable).getEntry(world, x, y, z, player, lexicon)!!
    
    override fun getBurnTime(fuel: ItemStack) = if (fuel.item == Item.getItemFromBlock(this)) 150 else 0
}

open class BlockAuroraWoodStairs(source: Block = ShadowFoxBlocks.auroraPlanks):
    BlockStairsMod(source, 0, source.unlocalizedName.replace("tile.".toRegex(), "") + "Stairs"), ILexiconable {
	
	@SideOnly(Side.CLIENT)
	override fun colorMultiplier(world: IBlockAccess, x: Int, y: Int, z: Int) = BlockAuroraDirt.getBlockColor(x, y, z)
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = (source as ILexiconable).getEntry(p0, p1, p2, p3, p4, p5)!!
	
	override fun isToolEffective(type: String?, metadata: Int) = (type != null && type == "axe")
	
	override fun getHarvestTool(metadata: Int) = "axe"
}
