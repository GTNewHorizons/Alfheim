package alfheim.common.block

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.common.achievement.AlfheimAchievements
import alfheim.common.block.tile.TileAlfheimPortal
import alfheim.common.core.util.AlfheimConfig
import alfheim.common.item.AlfheimItems
import alfheim.common.item.AlfheimItems.ElvenResourcesMetas
import alfheim.common.lexicon.AlfheimLexiconData
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraft.world.*
import vazkii.botania.api.lexicon.ILexiconable

class BlockAlfheimPortal: BlockContainer(Material.wood), ILexiconable {
	
	init {
		setBlockName("AlfheimPortal")
		setBlockTextureName(ModInfo.MODID + ":AlfheimPortal")
		setCreativeTab(AlfheimCore.alfheimTab)
		setHardness(10.0f)
		setResistance(600.0f)
		setStepSound(Block.soundTypeWood)
	}
	
	override fun registerBlockIcons(reg: IIconRegister) {
		textures[0] = reg.registerIcon(ModInfo.MODID + ":AlfheimPortal")
		textures[1] = reg.registerIcon(ModInfo.MODID + ":AlfheimPortalActive")
		textures[2] = reg.registerIcon(ModInfo.MODID + ":AlfheimPortalInside")
	}
	
	override fun getIcon(side: Int, meta: Int) = if (meta == 0) textures[0]!! else textures[1]!!
	
	override fun createNewTileEntity(world: World, meta: Int) = TileAlfheimPortal()
	
	override fun onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		val newMeta = (world.getTileEntity(x, y, z) as TileAlfheimPortal).validMetadata
		if (newMeta == 0) return false
		
		if (world.provider.dimensionId != AlfheimConfig.dimensionIDAlfheim) {
			if (world.getBlockMetadata(x, y, z) == 0 && player.currentEquippedItem?.item === AlfheimItems.elvenResource && player.currentEquippedItem.itemDamage == ElvenResourcesMetas.InterdimensionalGatewayCore) {
				ASJUtilities.consumeItemStack(player.inventory, ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.InterdimensionalGatewayCore))
			} else
				return false
		}
		
		val did = (world.getTileEntity(x, y, z) as TileAlfheimPortal).onWanded(newMeta)
		if (did) player.addStat(AlfheimAchievements.alfheim, 1)
		return did
	}
	
	override fun getLightValue(world: IBlockAccess, x: Int, y: Int, z: Int) = if (world.getBlockMetadata(x, y, z) == 0) 0 else 15
	
	override fun breakBlock(world: World, x: Int, y: Int, z: Int, block: Block?, meta: Int) {
		if (world.provider.dimensionId != AlfheimConfig.dimensionIDAlfheim && meta != 0) world.spawnEntityInWorld(EntityItem(world, x + 0.5, y + 0.5, z + 0.5, ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.InterdimensionalGatewayCore)))
		super.breakBlock(world, x, y, z, block, meta)
	}
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack) = AlfheimLexiconData.portal
	
	companion object {
		val textures = arrayOfNulls<IIcon>(3)
	}
}