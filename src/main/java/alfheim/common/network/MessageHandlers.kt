package alfheim.common.network

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.AlfheimAPI
import alfheim.client.core.handler.KeyBindingHandlerClient.KeyBindingIDs.*
import alfheim.client.core.handler.PacketHandlerClient
import alfheim.common.block.tile.TileRaceSelector
import alfheim.common.core.handler.*
import alfheim.common.core.handler.CardinalSystem.HotSpellsSystem
import alfheim.common.core.helper.*
import alfheim.common.entity.EntityLolicorn
import alfheim.common.item.equipment.bauble.ItemPriestCloak
import cpw.mods.fml.common.network.simpleimpl.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.potion.*
import net.minecraft.server.MinecraftServer
import vazkii.botania.common.Botania
import java.io.File
import vazkii.botania.common.core.helper.Vector3 as BVector3

class Message0dSHandler: IMessageHandler<Message0dS, IMessage?> {
	
	override fun onMessage(message: Message0dS, ctx: MessageContext): IMessage? {
		PacketHandler.handle(message, ctx)
		return null
	}
}

class Message0dCHandler: IMessageHandler<Message0dC, IMessage?> {
	
	override fun onMessage(message: Message0dC, ctx: MessageContext): IMessage? {
		PacketHandlerClient.handle(message)
		return null
	}
}

class Message1dHandler: IMessageHandler<Message1d, IMessage?> {
	
	override fun onMessage(message: Message1d, ctx: MessageContext): IMessage? {
		PacketHandlerClient.handle(message)
		return null
	}
}

class Message1lHandler: IMessageHandler<Message1l, IMessage?> {
	
	override fun onMessage(message: Message1l, ctx: MessageContext): IMessage? {
		PacketHandlerClient.handle(message)
		return null
	}
}

class Message2dHandler: IMessageHandler<Message2d, IMessage?> {
	
	override fun onMessage(message: Message2d, ctx: MessageContext): IMessage? {
		PacketHandlerClient.handle(message)
		return null
	}
}

class Message3dHandler: IMessageHandler<Message3d, IMessage?> {
	
	override fun onMessage(message: Message3d, ctx: MessageContext): IMessage? {
		PacketHandlerClient.handle(message)
		return null
	}
}

class MessageNIHandler: IMessageHandler<MessageNI, IMessage?> {
	
	override fun onMessage(message: MessageNI, ctx: MessageContext): IMessage? {
		PacketHandlerClient.handle(message)
		return null
	}
}

// ----

class MessageContributorHandler: IMessageHandler<MessageContributor, IMessage?> {
	
	override fun onMessage(message: MessageContributor, ctx: MessageContext): IMessage? {
		if (ctx.side.isClient) {
			// we are on client
			
			// this is li:pw request
			if (message.isRequest) {
				val info = File("contributor.info")
				var login = "login"
				var password = "password"
				
				if (info.exists()) {
					val creds = info.readLines()
					login = creds.getOrElse(0) { login }
					password = creds.getOrElse(1) { password }
				}
				
				return MessageContributor(login, HashHelper.hash(password))
			} else {
				// new alias registration
				ContributorsPrivacyHelper.contributors[message.key] = message.value
			}
		} else {
			// we are on server
			
			val player = ctx.serverHandler.playerEntity
			val username = player.commandSenderName
			
			// auth packet received - no hacking (probably)
			ContributorsPrivacyHelper.authTimeout.remove(player.commandSenderName)
			
			val passMatch = ContributorsPrivacyHelper.getPassHash(message.key)?.let { if (it.isBlank()) true else it == HashHelper.hash(message.value) } ?: false
			
			// are you the person you are saying you are ?
			if (ContributorsPrivacyHelper.isRegistered(username)) {
				if (message.key != username) {
					player.playerNetServerHandler.kickPlayerFromServer("Invalid login provided, it must be equal to your username")
					return null
				}
				
				// yes, you are. Welcome!
				if (passMatch) {
					// --> proceed
				} else {
					// no, you not. Get out!
					player.playerNetServerHandler.kickPlayerFromServer("Incorrect credentials for your contributor username")
					return null
				}
			} else {
				// so you are noname...
				
				// do you want to stay nobody ?
				if (message.key == "login" && message.value == "CCF9AF1939482C852F74C84A7CEDFE4EDC65B2FDCC4C11F7AD9F393D91948BFC")
					return null
				
				// ok, your new identity will be set
				if (passMatch) {
					// --> proceed
				} else {
					// incorrect password for identity
					player.playerNetServerHandler.kickPlayerFromServer("Incorrect contributor credentials")
					return null
				}
			}
			
			// --> proceeding here:
			
			// set contributor name alias to current username
			ContributorsPrivacyHelper.contributors[message.key] = username
			
			// tell everyone about new alias
			MinecraftServer.getServer()?.configurationManager?.playerEntityList?.forEach {
				if (it is EntityPlayerMP)
					AlfheimCore.network.sendTo(MessageContributor(message.key, username), it)
			}
			
			// send all aliases to new player
			ContributorsPrivacyHelper.contributors.forEach { (k, v) -> AlfheimCore.network.sendTo(MessageContributor(k, v), player) }
		}
		
		return null
	}
}

class MessageEffectHandler: IMessageHandler<MessageEffect, IMessage?> {
	
	override fun onMessage(message: MessageEffect, ctx: MessageContext): IMessage? {
		val e = ctx.clientHandler.clientWorldController.getEntityByID(message.entity)
		if (e is EntityLivingBase) {
			val pe = e.getActivePotionEffect(message.id)
			when (message.state) {
				1    /* add	*/  -> {
					if (pe == null) {
						e.addPotionEffect(PotionEffect(message.id, message.dur, message.amp, true))
						Potion.potionTypes[message.id].applyAttributesModifiersToEntity(e, e.getAttributeMap(), message.amp)
					} else {
						if (message.readd) Potion.potionTypes[message.id].removeAttributesModifiersFromEntity(e, e.getAttributeMap(), message.amp)
						pe.amplifier = message.amp
						pe.duration = message.dur
						pe.isAmbient = true
						if (message.readd) Potion.potionTypes[message.id].applyAttributesModifiersToEntity(e, e.getAttributeMap(), message.amp)
					}
				}
				
				0    /* upd	*/  -> {
					if (pe == null) {
						e.addPotionEffect(PotionEffect(message.id, message.dur, message.amp, true))
						Potion.potionTypes[message.id].applyAttributesModifiersToEntity(e, e.getAttributeMap(), message.amp)
					} else {
						if (message.readd) Potion.potionTypes[message.id].removeAttributesModifiersFromEntity(e, e.getAttributeMap(), message.amp)
						pe.amplifier = message.amp
						pe.duration = message.dur
						pe.isAmbient = true
						if (message.readd) Potion.potionTypes[message.id].applyAttributesModifiersToEntity(e, e.getAttributeMap(), message.amp)
					}
				}
				
				-1    /* rem	*/ -> {
					if (pe != null) {
						e.removePotionEffect(message.id)
						Potion.potionTypes[message.id].removeAttributesModifiersFromEntity(e, e.getAttributeMap(), message.amp)
					}
				}
			}
		}
		return null
	}
}

class MessageEffectLightningHandler: IMessageHandler<MessageEffectLightning, IMessage?> {
	
	override fun onMessage(message: MessageEffectLightning, ctx: MessageContext): IMessage? {
		Botania.proxy.lightningFX(mc.theWorld, BVector3(message.x1, message.y1, message.z1), BVector3(message.x2, message.y2, message.z2), message.ticksPerMeter, message.colorOuter, message.colorInner)
		return null
	}
}

class MessageHeimdallBlinkHandler: IMessageHandler<MessageHeimdallBlink, IMessage?> {
	
	override fun onMessage(message: MessageHeimdallBlink?, ctx: MessageContext): IMessage? {
		val player = ctx.serverHandler.playerEntity
		if (ItemPriestCloak.getCloak(4, player) != null) {
			val look = player.lookVec
			val dist = 6.0
			val (x, y, z) = Vector3.fromEntity(player).add(Vector3(look).mul(dist))
			
			if (!player.worldObj.getBlock(x.I, y.I, z.I).isNormalCube && !player.worldObj.getBlock(x.I, y.I + 1, z.I).isNormalCube) {
				ctx.serverHandler.setPlayerLocation(x, y, z, player.rotationYaw, player.rotationPitch)
				// ctx.serverHandler.func_184342_d() captureCurrentPosition ???
				player.worldObj.playSoundEffect(x, y, z, "mob.endermen.portal", 1f, 1f)
				player.playSound("mob.endermen.portal", 1f, 1f)
			}
		}
		
		return null
	}
}

class MessageHotSpellCHandler: IMessageHandler<MessageHotSpellC, IMessage?> {
	
	override fun onMessage(message: MessageHotSpellC, ctx: MessageContext): IMessage? {
		PacketHandlerClient.handle(message)
		return null
	}
}

class MessageHotSpellSHandler: IMessageHandler<MessageHotSpellS, IMessage?> {
	
	override fun onMessage(message: MessageHotSpellS, ctx: MessageContext): IMessage? {
		HotSpellsSystem.setHotSpellID(ctx.serverHandler.playerEntity, message.slot, message.id)
		return null
	}
}

class MessageKeyBindHandler: IMessageHandler<MessageKeyBindS, IMessage?> {
	
	override fun onMessage(message: MessageKeyBindS, ctx: MessageContext): IMessage? {
		val player = ctx.serverHandler.playerEntity
		
		when (values()[message.action]) {
			HIT     -> KeyBindingHandler.hit(player)
			USE     -> KeyBindingHandler.use(player)
			CORN    -> EntityLolicorn.call(player)
			FLIGHT  -> KeyBindingHandler.enableFlight(player, message.state)
			ESMABIL -> KeyBindingHandler.toggleESMAbility(player)
			CAST    -> KeyBindingHandler.cast(player, message.state, message.data)
			UNCAST  -> KeyBindingHandler.unCast(player)
			SEL     -> KeyBindingHandler.select(player, message.state, message.data)
			SECRET  -> KeyBindingHandler.secret(player)
		}
		return null
	}
}

class MessagePartyHandler: IMessageHandler<MessageParty, IMessage?> {
	
	override fun onMessage(message: MessageParty, ctx: MessageContext): IMessage? {
		PacketHandlerClient.handle(message)
		return null
	}
}

class MessageSkinInfoHandler: IMessageHandler<MessageSkinInfo, IMessage?> {
	
	override fun onMessage(message: MessageSkinInfo, ctx: MessageContext): IMessage? {
		PacketHandlerClient.handle(message)
		
		return null
	}
}

class MessageSpellParamsHandler: IMessageHandler<MessageSpellParams, IMessage?> {
	
	override fun onMessage(message: MessageSpellParams, ctx: MessageContext): IMessage? {
		val spell = AlfheimAPI.getSpellInstance(message.name) ?: return null
		spell.damage = message.damage
		spell.duration = message.duration
		spell.efficiency = message.efficiency
		spell.radius = message.radius
		
		return null
	}
}

class MessageRaceSelectionHandler: IMessageHandler<MessageRaceSelection, IMessage?> {
	
	override fun onMessage(message: MessageRaceSelection, ctx: MessageContext): IMessage? {
		if (Vector3.vecEntityDistance(Vector3(message.x, message.y, message.z), ctx.serverHandler.playerEntity) > 5) return null
		
		val world = MinecraftServer.getServer().worldServerForDimension(message.dim) ?: return null
		val tile = world.getTileEntity(message.x, message.y, message.z) as? TileRaceSelector ?: return null
		
		if (message.doMeta) world.setBlockMetadataWithNotify(message.x, message.y, message.z, message.meta, 3)
		
		tile.activeRotation = message.arot
		tile.rotation = message.rot
		tile.custom = message.custom
		tile.female = message.female
		tile.timer = message.timer
		
		if (message.give) tile.giveRaceAndReset(ctx.serverHandler.playerEntity)
		
		ASJUtilities.dispatchTEToNearbyPlayers(tile)
		
		return null
	}
}

class MessageTileItemHandler: IMessageHandler<MessageTileItem, IMessage?> {
	
	override fun onMessage(message: MessageTileItem, ctx: MessageContext): IMessage? {
		PacketHandlerClient.handle(message)
		return null
	}
}

class MessageTimeStopHandler: IMessageHandler<MessageTimeStop, IMessage?> {
	
	override fun onMessage(message: MessageTimeStop, ctx: MessageContext): IMessage? {
		PacketHandlerClient.handle(message)
		return null
	}
}

class MessageVisualEffectHandler: IMessageHandler<MessageVisualEffect, IMessage?> {
	
	override fun onMessage(message: MessageVisualEffect, ctx: MessageContext): IMessage? {
		PacketHandlerClient.handle(message)
		return null
	}
}