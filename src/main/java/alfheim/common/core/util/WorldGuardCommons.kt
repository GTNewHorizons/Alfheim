package alfheim.common.core.util

import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.world.*
import net.minecraftforge.common.ForgeHooks

object WorldGuardCommons {
	
	fun canDoSomethingHere(performer: EntityLivingBase) = canDoSomethingHere(performer, performer.posX, performer.posY, performer.posZ, performer.worldObj)
	
	fun canDoSomethingHere(performer: EntityLivingBase, x: Double, y: Double, z: Double, world: World = performer.worldObj) = canDoSomethingHere(performer, x.mfloor(), y.mfloor(), z.mfloor(), world)
	
	fun canDoSomethingHere(performer: EntityLivingBase, x: Int, y: Int, z: Int, world: World = performer.worldObj): Boolean {
		if (performer !is EntityPlayerMP) return true
		
		var gt = WorldSettings.GameType.SURVIVAL
		
		if (performer.capabilities.allowEdit) {
			if (performer.capabilities.isCreativeMode)
				gt = WorldSettings.GameType.CREATIVE
		} else
			gt = WorldSettings.GameType.ADVENTURE
		
		return !ForgeHooks.onBlockBreakEvent(world, gt, performer, x, y, z).isCanceled
	}
	
	fun canDoSomethingWithEntity(performer: EntityLivingBase, target: Entity, world: World = target.worldObj) = canDoSomethingHere(performer, target.posX.mfloor(), target.posY.mfloor(), target.posZ.mfloor(), world)
	
	fun canHurtEntity(attacker: EntityLivingBase, targer: EntityLivingBase) = canDoSomethingWithEntity(attacker, targer)
}