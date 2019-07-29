package alfheim.common.block.colored.rainbow

import alfheim.common.block.ShadowFoxBlocks
import alfheim.common.block.base.BlockLeavesMod
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.world.*
import alfheim.common.item.block.ItemIridescentBlockMod
import alfheim.common.lexicon.ShadowFoxLexiconData
import java.util.*

class BlockRainbowLeaves: BlockLeavesMod() {
	
	init {
		setBlockName("rainbowLeaves")
	}
	
	override fun register(name: String) {
		GameRegistry.registerBlock(this, ItemIridescentBlockMod::class.java, name)
	}
	
	override fun quantityDropped(random: Random) = if (random.nextInt(20) == 0) 1 else 0
	
	override fun getItemDropped(meta: Int, random: Random, fortune: Int) = Item.getItemFromBlock(ShadowFoxBlocks.irisSapling)!!
	
	override fun func_150125_e() = arrayOf("rainbowLeaves")
	
	@SideOnly(Side.CLIENT)
	override fun getRenderColor(meta: Int) = 0xFFFFFF
	
	@SideOnly(Side.CLIENT)
	override fun colorMultiplier(world: IBlockAccess?, x: Int, y: Int, z: Int) = 0xFFFFFF
	
	override fun isInterpolated() = true
	
	override fun decayBit() = 0x1
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = ShadowFoxLexiconData.irisSapling
}
