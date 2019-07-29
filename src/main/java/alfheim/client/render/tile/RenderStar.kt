package alfheim.client.render.tile

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import alfheim.common.block.tile.TileEntityStar
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL
import vazkii.botania.client.core.handler.ClientTickHandler
import vazkii.botania.client.core.helper.RenderHelper
import java.awt.Color
import java.util.*

/**
 * @author WireSegal
 * Created at 9:43 PM on 2/6/16.
 */
class RenderStar : TileEntitySpecialRenderer() {
    
    override fun renderTileEntityAt(tile: TileEntity, x: Double, y: Double, z: Double, partticks: Float) {
        if (tile is TileEntityStar) {
            glPushMatrix()
            glEnable(GL_BLEND)
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
            glEnable(GL_RESCALE_NORMAL)
            glColor3f(1f, 1f, 1f)
            glTranslated(x + 0.5, y + 0.5, z + 0.5)
            
            val seed = (tile.xCoord xor tile.yCoord xor tile.zCoord).toLong()
            var color = tile.getColor()
            if (color == -1) {
                var time = ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks
                time += Random(seed).nextInt(100000)
                color = Color.HSBtoRGB(time * 0.005F, 1F, 1F)
            }
            val size = tile.size
            RenderHelper.renderStar(color, size, size, size, seed)
            
            glColor3f(1f, 1f, 1f)
            glDisable(GL_BLEND)
            glDisable(GL_RESCALE_NORMAL)
            glPopMatrix()
        }
    }
}
