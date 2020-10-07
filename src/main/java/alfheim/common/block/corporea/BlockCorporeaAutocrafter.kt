package alfheim.common.block.corporea

import alexsocol.asjlib.*
import alfheim.common.block.base.BlockContainerMod
import alfheim.common.block.tile.corporea.TileCorporeaAutocrafter
import alfheim.client.core.helper.IconHelper
import alfheim.common.lexicon.AlfheimLexiconData
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.*
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.world.*
import org.lwjgl.opengl.*
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.wand.*
import kotlin.math.max

class BlockCorporeaAutocrafter: BlockContainerMod(Material.iron), ILexiconable, IWandable, IWandHUD {
	
	lateinit var iconSide: IIcon
	
	init {
		setBlockName("CorporeaAutocrafter")
		setHardness(5.5f)
		setStepSound(Block.soundTypeMetal)
	}
	
	override fun registerBlockIcons(reg: IIconRegister) {
		super.registerBlockIcons(reg)
		iconSide = IconHelper.forBlock(reg, this, "Side")
	}
	
	override fun getIcon(side: Int, meta: Int) = if (side < 2) blockIcon!! else iconSide
	
	override fun breakBlock(world: World, x: Int, y: Int, z: Int, block: Block?, meta: Int) {
		onUsedByWand(null, null, world, x, y, z, 0)
		world.func_147453_f(x, y, z, block)
		super.breakBlock(world, x, y, z, block, meta)
	}
	
	override fun createNewTileEntity(world: World?, meta: Int) = TileCorporeaAutocrafter()
	
	override fun onBlockClicked(world: World, x: Int, y: Int, z: Int, player: EntityPlayer) {
		(world.getTileEntity(x, y, z) as? TileCorporeaAutocrafter)?.let {
			it.craftResult = max(1, it.craftResult + if (player.isSneaking) -1 else 1)
			if (!world.isRemote) ASJUtilities.say(player, "alfheimmisc.craftresult", it.craftResult)
		}
	}
	
	override fun onUsedByWand(player: EntityPlayer?, stack: ItemStack?, world: World, x: Int, y: Int, z: Int, side: Int): Boolean {
		return (world.getTileEntity(x, y, z) as? TileCorporeaAutocrafter)?.onWanded() == true
	}
	
	override fun getWeakChanges(world: IBlockAccess?, x: Int, y: Int, z: Int) = true
	
	override fun onNeighborChange(world: IBlockAccess, x: Int, y: Int, z: Int, tileX: Int, tileY: Int, tileZ: Int) {
		if (tileY - 1 == y)
			(world.getTileEntity(x, y, z) as? TileCorporeaAutocrafter)?.updateBufferSize()
	}
	
	override fun renderHUD(mc: Minecraft, res: ScaledResolution, world: World, x: Int, y: Int, z: Int) {
		val tile = world.getTileEntity(x, y, z) as? TileCorporeaAutocrafter ?: return
		
		val request = tile.request ?: return
		
		run {
			val size = when {
				tile.buffer.size < 2  -> tile.buffer.size
				tile.buffer.size < 5  -> 2
				tile.buffer.size < 10 -> 3
				else                  -> 9
			}
			
			val h = MathHelper.ceiling_float_int(tile.buffer.size / size.F)
			
			val width = size * 18 - 2
			val height = h * 18 - 2
			
			val xc = res.scaledWidth / 2 + 20
			val yc = res.scaledHeight / 2 - height / 2
			
			Gui.drawRect(xc - 6, yc - 6, xc + width + 6, yc + height + 6, 0x44000000)
			Gui.drawRect(xc - 4, yc - 4, xc + width + 4, yc + height + 4, 0x44000000)
			
			for (i in 0 until h)
				for (j in 0 until size) {
					val index = i * size + j
					
					if (index >= tile.buffer.size) return@run
					
					val xp = xc + j * 18
					val yp = yc + i * 18
					
					Gui.drawRect(xp, yp, xp + 16, yp + 16, 0x44FFFFFF)
					
					val item = tile.buffer[index] ?: continue
					
					RenderHelper.enableGUIStandardItemLighting()
					GL11.glEnable(GL12.GL_RESCALE_NORMAL)
					RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, item, xp, yp)
					RenderHelper.disableStandardItemLighting()
					
					val count = item.stackSize.toString()
					GL11.glTranslatef(0f, 0f, 100f)
					mc.fontRenderer.drawString(count, xp + 17 - mc.fontRenderer.getStringWidth(count), yp + 18 - mc.fontRenderer.FONT_HEIGHT, 0xFFFFFF)
					GL11.glTranslatef(0f, 0f, -100f)
				}
		}
		
		run {
			val count = tile.requestMissing.toString()
			
			val w = 16
			val h = 16
			
			val xc = res.scaledWidth / 2 - 20 - w
			var yc = res.scaledHeight / 2 - (h * 1.5).I
			
			val isStack = request is ItemStack
			
			var text = StatCollector.translateToLocal("alfheimmisc.autocraft.request")
			
			if (isStack)
				mc.fontRenderer.drawString(text, xc - 6 - mc.fontRenderer.getStringWidth(text) - 5, yc + 8 - 2 - mc.fontRenderer.FONT_HEIGHT / 2, 0xFFFFFF)
			else
				mc.fontRenderer.drawString(text, xc - 6 - mc.fontRenderer.getStringWidth(text) - 5, yc + 8 - 2 - mc.fontRenderer.FONT_HEIGHT, 0xFFFFFF)
			
			if (isStack) {
				yc -= 2
				
				Gui.drawRect(xc - 6, yc - 6, xc + w + 6, yc + h + 6, 0x44000000)
				Gui.drawRect(xc - 4, yc - 4, xc + w + 4, yc + h + 4, 0x44000000)
				Gui.drawRect(xc, yc, xc + 16, yc + 16, 0x44FFFFFF)
				
				RenderHelper.enableGUIStandardItemLighting()
				GL11.glEnable(GL12.GL_RESCALE_NORMAL)
				RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, request as ItemStack, xc, yc)
				RenderHelper.disableStandardItemLighting()
				
				GL11.glTranslatef(0f, 0f, 100f)
				mc.fontRenderer.drawString(count, xc + 17 - mc.fontRenderer.getStringWidth(count), yc + 18 - mc.fontRenderer.FONT_HEIGHT, 0xFFFFFF)
				GL11.glTranslatef(0f, 0f, -100f)
			} else {
				text = "$request"
				mc.fontRenderer.drawString(text, xc - 6 - mc.fontRenderer.getStringWidth(text) - 5, yc + 8 - 2, 0xFFFFFF)
				text = "x$count"
				mc.fontRenderer.drawString(text, xc + 9 - mc.fontRenderer.getStringWidth(text) / 2, yc + 8 - 2 - mc.fontRenderer.FONT_HEIGHT / 2, 0xFFFFFF)
			}
		}
		
		run {
			if (tile.waitingForIngredient && tile.awaitedIngredient != null) {
				val w = 16
				val h = 16
				
				val xc = res.scaledWidth / 2 - 20 - w
				var yc = res.scaledHeight / 2 + h / 2
				
				val text = StatCollector.translateToLocal("alfheimmisc.autocraft.waiting")
				mc.fontRenderer.drawString(text, xc - 6 - mc.fontRenderer.getStringWidth(text) - 5, yc + 8 + 2 - mc.fontRenderer.FONT_HEIGHT / 2, 0xFFFFFF)
				
				yc += 2
				Gui.drawRect(xc - 6, yc - 6, xc + w + 6, yc + h + 6, 0x44000000)
				Gui.drawRect(xc - 4, yc - 4, xc + w + 4, yc + h + 4, 0x44000000)
				Gui.drawRect(xc, yc, xc + 16, yc + 16, 0x44FFFFFF)
				
				RenderHelper.enableGUIStandardItemLighting()
				GL11.glEnable(GL12.GL_RESCALE_NORMAL)
				RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, tile.awaitedIngredient, xc, yc)
				RenderHelper.disableStandardItemLighting()
				
				val count = tile.awaitedIngredient?.stackSize.toString()
				GL11.glTranslatef(0f, 0f, 100f)
				mc.fontRenderer.drawString(count, xc + 17 - mc.fontRenderer.getStringWidth(count), yc + 18 - mc.fontRenderer.FONT_HEIGHT, 0xFFFFFF)
				GL11.glTranslatef(0f, 0f, -100f)
			}
		}
	}
	
	override fun getEntry(world: World?, x: Int, y: Int, z: Int, player: EntityPlayer?, lexicon: ItemStack?) = AlfheimLexiconData.corpSeq
}
