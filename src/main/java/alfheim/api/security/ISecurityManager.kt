package alfheim.api.security

import net.minecraft.entity.*
import net.minecraft.world.World

interface ISecurityManager {
	fun canDoSomethingHere(performer: EntityLivingBase): Boolean
	fun canDoSomethingHere(performer: EntityLivingBase, x: Double, y: Double, z: Double, world: World = performer.worldObj): Boolean
	fun canDoSomethingHere(performer: EntityLivingBase, x: Int, y: Int, z: Int, world: World = performer.worldObj): Boolean
	fun canDoSomethingWithEntity(performer: EntityLivingBase, target: Entity, world: World = target.worldObj): Boolean
	fun canHurtEntity(attacker: EntityLivingBase, target: EntityLivingBase): Boolean
}