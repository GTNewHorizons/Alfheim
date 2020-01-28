package alfheim.api.boss

import cpw.mods.fml.relauncher.*
import vazkii.botania.api.boss.*

interface IBotaniaBossWithName: IBotaniaBoss {
	
	@SideOnly(Side.CLIENT)
	fun getNameColor(): Int
}

interface IBotaniaBossWithShaderAndName: IBotaniaBossWithShader {
	
	@SideOnly(Side.CLIENT)
	fun getNameColor(): Int
}