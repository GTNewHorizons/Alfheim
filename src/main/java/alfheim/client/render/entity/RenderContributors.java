package alfheim.client.render.entity;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alexsocol.asjlib.render.ASJShaderHelper;
import alexsocol.asjlib.render.ASJShaderHelper.ShaderCallback;
import alexsocol.asjlib.render.RenderPostShaders;
import alexsocol.asjlib.render.ShadedObject;
import alfheim.api.ModInfo;
import alfheim.api.lib.LibResourceLocations;
import alfheim.api.lib.LibShaderIDs;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.core.util.AlfheimConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.common.core.handler.ConfigHandler;

public class RenderContributors {

	public static EffectRenderer effectRenderer = new EffectRenderer(Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().renderEngine);
	public static IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(ModInfo.MODID, "model/wing.obj"));
	public static ShaderCallback callback = new ShaderCallback() {
		@Override
		public void call(int shaderID) {
			TextureManager r = Minecraft.getMinecraft().renderEngine;
			
			int explosionUniform = glGetUniformLocation(shaderID, "explosion");
			int gravelUniform = glGetUniformLocation(shaderID, "gravel");
			
			OpenGlHelper.setActiveTexture(GL_TEXTURE0); 
			r.bindTexture(LibResourceLocations.explosion);
			glUniform1i(explosionUniform, 0);

			OpenGlHelper.setActiveTexture(GL_TEXTURE0 + ConfigHandler.glSecondaryTextureUnit);
			glEnable(GL_TEXTURE_2D);
			glGetInteger(GL_TEXTURE_BINDING_2D);
			r.bindTexture(LibResourceLocations.gravel);
			glUniform1i(gravelUniform, ConfigHandler.glSecondaryTextureUnit);
			
			OpenGlHelper.setActiveTexture(GL_TEXTURE0); 
		}
	};
	
	public static ShadedObject so = new ShadedObject(ShaderHelper.halo, RenderPostShaders.getNextAvailableRenderObjectMaterialID(), LibResourceLocations.babylon) {
		
		@Override
		public void preRender() {
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glDisable(GL_CULL_FACE);
			glShadeModel(GL_SMOOTH);
		}
		
		@Override
		public void drawMesh() {
			Tessellator tes = Tessellator.instance;
			tes.startDrawingQuads();
			tes.addVertexWithUV(-1, 0, -1, 0, 0);
			tes.addVertexWithUV(-1, 0, 1, 0, 1);
			tes.addVertexWithUV(1, 0, 1, 1, 1);
			tes.addVertexWithUV(1, 0, -1, 1, 0);
			tes.draw();
		}
		
		@Override
		public void postRender() {
			glShadeModel(GL_FLAT);
			glEnable(GL_CULL_FACE);
			glDisable(GL_BLEND);
		}
	};
	
	static {
		RenderPostShaders.registerShadedObject(so);
	}
	
	public static void render(RenderWorldLastEvent e) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		EntityPlayer author = Minecraft.getMinecraft().theWorld.getPlayerEntityByName("AlexSocol");
		EntityPlayer lore = Minecraft.getMinecraft().theWorld.getPlayerEntityByName("DmitryWS");
		EntityPlayer tester = Minecraft.getMinecraft().theWorld.getPlayerEntityByName("KAIIIAK");

		AlexSocol: if (author != null) {
			if (player.equals(author) && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) break AlexSocol;
			if (player.equals(author) && !AlfheimConfig.fancies) break AlexSocol;
			//if (author.isPotionActive(Potion.invisibility) || author.isPotionActive(AlfheimRegistry.leftFlame)) break AlexSocol; // FIXME more invis checks
			
			glPushMatrix();
			
			if (!player.equals(author)) {
				ASJUtilities.interpolatedTranslation(author);
				ASJUtilities.interpolatedTranslationReverse(player);
				glTranslated(0, 1.5 + Minecraft.getMinecraft().thePlayer.eyeHeight, 0);
			}
			
			glRotated(-90, 1, 0, 0);
			
			if (!(author.isRiding() && author.ridingEntity instanceof EntityHorse)) {
				glRotated(-ASJUtilities.interpolate(author.prevRenderYawOffset, author.renderYawOffset), 0, 0, 1);
			} else {
				glRotated(-ASJUtilities.interpolate(author.prevRotationYaw, author.rotationYaw), 0, 0, 1);
			}
			
			if(author.isSneaking()) {
				glRotated(28.64789, 1, 0, 0);
				glTranslated(0, 0.05, -0.5);
			} else glTranslated(0, 0.15, -0.5);
			
			{ // ring
				glPushMatrix();
				glRotated((Minecraft.getMinecraft().theWorld.getTotalWorldTime() / 2.0) + e.partialTicks, 0, 1, 0);
				glScaled(0.2, 0.2, 0.2);
		
				so.addTranslation();
				glPopMatrix();
			}
			
			{ // wings
				glPushMatrix();
				Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.mine3);
				glColor3d(0.1, 0.1, 0.1);
				
				glRotated(90, 1, 0, 0);
				double s = 2, spd = 0.5;
				glScaled(s, s, s);
				
				author.sendPlayerAbilities();
				boolean flying = author.capabilities.isFlying;
				double ry = (flying ? 30 : 15) + (Math.sin((author.ticksExisted + Minecraft.getMinecraft().timer.renderPartialTicks) * spd * (flying ? 0.4 : 0.2)) + 0.5) * (flying ? 25. : 5.);
				
				{ // bones
					glPushMatrix();
					glRotated(-ry, 0, 1, 0);
					glTranslated(-0.35, 0.05, 0.065);
					model.renderAllExcept("leather");
					glPopMatrix();
					
					glPushMatrix();
					glRotated(180, 0, 1, 0);
					glRotated(ry, 0, 1, 0);
					glTranslated(-0.35, 0.05, -0.065);
					model.renderAllExcept("leather");	
					glPopMatrix();
				}
				
				glColor3d(1, 1, 1);
				ASJShaderHelper.useShader(LibShaderIDs.idFire, callback);
				
				{ // leather
					glPushMatrix();
					glRotated(-ry, 0, 1, 0);
					glTranslated(-0.35, 0.05, 0.065);
					
					model.renderPart("leather");
					glPopMatrix();
					
					glPushMatrix();
					glRotated(180, 0, 1, 0);
					glRotated(ry, 0, 1, 0);
					glTranslated(-0.35, 0.05, -0.065);
					
					model.renderPart("leather");
					glPopMatrix();
				}
				
				ASJShaderHelper.releaseShader();
				glColor3d(1, 1, 1);
				
				double off = 0;
				
				for (int c = 0; c < 8; c++) {
					for (int i = -1; i < 2; i += 2) {
						off = (0.1 + Math.random() * 1.8);
						wispFX(Minecraft.getMinecraft().theWorld,
								// pos
								0, -.3 + (off > 0.8 ? (1.9 - off) / 2 + Math.sin(off) - 1 : off / 4 - 0.1), 0,
								// rgb
								1, /*(float) Math.random() **/ 0.25F, /*(float) Math.random() **/ 0.075F,
								// size
								0.05F + (float) Math.random() * 0.05F,
								// motion
								0, -0.005F - (float)(Math.random() * 0.01), 0,
								// max age
								2,
								// offset
								off * i);
					}
				}
				
				effectRenderer.updateEffects();
				if (Minecraft.getMinecraft().entityRenderer.debugViewDirection == 0)
					effectRenderer.renderParticles(player, Minecraft.getMinecraft().timer.renderPartialTicks);
				
				glPopMatrix();
			}
			
			glPopMatrix();
		}
		
		DmitryWS: if (lore != null) {
			if (player.equals(lore) && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) break DmitryWS;
			if (player.equals(lore) && !AlfheimConfig.fancies) break DmitryWS;
			if (lore.isPotionActive(Potion.invisibility) || author.isPotionActive(AlfheimRegistry.leftFlame)) break DmitryWS; // FIXME more invis checks
			
			glPushMatrix();
			glEnable(GL_CULL_FACE);
			float t = Minecraft.getMinecraft().theWorld.getTotalWorldTime() + e.partialTicks;
			
			if (!player.equals(lore)) {
				ASJUtilities.interpolatedTranslation(lore);
				ASJUtilities.interpolatedTranslationReverse(player);
				glTranslated(0, 1.75 + Minecraft.getMinecraft().thePlayer.eyeHeight, 0);
			}
			
			glTranslated(0, 1.5 -(0.9 + Math.sin(t / 20) * 0.025), 0);
			glRotated(-ASJUtilities.interpolate(lore.prevRotationYaw, lore.rotationYaw), 0, 1, 0);
			glRotated(-90, 0, 1, 0);
			glRotated(60, 0, 0, 1);
			Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.lexica);
			ModelBook model = new ModelBook();
			model.render((Entity)null, 0, 0, 0.95F, 1, 0.0F, 0.0625F);
			glPopMatrix();
		}
		
		KAIIIAK: if (tester != null) {
			if (player.equals(tester) && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) break KAIIIAK;
			if (player.equals(tester) && !AlfheimConfig.fancies) break KAIIIAK;
			if (tester.isPotionActive(Potion.invisibility) || tester.isPotionActive(AlfheimRegistry.leftFlame)) break KAIIIAK; // FIXME more invis checks
			
			glPushMatrix();
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glDisable(GL_CULL_FACE);
			glShadeModel(GL_SMOOTH);
			
			if (!player.equals(tester)) {
				ASJUtilities.interpolatedTranslation(tester);
				ASJUtilities.interpolatedTranslationReverse(player);
			} else {
				glTranslated(0, -1.5, 0);
			}
			
			glRotated((Minecraft.getMinecraft().theWorld.getTotalWorldTime() / 2.0) + e.partialTicks, 0, 1, 0);
			glScaled(2, 2, 2);
			
			Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.aura);
			Tessellator tes = Tessellator.instance;
			tes.startDrawingQuads();
			tes.addVertexWithUV(-1, 0, -1, 0, 0);
			tes.addVertexWithUV(-1, 0, 1, 0, 1);
			tes.addVertexWithUV(1, 0, 1, 1, 1);
			tes.addVertexWithUV(1, 0, -1, 1, 0);
			tes.draw();
			
			glShadeModel(GL_FLAT);
			glEnable(GL_CULL_FACE);
			glDisable(GL_BLEND);
			glPopMatrix();
		}
	}
	
	private static void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul, double offset) {
		if(!doParticle(world))
			return;

		FXWispBound wisp = new FXWispBound(world, x, y, z, size, r, g, b, true, true, maxAgeMul, offset);
		wisp.motionX = motionx;
		wisp.motionY = motiony;
		wisp.motionZ = motionz;

		effectRenderer.addEffect(wisp);
	}

	private static boolean doParticle(World world) {
		if(!world.isRemote)
			return false;

		if(!ConfigHandler.useVanillaParticleLimiter)
			return true;

		float chance = 1F;
		if(Minecraft.getMinecraft().gameSettings.particleSetting == 1)
			chance = 0.6F;
		else if(Minecraft.getMinecraft().gameSettings.particleSetting == 2)
			chance = 0.2F;

		return chance == 1F || Math.random() < chance;
	}
}