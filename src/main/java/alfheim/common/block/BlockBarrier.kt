package alfheim.common.block

import alfheim.common.block.base.BlockMod
import cpw.mods.fml.relauncher.*
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.init.Blocks
import net.minecraft.world.World

class BlockBarrier: BlockMod(Material.cake) {
	
	init {
		setBlockUnbreakable()
		setResistance(6000001f)
		setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F)
		disableStats()
		setLightOpacity(0)
		setBlockName("barrier")
	}
	
	override fun getIcon(side: Int, meta: Int) = Blocks.bedrock.getIcon(side, meta)!!
	
	override fun getRenderType(): Int = -1
	
	override fun isOpaqueCube(): Boolean = false
	
	override fun registerBlockIcons(par1IconRegister: IIconRegister) = Unit
	
	@SideOnly(Side.CLIENT)
	override fun getAmbientOcclusionLightValue(): Float = 1f
	
	override fun dropBlockAsItemWithChance(worldIn: World, x: Int, y: Int, z: Int, meta: Int, chance: Float, fortune: Int) = Unit
}
