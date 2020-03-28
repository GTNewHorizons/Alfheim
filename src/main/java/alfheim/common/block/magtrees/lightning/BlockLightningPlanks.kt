package alfheim.common.block.magtrees.lightning

import alexsocol.asjlib.toItem
import alfheim.common.block.base.BlockMod
import alfheim.common.item.block.ItemBlockLeavesMod
import alfheim.common.lexicon.ShadowFoxLexiconData
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World
import vazkii.botania.api.lexicon.ILexiconable
import java.util.*

class BlockLightningPlanks: BlockMod(Material.wood), ILexiconable {
	
	private val name = "lightningPlanks"
	
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
	
	override fun setBlockName(name: String): Block {
		register(name)
		return super.setBlockName(name)
	}
	
	override fun quantityDropped(random: Random) = 1
	
	override fun getItemDropped(meta: Int, random: Random, fortune: Int) = this.toItem()
	
	internal fun register(name: String) {
		GameRegistry.registerBlock(this, ItemBlockLeavesMod::class.java, name)
	}
	
	override fun getPickBlock(target: MovingObjectPosition?, world: World, x: Int, y: Int, z: Int): ItemStack {
		val meta = world.getBlockMetadata(x, y, z)
		return ItemStack(this, 1, meta)
	}
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = ShadowFoxLexiconData.lightningSapling
}
