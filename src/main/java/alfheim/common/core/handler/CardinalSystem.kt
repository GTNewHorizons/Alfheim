package alfheim.common.core.handler

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.*
import alfheim.api.entity.*
import alfheim.api.event.*
import alfheim.api.event.TimeStopCheckEvent.TimeStopEntityCheckEvent
import alfheim.api.spell.*
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party
import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.network.*
import alfheim.common.network.Message2d.m2d.COOLDOWN
import alfheim.common.network.Message3d.m3d.PARTY_STATUS
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.PlayerEvent.*
import cpw.mods.fml.common.network.ByteBufUtils
import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.entity.*
import net.minecraft.entity.boss.IBossDisplayData
import net.minecraft.entity.player.*
import net.minecraft.potion.*
import net.minecraft.server.MinecraftServer
import net.minecraft.util.*
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.EntityStruckByLightningEvent
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.mana.*
import java.io.*
import java.util.*

@Suppress("UNCHECKED_CAST")
object CardinalSystem {
	
	var playerSegments = HashMap<String, PlayerSegment>()
	
	fun load(save: String) {
		val file = File("$save/data/Cardinal.sys")
		if (!file.exists()) {
			ASJUtilities.log("Cardinal System data file not found. Generating default values...")
			playerSegments = HashMap()
			TimeStopSystem.tsAreas = HashMap()
			return
		}
		
		try {
			val fis = FileInputStream(file)
			val oin = ObjectInputStream(fis)
			playerSegments = oin.readObject() as HashMap<String, PlayerSegment>
			TimeStopSystem.tsAreas = oin.readObject() as HashMap<Int, LinkedList<TimeStopSystem.TimeStopArea>>
			oin.close()
		} catch (e: Throwable) {
			ASJUtilities.error("Unable to read whole Cardinal System data. Generating default values...")
			e.printStackTrace()
			playerSegments = HashMap()
			TimeStopSystem.tsAreas = HashMap()
		}
	}
	
	fun transfer(player: EntityPlayerMP) {
		KnowledgeSystem.transfer(player)
		
		if (AlfheimCore.enableMMO) {
			SpellCastingSystem.transfer(player)
			HotSpellsSystem.transfer(player)
			PartySystem.transfer(player)
			TimeStopSystem.transfer(player, 0)
		}
	}
	
	fun save(save: String) {
		try {
			val fos = FileOutputStream("$save/data/Cardinal.sys")
			val oos = ObjectOutputStream(fos)
			oos.writeObject(playerSegments)
			oos.writeObject(TimeStopSystem.tsAreas)
			oos.flush()
			oos.close()
		} catch (e: Throwable) {
			ASJUtilities.error("Unable to save whole Cardinal System data. Discarding. Sorry :(")
			e.printStackTrace()
		}
	}
	
	fun ensureExistance(player: EntityPlayer): Boolean {
		if (!playerSegments.containsKey(player.commandSenderName)) {
			playerSegments[player.commandSenderName] = PlayerSegment(player)
		}
		return true
	}
	
	fun forPlayer(player: EntityPlayer): PlayerSegment {
		if (!ASJUtilities.isServer) throw RuntimeException("You shouldn't access this from client")
		ensureExistance(player)
		return playerSegments[player.commandSenderName]!!
	}
	
	object KnowledgeSystem {
		
		fun learn(player: EntityPlayerMP, kn: Knowledge) {
			val seg = forPlayer(player)
			if (!know(player, kn)) {
				seg.knowledge.add("$kn")
				AlfheimCore.network.sendTo(Message1d(Message1d.m1d.KNOWLEDGE, kn.ordinal.toDouble()), player)
			}
		}
		
		fun know(player: EntityPlayerMP, kn: Knowledge) = forPlayer(player).knowledge.contains("$kn")
		
		fun transfer(player: EntityPlayerMP) {
			for (kn in Knowledge.values()) if (know(player, kn)) AlfheimCore.network.sendTo(Message1d(Message1d.m1d.KNOWLEDGE, kn.ordinal.toDouble()), player)
		}
		
		enum class Knowledge {
			GLOWSTONE, PYLONS
		}
	}
	
	object SpellCastingSystem {
		
		fun transfer(player: EntityPlayerMP) {
			for (affinity in EnumRace.values())
				for (spell in AlfheimAPI.getSpellsFor(affinity))
					AlfheimCore.network.sendTo(Message2d(COOLDOWN, (affinity.ordinal and 0xF shl 28 or (AlfheimAPI.getSpellID(spell) and 0xFFFFFFF)).toDouble(), getCoolDown(player, spell).toDouble()), player)
		}
		
		fun setCoolDown(caster: EntityPlayer, spell: SpellBase, cd: Int): Int {
			forPlayer(caster).coolDown[spell] = cd
			return cd
		}
		
		fun getCoolDown(caster: EntityPlayer, spell: SpellBase): Int {
			return try {
				forPlayer(caster).coolDown[spell] ?: 0
			} catch (e: Throwable) {
				ASJUtilities.error(String.format("Something went wrong getting cooldown for %s. Returning 0.", spell))
				e.printStackTrace()
				0
			}
		}
		
		fun tick() {
			try {
				playerSegments.forEach { (name, segment) ->
					for (spell in segment.coolDown.keys) {
						var time = segment.coolDown[spell] ?: 0
						if (time > 5 && MinecraftServer.getServer().configurationManager.func_152612_a(name)?.capabilities?.isCreativeMode == true) time = 5
						if (time > 0) segment.coolDown[spell] = time - 1
					}
					
					val player = MinecraftServer.getServer().configurationManager.func_152612_a(segment.userName)
					if (player != null) {
						if (segment.init > 0)
							--segment.init
						else {
							if (segment.ids != 0 && segment.castableSpell != null) {
								AlfheimCore.network.sendTo(Message2d(COOLDOWN, segment.ids.toDouble(), KeyBindingHandler.cast(player, segment.ids shr 28 and 0xF, segment.ids and 0xFFFFFFF).toDouble()), player)
								segment.ids = 0
								segment.init = segment.ids
								segment.castableSpell = null
							}
						}
					} else {
						segment.ids = 0
						segment.init = segment.ids
						segment.castableSpell = null
					}
				}
			} catch (e: Throwable) {
				ASJUtilities.error("Something went wrong ticking spells. Skipping this tick.")
				e.printStackTrace()
			}
			
		}
		
		fun reset() {
			for (segment in playerSegments.values) {
				for (spell in segment.coolDown.keys) {
					segment.coolDown[spell] = 0
				}
			}
		}
		
		init {
			MinecraftForge.EVENT_BUS.register(SpellCastingHandler())
			MinecraftForge.EVENT_BUS.register(QuadDamageHandler())
		}
		
		class SpellCastingHandler {
			
			@SubscribeEvent
			fun onSpellCasting(e: SpellCastEvent.Pre) {
				if (!e.caster.isEntityAlive || e.caster.isDead) {
					e.isCanceled = true
					return
				}
				
				if (e.caster.isPotionActive(AlfheimConfigHandler.potionIDLeftFlame)) {
					e.isCanceled = true
					return
				}
				
				if (TimeStopSystem.affected(e.caster)) {
					e.isCanceled = true
				}
			}
			
			@SubscribeEvent
			fun onSpellCasted(e: SpellCastEvent.Post) {
				if (ModInfo.DEV || e.caster is EntityPlayer && e.caster.capabilities.isCreativeMode) e.cd = 5
			}
		}
		
		class QuadDamageHandler {
			
			@SubscribeEvent
			fun handleQuadDamageSequence(e: SpellCastEvent.Post) {
				if (e.caster !is EntityPlayer) return
				val player = e.caster
				val seg = forPlayer(player)
				
				when (seg.quadStage) {
					0    -> {
						if (e.spell.name == "stoneskin") {
							++seg.quadStage
						}
						if (e.spell.name == "uphealth" && player.isPotionActive(AlfheimRegistry.stoneSkin)) {
							++seg.quadStage
						} else {
							seg.quadStage = 0
						}
					}
					
					1    -> if (e.spell.name == "uphealth" && player.isPotionActive(AlfheimRegistry.stoneSkin)) {
						++seg.quadStage
					} else {
						seg.quadStage = 0
					}
					
					2    -> if (e.spell.name == "icelens" && player.isPotionActive(AlfheimRegistry.stoneSkin) && player.isPotionActive(Potion.field_76434_w) && player.getActivePotionEffect(Potion.field_76434_w).amplifier == 1) {
						++seg.quadStage
					} else {
						seg.quadStage = 0
					}
					
					3    -> if (e.spell.name == "battlehorn" && player.isPotionActive(AlfheimRegistry.stoneSkin) && player.isPotionActive(Potion.field_76434_w) && player.getActivePotionEffect(Potion.field_76434_w).amplifier == 1 && player.isPotionActive(AlfheimRegistry.icelens)) {
						++seg.quadStage
					} else {
						seg.quadStage = 0
					}
					
					4    -> if (e.spell.name == "thor" && player.isPotionActive(AlfheimRegistry.stoneSkin) && player.isPotionActive(Potion.field_76434_w) && player.getActivePotionEffect(Potion.field_76434_w).amplifier == 1 && player.isPotionActive(AlfheimRegistry.icelens)) {
						++seg.quadStage
					} else {
						seg.quadStage = 0
					}
					else -> seg.quadStage = 0
				}
			}
			
			@SubscribeEvent
			fun addQuadDamageEffect(e: EntityStruckByLightningEvent) {
				if (AlfheimCore.enableMMO) {
					if (e.entity !is EntityPlayer) return
					val player = e.entity as EntityPlayer
					val seg = forPlayer(player)
					if (seg.quadStage >= 5 && player.isPotionActive(AlfheimRegistry.stoneSkin) && player.isPotionActive(Potion.field_76434_w) && player.getActivePotionEffect(Potion.field_76434_w).amplifier == 1 && player.isPotionActive(AlfheimRegistry.icelens)) {
						seg.quadStage = 0
						player.removePotionEffect(AlfheimRegistry.stoneSkin.id)
						player.removePotionEffect(Potion.field_76434_w.id)
						player.removePotionEffect(AlfheimRegistry.icelens.id)
						player.removePotionEffect(Potion.damageBoost.id)
						player.addPotionEffect(PotionEffect(AlfheimRegistry.quadDamage.id, 600, 0, false))
						AlfheimCore.network.sendToAll(MessageEffect(e.entity.entityId, AlfheimRegistry.quadDamage.id, 600, 0))
						e.isCanceled = true
					}
				}
			}
		}
	}
	
	object ManaSystem {
		
		fun handleManaChange(player: EntityPlayer) {
			PartySystem.getParty(player).sendMana(player, getMana(player))
		}
		
		fun getMana(player: EntityPlayer): Int {
			var totalMana = 0
			
			val mainInv = player.inventory
			val baublesInv = BotaniaAPI.internalHandler.getBaublesInventory(player)
			
			val invSize = mainInv.sizeInventory
			var size = invSize
			if (baublesInv != null)
				size += baublesInv.sizeInventory
			
			for (i in 0 until size) {
				val useBaubles = i >= invSize
				val inv = if (useBaubles) baublesInv else mainInv
				val stack = inv.getStackInSlot(i - if (useBaubles) invSize else 0)
				
				if (stack != null) {
					val item = stack.item
					
					if (item is ICreativeManaProvider && (item as ICreativeManaProvider).isCreative(stack)) return Integer.MAX_VALUE
					
					if (item is IManaItem)
						if (!(item as IManaItem).isNoExport(stack)) {
							if (Integer.MAX_VALUE - (item as IManaItem).getMana(stack) <= totalMana)
								return Integer.MAX_VALUE
							totalMana += (item as IManaItem).getMana(stack)
						}
				}
			}
			
			return totalMana
		}
		
		fun getMana(mr: EntityLivingBase) = (mr as? EntityPlayer)?.let { getMana(it) } ?: 0
		
		init {
			MinecraftForge.EVENT_BUS.register(ManaSyncHandler())
		}
		
		class ManaSyncHandler {
			
			@SubscribeEvent
			fun onLivingUpdate(e: LivingUpdateEvent) {
				if (AlfheimCore.enableMMO && ASJUtilities.isServer && e.entityLiving is EntityPlayer) {
					val player = e.entityLiving as EntityPlayer
					if (player.worldObj.totalWorldTime % 20 == 0L) handleManaChange(player)
				}
			}
		}
	}
	
	object TargetingSystem {
		
		fun setTarget(player: EntityPlayer, target: EntityLivingBase?, isParty: Boolean) {
			val c = forPlayer(player)
			c.target = target
			c.isParty = isParty
		}
		
		fun getTarget(player: EntityPlayer): Target {
			val c = forPlayer(player)
			// stupid kotlin -_-
			return Target(c.target, c.isParty)
		}
		
		data class Target(val target: EntityLivingBase?, val isParty: Boolean)
	}
	
	object PartySystem {
		
		fun transfer(player: EntityPlayerMP) {
			AlfheimCore.network.sendTo(MessageParty(forPlayer(player).party), player)
		}
		
		fun setParty(player: EntityPlayer, party: Party) {
			forPlayer(player).party = party
			party.sendChanges()
		}
		
		fun getParty(player: EntityPlayer) = forPlayer(player).party
		
		fun getMobParty(living: EntityLivingBase?): Party? =
			playerSegments.values.firstOrNull { it.party.isMember(living) }?.party
		
		fun getUUIDParty(id: UUID?): Party? =
			playerSegments.values.firstOrNull { it.party.isMember(id) }?.party
		
		fun sameParty(p1: EntityPlayer, p2: EntityLivingBase?) = getParty(p1).isMember(p2)
		
		fun sameParty(id: UUID?, e: EntityLivingBase?) =
			playerSegments.values.any { it.party.isMember(id) && it.party.isMember(e) }
		
		fun mobsSameParty(e1: EntityLivingBase?, e2: EntityLivingBase?) =
			playerSegments.values.any { it.party.isMember(e1) && it.party.isMember(e2) }
		
		fun friendlyFire(entityLiving: EntityLivingBase, source: DamageSource): Boolean {
			if (!AlfheimCore.enableMMO || source.damageType.contains("_FF")) return false
			
			if (!ASJUtilities.isServer) return false
			if (source.entity != null && source.entity is EntityPlayer) {
				if (getParty(source.entity as EntityPlayer).isMember(entityLiving))
					return true
			}
			if (entityLiving is EntityPlayer && source.entity != null && source.entity is EntityLivingBase) {
				if (getParty(entityLiving).isMember(source.entity as EntityLivingBase))
					return true
			}
			return source.entity != null && source.entity is EntityLivingBase && mobsSameParty(entityLiving, source.entity as EntityLivingBase)
		}
		
		@JvmStatic
		fun notifySpawn(e: Entity) {
			if (e is EntityLivingBase) {
				for (segment in playerSegments.values) {
					if (segment.party.isMember(e)) {
						for (i in 0 until segment.party.count) {
							if (segment.party.isPlayer(i)) {
								val mr = segment.party[i]
								if (mr is EntityPlayerMP) {
									AlfheimCore.network.sendTo(Message2d(Message2d.m2d.UUID, e.getEntityId().toDouble(), segment.party.indexOf(e).toDouble()), mr as EntityPlayerMP?)
								}
							}
						}
					}
				}
			}
		}
		
		class Party: Serializable, Cloneable {
			
			private var members: Array<Member?>
			var count: Int = 0
			
			val pl: EntityPlayer?
				get() = get(0) as EntityPlayer?
			
			constructor() {
				members = arrayOfNulls(AlfheimConfigHandler.maxPartyMembers)
			}
			
			private constructor(i: Int) {
				members = arrayOfNulls(i)
			}
			
			constructor(pl: EntityPlayer): this() {
				members[count++] = Member(pl.commandSenderName, pl.uniqueID, ManaSystem.getMana(pl), true, !pl.isEntityAlive, pl.health, pl.maxHealth, Member.MemberType.typeOf(pl).ordinal)
			}
			
			operator fun get(i: Int): EntityLivingBase? {
				if (members[i]?.isPlayer == true) {
					return if (ASJUtilities.isServer) {
						MinecraftServer.getServer().configurationManager.func_152612_a(members[i]?.name)
					} else {
						Minecraft.getMinecraft().theWorld.getPlayerEntityByName(members[i]?.name)
					}
				}
				if (ASJUtilities.isServer) {
					for (world in MinecraftServer.getServer().worldServers) {
						for (entity in world.loadedEntityList) {
							if (entity is EntityLivingBase && entity.uniqueID == members[i]?.uuid) {
								return entity
							}
						}
					}
				} else {
					val e = Minecraft.getMinecraft().theWorld.getEntityByID(members[i]?.uuid?.mostSignificantBits?.toInt()
																			?: 0)
					return if (e is EntityLivingBase) e else null
				}
				return null
			}
			
			operator fun get(name: String) =
				(0 until count).firstOrNull { members[it] != null && members[it]!!.name == name }?.let { get(it) }
			
			fun getHealth(i: Int) = members[i]?.health ?: 0f
			
			fun getMaxHealth(i: Int) = members[i]?.maxHealth ?: 0f
			
			fun getMana(i: Int) = members[i]?.mana ?: 0
			
			fun getName(i: Int) = members[i]?.name ?: ""
			
			fun getType(i: Int) = members[i]?.type ?: 0
			
			fun isPlayer(i: Int) = members[i]?.isPlayer ?: false
			
			fun isDead(i: Int) = members[i]?.isDead ?: false
			
			fun setHealth(i: Int, health: Float) {
				val mr = members[i] ?: return
				val was = mr.health
				if (was != health) {
					mr.health = health
					if (ASJUtilities.isServer) sendHealth(i, health)
				}
			}
			
			fun setMaxHealth(i: Int, maxHealth: Float) {
				val mr = members[i] ?: return
				val was = mr.maxHealth
				if (was != maxHealth) {
					mr.maxHealth = maxHealth
					if (ASJUtilities.isServer) sendMaxHealth(i, maxHealth)
				}
			}
			
			fun setMana(i: Int, mana: Int) {
				members[i]?.mana = mana
			}
			
			fun setType(i: Int, type: Int) {
				members[i]?.type = type
				if (ASJUtilities.isServer) sendType(i, type)
			}
			
			fun indexOf(mr: EntityLivingBase?): Int {
				if (mr == null) return -1
				return (0 until count).firstOrNull {
					if (ASJUtilities.isServer)
						mr.uniqueID == members[it]?.uuid
					else
						mr.entityId.toLong() == members[it]?.uuid?.mostSignificantBits
				} ?: -1
			}
			
			fun indexOf(name: String?): Int {
				if (name == null || name.isEmpty()) return -1
				return (0 until count).firstOrNull { name == members[it]?.name } ?: -1
			}
			
			fun isMember(mr: EntityLivingBase?): Boolean {
				if (mr != null)
					for (i in 0 until count) {
						if (ASJUtilities.isServer) {
							if (mr.uniqueID == members[i]?.uuid) return true
						} else {
							if (mr.entityId.toLong() == members[i]?.uuid?.mostSignificantBits) return true
						}
						
					}
				return false
			}
			
			fun isMember(uuid: UUID?): Boolean {
				if (uuid != null)
					for (i in 0 until count) {
						if (ASJUtilities.isServer) {
							if (uuid == members[i]?.uuid) return true
						} else {
							if (uuid.mostSignificantBits == members[i]?.uuid?.mostSignificantBits) return true
						}
						
					}
				return false
			}
			
			fun setDead(i: Int, d: Boolean) {
				if (!ASJUtilities.isServer) members[i]?.isDead = d
			}
			
			fun setDead(mr: EntityLivingBase, d: Boolean) {
				val i = indexOf(mr)
				if (i != -1) {
					if (mr is EntityPlayer) {
						members[i]?.isDead = d
						sendDead(i, d)
					} else if (d) {
						remove(mr)
						for (j in 0 until count) {
							if (members[j]?.isPlayer == true) {
								val e = get(j)
								if (e is EntityPlayer) e.addChatMessage(ChatComponentText(String.format(StatCollector.translateToLocal("alfheimmisc.party.memberdied"), mr.commandSenderName)))
							}
						}
					}
				}
			}
			
			fun setUUID(i: Int, enID: Int) {
				if (members[i] != null) members[i]?.uuid = UUID(enID.toLong(), enID.toLong())
			}
			
			fun add(mr: EntityLivingBase?): Boolean {
				if (mr == null) return false
				if (indexOf(mr) != -1) return false
				if (count >= members.size) return false
				members[count++] = Member(mr.commandSenderName, mr.uniqueID, ManaSystem.getMana(mr), mr is EntityPlayer, !mr.isEntityAlive, mr.health, mr.maxHealth, Member.MemberType.typeOf(mr).ordinal)
				sendChanges()
				return true
			}
			
			fun remove(mr: EntityLivingBase?): Boolean {
				if (mr == null) return false
				return if (mr is EntityPlayer && members[0]?.name == mr.commandSenderName) removePL() else removeSafe(mr)
			}
			
			fun remove(name: String?): Boolean {
				if (name == null || name.isEmpty()) return false
				return if (members[0]?.name == name) removePL() else removeSafe(name)
			}
			
			private fun removePL(): Boolean {
				if (pl == null) return false
				setParty(pl!!, Party(pl!!))
				var i = 1
				while (i < count) {
					if (members[i]?.isPlayer == true) {
						members[0] = members[i]
						--count
						while (i < count) {
							members[i] = members[i + 1]
							i++
						}
						sendChanges()
					}
					i++
				}
				
				return true
			}
			
			private fun removeSafe(name: String?): Boolean {
				if (name == null || name.isEmpty()) return false
				val mr = get(name)
				var id = indexOf(name)
				if (mr == null && id != -1 && !isPlayer(id)) {
					--count
					while (id < count) {
						members[id] = members[id + 1]
						id++
					}
					members[count] = null
					
					sendChanges()
					return true
				}
				return removeSafe(mr)
			}
			
			private fun removeSafe(mr: EntityLivingBase?): Boolean {
				if (mr == null) return false
				var id = indexOf(mr)
				if (id == -1) return false
				--count
				if (mr is EntityPlayer)
					setParty(mr, Party(mr))
				while (id < count) {
					members[id] = members[id + 1]
					id++
				}
				members[count] = null
				
				sendChanges()
				return true
			}
			
			fun sendChanges() {
				if (ASJUtilities.isServer)
					for (i in 0 until count) {
						val e = get(i)
						if (e != null && members[i]?.isPlayer == true && e is EntityPlayerMP)
							transfer(e)
					}
			}
			
			fun sendDead(id: Int, d: Boolean) {
				for (i in 0 until count) {
					val e = get(i)
					if (e != null && members[i]?.isPlayer == true && e is EntityPlayerMP)
						AlfheimCore.network.sendTo(Message3d(PARTY_STATUS, PartyStatus.DEAD.ordinal.toDouble(), id.toDouble(), (if (d) -10 else -100).toDouble()), e)
				}
			}
			
			fun sendHealth(index: Int, health: Float) {
				for (i in 0 until count) {
					val e = get(i)
					if (e != null && members[i]?.isPlayer == true && e is EntityPlayerMP)
						AlfheimCore.network.sendTo(Message3d(PARTY_STATUS, PartyStatus.HEALTH.ordinal.toDouble(), index.toDouble(), health.toDouble()), e)
				}
			}
			
			fun sendMaxHealth(index: Int, maxHealth: Float) {
				for (i in 0 until count) {
					val e = get(i)
					if (e != null && members[i]?.isPlayer == true && e is EntityPlayerMP)
						AlfheimCore.network.sendTo(Message3d(PARTY_STATUS, PartyStatus.MAXHEALTH.ordinal.toDouble(), index.toDouble(), maxHealth.toDouble()), e)
				}
			}
			
			fun sendMana(player: EntityPlayer, mana: Int) {
				val index = indexOf(player)
				
				for (i in 0 until count) {
					val e = get(i)
					if (e != null && members[i]?.isPlayer == true && e is EntityPlayerMP)
						AlfheimCore.network.sendTo(Message3d(PARTY_STATUS, PartyStatus.MANA.ordinal.toDouble(), index.toDouble(), mana.toDouble()), e)
				}
			}
			
			fun sendType(index: Int, type: Int) {
				for (i in 0 until count) {
					val e = get(i)
					if (e != null && members[i]?.isPlayer == true && e is EntityPlayerMP)
						AlfheimCore.network.sendTo(Message3d(PARTY_STATUS, PartyStatus.TYPE.ordinal.toDouble(), index.toDouble(), type.toDouble()), e)
				}
			}
			
			enum class PartyStatus {
				DEAD, HEALTH, MAXHEALTH, MANA, TYPE
			}
			
			fun write(buf: ByteBuf) {
				buf.writeInt(members.size)
				buf.writeInt(count)
				var mr: EntityLivingBase?
				for (i in 0 until count) {
					mr = get(i)
					if (serverIO) {
						buf.writeLong(mr?.uniqueID?.mostSignificantBits ?: 0)
						buf.writeLong(mr?.uniqueID?.leastSignificantBits ?: 0)
					} else {
						buf.writeInt(mr?.entityId ?: 0)
					}
					ByteBufUtils.writeUTF8String(buf, members[i]?.name ?: "")
					buf.writeInt(members[i]?.mana ?: 0)
					buf.writeBoolean(members[i]?.isPlayer ?: false)
					buf.writeBoolean(members[i]?.isDead ?: false)
					buf.writeFloat(members[i]?.health ?: 0f)
					buf.writeFloat(members[i]?.maxHealth ?: 0f)
					buf.writeInt(members[i]?.type ?: Member.MemberType.MOB.ordinal)
				}
			}
			
			public override fun clone(): Any {
				val result = Party()
				result.members = members.clone()
				result.count = count
				return result
			}
			
			private class Member(val name: String, var uuid: UUID, var mana: Int, val isPlayer: Boolean, var isDead: Boolean, var health: Float, var maxHealth: Float, var type: Int): Serializable, Cloneable {
				
				public override fun clone() = Member(name, uuid, mana, isPlayer, isDead, health, maxHealth, type)
				
				enum class MemberType {
					HUMAN, SALAMANDER, SYLPH, CAITSITH, POOKA, GNOME, LEPRECHAUN, SPRIGGAN, UNDINE, IMP, ALV, MOB, NPC, BOSS;
					
					companion object {
						fun typeOf(e: EntityLivingBase) = when (e) {
							is EntityPlayer     -> values()[e.raceID]
							is IBossDisplayData -> BOSS
							is INpc             -> NPC
							else                -> MOB
						}
					}
				}
				
				companion object {
					private const val serialVersionUID = 8416468367146381L
				}
			}
			
			companion object {
				
				private const val serialVersionUID = 84616843168484257L
				
				/** Flag for server's storing functions, always false */
				@Transient
				var serverIO = false
				
				fun read(buf: ByteBuf): Party {
					val size = buf.readInt()
					val count = buf.readInt()
					val pt = Party(size)
					pt.count = count
					var most: Long
					var least: Long
					for (i in 0 until count) {
						if (serverIO) {
							most = buf.readLong()
							least = buf.readLong()
						} else {
							least = buf.readInt().toLong()
							most = least
						}
						pt.members[i] = Member(ByteBufUtils.readUTF8String(buf), UUID(most, least), buf.readInt(), buf.readBoolean(), buf.readBoolean(), buf.readFloat(), buf.readFloat(), buf.readInt())
					}
					return pt
				}
			}
		}
		
		init {
			MinecraftForge.EVENT_BUS.register(PartyThingsListener())
		}
		
		class PartyThingsListener {
			
			@SubscribeEvent
			fun onClonePlayer(e: PlayerEvent.Clone) {
				if (AlfheimCore.enableMMO && e.wasDeath) getParty(e.entityPlayer).setDead(e.entityPlayer, false)
			}
			
			@SubscribeEvent
			fun onPlayerRespawn(e: PlayerRespawnEvent) {
				if (AlfheimCore.enableMMO) getParty(e.player).setDead(e.player, false)
			}
		}
	}
	
	object HotSpellsSystem {
		
		fun transfer(player: EntityPlayerMP) = AlfheimCore.network.sendTo(MessageHotSpellC(forPlayer(player).hotSpells), player)
		
		fun getHotSpellID(player: EntityPlayer, slot: Int) = forPlayer(player).hotSpells[slot]
		
		fun setHotSpellID(player: EntityPlayer, slot: Int, id: Int) {
			forPlayer(player).hotSpells[slot] = id
		}
	}
	
	object TimeStopSystem {
		
		var tsAreas = HashMap<Int, LinkedList<TimeStopArea>>()
		
		fun transfer(player: EntityPlayerMP, fromDim: Int) {
			if (tsAreas[fromDim] != null) for (tsa in tsAreas[fromDim]!!) AlfheimCore.network.sendTo(Message1d(Message1d.m1d.TIME_STOP_REMOVE, tsa.id.toDouble()), player)
			if (tsAreas[player.dimension] != null) for (tsa in tsAreas[player.dimension]!!) AlfheimCore.network.sendTo(MessageTimeStop(PartySystem.getUUIDParty(tsa.uuid), tsa.pos.x, tsa.pos.y, tsa.pos.z, tsa.id), player)
		}
		
		fun stop(caster: EntityLivingBase) {
			caster.worldObj.playBroadcastSound(1013, caster.posX.toInt(), caster.posY.toInt(), caster.posZ.toInt(), 0)
			if (tsAreas[caster.dimension] == null) tsAreas[caster.dimension] = LinkedList()
			tsAreas[caster.dimension]!!.addLast(TimeStopArea(caster))
			AlfheimCore.network.sendToDimension(MessageTimeStop(PartySystem.getMobParty(caster), caster.posX, caster.posY, caster.posZ, TimeStopArea.nextID), caster.dimension)
		}
		
		fun tick() {
			var tsa: TimeStopArea
			for (dim in tsAreas.keys) {
				val tsas = tsAreas[dim]!!
				val i = tsas.iterator()
				while (i.hasNext()) {
					tsa = i.next()
					if (--tsa.life <= 0) {
						i.remove()
						AlfheimCore.network.sendToDimension(Message1d(Message1d.m1d.TIME_STOP_REMOVE, tsa.id.toDouble()), dim)
					}
				}
			}
		}
		
		fun affected(e: Entity?): Boolean {
			if (e == null) return false
			val ev = TimeStopEntityCheckEvent(e)
			if (MinecraftForge.EVENT_BUS.post(ev)) return ev.result
			if (e is IBossDisplayData) return false
			if (e is ITimeStopSpecific && (e as ITimeStopSpecific).isImmune) return false
			if (tsAreas[e.dimension] == null) return false
			for (tsa in tsAreas[e.dimension]!!) {
				if (Vector3.vecEntityDistance(tsa.pos, e) < 16) {
					if (e is ITimeStopSpecific && (e as ITimeStopSpecific).affectedBy(tsa.uuid)) return true
					if (e is EntityLivingBase) {
						if (!PartySystem.sameParty(tsa.uuid, e)) return true
					} else {
						return true
					}
				}
			}
			return false
		}
		
		class TimeStopArea(caster: EntityLivingBase): Serializable {
			val pos = Vector3.fromEntity(caster)
			val uuid = caster.entityUniqueID!!
			@Transient
			val id: Int
			var life = 1200
			
			init {
				id = ++nextID
			}
			
			companion object {
				
				private const val serialVersionUID = 4146871637815241L
				
				@Transient
				var nextID = -1
			}
		}
		
		init {
			MinecraftForge.EVENT_BUS.register(TimeStopThingsListener())
		}
		
		class TimeStopThingsListener {
			@SubscribeEvent
			fun onPlayerChangedDimension(e: PlayerChangedDimensionEvent) {
				if (AlfheimCore.enableMMO && e.player is EntityPlayerMP) transfer(e.player as EntityPlayerMP, e.fromDim)
			}
			
			@SubscribeEvent
			fun onEntityUpdate(e: EntityUpdateEvent) {
				if (!e.entity.isEntityAlive) return
				if (AlfheimCore.enableMMO && ASJUtilities.isServer && affected(e.entity)) e.isCanceled = true
			}
			
			@SubscribeEvent
			fun onLivingUpdate(e: LivingUpdateEvent) {
				if (AlfheimCore.enableMMO && ASJUtilities.isServer && affected(e.entity)) e.isCanceled = true
			}
		}
	}
	
	class PlayerSegment(player: EntityPlayer): Serializable {
		
		var coolDown = HashMap<SpellBase, Int>()
		var hotSpells = IntArray(12)
		
		var party: Party = Party(player)
		@Transient
		var target: EntityLivingBase? = null
		@Transient
		var isParty = false
		
		@Transient
		var castableSpell: SpellBase? = null
		@Transient
		var ids: Int = 0
		@Transient
		var init: Int = 0
		
		var knowledge: MutableSet<String> = HashSet()
		
		var userName: String = player.commandSenderName
		
		@Transient
		var quadStage = 0
		
		companion object {
			
			private const val serialVersionUID = 6871678638741684L
		}
	}
}