package alexsocol.asjlib.render

import alfheim.api.ModInfo
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.shader.Framebuffer
import net.minecraft.util.ResourceLocation
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.*
import java.awt.image.BufferedImage
import java.io.*
import javax.imageio.ImageIO

// Utility class to process images with shaders (not for use in mod itself)
object GLSL_to_Image {
	
	const val TEXTURE_WIDTH = 0x200
	const val TEXTURE_HEIGHT = 0x200
	
	var shaderId = 0
	lateinit var fb: Framebuffer
	
	fun main() {
		init()
		process()
		end()
	}
	
	fun init() {
		fb = Framebuffer(TEXTURE_WIDTH, TEXTURE_HEIGHT, false)
		fb.bindFramebuffer(true)
		shaderId = ASJShaderHelper.createProgram(null, "shaders/fbo.frag")
	}
	
	fun process() {
		ASJShaderHelper.useShader(shaderId)
		val files = File("D:\\Games\\Forge\\src\\main\\resources\\assets\\alfheim\\textures\\fbo").listFiles()
		for (file in files!!) {
			if (file.isDirectory) continue
			draw(file.name)
			
			try {
				write2(file.name)
			} catch (e: IOException) {
				System.err.println("Caught exception while writing texture.")
				e.printStackTrace()
			}
			
		}
		ASJShaderHelper.releaseShader()
	}
	
	// draw a scene to a texture directly
	fun draw(name: String) {
		glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
		Minecraft.getMinecraft().renderEngine.bindTexture(ResourceLocation(ModInfo.MODID, "textures/fbo/$name"))
		// Hate Minecraft for this took me like 3 hours to understand it
		val u = TEXTURE_WIDTH * 2.0 / Minecraft.getMinecraft().displayWidth
		val v = TEXTURE_HEIGHT * 2.0 / Minecraft.getMinecraft().displayHeight
		
		Tessellator.instance.startDrawingQuads()
		Tessellator.instance.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.0)
		Tessellator.instance.addVertexWithUV(0.0, TEXTURE_HEIGHT.toDouble(), 0.0, 0.0, v)
		Tessellator.instance.addVertexWithUV(TEXTURE_WIDTH.toDouble(), TEXTURE_HEIGHT.toDouble(), 0.0, u, v)
		Tessellator.instance.addVertexWithUV(TEXTURE_WIDTH.toDouble(), 0.0, 0.0, u, 0.0)
		Tessellator.instance.draw()
	}
	
	@Throws(IOException::class)
	fun write2(name: String) {
		glPixelStorei(GL_PACK_ALIGNMENT, 1)
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
		val buffer = BufferUtils.createIntBuffer(TEXTURE_WIDTH * TEXTURE_HEIGHT)
		glReadPixels(0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT, GL_BGRA, GL_UNSIGNED_INT_8_8_8_8_REV, buffer)
		val data = IntArray(TEXTURE_WIDTH * TEXTURE_HEIGHT)
		buffer.get(data)
		val image = BufferedImage(TEXTURE_WIDTH, TEXTURE_HEIGHT, BufferedImage.TYPE_INT_ARGB)
		
		for (y in 0 until TEXTURE_HEIGHT / 2) {
			for (x in 0 until TEXTURE_WIDTH) {
				val f = y * TEXTURE_WIDTH + x
				val s = (TEXTURE_HEIGHT - y - 1) * TEXTURE_WIDTH + x
				val tmp = data[s]
				data[s] = data[f]
				data[f] = tmp
			}
		}
		
		image.setRGB(0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT, data, 0, TEXTURE_WIDTH)
		
		ImageIO.write(image, "PNG", File("D:\\Games\\Forge\\src\\main\\resources\\assets\\alfheim\\textures\\fbo\\out_$name"))
	}
	
	fun end() {
		fb.unbindFramebuffer()
		ASJShaderHelper.releaseShader()
	}
}