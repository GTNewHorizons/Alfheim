package alfheim.common.registry;

import alfheim.common.blocks.tileentity.ManaInfuserTileEntity;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class AlfheimRegistry {
	
	public static final ArmorMaterial ELVORIUM = EnumHelper.addArmorMaterial("ELVORIUM", 50, new int[] {4, 9, 7, 4}, 30);
	public static final ArmorMaterial ELEMENTAL = EnumHelper.addArmorMaterial("ELEMENTAL", 20, new int[] {2, 7, 5, 2}, 20);
	public static final ToolMaterial REALITY = EnumHelper.addToolMaterial("REALITY", 10, 9000, 3, 8, 30);
	
	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(ManaInfuserTileEntity.class, "ManaInfuser");
	}
}
