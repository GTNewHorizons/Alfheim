package alfmod.common.entity

import net.minecraft.entity.*
import net.minecraft.entity.ai.EntityAIPanic
import net.minecraft.init.Items
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.DamageSource
import net.minecraft.world.World

class EntityRollingMelon(world: World): EntityCreature(world) {
	
	var isLava: Boolean
		get() = dataWatcher.getWatchableObjectInt(12) != 0
		set(lava) = dataWatcher.updateObject(12, if (lava) 1 else 0)
	
	// in radians
	var rotation: Float
		get() = dataWatcher.getWatchableObjectFloat(13)
		set(rot) = dataWatcher.updateObject(13, rot)
	
	init {
		setSize(0.9f, 0.9f)
		tasks.addTask(1, EntityAIPanic(this, 2.0))
	}
	
	override fun applyEntityAttributes() {
		super.applyEntityAttributes()
		getEntityAttribute(SharedMonsterAttributes.maxHealth).baseValue = 8.0
	}
	
	override fun entityInit() {
		super.entityInit()
		dataWatcher.addObject(12, 0)
		dataWatcher.addObject(13, 0f)
	}
	
	override fun attackEntityFrom(source: DamageSource?, amount: Float): Boolean {
		val attacker = source?.entity as? EntityLivingBase ?: return false
		if (attacker.heldItem?.item !== Items.wooden_sword) return false
		if (isLava) {
			attacker.attackEntityFrom(DamageSource.causeMobDamage(this).setFireDamage(), 2f)
			attacker.setFire(rand.nextInt(5) + 5)
		}
		return super.attackEntityFrom(source, amount)
	}
	
	override fun writeEntityToNBT(nbt: NBTTagCompound) {
		super.writeEntityToNBT(nbt)
		nbt.setBoolean(TAG_LAVA, isLava)
		nbt.setFloat(TAG_ROTATION, rotation)
	}
	
	override fun readEntityFromNBT(nbt: NBTTagCompound) {
		super.readEntityFromNBT(nbt)
		isLava = nbt.getBoolean(TAG_LAVA)
		rotation = nbt.getFloat(TAG_ROTATION)
	}
	
	companion object {
		const val TAG_LAVA = "isLava"
		const val TAG_ROTATION = "rotation"
	}
}
