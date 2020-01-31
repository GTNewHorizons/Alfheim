package alexsocol.asjlib.math

import alfheim.common.core.util.D
import cpw.mods.fml.relauncher.*
import net.minecraft.entity.Entity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import org.lwjgl.util.vector.*
import java.io.Serializable
import java.math.*
import kotlin.math.*

/**
 * Class representing 3-point vector
 * @author ChickenBones, Vazkii, AlexSocol
 */
@Suppress("unused")
class Vector3: Serializable {
	
	var x = 0.0
	var y = 0.0
	var z = 0.0
	
	operator fun component1() = x
	operator fun component2() = y
	operator fun component3() = z
	
	val isZero: Boolean
		get() = x == 0.0 && y == 0.0 && z == 0.0
	
	val isAxial: Boolean
		get() = if (x == 0.0) y == 0.0 || z == 0.0 else y == 0.0 && z == 0.0
	
	constructor(d: Number = 0.0, d1: Number = d, d2: Number = d) {
		x = d.D
		y = d1.D
		z = d2.D
	}
	
	constructor(vec: Vector3) {
		set(vec)
	}
	
	constructor(vec: Vec3) {
		x = vec.xCoord
		y = vec.yCoord
		z = vec.zCoord
	}
	
	fun toVec3() = Vec3.createVectorHelper(x, y, z)!!
	
	fun copy() = Vector3(this)
	
	fun discard() = set(0.0, 0.0, 0.0)
	
	fun set(d: Number, d1: Number = d, d2: Number = d): Vector3 {
		x = d.D
		y = d1.D
		z = d2.D
		return this
	}
	
	fun set(vec: Vec3): Vector3 {
		x = vec.xCoord
		y = vec.yCoord
		z = vec.zCoord
		return this
	}
	
	fun set(vec: Vector3): Vector3 {
		x = vec.x
		y = vec.y
		z = vec.z
		return this
	}
	
	fun set(e: Entity): Vector3 {
		x = e.posX
		y = e.posY
		z = e.posZ
		return this
	}
	
	fun set(te: TileEntity): Vector3 {
		x = te.xCoord.D
		y = te.yCoord.D
		z = te.zCoord.D
		return this
	}
	
	fun rand(): Vector3 {
		x = Math.random()
		y = Math.random()
		z = Math.random()
		return this
	}
	
	fun dotProduct(vec: Vector3): Double {
		var d = vec.x * x + vec.y * y + vec.z * z
		
		if (d > 1 && d < 1.00001)
			d = 1.0
		else if (d < -1 && d > -1.00001)
			d = -1.0
		return d
	}
	
	fun dotProduct(d: Number, d1: Number, d2: Number) = d.D * x + d1.D * y + d2.D * z
	
	fun crossProduct(vec: Vector3): Vector3 {
		val d = y * vec.z - z * vec.y
		val d1 = z * vec.x - x * vec.z
		val d2 = x * vec.y - y * vec.x
		x = d
		y = d1
		z = d2
		return this
	}
	
	fun add(d: Number, d1: Number = d, d2: Number = d): Vector3 {
		x += d.D
		y += d1.D
		z += d2.D
		return this
	}
	
	fun add(vec: Vector3): Vector3 {
		x += vec.x
		y += vec.y
		z += vec.z
		return this
	}
	
	fun add(e: Entity): Vector3 {
		x += e.posX
		y += e.posY
		z += e.posZ
		return this
	}
	
	fun add(te: TileEntity): Vector3 {
		x += te.xCoord.D
		y += te.yCoord.D
		z += te.zCoord.D
		return this
	}
	
	@JvmOverloads
	fun sub(d: Number, d1: Number = d, d2: Number = d): Vector3 {
		x -= d.D
		y -= d1.D
		z -= d2.D
		return this
	}
	
	fun sub(vec: Vector3): Vector3 {
		x -= vec.x
		y -= vec.y
		z -= vec.z
		return this
	}
	
	fun sub(e: Entity): Vector3 {
		x -= e.posX
		y -= e.posY
		z -= e.posZ
		return this
	}
	
	fun sub(te: TileEntity): Vector3 {
		x -= te.xCoord.D
		y -= te.yCoord.D
		z -= te.zCoord.D
		return this
	}
	
	fun mul(f: Vector3): Vector3 {
		x *= f.x
		y *= f.y
		z *= f.z
		return this
	}
	
	fun mul(d: Number, d1: Number = d, d2: Number = d): Vector3 {
		x *= d.D
		y *= d1.D
		z *= d2.D
		return this
	}
	
	fun extend(d: Number) = set(copy().normalize().mul(max(length() + d.D, 0.0)))
	
	fun shrink(d: Number) = set(copy().normalize().mul(min(length() - d.D, 0.0)))
	
	fun length() = sqrt(x * x + y * y + z * z)
	
	fun lengthSquared() = x * x + y * y + z * z
	
	fun normalize(): Vector3 {
		val d = length()
		if (d != 0.0)
			mul(1 / d)
		
		return this
	}
	
	override fun toString(): String {
		val cont = MathContext(4, RoundingMode.HALF_UP)
		return "Vector3(" + BigDecimal(x, cont) + ", " + BigDecimal(y, cont) + ", " + BigDecimal(z, cont) + ")"
	}
	
	fun perpendicular() = if (z == 0.0) zCrossProduct() else xCrossProduct()
	
	fun xCrossProduct(): Vector3 {
		val d = z
		val d1 = -y
		x = 0.0
		y = d
		z = d1
		return this
	}
	
	fun yCrossProduct(): Vector3 {
		val d = -z
		val d1 = x
		x = d
		y = 0.0
		z = d1
		return this
	}
	
	fun zCrossProduct(): Vector3 {
		val d = y
		
		val d1 = -x
		x = d
		y = d1
		z = 0.0
		return this
	}
	
	fun angle(vec: Vector3) = acos(copy().normalize().dotProduct(vec.copy().normalize()))
	
	fun isInside(aabb: AxisAlignedBB) = x >= aabb.minX && y >= aabb.maxY && z >= aabb.minZ && x < aabb.maxX && y < aabb.maxY && z < aabb.maxZ
	
	@SideOnly(Side.CLIENT)
	fun vector3f() = Vector3f(x.toFloat(), y.toFloat(), z.toFloat())
	
	@SideOnly(Side.CLIENT)
	fun vector4f() = Vector4f(x.toFloat(), y.toFloat(), z.toFloat(), 1f)
	
	@SideOnly(Side.CLIENT)
	fun glVertex(): Vector3 {
		org.lwjgl.opengl.GL11.glVertex3d(x, y, z)
		return this
	}
	
	fun negate(): Vector3 {
		x = -x
		y = -y
		z = -z
		return this
	}
	
	fun scalarProject(b: Vector3): Double {
		val l = b.length()
		return if (l == 0.0) 0.0 else dotProduct(b) / l
	}
	
	fun project(b: Vector3): Vector3 {
		val l = b.lengthSquared()
		if (l == 0.0) {
			set(0.0, 0.0, 0.0)
			return this
		}
		
		val m = dotProduct(b) / l
		set(b).mul(m)
		return this
	}
	
	fun rotate(angle: Double, axis: Vector3): Vector3 {
		Quaternion.aroundAxis(axis.copy().normalize(), Math.toRadians(angle)).rotate(this)
		return this
	}
	
	override fun equals(other: Any?): Boolean {
		if (other !is Vector3) return false
		
		return x == other.x && y == other.y && z == other.z
	}
	
	override fun hashCode(): Int {
		var result = x.hashCode()
		result = 31 * result + y.hashCode()
		result = 31 * result + z.hashCode()
		return result
	}
	
	companion object {
		
		private const val serialVersionUID = 84148136481306L
		
		@Transient
		val fallback = Vector3(-1.0, -1.0, -1.0)
		@Transient
		val zero = Vector3()
		@Transient
		val one = Vector3(1.0, 1.0, 1.0)
		@Transient
		val center = Vector3(0.5, 0.5, 0.5)
		@Transient
		val oX = Vector3(1.0, 0.0, 0.0)
		@Transient
		val oY = Vector3(0.0, 1.0, 0.0)
		@Transient
		val oZ = Vector3(0.0, 0.0, 1.0)
		
		fun fromEntity(e: Entity) = Vector3(e.posX, e.posY, e.posZ)
		
		fun fromEntityCenter(e: Entity) = Vector3(e.posX, e.posY - e.yOffset + e.height / 2, e.posZ)
		
		fun fromTileEntity(e: TileEntity) = Vector3(e.xCoord.D, e.yCoord.D, e.zCoord.D)
		
		fun fromTileEntityCenter(e: TileEntity) = Vector3(e.xCoord + 0.5, e.yCoord + 0.5, e.zCoord + 0.5)
		
		fun vecDistance(v1: Vector3, v2: Vector3) = sqrt((v1.x - v2.x).pow(2.0) + (v1.y - v2.y).pow(2.0) + (v1.z - v2.z).pow(2.0))
		
		fun vecEntityDistance(v: Vector3, e: Entity) = sqrt((v.x - e.posX).pow(2.0) + (v.y - e.posY).pow(2.0) + (v.z - e.posZ).pow(2.0))
		
		fun vecTileDistance(v: Vector3, te: TileEntity) = sqrt((v.x - te.xCoord).pow(2.0) + (v.y - te.yCoord).pow(2.0) + (v.z - te.zCoord).pow(2.0))
		
		fun entityTileDistance(e: Entity, te: TileEntity) = sqrt((e.posX - te.xCoord).pow(2.0) + (e.posY - te.yCoord).pow(2.0) + (e.posZ - te.zCoord).pow(2.0))
		
		fun entityDistance(e1: Entity, e2: Entity) = sqrt((e1.posX - e2.posX).pow(2.0) + (e1.posY - e2.posY).pow(2.0) + (e1.posZ - e2.posZ).pow(2.0))
		
		fun entityDistancePlane(e1: Entity, e2: Entity) = hypot(e1.posX - e2.posX, e1.posZ - e2.posZ)
		
		fun pointDistancePlane(x1: Number, y1: Number, x2: Number, y2: Number) = hypot(x1.D - x2.D, y1.D - y2.D)
		
		fun pointDistanceSpace(x1: Number, y1: Number, z1: Number, x2: Number, y2: Number, z2: Number) = sqrt((x1.D - x2.D).pow(2) + (y1.D - y2.D).pow(2) + (z1.D - z2.D).pow(2))
	}
}