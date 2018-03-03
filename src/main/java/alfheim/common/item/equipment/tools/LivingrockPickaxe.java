package alfheim.common.item.equipment.tools;

import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import net.minecraft.item.ItemPickaxe;

public class LivingrockPickaxe extends ItemPickaxe {

	public LivingrockPickaxe() {
		super(ToolMaterial.STONE);
		this.setCreativeTab(AlfheimCore.alfheimTab);
		this.setTextureName(ModInfo.MODID + ":LivingrockPickaxe");
		this.setUnlocalizedName("LivingrockPickaxe");
	}
}