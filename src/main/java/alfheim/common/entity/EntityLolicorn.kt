package alfheim.common.entity

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

class EntityLolicorn(world: World) : EntityRidableFlying(world) {
	
	var mouthOpenness = 0f
	var prevMouthOpenness = 0f
	var rearingAmount = 0f
	var prevRearingAmount = 0f
	private var openMouthCounter = 0
	private var jumpRearingCounter = 0
	var tailMovement = 0
	
	override fun interact(player: EntityPlayer?): Boolean {
		val sup = super.interact(player)
		if (!worldObj.isRemote && !sup) makeHorseRearWithSound()
		return sup
	}
	
	override fun entityInit() {
		super.entityInit()
		dataWatcher.addObject(16, 0)
	}
	
	override fun onLivingUpdate() {
		if (rand.nextInt(200) == 0) {
			tailMovement = 1
		}
		super.onLivingUpdate()
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
	
	override fun isMovementBlocked() = if (this.riddenByEntity != null) true else isRearing()
	
	private fun makeHorseRearWithSound() {
		makeHorseRear()
		playSound(getAngrySoundName(), soundVolume, soundPitch)
	}
	
	private fun openHorseMouth() {
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
	
	private fun isRearing() = this.getHorseWatchableBoolean(64)
	
	override fun getLivingSound() =
		try {
			"mob.horse.idle"
		} finally {
			openHorseMouth()
			if (rand.nextInt(10) == 0 && !isMovementBlocked) makeHorseRear()	
		}
	
	private fun getAngrySoundName() =
		try {
			"mob.horse.angry"
		} finally {
			openHorseMouth()
			makeHorseRear()
		}
	
	override fun getHurtSound() =
		try {
			"mob.horse.hit"
		} finally {
			openHorseMouth()
			if (rand.nextInt(3) == 0) makeHorseRear()
		}
	
	override fun getDeathSound() =
		try {
			"mob.horse.death"
		} finally {
			openHorseMouth()
		}
	
	private fun getHorseWatchableBoolean(index: Int): Boolean {
		return dataWatcher.getWatchableObjectInt(16) and index != 0
	}
	
	private fun setHorseWatchableBoolean(index: Int, value: Boolean) {
		val j = dataWatcher.getWatchableObjectInt(16)
		
		if (value) {
			dataWatcher.updateObject(16, (j or index))
		} else {
			dataWatcher.updateObject(16, (j and index.inv()))
		}
	}
}