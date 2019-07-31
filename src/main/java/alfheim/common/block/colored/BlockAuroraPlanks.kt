package alfheim.common.block.colored

import alfheim.common.block.ShadowFoxBlocks
import alfheim.common.block.base.BlockMod
import alfheim.common.block.material.MaterialCustomSmeltingWood
import alfheim.common.block.tile.TileTreeCrafter
import alfheim.common.lexicon.ShadowFoxLexiconData
import cpw.mods.fml.common.IFuelHandler
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.*
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.wand.IWandable

class BlockAuroraPlanks: BlockMod(MaterialCustomSmeltingWood.instance), ILexiconable, IFuelHandler, IWandable {
	
	private val name = "auroraPlanks"
	
	init {
		blockHardness = 2F
		
		stepSound = soundTypeWood
		setBlockName(name)
		
		tickRandomly = true
		GameRegistry.registerFuelHandler(this)
	}
	
	@SideOnly(Side.CLIENT)
	override fun colorMultiplier(world: IBlockAccess, x: Int, y: Int, z: Int) = BlockAuroraDirt.getBlockColor(x, y, z)
	
	override fun isToolEffective(type: String?, metadata: Int) = (type != null && type == "axe")
	
	override fun getHarvestTool(metadata: Int) = "axe"
	
	override fun onUsedByWand(p0: EntityPlayer?, p1: ItemStack?, p2: World?, p3: Int, p4: Int, p5: Int, p6: Int): Boolean {
		if (p2 != null) {
			if (TileTreeCrafter.canEnchanterExist(p2, p3, p4, p5)) {
				p2.setBlock(p3, p4, p5, ShadowFoxBlocks.treeCrafterBlockRB, p6, 3)
				p2.playSoundEffect(p3.toDouble(), p4.toDouble(), p5.toDouble(), "botania:enchanterBlock", 0.5F, 0.6F)
				
				return true
			}
		}
		
		return false
	}
	
	override fun getPickBlock(target: MovingObjectPosition?, world: World, x: Int, y: Int, z: Int): ItemStack {
		val meta = world.getBlockMetadata(x, y, z)
		return ItemStack(this, 1, meta)
	}
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = ShadowFoxLexiconData.aurora
	
	override fun getBurnTime(fuel: ItemStack) = if (fuel.item == Item.getItemFromBlock(this)) 300 else 0
}