package alfheim.common.entity

import alexsocol.asjlib.math.Vector3
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.util.MathHelper
import net.minecraft.world.World

class EntityLolicorn(world: World) : EntityRidableFlying(world) {
	
	var tailMovement = 0
	var tugudukCounter = 0
	
	init {
		stepHeight = 1.5f
		flySpeed = 0.95f
		
		setSize(1.4f, 1.6f)
	}
	
	override fun setHealth(hp: Float) = Unit // NO-OP
	
	override fun fall(f: Float) {
		if (f > 1.0f) {
			playSound("mob.horse.land", 0.4f, 1.0f)
		}
		
		val i = MathHelper.ceiling_float_int(f * 0.5f - 3.0f)
		
		if (i > 0) {
			val block = worldObj.getBlock(MathHelper.floor_double(posX), MathHelper.floor_double(posY - 0.2 - prevRotationYaw.toDouble()), MathHelper.floor_double(posZ))
			
			if (block.material !== Material.air) {
				val soundtype = block.stepSound
				worldObj.playSoundAtEntity(this, soundtype.stepResourcePath, soundtype.getVolume() * 0.5f, soundtype.pitch * 0.75f)
			}
		}
	}
	
	override fun interact(player: EntityPlayer?): Boolean {
		val sup = super.interact(player)
		if (!worldObj.isRemote && !sup) playSound(getAngrySoundName(), soundVolume, soundPitch)
		return sup
	}
	
	override fun entityInit() {
		super.entityInit()
		dataWatcher.addObject(16, 0)
	}
	
	override fun applyEntityAttributes() {
		super.applyEntityAttributes()
		getEntityAttribute(SharedMonsterAttributes.maxHealth).baseValue = 2.0
		dataWatcher.updateObject(6, 2f)
	}
	
	override fun onLivingUpdate() {
		if (rand.nextInt(200) == 0) {
			tailMovement = 1
		}
		
		super.onLivingUpdate()
		
		if (rider == null && posY < -1000) setDead()
	}
	
	override fun onUpdate() {
		super.onUpdate()
		
		if (tailMovement > 0 && ++tailMovement > 8) tailMovement = 0
	}
	
	override fun moveEntityWithHeading(mS: Float, mF: Float) {
		if (mF <= 0f) tugudukCounter = 0
		super.moveEntityWithHeading(mS, mF)
	}
													 // TODO test remove
	override fun isMovementBlocked() = rider != null && !rider!!.isJumping
	
	override fun getLivingSound() = "mob.horse.idle"
	
	private fun getAngrySoundName() = "mob.horse.angry"
	
	override fun getHurtSound() = "mob.horse.hit"
	
	override fun getDeathSound() = "mob.horse.death"
	
	override fun func_145780_a(x: Int, y: Int, z: Int, block: Block) {
		var soundtype: Block.SoundType = block.stepSound
		
		if (worldObj.getBlock(x, y + 1, z) === Blocks.snow_layer) {
			soundtype = Blocks.snow_layer.stepSound
		}
		
		if (!block.material.isLiquid) {
			
			if (riddenByEntity != null) {
				++tugudukCounter
				
				if (tugudukCounter > 5 && tugudukCounter % 3 == 0) {
					playSound("mob.horse.gallop", soundtype.getVolume() * 0.15f, soundtype.pitch)
					
					if (rand.nextInt(10) == 0) {
						playSound("mob.horse.breathe", soundtype.getVolume() * 0.6f, soundtype.pitch)
					}
				} else if (tugudukCounter <= 5) {
					playSound("mob.horse.wood", soundtype.getVolume() * 0.15f, soundtype.pitch)
				}
			} else if (soundtype === Block.soundTypeWood) {
				playSound("mob.horse.wood", soundtype.getVolume() * 0.15f, soundtype.pitch)
			} else {
				playSound("mob.horse.soft", soundtype.getVolume() * 0.15f, soundtype.pitch)
			}
		}
	}
	
	var look = Vector3()
	
	override fun updateRiderPosition() {
		if (riddenByEntity != null) {
			look.set(lookVec).mul(1.0, 0.0, 1.0).normalize().mul(-0.25)
			riddenByEntity.setPosition(posX + look.x, posY + mountedYOffset + riddenByEntity.getYOffset(), posZ + look.z)
		}
	}
}