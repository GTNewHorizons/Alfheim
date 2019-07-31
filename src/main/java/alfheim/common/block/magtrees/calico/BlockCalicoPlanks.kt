package alfheim.common.block.magtrees.calico

import alfheim.common.block.base.BlockMod
import alfheim.common.block.material.MaterialCustomSmeltingWood
import alfheim.common.lexicon.ShadowFoxLexiconData
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.*
import vazkii.botania.api.lexicon.ILexiconable

class BlockCalicoPlanks: BlockMod(MaterialCustomSmeltingWood.instance), IExplosionDampener, ILexiconable {
	
	private val name = "calicoPlanks"
	
	init {
		blockHardness = 2F
		
		stepSound = soundTypeWood
		setBlockName(name)
		
		tickRandomly = true
	}
	
	override fun isToolEffective(type: String?, metadata: Int) = (type != null && type == "axe")
	
	override fun getHarvestTool(metadata: Int) = "axe"
	
	override fun getPickBlock(target: MovingObjectPosition?, world: World, x: Int, y: Int, z: Int): ItemStack {
		val meta = world.getBlockMetadata(x, y, z)
		return ItemStack(this, 1, meta)
	}
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = ShadowFoxLexiconData.calicoSapling
	
	// ####
	
	override fun onBlockExploded(world: World, x: Int, y: Int, z: Int, explosion: Explosion) = Unit //NO-OP
}