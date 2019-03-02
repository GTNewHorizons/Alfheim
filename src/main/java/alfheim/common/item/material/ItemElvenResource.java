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
import vazkii.botania.common.Botania;

public class ItemElvenResource extends Item /*implements IElvenItem, ILensEffect BACK*/ {
	
	public static final String[] subItems = new String[] { "InterdimensionalGatewayCore", "ManaInfusionCore", "ElvoriumIngot", "MauftriumIngot", "MuspelheimPowerIngot", "NiflheimPowerIngot", "ElvoriumNugget", "MauftriumNugget", "MuspelheimEssence", "NiflheimEssence", "IffesalDust", "PrimalRune", "MuspelheimRune", "NiflheimRune", "InfusedDreamwoodTwig"/*, "Transferer" BACK*/ };
	private IIcon[] texture = new IIcon[subItems.length];
	
	public static IIcon harp = null;
	public static IIcon mine = null;
	public static IIcon wind = null;
	public static IIcon wing = null;
	
	public ItemElvenResource() {
		this.setCreativeTab(AlfheimCore.alfheimTab);
		this.setHasSubtypes(true);
		this.setUnlocalizedName("ElvenItems");
	}
	
	public void registerIcons(IIconRegister reg) {
		for (int i = 0; i < subItems.length; i++)
			texture[i] = reg.registerIcon(ModInfo.MODID + ":materials/" + subItems[i]);
		
		harp = reg.registerIcon(ModInfo.MODID + ":misc/harp");
		mine = reg.registerIcon(ModInfo.MODID + ":misc/mine");
		wind = reg.registerIcon(ModInfo.MODID + ":misc/wind");
		wing = reg.registerIcon(ModInfo.MODID + ":misc/wing");
	}
	
	public IIcon getIconFromDamage(int meta) {
		return texture[Math.max(0, Math.min(meta, texture.length-1))];
	}
	
	public String getUnlocalizedName(ItemStack stack) {
		return "item." + subItems[Math.max(0, Math.min(stack.getItemDamage(), subItems.length-1))];
	}
	
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < subItems.length; ++i) {
			if (i == ElvenResourcesMetas.InfusedDreamwoodTwig && !Botania.thaumcraftLoaded) continue;
				list.add(new ItemStack(item, 1, i));
		}
	}
	/*
	@Override
	public boolean isElvenItem(ItemStack stack) {
		return stack.getItemDamage() == ElvenResourcesMetas.InterdimensionalGatewayCore;
	}
	
	@Override
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
	}BACK*/
}
