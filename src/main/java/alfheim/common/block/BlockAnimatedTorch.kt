package alfheim.common.block

import alfheim.api.ModInfo
import alfheim.api.block.IHourglassTrigger
import alfheim.api.lib.LibRenderIDs
import alfheim.common.block.base.BlockContainerMod
import alfheim.common.block.tile.TileAnimatedTorch
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.relauncher.*
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.*
import vazkii.botania.api.internal.IManaBurst
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.mana.IManaTrigger
import vazkii.botania.api.wand.*

class BlockAnimatedTorch: BlockContainerMod(Material.circuits), IHourglassTrigger, IWandable, IManaTrigger, IWandHUD, ILexiconable {
	
	init {
		setBlockBounds(0f, 0f, 0f, 1f, 0.25f, 1f)
		setBlockName("AnimatedTorch")
		setBlockTextureName(ModInfo.MODID + ":AnimatedTorch")
		setLightLevel(0.5f)
	}
	
	override fun onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		if (player.isSneaking && player.heldItem == null) {
			val te = world.getTileEntity(x, y, z) as? TileAnimatedTorch ?: return false
			
			te.handRotate()
			world.updateLightByType(EnumSkyBlock.Sky, x, y, z)
			
			return true
		}
		
		return false
	}
	
	override fun onBlockEventReceived(world: World, x: Int, y: Int, z: Int, id: Int, param: Int): Boolean {
		super.onBlockEventReceived(world, x, y, z, id, param)
		val tile = world.getTileEntity(x, y, z)
		return tile != null && tile.receiveClientEvent(id, param)
	}
	
	override fun onUsedByWand(player: EntityPlayer?, stack: ItemStack, world: World, x: Int, y: Int, z: Int, side: Int): Boolean {
		val tile = world.getTileEntity(x, y, z) as? TileAnimatedTorch ?: return false
		
		tile.onWanded()
		world.updateLightByType(EnumSkyBlock.Sky, x, y, z)
		
		return true
	}
	
	override fun onBlockPlacedBy(world: World, x: Int, y: Int, z: Int, entity: EntityLivingBase?, stack: ItemStack?) = (world.getTileEntity(x, y, z) as TileAnimatedTorch).onPlace(entity)
	override fun onBurstCollision(burst: IManaBurst, world: World, x: Int, y: Int, z: Int) = if (!burst.isFake) (world.getTileEntity(x, y, z) as TileAnimatedTorch).toggle() else Unit
	override fun onTriggeredByHourglass(world: World, x: Int, y: Int, z: Int, hourglass: TileEntity) = (world.getTileEntity(x, y, z) as TileAnimatedTorch).toggle()
	
	@SideOnly(Side.CLIENT)
	override fun renderHUD(mc: Minecraft, res: ScaledResolution, world: World, x: Int, y: Int, z: Int) = (world.getTileEntity(x, y, z) as TileAnimatedTorch).renderHUD(mc, res)
	override fun canProvidePower() = true
	override fun isProvidingStrongPower(world: IBlockAccess?, x: Int, y: Int, z: Int, side: Int) = isProvidingWeakPower(world!!, x, y, z, side)
	
	override fun isProvidingWeakPower(world: IBlockAccess, x: Int, y: Int, z: Int, side: Int): Int {
		val tile = world.getTileEntity(x, y, z) as TileAnimatedTorch
		if (tile.rotating) return 0
		return if (TileAnimatedTorch.SIDES[tile.side].ordinal == side) 15 else 0
	}
	
	override fun getRenderType() = LibRenderIDs.idAniTorch
	override fun isOpaqueCube() = false
	override fun isNormalCube() = false
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack) = AlfheimLexiconData.animatedTorch
	override fun createNewTileEntity(p_149915_1_: World, p_149915_2_: Int) = TileAnimatedTorch()
	
	override fun onBlockDestroyedByPlayer(world: World, x: Int, y: Int, z: Int, meta: Int) {
		world.notifyBlocksOfNeighborChange(x, y, z, this)
		super.onBlockDestroyedByPlayer(world, x, y, z, meta)
	}
}