package alfheim.common.block

import alexsocol.asjlib.*
import alfheim.common.block.base.BlockMod
import alfheim.common.block.tile.TileItemDisplay
import alfheim.common.core.helper.IconHelper
import alfheim.common.item.block.ItemUniqueSubtypedBlockMod
import alfheim.common.lexicon.ShadowFoxLexiconData
import cpw.mods.fml.common.IFuelHandler
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.*
import net.minecraft.item.*
import net.minecraft.util.*
import net.minecraft.world.World
import vazkii.botania.api.lexicon.ILexiconable
import java.util.*
import kotlin.math.min

class BlockItemDisplay: BlockMod(Material.wood), ILexiconable, ITileEntityProvider, IFuelHandler {
	
	val TYPES = 3
	var icons: Array<IIcon?> = arrayOfNulls(TYPES)
	var sideIcons: Array<IIcon?> = arrayOfNulls(TYPES)
	
	init {
		isBlockContainer = true
		setBlockName("itemDisplay")
		setHardness(2f)
		setStepSound(soundTypeWood)
		setBlockBounds(0f, 0f, 0f, 1f, 0.5f, 1f)
		
		GameRegistry.registerFuelHandler(this)
	}
	
	override fun getSubBlocks(item: Item?, tab: CreativeTabs?, list: MutableList<Any?>?) {
		if (list != null && item != null)
			for (i in 0 until TYPES)
				list.add(ItemStack(item, 1, i))
	}
	
	override fun hasComparatorInputOverride() = true
	
	override fun getComparatorInputOverride(world: World?, x: Int, y: Int, z: Int, direction: Int): Int {
		if (world == null) return 0
		return Container.calcRedstoneFromInventory(world.getTileEntity(x, y, z)!! as TileItemDisplay)
	}
	
	override fun shouldRegisterInNameSet() = false
	
	override fun setBlockName(name: String): Block {
		register(name)
		return super.setBlockName(name)
	}
	
	internal fun register(name: String) {
		GameRegistry.registerBlock(this, ItemUniqueSubtypedBlockMod::class.java, name)
	}
	
	override fun addCollisionBoxesToList(world: World, x: Int, y: Int, z: Int, axis: AxisAlignedBB, bounds: MutableList<Any?>, entity: Entity?) {
		setBlockBounds(0f, 0f, 0f, 1f, 0.5F, 1f)
		super.addCollisionBoxesToList(world, x, y, z, axis, bounds, entity)
	}
	
	override fun registerBlockIcons(reg: IIconRegister) {
		for (i in 0 until TYPES) {
			icons[i] = IconHelper.forBlock(reg, this, i)
			sideIcons[i] = IconHelper.forBlock(reg, this, "Side$i")
		}
		
	}
	
	override fun createNewTileEntity(world: World?, meta: Int) = TileItemDisplay()
	
	@SideOnly(Side.CLIENT)
	override fun getIcon(side: Int, meta: Int) =
		if (side == 1 || side == 0) icons[min(meta, TYPES - 1)]!! else sideIcons[min(meta, TYPES - 1)]!!
	
	override fun onBlockActivated(world: World?, x: Int, y: Int, z: Int, player: EntityPlayer?, meta: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		if (world!!.isRemote || player == null) return true
		
		val tileEntity = world.getTileEntity(x, y, z)
		
		if (tileEntity is TileItemDisplay) {
			if (tileEntity.get(0) != null) {
				dropItemsAtEntity(world, x, y, z, player)
				return true
			}
			
			if (player.currentEquippedItem != null) {
				val item = player.currentEquippedItem.copy()
				item.stackSize = 1
				tileEntity.set(0, item)
				
				--player.currentEquippedItem.stackSize
				if (player.currentEquippedItem.stackSize == 0) {
					player.setCurrentItemOrArmor(0, null)
				}
				
				player.inventory.markDirty()
			}
		}
		
		return true
	}
	
	fun dropItemsAtEntity(world: World, x: Int, y: Int, z: Int, entity: Entity) {
		Random()
		val tileEntity = world.getTileEntity(x, y, z)
		if (tileEntity is IInventory) {
			for (i in 0 until tileEntity.sizeInventory) {
				val item = tileEntity.get(i)
				if (item != null && item.stackSize > 0) {
					val entityItem = EntityItem(world, entity.posX, entity.posY + (entity.eyeHeight / 2f).D, entity.posZ, item.copy())
					world.spawnEntityInWorld(entityItem)
					tileEntity.set(i, null)
				}
			}
		}
	}
	
	override fun getPickBlock(target: MovingObjectPosition?, world: World, x: Int, y: Int, z: Int): ItemStack {
		val meta = world.getBlockMetadata(x, y, z)
		return ItemStack(this, 1, meta)
	}
	
	override fun damageDropped(par1: Int): Int = par1
	
	override fun isOpaqueCube(): Boolean = false
	
	override fun renderAsNormalBlock(): Boolean = false
	
	override fun hasTileEntity(metadata: Int): Boolean = true
	
	override fun onBlockEventReceived(world: World?, x: Int, y: Int, z: Int, event: Int, eventArg: Int): Boolean {
		super.onBlockEventReceived(world, x, y, z, event, eventArg)
		val tileentity = world!!.getTileEntity(x, y, z)
		return tileentity?.receiveClientEvent(event, eventArg) ?: false
	}
	
	override fun breakBlock(world: World, x: Int, y: Int, z: Int, block: Block, meta: Int) {
		Random()
		val tileEntity = world.getTileEntity(x, y, z)
		if (tileEntity is IInventory) {
			for (i in 0 until tileEntity.sizeInventory) {
				val item = tileEntity.get(i)
				if (item != null && item.stackSize > 0) {
					val entityItem = EntityItem(world, x.D, y.D, z.D, item.copy())
					world.spawnEntityInWorld(entityItem)
				}
			}
		}
		super.breakBlock(world, x, y, z, block, meta)
		world.removeTileEntity(x, y, z)
	}
	
	override fun createTileEntity(world: World?, meta: Int) = TileItemDisplay()
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) =
		ShadowFoxLexiconData.itemDisplay
	
	override fun getBurnTime(fuel: ItemStack) = if (fuel.item === this.toItem()) 150 else 0
}
