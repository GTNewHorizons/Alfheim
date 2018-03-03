package alfheim.common.core.asm;

import java.util.ArrayList;
import java.util.List;

import alfheim.api.event.NetherPortalActivationEvent;
import alfheim.common.block.tile.TileTransferer;
import alfheim.common.core.utils.AlfheimConfig;
import gloomyfolken.hooklib.asm.Hook;
import gloomyfolken.hooklib.asm.ReturnCondition;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.recipe.RecipePureDaisy;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.BlockPylon;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TilePylon;
import vazkii.botania.common.entity.EntityManaBurst;

public class AlfheimHookHandler {

	private static boolean updatingEntity = false;
	private static final String TAG_TRANSFER_STACK = "transferStack";
	
	@Hook
	public static void updateEntity(TilePylon entity) {
		if (entity.getWorldObj().isRemote) updatingEntity = true;
	}

	@Hook(injectOnExit = true, targetMethod = "updateEntity")
	public static void updateEntityPost(TilePylon entity) {
		if (entity.getWorldObj().isRemote) {
			updatingEntity = false;
			if (entity.getWorldObj().rand.nextBoolean()) {
				int meta = entity.getBlockMetadata();
				Botania.proxy.sparkleFX(entity.getWorldObj(), entity.xCoord + Math.random(), entity.yCoord + Math.random() * 1.5, entity.zCoord + Math.random(), meta == 2 ? 0F : 0.5F, meta == 0 ? 0.5F : 1F, meta == 1 ? 0.5F : 1F, (float) Math.random(), 2);
			}
		}
	}

	@Hook(returnCondition = ReturnCondition.ON_TRUE)
	public static boolean sparkleFX(ClientProxy proxy, World world, double x, double y, double z, float r, float g, float b, float size, int m, boolean fake) {
		return updatingEntity;
	}
	
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	public static IIcon getIcon(BlockPylon pylon, int side, int meta) {
		return meta == 0 || meta == 1 ? ModBlocks.storage.getIcon(side, meta) : Blocks.diamond_block.getIcon(side, 0);
	}
	
	@Hook(returnCondition = ReturnCondition.ON_TRUE, targetMethod = "func_150000_e", isMandatory = true)
	public static boolean onNetherPortalActivation(BlockPortal portal, World world, int x, int y, int z) {
		return MinecraftForge.EVENT_BUS.post(new NetherPortalActivationEvent(world, x, y, z));
	}
	
	@Hook(returnCondition = ReturnCondition.ON_TRUE, booleanReturnConstant = false, isMandatory = true)
	public static boolean matches(RecipePureDaisy recipe, World world, int x, int y, int z, SubTileEntity pureDaisy, Block block, int meta) {
		return recipe.getOutput().equals(ModBlocks.livingwood) && world.provider.dimensionId == AlfheimConfig.dimensionIDAlfheim;
	}
	
	/*@Hook(injectOnExit = true)
	public static void writeEntityToNBT(EntityManaBurst burst, NBTTagCompound par1nbtTagCompound) {
		ItemStack stack = TileTransferer.getTrackingStack(burst);
		NBTTagCompound transCmp = new NBTTagCompound();
		if(stack != null) stack.writeToNBT(transCmp);
		par1nbtTagCompound.setTag(TAG_TRANSFER_STACK, transCmp);
	}
	
	@Hook(injectOnExit = true)
	public static void readEntityFromNBT(EntityManaBurst burst, NBTTagCompound par1nbtTagCompound) {
		NBTTagCompound transCmp = par1nbtTagCompound.getCompoundTag(TAG_TRANSFER_STACK);
		ItemStack stack = ItemStack.loadItemStackFromNBT(transCmp);
		if(stack != null) {
			TileTransferer.addStackToTrack(burst);
			TileTransferer.setTrackingStack(burst, stack);
		}
		else TileTransferer.setTrackingStack(burst, new ItemStack(Blocks.stone, 0, 0));
	}*/
}
