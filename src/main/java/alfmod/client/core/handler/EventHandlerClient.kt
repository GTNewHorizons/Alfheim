package alfmod.client.core.handler

import cpw.mods.fml.common.FMLCommonHandler
import net.minecraftforge.common.MinecraftForge

object EventHandlerClient {
	
	init {
		FMLCommonHandler.instance().bus().register(this)
		MinecraftForge.EVENT_BUS.register(this)
	}
}