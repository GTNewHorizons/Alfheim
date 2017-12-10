package alfheim.client.render;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.client.renderer.OpenGlHelper;
import vazkii.botania.api.internal.ShaderCallback;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.core.handler.ConfigHandler;

public final class ShaderHelperAlfheim {

	private static final int VERT = ARBVertexShader.GL_VERTEX_SHADER_ARB;
	private static final int FRAG = ARBFragmentShader.GL_FRAGMENT_SHADER_ARB;

	public static int forcefield = 0;

	public static void initShaders() {
		if(!useShaders())
			return;

		forcefield = createProgram(null, "/assets/alfheim/shader/forcefield.frag");
	}

	public static void useShader(int proramID, ShaderCallback callback) {
		if(!useShaders())
			return;

		ARBShaderObjects.glUseProgramObjectARB(proramID);

		if(proramID != 0) {
			int time = ARBShaderObjects.glGetUniformLocationARB(proramID, "time");
			ARBShaderObjects.glUniform1fARB(time, ClientTickHandler.ticksInGame / 20.0F);

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

	private static int createProgram(String vertLocaion, String fragLocation) {
		int vertID = 0, fragID = 0, programID = 0;
		if(vertLocaion != null)
			vertID = createShader(vertLocaion, VERT);
		if(fragLocation != null)
			fragID = createShader(fragLocation, FRAG);

		programID = ARBShaderObjects.glCreateProgramObjectARB();
		if(programID == 0)
			return 0;

		if(vertLocaion != null)
			ARBShaderObjects.glAttachObjectARB(programID, vertID);
		if(fragLocation != null)
			ARBShaderObjects.glAttachObjectARB(programID, fragID);

		ARBShaderObjects.glLinkProgramARB(programID);
		if(ARBShaderObjects.glGetObjectParameteriARB(programID, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
			FMLLog.log(Level.ERROR, getLogInfo(programID));
			return 0;
		}

		ARBShaderObjects.glValidateProgramARB(programID);
		if (ARBShaderObjects.glGetObjectParameteriARB(programID, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
			FMLLog.log(Level.ERROR, getLogInfo(programID));
			return 0;
		}

		return programID;
	}

	private static int createShader(String filename, int shaderType){
		int shaderID = 0;
		try {
			shaderID = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

			if(shaderID == 0)
				return 0;

			ARBShaderObjects.glShaderSourceARB(shaderID, readFileAsString(filename));
			ARBShaderObjects.glCompileShaderARB(shaderID);

			if (ARBShaderObjects.glGetObjectParameteriARB(shaderID, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
				throw new RuntimeException("Error creating shader: " + getLogInfo(shaderID));

			return shaderID;
		}
		catch(Exception e) {
			ARBShaderObjects.glDeleteObjectARB(shaderID);
			e.printStackTrace();
			return -1;
		}
	}

	private static String getLogInfo(int obj) {
		return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
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
