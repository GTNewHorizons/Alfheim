package alfheim.common.security

import alexsocol.asjlib.mfloor
import alfheim.api.*
import alfheim.api.security.ISecurityManager
import alfheim.common.core.handler.AlfheimConfigHandler
import cpw.mods.fml.relauncher.FMLRelaunchLog
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemSword
import net.minecraft.network.play.server.S23PacketBlockChange
import net.minecraft.server.MinecraftServer
import net.minecraft.util.DamageSource
import net.minecraft.world.World
import net.minecraft.world.WorldSettings.GameType
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.world.BlockEvent.BreakEvent

object InteractionSecurity: ISecurityManager {
	
	fun manager(): ISecurityManager =
		AlfheimAPI.securityManagers[AlfheimConfigHandler.interactionSecurity]?.also {
			if (it === this)
				throw IllegalArgumentException("General InteractionSecurity manager was set as custom security manager. This will cause recursive stack overflow")
		} ?:
			throw IllegalArgumentException("No security manager was found with name \"${AlfheimConfigHandler.interactionSecurity}\". Available managers are: ${AlfheimAPI.securityManagers.keys}")
	
	init {
		AlfheimAPI.registerSecurityManager(DefaultSecurityManager, "default")
		AlfheimAPI.registerSecurityManager(BlockSecurityManager, "block")
		AlfheimAPI.registerSecurityManager(MixedSecurityManager, "mixed")
		
		manager().also { FMLRelaunchLog.info("[${ModInfo.MODID.toUpperCase()}] Security manager is set to ${AlfheimConfigHandler.interactionSecurity}") }
	}
	
	override fun canDoSomethingHere(performer: EntityLivingBase) = manager().canDoSomethingHere(performer)
	override fun canDoSomethingHere(performer: EntityLivingBase, x: Double, y: Double, z: Double, world: World) = manager().canDoSomethingHere(performer, x, y, z)
	override fun canDoSomethingHere(performer: EntityLivingBase, x: Int, y: Int, z: Int, world: World) = manager().canDoSomethingHere(performer, x, y, z)
	override fun canDoSomethingWithEntity(performer: EntityLivingBase, target: Entity, world: World) = if (performer === target) true else manager().canDoSomethingWithEntity(performer, target)
	override fun canHurtEntity(attacker: EntityLivingBase, target: EntityLivingBase) = if (attacker === target) true else manager().canHurtEntity(attacker, target)
}

private object DefaultSecurityManager: ISecurityManager {
	override fun canDoSomethingHere(performer: EntityLivingBase) = true
	override fun canDoSomethingHere(performer: EntityLivingBase, x: Double, y: Double, z: Double, world: World) = true
	override fun canDoSomethingHere(performer: EntityLivingBase, x: Int, y: Int, z: Int, world: World) = true
	override fun canDoSomethingWithEntity(performer: EntityLivingBase, target: Entity, world: World) = true
	override fun canHurtEntity(attacker: EntityLivingBase, target: EntityLivingBase) = true
}

private object BlockSecurityManager: ISecurityManager {
	
	override fun canDoSomethingHere(performer: EntityLivingBase) = canDoSomethingHere(performer, performer.posX, performer.posY, performer.posZ, performer.worldObj)
	override fun canDoSomethingHere(performer: EntityLivingBase, x: Double, y: Double, z: Double, world: World) = canDoSomethingHere(performer, x.mfloor(), y.mfloor(), z.mfloor(), world)
	override fun canDoSomethingHere(performer: EntityLivingBase, x: Int, y: Int, z: Int, world: World): Boolean {
		if (performer !is EntityPlayerMP) return true
		if (MinecraftServer.getServer().isSinglePlayer) return true
		
		return !onBlockBreakEvent(world, GameType.SURVIVAL, performer, x, y, z).isCanceled.also { world.markBlockForUpdate(x, y, z) }
	}
	
	override fun canDoSomethingWithEntity(performer: EntityLivingBase, target: Entity, world: World) = canDoSomethingHere(performer, target.posX.mfloor(), target.posY.mfloor(), target.posZ.mfloor(), world)
	override fun canHurtEntity(attacker: EntityLivingBase, target: EntityLivingBase) = canDoSomethingWithEntity(attacker, target)
	
	// from ForgeHooks
	fun onBlockBreakEvent(world: World, gameType: GameType, entityPlayer: EntityPlayerMP, x: Int, y: Int, z: Int): BreakEvent {
		// Logic from tryHarvestBlock for pre-canceling the event
		var preCancelEvent = false
		if (gameType.isAdventure && !entityPlayer.isCurrentToolAdventureModeExempt(x, y, z)) {
			preCancelEvent = true
		} else if (gameType.isCreative && entityPlayer.heldItem != null && entityPlayer.heldItem.item is ItemSword) {
			preCancelEvent = true
		}
		
		// Post the block break event
		val block = world.getBlock(x, y, z)
		val blockMetadata = world.getBlockMetadata(x, y, z)
		val event = BreakEvent(x, y, z, world, block, blockMetadata, entityPlayer)
		event.isCanceled = preCancelEvent
		MinecraftForge.EVENT_BUS.post(event)
		
		// Handle if the event is canceled
		if (event.isCanceled) {
			// Let the client know the block still exists
			entityPlayer.playerNetServerHandler.sendPacket(S23PacketBlockChange(x, y, z, world))
			
			// Update any tile entity data for this block
			val tileentity = world.getTileEntity(x, y, z)
			if (tileentity != null) {
				val pkt = tileentity.descriptionPacket
				if (pkt != null) {
					entityPlayer.playerNetServerHandler.sendPacket(pkt)
				}
			}
		}
		
		return event
	}
}

private object MixedSecurityManager: ISecurityManager {
	
	override fun canDoSomethingHere(performer: EntityLivingBase) = BlockSecurityManager.canDoSomethingHere(performer)
	override fun canDoSomethingHere(performer: EntityLivingBase, x: Double, y: Double, z: Double, world: World) = BlockSecurityManager.canDoSomethingHere(performer, x, y, z)
	override fun canDoSomethingHere(performer: EntityLivingBase, x: Int, y: Int, z: Int, world: World) = BlockSecurityManager.canDoSomethingHere(performer, x, y, z)
	
	override fun canDoSomethingWithEntity(performer: EntityLivingBase, target: Entity, world: World) = if (MinecraftServer.getServer() == null || MinecraftServer.getServer().isSinglePlayer) true else target.attackEntityFrom(DamageSource.causeMobDamage(performer), 0f)
	override fun canHurtEntity(attacker: EntityLivingBase, target: EntityLivingBase) = canDoSomethingWithEntity(attacker, target)
}