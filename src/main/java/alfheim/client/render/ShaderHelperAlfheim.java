package alfheim.client.render;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import net.minecraft.client.renderer.OpenGlHelper;
import vazkii.botania.api.internal.ShaderCallback;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.core.handler.ConfigHandler;

public final class ShaderHelperAlfheim {

	private static final int VERT = GL20.GL_VERTEX_SHADER;
	private static final int FRAG = GL20.GL_FRAGMENT_SHADER;

	public static int forcefield = 0;
	public static int sphere = 0;

	public static void initShaders() {
		if(!useShaders())
			return;

		forcefield = createProgram(null, "/assets/alfheim/shader/forcefield.frag");
		sphere = createProgram("/assets/alfheim/shader/fresnel.vert", "/assets/alfheim/shader/fresnel.frag");
	}

	public static void useShader(int proramID, ShaderCallback callback) {
		if(!useShaders())
			return;

		GL20.glUseProgram(proramID);

		if(proramID != 0) {
			int time = GL20.glGetUniformLocation(proramID, "time");
			GL20.glUniform1f(time, ClientTickHandler.ticksInGame / 20.0F);

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

	public static boolean useShaders() {
		return ConfigHandler.useShaders && OpenGlHelper.shadersSupported;
	}

	// Most of the code taken from the LWJGL wiki
	// http://lwjgl.org/wiki/index.php?title=GLSL_Shaders_with_LWJGL

	public static int createProgram(String vertLocaion, String fragLocation) {
		int vertID = 0, fragID = 0, programID = 0;
		if(vertLocaion != null)
			vertID = createShader(vertLocaion, VERT);
		if(fragLocation != null)
			fragID = createShader(fragLocation, FRAG);

		programID = GL20.glCreateProgram();
		if(programID == 0)
			return 0;

		if(vertLocaion != null)
			GL20.glAttachShader(programID, vertID);
		if(fragLocation != null)
			GL20.glAttachShader(programID, fragID);

		GL20.glLinkProgram(programID);
		if(GL20.glGetShaderi(programID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
			throw new RuntimeException("Error creating shader: " + getLogInfo(programID));
		}

		GL20.glValidateProgram(programID);
		if (GL20.glGetShaderi(programID, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
			throw new RuntimeException("Error creating shader: " + getLogInfo(programID));
		}

		return programID;
	}

	private static int createShader(String filename, int shaderType){
		int shaderID = 0;
		try {
			shaderID = GL20.glCreateShader(shaderType);

			if(shaderID == 0)
				return 0;

			GL20.glShaderSource(shaderID, readFileAsString(filename));
			GL20.glCompileShader(shaderID);

			if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
				throw new RuntimeException("Error creating shader: " + getLogInfo(shaderID));

			return shaderID;
		}
		catch(Exception e) {
			GL20.glDeleteShader(shaderID);
			e.printStackTrace();
			return -1;
		}
	}

	private static String getLogInfo(int obj) {
		return GL20.glGetProgramInfoLog(obj, GL20.GL_INFO_LOG_LENGTH);
	}

	private static String readFileAsString(String filename) throws Exception {
		StringBuilder source = new StringBuilder();
		InputStream in = ShaderHelperAlfheim.class.getResourceAsStream(filename);
		Exception exception = null;
		BufferedReader reader;

		if(in == null)
			return "";

		try {
			reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

			Exception innerExc= null;
			try {
				String line;
				while((line = reader.readLine()) != null)
					source.append(line).append('\n');
			} catch(Exception exc) {
				exception = exc;
			} finally {
				try {
					reader.close();
				} catch(Exception exc) {
					if(innerExc == null)
						innerExc = exc;
					else exc.printStackTrace();
				}
			}

			if(innerExc != null)
				throw innerExc;
		} catch(Exception exc) {
			exception = exc;
		} finally {
			try {
				in.close();
			} catch(Exception exc) {
				if(exception == null)
					exception = exc;
				else exc.printStackTrace();
			}

			if(exception != null)
				throw exception;
		}

		return source.toString();
	}

}
