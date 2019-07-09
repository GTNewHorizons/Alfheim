package alfheim.common.entity

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.util.MathHelper
import net.minecraft.world.World

class EntityLolicorn(world: World) : EntityRidableFlying(world) {
	
	var mouthOpenness = 0f
	var prevMouthOpenness = 0f
	var rearingAmount = 0f
	var prevRearingAmount = 0f
	private var openMouthCounter = 0
	private var jumpRearingCounter = 0
	var tailMovement = 0
	var tugudukCounter = 0
	
	init {
		stepHeight = 1.5f
		flySpeed = 0.95f
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
		if (!worldObj.isRemote && !sup) makeHorseRearWithSound()
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
		
		if (openMouthCounter > 0 && ++openMouthCounter > 30) {
			openMouthCounter = 0
			setHorseWatchableBoolean(128, false)
		}
		
		if (!worldObj.isRemote && jumpRearingCounter > 0 && ++jumpRearingCounter > 20) {
			jumpRearingCounter = 0
			setRearing(false)
		}
		
		prevRearingAmount = rearingAmount
		if (isRearing()) {
			rearingAmount += (1.0f - rearingAmount) * 0.4f + 0.05f
			if (rearingAmount > 1.0f) rearingAmount = 1.0f
		} else {
			rearingAmount += (0.8f * rearingAmount * rearingAmount * rearingAmount - rearingAmount) * 0.6f - 0.05f
			if (rearingAmount < 0.0f) rearingAmount = 0.0f
		}
		
		prevMouthOpenness = mouthOpenness
		if (getHorseWatchableBoolean(128)) {
			mouthOpenness += (1.0f - mouthOpenness) * 0.7f + 0.05f
			if (mouthOpenness > 1.0f) mouthOpenness = 1.0f
		} else {
			mouthOpenness += (0.0f - mouthOpenness) * 0.7f - 0.05f
			if (mouthOpenness < 0.0f) mouthOpenness = 0.0f
		}
		
		if (tailMovement > 0 && ++tailMovement > 8) tailMovement = 0
	}
	
	override fun moveEntityWithHeading(mS: Float, mF: Float) {
		if (mF <= 0f) tugudukCounter = 0
		super.moveEntityWithHeading(mS, mF)
	}
														// TODO test remove
	override fun isMovementBlocked() = if (rider != null && !rider!!.isJumping) true else isRearing()
	
	private fun makeHorseRearWithSound() {
		makeHorseRear()
		playSound(getAngrySoundName(), soundVolume, soundPitch)
	}
	
	@Deprecated("NO-OP")
	private fun openMouth() {
		if (!worldObj.isRemote) {
			openMouthCounter = 1
			setHorseWatchableBoolean(128, true)
		}
	}
	
	private fun makeHorseRear() {
		if (!worldObj.isRemote) {
			jumpRearingCounter = 1
			setRearing(true)
		}
	}
	
	private fun setRearing(read: Boolean) = setHorseWatchableBoolean(64, read)
	
	private fun isRearing() = getHorseWatchableBoolean(64)
	
	override fun getLivingSound() =
		try {
			"mob.horse.idle"
		} finally {
			openMouth()
			if (rand.nextInt(10) == 0 && !isMovementBlocked) makeHorseRear()
		}
	
	private fun getAngrySoundName() =
		try {
			"mob.horse.angry"
		} finally {
			openMouth()
			makeHorseRear()
		}
	
	override fun getHurtSound() =
		try {
			"mob.horse.hit"
		} finally {
			openMouth()
			if (rand.nextInt(3) == 0) makeHorseRear()
		}
	
	override fun getDeathSound() =
		try {
			"mob.horse.death"
		} finally {
			openMouth()
		}
	
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
	
	private fun getHorseWatchableBoolean(index: Int) = dataWatcher.getWatchableObjectInt(16) and index != 0
	
	private fun setHorseWatchableBoolean(index: Int, value: Boolean) {
		val j = dataWatcher.getWatchableObjectInt(16)
		
		if (value) {
			dataWatcher.updateObject(16, (j or index))
		} else {
			dataWatcher.updateObject(16, (j and index.inv()))
		}
	}
	
	override fun updateRiderPosition() {
		super.updateRiderPosition()
		
		if (prevRearingAmount > 0.0f) {
			val f = MathHelper.sin(renderYawOffset * Math.PI.toFloat() / 180.0f)
			val f1 = MathHelper.cos(renderYawOffset * Math.PI.toFloat() / 180.0f)
			val f2 = 0.7f * prevRearingAmount
			val f3 = 0.15f * prevRearingAmount
			riddenByEntity.setPosition(posX + (f2 * f).toDouble(), posY + mountedYOffset + riddenByEntity.getYOffset() + f3.toDouble(), posZ - (f2 * f1).toDouble())
			
			if (riddenByEntity is EntityLivingBase) {
				(riddenByEntity as EntityLivingBase).renderYawOffset = renderYawOffset
			}
		}
	}
}