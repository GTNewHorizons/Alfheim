package alfheim.common.item.relic

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alexsocol.asjlib.render.ASJRenderHelper
import alexsocol.asjlib.render.ASJRenderHelper.discard
import alexsocol.asjlib.render.ASJRenderHelper.interpolate
import alexsocol.asjlib.render.ASJRenderHelper.setBlend
import alexsocol.asjlib.render.ASJRenderHelper.setGlow
import alexsocol.asjlib.render.ASJRenderHelper.setTwoside
import alfheim.api.ModInfo
import alfheim.api.event.RenderEntityPostEvent
import alfheim.api.lib.LibResourceLocations
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.helper.ContributorsPrivacyHelper
import alfheim.common.core.util.AlfheimTab
import alfheim.common.entity.EntityGleipnir
import alfheim.common.item.relic.LeashingHandler.isLeashed
import alfheim.common.item.relic.LeashingHandler.leashedTo
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.culling.ICamera
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.world.World
import net.minecraftforge.client.event.RenderLivingEvent
import net.minecraftforge.event.entity.living.*
import org.lwjgl.opengl.GL11.*
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.client.core.handler.ClientTickHandler
import vazkii.botania.common.core.helper.ItemNBTHelper.*
import vazkii.botania.common.item.relic.ItemRelic
import java.awt.Color
import java.util.*
import kotlin.math.*

class ItemGleipnir: ItemRelic("Gleipnir") {
	
	init {
		creativeTab = AlfheimTab
	}
	
	override fun itemInteractionForEntity(stack: ItemStack, player: EntityPlayer, target: EntityLivingBase): Boolean {
		if (player.isSneaking) return false
		
		if (target.isLeashed) {
			if (target.leashedTo === player)
				target.leashedTo = null
		} else {
			// elite memes start
			// unbindable
			if (ContributorsPrivacyHelper.isCorrect(target.commandSenderName, "AlexSocol")) return false
			// only by socol
			if (ContributorsPrivacyHelper.isCorrect(target.commandSenderName, "KAIIIAK") && !ContributorsPrivacyHelper.isCorrect(player.commandSenderName, "AlexSocol")) return false
			// only by cat and socol
			if (ContributorsPrivacyHelper.isCorrect(target.commandSenderName, "GedeonGrays") && !ContributorsPrivacyHelper.isCorrect(player.commandSenderName, "AlexSocol") && !ContributorsPrivacyHelper.isCorrect(player.commandSenderName, "KAIIIAK")) return false
			// elite memes end
			
			target.leashedTo = player
			stack.cooldown = 50
		}
		
		return true
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		if (stack.cooldown > 0) return stack
		
		if (player.isSneaking) {
			if (!ManaItemHandler.requestManaExact(stack, player, 2500, true)) // +2500 for cooldown
				return stack
			
			stack.cooldown = 500
			setBoolean(stack, TAG_ENTANGLED, true)
			setLong(stack, TAG_RAND_SEED, player.rng.nextLong())
		} else {
			val mop = ASJUtilities.getMouseOver(player, 64.0, true) ?: return stack
			val (x, y, z) = mop.hitVec
			
			// 15000 manacost
			if (!world.isRemote)
				world.spawnEntityInWorld(EntityGleipnir(world, player).apply { setPosition(x.D, y.D, z.D) })
			
			if (!player.capabilities.isCreativeMode)
				stack.cooldown = 1500
		}
		
		return stack
	}
	
	override fun onUpdate(stack: ItemStack, world: World, entity: Entity?, slot: Int, inHand: Boolean) {
		super.onUpdate(stack, world, entity, slot, inHand)
		if (entity !is EntityPlayer) return
		
		val cd = stack.cooldown - 1
		val take = cd < 250
		
		if (cd >= 0 && (!take || ManaItemHandler.requestManaExact(stack, entity, 10, true)))
			stack.cooldown = cd
		
		if (getBoolean(stack, TAG_ENTANGLED, false)) {
			entity.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDEternity, 2, 1, false))
			
			if (stack.cooldown in 250 until 260) {
				val size = (260 - stack.cooldown) / 2.5
				val list = world.getEntitiesWithinAABB(EntityLivingBase::class.java, entity.boundingBox(1).expand(size, 0.0, size)) as MutableList<EntityLivingBase>
				list.remove(entity)
				list.forEach {
					val punch = Vector3.fromEntity(entity).sub(Vector3.fromEntity(it)).normalize().mul(size)
					it.knockBack(entity, 1f, punch.x, punch.z)
				}
			}
			
			if (take || !inHand)
				setBoolean(stack, TAG_ENTANGLED, false)
		} else if (entity.capabilities.isCreativeMode)
			stack.cooldown = 0
	}
	
	companion object {
		
		const val TAG_COOLDOWN = "cooldown"
		const val TAG_ENTANGLED = "entangled"
		const val TAG_RAND_SEED = "randomSeed"
		
		val rand = Random()
		
		init {
			eventForge()
			LeashingHandler
		}
		
		fun isEntangled(entity: Entity?): Boolean {
			if (entity !is EntityLivingBase) return false
			val stack = entity.heldItem ?: return false
			if (stack.item !is ItemGleipnir) return false
			return getBoolean(stack, TAG_ENTANGLED, false)
		}
		
		@SubscribeEvent
		fun onLivingAttack(e: LivingAttackEvent) {
			if (isEntangled(e.entity)) {
				e.isCanceled = true
				
				val attacker = e.source.entity as? EntityLivingBase ?: return
				
				val punch = Vector3.fromEntity(e.entity).sub(Vector3.fromEntity(attacker))
				if (punch.length() > 3) return
				punch.normalize()
				attacker.knockBack(e.entity, 1f, punch.x, punch.z)
			}
		}
		
		@SubscribeEvent
		fun renderEntangle(e: RenderEntityPostEvent) {
			if (!isEntangled(e.entity)) return
			
			val entity = e.entity as EntityLivingBase
			val timer = entity.heldItem.cooldown - mc.timer.renderPartialTicks.D // minus because timer decreasing
			
			glPushMatrix()
			glTranslated(e.x, e.y - if (entity === mc.thePlayer) 1.62 else 0.0, e.z)
			
			setGlow()
			setBlend()
			setTwoside()
			
			val alpha = when {
				timer > 490 -> (500 - timer) / 10
				timer < 250 -> return
				timer < 260 -> 1 - (260 - timer) / 10
				else        -> 1.0
			}
			
			val color = ASJRenderHelper.interpolateColor(Color(0xFFD400).rgb, Color(0xFFD400).brighter().brighter().rgb, sin(ClientTickHandler.ticksInGame / 20.0) * 0.5 + 0.5)
			glColor4d(color.red.D, color.green.D, color.blue.D, alpha)
			mc.renderEngine.bindTexture(LibResourceLocations.gleipnir0)
			
			var size = entity.width.D
			if (timer < 260) size += (260 - timer) / 2.5
			
			rand.setSeed(getLong(entity.heldItem, TAG_RAND_SEED, 0L))
			
			val tes = Tessellator.instance
			for (i in 1..4) {
				glTranslatef(0f, entity.height / 5, 0f)
				
				glPushMatrix()
				glRotated(timer, 0.0, if (rand.nextBoolean()) 1.0 else -1.0, 0.0)
				glRotatef(rand.nextFloat() * 60 - 30, rand.nextFloat(), 0f, rand.nextFloat())
				val scale = size - rand.nextDouble() * 0.1
				
				tes.startDrawingQuads()
				for (a in 0..360 step 45) {
					val x = cos(Math.toRadians(a.D)) * scale
					val y = 0.1 * scale / 0.6
					val z = sin(Math.toRadians(a.D)) * scale
					
					if (a != 0) {
						// ending
						tes.addVertexWithUV(x, +y, z, 1.0, 1.0)
						tes.addVertexWithUV(x, -y, z, 1.0, 0.0)
					}
					
					if (a != 360) {
						// starting
						tes.addVertexWithUV(x, -y, z, 0.0, 0.0)
						tes.addVertexWithUV(x, +y, z, 0.0, 1.0)
					}
				}
				tes.draw()
				glPopMatrix()
			}
			
			discard()
			glPopMatrix()
		}
		
		private var ItemStack.cooldown
			get() = getInt(this, TAG_COOLDOWN, 0)
			set(value) = setInt(this, TAG_COOLDOWN, value)
	}
}

object LeashingHandler {
	
	const val PREFIX = "${ModInfo.MODID}.gleipnir"
	const val TAG_LEASHED = "$PREFIX.leashedTo"
	
	val EntityLivingBase.isLeashed: Boolean
		get() = entityData.hasKey(TAG_LEASHED)
	
	var EntityLivingBase.leashedTo: EntityPlayer?
		get() =
			worldObj.getPlayerEntityByName(entityData.getString(TAG_LEASHED))
		// TODO maybe send packets to client for every change...
		set(player) {
			if (player == null)
				entityData.removeTag(TAG_LEASHED)
			else
				entityData.setString(TAG_LEASHED, player.commandSenderName)
		}
	
	init {
		eventForge()
	}
	
	@SubscribeEvent
	fun updateLeashedState(e: LivingEvent.LivingUpdateEvent) {
		val entity = e.entityLiving
		if (entity.worldObj.isRemote || !entity.isLeashed) return
		
		val player = entity.leashedTo
		if (player == null || !player.isEntityAlive || player.worldObj != entity.worldObj || !entity.isEntityAlive) {
			entity.leashedTo = null
			return
		}
		
		val f = entity.getDistanceToEntity(player)
		
		if (f < 6) return
		
		if (f < 10) {
			val d0 = (player.posX - entity.posX) / f
			val d1 = (player.posY - entity.posY) / f
			val d2 = (player.posZ - entity.posZ) / f
			entity.motionX += d0 * abs(d0) * 0.4
			entity.motionY += d1 * abs(d1) * 0.4
			entity.motionZ += d2 * abs(d2) * 0.4
		} else {
			entity.setPosition(player)
		}
	}
	
	@SubscribeEvent
	fun renderLeash(e: RenderLivingEvent.Post) {
		if (!e.entity.isLeashed || !e.entity.isEntityAlive) return
		
		val entity = e.entity
		var x = e.x
		var y = e.y
		var z = e.z
		
		val player = entity.leashedTo
		
		if (player != null) {
			y -= (1.6 - entity.height) * 0.5
			val tes = Tessellator.instance
			val d3 = interpolate(player.prevRotationYaw.D, player.rotationYaw.D, mc.timer.renderPartialTicks * 0.5) * 0.01745329238474369
			val d4 = interpolate(player.prevRotationPitch.D, player.rotationPitch.D, mc.timer.renderPartialTicks * 0.5) * 0.01745329238474369
			var d5 = cos(d3)
			var d6 = sin(d3)
			val d7 = sin(d4)
			val d8 = cos(d4)
			val d9 = interpolate(player.prevPosX, player.posX) - d5 * 0.7 - d6 * 0.5 * d8
			val d10 = interpolate(player.prevPosY + player.eyeHeight.D * 0.7, player.posY + player.eyeHeight.D * 0.7) - d7 * 0.5 - 0.25
			val d11 = interpolate(player.prevPosZ, player.posZ) - d6 * 0.7 + d5 * 0.5 * d8
			val d12 = interpolate(entity.prevRenderYawOffset.D, entity.renderYawOffset.D) * 0.01745329238474369 + Math.PI / 2.0
			d5 = cos(d12) * entity.width.D * 0.4
			d6 = sin(d12) * entity.width.D * 0.4
			val d13 = interpolate(entity.prevPosX, entity.posX) + d5
			val d14 = interpolate(entity.prevPosY, entity.posY)
			val d15 = interpolate(entity.prevPosZ, entity.posZ) + d6
			x += d5
			z += d6
			val d16 = d9 - d13
			val d17 = d10 - d14
			val d18 = d11 - d15
			glDisable(GL_TEXTURE_2D)
			setGlow()
			setTwoside()
			tes.startDrawing(5)
			for (i in 0..24) {
				if (i % 2 == 0)
					tes.setColorRGBA(255, 223, 0, 255)
				else
					tes.setColorRGBA(255, 212, 0, 255)
				
				val d19 = i / 24.0
				tes.addVertex(x + d16 * d19 + 0.0, y + d17 * (d19 * d19 + d19) * 0.5 + ((24 - i) / 18.0 + 0.125), z + d18 * d19)
				tes.addVertex(x + d16 * d19 + 0.025, y + d17 * (d19 * d19 + d19) * 0.5 + ((24 - i) / 18.0 + 0.125) + 0.025, z + d18 * d19)
			}
			
			tes.draw()
			tes.startDrawing(5)
			for (i in 0..24) {
				if (i % 2 == 0)
					tes.setColorRGBA(255, 223, 0, 255)
				else
					tes.setColorRGBA(255, 212, 0, 255)
				
				val d19 = i / 24.0
				tes.addVertex(x + d16 * d19 + 0.0, y + d17 * (d19 * d19 + d19).D * 0.5 + ((24 - i) / 18.0 + 0.125) + 0.025, z + d18 * d19)
				tes.addVertex(x + d16 * d19 + 0.025, y + d17 * (d19 * d19 + d19).D * 0.5 + ((24 - i) / 18.0 + 0.125), z + d18 * d19 + 0.025)
			}
			
			tes.draw()
			discard()
			glEnable(GL_TEXTURE_2D)
		}
	}
	
	fun interpolate(prev: Double, current: Double, ticks: Double) = prev + (current - prev) * ticks
	
	// used in transformers
	fun isBoundInRender(flag: Boolean, entity: Entity, camera: ICamera): Boolean {
		if (!flag && entity is EntityLivingBase) {
			if (entity.isLeashed && entity.leashedTo != null) {
				return camera.isBoundingBoxInFrustum(entity.leashedTo!!.boundingBox)
			}
		}
		
		return flag
	}
}