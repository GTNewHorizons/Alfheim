package alexsocol.asjlib.math

import cpw.mods.fml.relauncher.*
import java.math.*
import java.nio.*

/**
 * Class representing 4x4 Matrix
 * @author ChickenBones
 */
class Matrix4 {
	
	// m<row><column>
	var m00: Double = 0.toDouble()
	var m01: Double = 0.toDouble()
	var m02: Double = 0.toDouble()
	var m03: Double = 0.toDouble()
	var m10: Double = 0.toDouble()
	var m11: Double = 0.toDouble()
	var m12: Double = 0.toDouble()
	var m13: Double = 0.toDouble()
	var m20: Double = 0.toDouble()
	var m21: Double = 0.toDouble()
	var m22: Double = 0.toDouble()
	var m23: Double = 0.toDouble()
	var m30: Double = 0.toDouble()
	var m31: Double = 0.toDouble()
	var m32: Double = 0.toDouble()
	var m33: Double = 0.toDouble()
	
	constructor() {
		m33 = 1.0
		m22 = m33
		m11 = m22
		m00 = m11
	}
	
	constructor(d00: Double, d01: Double, d02: Double, d03: Double, d10: Double, d11: Double, d12: Double, d13: Double, d20: Double, d21: Double, d22: Double, d23: Double, d30: Double, d31: Double, d32: Double, d33: Double) {
		m00 = d00
		m01 = d01
		m02 = d02
		m03 = d03
		m10 = d10
		m11 = d11
		m12 = d12
		m13 = d13
		m20 = d20
		m21 = d21
		m22 = d22
		m23 = d23
		m30 = d30
		m31 = d31
		m32 = d32
		m33 = d33
	}
	
	constructor(mat: Matrix4) {
		set(mat)
	}
	
	fun setIdentity(): Matrix4 {
		m33 = 1.0
		m22 = m33
		m11 = m22
		m00 = m11
		m32 = 0.0
		m31 = m32
		m30 = m31
		m23 = m30
		m21 = m23
		m20 = m21
		m13 = m20
		m12 = m13
		m10 = m12
		m03 = m10
		m02 = m03
		m01 = m02
		
		return this
	}
	
	fun translate(vec: Vector3): Matrix4 {
		m03 += m00 * vec.x + m01 * vec.y + m02 * vec.z
		m13 += m10 * vec.x + m11 * vec.y + m12 * vec.z
		m23 += m20 * vec.x + m21 * vec.y + m22 * vec.z
		m33 += m30 * vec.x + m31 * vec.y + m32 * vec.z
		
		return this
	}
	
	fun scale(vec: Vector3): Matrix4 {
		m00 *= vec.x
		m10 *= vec.x
		m20 *= vec.x
		m30 *= vec.x
		m01 *= vec.y
		m11 *= vec.y
		m21 *= vec.y
		m31 *= vec.y
		m02 *= vec.z
		m12 *= vec.z
		m22 *= vec.z
		m32 *= vec.z
		
		return this
	}
	
	fun rotate(angle: Double, axis: Vector3): Matrix4 {
		val c = Math.cos(angle)
		val s = Math.sin(angle)
		val mc = 1f - c
		val xy = axis.x * axis.y
		val yz = axis.y * axis.z
		val xz = axis.x * axis.z
		val xs = axis.x * s
		val ys = axis.y * s
		val zs = axis.z * s
		
		val f00 = axis.x * axis.x * mc + c
		val f10 = xy * mc + zs
		val f20 = xz * mc - ys
		
		val f01 = xy * mc - zs
		val f11 = axis.y * axis.y * mc + c
		val f21 = yz * mc + xs
		
		val f02 = xz * mc + ys
		val f12 = yz * mc - xs
		val f22 = axis.z * axis.z * mc + c
		
		val t00 = m00 * f00 + m01 * f10 + m02 * f20
		val t10 = m10 * f00 + m11 * f10 + m12 * f20
		val t20 = m20 * f00 + m21 * f10 + m22 * f20
		val t30 = m30 * f00 + m31 * f10 + m32 * f20
		val t01 = m00 * f01 + m01 * f11 + m02 * f21
		val t11 = m10 * f01 + m11 * f11 + m12 * f21
		val t21 = m20 * f01 + m21 * f11 + m22 * f21
		val t31 = m30 * f01 + m31 * f11 + m32 * f21
		m02 = m00 * f02 + m01 * f12 + m02 * f22
		m12 = m10 * f02 + m11 * f12 + m12 * f22
		m22 = m20 * f02 + m21 * f12 + m22 * f22
		m32 = m30 * f02 + m31 * f12 + m32 * f22
		m00 = t00
		m10 = t10
		m20 = t20
		m30 = t30
		m01 = t01
		m11 = t11
		m21 = t21
		m31 = t31
		
		return this
	}
	
	fun leftMultiply(mat: Matrix4): Matrix4 {
		val n00 = m00 * mat.m00 + m10 * mat.m01 + m20 * mat.m02 + m30 * mat.m03
		val n01 = m01 * mat.m00 + m11 * mat.m01 + m21 * mat.m02 + m31 * mat.m03
		val n02 = m02 * mat.m00 + m12 * mat.m01 + m22 * mat.m02 + m32 * mat.m03
		val n03 = m03 * mat.m00 + m13 * mat.m01 + m23 * mat.m02 + m33 * mat.m03
		val n10 = m00 * mat.m10 + m10 * mat.m11 + m20 * mat.m12 + m30 * mat.m13
		val n11 = m01 * mat.m10 + m11 * mat.m11 + m21 * mat.m12 + m31 * mat.m13
		val n12 = m02 * mat.m10 + m12 * mat.m11 + m22 * mat.m12 + m32 * mat.m13
		val n13 = m03 * mat.m10 + m13 * mat.m11 + m23 * mat.m12 + m33 * mat.m13
		val n20 = m00 * mat.m20 + m10 * mat.m21 + m20 * mat.m22 + m30 * mat.m23
		val n21 = m01 * mat.m20 + m11 * mat.m21 + m21 * mat.m22 + m31 * mat.m23
		val n22 = m02 * mat.m20 + m12 * mat.m21 + m22 * mat.m22 + m32 * mat.m23
		val n23 = m03 * mat.m20 + m13 * mat.m21 + m23 * mat.m22 + m33 * mat.m23
		val n30 = m00 * mat.m30 + m10 * mat.m31 + m20 * mat.m32 + m30 * mat.m33
		val n31 = m01 * mat.m30 + m11 * mat.m31 + m21 * mat.m32 + m31 * mat.m33
		val n32 = m02 * mat.m30 + m12 * mat.m31 + m22 * mat.m32 + m32 * mat.m33
		val n33 = m03 * mat.m30 + m13 * mat.m31 + m23 * mat.m32 + m33 * mat.m33
		
		m00 = n00
		m01 = n01
		m02 = n02
		m03 = n03
		m10 = n10
		m11 = n11
		m12 = n12
		m13 = n13
		m20 = n20
		m21 = n21
		m22 = n22
		m23 = n23
		m30 = n30
		m31 = n31
		m32 = n32
		m33 = n33
		
		return this
	}
	
	fun multiply(mat: Matrix4): Matrix4 {
		val n00 = m00 * mat.m00 + m01 * mat.m10 + m02 * mat.m20 + m03 * mat.m30
		val n01 = m00 * mat.m01 + m01 * mat.m11 + m02 * mat.m21 + m03 * mat.m31
		val n02 = m00 * mat.m02 + m01 * mat.m12 + m02 * mat.m22 + m03 * mat.m32
		val n03 = m00 * mat.m03 + m01 * mat.m13 + m02 * mat.m23 + m03 * mat.m33
		val n10 = m10 * mat.m00 + m11 * mat.m10 + m12 * mat.m20 + m13 * mat.m30
		val n11 = m10 * mat.m01 + m11 * mat.m11 + m12 * mat.m21 + m13 * mat.m31
		val n12 = m10 * mat.m02 + m11 * mat.m12 + m12 * mat.m22 + m13 * mat.m32
		val n13 = m10 * mat.m03 + m11 * mat.m13 + m12 * mat.m23 + m13 * mat.m33
		val n20 = m20 * mat.m00 + m21 * mat.m10 + m22 * mat.m20 + m23 * mat.m30
		val n21 = m20 * mat.m01 + m21 * mat.m11 + m22 * mat.m21 + m23 * mat.m31
		val n22 = m20 * mat.m02 + m21 * mat.m12 + m22 * mat.m22 + m23 * mat.m32
		val n23 = m20 * mat.m03 + m21 * mat.m13 + m22 * mat.m23 + m23 * mat.m33
		val n30 = m30 * mat.m00 + m31 * mat.m10 + m32 * mat.m20 + m33 * mat.m30
		val n31 = m30 * mat.m01 + m31 * mat.m11 + m32 * mat.m21 + m33 * mat.m31
		val n32 = m30 * mat.m02 + m31 * mat.m12 + m32 * mat.m22 + m33 * mat.m32
		val n33 = m30 * mat.m03 + m31 * mat.m13 + m32 * mat.m23 + m33 * mat.m33
		
		m00 = n00
		m01 = n01
		m02 = n02
		m03 = n03
		m10 = n10
		m11 = n11
		m12 = n12
		m13 = n13
		m20 = n20
		m21 = n21
		m22 = n22
		m23 = n23
		m30 = n30
		m31 = n31
		m32 = n32
		m33 = n33
		
		return this
	}
	
	fun transpose(): Matrix4 {
		val n10 = m01
		val n20 = m02
		val n30 = m03
		val n01 = m10
		val n21 = m12
		val n31 = m13
		val n02 = m20
		val n12 = m21
		val n32 = m23
		val n03 = m30
		val n13 = m31
		val n23 = m32
		
		m01 = n01
		m02 = n02
		m03 = n03
		m10 = n10
		m12 = n12
		m13 = n13
		m20 = n20
		m21 = n21
		m23 = n23
		m30 = n30
		m31 = n31
		m32 = n32
		
		return this
	}
	
	fun copy(): Matrix4 {
		return Matrix4(this)
	}
	
	fun set(mat: Matrix4): Matrix4 {
		m00 = mat.m00
		m01 = mat.m01
		m02 = mat.m02
		m03 = mat.m03
		m10 = mat.m10
		m11 = mat.m11
		m12 = mat.m12
		m13 = mat.m13
		m20 = mat.m20
		m21 = mat.m21
		m22 = mat.m22
		m23 = mat.m23
		m30 = mat.m30
		m31 = mat.m31
		m32 = mat.m32
		m33 = mat.m33
		
		return this
	}
	
	private fun mult3x3(vec: Vector3) {
		val x = m00 * vec.x + m01 * vec.y + m02 * vec.z
		val y = m10 * vec.x + m11 * vec.y + m12 * vec.z
		val z = m20 * vec.x + m21 * vec.y + m22 * vec.z
		
		vec.x = x
		vec.y = y
		vec.z = z
	}
	
	override fun toString(): String {
		val cont = MathContext(4, RoundingMode.HALF_UP)
		return "[" + BigDecimal(m00, cont) + "," + BigDecimal(m01, cont) + "," + BigDecimal(m02, cont) + "," + BigDecimal(m03, cont) + "]\n" +
			   "[" + BigDecimal(m10, cont) + "," + BigDecimal(m11, cont) + "," + BigDecimal(m12, cont) + "," + BigDecimal(m13, cont) + "]\n" +
			   "[" + BigDecimal(m20, cont) + "," + BigDecimal(m21, cont) + "," + BigDecimal(m22, cont) + "," + BigDecimal(m23, cont) + "]\n" +
			   "[" + BigDecimal(m30, cont) + "," + BigDecimal(m31, cont) + "," + BigDecimal(m32, cont) + "," + BigDecimal(m33, cont) + "]"
	}
	
	@SideOnly(Side.CLIENT)
	fun glApply() {
		glBuf.put(m00).put(m10).put(m20).put(m30).put(m01).put(m11).put(m21).put(m31).put(m02).put(m12).put(m22).put(m32).put(m03).put(m13).put(m23).put(m33)
		glBuf.flip()
		org.lwjgl.opengl.GL11.glMultMatrix(glBuf)
	}
	
	companion object {
		private val glBuf = ByteBuffer.allocateDirect(16 * 8).order(ByteOrder.nativeOrder()).asDoubleBuffer()
	}
}