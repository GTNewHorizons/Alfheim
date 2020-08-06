package alfheim.common.block.tile

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.api.AlfheimAPI
import alfheim.common.core.asm.hook.AlfheimHookHandler
import alfheim.common.core.util.DamageSourceSpell
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.AxisAlignedBB
import net.minecraftforge.common.util.ForgeDirection.VALID_DIRECTIONS
import vazkii.botania.common.Botania
import vazkii.botania.common.block.tile.TileMod
import vazkii.botania.common.core.helper.Vector3 as VVec3

class TileAnomalyHarvester: TileMod() {
	
	var radius = Vector3(1.0)
	var offset = Vector3()
	var power = 1.0
	
	// Protection from speeding up
	var tick = -1L
	
	private var subTiles = HashSet<String>()
	
	fun addSubTile(sub: String) {
		if (subTiles.contains(sub)) return
		
		subTiles.add(sub)
	}
	
	// omg Forge seriously null worldObj when calling this?
	override fun canUpdate() = worldObj?.totalWorldTime != tick
	
	val tunnels = setOf("Antigrav", "Gravity")
	
	override fun updateEntity() {
		if (worldObj.totalWorldTime == tick) return
		tick = worldObj.totalWorldTime
		
		if (worldObj.isBlockDirectlyGettingPowered(xCoord, yCoord, zCoord) || power <= 0.0) return
		
		val tunnel = subTiles.containsAll(tunnels)
		
		if (tunnel) AlfheimAPI.anomalyBehaviors["Tunnel"]!!(this)
		
		for (st in subTiles) {
			if (tunnel && st in tunnels) continue
			AlfheimAPI.anomalyBehaviors[st]?.invoke(this)
		}
		
		if (worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)) return
		
		renderBoundBox()
	}
	
	fun getAoE(): AxisAlignedBB = getBoundingBox(xCoord, yCoord, zCoord).expand(radius.x / 2, radius.y / 2, radius.z / 2).getOffsetBoundingBox(offset.x + 0.5, offset.y + 0.5, offset.z + 0.5)
	
	fun renderBoundBox() {
		val aabb = getAoE()
		var i: Double
		
		i = aabb.minX
		while (i <= aabb.maxX) {
			Botania.proxy.sparkleFX(worldObj, i, aabb.minY, aabb.minZ, 1f, 0f, 0f, 0.5f, 1, true)
			i += 0.25
		}
		
		i = aabb.minX
		while (i <= aabb.maxX) {
			Botania.proxy.sparkleFX(worldObj, i, aabb.maxY, aabb.maxZ, 1f, 0f, 0f, 0.5f, 1, true)
			i += 0.25
		}
		
		i = aabb.minX
		while (i <= aabb.maxX) {
			Botania.proxy.sparkleFX(worldObj, i, aabb.maxY, aabb.minZ, 1f, 0f, 0f, 0.5f, 1, true)
			i += 0.25
		}
		
		i = aabb.minX
		while (i <= aabb.maxX) {
			Botania.proxy.sparkleFX(worldObj, i, aabb.minY, aabb.maxZ, 1f, 0f, 0f, 0.5f, 1, true)
			i += 0.25
		}
		
		i = aabb.minY
		while (i <= aabb.maxY) {
			Botania.proxy.sparkleFX(worldObj, aabb.minX, i, aabb.minZ, 1f, 0f, 0f, 0.5f, 1, true)
			i += 0.25
		}
		
		i = aabb.minY
		while (i <= aabb.maxY) {
			Botania.proxy.sparkleFX(worldObj, aabb.maxX, i, aabb.maxZ, 1f, 0f, 0f, 0.5f, 1, true)
			i += 0.25
		}
		
		i = aabb.minY
		while (i <= aabb.maxY) {
			Botania.proxy.sparkleFX(worldObj, aabb.maxX, i, aabb.minZ, 1f, 0f, 0f, 0.5f, 1, true)
			i += 0.25
		}
		
		i = aabb.minY
		while (i <= aabb.maxY) {
			Botania.proxy.sparkleFX(worldObj, aabb.minX, i, aabb.maxZ, 1f, 0f, 0f, 0.5f, 1, true)
			i += 0.25
		}
		
		i = aabb.minZ
		while (i <= aabb.maxZ) {
			Botania.proxy.sparkleFX(worldObj, aabb.minX, aabb.minY, i, 1f, 0f, 0f, 0.5f, 1, true)
			i += 0.25
		}
		
		i = aabb.minZ
		while (i <= aabb.maxZ) {
			Botania.proxy.sparkleFX(worldObj, aabb.maxX, aabb.maxY, i, 1f, 0f, 0f, 0.5f, 1, true)
			i += 0.25
		}
		
		i = aabb.minZ
		while (i <= aabb.maxZ) {
			Botania.proxy.sparkleFX(worldObj, aabb.maxX, aabb.minY, i, 1f, 0f, 0f, 0.5f, 1, true)
			i += 0.25
		}
		
		i = aabb.minZ
		while (i <= aabb.maxZ) {
			Botania.proxy.sparkleFX(worldObj, aabb.minX, aabb.maxY, i, 1f, 0f, 0f, 0.5f, 1, true)
			i += 0.25
		}
	}
	
	override fun writeCustomNBT(nbt: NBTTagCompound) {
		nbt.setInteger("subtiles", subTiles.size)
		subTiles.forEachIndexed { i, s -> nbt.setString("subtile$i", s) }
		
		nbt.setDouble("rX", radius.x)
		nbt.setDouble("rY", radius.y)
		nbt.setDouble("rZ", radius.z)
		nbt.setDouble("oX", offset.x)
		nbt.setDouble("oY", offset.y)
		nbt.setDouble("oZ", offset.z)
		
		nbt.setDouble("power", power)
	}
	
	override fun readCustomNBT(nbt: NBTTagCompound) {
		AnomalyHarvesterBehaviors
		
		val size = nbt.getInteger("subtiles")
		subTiles.clear()
		
		for (i in 0 until size)
			subTiles.add(nbt.getString("subtile$i"))
			
		radius.set(nbt.getDouble("rX"), nbt.getDouble("rY"), nbt.getDouble("rZ"))
		offset.set(nbt.getDouble("oX"), nbt.getDouble("oY"), nbt.getDouble("oZ"))
		
		power = nbt.getDouble("power")
	}
}

object AnomalyHarvesterBehaviors {
	
	init {
		AlfheimAPI.anomalyBehaviors["Antigrav"] = { tile: TileAnomalyHarvester -> doAntigrav(tile) }
		AlfheimAPI.anomalyBehaviors["Gravity"] = { tile: TileAnomalyHarvester -> doGravity(tile) }
		AlfheimAPI.anomalyBehaviors["Tunnel"] = { tile: TileAnomalyHarvester -> doTunnel(tile) }
		AlfheimAPI.anomalyBehaviors["Lightning"] = { tile: TileAnomalyHarvester -> doLightning(tile) }
		AlfheimAPI.anomalyBehaviors["SpeedUp"] = { tile: TileAnomalyHarvester -> doSpeedUp(tile) }
	}
	
	private fun doAntigrav(tile: TileAnomalyHarvester) {
		val aabb = tile.getAoE()
		tile.worldObj.getEntitiesWithinAABB(Entity::class.java, aabb).forEach { it as Entity
			it.motionY += if (it.isSneaking) 0.05 else 0.085 + tile.power * 0.005
			it.fallDistance = 0f
		}
		
		AlfheimHookHandler.wispNoclip = false
		
		for (c in 0..3) {
			val x = (Math.random() - 0.5) * tile.radius.x + tile.offset.x
			val y = (Math.random() - 0.5) * tile.radius.y + tile.offset.y
			val z = (Math.random() - 0.5) * tile.radius.z + tile.offset.z
			
			Botania.proxy.wispFX(tile.worldObj, tile.xCoord + x + 0.5, tile.yCoord + y - 0.5, tile.zCoord + z + 0.5, 0.5f, 0.9f, 1f, 0.1f, -0.1f, 1f)
		}
		
		AlfheimHookHandler.wispNoclip = true
	}
	
	fun doGravity(tile: TileAnomalyHarvester) {
		val x = tile.xCoord + tile.offset.x + 0.5
		val y = tile.yCoord + tile.offset.y + 0.5// - tile.radius.y / 2
		val z = tile.zCoord + tile.offset.z + 0.5
		
		tile.worldObj.getEntitiesWithinAABB(Entity::class.java, tile.getAoE()).filter { it !is EntityPlayer }.forEach { it as Entity
			val v = Vector3(x, y, z).add(0.5).sub(Vector3.fromEntity(it)).normalize().mul(0.5 + tile.power * 0.1)
			
			it.motionX += v.x
			it.motionY += v.y * 1.25
			it.motionZ += v.z
		}
		
		Botania.proxy.sparkleFX(tile.worldObj, x, y, z, 0.5f, 0.75f, 1f, 1f, 10, true)
	}
	
	fun doTunnel(tile: TileAnomalyHarvester) {
		val dir = VALID_DIRECTIONS.safeGet(tile.worldObj.getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord))
		
		val mX = dir.offsetX / 10f
		val mY = dir.offsetY / 10f
		val mZ = dir.offsetZ / 10f
		
		val aabb = tile.getAoE()
		tile.worldObj.getEntitiesWithinAABB(Entity::class.java, aabb).forEach { it as Entity
			it.motionX = mX * tile.power / 2f
			it.motionY = mY * tile.power / 2f
			it.motionZ = mZ * tile.power / 2f
			it.fallDistance = 0f
			
//			if (it !is EntityPlayer) return@forEach
//			
//			val rotations = arrayOf(it.rotationYaw, it.rotationYaw, 180f, 0f, 90f, -90f)
//			
//			if (it.rotationYaw != rotations[dir.ordinal]) {
//				it.cameraYaw += (rotations[dir.ordinal] - it.cameraYaw) % 5f
//			}
		}
		
		AlfheimHookHandler.wispNoclip = false
		
		for (c in 0..3) {
			val x = (Math.random() - 0.5) * tile.radius.x + tile.offset.x
			val y = (Math.random() - 0.5) * tile.radius.y + tile.offset.y
			val z = (Math.random() - 0.5) * tile.radius.z + tile.offset.z
			
			Botania.proxy.wispFX(tile.worldObj, tile.xCoord + x + 0.5 - mX * 10, tile.yCoord + y + 0.5 - mY * 10, tile.zCoord + z + 0.5 - mZ * 10, 0.3f, 0.9f, 0.8f, 0.1f, mX, mY, mZ, 1f)
		}
		
		AlfheimHookHandler.wispNoclip = true
	}
	
	fun doLightning(tile: TileAnomalyHarvester) {
		val x = tile.xCoord + tile.offset.x + 0.5
		val y = tile.yCoord + tile.offset.y + 0.5
		val z = tile.zCoord + tile.offset.z + 0.5
		
		tile.worldObj.getEntitiesWithinAABB(Entity::class.java, tile.getAoE()).forEach { it as Entity
			if (it.attackEntityFrom(DamageSourceSpell.anomaly, (Math.random() * tile.power / 2 + tile.power / 2).F))
				Botania.proxy.lightningFX(tile.worldObj, VVec3(x, y, z), VVec3.fromEntityCenter(it), 1f, tile.worldObj.rand.nextLong(), 0, 0xFF0000)
		}
		
		Botania.proxy.sparkleFX(tile.worldObj, x, y, z, 1f, 0f, 0f, 2f, 1, true)
		Botania.proxy.sparkleFX(tile.worldObj, x, y, z, 1f, 1f, 1f, 1f, 1, true)
	}
	
	fun doSpeedUp(tile: TileAnomalyHarvester) {
		val aabb = tile.getAoE()
		
		for (i in 1 until tile.power.I) {
			tile.worldObj.getEntitiesWithinAABB(Entity::class.java, aabb).forEach { (it as Entity).onUpdate() }
			
			tile.worldObj.loadedTileEntityList.forEach {
				it as TileEntity
				if (aabb.isVecInside(Vector3.fromTileEntity(it).add(0.5).toVec3()) && it.canUpdate()) it.updateEntity()
			}
		}
		
		for (x in aabb.minX.I..aabb.maxX.I.minus(1))
			for (y in aabb.minY.I..aabb.maxY.I.minus(1))
				for (z in aabb.minZ.I..aabb.maxZ.I.minus(1))
					tile.worldObj.getBlock(x, y, z).updateTick(tile.worldObj, x, y, z, tile.worldObj.rand)
		
		val x = (Math.random() - 0.5) * tile.radius.x + tile.offset.x + tile.xCoord + 0.5
		val y = (Math.random() - 0.5) * tile.radius.y + tile.offset.y + tile.yCoord + 0.5
		val z = (Math.random() - 0.5) * tile.radius.z + tile.offset.z + tile.zCoord + 0.5
		
		Botania.proxy.sparkleFX(tile.worldObj, x, y, z, 0.5f, 1f, 0.5f, 1f, 1, true)
	}
}