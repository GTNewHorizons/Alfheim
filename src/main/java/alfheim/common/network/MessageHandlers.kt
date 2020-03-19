package alfheim.common.network

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.AlfheimAPI
import alfheim.client.core.handler.KeyBindingHandlerClient.KeyBindingIDs.*
import alfheim.client.core.handler.PacketHandlerClient
import alfheim.client.core.util.mc
import alfheim.common.block.tile.TileRaceSelector
import alfheim.common.core.handler.*
import alfheim.common.core.handler.CardinalSystem.HotSpellsSystem
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.handler.CardinalSystem.TargetingSystem
import alfheim.common.core.helper.*
import alfheim.common.core.util.*
import alfheim.common.entity.EntityLolicorn
import alfheim.common.item.AlfheimItems
import cpw.mods.fml.common.network.simpleimpl.*
import net.minecraft.command.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.potion.*
import net.minecraft.server.MinecraftServer
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.Vector3
import java.io.File

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

class MessageNIHandler:  IMessageHandler<MessageNI, IMessage?> {
	
	override fun onMessage(message: MessageNI, ctx: MessageContext): IMessage? {
		PacketHandlerClient.handle(message)
		return null
	}
}

// ----

class MessageContributorHandler: IMessageHandler<MessageContributor, IMessage?> {
	
	override fun onMessage(message: MessageContributor, ctx: MessageContext): IMessage? {
		println("Message!")
		
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
				
				return MessageContributor(login, password)
			} else {
				// new alias registration
				ContributorsPrivacyHelper.contributors[message.key] = message.value
			}
		} else {
			// we are on server
			
			val player = ctx.serverHandler.playerEntity
			val username = player.commandSenderName
			
			val passMatch = HashHelper.hash(message.value) == ContributorsPrivacyHelper.getPassHash(message.key)
			
			// are you the person you are saying you are ?
			if (ContributorsPrivacyHelper.isRegistered(username)) {
				if (message.key != username) {
					player.playerNetServerHandler.kickPlayerFromServer("Invalid login provided, it must be equal to your username")
					return null
				}
				
				// yes, you are. Welcome!
				if (passMatch) {
					// TODO proceed
				} else {
					// no, you not. Get out!
					player.playerNetServerHandler.kickPlayerFromServer("Incorrect credentials for your contributor username")
					return null
				}
			} else {
				// so you are noname...
				
				// do you want to stay nobody ?
				if (message.key == "login" && message.value == "password")
					return null
				
				// ok, your new identity will be set
				if (passMatch) {
					// TODO proceed
				} else {
					// incorrect password for identity
					player.playerNetServerHandler.kickPlayerFromServer("Incorrect contributor credentials")
					return null
				}
			}
			
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
				1	/* add	*/	-> {
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
				
				0	/* upd	*/	-> {
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
				
				-1	/* rem	*/	-> {
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
		Botania.proxy.lightningFX(mc.theWorld, Vector3(message.x1, message.y1, message.z1), Vector3(message.x2, message.y2, message.z2), message.ticksPerMeter, message.colorOuter, message.colorInner)
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
			ATTACK  -> KeyBindingHandler.atack(player)
			CORN    -> EntityLolicorn.call(player)
			FLIGHT  -> KeyBindingHandler.enableFlight(player, message.state)
			ESMABIL -> CardinalSystem.forPlayer(player).toggleESMAbility()
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

class MessagePlayerItemHandler: IMessageHandler<MessagePlayerItemS, IMessage?> {
	
	override fun onMessage(message: MessagePlayerItemS, ctx: MessageContext): IMessage? {
		if (message.item != null && ctx.side.isServer) {
			val player = ctx.serverHandler.playerEntity
			
			val heldItem = player.currentEquippedItem
			
			if (heldItem == null) {
				player.setCurrentItemOrArmor(0, message.item!!.copy())
			} else if (!player.inventory.addItemStackToInventory(message.item!!.copy())) {
				player.dropPlayerItemWithRandomChoice(message.item!!.copy(), false)
			}
		}
		
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