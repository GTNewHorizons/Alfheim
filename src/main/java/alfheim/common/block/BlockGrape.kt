package alfheim.common.block

import alfheim.common.core.helper.IconHelper
import alfheim.common.core.util.AlfheimTab
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.world.World
import java.util.*

class BlockGrape: BlockVine() {
	
	init {
		setBlockName("grape")
		setCreativeTab(AlfheimTab)
		setLightOpacity(0)
		setStepSound(soundTypeGrass)
	}
	
	override fun setBlockName(name: String): Block {
		GameRegistry.registerBlock(this, name)
		return super.setBlockName(name)
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerBlockIcons(reg: IIconRegister) {
		blockIcon = IconHelper.forBlock(reg, this)
	}
	
	override fun updateTick(world: World, x: Int, y: Int, z: Int, random: Random) {
		super.updateTick(world, x, y, z, random)
		
	}
}
