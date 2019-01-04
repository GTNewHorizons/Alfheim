package alfheim.common.item.rod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.subtile.ISpecialFlower;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.rod.ItemDirtRod;
import vazkii.botania.common.item.rod.ItemTerraformRod;
import vazkii.botania.common.lib.LibMisc;

public class ItemRodGrass extends Item implements IManaUsingItem {

	static final List<String> validBlocks = Arrays.asList(new String[] { "dirt", "mycelium", "podzol" });
	
	public ItemRodGrass() {
		setCreativeTab(AlfheimCore.alfheimTab);
		setMaxStackSize(1);
		setTextureName(ModInfo.MODID + ":grassRod");
		setUnlocalizedName("grassRod");
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.bow;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
		if(count != getMaxItemUseDuration(stack)) terraform(stack, player.worldObj, player);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		player.setItemInUse(stack, getMaxItemUseDuration(stack));
		return stack;
	}
	
	public void terraform(ItemStack stack, World world, EntityPlayer player) {
		int range = IManaProficiencyArmor.Helper.hasProficiency(player) ? 22 : 16;

		int x = MathHelper.floor_double(player.posX);
		int y = MathHelper.floor_double(player.posY - (world.isRemote ? 2 : 1));
		int z = MathHelper.floor_double(player.posZ);

		for (int i = -range; i <= range; i++) {
			for (int k = -range; k <= range; k++) {
				for (int j = -range; j <= range; j++) {
					if (!world.isAirBlock(x + i, y + j + 1, z + k)) continue;
					for(int id : OreDictionary.getOreIDs(new ItemStack(world.getBlock(x + i, y + j, z + k), 1, world.getBlockMetadata(x + i, y + j, z + k))))
						if(validBlocks.contains(OreDictionary.getOreName(id))) 
							if (place(stack, player, world, x + i, y + j, z + k, 1, 0.5F, 1, 0.5F, Blocks.grass, world.canBlockSeeTheSky(x + i, y + j, z + k) ? 30 : 50, 0, 1, 0)) return;
				}
			}
		}
	}
	
	public static boolean place(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, Block block, int cost, float r, float g, float b) {
		if(!ManaItemHandler.requestManaExactForTool(stack, player, cost, false)) return false;
		world.setBlock(x, y, z, block);
		ManaItemHandler.requestManaExactForTool(stack, player, cost, true);
		for(int i = 0; i < 6; i++) Botania.proxy.sparkleFX(world, x + Math.random(), y + Math.random(), z + Math.random(), r, g, b, 1F, 5);
		
		return true;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}
}