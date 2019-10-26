package alfheim.client.core.handler

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.SpellVisualizations
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party
import net.minecraft.client.Minecraft
import net.minecraft.entity.*
import net.minecraft.entity.boss.IBossDisplayData
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.Potion
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

object CardinalSystemClient {
	
	val mc get() = Minecraft.getMinecraft()!!
	
	val playerSkinsData = HashMap<String, Pair<Boolean, Boolean>>()
	
	object SpellCastingSystemClient {
		
		fun setCoolDown(spell: SpellBase?, cd: Int) {
			if (spell == null) return
			PlayerSegmentClient.coolDown[spell.name] = cd
		}
		
		fun getCoolDown(spell: SpellBase): Int {
			return try {
				PlayerSegmentClient.coolDown[spell.name] ?: 0
			} catch (e: Throwable) {
				ASJUtilities.error("Something went wrong getting cooldown for $spell. Returning 0.")
				e.printStackTrace()
				0
			}
		}
		
		fun tick() {
			try {
				for (spell in PlayerSegmentClient.coolDown.keys) {
					val time = PlayerSegmentClient.coolDown[spell]!!
					if (time > 0) PlayerSegmentClient.coolDown[spell] = time - 1
				}
				if (PlayerSegmentClient.init > 0) --PlayerSegmentClient.init
			} catch (e: Throwable) {
				System.err.println("Something went wrong ticking spells. Skipping this tick.")
				e.printStackTrace()
			}
		}
	}
	
	object TargetingSystemClient {
		
		fun selectMob(): Boolean {
			if (mc.thePlayer == null) return false
			val mop = ASJUtilities.getMouseOver(mc.thePlayer, 128.0, true)
			if (mop != null && mop.typeOfHit == MovingObjectType.ENTITY && mop.entityHit is EntityLivingBase) {
				if (PlayerSegmentClient.party?.isMember(mop.entityHit as EntityLivingBase) == false) {
					val invis = (mop.entityHit as EntityLivingBase).isPotionActive(Potion.invisibility) || mop.entityHit.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer)
					if (Vector3.entityDistance(mop.entityHit, mc.thePlayer) < (if (mop.entityHit is IBossDisplayData) 128 else 32) && !invis) {
						PlayerSegmentClient.target = mop.entityHit as EntityLivingBase
						PlayerSegmentClient.isParty = false
					}
				} else
					ASJUtilities.say(mc.thePlayer, "alfheimmisc.teamnotmob")
			}
			return PlayerSegmentClient.target != null && !PlayerSegmentClient.isParty
		}
		
		fun selectTeam(): Boolean {
			if (mc.thePlayer == null) return false
			PlayerSegmentClient.isParty = true
			
			if (PlayerSegmentClient.party == null) PlayerSegmentClient.party = Party(mc.thePlayer)
			
			while (true) {
				PlayerSegmentClient.partyIndex = ++PlayerSegmentClient.partyIndex % PlayerSegmentClient.party!!.count
				val team = PlayerSegmentClient.party!![PlayerSegmentClient.partyIndex]
				PlayerSegmentClient.target = team
				if (team != null && Vector3.entityDistancePlane(mc.thePlayer, PlayerSegmentClient.target!!) < (if (team is IBossDisplayData) 128 else 32)) break
			}
			return PlayerSegmentClient.isParty
		}
	}
	
	object TimeStopSystemClient {
		
		private val tsAreas = LinkedList<TimeStopAreaClient>()
		
		fun clear() = tsAreas.clear()
		
		fun stop(x: Double, y: Double, z: Double, pt: Party, id: Int) {
			tsAreas.add(TimeStopAreaClient(x, y, z, pt, id))
		}
		
		fun affected(e: Entity?): Boolean {
			if (e == null || e is IBossDisplayData) return false
			tsAreas
				.filter { Vector3.vecEntityDistance(it.pos, e) < 16 }
				.forEach {
					if (e is EntityLivingBase) {
						if (!it.cPt?.isMember(e as EntityLivingBase?)!!) return true
					} else {
						return true
					}
				}
			return false
		}
		
		fun inside(pl: EntityPlayer) = tsAreas.any { Vector3.vecEntityDistance(it.pos, pl) < 16.5 }
		
		fun render() {
			for (tsa in tsAreas) SpellVisualizations.redSphere(tsa.pos.x, tsa.pos.y, tsa.pos.z)
		}
		
		fun remove(i: Int) {
			tsAreas.remove(TimeStopAreaClient(0.0, 0.0, 0.0, null, i))
		}
		
		private class TimeStopAreaClient(x: Double, y: Double, z: Double, val cPt: Party?, val id: Int) {
			
			val pos = Vector3(x, y, z)
			
			override fun equals(other: Any?) = if (other !is TimeStopAreaClient) false else other.id == id
		}
	}
	
	object PlayerSegmentClient: Serializable {
		
		val coolDown = HashMap<String, Int>()
		var hotSpells = IntArray(12)
		// current and max spell init time (for blue bar)
		var init: Int = 0
		var initM: Int = 0
		
		var party: Party? = null
		var target: EntityLivingBase? = null
		var isParty: Boolean = false
		var partyIndex: Int = 0
		
		var knowledge: MutableSet<String> = HashSet()
		
		var esmAbility = true
		
		private const val serialVersionUID = 6871678638741684L
	}
}