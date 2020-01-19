package vazkii.botania.api.boss

import cpw.mods.fml.relauncher.*

interface IBotaniaBossWithName: IBotaniaBoss {
	
	@SideOnly(Side.CLIENT)
	fun getNameColor(): Int
}

interface IBotaniaBossWithShaderAndName: IBotaniaBossWithShader {
	
	@SideOnly(Side.CLIENT)
	fun getNameColor(): Int
}