package alfheim.common.block.colored

import alexsocol.asjlib.toItem
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.base.BlockLeavesMod
import alfheim.common.item.block.ItemBlockAurora
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.*
import vazkii.botania.api.lexicon.ILexiconable
import java.util.*

class BlockAuroraLeaves: BlockLeavesMod(), ILexiconable {
	
	init {
		setBlockName("auroraLeaves")
	}
	
	override fun register(name: String) {
		GameRegistry.registerBlock(this, ItemBlockAurora::class.java, name)
	}
	
	override fun quantityDropped(random: Random) = if (random.nextInt(20) == 0) 1 else 0
	
	@SideOnly(Side.CLIENT)
	override fun colorMultiplier(world: IBlockAccess?, x: Int, y: Int, z: Int) = BlockAuroraDirt.getBlockColor(x, y, z)
	
	override fun func_150125_e() = arrayOf("auroraLeaves")
	
	override fun decayBit(): Int = 0x8
	
	override fun getItemDropped(p_149650_1_: Int, p_149650_2_: Random?, p_149650_3_: Int) = AlfheimBlocks.irisSapling.toItem()
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = AlfheimLexiconData.aurora
}