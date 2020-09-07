package alfheim.common.core.handler

import alexsocol.asjlib.*
import alfheim.AlfheimCore
import alfheim.api.AlfheimAPI
import alfheim.api.entity.*
import alfheim.api.event.PlayerInteractAdequateEvent.*
import alfheim.api.event.PlayerInteractAdequateEvent.LeftClick.Action.*
import alfheim.api.event.PlayerInteractAdequateEvent.RightClick.Action.*
import alfheim.api.spell.SpellBase.SpellCastResult.DESYNC
import alfheim.common.core.helper.*
import alfheim.common.item.AlfheimItems
import alfheim.common.network.Message2d
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import net.minecraftforge.common.MinecraftForge

object KeyBindingHandler {
	
	fun enableFlight(player: EntityPlayerMP, boost: Boolean) {
		if (AlfheimConfigHandler.wingsBlackList.contains(player.worldObj.provider.dimensionId)) {
			ASJUtilities.say(player, "mes.flight.unavailable")
		} else {
			if (!AlfheimConfigHandler.enableElvenStory || player.race == EnumRace.HUMAN || (player.capabilities.isCreativeMode && boost)) return
			if (!CardinalSystem.forPlayer(player).esmAbility) return
			
			player.capabilities.allowFlying = true
			player.capabilities.isFlying = !player.capabilities.isFlying
			player.sendPlayerAbilities()
			if (boost) ElvenFlightHelper.sub(player, 300)
		}
	}
	
	fun toggleESMAbility(player: EntityPlayerMP) {
		val seg = CardinalSystem.forPlayer(player)
		seg.toggleESMAbility()
		
		if (!seg.esmAbility && !player.capabilities.isCreativeMode) {
			player.capabilities.isFlying = false
			player.capabilities.allowFlying = false
			player.sendPlayerAbilities()
		}
	}
	
	fun hit(player: EntityPlayerMP) {
		val mopEntity = ASJUtilities.getMouseOver(player, player.theItemInWorldManager.blockReachDistance, true)
		val mopNoEntity = ASJUtilities.getMouseOver(player, player.theItemInWorldManager.blockReachDistance, false)
		
		if (mopNoEntity == null || mopNoEntity.typeOfHit == MovingObjectType.MISS) {
			when (mopEntity?.typeOfHit) {
				MovingObjectType.BLOCK  ->
					MinecraftForge.EVENT_BUS.post(LeftClick(player, LEFT_CLICK_LIQUID, mopEntity.blockX, mopEntity.blockY, mopEntity.blockZ, mopEntity.sideHit, mopEntity.entityHit))
				MovingObjectType.ENTITY ->
					MinecraftForge.EVENT_BUS.post(LeftClick(player, LEFT_CLICK_ENTIY, mopEntity.blockX, mopEntity.blockY, mopEntity.blockZ, mopEntity.sideHit, mopEntity.entityHit))
				else                    ->
					MinecraftForge.EVENT_BUS.post(LeftClick(player, LEFT_CLICK_AIR, mopNoEntity?.blockX ?: -1, mopNoEntity?.blockY ?: -1, mopNoEntity?.blockZ ?: -1, mopNoEntity?.sideHit ?: -1, mopNoEntity?.entityHit))
			}
		} else if (mopNoEntity.typeOfHit == MovingObjectType.BLOCK)
			MinecraftForge.EVENT_BUS.post(LeftClick(player, LEFT_CLICK_BLOCK, mopNoEntity.blockX, mopNoEntity.blockY, mopNoEntity.blockZ, mopNoEntity.sideHit, mopNoEntity.entityHit))
	}
	
	fun use(player: EntityPlayerMP) {
		val mopEntity = ASJUtilities.getMouseOver(player, player.theItemInWorldManager.blockReachDistance, true)
		val mopNoEntity = ASJUtilities.getMouseOver(player, player.theItemInWorldManager.blockReachDistance, false)
		
		if (mopNoEntity == null || mopNoEntity.typeOfHit == MovingObjectType.MISS) {
			when (mopEntity?.typeOfHit) {
				MovingObjectType.BLOCK  ->
					MinecraftForge.EVENT_BUS.post(RightClick(player, RIGHT_CLICK_LIQUID, mopEntity.blockX, mopEntity.blockY, mopEntity.blockZ, mopEntity.sideHit, mopEntity.entityHit))
				MovingObjectType.ENTITY ->
					MinecraftForge.EVENT_BUS.post(RightClick(player, RIGHT_CLICK_ENTIY, mopEntity.blockX, mopEntity.blockY, mopEntity.blockZ, mopEntity.sideHit, mopEntity.entityHit))
				else                    ->
					MinecraftForge.EVENT_BUS.post(RightClick(player, RIGHT_CLICK_AIR, mopNoEntity?.blockX ?: -1, mopNoEntity?.blockY ?: -1, mopNoEntity?.blockZ ?: -1, mopNoEntity?.sideHit ?: -1, mopNoEntity?.entityHit))
			}
		} else if (mopNoEntity.typeOfHit == MovingObjectType.BLOCK)
			MinecraftForge.EVENT_BUS.post(RightClick(player, RIGHT_CLICK_BLOCK, mopNoEntity.blockX, mopNoEntity.blockY, mopNoEntity.blockZ, mopNoEntity.sideHit, mopNoEntity.entityHit))
	}
	
	fun cast(player: EntityPlayerMP, hotSpell: Boolean, id: Int) {
		val ids = if (hotSpell) CardinalSystem.HotSpellsSystem.getHotSpellID(player, id) else id
		val seg = CardinalSystem.forPlayer(player)
		val spell = AlfheimAPI.getSpellByIDs(ids shr 28 and 0xF, ids and 0xFFFFFFF)
		if (spell == null)
			AlfheimCore.network.sendTo(Message2d(Message2d.m2d.COOLDOWN, ids.D, (-DESYNC.ordinal).D), player)
		else {
			seg.ids = ids
			seg.init = spell.getCastTime()
			seg.castableSpell = spell
		}
	}
	
	fun unCast(player: EntityPlayerMP) {
		val seg = CardinalSystem.forPlayer(player)
		seg.ids = 0
		seg.init = seg.ids
		seg.castableSpell = null
	}
	
	fun select(player: EntityPlayerMP, team: Boolean, id: Int) {
		if (team) {
			val mr = CardinalSystem.PartySystem.getParty(player)[id]
			if (mr is EntityLivingBase)
				CardinalSystem.TargetingSystem.setTarget(player, mr, team, id)
		} else {
			if (id != -1) {
				val e = player.worldObj.getEntityByID(id)
				if (e is EntityLivingBase) {
					CardinalSystem.TargetingSystem.setTarget(player, e, team)
				}
			} else
				CardinalSystem.TargetingSystem.setTarget(player, null, false)
		}
	}
	
	fun secret(player: EntityPlayerMP) { // now just gives me my staff in exchange for 9 sticks
		if (ContributorsPrivacyHelper.isCorrect(player, "AlexSocol")) {
			if (player.currentEquippedItem?.item === Items.stick && player.currentEquippedItem?.stackSize == 9) {
				player.setCurrentItemOrArmor(0, ItemStack(AlfheimItems.royalStaff))
			}
		}
	}
}