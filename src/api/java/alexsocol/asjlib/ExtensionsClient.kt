package alexsocol.asjlib

import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityClientPlayerMP
import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.stats.Achievement
import org.lwjgl.opengl.GL11

fun glTranslated(d: Double) = GL11.glTranslated(d, d, d)
fun glTranslatef(f: Float) = GL11.glTranslatef(f, f, f)
fun glScaled(d: Double) = GL11.glScaled(d, d, d)
fun glScalef(f: Float) = GL11.glScalef(f, f, f)

val mc: Minecraft get() = Minecraft.getMinecraft()

val renderBlocks = RenderBlocks()

fun EntityClientPlayerMP.hasAchievement(a: Achievement?) = if (a == null) false else statFileWriter.hasAchievementUnlocked(a)