package alfheim.api.entity

import alfheim.api.AlfheimAPI
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.MathHelper
import net.minecraft.util.StatCollector

enum class EnumRace {
	HUMAN, SALAMANDER, SYLPH, CAITSITH, POOKA, GNOME, LEPRECHAUN, SPRIGGAN, UNDINE, IMP, ALV;
	
	val rgbColor: Int
		get() = getRGBColor(ordinal.toDouble())
	
	val enumColor: EnumChatFormatting
		get() = getEnumColor(ordinal.toDouble())
	
	fun glColor() {
		glColor(ordinal.toDouble())
	}
	
	fun glColorA(alpha: Double) {
		glColorA(ordinal.toDouble(), alpha)
	}
	
	fun localize(): String {
		return StatCollector.translateToLocal("race." + toString() + ".name")
	}
	
	companion object {
		
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
			glColor1u(addAlpha(getRGBColor(id), (alpha * 255).toInt()))
		}
		
		private fun addAlpha(color: Int, alpha: Int): Int {
			return alpha and 0xFF shl 24 or (color and 0x00FFFFFF)
		}
		
		private fun glColor1u(color: Int) {
			org.lwjgl.opengl.GL11.glColor4ub((color shr 16 and 0xFF).toByte(), (color shr 8 and 0xFF).toByte(), (color and 0xFF).toByte(), (color shr 24 and 0xFF).toByte())
		}
		
		fun getByID(id: Double): EnumRace {
			return if (0 > id || id > EnumRace.values().size) HUMAN else EnumRace.values()[MathHelper.floor_double(id)]
		}
		
		fun unlocalize(name: String): String {
			return StatCollector.translateToLocal("race.$name.reverse")
		}
		
		fun ensureExistance(player: EntityPlayer) {
			if (player.getAttributeMap().getAttributeInstance(AlfheimAPI.RACE) == null) registerRace(player)
		}
		
		fun getRace(player: EntityPlayer): EnumRace {
			ensureExistance(player)
			return getByID(player.getEntityAttribute(AlfheimAPI.RACE).attributeValue)
		}
		
		fun getRaceID(player: EntityPlayer): Int {
			ensureExistance(player)
			return MathHelper.floor_double(player.getEntityAttribute(AlfheimAPI.RACE).attributeValue)
		}
		
		fun setRace(player: EntityPlayer, race: EnumRace) {
			ensureExistance(player)
			player.getEntityAttribute(AlfheimAPI.RACE).baseValue = race.ordinal.toDouble()
		}
		
		fun setRaceID(player: EntityPlayer, raceID: Double) {
			player.getEntityAttribute(AlfheimAPI.RACE).baseValue = raceID
		}
		
		private fun registerRace(player: EntityPlayer) {
			player.getAttributeMap().registerAttribute(AlfheimAPI.RACE)
			setRace(player, HUMAN)
		}
	}
}