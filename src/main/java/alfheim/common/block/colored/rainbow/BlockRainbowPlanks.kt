package alfheim.common.block.colored.rainbow

import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.base.BlockMod
import alfheim.common.block.material.MaterialCustomSmeltingWood
import alfheim.common.block.tile.TileTreeCrafter
import alfheim.common.core.util.D
import alfheim.common.item.block.ItemIridescentBlockMod
import alfheim.common.lexicon.ShadowFoxLexiconData
import cpw.mods.fml.common.IFuelHandler
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.wand.IWandable
import java.util.*

class BlockRainbowPlanks: BlockMod(MaterialCustomSmeltingWood.instance), ILexiconable, IFuelHandler, IWandable {
	
	private val name = "rainbowPlanks"
	
	init {
		blockHardness = 2F
		setLightLevel(0f)
		stepSound = soundTypeWood
		
		setBlockName(name)
		GameRegistry.registerFuelHandler(this)
	}
	
	override fun isInterpolated() = true
	
	override fun onUsedByWand(p0: EntityPlayer?, p1: ItemStack?, p2: World?, p3: Int, p4: Int, p5: Int, p6: Int): Boolean {
		if (p2 != null) {
			if (TileTreeCrafter.canEnchanterExist(p2, p3, p4, p5)) {
				p2.setBlock(p3, p4, p5, AlfheimBlocks.treeCrafterBlockRB, p6, 3)
				p2.playSoundEffect(p3.D, p4.D, p5.D, "botania:enchanterBlock", 0.5F, 0.6F)
				
				return true
			}
		}
		
		return false
	}
	
	override fun isToolEffective(type: String?, metadata: Int) = (type != null && type == "axe")
	
	override fun getHarvestTool(metadata: Int) = "axe"
	
	override fun shouldRegisterInNameSet() = false
	
	override fun damageDropped(par1: Int) = par1
	
	override fun setBlockName(name: String): Block {
		register(name)
		return super.setBlockName(name)
	}
	
	override fun quantityDropped(random: Random) = 1
	
	override fun getItemDropped(meta: Int, random: Random, fortune: Int) = Item.getItemFromBlock(this)!!
	
	internal fun register(name: String) {
		GameRegistry.registerBlock(this, ItemIridescentBlockMod::class.java, name)
	}
	
	override fun getPickBlock(target: MovingObjectPosition?, world: World, x: Int, y: Int, z: Int): ItemStack {
		val meta = world.getBlockMetadata(x, y, z)
		return ItemStack(this, 1, meta)
	}
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = ShadowFoxLexiconData.irisSapling
	
	override fun getBurnTime(fuel: ItemStack) = if (fuel.item == Item.getItemFromBlock(this)) 300 else 0
}
