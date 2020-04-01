package alfheim.common.security

import alexsocol.asjlib.mfloor
import alfheim.api.*
import alfheim.api.security.ISecurityManager
import alfheim.common.core.asm.AlfheimHookHandler
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
import net.minecraftforge.common.*
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
	override fun canDoSomethingHere(performer: EntityLivingBase, x: Double, y: Double, z: Double, world: World) = manager().canDoSomethingHere(performer, x, y, z, world)
	override fun canDoSomethingHere(performer: EntityLivingBase, x: Int, y: Int, z: Int, world: World) = manager().canDoSomethingHere(performer, x, y, z, world)
	override fun canDoSomethingWithEntity(performer: EntityLivingBase, target: Entity, world: World) = if (performer === target) true else manager().canDoSomethingWithEntity(performer, target, world)
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
		
		// fuck you worldguard
		AlfheimHookHandler.sendToClient = false
		return !ForgeHooks.onBlockBreakEvent(world, GameType.SURVIVAL, performer, x, y, z).isCanceled.also { world.markBlockForUpdate(x, y, z) }
	}
	
	override fun canDoSomethingWithEntity(performer: EntityLivingBase, target: Entity, world: World) = canDoSomethingHere(performer, target.posX.mfloor(), target.posY.mfloor(), target.posZ.mfloor(), world)
	override fun canHurtEntity(attacker: EntityLivingBase, target: EntityLivingBase) = canDoSomethingWithEntity(attacker, target)
}

private object MixedSecurityManager: ISecurityManager {
	
	override fun canDoSomethingHere(performer: EntityLivingBase) = BlockSecurityManager.canDoSomethingHere(performer)
	override fun canDoSomethingHere(performer: EntityLivingBase, x: Double, y: Double, z: Double, world: World) = BlockSecurityManager.canDoSomethingHere(performer, x, y, z, world)
	override fun canDoSomethingHere(performer: EntityLivingBase, x: Int, y: Int, z: Int, world: World) = BlockSecurityManager.canDoSomethingHere(performer, x, y, z, world)
	
	override fun canDoSomethingWithEntity(performer: EntityLivingBase, target: Entity, world: World) = if (MinecraftServer.getServer()?.isSinglePlayer == true) true else target.attackEntityFrom(DamageSource.causeMobDamage(performer), 0f)
	override fun canHurtEntity(attacker: EntityLivingBase, target: EntityLivingBase) = canDoSomethingWithEntity(attacker, target)
}