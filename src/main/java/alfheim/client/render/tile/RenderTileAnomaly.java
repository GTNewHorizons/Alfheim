package alfheim.client.render.tile;

import static org.lwjgl.opengl.GL11.*;

import alfheim.api.block.tile.SubTileEntity;
import alfheim.common.block.tile.TileAnomaly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

// Render from Thaumcraft nodes by Azanor
public class RenderTileAnomaly extends TileEntitySpecialRenderer {
	
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
		if (!(tile instanceof TileAnomaly)) return;
		TileAnomaly anomaly = (TileAnomaly) tile;
		
		SubTileEntity mainSTE = anomaly.subTiles.get(anomaly.mainSubTile);
		if (mainSTE == null) return;
		
		float pt = Minecraft.getMinecraft().timer.renderPartialTicks;
		mainSTE.bindTexture();
		
		glPushMatrix();
		glAlphaFunc(GL_GREATER, 0.003921569F);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_LIGHTING);
		glDepthMask(false);
		
		glTranslated(mainSTE.x() + 0.5, mainSTE.y() + 0.5, mainSTE.z() + 0.5);
		
		int frame = (int) ((System.nanoTime() / 40000000L + x) % mainSTE.getFrames());
		
		renderFacingStrip(0, 0, 0, 0, 1, 1, mainSTE.getFrames(), mainSTE.getStrip(), frame, partialTicks, mainSTE.getColor());
		
		glDepthMask(true);
		glEnable(GL_LIGHTING);
		glDisable(GL_BLEND);
		glAlphaFunc(GL_GREATER, 0.1F);
		glPopMatrix();
		
	}
	
	public static void renderFacingStrip(double px, double py, double pz, float angle, float scale, float alpha, int frames, int strip, int frame, float partialTicks, int color) {
		if (Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer) {
			Tessellator tessellator = Tessellator.instance;
			float arX = ActiveRenderInfo.rotationX;
			float arZ = ActiveRenderInfo.rotationZ;
			float arYZ = ActiveRenderInfo.rotationYZ;
			float arXY = ActiveRenderInfo.rotationXY;
			float arXZ = ActiveRenderInfo.rotationXZ;
			EntityPlayer player = (EntityPlayer) Minecraft.getMinecraft().renderViewEntity;
			double iPX = player.prevPosX + (player.posX - player.prevPosX) * (double) partialTicks;
			double iPY = player.prevPosY + (player.posY - player.prevPosY) * (double) partialTicks;
			double iPZ = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) partialTicks;
			glTranslated(-iPX, -iPY, -iPZ);
			tessellator.startDrawingQuads();
			tessellator.setBrightness(220);
			tessellator.setColorRGBA_I(color, (int) (alpha * 255.0F));
			Vec3 v1 = Vec3.createVectorHelper((double) (-arX * scale - arYZ * scale), (double) (-arXZ * scale), (double) (-arZ * scale - arXY * scale));
			Vec3 v2 = Vec3.createVectorHelper((double) (-arX * scale + arYZ * scale), (double) (arXZ * scale), (double) (-arZ * scale + arXY * scale));
			Vec3 v3 = Vec3.createVectorHelper((double) (arX * scale + arYZ * scale), (double) (arXZ * scale), (double) (arZ * scale + arXY * scale));
			Vec3 v4 = Vec3.createVectorHelper((double) (arX * scale - arYZ * scale), (double) (-arXZ * scale), (double) (arZ * scale - arXY * scale));
			if (angle != 0.0F) {
				Vec3 f2 = Vec3.createVectorHelper(iPX, iPY, iPZ);
				Vec3 f3 = Vec3.createVectorHelper(px, py, pz);
				Vec3 f4 = f2.subtract(f3).normalize();
				QuadHelper.setAxis(f4, (double) angle).rotate(v1);
				QuadHelper.setAxis(f4, (double) angle).rotate(v2);
				QuadHelper.setAxis(f4, (double) angle).rotate(v3);
				QuadHelper.setAxis(f4, (double) angle).rotate(v4);
			}
			
			float f21 = (float) frame / (float) frames;
			float f31 = (float) (frame + 1) / (float) frames;
			float f41 = (float) strip / (float) frames;
			float f5 = ((float) strip + 1.0F) / (float) frames;
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			tessellator.addVertexWithUV(px + v1.xCoord, py + v1.yCoord, pz + v1.zCoord, (double) f31, (double) f5);
			tessellator.addVertexWithUV(px + v2.xCoord, py + v2.yCoord, pz + v2.zCoord, (double) f31, (double) f41);
			tessellator.addVertexWithUV(px + v3.xCoord, py + v3.yCoord, pz + v3.zCoord, (double) f21, (double) f41);
			tessellator.addVertexWithUV(px + v4.xCoord, py + v4.yCoord, pz + v4.zCoord, (double) f21, (double) f5);
			tessellator.draw();
		}
	}
	
	private static class QuadHelper {
		
		public final double x;
		public final double y;
		public final double z;
		public final double angle;
		
		public QuadHelper(double ang, double xx, double yy, double zz) {
			this.x = xx;
			this.y = yy;
			this.z = zz;
			this.angle = ang;
		}
		
		public static QuadHelper setAxis(Vec3 vec, double angle) {
			angle *= 0.5D;
			double d4 = (double) MathHelper.sin((float) angle);
			return new QuadHelper((double) MathHelper.cos((float) angle), vec.xCoord * d4, vec.yCoord * d4, vec.zCoord * d4);
		}
		
		public void rotate(Vec3 vec) {
			double d = -this.x * vec.xCoord - this.y * vec.yCoord - this.z * vec.zCoord;
			double d1 = this.angle * vec.xCoord + this.y * vec.zCoord - this.z * vec.yCoord;
			double d2 = this.angle * vec.yCoord - this.x * vec.zCoord + this.z * vec.xCoord;
			double d3 = this.angle * vec.zCoord + this.x * vec.yCoord - this.y * vec.xCoord;
			vec.xCoord = d1 * this.angle - d * this.x - d2 * this.z + d3 * this.y;
			vec.yCoord = d2 * this.angle - d * this.y + d1 * this.z - d3 * this.x;
			vec.zCoord = d3 * this.angle - d * this.z - d1 * this.y + d2 * this.x;
		}
	}
}