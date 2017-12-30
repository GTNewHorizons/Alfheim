package alfheim.client.registry;

import alfheim.client.render.ASJShaderHelper;

public class AflheimClientRegistry {
	
	public static int fireball = 0;
	public static int forcefield = 0;
	public static int sphere = 0;
	public static int star = 0;
	
	public static void init() {
		registerShaders();
	}
	
	private static void registerShaders() {
		fireball = ASJShaderHelper.createProgram("/assets/alfheim/shader/fireball.vert", "/assets/alfheim/shader/fireball.frag");
		forcefield = ASJShaderHelper.createProgram(null, "/assets/alfheim/shader/forcefield.frag");
		sphere = ASJShaderHelper.createProgram("/assets/alfheim/shader/fresnel.vert", "/assets/alfheim/shader/fresnel.frag");
		star = ASJShaderHelper.createProgram(null, "/assets/alfheim/shader/star.frag");
	}
}
