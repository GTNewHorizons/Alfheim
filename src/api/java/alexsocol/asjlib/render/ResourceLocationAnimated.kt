package alexsocol.asjlib.render

import alexsocol.asjlib.*
import net.minecraft.client.renderer.texture.TextureUtil
import net.minecraft.client.resources.IResource
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.InputStream
import javax.imageio.ImageIO
import kotlin.streams.toList

class ResourceLocationAnimated: ResourceLocation {
	
	private lateinit var frameList: IntArray
	private var framerate: Int = 1
	
	private constructor(): super("textures/entity/steve.png")
	private constructor(mod: String, path: String): super(mod, path) {
		init(getResourceSafe(ResourceLocation(resourceDomain, "$resourcePath.meta"))?.inputStream, mc.resourceManager.getResource(this).inputStream)
	}
	
	private fun init(metaStream: InputStream?, imageStream: InputStream): ResourceLocationAnimated {
		val meta = metaStream?.bufferedReader()?.lines()?.toList()
		framerate = getMeta(meta, MARKER_FRAMERATE, 1)
		frameList = loadImage(getMeta(meta, MARKER_HEIGHT, 0), imageStream, getMeta(meta, MARKER_INTERPOLATION_STEPS, 1))
		return this
	}
	
	private fun getMeta(meta: List<String>?, marker: String, default: Int) = (meta?.firstOrNull { it.startsWith(marker) } ?: "$marker$default").replace(marker, "").toInt()
	
	private fun loadImage(h: Int, imageStream: InputStream, interpolationFrames: Int): IntArray {
		val image = ImageIO.read(imageStream)
		val height = if (h == 0) image.width else h
		val frameList = ArrayList<Int>()
		
		var first: BufferedImage? = null
		var prev: BufferedImage? = null
		
		val interpolate = interpolationFrames > 1
		
		for (i in 0 until image.height step height) {
			val part = image.getSubimage(0, i, image.width, height)
			
			if (interpolate) {
				if (first == null) {
					first = part
					prev = first
					put(frameList, first)
					continue
				}
				
				doInterpolation(prev!!, part, frameList, interpolationFrames, false)
				prev = part
			} else {
				put(frameList, part)
			}
		}
		
		if (interpolate) doInterpolation(prev!!, first!!, frameList, interpolationFrames, true)
		
		return frameList.toIntArray()
	}
	
	private fun doInterpolation(prev: BufferedImage, cur: BufferedImage, frameList: ArrayList<Int>, step: Int, last: Boolean) {
		val fraction = 1.0 / step
		val width = cur.width
		val height = cur.height
		
		for (i in 1 until step) {
			val interstep = BufferedImage(width, height, cur.type)
			
			for (u in 0 until width) {
				for (v in 0 until height) {
					interstep.setRGB(u, v, interpolateColor(prev.getRGB(u, v), cur.getRGB(u, v), fraction * i))
				}
			}
			
			put(frameList, interstep)
		}
		
		if (!last) put(frameList, cur)
	}
	
	private fun interpolateColor(color1: Int, color2: Int, fraction: Double): Int {
		val c1 = Color(color1, true)
		val c2 = Color(color2, true)
		return Color(interpolateChannel(c1.red, c2.red, fraction), interpolateChannel(c1.green, c2.green, fraction), interpolateChannel(c1.blue, c2.blue, fraction), interpolateChannel(c1.alpha, c2.alpha, fraction)).rgb
	}
	
	private fun interpolateChannel(channel1: Int, channel2: Int, fraction: Double) = ((channel2 - channel1) * fraction + channel1).I
	
	private fun put(frameList: ArrayList<Int>, part: BufferedImage) = frameList.add(TextureUtil.uploadTextureImageAllocate(GL11.glGenTextures(), part, false, false))
	
	fun getCurrentFrame() = frameList[((mc.theWorld.totalWorldTime % (framerate * frameList.size)) / framerate).I]
	
	fun bind() = GL11.glBindTexture(GL11.GL_TEXTURE_2D, getCurrentFrame())
	
	fun getResourceSafe(loc: ResourceLocation): IResource? {
		return try {
			mc.resourceManager.getResource(loc)
		} catch (e: Throwable) {
			e.printStackTrace()
			null
		}
	}
	
	companion object {
		
		private const val MARKER_FRAMERATE = "framerate="
		private const val MARKER_HEIGHT = "height="
		private const val MARKER_INTERPOLATION_STEPS = "interpolation="
		
		fun custom(metaStream: InputStream, imageStream: InputStream): ResourceLocationAnimated {
			return ResourceLocationAnimated().init(metaStream, imageStream)
		}
		
		fun local(mod: String, path: String): ResourceLocationAnimated {
			return ResourceLocationAnimated(mod, path)
		}
	}
}