package aaa.alexsocol.glsltoimage;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.OpenGLException;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.render.ASJShaderHelper;
import alfheim.api.ModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;


public class Main {

	public static final	int			TEXTURE_WIDTH	= 0x200,
									TEXTURE_HEIGHT	= 0x200;
	
	public static		int			shaderId		= 0;
	public static		Framebuffer fb;
	
	public static void main() {
		init();
		process();
		end();
	}

	public static void init() {
		fb = new Framebuffer(TEXTURE_WIDTH, TEXTURE_HEIGHT, false);
		fb.bindFramebuffer(true);	
		shaderId = ASJShaderHelper.createProgram(null, "shaders/alpha.frag");
	}
	
	public static void process() {
		ASJShaderHelper.useShader(shaderId);
		File[] files = new File("D:\\Games\\Forge\\src\\main\\resources\\assets\\alfheim\\textures\\fbo").listFiles();
		for (File file : files) {
			if (file.isDirectory()) continue;
			draw(file.getName());
			
			try {
				write2(file.getName());
			} catch (IOException e) {
				System.err.println("Caught exception while writing texture.");
				e.printStackTrace();
			}
		}
		ASJShaderHelper.releaseShader();
	}
	
	// draw a scene to a texture directly
	public static void draw(String name) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(ModInfo.MODID, "textures/fbo/" + name));
		// Hate Minecraft for this took me like 3 hours to understand it
		double u = TEXTURE_WIDTH * 2. / Minecraft.getMinecraft().displayWidth;
		double v = TEXTURE_HEIGHT * 2. / Minecraft.getMinecraft().displayHeight;
		
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.addVertexWithUV(0,				0,				0, 0, 0);
		Tessellator.instance.addVertexWithUV(0, 			TEXTURE_HEIGHT, 0, 0, v);
		Tessellator.instance.addVertexWithUV(TEXTURE_WIDTH, TEXTURE_HEIGHT, 0, u, v);
		Tessellator.instance.addVertexWithUV(TEXTURE_WIDTH, 0,				0, u, 0);
		Tessellator.instance.draw();
	}
	
	/*public static void write(String name) throws IOException {
		int format = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_INTERNAL_FORMAT);
		
		int channels = 4;
		if (format == GL_RGB)
		    channels = 3;

		ByteBuffer buffer = BufferUtils.createByteBuffer(TEXTURE_WIDTH * TEXTURE_HEIGHT * channels);
		BufferedImage image = new BufferedImage(TEXTURE_WIDTH, TEXTURE_HEIGHT, BufferedImage.TYPE_INT_ARGB);

		glGetTexImage(GL_TEXTURE_2D, 0, format, GL_UNSIGNED_BYTE, buffer);

		for (int x = 0; x < TEXTURE_WIDTH; ++x) {
		    for (int y = 0; y < TEXTURE_HEIGHT; ++y) {
		        int i = (x + y * TEXTURE_WIDTH) * channels;

		        int r = buffer.get(i) & 0xFF;
		        int g = buffer.get(i + 1) & 0xFF;
		        int b = buffer.get(i + 2) & 0xFF;
		        int a = 255;
		        if (channels == 4)
		            a = buffer.get(i + 3) & 0xFF;

		        image.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
		    }
		}
		
		ImageIO.write(image, "PNG", new File("D:\\Games\\Forge\\src\\main\\resources\\assets\\alfheim\\textures\\fbo\\out\\" + name.split("\\.")[0] + "_out.png"));
	}*/
	
	public static void write2(String name) throws IOException {
		glPixelStorei(GL_PACK_ALIGNMENT, 1);
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		IntBuffer buffer = BufferUtils.createIntBuffer(TEXTURE_WIDTH * TEXTURE_HEIGHT);
		glReadPixels(0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT, GL_BGRA, GL_UNSIGNED_INT_8_8_8_8_REV, buffer);
		int[] data = new int[TEXTURE_WIDTH * TEXTURE_HEIGHT];
		buffer.get(data);
		BufferedImage image = new BufferedImage(TEXTURE_WIDTH, TEXTURE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		
		for (int y = 0; y < TEXTURE_HEIGHT / 2; y++) {
			for (int x = 0; x < TEXTURE_WIDTH; x++) {
				int f = y * TEXTURE_WIDTH + x;
				int s = (TEXTURE_HEIGHT - y - 1) * TEXTURE_WIDTH + x;
				int tmp = data[s];
				data[s] = data[f];
				data[f] = tmp;
			}
		}
		
		image.setRGB(0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT, data, 0, TEXTURE_WIDTH);
		
		ImageIO.write(image, "PNG", new File("D:\\Games\\Forge\\src\\main\\resources\\assets\\alfheim\\textures\\fbo\\out\\" + name));
	}
	
	public static void end() {
		fb.unbindFramebuffer();
		ASJShaderHelper.releaseShader();
	}
}