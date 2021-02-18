package alfheim.api.entity

import alexsocol.asjlib.*
import alfheim.api.ModInfo
import alfheim.api.event.PlayerChangedRaceEvent
import net.minecraft.entity.ai.attributes.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.*
import net.minecraftforge.common.MinecraftForge

enum class EnumRace {
	
	HUMAN, SALAMANDER, SYLPH, CAITSITH, POOKA, GNOME, LEPRECHAUN, SPRIGGAN, UNDINE, IMP, ALV;
	
	val rgbColor: Int
		get() = getRGBColor(ordinal.D)
	
	val enumColor: EnumChatFormatting
		get() = getEnumColor(ordinal.D)
	
	fun glColor() {
		glColor(ordinal.D)
	}
	
	fun glColorA(alpha: Double) {
		glColorA(ordinal.D, alpha)
	}
	
	fun localize() =
		StatCollector.translateToLocal("race." + toString() + ".name")!!
	
	companion object {
		
		private val RACE: IAttribute = RangedAttribute(ModInfo.MODID.toUpperCase() + ":RACE", 0.0, 0.0, values().size.D.minus(1)).setShouldWatch(true)
		
		fun getRGBColor(id: Double): Int {
			return when (getByID(id)) {
				SALAMANDER -> 0xb61f24
				SYLPH      -> 0x5ee52e
				CAITSITH   -> 0xcdb878
				POOKA      -> 0x99cb3b
				GNOME      -> 0x816b57
				LEPRECHAUN -> 0x6d6b7b
				SPRIGGAN   -> 0x282739
				UNDINE     -> 0x40c0a4
				IMP        -> 0x786a89
				else       -> 0xffffff
			}
		}
		
		fun getEnumColor(id: Double): EnumChatFormatting {
			return when (getByID(id)) {
				SALAMANDER -> EnumChatFormatting.DARK_RED
				SYLPH      -> EnumChatFormatting.GREEN
				CAITSITH   -> EnumChatFormatting.YELLOW
				POOKA      -> EnumChatFormatting.GOLD
				GNOME      -> EnumChatFormatting.DARK_GREEN
				LEPRECHAUN -> EnumChatFormatting.GRAY
				SPRIGGAN   -> EnumChatFormatting.WHITE
				UNDINE     -> EnumChatFormatting.AQUA
				IMP        -> EnumChatFormatting.LIGHT_PURPLE
				else       -> EnumChatFormatting.WHITE
			}
		}
		
		fun glColor(id: Double) {
			glColor1u(addAlpha(getRGBColor(id), 255))
		}
		
		fun glColorA(id: Double, alpha: Double) {
			glColor1u(addAlpha(getRGBColor(id), (alpha * 255).I))
		}
		
		private fun addAlpha(color: Int, alpha: Int) =
			alpha and 0xFF shl 24 or (color and 0x00FFFFFF)
		
		private fun glColor1u(color: Int) {
			org.lwjgl.opengl.GL11.glColor4ub((color shr 16 and 0xFF).toByte(), (color shr 8 and 0xFF).toByte(), (color and 0xFF).toByte(), (color shr 24 and 0xFF).toByte())
		}
		
		private fun getByID(id: Double) =
			if (0 > id || id > values().size) HUMAN else values()[id.I]
		
		fun ensureExistance(player: EntityPlayer) {
			if (player.getEntityAttribute(RACE) == null) registerRace(player)
		}
		
		private fun registerRace(player: EntityPlayer) {
			player.getAttributeMap().registerAttribute(RACE)
			setRaceID(player, 0.0)
		}
		
		operator fun get(id: Int) = getByID(id.D)
		
		operator fun get(player: EntityPlayer): EnumRace {
			ensureExistance(player)
			return getByID(player.getEntityAttribute(RACE).attributeValue)
		}
		
		fun getRaceID(player: EntityPlayer): Int {
			ensureExistance(player)
			return player.getEntityAttribute(RACE).attributeValue.I
		}
		
		operator fun set(player: EntityPlayer, race: EnumRace) {
			ensureExistance(player)
			player.getEntityAttribute(RACE).baseValue = race.ordinal.D
			
			MinecraftForge.EVENT_BUS.post(PlayerChangedRaceEvent(player, player.race, race))
		}
		
		internal fun setRaceID(player: EntityPlayer, raceID: Double) {
			player.getEntityAttribute(RACE).baseValue = raceID
		}
	}
}

var EntityPlayer.race
	get() = EnumRace[this]
	set(value) {
		EnumRace[this] = value
	}

/**
 * Internal Alfheim value, please, don't set it unless you know what you are doing
 * <br>
 * and fire [alfheim.api.event.PlayerChangedRaceEvent] if needed
 */
var EntityPlayer.raceID
	get() = EnumRace.getRaceID(this)
	internal set(value) {
		EnumRace.setRaceID(this, value.D)
	}