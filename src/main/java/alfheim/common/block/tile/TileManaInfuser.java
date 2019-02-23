package alfheim.common.block.tile;

import static alexsocol.asjlib.ASJUtilities.*;

import java.util.List;

import alexsocol.asjlib.math.Vector3;
import alfheim.api.AlfheimAPI;
import alfheim.api.crafting.recipe.RecipeManaInfuser;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.core.registry.AlfheimItems;
import alfheim.common.item.relic.ItemFlugelSoul;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.api.mana.spark.SparkHelper;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.tile.TileBrewery;
import vazkii.botania.common.block.tile.TileMod;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.entity.EntityDoppleganger;

public class TileManaInfuser extends TileMod implements ISparkAttachable {
	
	private static final boolean DEBUG = false;
	
	public static final int MAX_MANA = TilePool.MAX_MANA * 8;
	
	private static final int[][] GAIAS = {{4,-1,4},{-4,-1,4},{-4,-1,-4},{4,-1,-4}};
	private static final int[][] PYLONS = {{6,-1,0},{0,-1,6},{-6,-1,0},{0,-1,-6}};
	private static final int[][] QUARTZ_BLOCK = {{1,0,0},{-1,0,0},{0,0,1},{0,0,-1}};
	private static final int[][] ELEMENTIUM_BLOCKS = {{1,0,1},{1,0,-1},{-1,0,1},{-1,0,-1}};
	private static final String TAG_MANA = "mana", TAG_MANA_REQUIRED = "manaRequired", TAG_KNOWN_MANA = "knownMana";
	
	int mana, manaRequest, knownMana = -1;
	ItemStack result;
	
	public static MultiblockSet makeMultiblockSetSoul() {
		Multiblock mb = new Multiblock();
		for(int[] l : PYLONS) mb.addComponent(l[0], 1, l[2], AlfheimBlocks.alfheimPylon, 2);
		mb.addComponent(0, 0, 0, Blocks.beacon, 0);
		mb.addComponent(0, 2, 0, AlfheimBlocks.manaInfuser, 0);
		mb.addComponent(0, 5, 0, ModBlocks.brewery, 0);
		mb.setRenderOffset(0, 0, 0);
		return mb.makeSet();
	}
	
	public static MultiblockSet makeMultiblockSetUnknown() {
		Multiblock mb = new Multiblock();
		for(int[] l : QUARTZ_BLOCK) mb.addComponent(l[0], 0, l[2], Blocks.wool, 0);
		for(int[] l : ELEMENTIUM_BLOCKS) mb.addComponent(l[0], 0, l[2], Blocks.wool, 15);
		mb.addComponent(0, 0, 0, AlfheimBlocks.manaInfuser, 0);
		mb.setRenderOffset(0, 1, 0);
		return mb.makeSet();
	}
	
	public static MultiblockSet makeMultiblockSet() {
		Multiblock mb = new Multiblock();
		for(int[] l : QUARTZ_BLOCK) mb.addComponent(l[0], 0, l[2], ModFluffBlocks.elfQuartz, 0);
		for(int[] l : ELEMENTIUM_BLOCKS) mb.addComponent(l[0], 0, l[2], ModBlocks.storage, 2);
		mb.addComponent(0, 0, 0, AlfheimBlocks.manaInfuser, 0);
		mb.setRenderOffset(0, 1, 0);
		return mb.makeSet();
	}
	
	@Override
	public void updateEntity() {
		boolean removeMana = true;
		int boom = 3;
		
		gaia: if (isReadyToKillGaia()) {
			List<EntityDoppleganger> l = worldObj.getEntitiesWithinAABB(EntityDoppleganger.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord + 1, zCoord, xCoord + 1, yCoord + 3, zCoord + 1));
			boom: if (!l.isEmpty()) {
				if (l.size() > 1) break boom;
				else {
					if (blockMetadata != 2)	worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 2, 3);
					boom = 5;
					EntityDoppleganger dop = l.get(0);
					boolean hard = dop.isHardMode();
					
					if (dop.getInvulTime() <= 0) {
						soulParticles();
						boom = 7;
						dop.setHealth(dop.getHealth() - 10);
						
						if (dop.getHealth() <= 0) {
							dop.setDead();
							if (dop.isEntityAlive()) break boom;
						} else return;
						
						TileEntity te = worldObj.getTileEntity(xCoord, yCoord + 3, zCoord);
						
						if (te != null && te instanceof TileBrewery) {
							TileBrewery brew = (TileBrewery) te;
							
							if (brew.getStackInSlot(0) != null && brew.getStackInSlot(0).getItem() == AlfheimItems.flugelSoul) {
								ItemFlugelSoul soul = (ItemFlugelSoul) ((TileBrewery) te).getStackInSlot(0).getItem();
								
								if (soul.getBlocked(brew.getStackInSlot(0)) > 0) {
									boom = 10;
									if (hard || Math.random() > 0.5) soul.setDisabled(brew.getStackInSlot(0), soul.getBlocked(brew.getStackInSlot(0)), false);
									else break boom;
									doneParticles();
									break gaia;
								}
							}
						} else break boom;
					} else {
						if (worldObj.getTotalWorldTime() % 5 == 0) prepareParticles();
						return;
					}
				}
			} else {
				if (blockMetadata != 0)	worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
				break gaia;
			}
			
			worldObj.newExplosion(null, xCoord, yCoord, zCoord, boom, true, false);
			worldObj.setBlockToAir(xCoord, yCoord, zCoord);
			return;
		}
		
		if (mana <= 0 && blockMetadata != 0)  worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
		
		if(hasValidPlatform()) {
			List<EntityItem> items = getItems();
			if(areItemsValid(items)) {
				if (DEBUG) System.out.println("Mana: " + mana + "\tMana requested: " + manaRequest + "\tResult: " + result.toString());
				
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
				
				if(mana >= manaRequest && !worldObj.isRemote) {
					EntityItem item = items.get(0);
					for(EntityItem otherItem : items)
						if(otherItem != item)
							otherItem.setDead();
						else item.setEntityItemStack(new ItemStack(result.getItem(), Math.max(result.stackSize, 1), result.getItemDamage()));
					item.worldObj.playSoundAtEntity(item, "botania:terrasteelCraft", 1F, 1F);
					mana -= manaRequest;
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
					worldObj.func_147453_f(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, xCoord, yCoord, zCoord);
					result = null;
					manaRequest = 0;
				}
			} else {
				result = null;
				manaRequest = 0;
			}
		}
		
		if(removeMana) recieveMana(-1000);
	}
	
	void doParticles() {
		if(worldObj.isRemote) {
			int ticks = (int) (100.0 * ((double) getCurrentMana() / (double) manaRequest));
			
			int totalSpiritCount = 6;
			double tickIncrement = 360.0 / totalSpiritCount;
			
			int speed = 5;
			double wticks = ticks * speed - tickIncrement;
			
			double r = Math.sin((ticks - 100) / 10.0) * 2;
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
	
	Vector3 v = new Vector3();
	
	void prepareParticles() {
		int ci = 0;
		for (int[] c : PYLONS) {
			for (int i = -1; i < 84; i++) {
				v.set(0, 0, 0.1).rotate(-45 - 90 * ci, Vector3.oY).extend(i / 10.).add(c[0], c[1], c[2]);
				Botania.proxy.sparkleFX(worldObj, xCoord + v.x + 0.5, yCoord + v.y + 0.65, zCoord + v.z + 0.5, 1, 0.01F, 0.01F, 1, 2);
			}
			ci++;
		}
	}
	
	void soulParticles() {
		for (int[] c : PYLONS) {
			v.set(-c[0], -c[1], -c[2]).normalize().mul(0.4);
			Botania.proxy.wispFX(worldObj, xCoord + c[0] + 0.5, yCoord + c[1] + 0.65, zCoord + c[2] + 0.5, 1, 0.01F, 0.01F, 0.5F, (float) v.x, (float) v.y, (float) v.z, 0.5F);
		}
		for (int[] c : GAIAS) {
			v.set(c[0], 0, c[2]).normalize().mul(0.3);
			float r = (float) Math.random() * 0.3F;
			float g = 0.7F + (float) Math.random() * 0.3F;
			float b = 0.7F + (float) Math.random() * 0.3F;
			v.rotate(107.5, Vector3.oY);
			Botania.proxy.wispFX(worldObj, xCoord + c[0] + 0.5, yCoord + c[1] + 0.65, zCoord + c[2] + 0.5, r, g, b, 0.5F, (float) v.x, (float) v.y, (float) v.z, 0.5F);
			v.rotate(-215, Vector3.oY);
			Botania.proxy.wispFX(worldObj, xCoord + c[0] + 0.5, yCoord + c[1] + 0.65, zCoord + c[2] + 0.5, r, g, b, 0.5F, (float) v.x, (float) v.y, (float) v.z, 0.5F);
		}
	}
	
	void doneParticles() {
		for (int i = 0; i < 64; i++) {
			v.set(Math.random() - 0.5, 0, Math.random() - 0.5).normalize().mul(Math.random() * 0.2 + 0.1);
			Botania.proxy.wispFX(worldObj, xCoord + 0.5, yCoord + 1.65 + Math.random() * 0.2, zCoord + 0.5, 1, 0.01F, 0.01F, 0.5F, (float) v.x, (float) v.y, (float) v.z, 0.5F);
		}
		for (int i = 0; i < 16; i++) {
			v.set(0, 1, 0).mul(Math.random() * 0.2 + 0.1);
			Botania.proxy.wispFX(worldObj, xCoord + 0.5, yCoord + 1 + Math.random(), zCoord + 0.5, 1, 0.01F, 0.01F, 0.5F, (float) v.x, (float) v.y, (float) v.z, 0.5F);
		}
		for (int i = 0; i < 64; i++) {
			Botania.proxy.sparkleFX(worldObj, xCoord + 0.5 + Math.random() * 0.25 - 0.125, yCoord + 4, zCoord + 0.5 + Math.random() * 0.25 - 0.125, 1, 0.01F, 0.01F, 5, 15);
		}
	}
	
	List<EntityItem> getItems() {
		return worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord + 1, zCoord, xCoord + 1, yCoord + 2, zCoord + 1));
	}
	
	boolean areItemsValid(List<EntityItem> items) {
		//boolean DBG = !worldObj.isRemote;
		if (items.isEmpty()) return false;
		for (RecipeManaInfuser recipe : AlfheimAPI.manaInfuserRecipes) {
			if (DEBUG) System.out.println(recipe.toString());
			if(items.size() != recipe.getInputs().size()) {
				if (DEBUG) System.out.println("Incorrect items amount (" + items.size() + "). Skipping this recipe.");
				continue; // Odd items will mess up the infusion, less means not enough materials
			}
			
			boolean[] equalitylist = new boolean[recipe.getInputs().size()]; // this array contains whether required ingredient is inside of AABB of infuser
			
			if (DEBUG) System.out.println("Scanning entities...");
			
			for(EntityItem item : items) { // For every item in AABB
				ItemStack stack = item.getEntityItem();
				if (DEBUG) System.out.println("Entity stack: " + stack.toString());
				if (DEBUG) System.out.println("Scanning recipe for stack...");
				for (int i = 0; i < recipe.getInputs().size(); i++) {
					if (equalitylist[i]) continue;
					Object ing = recipe.getInputs().get(i);
					if (DEBUG) System.out.println("Ingredient: " + ing.toString());
					boolean flag = false;
					if (ing instanceof ItemStack) {
						ItemStack cing = ((ItemStack) ing).copy();
						if(getTrueDamage(cing) == OreDictionary.WILDCARD_VALUE) { // Cause some shit clamps values to maxDamage
							cing.setItemDamage(getTrueDamage(stack));
						}
						flag = isItemStackTrueEqual(stack, cing);
					} else 
					
					if (ing instanceof String) {
						List<ItemStack> ores = OreDictionary.getOres((String) ing);
						for (ItemStack ore : ores) {
							ItemStack core = ore.copy();
							if(getTrueDamage(core) == OreDictionary.WILDCARD_VALUE) core.setItemDamage(getTrueDamage(stack));
							
							if (isItemStackTrueEqual(stack, new ItemStack(core.getItem(), 1, core.getItemDamage()))) {
								flag = true;
								break;
							}
						}
					}
						
					if(flag) {
						if (DEBUG) System.out.println("Entity stack matches ingredient stack (" + stack.toString() + " == " + ing.toString() + ") Continuing scanning.");
						equalitylist[i] = true; // Marking true for further processing
						continue;
					}
					if (DEBUG) System.out.println("Entity stack DON'T match ingredient stack (" + stack.toString() + " != " + ing.toString() + ") Continuing scanning.");
				}
			}
			
			if (DEBUG) System.out.println("Scanning complete. Checking matching");
			
			boolean flagAllEqual = true; // I'm sure everything matches
			for (boolean deflag : equalitylist) { // But let's check
				flagAllEqual = deflag;
				if (!flagAllEqual) {
					if (DEBUG) System.out.println("Matching error. Breaking cycle!");
					break; // Oh no! Something went wrong!
				}
				// Leaving to maybe do something else
			}
			
			if (flagAllEqual) { // I told you everything is fine
				if (DEBUG) System.out.println("Everything matches. Sending item and mana cost to tile, returning true.");
				manaRequest = recipe.mana;
				result = recipe.getOutput();
				return true;
			}
		}
		
		if (DEBUG) System.out.println("Scanned all recipes, no matching found. Returning false.");
		return false;
	}
	
	boolean isReadyToKillGaia() {
		return checkPlatform(0, -2,  0, Blocks.beacon, 0) && checkAll(PYLONS, AlfheimBlocks.alfheimPylon, 2);
	}
	
	boolean hasValidPlatform() {
		return checkAll(QUARTZ_BLOCK, ModFluffBlocks.elfQuartz, 0) && checkAll(ELEMENTIUM_BLOCKS, ModBlocks.storage, 2);
	}
	
	boolean checkAll(int[][] positions, Block block, int meta) {
		for (int[] position : positions) {
			if(!checkPlatform(position[0], position[1], position[2], block, meta))
				return false;
		}
		
		return true;
	}
	
	boolean checkPlatform(int xOff, int yOff, int zOff, Block block, int meta) {
		return worldObj.getBlock(xCoord + xOff, yOff + yCoord, zOff + zCoord) == block && worldObj.getBlockMetadata(xCoord + xOff, yCoord + yOff, zOff + zCoord) == meta;
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound nbt) {
		nbt.setInteger(TAG_MANA, mana);
		nbt.setInteger(TAG_MANA_REQUIRED, manaRequest);
		nbt.setInteger(TAG_KNOWN_MANA, knownMana);
	}
	
	@Override
	public void readCustomNBT(NBTTagCompound nbt) {
		mana = nbt.getInteger(TAG_MANA);
		knownMana = nbt.getInteger(TAG_KNOWN_MANA);
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
		return hasValidPlatform() && areItemsValid(getItems());
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
		return !hasValidPlatform() || !areItemsValid(getItems());
	}
	
	@Override
	public int getAvailableSpaceForMana() {
		return Math.max(0, MAX_MANA - getCurrentMana());
	}
	
	public void onWanded(EntityPlayer player, ItemStack stack) {
		if(player == null)
			return;
		
		if(!player.isSneaking()) {
			if(!worldObj.isRemote) {
				knownMana = mana;
				NBTTagCompound nbt = new NBTTagCompound();
				writeCustomNBT(nbt);
				nbt.setInteger(TAG_KNOWN_MANA, knownMana);
				if(player instanceof EntityPlayerMP)
					((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, nbt));
			}
			worldObj.playSoundAtEntity(player, "botania:ding", 0.1F, 1F);
		}
	}
	
	public void renderHUD(Minecraft mc, ScaledResolution res) {
		String name = StatCollector.translateToLocal(new ItemStack(AlfheimBlocks.manaInfuser, 1, getBlockMetadata()).getUnlocalizedName() + ".name");
		int color = 0xCC00FF;
		HUDHandler.drawSimpleManaHUD(color, knownMana, MAX_MANA, name, res);
		org.lwjgl.opengl.GL11.glColor4d(1, 1, 1, 1);
	}
}
