package alfheim.client.integration.nei

import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.entity.boss.*
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
		if (AlfheimConfigHandler.blacklistWither)
			ItemMobSpawner.IDtoNameMap.remove(EntityList.classToIDMapping[EntityWither::class.java])
	}
}