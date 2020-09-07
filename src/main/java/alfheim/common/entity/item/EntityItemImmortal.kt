package alfheim.common.entity.item

import alexsocol.asjlib.*
import net.minecraft.block.material.Material
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.play.server.S0DPacketCollectItem
import net.minecraft.util.DamageSource
import net.minecraft.world.*

open class EntityItemImmortal: Entity {
	
	var age = 0
	var delayBeforeCanPickup = 30
	var hoverStart = (Math.random() * Math.PI * 2.0).F
	var lifespan = 6000
	
	constructor(world: World, origin: Entity, stack: ItemStack?): this(world, origin.posX, origin.posY, origin.posZ, stack) {
		motionX = origin.motionX
		motionY = origin.motionY
		motionZ = origin.motionZ
	}
	
	constructor(world: World, x: Double, y: Double, z: Double, stack: ItemStack?): this(world, x, y, z) {
		this.stack = stack
		lifespan = if (stack?.item == null) 6000 else stack.item.getEntityLifespan(stack, world)
	}
	
	constructor(world: World, x: Double, y: Double, z: Double): this(world) {
		setPosition(x, y, z)
	}
	
	constructor(world: World): super(world) {
		setSize(0.25f, 0.25f)
		yOffset = height / 2f

		rotationYaw = (Math.random() * 360.0).F
		isImmuneToFire = true
//		motionX = (Math.random() * 0.20000000298023224 - 0.10000000149011612)
//		motionY = 0.20000000298023224
//		motionZ = (Math.random() * 0.20000000298023224 - 0.10000000149011612)
	}
	
	override fun entityInit() {
		getDataWatcher().addObjectByDataType(10, 5)
	}
	
	override fun canTriggerWalking() = false
	
	override fun onUpdate() {
		val stack = stack
		
		if (stack?.item is IImmortalHandledItem) {
			if ((stack.item as IImmortalHandledItem).onEntityItemImmortalUpdate(this)) {
				return
			}
		}
		
		if (stack == null) {
			setDead()
		} else {
			super.onUpdate()
			
			if (delayBeforeCanPickup > 0) {
				--delayBeforeCanPickup
			}
			
			prevPosX = posX
			prevPosY = posY
			prevPosZ = posZ
			
			motionY -= 0.04
			
			noClip = func_145771_j(posX, (boundingBox.minY + boundingBox.maxY) / 2.0, posZ)
			
			moveEntity(motionX, motionY, motionZ)
			
			val flag = prevPosX.I != posX.I || prevPosY.I != posY.I || prevPosZ.I != posZ.I
			
			if (flag || ticksExisted % 25 == 0) {
				if (worldObj.getBlock(this).material === Material.lava) {
					motionY = 0.2
					motionX = ((rand.nextDouble() - rand.nextDouble()) * 0.2)
					motionZ = ((rand.nextDouble() - rand.nextDouble()) * 0.2)
					playSound("random.fizz", 0.4f, 2f + rand.nextFloat() * 0.4f)
				}
			}
			
			val f =
				if (onGround)
					worldObj.getBlock(this, y = -1).slipperiness * 0.98f
				else
					0.98f
			
			motionX *= f.D
			motionY *= 0.98
			motionZ *= f.D
			
			if (onGround) {
				motionY *= -0.5
			}
			
			++age
			
			val item = getDataWatcher().getWatchableObjectItemStack(10)
			
			if (!worldObj.isRemote && age >= lifespan && item == null)
				setDead()
			
			if (item != null && item.stackSize <= 0)
				setDead()
		}
	}
	
	override fun handleWaterMovement() =
		worldObj.handleMaterialAcceleration(boundingBox, Material.water, this)
	
	override fun dealFireDamage(dmg: Int) = Unit
	
	override fun attackEntityFrom(src: DamageSource, amout: Float) = false
	
	override fun canAttackWithItem() = false
	
	override fun isEntityInvulnerable() = true
	
	override fun setFire(seconds: Int) = Unit
	
	override fun writeEntityToNBT(nbt: NBTTagCompound) {
		nbt.setInteger("Age", age)
		nbt.setInteger("Lifespan", lifespan)
		
		stack?.let { nbt.setTag("Item", it.writeToNBT(NBTTagCompound())) }
	}
	
	override fun readEntityFromNBT(nbt: NBTTagCompound) {
		age = nbt.getInteger("Age")
		if (nbt.hasKey("Lifespan")) {
			lifespan = nbt.getInteger("Lifespan")
		}
		
		val itemNBT = nbt.getCompoundTag("Item")
		stack = ItemStack.loadItemStackFromNBT(itemNBT)
		
		val item = getDataWatcher().getWatchableObjectItemStack(10)
		if (item == null || item.stackSize <= 0) {
			setDead()
		}
	}
	
	override fun onCollideWithPlayer(player: EntityPlayer) {
		if (!worldObj.isRemote) {
			if (delayBeforeCanPickup > 0)
				return
			
			if (!canBePickedByPlayer(player))
				return
			
			val itemstack = stack ?: return
			
			val i = itemstack.stackSize
			
			if (delayBeforeCanPickup <= 0 /*&& lifespan - age <= 200*/ && (i <= 0 || player.inventory.addItemStackToInventory(itemstack))) {
				player.playSoundAtEntity("random.pop", 0.2f, ((rand.nextFloat() - rand.nextFloat()) * 0.7f + 1f) * 2f)
				
				if (!worldObj.isRemote) {
					val entitytracker = (worldObj as WorldServer).entityTracker
					entitytracker.func_151247_a(this, S0DPacketCollectItem(entityId, player.entityId))
				}
				
				if (itemstack.stackSize <= 0) {
					setDead()
				}
			}
		}
	}
	
	open fun canBePickedByPlayer(player: EntityPlayer) = true
	
	override fun getCommandSenderName(): String {
		return stack?.displayName ?: "-null-"
	}
	
	var stack: ItemStack?
		get() = getDataWatcher().getWatchableObjectItemStack(10)
		set(stack) {
			getDataWatcher().updateObject(10, stack)
			getDataWatcher().setObjectWatched(10)
		}
}