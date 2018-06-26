package alfheim.common.item.rod;

import java.awt.Color;

import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.common.core.registry.AlfheimItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;

public class ItemRodElemental extends Item implements IManaUsingItem {
	
	private Block barrier;
	private IIcon rubyIcon;
	private IIcon sapphireIcon;
	
	public ItemRodElemental(String name, Block barrier) {
		this.barrier = barrier;
		setCreativeTab(AlfheimCore.alfheimTab);
		setFull3D();
		setMaxDamage(100);
		setMaxStackSize(1);
		setTextureName(ModInfo.MODID + ':' + name);
		setUnlocalizedName(name);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
		itemIcon = reg.registerIcon(ModInfo.MODID + ':' + this.getUnlocalizedName().substring(5));
		rubyIcon = reg.registerIcon(ModInfo.MODID + ":RubyRod");
		sapphireIcon = reg.registerIcon(ModInfo.MODID + ":SapphireRod");
	}
	
	@Override
	public IIcon getIconIndex(ItemStack par1ItemStack) {
		String name = par1ItemStack.getDisplayName().toLowerCase().trim();
		return name.equals("magical ruby") && this == AlfheimItems.rodFire ? rubyIcon : name.equals("magical sapphire") && this == AlfheimItems.rodIce ? sapphireIcon : super.getIconIndex(par1ItemStack);
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return getIconIndex(stack);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (stack.getItemDamage() > 0) return stack;
		if (!world.isRemote) {
			for (int x = -6; x < 7; x++)
				for (int z = -6; z < 7; z++)
					for (int y = -2; y < 3; y++)
						if (3 < Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2)) && Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2)) < 6) {
							int X = MathHelper.floor_double(player.posX) + x;
							int Y = MathHelper.floor_double(player.posY) + y;
							int Z = MathHelper.floor_double(player.posZ) + z;
							Color c = new Color(this == AlfheimItems.rodFire ? 0x880000 : 0x0055AA);
							if (world.isAirBlock(X, Y, Z) && barrier.canPlaceBlockAt(world, X, Y, Z)) place(stack, player, world, X, Y, Z, 0, 0.5F, 0.5F, 0.5F, barrier, player.capabilities.isCreativeMode ? 0 : 150, c.getRed(), c.getGreen(), c.getBlue());
						}
			stack.setItemDamage(this.getMaxDamage());
		}
		return stack;
	}
	
	// Modified code from ItemDirtRod
	public static boolean place(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10, Block block, int cost, float r, float g, float b) {
		if(ManaItemHandler.requestManaExactForTool(par1ItemStack, par2EntityPlayer, cost, false)) {
			ForgeDirection dir = ForgeDirection.getOrientation(par7);

			ItemStack stackToPlace = new ItemStack(block);
			stackToPlace.tryPlaceItemIntoWorld(par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10);

			if(stackToPlace.stackSize == 0) {
				ManaItemHandler.requestManaExactForTool(par1ItemStack, par2EntityPlayer, cost, true);
				for(int i = 0; i < 6; i++) Botania.proxy.sparkleFX(par3World, par4 + dir.offsetX + Math.random(), par5 + dir.offsetY + Math.random(), par6 + dir.offsetZ + Math.random(), r, g, b, 1F, 5);
			}
		}

		return true;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slotID, boolean inHand) {
		if (stack.getItemDamage() > 0) stack.setItemDamage(stack.getItemDamage() - 1);
	}
	
	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}
}
