package alfheim.client.lib

import alfheim.api.ModInfo
import alfheim.api.lib.LibResourceLocations
import alfheim.common.item.equipment.bauble.ItemPriestEmblem
import net.minecraft.util.ResourceLocation
import vazkii.botania.client.lib.LibResources

object LibResourceLocationsActual {
	
	fun init() {
		LibResourceLocations.babylon = ResourceLocation(LibResources.MISC_BABYLON)
		LibResourceLocations.elvenPylon = ResourceLocation(LibResources.MODEL_PYLON_PINK)
		LibResourceLocations.elvenPylonOld = ResourceLocation(LibResources.MODEL_PYLON_PINK_OLD)
		LibResourceLocations.glowCyan = ResourceLocation(LibResources.MISC_GLOW_CYAN)
		LibResourceLocations.halo = ResourceLocation(LibResources.MISC_HALO)
		LibResourceLocations.lexica = ResourceLocation(LibResources.MODEL_LEXICA)
		LibResourceLocations.manaInfuserOverlay = ResourceLocation(LibResources.GUI_MANA_INFUSION_OVERLAY)
		LibResourceLocations.petalOverlay = ResourceLocation(LibResources.GUI_PETAL_OVERLAY)
		LibResourceLocations.pixie = ResourceLocation(LibResources.MODEL_PIXIE)
		LibResourceLocations.spreader = ResourceLocation(LibResources.MODEL_SPREADER)
		
		LibResourceLocations.godCloak = Array(ItemPriestEmblem.TYPES) {
			ResourceLocation(ModInfo.MODID, "textures/model/armor/cloak/God$it.png")
		}
		
		LibResourceLocations.godCloakGlow = Array(ItemPriestEmblem.TYPES) {
			ResourceLocation(ModInfo.MODID, "textures/model/armor/cloak/God${it}_glow.png")
		}
	}
}