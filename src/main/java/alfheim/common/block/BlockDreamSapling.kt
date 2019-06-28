package alfheim.common.block

import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.common.core.registry.AlfheimBlocks
import alfheim.common.lexicon.AlfheimLexiconData
import alfheim.common.world.dim.alfheim.struct.StructureDreamsTree
import net.minecraft.block.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import vazkii.botania.api.lexicon.*
import java.util.*

class BlockDreamSapling: BlockBush(), IGrowable, ILexiconable {
	init {
		this.setBlockName("DreamSapling")
		this.setBlockTextureName(ModInfo.MODID + ":DreamSapling")
		this.setCreativeTab(AlfheimCore.alfheimTab)
		this.setLightLevel(9.0f / 15.0f)
		this.setLightOpacity(0)
		this.tickRandomly = true
	}
	
	override fun updateTick(world: World, x: Int, y: Int, z: Int, rand: Random?) {
		if (!world.isRemote) {
			super.updateTick(world, x, y, z, rand)
			
			if (world.getBlockLightValue(x, y + 1, z) >= 9 && rand!!.nextInt(7) == 0) {
				this.func_149879_c(world, x, y, z, rand)
			}
		}
	}
	
	fun func_149879_c(world: World, x: Int, y: Int, z: Int, rand: Random) {
		val l = world.getBlockMetadata(x, y, z)
		
		if (l and 8 == 0) {
			world.setBlockMetadataWithNotify(x, y, z, l or 8, 4)
		} else {
			this.func_149878_d(world, x, y, z, rand)
		}
	}
	
	fun func_149878_d(world: World, x: Int, y: Int, z: Int, rand: Random) {
		if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(world, rand, x, y, z)) return
		val l = world.getBlockMetadata(x, y, z) and 7
		world.setBlock(x, y, z, Blocks.air, 0, 4)
		if (!StructureDreamsTree(AlfheimBlocks.dreamLog, AlfheimBlocks.dreamLeaves, 0, 4, 8, 0).generate(world, rand, x, y, z)) world.setBlock(x, y, z, this, l, 4)
	}
	
	/** Can the block grow
	 * @param b == world.isRemote
	 */
	override fun func_149851_a(world: World, x: Int, y: Int, z: Int, b: Boolean): Boolean {
		return true
	}
	
	/** Applying chance to grow
	 * @return true to grow tree
	 */
	override fun func_149852_a(world: World, rand: Random, x: Int, y: Int, z: Int): Boolean {
		return world.rand.nextDouble() < 0.45
	}
	
	/** Grow block  */
	override fun func_149853_b(world: World, rand: Random, x: Int, y: Int, z: Int) {
		this.func_149879_c(world, x, y, z, rand)
	}
	
	override fun damageDropped(meta: Int): Int {
		return 0
	}
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack): LexiconEntry {
		return AlfheimLexiconData.worldgen
	}
}
