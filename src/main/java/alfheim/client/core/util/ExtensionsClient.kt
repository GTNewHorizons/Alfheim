package alfheim.client.core.util

import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityClientPlayerMP
import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.stats.Achievement

val mc: Minecraft = Minecraft.getMinecraft()

val renderBlocks = RenderBlocks()

fun EntityClientPlayerMP.hasAchievement(a: Achievement?) = if(a == null) false else statFileWriter.hasAchievementUnlocked(a)