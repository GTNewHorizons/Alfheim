package alfheim.common.network

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.network.ASJPacket
import alfheim.AlfheimCore
import alfheim.api.AlfheimAPI
import alfheim.api.spell.SpellBase.SpellCastResult.DESYNC
import alfheim.client.core.handler.KeyBindingHandlerClient.KeyBindingIDs
import alfheim.common.core.handler.*
import alfheim.common.core.handler.CardinalSystem.forPlayer
import alfheim.common.network.Message2d.m2d
import cpw.mods.fml.common.network.simpleimpl.*
import io.netty.buffer.ByteBuf
import net.minecraft.entity.EntityLivingBase

class MessageKeyBind(var action: Int, var state: Boolean, var ticks: Int): ASJPacket() {
	
	override fun fromBytes(buf: ByteBuf) {
		if (!ASJUtilities.isServer) return
		action = buf.readInt()
		ticks = buf.readInt()
		state = buf.readBoolean()
	}
	
	class Handler: IMessageHandler<MessageKeyBind, IMessage> {
		
		override fun onMessage(packet: MessageKeyBind, message: MessageContext): IMessage? {
			val player = message.serverHandler.playerEntity
			
			when (KeyBindingIDs.values()[packet.action]) {
				KeyBindingIDs.ATTACK                      -> KeyBindingHandler.atack(player)
				
				KeyBindingIDs.CAST -> {
					val ids = if (packet.state) CardinalSystem.HotSpellsSystem.getHotSpellID(player, packet.ticks) else packet.ticks
					val seg = forPlayer(player)
					val spell = AlfheimAPI.getSpellByIDs(ids shr 28 and 0xF, ids and 0xFFFFFFF)
					if (spell == null)
						AlfheimCore.network.sendTo(Message2d(m2d.COOLDOWN, ids.toDouble(), (-DESYNC.ordinal).toDouble()), player)
					else {
						seg.ids = ids
						seg.init = spell.getCastTime()
						seg.castableSpell = spell
					}
				}
				
				KeyBindingIDs.UNCAST -> {
					run {
						val seg = forPlayer(player)
						seg.ids = 0
						seg.init = seg.ids
						seg.castableSpell = null
					}
					KeyBindingHandler.enableFlight(player, packet.state)
				}
				
				KeyBindingIDs.FLIGHT -> KeyBindingHandler.enableFlight(player, packet.state)
				
				KeyBindingIDs.SEL    -> {
					val e = player.worldObj.getEntityByID(packet.ticks)
					if (e is EntityLivingBase) CardinalSystem.TargetingSystem.setTarget(player, e, packet.state)
				}
			}
			return null
		}
	}
}