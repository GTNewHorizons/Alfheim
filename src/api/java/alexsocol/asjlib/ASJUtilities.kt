package alexsocol.asjlib

import alexsocol.asjlib.math.Vector3
import cpw.mods.fml.common.*
import cpw.mods.fml.common.registry.*
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.material.Material.*
import net.minecraft.client.Minecraft
import net.minecraft.command.ICommandSender
import net.minecraft.entity.*
import net.minecraft.entity.player.*
import net.minecraft.init.Blocks
import net.minecraft.inventory.IInventory
import net.minecraft.item.*
import net.minecraft.item.crafting.*
import net.minecraft.nbt.*
import net.minecraft.potion.Potion
import net.minecraft.server.MinecraftServer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.world.*
import net.minecraft.world.biome.BiomeGenBase
import net.minecraft.world.gen.feature.WorldGenMinable
import net.minecraftforge.common.DimensionManager
import net.minecraftforge.event.entity.living.*
import net.minecraftforge.oredict.*
import org.apache.logging.log4j.Level
import java.text.DecimalFormat
import java.util.*
import kotlin.math.*

/**
 * Small utility lib to help with some tricks. Feel free to use it in your mods.
 * @author AlexSocol
 */
@Suppress("unused", "MemberVisibilityCanBePrivate", "UNCHECKED_CAST")
object ASJUtilities {
	
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
	 * Registers this block as burnable
	 */
	@JvmStatic
	fun setBurnable(block: Block, encouragement: Int, flammablility: Int) {
		Blocks.fire.setFireInfo(block, encouragement, flammablility)
	}
	
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
	 * @param target Entity to send
	 * @param dimTo ID of the dimension the entity should be sent to
	 */
	@JvmStatic
	fun sendToDimensionWithoutPortal(target: Entity, dimTo: Int, x: Double, y: Double, z: Double) {
		if (target.worldObj.isRemote || target.isDead) return
		
		val server = MinecraftServer.getServer()
		
		target.ridingEntity?.riddenByEntity = null
		target.ridingEntity = null
		
		if (dimTo == target.dimension)
			return target.setLocationAndAngles(x, y, z, target.rotationYaw, target.rotationPitch)
		
		val worldTo = server.worldServerForDimension(dimTo)
		
		if (target is EntityPlayerMP)
			return server.configurationManager.transferPlayerToDimension(target, dimTo, FreeTeleporter(worldTo, x, y, z))
		
		val dimFrom = target.dimension
		val worldFrom = server.worldServerForDimension(dimFrom)
		
		target.worldObj.theProfiler.startSection("changeDimension")
		
		worldTo.getBlock(x.mfloor(), y.mfloor(), z.mfloor()) // explicit world load ._.
		
		target.dimension = dimTo
		target.worldObj.removeEntity(target)
		target.isDead = false
		
		target.worldObj.theProfiler.startSection("reposition")
		server.configurationManager.transferEntityToWorld(target, dimFrom, worldFrom, worldTo, FreeTeleporter(worldTo, x, y, z))
		target.worldObj.theProfiler.endStartSection("reloading")
		
		EntityList.createEntityByName(EntityList.getEntityString(target), worldTo)?.also { new ->
			new.copyDataFrom(target, true)
			new.spawn(worldTo)
		}
		
		target.isDead = true
		target.worldObj.theProfiler.endSection()
		worldFrom.resetUpdateEntityTick()
		worldTo.resetUpdateEntityTick()
		target.worldObj.theProfiler.endSection()
	}
	
	/**
	 * Sends data about [tile] to client
	 * @author Vazkii
	 */
	fun dispatchTEToNearbyPlayers(tile: TileEntity) {
		val world = tile.worldObj
		val players = world.playerEntities
		for (player in players)
			if (player is EntityPlayerMP && Vector3.pointDistancePlane(player.posX, player.posZ, tile.xCoord + 0.5, tile.zCoord + 0.5) < 64)
				tile.descriptionPacket?.let { player.playerNetServerHandler.sendPacket(it) }
	}
	
	/**
	 * Sends data about tile at [x] [y] [z] to client
	 * @author Vazkii
	 */
	fun dispatchTEToNearbyPlayers(world: World, x: Int, y: Int, z: Int) {
		world.getTileEntity(x, y, z)?.let { dispatchTEToNearbyPlayers(it) }
	}
	
	@JvmStatic
	fun willEntityDie(event: LivingHurtEvent) = willEntityDie(LivingAttackEvent(event.entityLiving, event.source, event.ammount))
	
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
			amount = amount * armor / 25f
		}
		if (!source.isDamageAbsolute && living.isPotionActive(Potion.resistance)) {
			val resistance = 25 - (living.getActivePotionEffect(Potion.resistance).amplifier + 1) * 5
			amount = amount * resistance / 25f
		}
		return ceil(amount) >= floor(living.health)
	}
	
	// ################################ STACKS ################################
	
	/**
	 * Returns the number of the slot with item matching to item passed in
	 * @param item The item to compare
	 * @param inventory The inventory to scan
	 */
	@JvmStatic
	fun getSlotWithItem(item: Item, inventory: IInventory) = (0 until inventory.sizeInventory).firstOrNull { inventory[it]?.item === item } ?: -1
	
	/**
	 * Checks if two itemstacks has same ID, metadata and NBT
	 * @param stack1 First itemstack
	 * @param stack2 Second itemstack
	 */
	@JvmStatic
	fun isItemStackEqualData(stack1: ItemStack, stack2: ItemStack): Boolean {
		return stack1.isItemEqual(stack2) && ItemStack.areItemStackTagsEqual(stack1, stack2)
	}
	
	/**
	 * Boolean tag label for ingredient to ignore nbt on crafting
	 */
	const val TAG_ASJIGNORENBT = "ASJIGNORENBT"
	
	/**
	 * Boolean tag label for ingredient to check only tags presented in it
	 */
	const val TAG_ASJONLYNBT = "ASJONLYNBT"
	
	/**
	 * Checks if two itemstacks has same ID, metadata and NBT
	 *
	 * @param ingredient stack from recipe
	 * @param input stack from crafting input
	 * @see [TAG_ASJIGNORENBT]
	 * @see [TAG_ASJONLYNBT]
	 */
	// WHAT THE HELL IS WRONG WITH THE COMPILER?!
	@JvmStatic
	fun isItemStackEqualCrafting(ingredient: ItemStack, input: ItemStack): Boolean {
		if (!ingredient.isItemEqual(input))
			return false
		
		if (ItemNBTHelper.getBoolean(ingredient, TAG_ASJIGNORENBT, false))
			return true
		
		if (ItemNBTHelper.getBoolean(ingredient, TAG_ASJONLYNBT, false)) {
			val tags = input.tagCompound ?: return false
			val itags = ingredient.tagCompound.copy() as NBTTagCompound
			itags.removeTag(TAG_ASJONLYNBT)
			
			for (key in itags.func_150296_c()) {
				if (!tags.hasKey(key as String))
					return false
				
				if (itags.getTag(key) != tags.getTag(key))
					return false
			}
			
			return true
		}
		
		return ingredient.areItemStackTagsEqual(input)
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
			val slot = inventory[i] ?: continue
			if (stack.isItemEqual(slot)) amount += slot.stackSize
		}
		return amount
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
			val slot = inventory[i] ?: continue
			if (isItemStackEqualData(slot, stack)) amount += slot.stackSize
		}
		return amount
	}
	
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
				val slot = inventory[i] ?: continue
				if (stack.isItemEqual(slot)) {
					val amount = min(stack.stackSize, slot.stackSize)
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
	 * Removes itemstack with NBT from inventory
	 * @param inventory Inventory
	 * @param stack ItemStack to remove
	 * @return If the stack was removed
	 */
	@JvmStatic
	fun consumeItemStackNBT(inventory: IInventory, stack: ItemStack): Boolean {
		if (getAmountNBT(inventory, stack) >= stack.stackSize) {
			for (i in 0 until inventory.sizeInventory) {
				val slot = inventory[i] ?: continue
				if (isItemStackEqualData(slot, stack)) {
					val amount = min(stack.stackSize, slot.stackSize)
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
	
	/**
	 * Checks whether `stack` is registered to oredict `name`
	 */
	@JvmStatic
	fun isOre(stack: ItemStack?, name: String) = OreDictionary.getOreIDs(stack).any { it == OreDictionary.getOreID(name) }
	
	@JvmStatic
	fun removeRecipe(block: Block, stackSize: Int = 1, meta: Int = 0) = removeRecipe(ItemStack(block, stackSize, meta))
	
	@JvmStatic
	fun removeRecipe(item: Item, stackSize: Int = 1, meta: Int = 0) = removeRecipe(ItemStack(item, stackSize, meta))
	
	/**
	 * Removes recipe of [resultItem]. Note: stackSize and meta sensitive
	 * @param resultItem Stack to remove recipe
	 */
	@JvmStatic
	fun removeRecipe(resultItem: ItemStack) = (CraftingManager.getInstance().recipeList as ArrayList<IRecipe>).removeAll {
		ItemStack.areItemStacksEqual(resultItem, it.recipeOutput)
	}
	
	/**
	 * Checks whether [target] is NOT in FOV of [observer]
	 * @author a_dizzle (minecraftforum.net)
	 */
	@JvmStatic
	fun isNotInFieldOfVision(target: Entity, observer: EntityLivingBase): Boolean {
		//save Entity 2's original rotation variables
		var rotationYawPrime = observer.rotationYaw
		var rotationPitchPrime = observer.rotationPitch
		//make Entity 2 directly face Entity 1
		faceEntity(observer, target, 360f, 360f)
		//switch values of prime rotation variables with current rotation variables
		val f = observer.rotationYaw
		val f1 = observer.rotationPitch
		observer.rotationYaw = rotationYawPrime
		observer.rotationPitch = rotationPitchPrime
		rotationYawPrime = f
		rotationPitchPrime = f1
		val x = 60f //this is only a guess, I don't know the actual range
		val y = 60f //this is only a guess, I don't know the actual range
		val yawFOVMin = observer.rotationYaw - x
		val yawFOVMax = observer.rotationYaw + x
		val pitchFOVMin = observer.rotationPitch - y
		val pitchFOVMax = observer.rotationPitch + y
		val flag1 = yawFOVMin < 0f && (rotationYawPrime >= yawFOVMin + 360f || rotationYawPrime <= yawFOVMax) || yawFOVMax >= 360f && (rotationYawPrime <= yawFOVMax - 360f || rotationYawPrime >= yawFOVMin) || yawFOVMax < 360f && yawFOVMin >= 0f && rotationYawPrime <= yawFOVMax && rotationYawPrime >= yawFOVMin
		val flag2 = pitchFOVMin <= -180f && (rotationPitchPrime >= pitchFOVMin + 360f || rotationPitchPrime <= pitchFOVMax) || pitchFOVMax > 180f && (rotationPitchPrime <= pitchFOVMax - 360f || rotationPitchPrime >= pitchFOVMin) || pitchFOVMax < 180f && pitchFOVMin >= -180f && rotationPitchPrime <= pitchFOVMax && rotationPitchPrime >= pitchFOVMin
		return !flag1 || !flag2 || !observer.canEntityBeSeen(target)
	}
	
	/**
	 * Makes [e1] to face [e2]
	 */
	@JvmStatic
	fun faceEntity(e1: EntityLivingBase, e2: Entity, yaw: Float, pitch: Float) {
		val d0 = e2.posX - e1.posX
		val d2 = e2.posZ - e1.posZ
		val d1 = if (e2 is EntityLivingBase) {
			e2.posY + e2.eyeHeight.D - (e1.posY + e1.eyeHeight.D)
		} else {
			(e2.boundingBox.minY + e2.boundingBox.maxY) / 2.0 - (e1.posY + e1.eyeHeight.D)
		}
		
		val d3 = sqrt(d0 * d0 + d2 * d2)
		val f2 = (atan2(d2, d0) * 180.0 / Math.PI).F - 90f
		val f3 = (-(atan2(d1, d3) * 180.0 / Math.PI)).F
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
	 * @param interact Whether to get uncollidable entities / stop on hitting water
	 * @author timaxa007
	 */
	@JvmStatic
	fun getMouseOver(entity: EntityLivingBase?, dist: Double, interact: Boolean): MovingObjectPosition? {
		if (entity?.worldObj == null) return null
		
		var pointedEntity: Entity? = null
		var d1 = dist
		val vec3 = Vec3.createVectorHelper(entity.posX, if (isClient) entity.posY else entity.posY + entity.eyeHeight, entity.posZ)
		val vec31 = entity.lookVec
		val vec32 = vec3.addVector(vec31.xCoord * dist, vec31.yCoord * dist, vec31.zCoord * dist)
		var vec33: Vec3? = null
		val objectMouseOver = rayTrace(entity, dist)
		
		if (objectMouseOver != null) {
			d1 = objectMouseOver.hitVec.distanceTo(vec3)
		}
		
		val f1 = 1f
		val list = entity.worldObj.getEntitiesWithinAABBExcludingEntity(entity, entity.boundingBox.addCoord(vec31.xCoord * dist, vec31.yCoord * dist, vec31.zCoord * dist).expand(f1.D, f1.D, f1.D))
		var d2 = d1
		
		for (e in list) {
			e as Entity
			if (e.canBeCollidedWith() || interact) {
				val f2 = e.collisionBorderSize
				val axisalignedbb = e.boundingBox.expand(f2.D, f2.D, f2.D)
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
	 * Raytracer for 'getMouseOver' method.
	 */
	private fun rayTrace(entity: EntityLivingBase, dist: Double): MovingObjectPosition? {
		// NO THAT CANNOT BE REPLACED BY Vector3#fromEntity
		val vec3 = Vec3.createVectorHelper(entity.posX, if (isServer) entity.posY + entity.eyeHeight else entity.posY, entity.posZ)
		val vec31 = entity.lookVec
		val vec32 = vec3.addVector(vec31.xCoord * dist, vec31.yCoord * dist, vec31.zCoord * dist)
		return entity.worldObj.func_147447_a(vec3, vec32, false, false, true)
	}
	
	/**
	 * Returns MOP with only blocks.
	 * @param entity Player to calculate vector from
	 * @param dist Max distance for use
	 * @param stopOnWater Whether to stop raytrace when hitting liquid
	 */
	@JvmStatic
	fun getSelectedBlock(entity: EntityLivingBase, dist: Double, stopOnWater: Boolean): MovingObjectPosition? {
		// NO THAT CANNOT BE REPLACED BY Vector3#fromEntity
		val pos = getPosition(entity)
		val look = entity.getLook(0f)
		val combined = pos.addVector(look.xCoord * dist, look.yCoord * dist, look.zCoord * dist)
		return entity.worldObj.rayTraceBlocks(pos, combined, stopOnWater)
	}
	
	/**
	 * Corrected position vector
	 * @author Azanor
	 */
	@JvmStatic
	fun getPosition(target: EntityLivingBase): Vec3 {
		val v = Vec3.createVectorHelper(target.posX, target.posY, target.posZ)
		if (target.worldObj.isRemote) {
			v.yCoord += (target.eyeHeight - if (target is EntityPlayer) target.defaultEyeHeight else 0f)
		} else {
			v.yCoord += target.eyeHeight
			if (target.isSneaking)
				v.yCoord -= 0.08
		}
		
		return v
	}
	
	@JvmStatic
	fun getLookVec(e: Entity): Vec3 {
		val f1 = MathHelper.cos(-e.rotationYaw * 0.017453292f - PI.F)
		val f2 = MathHelper.sin(-e.rotationYaw * 0.017453292f - PI.F)
		val f3 = -MathHelper.cos(-e.rotationPitch * 0.017453292f)
		val f4 = MathHelper.sin(-e.rotationPitch * 0.017453292f)
		return Vec3.createVectorHelper((f2 * f3).D, f4.D, (f1 * f3).D)
	}
	
	/**
	 * @return Closest vulnerable player to entity within the given radius,
	 * ignoring invisibility and sneaking<br></br>
	 * Can be null if none is found
	 */
	@JvmStatic
	fun getClosestVulnerablePlayerToEntity(entity: Entity, distance: Double) = getClosestVulnerablePlayer(entity.worldObj, entity.posX, entity.posY, entity.posZ, distance)
	
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
	 * @param id Mod-specific entity id
	 */
	@JvmStatic
	fun registerEntity(entityClass: Class<out Entity>, name: String, id: Int) {
		EntityRegistry.registerModEntity(entityClass, name, id, Loader.instance().activeModContainer().mod, 128, 1, true)
	}
	
	/**
	 * Registers new entity with egg
	 * @param entityClass Entity's class file
	 * @param name The name of this entity
	 * @param backColor Background egg color
	 * @param frontColor The color of dots
	 */
	@Deprecated("Use local registrations instead", ReplaceWith("registerEntity(entityClass, name, instance, id)"), DeprecationLevel.ERROR)
	@JvmStatic
	fun registerEntityEgg(entityClass: Class<out Entity>, name: String, backColor: Int, frontColor: Int, instance: Any) {
		val id = EntityRegistry.findGlobalUniqueEntityId()
//		val modid = FMLCommonHandler.instance().findContainerFor(instance).modId
		EntityRegistry.registerGlobalEntityID(entityClass, name, id)
		EntityRegistry.registerModEntity(entityClass, name, id, instance, 128, 1, true)
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
	fun setBiomeAt(world: World, x: Int, z: Int, biome: BiomeGenBase) {
		val chunk = world.getChunkFromBlockCoords(x, z)
		val array = chunk.biomeArray
		array[z and 0xF shl 4 or (x and 0xF)] = (biome.biomeID and 0xFF).toByte()
		chunk.biomeArray = array
	}
	
	/**
	 * @return random value in range [[min], [max]] (inclusive)
	 */
	@JvmStatic
	fun randInBounds(min: Int, max: Int, rand: Random = Random()) = rand.nextInt(max - min + 1) + min
	
	/**
	 * @return true with [percent]% chance
	 */
	@JvmStatic
	fun chance(percent: Number) = Math.random() * 100 < percent.D
	
	/**
	 * @return String which tolds you to hold shift-key
	 */
	@JvmStatic
	fun holdShift() = StatCollector.translateToLocal("tooltip.hold") + EnumChatFormatting.WHITE + " SHIFT " + EnumChatFormatting.GRAY + StatCollector.translateToLocal("tooltip.shift")
	
	/**
	 * @return String which tolds you to hold control-key
	 */
	@JvmStatic
	fun holdCtrl() = StatCollector.translateToLocal("tooltip.hold") + EnumChatFormatting.WHITE + " CTRL " + EnumChatFormatting.GRAY + StatCollector.translateToLocal("tooltip.ctrl")
	
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
	fun <T: Comparable<T>> indexOfComparableArray(array: Array<T>, key: T) = array.indices.firstOrNull { array[it].compareTo(key) == 0 } ?: -1
	
	@JvmStatic
	fun <T: Comparable<T>> indexOfComparableColl(coll: Collection<T?>, key: T): Int {
		var id = -1
		for (t in coll) {
			++id
			
			if (t?.let { key.compareTo(it) } ?: continue == 0) return id
		}
		return id
	}
	
	/**
	 * Registers dimension
	 * @param keepLoaded Keep spawn chunks loaded
	 */
	@JvmStatic
	fun registerDimension(id: Int, w: Class<out WorldProvider>, keepLoaded: Boolean) {
		require(DimensionManager.registerProviderType(id, w, keepLoaded)) { String.format("Failed to register provider for id %d, One is already registered", id) }
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
		if (!chance(chanceToSpawn)) return
		
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
					if (radius != 0) if (sqrt(((xmx - xmn) / 2 + xmn - i).D.pow(2.0) + ((zmx - zmn) / 2 + zmn - k).D.pow(2.0)) > radius) {
						j--
						continue
					}
					world.setBlock(i, j, k, filler, meta, 3)
					j--
				}
			}
		}
	}
	
	private val replaceableMaterials = arrayOf(air, cactus, coral, fire, gourd, leaves, lava, plants, vine, water, web)
	
	@JvmStatic
	fun isBlockReplaceable(block: Block): Boolean {
		return block === Blocks.air || block === Blocks.snow_layer || block.material in replaceableMaterials
	}
	
	@JvmStatic
	fun getTopLevel(worldObj: World, x: Int, z: Int): Int {
		var y = 1
		while (!worldObj.isAirBlock(x, y, z)) ++y
		return y
	}
	
	@JvmStatic
	fun soundFromMaterial(mat: Material) = when (mat) {
		anvil                                      -> Block.soundTypeAnvil
		air, cake, carpet, cloth, sponge, tnt, web -> Block.soundTypeCloth
		glass, ice, packedIce, portal              -> Block.soundTypeGlass
		cactus, coral, grass, leaves, plants, vine -> Block.soundTypeGrass
		ground, clay                               -> Block.soundTypeGravel
		iron                                       -> Block.soundTypeMetal
		sand                                       -> Block.soundTypeSand
		craftedSnow, snow                          -> Block.soundTypeSnow
		circuits, dragonEgg, redstoneLight, rock   -> Block.soundTypeStone
		gourd, wood                                -> Block.soundTypeWood
		else                                       -> null
	}
	
	private val format = DecimalFormat("000")
	private fun time(world: World?) = "[${format.format(world?.let { it.totalWorldTime % 1000 } ?: 0)}]"
	
	@JvmStatic
	fun chatLog(message: String) {
		val world = if (isServer) MinecraftServer.getServer()?.entityWorld else Minecraft.getMinecraft()?.theWorld
		val msg = "${worldInfoForLog(world)} $message"
		if (isServer) sayToAllOnline(msg)
		else Minecraft.getMinecraft()?.thePlayer?.addChatMessage(ChatComponentText(msg))
	}
	
	@JvmStatic
	fun chatLog(message: String, world: World?) {
		val msg = "${worldInfoForLog(world)} $message"
		if (isServer) sayToAllOnline(msg)
		else Minecraft.getMinecraft()?.thePlayer?.addChatMessage(ChatComponentText(msg))
	}
	
	@JvmStatic
	fun chatLog(message: String, player: EntityPlayer) {
		player.addChatMessage(ChatComponentText("${worldInfoForLog(player.worldObj)} $message"))
	}
	
	@JvmStatic
	fun worldInfoForLog(world: World?) = "${time(world)} ${if (world?.isRemote == true) "[C]" else "[S]"}"
	
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
	
	@JvmStatic
	fun fatal(message: String) {
		FMLRelaunchLog.log(Loader.instance().activeModContainer().modId.toUpperCase(), Level.FATAL, message)
	}
	
	@JvmStatic
	fun trace(message: String) {
		FMLRelaunchLog.log(Loader.instance().activeModContainer().modId.toUpperCase(), Level.TRACE, message)
	}
	
	@JvmStatic
	fun printStackTrace() {
		log("Stack trace: ")
		val stes = Thread.currentThread().stackTrace
		for (i in 2 until stes.size) log("\tat ${stes[i]}")
	}
	
	@JvmStatic
	fun say(sender: ICommandSender?, message: String, vararg format: Any) {
		sender ?: return
		
		sender.addChatMessage(ChatComponentTranslation(message, *format))
		log("[${sender.commandSenderName}!] ${StatCollector.translateToLocalFormatted(message, *format)}")
	}
	
	@JvmStatic
	fun sayToAllOnline(message: String, vararg format: Any) {
		if (isClient) return
		
		MinecraftServer.getServer().configurationManager.sendChatMsg(ChatComponentTranslation(message, *format))
		log("[SYSTEM!] ${StatCollector.translateToLocalFormatted(message, *format)}")
	}
	
	@JvmStatic
	@Deprecated("Untested")
	fun sayToAllOPs(message: String) {
		val ops = MinecraftServer.getServer().configurationManager.func_152606_n()
		MinecraftServer.getServer().configurationManager.playerEntityList.forEach { if ((it as EntityPlayer).commandSenderName in ops)  say(it, message) }
		log(message)
	}
	
	@JvmStatic
	val isServer: Boolean
		get() = FMLCommonHandler.instance().effectiveSide == Side.SERVER
	
	@JvmStatic
	val isClient
		get() = FMLCommonHandler.instance().effectiveSide == Side.CLIENT
	
	@JvmStatic
	fun toString(nbt: NBTTagCompound): String {
		val sb = StringBuilder("{\n")
		for (o in nbt.tagMap.entries) {
			val e = o as Map.Entry<String, NBTBase>
			val v = e.value
			if (v is NBTTagList || v is NBTTagCompound) {
				val arr = if (v is NBTTagList) toString(v).split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
				else toString(v as NBTTagCompound).split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
				sb.append("    ").append(e.key).append(" = ").append(arr[0]).append("\n")
				for (i in 1 until arr.size) sb.append("    ").append(arr[i]).append("\n")
			} else sb.append("    ").append(e.key).append(" = ").append(e.value).append("\n")
		}
		sb.append("}")
		return "$sb"
	}
	
	@JvmStatic
	fun toString(nbt: NBTTagList): String {
		val sb = StringBuilder("list [\n")
		for (obj in nbt.tagList) if (obj is NBTTagList || obj is NBTTagCompound) {
			val ts = if (obj is NBTTagList) toString(obj)
			else toString(obj as NBTTagCompound)
			for (s in ts.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) sb.append("    ").append(s).append("\n")
		} else sb.append(obj).append("\n")
		sb.append("]")
		return "$sb"
	}
	
	// backward compatibility
	/**
	 * Registers new entity
	 * @param entityClass Entity's class file
	 * @param name The name of this entity
	 * @param instance Mod instance
	 * @param id Mod-specific entity id
	 */
	@JvmStatic
	fun registerEntity(entityClass: Class<out Entity>, name: String, instance: Any, id: Int) {
		//val modid = FMLCommonHandler.instance().findContainerFor(instance).modId
		EntityRegistry.registerModEntity(entityClass, name, id, instance, 128, 1, true)
	}
	
	/**
	 * Sends entity to dimension without portal frames
	 * @param player player to send
	 * @param dimTo ID of the dimension the entity should be sent to
	 */
	@JvmStatic
	fun sendToDimensionWithoutPortal(player: EntityPlayer, dimTo: Int, x: Double, y: Double, z: Double) = sendToDimensionWithoutPortal(player as Entity, dimTo, x, y, z)
	
	/**
	 * Checks whether [target] is NOT in FOV of [observer]
	 * @author a_dizzle (minecraftforum.net)
	 */
	@JvmStatic
	fun isNotInFieldOfVision(target: EntityLivingBase, observer: EntityLivingBase) = isNotInFieldOfVision(target as Entity, observer)
	
	@JvmStatic
	fun sayToAllOnline(message: String) = sayToAllOnline(message, *emptyArray())
}