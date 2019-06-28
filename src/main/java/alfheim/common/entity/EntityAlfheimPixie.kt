package alfheim.common.entity

import alexsocol.asjlib.ASJUtilities
import alfheim.common.core.registry.AlfheimItems
import baubles.api.BaublesApi
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.relauncher.*
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.*
import net.minecraft.world.World
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.Botania
import vazkii.botania.common.entity.EntityFlyingCreature
import vazkii.botania.common.item.ModItems

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
	
	override fun canBePushed(): Boolean {
		return false
	}
	
	public override fun collideWithEntity(p_82167_1_: Entity) {}
	
	public override fun collideWithNearbyEntities() {}
	
	override fun isAIEnabled(): Boolean {
		return true
	}
	
	override fun canTriggerWalking(): Boolean {
		return false
	}
	
	override fun doesEntityNotTriggerPressurePlate(): Boolean {
		return true
	}
	
	override fun getDropItem(): Item? {
		return null
	}
	
	override fun dropFewItems(hit: Boolean, looting: Int) {
		this.entityDropItem(ItemStack(ModItems.manaResource, 1, 8), 0.0f)
	}
	
	override fun updateEntityActionState() {
		rotationYaw = -Math.atan2(motionX, motionZ).toFloat() * 180.0f / Math.PI.toFloat()
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
		if (player != null && PlayerHandler.getPlayerBaubles(player) != null && BaublesApi.getBaubles(player).getStackInSlot(0) != null && BaublesApi.getBaubles(player).getStackInSlot(0).item === AlfheimItems.pixieAttractor && ManaItemHandler.requestManaExact(BaublesApi.getBaubles(player).getStackInSlot(0), player, 1, true)) {
			val vec = player.getLook(1.0f)
			this.motionX = (player.posX + vec.xCoord - this.posX) / 8.0f
			this.motionY = (player.posY + vec.yCoord + 1.5 - this.posY) / 8.0f
			this.motionZ = (player.posZ + vec.zCoord - this.posZ) / 8.0f
			super.onEntityUpdate()
			return
		}
		
		this.motionY *= 0.6
		if (this.worldObj.rand.nextInt(600) == 0) this.motionY -= 5.0
		
		super.onEntityUpdate()
	}
	
	override fun updateAITasks() {
		//super.updateAITasks();
		if (this.spawnPosition != null && (!this.worldObj.isAirBlock(this.spawnPosition!!.posX, this.spawnPosition!!.posY, this.spawnPosition!!.posZ) || this.spawnPosition!!.posY < 1)) {
			this.spawnPosition = null
		}
		
		if (this.spawnPosition == null || this.rand.nextInt(30) == 0 || this.spawnPosition!!.getDistanceSquared(this.posX.toInt(), this.posY.toInt(), this.posZ.toInt()) < 4.0f) {
			this.spawnPosition = ChunkCoordinates(this.posX.toInt() + this.rand.nextInt(7) - this.rand.nextInt(7), this.posY.toInt() + this.rand.nextInt(6) - 2, this.posZ.toInt() + this.rand.nextInt(7) - this.rand.nextInt(7))
		}
		
		val d0 = this.spawnPosition!!.posX.toDouble() + 0.5 - this.posX
		val d1 = this.spawnPosition!!.posY.toDouble() + 0.1 - this.posY
		val d2 = this.spawnPosition!!.posZ.toDouble() + 0.5 - this.posZ
		this.motionX += (Math.signum(d0) * 0.5 - this.motionX) * 0.1
		this.motionY += (Math.signum(d1) * 0.7 - this.motionY) * 0.1
		this.motionZ += (Math.signum(d2) * 0.5 - this.motionZ) * 0.1
		val f = (Math.atan2(this.motionZ, this.motionX) * 180.0 / Math.PI).toFloat() - 90.0f
		val f1 = MathHelper.wrapAngleTo180_float(f - this.rotationYaw)
		this.moveForward = 0.5f
		this.rotationYaw += f1
	}
	
	override fun setDead() {
		dead = true
		isDead = dead
		if (worldObj.isRemote)
			for (i in 0..11)
				Botania.proxy.sparkleFX(worldObj, posX + (Math.random() - 0.5) * 0.25, posY + 0.5 + (Math.random() - 0.5) * 0.25, posZ + (Math.random() - 0.5) * 0.25, 1f, 0.25f, 0.9f, 1f + Math.random().toFloat() * 0.25f, 5)
	}
	
	override fun getCanSpawnHere(): Boolean {
		return posY > 64 && 0 < this.worldObj.worldTime % 24000 && this.worldObj.worldTime % 24000 < 13333 || 22666 < this.worldObj.worldTime % 24000 && worldObj.getClosestPlayerToEntity(this, 64.0) != null && super.getCanSpawnHere()
	}
	
	@SideOnly(Side.CLIENT)
	override fun isInRangeToRenderDist(distance: Double): Boolean {
		return super.isInRangeToRenderDist(distance / 16.0)
	}
}
