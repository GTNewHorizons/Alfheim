package alfheim.common.items;

import java.util.List;

import alfheim.AlfheimCore;
import alfheim.ModInfo;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ElvenResource extends Item {
	
	public static final String[] subItems = new String[] { "ManaInfusionCore", "ElvoriumIngot", "MauftriumIngot", "MuspelheimPowerIngot", "NiflheimPowerIngot", "ElvoriumNugget", "MauftriumNugget", "MuspelheimEssence", "NiflheimEssence", "IffesalDust", "PrimalRune", "MuspelheimRune", "NiflheimRune", "InfusedDreamwoodTwig" };
	private IIcon[] texture = new IIcon[subItems.length];
	
	public ElvenResource() {
		this.setCreativeTab(AlfheimCore.alfheimTab);
		this.setHasSubtypes(true);
		this.setUnlocalizedName("ElvenItems");
	}
	
	public void registerIcons(IIconRegister iconRegister){
		for (int i = 0; i < subItems.length; i++){
			texture[i] = iconRegister.registerIcon(ModInfo.MODID + ":materials/" + subItems[i]);
		}
	}

    public IIcon getIconFromDamage(int i) {
    	if (i < texture.length) {
        	return texture[i];
    	} else {
    		return texture[0];
    	}
    }

    public String getUnlocalizedName(ItemStack stack) {
    	if (stack.getItemDamage() < subItems.length) {
        	return "item." + subItems[stack.getItemDamage()];
    	} else {
    		return subItems[0];
    	}
    }

    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < subItems.length; ++i) {
            list.add(new ItemStack(item, 1, i));
        }
    }
}
