package alfheim.common.core.util

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import net.minecraft.util.StatCollector
import net.minecraftforge.common.MinecraftForge
import org.w3c.dom.Node
import java.net.URL
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory

object InfoLoader {
	
	val info: MutableList<String> = ArrayList()
	var outdated = false
	
	var doneChecking = false
	var onlineVersion = 0
	var triedToWarnPlayer = false
	
	fun start() {
		ThreadLoadInfo()
	}
	
	fun getNodeValue(root: Node, attributeValue: String): String {
		val versions = root.childNodes
		for (i in 0 until versions.length) {
			val version = versions.item(i)
			if (!version.hasChildNodes() || !version.hasAttributes() || version.attributes.item(0).nodeValue != attributeValue) continue
			
			val vals = version.childNodes // <string>s in next iteration
			for (j in 0 until vals.length) {
				val aval = vals.item(j)
				if (!aval.hasChildNodes()) continue
				
				// only one will be parsed
				return aval.childNodes.item(0).nodeValue
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
				onlineVersion = Integer.parseInt(latest.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])
				outdated = onlineVersion > Integer.parseInt(AlfheimCore.meta.version.replace("\\D".toRegex(), ""))
				if (outdated) info.add(StatCollector.translateToLocalFormatted("alfheimmisc.update", AlfheimCore.meta.version, latest))
				var s: String? = getNodeValue(root, "UNIVERSAL")
				info.addAll(listOf(*s!!.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()))
				s = getNodeValue(root, AlfheimCore.meta.version)
				if (s.isNotEmpty()) {
					info.add("=====================================================")
					info.addAll(listOf(*s.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()))
				}
				ASJUtilities.log("Successfully loaded news & version")
			} catch (e: Exception) {
				ASJUtilities.error("Unable to load news & version from official repo. Check your internet connection.")
				e.printStackTrace(System.err)
			}
			
			doneChecking = true
		}
	}
}
