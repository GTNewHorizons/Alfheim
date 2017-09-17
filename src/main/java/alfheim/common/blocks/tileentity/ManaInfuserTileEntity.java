package alfheim.common.blocks.tileentity;

import java.util.List;
import java.util.Vector;

import alexsocol.asjlib.ASJUtilities;
import alfheim.common.crafting.IManaInfusionRecipe;
import alfheim.common.crafting.ManaInfusionRecipies;
import alfheim.common.registry.AlfheimBlocks;
import alfheim.common.registry.AlfheimItems;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.api.mana.spark.SparkHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.decor.quartz.BlockSpecialQuartz;
import vazkii.botania.common.block.tile.TileMod;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibBlockNames;

public class ManaInfuserTileEntity extends TileMod implements ISparkAttachable {

	private static final boolean DEBUG = false;
	
	public static final int MAX_MANA = TilePool.MAX_MANA * 8;

	private static final int[][] QUART_BLOCK = {{1,0},{-1,0},{0,1},{0,-1}};
	private static final int[][] ELEMENTIUM_BLOCKS = {{1,1},{1,-1},{-1,1},{-1,-1}};
	private static final String TAG_MANA = "mana";
	int mana;
	int manarequest;
	ItemStack result;

	public static MultiblockSet makeMultiblockSet() {
		Multiblock mb = new Multiblock();
		for(int[] l : QUART_BLOCK) mb.addComponent(l[0], 0, l[1], ModBlocks.storage, 4);
		for(int[] l : ELEMENTIUM_BLOCKS) mb.addComponent(l[0], 0, l[1], ModBlocks.storage, 2);
		mb.addComponent(0, 0, 0, AlfheimBlocks.manaInfuser, 0);
		mb.setRenderOffset(0, 1, 0);
		return mb.makeSet();
	}
	
	@Override
	public void updateEntity() {
		boolean removeMana = true;

		if(hasValidPlatform()) {
			List<EntityItem> items = getItems();
			if(areItemsValid(items)) {
				if (DEBUG) System.out.println("Mana: " + mana + "\tMana requested: " + manarequest + "\tResult: " + result.toString());
				
				removeMana = false;
				ISparkEntity spark = getAttachedSpark();
				if(spark != null) {
					List<ISparkEntity> sparkEntities = SparkHelper.getSparksAround(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
					for(ISparkEntity otherSpark : sparkEntities) {
						if(spark == otherSpark)
							continue;

						if(otherSpark.getAttachedTile() != null && otherSpark.getAttachedTile() instanceof IManaPool)
							otherSpark.registerTransfer(spark);
					}
				}
				
				if(mana > 0) {
					doParticles();
					if (blockMetadata != 1) worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 3);
				}

				if(mana >= manarequest && !worldObj.isRemote) {
					EntityItem item = items.get(0);
					for(EntityItem otherItem : items)
						if(otherItem != item)
							otherItem.setDead();
						else item.setEntityItemStack(new ItemStack(result.getItem(), Math.max(result.stackSize, 1), result.getItemDamage()));
					item.worldObj.playSoundAtEntity(item, "botania:terrasteelCraft", 1F, 1F);
					mana -= manarequest;
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
					worldObj.func_147453_f(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, xCoord, yCoord, zCoord);
					result = null;
					manarequest = 0;
				}
			} else {
				result = null;
				manarequest = 0;
			}
		}

		if(removeMana) recieveMana(-1000);
	}

	void doParticles() {
		if(worldObj.isRemote) {
			int ticks = (int) (100.0 * ((double) getCurrentMana() / (double) manarequest));

			int totalSpiritCount = 6;
			double tickIncrement = 360D / totalSpiritCount;

			int speed = 5;
			double wticks = ticks * speed - tickIncrement;

			double r = Math.sin((ticks - 100) / 10D) * 2;
			double g = Math.sin(wticks * Math.PI / 180 * 0.55);

			for(int i = 0; i < totalSpiritCount; i++) {
				double x = xCoord + Math.sin(wticks * Math.PI / 180) * r + 0.5;
				double y = yCoord + 0.25 + Math.abs(r) * 0.7;
				double z = zCoord + Math.cos(wticks * Math.PI / 180) * r + 0.5;

				wticks += tickIncrement;
				float[] colorsfx = new float[] {
						(float) ticks / (float) 100, 0F, 1F - (float) ticks / (float) 100
				};
				Botania.proxy.wispFX(worldObj, x, y + 1, z, colorsfx[0], colorsfx[1], colorsfx[2], 0.85F, (float)g * 0.05F, 0.25F);
				Botania.proxy.wispFX(worldObj, x, y + 1, z, colorsfx[0], colorsfx[1], colorsfx[2], (float) Math.random() * 0.1F + 0.1F, (float) (Math.random() - 0.5) * 0.05F, (float) (Math.random() - 0.5) * 0.05F, (float) (Math.random() - 0.5) * 0.05F, 0.9F);

				if(ticks == 100)
					for(int j = 0; j < 15; j++)
						Botania.proxy.wispFX(worldObj, xCoord + 0.5, yCoord + 1.25, zCoord + 0.5, colorsfx[0], colorsfx[1], colorsfx[2], (float) Math.random() * 0.15F + 0.15F, (float) (Math.random() - 0.5F) * 0.125F, (float) (Math.random() - 0.5F) * 0.125F, (float) (Math.random() - 0.5F) * 0.125F);
			}
		}
	}

	List<EntityItem> getItems() {
		return worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord + 1, zCoord, xCoord + 1, yCoord + 2, zCoord + 1));
	}

	boolean areItemsValid(List<EntityItem> items) {
		for (IManaInfusionRecipe recipe : ManaInfusionRecipies.recipes) {
			if (DEBUG) System.out.println(recipe.toString());
			if(items.size() != recipe.getInputs().size()) {
				if (DEBUG) System.out.println("Incorrect items amount (" + items.size() + "). Skipping this recipe.");
				continue; // Odd items will mess up the infusion, less means not enough materials
			}
			
			boolean[] equalitylist = new boolean[recipe.getInputs().size()]; // this array contains whether required ingredient is inside of AABB of infuser
			for (boolean b : equalitylist) b = false; // Setting every value to false
			
			if (DEBUG) System.out.println("Scanning entities...");
			
			for(EntityItem item : items) { // For every item in AABB
				ItemStack stack = item.getEntityItem();
				if (DEBUG) System.out.println("Entity stack: " + stack.toString());
				int size = stack.stackSize;
				int meta = stack.getItemDamage();
				if (DEBUG) System.out.println("Scanning recipe for stack...");
				for (int i = 0; i < recipe.getInputs().size(); i++) {
					if (equalitylist[i]) continue;
					ItemStack ing = (ItemStack) recipe.getInputs().get(i);
					if (DEBUG) System.out.println("Ingredient: " + ing.toString());
					if(ASJUtilities.isItemStackEqual(stack, ing) && stack.stackSize == ing.stackSize) {
						if (DEBUG) System.out.println("Entity stack matches ingredient stack (" + stack.toString() + " = " + ing.toString() + ") Continuing scanning.");
						equalitylist[i] = true; // Marking true for further processing
						continue;
					}
				}
			}

			if (DEBUG) System.out.println("Scanning complete. Checking matching");
			
			boolean flagAllEqual = true; // I'm sure everything matches
			for (boolean deflag : equalitylist) { // But let's check
				flagAllEqual = deflag;
				if (!flagAllEqual) {
					if (DEBUG) System.out.println("Matching error. Continuing on...");
					continue;// Oh no! Something went wrong!
				}
				// Leaving to maybe do something else
			}
			
			if (flagAllEqual) { // I told you everything is fine
				if (DEBUG) System.out.println("Everything matches. Sending item and mana cost to tile, returning true.");
				manarequest = recipe.mana;
				result = recipe.getOutput();
				return true;
			}
		}

		if (DEBUG) System.out.println("Scanned all recipes, no matching found. Returning false.");
		return false;
	}

	boolean hasValidPlatform() {
		return checkAll(QUART_BLOCK, ModFluffBlocks.elfQuartz, 0) && checkAll(ELEMENTIUM_BLOCKS, ModBlocks.storage, 2);
	}

	boolean checkAll(int[][] positions, Block block, int meta) {
		for (int[] position : positions) {
			if(!checkPlatform(position[0], position[1], block, meta))
				return false;
		}

		return true;
	}

	boolean checkPlatform(int xOff, int zOff, Block block, int meta) {
		return worldObj.getBlock(xCoord + xOff, yCoord, zOff + zCoord) == block && worldObj.getBlockMetadata(xCoord + xOff, yCoord, zOff + zCoord) == meta;
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_MANA, mana);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		mana = cmp.getInteger(TAG_MANA);
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	@Override
	public boolean isFull() {
		return mana >= MAX_MANA;
	}

	@Override
	public void recieveMana(int mana) {
		this.mana = Math.max(0, Math.min(MAX_MANA, this.mana + mana));
		worldObj.func_147453_f(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return areItemsValid(getItems());
	}

	@Override
	public boolean canAttachSpark(ItemStack stack) {
		return true;
	}

	@Override
	public void attachSpark(ISparkEntity entity) {
		// NO-OP
	}

	@Override
	public ISparkEntity getAttachedSpark() {
		List<ISparkEntity> sparks = worldObj.getEntitiesWithinAABB(ISparkEntity.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord + 1, zCoord, xCoord + 1, yCoord + 2, zCoord + 1));
		if(sparks.size() == 1) {
			Entity e = (Entity) sparks.get(0);
			return (ISparkEntity) e;
		}

		return null;
	}

	@Override
	public boolean areIncomingTranfersDone() {
		return !areItemsValid(getItems());
	}

	@Override
	public int getAvailableSpaceForMana() {
		return Math.max(0, MAX_MANA - getCurrentMana());
	}
}
