package alfheim.common.entity

import alexsocol.asjlib.*
import alfheim.common.item.rod.ItemBlackHoleRod
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.Botania
import vazkii.botania.common.block.tile.TileOpenCrate
import vazkii.botania.common.core.helper.MathHelper
import java.util.*
import kotlin.math.*

class EntityFracturedSpaceCollector(world: World): Entity(world) {
	
	var x = 0
	var y = -1
	var z = 0
	var ownerUUID: UUID? = null
	
	constructor(world: World, toX: Int, toY: Int, toZ: Int, player: EntityPlayer): this(world) {
		x = toX
		y = toY
		z = toZ
		ownerUUID = player.uniqueID
	}
	
	override fun entityInit() {
		setSize(0f, 0f)
	}
	
	override fun onUpdate() {
		motionX = 0.0
		motionY = 0.0
		motionZ = 0.0
		
		super.onUpdate()
		
		val age = ticksExisted
		if (worldObj.isRemote && age <= MAX_AGE) {
			doSparkles(age)
		} else {
			if (age > AGE_SPECIAL_START) {
				val nearbyItemEnts = worldObj.getEntitiesWithinAABB(EntityItem::class.java, boundingBox().expand(RADIUS, 1.0, RADIUS)) as MutableList<EntityItem>
				
				nearbyItemEnts.removeAll {
					MathHelper.pointDistancePlane(it.posX, it.posZ, posX, posZ) > RADIUS ||
					!it.isEntityAlive ||
					it.entityItem == null ||
					it.entityItem.stackSize <= 0
				}
				
				//Succ into the wormhole
				for (ent in nearbyItemEnts) {
					val xDifference = posX - ent.posX
					val zDifference = posZ - ent.posZ
					
					//play with this setting
					ent.motionX += xDifference * .3
					ent.motionZ += zDifference * .3
					ent.velocityChanged = true
				}
				if (age >= MAX_AGE) {
					//Transport the items
					//first figure out who to take the mana from
					if (ownerUUID == null) {
						setDead()
						return
					}
					
					val player = worldObj.func_152378_a(ownerUUID)
					if (player == null) {
						setDead()
						return
					}
					
					//fuckit let's just load the chunk
					val tile = worldObj.getTileEntity(x, y, z)
					if (tile is TileOpenCrate && tile.canEject()) {
						//delete all the items and emit them from the crate
						for (ent in nearbyItemEnts) {
							val stack = ent.entityItem // ?: continue removed above
							val count = stack.stackSize
//							if (count <= 0) continue // removed above
							val cost = count * MANA_COST_PER_ITEM
							if (ManaItemHandler.requestManaExact(TOOL_STACK, player, cost, false)) {
								//(item stacks aren't sorted by size so don't break on a failed mana extraction)
								ManaItemHandler.requestManaExact(TOOL_STACK, player, cost, true)
								tile.fakeCrateEject(stack)
								// dupe prevention
								ent.setEntityItemStack(null)
								ent.setDead()
							}
						}
					}
					
					//My work here is done
					setDead()
				}
			}
		}
	}
	
	fun doSparkles(age: Int) {
		val ageFraction = age / MAX_AGE.D
		//double radiusMult = 4 * (ageFraction - ageFraction * ageFraction); //simple and cute easing function
		val radiusMult = 1.6 * (ageFraction - ageFraction.pow(7.0)) //less simple but cuter easing function
		var particleAngle = age / 25.0
		val height = radiusMult / 2
		
		for (i in 0..PARTICLE_COUNT) {
			val x = cos(particleAngle) * RADIUS * radiusMult
			val z = sin(particleAngle) * RADIUS * radiusMult
			val size = (1 + ageFraction * 5 * Math.random()).F
			Botania.proxy.sparkleFX(worldObj, posX + x, posY + height, posZ + z, 0.5f, 0.15f, 0.9f, size, 5)
			particleAngle += 2 * Math.PI / PARTICLE_COUNT
		}
		
		val x = cos(Math.random() * Math.PI * 2) * RADIUS * radiusMult
		val z = cos(Math.random() * Math.PI * 2) * RADIUS * radiusMult
		
		Botania.proxy.wispFX(worldObj, posX + x, posY - 0.5 + height, posZ + z, 0.5f, 0.15f, 1f, 0.3f, -0.3f, 0.5f)
		
		if (age >= MAX_AGE - 2) {
			worldObj.spawnParticle("largesmoke", posX, posY, posZ, 0.0, 0.0, 0.0)
			
			for (i in 0..4)
				Botania.proxy.wispFX(worldObj, posX, posY, posZ, 0.5f, 0.45f, 0.9f, 2f, -0.1f, 0.1f)
		}
	}
	
	override fun writeEntityToNBT(nbt: NBTTagCompound) {
		nbt.setInteger(ItemBlackHoleRod.TAG_X, x)
		nbt.setInteger(ItemBlackHoleRod.TAG_Y, y)
		nbt.setInteger(ItemBlackHoleRod.TAG_Z, z)
		
		(ownerUUID ?: UUID(0, 0) ).apply {
			nbt.setLong(TAG_OWNER_MOST, mostSignificantBits)
			nbt.setLong(TAG_OWNER_LEAST, leastSignificantBits)
		}
	}
	
	override fun readEntityFromNBT(nbt: NBTTagCompound) {
		x = nbt.getInteger(ItemBlackHoleRod.TAG_X)
		y = nbt.getInteger(ItemBlackHoleRod.TAG_Y)
		z = nbt.getInteger(ItemBlackHoleRod.TAG_Z)
		
		ownerUUID = UUID(nbt.getLong(TAG_OWNER_MOST), nbt.getLong(TAG_OWNER_LEAST))
	}
	
	override fun getMaxInPortalTime() = Int.MAX_VALUE //Nope!
	
	companion object {
		
		const val TAG_OWNER_MOST = "ownerMost"
		const val TAG_OWNER_LEAST = "ownerLeast"
		
		const val RADIUS = 2.0
		const val MAX_AGE = 30
		const val AGE_SPECIAL_START = MAX_AGE * 3f / 4f
		const val MANA_COST_PER_ITEM = 5 //TODO balance this?
		const val PARTICLE_COUNT = 12
		
		val TOOL_STACK: ItemStack? = ItemStack(alfheim.common.item.AlfheimItems.rodBlackHole)
		
		fun TileOpenCrate.isPowered(): Boolean {
			//Uses the exact same logic open crates do to check if they're powered!
			for (dir in ForgeDirection.VALID_DIRECTIONS) {
				val redstoneSide = worldObj.getIndirectPowerLevelTo(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ, dir.ordinal)
				if (redstoneSide > 0)
					return true
			}
			return false
		}
		
		fun TileOpenCrate.fakeCrateEject(stack: ItemStack) {
			//mostly a copy of the open crate ejection logic
			//but doesn't touch the buffered item in the crate, if there is any 
			val newEnt = EntityItem(worldObj, xCoord + 0.5, yCoord - 0.5, zCoord + 0.5, stack)
			newEnt.motionX = 0.0
			newEnt.motionY = 0.0
			newEnt.motionZ = 0.0
			if (isPowered()) newEnt.age = -200
			worldObj.spawnEntityInWorld(newEnt)
		}
	}
}