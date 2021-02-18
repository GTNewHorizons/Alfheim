package alfheim.common.block.base

import alfheim.common.block.tile.TileManaFlame
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.common.Optional
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.world.*
import vazkii.botania.api.lexicon.*
import vazkii.botania.common.lexicon.LexiconData
import java.util.*

class BlockManaFlame(val name: String, val tile: Class<out TileManaFlame>): BlockMod(Material.cloth), ILexiconable {
	
	init {
		setBlockName(name)
		val f = 0.25f
		setBlockBounds(f, f, f, 1f - f, 1f - f, 1f - f)
		setCreativeTab(null)
		setLightLevel(1f)
		setStepSound(soundTypeCloth)
	}
	
	@Optional.Method(modid = "easycoloredlights")
	override fun getLightValue(world: IBlockAccess, x: Int, y: Int, z: Int) =
		tile.cast(world.getTileEntity(x, y, z)).getLightColor()
	
	override fun registerBlockIcons(reg: IIconRegister) = Unit
	
	override fun getRenderType(): Int = -1
	
	override fun getIcon(side: Int, meta: Int) = Blocks.fire.getIcon(side, meta)!!
	
	override fun isOpaqueCube() = false
	
	override fun renderAsNormalBlock() = false
	
	override fun hasTileEntity(metadata: Int) = true
	
	override fun getBlocksMovement(world: IBlockAccess?, x: Int, y: Int, z: Int) = true
	
	override fun getCollisionBoundingBoxFromPool(world: World?, x: Int, y: Int, z: Int) = null
	
	override fun getDrops(world: World, x: Int, y: Int, z: Int, metadata: Int, fortune: Int) = ArrayList<ItemStack>()
	
	override fun createTileEntity(world: World?, meta: Int) = tile.newInstance()
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?): LexiconEntry? {
		return when (name) {
			"invisibleFlame" -> LexiconData.lenses
			"rainbowFlame"   -> AlfheimLexiconData.rodPrismatic
			else             -> null
		}
	}
}