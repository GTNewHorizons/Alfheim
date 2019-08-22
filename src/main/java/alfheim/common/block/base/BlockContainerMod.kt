package alfheim.common.block.base

import alfheim.AlfheimCore
import alfheim.common.core.helper.*
import alfheim.common.item.block.ItemBlockMod
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.*
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.MinecraftForge

@Suppress("LeakingThis")
abstract class BlockContainerMod(material: Material): BlockContainer(material) {
	
	var originalLight: Int = 0
	open val registerInCreative: Boolean = true
	
	init {
		if (registerInCreative) {
			setCreativeTab(AlfheimCore.baTab)
		}
		if (FMLLaunchHandler.side().isClient && isInterpolated())
			MinecraftForge.EVENT_BUS.register(this)
	}
	
	override fun setBlockName(name: String): Block {
		if (shouldRegisterInNameSet()) {
			GameRegistry.registerBlock(this, ItemBlockMod::class.java, name)
		}
		
		return super.setBlockName(name)
	}
	
	open fun shouldRegisterInNameSet() = true
	
	override fun setLightLevel(light: Float): Block {
		originalLight = (light * 15.0f).toInt()
		return super.setLightLevel(light)
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerBlockIcons(reg: IIconRegister) {
		if (!isInterpolated())
			blockIcon = IconHelper.forBlock(reg, this)
	}
	
	open fun isInterpolated(): Boolean = false
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun loadTextures(event: TextureStitchEvent.Pre) {
		if (event.map.textureType == 0 && isInterpolated())
			loadTextures(event.map)
	}
	
	@SideOnly(Side.CLIENT)
	open fun loadTextures(map: TextureMap) {
		blockIcon = InterpolatedIconHelper.forBlock(map, this)
	}
}
