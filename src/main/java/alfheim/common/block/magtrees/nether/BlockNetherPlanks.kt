package alfheim.common.block.magtrees.nether

import alfheim.common.block.base.BlockMod
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.*
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.*
import net.minecraftforge.common.util.ForgeDirection
import alfheim.common.block.material.MaterialCustomSmeltingWood
import alfheim.common.item.block.ItemBlockMod
import alfheim.common.lexicon.ShadowFoxLexiconData
import vazkii.botania.api.lexicon.ILexiconable
import java.util.*

class BlockNetherPlanks: BlockMod(MaterialCustomSmeltingWood.instance), ILexiconable {
	
	private val name = "netherPlanks"
	
	init {
		blockHardness = 2F
		setLightLevel(0f)
		stepSound = soundTypeWood
		
		setBlockName(name)
	}
	
	override fun isInterpolated() = true
	
	override fun isToolEffective(type: String?, metadata: Int) = (type != null && type == "axe")
	
	override fun getHarvestTool(metadata: Int) = "axe"
	
	override fun shouldRegisterInNameSet() = false
	
	override fun damageDropped(par1: Int) = par1
	
	override fun setBlockName(par1Str: String): Block {
		register(par1Str)
		return super.setBlockName(par1Str)
	}
	
	override fun quantityDropped(random: Random) = 1
	
	override fun getItemDropped(meta: Int, random: Random, fortune: Int) = Item.getItemFromBlock(this)!!
	
	override fun isFlammable(world: IBlockAccess?, x: Int, y: Int, z: Int, face: ForgeDirection?) = false
	
	override fun getFireSpreadSpeed(world: IBlockAccess?, x: Int, y: Int, z: Int, face: ForgeDirection?) = 0
	
	internal fun register(name: String) {
		GameRegistry.registerBlock(this, ItemBlockMod::class.java, name)
		Blocks.netherrack
	}
	
	override fun getPickBlock(target: MovingObjectPosition?, world: World, x: Int, y: Int, z: Int): ItemStack {
		val meta = world.getBlockMetadata(x, y, z)
		return ItemStack(this, 1, meta)
	}
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) =
		ShadowFoxLexiconData.netherSapling
}
