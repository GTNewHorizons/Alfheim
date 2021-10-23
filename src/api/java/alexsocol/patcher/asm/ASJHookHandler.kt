package alexsocol.patcher.asm

import alexsocol.asjlib.*
import alexsocol.asjlib.render.ICustomArmSwingEndEntity
import alexsocol.patcher.PatcherConfigHandler
import alexsocol.patcher.event.*
import com.google.common.collect.*
import cpw.mods.fml.client.FMLClientHandler
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import gloomyfolken.hooklib.asm.*
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.gui.*
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.entity.Render
import net.minecraft.command.*
import net.minecraft.command.server.CommandSummon
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.*
import net.minecraft.entity.EntityList.EntityEggInfo
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.boss.*
import net.minecraft.entity.effect.*
import net.minecraft.entity.monster.*
import net.minecraft.entity.passive.EntityMooshroom
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.init.Blocks
import net.minecraft.inventory.*
import net.minecraft.item.*
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.potion.*
import net.minecraft.tileentity.TileEntityFurnace
import net.minecraft.util.ResourceLocation
import net.minecraft.world.*
import net.minecraft.world.biome.BiomeGenBase
import net.minecraftforge.client.event.EntityViewRenderEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.util.ForgeDirection
import org.lwjgl.opengl.*
import org.objectweb.asm.Opcodes
import java.nio.FloatBuffer
import java.util.*
import kotlin.math.*

@Suppress("UNUSED_PARAMETER")
object ASJHookHandler {
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS, targetMethod = "<init>", createMethod = true, superClass = "net/minecraft/entity/effect/EntityWeatherEffect.${Opcodes.ALOAD}.1.(Lnet/minecraft/world/World;)V")
	fun EntityLightningBolt(thiz: EntityLightningBolt, world: World) {
		thiz.lightningState = 2
		thiz.boltVertex = (Math.random() * Long.MAX_VALUE).toLong()
		thiz.boltLivingTime = ASJUtilities.randInBounds(1, 3)
		
//		if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doFireTick") && (world.difficultySetting == EnumDifficulty.NORMAL || world.difficultySetting == EnumDifficulty.HARD) && world.doChunksNearChunkExist(MathHelper.floor_double(p_i1703_2_), MathHelper.floor_double(p_i1703_4_), MathHelper.floor_double(p_i1703_6_), 10)) {
//			var i = MathHelper.floor_double(p_i1703_2_)
//			var j = MathHelper.floor_double(p_i1703_4_)
//			var k = MathHelper.floor_double(p_i1703_6_)
//			if (world.getBlock(i, j, k).getMaterial() === Material.air && Blocks.fire.canPlaceBlockAt(world, i, j, k)) {
//				world.setBlock(i, j, k, Blocks.fire)
//			}
//			i = 0
//			while (i < 4) {
//				j = MathHelper.floor_double(p_i1703_2_) + thiz.rand.nextInt(3) - 1
//				k = MathHelper.floor_double(p_i1703_4_) + thiz.rand.nextInt(3) - 1
//				val l: Int = MathHelper.floor_double(p_i1703_6_) + thiz.rand.nextInt(3) - 1
//				if (world.getBlock(j, k, l).getMaterial() === Material.air && Blocks.fire.canPlaceBlockAt(world, j, k, l)) {
//					world.setBlock(j, k, l, Blocks.fire)
//				}
//				++i
//			}
//		}
	}
	
	@JvmStatic // AIOOBE 257+ crash fix
	@Hook(returnCondition = ReturnCondition.ALWAYS, targetMethod = "<clinit>")
	fun EntityEnderman(thiz: EntityEnderman?) {
		EntityEnderman.attackingSpeedBoostModifierUUID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0")
		EntityEnderman.attackingSpeedBoostModifier = AttributeModifier(EntityEnderman.attackingSpeedBoostModifierUUID, "Attacking speed boost", 6.199999809265137, 0).setSaved(false)
		
		EntityEnderman.setCarriable(Blocks.grass, true)
		EntityEnderman.setCarriable(Blocks.dirt, true)
		EntityEnderman.setCarriable(Blocks.sand, true)
		EntityEnderman.setCarriable(Blocks.gravel, true)
		EntityEnderman.setCarriable(Blocks.yellow_flower, true)
		EntityEnderman.setCarriable(Blocks.red_flower, true)
		EntityEnderman.setCarriable(Blocks.brown_mushroom, true)
		EntityEnderman.setCarriable(Blocks.red_mushroom, true)
		EntityEnderman.setCarriable(Blocks.tnt, true)
		EntityEnderman.setCarriable(Blocks.cactus, true)
		EntityEnderman.setCarriable(Blocks.clay, true)
		EntityEnderman.setCarriable(Blocks.pumpkin, true)
		EntityEnderman.setCarriable(Blocks.melon_block, true)
		EntityEnderman.setCarriable(Blocks.mycelium, true)
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ON_TRUE)
	fun spawnEntityInWorld(world: World, target: Entity?): Boolean {
		if (target !is EntityWeatherEffect)
			return false
		
		world.addWeatherEffect(target)
		return false
	}
	
	@JvmStatic
	@Hook
	fun damageArmor(entity: EntityLivingBase, damage: Float) {
		if (!PatcherConfigHandler.damageMobArmor) return
		
		val dmg = max(damage / 4f, 1f).I
		for (i in 1..4) entity.getEquipmentInSlot(i)?.damageItem(dmg, entity)
	}
	
	@JvmStatic
	@Hook(targetMethod = "<clinit>", injectOnExit = true)
	fun `EntityList$clinit`(e: EntityList?) {
		addEntityEgg(EntityGiantZombie::class.java, 0x00AFAF, 0x4D6341) // Giant
		addEntityEgg(EntityDragon::class.java, 0x0E0E0E, 0xCC00FA) // Ender Dragon
		addEntityEgg(EntityWither::class.java, 0x141414, 0x5C5C5C) // Wither Boss
		addEntityEgg(EntitySnowman::class.java, 0xEEFFFF, 0xFFA221) // Snowman
		addEntityEgg(EntityIronGolem::class.java, 0xC5C2C1, 0xFFE1CC) // Iron Golem
		
		if (PatcherConfigHandler.lightningID != -1) EntityList.addMapping(EntityLightningBolt::class.java, "LightningBolt", PatcherConfigHandler.lightningID)
	}
	
	// NEI function copy, added check
	fun addEntityEgg(entity: Class<*>, i: Int, j: Int) {
		val id = EntityList.classToIDMapping[entity] as Int
		if (EntityList.entityEggs[id] != null) return
		EntityList.entityEggs[id] = EntityEggInfo(id, i, j)
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS, createMethod = true)
	fun getCommandAliases(c: CommandGameMode): List<String> {
		return listOf("gm")
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS, createMethod = true)
	fun getCommandAliases(c: CommandDefaultGameMode): List<String>? {
		return null
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS, createMethod = true)
	fun getCommandUsage(c: CommandSummon, sender: ICommandSender?): String {
		return "commands.summon.usage.new"
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ON_TRUE)
	fun processCommand(c: CommandSummon, sender: ICommandSender?, args: Array<String?>): Boolean {
		val count = args.getOrNull(1) ?: return false
		if (!count.startsWith('x')) return false
		
		val newArgs = args.toMutableList().apply { removeAt(1) }.toTypedArray()
		for (i in 0 until count.substring(1).toInt())
			c.processCommand(sender, newArgs)
		
		return true
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun func_147182_d(c: CommandSummon): Array<String> {
		return (EntityList.stringToClassMapping.keys as Set<String>).toTypedArray()
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun addTabCompletionOptions(c: CommandSummon, sender: ICommandSender?, args: Array<String?>): MutableList<*>? {
		if (args.size != 1) return null
		
		val last = args[0]!!
		val sb = StringBuilder()
		
		try {
			var fullNames: Iterable<String> = func_147182_d(c).toList()
			val ends = last.matches(Regex(".*\\W$"))
			if (last.isNotEmpty()) fullNames = fullNames.filter { it.startsWith(last, true) }
			
			fullNames = fullNames.mapTo(HashSet()) { mob ->
				sb.setLength(0)
				
				var doBreak = false
				for ((id, it) in mob.withIndex()) {
					if (id < last.length) {
						sb.append(it)
						continue
					} else if ("$it".matches(Regex("\\W"))) {
						if (doBreak) break
						
						if (ends) {
							doBreak = true
							sb.append(it)
						} else break
					} else {
						sb.append(it)
					}
				}
				
				sb.toString()
			}
			
			return CommandBase.getListOfStringsMatchingLastWord(args, *fullNames.toTypedArray())
		} catch (e: Throwable) {
			e.printStackTrace()
			return null
		}
	}
	
	@JvmStatic
	@Hook
	fun onCollideWithPlayer(arrow: EntityArrow, player: EntityPlayer) {
		if (arrow.canBePickedUp == 0 && player.capabilities.isCreativeMode) arrow.canBePickedUp = 2
	}
	
	@JvmStatic
	@Hook(targetMethod = "<init>")
	fun BiomeGenBase(thiz: BiomeGenBase, id: Int, register: Boolean) {
		if (PatcherConfigHandler.biomeDuplication && BiomeGenBase.getBiomeGenArray()[id] != null)
			throw IllegalArgumentException("Biome with id $id is already registered!")
	}
	
	@JvmStatic
	@Hook(targetMethod = "<init>")
	fun Potion(id: Int, bad: Boolean, color: Int) {
		if (PatcherConfigHandler.potionDuplication && Potion.potionTypes[id] != null)
			throw IllegalArgumentException("Potion with id $id is already registered!")
	}
	
	// stack attributes fix
	
	@JvmStatic
	@Hook(isMandatory = true, returnCondition = ReturnCondition.ALWAYS)
	fun getAttributeModifiers(stack: ItemStack): Multimap<Any, Any> {
		val map = stack.item.getAttributeModifiers(stack) ?: HashMultimap.create<Any, Any>()
		
		if (stack.stackTagCompound?.hasKey("AttributeModifiers", 9) != true)
			return map
		
		val list = stack.stackTagCompound.getTagList("AttributeModifiers", 10)
		
		for (i in 0 until list.tagCount()) {
			val nbt = list.getCompoundTagAt(i)
			val modifier = SharedMonsterAttributes.readAttributeModifierFromNBT(nbt)
			
			if (modifier.id.leastSignificantBits != 0L && modifier.id.mostSignificantBits != 0L)
				map.put(nbt.getString("AttributeName"), modifier)
		}
		
		return map
	}
	
	// stack NBT fix
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ON_NOT_NULL)
	fun writeToNBT(stack: ItemStack, nbt: NBTTagCompound): NBTTagCompound? {
		if (!PatcherConfigHandler.textIDs) return null
		
		nbt.setString("id", GameRegistry.findUniqueIdentifierFor(stack.field_151002_e).toString())
		nbt.setInteger("Count", stack.stackSize)
		nbt.setInteger("Damage", stack.itemDamage)
		
		stack.stackTagCompound?.let { nbt.setTag("tag", it) }
		
		return nbt
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ON_TRUE)
	fun readFromNBT(stack: ItemStack, nbt: NBTTagCompound): Boolean {
		if (!PatcherConfigHandler.textIDs) return false
		if (nbt.hasNoTags()) return true
		
		migrate(nbt)
		
		val id = nbt.getString("id")
		if (id.isBlank()) return true
		
		val (modid, name) = id.split(':')
		stack.func_150996_a(GameRegistry.findItem(modid, name))
		stack.stackSize = nbt.getInteger("Count")
		stack.itemDamage = max(0, nbt.getInteger("Damage"))
		
		if (nbt.hasKey("tag", 10))
			stack.stackTagCompound = nbt.getCompoundTag("tag")
		
		return true
	}
	
	private fun migrate(nbt: NBTTagCompound) {
		if (!nbt.hasKey("id", 2)) return
		
		val stack = ItemStack(Item.getItemById(nbt.getShort("id").toInt()), nbt.getByte("Count").toInt(), max(0, nbt.getShort("Damage").toInt()))
		
		if (nbt.hasKey("tag", 10))
			stack.stackTagCompound = nbt.getCompoundTag("tag")
		
		nbt.removeTag("id")
		nbt.removeTag("Count")
		nbt.removeTag("Damage")
		nbt.removeTag("tag")
		
		stack.writeToNBT(nbt)
	}
	
//	@JvmStatic
//	@Hook(returnCondition = ReturnCondition.ON_TRUE)
//	fun writeItemStackToBuffer(buf: PacketBuffer, stack: ItemStack?): Boolean {
//		if (!PatcherConfigHandler.textIDs) return false
//
//		if (stack == null) {
//			buf.writeStringToBuffer("")
//			return true
//		}
//
//		val name = GameRegistry.findUniqueIdentifierFor(stack.item).toString()
//		buf.writeInt(name.length)
//		buf.writeStringToBuffer(name)
//		buf.writeInt(stack.stackSize)
//		buf.writeInt(stack.itemDamage)
//		var nbt: NBTTagCompound? = null
//		if (stack.item.isDamageable || stack.item.shareTag)
//			nbt = stack.stackTagCompound
//
//		buf.writeNBTTagCompoundToBuffer(nbt)
//
//		return true
//	}
//
//	@JvmStatic
//	@Hook(returnCondition = ReturnCondition.ON_NOT_NULL)
//	fun readItemStackFromBuffer(buf: PacketBuffer): ItemStack? {
//		if (!PatcherConfigHandler.textIDs) return null
//
//		val id = buf.readStringFromBuffer(buf.readInt())
//		if (id.isEmpty())
//			return null
//
//		val (modid, name) = id.split(':')
//		val item = GameRegistry.findItem(modid, name) ?: return null
//		val count = buf.readInt()
//		val meta = buf.readInt()
//		val stack = ItemStack(item, count, meta)
//		stack.stackTagCompound = buf.readNBTTagCompoundFromBuffer()
//
//		return stack
//	}
	
	// Events
	
	@JvmStatic
	@Hook(injectOnExit = true)
	fun wakeAllPlayers(world: WorldServer) {
		MinecraftForge.EVENT_BUS.post(ServerWakeUpEvent(world))
	}
	
	@JvmStatic
	@Hook(injectOnExit = true, isMandatory = true)
	fun onNewPotionEffect(e: EntityLivingBase, pe: PotionEffect) {
		MinecraftForge.EVENT_BUS.post(LivingPotionEvent.Add.Post(e, pe))
	}
	
	@JvmStatic
	@Hook(injectOnExit = true, isMandatory = true)
	fun onChangedPotionEffect(e: EntityLivingBase, pe: PotionEffect, was: Boolean) {
		MinecraftForge.EVENT_BUS.post(LivingPotionEvent.Change.Post(e, pe, was))
	}
	
	@JvmStatic
	@Hook(injectOnExit = true, isMandatory = true)
	fun onFinishedPotionEffect(e: EntityLivingBase, pe: PotionEffect) {
		MinecraftForge.EVENT_BUS.post(LivingPotionEvent.Remove.Post(e, pe))
	}
	
	// Portal closes GUI fix
	
	var portalHook = false
	
	@JvmStatic
	@Hook
	fun onLivingUpdate(player: EntityPlayerSP) {
		portalHook = PatcherConfigHandler.portalHook
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ON_TRUE)
	@SideOnly(Side.CLIENT)
	fun displayGuiScreen(mc: Minecraft, gui: GuiScreen?): Boolean {
		return if (portalHook && mc.thePlayer?.inPortal == true) {
			portalHook = false
			gui == null
		} else false
	}
	
	// #### BlockFence connection fix ####
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ON_TRUE)
	fun canConnectFenceTo(fence: BlockFence, world: IBlockAccess, x: Int, y: Int, z: Int) = world.getBlock(x, y, z) is BlockFence
	
	// #### BlockWall fix ####
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS, createMethod = true)
	fun isSideSolid(wall: BlockWall, world: IBlockAccess, x: Int, y: Int, z: Int, side: ForgeDirection) = when (side) {
		ForgeDirection.DOWN -> true
		ForgeDirection.UP   -> wall.blockBoundsMaxY == 1.0
		else                -> false
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun renderBlockWall(render: RenderBlocks, block: BlockWall, x: Int, y: Int, z: Int): Boolean {
		val flag = block.canConnectWallTo(render.blockAccess, x - 1, y, z)
		val flag1 = block.canConnectWallTo(render.blockAccess, x + 1, y, z)
		val flag2 = block.canConnectWallTo(render.blockAccess, x, y, z - 1)
		val flag3 = block.canConnectWallTo(render.blockAccess, x, y, z + 1)
		val flag4 = flag2 && flag3 && !flag && !flag1
		val flag5 = !flag2 && !flag3 && flag && flag1
		val doNotRenderPost = render.blockAccess.getBlock(x, y + 1, z) !is BlockWall && render.blockAccess.getBlock(x, y + 1, z) !is BlockSkull && render.blockAccess.getBlock(x, y - 1, z) !is BlockWall
		
		if ((flag4 || flag5) && doNotRenderPost) {
			if (flag4) {
				render.setRenderBounds(0.3125, 0.0, 0.0, 0.6875, 0.8125, 1.0)
				render.renderStandardBlock(block, x, y, z)
			} else {
				render.setRenderBounds(0.0, 0.0, 0.3125, 1.0, 0.8125, 0.6875)
				render.renderStandardBlock(block, x, y, z)
			}
		} else {
			render.setRenderBounds(0.25, 0.0, 0.25, 0.75, 1.0, 0.75)
			render.renderStandardBlock(block, x, y, z)
			if (flag) {
				render.setRenderBounds(0.0, 0.0, 0.3125, 0.25, 0.8125, 0.6875)
				render.renderStandardBlock(block, x, y, z)
			}
			if (flag1) {
				render.setRenderBounds(0.75, 0.0, 0.3125, 1.0, 0.8125, 0.6875)
				render.renderStandardBlock(block, x, y, z)
			}
			if (flag2) {
				render.setRenderBounds(0.3125, 0.0, 0.0, 0.6875, 0.8125, 0.25)
				render.renderStandardBlock(block, x, y, z)
			}
			if (flag3) {
				render.setRenderBounds(0.3125, 0.0, 0.75, 0.6875, 0.8125, 1.0)
				render.renderStandardBlock(block, x, y, z)
			}
		}
		block.setBlockBoundsBasedOnState(render.blockAccess, x, y, z)
		return true
	}
	
	// #### BlockWall fix end ####
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS, isMandatory = true)
	fun updatePotionEffects(e: EntityLivingBase) {
		try {
			val iterator = e.activePotionsMap.keys.iterator()
			
			while (iterator.hasNext()) {
				val integer = iterator.next() as Int
				val potioneffect = e.activePotionsMap[integer] as PotionEffect
				
				if (potioneffect.potionID < 0)
					throw IllegalArgumentException("Potion ID is negative (${potioneffect.potionID}). Did you set some ID to 128+ without potion fixing mod?")
				
				if (potioneffect.potionID !in Potion.potionTypes.indices || Potion.potionTypes[potioneffect.potionID] == null)
					throw IllegalArgumentException("Potential potion ID conflict #${potioneffect.potionID}")
				
				if (!potioneffect.onUpdate(e)) {
					//if (!e.worldObj.isRemote) {
					iterator.remove()
					e.onFinishedPotionEffect(potioneffect)
					//}
				} else if (potioneffect.duration % 600 == 0) {
					e.onChangedPotionEffect(potioneffect, false)
				}
			}
			
			var i: Int
			
			if (e.potionsNeedUpdate) {
				if (!e.worldObj.isRemote) {
					if (e.activePotionsMap.isEmpty()) {
						e.dataWatcher.updateObject(8, 0.toByte())
						e.dataWatcher.updateObject(7, 0)
						e.isInvisible = false
					} else {
						i = PotionHelper.calcPotionLiquidColor(e.activePotionsMap.values)
						e.dataWatcher.updateObject(8, (if (PotionHelper.func_82817_b(e.activePotionsMap.values)) 1 else 0).toByte())
						e.dataWatcher.updateObject(7, i)
						e.isInvisible = e.isPotionActive(Potion.invisibility.id)
					}
				}
				
				e.potionsNeedUpdate = false
			}
			
			i = e.dataWatcher.getWatchableObjectInt(7)
			val flag1 = e.dataWatcher.getWatchableObjectByte(8) > 0
			
			if (i > 0) {
				var flag: Boolean
				
				flag = if (!e.isInvisible) {
					e.worldObj.rand.nextBoolean()
				} else {
					e.worldObj.rand.nextInt(15) == 0
				}
				
				if (flag1) {
					flag = flag and (e.worldObj.rand.nextInt(5) == 0)
				}
				
				if (flag) {
					val d0 = (i shr 16 and 255).D / 255.0
					val d1 = (i shr 8 and 255).D / 255.0
					val d2 = (i and 255).D / 255.0
					e.worldObj.spawnParticle(if (flag1) "mobSpellAmbient" else "mobSpell", e.posX + (e.worldObj.rand.nextDouble() - 0.5) * e.width.D, e.posY + e.worldObj.rand.nextDouble() * e.height.D - e.yOffset.D, e.posZ + (e.worldObj.rand.nextDouble() - 0.5) * e.width.D, d0, d1, d2)
				}
			}
		} catch (ex: ConcurrentModificationException) {
			ASJUtilities.log("Well, that was expected. Ignore.")
			ex.printStackTrace()
		} catch (e: Exception) {
			ASJReflectionHelper.setValue(message_f, e, ASJReflectionHelper.getValue<String>(message_f, e) + " Stop harassing me for your potion id conflicts! Go install Dragon API or smth! This hook has nothing to do with your problem! F*ck off!", true)
			val stackTrace = e.stackTrace.filter { "alexsocol" !in it.className }.toTypedArray()
			ASJReflectionHelper.setValue(stackTrace_f, e, stackTrace)
			throw e
		}
	}
	
	private val message_f = ASJReflectionHelper.getField(java.lang.Throwable::class.java, "detailMessage")
	private val stackTrace_f = ASJReflectionHelper.getField(java.lang.Throwable::class.java, "stackTrace")
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ON_TRUE, targetMethod = "func_150000_e", isMandatory = true)
	fun tryToCreatePortal(portal: BlockPortal, world: World, x: Int, y: Int, z: Int) = MinecraftForge.EVENT_BUS.post(NetherPortalActivationEvent(world, x, y, z))
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS, isMandatory = true)
	fun extinguishFire(world: World, player: EntityPlayer?, x: Int, y: Int, z: Int, side: Int): Boolean {
		var i = x
		var j = y
		var k = z
		if (side == 0) --j
		if (side == 1) ++j
		if (side == 2) --k
		if (side == 3) ++k
		if (side == 4) --i
		if (side == 5) ++i
		val block = world.getBlock(i, j, k)
		
		val breakable = if (player != null) block.getPlayerRelativeBlockHardness(player, world, i, j, k) > 0f || player.capabilities.isCreativeMode else true
		
		if (block.material === Material.fire && breakable) {
			world.playAuxSFXAtEntity(player, 1004, i, j, k, 0)
			world.setBlockToAir(i, j, k)
			return true
		}
		return false
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun getNightVisionBrightness(render: EntityRenderer, player: EntityPlayer, partialTicks: Float) = if ((player.getActivePotionEffect(Potion.nightVision.id)?.duration ?: 0) > 0) 1f else 0f
	
	// Fix nbt clearing in Enchanting Table
	
	val mergeItemStack by lazy {
		ASJReflectionHelper.getMethod(Container::class.java, arrayOf("mergeItemStack", "func_75135_a"), arrayOf(ItemStack::class.java, Int::class.java, Int::class.java, Boolean::class.java))?.also {
			it.isAccessible = true
		}
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun transferStackInSlot(container: ContainerEnchantment, player: EntityPlayer?, slotID: Int): ItemStack? {
		var itemstack: ItemStack? = null
		val slot = container.inventorySlots[slotID] as Slot?
		if (slot != null && slot.hasStack) {
			val itemstack1 = slot.stack
			itemstack = itemstack1.copy()
			if (slotID == 0) {
				// I'm sooo sorry but all inheritance will just fuck everything up
				if (mergeItemStack?.invoke(container, itemstack1, 1, 37, true) == false) return null
			} else {
				if ((container.inventorySlots[0] as Slot).hasStack || !(container.inventorySlots[0] as Slot).isItemValid(itemstack1)) return null
				
				if (itemstack1.hasTagCompound() && itemstack1.stackSize == 1) {
					(container.inventorySlots[0] as Slot).putStack(itemstack1.copy())
					itemstack1.stackSize = 0
				} else if (itemstack1.stackSize >= 1) {
					val copy = itemstack1.copy()
					copy.stackSize = 1
					(container.inventorySlots[0] as Slot).putStack(copy)
					--itemstack1.stackSize
				}
			}
			if (itemstack1.stackSize == 0) slot.putStack(null as ItemStack?)
			else slot.onSlotChanged()
			
			if (itemstack1.stackSize == itemstack.stackSize) return null
			
			slot.onPickupFromSlot(player, itemstack1)
		}
		return itemstack
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ON_TRUE)
	fun itemInteractionForEntity(item: ItemNameTag, stack: ItemStack, player: EntityPlayer?, target: EntityLivingBase?): Boolean {
		if (!stack.hasDisplayName() && target is EntityLiving) {
			target.customNameTag = ""
			return true
		}
		
		return false
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ON_NOT_NULL)
	fun onSheared(entity: EntityMooshroom, item: ItemStack?, world: IBlockAccess?, x: Int, y: Int, z: Int, fortune: Int) = if (entity.isDead) ArrayList<Any?>() else null
	
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook
	fun getSubBlocks(block: BlockTallGrass, item: Item?, tab: CreativeTabs?, list: MutableList<ItemStack?>) {
		list.add(ItemStack(item))
	}
	
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook(injectOnExit = true)
	fun getSubBlocks(block: BlockDirt, item: Item?, tab: CreativeTabs?, list: MutableList<ItemStack?>) {
		list.add(list.size - 1, ItemStack(item, 1, 1))
	}
	
	// int overflow fix
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun getBurnTimeRemainingScaled(furnace: TileEntityFurnace, mod: Int): Int {
		if (furnace.currentItemBurnTime == 0) {
			furnace.currentItemBurnTime = 200
		}
		
		return (furnace.furnaceBurnTime.D / furnace.currentItemBurnTime * mod).I
	}
	
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook(isMandatory = true, returnCondition = ReturnCondition.ON_TRUE)
	fun doRenderShadowAndFire(render: Render, entity: Entity, x: Double, y: Double, z: Double, yaw: Float, ticks: Float): Boolean {
		return MinecraftForge.EVENT_BUS.post(RenderEntityPostEvent(entity, x, y, z, yaw))
	}
	
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun setupFog(renderer: EntityRenderer, fogMode: Int, renderPartialTicks: Float) {
		val entitylivingbase = renderer.mc.renderViewEntity
		val creative = if (entitylivingbase is EntityPlayer) entitylivingbase.capabilities.isCreativeMode else false
		
		fun setFogColorBuffer(p_78469_1_: Float, p_78469_2_: Float, p_78469_3_: Float, p_78469_4_: Float): FloatBuffer {
			renderer.fogColorBuffer.clear()
			renderer.fogColorBuffer.put(p_78469_1_).put(p_78469_2_).put(p_78469_3_).put(p_78469_4_)
			renderer.fogColorBuffer.flip()
			return renderer.fogColorBuffer
		}
		
		if (fogMode == 999) {
			GL11.glFog(GL11.GL_FOG_COLOR, setFogColorBuffer(0f, 0f, 0f, 1f))
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR)
			GL11.glFogf(GL11.GL_FOG_START, 0f)
			GL11.glFogf(GL11.GL_FOG_END, 8f)
			
			if (GLContext.getCapabilities().GL_NV_fog_distance) {
				GL11.glFogi(NVFogDistance.GL_FOG_DISTANCE_MODE_NV, NVFogDistance.GL_EYE_RADIAL_NV)
			}
			
			GL11.glFogf(GL11.GL_FOG_START, 0f)
		} else {
			GL11.glFog(GL11.GL_FOG_COLOR, setFogColorBuffer(renderer.fogColorRed, renderer.fogColorGreen, renderer.fogColorBlue, 1f))
			GL11.glNormal3f(0f, -1f, 0f)
			GL11.glColor4f(1f, 1f, 1f, 1f)
			val block = ActiveRenderInfo.getBlockAtEntityViewpoint(renderer.mc.theWorld, entitylivingbase, renderPartialTicks)
			var f1: Float
			
			val event = EntityViewRenderEvent.FogDensity(renderer, entitylivingbase, block, renderPartialTicks.D, 0.1f)
			
			if (MinecraftForge.EVENT_BUS.post(event)) {
				GL11.glFogf(GL11.GL_FOG_DENSITY, event.density)
			} else if (entitylivingbase.isPotionActive(Potion.blindness) && !creative) {
				f1 = 5f
				val j = entitylivingbase.getActivePotionEffect(Potion.blindness.id)!!.duration
				
				if (j < 20) {
					f1 = 5f + (renderer.farPlaneDistance - 5f) * (1f - j.F / 20f)
				}
				
				GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR)
				
				if (fogMode < 0) {
					GL11.glFogf(GL11.GL_FOG_START, 0f)
					GL11.glFogf(GL11.GL_FOG_END, f1 * 0.8f)
				} else {
					GL11.glFogf(GL11.GL_FOG_START, f1 * 0.25f)
					GL11.glFogf(GL11.GL_FOG_END, f1)
				}
				
				if (GLContext.getCapabilities().GL_NV_fog_distance) {
					GL11.glFogi(NVFogDistance.GL_FOG_DISTANCE_MODE_NV, NVFogDistance.GL_EYE_RADIAL_NV)
				}
			} else if (renderer.cloudFog) {
				GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP)
				GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1f)
			} else if (block.material === Material.water) {
				GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP)
				
				if (entitylivingbase.isPotionActive(Potion.waterBreathing)) {
					GL11.glFogf(GL11.GL_FOG_DENSITY, if (PatcherConfigHandler.clearWater) 0.01f else 0.05f)
				} else {
					GL11.glFogf(GL11.GL_FOG_DENSITY, if (PatcherConfigHandler.clearWater) 0.01f else 0.1f - EnchantmentHelper.getRespiration(entitylivingbase).F * 0.03f)
				}
			} else if (block.material === Material.lava) {
				GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP)
				GL11.glFogf(GL11.GL_FOG_DENSITY, 2f)
			} else {
				f1 = renderer.farPlaneDistance
				
				if (renderer.mc.theWorld.provider.worldHasVoidParticles && PatcherConfigHandler.voidFog && !creative) {
					var d0 = (entitylivingbase.getBrightnessForRender(renderPartialTicks) and 15728640 shr 20).D / 16.0 + (entitylivingbase.lastTickPosY + (entitylivingbase.posY - entitylivingbase.lastTickPosY) * renderPartialTicks.D + 4.0) / 32.0
					
					if (d0 < 1.0) {
						if (d0 < 0.0) {
							d0 = 0.0
						}
						
						d0 *= d0
						var f2 = 100f * d0.F
						
						if (f2 < 5f) {
							f2 = 5f
						}
						
						if (f1 > f2) {
							f1 = f2
						}
					}
				}
				
				GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR)
				
				if (fogMode < 0) {
					GL11.glFogf(GL11.GL_FOG_START, 0f)
					GL11.glFogf(GL11.GL_FOG_END, f1)
				} else {
					GL11.glFogf(GL11.GL_FOG_START, f1 * 0.75f)
					GL11.glFogf(GL11.GL_FOG_END, f1)
				}
				
				if (GLContext.getCapabilities().GL_NV_fog_distance) {
					GL11.glFogi(NVFogDistance.GL_FOG_DISTANCE_MODE_NV, NVFogDistance.GL_EYE_RADIAL_NV)
				}
				
				if (renderer.mc.theWorld.provider.doesXZShowFog(entitylivingbase.posX.I, entitylivingbase.posZ.I)) {
					GL11.glFogf(GL11.GL_FOG_START, f1 * 0.05f)
					GL11.glFogf(GL11.GL_FOG_END, min(f1, 192f) * 0.5f)
				}
				
				MinecraftForge.EVENT_BUS.post(EntityViewRenderEvent.RenderFogEvent(renderer, entitylivingbase, block, renderPartialTicks.D, fogMode, f1))
			}
			
			GL11.glEnable(GL11.GL_COLOR_MATERIAL)
			GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT)
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Synchronized
	@JvmStatic // fixing some occasional OptiFine crashes
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun deleteDisplayLists(gla: GLAllocation?, id: Int) {
		if (GLAllocation.mapDisplayLists.contains(id)) GL11.glDeleteLists(id, GLAllocation.mapDisplayLists.remove(id) as Int)
	}
	
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook(targetMethod = "<clinit>", injectOnExit = true)
	fun GuiChat_clinit(gui: GuiChat?) {
		GuiChat.field_152175_f.add("file")
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ON_TRUE)
	fun trackBrokenTexture(handler: FMLClientHandler, resourceLocation: ResourceLocation, error: String?): Boolean {
		if (error == null) {
			handler.trackBrokenTexture(resourceLocation, "Unknown Error")
			return true
		}
		
		return false
	}
	
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS, injectOnExit = true)
	fun getArmSwingAnimationEnd(e: EntityLivingBase, @Hook.ReturnValue result: Int) = if (e is ICustomArmSwingEndEntity) e.getArmSwingAnimationEnd() else result
}