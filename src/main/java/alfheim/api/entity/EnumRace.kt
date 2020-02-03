package alfheim.api.entity

import alfheim.api.ModInfo
import alfheim.api.event.PlayerChangedRaceEvent
import alfheim.common.core.util.*
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
			//return ASJUtilities.enumColorToRGB(getEnumColor(id));
			
			if (id == 1.0) return 0xb61f24
			if (id == 2.0) return 0x5ee52e
			if (id == 3.0) return 0xcdb878
			if (id == 4.0) return 0x99cb3b
			if (id == 5.0) return 0x816b57
			if (id == 6.0) return 0x6d6b7b
			if (id == 7.0) return 0x282739
			if (id == 8.0) return 0x40c0a4
			return if (id == 9.0) 0x786a89 else 0xffffff
		}
		
		fun getEnumColor(id: Double): EnumChatFormatting {
			if (id == 1.0) return EnumChatFormatting.DARK_RED
			if (id == 2.0) return EnumChatFormatting.GREEN
			if (id == 3.0) return EnumChatFormatting.YELLOW
			if (id == 4.0) return EnumChatFormatting.GOLD
			if (id == 5.0) return EnumChatFormatting.DARK_GREEN
			if (id == 6.0) return EnumChatFormatting.GRAY
			if (id == 7.0) return EnumChatFormatting.WHITE
			if (id == 8.0) return EnumChatFormatting.AQUA
			return if (id == 9.0) EnumChatFormatting.LIGHT_PURPLE else EnumChatFormatting.WHITE
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
			if (player.getAttributeMap().getAttributeInstanceByName(RACE.attributeUnlocalizedName) == null) registerRace(player)
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