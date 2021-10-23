package alexsocol.asjlib.render

import net.minecraft.util.ResourceLocation
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.*
import org.lwjgl.util.vector.Matrix4f
import java.util.*

abstract class ShadedObject(val shaderID: Int, val materialID: Int, val texture: ResourceLocation?): Comparable<ShadedObject> {
	
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
	
	override fun compareTo(other: ShadedObject): Int {
		if (shaderID < other.shaderID) return -1
		if (shaderID > other.shaderID) return 1
		if (materialID < other.materialID) return -1
		return if (materialID > other.materialID) 1 else 0
		//if (texture != ro.texture) return -1;
	}
	
	override fun equals(other: Any?) =
		if (other is ShadedObject) compareTo((other as ShadedObject?)!!) == 0 else super.equals(other)
	
	override fun hashCode() =
		Objects.hash(shaderID, materialID, texture)
	
	companion object {
		private val usableBuffer = BufferUtils.createFloatBuffer(32)
	}
}