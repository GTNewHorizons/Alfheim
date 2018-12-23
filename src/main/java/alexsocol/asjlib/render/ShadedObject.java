package alexsocol.asjlib.render;

import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;

import net.minecraft.util.ResourceLocation;

public abstract class ShadedObject implements Comparable<ShadedObject> {
	
	public final int shaderID;
	public final int materialID;
	public final ResourceLocation texture;
	public ArrayList<Matrix4f> translations = new ArrayList<Matrix4f>();
	private static FloatBuffer usableBuffer = BufferUtils.createFloatBuffer(32);
	
	public ShadedObject(int shader, int material, ResourceLocation text) {
		shaderID = shader;
		materialID = material;
		texture = text; 
	}
	
	public abstract void preRender();
	
	public final void addTranslation() {
		glGetFloat(GL_MODELVIEW_MATRIX, usableBuffer);
		translations.add((Matrix4f) new Matrix4f().load(usableBuffer));
		usableBuffer.clear();
	}
	
	public abstract void drawMesh();
	
	public final void doRender() {
		glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
		for (Matrix4f translation : translations) {
			glLoadIdentity();
			translation.store(usableBuffer);
			usableBuffer.flip();
			glMultMatrix(usableBuffer);
			usableBuffer.clear();
			drawMesh();
		}
		glPopMatrix();
	}
	
	public abstract void postRender();

	@Override
	public final int compareTo(ShadedObject ro) {
		if (shaderID < ro.shaderID) return -1;
		if (shaderID > ro.shaderID) return 1;
		if (materialID < ro.materialID) return -1;
		if (materialID > ro.materialID) return 1;
		//if (texture != ro.texture) return -1;
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ShadedObject) return compareTo((ShadedObject) obj) == 0; 
		return super.equals(obj);
	}
}