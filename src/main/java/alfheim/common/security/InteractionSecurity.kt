package alfheim.common.security

import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.mfloor
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.DamageSource
import net.minecraft.world.*
import net.minecraftforge.common.ForgeHooks

interface ISecurityManager {
	fun canDoSomethingHere(performer: EntityLivingBase): Boolean
	fun canDoSomethingHere(performer: EntityLivingBase, x: Double, y: Double, z: Double, world: World = performer.worldObj): Boolean
	fun canDoSomethingHere(performer: EntityLivingBase, x: Int, y: Int, z: Int, world: World = performer.worldObj): Boolean
	fun canDoSomethingWithEntity(performer: EntityLivingBase, target: Entity, world: World = target.worldObj): Boolean
	fun canHurtEntity(attacker: EntityLivingBase, target: EntityLivingBase): Boolean
}

object InteractionSecurity: ISecurityManager {
	
	val manager: ISecurityManager
		get() = when(AlfheimConfigHandler.interactionSecurity) {
		"block" -> BlockSecurityManager
		"mixed" -> MixedSecurityManager
		else -> DefaultSecurityManager
	}
	
	override fun canDoSomethingHere(performer: EntityLivingBase) = manager.canDoSomethingHere(performer)
	override fun canDoSomethingHere(performer: EntityLivingBase, x: Double, y: Double, z: Double, world: World) = manager.canDoSomethingHere(performer, x, y, z)
	override fun canDoSomethingHere(performer: EntityLivingBase, x: Int, y: Int, z: Int, world: World) = manager.canDoSomethingHere(performer, x, y, z)
	override fun canDoSomethingWithEntity(performer: EntityLivingBase, target: Entity, world: World) = manager.canDoSomethingWithEntity(performer, target)
	override fun canHurtEntity(attacker: EntityLivingBase, target: EntityLivingBase) = manager.canHurtEntity(attacker, target)
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
		
		var gt = WorldSettings.GameType.SURVIVAL
		
		if (performer.capabilities.allowEdit) {
			if (performer.capabilities.isCreativeMode)
				gt = WorldSettings.GameType.CREATIVE
		} else
			gt = WorldSettings.GameType.ADVENTURE
		
		return !ForgeHooks.onBlockBreakEvent(world, gt, performer, x, y, z).isCanceled
	}
	
	override fun canDoSomethingWithEntity(performer: EntityLivingBase, target: Entity, world: World) = canDoSomethingHere(performer, target.posX.mfloor(), target.posY.mfloor(), target.posZ.mfloor(), world)
	override fun canHurtEntity(attacker: EntityLivingBase, target: EntityLivingBase) = canDoSomethingWithEntity(attacker, target)
}

private object MixedSecurityManager: ISecurityManager {
	
	override fun canDoSomethingHere(performer: EntityLivingBase) = BlockSecurityManager.canDoSomethingHere(performer)
	override fun canDoSomethingHere(performer: EntityLivingBase, x: Double, y: Double, z: Double, world: World) = BlockSecurityManager.canDoSomethingHere(performer, x, y, z)
	override fun canDoSomethingHere(performer: EntityLivingBase, x: Int, y: Int, z: Int, world: World) = BlockSecurityManager.canDoSomethingHere(performer, x, y, z)
	
	override fun canDoSomethingWithEntity(performer: EntityLivingBase, target: Entity, world: World) = target.attackEntityFrom(DamageSource.causeMobDamage(performer), 0f)
	override fun canHurtEntity(attacker: EntityLivingBase, target: EntityLivingBase) = canDoSomethingWithEntity(attacker, target)
}