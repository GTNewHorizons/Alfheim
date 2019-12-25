package alfheim.common.core.asm

import alfheim.api.ModInfo
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.InfoLoader
import cpw.mods.fml.relauncher.*
import net.minecraftforge.common.MinecraftForge
import org.apache.logging.log4j.Level
import java.io.*
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

object AlfheimModularLoader {
	
	init {
		FMLRelaunchLog.info("[${ModInfo.MODID.toUpperCase()}] Trying to update Alfheim Modular...")
		
		if (AlfheimConfigHandler.modularThread)
			Thread(Runnable { download() }, "Alfheim Modular Download").also { it.isDaemon = true }.start()
		else
			download()
	}
	
	fun download() {
		try {
			val root = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(URL("https://bitbucket.org/AlexSocol/alfheim/raw/" + (if (ModInfo.DEV) "development" else "master") + "/news/" + MinecraftForge.MC_VERSION + ".xml").openStream()).documentElement
			val url = InfoLoader.getNodeValue(root, "MODULAR")
			var possibleMatch = false
			var download = false
			
			val mcVer = FMLInjectionData.data()[4] as String
			val mcDir = FMLInjectionData.data()[6] as File
			val v_modsDir = File(mcDir, "mods/$mcVer")
			if (!v_modsDir.exists()) {
				v_modsDir.mkdirs()
				download = true
			} else {
				possibleMatch = true
			}
			
			val fullname = url.substring(url.lastIndexOf('/') + 1)
			val modname = fullname.substring(0, fullname.lastIndexOf('-'))
			val version = fullname.substring(fullname.lastIndexOf('-') + 1).let { it.substring(0, it.lastIndexOf('.')) }
			
			if (possibleMatch) {
				v_modsDir.listFiles()?.forEach {
					if (it.name.startsWith(modname)) {
						val versionLocal = it.name.substring(it.name.lastIndexOf('-') + 1).let { ver -> ver.substring(0, ver.lastIndexOf('.')) }
						if (version != versionLocal) {
							it.delete()
							download = true
						}
					}
				}
			}
			
			if (download) {
				var err = "Unable to download Alfheim Modular from official repo. Check your internet connection"
				
				try {
					val connection = URL(url).openConnection()
					connection.connectTimeout = 5000
					connection.readTimeout = 5000
					val mod = connection.getInputStream().readBytes()
					
					err = "Unable to save Alfheim Modular"
					
					val modular = File(v_modsDir, fullname).also { if (!it.exists()) it.createNewFile() }
					val fos = FileOutputStream(modular)
					fos.write(mod)
					fos.close()
					
					FMLRelaunchLog.info("[${ModInfo.MODID.toUpperCase()}] Successfully updated Alfheim Modular")
				} catch (e: Throwable) {
					FMLRelaunchLog.log(Level.ERROR, e, "[${ModInfo.MODID.toUpperCase()}] $err")
					e.printStackTrace(System.err)
				}
			}
			
			FMLRelaunchLog.info("[${ModInfo.MODID.toUpperCase()}] Congrats! Alfheim Modular is up to date")
		} catch (e: Throwable) {
			FMLRelaunchLog.log(Level.ERROR, e, "[${ModInfo.MODID.toUpperCase()}] Unable to perform Alfheim Modular update. Running with no/possibly outdated one")
		}
	}
}