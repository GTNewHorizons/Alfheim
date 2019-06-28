package alfheim.common.block

import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.item.EntityFallingBlock
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.*
import vazkii.botania.api.lexicon.*
import java.util.*

class BlockPatternLexicon(modid: String, material: Material, name: String, tab: CreativeTabs, lightlvl: Float, lightOpacity: Int, hardness: Float, harvTool: String, harvLvl: Int, resistance: Float, sound: Block.SoundType, private val isOpaque: Boolean, private val isBeacon: Boolean, private val isFalling: Boolean, private val entry: LexiconEntry): BlockFalling(material), ILexiconable {
	
	init {
		this.setBlockName(name)
		this.setBlockTextureName("$modid:$name")
		this.setCreativeTab(tab)
		this.setLightLevel(lightlvl)
		this.setLightOpacity(lightOpacity)
		this.setHardness(hardness)
		this.setHarvestLevel(harvTool, harvLvl)
		this.setResistance(resistance)
		this.setStepSound(sound)
	}
	
	override fun isOpaqueCube(): Boolean {
		return isOpaque
	}
	
	override fun isBeaconBase(world: IBlockAccess?, x: Int, y: Int, z: Int, beaconX: Int, beaconY: Int, beaconZ: Int): Boolean {
		return this.isBeacon
	}
	
	override fun updateTick(world: World, x: Int, y: Int, z: Int, rand: Random?) {
		if (!world.isRemote && this.isFalling) this.func_149830_m(world, x, y, z)
	}
	
	private fun func_149830_m(world: World, x: Int, y: Int, z: Int) {
		var y = y
		if (BlockFalling.func_149831_e(world, x, y - 1, z) && y >= 0) {
			val b0: Byte = 32
			
			if (!BlockFalling.fallInstantly && world.checkChunksExist(x - b0, y - b0, z - b0, x + b0, y + b0, z + b0)) {
				if (!world.isRemote) {
					val entityfallingblock = EntityFallingBlock(world, (x.toFloat() + 0.5f).toDouble(), (y.toFloat() + 0.5f).toDouble(), (z.toFloat() + 0.5f).toDouble(), this, world.getBlockMetadata(x, y, z))
					this.func_149829_a(entityfallingblock)
					world.spawnEntityInWorld(entityfallingblock)
				}
			} else {
				world.setBlockToAir(x, y, z)
				while (BlockFalling.func_149831_e(world, x, y - 1, z) && y > 0) --y
				if (y > 0) world.setBlock(x, y, z, this)
			}
		}
	}
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack): LexiconEntry {
		return entry
	}
}