package alfheim.common.block

import alfheim.AlfheimCore
import alfheim.api.lib.LibRenderIDs
import alfheim.common.block.tile.TileAlfheimPylon
import alfheim.common.core.registry.AlfheimBlocks
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.common.Optional
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.IIcon
import net.minecraft.world.World
import thaumcraft.api.crafting.IInfusionStabiliser
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry
import vazkii.botania.common.block.BlockModContainer
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.core.handler.ConfigHandler

@Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.crafting.IInfusionStabiliser", striprefs = true)
class BlockAlfheimPylon: BlockModContainer<*>(Material.iron), ILexiconable, IInfusionStabiliser {
	
	init {
		val f = 1f / 16f * 2f
		this.setBlockBounds(f, 0f, f, 1f - f, 1f / 16f * 21f, 1f - f)
		this.setBlockName("AlfheimPylons")
		this.setCreativeTab(AlfheimCore.alfheimTab)
		this.setLightLevel(0.5f)
		this.setHardness(5.5f)
		this.setStepSound(Block.soundTypeMetal)
	}
	
	override fun shouldRegisterInNameSet(): Boolean {
		return false
	}
	
	override fun registerBlockIcons(reg: IIconRegister) {
		// NO-OP
	}
	
	override fun damageDropped(meta: Int): Int {
		return meta
	}
	
	override fun getSubBlocks(item: Item, tab: CreativeTabs?, subs: MutableList<*>) {
		super.getSubBlocks(item, tab, subs) // elven		(pink)
		subs.add(ItemStack(item, 1, 1)) // elvorium	(orange)
		subs.add(ItemStack(item, 1, 2)) // anti		(red)
	}
	
	override fun getIcon(side: Int, meta: Int): IIcon { // elementium for pink; elvorium for orange; redstone for red
		return if (meta == 2) Blocks.redstone_block.getIcon(side, 0) else if (meta == 1) AlfheimBlocks.elvoriumBlock.getIcon(side, 0) else ModBlocks.storage.getIcon(side, 2)
	}
	
	override fun isOpaqueCube(): Boolean {
		return false
	}
	
	override fun renderAsNormalBlock(): Boolean {
		return false
	}
	
	override fun getRenderType(): Int {
		return LibRenderIDs.idPylon
	}
	
	override fun getEnchantPowerBonus(world: World, x: Int, y: Int, z: Int): Float {
		return (if (world.getBlockMetadata(x, y, z) == 0) 8 else 15).toFloat() // pink 8; orange and red 15
	}
	
	override fun createNewTileEntity(world: World, meta: Int): TileEntity {
		return TileAlfheimPylon()
	}
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack): LexiconEntry {
		val meta = world.getBlockMetadata(x, y, z)
		return if (meta == 2) AlfheimLexiconData.soul else if (meta == 1) AlfheimLexiconData.trade else AlfheimLexiconData.pylons
	}
	
	override fun canStabaliseInfusion(world: World, x: Int, y: Int, z: Int): Boolean {
		return ConfigHandler.enableThaumcraftStablizers
	}
}
