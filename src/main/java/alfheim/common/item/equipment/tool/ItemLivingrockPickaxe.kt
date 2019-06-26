package alfheim.common.item.equipment.tool

import alfheim.AlfheimCore
import alfheim.api.ModInfo
import net.minecraft.item.ItemPickaxe

class ItemLivingrockPickaxe: ItemPickaxe(Item.ToolMaterial.STONE) {
	init {
		this.creativeTab = AlfheimCore.alfheimTab
		this.setTextureName(ModInfo.MODID + ":LivingrockPickaxe")
		this.unlocalizedName = "LivingrockPickaxe"
	}
}