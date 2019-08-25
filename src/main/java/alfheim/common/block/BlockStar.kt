package alfheim.common.block

import alfheim.common.block.base.BlockMod
import alfheim.common.block.tile.TileEntityStar
import alfheim.common.item.block.ItemStarPlacer
import alfheim.common.lexicon.ShadowFoxLexiconData
import cpw.mods.fml.common.Optional
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.world.*
import vazkii.botania.api.lexicon.ILexiconable
import java.util.*

/**
 * @author WireSegal
 * Created at 9:23 PM on 2/6/16.
 */
class BlockStar(name: String = "starBlock"): BlockMod(Material.cloth), ILexiconable {
	
	init {
		setBlockName(name)
		val f = 0.25f
		setBlockBounds(f, f, f, 1.0f - f, 1.0f - f, 1.0f - f)
		setLightLevel(1.0f)
		setStepSound(soundTypeCloth)
	}
	
	@Optional.Method(modid = "easycoloredlights")
	override fun getLightValue(world: IBlockAccess, x: Int, y: Int, z: Int) =
		(world.getTileEntity(x, y, z) as TileEntityStar).getLightColor()
	
	override fun registerBlockIcons(par1IconRegister: IIconRegister) = Unit
	
	override fun getRenderType(): Int = -1
	
	override fun getIcon(side: Int, meta: Int) = Blocks.fire.getIcon(side, meta)!!
	
	override fun isOpaqueCube(): Boolean = false
	
	override fun renderAsNormalBlock(): Boolean = false
	
	override fun hasTileEntity(metadata: Int): Boolean = true
	
	override fun getBlocksMovement(world: IBlockAccess?, x: Int, y: Int, z: Int): Boolean = true
	
	override fun getCollisionBoundingBoxFromPool(world: World?, x: Int, y: Int, z: Int): AxisAlignedBB? = null
	
	override fun getDrops(world: World, x: Int, y: Int, z: Int, metadata: Int, fortune: Int): ArrayList<ItemStack> = ArrayList()
	
	override fun onBlockHarvested(world: World, x: Int, y: Int, z: Int, meta: Int, player: EntityPlayer?) {
		if (player?.capabilities?.isCreativeMode != true) {
			val te = world.getTileEntity(x, y, z)
			if (te is TileEntityStar) {
				val f = 0.5
				
				val color = te.getColor()
				val stack = ItemStarPlacer.colorStack(color)
				
				val entityitem = EntityItem(world, x.toFloat() + f, y.toFloat() + f, z.toFloat() + f, stack)
				
				val f3 = 0.05f
				entityitem.motionX = (world.rand.nextGaussian().toFloat() * f3).toDouble()
				entityitem.motionY = (world.rand.nextGaussian().toFloat() * f3 + 0.2f).toDouble()
				entityitem.motionZ = (world.rand.nextGaussian().toFloat() * f3).toDouble()
				world.spawnEntityInWorld(entityitem)
			}
		}
		
		super.onBlockHarvested(world, x, y, z, meta, player)
	}
	
	override fun getPickBlock(target: MovingObjectPosition, world: World, x: Int, y: Int, z: Int, player: EntityPlayer): ItemStack? {
		val te = world.getTileEntity(x, y, z)
		if (te is TileEntityStar) {
			return ItemStarPlacer.colorStack(te.getColor())
		}
		return super.getPickBlock(target, world, x, y, z, player)
	}
	
	override fun createTileEntity(world: World?, meta: Int) = TileEntityStar()
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = ShadowFoxLexiconData.frozenStar
}
