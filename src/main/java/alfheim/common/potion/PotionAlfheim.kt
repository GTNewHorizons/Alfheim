package alfheim.common.potion

import alfheim.api.lib.LibResourceLocations
import cpw.mods.fml.relauncher.*
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.GL11.*
import vazkii.botania.common.brew.potion.PotionMod

open class PotionAlfheim(id: Int, name: String, badEffect: Boolean, color: Int): PotionMod(id, name, badEffect, color, iconID++) {
	
	init {
		setPotionName("alfheim.potion.$name")
	}
	
	@SideOnly(Side.CLIENT)
	override fun getStatusIconIndex(): Int {
		glEnable(GL_BLEND)
		val id = super.getStatusIconIndex()
		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.potions)
		return id
	}
	
	companion object {
		
		private var iconID = 0
	}
}