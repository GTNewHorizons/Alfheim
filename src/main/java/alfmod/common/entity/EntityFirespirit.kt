package alfmod.common.entity

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.client.render.world.VisualEffectHandlerClient
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.handler.VisualEffectHandler
import alfheim.common.item.AlfheimItems
import alfheim.common.item.equipment.bauble.faith.bidiRange
import alfmod.common.item.AlfheimModularItems
import alfmod.common.item.material.EventResourcesMetas
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity.*
import net.minecraft.entity.ai.*
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.monster.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.*
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.event.entity.living.LivingAttackEvent
import vazkii.botania.common.item.ModItems
import kotlin.math.*

class EntityFirespirit(world: World): EntityLiving(world) {
	
	var timer = 0
	
	var master: Boolean
		get() = dataWatcher.getWatchableObjectInt(13) != 0
		set(master) = dataWatcher.updateObject(13, if (master) 1 else 0)
	
	var position: ChunkCoordinates
		get() = dataWatcher.getWatchedObject(12).`object` as ChunkCoordinates
		set(pos) = dataWatcher.updateObject(12, pos)
	
	override fun entityInit() {
		super.entityInit()
		noClip = true
		setSize(0f, 0f)
		dataWatcher.addObject(12, ChunkCoordinates())
		dataWatcher.addObject(13, 0)
	}
	
	override fun onEntityUpdate() {
		val (x, y, z) = position
		setPosition(x + 0.5, y + if (master) 0.5 else 1.5, z + 0.5)
		setMotion(0.0)
		clearActivePotions()
		
		getPlayersAround(worldObj, x, y, z).forEach {
			if (it.heldItem?.item === AlfheimItems.rodInterdiction)
				if (it.dropOneItem(true) == null)
					it.setCurrentItemOrArmor(0, null)
		}
		
		super.onEntityUpdate()
		
		if (master && !checkStructure(worldObj, x, y - 1, z, false, null)) {
			setDead()
			return
		}
		
		if (!worldObj.isRemote) {
			val angles = arrayOf(0, 120, 240)
			
			if (master) {
				for (a in arrayOf(60, 180, 300)) {
					val i = cos(Math.toRadians(-ticksExisted * 4.0 - a))
					val k = sin(Math.toRadians(-ticksExisted * 4.0 - a))
					VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.WISP, dimension, posX + i, posY + 1, posZ + k, 1.0, Math.random() * 0.5, 0.0, 0.25, 0.0, 0.0, 0.0, 0.5)
				}
				
				VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.WISP, dimension, posX, posY + 0.5, posZ, 1.0, Math.random() * 0.5, 0.0, 0.25, 0.0, 0.0, 0.0, 0.5)
			}
			
			for (a in angles) {
				val i = cos(Math.toRadians(ticksExisted * 4.0 + a)) / 2
				val k = sin(Math.toRadians(ticksExisted * 4.0 + a)) / 2
				VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.WISP, dimension, posX + i, posY, posZ + k, 1.0, Math.random() * 0.5, 0.0, 0.25, 0.0, 0.0, 0.0, 0.5)
			}
		}
		
		if (master) {
			if (timer-- <= 0) {
				if (!worldObj.isRemote) setDead()
				return
			}
		} else {
			return
		}
		
		if (timer == 1) {
			worldObj.setBlockToAir(x, y - 1, z)
			if (!worldObj.isRemote)
				worldObj.spawnEntityInWorld(EntityItem(worldObj, x + 0.5, y + 1.5, z + 0.5, ItemStack(AlfheimModularItems.eventResource, 1, EventResourcesMetas.SunRelic)).apply { setMotion(0.0) })
			return
		}
		
		if (timer % 50 == 0) {
			if (!worldObj.isRemote) worldObj.spawnEntityInWorld(
				@Suppress("IMPLICIT_CAST_TO_ANY") // this is not Any, this is EntityLiving you stupid plugin -_-
				(when (rand.nextInt(11)) {
					0       -> {
						EntityMuspellsun(worldObj).apply {
							setRandomPos(x, y, z, false)
						}
					}
					
					// no targeting so no need in many
					1       -> EntityMagmaCube(worldObj).apply {
						setRandomPos(x, y, z, false)
						
						slimeSize = rand.nextInt(3) + 4
						getEntityAttribute(SharedMonsterAttributes.maxHealth).baseValue = health * 1.25
						health = maxHealth
					}
					
					in 3..4 -> EntityBlaze(worldObj).apply {
						setRandomPos(x, y, z, true)
						
						setTarget(this@EntityFirespirit)
						
						getEntityAttribute(SharedMonsterAttributes.maxHealth).baseValue = 50.0
						health = maxHealth
					}
					
					in 5..6 -> EntityPigZombie(worldObj).apply {
						setRandomPos(x, y, z, false)
						
						when (rand.nextInt(10)) {
							0       -> epicSet
							in 1..3 -> eliteSet
							else    -> regularSet
						}.forEachIndexed { id, it ->
							setCurrentItemOrArmor(id, ItemStack(it))
							setEquipmentDropChance(id, 0f)
						}
						
						getEntityAttribute(SharedMonsterAttributes.attackDamage).baseValue = 3.0
						getEntityAttribute(SharedMonsterAttributes.maxHealth).baseValue = 30.0
						health = maxHealth
						
						setTarget(this@EntityFirespirit)
					}
					
					7       -> EntityGhast(worldObj).apply {
						setRandomPos(x, y, z, true)
						
						if (rand.nextInt(3) == 0) {
							targetedEntity = this@EntityFirespirit
							aggroCooldown = this@EntityFirespirit.timer + 50
						}
						
						explosionStrength = rand.nextInt(3) + 2
					}

//					8..10
					else    -> EntitySkeleton(worldObj).apply {
						setRandomPos(x, y, z, false)
						skeletonType = 1
						
						when (rand.nextInt(10)) {
							0       -> epicSet
							in 1..3 -> eliteSet
							else    -> regularSet
						}.forEachIndexed { id, it ->
							setCurrentItemOrArmor(id, ItemStack(it))
							setEquipmentDropChance(id, 0f)
						}
						
						getEntityAttribute(SharedMonsterAttributes.attackDamage).baseValue = 4.0
						getEntityAttribute(SharedMonsterAttributes.maxHealth).baseValue = 40.0
						health = maxHealth
						
						tasks.addTask(4, EntityAIAttackOnCollide(this, EntityFirespirit::class.java, 1.2, true))
						targetTasks.addTask(2, EntityAINearestAttackableTarget(this, EntityFirespirit::class.java, 0, true))
					}
				} as EntityLiving).apply {
					entityData.setBoolean(TAG_RITUAL_SUMMONED, true)
				}
			)
		}
	}
	
	override fun attackEntityFrom(src: DamageSource?, amount: Float): Boolean {
		// protecting spirit for being accidentally harmed by a player
		if (src?.entity is EntityPlayer) return false
		
		return super.attackEntityFrom(src, amount)
	}
	
	override fun setDead() {
		super.setDead()
		
		val (x, y, z) = position
		
		if (health <= 0f || master) {
			worldObj.playAuxSFX(2001, x, y, z, worldObj.getBlock(x, y, z).id + (worldObj.getBlockMetadata(x, y, z) shl 12))
			worldObj.setBlockToAir(x, y, z)
		}
		
		if (!master) return
		
		for (c in PYLONS) {
			worldObj.getEntitiesWithinAABB(EntityFirespirit::class.java, getBoundingBox(x + c[0] + 0.5, y + c[1] + 0.5, z + c[2] + 0.5).expand(0.5)).forEach {
				(it as? EntityFirespirit)?.setDead()
			}
		}
	}
	
	override fun getCommandSenderName(): String {
		return StatCollector.translateToLocal("${super.getCommandSenderName()}${if (master) ".greater" else ""}")
	}
	
	override fun canDespawn() = false
	
	override fun writeEntityToNBT(nbt: NBTTagCompound) {
		super.writeEntityToNBT(nbt)
		
		val (x, y, z) = position
		nbt.setInteger(TAG_POS_X, x)
		nbt.setInteger(TAG_POS_Y, y)
		nbt.setInteger(TAG_POS_Z, z)
		
		nbt.setBoolean(TAG_MASTER, master)
	}
	
	override fun readEntityFromNBT(nbt: NBTTagCompound) {
		super.readEntityFromNBT(nbt)
		
		position = ChunkCoordinates(
			nbt.getInteger(TAG_POS_X),
			nbt.getInteger(TAG_POS_Y),
			nbt.getInteger(TAG_POS_Z)
		)
		
		master = nbt.getBoolean(TAG_MASTER)
	}
	
	companion object {
		
		const val TAG_RITUAL_SUMMONED = "firespiritsummoned"
		
		const val TAG_MASTER = "isMaster"
		const val TAG_POS_X = "posX"
		const val TAG_POS_Y = "posY"
		const val TAG_POS_Z = "posZ"
		
		const val RADIUS = 16
		
		val PYLONS = arrayOf(arrayOf(5, 1, 5), arrayOf(5, 1, -5), arrayOf(-5, 1, -5), arrayOf(-5, 1, 5))
		
		val regularSet = arrayOf(Items.iron_sword, Items.chainmail_boots, Items.chainmail_leggings, Items.chainmail_chestplate, Items.chainmail_helmet)
		val eliteSet = arrayOf(ModItems.elementiumSword, ModItems.elementiumBoots, ModItems.elementiumLegs, ModItems.elementiumChest, ModItems.elementiumHelm)
		val epicSet = arrayOf(AlfheimModularItems.volcanoMace, AlfheimModularItems.volcanoBoots, AlfheimModularItems.volcanoLeggings, AlfheimModularItems.volcanoChest, AlfheimModularItems.volcanoHelmet)
		
		init {
			eventForge()
		}
		
		fun startRitual(world: World, x: Int, y: Int, z: Int, player: EntityPlayer): Boolean {
			if (!checkStructure(world, x, y, z, true, player)) return false
			
			if (!world.setBlock(x, y + 1, z, AlfheimBlocks.redFlame)) return false
			
			var master = true
			
			for (c in arrayOf(arrayOf(0, 1, 0), *PYLONS)) {
				val spirit = EntityFirespirit(world)
				spirit.master = master
				spirit.setPosition(x + c[0] + 0.5, y + c[1] + if (master) 0.5 else 1.5, z + c[2] + 0.5)
				spirit.position = ChunkCoordinates(x + c[0], y + c[1], z + c[2])
				spirit.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth).baseValue = if (master) 1000.0 else 500.0
				spirit.health = spirit.maxHealth
				if (master) spirit.timer = 4000
				if (!world.spawnEntityInWorld(spirit)) return false
				master = false
			}
			
			if (!player.capabilities.isCreativeMode) --player.heldItem.stackSize
			
			return true
		}
		
		fun checkStructure(world: World, x: Int, y: Int, z: Int, first: Boolean, player: EntityPlayer?): Boolean {
			if (!first && world.getBlock(x, y + 1, z) !== AlfheimBlocks.redFlame) {
				if (!world.isRemote) getPlayersAround(world, x, y, z).forEach { ASJUtilities.say(it, "alfmodmisc.spirit.flamedied") }
				return false
			}
			
			if (first) {
				for (c in PYLONS)
					if (world.getBlock(x + c[0], y + c[1], z + c[2]) !== AlfheimBlocks.alfheimPylon || world.getBlockMetadata(x + c[0], y + c[1], z + c[2]) != 1) {
						if (!world.isRemote) {
							ASJUtilities.say(player, "alfmodmisc.spirit.nopylons.$first")
							VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.WISP, world.provider.dimensionId, x + c[0] + 0.5, y + c[1] + 0.5, z + c[2] + 0.5, 1.0, 0.5, 0.0, 1.0, 0.0, 0.0, 0.0, 5.0, 0.0)
						}
						return false
					}
			}
			
			if (!hasProperArena(world, x, y, z)) {
				if (!world.isRemote) {
					if (first)
						ASJUtilities.say(player, "alfmodmisc.spirit.badarena")
					else
						getPlayersAround(world, x, y, z).forEach {
							ASJUtilities.say(it, "alfmodmisc.spirit.arenacorrupt")
						}
				}
				
				return false
			}
			
			return true
		}
		
		fun hasProperArena(world: World, x: Int, y: Int, z: Int): Boolean {
			for (i in x.bidiRange(RADIUS + 3))
				for (j in y..(RADIUS + 3))
					for (k in z.bidiRange(RADIUS + 3)) {
						if (i == x && j == y + 1 && k == z) continue // ignore fire
						if (abs(i - x) == 5 && abs(k - z) == 5 && j == y + 1 || Vector3.pointDistanceSpace(i, j, k, x, y, z) > RADIUS) continue  // Ignore pylons and out of circle
						
						if (world.getTileEntity(i, j, k) != null) {
							VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.WISP, world.provider.dimensionId, i + 0.5, j + 0.5, k + 0.5, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 5.0, 0.0)
							
							return false
						}
						
						val block = world.getBlock(i, j, k)
						if (block === Blocks.fire) continue
						
						if (j != y) {
							if (!world.isAirBlock(i, j, k)) {
								VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.WISP, world.provider.dimensionId, i + 0.5, j + 0.5, k + 0.5, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 5.0, 0.0)
								
								return false
							}
						} else {
							if (!block.material.blocksMovement() ||
								!block.renderAsNormalBlock() ||
								!block.isOpaqueCube ||
								!block.isNormalCube ||
								block.blockBoundsMinX != 0.0 ||
								block.blockBoundsMinY != 0.0 ||
								block.blockBoundsMinZ != 0.0 ||
								block.blockBoundsMaxX != 1.0 ||
								block.blockBoundsMaxY != 1.0 ||
								block.blockBoundsMaxZ != 1.0) {
								
								VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.WISP, world.provider.dimensionId, i + 0.5, j + 0.5, k + 0.5, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 5.0, 0.0)
								
								return false
							}
						}
					}
			return true
		}
		
		@Suppress("UNCHECKED_CAST")
		fun getPlayersAround(world: World, x: Int, y: Int, z: Int) = world.getEntitiesWithinAABB(EntityPlayer::class.java, getBoundingBox(x, y, z).expand(RADIUS).apply { minY += RADIUS }) as MutableList<EntityPlayer>
		
		private fun EntityLivingBase.setRandomPos(x: Int, y: Int, z: Int, above: Boolean) {
			val j = if (above) Math.random() * RADIUS else 0.0
			val (a, b, c) = Vector3(RADIUS * 0.75, 0, 0).rotate(Math.random() * 360, Vector3.oY).add(x, y + j, z)
			setPosition(a, b, c)
		}
		
		// disable friendly-fire for ritual summoned entities
		@SubscribeEvent
		fun onLivingAttacked(e: LivingAttackEvent) {
			e.isCanceled = e.entityLiving.entityData.getBoolean(TAG_RITUAL_SUMMONED) && e.source.entity?.entityData?.getBoolean(TAG_RITUAL_SUMMONED) == true
		}
	}
}