package alexsocol.asjlib.math

import java.util.*
import kotlin.math.*

/**
 * Class representing quaternion
 * @author ChickenBones, Vazkii
 */
class Quaternion {
	
	var x = 0.0
	var y = 0.0
	var z = 0.0
	var s = 0.0
	
	constructor() {
		s = 1.0
		x = 0.0
		y = 0.0
		z = 0.0
	}
	
	constructor(quat: Quaternion) {
		x = quat.x
		y = quat.y
		z = quat.z
		s = quat.s
	}
	
	constructor(d: Double, d1: Double, d2: Double, d3: Double) {
		x = d1
		y = d2
		z = d3
		s = d
	}
	
	fun set(quat: Quaternion) {
		x = quat.x
		y = quat.y
		z = quat.z
		s = quat.s
	}
	
	fun multiply(quat: Quaternion) {
		val d = s * quat.s - x * quat.x - y * quat.y - z * quat.z
		val d1 = s * quat.x + x * quat.s - y * quat.z + z * quat.y
		val d2 = s * quat.y + x * quat.z + y * quat.s - z * quat.x
		val d3 = s * quat.z - x * quat.y + y * quat.x + z * quat.s
		s = d
		x = d1
		y = d2
		z = d3
	}
	
	fun rightMultiply(quat: Quaternion) {
		val d = s * quat.s - x * quat.x - y * quat.y - z * quat.z
		val d1 = s * quat.x + x * quat.s + y * quat.z - z * quat.y
		val d2 = s * quat.y - x * quat.z + y * quat.s + z * quat.x
		val d3 = s * quat.z + x * quat.y - y * quat.x + z * quat.s
		s = d
		x = d1
		y = d2
		z = d3
	}
	
	fun mag() = sqrt(x * x + y * y + z * z + s * s)
	
	fun normalize() {
		var d = mag()
		if (d != 0.0) {
			d = 1.0 / d
			x *= d
			y *= d
			z *= d
			s *= d
		}
	}
	
	fun rotate(vec: Vector3) {
		val d = -x * vec.x - y * vec.y - z * vec.z
		val d1 = s * vec.x + y * vec.z - z * vec.y
		val d2 = s * vec.y - x * vec.z + z * vec.x
		val d3 = s * vec.z + x * vec.y - y * vec.x
		vec.x = d1 * s - d * x - d2 * z + d3 * y
		vec.y = d2 * s - d * y + d1 * z - d3 * x
		vec.z = d3 * s - d * z - d1 * y + d2 * x
	}
	
	override fun toString(): String {
		val stringbuilder = StringBuilder()
		val formatter = Formatter(stringbuilder, Locale.US)
		formatter.format("Quaternion:\n")
		formatter.format("  < %f %f %f %f >\n", s, x, y, z)
		formatter.close()
		return "$stringbuilder"
	}
	
	companion object {
		
		fun aroundAxis(ax: Double, ay: Double, az: Double, angle: Double): Quaternion {
			var ang = angle
			ang *= 0.5
			val d4 = sin(ang)
			return Quaternion(cos(ang), ax * d4, ay * d4, az * d4)
		}
		
		fun aroundAxis(axis: Vector3, angle: Double) = aroundAxis(axis.x, axis.y, axis.z, angle)
	}
}