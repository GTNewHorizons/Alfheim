package alfheim.common.entity

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.extendables.EntityRidableFlying
import alexsocol.asjlib.math.Vector3
import alfheim.api.spell.ITimeStopSpecific
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.mfloor
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.*
import net.minecraft.entity.ai.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.*
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.potion.*
import net.minecraft.server.MinecraftServer
import net.minecraft.util.*
import net.minecraft.world.World
import vazkii.botania.api.mana.ManaItemHandler
import java.util.*

class EntityLolicorn(world: World): EntityRidableFlying(world), ITimeStopSpecific {
	
	val fallbackUUID = UUID(0, 0)
	override val isImmune = false
	
	override fun affectedBy(uuid: UUID) = ownerUUID != uuid
	
	var tailMovement = 0
	var tugudukCounter = 0
	var unmountCounter = 0
	
	var owner: String
		get() = dataWatcher.getWatchableObjectString(15)
		set(owner) = dataWatcher.updateObject(15, owner)
	
	var ownerUUID: UUID
		get() {
			val name = dataWatcher.getWatchableObjectString(16)
			return if (name.isNotBlank()) UUID.fromString(name) else fallbackUUID
		}
		set(uuid) = dataWatcher.updateObject(16, "$uuid")
	
	init {
		stepHeight = 1.5f
		flySpeed = 0.95f
		
		setSize(1.4f, 1.6f)
		
		navigator.avoidsWater = true
		tasks.addTask(0, EntityAISwimming(this))
		tasks.addTask(1, EntityAIWander(this, 1.0))
		tasks.addTask(2, EntityAIWatchClosest(this, EntityPlayer::class.java, 6f))
		tasks.addTask(3, EntityAILookIdle(this))
	}
	
	override fun entityInit() {
		super.entityInit()
		dataWatcher.addObject(15, "")
		dataWatcher.addObject(16, "")
	}
	
	override fun isAIEnabled() = true
	
	override fun attackEntityFrom(src: DamageSource, dmg: Float): Boolean {
		if (src.entity is EntityLivingBase && (src.entity as EntityLivingBase).heldItem?.item === Items.slime_ball) {
			addPotionEffect(PotionEffect(Potion.moveSlowdown.id, 50, 4))
			rider?.addPotionEffect(PotionEffect(Potion.confusion.id, 100, 0))
		}
		
		return false
	}
	
	override fun setHealth(hp: Float) = Unit // NO-OP
	
	override fun fall(f: Float) {
		if (f > 1f) {
			playSound("mob.horse.land", 0.4f, 1f)
		}
		
		val i = MathHelper.ceiling_float_int(f * 0.5f - 3f)
		
		if (i > 0) {
			val block = worldObj.getBlock(posX.mfloor(), (posY - 0.2 - prevRotationYaw).mfloor(), posZ.mfloor())
			
			if (block.material !== Material.air) {
				val soundtype = block.stepSound
				worldObj.playSoundAtEntity(this, soundtype.stepResourcePath, soundtype.getVolume() * 0.5f, soundtype.pitch * 0.75f)
			}
		}
	}
	
	override fun interact(player: EntityPlayer?): Boolean {
		var sup = false
		try {
			if (player == null) return sup
			if (owner.isNotEmpty() && player.commandSenderName != owner) {
				ASJUtilities.say(player, "Owned by $owner")
				return sup
			}
			
			sup = super.interact(player)
			return sup
		} finally {
			if (!worldObj.isRemote && !sup) playSound(getAngrySoundName(), soundVolume, soundPitch)
		}
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
		
		if (rider == null && posY < -256) setDead()
	}
	
	override fun onUpdate() {
		if (ASJUtilities.isServer && rider == null && riddenByEntity == null) doTPorDIE()
		
		super.onUpdate()
		
		if (tailMovement > 0 && ++tailMovement > 8) tailMovement = 0
	}
	
	fun doTPorDIE() {
		val master = MinecraftServer.getServer()?.configurationManager?.func_152612_a(owner) ?: run { setDead(); return }
		if (master.dimension != dimension) { setDead(); return }
		if (requests.remove(owner))
			master.also {
				setPosition(it.posX, it.posY, it.posZ)
				unmountCounter = 0
			}
		else
			if (Vector3.entityDistancePlane(this, master) > 32.0) setDead()
			else {
				++unmountCounter
				if (unmountCounter >= AlfheimConfigHandler.lolicornLife) setDead()
			}
	}
	
	override fun moveEntityWithHeading(mS: Float, mF: Float) {
		if (mF <= 0f) tugudukCounter = 0
		super.moveEntityWithHeading(mS, mF)
		
		if (mF > 0f)
			for (i in 1..8)
				worldObj.spawnParticle("reddust",
									   posX + Math.random() - 0.5,
									   posY + Math.random() - 0.5,
									   posZ + Math.random() - 0.5,
									   if (Math.random() < 0.5) 0.0 else 1.0,
									   if (Math.random() < 0.5) 0.0 else 1.0,
									   if (Math.random() < 0.5) 0.0 else 1.0
				)
	}
	
	override fun isMovementBlocked() = rider != null && !rider!!.isJumping
	
	override fun playLivingSound() {
		if (rng.nextInt(8) == 1) super.playLivingSound()
	}
	
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
	
	override fun mount(player: EntityPlayer) {
		super.mount(player)
		unmountCounter = 0
		
		owner = player.commandSenderName
		ownerUUID = player.uniqueID
		customNameTag = StatCollector.translateToLocalFormatted("entity.alfheim:Lolicorn.desc", owner)
	}
	
	var look = Vector3()
	
	override fun updateRiderPosition() {
		if (riddenByEntity != null) {
			look.set(lookVec).mul(1.0, 0.0, 1.0).normalize().mul(-0.25)
			riddenByEntity.setPosition(posX + look.x, posY + mountedYOffset + riddenByEntity.getYOffset(), posZ + look.z)
		}
	}
	
	override fun writeEntityToNBT(nbt: NBTTagCompound) {
		super.writeEntityToNBT(nbt)
		nbt.setString("Owner", owner)
		nbt.setString("OwnerUUID", "$ownerUUID")
	}
	
	override fun readEntityFromNBT(nbt: NBTTagCompound) {
		super.readEntityFromNBT(nbt)
		owner = nbt.getString("Owner")
		ownerUUID = UUID.fromString(nbt.getString("OwnerUUID"))
	}
	
	companion object {
		val requests = mutableSetOf<String>()
		val timing = mutableMapOf<String, Int>()
		
		fun call(caller: EntityPlayer) {
			if (caller.dimension != AlfheimConfigHandler.dimensionIDAlfheim && AlfheimConfigHandler.lolicornAlfheimOnly)
				ASJUtilities.say(caller, "alfheimmisc.mount.unavailable").also { return }
			
			if (caller.ridingEntity is EntityLolicorn) return
			
			requests.add(caller.commandSenderName)
			timing[caller.commandSenderName] = 5
		}
		
		fun tick() {
			val i = requests.iterator()
			while(i.hasNext()) {
				val cr = i.next()
				timing[cr] = timing[cr]!!-1
				if (timing[cr]!! <= 0) {
					
					MinecraftServer.getServer()?.configurationManager?.func_152612_a(cr)?.let { player ->
						var can = ManaItemHandler.requestManaExact(ItemStack(Blocks.stone), player, AlfheimConfigHandler.lolicornCost, false)
						if (!can) ASJUtilities.say(player, "alfheimmisc.cast.momana").also { return@let }
						if (can) can = player.worldObj.spawnEntityInWorld(EntityLolicorn(player.worldObj).apply { owner = player.commandSenderName }.apply { setPosition(player.posX, player.posY, player.posZ) })
						if (!can) ASJUtilities.say(player, "alfheimmisc.mount.unavailable").also { return@let }
						if (can) ManaItemHandler.requestManaExact(ItemStack(Blocks.stone), player, AlfheimConfigHandler.lolicornCost, true)
					}
					
					i.remove()
					timing.remove(cr)
				}
			}
		}
	}
}