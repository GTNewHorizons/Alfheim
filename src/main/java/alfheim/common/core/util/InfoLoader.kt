package alfheim.common.core.util

import java.net.URL
import java.util.ArrayList
import java.util.Arrays

import javax.xml.parsers.DocumentBuilderFactory

import org.w3c.dom.Node
import org.w3c.dom.NodeList

import alexsocol.asjlib.ASJUtilities
import alfheim.api.ModInfo
import net.minecraft.util.StatCollector
import net.minecraftforge.common.MinecraftForge

object InfoLoader {
	
	val info: MutableList<String> = ArrayList()
	var outdated = false
	
	var doneChecking = false
	var onlineVersion = 0
	var triedToWarnPlayer = false
	
	fun start() {
		ThreadLoadInfo()
		MinecraftForge.EVENT_BUS.register(InfoLoader())
	}
	
	fun getNodeValue(root: Node, attributeValue: String): String {
		val versions = root.childNodes
		for (i in 0 until versions.length) {
			val minecraft = versions.item(i) // <addon> in next iteration
			if (!minecraft.hasChildNodes() || !minecraft.hasAttributes() || minecraft.attributes.item(0).nodeValue != attributeValue) continue
			
			val links = minecraft.childNodes // <string>s in next iteration
			for (j in 0 until links.length) {
				val link = links.item(j)
				if (!link.hasChildNodes()) continue
				
				// only one will be parsed
				return link.childNodes.item(0).nodeValue
			}
		}
		return ""
	}
	
	internal class ThreadLoadInfo: Thread() {
		init {
			name = "Alfheim Version Checker Thread"
			isDaemon = true
			start()
		}
		
		override fun run() {
			try {
				val root = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(URL("https://bitbucket.org/AlexSocol/alfheim/raw/" + (if (ModInfo.DEV) "development" else "master") + "/news/" + MinecraftForge.MC_VERSION + ".xml").openStream()).documentElement
				val latest = getNodeValue(root, "LATEST")
				onlineVersion = Integer.parseInt(latest.split("-".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1])
				outdated = onlineVersion > Integer.parseInt(ModInfo.BUILD.replace("\\D".toRegex(), ""))
				if (outdated) info.add(StatCollector.translateToLocalFormatted("alfheimmisc.update", ModInfo.VERSION, latest))
				var s: String? = getNodeValue(root, "UNIVERSAL")
				info.addAll(Arrays.asList(*s!!.split("&".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()))
				s = getNodeValue(root, ModInfo.VERSION)
				if (s != null && !s.isEmpty()) {
					info.add("=====================================================")
					info.addAll(Arrays.asList(*s.split("&".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()))
				}
			} catch (e: Exception) {
				ASJUtilities.error("Unable to load news & version from official repo. Check your internet connection.")
				e.printStackTrace(System.err)
			}
			
			doneChecking = true
			ASJUtilities.log("Successfully loaded news & version")
		}
	}
}
