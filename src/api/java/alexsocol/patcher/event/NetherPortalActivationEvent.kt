package alexsocol.patcher.event

import cpw.mods.fml.common.eventhandler.*
import net.minecraft.world.World

@Cancelable class NetherPortalActivationEvent(val worldObj: World, val xCoord: Int, val yCoord: Int, val zCoord: Int): Event()