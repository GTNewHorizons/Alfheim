package alfheim.common.block

import alexsocol.asjlib.extendables.block.BlockModContainerMeta
import alfheim.api.ModInfo
import alfheim.api.lib.LibRenderIDs
import alfheim.common.block.tile.TileAlfheimPylon
import alfheim.common.core.util.AlfheimTab
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.common.Optional
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraft.world.World
import thaumcraft.api.crafting.IInfusionStabiliser
import vazkii.botania.api.lexicon.*
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.core.handler.ConfigHandler

@Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.crafting.IInfusionStabiliser", striprefs = true)
class BlockAlfheimPylon: BlockModContainerMeta(Material.iron, 3, ModInfo.MODID, "AlfheimPylons", AlfheimTab, 5.5f), ILexiconable, IInfusionStabiliser {
	
	init {
		val f = 1f / 16f * 2f
		setBlockBounds(f, 0f, f, 1f - f, 1f / 16f * 21f, 1f - f)
		setLightLevel(0.5f)
	}
	
	override fun registerBlockIcons(reg: IIconRegister) = Unit
	
	override fun getIcon(side: Int, meta: Int): IIcon = // elementium for pink; elvorium for orange; redstone for red
		if (meta == 2) Blocks.redstone_block.getIcon(side, 0) else if (meta == 1) AlfheimBlocks.alfStorage.getIcon(side, 0) else ModBlocks.storage.getIcon(side, 2)
	
	override fun isOpaqueCube() = false
	override fun renderAsNormalBlock() = false
	override fun getRenderType() = LibRenderIDs.idPylon
	override fun createNewTileEntity(world: World, meta: Int) = TileAlfheimPylon()
	override fun canStabaliseInfusion(world: World, x: Int, y: Int, z: Int) = ConfigHandler.enableThaumcraftStablizers
	override fun getEnchantPowerBonus(world: World, x: Int, y: Int, z: Int) = if (world.getBlockMetadata(x, y, z) == 0) 10f else 18f // pink 10; orange and red 15
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack): LexiconEntry {
		val meta = world.getBlockMetadata(x, y, z)
		return if (meta == 2) AlfheimLexiconData.soul else if (meta == 1) AlfheimLexiconData.trade else AlfheimLexiconData.pylons
	}
}
