package alfheim.common.block.mana

import alexsocol.asjlib.extendables.TileItemContainer
import alfheim.AlfheimCore
import alfheim.api.lib.LibRenderIDs
import alfheim.common.block.tile.TileTransferer
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.world.*
import vazkii.botania.api.lexicon.*
import vazkii.botania.api.wand.*
import vazkii.botania.common.block.*
import vazkii.botania.common.item.ModItems
import java.util.*

class BlockTransferer: BlockModContainer<TileEntity>(Material.wood), IWandable, IWandHUD, ILexiconable, IWireframeAABBProvider {
	
	internal val random: Random
	
	init {
		this.setBlockName("Transferer")
		this.setCreativeTab(AlfheimCore.alfheimTab)
		this.setHardness(2.0f)
		this.setStepSound(Block.soundTypeWood)
		random = Random()
	}
	
	override fun shouldRegisterInNameSet(): Boolean {
		return false
	}
	
	override fun registerBlockIcons(par1IconRegister: IIconRegister) {
		// NO-OP
	}
	
	override fun onBlockPlacedBy(par1World: World?, par2: Int, par3: Int, par4: Int, par5EntityLivingBase: EntityLivingBase?, par6ItemStack: ItemStack?) {
		val orientation = BlockPistonBase.determineOrientation(par1World, par2, par3, par4, par5EntityLivingBase!!)
		val spreader = par1World!!.getTileEntity(par2, par3, par4) as TileTransferer
		par1World.setBlockMetadataWithNotify(par2, par3, par4, 0, 1 or 2)
		
		when (orientation) {
			0    -> spreader.rotY = -90f
			1    -> spreader.rotY = 90f
			2    -> spreader.rotX = 270f
			3    -> spreader.rotX = 90f
			
			4    -> {
			}
			
			else -> spreader.rotX = 180f
		}
	}
	
	override fun isOpaqueCube(): Boolean {
		return false
	}
	
	override fun renderAsNormalBlock(): Boolean {
		return false
	}
	
	override fun getIcon(par1: Int, par2: Int): IIcon {
		return ModBlocks.dreamwood.getIcon(par1, 0)
	}
	
	override fun getRenderType(): Int {
		return LibRenderIDs.idTransferer
	}
	
	override fun onBlockActivated(world: World?, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		if (player.isSneaking) return false
		val stack = player.inventory.getCurrentItem()
		if (stack != null && stack.item === ModItems.twigWand) return false
		val tile = world!!.getTileEntity(x, y, z) as? TileTransferer ?: return false
		val te = tile as TileItemContainer
		
		if (te.item != null) {
			if (!world.isRemote) {
				val entityitem = EntityItem(world, x + 0.5, y + 0.5, z + 0.5, te.item!!)
				world.spawnEntityInWorld(entityitem)
			}
			te.item = null
		}
		
		if (stack != null) {
			te.item = stack.copy()
			te.item!!.stackSize = stack.stackSize
			stack.stackSize = 0
		}
		
		world.setTileEntity(x, y, z, te)
		world.updateLightByType(EnumSkyBlock.Sky, x, y, z)
		world.markTileEntityChunkModified(x, y, z, te)
		
		return true
	}
	
	override fun breakBlock(world: World, x: Int, y: Int, z: Int, block: Block?, meta: Int) {
		val te = world.getTileEntity(x, y, z) as TileItemContainer
		if (te.item != null) {
			val entityitem = EntityItem(world, x + 0.5, y + 0.5, z + 0.5, te.item!!.copy())
			world.spawnEntityInWorld(entityitem)
			te.item = null
		}
		world.func_147453_f(x, y, z, block)
		
		super.breakBlock(world, x, y, z, block, meta)
	}
	
	override fun onUsedByWand(player: EntityPlayer, stack: ItemStack, world: World, x: Int, y: Int, z: Int, side: Int): Boolean {
		(world.getTileEntity(x, y, z) as TileTransferer).onWanded(player)
		return true
	}
	
	override fun createNewTileEntity(world: World, meta: Int): TileEntity {
		return TileTransferer()
	}
	
	override fun renderHUD(mc: Minecraft, res: ScaledResolution, world: World, x: Int, y: Int, z: Int) {
		(world.getTileEntity(x, y, z) as TileTransferer).renderHUD(mc, res)
	}
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack): LexiconEntry? {
		return null // AlfheimLexiconData.trans; BACK
	}
	
	override fun getWireframeAABB(world: World, x: Int, y: Int, z: Int): AxisAlignedBB {
		val f = 1f / 16f
		return AxisAlignedBB.getBoundingBox((x + f).toDouble(), (y + f).toDouble(), (z + f).toDouble(), (x + 1 - f).toDouble(), (y + 1 - f).toDouble(), (z + 1 - f).toDouble())
	}
}