package alfheim.common.items;

import alfheim.AlfheimCore;
import alfheim.Constants;
import net.minecraft.item.ItemPickaxe;

public class LivingrockPickaxe extends ItemPickaxe {

	public LivingrockPickaxe() {
		super(ToolMaterial.STONE);
		this.setCreativeTab(AlfheimCore.alfheimTab);
		this.setTextureName(Constants.MODID + ":LivingrockPickaxe");
		this.setUnlocalizedName("LivingrockPickaxe");
	}
}