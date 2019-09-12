package alfheim.common.entity

import alexsocol.asjlib.ASJUtilities
import alfheim.common.item.AlfheimItems
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.relauncher.*
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.world.World
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.Botania
import vazkii.botania.common.entity.EntityFlyingCreature
import vazkii.botania.common.item.ModItems
import kotlin.math.*

class EntityAlfheimPixie(world: World): EntityFlyingCreature(world) {
	
	/** Coordinates of where the pixie spawned.  */
	private var spawnPosition: ChunkCoordinates? = null
	
	init {
		setSize(0.25f, 0.25f)
	}
	
	override fun applyEntityAttributes() {
		super.applyEntityAttributes()
		getEntityAttribute(SharedMonsterAttributes.maxHealth).baseValue = 4.0
	}
	
	override fun canBePushed() = false
	override fun collideWithEntity(entity: Entity) = Unit
	override fun collideWithNearbyEntities() = Unit
	override fun isAIEnabled(): Boolean = true
	override fun canTriggerWalking() = false
	override fun doesEntityNotTriggerPressurePlate() = true
	override fun getDropItem() = null
	
	override fun dropFewItems(hit: Boolean, looting: Int) {
		entityDropItem(ItemStack(ModItems.manaResource, 1, 8), 0.0f)
	}
	
	override fun updateEntityActionState() {
		rotationYaw = -atan2(motionX, motionZ).toFloat() * 180.0f / Math.PI.toFloat()
		renderYawOffset = rotationYaw
	}
	
	override fun onEntityUpdate() {
		if (worldObj.isRemote)
			for (i in 0..3)
				Botania.proxy.sparkleFX(worldObj, posX + (Math.random() - 0.5) * 0.25, posY + 0.5 + (Math.random() - 0.5) * 0.25, posZ + (Math.random() - 0.5) * 0.25, 1f, 0.25f, 0.9f, 0.1f + Math.random().toFloat() * 0.25f, 12)
		
		var player: EntityPlayer? = worldObj.getClosestPlayerToEntity(this, 64.0)
		if (player == null) {
			setDead()
			onDeath(DamageSource.outOfWorld)
		}
		
		player = ASJUtilities.getClosestVulnerablePlayerToEntity(this, 4.0)
		if (player != null && PlayerHandler.getPlayerBaubles(player) != null && PlayerHandler.getPlayerBaubles(player).getStackInSlot(0) != null && PlayerHandler.getPlayerBaubles(player).getStackInSlot(0).item === AlfheimItems.pixieAttractor && ManaItemHandler.requestManaExact(PlayerHandler.getPlayerBaubles(player).getStackInSlot(0), player, 1, true)) {
			val vec = player.getLook(1.0f)
			motionX = (player.posX + vec.xCoord - posX) / 8.0f
			motionY = (player.posY + vec.yCoord + 1.5 - posY) / 8.0f
			motionZ = (player.posZ + vec.zCoord - posZ) / 8.0f
			super.onEntityUpdate()
			return
		}
		
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
		return posY > 64 && 0 < worldObj.worldTime % 24000 && worldObj.worldTime % 24000 < 13333 || 22666 < worldObj.worldTime % 24000 && worldObj.getClosestPlayerToEntity(this, 64.0) != null && super.getCanSpawnHere()
	}
	
	@SideOnly(Side.CLIENT)
	override fun isInRangeToRenderDist(distance: Double): Boolean {
		return super.isInRangeToRenderDist(distance / 16.0)
	}
}
