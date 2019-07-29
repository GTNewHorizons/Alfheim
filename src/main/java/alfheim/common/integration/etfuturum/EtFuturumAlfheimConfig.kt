package alfheim.common.integration.etfuturum

import alfheim.api.ModInfo
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.EnumHelper
import alfheim.common.item.ShadowFoxItems

object EtFuturumAlfheimConfig {
	
	fun loadConfig() {
		EFHandlerBanners.addBanners()
	}
}

@Suppress("UNCHECKED_CAST")
object EFHandlerBanners {
	val coatNames = arrayOf("chile", "france", "japan", "germany", "greece", "iceland", "ireland", "israel", "jamaica", "singapore", "southafrica", "spain", "switzerland", "texas", "ukraine", "unitedstates", "alfheim")
	
	fun addBanners() {
		try {
			val clazz = Class.forName("ganymedes01.etfuturum.tileentities.TileEntityBanner\$EnumBannerPattern") as Class<Enum<*>>
			for (i in coatNames.indices) {
				try {
					addPattern(clazz, coatNames[i], "c${if (i < 10) "0$i" else "$i"}", ItemStack(ShadowFoxItems.coatOfArms, 1, i))
				} catch (e: Exception) {}
			}
		} catch (e: Exception) {}
	}
	
	fun addPattern(clazz: Class<Enum<*>>, name: String, id: String, craftingItem: ItemStack) {
		val changedname = "${ModInfo.MODID}_$name"
		val changedid = "${ModInfo.MODID.substring(0, 3)}_$id"
		EnumHelper.addEnum(clazz, changedname.toUpperCase(), arrayOf(String::class.java, String::class.java, ItemStack::class.java), arrayOf(changedname, changedid, craftingItem))
	}
}