package alfheim.client.core.handler

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.api.AlfheimAPI
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.SpellVisualizations
import alfheim.common.core.handler.CardinalSystem.KnowledgeSystem.Knowledge
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party
import net.minecraft.client.Minecraft
import net.minecraft.entity.*
import net.minecraft.entity.boss.IBossDisplayData
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.Potion
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.MovingObjectPosition.MovingObjectType

import java.io.Serializable
import java.util.*

object CardinalSystemClient {
	
	private val mc get() = Minecraft.getMinecraft()
	
	var segment: PlayerSegmentClient? = null
	
	fun segment(): PlayerSegmentClient {
		if (segment == null) segment = PlayerSegmentClient()
		return segment!!
	}
	
	object SpellCastingSystemClient {
		
		fun setCoolDown(spell: SpellBase?, cd: Int) {
			if (spell == null) return
			segment().coolDown[spell] = cd
		}
		
		fun getCoolDown(spell: SpellBase?): Int {
			return try {
				if (spell == null) 0 else segment().coolDown[spell]!!
			} catch (e: Throwable) {
				System.err.println(String.format("Something went wrong getting cooldown for %s. Returning 0.", spell))
				e.printStackTrace()
				0
			}
		}
		
		fun tick() {
			try {
				for (spell in segment().coolDown.keys) {
					val time = segment!!.coolDown[spell]!!
					if (time > 0) segment().coolDown[spell] = time - 1
				}
				if (segment!!.init > 0) --segment!!.init
			} catch (e: Throwable) {
				System.err.println("Something went wrong ticking spells. Skipping this tick.")
				e.printStackTrace()
			}
			
		}
		
		fun reset() {
			for (spell in segment().coolDown.keys) {
				segment().coolDown[spell] = 0
			}
		}
	}
	
	object TargetingSystemClient {
		
		fun selectMob(): Boolean {
			if (mc?.thePlayer == null) return false
			val mop = ASJUtilities.getMouseOver(mc.thePlayer, 128.0, true)
			if (mop != null && mop.typeOfHit == MovingObjectType.ENTITY && mop.entityHit is EntityLivingBase) {
				if (!segment!!.party.isMember(mop.entityHit as EntityLivingBase)) {
					val invis = (mop.entityHit as EntityLivingBase).isPotionActive(Potion.invisibility) || mop.entityHit.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer)
					if (Vector3.entityDistance(mop.entityHit, mc.thePlayer) < (if (mop.entityHit is IBossDisplayData) 128 else 32) && !invis) {
						segment!!.target = mop.entityHit as EntityLivingBase
						segment!!.isParty = false
					}
				} else
					ASJUtilities.say(mc.thePlayer, "alfheimmisc.teamnotmob")
			}
			return segment!!.target != null && !segment!!.isParty
		}
		
		fun selectTeam(): Boolean {
			if (mc?.thePlayer == null) return false
			segment().isParty = true
			if (segment!!.party == null) segment!!.party = Party(mc.thePlayer)
			while (true) {
				PlayerSegmentClient.partyIndex = ++PlayerSegmentClient.partyIndex % segment!!.party!!.count
				val team = segment!!.party!![PlayerSegmentClient.partyIndex]
				segment!!.target = team
				if (team != null && Vector3.entityDistancePlane(mc.thePlayer, segment!!.target!!) < (if (team is IBossDisplayData) 128 else 32)) break
			}
			return segment!!.isParty
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
			for (tsa in tsAreas) {
				if (Vector3.vecEntityDistance(tsa.pos, e) < 16) {
					if (e is EntityLivingBase) {
						if (!tsa.cPt?.isMember(e as EntityLivingBase?)!!) return true
					} else {
						return true
					}
				}
			}
			return false
		}
		
		fun affected(te: TileEntity?): Boolean {
			if (te == null || !te.hasWorldObj()) return false
			for (tsa in tsAreas)
				if (Vector3.vecTileDistance(tsa.pos, te) < 16) return true
			return false
		}
		
		fun inside(pl: EntityPlayer): Boolean {
			for (tsa in tsAreas) if (Vector3.vecEntityDistance(tsa.pos, pl) < 16.5) return true
			return false
		}
		
		fun render() {
			for (tsa in tsAreas) SpellVisualizations.redSphere(tsa.pos.x, tsa.pos.y, tsa.pos.z)
		}
		
		fun remove(i: Int) {
			tsAreas.remove(TimeStopAreaClient(0.0, 0.0, 0.0, null, i))
		}
		
		private class TimeStopAreaClient(x: Double, y: Double, z: Double, val cPt: Party?, val id: Int) {
			
			val pos = Vector3(x, y, z)
			
			override fun equals(other: Any?): Boolean {
				return if (other !is TimeStopAreaClient) false else other.id == id
			}
		}
	}
	
	class PlayerSegmentClient: Serializable {
		
		val coolDown = HashMap<SpellBase, Int>()
		var hotSpells = IntArray(12)
		// current and max spell init time (for blue bar)
		var init: Int = 0
		var initM: Int = 0
		
		var party: Party
		var target: EntityLivingBase? = null
		var isParty: Boolean = false
		
		val userName: String
		
		init {
			for (spell in AlfheimAPI.spells) coolDown[spell] = 0
			party = Party(mc!!.thePlayer)
			userName = mc.thePlayer.commandSenderName
			knowledge = BooleanArray(Knowledge.values().size)
		}
		
		companion object {
			
			private const val serialVersionUID = 6871678638741684L
			var partyIndex: Int = 0
			
			lateinit var knowledge: BooleanArray
		}
	}
}