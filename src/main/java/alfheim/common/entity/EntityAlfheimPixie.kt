package alfheim.common.entity

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.common.item.AlfheimItems
import alfheim.common.world.dim.alfheim.biome.BiomeField
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.relauncher.*
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.world.World
import ru.vamig.worldengine.*
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
		entityDropItem(ItemStack(ModItems.manaResource, 1, 8), 0f)
	}
	
	private val immuneTo = arrayOf(DamageSource.inWall.damageType, DamageSource.drown.damageType, DamageSource.fall.damageType)
	
	override fun attackEntityFrom(src: DamageSource, amount: Float): Boolean {
		if (src.damageType in immuneTo) return false
		
		return super.attackEntityFrom(src, amount)
	}
	
	override fun updateEntityActionState() {
		rotationYaw = -atan2(motionX, motionZ).F * 180f / Math.PI.F
		renderYawOffset = rotationYaw
	}
	
	override fun onEntityUpdate() {
		if (worldObj.isRemote)
			for (i in 0..3)
				Botania.proxy.sparkleFX(worldObj, posX + (Math.random() - 0.5) * 0.25, posY + 0.5 + (Math.random() - 0.5) * 0.25, posZ + (Math.random() - 0.5) * 0.25, 1f, 0.25f, 0.9f, 0.1f + Math.random().F * 0.25f, 12)
		
		var player: EntityPlayer? = worldObj.getClosestPlayerToEntity(this, 64.0)
		if (player == null) {
			setDead()
			onDeath(DamageSource.outOfWorld)
			return
		}
		
		if (Vector3.entityDistance(this, player) > 16.0) player = null
		if (player != null && PlayerHandler.getPlayerBaubles(player)[0]?.item === AlfheimItems.pixieAttractor && ManaItemHandler.requestManaExact(PlayerHandler.getPlayerBaubles(player)[0], player, 1, true)) {
			val vec = player.getLook(1f)
			motionX = (player.posX + vec.xCoord - posX) / 8f
			motionY = (player.posY + vec.yCoord + 1.5 - posY) / 8f
			motionZ = (player.posZ + vec.zCoord - posZ) / 8f
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
		
		if (spawnPosition == null || rand.nextInt(30) == 0 || spawnPosition!!.getDistanceSquared(posX.I, posY.I, posZ.I) < 4f) {
			spawnPosition = ChunkCoordinates(posX.I + rand.nextInt(7) - rand.nextInt(7), posY.I + rand.nextInt(6) - 2, posZ.I + rand.nextInt(7) - rand.nextInt(7))
		}
		
		val d0 = spawnPosition!!.posX.D + 0.5 - posX
		val d1 = spawnPosition!!.posY.D + 0.1 - posY
		val d2 = spawnPosition!!.posZ.D + 0.5 - posZ
		motionX += (sign(d0) * 0.5 - motionX) * 0.1
		motionY += (sign(d1) * 0.7 - motionY) * 0.1
		motionZ += (sign(d2) * 0.5 - motionZ) * 0.1
		val f = (atan2(motionZ, motionX) * 180.0 / Math.PI).F - 90f
		val f1 = MathHelper.wrapAngleTo180_float(f - rotationYaw)
		moveForward = 0.5f
		rotationYaw += f1
	}
	
	override fun setDead() {
		dead = true
		isDead = dead
		if (worldObj.isRemote)
			for (i in 0..11)
				Botania.proxy.sparkleFX(worldObj, posX + (Math.random() - 0.5) * 0.25, posY + 0.5 + (Math.random() - 0.5) * 0.25, posZ + (Math.random() - 0.5) * 0.25, 1f, 0.25f, 0.9f, 1f + Math.random().F * 0.25f, 5)
	}
	
	override fun getCanSpawnHere(): Boolean {
		setPosition(posX, posY + 5, posZ)
		val flagTime = (worldObj.worldTime % 24000L).I in ((0..13333) + (22666..23999))
		var flagBiome = false
		
		val chunk = (worldObj.provider as? WE_WorldProvider)?.cp
		if (chunk != null)
			flagBiome = WE_Biome.getBiomeAt(chunk, posX.mfloor().toLong(), posZ.mfloor().toLong()).isEqualTo(BiomeField)
		
		return flagTime && flagBiome && posY > 64 && super.getCanSpawnHere()
	}
	
	@SideOnly(Side.CLIENT)
	override fun isInRangeToRenderDist(distance: Double): Boolean {
		return super.isInRangeToRenderDist(distance / 16.0)
	}
}
