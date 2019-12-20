package alfheim.client.render.world

import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.api.entity.race
import alfheim.client.gui.GUIDeathTimer
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects.*
import alfheim.common.block.tile.TileManaInfuser
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.mfloor
import alfheim.common.item.AlfheimItems
import alfheim.common.spell.illusion.SpellSmokeScreen
import alfheim.common.spell.water.*
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderGlobal
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.potion.PotionEffect
import vazkii.botania.common.Botania
import kotlin.math.*
import vazkii.botania.common.core.helper.Vector3 as VVec3

object VisualEffectHandlerClient {
	
	internal val m = Vector3()
	
	fun select(s: VisualEffects, x: Double, y: Double, z: Double, x2: Double, y2: Double, z2: Double) {
		if (s == SPLASH) spawnSplash(x, y, z)
		
		if (AlfheimCore.enableMMO) {
			when (s) {
				ACID           -> spawnAcid(x, y, z)
				AQUABIND       -> spawnAquaBind(x, y, z)
				AQUASTREAM     -> spawnAquaStream(x, y, z, x2, y2, z2)
				AQUASTREAM_HIT -> spawnAquaStreamHit(x, y, z)
				DISPEL         -> spawnBurst(x, y, z, 1f, 0f, 0f)
				ECHO           -> spawnEcho(x, y, z)
				ECHO_ENTITY    -> spawnEchoEntity(x, y, z)
				ECHO_ITEM      -> spawnEchoItem(x, y, z)
				ECHO_MOB       -> spawnEchoMob(x, y, z)
				ECHO_PLAYER    -> spawnEchoPlayer(x, y, z)
				EXPL           -> spawnExplosion(x, y, z)
				GAIA_SOUL      -> spawnGaiaSoul(x, y, z)
				GRAVITY        -> spawnGravity(x, y, z, x2, y2, z2)
				HEAL           -> spawnBurst(x, y, z, 0f, 1f, 0f)
				HORN           -> horn(x, y, z)
				ICELENS        -> addIceLens()
				MANA           -> addMana(x, y)
				MOON           -> moonBoom(x, y, z)
				NOTE           -> spawnNote(x, y, z)
				NVISION        -> spawnBurst(x, y, z, 0f, 0f, 1f)
				POTION         -> spawnPotion(x, y, z, x2.toInt(), y2 == 1.0)
				PURE           -> spawnBurst(x, y, z, 0f, 0.75f, 1f)
				PURE_AREA      -> spawnPure(x, y, z)
				QUAD           -> quadDamage()
				QUADH          -> quadHurt()
				SHADOW         -> spawnBurst(x, y, z, 0.75f, 0.75f, 0.75f)
				SMOKE          -> spawnSmoke(x, y, z)
				THROW          -> spawnThrow(x, y, z, x2, y2, z2)
				TREMORS        -> spawnTremors(x, y, z)
				UPHEAL         -> spawnBurst(x, y, z, 1f, 0.75f, 0f)
				WIRE           -> spawnWire(x, y, z, x2)
				
				else           -> Unit
			}
		}
	}
	
	fun spawnWire(x: Double, y: Double, z: Double, range: Double) {
		val v = VVec3(x, y, z)
		for (var11 in 0..20) {
			Botania.proxy.lightningFX(Minecraft.getMinecraft().theWorld, v, v.copy().add(randomVec(range)), (range * 0.01).toFloat(), 255 shl 16, 0)
		}
	}
	
	private fun randomVec(length: Double): vazkii.botania.common.core.helper.Vector3 {
		val vec = VVec3(0.0, Math.random() * length, 0.0)
		vec.rotate(Math.random() * Math.PI * 2, VVec3(1.0, 0.0, 0.0))
		vec.rotate(Math.random() * Math.PI * 2, VVec3(0.0, 0.0, 1.0))
		return vec
	}
	
	fun addIceLens() {
		Minecraft.getMinecraft().thePlayer.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDIceLens, SpellIceLens.duration, -1, true))
	}
	
	fun addMana(enID: Double, mana: Double) {
		val e = Minecraft.getMinecraft().theWorld.getEntityByID(enID.toInt())
		if (e is EntityPlayer) {
			if (mana == 0.0) {
				var d = 0.0
				while (d < 1.0) {
					spawnBurst(e.posX, e.posY + d, e.posZ, 0.975f, 0.85f, 0.1f)
					d += .2
				}
			} else
				e.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDShowMana, mana.toInt(), 100, true))
		}
	}
	
	fun horn(x: Double, y: Double, z: Double) {
		Minecraft.getMinecraft().theWorld.playSound(x, y, z, ModInfo.MODID + ":horn.bhorn", 100.0f, 0.8f + Minecraft.getMinecraft().theWorld.rand.nextFloat() * 0.2f, false)
	}
	
	fun moonBoom(x: Double, y: Double, z: Double) {
		for (i in 0..63) {
			m.rand().sub(0.5).normalize().mul(0.1)
			Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x, y, z, 0.3f + (Math.random().toFloat() - 0.5f) * 0.1f, 0.85f + (Math.random().toFloat() - 0.5f) * 0.1f, 0.85f + (Math.random().toFloat() - 0.5f) * 0.1f, 0.5f, m.x.toFloat(), m.y.toFloat(), m.z.toFloat())
		}
	}
	
	fun quadDamage() {
		Minecraft.getMinecraft().thePlayer.playSound(ModInfo.MODID + ":quad", 10f, 1f)
	}
	
	fun quadHurt() {
		Minecraft.getMinecraft().thePlayer.playSound(ModInfo.MODID + ":quadh", 1f, 1f)
	}
	
	fun spawnAcid(x: Double, y: Double, z: Double) {
		for (i in 0..255) {
			m.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize().mul(Math.random() * 9, Math.random() * 9, Math.random() * 9)
			Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x + m.x, y + m.y, z + m.z, (Math.random() * 0.2).toFloat(), 1f, 0f, 2f, 0f, 2f)
		}
	}
	
	fun spawnAquaBind(x: Double, y: Double, z: Double) {
		var i = 0
		while (i < 360) {
			val X = cos(i.toDouble()) * 3.5
			val Z = sin(i.toDouble()) * 3.5
			Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x + X, y, z + Z, 0f, 0.5f, 1f, 0.5f)
			i += 5
		}
	}
	
	fun spawnAquaStream(x: Double, y: Double, z: Double, x2: Double, y2: Double, z2: Double) {
		Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x, y, z, 0.5f, 0.5f, 1f, 1.0f, x2.toFloat(), y2.toFloat(), z2.toFloat(), 0.5f)
	}
	
	fun spawnAquaStreamHit(x: Double, y: Double, z: Double) {
		Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x, y, z, 0f, 0.5f, 1f, 0.5f)
	}
	
	fun spawnBurst(x: Double, y: Double, z: Double, r: Float, g: Float, b: Float) {
		for (i in 0..7) Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x + Math.random() - 0.5, y + Math.random() * 0.25, z + Math.random() - 0.5, r, g, b, 1f, (Math.random() * -0.075).toFloat())
	}
	
	fun spawnEcho(x: Double, y: Double, z: Double) {
		for (i in 0..63) {
			m.set(Math.random() - 0.5, 0.0, Math.random() - 0.5).normalize().mul(0.5)
			Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x, y + 0.2, z, 0.5f, 0.5f, 0.5f, 1f, m.x.toFloat(), 0f, m.z.toFloat())
		}
	}
	
	fun spawnEchoEntity(x: Double, y: Double, z: Double) {
		Botania.proxy.setWispFXDepthTest(false)
		Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x, y + 0.2, z, 1f, 1f, 0f, 1f, 0f, 3f)
		Botania.proxy.setWispFXDepthTest(true)
	}
	
	fun spawnEchoItem(x: Double, y: Double, z: Double) {
		Botania.proxy.setWispFXDepthTest(false)
		Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x, y + 0.2, z, 0f, 1f, 0f, 1f, 0f, 3f)
		Botania.proxy.setWispFXDepthTest(true)
	}
	
	fun spawnEchoMob(x: Double, y: Double, z: Double) {
		Botania.proxy.setWispFXDepthTest(false)
		Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x, y + 0.2, z, 1f, 0f, 0f, 1f, 0f, 3f)
		Botania.proxy.setWispFXDepthTest(true)
	}
	
	fun spawnEchoPlayer(x: Double, y: Double, z: Double) {
		Botania.proxy.setWispFXDepthTest(false)
		Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x, y + 0.2, z, 0f, 0f, 1f, 1f, 0f, 3f)
		Botania.proxy.setWispFXDepthTest(true)
	}
	
	fun spawnExplosion(x: Double, y: Double, z: Double) {
		Minecraft.getMinecraft().theWorld.spawnParticle("largeexplode", x, y, z, 1.0, 0.0, 0.0)
		
		for (i in 0..31) {
			m.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize().mul(0.15)
			Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x, y, z, 1f, Math.random().toFloat() * 0.25f, Math.random().toFloat() * 0.075f, 0.25f + Math.random().toFloat() * 0.45f, m.x.toFloat(), m.y.toFloat(), m.z.toFloat(), 0.5f)
		}
	}
	
	fun spawnGaiaSoul(x: Double, y: Double, z: Double) {
		(Minecraft.getMinecraft().theWorld.getTileEntity(x.toInt(), y.toInt(), z.toInt()) as? TileManaInfuser)?.soulParticlesTime = 20
	}
	
	fun spawnGravity(x: Double, y: Double, z: Double, x2: Double, y2: Double, z2: Double) {
		Minecraft.getMinecraft().theWorld.spawnParticle("smoke", x, y, z, x2, y2, z2)
	}
	
	fun spawnMana(living: EntityLivingBase, mana: Double) {
		var mana = mana
		mana *= 0.5
		val d = Math.random() * mana
		m.set(Math.random() - 0.5, 0.0, Math.random() - 0.5).normalize().mul(Math.random()).mul(1.0 * (mana * 0.25) - d / mana * (mana * 2.0 / 7.0)).add(0.0, d, 0.0)
		Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, living.posX + m.x, living.posY + m.y, living.posZ + m.z,
							 0.025f, 0.15f, 0.9f, (Math.random() * (mana * 0.5) + 0.5).toFloat(),
							 0f, (Math.random() * 2.0 + 1.0).toFloat())
	}
	
	fun spawnNote(x: Double, y: Double, z: Double) {
		Minecraft.getMinecraft().theWorld.spawnParticle("note", x, y, z, Minecraft.getMinecraft().theWorld.rand.nextInt(25) / 24.0, 0.0, 0.0)
	}
	
	fun spawnPotion(x: Double, y: Double, z: Double, color: Int, insta: Boolean) {
		val worldObj = Minecraft.getMinecraft().theWorld
		val rand = worldObj.rand
		
		if (worldObj.isRemote) {
			for (acc in worldObj.worldAccesses) {
				if (acc !is RenderGlobal) continue
				
				val s = "iconcrack_${Item.getIdFromItem(AlfheimItems.splashPotion)}_0"
				
				for (i in 0..8) {
					worldObj.spawnParticle(s, x, y, z, rand.nextGaussian() * 0.15, rand.nextDouble() * 0.2, rand.nextGaussian() * 0.15)
				}
				
				val f = (color shr 16 and 255).toFloat() / 255.0f
				val f1 = (color shr 8 and 255).toFloat() / 255.0f
				val f2 = (color shr 0 and 255).toFloat() / 255.0f
				val s1 = if (insta) "instantSpell" else "spell"
				
				for (l2 in 1..100) {
					val d4 = rand.nextDouble() * 4.0
					val d13 = rand.nextDouble() * Math.PI * 2.0
					val d5 = cos(d13) * d4
					val d6 = 0.01 + rand.nextDouble() * 0.5
					val d7 = sin(d13) * d4
					
					val entityfx = acc.doSpawnParticle(s1, x + d5 * 0.1, y + 0.3, z + d7 * 0.1, d5, d6, d7)
					
					if (entityfx != null) {
						val f4 = 0.75f + rand.nextFloat() * 0.25f
						entityfx.setRBGColorF(f * f4, f1 * f4, f2 * f4)
						entityfx.multiplyVelocity(d4.toFloat())
					}
				}
				
				worldObj.playSound(x + 0.5, y + 0.5, z + 0.5, "game.potion.smash", 1.0f, worldObj.rand.nextFloat() * 0.1f + 0.9f, false)
			}
		}
	}
	
	fun spawnPure(x: Double, y: Double, z: Double) {
		for (i in 0..63) {
			m.set(Math.random() - 0.5, 0.0, Math.random() - 0.5).normalize().mul(SpellPurifyingSurface.radius / 25)
			Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x, y + 0.2, z, 0f, 0.75f, 1f, 1f, m.x.toFloat(), 0f, m.z.toFloat())
		}
	}
	
	// TODO change to OSM particles
	fun spawnSmoke(x: Double, y: Double, z: Double) {
		for (i in 0..255) {
			m.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize().mul(Math.random() * SpellSmokeScreen.radius)
			Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x + m.x, y + m.y, z + m.z, 0.1f, 0.1f, 0.1f, (Math.random() * 4 + 4).toFloat(), (Math.random() * -0.075).toFloat())
		}
	}
	
	fun spawnSplash(x: Double, y: Double, z: Double) {
		for (j in 0..31) {
			m.rand().sub(0.5, 0.0, 0.5).normalize().mul(Math.random() * 0.5 + 0.5).mul(0.5).mul(0.5, 2.0, 0.5)
			Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x, y, z, 0.1f, 0.5f, 1f, 0.5f, m.x.toFloat(), m.y.toFloat(), m.z.toFloat(), 0.5f)
		}
	}
	
	fun spawnThrow(x: Double, y: Double, z: Double, x2: Double, y2: Double, z2: Double) {
		for (i in 0..7) Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x + Math.random() - 0.5, y + Math.random() * 0.25, z + Math.random() - 0.5, 0f, 1f, 0.25f, 1f, x2.toFloat(), y2.toFloat(), z2.toFloat())
	}
	
	fun spawnTremors(x: Double, y: Double, z: Double) {
		val block = Minecraft.getMinecraft().theWorld.getBlock(x.mfloor(), y.mfloor() - 1, z.mfloor())
		val meta = Minecraft.getMinecraft().theWorld.getBlockMetadata(x.mfloor(), y.mfloor() - 1, z.mfloor())
		for (i in 0..511) {
			m.set(Math.random() - 0.5, 0.0, Math.random() - 0.5).normalize().mul(Math.random() * 1.5 + 0.5).set(m.x, Math.random() * 0.25, m.z)
			Minecraft.getMinecraft().theWorld.spawnParticle("blockdust_" + Block.getIdFromBlock(block) + "_" + meta, x, y + 0.25, z, m.x, m.y, m.z)
		}
	}
	
	enum class VisualEffects {
		ACID, AQUABIND, AQUASTREAM, AQUASTREAM_HIT, DISPEL, ECHO, ECHO_ENTITY, ECHO_ITEM, ECHO_MOB, ECHO_PLAYER, EXPL, GAIA_SOUL, GRAVITY, HEAL, HORN, ICELENS, MANA, MOON, NOTE, NVISION, POTION, PURE, PURE_AREA, QUAD, QUADH, SHADOW, SMOKE, SPLASH, THROW, TREMORS, WIRE, UPHEAL
	}
	
	fun onDeath(target: EntityLivingBase) {
		if (AlfheimCore.enableMMO) {
			target.hurtTime = 0
			target.deathTime = 0
			target.attackTime = 0
			
			if (Minecraft.getMinecraft().thePlayer === target) {
				Minecraft.getMinecraft().displayGuiScreen(GUIDeathTimer())
				Minecraft.getMinecraft().setIngameNotInFocus()
			}
		}
	}
	
	fun onDeathTick(target: EntityLivingBase) {
		if (AlfheimCore.enableMMO) {
			if (target === Minecraft.getMinecraft().thePlayer && Minecraft.getMinecraft().currentScreen !is GUIDeathTimer)
				Minecraft.getMinecraft().displayGuiScreen(GUIDeathTimer())
			
			target.hurtTime = 0
			target.deathTime = 0
			target.attackTime = 0
			
			var c = 0xFFFFFF
			if (target is EntityPlayer) c = target.race.rgbColor
			Botania.proxy.wispFX(target.worldObj, target.posX, target.posY - if (Minecraft.getMinecraft().thePlayer === target) 1.5 else 0.0, target.posZ, (c shr 16 and 0xFF) / 255f, (c shr 8 and 0xFF) / 255f, (c and 0xFF) / 255f, (Math.random() * 0.5).toFloat(), (Math.random() * 0.015 - 0.0075).toFloat(), (Math.random() * 0.025).toFloat(), (Math.random() * 0.015 - 0.0075).toFloat(), 2f)
		}
	}
	
	fun onRespawned() {
		Minecraft.getMinecraft().displayGuiScreen(null)
		Minecraft.getMinecraft().setIngameFocus()
	}
}