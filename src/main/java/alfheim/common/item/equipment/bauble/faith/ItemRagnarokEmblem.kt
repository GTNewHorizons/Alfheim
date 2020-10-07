package alfheim.common.item.equipment.bauble.faith

import alexsocol.asjlib.*
import alexsocol.asjlib.ItemNBTHelper.getBoolean
import alexsocol.asjlib.math.Vector3
import alexsocol.asjlib.render.ASJRenderHelper
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.api.event.PlayerInteractAdequateEvent
import alfheim.api.item.equipment.bauble.IManaDiscountBauble
import alfheim.common.achievement.AlfheimAchievements
import alfheim.common.core.handler.ragnarok.RagnarokEmblemStabilizationHandler
import alfheim.client.core.helper.IconHelper
import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.AlfheimItems
import alfheim.common.item.equipment.bauble.*
import alfheim.common.item.material.ElvenResourcesMetas
import alfheim.common.item.relic.*
import baubles.api.BaubleType
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.block.*
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.texture.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.passive.EntityVillager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.*
import net.minecraft.item.*
import net.minecraft.potion.*
import net.minecraft.util.*
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.event.entity.living.*
import org.lwjgl.opengl.GL11
import travellersgear.api.TravellersGearAPI
import vazkii.botania.api.item.IBaubleRender
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.api.subtile.ISpecialFlower
import vazkii.botania.common.core.handler.ConfigHandler
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.item.equipment.bauble.ItemBauble
import vazkii.botania.common.item.relic.*
import java.util.*

class ItemRagnarokEmblem: ItemBauble("ragnarokEmblem"), IBaubleRender, IManaDiscountBauble {
	
	lateinit var gemIcons: Array<IIcon>
	
	init {
		creativeTab = AlfheimTab
		setHasSubtypes(true)
	}
	
	lateinit var godRelics: Array<Item>
	
	override fun canEquip(stack: ItemStack?, player: EntityLivingBase?) =
		player is EntityPlayer && player.hasAchievement(AlfheimAchievements.ragnarok)
	
	override fun onEquippedOrLoadedIntoWorld(stack: ItemStack, player: EntityLivingBase) {
		if (stack.hasSoul(0))
			FaithHandlerThor.onEquipped(stack, player as? EntityPlayer ?: return, IFaithHandler.FaithBauble.EMBLEM)
	}
	
	override fun onWornTick(stack: ItemStack, player: EntityLivingBase?) {
		super.onWornTick(stack, player)
		
		if (player !is EntityPlayer) return
		
		if (stack.hasSoul(1))
			doReversedSif(stack, player)
		
		if (stack.hasSoul(2))
			doReversedNjord(stack, player)
		
		if (stack.hasSoul(3))
			FaithHandlerLoki.onWornTick(stack, player, IFaithHandler.FaithBauble.EMBLEM)
		
		if (stack.hasSoul(4) && !player.worldObj.isRemote && ManaItemHandler.requestManaExact(stack, player, 1, !player.worldObj.isRemote)) {
			player.addPotionEffect(PotionEffect(Potion.nightVision.id, 10, 0))
			player.removePotionEffect(Potion.blindness.id)
		}
		
		if (!::godRelics.isInitialized)
			godRelics = arrayOf(AlfheimItems.mjolnir, ModItems.infiniteFruit, AlfheimItems.daolos, AlfheimItems.gleipnir, AlfheimItems.gjallarhorn, AlfheimItems.gungnir,
								ModItems.thorRing, AlfheimItems.priestRingSif, AlfheimItems.priestRingNjord, ModItems.lokiRing, AlfheimItems.priestRingHeimdall, ModItems.odinRing, ModItems.aesirRing)
		
		if (ItemThorRing.getThorRing(player) != null ||
			ItemSifRing.getSifRing(player) != null ||
			ItemNjordRing.getNjordRing(player) != null ||
			ItemLokiRing.getLokiRing(player) != null ||
			ItemHeimdallRing.getHeimdallRing(player) != null ||
			ItemOdinRing.getOdinRing(player) != null ||
			godRelics.any {
				val slot = ASJUtilities.getSlotWithItem(it, player.inventory)
				if (slot == -1) return@any false
				val relic = player.inventory[slot]
				relic != null && ItemRelic.isRightPlayer(player, relic)
			}) {
			
			stack.instability++
			
			when {
				stack.instability == 600  -> ASJUtilities.say(player, "alfheimmisc.ragnarok.instability1")
				stack.instability == 1200 -> ASJUtilities.say(player, "alfheimmisc.ragnarok.instability2")
				stack.instability == 1800 -> ASJUtilities.say(player, "alfheimmisc.ragnarok.instability3")
				
				stack.instability >= 2400 -> {
					ASJUtilities.say(player, "alfheimmisc.ragnarok.instabilityS")
					
					PlayerHandler.getPlayerBaubles(player)[0] = null
				}
			}
			
			return
		}
		
		for (meta in 0 until ItemPriestEmblem.TYPES) {
			ItemPriestCloak.getCloak(meta, player) ?: continue
			
			stack.instability++
			
			when {
				stack.instability == 600  -> ASJUtilities.say(player, "alfheimmisc.ragnarok.instability1")
				stack.instability == 1200 -> ASJUtilities.say(player, "alfheimmisc.ragnarok.instability2")
				stack.instability == 1800 -> ASJUtilities.say(player, "alfheimmisc.ragnarok.instability3")
				
				stack.instability >= 2400 -> {
					ASJUtilities.say(player, "alfheimmisc.ragnarok.instabilityO")
					
					if (AlfheimCore.TravellersGearLoaded) {
						val tg = TravellersGearAPI.getExtendedInventory(player)
						tg[0] = null
						TravellersGearAPI.setExtendedInventory(player, tg)
					} else {
						PlayerHandler.getPlayerBaubles(player)[3] = null
					}
				}
			}
			
			return
		}
	}
	
	override fun canUnequip(stack: ItemStack?, player: EntityLivingBase?) =
		!getBoolean(stack, TAG_BOUND, false)
	
	override fun onUnequipped(stack: ItemStack, player: EntityLivingBase) {
		if (stack.hasSoul(0))
			FaithHandlerThor.onUnequipped(stack, player as? EntityPlayer ?: return, IFaithHandler.FaithBauble.EMBLEM)
	}
	
	fun doReversedSif(stack: ItemStack, player: EntityPlayer) {
		if (player.rng.nextInt(120) != 0) return
		if (!ManaItemHandler.requestManaExact(stack, player, 10, true)) return
		
		val (srcx, srcy, srcz) = Vector3.fromEntity(player).mf()
		val world = player.worldObj
		
		val range = 8
		val rangeY = 4
		val coords: MutableList<ChunkCoordinates?> = ArrayList()
		
		for (i in -range..range) {
			for (j in -rangeY..rangeY) {
				for (k in -range..range) {
					val x = srcx + i
					val y = srcy + j
					val z = srcz + k
					
					val block = world.getBlock(x, y, z)
					
					if (!(block is BlockBush || block is ISpecialFlower || block.isLeaves(world, x, y, z)))
						continue
					
					coords.add(ChunkCoordinates(x, y, z))
				}
			}
		}
		
		if (coords.isEmpty()) return
		
		val currCoords = coords.random()!!
		val block = world.getBlock(currCoords.posX, currCoords.posY, currCoords.posZ)
		val meta = world.getBlockMetadata(currCoords.posX, currCoords.posY, currCoords.posZ)
		val items = block.getDrops(world, currCoords.posX, currCoords.posY, currCoords.posZ, meta, 0)
		
		if (!world.isRemote) {
			world.setBlockToAir(currCoords.posX, currCoords.posY, currCoords.posZ)
			
			if (ConfigHandler.blockBreakParticles)
				world.playAuxSFX(2001, currCoords.posX, currCoords.posY, currCoords.posZ, Block.getIdFromBlock(block) + (meta shl 12))
			
			items.forEach { world.spawnEntityInWorld(EntityItem(world, currCoords.posX.toDouble() + 0.5, currCoords.posY.toDouble() + 0.5, currCoords.posZ.toDouble() + 0.5, it)) }
		}
	}
	
	fun doReversedNjord(stack: ItemStack, player: EntityPlayer) {
		if (player.rng.nextInt(60) != 0) return
		if (!ManaItemHandler.requestManaExact(stack, player, 10, true)) return
		
		val (srcx, srcy, srcz) = Vector3.fromEntity(player).mf()
		val world = player.worldObj
		
		val range = 8
		val rangeY = 4
		val coords: MutableList<ChunkCoordinates?> = ArrayList()
		
		for (i in -range..range) {
			for (j in -rangeY..rangeY) {
				for (k in -range..range) {
					val x = srcx + i
					val y = srcy + j
					val z = srcz + k
					
					val block = world.getBlock(x, y, z)
					
					if (!(block === Blocks.fire || block === Blocks.flowing_lava || block === Blocks.lava))
						continue
					
					coords.add(ChunkCoordinates(x, y, z))
				}
			}
		}
		
		if (coords.isEmpty()) return
		
		val currCoords = coords.random()!!
		val block = world.getBlock(currCoords.posX, currCoords.posY, currCoords.posZ)
		val meta = world.getBlockMetadata(currCoords.posX, currCoords.posY, currCoords.posZ)
		
		if (!world.isRemote) {
			if (block === Blocks.lava)
				world.setBlock(currCoords.posX, currCoords.posY, currCoords.posZ, Blocks.cobblestone)
			else
				world.setBlockToAir(currCoords.posX, currCoords.posY, currCoords.posZ)
			
			if (ConfigHandler.blockBreakParticles)
				world.playAuxSFX(2001, currCoords.posX, currCoords.posY, currCoords.posZ, Block.getIdFromBlock(block) + (meta shl 12))
		}
	}
	
	private var ItemStack.instability
		get() = ItemNBTHelper.getInt(this, TAG_INSTABILITY, 0)
		set(value) = ItemNBTHelper.setInt(this, TAG_INSTABILITY, value)
	
	override fun addHiddenTooltip(stack: ItemStack, player: EntityPlayer, list: MutableList<Any?>, adv: Boolean) {
		super.addHiddenTooltip(stack, player, list, adv)
		
		val souls = ItemNBTHelper.getByteArray(stack, TAG_CONSUMED, ByteArray(6))
		if (souls.any { it < 1 }) return
		
		addStringToTooltip(list, "\"${RagnarokEmblemStabilizationHandler.phrase1}")
		addStringToTooltip(list, "${RagnarokEmblemStabilizationHandler.phrase2}\"")
	}
	
	override fun getDiscount(stack: ItemStack, slot: Int, player: EntityPlayer): Float {
		return ItemNBTHelper.getByteArray(stack, TAG_CONSUMED, ByteArray(6)).sum() / 6f * 0.25f
	}
	
	override fun getUnlocalizedNameInefficiently(par1ItemStack: ItemStack) =
		super.getUnlocalizedNameInefficiently(par1ItemStack).replace("item\\.botania:".toRegex(), "item.${ModInfo.MODID}:")
	
	override fun getItemStackDisplayName(stack: ItemStack) =
		super.getItemStackDisplayName(stack).replace("&".toRegex(), "\u00a7")
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		itemIcon = IconHelper.forItem(reg, this)
		gemIcons = Array(2) { IconHelper.forItem(reg, this, "Render$it") }
	}
	
	override fun getIcon(stack: ItemStack, pass: Int) = if (getBoolean(stack, TAG_GEM_FLAG, false)) gemIcons.safeGet(pass) else itemIcon!!
	
	override fun requiresMultipleRenderPasses() = true
	
	override fun hasEffect(stack: ItemStack?, pass: Int) =
		pass == 0 && getBoolean(stack, TAG_BOUND, false)
	
	override fun getBaubleType(stack: ItemStack) = BaubleType.AMULET
	
	override fun onPlayerBaubleRender(stack: ItemStack?, event: RenderPlayerEvent, type: IBaubleRender.RenderType?) {
		if (type == IBaubleRender.RenderType.BODY) {
			val player = event.entityPlayer
			
			if (player != mc.thePlayer || mc.gameSettings.thirdPersonView != 0) {
				val invert = player.rng.nextBoolean()
				val (mx, my, mz) = Vector3().rand().sub(0.5).normalize().mul(0.1)
				val (x, y, z) = Vector3.fromEntity(player).add(0, player.height * 0.6875, 0)
				if (invert)
					mc.theWorld.spawnParticle("smoke", x + mx * 10, y + my * 10, z + mz * 10, -mx, -my, -mz)
				else
					mc.theWorld.spawnParticle("smoke", x, y, z, mx, my, mz)
			}
			
			mc.renderEngine.bindTexture(TextureMap.locationItemsTexture)
			IBaubleRender.Helper.rotateIfSneaking(player)
			val armor = player.getCurrentArmor(2) != null
			GL11.glTranslatef(-15 / 64f, 0f, -1 * if (armor) 0.21F else 0.15F)
			glScalef(0.5f)
			
			for ((id, baubleIcon) in gemIcons.withIndex()) {
				if (id != 0) ASJRenderHelper.setGlow()
				ItemRenderer.renderItemIn2D(Tessellator.instance, baubleIcon.maxU, baubleIcon.minV, baubleIcon.minU, baubleIcon.maxV, baubleIcon.iconWidth, baubleIcon.iconHeight, 1F / 32F)
				if (id != 0) ASJRenderHelper.discard()
			}
		}
	}
	
	companion object {
		
		const val TAG_BOUND = "ragnarok.bound"
		const val TAG_CONSUMED = "consumedPowers"
		const val TAG_GEM_FLAG = "renderGem"
		const val TAG_INSTABILITY = "instability"
		
		const val TAG_STOLEN = "ragnarok.stolen"
		
		init {
			eventForge()
		}
		
		private fun ItemStack.hasSoul(meta: Int): Boolean = ItemNBTHelper.getByteArray(this, TAG_CONSUMED, ByteArray(6))[meta] > 0
		
		fun getEmblem(player: EntityPlayer?, meta: Int = -1): ItemStack? {
			val baubles = PlayerHandler.getPlayerBaubles(player ?: return null)
			val stack = baubles[0] ?: return null
			if (stack.item !== AlfheimItems.ragnarokEmblem) return null
			if (meta != -1 && !stack.hasSoul(meta)) return null
			return stack
		}
		
		@SubscribeEvent
		fun onLivingHurt(e: LivingHurtEvent) {
			if (e.source.damageType != "player") return
			
			val player = e.source.entity as? EntityPlayer ?: return
			if (getEmblem(player, 4) != null && Math.random() > 0.25)
				e.ammount *= 1.5f
			
			if (getEmblem(player, 5) != null)
				e.ammount *= 1 + (1 - player.health / player.maxHealth) * 0.5f
		}
		
		@SubscribeEvent
		fun stealFromVillagers(e: PlayerInteractAdequateEvent.RightClick) {
			val target = e.entity as? EntityVillager ?: return
			if (e.player.heldItem != null && !e.player.isSneaking) return
			if (target.entityData.getBoolean(TAG_STOLEN)) return
			
			e.player.dropPlayerItemWithRandomChoice(if (target.isChild) ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.ElvenWeed) else ItemStack(Items.emerald), true)
			target.entityData.setBoolean(TAG_STOLEN, true)
		}
		
		@SubscribeEvent
		fun restoreStolenVillager(e: LivingEvent.LivingUpdateEvent) {
			val target = e.entity as? EntityVillager ?: return
			if (!target.entityData.getBoolean(TAG_STOLEN)) return
			if (target.worldObj.rand.nextInt(10000) != 0) return
			target.entityData.setBoolean(TAG_STOLEN, false)
		}
	}
}