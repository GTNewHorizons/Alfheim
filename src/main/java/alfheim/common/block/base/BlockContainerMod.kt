package alfheim.common.block.base

import alexsocol.asjlib.*
import alfheim.client.core.helper.*
import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.block.ItemBlockLeavesMod
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
	
	init {
		setCreativeTab(AlfheimTab)
		if (ASJUtilities.isClient && isInterpolated())
			MinecraftForge.EVENT_BUS.register(this)
	}
	
	override fun setBlockName(name: String): Block {
		if (shouldRegisterInNameSet()) {
			GameRegistry.registerBlock(this, ItemBlockLeavesMod::class.java, name)
		}
		
		return super.setBlockName(name)
	}
	
	open fun shouldRegisterInNameSet() = true
	
	override fun setLightLevel(light: Float): Block {
		originalLight = (light * 15f).I
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
		if (event.map.textureType == 0)
			loadTextures(event.map)
	}
	
	@SideOnly(Side.CLIENT)
	open fun loadTextures(map: TextureMap) {
		if (isInterpolated())
			blockIcon = InterpolatedIconHelper.forBlock(map, this)
	}
}
