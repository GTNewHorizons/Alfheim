package alfheim.client.integration.nei

import alexsocol.asjlib.ASJReflectionHelper
import alfheim.common.core.handler.AlfheimConfigHandler
import codechicken.nei.ItemMobSpawner
import codechicken.nei.api.ItemInfo
import gloomyfolken.hooklib.asm.Hook
import net.minecraft.entity.EntityList
import net.minecraft.entity.boss.EntityWither
import net.minecraft.world.World

object NEINoBossHook {
	
	@JvmStatic
	@Hook(injectOnExit = true)
	fun load(spawner: ItemInfo?, world: World) {
		if (AlfheimConfigHandler.blacklistWither) // oh just shut the fuck up, I'm tired of making that shit public every time gradle decides to redownload the whole mod
			ASJReflectionHelper.getStaticValue<ItemMobSpawner, MutableMap<Int, String>>(ItemMobSpawner::class.java, "IDtoNameMap").remove(EntityList.classToIDMapping[EntityWither::class.java])
	}
}