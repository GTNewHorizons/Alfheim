package alfheim.common.block

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import net.minecraft.block.BlockRotatedPillar
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.IIcon

class BlockShrinePillar: BlockRotatedPillar(Material.rock) {
	
	lateinit var iconTop: IIcon
	lateinit var iconSide: IIcon
	
	init {
		setBlockName("ShrinePillar")
		setCreativeTab(AlfheimCore.alfheimTab)
		setHardness(10f)
		setHarvestLevel("pickaxe", 2)
		setResistance(10000f)
		setStepSound(soundTypeStone)
		
		ASJUtilities.register(this)
	}
	
	override fun registerBlockIcons(ireg: IIconRegister) {
		iconTop = ireg.registerIcon(ModInfo.MODID + ":shrines/ShrinePillarTop")
		iconSide = ireg.registerIcon(ModInfo.MODID + ":shrines/ShrinePillar")
	}
	
	override fun getTopIcon(side: Int) = iconTop
	override fun getSideIcon(side: Int) = iconSide
}
