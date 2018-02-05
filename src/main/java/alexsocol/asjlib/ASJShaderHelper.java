package alexsocol.asjlib;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import cpw.mods.fml.common.Loader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

/**
 * Almost all code is by Vazkii, I just ported it to GL20 and made lib-style
 * */
public final class ASJShaderHelper {

	private static final int FRAG = GL_FRAGMENT_SHADER;
	private static final int VERT = GL_VERTEX_SHADER;

	public static void useShader(int proramID, ShaderCallback callback) {
		if(!OpenGlHelper.shadersSupported) return;

		glUseProgram(proramID);

		if(proramID != 0) {
			int time = glGetUniformLocation(proramID, "time");
			glUniform1f(time, Minecraft.getMinecraft().theWorld.getTotalWorldTime() / 20.0F);

			if(callback != null)
				callback.call(proramID);
		}
	}

	public static void useShader(int shaderID) {
		useShader(shaderID, null);
	}

	public static void releaseShader() {
		useShader(0);
	}

	// Most of the code taken from the LWJGL wiki
	// http://lwjgl.org/wiki/index.php?title=GLSL_Shaders_with_LWJGL
	/**
	 * Creates shader bundle for future using.
	 * Put your shaders to <b>/assets/modid/</b>.
	 * @param vertLocation Vertex shader location
	 * @param fragLocation Fragment shader location
	 * */
	public static int createProgram(String vertLocation, String fragLocation) {
		int vertID = 0, geomID = 0, fragID = 0, programID = 0;
		if(vertLocation != null)
			vertID = createShader(vertLocation, VERT);
		if(fragLocation != null)
			fragID = createShader(fragLocation, FRAG);

		programID = glCreateProgram();
		if(programID == 0)
			return 0;

		if(vertLocation != null)
			glAttachShader(programID, vertID);
		if(fragLocation != null)
			glAttachShader(programID, fragID);

		glLinkProgram(programID);
		if(glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE) {
			glDeleteProgram(programID);
			throw new RuntimeException("Error Linking program: " + getProgramLogInfo(programID));
		}

		glValidateProgram(programID);
		if (glGetProgrami(programID, GL_VALIDATE_STATUS) == GL_FALSE) {
			glDeleteProgram(programID);
			throw new RuntimeException("Error Validating program: " + getProgramLogInfo(programID));
		}

		return programID;
	}

	private static int createShader(String filename, int shaderType){
		int shaderID = 0;
		try {
			shaderID = glCreateShader(shaderType);

			if(shaderID == 0)
				return 0;

			glShaderSource(shaderID, readFileAsString(filename));
			glCompileShader(shaderID);

			if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE)
				throw new RuntimeException("Error Compiling shader: " + getShaderLogInfo(shaderID));

			return shaderID;
		}
		catch(Exception e) {
			glDeleteShader(shaderID);
			e.printStackTrace();
			return -1;
		}
	}

	private static String getShaderLogInfo(int obj) {
		return glGetShaderInfoLog(obj, GL_INFO_LOG_LENGTH);
	}

	private static String getProgramLogInfo(int obj) {
		return glGetProgramInfoLog(obj, GL_INFO_LOG_LENGTH);
	}

	private static String readFileAsString(String filename) throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(Loader.instance().activeModContainer().getModId(), filename)).getInputStream(), "UTF-8"));
		StringBuilder source = new StringBuilder();
		while (in.ready()) {
			source.append(in.readLine()).append("\r\n");
		}
		return source.toString();
	}

	public static abstract class ShaderCallback {

		public abstract void call(int shader);

	}
}
