package alfheim.common.block.base

import alexsocol.asjlib.I
import alfheim.common.core.helper.*
import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.block.ItemBlockLeavesMod
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.MinecraftForge

@Suppress("LeakingThis")
open class BlockMod(material: Material): Block(material) {
	
	var originalLight: Int = 0
	
	init {
		setCreativeTab(AlfheimTab)
		if (FMLLaunchHandler.side().isClient && isInterpolated())
			MinecraftForge.EVENT_BUS.register(this)
	}
	
	override fun setBlockName(name: String): Block {
		if (shouldRegisterInNameSet())
			GameRegistry.registerBlock(this, ItemBlockLeavesMod::class.java, name)
		return super.setBlockName(name)
	}
	
	protected open fun shouldRegisterInNameSet() = true
	
	override fun setLightLevel(level: Float): Block {
		originalLight = (level * 15).I
		return super.setLightLevel(level)
	}
	
	open fun isInterpolated() = false
	
	@SideOnly(Side.CLIENT)
	override fun registerBlockIcons(reg: IIconRegister) {
		if (!isInterpolated())
			blockIcon = IconHelper.forBlock(reg, this)
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	open fun loadTextures(event: TextureStitchEvent.Pre) {
		if (event.map.textureType == 0 && isInterpolated())
			blockIcon = InterpolatedIconHelper.forBlock(event.map, this)
	}
}
