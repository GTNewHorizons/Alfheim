package alfheim.common.block.colored.rainbow

import alexsocol.asjlib.toItem
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.base.BlockLeavesMod
import alfheim.common.item.block.ItemIridescentBlockMod
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import java.util.*

class BlockRainbowLeaves: BlockLeavesMod() {
	
	init {
		setBlockName("rainbowLeaves")
	}
	
	override fun register(name: String) {
		GameRegistry.registerBlock(this, ItemIridescentBlockMod::class.java, name)
	}
	
	override fun quantityDropped(random: Random) = if (random.nextInt(20) == 0) 1 else 0
	
	override fun getItemDropped(meta: Int, random: Random, fortune: Int) = AlfheimBlocks.irisSapling.toItem()
	
	override fun func_150125_e() = arrayOf("rainbowLeaves")
	
	override fun isInterpolated() = true
	
	override fun decayBit() = 0x1
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = AlfheimLexiconData.irisSapling
}
