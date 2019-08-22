package alfheim.common.block

import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.api.lib.LibRenderIDs
import alfheim.common.block.base.BlockContainerMod
import alfheim.common.block.tile.TileLivingwoodFunnel
import alfheim.common.core.helper.IconHelper
import alfheim.common.lexicon.ShadowFoxLexiconData
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.*
import net.minecraft.world.*
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.wand.IWandHUD
import java.util.*

class BlockFunnel: BlockContainerMod<TileLivingwoodFunnel>(Material.wood), IWandHUD, ILexiconable {
	
	private val random = Random()
	lateinit var top_icon: IIcon
	lateinit var inside_icon: IIcon
	lateinit var outside_icon: IIcon
	
	companion object {
		@SideOnly(Side.CLIENT)
		fun getHopperIcon(string: String): IIcon? {
			return when (string) {
				"funnel_top"    -> (ShadowFoxBlocks.livingwoodFunnel as BlockFunnel).top_icon
				"funnel_inside" -> (ShadowFoxBlocks.livingwoodFunnel as BlockFunnel).inside_icon
				else            -> (ShadowFoxBlocks.livingwoodFunnel as BlockFunnel).outside_icon
			}
		}
		
		fun getDirectionFromMetadata(meta: Int): Int = meta and 7
		fun getActiveStateFromMetadata(meta: Int): Boolean = (meta and 8) != 8
	}
	
	init {
		setBlockName("livingwoodFunnel")
		blockHardness = 2f
		
		setCreativeTab(AlfheimCore.baTab)
		setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f)
	}
	
	override fun isSideSolid(world: IBlockAccess?, x: Int, y: Int, z: Int, side: ForgeDirection?) =
		side == ForgeDirection.UP
	
	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	override fun setBlockBoundsBasedOnState(world: IBlockAccess?, x: Int, y: Int, z: Int) {
		setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f)
	}
	
	override fun addCollisionBoxesToList(world: World?, x: Int, y: Int, z: Int, axis: AxisAlignedBB?, bounds: MutableList<Any?>?, entity: Entity?) {
		setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.625f, 1.0f)
		super.addCollisionBoxesToList(world, x, y, z, axis, bounds, entity)
		val f = 0.125f
		setBlockBounds(0.0f, 0.0f, 0.0f, f, 1.0f, 1.0f)
		super.addCollisionBoxesToList(world, x, y, z, axis, bounds, entity)
		setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, f)
		super.addCollisionBoxesToList(world, x, y, z, axis, bounds, entity)
		setBlockBounds(1.0f - f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f)
		super.addCollisionBoxesToList(world, x, y, z, axis, bounds, entity)
		setBlockBounds(0.0f, 0.0f, 1.0f - f, 1.0f, 1.0f, 1.0f)
		super.addCollisionBoxesToList(world, x, y, z, axis, bounds, entity)
		setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f)
	}
	
	/**
	 * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
	 */
	override fun onBlockPlaced(world: World?, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float, meta: Int): Int {
		var j1 = Facing.oppositeSide[side]
		
		if (j1 == 1) j1 = 0
		return j1
	}
	
	override fun createNewTileEntity(var1: World, var2: Int): TileLivingwoodFunnel = TileLivingwoodFunnel()
	
	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	override fun onBlockAdded(world: World?, x: Int, y: Int, z: Int) {
		super.onBlockAdded(world, x, y, z)
		if (world != null) updateBlockData(world, x, y, z)
	}
	
	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
	 * their own) Args: x, y, z, neighbor Block
	 */
	override fun onNeighborBlockChange(world: World?, x: Int, y: Int, z: Int, neighbor: Block?) {
		if (world != null) updateBlockData(world, x, y, z)
	}
	
	private fun updateBlockData(world: World, x: Int, y: Int, z: Int) {
		val l = world.getBlockMetadata(x, y, z)
		val i1 = getDirectionFromMetadata(l)
		val flag = !world.isBlockIndirectlyGettingPowered(x, y, z)
		val flag1 = getActiveStateFromMetadata(l)
		
		if (flag != flag1) {
			world.setBlockMetadataWithNotify(x, y, z, i1 or (if (flag) 0 else 8), 4)
		}
	}
	
	override fun renderHUD(mc: Minecraft, res: ScaledResolution, world: World, x: Int, y: Int, z: Int) {
		(world.getTileEntity(x, y, z) as TileLivingwoodFunnel).renderHUD(mc, res)
	}
	
	override fun breakBlock(world: World, x: Int, y: Int, z: Int, block: Block?, meta: Int) {
		val tile = world.getTileEntity(x, y, z)
		
		if (tile != null && tile is TileLivingwoodFunnel) {
			for (i1 in 0 until tile.sizeInventory) {
				val itemstack = tile.getStackInSlot(i1)
				
				if (itemstack != null) {
					val f = this.random.nextFloat() * 0.8f + 0.1f
					val f1 = this.random.nextFloat() * 0.8f + 0.1f
					val f2 = this.random.nextFloat() * 0.8f + 0.1f
					
					while (itemstack.stackSize > 0) {
						var j1 = this.random.nextInt(21) + 10
						
						if (j1 > itemstack.stackSize) {
							j1 = itemstack.stackSize
						}
						
						itemstack.stackSize -= j1
						val entityitem = EntityItem(world, (x.toFloat() + f).toDouble(), (y.toFloat() + f1).toDouble(), (z.toFloat() + f2).toDouble(), ItemStack(itemstack.item, j1, itemstack.itemDamage))
						
						if (itemstack.hasTagCompound()) {
							entityitem.entityItem.tagCompound = itemstack.tagCompound.copy() as NBTTagCompound
						}
						
						val f3 = 0.05f
						entityitem.motionX = (this.random.nextGaussian().toFloat() * f3).toDouble()
						entityitem.motionY = (this.random.nextGaussian().toFloat() * f3 + 0.2f).toDouble()
						entityitem.motionZ = (this.random.nextGaussian().toFloat() * f3).toDouble()
						world.spawnEntityInWorld(entityitem)
					}
				}
			}
			
			world.func_147453_f(x, y, z, block)
		}
		
		super.breakBlock(world, x, y, z, block, meta)
	}
	
	/**
	 * The type of render function that is called for this block
	 */
	override fun getRenderType() = LibRenderIDs.idHopper
	
	/**
	 * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
	 */
	override fun renderAsNormalBlock() = false
	
	/**
	 * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
	 * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
	 */
	override fun isOpaqueCube() = false
	
	/**
	 * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
	 * coordinates.  Args: blockAccess, x, y, z, side
	 */
	@SideOnly(Side.CLIENT)
	override fun shouldSideBeRendered(world: IBlockAccess, x: Int, y: Int, z: Int, side: Int) = true
	
	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@SideOnly(Side.CLIENT)
	override fun getIcon(side: Int, meta: Int): IIcon {
		if (side == 1) return top_icon
		return outside_icon
	}
	
	/**
	 * If this returns true, then comparators facing away from this block will use the value from
	 * getComparatorInputOverride instead of the actual redstone signal strength.
	 */
	override fun hasComparatorInputOverride(): Boolean = true
	
	/**
	 * If hasComparatorInputOverride returns true, the return value from this is used instead of the redstone signal
	 * strength when this block inputs to a comparator.
	 */
	override fun getComparatorInputOverride(world: World?, x: Int, y: Int, z: Int, side: Int): Int {
		if (world == null) return 0
		return Container.calcRedstoneFromInventory(getTile(world, x, y, z))
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerBlockIcons(par1IconRegister: IIconRegister) {
		top_icon = IconHelper.forName(par1IconRegister, "funnel_top")
		inside_icon = IconHelper.forName(par1IconRegister, "funnel_inside")
		outside_icon = IconHelper.forName(par1IconRegister, "funnel_outside")
	}
	
	fun getTile(world: IBlockAccess, x: Int, y: Int, z: Int): TileLivingwoodFunnel? =
		world.getTileEntity(x, y, z) as TileLivingwoodFunnel
	
	/**
	 * Gets the icon name of the ItemBlock corresponding to this block. Used by hoppers.
	 */
	@SideOnly(Side.CLIENT)
	override fun getItemIconName(): String = "${ModInfo.MODID}:livingwood_funnel"
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) =
		ShadowFoxLexiconData.livingwoodFunnel
}
