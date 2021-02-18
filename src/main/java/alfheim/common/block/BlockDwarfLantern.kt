package alfheim.common.block

import alexsocol.asjlib.ASJUtilities
import alfheim.client.core.helper.*
import alfheim.common.block.base.BlockMod
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.IIcon
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.MinecraftForge

class BlockDwarfLantern: BlockMod(Material.rock) {
	
	lateinit var iconSide: IIcon
	
	init {
		setBlockName("DwarfLantern")
		setHardness(10f)
		setHarvestLevel("pickaxe", 2)
		setLightLevel(1f)
		setResistance(10000f)
		setStepSound(soundTypeStone)
		
		if (ASJUtilities.isClient)
			MinecraftForge.EVENT_BUS.register(this)
	}
	
	override fun registerBlockIcons(reg: IIconRegister) {
		blockIcon = IconHelper.forBlock(reg, this, "Top", "decor")
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	override fun loadTextures(event: TextureStitchEvent.Pre) {
		if (event.map.textureType == 0)
			iconSide = InterpolatedIconHelper.forBlock(event.map, this, "", "decor")!!
	}
	
	override fun getIcon(side: Int, meta: Int) = (if (meta != 1) {
		if (side < 2) blockIcon else iconSide
	} else blockIcon)!!
}
