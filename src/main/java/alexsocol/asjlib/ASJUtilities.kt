package alexsocol.asjlib

import cpw.mods.fml.common.*
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.registry.*
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.*
import net.minecraft.entity.player.*
import net.minecraft.init.Blocks
import net.minecraft.inventory.IInventory
import net.minecraft.item.*
import net.minecraft.item.crafting.*
import net.minecraft.nbt.*
import net.minecraft.potion.Potion
import net.minecraft.server.MinecraftServer
import net.minecraft.util.*
import net.minecraft.world.*
import net.minecraft.world.biome.BiomeGenBase
import net.minecraft.world.gen.feature.WorldGenMinable
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.*
import net.minecraftforge.event.entity.living.*
import net.minecraftforge.oredict.*
import org.apache.logging.log4j.Level
import org.lwjgl.opengl.GL11.*
import java.text.DecimalFormat
import java.util.*
import kotlin.math.*

/**
 * Small utility lib to help with some tricks. Feel free to use it in your mods.
 * @author AlexSocol
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class ASJUtilities {
	
	/**
	 * This will automatically register icons from lists
	 */
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	fun onTextureStitchEvent(event: TextureStitchEvent.Pre) {
		when (event.map.textureType) {
			0 -> for (s in blockIconsNames) blockIcons[s] = event.map.registerIcon(s)
			1 -> for (s in itemsIconsNames) itemsIcons[s] = event.map.registerIcon(s)
		}
	}
	
	companion object {
		
		private val INSTANCE = ASJUtilities()
		private val blockIconsNames = ArrayList<String>()
		private val itemsIconsNames = ArrayList<String>()
		private val blockIcons = HashMap<String, IIcon>()
		private val itemsIcons = HashMap<String, IIcon>()
		
		// Registers this like event handler to load icons
		init {
			MinecraftForge.EVENT_BUS.register(INSTANCE)
		}
		
		/**
		 * Returns the name of the block
		 * @param block Block to get name from
		 */
		@JvmStatic
		fun getBlockName(block: Block) = block.unlocalizedName.substring(5)
		
		/**
		 * Returns the name of the item
		 * @param item Item to get name from
		 */
		@JvmStatic
		fun getItemName(item: Item) = item.unlocalizedName.substring(5)
		
		/**
		 * Registers block by name
		 * @param block Block to register
		 */
		@JvmStatic
		fun register(block: Block) = GameRegistry.registerBlock(block, getBlockName(block))!!
		
		/**
		 * Registers item by name
		 * @param item Item to register
		 */
		@JvmStatic
		fun register(item: Item) = GameRegistry.registerItem(item, getItemName(item))
		
		/**
		 * Returns String ID of the mod this block/item is registered in
		 * @param stack ItemStack with block/item for analysis
		 */
		@JvmStatic
		fun getModId(stack: ItemStack): String {
			val id = GameRegistry.findUniqueIdentifierFor(stack.item)
			return if (id == null || id.modId == "") "minecraft" else id.modId
		}
		
		/**
		 * Sends entity to dimension without portal frames
		 * @param entity The entity to send
		 * @param dimTo ID of the dimension the entity should be sent to
		 */
		@JvmStatic
		fun sendToDimensionWithoutPortal(entity: Entity, dimTo: Int, x: Double, y: Double, z: Double) {
			entity.ridingEntity?.riddenByEntity = null
			entity.ridingEntity = null
			
			if (dimTo == entity.dimension) {
				if (entity is EntityLivingBase)
					entity.setPositionAndUpdate(x, y, z)
				else
					entity.setPosition(x, y, z)
			} else if (entity is EntityPlayerMP) {
				val worldTo = entity.mcServer.worldServerForDimension(dimTo)
				entity.mcServer.configurationManager.transferPlayerToDimension(entity, dimTo, FreeTeleporter(worldTo, x, y, z))
			}
		}
		
		@JvmStatic
		fun willEntityDie(event: LivingHurtEvent) =
			willEntityDie(LivingAttackEvent(event.entityLiving, event.source, event.ammount))
		
		/**
		 * Determines whether entity will die from next hit
		 * @param event Some event fired when entity's HP decreases
		 */
		@JvmStatic
		fun willEntityDie(event: LivingAttackEvent): Boolean {
			var amount = event.ammount
			val source = event.source
			val living = event.entityLiving
			if (!source.isUnblockable) {
				val armor = 25 - living.totalArmorValue
				amount = amount * armor / 25.0f
			}
			if (!source.isDamageAbsolute && living.isPotionActive(Potion.resistance)) {
				val resistance = 25 - (living.getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5
				amount = amount * resistance / 25.0f
			}
			return ceil(amount) >= floor(living.health)
		}
		
		/**
		 * Returns the number of the slot with item matching to item passed in
		 * @param item The item to compare
		 * @param inventory The inventory to scan
		 */
		@JvmStatic
		fun getSlotWithItem(item: Item, inventory: IInventory) =
			(0 until inventory.sizeInventory).firstOrNull { inventory.getStackInSlot(it) != null && inventory.getStackInSlot(it).item === item } ?: -1
		
		/**
		 * Removes itemstack from inventory
		 * @param inventory Inventory
		 * @param stack ItemStack to remove
		 * @return If the stack was removed
		 */
		@JvmStatic
		fun consumeItemStack(inventory: IInventory, stack: ItemStack): Boolean {
			if (getAmount(inventory, stack) >= stack.stackSize) {
				for (i in 0 until inventory.sizeInventory) {
					if (isItemStackEqualData(inventory.getStackInSlot(i), stack)) {
						val amount = min(stack.stackSize, inventory.getStackInSlot(i).stackSize)
						if (amount > 0) {
							inventory.decrStackSize(i, amount)
							stack.stackSize -= amount
						}
						if (stack.stackSize <= 0) {
							return true
						}
					}
				}
			}
			return false
		}
		
		/**
		 * Returns the amount of item from itemstack in inventory
		 * @param inventory Inventory
		 * @param stack Stack to compare item
		 * @return Amount
		 */
		@JvmStatic
		fun getAmount(inventory: IInventory, stack: ItemStack): Int {
			var amount = 0
			for (i in 0 until inventory.sizeInventory) {
				if (isItemStackEqualData(inventory.getStackInSlot(i), stack)) {
					amount += inventory.getStackInSlot(i).stackSize
				}
			}
			return amount
		}
		
		/**
		 * @return damage from stack itself, not through item
		 */
		@JvmStatic
		fun getTrueDamage(stack: ItemStack) =
			"$stack".split("@".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].toInt()
		
		/**
		 * Checks if two itemstacks has same ID, size and metadata
		 */
		@JvmStatic
		fun isItemStackEqual(stack1: ItemStack?, stack2: ItemStack?) =
			stack1 != null && stack2 != null && stack1.item === stack2.item && stack1.stackSize == stack2.stackSize && stack1.itemDamage == stack2.itemDamage
		
		/**
		 * Checks if two itemstacks has same ID and metadata
		 */
		@JvmStatic
		fun isItemStackEqualData(stack1: ItemStack?, stack2: ItemStack?) =
			stack1 != null && stack2 != null && stack1.item === stack2.item && stack1.itemDamage == stack2.itemDamage
		
		/**
		 * Checks if two itemstacks has same ID, size and metadata (from stack itself)
		 */
		@JvmStatic
		fun isItemStackTrueEqual(stack1: ItemStack?, stack2: ItemStack?) =
			stack1 != null && stack2 != null && stack1.item === stack2.item && stack1.stackSize == stack2.stackSize && getTrueDamage(stack1) == getTrueDamage(stack2)
		
		/**
		 * Checks if two itemstacks has same ID and metadata (from stack itself)
		 */
		@JvmStatic
		fun isItemStackTrueEqualData(stack1: ItemStack?, stack2: ItemStack?) =
			stack1 != null && stack2 != null && stack1.item === stack2.item && getTrueDamage(stack1) == getTrueDamage(stack2)
		
		/**
		 * Removes itemstack with NBT from inventory
		 * @param inventory Inventory
		 * @param stack ItemStack to remove
		 * @return If the stack was removed
		 */
		@JvmStatic
		fun consumeItemStackNBT(inventory: IInventory, stack: ItemStack): Boolean {
			if (getAmountNBT(inventory, stack) >= stack.stackSize) {
				for (i in 0 until inventory.sizeInventory) {
					if (isItemStackEqualNBT(inventory.getStackInSlot(i), stack)) {
						val amount = min(stack.stackSize, inventory.getStackInSlot(i).stackSize)
						if (amount > 0) {
							inventory.decrStackSize(i, amount)
							stack.stackSize -= amount
						}
						if (stack.stackSize <= 0) {
							return true
						}
					}
				}
			}
			return false
		}
		
		/**
		 * Returns the amount of items in stack with NBT from inventory
		 * @param inventory Inventory
		 * @param stack Stack to compare item
		 * @return Amount
		 */
		@JvmStatic
		fun getAmountNBT(inventory: IInventory, stack: ItemStack): Int {
			var amount = 0
			for (i in 0 until inventory.sizeInventory) {
				if (isItemStackEqualNBT(inventory.getStackInSlot(i), stack))
					amount += inventory.getStackInSlot(i).stackSize
			}
			return amount
		}
		
		/**
		 * Checks if two itemstacks has same ID, metadata and NBT
		 * @param stack1 First itemstack
		 * @param stack2 Second itemstack
		 */
		@JvmStatic
		fun isItemStackEqualNBT(stack1: ItemStack?, stack2: ItemStack?): Boolean {
			return if (stack1 != null && stack2 != null && stack1.item === stack2.item && stack1.itemDamage == stack2.itemDamage) {
				if (!stack1.hasTagCompound() && !stack2.hasTagCompound()) {
					true
				} else if (stack1.hasTagCompound() != stack2.hasTagCompound()) {
					false
				} else
					stack1.stackTagCompound == stack2.stackTagCompound
			} else false
		}
		
		/**
		 * Changes itemstack's item
		 * @param stack Stack to change its item
		 * @param item Item to set in `stack`
		 */
		@JvmStatic
		fun changeStackItem(stack: ItemStack, item: Item): ItemStack {
			val newStack = ItemStack(item, stack.stackSize, stack.itemDamage)
			newStack.stackTagCompound = stack.stackTagCompound
			return newStack
		}
		
		// Removes <b>block</b> from GameRegistry
		/*public static void unregisterBlock(Block block) {
		
	}*/
		
		//Removes <b>item</b> from GameData
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
		 * Adds new recipe with [OreDictionary] support
		 */
		@JvmStatic
		fun addOreDictRecipe(output: ItemStack, vararg recipe: Any) {
			CraftingManager.getInstance().recipeList.add(ShapedOreRecipe(output, *recipe))
		}
		
		/**
		 * Adds new shapeless recipe with [OreDictionary] support
		 */
		@JvmStatic
		fun addShapelessOreDictRecipe(output: ItemStack, vararg recipe: Any) {
			CraftingManager.getInstance().recipeList.add(ShapelessOreRecipe(output, *recipe))
		}
		
		fun isOre(stack: ItemStack?, name: String) = OreDictionary.getOreIDs(stack).any { it == OreDictionary.getOreID(name) }
		
		/**
		 * Removes recipe of `resultItem`
		 * @param resultItem Stack to remove recipe
		 * @author Code by yope_fried, inspired by pigalot, provided by Develance on forum.mcmodding.ru
		 */
		@JvmStatic
		fun removeRecipe(resultItem: ItemStack) {
			val i = CraftingManager.getInstance().recipeList.iterator()
			while (i.hasNext()) {
				val r = i.next() as IRecipe
				if (ItemStack.areItemStacksEqual(resultItem, r.recipeOutput)) {
					FMLRelaunchLog.log("ASJLib", Level.INFO, "Removed Recipe: " + r + " -> " + r.recipeOutput)
					i.remove()
				}
			}
		}
		
		/**
		 * Checks whether `e1` is in FOV of `e2`
		 * @author a_dizzle (minecraftforum.net)
		 */
		@JvmStatic
		fun isNotInFieldOfVision(e1: EntityLivingBase, e2: EntityLivingBase): Boolean {
			//save Entity 2's original rotation variables
			var rotationYawPrime = e2.rotationYaw
			var rotationPitchPrime = e2.rotationPitch
			//make Entity 2 directly face Entity 1
			faceEntity(e2, e1, 360f, 360f)
			//switch values of prime rotation variables with current rotation variables
			val f = e2.rotationYaw
			val f1 = e2.rotationPitch
			e2.rotationYaw = rotationYawPrime
			e2.rotationPitch = rotationPitchPrime
			rotationYawPrime = f
			rotationPitchPrime = f1
			val x = 60f //this is only a guess, I don't know the actual range
			val y = 60f //this is only a guess, I don't know the actual range
			val yawFOVMin = e2.rotationYaw - x
			val yawFOVMax = e2.rotationYaw + x
			val pitchFOVMin = e2.rotationPitch - y
			val pitchFOVMax = e2.rotationPitch + y
			val flag1 = yawFOVMin < 0f && (rotationYawPrime >= yawFOVMin + 360f || rotationYawPrime <= yawFOVMax) || yawFOVMax >= 360f && (rotationYawPrime <= yawFOVMax - 360f || rotationYawPrime >= yawFOVMin) || yawFOVMax < 360f && yawFOVMin >= 0f && rotationYawPrime <= yawFOVMax && rotationYawPrime >= yawFOVMin
			val flag2 = pitchFOVMin <= -180f && (rotationPitchPrime >= pitchFOVMin + 360f || rotationPitchPrime <= pitchFOVMax) || pitchFOVMax > 180f && (rotationPitchPrime <= pitchFOVMax - 360f || rotationPitchPrime >= pitchFOVMin) || pitchFOVMax < 180f && pitchFOVMin >= -180f && rotationPitchPrime <= pitchFOVMax && rotationPitchPrime >= pitchFOVMin
			return !flag1 || !flag2 || !e2.canEntityBeSeen(e1)
		}
		
		/**
		 * Makes `e1` to face `e2`
		 */
		@JvmStatic
		fun faceEntity(e1: EntityLivingBase, e2: Entity, yaw: Float, pitch: Float) {
			val d0 = e2.posX - e1.posX
			val d2 = e2.posZ - e1.posZ
			val d1 = if (e2 is EntityLivingBase) {
				e2.posY + e2.eyeHeight.toDouble() - (e1.posY + e1.eyeHeight.toDouble())
			} else {
				(e2.boundingBox.minY + e2.boundingBox.maxY) / 2.0 - (e1.posY + e1.eyeHeight.toDouble())
			}
			
			val d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2).toDouble()
			val f2 = (atan2(d2, d0) * 180.0 / Math.PI).toFloat() - 90.0f
			val f3 = (-(atan2(d1, d3) * 180.0 / Math.PI)).toFloat()
			e1.rotationPitch = updateRotation(e1.rotationPitch, f3, pitch)
			e1.rotationYaw = updateRotation(e1.rotationYaw, f2, yaw)
		}
		
		@JvmStatic
		fun updateRotation(f: Float, f1: Float, f2: Float): Float {
			var f3 = MathHelper.wrapAngleTo180_float(f1 - f)
			if (f3 > f2) f3 = f2
			if (f3 < -f2) f3 = -f2
			return f + f3
		}
		
		/**
		 * Returns MOP with block and entity
		 * @param entity Entity to calculate vector from
		 * @param dist Max distance for use
		 * @param interact Whether to get uncollidable entities
		 * @author timaxa007
		 */
		@JvmStatic
		fun getMouseOver(entity: EntityLivingBase?, dist: Double, interact: Boolean): MovingObjectPosition? {
			if (entity?.worldObj == null) {
				return null
			}
			
			var pointedEntity: Entity? = null
			var d1 = dist
			val vec3 = Vec3.createVectorHelper(entity.posX, if (FMLCommonHandler.instance().effectiveSide == Side.CLIENT) entity.posY else entity.posY + entity.eyeHeight, entity.posZ)
			val vec31 = entity.lookVec
			val vec32 = vec3.addVector(vec31.xCoord * dist, vec31.yCoord * dist, vec31.zCoord * dist)
			var vec33: Vec3? = null
			val objectMouseOver = rayTrace(entity, dist)
			
			if (objectMouseOver != null) {
				d1 = objectMouseOver.hitVec.distanceTo(vec3)
			}
			
			val f1 = 1.0f
			val list = entity.worldObj.getEntitiesWithinAABBExcludingEntity(entity, entity.boundingBox.addCoord(vec31.xCoord * dist, vec31.yCoord * dist, vec31.zCoord * dist).expand(f1.toDouble(), f1.toDouble(), f1.toDouble()))
			var d2 = d1
			
			for (e in list) {
				e as Entity
				if (e.canBeCollidedWith() || interact) {
					val f2 = e.collisionBorderSize
					val axisalignedbb = e.boundingBox.expand(f2.toDouble(), f2.toDouble(), f2.toDouble())
					val movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32)
					
					if (axisalignedbb.isVecInside(vec3)) {
						if (0.0 < d2 || d2 == 0.0) {
							pointedEntity = e
							vec33 = if (movingobjectposition == null) vec3 else movingobjectposition.hitVec
							d2 = 0.0
						}
					} else if (movingobjectposition != null) {
						val d3 = vec3.distanceTo(movingobjectposition.hitVec)
						
						if (d3 < d2 || d2 == 0.0) {
							if (e === entity.ridingEntity && !e.canRiderInteract()) {
								if (d2 == 0.0) {
									pointedEntity = e
									vec33 = movingobjectposition.hitVec
								}
							} else {
								pointedEntity = e
								vec33 = movingobjectposition.hitVec
								d2 = d3
							}
						}
					}
				}
			}
			
			return if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
				MovingObjectPosition(pointedEntity, vec33)
			} else getSelectedBlock(entity, dist, interact)
		}
		
		/**
		 * Raytracer for 'getMouseOver' method.<br></br>
		 * Don't use it. Use [getMouseOver][.getSelectedBlock] instead
		 */
		private fun rayTrace(entity: EntityLivingBase, dist: Double): MovingObjectPosition? {
			val vec3 = Vec3.createVectorHelper(entity.posX, if (isServer) entity.posY + entity.eyeHeight else entity.posY, entity.posZ)
			val vec31 = entity.lookVec
			val vec32 = vec3.addVector(vec31.xCoord * dist, vec31.yCoord * dist, vec31.zCoord * dist)
			return entity.worldObj.func_147447_a(vec3, vec32, false, false, true)
		}
		
		/**
		 * Returns MOP with only blocks.
		 * @param entity Player to calculate vector from
		 * @param dist Max distance for use
		 * @param interact Can player interact with blocks (not sure)
		 */
		@JvmStatic
		fun getSelectedBlock(entity: EntityLivingBase, dist: Double, interact: Boolean): MovingObjectPosition? {
			val vec3 = getPosition(entity, 1.0f)
			vec3.yCoord += entity.eyeHeight.toDouble()
			val vec31 = entity.lookVec
			val vec32 = vec3.addVector(vec31.xCoord * dist, vec31.yCoord * dist, vec31.zCoord * dist)
			return entity.worldObj.rayTraceBlocks(vec3, vec32, interact)
		}
		
		/**
		 * Interpolated position vector
		 */
		@JvmStatic
		fun getPosition(living: EntityLivingBase, par1: Float): Vec3 {
			val i = (living as? EntityPlayer)?.defaultEyeHeight ?: 0f
			return if (par1 == 1.0f) {
				Vec3.createVectorHelper(living.posX, living.posY + (living.eyeHeight - i), living.posZ)
			} else {
				val d0 = living.prevPosX + (living.posX - living.prevPosX) * par1.toDouble()
				val d1 = living.prevPosY + (living.posY - living.prevPosY) * par1.toDouble() + (living.eyeHeight - i).toDouble()
				val d2 = living.prevPosZ + (living.posZ - living.prevPosZ) * par1.toDouble()
				Vec3.createVectorHelper(d0, d1, d2)
			}
		}
		
		@JvmStatic
		fun getLookVec(e: Entity): Vec3 {
			val f1 = MathHelper.cos(-e.rotationYaw * 0.017453292f - Math.PI.toFloat())
			val f2 = MathHelper.sin(-e.rotationYaw * 0.017453292f - Math.PI.toFloat())
			val f3 = -MathHelper.cos(-e.rotationPitch * 0.017453292f)
			val f4 = MathHelper.sin(-e.rotationPitch * 0.017453292f)
			return Vec3.createVectorHelper((f2 * f3).toDouble(), f4.toDouble(), (f1 * f3).toDouble())
		}
		
		/**
		 * @return Closest vulnerable player to entity within the given radius,
		 * ignoring invisibility and sneaking<br></br>
		 * Can be null if none is found
		 */
		@JvmStatic
		fun getClosestVulnerablePlayerToEntity(entity: Entity, distance: Double) =
			getClosestVulnerablePlayer(entity.worldObj, entity.posX, entity.posY, entity.posZ, distance)
		
		/**
		 * @return Closest vulnerable player to coords within the given radius,
		 * ignoring invisibility and sneaking<br></br>
		 * Can be null if none is found
		 */
		@JvmStatic
		fun getClosestVulnerablePlayer(world: World, x: Double, y: Double, z: Double, distance: Double): EntityPlayer? {
			var prevDist = -1.0
			var entityplayer: EntityPlayer? = null
			
			for (i in world.playerEntities.indices) {
				val entityplayer1 = world.playerEntities[i] as EntityPlayer
				
				if (!entityplayer1.capabilities.disableDamage && entityplayer1.isEntityAlive) {
					val dist = entityplayer1.getDistanceSq(x, y, z)
					
					if ((distance < 0.0 || dist < distance * distance) && (prevDist == -1.0 || dist < prevDist)) {
						prevDist = dist
						entityplayer = entityplayer1
					}
				}
			}
			
			return entityplayer
		}
		
		/**
		 * Registers new entity
		 * @param entityClass Entity's class file
		 * @param name The name of this entity
		 */
		@JvmStatic
		fun registerEntity(entityClass: Class<out Entity>, name: String, instance: Any) {
			val id = EntityRegistry.findGlobalUniqueEntityId()
			val nama = "${FMLCommonHandler.instance().findContainerFor(instance).modId}:$name"
			EntityRegistry.registerGlobalEntityID(entityClass, nama, id)
			EntityRegistry.registerModEntity(entityClass, nama, id, instance, 128, 1, true)
		}
		
		/**
		 * Registers new entity with egg
		 * @param entityClass Entity's class file
		 * @param name The name of this entity
		 * @param backColor Background egg color
		 * @param frontColor The color of dots
		 */
		@JvmStatic
		fun registerEntityEgg(entityClass: Class<out Entity>, name: String, backColor: Int, frontColor: Int, instance: Any) {
			val id = EntityRegistry.findGlobalUniqueEntityId()
			val nama = "${FMLCommonHandler.instance().findContainerFor(instance).modId}:$name"
			EntityRegistry.registerGlobalEntityID(entityClass, nama, id)
			EntityRegistry.registerModEntity(entityClass, nama, id, instance, 128, 1, true)
			EntityList.entityEggs[id] = EntityList.EntityEggInfo(id, backColor, frontColor)
		}
		
		/**
		 * Changes the biome at given coordinates, currently really buggy
		 * @param world This World
		 * @param x -Coordinate
		 * @param z -Coordinate
		 * @param biome The biome to set at this location
		 */
		@Deprecated("")
		@JvmStatic
		fun setBiomeAt(world: World, x: Int, z: Int, biome: BiomeGenBase?) {
			if (biome == null) {
				return
			}
			val chunk = world.getChunkFromBlockCoords(x, z)
			val array = chunk.biomeArray
			array[z and 0xF shl 4 or (x and 0xF)] = (biome.biomeID and 0xFF).toByte()
			chunk.biomeArray = array
		}
		
		/**
		 * @return random value in range [min, max] (inclusive)
		 */
		@JvmStatic
		fun randInBounds(min: Int, max: Int) = randInBounds(Random(), min, max)
		
		/**
		 * @return random value in range [min, max] (inclusive)
		 */
		@JvmStatic
		fun randInBounds(rand: Random, min: Int, max: Int) = rand.nextInt(max - min + 1) + min
		
		/**
		 * Interpolates values, e.g. for smoother render
		 */
		@JvmStatic
		fun interpolate(last: Double, now: Double) = last + (now - last) * Minecraft.getMinecraft().timer.renderPartialTicks
		
		/**
		 * Translates matrix to follow player (if something is bound to world's zero coords)
		 */
		@JvmStatic
		fun interpolatedTranslation(entity: Entity) =
			glTranslated(interpolate(entity.lastTickPosX, entity.posX), interpolate(entity.lastTickPosY, entity.posY), interpolate(entity.lastTickPosZ, entity.posZ))
		
		/**
		 * Translates matrix not to follow player (if something is bound to camera's zero coords)
		 */
		@JvmStatic
		fun interpolatedTranslationReverse(entity: Entity) =
			glTranslated(-interpolate(entity.lastTickPosX, entity.posX), -interpolate(entity.lastTickPosY, entity.posY), -interpolate(entity.lastTickPosZ, entity.posZ))
		
		/**
		 * Sets matrix and translation to world's zero coordinates
		 * so you can render something as in TileEntitySpecialRenderer (if you are used to it)
		 * Don't forget to call [.postRenderISBRH]
		 * Use this before your render something in ISimpleBlockRenderingHandler
		 */
		@JvmStatic
		fun preRenderISBRH(x: Int, z: Int) {
			val X = (x / 16 - if (x < 0 && x % 16 != 0) 1 else 0) * -16
			val Z = (z / 16 - if (z < 0 && z % 16 != 0) 1 else 0) * -16
			Tessellator.instance.draw()
			Tessellator.instance.setTranslation(0.0, 0.0, 0.0)
			glPushMatrix()
			glTranslated(X.toDouble(), 0.0, Z.toDouble())
		}
		
		/**
		 * This gets everything back for other blocks to render properly
		 * Don't use unless you've used [.preRenderISBRH]
		 * Use this after your render something in ISimpleBlockRenderingHandler
		 */
		@JvmStatic
		fun postRenderISBRH(x: Int, z: Int) {
			val X = (x / 16 - if (x < 0 && x % 16 != 0) 1 else 0) * -16
			val Z = (z / 16 - if (z < 0 && z % 16 != 0) 1 else 0) * -16
			glPopMatrix()
			Tessellator.instance.startDrawingQuads()
			Tessellator.instance.setTranslation(X.toDouble(), 0.0, Z.toDouble())
		}
		
		/**
		 * @return String which tolds you to hold shift-key
		 */
		@JvmStatic
		fun holdShift() =
			StatCollector.translateToLocal("tooltip.hold") + EnumChatFormatting.WHITE + " SHIFT " + EnumChatFormatting.GRAY + StatCollector.translateToLocal("tooltip.shift")
		
		/**
		 * @return String which tolds you to hold control-key
		 */
		@JvmStatic
		fun holdCtrl() =
			StatCollector.translateToLocal("tooltip.hold") + EnumChatFormatting.WHITE + " CTRL " + EnumChatFormatting.GRAY + StatCollector.translateToLocal("tooltip.ctrl")
		
		/**
		 * @return String which tolds you that this block/item/stuff is only for creative use
		 */
		@JvmStatic
		fun creativeOnly() = StatCollector.translateToLocal("tooltip.creativeonly")!!
		
		/**
		 * @return map key for specified value if persist (null if none)
		 */
		@JvmStatic
		fun <K, V> mapGetKey(map: Map<K, V>, v: V): K? {
			for ((key, value) in map) if (value == v) return key
			return null
		}
		
		/**
		 * @return map key for specified value if persist (default if none)
		 */
		@JvmStatic
		fun <K, V> mapGetKeyOrDefault(map: Map<K, V>, v: V, def: K): K {
			for ((key, value) in map) if (value == v) return key
			return def
		}
		
		@JvmStatic
		fun <T: Comparable<T>> indexOfComparableArray(array: Array<T>, key: T) =
			array.indices.firstOrNull { array[it].compareTo(key) == 0 } ?: -1
		
		@JvmStatic
		fun <T: Comparable<T>> indexOfComparableColl(coll: Collection<T>, key: T): Int {
			var id = -1
			for (t in coll) {
				++id
				if (t.compareTo(key) == 0)
					return id
			}
			return id
		}
		
		val colorCode = IntArray(32)
		
		init {
			for (i in 0..31) {
				val j = (i shr 3 and 1) * 85
				var k = (i shr 2 and 1) * 170 + j
				var l = (i shr 1 and 1) * 170 + j
				var i1 = (i and 1) * 170 + j
				
				if (i == 6) {
					k += 85
				}
				
				if (i >= 16) {
					k /= 4
					l /= 4
					i1 /= 4
				}
				
				colorCode[i] = k and 255 shl 16 or (l and 255 shl 8) or (i1 and 255)
			}
		}
		
		/**
		 * @return enum color packed in uInt with max alpha
		 * @author qiexie
		 */
		@JvmStatic
		fun enumColorToRGB(eColor: EnumChatFormatting) = addAlpha(colorCode[eColor.ordinal], 0xff)
		
		/**
		 * Adds `alpha` value to @{code color}
		 */
		@JvmStatic
		fun addAlpha(color: Int, alpha: Int) = alpha and 0xFF shl 24 or (color and 0x00FFFFFF)
		
		/**
		 * Sets render color unpacked from uInt
		 */
		@JvmStatic
		fun glColor1u(color: Int) {
			glColor4ub((color shr 16 and 0xFF).toByte(), (color shr 8 and 0xFF).toByte(), (color and 0xFF).toByte(), (color shr 24 and 0xFF).toByte())
		}
		
		/**
		 * Registers IIcon for block. Call this in preInit
		 */
		@JvmStatic
		fun registerBlockIcon(name: String) {
			blockIconsNames.add(name)
		}
		
		/**
		 * Registers IIcon for item. Call this in preInit
		 */
		@JvmStatic
		fun registerItemIcon(name: String) {
			itemsIconsNames.add(name)
		}
		
		/**
		 * Returns block IIcon registered with [.registerBlockIcon]
		 */
		@JvmStatic
		fun getBlockIcon(name: String) = blockIcons[name]!!
		
		/**
		 * Returns item IIcon registered with [.registerItemIcon]
		 */
		@JvmStatic
		fun getItemIcon(name: String) = itemsIcons[name]!!
		
		/**
		 * Registers dimension
		 * @param keepLoaded Keep spawn chunks loaded
		 */
		@JvmStatic
		fun registerDimension(id: Int, w: Class<out WorldProvider>, keepLoaded: Boolean) {
			if (!DimensionManager.registerProviderType(id, w, keepLoaded)) throw IllegalArgumentException(String.format("Failed to register provider for id %d, One is already registered", id))
			DimensionManager.registerDimension(id, id)
		}
		
		/**
		 * Generates **ore** into the world
		 * Args: block, it's meta, block to replace, world, rng, x, z positions,
		 * min, max blocks in one place, min, max block groups in chunk,
		 * chance to be generated, min, max Y-level
		 */
		@JvmStatic
		fun generateOre(ore: Block, meta: Int, replace: Block, world: World, rand: Random, blockXPos: Int, blockZPos: Int, minVeinSize: Int, maxVeinSize: Int, minVeinsPerChunk: Int, maxVeinsPerChunk: Int, chanceToSpawn: Int, minY: Int, maxY: Int) {
			if (rand.nextInt(101) > chanceToSpawn) return
			val veins = rand.nextInt(maxVeinsPerChunk - minVeinsPerChunk + 1) + minVeinsPerChunk
			for (i in 0 until veins) {
				val posX = blockXPos + rand.nextInt(16) + 8
				val posY = minY + rand.nextInt(maxY - minY)
				val posZ = blockZPos + rand.nextInt(16) + 8
				WorldGenMinable(ore, meta, minVeinSize + rand.nextInt(maxVeinSize - minVeinSize + 1), replace).generate(world, rand, posX, posY, posZ)
			}
		}
		
		/**
		 * Fills holes of worldgen (no more structures in mid-air)
		 * Args: world, filler, x start, x end, upper height, z start, z end
		 * @param radius Radius of cylinder-shaped structure's base (0 for square)
		 */
		@JvmStatic
		fun fillGenHoles(world: World, filler: Block, meta: Int, xmn: Int, xmx: Int, ystart: Int, zmn: Int, zmx: Int, radius: Int) {
			if (xmn < -29999999 || xmx > 29999999 || ystart < 0 || ystart > 255 || zmn < -29999999 || zmx > 29999999 || radius < 0) return
			for (i in xmn..xmx) {
				for (k in zmn..zmx) {
					var j = ystart - 1
					while (j >= 0 && isBlockReplaceable(world.getBlock(i, j, k))) {
						if (radius != 0) if (sqrt(((xmx - xmn) / 2 + xmn - i).toDouble().pow(2.0) + ((zmx - zmn) / 2 + zmn - k).toDouble().pow(2.0)) > radius) {
							j--
							continue
						}
						world.setBlock(i, j, k, filler, meta, 3)
						j--
					}
				}
			}
		}
		
		@JvmStatic
		fun isBlockReplaceable(block: Block): Boolean {
			return block === Blocks.air ||
				   block === Blocks.snow_layer ||
				   block.material === Material.air ||
				   block.material === Material.cactus ||
				   block.material === Material.coral ||
				   block.material === Material.fire ||
				   block.material === Material.gourd ||
				   block.material === Material.leaves ||
				   block.material === Material.lava ||
				   block.material === Material.plants ||
				   block.material === Material.vine ||
				   block.material === Material.water ||
				   block.material === Material.web
		}
		
		private val format = DecimalFormat("000")
		
		private fun time(world: World?) = "[${format.format(world?.let { it.totalWorldTime % 1000 } ?: 0)}]"
		
		@SideOnly(Side.CLIENT)
		@JvmStatic
		fun chatLog(message: String) {
			Minecraft.getMinecraft()?.thePlayer?.addChatMessage(ChatComponentText("${time(Minecraft.getMinecraft()?.theWorld)} $message"))
		}
		
		@SideOnly(Side.CLIENT)
		@JvmStatic
		fun chatLog(message: String, world: World?) {
			Minecraft.getMinecraft()?.thePlayer?.addChatMessage(ChatComponentText("${time(world)} ${if (world?.isRemote == true) "[C]" else "[S]"} $message"))
		}
		
		@JvmStatic
		fun chatLog(message: String, player: EntityPlayer) {
			player.addChatMessage(ChatComponentText("${time(player.worldObj)} ${if (player.worldObj.isRemote) "[C]" else "[S]"} $message"))
		}
		
		@JvmStatic
		fun log(message: String) {
			FMLRelaunchLog.log(Loader.instance().activeModContainer().modId.toUpperCase(), Level.INFO, message)
		}
		
		@JvmStatic
		fun debug(message: String) {
			FMLRelaunchLog.log(Loader.instance().activeModContainer().modId.toUpperCase(), Level.DEBUG, message)
		}
		
		@JvmStatic
		fun warn(message: String) {
			FMLRelaunchLog.log(Loader.instance().activeModContainer().modId.toUpperCase(), Level.WARN, message)
		}
		
		@JvmStatic
		fun error(message: String) {
			FMLRelaunchLog.log(Loader.instance().activeModContainer().modId.toUpperCase(), Level.ERROR, message)
		}
		
		private fun trace(message: String) {
			FMLRelaunchLog.log(Loader.instance().activeModContainer().modId.toUpperCase(), Level.TRACE, message)
		}
		
		@JvmStatic
		fun printStackTrace() {
			log("Stack trace: ")
			val stes = Thread.currentThread().stackTrace
			for (i in 2 until stes.size) log("\tat ${stes[i]}")
		}
		
		@JvmStatic
		fun say(player: EntityPlayer?, message: String) {
			player?.addChatMessage(ChatComponentText(StatCollector.translateToLocal(message)))
		}
		
		@JvmStatic
		fun sayToAllOnline(message: String) {
			val list = MinecraftServer.getServer().configurationManager.playerEntityList
			for (online in list) say(online as EntityPlayer, message)
			log(message)
		}
		
		/** Untested!  */
		@Deprecated("")
		@JvmStatic
		fun sayToAllOPs(message: String) {
			MinecraftServer.getServer().configurationManager.func_152606_n()
				.mapNotNull { MinecraftServer.getServer().configurationManager.func_152612_a(it) }
				.forEach { say(it, message) }
			log(message)
		}

		@JvmStatic
		val isServer: Boolean
			get() = FMLCommonHandler.instance().effectiveSide == Side.SERVER

		@JvmStatic
		fun toString(nbt: NBTTagCompound): String {
			val sb = StringBuilder("{\n")
			for (o in nbt.tagMap.entries) {
				val e = o as Map.Entry<*, *>
				val v = e.value
				if (v is NBTTagList || v is NBTTagCompound) {
					val arr = if (v is NBTTagList)
						toString(v).split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
					else
						toString(v as NBTTagCompound).split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
					sb.append("    ").append(e.key).append(" = ").append(arr[0]).append("\n")
					for (i in 1 until arr.size) sb.append("    ").append(arr[i]).append("\n")
				} else
					sb.append("    ").append(e.key).append(" = ").append(e.value).append("\n")
			}
			sb.append("}")
			return "$sb"
		}
		
		@JvmStatic
		fun toString(nbt: NBTTagList): String {
			val sb = StringBuilder("list [\n")
			for (obj in nbt.tagList)
				if (obj is NBTTagList || obj is NBTTagCompound) {
					val ts = if (obj is NBTTagList)
						toString(obj)
					else
						toString(obj as NBTTagCompound)
					for (s in ts.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) sb.append("    ").append(s).append("\n")
				} else
					sb.append(obj).append("\n")
			sb.append("]")
			return "$sb"
		}
	}
}