package alexsocol.asjlib.security

import alexsocol.asjlib.*
import alexsocol.patcher.PatcherConfigHandler
import alexsocol.patcher.asm.ASJHookHandler
import cpw.mods.fml.relauncher.FMLRelaunchLog
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.server.MinecraftServer
import net.minecraft.util.DamageSource
import net.minecraft.world.World
import net.minecraft.world.WorldSettings.GameType
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingAttackEvent

/** Map of security managers by name */
val securityManagers = HashMap<String, ISecurityManager>()

fun registerSecurityManager(man: ISecurityManager, name: String) {
	if (name.isBlank()) throw IllegalArgumentException("Name should not be blank")
	if (securityManagers.containsKey(name)) throw IllegalArgumentException("Security Manager \"$name\" is already registered")
	
	ASJUtilities.log("Registering security manager with name \"$name\"")
	securityManagers[name] = man
}

object InteractionSecurity: ISecurityManager {
	
	fun manager(): ISecurityManager =
		securityManagers[PatcherConfigHandler.interactionSecurity]?.also {
			if (it === this)
				throw IllegalArgumentException("General InteractionSecurity manager was set as custom security manager. This will cause recursive stack overflow")
		} ?: throw IllegalArgumentException("No security manager was found with name \"${PatcherConfigHandler.interactionSecurity}\". Available managers are: ${securityManagers.keys}")
	
	init {
		registerSecurityManager(DefaultSecurityManager, "default")
		registerSecurityManager(BlockSecurityManager, "block")
		registerSecurityManager(MixedSecurityManager, "mixed")
		
		manager().also { FMLRelaunchLog.info("[ASJLib] Security manager is set to ${PatcherConfigHandler.interactionSecurity}") }
	}
	
	override fun canDoSomethingHere(performer: EntityLivingBase) = manager().canDoSomethingHere(performer)
	override fun canDoSomethingHere(performer: EntityLivingBase, x: Double, y: Double, z: Double, world: World) = manager().canDoSomethingHere(performer, x, y, z, world)
	override fun canDoSomethingHere(performer: EntityLivingBase, x: Int, y: Int, z: Int, world: World) = manager().canDoSomethingHere(performer, x, y, z, world)
	override fun canDoSomethingWithEntity(performer: EntityLivingBase, target: Entity) = if (performer === target) true else manager().canDoSomethingWithEntity(performer, target)
	override fun canHurtEntity(attacker: EntityLivingBase, target: EntityLivingBase) = if (attacker === target) true else manager().canHurtEntity(attacker, target)
}

private object DefaultSecurityManager: ISecurityManager {
	
	override fun canDoSomethingHere(performer: EntityLivingBase) = true
	override fun canDoSomethingHere(performer: EntityLivingBase, x: Double, y: Double, z: Double, world: World) = true
	override fun canDoSomethingHere(performer: EntityLivingBase, x: Int, y: Int, z: Int, world: World) = true
	override fun canDoSomethingWithEntity(performer: EntityLivingBase, target: Entity) = true
	override fun canHurtEntity(attacker: EntityLivingBase, target: EntityLivingBase) = true
}

private object BlockSecurityManager: ISecurityManager {
	
	override fun canDoSomethingHere(performer: EntityLivingBase) = canDoSomethingHere(performer, performer.posX, performer.posY, performer.posZ, performer.worldObj)
	override fun canDoSomethingHere(performer: EntityLivingBase, x: Double, y: Double, z: Double, world: World) = canDoSomethingHere(performer, x.mfloor(), y.mfloor(), z.mfloor(), world)
	override fun canDoSomethingHere(performer: EntityLivingBase, x: Int, y: Int, z: Int, world: World): Boolean {
		if (performer !is EntityPlayerMP) return true
		if (MinecraftServer.getServer().isSinglePlayer) return true
		
		// fuck you worldguard
		ASJHookHandler.sendToClient = false
		return !ForgeHooks.onBlockBreakEvent(world, GameType.SURVIVAL, performer, x, y, z).isCanceled.also { world.markBlockForUpdate(x, y, z) }
	}
	
	override fun canDoSomethingWithEntity(performer: EntityLivingBase, target: Entity) = canDoSomethingHere(performer, target.posX.mfloor(), target.posY.mfloor(), target.posZ.mfloor())
	override fun canHurtEntity(attacker: EntityLivingBase, target: EntityLivingBase) = canDoSomethingWithEntity(attacker, target)
}

private object MixedSecurityManager: ISecurityManager {
	
	override fun canDoSomethingHere(performer: EntityLivingBase) = BlockSecurityManager.canDoSomethingHere(performer)
	override fun canDoSomethingHere(performer: EntityLivingBase, x: Double, y: Double, z: Double, world: World) = BlockSecurityManager.canDoSomethingHere(performer, x, y, z, world)
	override fun canDoSomethingHere(performer: EntityLivingBase, x: Int, y: Int, z: Int, world: World) = BlockSecurityManager.canDoSomethingHere(performer, x, y, z, world)

//	override fun canDoSomethingWithEntity(performer: EntityLivingBase, target: Entity) = if (MinecraftServer.getServer()?.isSinglePlayer == true) true else target.attackEntityFrom(DamageSource.causeMobDamage(performer), 0f)
//	override fun canHurtEntity(attacker: EntityLivingBase, target: EntityLivingBase) = canDoSomethingWithEntity(attacker, target)
	
	override fun canDoSomethingWithEntity(performer: EntityLivingBase, target: Entity) = if (target is EntityLivingBase) canHurtEntity(performer, target) else true
	override fun canHurtEntity(attacker: EntityLivingBase, target: EntityLivingBase) = if (MinecraftServer.getServer()?.isSinglePlayer == true) true else !MinecraftForge.EVENT_BUS.post(LivingAttackEvent(target, DamageSource.causeMobDamage(attacker), 0f))

//	override fun canDoSomethingWithEntity(performer: EntityLivingBase, target: Entity, world: World) = when {
//		MinecraftServer.getServer()?.isSinglePlayer == true -> true
//		performer is EntityPlayer -> !MinecraftForge.EVENT_BUS.post(EntityInteractEvent(performer, target))
//		else -> true
//	}
}

interface ISecurityManager {
	
	fun canDoSomethingHere(performer: EntityLivingBase): Boolean
	fun canDoSomethingHere(performer: EntityLivingBase, x: Double, y: Double, z: Double, world: World = performer.worldObj): Boolean
	fun canDoSomethingHere(performer: EntityLivingBase, x: Int, y: Int, z: Int, world: World = performer.worldObj): Boolean
	fun canDoSomethingWithEntity(performer: EntityLivingBase, target: Entity): Boolean
	fun canHurtEntity(attacker: EntityLivingBase, target: EntityLivingBase): Boolean
}