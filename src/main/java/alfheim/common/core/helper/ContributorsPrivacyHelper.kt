package alfheim.common.core.helper

import alexsocol.asjlib.*
import alfheim.AlfheimCore
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.network.MessageContributor
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.*
import net.minecraft.entity.player.*
import net.minecraft.server.MinecraftServer
import java.net.URL
import java.nio.charset.Charset
import java.security.*
import java.util.*
import javax.xml.bind.annotation.adapters.HexBinaryAdapter
import kotlin.collections.HashMap
import kotlin.experimental.xor

object ContributorsPrivacyHelper {
	
			//  contributor - username alias
			val contributors = HashMap<String, String>()
	private	val authCredits = HashMap<String, String>()
	
	val shields = HashMap<String, Int>()
	
	init {
		this.eventFML()
		download()
	}
	
	private fun download() {
		try {
			URL("https://bitbucket.org/AlexSocol/alfheim/raw/master/hashes.txt").openConnection().also { it.connectTimeout = 5000; it.readTimeout = 5000 }.getInputStream().bufferedReader().readLines().paired().forEach { (k, v) -> register(k, v) }
		} catch (e: Throwable) {
			ASJUtilities.error("Failed to register contributors, using default parameters")
			// default username:password pairs just in case
			register("AlexSocol", "C483AC3FF3031172FD8D1EB5A727B186C4059B927C38C0A19C202D748D2D0428")
			register("GedeonGrays", "B2612EA4C009B2C3FDDCAA7D6C1FFB8DD6C9C7ECFFD785DCD1A08BB41CAD47C0")
			register("KAIIIAK", "D761FAABD0C7F4042189C0CE308FDAD79566B198416BFDE23361EBA8DCB0BB96")
		}
		
		try {
			URL("https://bitbucket.org/AlexSocol/alfheim/raw/master/patrons.txt").openConnection().also { it.connectTimeout = 5000; it.readTimeout = 5000 }.getInputStream().bufferedReader().readLines().forEach { it.split(":").also { (k, v) -> shields[k] = v.toIntOrNull() ?: -1 } }
		} catch (e: Throwable) {
			ASJUtilities.error("Failed to register patrons")
		}
	}
	
	private fun register(contributor: String, passwordHash: String) {
		authCredits[contributor] = passwordHash
		
		if (MinecraftServer.getServer()?.isSinglePlayer != false)
			contributors[contributor] = contributor // no power on server if no response
	}
	
	fun isRegistered(login: String) = authCredits.contains(login)
	
	fun getPassHash(login: String) = authCredits[login]
	
	fun isCorrect(user: EntityPlayer, contributor: String) = isCorrect(user.commandSenderName, contributor)
	
	fun isCorrect(user: String, contributor: String) = contributors[contributor] == user
	
	val authTimeout = HashMap<String, Int>()
	
	@SubscribeEvent
	fun onServerTick(e: TickEvent.ServerTickEvent) {
		for (name in authTimeout.keys) {
			val timeLeft = authTimeout[name] ?: continue
			if (timeLeft == -1) continue
			
			authTimeout[name] = timeLeft - 1
			// no response in N seconds - kick
			if (timeLeft == 0)
				MinecraftServer.getServer()?.configurationManager?.func_152612_a(name)?.playerNetServerHandler?.kickPlayerFromServer("Authentication request timed out")
		}
	}
	
	@SubscribeEvent
	fun onPlayerLogin(e: PlayerEvent.PlayerLoggedInEvent) {
		val player = e.player as? EntityPlayerMP ?: return
		
		if (MinecraftServer.getServer()?.isSinglePlayer == true) return
		
		AlfheimCore.network.sendTo(MessageContributor(true), player)
		
		if (isRegistered(player.commandSenderName))
			authTimeout[player.commandSenderName] = AlfheimConfigHandler.authTimeout
	}
	
	@SubscribeEvent
	fun onPlayerLogout(e: PlayerEvent.PlayerLoggedOutEvent) {
		if (MinecraftServer.getServer()?.isSinglePlayer == true) return
		
		contributors.values.removeAll { it == e.player.commandSenderName }
	}
}

object HashHelper {
	
	fun hash(str: String?): String {
		if (str != null)
			try {
				val md = MessageDigest.getInstance("SHA-256")
				return HexBinaryAdapter().marshal(md.digest(salt(str).toByteArray(Charset.forName("UTF-8"))))
			} catch (e: NoSuchAlgorithmException) {
				e.printStackTrace()
			}
		
		return ""
	}
	
	// Might as well be called sugar given it's not secure at all :D
	fun salt(str: String): String {
		val salt = str + "soyeahthatsjustarandomuselesssecuritysaltthingsoyeah"
		val rand = Random(salt.length.toLong())
		val l = salt.length
		val steps = rand.nextInt(l)
		val chrs = salt.toCharArray()
		for (i in 0 until steps) {
			val indA = rand.nextInt(l)
			var indB: Int
			do {
				indB = rand.nextInt(l)
			} while (indB == indA)
			val c = (chrs[indA].toShort() xor chrs[indB].toShort()).toChar()
			chrs[indA] = c
		}
		
		return String(chrs)
	}
}