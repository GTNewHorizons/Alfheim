package alfheim.common.registry;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.common.blocks.tileentity.ManaInfuserTileEntity;
import alfheim.common.dimension.world.gen.WorldGenAlfheim;
import alfheim.common.entity.ElfEntity;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class AlfheimRegistry {
	
	public static final ArmorMaterial ELVORIUM = EnumHelper.addArmorMaterial("ELVORIUM", 50, new int[] {4, 9, 7, 4}, 30);
	public static final ArmorMaterial ELEMENTAL = EnumHelper.addArmorMaterial("ELEMENTAL", 20, new int[] {2, 7, 5, 2}, 20);
	public static final ToolMaterial REALITY = EnumHelper.addToolMaterial("REALITY", 10, 9000, 3, 8, 30);
	
	public static final IWorldGenerator worldGen = new WorldGenAlfheim();
	
	public static void preInit() {
		registerEntities();
		registerTileEntities();
	}
	
	public static void init() {
		GameRegistry.registerWorldGenerator(worldGen, 1);
	}
	
	public static void registerEntities() {
		ASJUtilities.registerEntityEgg(ElfEntity.class, "Elf", 0x1A660A, 0x4D3422, AlfheimCore.instance);
	}
	
	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(ManaInfuserTileEntity.class, "ManaInfuser");
	}
}
