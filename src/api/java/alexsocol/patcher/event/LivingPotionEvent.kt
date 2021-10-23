package alexsocol.patcher.event

import cpw.mods.fml.common.eventhandler.Cancelable
import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.PotionEffect
import net.minecraftforge.event.entity.living.LivingEvent

abstract class LivingPotionEvent(entity: EntityLivingBase, val effect: PotionEffect): LivingEvent(entity) {
	
	abstract class Add(entity: EntityLivingBase, pe: PotionEffect): LivingPotionEvent(entity, pe) {
		
		@Cancelable
		@Deprecated("Unimplemented", level = DeprecationLevel.ERROR)
		class Pre(entity: EntityLivingBase, pe: PotionEffect): Add(entity, pe)
		
		class Post(entity: EntityLivingBase, pe: PotionEffect): Add(entity, pe)
	}
	
	abstract class Change(entity: EntityLivingBase, pe: PotionEffect,
	                      /** Remove and add potion modifiers  */
	                      val update: Boolean): LivingPotionEvent(entity, pe) {
		
		@Cancelable
		@Deprecated("Unimplemented", level = DeprecationLevel.ERROR)
		class Pre(entity: EntityLivingBase, pe: PotionEffect, update: Boolean): Change(entity, pe, update)
		
		class Post(entity: EntityLivingBase, pe: PotionEffect, update: Boolean): Change(entity, pe, update)
	}
	
	abstract class Remove(entity: EntityLivingBase, pe: PotionEffect): LivingPotionEvent(entity, pe) {
		
		@Cancelable
		@Deprecated("Unimplemented", level = DeprecationLevel.ERROR)
		class Pre(entity: EntityLivingBase, pe: PotionEffect): Remove(entity, pe)
		
		class Post(entity: EntityLivingBase, pe: PotionEffect): Remove(entity, pe)
	}
}
