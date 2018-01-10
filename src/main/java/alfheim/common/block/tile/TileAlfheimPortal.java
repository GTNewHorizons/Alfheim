package alfheim.common.block.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.base.Function;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.core.utils.AlfheimConfig;
import alfheim.common.entity.EnumRace;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileMod;
import vazkii.botania.common.block.tile.TilePylon;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.Vector3;

public class TileAlfheimPortal extends TileMod {

	private static final int[][] DREAMWOOD_POSITIONS = { { -1, 0, 0 }, { 1, 0, 0 }, { -2, 1, 0 }, { 2, 1, 0 }, { -2, 3, 0 }, { 2, 3, 0 }, { -1, 4, 0 }, { 1, 4, 0 } };
	private static final int[][] GLIMMERING_DREAMWOOD_POSITIONS = { { -2, 2, 0 }, { 2, 2, 0 }, { 0, 4, 0 } };
	private static final int[][] PYLON_POSITIONS = { { -3, 1, 3 }, { 3, 1, 3 } };
	private static final int[][] POOL_POSITIONS = { { -3, 0, 3 }, { 3, 0, 3 } };
	private static final int[][] AIR_POSITIONS = { { -1, 1, 0 }, { 0, 1, 0 }, { 1, 1, 0 }, { -1, 2, 0 }, { 0, 2, 0 }, { 1, 2, 0 }, { -1, 3, 0 }, { 0, 3, 0 }, { 1, 3, 0 } };

	private static final String TAG_TICKS_OPEN = "ticksOpen";

	private static final int activation = 75000;
	private static final int idle = 2;

	public int ticksOpen = 0;
	private boolean closeNow = false;
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

		for (int[] l : DREAMWOOD_POSITIONS)
			mb.addComponent(l[0], l[1] + 1, l[2], ModBlocks.dreamwood, 0);
		for (int[] g : GLIMMERING_DREAMWOOD_POSITIONS)
			mb.addComponent(g[0], g[1] + 1, g[2], ModBlocks.dreamwood, 5);
		for (int[] p : PYLON_POSITIONS)
			mb.addComponent(-p[0], p[1] + 1, -p[2], ModBlocks.pylon, 2);
		for (int[] p : POOL_POSITIONS)
			mb.addComponent(-p[0], p[1] + 1, -p[2], ModBlocks.pool, 0);

		mb.addComponent(0, 1, 0, AlfheimBlocks.alfheimPortal, 0);
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
				List<EntityPlayer> players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
				if (!worldObj.isRemote)
					for (EntityPlayer player : players) {
						if (player.isDead) continue;
						
						boolean flag = player.dimension == AlfheimConfig.dimensionIDAlfheim;
						if (flag) {
							ChunkCoordinates coords = player.getBedLocation(0);
							if (coords == null) coords = MinecraftServer.getServer().worldServerForDimension(0).getSpawnPoint();
							if (coords == null) coords = new ChunkCoordinates(0, MinecraftServer.getServer().worldServerForDimension(0).getHeightValue(0, 0), 0);

							if (AlfheimConfig.destroyPortal && (this.xCoord != 0 || this.zCoord != 0)) {
								this.worldObj.newExplosion(player, this.xCoord, this.yCoord, this.zCoord, 5, false, false);
								this.worldObj.setBlockToAir(this.xCoord - 2, this.yCoord + 2, this.zCoord);
								this.worldObj.setBlockToAir(this.xCoord + 2, this.yCoord + 2, this.zCoord);
								this.worldObj.setBlockToAir(this.xCoord, this.yCoord + 4, this.zCoord);
								
								this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
							}
							
							ASJUtilities.sendToDimensionWithoutPortal(player, 0, coords.posX, coords.posY, coords.posZ);
						} else {
							if (AlfheimCore.enableElvenStory) {
								int race = EnumRace.getRaceID(player) - 1;
								if (0 <= race && race < 9) ASJUtilities.sendToDimensionWithoutPortal(player, AlfheimConfig.dimensionIDAlfheim, AlfheimConfig.zones[race].xCoord, AlfheimConfig.zones[race].yCoord, AlfheimConfig.zones[race].zCoord);
								else ASJUtilities.sendToDimensionWithoutPortal(player, AlfheimConfig.dimensionIDAlfheim, 0.5, 253, 0.5);
							}
							else ASJUtilities.sendToDimensionWithoutPortal(player, AlfheimConfig.dimensionIDAlfheim, 0.5, 75, -1.5);
						}
					}
				if (ConfigHandler.elfPortalParticlesEnabled)
					blockParticle(meta);
				
				/*ticksSinceLastItem++;

				List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class, aabb);
				if (!worldObj.isRemote)
					for (EntityItem item : items) {
						if (item.isDead)
							continue;

						ItemStack stack = item.getEntityItem();
						if (stack != null && (!(stack.getItem() instanceof IElvenItem) || !((IElvenItem) stack.getItem()).isElvenItem(stack)) && !item.getEntityData().hasKey(TAG_PORTAL_FLAG)) {
							item.setDead();
							addItem(stack);
							ticksSinceLastItem = 0;
						}
					}

				if (ticksSinceLastItem >= 4) {
					if (!worldObj.isRemote)
						resolveRecipes();
				}*/
			}
		} else
			closeNow = false;

		if (closeNow) {
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
			for (int i = 0; i < 36; i++)
				blockParticle(meta);
			closeNow = false;
		} else if (newMeta != meta) {
			if (newMeta == 0)
				for (int i = 0; i < 36; i++)
					blockParticle(meta);
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, newMeta, 3);
		}

		hasUnloadedParts = false;
	}

	private void blockParticle(int meta) {
		int i = worldObj.rand.nextInt(AIR_POSITIONS.length);
		double[] pos = new double[] { AIR_POSITIONS[i][0] + 0.5F, AIR_POSITIONS[i][1] + 0.5F,
				AIR_POSITIONS[i][2] + 0.5F };
		if (meta == 2)
			pos = CONVERTER_X_Z_FP.apply(pos);

		float motionMul = 0.2F;
		Botania.proxy.wispFX(getWorldObj(), xCoord + pos[0], yCoord + pos[1], zCoord + pos[2],
				(float) Math.random() * 0.25F + 0.5F, (float) Math.random() * 0.25F + 0.5F, (float) Math.random() * 0.25F,
				(float) (Math.random() * 0.15F + 0.1F),
				(float) (Math.random() - 0.5F) * motionMul,	(float) (Math.random() - 0.5F) * motionMul, (float) (Math.random() - 0.5F) * motionMul);
	}

	public boolean onWanded() {
		int meta = getBlockMetadata();
		if (meta == 0) {
			int newMeta = getValidMetadata();
			if (newMeta != 0) {
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, newMeta, 3);
				return true;
			}
		}

		return false;
	}

	AxisAlignedBB getPortalAABB() {
		AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(xCoord - 1, yCoord + 1, zCoord + 0.25, xCoord + 2, yCoord + 4, zCoord + 0.75);
		if (getBlockMetadata() == 2)
			aabb = AxisAlignedBB.getBoundingBox(xCoord + 0.25, yCoord + 1, zCoord - 1, xCoord + 0.75, yCoord + 4, zCoord + 2);

		return aabb;
	}

	/*void addItem(ItemStack stack) {
		int size = stack.stackSize;
		stack.stackSize = 1;
		for (int i = 0; i < size; i++)
			stacksIn.add(stack.copy());
	}

	void resolveRecipes() {
		int i = 0;
		for (ItemStack stack : stacksIn) {
			if (stack != null && stack.getItem() instanceof ILexicon) {
				((ILexicon) stack.getItem()).unlockKnowledge(stack, BotaniaAPI.elvenKnowledge);
				ItemLexicon.setForcedPage(stack, LexiconData.elvenMessage.getUnlocalizedName());
				spawnItem(stack);
				stacksIn.remove(i);
				return;
			}
			i++;
		}

		for (RecipeElvenTrade recipe : BotaniaAPI.elvenTradeRecipes) {
			if (recipe.matches(stacksIn, false)) {
				recipe.matches(stacksIn, true);
				spawnItem(recipe.getOutput().copy());
				break;
			}
		}
	}

	void spawnItem(ItemStack stack) {
		EntityItem item = new EntityItem(worldObj, xCoord + 0.5, yCoord + 1.5, zCoord + 0.5, stack);
		item.getEntityData().setBoolean(TAG_PORTAL_FLAG, true);
		worldObj.spawnEntityInWorld(item);
		ticksSinceLastItem = 0;
	}*/

	@Override
	public void writeToNBT(NBTTagCompound cmp) {
		super.writeToNBT(cmp);
	}

	@Override
	public void readFromNBT(NBTTagCompound cmp) {
		super.readFromNBT(cmp);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_TICKS_OPEN, ticksOpen);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		ticksOpen = cmp.getInteger(TAG_TICKS_OPEN);
	}

	private int getValidMetadata() {
		if (checkConverter(null))
			return 1;

		if (checkConverter(CONVERTER_X_Z))
			return 2;

		return 0;
	}

	private boolean checkConverter(Function<int[], int[]> baseConverter) {
		return checkMultipleConverters(baseConverter) || checkMultipleConverters(CONVERTER_Z_SWAP, baseConverter);
	}

	private boolean checkMultipleConverters(Function<int[], int[]>... converters) {
		if (!check2DArray(AIR_POSITIONS, Blocks.air, -1, converters))
			return false;
		if (!check2DArray(DREAMWOOD_POSITIONS, ModBlocks.dreamwood, 0, converters))
			return false;
		if (!check2DArray(GLIMMERING_DREAMWOOD_POSITIONS, ModBlocks.dreamwood, 5, converters))
			return false;
		if (!check2DArray(PYLON_POSITIONS, ModBlocks.pylon, 2, converters) && this.worldObj.provider.dimensionId != AlfheimConfig.dimensionIDAlfheim)
			return false;
		if (!check2DArray(POOL_POSITIONS, ModBlocks.pool, -1, converters) && this.worldObj.provider.dimensionId != AlfheimConfig.dimensionIDAlfheim)
			return false;

		lightPylons(converters);
		return true;
	}

	private void lightPylons(Function<int[], int[]>... converters) {
		if (ticksOpen < 50)
			return;

		int cost = ticksOpen == 50 ? activation : idle;

		for (int[] pos : PYLON_POSITIONS) {
			for (Function<int[], int[]> f : converters)
				if (f != null)
					pos = f.apply(pos);

			TileEntity tile = worldObj.getTileEntity(xCoord + pos[0], yCoord + pos[1], zCoord + pos[2]);
			if (tile instanceof TilePylon) {
				
				Vector3 centerBlock = new Vector3(xCoord + 0.5, yCoord + 0.75 + (Math.random() - 0.5 * 0.25), zCoord + 0.5);
				
				if(ConfigHandler.elfPortalParticlesEnabled) {
					double worldTime = this.worldObj.getTotalWorldTime();
					worldTime += new Random(xCoord + pos[0] ^ yCoord + pos[1] ^ zCoord + pos[2]).nextInt(1000);
					worldTime /= 5;

					float r = 0.75F + (float) Math.random() * 0.05F;
					double x = xCoord + pos[0] + 0.5 + Math.cos(worldTime) * r;
					double z = zCoord + pos[2] + 0.5 + Math.sin(worldTime) * r;

					Vector3 ourCoords = new Vector3(x, yCoord + pos[1] + 0.25, z);
					centerBlock.sub(new Vector3(0, 0.5, 0));
					Vector3 movementVector = centerBlock.sub(ourCoords).normalize().multiply(0.2);

					Botania.proxy.wispFX(worldObj, x, yCoord + pos[1] + 0.25, z,
							0.75F + (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 0.75F + (float) Math.random() * 0.25F,
							0.25F + (float) Math.random() * 0.1F, -0.075F - (float) Math.random() * 0.015F);
					
					if(worldObj.rand.nextInt(3) == 0)
						Botania.proxy.wispFX(worldObj, x, yCoord + pos[1] + 0.25, z,
								(float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 0.75F + (float) Math.random() * 0.25F,
								0.25F + (float) Math.random() * 0.1F,
								(float) movementVector.x, (float) movementVector.y, (float) movementVector.z);
				}
			}

			tile = worldObj.getTileEntity(xCoord + pos[0], yCoord + pos[1] - 1, zCoord + pos[2]);
			if (tile instanceof TilePool) {
				TilePool pool = (TilePool) tile;
				if (pool.getCurrentMana() < cost)
					closeNow = true;
				else if (!worldObj.isRemote)
					pool.recieveMana(-cost);
			}
		}
	}

	private boolean check2DArray(int[][] positions, Block block, int meta, Function<int[], int[]>... converters) {
		for (int[] pos : positions) {
			for (Function<int[], int[]> f : converters)
				if (f != null)
					pos = f.apply(pos);

			if (!checkPosition(pos, block, meta))
				return false;
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
}