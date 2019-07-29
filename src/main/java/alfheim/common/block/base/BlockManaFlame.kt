package alfheim.common.block.base

import cpw.mods.fml.common.Optional
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.world.*
import alfheim.common.block.tile.TileManaFlame
import alfheim.common.lexicon.ShadowFoxLexiconData
import vazkii.botania.api.lexicon.*
import vazkii.botania.common.lexicon.LexiconData
import java.util.*

class BlockManaFlame(val name: String, val Tile: Class<out TileManaFlame>) : BlockMod(Material.cloth), ILexiconable {

    override val registerInCreative = false

    init {
        setBlockName(name)
        val f = 0.25f
        setStepSound(soundTypeCloth)
        setBlockBounds(f, f, f, 1.0f - f, 1.0f - f, 1.0f - f)
        setLightLevel(1.0f)
    }

    @Optional.Method(modid = "easycoloredlights")
    override fun getLightValue(world: IBlockAccess, x: Int, y: Int, z: Int) =
        Tile.cast(world.getTileEntity(x, y, z)).getLightColor()

    override fun registerBlockIcons(par1IconRegister: IIconRegister) = Unit

    override fun getRenderType(): Int = -1

    override fun getIcon(side: Int, meta: Int) = Blocks.fire.getIcon(side, meta)!!

    override fun isOpaqueCube() = false
    
    override fun renderAsNormalBlock() = false
    
    override fun hasTileEntity(metadata: Int) = true

    override fun getBlocksMovement(world: IBlockAccess?, x: Int, y: Int, z: Int) = true
    
    override fun getCollisionBoundingBoxFromPool(world: World?, x: Int, y: Int, z: Int) = null

    override fun getDrops(world: World, x: Int, y: Int, z: Int, metadata: Int, fortune: Int) = ArrayList<ItemStack>()

    override fun createTileEntity(world: World?, meta: Int) = Tile.newInstance()!!

    override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?): LexiconEntry? {
        return when (name) {
            "invisibleFlame" -> LexiconData.lenses
            "rainbowFlame" -> ShadowFoxLexiconData.waveRod
            else -> null
        }
    }
}