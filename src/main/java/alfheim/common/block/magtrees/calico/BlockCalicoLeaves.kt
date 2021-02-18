package alfheim.common.block.magtrees.calico

import alexsocol.asjlib.toItem
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.base.BlockLeavesMod
import alfheim.common.item.block.ItemBlockLeavesMod
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.*
import vazkii.botania.api.lexicon.ILexiconable
import java.util.*

class BlockCalicoLeaves: BlockLeavesMod(), IExplosionDampener, ILexiconable {
	
	init {
		setBlockName("calicoLeaves")
	}
	
	override fun isInterpolated() = true
	
	override fun register(name: String) {
		GameRegistry.registerBlock(this, ItemBlockLeavesMod::class.java, name)
	}
	
	override fun getItemDropped(meta: Int, random: Random, fortune: Int) = AlfheimBlocks.calicoSapling.toItem()
	
	override fun quantityDropped(random: Random) = if (random.nextInt(60) == 0) 1 else 0
	
	override fun func_150125_e() = arrayOf("calicoLeaves")
	
	override fun decayBit(): Int = 0x1
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = AlfheimLexiconData.calicoSapling
	
	// ####
	
	override fun onBlockExploded(world: World, x: Int, y: Int, z: Int, explosion: Explosion) = Unit //NO-OP
}