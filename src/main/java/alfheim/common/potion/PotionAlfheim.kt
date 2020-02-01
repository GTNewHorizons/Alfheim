package alfheim.common.potion

import alfheim.api.lib.LibResourceLocations
import alfheim.client.core.util.mc
import cpw.mods.fml.relauncher.*
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
		mc.renderEngine.bindTexture(LibResourceLocations.potions)
		return id
	}
	
	companion object {
		
		private var iconID = 0
	}
}