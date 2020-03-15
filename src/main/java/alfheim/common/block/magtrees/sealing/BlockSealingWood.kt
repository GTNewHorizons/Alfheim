package alfheim.common.block.magtrees.sealing

import alfheim.common.block.base.BlockModRotatedPillar
import alfheim.common.item.block.ItemBlockMod
import alfheim.common.lexicon.ShadowFoxLexiconData
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.FMLLaunchHandler
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.*
import net.minecraftforge.client.event.sound.PlaySoundEvent17
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.api.lexicon.ILexiconable
import java.util.*

class BlockSealingWood: BlockModRotatedPillar(Material.wood), ISoundSilencer {
	
	init {
		setBlockName("sealingWood")
		blockHardness = 2f
		setStepSound(soundTypeCloth)
		if (FMLLaunchHandler.side().isClient)
            EventHandlerSealingOak.register()
	}
	
	override fun canSilence(world: World, x: Int, y: Int, z: Int, dist: Double, soundEvent: PlaySoundEvent17) = dist <= 8
	
	override fun getVolumeMultiplier(world: World, x: Int, y: Int, z: Int, dist: Double, soundEvent: PlaySoundEvent17) = 0.5f
	
	override fun isInterpolated() = true
	
	override fun canSustainLeaves(world: IBlockAccess, x: Int, y: Int, z: Int) = true
	
	override fun isWood(world: IBlockAccess?, x: Int, y: Int, z: Int) = true
	
	override fun breakBlock(world: World, x: Int, y: Int, z: Int, block: Block, fortune: Int) {
		val b0: Byte = 4
		val i1: Int = b0 + 1
		
		if (world.checkChunksExist(x - i1, y - i1, z - i1, x + i1, y + i1, z + i1)) {
			for (j1 in -b0..b0) for (k1 in -b0..b0)
				for (l1 in -b0..b0) {
					val blockInWorld: Block = world.getBlock(x + j1, y + k1, z + l1)
					if (blockInWorld.isLeaves(world, x + j1, y + k1, z + l1)) {
						blockInWorld.beginLeavesDecay(world, x + j1, y + k1, z + l1)
					}
				}
		}
		super.breakBlock(world, x, y, z, block, fortune)
	}
	
	override fun isFireSource(world: World?, x: Int, y: Int, z: Int, side: ForgeDirection?) = true
	
	override fun isFlammable(world: IBlockAccess?, x: Int, y: Int, z: Int, face: ForgeDirection?) = false
	
	override fun getFireSpreadSpeed(world: IBlockAccess?, x: Int, y: Int, z: Int, face: ForgeDirection?) = 0
	
	override fun damageDropped(meta: Int) = 0
	
	override fun quantityDropped(random: Random) = 1
	
	override fun register(par1Str: String) {
		GameRegistry.registerBlock(this, ItemBlockMod::class.java, par1Str)
	}
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = ShadowFoxLexiconData.silencer
}
