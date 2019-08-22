package alfheim.common.block.magtrees.nether

import alfheim.common.block.ShadowFoxBlocks
import alfheim.common.block.base.BlockLeavesMod
import alfheim.common.item.block.ItemBlockMod
import alfheim.common.lexicon.ShadowFoxLexiconData
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.world.*
import net.minecraftforge.common.util.ForgeDirection
import java.util.*

class BlockNetherLeaves: BlockLeavesMod() {
	
	init {
		setBlockName("netherLeaves")
	}
	
	override fun isFlammable(world: IBlockAccess?, x: Int, y: Int, z: Int, face: ForgeDirection?) = false
	
	@SideOnly(Side.CLIENT)
	override fun getBlockColor() = 0xFFFFFF
	
	@SideOnly(Side.CLIENT)
	override fun getRenderColor(meta: Int) = 0xFFFFFF
	
	@SideOnly(Side.CLIENT)
	override fun colorMultiplier(world: IBlockAccess?, x: Int, y: Int, z: Int) = 0xFFFFFF
	
	override fun isInterpolated() = true
	
	override fun register(name: String) {
		GameRegistry.registerBlock(this, ItemBlockMod::class.java, name)
	}
	
	override fun getItemDropped(meta: Int, random: Random, fortune: Int) = Item.getItemFromBlock(ShadowFoxBlocks.netherSapling)!!
	
	override fun quantityDropped(random: Random) = if (random.nextInt(60) == 0) 1 else 0
	
	override fun func_150125_e() = arrayOf("netherLeaves")
	
	override fun decayBit() = 0x1
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = ShadowFoxLexiconData.netherSapling
}
