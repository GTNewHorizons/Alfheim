package alexsocol.patcher.asm

import alexsocol.asjlib.*
import alexsocol.patcher.event.*
import cpw.mods.fml.relauncher.*
import gloomyfolken.hooklib.asm.*
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.entity.Render
import net.minecraft.command.server.CommandSummon
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.*
import net.minecraft.tileentity.TileEntityFurnace
import net.minecraft.world.*
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.util.ForgeDirection
import org.lwjgl.opengl.*
import java.nio.FloatBuffer
import java.util.*
import kotlin.math.min

object ASJHookHandler {
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun func_147182_d(c: CommandSummon): Array<String> {
		return (EntityList.stringToClassMapping.keys as Set<String>).toTypedArray()
	}
	
	@JvmStatic
	@Hook(injectOnExit = true)
	fun wakeAllPlayers(world: WorldServer) {
		MinecraftForge.EVENT_BUS.post(ServerWakeUpEvent(world))
	}
	
	@JvmStatic
	@Hook(injectOnExit = true, isMandatory = true)
	fun onNewPotionEffect(e: EntityLivingBase, pe: PotionEffect) {
		MinecraftForge.EVENT_BUS.post(LivingPotionEvent.Add.Post(e, pe))
	}
	
	@JvmStatic
	@Hook(injectOnExit = true, isMandatory = true)
	fun onChangedPotionEffect(e: EntityLivingBase, pe: PotionEffect, was: Boolean) {
		MinecraftForge.EVENT_BUS.post(LivingPotionEvent.Change.Post(e, pe, was))
	}
	
	@JvmStatic
	@Hook(injectOnExit = true, isMandatory = true)
	fun onFinishedPotionEffect(e: EntityLivingBase, pe: PotionEffect) {
		MinecraftForge.EVENT_BUS.post(LivingPotionEvent.Remove.Post(e, pe))
	}
	
	// Portal closes GUI fix
	
	var portalHook = false
	
	@JvmStatic
	@Hook(targetMethod = "onLivingUpdate")
	fun onLivingUpdatePre(player: EntityPlayerSP) {
		portalHook = true
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ON_TRUE)
	@SideOnly(Side.CLIENT)
	fun displayGuiScreen(mc: Minecraft, gui: GuiScreen?): Boolean {
		return portalHook && gui == null
	}
	
	@JvmStatic
	@Hook(targetMethod = "onLivingUpdate", injectOnExit = true)
	fun onLivingUpdatePost(player: EntityPlayerSP) {
		portalHook = false
	}
	
	// #### BlockFence connection fix ####
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ON_TRUE)
	fun canConnectFenceTo(fence: BlockFence, world: IBlockAccess, x: Int, y: Int, z: Int) = world.getBlock(x, y, z) is BlockFence
	
	// #### BlockWall fix ####
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS, createMethod = true)
	fun isSideSolid(wall: BlockWall, world: IBlockAccess, x: Int, y: Int, z: Int, side: ForgeDirection) =
		when (side) {
			ForgeDirection.DOWN -> true
			ForgeDirection.UP   -> wall.blockBoundsMaxY == 1.0
			else                -> false
		}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun renderBlockWall(render: RenderBlocks, block: BlockWall, x: Int, y: Int, z: Int): Boolean {
		val flag = block.canConnectWallTo(render.blockAccess, x - 1, y, z)
		val flag1 = block.canConnectWallTo(render.blockAccess, x + 1, y, z)
		val flag2 = block.canConnectWallTo(render.blockAccess, x, y, z - 1)
		val flag3 = block.canConnectWallTo(render.blockAccess, x, y, z + 1)
		val flag4 = flag2 && flag3 && !flag && !flag1
		val flag5 = !flag2 && !flag3 && flag && flag1
		val doNotRenderPost = render.blockAccess.getBlock(x, y + 1, z) !is BlockWall && render.blockAccess.getBlock(x, y + 1, z) !is BlockSkull && render.blockAccess.getBlock(x, y - 1, z) !is BlockWall
		
		if ((flag4 || flag5) && doNotRenderPost) {
			if (flag4) {
				render.setRenderBounds(0.3125, 0.0, 0.0, 0.6875, 0.8125, 1.0)
				render.renderStandardBlock(block, x, y, z)
			} else {
				render.setRenderBounds(0.0, 0.0, 0.3125, 1.0, 0.8125, 0.6875)
				render.renderStandardBlock(block, x, y, z)
			}
		} else {
			render.setRenderBounds(0.25, 0.0, 0.25, 0.75, 1.0, 0.75)
			render.renderStandardBlock(block, x, y, z)
			if (flag) {
				render.setRenderBounds(0.0, 0.0, 0.3125, 0.25, 0.8125, 0.6875)
				render.renderStandardBlock(block, x, y, z)
			}
			if (flag1) {
				render.setRenderBounds(0.75, 0.0, 0.3125, 1.0, 0.8125, 0.6875)
				render.renderStandardBlock(block, x, y, z)
			}
			if (flag2) {
				render.setRenderBounds(0.3125, 0.0, 0.0, 0.6875, 0.8125, 0.25)
				render.renderStandardBlock(block, x, y, z)
			}
			if (flag3) {
				render.setRenderBounds(0.3125, 0.0, 0.75, 0.6875, 0.8125, 1.0)
				render.renderStandardBlock(block, x, y, z)
			}
		}
		block.setBlockBoundsBasedOnState(render.blockAccess, x, y, z)
		return true
	}
	
	// #### BlockWall fix end ####
	
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS, isMandatory = true)
	fun updatePotionEffects(e: EntityLivingBase) {
		try {
			val iterator = e.activePotionsMap.keys.iterator()
			
			while (iterator.hasNext()) {
				val integer = iterator.next() as Int
				val potioneffect = e.activePotionsMap[integer] as PotionEffect
				
				if (!potioneffect.onUpdate(e)) {
					//if (!e.worldObj.isRemote) {
					iterator.remove()
					ASJSyntheticMethods.onFinishedPotionEffect(e, potioneffect)
					//}
				} else if (potioneffect.duration % 600 == 0) {
					ASJSyntheticMethods.onChangedPotionEffect(e, potioneffect, false)
				}
			}
			
			var i: Int
			
			if (e.potionsNeedUpdate) {
				if (!e.worldObj.isRemote) {
					if (e.activePotionsMap.isEmpty()) {
						e.dataWatcher.updateObject(8, 0.toByte())
						e.dataWatcher.updateObject(7, 0)
						e.isInvisible = false
					} else {
						i = PotionHelper.calcPotionLiquidColor(e.activePotionsMap.values)
						e.dataWatcher.updateObject(8, (if (PotionHelper.func_82817_b(e.activePotionsMap.values)) 1 else 0).toByte())
						e.dataWatcher.updateObject(7, i)
						e.isInvisible = e.isPotionActive(Potion.invisibility.id)
					}
				}
				
				e.potionsNeedUpdate = false
			}
			
			i = e.dataWatcher.getWatchableObjectInt(7)
			val flag1 = e.dataWatcher.getWatchableObjectByte(8) > 0
			
			if (i > 0) {
				var flag: Boolean
				
				flag = if (!e.isInvisible) {
					e.worldObj.rand.nextBoolean()
				} else {
					e.worldObj.rand.nextInt(15) == 0
				}
				
				if (flag1) {
					flag = flag and (e.worldObj.rand.nextInt(5) == 0)
				}
				
				if (flag) {
					val d0 = (i shr 16 and 255).D / 255.0
					val d1 = (i shr 8 and 255).D / 255.0
					val d2 = (i and 255).D / 255.0
					e.worldObj.spawnParticle(if (flag1) "mobSpellAmbient" else "mobSpell", e.posX + (e.worldObj.rand.nextDouble() - 0.5) * e.width.D, e.posY + e.worldObj.rand.nextDouble() * e.height.D - e.yOffset.D, e.posZ + (e.worldObj.rand.nextDouble() - 0.5) * e.width.D, d0, d1, d2)
				}
			}
		} catch (ex: ConcurrentModificationException) {
			ASJUtilities.log("Well, that was expected. Ignore.")
			ex.printStackTrace()
		}
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ON_TRUE, targetMethod = "func_150000_e", isMandatory = true)
	fun tryToCreatePortal(portal: BlockPortal, world: World, x: Int, y: Int, z: Int) =
		MinecraftForge.EVENT_BUS.post(NetherPortalActivationEvent(world, x, y, z))
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS, isMandatory = true)
	fun extinguishFire(world: World, player: EntityPlayer?, x: Int, y: Int, z: Int, side: Int): Boolean {
		var i = x
		var j = y
		var k = z
		if (side == 0) --j
		if (side == 1) ++j
		if (side == 2) --k
		if (side == 3) ++k
		if (side == 4) --i
		if (side == 5) ++i
		val block = world.getBlock(i, j, k)
		
		val breakable = if (player != null) block.getPlayerRelativeBlockHardness(player, world, i, j, k) > 0f || player.capabilities.isCreativeMode else true
		
		if (block.material === Material.fire && breakable) {
			world.playAuxSFXAtEntity(player, 1004, i, j, k, 0)
			world.setBlockToAir(i, j, k)
			return true
		}
		return false
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun getNightVisionBrightness(render: EntityRenderer, player: EntityPlayer, partialTicks: Float) =
		if ((player.getActivePotionEffect(Potion.nightVision.id)?.duration ?: 0) > 0) 1f else 0f
	
	// int overflow fix
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun getBurnTimeRemainingScaled(furnace: TileEntityFurnace, mod: Int): Int {
		if (furnace.currentItemBurnTime == 0) {
			furnace.currentItemBurnTime = 200
		}
		
		return (furnace.furnaceBurnTime.D / furnace.currentItemBurnTime * mod).I
	}
	
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook(isMandatory = true, returnCondition = ReturnCondition.ON_TRUE)
	fun doRenderShadowAndFire(render: Render, entity: Entity, x: Double, y: Double, z: Double, yaw: Float, ticks: Float): Boolean {
		return MinecraftForge.EVENT_BUS.post(RenderEntityPostEvent(entity, x, y, z, yaw))
	}
	
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun setupFog(renderer: EntityRenderer, fogMode: Int, renderPartialTicks: Float) {
		val entitylivingbase = renderer.mc.renderViewEntity
		val creative = if (entitylivingbase is EntityPlayer) entitylivingbase.capabilities.isCreativeMode else false
		
		fun setFogColorBuffer(p_78469_1_: Float, p_78469_2_: Float, p_78469_3_: Float, p_78469_4_: Float): FloatBuffer {
			renderer.fogColorBuffer.clear()
			renderer.fogColorBuffer.put(p_78469_1_).put(p_78469_2_).put(p_78469_3_).put(p_78469_4_)
			renderer.fogColorBuffer.flip()
			return renderer.fogColorBuffer
		}
		
		if (fogMode == 999) {
			GL11.glFog(GL11.GL_FOG_COLOR, setFogColorBuffer(0f, 0f, 0f, 1f))
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR)
			GL11.glFogf(GL11.GL_FOG_START, 0f)
			GL11.glFogf(GL11.GL_FOG_END, 8f)
			
			if (GLContext.getCapabilities().GL_NV_fog_distance) {
				GL11.glFogi(NVFogDistance.GL_FOG_DISTANCE_MODE_NV, NVFogDistance.GL_EYE_RADIAL_NV)
			}
			
			GL11.glFogf(GL11.GL_FOG_START, 0f)
		} else {
			GL11.glFog(GL11.GL_FOG_COLOR, setFogColorBuffer(renderer.fogColorRed, renderer.fogColorGreen, renderer.fogColorBlue, 1f))
			GL11.glNormal3f(0f, -1f, 0f)
			GL11.glColor4f(1f, 1f, 1f, 1f)
			val block = ActiveRenderInfo.getBlockAtEntityViewpoint(renderer.mc.theWorld, entitylivingbase, renderPartialTicks)
			var f1: Float
			
			val event = net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity(renderer, entitylivingbase, block, renderPartialTicks.D, 0.1f)
			
			if (MinecraftForge.EVENT_BUS.post(event)) {
				GL11.glFogf(GL11.GL_FOG_DENSITY, event.density)
			} else if (entitylivingbase.isPotionActive(Potion.blindness) && !creative) {
				f1 = 5f
				val j = entitylivingbase.getActivePotionEffect(Potion.blindness.id)!!.duration
				
				if (j < 20) {
					f1 = 5f + (renderer.farPlaneDistance - 5f) * (1f - j.F / 20f)
				}
				
				GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR)
				
				if (fogMode < 0) {
					GL11.glFogf(GL11.GL_FOG_START, 0f)
					GL11.glFogf(GL11.GL_FOG_END, f1 * 0.8f)
				} else {
					GL11.glFogf(GL11.GL_FOG_START, f1 * 0.25f)
					GL11.glFogf(GL11.GL_FOG_END, f1)
				}
				
				if (GLContext.getCapabilities().GL_NV_fog_distance) {
					GL11.glFogi(NVFogDistance.GL_FOG_DISTANCE_MODE_NV, NVFogDistance.GL_EYE_RADIAL_NV)
				}
			} else if (renderer.cloudFog) {
				GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP)
				GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1f)
			} else if (block.material === Material.water) {
				GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP)
				
				if (entitylivingbase.isPotionActive(Potion.waterBreathing)) {
					GL11.glFogf(GL11.GL_FOG_DENSITY, if (ASJHookLoader.clearWater) 0.01f else 0.05f)
				} else {
					GL11.glFogf(GL11.GL_FOG_DENSITY, if (ASJHookLoader.clearWater) 0.01f else 0.1f - EnchantmentHelper.getRespiration(entitylivingbase).F * 0.03f)
				}
			} else if (block.material === Material.lava) {
				GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP)
				GL11.glFogf(GL11.GL_FOG_DENSITY, 2f)
			} else {
				f1 = renderer.farPlaneDistance
				
				if (renderer.mc.theWorld.provider.worldHasVoidParticles && ASJHookLoader.voidFog && !creative) {
					var d0 = (entitylivingbase.getBrightnessForRender(renderPartialTicks) and 15728640 shr 20).D / 16.0 + (entitylivingbase.lastTickPosY + (entitylivingbase.posY - entitylivingbase.lastTickPosY) * renderPartialTicks.D + 4.0) / 32.0
					
					if (d0 < 1.0) {
						if (d0 < 0.0) {
							d0 = 0.0
						}
						
						d0 *= d0
						var f2 = 100f * d0.F
						
						if (f2 < 5f) {
							f2 = 5f
						}
						
						if (f1 > f2) {
							f1 = f2
						}
					}
				}
				
				GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR)
				
				if (fogMode < 0) {
					GL11.glFogf(GL11.GL_FOG_START, 0f)
					GL11.glFogf(GL11.GL_FOG_END, f1)
				} else {
					GL11.glFogf(GL11.GL_FOG_START, f1 * 0.75f)
					GL11.glFogf(GL11.GL_FOG_END, f1)
				}
				
				if (GLContext.getCapabilities().GL_NV_fog_distance) {
					GL11.glFogi(NVFogDistance.GL_FOG_DISTANCE_MODE_NV, NVFogDistance.GL_EYE_RADIAL_NV)
				}
				
				if (renderer.mc.theWorld.provider.doesXZShowFog(entitylivingbase.posX.I, entitylivingbase.posZ.I)) {
					GL11.glFogf(GL11.GL_FOG_START, f1 * 0.05f)
					GL11.glFogf(GL11.GL_FOG_END, min(f1, 192f) * 0.5f)
				}
				
				MinecraftForge.EVENT_BUS.post(net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent(renderer, entitylivingbase, block, renderPartialTicks.D, fogMode, f1))
			}
			
			GL11.glEnable(GL11.GL_COLOR_MATERIAL)
			GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT)
		}
	}
}