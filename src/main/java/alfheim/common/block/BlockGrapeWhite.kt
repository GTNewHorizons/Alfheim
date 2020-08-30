package alfheim.common.block

import alexsocol.asjlib.*
import alfheim.api.lib.LibRenderIDs
import alfheim.common.core.helper.IconHelper
import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.AlfheimItems
import alfheim.common.item.block.ItemBlockGrapeWhite
import alfheim.common.item.material.ElvenFoodMetas
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityBoat
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.api.lexicon.ILexiconable
import java.util.*

class BlockGrapeWhite: BlockBush(), IGrowable, ILexiconable {
	
	lateinit var iconsBush: Array<IIcon>
	
	init {
		val f = 0.5f
		val f1 = 0.015625f
		setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, f1, 0.5f + f)
		setBlockName("WhiteGrape")
		setCreativeTab(AlfheimTab)
		setHardness(0.2f)
		setStepSound(soundTypeGrass)
	}
	
	override fun setBlockName(name: String): Block {
		GameRegistry.registerBlock(this, ItemBlockGrapeWhite::class.java, name)
		return super.setBlockName(name)
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerBlockIcons(reg: IIconRegister) {
		blockIcon = IconHelper.forBlock(reg, this, "Bottom")
		iconsBush = Array(3) { IconHelper.forBlock(reg, this, it + 1) }
	}
	
	override fun getIcon(side: Int, meta: Int) = if (side == 0) iconsBush.safeGet(meta) else blockIcon!!
	override fun getRenderType() = LibRenderIDs.idGrapeWhite
	override fun addCollisionBoxesToList(world: World?, x: Int, y: Int, z: Int, aabb: AxisAlignedBB?, list: List<Any?>?, entity: Entity?) = if (entity !is EntityBoat) super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity) else Unit
	override fun getCollisionBoundingBoxFromPool(world: World?, x: Int, y: Int, z: Int) = AxisAlignedBB.getBoundingBox(x + minX, y + minY, z + minZ, x + maxX, y + maxY, z + maxZ)!!
	override fun canPlaceBlockOn(block: Block) = block === Blocks.water
	override fun canBlockStay(world: World, x: Int, y: Int, z: Int) = if (y in 0..255) world.getBlock(x, y - 1, z) === Blocks.water && world.getBlockMetadata(x, y - 1, z) == 0 else false
	
	override fun updateTick(world: World, x: Int, y: Int, z: Int, random: Random) {
		super.updateTick(world, x, y, z, random)
		
		val meta = world.getBlockMetadata(x, y, z)
		if (meta < 2 && random.nextInt(if (meta == 0) 50 else 10) == 0) {
			world.setBlockMetadataWithNotify(x, y, z, meta + 1, 3)
			return
		}
		
		if (random.nextInt(100) == 0) {
			for (d in arrayOf(ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.NORTH, ForgeDirection.EAST).shuffled()) {
				if (world.getBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ) == Blocks.waterlily)
					world.setBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ, this, 0, 3)
			}
		}
	}
	
	override fun onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		if (!world.isRemote && world.getBlockMetadata(x, y, z) >= 2 && player.heldItem == null) {
			player.dropPlayerItemWithRandomChoice(ItemStack(AlfheimItems.elvenFood, world.rand.nextInt(2) + 1, ElvenFoodMetas.WhiteGrapes), true)?.delayBeforeCanPickup = 0
			world.setBlockMetadataWithNotify(x, y, z, 0, 3)
			return true
		}
		
		return false
	}
	
	// can be bonemealed at all? If true will consume one item
	override fun func_149851_a(world: World, x: Int, y: Int, z: Int, isRemote: Boolean) = world.getBlockMetadata(x, y, z) < 2
	
	// can "do bonemealing" function be called?
	override fun func_149852_a(world: World?, random: Random, x: Int, y: Int, z: Int) = random.nextInt(3) == 0
	
	// "do bonemealing" function
	override fun func_149853_b(world: World, random: Random?, x: Int, y: Int, z: Int) {
		world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) + 1, 3)
	}
	
	override fun getEntry(world: World?, x: Int, y: Int, z: Int, player: EntityPlayer?, lexicon: ItemStack?) = AlfheimLexiconData.winery
}
