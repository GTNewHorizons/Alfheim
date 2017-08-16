package alfheim;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import alfheim.core.register.blocks.AlfheimBlocks;
import alfheim.core.register.items.AlfheimItems;
import alfheim.core.utils.AlfheimCreativeTabs;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;


@Mod(
		   modid = AlfheimCore.MODID,
		   version = AlfheimCore.VERSION,
		   dependencies = "required-after:Botania;",
		   name = AlfheimCore.MODID
		)
public class AlfheimCore {

	public static final int major_version = 0;
	public static final int minor_version = 0;
	public static final int build_version = 1;
	
    public static final String MODID = "Alfheim";
    public static final String VERSION = major_version + "." + minor_version + "." + build_version;
    public static final String ASSET_PREFIX = "alfheim";
    
    public static AlfheimCreativeTabs BlocksCreativeTabs;
    public static AlfheimCreativeTabs ItemsCreativeTabs;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) 
    {
    	
    	AlfheimBlocks.initialize();
    	AlfheimItems.initialize();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    { 
    	BlocksCreativeTabs = new AlfheimCreativeTabs("AlfheimBlocks");
    	BlocksCreativeTabs.setIconItemIndex(Item.getItemFromBlock(ModBlocks.alfPortal));
    	
    	ItemsCreativeTabs = new AlfheimCreativeTabs("AlfheimItems");
    	ItemsCreativeTabs.setIconItemIndex(ModItems.starSword);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	
    }
}
