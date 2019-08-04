package alfheim.client.render.world

import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.api.entity.EnumRace
import alfheim.client.gui.GUIDeathTimer
import alfheim.client.render.world.SpellEffectHandlerClient.Spells.*
import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.core.util.mfloor
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.PotionEffect
import vazkii.botania.common.Botania
import kotlin.math.*
import vazkii.botania.common.core.helper.Vector3 as VVec3

object SpellEffectHandlerClient {
	
	internal val m = Vector3()
	
	fun select(s: Spells, x: Double, y: Double, z: Double, x2: Double, y2: Double, z2: Double) {
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
				GRAVITY        -> spawnGravity(x, y, z, x2, y2, z2)
				HEAL           -> spawnBurst(x, y, z, 0f, 1f, 0f)
				ICELENS        -> addIceLens()
				MANA           -> addMana(x, y)
				NOTE           -> spawnNote(x, y, z)
				NVISION        -> spawnBurst(x, y, z, 0f, 0f, 1f)
				PURE           -> spawnBurst(x, y, z, 0f, 0.75f, 1f)
				PURE_AREA      -> spawnPure(x, y, z)
				QUAD           -> quadDamage()
				QUADH          -> quadHurt()
				SMOKE          -> spawnSmoke(x, y, z)
				THROW          -> spawnThrow(x, y, z, x2, y2, z2)
				TREMORS        -> spawnTremors(x, y, z)
				UPHEAL         -> spawnBurst(x, y, z, 1f, 0.75f, 0f)
				WIRE           -> spawnWire(x, y, z, x2)
				else           -> {}
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
		Minecraft.getMinecraft().thePlayer.addPotionEffect(PotionEffect(AlfheimRegistry.icelens.id, 200, 0, true))
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
				e.addPotionEffect(PotionEffect(AlfheimRegistry.showMana.id, mana.toInt(), 100, true))
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
	
	fun spawnPure(x: Double, y: Double, z: Double) {
		for (i in 0..63) {
			m.set(Math.random() - 0.5, 0.0, Math.random() - 0.5).normalize().mul(0.2)
			Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x, y + 0.2, z, 0f, 0.75f, 1f, 1f, m.x.toFloat(), 0f, m.z.toFloat())
		}
	}
	
	// TODO change to OSM particles
	fun spawnSmoke(x: Double, y: Double, z: Double) {
		for (i in 0..255) {
			m.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize().mul(Minecraft.getMinecraft().theWorld.rand.nextInt(15) + Math.random())
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
			m.set(Math.random() - 0.5, 0.0, Math.random() - 0.5).normalize().mul(Math.random() * 1.5 + 0.5)[m.x, Math.random() * 0.25] = m.z
			Minecraft.getMinecraft().theWorld.spawnParticle("blockdust_" + Block.getIdFromBlock(block) + "_" + meta, x, y + 0.25, z, m.x, m.y, m.z)
		}
	}
	
	enum class Spells {
		ACID, AQUABIND, AQUASTREAM, AQUASTREAM_HIT, DISPEL, ECHO, ECHO_ENTITY, ECHO_ITEM, ECHO_MOB, ECHO_PLAYER, EXPL, GRAVITY, HEAL, ICELENS, MANA, NOTE, NVISION, PURE, PURE_AREA, QUAD, QUADH, SMOKE, SPLASH, THROW, TREMORS, WIRE, UPHEAL
	}
	
	fun onDeath(target: EntityLivingBase) {
		if (AlfheimCore.enableMMO && Minecraft.getMinecraft().thePlayer === target) {
			Minecraft.getMinecraft().displayGuiScreen(GUIDeathTimer())
			Minecraft.getMinecraft().thePlayer.hurtTime = 0
		}
	}
	
	fun onDeathTick(target: EntityLivingBase) {
		if (AlfheimCore.enableMMO) {
			var c = 0xFFFFFF
			if (target is EntityPlayer) c = EnumRace.getRace(target).rgbColor
			Botania.proxy.wispFX(target.worldObj, target.posX, target.posY - if (Minecraft.getMinecraft().thePlayer === target) 1.5 else 0.0, target.posZ, (c shr 16 and 0xFF) / 255f, (c shr 8 and 0xFF) / 255f, (c and 0xFF) / 255f, (Math.random() * 0.5).toFloat(), (Math.random() * 0.015 - 0.0075).toFloat(), (Math.random() * 0.025).toFloat(), (Math.random() * 0.015 - 0.0075).toFloat(), 2f)
		}
	}
	
	fun onRespawned() {
		Minecraft.getMinecraft().displayGuiScreen(null)
		Minecraft.getMinecraft().setIngameFocus()
	}
}