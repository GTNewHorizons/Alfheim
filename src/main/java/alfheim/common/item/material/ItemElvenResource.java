package alfheim.common.item.material;

import java.util.List;

import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.common.core.registry.AlfheimItems.ElvenResourcesMetas;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import vazkii.botania.api.recipe.IElvenItem;

public class ItemElvenResource extends Item implements IElvenItem/*, ILensEffect FIXME*/ {
	
	public static final String[] subItems = new String[] { "InterdimensionalGatewayCore", "ManaInfusionCore", "ElvoriumIngot", "MauftriumIngot", "MuspelheimPowerIngot", "NiflheimPowerIngot", "ElvoriumNugget", "MauftriumNugget", "MuspelheimEssence", "NiflheimEssence", "IffesalDust", "PrimalRune", "MuspelheimRune", "NiflheimRune" /*"Transferer"*/ /*"InfusedDreamwoodTwig"*/ };
	private IIcon[] texture = new IIcon[subItems.length];
	
	public ItemElvenResource() {
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
	
	@Override
	public boolean isElvenItem(ItemStack stack) {
		return stack.getItemDamage() == ElvenResourcesMetas.InterdimensionalGatewayCore;
	}

	/*@Override
	public void apply(ItemStack stack, BurstProperties props) {}

	@Override
	public boolean collideBurst(IManaBurst burst, MovingObjectPosition pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		if (stack.getItemDamage() != ElvenResourcesMetas.Transferer) return true;

		EntityManaBurst entity = (EntityManaBurst) burst;
		if (!entity.worldObj.isRemote && pos.typeOfHit == MovingObjectType.BLOCK && entity.worldObj.getBlock(pos.blockX, pos.blockY, pos.blockZ) == Blocks.sponge) {
			EntityItem item = new EntityItem(entity.worldObj, pos.blockX, pos.blockY, pos.blockZ, TileTransferer.getStack(entity).copy());
			entity.worldObj.spawnEntityInWorld(item);
			return true;
		}
		return false;
	}

	@Override
	public void updateBurst(IManaBurst burst, ItemStack stack) {
		if (stack.getItemDamage() != ElvenResourcesMetas.Transferer) ((EntityThrowable) burst).setDead();
	}

	@Override
	public boolean doParticles(IManaBurst burst, ItemStack stack) {
		return stack.getItemDamage() == ElvenResourcesMetas.Transferer;
	} FIXME*/
}
