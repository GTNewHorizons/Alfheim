package alfheim.common.block

import alexsocol.asjlib.extendables.block.*
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.common.block.AlfheimBlocks.setHarvestLevelI
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.client.lib.LibResources
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.block.decor.slabs.BlockModSlab
import vazkii.botania.common.block.decor.walls.BlockModWall

@Suppress("JoinDeclarationAndAssignment")
object AlfheimFluffBlocks {
	
	val dreamwoodFence: Block
	val dreamwoodFenceGate: Block
	val dreamwoodBarkFence: Block
	val dreamwoodBarkFenceGate: Block
	val elvenSandstone: Block
	val elvenSandstoneStairs: Block
	val elvenSandstoneSlab: Block
	val elvenSandstoneSlabFull: Block
	val livingcobbleStairs: Block
	val livingcobbleSlab: Block
	val livingcobbleSlabFull: Block
	val livingcobbleWall: Block
	val livingrockBrickWall: Block
	val livingrockTileSlab: Block
	val livingrockTileSlabFull: Block
	val livingwoodFence: Block
	val livingwoodFenceGate: Block
	val livingwoodBarkFence: Block
	val livingwoodBarkFenceGate: Block
	val shrineLight: Block
	val shrineGlass: Block
	val shrinePanel: Block
	val shrinePillar: Block
	val shrineRock: Block
	val shrineRockWhiteSlab: Block
	val shrineRockWhiteSlabFull: Block
	val shrineRockWhiteStairs: Block
	
	init {
		shrineRock = BlockModMeta(Material.rock, 16, ModInfo.MODID, "ShrineRock", AlfheimCore.alfheimTab, 10f, harvLvl = 2, resist = 10000f, folder = "shrines/")
		shrinePillar = BlockShrinePillar()
		shrineRockWhiteStairs = BlockModStairs(shrineRock, 0).setBlockName("ShrineRockWhiteStairs").setCreativeTab(AlfheimCore.alfheimTab)
		shrineRockWhiteSlab = BlockRockShrineWhiteSlab(false)
		shrineRockWhiteSlabFull = BlockRockShrineWhiteSlab(true)
		shrineRockWhiteSlab.setCreativeTab(AlfheimCore.alfheimTab)
		(shrineRockWhiteSlab as BlockModSlab).register()
		(shrineRockWhiteSlabFull as BlockModSlab).register()
		
		shrineLight = BlockModMeta(Material.glass, 4, ModInfo.MODID, "ShrineLight", AlfheimCore.alfheimTab, resist = 6000f, folder = "shrines/").setLightLevel(1f).setLightOpacity(0)
		shrineGlass = BlockShrineGlass()
		shrinePanel = object: BlockPaneMeta(Material.glass, 4, "ShrinePanel", "shrines/") {
			override fun
				getRenderBlockPass() = 1
			
			override fun
				canPaneConnectTo(world: IBlockAccess, x: Int, y: Int, z: Int, dir: ForgeDirection) =
				super
					.canPaneConnectTo(world, x, y, z, dir) || world.getBlock(x, y, z) == shrineGlass
		}.setBlockName("ShrinePanel")
			.setCreativeTab(AlfheimCore.alfheimTab)
			.setLightOpacity(0)
			.setHardness(1f)
			.setResistance(600f)
			.setStepSound(Block.soundTypeGlass)
			.setHarvestLevelI("pickaxe", 1)
		
		elvenSandstone = BlockElvenSandstone()
		elvenSandstoneStairs = BlockModStairs(elvenSandstone, 0).setBlockName("ElvenSandstoneStairs").setCreativeTab(AlfheimCore.alfheimTab)
		elvenSandstoneSlab = BlockElvenSandstoneSlab(false)
		elvenSandstoneSlabFull = BlockElvenSandstoneSlab(true)
		elvenSandstoneSlab.setCreativeTab(AlfheimCore.alfheimTab)
		(elvenSandstoneSlab as BlockModSlab).register()
		(elvenSandstoneSlabFull as BlockModSlab).register()
		
		livingcobbleStairs = BlockModStairs(AlfheimBlocks.livingcobble, 0).setBlockName("LivingCobbleStairs").setCreativeTab(AlfheimCore.alfheimTab)
		livingcobbleSlab = BlockLivingCobbleSlab(false)
		livingcobbleSlabFull = BlockLivingCobbleSlab(true)
		livingcobbleSlab.setCreativeTab(AlfheimCore.alfheimTab)
		(livingcobbleSlab as BlockModSlab).register()
		(livingcobbleSlabFull as BlockModSlab).register()
		
		livingrockTileSlab = BlockLivingRockTileSlab(false)
		livingrockTileSlabFull = BlockLivingRockTileSlab(true)
		livingrockTileSlab.setCreativeTab(AlfheimCore.alfheimTab)
		(livingrockTileSlab as BlockModSlab).register()
		(livingrockTileSlabFull as BlockModSlab).register()
		
		livingcobbleWall = BlockModWall(AlfheimBlocks.livingcobble, 0).setCreativeTab(AlfheimCore.alfheimTab)
			.setHardness(5f)
			.setResistance(8000f)
			.setStepSound(Block.soundTypeStone)
			.setHarvestLevelI("pickaxe", 2)
		
		livingrockBrickWall = BlockModWall(ModBlocks.livingrock, 1).setCreativeTab(AlfheimCore.alfheimTab)
			.setHardness(5f)
			.setResistance(8000f)
			.setStepSound(Block.soundTypeStone)
			.setHarvestLevelI("pickaxe", 2)
		
		livingwoodBarkFenceGate = BlockModFenceGate(ModBlocks.livingwood, 0).setBlockName("LivingwoodBarkFenceGate")
			.setCreativeTab(AlfheimCore.alfheimTab)
			.setHardness(2F)
			.setResistance(5F)
			.setStepSound(Block.soundTypeWood)
		
		livingwoodBarkFence = BlockModFence(LibResources.PREFIX_MOD + "livingwood0", Material.wood, livingwoodBarkFenceGate).setBlockName("LivingwoodBarkFence")
			.setCreativeTab(AlfheimCore.alfheimTab)
			.setHardness(2F)
			.setResistance(5F)
			.setStepSound(Block.soundTypeWood)
		
		dreamwoodBarkFenceGate = BlockModFenceGate(ModBlocks.dreamwood, 0).setBlockName("DreamwoodBarkFenceGate")
			.setCreativeTab(AlfheimCore.alfheimTab)
			.setHardness(2F)
			.setResistance(5F)
			.setStepSound(Block.soundTypeWood)
		
		dreamwoodBarkFence = BlockModFence(LibResources.PREFIX_MOD + "dreamwood0", Material.wood, dreamwoodBarkFenceGate).setBlockName("DreamwoodBarkFence")
			.setCreativeTab(AlfheimCore.alfheimTab)
			.setHardness(2F)
			.setResistance(5F)
			.setStepSound(Block.soundTypeWood)
		
		livingwoodFenceGate = BlockModFenceGate(ModBlocks.livingwood, 1).setBlockName("LivingwoodFenceGate")
			.setCreativeTab(AlfheimCore.alfheimTab)
			.setHardness(2F)
			.setResistance(5F)
			.setStepSound(Block.soundTypeWood)
		
		livingwoodFence = BlockModFence(LibResources.PREFIX_MOD + "livingwood1", Material.wood, livingwoodFenceGate).setBlockName("LivingwoodFence")
			.setCreativeTab(AlfheimCore.alfheimTab)
			.setHardness(2F)
			.setResistance(5F)
			.setStepSound(Block.soundTypeWood)
		
		dreamwoodFenceGate = BlockModFenceGate(ModBlocks.dreamwood, 1).setBlockName("DreamwoodFenceGate")
			.setCreativeTab(AlfheimCore.alfheimTab)
			.setHardness(2F)
			.setResistance(5F)
			.setStepSound(Block.soundTypeWood)
		
		dreamwoodFence = BlockModFence(LibResources.PREFIX_MOD + "dreamwood1", Material.wood, dreamwoodFenceGate).setBlockName("DreamwoodFence")
			.setCreativeTab(AlfheimCore.alfheimTab)
			.setHardness(2F)
			.setResistance(5F)
			.setStepSound(Block.soundTypeWood)
	}
}