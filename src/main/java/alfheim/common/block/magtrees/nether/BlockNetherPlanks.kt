package alfheim.common.block.magtrees.nether

import alexsocol.asjlib.toItem
import alfheim.common.block.base.BlockMod
import alfheim.common.item.block.ItemBlockLeavesMod
import alfheim.common.lexicon.ShadowFoxLexiconData
import cpw.mods.fml.common.IFuelHandler
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.*
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.api.lexicon.ILexiconable
import java.util.*

class BlockNetherPlanks: BlockMod(Material.wood), ILexiconable, IFuelHandler {
	
	private val name = "netherPlanks"
	
	init {
		blockHardness = 2F
		setBlockName(name)
		setLightLevel(0.5f)
		stepSound = soundTypeWood
		GameRegistry.registerFuelHandler(this)
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
	
	override fun isFlammable(world: IBlockAccess?, x: Int, y: Int, z: Int, face: ForgeDirection?) = false
	
	override fun getFireSpreadSpeed(world: IBlockAccess?, x: Int, y: Int, z: Int, face: ForgeDirection?) = 0
	
	internal fun register(name: String) {
		GameRegistry.registerBlock(this, ItemBlockLeavesMod::class.java, name)
		Blocks.netherrack
	}
	
	override fun getPickBlock(target: MovingObjectPosition?, world: World, x: Int, y: Int, z: Int): ItemStack {
		val meta = world.getBlockMetadata(x, y, z)
		return ItemStack(this, 1, meta)
	}
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = ShadowFoxLexiconData.netherSapling
	
	override fun getBurnTime(fuel: ItemStack) = if (fuel.item === this.toItem()) 2000 else 0
}
