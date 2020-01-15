package alfmod.common.entity

import alfheim.common.core.util.mfloor
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingDeathEvent
import vazkii.botania.common.Botania
import vazkii.botania.common.entity.EntityFlyingCreature
import kotlin.math.*

class EntitySnowSprite(world: World): EntityFlyingCreature(world) {

	/** Coordinates of where the pixie spawned.  */
	private var spawnPosition: ChunkCoordinates? = null
	
	init {
		setSize(0.25f, 0.25f)
	}
	
	override fun applyEntityAttributes() {
		super.applyEntityAttributes()
		
		getEntityAttribute(SharedMonsterAttributes.maxHealth).baseValue = 8.0
	}
	
	override fun updateEntityActionState() {
		super.updateEntityActionState()
		
		rotationYaw = (-atan2(motionX, motionZ) * 180 / Math.PI).toFloat()
		renderYawOffset = rotationYaw
	}
	
	override fun canBePushed() = true
	override fun collideWithEntity(entity: Entity) = Unit
	override fun collideWithNearbyEntities() = Unit
	override fun isAIEnabled(): Boolean = true
	override fun canTriggerWalking() = false
	override fun doesEntityNotTriggerPressurePlate() = true
	override fun getDropItem() = Items.snowball!!
	
	override fun dropFewItems(hit: Boolean, looting: Int) {
		entityDropItem(ItemStack(dropItem, looting + 1), 0.0f)
	}
	
	override fun onEntityUpdate() {
		if (worldObj.isRemote && ticksExisted % 5 == 0)
			Botania.proxy.sparkleFX(worldObj, posX + (Math.random() - 0.5) * 0.25, posY + 0.5 + (Math.random() - 0.5) * 0.25, posZ + (Math.random() - 0.5) * 0.25, (Math.random() * 0.25 + 0.65).toFloat(), 1f, 1f, 1f + Math.random().toFloat() * 0.25f, 10)
		
		motionY *= 0.6
		if (worldObj.rand.nextInt(600) == 0) motionY -= 5.0
		
		super.onEntityUpdate()
	}
	
	override fun updateAITasks() {
		//super.updateAITasks();
		if (spawnPosition != null && (!worldObj.isAirBlock(spawnPosition!!.posX, spawnPosition!!.posY, spawnPosition!!.posZ) || spawnPosition!!.posY < 1)) {
			spawnPosition = null
		}
		
		if (spawnPosition == null || rand.nextInt(30) == 0 || spawnPosition!!.getDistanceSquared(posX.toInt(), posY.toInt(), posZ.toInt()) < 4.0f) {
			spawnPosition = ChunkCoordinates(posX.toInt() + rand.nextInt(7) - rand.nextInt(7), posY.toInt() + rand.nextInt(6) - 2, posZ.toInt() + rand.nextInt(7) - rand.nextInt(7))
		}
		
		val d0 = spawnPosition!!.posX.toDouble() + 0.5 - posX
		val d1 = spawnPosition!!.posY.toDouble() + 0.1 - posY
		val d2 = spawnPosition!!.posZ.toDouble() + 0.5 - posZ
		motionX += (sign(d0) * 0.5 - motionX) * 0.1
		motionY += (sign(d1) * 0.7 - motionY) * 0.1
		motionZ += (sign(d2) * 0.5 - motionZ) * 0.1
		val f = (atan2(motionZ, motionX) * 180.0 / Math.PI).toFloat() - 90.0f
		val f1 = MathHelper.wrapAngleTo180_float(f - rotationYaw)
		moveForward = 0.5f
		rotationYaw += f1
	}
	
	override fun setDead() {
		dead = true
		isDead = dead
		if (worldObj.isRemote)
			for (i in 0..11)
				Botania.proxy.sparkleFX(worldObj, posX + (Math.random() - 0.5) * 0.25, posY + 0.5 + (Math.random() - 0.5) * 0.25, posZ + (Math.random() - 0.5) * 0.25, 1f, 0.25f, 0.9f, 1f + Math.random().toFloat() * 0.25f, 5)
	}
	
	override fun getCanSpawnHere(): Boolean {
		return posY > 64 && worldObj.worldTime % 24000 in 13333..22666 && /*worldObj.getClosestPlayerToEntity(this, 64.0) != null && */ super.getCanSpawnHere()
	}
	
	@SideOnly(Side.CLIENT)
	override fun isInRangeToRenderDist(distance: Double): Boolean {
		return super.isInRangeToRenderDist(distance / 16.0)
	}
}

object SpriteKillhandler {
	
	val regions = HashMap<Pair<Int, Int>, Int>()
	
	init {
		MinecraftForge.EVENT_BUS.register(this)
	}
	
	@SubscribeEvent
	fun onSpriteKilled(e: LivingDeathEvent) {
		val sprite = e.entityLiving as? EntitySnowSprite ?: return
		val killer = e.source.entity as? EntityPlayer ?: return
		
		val pointer = (sprite.posX.mfloor() % 64) to (sprite.posZ.mfloor() % 64)
		val kills = regions.getOrDefault(pointer, 0) + 1
		
		if (kills >= 16) {
			val ded = EntityDedMoroz(sprite.worldObj, sprite.posX, sprite.posY, sprite.posZ)
			sprite.worldObj.spawnEntityInWorld(ded)
			
			ded.attackTarget = killer
			regions[pointer] = 0
		} else {
			regions[pointer] = kills
		}
	}
}