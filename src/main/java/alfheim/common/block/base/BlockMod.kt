package alfheim.common.block.base

import alfheim.AlfheimCore
import alfheim.common.core.helper.*
import alfheim.common.item.block.ItemBlockMod
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.MinecraftForge

open class BlockMod(par2Material: Material): Block(par2Material) {
	
	var originalLight: Int = 0
	
	open val registerInCreative: Boolean = true
	
	init {
		if (registerInCreative) {
			setCreativeTab(AlfheimCore.baTab)
		}
		if (FMLLaunchHandler.side().isClient && isInterpolated())
			MinecraftForge.EVENT_BUS.register(this)
	}
	
	override fun setBlockName(par1Str: String): Block {
		if (shouldRegisterInNameSet())
			GameRegistry.registerBlock(this, ItemBlockMod::class.java, par1Str)
		return super.setBlockName(par1Str)
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
