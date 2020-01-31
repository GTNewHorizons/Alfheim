package alfheim.common.block

import alexsocol.asjlib.extendables.TileItemContainer
import alfheim.api.lib.LibRenderIDs
import alfheim.common.block.base.BlockContainerMod
import alfheim.common.block.tile.TileAnyavil
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.MathHelper
import net.minecraft.world.*
import vazkii.botania.api.internal.IManaBurst
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.mana.IManaTrigger
import vazkii.botania.api.wand.IWandHUD
import kotlin.math.min

class BlockAnyavil: BlockContainerMod(Material.iron), IManaTrigger, IWandHUD, ILexiconable {
	
	init {
		setBlockName("Anyavil")
		setBlockTextureName("botania:storage2")
		setLightOpacity(0)
		setHardness(5.0f)
		setResistance(2000.0f)
		setStepSound(Block.soundTypeAnvil)
	}
	
	override fun registerBlockIcons(reg: IIconRegister) {
		blockIcon = reg.registerIcon(getTextureName());
	}
	
	override fun renderAsNormalBlock() = false
	override fun isOpaqueCube() = false
	override fun getRenderType() = LibRenderIDs.idAnyavil
	
	override fun onBlockPlacedBy(world: World, x: Int, y: Int, z: Int, entity: EntityLivingBase, stack: ItemStack?) {
		val l = MathHelper.floor_double((entity.rotationYaw * 4.0f / 360.0f).toDouble() + 0.5) and 3
		world.setBlockMetadataWithNotify(x, y, z, l, 3)
	}
	
	override fun onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		val te = world.getTileEntity(x, y, z) as TileItemContainer
		val stack = player.inventory.getCurrentItem()
		if (player.isSneaking) return false
		if (te.item != null) {
			if (!world.isRemote) {
				val entityitem = EntityItem(world, x + 0.5, y + 0.5, z + 0.5, te.item!!)
				world.spawnEntityInWorld(entityitem)
			}
			te.item = null
		}
		if (stack != null && stack.stackSize == 1 && stack.item.isDamageable && GameRegistry.findUniqueIdentifierFor(stack.item).toString() !in AlfheimConfigHandler.anyavilBL) {
			te.item = stack.copy()
			te.item!!.stackSize = stack.stackSize
			stack.stackSize = 0
		}
		
		world.updateLightByType(EnumSkyBlock.Sky, x, y, z)
		
		return true
	}
	
	override fun setBlockBoundsBasedOnState(world: IBlockAccess, x: Int, y: Int, z: Int) {
		val l = world.getBlockMetadata(x, y, z) and 3
		
		if (l != 3 && l != 1) {
			setBlockBounds(0.125f, 0.0f, 0.0f, 0.875f, 1.0f, 1.0f)
		} else {
			setBlockBounds(0.0f, 0.0f, 0.125f, 1.0f, 1.0f, 0.875f)
		}
	}
	
	@SideOnly(Side.CLIENT)
	override fun getMixedBrightnessForBlock(world: IBlockAccess, x: Int, y: Int, z: Int): Int {
		return world.getLightBrightnessForSkyBlocks(x, y, z, world.getBlock(x, y, z).getLightValue(world, x, y - 1, z))
	}
	
	override fun breakBlock(world: World, x: Int, y: Int, z: Int, block: Block?, meta: Int) {
		val te = world.getTileEntity(x, y, z) as TileItemContainer
		if (te.item != null) {
			val entityitem = EntityItem(world, x + 0.5, y + 0.5, z + 0.5, te.item!!)
			world.spawnEntityInWorld(entityitem)
			te.item = null
		}
		
		super.breakBlock(world, x, y, z, block, meta)
	}
	
	override fun hasComparatorInputOverride() = true
	
	override fun getComparatorInputOverride(world: World, x: Int, y: Int, z: Int, side: Int): Int {
		val te = world.getTileEntity(x, y, z) as TileItemContainer
		if (te.item != null) {
			if (te.item!!.itemDamage == te.item!!.maxDamage) return 1
			if (te.item!!.itemDamage == 0) return 15
			val pow = MathHelper.ceiling_double_int((te.item!!.maxDamage - te.item!!.itemDamage) * 15.0 / te.item!!.maxDamage)
			return min(pow, 14)
		}
		
		return 0
	}
	
	override fun createNewTileEntity(world: World, meta: Int) = TileAnyavil()
	
	override fun onBurstCollision(burst: IManaBurst, world: World, x: Int, y: Int, z: Int) {
		val tile = world.getTileEntity(x, y, z)
		if (tile is TileAnyavil) tile.onBurstCollision(burst, world)
		world.notifyBlocksOfNeighborChange(x, y, z, this)
	}
	
	@SideOnly(Side.CLIENT)
	override fun renderHUD(mc: Minecraft, res: ScaledResolution, world: World, x: Int, y: Int, z: Int) {
		val tile = world.getTileEntity(x, y, z)
		if (tile is TileAnyavil) tile.renderHUD(res)
	}
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack) = AlfheimLexiconData.anyavil
}