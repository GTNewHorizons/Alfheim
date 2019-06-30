package alfheim.common.core.handler

import alexsocol.asjlib.ASJReflectionHelper
import alfheim.common.item.equipment.bauble.*
import alfheim.common.network.Message0d
import alfheim.common.network.Message0d.m0d
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.common.network.simpleimpl.MessageContext
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.network.NetHandlerPlayServer
import net.minecraft.util.ChatComponentTranslation
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.item.equipment.bauble.ItemTravelBelt
import java.lang.reflect.Field

object PacketHandler {
	
	val fallBuffer = ASJReflectionHelper.getField(ItemTravelBelt::class.java, "fallBuffer") // FIXME transform access

	fun handle(packet: Message0d, ctx: MessageContext) {
		when (m0d.values()[packet.type]) {
			m0d.DODGE -> DOGIE(ctx.serverHandler)
			m0d.JUMP  -> jump(ctx.serverHandler.playerEntity)
		}
	}
	
	private fun DOGIE(sh: NetHandlerPlayServer) {
		val player = sh.playerEntity
		
		player.worldObj.playSoundAtEntity(player, "botania:dash", 1f, 1f)
		
		val baublesInv = PlayerHandler.getPlayerBaubles(player)
		var ringStack: ItemStack? = baublesInv.getStackInSlot(1)
		
		if (ringStack == null || ringStack.item !is ItemDodgeRing) {
			ringStack = baublesInv.getStackInSlot(2)
			if (ringStack == null || ringStack.item !is ItemDodgeRing) {
				sh.netManager.closeChannel(ChatComponentTranslation("alfheimmisc.invalidDodge"))
				return
			}
		}
		
		if (ItemNBTHelper.getInt(ringStack, ItemDodgeRing.TAG_DODGE_COOLDOWN, 0) > 0) {
			sh.netManager.closeChannel(ChatComponentTranslation("alfheimmisc.invalidDodge"))
			return
		}
		
		player.addExhaustion(0.3f)
		ItemNBTHelper.setInt(ringStack, ItemDodgeRing.TAG_DODGE_COOLDOWN, ItemDodgeRing.MAX_CD)
	}
	
	init {
		fallBuffer!!.isAccessible = true
	}
	
	private fun jump(player: EntityPlayerMP) {
		val baublesInv = PlayerHandler.getPlayerBaubles(player)
		val amuletStack = baublesInv.getStackInSlot(0)
		
		if (amuletStack != null && amuletStack.item is ItemCloudPendant) {
			player.addExhaustion(0.3f)
			player.fallDistance = 0f
			
			val belt = baublesInv.getStackInSlot(3)
			
			if (belt != null && belt.item is ItemTravelBelt) {
				val `val` = ASJReflectionHelper.getValue<Float>(fallBuffer, belt.item, false)!!
				player.fallDistance = -`val` * (amuletStack.item as ItemCloudPendant).maxAllowedJumps
			}
		}
	}
}