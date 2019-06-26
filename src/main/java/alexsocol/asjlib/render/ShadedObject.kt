package alexsocol.asjlib.render

import org.lwjgl.opengl.GL11.*

import java.nio.FloatBuffer
import java.util.ArrayList

import org.lwjgl.BufferUtils
import org.lwjgl.util.vector.Matrix4f

import net.minecraft.util.ResourceLocation

abstract class ShadedObject(val shaderID: Int, val materialID: Int, val texture: ResourceLocation): Comparable<ShadedObject> {
	val translations = ArrayList<Matrix4f>()
	
	abstract fun preRender()
	
	fun addTranslation() {
		glGetFloat(GL_MODELVIEW_MATRIX, usableBuffer)
		translations.add(Matrix4f().load(usableBuffer) as Matrix4f)
		usableBuffer.clear()
	}
	
	abstract fun drawMesh()
	
	fun doRender() {
		glMatrixMode(GL_MODELVIEW)
		glPushMatrix()
		for (translation in translations) {
			glLoadIdentity()
			translation.store(usableBuffer)
			usableBuffer.flip()
			glMultMatrix(usableBuffer)
			usableBuffer.clear()
			drawMesh()
		}
		glPopMatrix()
	}
	
	abstract fun postRender()
	
	override fun compareTo(ro: ShadedObject): Int {
		if (shaderID < ro.shaderID) return -1
		if (shaderID > ro.shaderID) return 1
		if (materialID < ro.materialID) return -1
		return if (materialID > ro.materialID) 1 else 0
		//if (texture != ro.texture) return -1;
	}
	
	override fun equals(obj: Any?): Boolean {
		return if (obj is ShadedObject) compareTo((obj as ShadedObject?)!!) == 0 else super.equals(obj)
	}
	
	companion object {
		private val usableBuffer = BufferUtils.createFloatBuffer(32)
	}
}