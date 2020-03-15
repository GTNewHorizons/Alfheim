package alfheim.common.block

import alexsocol.asjlib.extendables.block.*
import alfheim.api.ModInfo
import alfheim.common.block.AlfheimBlocks.setHarvestLevelI
import alfheim.common.block.base.*
import alfheim.common.block.magtrees.lightning.BlockLightningWoodSlab
import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.block.ItemBlockMod
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.world.*
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.api.lexicon.LexiconEntry
import vazkii.botania.client.lib.LibResources
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.block.decor.slabs.BlockModSlab
import vazkii.botania.common.block.decor.stairs.BlockModStairs
import vazkii.botania.common.block.decor.walls.BlockModWall

@Suppress("JoinDeclarationAndAssignment")
object AlfheimFluffBlocks {
	
	val dreamwoodFence: Block
	val dreamwoodFenceGate: Block
	val dreamwoodBarkFence: Block
	val dreamwoodBarkFenceGate: Block
	val dwarfPlanks: Block
	val dwarfLantern: Block
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
		shrineRock = BlockModMeta(Material.rock, 16, ModInfo.MODID, "ShrineRock", AlfheimTab, 10f, harvLvl = 2, resist = 10000f, folder = "shrines/")
		shrinePillar = BlockShrinePillar()
		shrineRockWhiteStairs = object: BlockStairsMod(shrineRock, 0, "ShrineRockWhiteStairs") {
			override fun register() { GameRegistry.registerBlock(this, ItemBlockMod::class.java, name) }
			override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = null
		}
		shrineRockWhiteSlab = BlockRockShrineWhiteSlab(false).setCreativeTab(AlfheimTab).setHardness(1.5f)
		shrineRockWhiteSlabFull = BlockRockShrineWhiteSlab(true).setCreativeTab(null).setHardness(1.5f)
		(shrineRockWhiteSlab as BlockModSlab).register()
		(shrineRockWhiteSlabFull as BlockModSlab).register()
		
		dwarfLantern = BlockDwarfLantern()
		
		shrineLight = BlockModMeta(Material.glass, 4, ModInfo.MODID, "ShrineLight", AlfheimTab, resist = 6000f, folder = "shrines/").setLightLevel(1f).setLightOpacity(0)
		shrineGlass = BlockShrineGlass()
		shrinePanel = object: BlockPaneMeta(Material.glass, 4, "ShrinePanel", "shrines/") {
			override fun getRenderBlockPass() = 1
			override fun canPaneConnectTo(world: IBlockAccess, x: Int, y: Int, z: Int, dir: ForgeDirection) = super.canPaneConnectTo(world, x, y, z, dir) || world.getBlock(x, y, z) == shrineGlass
		}	.setBlockName("ShrinePanel")
			.setCreativeTab(AlfheimTab)
			.setLightOpacity(0)
			.setHardness(1f)
			.setHarvestLevelI("pickaxe", 1)
			.setResistance(600f)
			.setStepSound(Block.soundTypeGlass)
		
		Blocks.planks
		
		dwarfPlanks = BlockModMeta(Material.wood, 1, ModInfo.MODID, "DwarfPlanks", AlfheimTab, 3f, "axe", 1, 100f, "shrines/")
		
		elvenSandstone = BlockElvenSandstone()
		elvenSandstoneStairs = BlockModStairs(elvenSandstone, 0, "ElvenSandstoneStairs").setCreativeTab(AlfheimTab)
		elvenSandstoneSlab = BlockElvenSandstoneSlab(false).setCreativeTab(AlfheimTab).setHardness(1.5f)
		elvenSandstoneSlabFull = BlockElvenSandstoneSlab(true).setCreativeTab(null).setHardness(1.5f)
		(elvenSandstoneSlab as BlockModSlab).register()
		(elvenSandstoneSlabFull as BlockModSlab).register()
		
		livingcobbleStairs = BlockModStairs(AlfheimBlocks.livingcobble, 0, "LivingCobbleStairs").setCreativeTab(AlfheimTab)
		livingcobbleSlab = BlockLivingCobbleSlab(false).setCreativeTab(AlfheimTab).setHardness(1.5f)
		livingcobbleSlabFull = BlockLivingCobbleSlab(true).setCreativeTab(null).setHardness(1.5f)
		(livingcobbleSlab as BlockModSlab).register()
		(livingcobbleSlabFull as BlockModSlab).register()
		
		livingrockTileSlab = BlockLivingRockTileSlab(false).setCreativeTab(AlfheimTab).setHardness(1.5f)
		livingrockTileSlabFull = BlockLivingRockTileSlab(true).setCreativeTab(null).setHardness(1.5f)
		(livingrockTileSlab as BlockModSlab).register()
		(livingrockTileSlabFull as BlockModSlab).register()
		
		livingcobbleWall = BlockModWall(AlfheimBlocks.livingcobble, 0)
			.setCreativeTab(AlfheimTab)
			.setHardness(5f)
			.setResistance(8000f)
			.setStepSound(Block.soundTypeStone)
			.setHarvestLevelI("pickaxe", 2)
		
		livingrockBrickWall = BlockModWall(ModBlocks.livingrock, 1)
			.setCreativeTab(AlfheimTab)
			.setHardness(5f)
			.setResistance(8000f)
			.setStepSound(Block.soundTypeStone)
			.setHarvestLevelI("pickaxe", 2)
		
		livingwoodBarkFenceGate = BlockModFenceGate(ModBlocks.livingwood, 0)
			.setCreativeTab(AlfheimTab)
			.setBlockName("LivingwoodBarkFenceGate")
			.setHardness(2F)
			.setResistance(5F)
			.setStepSound(Block.soundTypeWood)
		
		livingwoodBarkFence = BlockModFence(LibResources.PREFIX_MOD + "livingwood0", Material.wood, livingwoodBarkFenceGate)
			.setBlockName("LivingwoodBarkFence")
			.setCreativeTab(AlfheimTab)
			.setHardness(2F)
			.setResistance(5F)
			.setStepSound(Block.soundTypeWood)
		
		dreamwoodBarkFenceGate = BlockModFenceGate(ModBlocks.dreamwood, 0)
			.setBlockName("DreamwoodBarkFenceGate")
			.setCreativeTab(AlfheimTab)
			.setHardness(2F)
			.setResistance(5F)
			.setStepSound(Block.soundTypeWood)
		
		dreamwoodBarkFence = BlockModFence(LibResources.PREFIX_MOD + "dreamwood0", Material.wood, dreamwoodBarkFenceGate)
			.setBlockName("DreamwoodBarkFence")
			.setCreativeTab(AlfheimTab)
			.setHardness(2F)
			.setResistance(5F)
			.setStepSound(Block.soundTypeWood)
		
		livingwoodFenceGate = BlockModFenceGate(ModBlocks.livingwood, 1)
			.setBlockName("LivingwoodFenceGate")
			.setCreativeTab(AlfheimTab)
			.setHardness(2F)
			.setResistance(5F)
			.setStepSound(Block.soundTypeWood)
		
		livingwoodFence = BlockModFence(LibResources.PREFIX_MOD + "livingwood1", Material.wood, livingwoodFenceGate)
			.setBlockName("LivingwoodFence")
			.setCreativeTab(AlfheimTab)
			.setHardness(2F)
			.setResistance(5F)
			.setStepSound(Block.soundTypeWood)
		
		dreamwoodFenceGate = BlockModFenceGate(ModBlocks.dreamwood, 1)
			.setBlockName("DreamwoodFenceGate")
			.setCreativeTab(AlfheimTab)
			.setHardness(2F)
			.setResistance(5F)
			.setStepSound(Block.soundTypeWood)
		
		dreamwoodFence = BlockModFence(LibResources.PREFIX_MOD + "dreamwood1", Material.wood, dreamwoodFenceGate)
			.setBlockName("DreamwoodFence")
			.setCreativeTab(AlfheimTab)
			.setHardness(2F)
			.setResistance(5F)
			.setStepSound(Block.soundTypeWood)
	}
}