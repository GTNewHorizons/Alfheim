package alexsocol.asjlib.math

import com.google.common.collect.Lists
import cpw.mods.fml.relauncher.*
import net.minecraft.util.*
import org.lwjgl.opengl.GL11.*
import java.util.*
import kotlin.math.*

/**
 * Oriented Bound Box: scalable, translatable, rotatable!<br></br>
 * Collision calculations by [@gszauer](https://github.com/gszauer/GamePhysicsCookbook)<br></br>
 * <br></br>
 * Useful scheme:
 * <pre>
 * h----g
 * /|   /|
 * e----f |
 * | d--|-c
 * |/   |/
 * a----b
 *
 * Y
 * |  Z
 * | /
 * |/_____X
</pre> *
 *
 * @author AlexSocol
 */
@Suppress("unused")
open class OrientedBB() {
	
	val pos: Vector3 = Vector3(0.5, 0.5, 0.5)            // center
	val size: Vector3 = Vector3(0.5, 0.5, 0.5)        // half size
	val orient: Matrix4 = Matrix4()        // rotation (orientation) matrix
	
	val a: Vector3 = Vector3(0.0, 0.0, 0.0)
	val b: Vector3 = Vector3(1.0, 0.0, 0.0)
	val c: Vector3 = Vector3(1.0, 0.0, 1.0)
	val d: Vector3 = Vector3(0.0, 0.0, 1.0)
	val e: Vector3 = Vector3(0.0, 1.0, 0.0)
	val f: Vector3 = Vector3(1.0, 1.0, 0.0)
	val g: Vector3 = Vector3(1.0, 1.0, 1.0)
	val h: Vector3 = Vector3(0.0, 1.0, 1.0)
	
	constructor(aabb: AxisAlignedBB): this() {
		fromAABB(aabb)
	}
	
	/** Returns array of vertices for this BB  */
	fun vertices(): Array<Vector3> {
		return arrayOf(a, b, c, d, e, f, g, h)
	}
	
	fun fromAABB(aabb: AxisAlignedBB): OrientedBB {
		pos.set(getAABBPosition(aabb))
		size.set(getAABBSize(aabb))
		
		a[aabb.minX, aabb.minY] = aabb.minZ
		b[aabb.maxX, aabb.minY] = aabb.minZ
		c[aabb.maxX, aabb.minY] = aabb.maxZ
		d[aabb.minX, aabb.minY] = aabb.maxZ
		e[aabb.minX, aabb.maxY] = aabb.minZ
		f[aabb.maxX, aabb.maxY] = aabb.minZ
		g[aabb.maxX, aabb.maxY] = aabb.maxZ
		h[aabb.minX, aabb.maxY] = aabb.maxZ
		
		return this
	}
	
	/** Converts this OBB to AABB using min and max positions  */
	fun toAABB(): AxisAlignedBB {
		val xs = Lists.newArrayList(a.x, b.x, c.x, d.x, e.x, f.x, g.x, h.x)
		val ys = Lists.newArrayList(a.y, b.y, c.y, d.y, e.y, f.y, g.y, h.y)
		val zs = Lists.newArrayList(a.z, b.z, c.z, d.z, e.z, f.z, g.z, h.z)
		return AxisAlignedBB.getBoundingBox(Collections.min(xs), Collections.min(ys), Collections.min(zs), Collections.max(xs), Collections.max(ys), Collections.max(zs))
	}
	
	/** Sets BB's center to this coords  */
	fun setPosition(x: Double, y: Double, z: Double): OrientedBB {
		pos[x, y] = z
		
		a.set(pos).add(-size.x, -size.y, -size.z)
		b.set(pos).add(size.x, -size.y, -size.z)
		c.set(pos).add(size.x, -size.y, size.z)
		d.set(pos).add(-size.x, -size.y, size.z)
		e.set(pos).add(-size.x, size.y, -size.z)
		f.set(pos).add(size.x, size.y, -size.z)
		g.set(pos).add(size.x, size.y, size.z)
		h.set(pos).add(-size.x, size.y, size.z)
		
		return this
	}
	
	/** Moves BB on given distance  */
	fun translate(x: Double, y: Double, z: Double): OrientedBB {
		pos.add(x, y, z)
		
		a.add(x, y, z)
		b.add(x, y, z)
		c.add(x, y, z)
		d.add(x, y, z)
		e.add(x, y, z)
		f.add(x, y, z)
		g.add(x, y, z)
		h.add(x, y, z)
		
		return this
	}
	
	/** Rescales BB in both directions from middle  */
	fun scale(x: Double, y: Double, z: Double): OrientedBB {
		size.mul(x, y, z)
		
		val i = pos.x
		val j = pos.y
		val k = pos.z
		translate(-i, -j, -k)
		a.mul(x, y, z)
		b.mul(x, y, z)
		c.mul(x, y, z)
		d.mul(x, y, z)
		e.mul(x, y, z)
		f.mul(x, y, z)
		g.mul(x, y, z)
		h.mul(x, y, z)
		translate(i, j, k)
		
		return this
	}
	
	/** Rotates BB on given angle in DEG around given axis (axis coords are global)  */
	fun rotate(angle: Double, axis: Vector3): OrientedBB {
		orient.rotate(Math.toRadians(angle), axis)
		pos.rotate(angle, axis)
		
		a.rotate(angle, axis)
		b.rotate(angle, axis)
		c.rotate(angle, axis)
		d.rotate(angle, axis)
		e.rotate(angle, axis)
		f.rotate(angle, axis)
		g.rotate(angle, axis)
		h.rotate(angle, axis)
		
		return this
	}
	
	/** Rotates BB on given angle in DEG around given axis (axis coords are local, starting at pos[0, 0, 0])  */
	fun rotateLocal(angle: Double, axis: Vector3): OrientedBB {
		val x = pos.x
		val y = pos.y
		val z = pos.z
		translate(-x, -y, -z)
		rotate(angle, axis)
		translate(x, y, z)
		return this
	}
	
	/** Rotates BB on given angle in DEG around middle point of ADHE (BCGF) face  */
	fun rotateOX(angle: Double): OrientedBB {
		val x = pos.x
		val y = pos.y
		val z = pos.z
		translate(-x, -y, -z)
		rotate(angle, Vector3.oX)
		translate(x, y, z)
		return this
	}
	
	/** Rotates BB on given angle in DEG around middle point of ABCD (EFGH) face  */
	fun rotateOY(angle: Double): OrientedBB {
		val x = pos.x
		val y = pos.y
		val z = pos.z
		translate(-x, -y, -z)
		rotate(angle, Vector3.oY)
		translate(x, y, z)
		return this
	}
	
	/** Rotates BB on given angle in DEG around middle point of ABFE (DCGH) face  */
	fun rotateOZ(angle: Double): OrientedBB {
		val x = pos.x
		val y = pos.y
		val z = pos.z
		translate(-x, -y, -z)
		rotate(angle, Vector3.oZ)
		translate(x, y, z)
		return this
	}
	
	/** Checks if this OBB intersects with given Vec3  */
	fun intersectsWith(vec3: Vec3): Boolean {
		return intersectsWith(this, Vector3(vec3))
	}
	
	/** Checks if this OBB intersects with given Vec3  */
	fun intersectsWith(vec3: Vector3): Boolean {
		return intersectsWith(this, vec3)
	}
	
	/** Checks if this OBB intersects with given AABB  */
	fun intersectsWith(aabb: AxisAlignedBB): Boolean {
		return intersectsWith(this, aabb)
	}
	
	/** Checks if this OBB intersects with given OBB  */
	fun intersectsWith(obb: OrientedBB): Boolean {
		return intersectsWith(this, obb)
	}
	
	class Interval {
		var min: Double = 0.toDouble()
		var max: Double = 0.toDouble()
	}
	
	/**
	 * Draws OBB on its position
	 * @param dt 0 to disable depth test
	 */
	@SideOnly(Side.CLIENT)
	fun draw(dt: Int) {
		glPushMatrix()
		if (dt == 0) glDisable(GL_DEPTH_TEST)
		glColor4d(0.0, 0.0, 0.0, 1.0)
		glPointSize(6f)
		drawVertices()
		
		glColor4d(1.0, 0.0, 0.0, 1.0)
		glLineWidth(3f)
		drawEdges()
		
		glColor4d(0.0, 0.0, 1.0, 0.5)
		drawFaces()
		if (dt == 0) glEnable(GL_DEPTH_TEST)
		glPopMatrix()
		
		glColor4d(1.0, 1.0, 1.0, 1.0)
	}
	
	@SideOnly(Side.CLIENT)
	fun drawVertices() {
		glBegin(GL_POINTS)
		for (v in vertices()) {
			v.glVertex()
		}
		glEnd()
	}
	
	@SideOnly(Side.CLIENT)
	fun drawEdges() {
		glBegin(GL_LINES)
		a.glVertex()
		b.glVertex()
		b.glVertex()
		c.glVertex()
		c.glVertex()
		d.glVertex()
		d.glVertex()
		a.glVertex()
		
		e.glVertex()
		f.glVertex()
		f.glVertex()
		g.glVertex()
		g.glVertex()
		h.glVertex()
		h.glVertex()
		e.glVertex()
		
		a.glVertex()
		e.glVertex()
		b.glVertex()
		f.glVertex()
		c.glVertex()
		g.glVertex()
		d.glVertex()
		h.glVertex()
		glEnd()
	}
	
	@SideOnly(Side.CLIENT)
	fun drawFaces() {
		glDisable(GL_TEXTURE_2D)
		glEnable(GL_BLEND)
		glBegin(GL_QUADS)
		a.glVertex()
		b.glVertex()
		c.glVertex()
		d.glVertex()
		
		h.glVertex()
		g.glVertex()
		f.glVertex()
		e.glVertex()
		
		e.glVertex()
		f.glVertex()
		b.glVertex()
		a.glVertex()
		
		f.glVertex()
		g.glVertex()
		c.glVertex()
		b.glVertex()
		
		g.glVertex()
		h.glVertex()
		d.glVertex()
		c.glVertex()
		
		h.glVertex()
		e.glVertex()
		a.glVertex()
		d.glVertex()
		
		glEnd()
		glDisable(GL_BLEND)
		glEnable(GL_TEXTURE_2D)
	}
	
	companion object {
		
		fun intersectsWith(obb: OrientedBB, point: Vector3): Boolean {
			val dir = point.copy().sub(obb.pos)
			val o = doubleArrayOf(obb.orient.m00, obb.orient.m01, obb.orient.m02, obb.orient.m10, obb.orient.m11, obb.orient.m12, obb.orient.m20, obb.orient.m21, obb.orient.m22)
			val s = doubleArrayOf(obb.size.x, obb.size.y, obb.size.z)
			
			for (i in 0..2) {
				val axis = Vector3(o[i * 3], o[i * 3 + 1], o[i * 3 + 2])
				
				val distance = dir.dotProduct(axis)
				
				if (distance > s[i]) return false
				if (distance < -s[i]) return false
			}
			
			return true
		}
		
		/** Checks if given OBB intersects with given AABB  */
		fun intersectsWith(obb: OrientedBB, aabb: AxisAlignedBB): Boolean {
			val test = arrayOfNulls<Vector3>(15)
			
			test[0] = Vector3(1.0, 0.0, 0.0)            // AABB axis 1
			test[1] = Vector3(0.0, 1.0, 0.0)            // AABB axis 2
			test[2] = Vector3(0.0, 0.0, 1.0)            // AABB axis 3
			test[3] = Vector3(obb.orient.m00, obb.orient.m01, obb.orient.m02)
			test[4] = Vector3(obb.orient.m10, obb.orient.m11, obb.orient.m12)
			test[5] = Vector3(obb.orient.m20, obb.orient.m21, obb.orient.m22)
			
			for (i in 0..2) { // Fill out rest of axis
				test[6 + i * 3] = test[i]!!.copy().crossProduct(test[0]!!)
				test[6 + i * 3 + 1] = test[i]!!.copy().crossProduct(test[1]!!)
				test[6 + i * 3 + 2] = test[i]!!.copy().crossProduct(test[2]!!)
			}
			
			for (i in 0..14) {
				if (!overlapOnAxis(aabb, obb, test[i]!!)) {
					return false // Seperating axis found
				}
			}
			
			return true // Seperating axis not found
		}
		
		/** Checks if one OBB intersects with other  */
		fun intersectsWith(obb1: OrientedBB, obb2: OrientedBB): Boolean {
			val test = arrayOfNulls<Vector3>(15)
			
			test[0] = Vector3(obb1.orient.m00, obb1.orient.m01, obb1.orient.m02)
			test[1] = Vector3(obb1.orient.m10, obb1.orient.m11, obb1.orient.m12)
			test[2] = Vector3(obb1.orient.m20, obb1.orient.m21, obb1.orient.m22)
			test[3] = Vector3(obb2.orient.m00, obb2.orient.m01, obb2.orient.m02)
			test[4] = Vector3(obb2.orient.m10, obb2.orient.m11, obb2.orient.m12)
			test[5] = Vector3(obb2.orient.m20, obb2.orient.m21, obb2.orient.m22)
			
			for (i in 0..2) { // Fill out rest of axis
				test[6 + i * 3] = test[i]!!.copy().crossProduct(test[0]!!)
				test[6 + i * 3 + 1] = test[i]!!.copy().crossProduct(test[1]!!)
				test[6 + i * 3 + 2] = test[i]!!.copy().crossProduct(test[2]!!)
			}
			
			for (i in 0..14) {
				if (!overlapOnAxis(obb1, obb2, test[i]!!)) {
					return false // Seperating axis found
				}
			}
			
			return true // Seperating axis not found
		}
		
		protected fun overlapOnAxis(aabb: AxisAlignedBB, obb: OrientedBB, axis: Vector3): Boolean {
			val a = getInterval(aabb, axis)
			val b = getInterval(obb, axis)
			return b.min <= a.max && a.min <= b.max
		}
		
		protected fun overlapOnAxis(obb1: OrientedBB, obb2: OrientedBB, axis: Vector3): Boolean {
			val a = getInterval(obb1, axis)
			val b = getInterval(obb2, axis)
			return b.min <= a.max && a.min <= b.max
		}
		
		protected fun getInterval(obb: OrientedBB, axis: Vector3): Interval {
			val vertex = arrayOfNulls<Vector3>(8)
			
			val c = Vector3(obb.pos.x, obb.pos.y, obb.pos.z)        // OBB Center
			val e = Vector3(obb.size.x, obb.size.y, obb.size.z)    // OBB Extents
			
			val o = doubleArrayOf(obb.orient.m00, obb.orient.m01, obb.orient.m02, obb.orient.m10, obb.orient.m11, obb.orient.m12, obb.orient.m20, obb.orient.m21, obb.orient.m22)
			
			val a = arrayOf(// OBB Axis
				Vector3(o[0], o[1], o[2]), Vector3(o[3], o[4], o[5]), Vector3(o[6], o[7], o[8]))
			
			vertex[0] = c.copy().add(a[0].copy().mul(e.x)).add(a[1].copy().mul(e.y)).add(a[2].copy().mul(e.z))
			vertex[1] = c.copy().sub(a[0].copy().mul(e.x)).add(a[1].copy().mul(e.y)).add(a[2].copy().mul(e.z))
			vertex[2] = c.copy().add(a[0].copy().mul(e.x)).sub(a[1].copy().mul(e.y)).add(a[2].copy().mul(e.z))
			vertex[3] = c.copy().add(a[0].copy().mul(e.x)).add(a[1].copy().mul(e.y)).sub(a[2].copy().mul(e.z))
			vertex[4] = c.copy().sub(a[0].copy().mul(e.x)).sub(a[1].copy().mul(e.y)).sub(a[2].copy().mul(e.z))
			vertex[5] = c.copy().add(a[0].copy().mul(e.x)).sub(a[1].copy().mul(e.y)).sub(a[2].copy().mul(e.z))
			vertex[6] = c.copy().sub(a[0].copy().mul(e.x)).add(a[1].copy().mul(e.y)).sub(a[2].copy().mul(e.z))
			vertex[7] = c.copy().sub(a[0].copy().mul(e.x)).sub(a[1].copy().mul(e.y)).add(a[2].copy().mul(e.z))
			
			val result = Interval()
			result.max = axis.copy().dotProduct(vertex[0]!!)
			result.min = result.max
			
			for (i in 1..7) {
				val projection = axis.copy().dotProduct(vertex[i]!!)
				result.min = if (projection < result.min) projection else result.min
				result.max = if (projection > result.max) projection else result.max
			}
			
			return result
		}
		
		protected fun getInterval(aabb: AxisAlignedBB, axis: Vector3): Interval {
			val min = getMin(aabb)
			val max = getMax(aabb)
			
			val vertex = arrayOf(Vector3(min.x, max.y, max.z), Vector3(min.x, max.y, min.z), Vector3(min.x, min.y, max.z), Vector3(min.x, min.y, min.z), Vector3(max.x, max.y, max.z), Vector3(max.x, max.y, min.z), Vector3(max.x, min.y, max.z), Vector3(max.x, min.y, min.z))
			
			val result = Interval()
			result.max = axis.copy().dotProduct(vertex[0])
			result.min = result.max
			
			for (i in 1..7) {
				val projection = axis.copy().dotProduct(vertex[i])
				result.min = if (projection < result.min) projection else result.min
				result.max = if (projection > result.max) projection else result.max
			}
			
			return result
		}
		
		protected fun getMin(aabb: AxisAlignedBB): Vector3 {
			val p1 = getAABBPosition(aabb).add(getAABBSize(aabb))
			val p2 = getAABBPosition(aabb).sub(getAABBSize(aabb))
			
			return Vector3(min(p1.x, p2.x), min(p1.y, p2.y), min(p1.z, p2.z))
		}
		
		protected fun getMax(aabb: AxisAlignedBB): Vector3 {
			val p1 = getAABBPosition(aabb).add(getAABBSize(aabb))
			val p2 = getAABBPosition(aabb).sub(getAABBSize(aabb))
			
			return Vector3(max(p1.x, p2.x), max(p1.y, p2.y), max(p1.z, p2.z))
		}
		
		fun getAABBPosition(aabb: AxisAlignedBB): Vector3 {
			return Vector3((aabb.minX + aabb.maxX) / 2.0, (aabb.minY + aabb.maxY) / 2.0, (aabb.minZ + aabb.maxZ) / 2.0)
		}
		
		fun getAABBSize(aabb: AxisAlignedBB): Vector3 {
			return Vector3(sqrt((aabb.minX - aabb.maxX).pow(2.0)) / 2.0, sqrt((aabb.minY - aabb.maxY).pow(2.0)) / 2.0, sqrt((aabb.minZ - aabb.maxZ).pow(2.0)) / 2.0)
		}
	}
}