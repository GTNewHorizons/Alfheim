package alfheim.client.render.entity

import alexsocol.asjlib.F
import alfheim.api.ModInfo
import net.minecraft.client.model.ModelCreeper
import net.minecraft.client.renderer.entity.RenderCreeper
import net.minecraft.entity.Entity
import net.minecraft.entity.monster.EntityCreeper
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11.*

object RenderEntityGrieferCreeper: RenderCreeper() {
	
	private val armoredCreeperTextures = ResourceLocation("${ModInfo.MODID}:textures/model/entity/griefer_creeper/creeper_armor.png")
	private val creeperTextures = ResourceLocation("${ModInfo.MODID}:textures/model/entity/griefer_creeper/griefer_creeper.png")
	
	/** The creeper model.  */
	private val creeperModel = ModelCreeper(2f)
	
	override fun shouldRenderPass(entity: EntityCreeper, pass: Int, par3Float: Float): Int {
		if (entity.powered) {
			if (entity.isInvisible) {
				glDepthMask(false)
			} else {
				glDepthMask(true)
			}
			
			if (pass == 1) {
				val f1 = entity.ticksExisted.F + par3Float
				bindTexture(armoredCreeperTextures)
				glMatrixMode(GL_TEXTURE)
				glLoadIdentity()
				val f2 = f1 * 0.01f
				val f3 = f1 * 0.01f
				glTranslatef(f2, f3, 0f)
				setRenderPassModel(creeperModel)
				glMatrixMode(GL_MODELVIEW)
				glEnable(GL_BLEND)
				val f4 = 0.5f
				glColor4f(f4, f4, f4, 1f)
				glDisable(GL_LIGHTING)
				glBlendFunc(GL_ONE, GL_ONE)
				return 1
			}
			
			if (pass == 2) {
				glMatrixMode(GL_TEXTURE)
				glLoadIdentity()
				glMatrixMode(GL_MODELVIEW)
				glEnable(GL_LIGHTING)
				glDisable(GL_BLEND)
			}
		}
		
		return -1
	}
	
	override fun getEntityTexture(entity: Entity?) = creeperTextures
	override fun getEntityTexture(entity: EntityCreeper?) = creeperTextures
}
