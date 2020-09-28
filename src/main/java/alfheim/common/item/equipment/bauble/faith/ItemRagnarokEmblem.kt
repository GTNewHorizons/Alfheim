package alfheim.common.item.equipment.bauble.faith

import alexsocol.asjlib.*
import alexsocol.asjlib.ItemNBTHelper.getBoolean
import alexsocol.asjlib.ItemNBTHelper.getByteArray
import alexsocol.asjlib.ItemNBTHelper.setBoolean
import alexsocol.asjlib.ItemNBTHelper.setByteArray
import alexsocol.asjlib.math.Vector3
import alexsocol.asjlib.render.ASJRenderHelper
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.common.achievement.AlfheimAchievements
import alfheim.common.block.tile.*
import alfheim.common.core.helper.IconHelper
import alfheim.common.core.util.AlfheimTab
import alfheim.common.entity.FakeLightning
import alfheim.common.entity.item.EntityItemImmortal
import alfheim.common.item.AlfheimItems
import alfheim.common.item.equipment.bauble.ItemPriestEmblem
import alfheim.common.network.Message1d
import baubles.api.BaubleType
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.common.eventhandler.*
import cpw.mods.fml.common.gameevent.PlayerEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.texture.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.*
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.client.event.*
import net.minecraftforge.event.entity.EntityStruckByLightningEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.player.*
import org.lwjgl.opengl.GL11
import vazkii.botania.api.item.IBaubleRender
import vazkii.botania.common.achievement.IPickupAchievement
import vazkii.botania.common.item.equipment.bauble.ItemBauble

class ItemRagnarokEmblem: ItemBauble("ragnarokEmblem"), IBaubleRender, IPickupAchievement {
	
	lateinit var gemIcons: Array<IIcon>
	
	init {
		creativeTab = AlfheimTab
		setHasSubtypes(true)
	}
	
//	override fun onItemUse(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
//		if (!world.isRemote)
//			ASJUtilities.say(player, "Check: ${RagnarokStartHandler.check(player, x, y - 1, z)}")
//
//		return true
//	}
	
	override fun canEquip(stack: ItemStack?, player: EntityLivingBase?) =
		player is EntityPlayer && player.hasAchievement(AlfheimAchievements.ragnarok)
	
	override fun canUnequip(stack: ItemStack?, player: EntityLivingBase?) =
		player !is EntityPlayer || !player.hasAchievement(AlfheimAchievements.theEND)
	
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
	
	override fun getAchievementOnPickup(stack: ItemStack?, player: EntityPlayer?, entityItem: EntityItem?) = AlfheimAchievements.ragnarok
	
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
	
	// FIXME move to another file
	companion object RagnarokHandler {
		
		const val TAG_BOUND = "ragnarok.bound"
		const val TAG_GEM_FLAG = "renderGem"
		const val TAG_CONSUMED = "consumedPowers"
		
		var ragnarok = false
		var fogFade = 1f
		
		init {
			eventForge()
		}
		
		// ################################################################
		// ####################### Crafting Pendant #######################
		// ################################################################
		
		@SubscribeEvent
		fun spawnLightningForPendant(e: PlayerInteractEvent) {
			if (e.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return
			val player = e.entityPlayer
			if (player.heldItem?.item !== AlfheimItems.wiltedLotus || player.heldItem.meta != 1) return
			val tile = e.world.getTileEntity(e.x, e.y, e.z) as? TileAnomaly ?: return
			if (tile.mainSubTile != "Lightning") return
			e.world.addWeatherEffect(EntityLightningBolt(e.world, e.x.D, e.y.D, e.z.D))
		}
		
		val ORE_KEYS = arrayOf(4, 2, 0, 3, 1, 5)
		const val AETHER = -1
		const val WATER = 336227
		const val AIR = 15132211
		const val FIRE = 100400115
		const val EARTH = 6717491
		const val ORDER = 16777215
		const val VOID = 1710618
		
		@SubscribeEvent
		fun craftPendant(e: EntityStruckByLightningEvent) {
			val entityItem = e.entity
			if (entityItem !is EntityItem || entityItem.entityItem == null || entityItem.entityItem.item !== AlfheimItems.attributionBauble) return
			val world = entityItem.worldObj
			val anomaly = world.getTileEntity(entityItem) as? TileAnomaly ?: return
			if (anomaly.mainSubTile != "Lightning") return
			
			val (x, y, z) = Vector3.fromEntity(entityItem).mf()
			
			val poses = mutableListOf<Vector3>()
			
			for (i in x.bidiRange(5))
				for (j in y.bidiRange(5))
					for (k in z.bidiRange(5)) {
						if (Vector3.pointDistanceSpace(x, y, z, i, j, k) > 25) continue
						val star = world.getTileEntity(i, j, k) as? TileCracklingStar ?: continue
						
						if (star.color == AETHER)
							poses.add(Vector3(i, j, k))
					}
			
			mainLoop@ for (pos in poses) {
				val (path, connections, colors) = walkPath(pos, world, 5)
				if (path.size != 6) continue
				if (connections[5] != path[0]) continue
				if (colors[0] == AETHER &&
					colors[1] == WATER &&
					colors[2] == AIR &&
					colors[3] == FIRE &&
					colors[4] == EARTH &&
					colors[5] == ORDER) {
					for (i in path.indices)
						(checkItem(path, i, world) ?: continue@mainLoop).setDead()
					
					entityItem.setDead()
					val entity = EntityItemImmortal(world, entityItem.posX, entityItem.posY + 1, entityItem.posZ, ItemStack(AlfheimItems.ragnarokEmblem))
					entity.motionY = 1.0
					entity.delayBeforeCanPickup = 30
					world.spawnEntityInWorld(entity)
					val fakeBolt = FakeLightning(world, entityItem.posX, entityItem.posY, entityItem.posZ)
					world.addWeatherEffect(fakeBolt)
					
					for (i in 0.bidiRange(3))
						for (j in 0.bidiRange(3))
							for (k in 0.bidiRange(3))
								if (world.getBlock(entityItem, i, j, k) === Blocks.fire)
									world.setBlock(entityItem, Blocks.air, i, j, k)
					
					for (p in path) {
						val tile = p.getTileEntity(world) as? TileCracklingStar ?: continue
						tile.color = VOID
						tile.pos.set(0, -1, 0)
						tile.markDirty()
					}
					
					break
				}
			}
		}
		
		fun walkPath(start: Vector3, world: World, max: Int, walked: Array<Vector3> = arrayOf(start),
					 walkedConnections: Array<Vector3> = arrayOf((start.getTileEntity(world) as TileCracklingStar).pos),
					 walkedColors: IntArray = intArrayOf((start.getTileEntity(world) as TileCracklingStar).color))
			: Triple<Array<Vector3>, Array<Vector3>, IntArray> {
			
			if (walked.size > max) return Triple(walked, walkedConnections, walkedColors)
			val tile = start.getTileEntity(world)
			if (tile is TileCracklingStar) {
				val link = tile.pos.copy()
				if (link == Vector3(0, -1, 0)) return Triple(walked, walkedConnections, walkedColors)
				if (link in walked) return Triple(walked, walkedConnections, walkedColors)
				val linked = link.getTileEntity(world)
				if (linked is TileCracklingStar) {
					val linkPos = linked.pos.copy()
					if (linkPos != Vector3(0, -1, 0))
						return walkPath(link, world, max, arrayOf(*walked, link), arrayOf(*walkedConnections, linkPos), intArrayOf(*walkedColors, linked.color))
				}
			}
			return Triple(walked, walkedConnections, walkedColors)
		}
		
		fun Vector3.getTileEntity(world: World): TileEntity? = world.getTileEntity(x.mfloor(), y.mfloor(), z.mfloor())
		
		fun checkItem(path: Array<Vector3>, index: Int, world: World): EntityItem? {
			val (x, y, z) = path[index]
			val items = world.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1)) as List<EntityItem>
			return items.firstOrNull { it.entityItem != null && it.entityItem.item === AlfheimItems.priestEmblem && it.entityItem.meta == ORE_KEYS[index] }
		}
		
		// ################################################################
		// ######################## Begin Ragnarok ########################
		// ################################################################
		
		fun beginRagnarok(player: EntityPlayer) {
			if (!player.hasAchievement(AlfheimAchievements.ragnarok)) return
			val emblem = getEmblem(player) ?: return
			setBoolean(emblem, TAG_BOUND, true)
			player.triggerAchievement(AlfheimAchievements.theEND)
			
			ragnarok = true
			AlfheimCore.network.sendToAll(Message1d(Message1d.m1d.RAGNAROK, 0.999))
			// TODO the rest
		}
		
		// ################################################################
		// ################### Consume Priests' emblems ###################
		// ################################################################
		
		@SubscribeEvent(priority = EventPriority.LOWEST) // let it be canceled
		fun onPlayerDied(e: LivingDeathEvent) {
			if (!ragnarok) return
			
			val ragnar = e.source.entity as? EntityPlayer ?: return
			val priest = e.entityLiving as? EntityPlayer ?: return
			
			val emblemDark = getEmblem(ragnar) ?: return
			val emblemLight = ItemPriestEmblem.getEmblem(-1, priest) ?: return
			
			if (ragnar.heldItem?.item !== AlfheimItems.soulSword) return
			
			when (emblemLight.item) {
				AlfheimItems.priestEmblem -> {
					val arr = getByteArray(emblemDark, TAG_CONSUMED, ByteArray(6))
					if (arr[emblemLight.meta] > 0) return
					arr[emblemLight.meta] = 1
					setByteArray(emblemDark, TAG_CONSUMED, arr)
				}
				
				AlfheimItems.aesirEmblem  -> {
					val arr = getByteArray(emblemDark, TAG_CONSUMED, ByteArray(6))
					val id = arr.indexOfFirst { it < 1 }
					if (id == -1) return
					arr[id] = 1
					setByteArray(emblemDark, TAG_CONSUMED, arr)
				}
				
				else                      -> return
			}
			
			ragnar.playSoundAtEntity("mob.enderdragon.growl", 10f, 0.1f)
			--emblemLight.stackSize
		}
		
		// technical stuff
		
		@SubscribeEvent(priority = EventPriority.LOWEST) // for Baubles to add it's contents
		fun lockEmblemForever(e: PlayerDropsEvent) {
			val i = e.drops.indexOfFirst { getBoolean(it.entityItem, TAG_BOUND, false) }
			if (i == -1) return
			
			val entity = e.drops.removeAt(i) ?: return
			PlayerHandler.getPlayerBaubles(e.entityPlayer)[0] = entity.entityItem?.copy() ?: return
			entity.setEntityItemStack(null)
			entity.setDead()
		}
		
		@SubscribeEvent
		fun informAboutRagnarok(e: PlayerEvent.PlayerLoggedInEvent) {
			AlfheimCore.network.sendTo(Message1d(Message1d.m1d.RAGNAROK, if (ragnarok) 0.0 else 1.0), e.player as EntityPlayerMP)
		}
		
		@SubscribeEvent
		@SideOnly(Side.CLIENT)
		fun makeTheHorizonRed(e: EntityViewRenderEvent.FogColors) {
			if (!ragnarok) return
			
			if (e.entity.dimension == 1) return // not sure
			
			if (fogFade > 0) fogFade -= 0.001f
			
			e.red += (1f - e.red) * (1 - fogFade)
			e.green *= fogFade
			e.blue *= fogFade
		}
		
		fun getEmblem(player: EntityPlayer?): ItemStack? {
			val baubles = PlayerHandler.getPlayerBaubles(player ?: return null)
			val stack = baubles[0] ?: return null
			return if (stack.item === AlfheimItems.ragnarokEmblem) stack else null
		}
	}
}