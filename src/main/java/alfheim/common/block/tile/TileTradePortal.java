package alfheim.common.block.tile;

import java.util.List;

import com.google.common.base.Function;

import alfheim.api.AlfheimAPI;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.core.utils.AlfheimConfig;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.BlockStorage;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileMod;
import vazkii.botania.common.core.handler.ConfigHandler;

public class TileTradePortal extends TileMod {

	private static final int[][] LIVINGROCK_POSITIONS = {
			{ -1, 0, 0 }, { 1, 0, 0 },
			{ -2, 1, 0 }, { 2, 1, 0 },
			{ -2, 3, 0 }, { 2, 3, 0 },
			{ -1, 4, 0 }, { 1, 4, 0 }
	};

	private static final int[][] GLOWSTONE_POSITIONS = { { -2, 2, 0 }, { 2, 2, 0 }, { 0, 4, 0 } };
	
	private static final int[][] PYLON_POSITIONS = { { -2, 4, 0 }, { 2, 4, 0 } };

	private static final int[][] AIR_POSITIONS = {
			{ -1, 1, 0 }, { 0, 1, 0 }, { 1, 1, 0 },
			{ -1, 2, 0 }, { 0, 2, 0 }, { 1, 2, 0 },
			{ -1, 3, 0 }, { 0, 3, 0 }, { 1, 3, 0 }
	};

	private static final String TAG_TICKS_OPEN = "ticksOpen";
	private static final String TAG_RECIPE_MULT = "recipeMult";
	private static final String TAG_RECIPE_NUM = "recipeNub";

	RecipeElvenTrade tradeRecipe = null;
	int recipeMult = 0;
	int recipeNum = -1;

	public int ticksOpen = 0;
	private boolean hasUnloadedParts = false;

	private static final Function<int[], int[]> CONVERTER_X_Z = new Function<int[], int[]>() {
		@Override
		public int[] apply(int[] input) {
			return new int[] { input[2], input[1], input[0] };
		}
	};

	private static final Function<double[], double[]> CONVERTER_X_Z_FP = new Function<double[], double[]>() {
		@Override
		public double[] apply(double[] input) {
			return new double[] { input[2], input[1], input[0] };
		}
	};

	private static final Function<int[], int[]> CONVERTER_Z_SWAP = new Function<int[], int[]>() {
		@Override
		public int[] apply(int[] input) {
			return new int[] { input[0], input[1], -input[2] };
		}
	};

	public static MultiblockSet makeMultiblockSet() {
		Multiblock mb = new Multiblock();
		for (int[] l : LIVINGROCK_POSITIONS) mb.addComponent(l[0], l[1] + 1, l[2], ModBlocks.livingrock, 0);
		for (int[] g : GLOWSTONE_POSITIONS) mb.addComponent(g[0], g[1] + 1, g[2], Blocks.glowstone, 0);
		for (int[] p : PYLON_POSITIONS) mb.addComponent(p[0], p[1] + 1, p[2], AlfheimBlocks.alfheimPylons, 1);
		mb.addComponent(0, 1, 0, AlfheimBlocks.tradePortal, 0);
		mb.setRenderOffset(0, -1, 0);
		return mb.makeSet();
	}

	@Override
	public void updateEntity() {
		int meta = getBlockMetadata();
		if (meta == 0) {
			ticksOpen = 0;
			return;
		}
		int newMeta = getValidMetadata();

		if (!hasUnloadedParts) {
			ticksOpen++;

			AxisAlignedBB aabb = getPortalAABB();

			if (ticksOpen > 60) {
				if (ConfigHandler.elfPortalParticlesEnabled)
					blockParticle(meta);

				if (worldObj.rand.nextInt(12000) == 0 && !worldObj.isRemote) setRandomRecipe();
				
				if (this.tradeRecipe != null) {
					List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class, aabb);
					if (!worldObj.isRemote) {
						for (EntityItem item : items) {
							if (item.isDead)
								continue;
							
							ItemStack stack = item.getEntityItem();
							if (stack != null && isTradeAvailable(stack, this.tradeRecipe.getOutput())) {
								stack.stackSize -= this.tradeRecipe.getOutput().stackSize;
								performTrade();
								break;
							}
						}
					}
				}
			}
		}

		if (newMeta != meta) {
			if (newMeta == 0) for (int i = 0; i < 36; i++) blockParticle(meta);
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, newMeta, 1 | 2);
		}

		hasUnloadedParts = false;
	}

	boolean isTradeAvailable(ItemStack input, ItemStack output) {
		return input.getItem() == output.getItem() && input.getItemDamage() == output.getItemDamage() && input.stackSize >= output.stackSize;
	}
	
	private void blockParticle(int meta) {
		int i = worldObj.rand.nextInt(AIR_POSITIONS.length);
		double[] pos = new double[] { AIR_POSITIONS[i][0] + 0.5F, AIR_POSITIONS[i][1] + 0.5F, AIR_POSITIONS[i][2] + 0.5F };
		if (meta == 2) pos = CONVERTER_X_Z_FP.apply(pos);

		float motionMul = 0.2F;
		Botania.proxy.wispFX(getWorldObj(), xCoord + pos[0], yCoord + pos[1], zCoord + pos[2],
				(float) (Math.random() * 0.5F + 0.5F), (float) (Math.random() * 0.25F + 0.5F), (float) (Math.random() * 0.25F),
				(float) (Math.random() * 0.15F + 0.1F), (float) (Math.random() - 0.5F) * motionMul,
				(float) (Math.random() - 0.5F) * motionMul, (float) (Math.random() - 0.5F) * motionMul);
	}

	public boolean onWanded() {
		if (getBlockMetadata() == 0 && worldObj.provider.dimensionId == AlfheimConfig.dimensionIDAlfheim) {
			int newMeta = getValidMetadata();
			if (newMeta != 0) {
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, newMeta, 1 | 2);
				return true;
			}
		}
		return false;
	}

	AxisAlignedBB getPortalAABB() {
		if (getBlockMetadata() == 2) return AxisAlignedBB.getBoundingBox(xCoord, yCoord + 1, zCoord - 1, xCoord + 1, yCoord + 4, zCoord + 2);
		return AxisAlignedBB.getBoundingBox(xCoord - 1, yCoord + 1, zCoord, xCoord + 2, yCoord + 4, zCoord + 1);
	}

	void setRandomRecipe() {
		int i = this.worldObj.rand.nextInt(BotaniaAPI.elvenTradeRecipes.size());
		RecipeElvenTrade recipe = BotaniaAPI.elvenTradeRecipes.get(i);
		
		if (!AlfheimAPI.isRetradeForbidden(recipe.getOutput())) {
			if (recipe.getOutput().getItem() instanceof ItemBlock && Block.getBlockFromItem(recipe.getOutput().getItem()) instanceof BlockStorage && this.worldObj.rand.nextInt(10) != 0) setRandomRecipe();
			this.tradeRecipe = recipe;
			this.recipeMult = this.worldObj.rand.nextInt(16) + 1;
			this.recipeNum = i;
			this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			return;
		} else setRandomRecipe();
	}
	
	void performTrade() {
		if (recipeMult <= 0) return;
		List<Object> inputs = this.tradeRecipe.getInputs();
		for (Object in : inputs) {
			ItemStack stack;
			if (in instanceof String) {
				stack = OreDictionary.getOres((String) in).get(0);
			} else if (in instanceof ItemStack) {
				stack = ((ItemStack)in).copy();
			} else throw new IllegalArgumentException("Invalid input");
			spawnItem(new ItemStack(stack.getItem(), 1, stack.getItemDamage()));
		}
		if (--recipeMult <= 0) this.tradeRecipe = null;
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	void spawnItem(ItemStack stack) {
		EntityItem item = new EntityItem(worldObj, xCoord + 0.5, yCoord + 1.5, zCoord + 0.5, stack);
		worldObj.spawnEntityInWorld(item);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt) {
		nbt.setInteger(TAG_TICKS_OPEN, ticksOpen);
		nbt.setInteger(TAG_RECIPE_MULT, this.recipeMult);
		nbt.setInteger(TAG_RECIPE_NUM, this.recipeNum);
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt) {
		ticksOpen = nbt.getInteger(TAG_TICKS_OPEN);
		recipeMult = nbt.getInteger(TAG_RECIPE_MULT);
		recipeNum = nbt.getInteger(TAG_RECIPE_NUM);
		if (recipeNum != -1) this.tradeRecipe = BotaniaAPI.elvenTradeRecipes.get(recipeNum);
	}

	private int getValidMetadata() {
		if (this.worldObj.provider.dimensionId != AlfheimConfig.dimensionIDAlfheim) return 0;
		if (checkConverter(null)) return 1;
		if (checkConverter(CONVERTER_X_Z)) return 2;

		return 0;
	}

	private boolean checkConverter(Function<int[], int[]> baseConverter) {
		return checkMultipleConverters(baseConverter) || checkMultipleConverters(CONVERTER_Z_SWAP, baseConverter);
	}

	private boolean checkMultipleConverters(Function<int[], int[]>... converters) {
		if (!check2DArray(AIR_POSITIONS, Blocks.air, -1, converters)) return false;
		if (!check2DArray(LIVINGROCK_POSITIONS, ModBlocks.livingrock, 0, converters)) return false;
		if (!check2DArray(GLOWSTONE_POSITIONS, Blocks.glowstone, 0, converters)) return false;
		if (!check2DArray(PYLON_POSITIONS, AlfheimBlocks.alfheimPylons, 1, converters)) return false;

		lightPylons(converters);
		return true;
	}

	private void lightPylons(Function<int[], int[]>... converters) {
		if(ticksOpen < 50)
			return;

		for(int[] pos : PYLON_POSITIONS) {
			for(Function<int[], int[]> f : converters) if(f != null) pos = f.apply(pos);

			TileEntity tile = worldObj.getTileEntity(xCoord + pos[0], yCoord + pos[1], zCoord + pos[2]);
			if(tile instanceof TileAlfheimPylons) {
				TileAlfheimPylons pylon = (TileAlfheimPylons) tile;
				pylon.activated = true;
				pylon.centerX = xCoord;
				pylon.centerY = yCoord;
				pylon.centerZ = zCoord;
			}
		}
	}
	
	private boolean check2DArray(int[][] positions, Block block, int meta, Function<int[], int[]>... converters) {
		for (int[] pos : positions) {
			for (Function<int[], int[]> f : converters) if (f != null) pos = f.apply(pos);
			if (!checkPosition(pos, block, meta)) return false;
		}

		return true;
	}

	private boolean checkPosition(int[] pos, Block block, int meta) {
		int x = xCoord + pos[0];
		int y = yCoord + pos[1];
		int z = zCoord + pos[2];
		if (!worldObj.blockExists(x, y, z)) {
			hasUnloadedParts = true;
			return true; // Don't fuck everything up if there's a chunk unload
		}

		Block blockat = worldObj.getBlock(x, y, z);
		if (block == Blocks.air ? blockat.isAir(worldObj, x, y, z) : blockat == block) {
			if (meta == -1)
				return true;

			int metaat = worldObj.getBlockMetadata(x, y, z);
			return meta == metaat;
		}

		return false;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}
	
	public ItemStack getOutput() {
		return this.tradeRecipe.getOutput();
	}
	
	public boolean isTradeOn() {
		return tradeRecipe != null && recipeMult > 0;
	}
}
