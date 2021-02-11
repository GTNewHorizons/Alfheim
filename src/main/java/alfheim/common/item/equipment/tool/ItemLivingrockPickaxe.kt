package alfheim.common.item.equipment.tool

import alfheim.api.ModInfo
import alfheim.common.core.util.AlfheimTab
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.item.*

class ItemLivingrockPickaxe: ItemPickaxe(ToolMaterial.STONE) {
	
	init {
		creativeTab = AlfheimTab
		setTextureName(ModInfo.MODID + ":LivingrockPickaxe")
		unlocalizedName = "LivingrockPickaxe"
	}
	
	override fun setUnlocalizedName(name: String): Item {
		GameRegistry.registerItem(this, name)
		return super.setUnlocalizedName(name)
	}
}