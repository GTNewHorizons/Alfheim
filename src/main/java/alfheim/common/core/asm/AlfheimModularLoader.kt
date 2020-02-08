package alfheim.common.core.asm

import alfheim.api.ModInfo
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.InfoLoader
import com.google.gson.*
import cpw.mods.fml.relauncher.*
import net.minecraftforge.common.MinecraftForge
import org.apache.logging.log4j.Level
import sun.misc.URLClassPath
import sun.net.util.URLUtil
import java.io.*
import java.net.*
import java.nio.file.Files
import java.util.zip.ZipFile
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.system.exitProcess

object AlfheimModularLoader {
	
	var linkSpecified = false
	
	init {
		download()
	}
	
	private fun download() {
		if (!ModInfo.OBF) return
		
		FMLRelaunchLog.info("[${ModInfo.MODID.toUpperCase()}] Trying to update Alfheim Modular...")
		
		if (AlfheimConfigHandler.modularThread)
			Thread(Runnable { doDownload() }, "Alfheim Modular Download").also { it.isDaemon = true }.start()
		else
			doDownload()
	}
	
	private fun doDownload() {
		var crash = false
		
		try {
			val root = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(URL("https://bitbucket.org/AlexSocol/alfheim/raw/" + (if (ModInfo.DEV) "development" else "master") + "/news/" + MinecraftForge.MC_VERSION + ".xml").openStream()).documentElement
			val url = InfoLoader.getNodeValue(root, "MODULAR")
			
			if (url.isEmpty()) {
				FMLRelaunchLog.info("[${ModInfo.MODID.toUpperCase()}] No Alfheim Modular link specified, canceling update")
				return
			}
			
			linkSpecified = true
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
			val versionRemote = fullname.substring(fullname.lastIndexOf('-') + 1).let { it.substring(0, it.lastIndexOf('.')) }
			
			if (possibleMatch) {
				subModsDir.listFiles()?.forEach { mod ->
					if (mod.extension == "jar")
						ZipFile(mod).use { zip ->
							val modInfo = zip.getEntry("mcmod.info") ?: return@use
							
							val info = loadJSon(zip.getInputStream(modInfo))
							
							if (!info.first) return@use
							val versionLocal = info.second
							
							if (versionRemote != versionLocal) {
								crash = deleteMod(mod)
								
								return@forEach
							}
							
							download = false
							
							return@forEach
						}
				}
			}
			
			if (download) {
				var err = "Unable to download Alfheim Modular from official repo"
				
				try {
					val connection = URL(url).openConnection()
					connection.connectTimeout = 5000
					connection.readTimeout = 5000
					val mod = connection.getInputStream().readBytes()
					
					err = "Unable to save Alfheim Modular"
					
					var fileName = if (AlfheimConfigHandler.modularFilename.isBlank()) fullname else AlfheimConfigHandler.modularFilename.format(versionRemote)
					if (!fileName.endsWith(".jar")) fileName += ".jar"
					val modular = File(subModsDir, fileName).also { if (!it.exists()) it.createNewFile() }
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
			FMLRelaunchLog.warning("[${ModInfo.MODID.toUpperCase()}] Exiting JVM due to mod duplication")
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
	
	fun deleteMod(mod: File): Boolean {
		val act = "delete previous Alfheim Modular version"
		if (!mod.delete()) {
			FMLRelaunchLog.log(Level.WARN, "[${ModInfo.MODID.toUpperCase()}] Could not $act, trying to free resources...")
			
			try {
				val classLoader = this::class.java.classLoader
				val url = mod.toURI().toURL()
				val ucpF = URLClassLoader::class.java.getDeclaredField("ucp")
				val loadersF = URLClassPath::class.java.getDeclaredField("loaders")
				val lmapF = URLClassPath::class.java.getDeclaredField("lmap")
				ucpF.isAccessible = true
				loadersF.isAccessible = true
				lmapF.isAccessible = true
				val ucp = ucpF[classLoader] as URLClassPath
				val loader = (lmapF[ucp] as MutableMap<*, *>).remove(URLUtil.urlNoFragString(url)) as Closeable?
				if (loader != null) {
					loader.close()
					(loadersF[ucp] as MutableList<*>).remove(loader)
				}
			} catch (e: Throwable) {
				FMLRelaunchLog.log(Level.ERROR, e, "[${ModInfo.MODID.toUpperCase()}] Error occured while trying to $act")
			}
			
			return try {
				if (!mod.delete())
					Files.delete(mod.toPath())
				
				false
			} catch (e: Throwable) {
				FMLRelaunchLog.log(Level.ERROR, e, "[${ModInfo.MODID.toUpperCase()}] Unable to $act. JVM now will try to delete it on exit. If it fails, please, delete it manually")
				mod.deleteOnExit()
				
				true
			}
		}
		
		return false
	}
}