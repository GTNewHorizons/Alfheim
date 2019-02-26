package alfheim.common.item;

import java.util.ArrayList;
import java.util.List;

import alexsocol.asjlib.math.Vector3;
import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.client.gui.ItemsRemainingRenderHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.item.IBlockProvider;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.rod.ItemExchangeRod;

public class ItemAstrolabe extends Item {
	
	private static final String TAG_BLOCK_NAME = "blockName";
	private static final String TAG_BLOCK_META = "blockMeta";
	private static final String TAG_SIZE = "size";
	
	public ItemAstrolabe() {
		setCreativeTab(AlfheimCore.alfheimTab);
		setMaxStackSize(1);
		setTextureName(ModInfo.MODID + ":Astrolabe");
		setUnlocalizedName("Astrolabe");
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		Block block = world.getBlock(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
	
		if(player.isSneaking()) {
			if(setBlock(stack, block, meta)) {
				displayRemainderCounter(player, stack);
				return true;
			}
		} else {
			boolean did = placeAllBlocks(stack, player);
			if(did) {
				displayRemainderCounter(player, stack);
				if(!world.isRemote)
					player.swingItem();
			}
			
			return did;
		}
		
		return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(player.isSneaking()) {
			int size = getSize(stack);
			int newSize = size == 11 ? 3 : size + 2;
			setSize(stack, newSize);
			ItemsRemainingRenderHandler.set(stack, newSize + "x" + newSize);
			
			world.playSoundAtEntity(player, "random.orb", 0.1F, 0.5F * ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.8F));
		}
		
		return stack;
	}
	
	public boolean placeAllBlocks(ItemStack stack, EntityPlayer player) {
		List<Vector3> blocksToPlace = getBlocksToPlace(stack, player);
		if(!hasBlocks(stack, player, blocksToPlace))
			return false;
		
		int size = getSize(stack);
		int cost = size * 320;
		if(!ManaItemHandler.requestManaExact(stack, player, cost, false))
			return false;
		
		ItemStack stackToPlace = new ItemStack(getBlock(stack), 1, getBlockMeta(stack));
		for(Vector3 v : blocksToPlace) placeBlockAndConsume(player, stack, stackToPlace, (int) v.x, (int) v.y, (int) v.z);
		ManaItemHandler.requestManaExact(stack, player, cost, true);
		
		return true;
	}
	
	private void placeBlockAndConsume(EntityPlayer player, ItemStack requestor, ItemStack blockToPlace, int x, int y, int z) {
		if(blockToPlace.getItem() == null)
			return;
		
		Block block = Block.getBlockFromItem(blockToPlace.getItem());
		int meta = blockToPlace.getItemDamage();
		player.worldObj.setBlock(x, y, z, block, meta, 3);
		player.worldObj.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block));
		
		if(player.capabilities.isCreativeMode) return;
		
		List<ItemStack> stacksToCheck = new ArrayList<ItemStack>();
		for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stackInSlot = player.inventory.getStackInSlot(i);
			if(stackInSlot != null && stackInSlot.stackSize > 0 && stackInSlot.getItem() == blockToPlace.getItem() && stackInSlot.getItemDamage() == blockToPlace.getItemDamage()) {
				stackInSlot.stackSize--;
				return;
			}
			
			if (stackInSlot != null && stackInSlot.stackSize > 0 && stackInSlot.getItem() instanceof IBlockProvider)
				stacksToCheck.add(stackInSlot);
		}
		
		for(ItemStack providerStack : stacksToCheck) {
			IBlockProvider prov = (IBlockProvider) providerStack.getItem();
			
			if(prov.provideBlock(player, requestor, providerStack, block, meta, false)) {
				prov.provideBlock(player, requestor, providerStack, block, meta, true);
				return;
			}
		}
	}
	
	public static boolean hasBlocks(ItemStack stack, EntityPlayer player, List<Vector3> blocks) {
		if (player.capabilities.isCreativeMode)
			return true;
		
		Block block = getBlock(stack);
		int meta = getBlockMeta(stack);
		ItemStack reqStack = new ItemStack(block, 1, meta);
		
		int required = blocks.size();
		int current = 0;
		List<ItemStack> stacksToCheck = new ArrayList<ItemStack>();
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stackInSlot = player.inventory.getStackInSlot(i);
			if (stackInSlot != null && stackInSlot.stackSize > 0  && stackInSlot.getItem() == reqStack.getItem() && stackInSlot.getItemDamage() == reqStack.getItemDamage()) {
				current += stackInSlot.stackSize;
				if (current >= required)
					return true;
			}
			if(stackInSlot != null && stackInSlot.stackSize > 0  && stackInSlot.getItem() instanceof IBlockProvider)
				stacksToCheck.add(stackInSlot);
		}
		
		for(ItemStack providerStack : stacksToCheck) {
			IBlockProvider prov = (IBlockProvider) providerStack.getItem();
			int count = prov.getBlockCount(player, stack, providerStack, block, meta);
			if(count == -1)
				return true;
			
			current += count; 
			
			if(current >= required)
				return true;
		}
		
		return false;
	}
	
	public static List<Vector3> getBlocksToPlace(ItemStack stack, EntityPlayer player) {
		List<Vector3> coords = new ArrayList<Vector3>();
		MovingObjectPosition mop = ToolCommons.raytraceFromEntity(player.worldObj, player, true, 5);
		if(mop != null) {
			Vector3 p = new Vector3(mop.blockX, mop.blockY, mop.blockZ);
			Block block = player.worldObj.getBlock((int) p.x, (int) p.y, (int) p.z);
			if(block.isReplaceable(player.worldObj, (int) p.x, (int) p.y, (int) p.z)) p.sub(0, 1, 0);
			
			int range = (getSize(stack) ^ 1) / 2;
			
			ForgeDirection dir = ForgeDirection.getOrientation(mop.sideHit);
			int rot = (int) Math.floor(player.rotationYaw / 90.0D + 0.5D) & 3;
			EnumFacing rotationDir = rot == 0 ? EnumFacing.SOUTH : rot == 1 ? EnumFacing.WEST : rot == 2 ? EnumFacing.NORTH : EnumFacing.EAST;
			
			boolean pitchedVertically = player.rotationPitch > 60 || player.rotationPitch < -60;
			
			boolean axisX = rotationDir == EnumFacing.WEST || rotationDir == EnumFacing.EAST;
			boolean axisZ = rotationDir == EnumFacing.NORTH|| rotationDir == EnumFacing.SOUTH;
			
			int xOff = axisZ || pitchedVertically ? range : 0;
			int yOff = pitchedVertically ? 0 : range;
			int zOff = axisX || pitchedVertically ? range : 0;
			
			for(int x = -xOff; x < xOff + 1; x++) {
				for(int y = 0; y < yOff * 2 + 1; y++) {
					for(int z = -zOff; z < zOff + 1; z++) {
						int xp = (int) (p.x + x + dir.offsetX);
						int yp = (int) (p.y + y + dir.offsetY);
						int zp = (int) (p.z + z + dir.offsetZ);
						
						Vector3 newPos = new Vector3(xp, yp, zp);
						Block block1 = player.worldObj.getBlock(xp, yp, zp);
						if(player.worldObj.isAirBlock(xp, yp, zp) || block1.isReplaceable(player.worldObj, xp, yp, zp)) coords.add(newPos);
					}
				}
			}
		}
		
		return coords;
	}
	
	
	public void displayRemainderCounter(EntityPlayer player, ItemStack stack) {
		Block block = getBlock(stack);
		int meta = getBlockMeta(stack);
		int count = ItemExchangeRod.getInventoryItemCount(player, stack, block, meta);
		if(!player.worldObj.isRemote) ItemsRemainingRenderHandler.set(new ItemStack(block, 1, meta), count);
	}
	
	private boolean setBlock(ItemStack stack, Block block, int meta) {
		if(block != Blocks.air) {
			ItemNBTHelper.setString(stack, TAG_BLOCK_NAME, Block.blockRegistry.getNameForObject(block).toString());
			ItemNBTHelper.setInt(stack, TAG_BLOCK_META, meta);
			return true;
		}
		return false;
	}	
	
	private static void setSize(ItemStack stack, int size) {
		ItemNBTHelper.setInt(stack, TAG_SIZE, size | 1);
	}
	
	public static int getSize(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_SIZE, 3) | 1;
	}
	
	public static Block getBlock(ItemStack stack) {
		Block block = Block.getBlockFromName(getBlockName(stack));
		if(block == null) return Blocks.air;
		
		return block;
	}
	
	public static String getBlockName(ItemStack stack) {
		return ItemNBTHelper.getString(stack, TAG_BLOCK_NAME, "");
	}
	
	public static int getBlockMeta(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_BLOCK_META, 0);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer player, List par3List, boolean flags) {
		Block block = getBlock(par1ItemStack);
		int size = getSize(par1ItemStack);
		
		par3List.add(size + " x " + size);
		if (block != null && block != Blocks.air) par3List.add(new ItemStack(block, 1, getBlockMeta(par1ItemStack)).getDisplayName());
	}
}