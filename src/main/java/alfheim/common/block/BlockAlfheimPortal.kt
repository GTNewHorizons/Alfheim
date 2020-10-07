package alfheim.common.block

import alexsocol.asjlib.*
import alfheim.api.ModInfo
import alfheim.client.core.helper.*
import alfheim.common.achievement.AlfheimAchievements
import alfheim.common.block.base.BlockContainerMod
import alfheim.common.block.tile.TileAlfheimPortal
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.item.AlfheimItems
import alfheim.common.item.material.ElvenResourcesMetas
import alfheim.common.lexicon.AlfheimLexiconData
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.*
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraft.world.*
import vazkii.botania.api.lexicon.ILexiconable

class BlockAlfheimPortal: BlockContainerMod(Material.wood), ILexiconable {
	
	init {
		setBlockName("AlfheimPortal")
		setBlockTextureName(ModInfo.MODID + ":AlfheimPortal")
		setHardness(10f)
		setResistance(600f)
		setStepSound(Block.soundTypeWood)
	}
	
	override fun loadTextures(map: TextureMap) {
		textures = Array(2) { InterpolatedIconHelper.forBlock(map, this, it) }
	}
	
	override fun registerBlockIcons(reg: IIconRegister) {
		blockIcon = IconHelper.forBlock(reg, this)
	}
	
	override fun onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		val newMeta = (world.getTileEntity(x, y, z) as TileAlfheimPortal).validMetadata
		if (newMeta == 0) return false
		
		if (world.provider.dimensionId != AlfheimConfigHandler.dimensionIDAlfheim) {
			if (world.getBlockMetadata(x, y, z) == 0 && player.currentEquippedItem?.item === AlfheimItems.elvenResource && player.currentEquippedItem.meta == ElvenResourcesMetas.InterdimensionalGatewayCore) {
				ASJUtilities.consumeItemStack(player.inventory, ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.InterdimensionalGatewayCore))
			} else
				return false
		}
		
		val did = (world.getTileEntity(x, y, z) as TileAlfheimPortal).onWanded(newMeta)
		if (did) player.addStat(AlfheimAchievements.alfheim, 1)
		return did
	}
	
	override fun isInterpolated() = true
	override fun getIcon(side: Int, meta: Int) = if (meta == 0) blockIcon else textures[0]
	override fun createNewTileEntity(world: World, meta: Int) = TileAlfheimPortal()
	override fun getLightValue(world: IBlockAccess, x: Int, y: Int, z: Int) = if (world.getBlockMetadata(x, y, z) == 0) 0 else 15
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack) = AlfheimLexiconData.portal
	
	override fun breakBlock(world: World, x: Int, y: Int, z: Int, block: Block?, meta: Int) {
		if (world.provider.dimensionId != AlfheimConfigHandler.dimensionIDAlfheim) run {
			val tile = world.getTileEntity(x, y, z) as? TileAlfheimPortal ?: return@run
			
			if (tile.activated) {
				tile.activated = false
				world.spawnEntityInWorld(EntityItem(world, x + 0.5, y + 0.5, z + 0.5, ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.InterdimensionalGatewayCore)))
			}
		}
		
		super.breakBlock(world, x, y, z, block, meta)
	}
	
	companion object {
		lateinit var textures: Array<IIcon?>
	}
}