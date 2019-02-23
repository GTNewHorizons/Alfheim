package alfheim.client.render.entity;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

import org.lwjgl.opengl.GL11;

import alexsocol.asjlib.render.ASJShaderHelper;
import alexsocol.asjlib.render.ASJShaderHelper.ShaderCallback;
import alexsocol.asjlib.render.RenderPostShaders;
import alexsocol.asjlib.render.ShadedObject;
import alfheim.api.ModInfo;
import alfheim.api.lib.LibResourceLocations;
import alfheim.api.lib.LibShaderIDs;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.item.material.ItemElvenResource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import vazkii.botania.api.item.IBaubleRender.Helper;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;

public class RenderContributors {
	
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
	
	public static void render(RenderPlayerEvent.Specials.Post e, EntityPlayer player) {
		if (player.isInvisible() || player.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer) || player.isPotionActive(Potion.invisibility)) return;
		
		AlexSocol: if (player.getCommandSenderName().equals("AlexSocol")) {
			if (player.equals(Minecraft.getMinecraft().thePlayer) && (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) || !AlfheimConfig.fancies) break AlexSocol;
			
			{ // ring
				glPushMatrix();
				glRotated(90, 1, 0, 0);
				Helper.rotateIfSneaking(player);
				glTranslated(0, 0.15, -0.25);
				
				glRotated((Minecraft.getMinecraft().theWorld.getTotalWorldTime() / 2.0) + Minecraft.getMinecraft().timer.renderPartialTicks, 0, 1, 0);
				glScaled(0.2, 0.2, 0.2);
				
				so.addTranslation();
				glPopMatrix();
			}
			
			{ // wings
				if (AlfheimConfig.minimalGraphics) {
					IIcon icon = ItemElvenResource.wing;
					Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
					
					boolean flying = player.capabilities.isFlying;
					
					float rz = 120F;
					float rx = 20F + (float) ((Math.sin((double) (player.ticksExisted + Minecraft.getMinecraft().timer.renderPartialTicks) * (flying ? 0.4F : 0.2F)) + 0.5F) * (flying ? 30F : 5F));
					float ry = 0F;
					float h = 0.2F;
					float i = 0.15F;
					
					glPushMatrix();
					glEnable(GL_BLEND);
					glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
					glColor4d(1, 1, 1, 1);
					
					int light = 15728880;
					int lightmapX = light % 65536;
					int lightmapY = light / 65536;
					
					float f = icon.getMinU();
					float f1 = icon.getMaxU();
					float f2 = icon.getMinV();
					float f3 = icon.getMaxV();
					
					Helper.rotateIfSneaking(player);
					
					glTranslatef(0F, h, i);
					
					glRotatef(rz, 0F, 0F, 1F);
					glRotatef(rx, 1F, 0F, 0F);
					glRotatef(ry, 0F, 1F, 0F);
					ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 32F);
					glRotatef(-ry, 0F, 1F, 0F);
					glRotatef(-rx, 1F, 0F, 0F);
					glRotatef(-rz, 0F, 0F, 1F);
					
					glScalef(-1F, 1F, 1F);
					glRotatef(rz, 0F, 0F, 1F);
					glRotatef(rx, 1F, 0F, 0F);
					glRotatef(ry, 0F, 1F, 0F);
					ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 32F);
					glRotatef(-ry, 1F, 0F, 0F);
					glRotatef(-rx, 1F, 0F, 0F);
					glRotatef(-rz, 0F, 0F, 1F);
					
					glColor4d(1, 1, 1, 1);
					glPopMatrix();
				} else {
					glPushMatrix();
					Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.mine3);
					glColor3d(0.1, 0.1, 0.1);
					
					glRotated(90, 1, 0, 0);
					Helper.rotateIfSneaking(player);
					glTranslated(0, 0.15, -0.25);
					
					glRotated(90, 1, 0, 0);
					double s = 2, spd = 0.5;
					glScaled(s, s, s);
					
					player.sendPlayerAbilities();
					boolean flying = player.capabilities.isFlying;
					double ry = (flying ? 30 : 15) + (Math.sin((player.ticksExisted + Minecraft.getMinecraft().timer.renderPartialTicks) * spd * (flying ? 0.4 : 0.2)) + 0.5) * (flying ? 25. : 5.);
					
					{ // bones
						glPushMatrix();
						glRotated(-ry, 0, 1, 0);
						glTranslated(-0.35, 0.05, 0.025);
						model.renderAllExcept("leather");
						glPopMatrix();
						
						glPushMatrix();
						glRotated(180, 0, 1, 0);
						glRotated(ry, 0, 1, 0);
						glTranslated(-0.35, 0.05, -0.025);
						model.renderAllExcept("leather");	
						glPopMatrix();
					}
					
					glColor3d(1, 1, 1);
					glDisable(GL_CULL_FACE);
					if (ConfigHandler.useShaders)
						ASJShaderHelper.useShader(LibShaderIDs.idFire, callback);
					else {
						glDisable(GL_LIGHTING);
						OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
						Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.explosion);
					}
					
					{ // leather
						glPushMatrix();
						glRotated(-ry, 0, 1, 0);
						glTranslated(-0.35, 0.05, 0.025);					
						model.renderPart("leather");
						glPopMatrix();
						
						glPushMatrix();
						glRotated(180, 0, 1, 0);
						glRotated(ry, 0, 1, 0);
						glTranslated(-0.35, 0.05, -0.025);
						model.renderPart("leather");
						glPopMatrix();
					}
					
					if (ConfigHandler.useShaders) ASJShaderHelper.releaseShader();
					else glEnable(GL_LIGHTING);
					//glEnable(GL_CULL_FACE); for some reason this breaks tiara wings render -_-
					glColor4d(1, 1, 1, 1);
					glPopMatrix();
				}
			}
		}
		
		DmitryWS: if (player.getCommandSenderName().equals("DmitryWS")) {
			if (player.equals(Minecraft.getMinecraft().thePlayer) && (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) || !AlfheimConfig.fancies) break DmitryWS;
			
			glPushMatrix();
			glEnable(GL_CULL_FACE);
			double t = Math.sin((Minecraft.getMinecraft().theWorld.getTotalWorldTime() + Minecraft.getMinecraft().timer.renderPartialTicks) / 10.);
			
			glTranslated(0, -(0.9 + t * 0.05), 0);
			glRotated(180, 1, 0, 0);
			glRotated(-90, 0, 1, 0);
			glRotated(60, 0, 0, 1);
			Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.lexica);
			ModelBook model = new ModelBook();
			model.render((Entity)null, 0, 0.075F + (float) (t * 0.025), 0.925F - (float) (t * 0.025), 1, 0.0F, 0.0625F);
			glPopMatrix();
		}
		
		KAIIIAK: if (player.getCommandSenderName().equals("KAIIIAK")) {
			if (player.equals(Minecraft.getMinecraft().thePlayer) && (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) || !AlfheimConfig.fancies) break KAIIIAK;
			
			glPushMatrix();
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glDisable(GL_CULL_FACE);
			glShadeModel(GL_SMOOTH);
			
			glTranslated(0, player == Minecraft.getMinecraft().thePlayer ? 1.25 : 0.25, 0);
			glRotated((Minecraft.getMinecraft().theWorld.getTotalWorldTime() / 2.0) + Minecraft.getMinecraft().timer.renderPartialTicks, 0, 1, 0);
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
}