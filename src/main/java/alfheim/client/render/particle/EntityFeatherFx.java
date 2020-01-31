package alfheim.client.render.particle;

import alfheim.api.ModInfo;
import cpw.mods.fml.relauncher.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Queue;
import java.util.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author riskyken
 */
@SideOnly(Side.CLIENT)
public class EntityFeatherFx extends EntityFX {

    private static final ResourceLocation featherParticles = new ResourceLocation(ModInfo.MODID, "textures/misc/feather.png");
    private static Queue<EntityFeatherFx> featherRenderQueue = new ArrayDeque();
    
    private float rotationSpeed;
    float f0;
    float f1;
    float f2;
    float f3;
    float f4;
    float f5;

    public EntityFeatherFx(World world, double x, double y, double z, int colour, float wingScale, float lifetime) {
        super(world, x, y, z);

        this.posX = x;
        this.posY = y;
        this.posZ = z;

        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;

        this.particleScale = wingScale;
        
        Color c = new Color(colour);
        float red = (float) c.getRed() / 255;
        float green = (float) c.getGreen() / 255;
        float blue = (float) c.getBlue() / 255;
        this.particleRed = red;
        this.particleGreen = green;
        this.particleBlue = blue;
        
        particleMaxAge = (int) (50 * lifetime);

        this.motionX = (rand.nextFloat() - 0.5F) * 0.01F;
        this.motionZ = (rand.nextFloat() - 0.5F) * 0.01F;
        
        this.motionY = -0.03F;
        this.rotationSpeed = 2.0F + rand.nextFloat();
        this.rotationPitch = rand.nextFloat() * 20 - 10;
        if (rand.nextFloat() >= 0.5F) {
            this.rotationSpeed = -this.rotationSpeed;
        }
        this.particleGravity = 0;
        
        particleTextureIndexX = 0;
        particleTextureIndexY = 1;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.isCollidedVertically) {
            this.rotationSpeed = 0F;
        }

        this.motionY = -0.085F;
        this.rotationPitch += rotationSpeed;
        if (this.rotationPitch > 360F) {
            this.rotationPitch -= 360F;
        }
        if (particleMaxAge - particleAge < 50) {
            particleAlpha = 1 + -((float) (particleAge - particleMaxAge + 50) / 50);
        }
    }

    @Override
    public void renderParticle(Tessellator tessellator, float f0, float f1, float f2, float f3, float f4, float f5) {
        this.f0 = f0;
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
        this.f4 = f4;
        this.f5 = f5;
        featherRenderQueue.add(this);
    }
    
    public static void renderQueue() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        Minecraft.getMinecraft().renderEngine.bindTexture(featherParticles);
        for(EntityFeatherFx featherFx : featherRenderQueue) {
            featherFx.postRender();
        }
        
        featherRenderQueue.clear();
        glDisable(GL_BLEND);
    }
    
    public void postRender() {
        if (isDead)
            return;
        
        Tessellator.instance.setBrightness(getBrightnessForRender(0));

        float x = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)f0 - interpPosX);
        float y = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)f0 - interpPosY);
        float z = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f0 - interpPosZ);
        
        GL11.glPushMatrix();
        Tessellator.instance.startDrawingQuads();
        drawBillboard(x, y, z, 0, 0, 1, 1, rotationPitch);
        Tessellator.instance.draw();
        GL11.glPopMatrix();
    }
    
    private void drawBillboard(double x, double y, double z, double x1, double y1, double x2, double y2, float rotation) {
        RenderManager renderManager = RenderManager.instance;
        //GL11.glPushMatrix();
        float scale = 0.05F;

        GL11.glTranslatef((float) x, (float) y, (float) z);
        GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(180, 0, 0, 1);

        GL11.glTranslatef(-0.01F, -0.01F, -0.01F);
        GL11.glRotatef(rotation, 0, 0, 1);
        GL11.glTranslatef(0.01F, 0.01F, 0.01F);
        GL11.glScalef(scale, scale, scale);
        GL11.glScalef(particleScale, particleScale, particleScale);

        Tessellator.instance.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha * 0.85f);
        Tessellator.instance.addVertexWithUV(-1, -1, 0, x1, y1);
        Tessellator.instance.addVertexWithUV(-1, 1, 0, x1, y2);
        Tessellator.instance.addVertexWithUV(1, 1, 0, x2, y2);
        Tessellator.instance.addVertexWithUV(1, -1, 0, x2, y1);
        //GL11.glPopMatrix();
    }
}
