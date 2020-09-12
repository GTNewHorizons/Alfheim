package alfheim.common.block

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.common.block.base.BlockContainerMod
import alfheim.common.block.tile.TileCracklingStar
import alfheim.common.item.AlfheimItems
import alfheim.common.item.block.ItemStarPlacer2
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.common.Optional
import cpw.mods.fml.relauncher.*
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.world.*
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.wand.IWandable
import java.util.*

class BlockCracklingStar: BlockContainerMod(Material.cloth), IWandable, ILexiconable {
	
	init {
		setBlockName("cracklingStar")
		val f = 0.25f
		setBlockBounds(f, f, f, 1f - f, 1f - f, 1f - f)
		setLightLevel(1f)
		setStepSound(soundTypeCloth)
	}
	
	@Optional.Method(modid = "easycoloredlights")
	override fun getLightValue(world: IBlockAccess, x: Int, y: Int, z: Int) =
		(world.getTileEntity(x, y, z) as TileCracklingStar).getLightColor()
	
	override fun registerBlockIcons(reg: IIconRegister) = Unit
	
	override fun getRenderType(): Int = -1
	
	override fun getIcon(side: Int, meta: Int) = Blocks.fire.getIcon(side, meta)!!
	
	override fun getItemDropped(meta: Int, rand: Random?, fortune: Int) = AlfheimItems.starPlacer2
	
	@SideOnly(Side.CLIENT)
	override fun getItem(world: World?, x: Int, y: Int, z: Int) = AlfheimItems.starPlacer2
	
	override fun isOpaqueCube(): Boolean = false
	
	override fun renderAsNormalBlock(): Boolean = false
	
	override fun getBlocksMovement(world: IBlockAccess?, x: Int, y: Int, z: Int): Boolean = true
	
	override fun getCollisionBoundingBoxFromPool(world: World?, x: Int, y: Int, z: Int): AxisAlignedBB? = null
	
	override fun getDrops(world: World, x: Int, y: Int, z: Int, metadata: Int, fortune: Int): ArrayList<ItemStack> = ArrayList()
	
	override fun onBlockHarvested(world: World, x: Int, y: Int, z: Int, meta: Int, player: EntityPlayer?) {
		if (player?.capabilities?.isCreativeMode != true) {
			val te = world.getTileEntity(x, y, z)
			if (te is TileCracklingStar) {
				val f = 0.5
				
				val color = te.color
				val stack = ItemStarPlacer2.colorStack(color)
				
				val entityitem = EntityItem(world, x.F + f, y.F + f, z.F + f, stack)
				
				val f3 = 0.05f
				entityitem.motionX = (world.rand.nextGaussian().F * f3).D
				entityitem.motionY = (world.rand.nextGaussian().F * f3 + 0.2f).D
				entityitem.motionZ = (world.rand.nextGaussian().F * f3).D
				world.spawnEntityInWorld(entityitem)
			}
		}
		
		super.onBlockHarvested(world, x, y, z, meta, player)
	}
	
	override fun getPickBlock(target: MovingObjectPosition?, world: World, x: Int, y: Int, z: Int, player: EntityPlayer?): ItemStack? {
		val te = world.getTileEntity(x, y, z)
		if (te is TileCracklingStar) {
			return ItemStarPlacer2.colorStack(te.color)
		}
		return super.getPickBlock(target, world, x, y, z, player)
	}
	
	override fun createNewTileEntity(world: World?, meta: Int) = TileCracklingStar()
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = AlfheimLexiconData.frozenStar
	
	companion object {
		val playerPositions = mutableMapOf<UUID, DimWithPos>()
	}
	
	override fun onUsedByWand(player: EntityPlayer?, stack: ItemStack?, world: World, x: Int, y: Int, z: Int, meta: Int): Boolean {
		if (player == null || world.isRemote) return false
		val dwp = playerPositions[player.uniqueID]
		
		val here = DimWithPos(world.provider.dimensionId, x, y, z)
		
		if (dwp == null)
			playerPositions[player.uniqueID] = here
		else {
			playerPositions.remove(player.uniqueID)
			if (dwp == here) {
				val te = world.getTileEntity(x, y, z) as? TileCracklingStar ?: return true
				te.pos.set(0, -1, 0)
			} else if (dwp.dim == here.dim) {
				val otherTe = world.getTileEntity(dwp.x, dwp.y, dwp.z) as? TileCracklingStar ?: return true
				otherTe.pos = Vector3(x.D, y.D, z.D)
				otherTe.markDirty()
				world.markBlockForUpdate(dwp.x, dwp.y, dwp.z)
			}
		}
		return true
	}
	
	data class DimWithPos(val dim: Int, val x: Int, val y: Int, val z: Int) {
		constructor(world: World, x: Int, y: Int, z: Int) : this(world.provider.dimensionId, x, y, z)
		
		override fun toString() = "$dim:$x:$y:$z"
		
		companion object {
			@JvmStatic
			fun fromString(s: String): DimWithPos {
				val split = s.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
				return DimWithPos(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]))
			}
		}
	}
}