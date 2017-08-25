package alfheim.common.registry;

import alfheim.common.blocks.tileentity.ManaInfuserTileEntity;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class AlfheimRegistry {
	
	public static ArmorMaterial ELVORIUM = EnumHelper.addArmorMaterial("ELVORIUM", 50, new int[] {4, 9, 7, 4}, 50);
	
	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(ManaInfuserTileEntity.class, "ManaInfuser");
	}
}
