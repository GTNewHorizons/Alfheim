package alfheim.common.core.util

import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.security.SecureRandom
import kotlin.experimental.xor

object HashUtils {
	
	fun merge(s1: String, s2: String): String {
		val s = StringBuilder()
		for (c1 in s1) for (c2 in s2) s.append(((c1.toShort() * c2.toShort()) % 256).toChar())
		return "$s"
	}
	
	// Might as well be called sugar given it's not secure at all :D
	internal fun salt(str: String): String {
		val salt = str + "wellithoughtthatthisiscoolideaandicanmakesomethinglikethis#whynot"
		val rand = SecureRandom(salt.toByteArray(Charset.forName("UTF-8")))
		val l = salt.length
		val steps = rand.nextInt(l)
		val chrs = salt.toCharArray()
		for (i in 0 until steps) {
			val indA = rand.nextInt(l)
			var indB: Int
			do {
				indB = rand.nextInt(l)
			} while (indB == indA)
			val c = (chrs[indA].toShort() xor chrs[indB].toShort()).toChar()
			chrs[indA] = c
		}
		
		return String(chrs)
	}
}

object Converter {
	
	private val hexCode = "0123456789ABCDEF".toCharArray()
	
	fun bytesToHexString(data: ByteArray): String {
		val r = StringBuilder(data.size * 2)
		for (b in data) {
			r.append(hexCode[b.toInt() shr 4 and 0xF])
			r.append(hexCode[b.toInt() and 0xF])
		}
		return "$r"
	}
}

/**
 * Offers a `hash(byte[])` method for hashing messages with SHA-256.
 *
 * @author github.com/meyfa
 */
object Sha256 {
	
	private val K = intArrayOf(0x428a2f98, 0x71374491, -0x4a3f0431,
							   -0x164a245b, 0x3956c25b, 0x59f111f1, -0x6dc07d5c, -0x54e3a12b,
							   -0x27f85568, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74,
							   -0x7f214e02, -0x6423f959, -0x3e640e8c, -0x1b64963f, -0x1041b87a,
							   0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc,
							   0x76f988da, -0x67c1aeae, -0x57ce3993, -0x4ffcd838, -0x40a68039,
							   -0x391ff40d, -0x2a586eb9, 0x06ca6351, 0x14292967, 0x27b70a85,
							   0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb,
							   -0x7e3d36d2, -0x6d8dd37b, -0x5d40175f, -0x57e599b5, -0x3db47490,
							   -0x3893ae5d, -0x2e6d17e7, -0x2966f9dc, -0xbf1ca7b, 0x106aa070,
							   0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3,
							   0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3, 0x748f82ee, 0x78a5636f,
							   -0x7b3787ec, -0x7338fdf8, -0x6f410006, -0x5baf9315, -0x41065c09,
							   -0x398e870e)
	private val H0 = intArrayOf(0x6a09e667, -0x4498517b, 0x3c6ef372,
								-0x5ab00ac6, 0x510e527f, -0x64fa9774, 0x1f83d9ab, 0x5be0cd19)
	// working arrays
	private val W = IntArray(64)
	private val H = IntArray(8)
	private val TEMP = IntArray(8)
	/**
	 * Hashes the given message with SHA-256 and returns the hash.
	 *
	 * @param message The bytes to hash.
	 *
	 * @return The hash's bytes.
	 */
	fun hash(message: ByteArray): ByteArray { // let H = H0
		System.arraycopy(H0, 0, H, 0, H0.size)
		// initialize all words
		val words = toIntArray(pad(message))
		// enumerate all blocks (each containing 16 words)
		var i = 0
		val n = words.size / 16
		while (i < n) {
			// initialize W from the block's words
			System.arraycopy(words, i * 16, W, 0, 16)
			for (t in 16 until W.size) {
				W[t] = (smallSig1(W[t - 2]) + W[t - 7] + smallSig0(W[t - 15])
						+ W[t - 16])
			}
			// let TEMP = H
			System.arraycopy(H, 0, TEMP, 0, H.size)
			// operate on TEMP
			for (t in W.indices) {
				val t1 = (TEMP[7] + bigSig1(TEMP[4])
						  + ch(TEMP[4], TEMP[5], TEMP[6]) + K[t] + W[t])
				val t2 = bigSig0(TEMP[0]) + maj(TEMP[0], TEMP[1], TEMP[2])
				System.arraycopy(TEMP, 0, TEMP, 1, TEMP.size - 1)
				TEMP[4] += t1
				TEMP[0] = t1 + t2
			}
			// add values in TEMP to values in H
			for (t in H.indices) {
				H[t] += TEMP[t]
			}
			++i
		}
		return toByteArray(H)
	}
	
	/**
	 * Internal method, no need to call. Pads the given message to have a length
	 * that is a multiple of 512 bits (64 bytes), including the addition of a
	 * 1-bit, k 0-bits, and the message length as a 64-bit integer.
	 *
	 * @param message The message to pad.
	 *
	 * @return A new array with the padded message bytes.
	 */
	fun pad(message: ByteArray): ByteArray {
		val blockBits = 512
		val blockBytes = blockBits / 8
		// new message length: original + 1-bit and padding + 8-byte length
		var newMessageLength = message.size + 1 + 8
		val padBytes = (blockBytes - newMessageLength % blockBytes) % blockBytes
		newMessageLength += padBytes
		// copy message to extended array
		val paddedMessage = ByteArray(newMessageLength)
		System.arraycopy(message, 0, paddedMessage, 0, message.size)
		// write 1-bit
		paddedMessage[message.size] = 128.toByte()
		// skip padBytes many bytes (they are already 0)
		// write 8-byte integer describing the original message length
		val lenPos = message.size + 1 + padBytes
		ByteBuffer.wrap(paddedMessage, lenPos, 8).putLong(message.size * 8.toLong())
		return paddedMessage
	}
	
	/**
	 * Converts the given byte array into an int array via big-endian conversion
	 * (4 bytes become 1 int).
	 *
	 * @param bytes The source array.
	 *
	 * @return The converted array.
	 */
	fun toIntArray(bytes: ByteArray): IntArray {
		require(bytes.size % Integer.BYTES == 0) { "byte array length" }
		val buf = ByteBuffer.wrap(bytes)
		val result = IntArray(bytes.size / Integer.BYTES)
		for (i in result.indices) {
			result[i] = buf.int
		}
		return result
	}
	
	/**
	 * Converts the given int array into a byte array via big-endian conversion
	 * (1 int becomes 4 bytes).
	 *
	 * @param ints The source array.
	 *
	 * @return The converted array.
	 */
	fun toByteArray(ints: IntArray): ByteArray {
		val buf = ByteBuffer.allocate(ints.size * Integer.BYTES)
		for (i in ints.indices) {
			buf.putInt(ints[i])
		}
		return buf.array()
	}
	
	private fun ch(x: Int, y: Int, z: Int): Int {
		return x and y or (x.inv() and z)
	}
	
	private fun maj(x: Int, y: Int, z: Int): Int {
		return x and y or (x and z) or (y and z)
	}
	
	private fun bigSig0(x: Int): Int {
		return (Integer.rotateRight(x, 2) xor Integer.rotateRight(x, 13) xor Integer.rotateRight(x, 22))
	}
	
	private fun bigSig1(x: Int): Int {
		return (Integer.rotateRight(x, 6) xor Integer.rotateRight(x, 11) xor Integer.rotateRight(x, 25))
	}
	
	private fun smallSig0(x: Int): Int {
		return (Integer.rotateRight(x, 7) xor Integer.rotateRight(x, 18) xor (x ushr 3))
	}
	
	private fun smallSig1(x: Int): Int {
		return (Integer.rotateRight(x, 17) xor Integer.rotateRight(x, 19) xor (x ushr 10))
	}
}