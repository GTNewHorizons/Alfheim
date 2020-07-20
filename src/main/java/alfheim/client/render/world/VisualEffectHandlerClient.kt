package alfheim.client.render.world

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.api.ModInfo
import alfheim.api.entity.race
import alfheim.client.gui.GUIDeathTimer
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects.*
import alfheim.common.block.tile.TileManaInfuser
import alfheim.common.block.tile.sub.anomaly.SubTileManaVoid
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.item.AlfheimItems
import alfheim.common.item.rod.ItemRodInterdiction
import alfheim.common.spell.illusion.SpellSmokeScreen
import alfheim.common.spell.water.*
import net.minecraft.client.renderer.RenderGlobal
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.PotionEffect
import vazkii.botania.common.Botania
import vazkii.botania.common.entity.EntityManaBurst
import java.awt.Color
import kotlin.math.*
import vazkii.botania.common.core.helper.Vector3 as Bector3

object VisualEffectHandlerClient {
	
	val activeEmblems = HashMap<Int, Boolean>()
	val v = Vector3()
	val b = Bector3()
	
	fun select(s: VisualEffects, d: DoubleArray) {
		when (s) {
			ACID              -> spawnAcid(d[0], d[1], d[2])
			AQUABIND          -> spawnAquaBind(d[0], d[1], d[2])
			AQUASTREAM_HIT    -> spawnAquaStreamHit(d[0], d[1], d[2])
			BIFROST           -> spawnBifrost(d[0], d[1], d[2])
			BIFROST_DONE      -> spawnBifrostFinish(d[0], d[1], d[2], d[3].I)
			DISPEL            -> spawnBurst(d[0], d[1], d[2], 1f, 0f, 0f)
			ECHO              -> spawnEcho(d[0], d[1], d[2])
			ECHO_ENTITY       -> spawnEchoEntity(d[0], d[1], d[2])
			ECHO_ITEM         -> spawnEchoItem(d[0], d[1], d[2])
			ECHO_MOB          -> spawnEchoMob(d[0], d[1], d[2])
			ECHO_PLAYER       -> spawnEchoPlayer(d[0], d[1], d[2])
			EMBLEM_ACTIVATION -> activateEmblem(d[0], d[1])
			EXPL              -> spawnExplosion(d[0], d[1], d[2])
			FLAMESTAR         -> spawnFlameStar(d[0], d[1], d[2], d[3], d[4], d[5])
			GAIA_SOUL         -> spawnGaiaSoul(d[0], d[1], d[2])
			GRAVITY           -> spawnGravity(d[0], d[1], d[2], d[3], d[4], d[5])
			GUNGNIR           -> spawnGungnir(d[0].I)
			HEAL              -> spawnBurst(d[0], d[1], d[2], 0f, 1f, 0f)
			HORN              -> horn(d[0], d[1], d[2])
			ICELENS           -> addIceLens()
			LIGHTNING         -> spawnLightning(d[0], d[1], d[2], d[3], d[4], d[5], d[6].F, d[7].I, d[8].I, d.getOrElse(9) { 1.0 }.I)
			MANA              -> addMana(d[0], d[1].I)
			MANABURST         -> spawnManaburst(d[0], d[1], d[2])
			MANAVOID          -> spawnManaVoid(d[0], d[1], d[2], d[3], d[4], d[5])
			MOON              -> moonBoom(d[0], d[1], d[2])
			NOTE              -> spawnNote(d[0], d[1], d[2])
			NVISION           -> spawnBurst(d[0], d[1], d[2], 0f, 0f, 1f)
			POTION            -> spawnPotion(d[0], d[1], d[2], d[3].I, d[4] == 1.0)
			PURE              -> spawnBurst(d[0], d[1], d[2], 0f, 0.75f, 1f)
			PURE_AREA         -> spawnPure(d[0], d[1], d[2])
			QUAD              -> quadDamage()
			QUADH             -> quadHurt()
			SEAROD            -> (AlfheimItems.rodInterdiction as ItemRodInterdiction).particleRing(mc.theWorld, d[0], d[1], d[2], d[3].I, d[4].F, d[5].F, d[6].F)
			SHADOW            -> spawnBurst(d[0], d[1], d[2], 0.75f, 0.75f, 0.75f)
			SMOKE             -> spawnSmoke(d[0], d[1], d[2])
			SPLASH            -> spawnSplash(d[0], d[1], d[2])
			THROW             -> spawnThrow(d[0], d[1], d[2], d[3], d[4], d[5])
			TREMORS           -> spawnTremors(d[0], d[1], d[2])
			UPHEAL            -> spawnBurst(d[0], d[1], d[2], 1f, 0.75f, 0f)
			WIRE              -> spawnWire(d[0], d[1], d[2], d[3])
			WISP              -> spawnWisp(d[0], d[1], d[2], d[3].F, d[4].F, d[5].F, d[6].F, d[7].F, d[8].F, d[9].F, d[10].F, d.getOrNull(11) == null)
		}
	}
	
	fun activateEmblem(eID: Double, active: Double) {
		activeEmblems[eID.I] = active != 0.0
	}
	
	fun addIceLens() {
		mc.thePlayer.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDIceLens, SpellIceLens.duration, -1, true))
	}
	
	fun addMana(enID: Double, mana: Int) {
		val e = mc.theWorld.getEntityByID(enID.I) as? EntityPlayer ?: return
		
		if (mana == 0 || mana.I == Int.MAX_VALUE) {
			var d = 0.0
			while (d < 1.0) {
				spawnBurst(e.posX, e.posY + d, e.posZ, 0.975f, if (mana == 0) 0.1f else 0.85f, 0.1f)
				d += .2
			}
		} else
			e.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDShowMana, mana, 100, true))
	}
	
	fun horn(x: Double, y: Double, z: Double) {
		mc.theWorld.playSound(x, y, z, ModInfo.MODID + ":horn.bhorn", 100f, 0.8f + mc.theWorld.rand.nextFloat() * 0.2f, false)
	}
	
	fun moonBoom(x: Double, y: Double, z: Double) {
		for (i in 0..63) {
			v.rand().sub(0.5).normalize().mul(0.1)
			Botania.proxy.wispFX(mc.theWorld, x, y, z, 0.3f + (Math.random().F - 0.5f) * 0.1f, 0.85f + (Math.random().F - 0.5f) * 0.1f, 0.85f + (Math.random().F - 0.5f) * 0.1f, 0.5f, v.x.F, v.y.F, v.z.F)
		}
	}
	
	fun quadDamage() {
		mc.thePlayer.playSound(ModInfo.MODID + ":quad", 10f, 1f)
	}
	
	fun quadHurt() {
		mc.thePlayer.playSound(ModInfo.MODID + ":quadh", 1f, 1f)
	}
	
	fun spawnAcid(x: Double, y: Double, z: Double) {
		for (i in 0..255) {
			v.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize().mul(Math.random() * 9, Math.random() * 9, Math.random() * 9)
			Botania.proxy.wispFX(mc.theWorld, x + v.x, y + v.y, z + v.z, (Math.random() * 0.2).F, 1f, 0f, 2f, 0f, 2f)
		}
	}
	
	fun spawnAquaBind(x: Double, y: Double, z: Double) {
		var j = 0
		while (j < 360) {
			val i = cos(j.D) * SpellAquaBind.radius
			val k = sin(j.D) * SpellAquaBind.radius
			Botania.proxy.wispFX(mc.theWorld, x + i, y, z + k, 0f, 0.5f, 1f, 0.5f)
			j += 5
		}
	}
	
	fun spawnWisp(x: Double, y: Double, z: Double, red: Float, green: Float, blue: Float, size: Float, mx: Float, my: Float, mz: Float, age: Float, depth: Boolean) {
		Botania.proxy.setWispFXDepthTest(depth)
		Botania.proxy.wispFX(mc.theWorld, x, y, z, red, green, blue, size, mx, my, mz, age)
		Botania.proxy.setWispFXDepthTest(true)
	}
	
	fun spawnAquaStreamHit(x: Double, y: Double, z: Double) {
		Botania.proxy.wispFX(mc.theWorld, x, y, z, 0f, 0.5f, 1f, 0.5f)
	}
	
	fun spawnBifrost(x: Double, y: Double, z: Double) {
		for (a in 0..15) {
			val color = Color(Color.HSBtoRGB(mc.theWorld.rand.nextFloat(), 1F, 1F))
			Botania.proxy.wispFX(mc.theWorld, x + Math.random() * 3 - 1, y + Math.random(), z + Math.random() * 3 - 1, color.red / 255f, color.green / 255f, color.blue / 255f, 1f, -1f, 2f)
		}
	}
	
	fun spawnBifrostFinish(x: Double, y: Double, z: Double, id: Int) {
		mc.theWorld.getEntityByID(id)?.let { it.motionY += 5.0 }
		
		for (a in 0..64) {
			v.rand().normalize().mul(0.1)
			val color = Color(Color.HSBtoRGB(mc.theWorld.rand.nextFloat(), 1F, 1F))
			Botania.proxy.wispFX(mc.theWorld, x, y, z, color.red / 255f, color.green / 255f, color.blue / 255f, 1f, v.x.F, v.y.F, v.z.F, 2f)
		}
	}
	
	fun spawnBurst(x: Double, y: Double, z: Double, r: Float, g: Float, b: Float) {
		for (i in 0..7) Botania.proxy.wispFX(mc.theWorld, x + Math.random() - 0.5, y + Math.random() * 0.25, z + Math.random() - 0.5, r, g, b, 1f, (Math.random() * -0.075).F)
	}
	
	fun spawnEcho(x: Double, y: Double, z: Double) {
		for (i in 0..63) {
			v.set(Math.random() - 0.5, 0.0, Math.random() - 0.5).normalize().mul(0.5)
			Botania.proxy.wispFX(mc.theWorld, x, y + 0.2, z, 0.5f, 0.5f, 0.5f, 1f, v.x.F, 0f, v.z.F)
		}
	}
	
	fun spawnEchoEntity(x: Double, y: Double, z: Double) {
		Botania.proxy.setWispFXDepthTest(false)
		Botania.proxy.wispFX(mc.theWorld, x, y + 0.2, z, 1f, 1f, 0f, 1f, 0f, 3f)
		Botania.proxy.setWispFXDepthTest(true)
	}
	
	fun spawnEchoItem(x: Double, y: Double, z: Double) {
		Botania.proxy.setWispFXDepthTest(false)
		Botania.proxy.wispFX(mc.theWorld, x, y + 0.2, z, 0f, 1f, 0f, 1f, 0f, 3f)
		Botania.proxy.setWispFXDepthTest(true)
	}
	
	fun spawnEchoMob(x: Double, y: Double, z: Double) {
		Botania.proxy.setWispFXDepthTest(false)
		Botania.proxy.wispFX(mc.theWorld, x, y + 0.2, z, 1f, 0f, 0f, 1f, 0f, 3f)
		Botania.proxy.setWispFXDepthTest(true)
	}
	
	fun spawnEchoPlayer(x: Double, y: Double, z: Double) {
		Botania.proxy.setWispFXDepthTest(false)
		Botania.proxy.wispFX(mc.theWorld, x, y + 0.2, z, 0f, 0f, 1f, 1f, 0f, 3f)
		Botania.proxy.setWispFXDepthTest(true)
	}
	
	fun spawnExplosion(x: Double, y: Double, z: Double) {
		mc.theWorld.spawnParticle("largeexplode", x, y, z, 1.0, 0.0, 0.0)
		
		for (i in 0..31) {
			v.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize().mul(0.15)
			Botania.proxy.wispFX(mc.theWorld, x, y, z, 1f, Math.random().F * 0.25f, Math.random().F * 0.075f, 0.25f + Math.random().F * 0.45f, v.x.F, v.y.F, v.z.F, 0.5f)
		}
	}
	
	fun spawnFlameStar(x: Double, y: Double, z: Double, x2: Double, y2: Double, z2: Double) {
		Botania.proxy.sparkleFX(mc.theWorld, x, y, z, x2.F, y2.F, z2.F, 1f, 5)
	}
	
	fun spawnGaiaSoul(x: Double, y: Double, z: Double) {
		(mc.theWorld.getTileEntity(x.I, y.I, z.I) as? TileManaInfuser)?.soulParticlesTime = 20
	}
	
	fun spawnGravity(x: Double, y: Double, z: Double, x2: Double, y2: Double, z2: Double) {
		mc.theWorld.spawnParticle("smoke", x, y, z, x2, y2, z2)
	}
	
	fun spawnGungnir(id: Int) {
		val player = mc.theWorld.getEntityByID(id) as? EntityPlayer ?: return
		mc.theWorld.spawnEntityInWorld(EntityManaBurst(player).also {
			val motionModifier = 32
			it.color = 0xFFD400
			it.mana = 1
			it.startingMana = 1
			it.minManaLoss = 200
			it.manaLossPerTick = 1f
			it.gravity = 0f
			it.setMotion(it.motionX * motionModifier, it.motionY * motionModifier, it.motionZ * motionModifier)
		})
	}
	
	fun spawnLightning(x: Double, y: Double, z: Double, x2: Double, y2: Double, z2: Double, speed: Float, color: Int, color2: Int, count: Int) {
		for (i in 0 until count)
			Botania.proxy.lightningFX(mc.theWorld, b.set(x, y, z), Bector3(x2, y2, z2), speed, color, color2)
	}
	
	fun spawnMana(living: EntityLivingBase, mana: Double) {
		val d = Math.random() * mana
		v.set(Math.random() - 0.5, 0.0, Math.random() - 0.5).normalize().mul(Math.random()).mul(1.0 * (mana * 0.25) - d / mana * (mana * 2.0 / 7.0)).add(0.0, d, 0.0)
		Botania.proxy.wispFX(mc.theWorld, living.posX + v.x, living.posY + v.y, living.posZ + v.z,
							 0.025f, 0.15f, 0.9f, (Math.random() * (mana * 0.5) + 0.5).F,
							 0f, (Math.random() * 2.0 + 1.0).F)
	}
	
	fun spawnManaburst(x: Double, y: Double, z: Double) {
		for (i in 0..127) {
			v.rand().sub(0.5).normalize().mul(Math.random() * 0.1)
			Botania.proxy.wispFX(mc.theWorld, x + 0.5, y + 0.5, z + 0.5, 0.01f, 0.75f, 1f, 0.25f, v.x.F, v.y.F, v.z.F, 2f)
		}
	}
	
	fun spawnManaVoid(x: Double, y: Double, z: Double, x2: Double, y2: Double, z2: Double) {
		val l = v.set(x, y, z).add(0.5).sub(x2, y2, z2).length()
		v.normalize().mul(l / 40)
		Botania.proxy.wispFX(mc.theWorld, x2, y2, z2, 0.01f, 0.75f, 1f, SubTileManaVoid.radius / 40f, v.x.F, v.y.F, v.z.F, 2f)
	}
	
	fun spawnNote(x: Double, y: Double, z: Double) {
		mc.theWorld.spawnParticle("note", x, y, z, mc.theWorld.rand.nextInt(25) / 24.0, 0.0, 0.0)
	}
	
	fun spawnPotion(x: Double, y: Double, z: Double, color: Int, insta: Boolean) {
		val worldObj = mc.theWorld
		val rand = worldObj.rand
		
		if (worldObj.isRemote) {
			for (acc in worldObj.worldAccesses) {
				if (acc !is RenderGlobal) continue
				
				val s = "iconcrack_${AlfheimItems.splashPotion.id}_0"
				
				for (i in 0..8) {
					worldObj.spawnParticle(s, x, y, z, rand.nextGaussian() * 0.15, rand.nextDouble() * 0.2, rand.nextGaussian() * 0.15)
				}
				
				val f = (color shr 16 and 255).F / 255f
				val f1 = (color shr 8 and 255).F / 255f
				val f2 = (color shr 0 and 255).F / 255f
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
						entityfx.multiplyVelocity(d4.F)
					}
				}
				
				worldObj.playSound(x + 0.5, y + 0.5, z + 0.5, "game.potion.smash", 1f, worldObj.rand.nextFloat() * 0.1f + 0.9f, false)
			}
		}
	}
	
	fun spawnPure(x: Double, y: Double, z: Double) {
		for (i in 0..63) {
			v.set(Math.random() - 0.5, 0.0, Math.random() - 0.5).normalize().mul(SpellPurifyingSurface.radius / 25)
			Botania.proxy.wispFX(mc.theWorld, x, y + 0.2, z, 0f, 0.75f, 1f, 1f, v.x.F, 0f, v.z.F)
		}
	}
	
	// TODO change to OSM particles
	fun spawnSmoke(x: Double, y: Double, z: Double) {
		for (i in 0..255) {
			v.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize().mul(Math.random() * SpellSmokeScreen.radius)
			Botania.proxy.wispFX(mc.theWorld, x + v.x, y + v.y, z + v.z, 0.1f, 0.1f, 0.1f, (Math.random() * 4 + 4).F, (Math.random() * -0.075).F)
		}
	}
	
	fun spawnSplash(x: Double, y: Double, z: Double) {
		for (j in 0..31) {
			v.rand().sub(0.5, 0.0, 0.5).normalize().mul(Math.random() * 0.5 + 0.5).mul(0.5).mul(0.5, 2.0, 0.5)
			Botania.proxy.wispFX(mc.theWorld, x, y, z, 0.1f, 0.5f, 1f, 0.5f, v.x.F, v.y.F, v.z.F, 0.5f)
		}
	}
	
	fun spawnThrow(x: Double, y: Double, z: Double, x2: Double, y2: Double, z2: Double) {
		for (i in 0..7) Botania.proxy.wispFX(mc.theWorld, x + Math.random() - 0.5, y + Math.random() * 0.25, z + Math.random() - 0.5, 0f, 1f, 0.25f, 1f, x2.F, y2.F, z2.F)
	}
	
	fun spawnTremors(x: Double, y: Double, z: Double) {
		val block = mc.theWorld.getBlock(x.mfloor(), y.mfloor() - 1, z.mfloor())
		val meta = mc.theWorld.getBlockMetadata(x.mfloor(), y.mfloor() - 1, z.mfloor())
		for (i in 0..511) {
			v.set(Math.random() - 0.5, 0.0, Math.random() - 0.5).normalize().mul(Math.random() * 1.5 + 0.5).set(v.x, Math.random() * 0.25, v.z)
			mc.theWorld.spawnParticle("blockdust_${block.id}_$meta", x, y + 0.25, z, v.x, v.y, v.z)
		}
	}
	
	fun spawnWire(x: Double, y: Double, z: Double, range: Double) {
		for (i in 0..20) {
			Botania.proxy.lightningFX(mc.theWorld, b.set(x, y, z), b.add(randomVec(range)), (range * 0.01).F, 255 shl 16, 0)
		}
	}
	
	private fun randomVec(length: Double): vazkii.botania.common.core.helper.Vector3 {
		val vec = Bector3(0.0, Math.random() * length, 0.0)
		vec.rotate(Math.random() * Math.PI * 2, Bector3(1.0, 0.0, 0.0))
		vec.rotate(Math.random() * Math.PI * 2, Bector3(0.0, 0.0, 1.0))
		return vec
	}
	
	enum class VisualEffects {
		ACID, AQUABIND, AQUASTREAM_HIT, BIFROST, BIFROST_DONE, DISPEL, ECHO, ECHO_ENTITY, ECHO_ITEM, ECHO_MOB, ECHO_PLAYER, EMBLEM_ACTIVATION, EXPL, FLAMESTAR, GAIA_SOUL, GRAVITY, GUNGNIR, HEAL, HORN, ICELENS, LIGHTNING, MANA, MANABURST, MANAVOID, MOON, NOTE, NVISION, POTION, PURE, PURE_AREA, QUAD, QUADH, SEAROD, SHADOW, SMOKE, SPLASH, THROW, TREMORS, UPHEAL, WIRE, WISP
	}
	
	fun onDeath(target: EntityLivingBase) {
		if (AlfheimConfigHandler.enableMMO) {
			target.hurtTime = 0
			target.deathTime = 0
			target.attackTime = 0
			
			if (mc.thePlayer === target) {
				mc.displayGuiScreen(GUIDeathTimer())
				mc.setIngameNotInFocus()
			}
		}
	}
	
	fun onDeathTick(target: EntityLivingBase) {
		if (AlfheimConfigHandler.enableMMO) {
			if (target === mc.thePlayer && mc.currentScreen !is GUIDeathTimer)
				mc.displayGuiScreen(GUIDeathTimer())
			
			target.hurtTime = 0
			target.deathTime = 0
			target.attackTime = 0
			
			var c = 0xFFFFFF
			if (target is EntityPlayer) c = target.race.rgbColor
			Botania.proxy.wispFX(target.worldObj, target.posX, target.posY - if (mc.thePlayer === target) 1.5 else 0.0, target.posZ, (c shr 16 and 0xFF) / 255f, (c shr 8 and 0xFF) / 255f, (c and 0xFF) / 255f, (Math.random() * 0.5).F, (Math.random() * 0.015 - 0.0075).F, (Math.random() * 0.025).F, (Math.random() * 0.015 - 0.0075).F, 2f)
		}
	}
}