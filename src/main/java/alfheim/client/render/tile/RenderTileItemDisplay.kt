package alfheim.client.render.tile

import alfheim.common.block.tile.TileItemDisplay
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemBlock
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.client.ForgeHooksClient
import org.lwjgl.opengl.GL11.*
import vazkii.botania.client.core.handler.ClientTickHandler
import java.awt.Color
import kotlin.math.sin

@SideOnly(Side.CLIENT)
class RenderTileItemDisplay : TileEntitySpecialRenderer() {
    internal var renderBlocks = RenderBlocks()

    fun renderEntity(display: TileItemDisplay?, x: Double, y: Double, z: Double, partticks: Float) {
        val seed = Minecraft.getMinecraft()

        if (display != null && display.worldObj != null && seed != null) {

            glPushMatrix()
            glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
            glTranslated(x, y, z)

            val var27 = (ClientTickHandler.ticksInGame.toFloat() + partticks).toDouble()

            glPushMatrix()
            glScalef(0.5f, 0.5f, 0.5f)
            glTranslatef(1.0f, 1.25f, 1.0f)
            glRotatef(360f + var27.toFloat(), 0.0f, 1.0f, 0.0f)
            glTranslatef(0.0f, 0.0f, 0.5f)
            glRotatef(90.0f, 0.0f, 1.0f, 0.0f)
            glTranslated(0.0, 0.15 * sin(var27 / 7.5), 0.0)
            val scale = display.getStackInSlot(0)

            if (scale != null) {
                seed.renderEngine.bindTexture(if (scale.item is ItemBlock) TextureMap.locationBlocksTexture else TextureMap.locationItemsTexture)
                glScalef(2.0f, 2.0f, 2.0f)
                glTranslatef(0.25f, 0f, 0f)
                if (!ForgeHooksClient.renderEntityItem(EntityItem(display.worldObj, display.xCoord.toDouble(), display.yCoord.toDouble(), display.zCoord.toDouble(), scale), scale, 0.0f, 0.0f, display.worldObj.rand, seed.renderEngine, renderBlocks, 1)) {
                    glTranslatef(-0.25f, 0f, 0f)
                    glScalef(0.5f, 0.5f, 0.5f)
                    if (scale.item is ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(scale.item).renderType)) {
                        glScalef(0.5f, 0.5f, 0.5f)
                        glTranslatef(1.0f, 1.1f, 0.0f)
                        renderBlocks.renderBlockAsItem(Block.getBlockFromItem(scale.item), scale.itemDamage, 1.0f)
                        glTranslatef(-1.0f, -1.1f, 0.0f)
                        glScalef(2.0f, 2.0f, 2.0f)
                    } else if (scale.item is ItemBlock && !RenderBlocks.renderItemIn3d(Block.getBlockFromItem(scale.item).renderType)) {
                        val entityitem: EntityItem?
                        glPushMatrix()

                        glScalef(2.0f, 2.0f, 2.0f)
                        glTranslatef(.25f, .275f, 0.0f)


                        val `is` = scale.copy()
                        `is`.stackSize = 1
                        entityitem = EntityItem(display.worldObj, 0.0, 0.0, 0.0, `is`)
                        entityitem.hoverStart = 0.0f
                        RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0, 0.0, 0.0, 0.0f, 0.0f)

                        glTranslatef(-.25f, -.275f, 0.0f)

                        glPopMatrix()
                    } else {
                        var renderPass = 0

                        do {
                            val icon = scale.item.getIcon(scale, renderPass)
                            if (icon != null) {
                                val color = Color(scale.item.getColorFromItemStack(scale, renderPass))
                                glColor3ub(color.red.toByte(), color.green.toByte(), color.blue.toByte())
                                val f = icon.minU
                                val f1 = icon.maxU
                                val f2 = icon.minV
                                val f3 = icon.maxV
                                ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.iconWidth, icon.iconHeight, 0.0625f)
                                glColor3f(1.0f, 1.0f, 1.0f)
                            }

                            ++renderPass
                        } while (renderPass < scale.item.getRenderPasses(scale.itemDamage))
                    }
                }
            }

            glPopMatrix()

            glDisable(3008)
            glPushMatrix()
            glTranslatef(0.5f, 1.8f, 0.5f)
            glRotatef(180.0f, 1.0f, 0.0f, 1.0f)
            glPopMatrix()
            glTranslatef(0.0f, 0.2f, 0.0f)

            glEnable(3008)
            glPopMatrix()
        }
    }

    override fun renderTileEntityAt(par1TileEntity: TileEntity?, par2: Double, par4: Double, par6: Double, par8: Float) {
        renderEntity(par1TileEntity as TileItemDisplay, par2, par4, par6, par8)
    }
}
