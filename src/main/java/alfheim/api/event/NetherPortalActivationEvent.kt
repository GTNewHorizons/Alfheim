package alfheim.api.event

import cpw.mods.fml.common.eventhandler.Event
import net.minecraft.world.World

class NetherPortalActivationEvent(val worldObj: World, val xCoord: Int, val yCoord: Int, val zCoord: Int): Event() {
	override fun isCancelable() = true
}