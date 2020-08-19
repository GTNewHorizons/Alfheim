package alfheim.common.item.relic

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alexsocol.asjlib.render.ASJRenderHelper
import alexsocol.asjlib.render.ASJRenderHelper.discard
import alexsocol.asjlib.render.ASJRenderHelper.setBlend
import alexsocol.asjlib.render.ASJRenderHelper.setGlow
import alexsocol.asjlib.render.ASJRenderHelper.setTwoside
import alfheim.api.event.RenderEntityPostEvent
import alfheim.api.lib.LibResourceLocations
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.AlfheimTab
import alfheim.common.entity.EntityGleipnir
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.world.World
import net.minecraftforge.event.entity.living.LivingAttackEvent
import org.lwjgl.opengl.GL11.*
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.client.core.handler.ClientTickHandler
import vazkii.botania.common.core.helper.ItemNBTHelper.*
import vazkii.botania.common.item.relic.ItemRelic
import java.awt.Color
import java.io.*
import java.util.*
import kotlin.collections.HashSet
import kotlin.math.*

class ItemGleipnir: ItemRelic("Gleipnir") {
	
	init {
		creativeTab = AlfheimTab
	}
	
	override fun itemInteractionForEntity(stack: ItemStack, player: EntityPlayer, target: EntityLivingBase): Boolean {
		if (player.isSneaking || target !is EntityLiving) return false
		
		if (target.leashed) {
			if (target.leashedToEntity === player)
				target.clearLeashed(true, true)
		} else {
			target.setLeashedToEntity(player, true)
			leashes.add(target.uniqueID)
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
			world.spawnEntityInWorld(EntityGleipnir(world, player).also { it.setPosition(x.D, y.D, z.D) })
			
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
		
		val leashes = HashSet<UUID>()
		val rand = Random()
		
		init {
			eventForge()
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
		
		fun loadLeases(save: String) {
			leashes.clear()
			
//			val file = File("$save/data/GleipnirLeashes.dat")
//			if (!file.exists()) return
//
//			try {
//				val fis = FileInputStream(file)
//				val oin = ObjectInputStream(fis)
//				leashes.addAll(oin.readObject() as HashSet<UUID>)
//				oin.close()
//			} catch (ignore: Throwable) {}
		}
		
		fun saveLeases(save: String) {
			val fos = FileOutputStream("$save/data/GleipnirLeashes.dat")
			val oos = ObjectOutputStream(fos)
			oos.writeObject(leashes)
			oos.flush()
			oos.close()
		}
		
		private var ItemStack.cooldown
			get() = getInt(this, TAG_COOLDOWN, 0)
			set(value) = setInt(this, TAG_COOLDOWN, value)
	}
}
