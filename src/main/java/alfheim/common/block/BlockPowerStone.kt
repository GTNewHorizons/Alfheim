package alfheim.common.block

import alexsocol.asjlib.extendables.block.BlockModContainerMeta
import alfheim.api.ModInfo
import alfheim.api.lib.LibRenderIDs
import alfheim.common.block.tile.TilePowerStone
import alfheim.common.core.util.AlfheimTab
import alfheim.common.lexicon.AlfheimLexiconData
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import vazkii.botania.api.lexicon.ILexiconable

class BlockPowerStone: BlockModContainerMeta(Material.rock, 5, ModInfo.MODID, "PowerStone", AlfheimTab), ILexiconable {
	init {
		setBlockUnbreakable()
	}
	
	override fun onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, hitX: Float, hitY: Float, hitZ: Float) = (world.getTileEntity(x, y, z) as? TilePowerStone)?.onBlockActivated(player) ?: false
	override fun createNewTileEntity(world: World?, meta: Int) = TilePowerStone()
	override fun isOpaqueCube() = false
	override fun renderAsNormalBlock() = false
	override fun getRenderType() = LibRenderIDs.idPowerStone
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack) = AlfheimLexiconData.shrines
	override fun registerBlockIcons(reg: IIconRegister) = Unit
	override fun getIcon(side: Int, meta: Int) = AlfheimBlocks.manaInfuser.getIcon(0, 2)
}