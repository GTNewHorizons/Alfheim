package alfheim.common.block.magtrees.nether

import alexsocol.asjlib.toItem
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.base.BlockLeavesMod
import alfheim.common.item.block.ItemBlockLeavesMod
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.*
import net.minecraftforge.common.util.ForgeDirection
import java.util.*

class BlockNetherLeaves: BlockLeavesMod() {
	
	init {
		setBlockName("netherLeaves")
		setLightLevel(0.5f)
	}
	
	override fun isFlammable(world: IBlockAccess?, x: Int, y: Int, z: Int, face: ForgeDirection?) = false
	
	override fun getFireSpreadSpeed(world: IBlockAccess?, x: Int, y: Int, z: Int, face: ForgeDirection?) = 0
	
	override fun isInterpolated() = true
	
	override fun register(name: String) {
		GameRegistry.registerBlock(this, ItemBlockLeavesMod::class.java, name)
	}
	
	override fun getItemDropped(meta: Int, random: Random, fortune: Int) = AlfheimBlocks.netherSapling.toItem()
	
	override fun quantityDropped(random: Random) = if (random.nextInt(60) == 0) 1 else 0
	
	override fun func_150125_e() = arrayOf("netherLeaves")
	
	override fun decayBit() = 0x1
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = AlfheimLexiconData.netherSapling
}
