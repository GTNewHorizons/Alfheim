package alfheim.common.block.magtrees.lightning

import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.base.BlockLeavesMod
import alfheim.common.item.block.ItemBlockMod
import alfheim.common.lexicon.ShadowFoxLexiconData
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.world.*
import java.util.*

class BlockLightningLeaves: BlockLeavesMod() {
	
	init {
		setBlockName("lightningLeaves")
	}
	
	override fun isInterpolated() = true
	
	override fun register(name: String) {
		GameRegistry.registerBlock(this, ItemBlockMod::class.java, name)
	}
	
	override fun getItemDropped(meta: Int, random: Random, fortune: Int) = Item.getItemFromBlock(AlfheimBlocks.lightningSapling)!!
	
	override fun quantityDropped(random: Random) = if (random.nextInt(60) == 0) 1 else 0
	
	override fun func_150125_e() = arrayOf("lightningLeaves")
	
	override fun decayBit(): Int = 0x1
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = ShadowFoxLexiconData.lightningSapling
}
