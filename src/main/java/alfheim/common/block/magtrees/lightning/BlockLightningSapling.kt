package alfheim.common.block.magtrees.lightning

import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.colored.BlockColoredSapling
import alfheim.common.lexicon.ShadowFoxLexiconData
import alfheim.common.world.gen.HeartWoodTreeGen
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraft.world.gen.feature.WorldGenerator
import net.minecraftforge.event.terraingen.TerrainGen
import java.util.*

class BlockLightningSapling: BlockColoredSapling(name = "lightningSapling") {
 
	override fun growTree(world: World?, x: Int, y: Int, z: Int, random: Random?) {
		if (world != null) {
			
			if (!TerrainGen.saplingGrowTree(world, random, x, y, z)) return
			val plantedOn: Block = world.getBlock(x, y - 1, z)
			
			if (canGrowHere(plantedOn)) {
				val l = world.getBlockMetadata(x, y, z)
				
				val obj: WorldGenerator = HeartWoodTreeGen(5, AlfheimBlocks.lightningWood, 0, AlfheimBlocks.lightningWood, 1, AlfheimBlocks.lightningLeaves, 0)
				
				world.setBlock(x, y, z, Blocks.air, 0, 4)
				
				if (!obj.generate(world, random, x, y, z)) {
					world.setBlock(x, y, z, this, l, 4)
				}
			}
		}
	}
	
	override fun canGrowHere(block: Block) = block.material == Material.ground || block.material == Material.grass
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = ShadowFoxLexiconData.lightningSapling
}