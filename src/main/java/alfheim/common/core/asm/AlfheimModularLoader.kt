package alfheim.common.core.asm

import alfheim.api.ModInfo
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.InfoLoader
import com.google.gson.*
import cpw.mods.fml.relauncher.*
import net.minecraftforge.common.MinecraftForge
import org.apache.logging.log4j.Level
import java.io.*
import java.net.URL
import java.nio.file.*
import java.util.zip.*
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.system.exitProcess

object AlfheimModularLoader {
	
	init {
		FMLRelaunchLog.info("[${ModInfo.MODID.toUpperCase()}] Trying to update Alfheim Modular...")
		
		if (AlfheimConfigHandler.modularThread)
			Thread(Runnable { download() }, "Alfheim Modular Download").also { it.isDaemon = true }.start()
		else
			download()
	}
	
	fun download() {
		var crash = false
		
		try {
			val root = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(URL("https://bitbucket.org/AlexSocol/alfheim/raw/" + (if (ModInfo.DEV) "development" else "master") + "/news/" + MinecraftForge.MC_VERSION + ".xml").openStream()).documentElement
			val url = InfoLoader.getNodeValue(root, "MODULAR")
			
			if (url.isEmpty()) {
				FMLRelaunchLog.info("[${ModInfo.MODID.toUpperCase()}] No Alfheim Modular link specified, canceling update")
				return
			}
			
			var possibleMatch = false
			var download = true
			
			val mcVer = FMLInjectionData.data()[4] as String
			val mcDir = FMLInjectionData.data()[6] as File
			val subModsDir = File(mcDir, "mods/$mcVer")
			if (!subModsDir.exists()) {
				subModsDir.mkdirs()
			} else {
				possibleMatch = true
			}
			
			val fullname = url.substring(url.lastIndexOf('/') + 1)
			
			if (possibleMatch) {
				subModsDir.listFiles()?.forEach { mod ->
					ZipFile(mod).use { zip ->
						val modInfo = zip.getEntry("mcmod.info") ?: return@use
						
						val info = loadJSon(zip.getInputStream(modInfo))
						if (!info.first) return@use
						val versionLocal = info.second
						
						val versionRemote = fullname.substring(fullname.lastIndexOf('-') + 1).let { it.substring(0, it.lastIndexOf('.')) }
						
						if (versionRemote != versionLocal) {
							try {
								if (!mod.delete()) Files.delete(mod.toPath())
							} catch (e: Throwable) {
								FMLRelaunchLog.log(Level.ERROR, e, "[${ModInfo.MODID.toUpperCase()}] Unable to delete previous Alfheim Modular version. JVM now will try to delete it on exit. If it fails, please, delete it manually")
								mod.deleteOnExit()
								
								crash = true
							}
							
							return@forEach
						} else {
							download = false
							return@forEach
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
					
					val modular = File(subModsDir, fullname).also { if (!it.exists()) it.createNewFile() }
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
		
		if (crash) {
			FMLRelaunchLog.info("[${ModInfo.MODID.toUpperCase()}] Exiting JVM due to mod duplication")
			exitProcess(1)
		}
 	}
	
	fun loadJSon(input: InputStream): Pair<Boolean, String> {
		InputStreamReader(input).use { reader ->
			val root = JsonParser().parse(reader)
			return if (root.isJsonArray) loadJSonArr(root) else loadJson(root.asJsonObject)
		}
	}
	
	fun loadJSonArr(root: JsonElement): Pair<Boolean, String> {
		root.asJsonArray.forEach {
			val version = loadJson(it.asJsonObject)
			if (version.first) return version
		}
		
		return false to ""
	}
	
	fun loadJson(node: JsonObject): Pair<Boolean, String> {
		val modid = node.get("modid") ?: return false to ""
		
		if (modid.asString == "alfmod")
			return true to node.get("version").asString
		
		return false to ""
	}
}