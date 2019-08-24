package alfheim.common.block.colored

import alfheim.api.ShadowFoxAPI
import alfheim.common.core.helper.IconHelper
import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.block.ItemBlockMod
import alfheim.common.lexicon.ShadowFoxLexiconData
import alfheim.common.world.gen.SimpleTreeGen
import cpw.mods.fml.common.IFuelHandler
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.*
import net.minecraft.world.*
import net.minecraft.world.gen.feature.WorldGenerator
import net.minecraftforge.common.EnumPlantType
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.api.lexicon.ILexiconable
import java.util.*

open class BlockColoredSapling(val name: String = "irisSapling"): BlockSapling(), ILexiconable, IFuelHandler {
	
	init {
		setBlockName(name)
		setCreativeTab(AlfheimTab)
		stepSound = Block.soundTypeGrass
		tickRandomly = true
		
		GameRegistry.registerFuelHandler(this)
	}
	
	override fun setBlockName(par1Str: String): Block {
		GameRegistry.registerBlock(this, ItemBlockMod::class.java, par1Str)
		return super.setBlockName(par1Str)
	}
	
	override fun getCollisionBoundingBoxFromPool(world: World?, x: Int, y: Int, z: Int) = null
	
	override fun isOpaqueCube() = false
	
	override fun renderAsNormalBlock() = false
	
	override fun getRenderType() = 1
	
	override fun getPlant(world: IBlockAccess?, x: Int, y: Int, z: Int) = this
	
	override fun getPlantType(world: IBlockAccess?, x: Int, y: Int, z: Int) = EnumPlantType.Plains
	
	override fun getPlantMetadata(world: IBlockAccess?, x: Int, y: Int, z: Int) = world?.getBlockMetadata(x, y, z) ?: 0
	
	override fun canPlaceBlockAt(world: World, x: Int, y: Int, z: Int) =
        super.canPlaceBlockAt(world, x, y, z) && canBlockStay(world, x, y, z)
	
	/**
	 * Ticks the block if it's been scheduled
	 */
	override fun updateTick(world: World, x: Int, y: Int, z: Int, random: Random) {
		if (!world.isRemote) {
			checkAndDropBlock(world, x, y, z)
			
			if (world.getBlockLightValue(x, y + 1, z) >= 9 && random.nextInt(7) == 0) {
                markOrGrowMarked(world, x, y, z, random)
			}
		}
	}
	
	override fun checkAndDropBlock(world: World?, x: Int, y: Int, z: Int) {
		if (world != null && !canBlockStay(world, x, y, z)) {
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0)
			world.setBlock(x, y, z, Block.getBlockById(0), 0, 2)
		}
	}
	
	override fun canBlockStay(world: World, x: Int, y: Int, z: Int) =
        world.getBlock(x, y - 1, z).canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this) || canGrowHere(world.getBlock(x, y - 1, z))
	
	override fun getSubBlocks(item: Item?, tab: CreativeTabs?, list: MutableList<Any?>?) {
		if (list != null && item != null)
			list.add(ItemStack(this))
	}
	
	fun markOrGrowMarked(world: World?, x: Int, y: Int, z: Int, random: Random?) {
		if (world != null) {
			val l = world.getBlockMetadata(x, y, z)
			
			if ((l and 8) == 0) {
				world.setBlockMetadataWithNotify(x, y, z, l or 8, 4)
			} else {
				growTree(world, x, y, z, random)
			}
		}
	}
	
	open fun growTree(world: World?, x: Int, y: Int, z: Int, random: Random?) {
		if (world != null) {
			if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(world, random, x, y, z)) return
			
			val plantedOn: Block = world.getBlock(x, y - 1, z)
			
			if (canGrowHere(plantedOn)) {
				val l = world.getBlockMetadata(x, y, z)
				
				val obj: WorldGenerator = SimpleTreeGen(5)
				
				world.setBlock(x, y, z, Blocks.air, 0, 4)
				
				if (!obj.generate(world, random, x, y, z)) {
					world.setBlock(x, y, z, this, l, 4)
				}
			}
		}
	}
	
	/**
	 *  shouldFertilize
	 */
	override fun func_149852_a(world: World?, random: Random?, x: Int, y: Int, z: Int): Boolean {
		if (world != null) return world.rand.nextFloat().toDouble() < 0.45
		return false
	}
	
	/**
	 * fertilize
	 */
	override fun func_149853_b(world: World?, random: Random?, x: Int, y: Int, z: Int) {
        markOrGrowMarked(world, x, y, z, random)
	}
	
	/**
	 * canFertilize
	 */
	override fun func_149851_a(world: World?, x: Int, y: Int, z: Int, isRemote: Boolean) = canGrowHere(world!!.getBlock(x, y - 1, z))
	
	open fun canGrowHere(block: Block) = ShadowFoxAPI.getIridescentSoils().contains(block)
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = ShadowFoxLexiconData.irisSapling
	
	override fun getBurnTime(fuel: ItemStack) = if (fuel.item == Item.getItemFromBlock(this)) 100 else 0
	
	@SideOnly(Side.CLIENT)
	override fun registerBlockIcons(par1IconRegister: IIconRegister) {
		blockIcon = IconHelper.forBlock(par1IconRegister, this)
	}
	
	@SideOnly(Side.CLIENT)
	override fun getIcon(side: Int, meta: Int) = blockIcon!!
}
