package alexsocol.asjlib;

import static org.lwjgl.opengl.GL11.*;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.FMLRelaunchLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 * Small utility lib to help with some tricks. Feel free to use it in your mods.
 * @author AlexSocol
 * */
public class ASJUtilities {
	
	private static final ASJUtilities INSTANCE = new ASJUtilities();
	private static ArrayList<String> blockIconsNames = new ArrayList<String>();
	private static ArrayList<String> itemsIconsNames = new ArrayList<String>();
	private static Map<String, IIcon> blockIcons = new HashMap<String, IIcon>();
	private static Map<String, IIcon> itemsIcons = new HashMap<String, IIcon>();
	
	/**
	 * Registers this like event handler to load icons
	 * */
	static {
		MinecraftForge.EVENT_BUS.register(INSTANCE);
	}
	
	/**
	 * This will automatically register icons from lists
	 * */
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onTextureStitchEvent(TextureStitchEvent.Pre event) {
		switch (event.map.getTextureType()) {
		case 0: for(String s : blockIconsNames) blockIcons.put(s, event.map.registerIcon(s));
				break;
		case 1: for(String s : itemsIconsNames) itemsIcons.put(s, event.map.registerIcon(s));
				break;
		}
	}
	
	/**
	 * Returns the name of the block
	 * @param block Block to get name from
	 */
	public static String getBlockName(Block block) {
		return block.getUnlocalizedName().substring(5);
	}
	
	/**
	 * Returns the name of the item
	 * @param item Item to get name from
	 */
	public static String getItemName(Item item) {
		return item.getUnlocalizedName().substring(5);
	}
	
	/**
	 * Registers block by name
	 * @param block Block to register
	 */
	public static void register(Block block) {
		GameRegistry.registerBlock(block, getBlockName(block));
	}

	/**
	 * Registers item by name
	 * @param item Item to register
	 */
	public static void register(Item item) {
		GameRegistry.registerItem(item, getItemName(item));
	}
	
	/**
	 * Returns String ID of the mod this block/item is registered in
	 * @param stack ItemStack with block/item for analysis
	 * */
	public static String getModId(ItemStack stack) {
		GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(stack.getItem());
		return id == null || id.modId.equals("") ? "minecraft" : id.modId;
	}
	
	/**
	 * Sends entity to dimension without portal frames
	 * @param entity The entity to send
	 * @param dimTo ID of the dimension the entity should be sent to
	 * */
	public static void sendToDimensionWithoutPortal(Entity entity, int dimTo, double x, double y, double z) {
		if (dimTo == entity.dimension) {
			if (entity instanceof EntityLivingBase) ((EntityLivingBase) entity).setPositionAndUpdate(x, y, z);
			else entity.setPosition(x, y, z);
		}
		else if (entity instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) entity;
			WorldServer worldTo = player.mcServer.worldServerForDimension(dimTo);
			player.mcServer.getConfigurationManager().transferPlayerToDimension(player, dimTo, new FreeTeleporter(worldTo, x, y, z));
		}
	}
	
	public static boolean willEntityDie(LivingHurtEvent event) {
		return willEntityDie(new LivingAttackEvent(event.entityLiving, event.source, event.ammount));
	}
	
	/**
	 * Determines whether entity will die from next hit
	 * @param event Some event fired when entity's HP decreases
	 * */
	public static boolean willEntityDie(LivingAttackEvent event) {
		float amount = event.ammount;
		DamageSource source = event.source;
		EntityLivingBase living = event.entityLiving;
		if (!source.isUnblockable()) {
			int armor = 25 - living.getTotalArmorValue();
			amount = amount * armor / 25.0F;
		}
		if (living.isPotionActive(Potion.resistance)) {
			int resistance = 25 - (living.getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
			amount = amount * resistance / 25.0F;
		}
		return Math.ceil(amount) >= Math.floor(living.getHealth());
	}
	
	/**
	 * Returns the number of the slot with item matching to item passed in
	 * @param item The item to compare
	 * @param inventory The inventory to scan
	 */
	public static int getSlotWithItem(Item item, IInventory inventory) {
		for (int i = 0; i < inventory.getSizeInventory(); ++i) {
			if (inventory.getStackInSlot(i) != null && inventory.getStackInSlot(i).getItem() == item) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Removes itemstack from inventory
	 * @param inventory Inventory
	 * @param stack ItemStack to remove
	 * @return If the stack was removed
	 */
	public static boolean consumeItemStack(IInventory inventory, ItemStack stack) {
	   if(getAmount(inventory, stack) >= stack.stackSize) {
		  for (int i = 0; i < inventory.getSizeInventory(); i++) {
			 if(isItemStackEqualData(inventory.getStackInSlot(i), stack)){
				int amount = Math.min(stack.stackSize, inventory.getStackInSlot(i).stackSize);
				if(amount > 0) {
				   inventory.decrStackSize(i, amount);
				   stack.stackSize -= amount;
				}
				if(stack.stackSize <= 0) {
				   return true;
				}
			 }
		  }
	   }
	   return false;
	}

	/**
	 * Returns the amount of item from itemstack in inventory
	 * @param inventory Inventory
	 * @param stack Stack to compare item
	 * @return Amount
	 */
	public static int getAmount(IInventory inventory, ItemStack stack) {
	   int amount = 0;
	   for (int i = 0; i < inventory.getSizeInventory(); i++) {
		  if(isItemStackEqualData(inventory.getStackInSlot(i), stack)) {
			 amount += inventory.getStackInSlot(i).stackSize;
		  }
	   }
	   return amount;
	}
	
	/**
	 * @return damage from stack itself, not through item 
	 * */
	public static int getTrueDamage(ItemStack stack) {
		return Integer.valueOf(stack.toString().split("@")[1]).intValue();
	}
	
	/**
	 * Checks if two itemstacks has same ID, size and metadata
	 */
	public static boolean isItemStackEqual(ItemStack stack1, ItemStack stack2) {
		return (stack1 != null && stack2 != null && stack1.getItem() == stack2.getItem() && stack1.stackSize == stack2.stackSize && stack1.getItemDamage() == stack2.getItemDamage());
	}
	
	/**
	 * Checks if two itemstacks has same ID and metadata
	 */
	public static boolean isItemStackEqualData(ItemStack stack1, ItemStack stack2) {
		return (stack1 != null && stack2 != null && stack1.getItem() == stack2.getItem() && stack1.getItemDamage() == stack2.getItemDamage());
	}
	
	/**
	 * Checks if two itemstacks has same ID, size and metadata (from stack itself)
	 */
	public static boolean isItemStackTrueEqual(ItemStack stack1, ItemStack stack2) {
		return (stack1 != null && stack2 != null && stack1.getItem() == stack2.getItem() && stack1.stackSize == stack2.stackSize && getTrueDamage(stack1) == getTrueDamage(stack2));
	}
	
	/**
	 * Checks if two itemstacks has same ID and metadata (from stack itself)
	 */
	public static boolean isItemStackTrueEqualData(ItemStack stack1, ItemStack stack2) {
		return (stack1 != null && stack2 != null && stack1.getItem() == stack2.getItem() && getTrueDamage(stack1) == getTrueDamage(stack2));
	}
	
	/**
	 * Removes itemstack with NBT from inventory 
	 * @param inventory Inventory
	 * @param stack ItemStack to remove
	 * @return If the stack was removed
	 */
	public static boolean consumeItemStackNBT(IInventory inventory, ItemStack stack) {
	   if(getAmountNBT(inventory, stack) >= stack.stackSize) {
		  for (int i = 0; i < inventory.getSizeInventory(); i++) {
			 if(isItemStackEqualNBT(inventory.getStackInSlot(i), stack)){
				int amount = Math.min(stack.stackSize, inventory.getStackInSlot(i).stackSize);
				if(amount > 0) {
					inventory.decrStackSize(i, amount);
					stack.stackSize -= amount;
				}
				if(stack.stackSize <= 0) {
				   return true;
				}
			 }
		  }
	   }
	   return false;
	}

	/**
	 * Returns the amount of items in stack with NBT from inventory
	 * @param inventory Inventory
	 * @param stack Stack to compare item
	 * @return Amount
	 */
	public static int getAmountNBT(IInventory inventory, ItemStack stack) {
	   int amount = 0;
	   for (int i = 0; i < inventory.getSizeInventory(); i++) {
		  if(isItemStackEqualNBT(inventory.getStackInSlot(i), stack)) {
			 amount += inventory.getStackInSlot(i).stackSize;
		  }
	   }
	   return amount;
	}
	
	/**
	 * Checks if two itemstacks has same ID, metadata and NBT
	 * @param stack1 First itemstack
	 * @param stack2 Second itemstack
	 */
	public static boolean isItemStackEqualNBT(ItemStack stack1, ItemStack stack2) {
	   if(stack1 != null && stack2 != null && stack1.getItem() == stack2.getItem() && stack1.getItemDamage() == stack2.getItemDamage()) {
		  if(!stack1.hasTagCompound() && !stack2.hasTagCompound()) {
			 return true;
		  } else if(stack1.hasTagCompound() != stack2.hasTagCompound()) {
			 return false;
		  } else if(stack1.stackTagCompound.equals(stack2.stackTagCompound)) {
			 return true;
		  }
	   }
	   return false;
	}
	
	/**
	 * Changes itemstack's item
	 * @param stack Stack to change its item
	 * @param item Item to set in {@code stack}
	 * */
	public static ItemStack changeStackItem(ItemStack stack, Item item) {
		ItemStack newStack = new ItemStack(item, stack.stackSize, stack.getItemDamage());
		newStack.stackTagCompound = stack.stackTagCompound;
		return newStack;
	}
	
	/**
	 * Removes <b>block</b> from GameRegistry
	 * */
	/*public static void unregisterBlock(Block block) {
		
	}*/
	
	/**
	 * Removes <b>item</b> from GameData
	 * */
	/*public static void unregisterItem(Item item) {
		try {
			{ // Unregistering ID
				ObjectIntIdentityMap underlyingIntegerMap = (ObjectIntIdentityMap) ReflectionHelper.findField(RegistryNamespaced.class, "underlyingIntegerMap").get(GameData.getItemRegistry());
				IdentityHashMap field_148749_a = (IdentityHashMap) ReflectionHelper.findField(IdentityHashMap.class, "field_148749_a").get(underlyingIntegerMap);
				List field_148748_b = (List) ReflectionHelper.findField(ObjectIntIdentityMap.class, "field_148748_b").get(underlyingIntegerMap);
				if (field_148749_a.containsKey(item) && field_148749_a.containsValue(Integer.valueOf(Item.getIdFromItem(item)))) field_148749_a.remove(item); else throw new NullPointerException("IdentityHashMap doesn't contains " + item.toString());
				if (field_148748_b.contains(item)) field_148748_b.set(Item.getIdFromItem(item), (Object)null); else throw new NullPointerException("List doesn't contains " + item.toString());
			}
			
			{ // Unregistering Item
				Map registryObjects = (Map) ReflectionHelper.findField(RegistryNamespaced.class, "field_148758_b").get(GameData.getItemRegistry());
				String name = GameData.getItemRegistry().getNameForObject(item);
				if (registryObjects.containsKey(name) && registryObjects.containsValue(item)) registryObjects.replace(name, item, (Object)null); else throw new NullPointerException("Map doesn't contains " + item.toString());
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}*/

	/**
	 * Adds new recipe with {@link OreDictionary} support
	 * */
	public static void addOreDictRecipe(ItemStack output, Object... recipe) {
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(output, recipe));
	}

	/**
	 * Adds new shapeless recipe with {@link OreDictionary} support
	 * */
	public static void addShapelessOreDictRecipe(ItemStack output, Object... recipe) {
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(output, recipe));
	}
	
	/**
	 * Removes recipe of {@code resultItem}
	 * @param resultItem Stack to remove recipe
	 * @author Code by yope_fried, inspired by pigalot, provided by Develance on forum.mcmodding.ru
	 * */
	public static void removeRecipe(ItemStack resultItem) {
		List recipes = CraftingManager.getInstance().getRecipeList();
		
		for (int scan = 0; scan < recipes.size(); scan++) {
			if (ItemStack.areItemStacksEqual(resultItem, ((IRecipe) recipes.get(scan)).getRecipeOutput())) {
				FMLRelaunchLog.log("ASJLib", Level.INFO, "Removed Recipe: " + recipes.get(scan) + " -> " + ((IRecipe) recipes.get(scan)).getRecipeOutput());
				recipes.remove(scan);
			}
		}
	}
	
	/**
	 * Checks whether {@code e1} is in FOV of {@code e2}
	 * @author a_dizzle (minecraftforum.net)
	 */
	public static boolean isInFieldOfVision(EntityLivingBase e1, EntityLivingBase e2){
	    //save Entity 2's original rotation variables
	    float rotationYawPrime = e2.rotationYaw;
	    float rotationPitchPrime = e2.rotationPitch;
	    //make Entity 2 directly face Entity 1
	    faceEntity(e2, e1, 360F, 360F);
	    //switch values of prime rotation variables with current rotation variables
	    float f = e2.rotationYaw;
	    float f1 = e2.rotationPitch;
	    e2.rotationYaw = rotationYawPrime;
	    e2.rotationPitch = rotationPitchPrime;
	    rotationYawPrime = f;
	    rotationPitchPrime = f1;
	    float X = 60F; //this is only a guess, I don't know the actual range
	    float Y = 60F; //this is only a guess, I don't know the actual range
	    float yawFOVMin = e2.rotationYaw - X;
	    float yawFOVMax = e2.rotationYaw + X;
	    float pitchFOVMin = e2.rotationPitch - Y;
	    float pitchFOVMax = e2.rotationPitch + Y;
	    boolean flag1 = (yawFOVMin < 0F && (rotationYawPrime >= yawFOVMin + 360F || rotationYawPrime <= yawFOVMax)) || (yawFOVMax >= 360F && (rotationYawPrime <= yawFOVMax - 360F || rotationYawPrime >= yawFOVMin)) || (yawFOVMax < 360F && yawFOVMin >= 0F && rotationYawPrime <= yawFOVMax && rotationYawPrime >= yawFOVMin);
	    boolean flag2 = (pitchFOVMin <= -180F && (rotationPitchPrime >= pitchFOVMin + 360F || rotationPitchPrime <= pitchFOVMax)) || (pitchFOVMax > 180F && (rotationPitchPrime <= pitchFOVMax - 360F || rotationPitchPrime >= pitchFOVMin)) || (pitchFOVMax < 180F && pitchFOVMin >= -180F && rotationPitchPrime <= pitchFOVMax && rotationPitchPrime >= pitchFOVMin);
	    if(flag1 && flag2 && e2.canEntityBeSeen(e1)) return true;
	    else return false;
	}
	
	/** 
	 * Makes {@code e1} to face {@code e2}
	 */
	public static void faceEntity(EntityLivingBase e1, Entity e2, float yaw, float pitch) {
        double d0 = e2.posX - e1.posX;
        double d2 = e2.posZ - e1.posZ;
        double d1;

        if (e2 instanceof EntityLivingBase) {
            EntityLivingBase entitylivingbase = (EntityLivingBase) e2;
            d1 = entitylivingbase.posY + (double)entitylivingbase.getEyeHeight() - (e1.posY + (double)e1.getEyeHeight());
		} else {
            d1 = (e2.boundingBox.minY + e2.boundingBox.maxY) / 2.0D - (e1.posY + (double)e1.getEyeHeight());
        }

        double d3 = (double)MathHelper.sqrt_double(d0 * d0 + d2 * d2);
        float f2 = (float)(Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
        float f3 = (float)(-(Math.atan2(d1, d3) * 180.0D / Math.PI));
        e1.rotationPitch = updateRotation(e1.rotationPitch, f3, pitch);
        e1.rotationYaw = updateRotation(e1.rotationYaw, f2, yaw);
    }
	
	public static float updateRotation(float f, float f1, float f2) {
        float f3 = MathHelper.wrapAngleTo180_float(f1 - f);
        if (f3 > f2) f3 = f2;
        if (f3 < -f2) f3 = -f2;
        return f + f3;
    }
	
	/**
	 * Returns MOP with block and entity
	 * @param entity Entity to calculate vector from
	 * @param dist Max distance for use
	 * @param interact Whether to get uncollidable entities
	 * @author timaxa007
	 */
	public static MovingObjectPosition getMouseOver(EntityLivingBase entity, double dist, boolean interact) {
		if (entity == null || entity.worldObj == null) {
			return null;
		}
		
		Entity pointedEntity = null;
		double d0 = dist;
		double d1 = d0;
		Vec3 vec3 = Vec3.createVectorHelper(entity.posX, (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT ? entity.posY : entity.posY + entity.getEyeHeight()), entity.posZ);
		Vec3 vec31 = entity.getLookVec();
		Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
		Vec3 vec33 = null;
		MovingObjectPosition objectMouseOver = rayTrace(entity, dist);

		if (objectMouseOver != null) {
			d1 = objectMouseOver.hitVec.distanceTo(vec3);
		}

		float f1 = 1.0F;
		List list = entity.worldObj.getEntitiesWithinAABBExcludingEntity(entity, entity.boundingBox.addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand((double)f1, (double)f1, (double)f1));
		double d2 = d1;

		for (int i = 0; i < list.size(); ++i) {
			Entity e = (Entity)list.get(i);

			if (e.canBeCollidedWith() || interact) {
				float f2 = e.getCollisionBorderSize();
				AxisAlignedBB axisalignedbb = e.boundingBox.expand((double)f2, (double)f2, (double)f2);
				MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

				if (axisalignedbb.isVecInside(vec3)) {
					if (0.0 < d2 || d2 == 0.0) {
						pointedEntity = e;
						vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
						d2 = 0.0;
					}
				} else if (movingobjectposition != null) {
					double d3 = vec3.distanceTo(movingobjectposition.hitVec);

					if (d3 < d2 || d2 == 0.0) {
						if (e == entity.ridingEntity && !e.canRiderInteract()) {
							if (d2 == 0.0) {
								pointedEntity = e;
								vec33 = movingobjectposition.hitVec;
							}
						} else {
							pointedEntity = e;
							vec33 = movingobjectposition.hitVec;
							d2 = d3;
						}
					}
				}
			}
		}

		if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
			return new MovingObjectPosition(pointedEntity, vec33);
		}
		return getSelectedBlock(entity, dist, interact);
	}
	
	/**
	 * Raytracer for 'getMouseOver' method.<br>
	 * Don't use it. Use {@link #getMouseOver(EntityLivingBase, float, double, boolean) getMouseOver} instead
	 */
	private static MovingObjectPosition rayTrace(EntityLivingBase entity, double dist) {
		Vec3 vec3 = Vec3.createVectorHelper(entity.posX, (ASJUtilities.isServer() ? entity.posY + entity.getEyeHeight() : entity.posY), entity.posZ);
		Vec3 vec31 = entity.getLookVec();
		Vec3 vec32 = vec3.addVector(vec31.xCoord * dist, vec31.yCoord * dist, vec31.zCoord * dist);
		return entity.worldObj.func_147447_a(vec3, vec32, false, false, true);
	}
	
	/**
	 * Returns MOP with only blocks.
	 * @param entity Player to calculate vector from
	 * @param dist Max distance for use
	 * @param interact Can player interact with blocks (not sure)
	 */
	public static MovingObjectPosition getSelectedBlock(EntityLivingBase entity, double dist, boolean interact) {
		Vec3 vec3 = getPosition(entity, 1.0F);
		vec3.yCoord += entity.getEyeHeight();
		Vec3 vec31 = entity.getLookVec();
		Vec3 vec32 = vec3.addVector(vec31.xCoord * dist, vec31.yCoord * dist, vec31.zCoord * dist);
		MovingObjectPosition movingobjectposition = entity.worldObj.rayTraceBlocks(vec3, vec32, interact);
		return movingobjectposition;
	}
	
	/**
	 * Interpolated position vector
	 */
	public static Vec3 getPosition(EntityLivingBase living, float par1) {
		float i = living instanceof EntityPlayer ? ((EntityPlayer) living).getDefaultEyeHeight() : 0;
		if (par1 == 1.0F) {
			return Vec3.createVectorHelper(living.posX, living.posY + (living.getEyeHeight() - i), living.posZ);
		} else {
			double d0 = living.prevPosX + (living.posX - living.prevPosX) * (double)par1;
			double d1 = living.prevPosY + (living.posY - living.prevPosY) * (double)par1 + (living.getEyeHeight() - i);
			double d2 = living.prevPosZ + (living.posZ - living.prevPosZ) * (double)par1;
			return Vec3.createVectorHelper(d0, d1, d2);
		}
	}
	
    public static Vec3 getLookVec(Entity e) {
        float f1 = MathHelper.cos(-e.rotationYaw * 0.017453292F - (float)Math.PI);
        float f2 = MathHelper.sin(-e.rotationYaw * 0.017453292F - (float)Math.PI);
        float f3 = -MathHelper.cos(-e.rotationPitch * 0.017453292F);
        float f4 = MathHelper.sin(-e.rotationPitch * 0.017453292F);
        return Vec3.createVectorHelper(f2 * f3, f4, f1 * f3);
    }
	
	
	/**
	 * @return Closest vulnerable player to entity within the given radius,
	 * ignoring invisibility and sneaking<br>
	 * Can be null if none is found
	 */
	public static EntityPlayer getClosestVulnerablePlayerToEntity(Entity entity, double distance) {
		return getClosestVulnerablePlayer(entity.worldObj, entity.posX, entity.posY, entity.posZ, distance);
	}

	/**
	 * @return Closest vulnerable player to coords within the given radius,
	 * ignoring invisibility and sneaking<br>
	 * Can be null if none is found
	 */
	public static EntityPlayer getClosestVulnerablePlayer(World world, double x, double y, double z, double distance) {
		double prevDist = -1.0;
		EntityPlayer entityplayer = null;

		for (int i = 0; i < world.playerEntities.size(); ++i) {
			EntityPlayer entityplayer1 = (EntityPlayer) world.playerEntities.get(i);

			if (!entityplayer1.capabilities.disableDamage && entityplayer1.isEntityAlive()) {
				double dist = entityplayer1.getDistanceSq(x, y, z);

				if ((distance < 0.0 || dist < distance * distance) && (prevDist == -1.|| dist < prevDist)) {
					prevDist = dist;
					entityplayer = entityplayer1;
				}
			}
		}

		return entityplayer;
	}
	
	/**
	 * Registers new entity
	 * @param entityClass Entity's class file
	 * @param name The name of this entity
	 */
	public static void registerEntity(Class<? extends Entity> entityClass, String name, Object instance){
		int ID = EntityRegistry.findGlobalUniqueEntityId();
		name = FMLCommonHandler.instance().findContainerFor(instance).getModId() + ":" + name;
		EntityRegistry.registerGlobalEntityID(entityClass, name, ID);
		EntityRegistry.registerModEntity(entityClass, name, ID, instance, 128, 1, true);
	}
	
	/**
	 * Registers new entity with egg
	 * @param entityClass Entity's class file
	 * @param name The name of this entity
	 * @param backColor Background egg color
	 * @param frontColor The color of dots
	 */
	public static void registerEntityEgg(Class<? extends Entity> entityClass, String name, int backColor, int frontColor, Object instance){
		int ID = EntityRegistry.findGlobalUniqueEntityId();
		name = FMLCommonHandler.instance().findContainerFor(instance).getModId() + ":" + name;
		EntityRegistry.registerGlobalEntityID(entityClass, name, ID);
		EntityRegistry.registerModEntity(entityClass, name, ID, instance, 128, 1, true);
		EntityList.entityEggs.put(Integer.valueOf(ID), new EntityList.EntityEggInfo(ID, backColor, frontColor));
	}

	/**
	 * Changes the biome at given coordinates, currently really buggy
	 * @param world This World
	 * @param x -Coordinate
	 * @param z -Coordinate
	 * @param boime The biome to set at this location
	 * */
	@Deprecated
	public static void setBiomeAt(World world, int x, int z, BiomeGenBase biome) {
		if (biome == null) {
			return;
		}
		Chunk chunk = world.getChunkFromBlockCoords(x, z);
		byte[] array = chunk.getBiomeArray();
		array[((z & 0xF) << 4 | x & 0xF)] = ((byte) (biome.biomeID & 0xFF));
		chunk.setBiomeArray(array);
	}
	
	private static final Field isBadEffect;
	
	static {
		isBadEffect = ASJReflectionHelper.getField(Potion.class, "isBadEffect", "field_76418_K", "f");
		isBadEffect.setAccessible(true);
	}
	
	/**
	 * Determines whether this potion is bad or not
	 * */
	public static boolean isBadPotion(Potion effect) {
		return ASJReflectionHelper.getValue(isBadEffect, effect, false);
	}
	
	/**
	 * @return random value in range [min, max] (inclusive)
	 * */
	public static int randInBounds(int min, int max) {
		return randInBounds(new Random(), min, max);
	}
	
	/**
	 * @return random value in range [min, max] (inclusive)
	 * */
	public static int randInBounds(Random rand, int min, int max) {
		return rand.nextInt(max - min + 1) + min; 
	}
	
	/**
	 * Interpolates values, e.g. for smoother render
	 * */
	public static double interpolate(double last, double now) {
		return last + (now - last) * Minecraft.getMinecraft().timer.renderPartialTicks;
	}
	
	/**
	 * Translates matrix to follow player (if something is bound to world's zero coords)
	 * */
	public static void interpolatedTranslation(Entity entity) {
		glTranslated(interpolate(entity.lastTickPosX, entity.posX), interpolate(entity.lastTickPosY, entity.posY), interpolate(entity.lastTickPosZ, entity.posZ));
	}
	
	/**
	 * Translates matrix not to follow player (if something is bound to camera's zero coords)
	 * */
	public static void interpolatedTranslationReverse(Entity entity) {
		glTranslated(-interpolate(entity.lastTickPosX, entity.posX), -interpolate(entity.lastTickPosY, entity.posY), -interpolate(entity.lastTickPosZ, entity.posZ));
	}
	
	/**
	 * Sets matrix and translation to world's zero coordinates
	 * so you can render something as in TileEntitySpecialRenderer (if you are used to it)
	 * Don't forget to call {@link #postRenderISBRH}
	 * Use this before your render something in ISimpleBlockRenderingHandler
	 * */
	public static void preRenderISBRH(int x, int y, int z) {
		int X = (x / 16 - (x < 0 && x % 16 != 0 ? 1 : 0)) * -16;
		int Z = (z / 16 - (z < 0 && z % 16 != 0 ? 1 : 0)) * -16;
		Tessellator.instance.draw();
		Tessellator.instance.setTranslation(0, 0, 0);
		glPushMatrix();
		glTranslated(X, 0, Z);
	}
	
	/**
	 * This gets everything back for other blocks to render properly
	 * Don't use unless you've used {@link #preRenderISBRH}
	 * Use this after your render something in ISimpleBlockRenderingHandler
	 * */
	public static void postRenderISBRH(int x, int y, int z) {
		int X = (x / 16 - (x < 0 && x % 16 != 0 ? 1 : 0)) * -16;
		int Z = (z / 16 - (z < 0 && z % 16 != 0 ? 1 : 0)) * -16;
		glPopMatrix();
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.setTranslation(X, 0, Z);
	}
	
	/**
	 * @return String which tolds you to hold shift-key
	 * */
	public static String holdShift() {
		return (StatCollector.translateToLocal("tooltip.hold") + EnumChatFormatting.WHITE + " SHIFT " + EnumChatFormatting.GRAY + StatCollector.translateToLocal("tooltip.shift"));
	}
	
	/**
	 * @return String which tolds you to hold control-key
	 * */
	public static String holdCtrl() {
		return (StatCollector.translateToLocal("tooltip.hold") + EnumChatFormatting.WHITE + " CTRL " + EnumChatFormatting.GRAY + StatCollector.translateToLocal("tooltip.ctrl"));
	}
	
	/**
	 * @return String which tolds you that this block/item/stuff is only for creative use
	 * */
	public static String creativeOnly() {
		return StatCollector.translateToLocal("tooltip.creativeonly");
	}

	/**
	 * @return map key for specified value if persist (null if none)
	 * */
	public static <K, V> K mapGetKey(Map<K, V> map, V v) {
		for (Map.Entry<K, V> e : map.entrySet()) if (e.getValue().equals(v)) return e.getKey();
		return null;
	}
	
	/**
	 * @return map key for specified value if persist (default if none)
	 * */
	public static <K, V> K mapGetKeyOrDefault(Map<K, V> map, V v, K def) {
		for (Map.Entry<K, V> e : map.entrySet()) if (e.getValue().equals(v)) return e.getKey();
		return def;
	}
	
	public static <T extends Comparable<T>> int indexOfComparableArray(T[] array, T key) {
		for (int i = 0; i < array.length; i++)
			if (array[i].compareTo(key) == 0)
				return i;
		return -1;
	}
	
	public static <T extends Comparable<T>> int indexOfComparableColl(Collection<T> coll, T key) {
		int id = -1;
		for (T t : coll) {
			++id;
			if (t.compareTo(key) == 0)
				return id;
		}
		return id;
	}
	
	public static int[] colorCode = new int[32];
	
	static {
		for (int i = 0; i < 32; ++i) {
			int j = (i >> 3 & 1) * 85;
			int k = (i >> 2 & 1) * 170 + j;
			int l = (i >> 1 & 1) * 170 + j;
			int i1 = (i >> 0 & 1) * 170 + j;

			if (i == 6) {
				k += 85;
			}

			if (i >= 16) {
				k /= 4;
				l /= 4;
				i1 /= 4;
			}

			colorCode[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
		}
	}

	/**
	 * @return enum color packed in uInt with max alpha
	 * @author qiexie
	 * */
	public static int enumColorToRGB(EnumChatFormatting eColor) {
		return addAlpha(colorCode[eColor.ordinal()], 0xff); 
	}
	
	/**
	 * Adds {@code alpha} value to @{code color}
	 * */
	public static int addAlpha(int color, int alpha) { 
		return ((alpha & 0xFF) << 24) | (color & 0x00FFFFFF); 
	}
	
	/**
	 * Sets render color unpacked from uInt
	 * */
	public static void glColor1u(int color) {
		glColor4ub((byte) (color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte) (color & 0xFF), (byte) (color >> 24 & 0xFF));
	}
	
	/**
	 * Registers IIcon for block. Call this in preInit
	 * */
	public static void registerBlockIcon(String name) {
		blockIconsNames.add(name);
	}

	/**
	 * Registers IIcon for item. Call this in preInit
	 * */
	public static void registerItemIcon(String name) {
		itemsIconsNames.add(name);
	}
	
	/**
	 * Returns block IIcon registered with {@link #registerBlockIcon} 
	 * */
	public static IIcon getBlockIcon(String name) {
		return blockIcons.get(name);
	}
	
	/**
	 * Returns item IIcon registered with {@link #registerItemIcon} 
	 * */
	public static IIcon getItemIcon(String name) {
		return itemsIcons.get(name);
	}
	
	/**
	 * Registers dimension
	 * @param keepLoaded Keep spawn chunks loaded
	 * */
	public static void registerDimension(int id, Class<? extends WorldProvider> w, boolean keepLoaded) {
		if (!DimensionManager.registerProviderType(id, w, keepLoaded)) throw new IllegalArgumentException(String.format("Failed to register provider for id %d, One is already registered", id));
		DimensionManager.registerDimension(id, id);
	}
	
	/**
	 * Generates <b>ore</b> into the world</br>
	 * Args: block, it's meta, block to replace, world, rng, x, z positions,
	 * min, max blocks in one place, min, max block groups in chunk,
	 * chance to be generated, min, max Y-level
	 * */
	public static void generateOre(Block ore, int meta, Block replace, World world, Random rand, int blockXPos, int blockZPos, int minVeinSize, int maxVeinSize, int minVeinsPerChunk, int maxVeinsPerChunk, int chanceToSpawn, int minY, int maxY) {
		if (rand.nextInt(101) > chanceToSpawn) return;
		int veins = rand.nextInt(maxVeinsPerChunk - minVeinsPerChunk + 1) + minVeinsPerChunk;
		for (int i = 0; i < veins; i++) {
			int posX = blockXPos + rand.nextInt(16) + 8;
			int posY = minY + rand.nextInt(maxY - minY);
			int posZ = blockZPos + rand.nextInt(16) + 8;
			(new WorldGenMinable(ore, meta, minVeinSize + rand.nextInt(maxVeinSize - minVeinSize + 1), replace)).generate(world, rand, posX, posY, posZ);
		}
	}
	
	/**
	 * Fills holes of worldgen (no more structures in mid-air)
	 * Args: world, filler, x start, x end, upper height, z start, z end
	 * @param radius Radius of cylinder-shaped structure's base (0 for square)
	 * */
	public static void fillGenHoles(World world, Block filler, int meta, int xmn, int xmx, int ystart, int zmn, int zmx, int radius) {
		if (xmn < -29999999 || xmx > 29999999 || ystart < 0 || ystart > 255 || zmn < -29999999 || zmx > 29999999 || radius < 0) return;
		for (int i = xmn; i <= xmx; i++) {
			for (int k = zmn; k <= zmx; k++) {
				for (int j = ystart - 1; j >= 0 && isBlockReplaceable(world.getBlock(i, j, k)); j--) {
					if (radius != 0) if (Math.sqrt(Math.pow((((xmx - xmn) / 2) + xmn) - i, 2) + Math.pow((((zmx - zmn) / 2) + zmn) - k, 2)) > radius) continue;
					world.setBlock(i, j, k, filler, meta, 3);
				}
			}	
		}
	}
	
	public static boolean isBlockReplaceable(Block block) {
		return	block == Blocks.air						||
				block == Blocks.snow_layer				||
				block.getMaterial() == Material.air		||
				block.getMaterial() == Material.cactus	||
				block.getMaterial() == Material.coral	||
				block.getMaterial() == Material.fire	||
				block.getMaterial() == Material.gourd	||
				block.getMaterial() == Material.leaves	||
				block.getMaterial() == Material.lava	||
				block.getMaterial() == Material.plants	||
				block.getMaterial() == Material.vine	||
				block.getMaterial() == Material.water	||
				block.getMaterial() == Material.web;
	}

	private static DecimalFormat format = new DecimalFormat("000");
	
	private static String time(World world) {
		return String.format("[%s] ", format.format(world.getTotalWorldTime() % 1000));
	}
	
	@SideOnly(Side.CLIENT)
	public static void chatLog(String message) {
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(time(Minecraft.getMinecraft().theWorld) + message));
	}
	
	@SideOnly(Side.CLIENT)
	public static void chatLog(String message, World world) {
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(time(world) + (world.isRemote ? "[C] " : "[S] ") + message));
	}
	
	public static void chatLog(String message, EntityPlayer player) {
		player.addChatMessage(new ChatComponentText(time(player.worldObj) + (player.worldObj.isRemote ? "[C] " : "[S] ") + message));
	}
	
	public static void log(String message) { 
		FMLRelaunchLog.log(Loader.instance().activeModContainer().getModId().toUpperCase(), Level.INFO, message);
	}
	
	public static void debug(String message) { 
		FMLRelaunchLog.log(Loader.instance().activeModContainer().getModId().toUpperCase(), Level.DEBUG, message);
	}
	
	public static void warn(String message) { 
		FMLRelaunchLog.log(Loader.instance().activeModContainer().getModId().toUpperCase(), Level.WARN, message);
	}
	
	public static void error(String message) { 
		FMLRelaunchLog.log(Loader.instance().activeModContainer().getModId().toUpperCase(), Level.ERROR, message);
	}
	
	private static void trace(String message) { 
		FMLRelaunchLog.log(Loader.instance().activeModContainer().getModId().toUpperCase(), Level.TRACE, message);
	}

	public static void printStackTrace() {
		ASJUtilities.log("Stack trace: ");
		StackTraceElement[] stes = Thread.currentThread().getStackTrace();
		for (int i = 2; i < stes.length; i++) ASJUtilities.trace("\tat " + stes[i].toString());
	}
	
	public static void say(EntityPlayer player, String message) {
		player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal(message)));
	}
	
	public static void sayToAllOnline(String message) {
		List<EntityPlayer> list = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		for (EntityPlayer online : list) say(online, message);
		log(message);
	}
	
	/** Untested! */
	@Deprecated
	public static void sayToAllOPs(String message) {
		for (String op : MinecraftServer.getServer().getConfigurationManager().func_152606_n()) {
			EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(op);
			if (player != null) say(player, message);
		}
		log(message);
	}

	public static boolean isServer() {
		return FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER;
	}
	
	public static String toString(NBTTagCompound nbt) {
		StringBuilder sb = new StringBuilder("{\n");
		for (Object o : nbt.tagMap.entrySet()) {
			Map.Entry e = (Map.Entry) o;
			Object v = e.getValue();
			if (v instanceof NBTTagList || v instanceof NBTTagCompound) {
				String[] arr = new String[] {};
				if (v instanceof NBTTagList) arr = toString((NBTTagList) v).split("\n");
				else if (v instanceof NBTTagCompound) arr = toString((NBTTagCompound) v).split("\n");
				sb.append("    ").append(e.getKey()).append(" = ").append(arr[0]).append("\n");
				for (int i = 1; i < arr.length; i++) sb.append("    ").append(arr[i]).append("\n");
			} else sb.append("    ").append(e.getKey()).append(" = ").append(e.getValue()).append("\n");
		}
		sb.append("}");
		return sb.toString();
	}
	
	public static String toString(NBTTagList nbt) {
		StringBuilder sb = new StringBuilder("list [\n");
		for (Object obj : nbt.tagList) if (obj instanceof NBTTagList || obj instanceof NBTTagCompound) {
			String ts = "";
			if (obj instanceof NBTTagList) ts = toString((NBTTagList) obj);
			else if (obj instanceof NBTTagCompound) ts = toString((NBTTagCompound) obj);
			for (String s : ts.split("\n")) sb.append("    ").append(s).append("\n"); 
		} else sb.append(obj).append("\n");
		sb.append("]");
		return sb.toString();
	}
}