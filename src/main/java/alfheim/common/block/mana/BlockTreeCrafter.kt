package alfheim.common.block.mana

import alexsocol.asjlib.toItem
import alfheim.api.lib.LibRenderIDs
import alfheim.client.core.helper.IconHelper
import alfheim.client.render.tile.MultipassRenderer
import alfheim.common.block.base.*
import alfheim.common.block.tile.TileTreeCrafter
import alfheim.common.lexicon.AlfheimLexiconData
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.wand.IWandHUD
import java.util.*

class BlockTreeCrafter(name: String, val block: Block): BlockContainerMod(Material.wood), IWandHUD, ILexiconable, IMultipassRenderer {
	
	internal var random: Random
	
	init {
		setHardness(3f)
		setResistance(5f)
		setLightLevel(1f)
		setStepSound(Block.soundTypeWood)
		setBlockName(name)
		random = Random()
	}
	
	// this is not working
	override fun registerBlockIcons(reg: IIconRegister) {
		blockIcon = IconHelper.forName(reg, "treeCrafter")
	}
	
	override fun isOpaqueCube() = false
	override fun createNewTileEntity(var1: World, var2: Int) = TileTreeCrafter()
	override fun hasComparatorInputOverride() = true
	override fun getComparatorInputOverride(par1World: World?, par2: Int, par3: Int, par4: Int, par5: Int) = (par1World!!.getTileEntity(par2, par3, par4) as TileTreeCrafter).signal
	override fun getItemDropped(meta: Int, random: Random?, fortune: Int) = innerBlock(0).toItem()
	override fun damageDropped(meta: Int) = meta
	override fun getDamageValue(world: World, x: Int, y: Int, z: Int) = world.getBlockMetadata(x, y, z)
	override fun renderHUD(mc: Minecraft, res: ScaledResolution, world: World, x: Int, y: Int, z: Int) = (world.getTileEntity(x, y, z) as TileTreeCrafter).renderHUD(mc, res)
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = AlfheimLexiconData.treeCrafting
	override fun renderAsNormalBlock(): Boolean = false
	override fun canRenderInPass(pass: Int) = true.also { MultipassRenderer.pass = pass }
	override fun getRenderBlockPass(): Int = 1
	override fun getRenderType(): Int = LibRenderIDs.idMultipass
	override fun innerBlock(meta: Int): Block = block
}
