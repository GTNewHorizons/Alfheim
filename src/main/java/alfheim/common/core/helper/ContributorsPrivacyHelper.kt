package alfheim.common.core.helper

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.common.core.util.eventFML
import alfheim.common.network.MessageContributor
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.PlayerEvent
import cpw.mods.fml.common.network.FMLNetworkEvent
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.*
import net.minecraft.server.MinecraftServer
import java.io.File
import java.nio.charset.Charset
import java.security.*
import java.util.*
import javax.xml.bind.annotation.adapters.HexBinaryAdapter
import kotlin.collections.HashMap
import kotlin.experimental.xor

object ContributorsPrivacyHelper {
	
	// contributor - username alias
			val contributors = HashMap<String, String>()
	private	val authCredits = HashMap<String, String>()
	
	init {
		this.eventFML()
		
		register("AlexSocol", "A09B76CE9EAAD88CC035AB5FFFF9448B45777374820934ACDA676177581D8D0C")
		register("KAIIIAK", "1F437C5DF089B08A33220A8DC55F0CEADCD673A4D511F6FF47A9400B86C61C77")
		register("GedeonGrays", "55F9BF8DFEA11B09ECF12BE0E4859C6F0C07F4BAAAEE665CB074E294E841AC99")
	}
	
	private fun register(contributor: String, passwordHash: String) {
		authCredits[contributor] = passwordHash
		contributors[contributor] = contributor
	}
	
	fun isRegistered(login: String) = authCredits.contains(login)
	
	fun getPassHash(login: String) = authCredits[login]
	
	fun isCorrect(user: EntityPlayer, contributor: String) = isCorrect(user.commandSenderName, contributor)
	fun isCorrect(user: String, contributor: String) = contributors[contributor] == user
	
	@SubscribeEvent
	fun onPlayerLogin(e: PlayerEvent.PlayerLoggedInEvent) {
		val player = e.player as? EntityPlayerMP ?: return
		
		if (MinecraftServer.getServer()?.isSinglePlayer == true) return
		
		AlfheimCore.network.sendTo(MessageContributor(true), player)
	}
	
	@SubscribeEvent
	fun onPlayerLogout(e: PlayerEvent.PlayerLoggedOutEvent) {
		ASJUtilities.mapGetKey(contributors, e.player.commandSenderName)?.let { contributors.remove(it) }
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