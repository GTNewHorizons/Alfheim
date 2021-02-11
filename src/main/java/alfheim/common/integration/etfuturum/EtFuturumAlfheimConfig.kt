package alfheim.common.integration.etfuturum

import alexsocol.asjlib.ASJUtilities
import alfheim.api.ModInfo
import alfheim.common.item.AlfheimItems.coatOfArms
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.EnumHelper

object EtFuturumAlfheimConfig {
	
	fun loadConfig() {
		EFHandlerBanners.addBanners()
	}
}

@Suppress("UNCHECKED_CAST")
object EFHandlerBanners {
	
	val coatNames = arrayOf("chile", "france", "japan", "germany", "greece", "iceland", "ireland", "israel", "jamaica", "singapore", "southafrica", "spain", "switzerland", "texas", "ukraine", "unitedstates", "alfheim", "ussr")
	
	fun addBanners() {
		try {
			val clazz = Class.forName("ganymedes01.etfuturum.tileentities.TileEntityBanner\$EnumBannerPattern") as Class<Enum<*>>
			for (i in coatNames.indices) {
				try {
					addPattern(clazz, coatNames[i], "c${if (i < 10) "0$i" else "$i"}", ItemStack(coatOfArms, 1, i))
				} catch (e: Exception) {
					ASJUtilities.error("Error trying to add ${coatNames[i]} banner: ${e.message}")
					e.printStackTrace()
				}
			}
		} catch (e: Exception) {
			ASJUtilities.error("Error trying to add Et Futurum banners: ${e.message}")
			e.printStackTrace()
		}
	}
	
	fun addPattern(clazz: Class<Enum<*>>, name: String, id: String, craftingItem: ItemStack) {
		val changedname = "${ModInfo.MODID}_$name"
		val changedid = "${ModInfo.MODID.substring(0, 3)}_$id"
		EnumHelper.addEnum(clazz, changedname.toUpperCase(), arrayOf(String::class.java, String::class.java, ItemStack::class.java), arrayOf(changedname, changedid, craftingItem))
	}
}