package alfheim.common.block

import alfheim.api.ModInfo
import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.block.ItemBlockMod
import alfheim.common.lexicon.AlfheimLexiconData
import alfheim.common.world.dim.alfheim.structure.StructureDreamsTree
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.event.terraingen.TerrainGen
import vazkii.botania.api.lexicon.ILexiconable
import java.util.*

class BlockDreamSapling: BlockBush(), IGrowable, ILexiconable {
	
	init {
		setBlockBounds(0.1f, 0f, 0.1f, 0.9f, 0.8f, 0.9f)
		setBlockName("DreamSapling")
		setBlockTextureName(ModInfo.MODID + ":DreamSapling")
		setCreativeTab(AlfheimTab)
		setLightLevel(9f / 15f)
		setLightOpacity(0)
		stepSound = soundTypeGrass
		tickRandomly = true
	}
	
	override fun setBlockName(name: String): Block {
		GameRegistry.registerBlock(this, ItemBlockMod::class.java, name)
		return super.setBlockName(name)
	}
	
	override fun updateTick(world: World, x: Int, y: Int, z: Int, rand: Random?) {
		if (!world.isRemote) {
			super.updateTick(world, x, y, z, rand)
			
			if (world.getBlockLightValue(x, y + 1, z) >= 9 && rand!!.nextInt(7) == 0) {
				grow(world, x, y, z, rand)
			}
		}
	}
	
	fun grow(world: World, x: Int, y: Int, z: Int, rand: Random) {
		val l = world.getBlockMetadata(x, y, z)
		
		if (l and 8 == 0) {
			world.setBlockMetadataWithNotify(x, y, z, l or 8, 4)
		} else {
			growTree(world, x, y, z, rand)
		}
	}
	
	fun growTree(world: World, x: Int, y: Int, z: Int, rand: Random) {
		if (!TerrainGen.saplingGrowTree(world, rand, x, y, z)) return
		val l = world.getBlockMetadata(x, y, z) and 7
		world.setBlock(x, y, z, Blocks.air, 0, 4)
		if (!StructureDreamsTree(AlfheimBlocks.altWood1, AlfheimBlocks.altLeaves, 3, 7, 11, 7).generate(world, rand, x, y, z)) world.setBlock(x, y, z, this, l, 4)
	}
	
	/** Can the block grow
	 * @param b == world.isRemote
	 */
	override fun func_149851_a(world: World, x: Int, y: Int, z: Int, b: Boolean) = true
	
	/** Applying chance to grow
	 * @return true to grow tree
	 */
	override fun func_149852_a(world: World, rand: Random, x: Int, y: Int, z: Int) = world.rand.nextDouble() < 0.45
	
	/** Grow block  */
	override fun func_149853_b(world: World, rand: Random, x: Int, y: Int, z: Int) {
		grow(world, x, y, z, rand)
	}
	
	override fun damageDropped(meta: Int) = 0
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack) =
		AlfheimLexiconData.worldgen
}
