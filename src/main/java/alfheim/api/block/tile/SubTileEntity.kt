package alfheim.api.block.tile

import alexsocol.asjlib.math.Vector3
import alfheim.api.AlfheimAPI
import alfheim.api.lib.LibResourceLocations
import net.minecraft.client.Minecraft
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.world.World

import java.util.*

// Used for anomalies - TileAnomaly
abstract class SubTileEntity {
	
	val rand = Random()
	var superTile: TileEntity? = null
	var ticks: Int = 0
	var worldGen = false
	
	abstract val targets: List<Any>
	
	abstract val rarity: EnumAnomalityRarity
	
	val frames: Int
		get() = 32
	
	open val strip: Int
		get() = 0
	
	val color: Int
		get() = 0xFFFFFF
	
	enum class EnumAnomalityRarity {
		COMMON, RARE, EPIC
	}
	
	/** optional update method for particles or other stuff  */
	protected open fun update() {}
	
	fun updateEntity(l: MutableList<Any?>?) {
		update()
		
		try {
			if (l == null || l.isEmpty()) return
			while (l.contains(null)) l.remove(null)
			if (l.isEmpty()) return
			
			for (target in l) performEffect(target!!)
		} finally {
			ticks++
		}
	}
	
	abstract fun performEffect(target: Any)
	
	/** Checks if two SubTiles can be mixed in single anomaly  */
	abstract fun typeBits(): Int
	
	open fun onActivated(stack: ItemStack?, player: EntityPlayer, world: World, x: Int, y: Int, z: Int) = false
	
	fun writeToNBT(cmp: NBTTagCompound) {
		cmp.setInteger(TAG_TICKS, ticks)
		writeCustomNBT(cmp)
	}
	
	open fun writeCustomNBT(cmp: NBTTagCompound) {}
	
	fun readFromNBT(cmp: NBTTagCompound) {
		ticks = cmp.getInteger(TAG_TICKS)
		readCustomNBT(cmp)
	}
	
	open fun readCustomNBT(cmp: NBTTagCompound) {}
	
	// ################################ SUPERTILE ################################
	
	val worldObj
	get() = superTile!!.worldObj!!
	
	fun x() = superTile!!.xCoord
	
	fun x(x: Double) = x() + MathHelper.floor_double(x)
	
	fun y() = superTile!!.yCoord
	
	fun y(y: Double) = y() + MathHelper.floor_double(y)
	
	fun z() = superTile!!.zCoord
	
	fun z(z: Double) = z() + MathHelper.floor_double(z)
	
	// ################################ UTILS ################################
	
	fun findNearestVulnerableEntity(radius: Double): EntityLivingBase? {
		val list = allAround(EntityLivingBase::class.java, radius)
		var entity1: EntityLivingBase? = null
		var d0 = java.lang.Double.MAX_VALUE
		
		for (entity2 in list) {
			if (entity2.isEntityInvulnerable) continue
			if (entity2 is EntityPlayer && entity2.capabilities.disableDamage) continue
			
			val d1 = Vector3.entityTileDistance(entity2, superTile!!)
			
			if (d1 <= d0) {
				entity1 = entity2
				d0 = d1
			}
		}
		
		return entity1
	}
	
	fun findNearestEntity(radius: Double): EntityLivingBase? {
		val list = allAround(EntityLivingBase::class.java, radius)
		var entity1: EntityLivingBase? = null
		var d0 = java.lang.Double.MAX_VALUE
		
		for (entity2 in list) {
			
			val d1 = Vector3.entityTileDistance(entity2, superTile!!)
			
			if (d1 <= d0) {
				entity1 = entity2
				d0 = d1
			}
		}
		return entity1
	}
	
	fun <E> allAround(clazz: Class<E>, radius: Double) =
		worldObj.getEntitiesWithinAABB(clazz, AxisAlignedBB.getBoundingBox(x().toDouble(), y().toDouble(), z().toDouble(), x(1.0).toDouble(), y(1.0).toDouble(), z(1.0).toDouble()).expand(radius, radius, radius)) as MutableList<E>
	
	fun allAroundRaw(clazz: Class<*>, radius: Double) =
		worldObj.getEntitiesWithinAABB(clazz, AxisAlignedBB.getBoundingBox(x().toDouble(), y().toDouble(), z().toDouble(), x(1.0).toDouble(), y(1.0).toDouble(), z(1.0).toDouble()).expand(radius, radius, radius)) as MutableList<Any>
	
	fun inWG() = worldGen
	
	// ################################ RENDER ################################
	
	fun bindTexture() {
		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.anomalies)
	}
	
	companion object {
		
		const val TAG_TICKS = "ticks"
		val EMPTY_LIST = ArrayList<Any>(0)
		
		const val NONE = 0
		//	0b00000 - fully compatible, do not use this unless you know what you are doing
		const val MOTION = 1
		//	0b00001 - motion manipulation	- gravity
		const val HEALTH = 2
		//	0b00010 - health manipulation	- damaging
		const val MANA = 4
		//	0b00100 - mana manipulation		- drain mana
		const val TIME = 8
		//	0b01000 - ticks manipulation	- time speedup
		const val SPACE = 17
		//	0b10001 - space manipulation	- teleportation		- also incompatible with motion
		const val ALL = -0x1    // 			- fully incompatible
		
		fun forName(name: String): SubTileEntity? {
			return try {
				AlfheimAPI.getAnomaly(name).newInstance()
			} catch (e: Exception) {
				e.printStackTrace()
				null
			}
		}
	}
}
