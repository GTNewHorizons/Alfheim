package alfheim.api.boss

import cpw.mods.fml.relauncher.*
import vazkii.botania.api.boss.IBotaniaBoss

interface IBotaniaBossWithName: IBotaniaBoss {
	
	@SideOnly(Side.CLIENT)
	fun getNameColor(): Int
}