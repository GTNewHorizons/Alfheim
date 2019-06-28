package alfheim.common.network

import alexsocol.asjlib.network.ASJPacket
import alfheim.client.core.handler.PacketHandlerClient
import cpw.mods.fml.common.network.simpleimpl.*

class MessageParticles(val i: Int, val x: Double, val y: Double, val z: Double): ASJPacket() {
	var x2: Double = 0.toDouble()
	var y2: Double = 0.toDouble()
	var z2: Double = 0.toDouble()
	
	constructor(i: Int, x: Double, y: Double, z: Double, x2: Double, y2: Double, z2: Double): this(i, x, y, z) {
		this.x2 = x2
		this.y2 = y2
		this.z2 = z2
	}
	
	class Handler: IMessageHandler<MessageParticles, IMessage> {
		
		override fun onMessage(packet: MessageParticles, message: MessageContext): IMessage? {
			PacketHandlerClient.handle(packet)
			return null
		}
	}
}