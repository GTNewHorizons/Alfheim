package alfheim.common.item.relic.record

import alexsocol.asjlib.hasAchievement
import alfheim.api.entity.*
import alfheim.api.item.relic.record.AkashicRecord
import alfheim.common.achievement.AlfheimAchievements
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.item.relic.ItemAkashicRecords
import net.minecraft.entity.player.*
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagString

object AkashicRecordNewChance: AkashicRecord("newchance") {
	
	override fun canGet(player: EntityPlayer, stack: ItemStack): Boolean {
		if (!AlfheimConfigHandler.enableElvenStory) return false
		
		val noach = (player as? EntityPlayerMP)?.hasAchievement(AlfheimAchievements.newChance)?.not() ?: false
		val noinside = stack.tagCompound.tagMap.none { (key, value) ->
			(key as String).startsWith(ItemAkashicRecords.TAG_RECORD_PREF) && (value as NBTTagString).func_150285_a_() == name
		}
		
		return noach && noinside
	}
	
	override fun apply(player: EntityPlayer, stack: ItemStack): Boolean {
		if (AlfheimConfigHandler.enableElvenStory && player.race != EnumRace.HUMAN && player.race != EnumRace.ALV) {
			player.triggerAchievement(AlfheimAchievements.newChance)
			player.race = EnumRace.HUMAN
			
			return true
		}
		
		return false
	}
}