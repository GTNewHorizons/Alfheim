package alfheim.common.block.base

import alfheim.common.core.helper.*
import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.block.ItemBlockMod
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.MinecraftForge

@Suppress("LeakingThis")
open class BlockMod(par2Material: Material): Block(par2Material) {
	
	var originalLight: Int = 0
	
	init {
		setCreativeTab(AlfheimTab)
		if (FMLLaunchHandler.side().isClient && isInterpolated())
			MinecraftForge.EVENT_BUS.register(this)
	}
	
	override fun setBlockName(name: String): Block {
		if (shouldRegisterInNameSet())
			GameRegistry.registerBlock(this, ItemBlockMod::class.java, name)
		return super.setBlockName(name)
	}
	
	protected open fun shouldRegisterInNameSet() = true
	
	override fun setLightLevel(level: Float): Block {
		originalLight = (level * 15).toInt()
		return super.setLightLevel(level)
	}
	
	open fun isInterpolated() = false
	
	@SideOnly(Side.CLIENT)
	override fun registerBlockIcons(par1IconRegister: IIconRegister) {
		if (!isInterpolated())
			blockIcon = IconHelper.forBlock(par1IconRegister, this)
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	open fun loadTextures(event: TextureStitchEvent.Pre) {
		if (event.map.textureType == 0 && isInterpolated())
			blockIcon = InterpolatedIconHelper.forBlock(event.map, this)
	}
}
