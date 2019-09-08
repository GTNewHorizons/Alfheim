package alfheim.common.core.asm

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.block.IHourglassTrigger
import alfheim.api.entity.*
import alfheim.api.event.*
import alfheim.api.lib.LibResourceLocations
import alfheim.client.render.entity.RenderButterflies
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.entity.ai.EntityAICreeperAvoidPooka
import alfheim.common.entity.boss.EntityFlugel
import alfheim.common.item.AlfheimItems
import alfheim.common.item.lens.*
import alfheim.common.potion.PotionSoulburn
import codechicken.nei.recipe.GuiRecipe
import cpw.mods.fml.relauncher.*
import gloomyfolken.hooklib.asm.Hook
import gloomyfolken.hooklib.asm.ReturnCondition.*
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.enchantment.*
import net.minecraft.entity.*
import net.minecraft.entity.monster.EntityCreeper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.*
import net.minecraft.potion.*
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.util.ForgeDirection
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GLContext
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.api.recipe.RecipePureDaisy
import vazkii.botania.api.subtile.SubTileEntity
import vazkii.botania.client.core.handler.HUDHandler
import vazkii.botania.client.core.proxy.ClientProxy
import vazkii.botania.client.lib.LibResources
import vazkii.botania.client.render.tile.RenderTileAltar
import vazkii.botania.common.Botania
import vazkii.botania.common.block.*
import vazkii.botania.common.block.decor.walls.BlockModWall
import vazkii.botania.common.block.subtile.generating.SubTileDaybloom
import vazkii.botania.common.block.tile.*
import vazkii.botania.common.core.BotaniaCreativeTab
import vazkii.botania.common.core.proxy.CommonProxy
import vazkii.botania.common.entity.EntityDoppleganger
import vazkii.botania.common.item.ItemGaiaHead
import vazkii.botania.common.item.block.ItemBlockSpecialFlower
import vazkii.botania.common.item.lens.ItemLens
import vazkii.botania.common.item.relic.ItemFlugelEye
import vazkii.botania.common.lib.LibBlockNames
import java.nio.FloatBuffer
import java.util.*
import kotlin.math.min

@Suppress("UNUSED_PARAMETER", "NAME_SHADOWING", "unused", "FunctionName")
object AlfheimHookHandler {
	
	private var updatingTile = false
	private var updatingEntity = false
	private const val TAG_TRANSFER_STACK = "transferStack"
	var numMana = true
	
	var rt = 0f
	var gt = 0f
	private var bt = 0f
	
	private const val MESSANGER = 22
	private const val TRIPWIRE = 23
	
	@JvmStatic
	@Hook(injectOnExit = true, targetMethod = "<init>")
	fun `EntityCreeper$init`(e: EntityCreeper, world: World) {
		e.tasks.addTask(3, EntityAICreeperAvoidPooka(e))
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
	
	@JvmStatic
	@Hook(returnCondition = ALWAYS, isMandatory = true)
	fun isPotionActive(e: EntityLivingBase, p: Potion) =
		if (p === Potion.resistance) {
			e.activePotionsMap.containsKey(Potion.resistance.id) || e.activePotionsMap.containsKey(AlfheimRegistry.tank.id)
		} else e.activePotionsMap.containsKey(p.id)
	
	@JvmStatic
	@Hook(returnCondition = ALWAYS, isMandatory = true)
	fun getActivePotionEffect(e: EntityLivingBase, p: Potion): PotionEffect? {
		var pe = e.activePotionsMap[p.id] as PotionEffect?
		if (p === Potion.resistance)
			if (e.isPotionActive(AlfheimRegistry.tank)) {
				val tank = e.activePotionsMap[AlfheimRegistry.tank.id] as PotionEffect
				if (pe == null) pe = PotionEffect(Potion.resistance.id, tank.duration, 0)
				pe.amplifier += tank.amplifier
			}
		
		return pe
	}
	
	@JvmStatic
	@Hook(returnCondition = ON_TRUE)
	fun requestManaExact(handler: ManaItemHandler?, stack: ItemStack, player: EntityPlayer, manaToGet: Int, remove: Boolean) = player.capabilities.isCreativeMode
	
	@JvmStatic
	@Hook(returnCondition = ON_TRUE, returnType = "int", returnAnotherMethod = "requestManaChecked")
	fun requestMana(handler: ManaItemHandler?, stack: ItemStack, player: EntityPlayer, manaToGet: Int, remove: Boolean) = player.capabilities.isCreativeMode
	
	@JvmStatic
	fun requestManaChecked(handler: ManaItemHandler?, stack: ItemStack, player: EntityPlayer, manaToGet: Int, remove: Boolean) = manaToGet
	
	@JvmStatic
	@Hook(injectOnExit = true, returnCondition = ALWAYS)
	fun getFullDiscountForTools(handler: ManaItemHandler?, player: EntityPlayer, @Hook.ReturnValue dis: Float): Float {
		return if (AlfheimCore.enableElvenStory && player.race === EnumRace.IMP) dis + 0.2f
		else dis
	}
	
	@JvmStatic
	@Hook(injectOnExit = true, returnCondition = ALWAYS)
	fun getStackItemTime(tile: TileHourglass?, stack: ItemStack?, @Hook.ReturnValue time: Int) =
		if (stack != null && time == 0) {
			if (stack.item === Item.getItemFromBlock(AlfheimBlocks.elvenSand)) 600 else 0
		} else time
	
	@JvmStatic
	@Hook(injectOnExit = true, returnCondition = ALWAYS)
	fun getColor(tile: TileHourglass, @Hook.ReturnValue color: Int): Int {
		val stack = tile.getStackInSlot(0)
		return if (stack != null && color == 0) {
			if (stack.item === Item.getItemFromBlock(AlfheimBlocks.elvenSand)) 0xf7f5d9 else 0
		} else color
	}
	
	@JvmStatic
	@Hook(injectOnExit = true, isMandatory = true)
	fun moveFlying(e: Entity, x: Float, y: Float, z: Float) {
		if (AlfheimCore.enableMMO && e is EntityLivingBase && e.isPotionActive(AlfheimConfigHandler.potionIDLeftFlame)) {
			e.motionZ = 0.0
			e.motionY = e.motionZ
			e.motionX = e.motionY
		}
	}
	
	@JvmStatic
	@Hook(returnCondition = ALWAYS, isMandatory = true)
	fun updatePotionEffects(e: EntityLivingBase) {
		try {
			val iterator = e.activePotionsMap.keys.iterator()
			
			while (iterator.hasNext()) {
				val integer = iterator.next() as Int
				val potioneffect = e.activePotionsMap[integer] as PotionEffect
				
				if (!potioneffect.onUpdate(e)) {
					//if (!e.worldObj.isRemote) {
					iterator.remove()
					AlfheimSyntheticMethods.onFinishedPotionEffect(e, potioneffect)
					//}
				} else if (potioneffect.getDuration() % 600 == 0) {
					AlfheimSyntheticMethods.onChangedPotionEffect(e, potioneffect, false)
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
					val d0 = (i shr 16 and 255).toDouble() / 255.0
					val d1 = (i shr 8 and 255).toDouble() / 255.0
					val d2 = (i and 255).toDouble() / 255.0
					e.worldObj.spawnParticle(if (flag1) "mobSpellAmbient" else "mobSpell", e.posX + (e.worldObj.rand.nextDouble() - 0.5) * e.width.toDouble(), e.posY + e.worldObj.rand.nextDouble() * e.height.toDouble() - e.yOffset.toDouble(), e.posZ + (e.worldObj.rand.nextDouble() - 0.5) * e.width.toDouble(), d0, d1, d2)
				}
			}
		} catch (ex: ConcurrentModificationException) {
			ASJUtilities.log("Well, that was expected. Ignore.")
			ex.printStackTrace()
		}
	}
	
	@JvmStatic
	@Hook
	fun onLivingUpdate(e: EntityDoppleganger) {
		updatingEntity = true
	}
	
	@JvmStatic
	@Hook(injectOnExit = true, targetMethod = "onLivingUpdate")
	fun onLivingUpdatePost(e: EntityDoppleganger) {
		updatingEntity = false
	}
	
	@JvmStatic
	@Hook(returnCondition = ALWAYS)
	fun wispFX(proxy: CommonProxy, world: World, x: Double, y: Double, z: Double, r: Float, g: Float, b: Float, size: Float, gravity: Float) {
		var r = r
		var g = g
		var b = b
		if (updatingEntity) {
			rt = Math.random().toFloat() * 0.3f
			r = rt
			gt = 0.7f + Math.random().toFloat() * 0.3f
			g = gt
			bt = 0.7f + Math.random().toFloat() * 0.3f
			b = bt
		}
		Botania.proxy.wispFX(world, x, y, z, r, g, b, size, gravity, 1f)
	}
	
	@JvmStatic
	@Hook(returnCondition = ALWAYS)
	fun wispFX(proxy: CommonProxy, world: World, x: Double, y: Double, z: Double, r: Float, g: Float, b: Float, size: Float, motionx: Float, motiony: Float, motionz: Float) {
		var r = r
		var g = g
		var b = b
		if (updatingEntity && size == 0.4f) {
			r = rt
			g = gt
			b = bt
		}
		Botania.proxy.wispFX(world, x, y, z, r, g, b, size, motionx, motiony, motionz, 1f)
	}
	
	@JvmStatic
	@Hook(injectOnExit = true, targetMethod = "updateEntity")
	fun `TileHourglass$updateEntity`(hourglass: TileHourglass) {
		if (hourglass.blockMetadata == 1 && hourglass.flipTicks == 3) {
			var block: Block
			for (dir in ForgeDirection.VALID_DIRECTIONS) {
				block = hourglass.worldObj.getBlock(hourglass.xCoord + dir.offsetX, hourglass.yCoord + dir.offsetY, hourglass.zCoord + dir.offsetZ)
				if (block is IHourglassTrigger)
					(block as IHourglassTrigger).onTriggeredByHourglass(hourglass.worldObj, hourglass.xCoord + dir.offsetX, hourglass.yCoord + dir.offsetY, hourglass.zCoord + dir.offsetZ, hourglass)
			}
		}
	}
	
	@JvmStatic
	@Hook(targetMethod = "updateEntity")
	fun `TilePylon$updateEntity`(entity: TilePylon) {
		updatingTile = entity.worldObj.isRemote
	}
	
	@JvmStatic
	@Hook(injectOnExit = true, targetMethod = "updateEntity")
	fun `TilePylon$updateEntityPost`(entity: TilePylon) {
		if (entity.worldObj.isRemote) {
			updatingTile = false
			if (entity.worldObj.rand.nextBoolean()) {
				val meta = entity.getBlockMetadata()
				Botania.proxy.sparkleFX(entity.worldObj, entity.xCoord + Math.random(), entity.yCoord + Math.random() * 1.5, entity.zCoord + Math.random(), if (meta == 2) 0f else 0.5f, if (meta == 0) 0.5f else 1f, if (meta == 1) 0.5f else 1f, Math.random().toFloat(), 2)
			}
		}
	}
	
	@JvmStatic
	@Hook(returnCondition = ON_TRUE)
	fun sparkleFX(proxy: ClientProxy, world: World, x: Double, y: Double, z: Double, r: Float, g: Float, b: Float, size: Float, m: Int, fake: Boolean): Boolean {
		return updatingTile
	}
	
	@JvmStatic
	@Hook(returnCondition = ALWAYS)
	fun getSubBlocks(flower: BlockSpecialFlower, item: Item, tab: CreativeTabs?, list: MutableList<Any?>) {
		for (s in BotaniaAPI.subtilesForCreativeMenu) {
			list.add(ItemBlockSpecialFlower.ofType(s))
			if (BotaniaAPI.miniFlowers.containsKey(s))
				list.add(ItemBlockSpecialFlower.ofType(BotaniaAPI.miniFlowers[s]))
			if (s == LibBlockNames.SUBTILE_DAYBLOOM)
				list.add(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_DAYBLOOM_PRIME))
			if (s == LibBlockNames.SUBTILE_NIGHTSHADE)
				list.add(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_NIGHTSHADE_PRIME))
		}
	}
	
	@JvmStatic
	@Hook(returnCondition = ALWAYS, createMethod = true)
	fun isValidArmor(item: ItemGaiaHead, stack: ItemStack, armorType: Int, entity: Entity) = armorType == 0
	
	@JvmStatic
	@Hook(injectOnExit = true, isMandatory = true, targetMethod = "<clinit>")
	fun `ItemLens$clinit`(lens: ItemLens?) {
		ItemLens.setProps(MESSANGER, 1)
		ItemLens.setProps(TRIPWIRE, 1 shl 5)
		
		ItemLens.setLens(MESSANGER, LensMessanger())
		ItemLens.setLens(TRIPWIRE, LensTripwire())
	}
	
	@JvmStatic
	@Hook(targetMethod = "<init>")
	fun `BlockModWall$init`(wall: BlockModWall, block: Block, meta: Int) {
		block.setCreativeTab(BotaniaCreativeTab.INSTANCE)
	}
	
	@JvmStatic
	@Hook(injectOnExit = true)
	fun displayAllReleventItems(tab: BotaniaCreativeTab, list: List<Any?>) {
		AlfheimItems.thinkingHand.getSubItems(AlfheimItems.thinkingHand, tab, list)
	}
	
	@JvmStatic
	@Hook
	fun onBlockPlacedBy(subtile: SubTileEntity, world: World, x: Int, y: Int, z: Int, entity: EntityLivingBase, stack: ItemStack) {
		if (subtile is SubTileDaybloom && subtile.isPrime) subtile.setPrimusPosition()
	}
	
	@JvmStatic
	@Hook
	fun onBlockAdded(subtile: SubTileEntity, world: World, x: Int, y: Int, z: Int) {
		if (subtile is SubTileDaybloom && subtile.isPrime) subtile.setPrimusPosition()
	}
	
	@JvmStatic
	@Hook(returnCondition = ALWAYS)
	fun getIcon(pylon: BlockPylon, side: Int, meta: Int): IIcon {
		return if (meta == 0 || meta == 1) ModBlocks.storage.getIcon(side, meta) else Blocks.diamond_block.getIcon(side, 0)
	}
	
	@JvmStatic
	@Hook(returnCondition = ON_TRUE, targetMethod = "func_150000_e", isMandatory = true)
	fun onNetherPortalActivation(portal: BlockPortal, world: World, x: Int, y: Int, z: Int): Boolean {
		return MinecraftForge.EVENT_BUS.post(NetherPortalActivationEvent(world, x, y, z))
	}
	
	@JvmStatic
	@Hook(returnCondition = ON_TRUE, isMandatory = true, booleanReturnConstant = false)
	fun matches(recipe: RecipePureDaisy, world: World, x: Int, y: Int, z: Int, pureDaisy: SubTileEntity, block: Block, meta: Int): Boolean {
		return recipe.output === ModBlocks.livingwood && world.provider.dimensionId == AlfheimConfigHandler.dimensionIDAlfheim
	}
	
	@JvmStatic
	@Hook(returnCondition = ON_TRUE, isMandatory = true)
	fun onItemUse(eye: ItemFlugelEye, stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		return if (player.isSneaking && world.getBlock(x, y, z) === Blocks.beacon) EntityFlugel.spawn(player, stack, world, x, y, z, false) else false
	}
	
	@JvmStatic
	@Hook(returnCondition = ON_TRUE)
	fun spawn(gaia: EntityDoppleganger?, player: EntityPlayer, stack: ItemStack, world: World, x: Int, y: Int, z: Int, hard: Boolean): Boolean {
		for (i in -1..1)
			for (k in -1..1)
				if (!world.getBlock(x + i, y - 1, z + k).isBeaconBase(world, x + i, y - 1, z + k, x, y, z)) {
					if (!world.isRemote) ASJUtilities.say(player, "alfheimmisc.inactive")
					return true
				}
		return false
	}
	
	@JvmStatic
	@Hook(createMethod = true)
	fun onItemRightClick(item: ItemGaiaHead, stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		if (player.getCurrentArmor(3) == null) player.setCurrentItemOrArmor(4, stack.splitStack(1))
		return stack
	}
	
	@JvmStatic
	@Hook(isMandatory = true, returnCondition = ALWAYS)
	fun getFortuneModifier(h: EnchantmentHelper?, e: EntityLivingBase) =
		EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, e.heldItem) + if (AlfheimCore.enableMMO && e.isPotionActive(AlfheimRegistry.goldRush)) 2 else 0
	
	@JvmStatic
	@Hook(returnCondition = ALWAYS, isMandatory = true)
	fun extinguishFire(world: World, player: EntityPlayer?, x: Int, y: Int, z: Int, side: Int): Boolean {
		var x = x
		var y = y
		var z = z
		if (side == 0) --y
		if (side == 1) ++y
		if (side == 2) --z
		if (side == 3) ++z
		if (side == 4) --x
		if (side == 5) ++x
		val b = world.getBlock(x, y, z)
		
		var f = b.getPlayerRelativeBlockHardness(player, world, x, y, z) > 0.0f
		
		if (player != null) f = f || player.capabilities.isCreativeMode
		if (b.material === Material.fire && f) {
			world.playAuxSFXAtEntity(player, 1004, x, y, z, 0)
			world.setBlockToAir(x, y, z)
			return true
		}
		return false
	}
	
	@JvmStatic
	@Hook(returnCondition = ALWAYS)
	fun getNightVisionBrightness(render: EntityRenderer, player: EntityPlayer, partialTicks: Float) =
		if (player.getActivePotionEffect(Potion.nightVision)?.duration ?: 0 > 0) 1f else 0f
	
	@JvmStatic
	@Hook(injectOnExit = true)
	fun getSubBlocks(block: BlockAltar, item: Item, tab: CreativeTabs?, list: MutableList<Any?>) {
		list.add(ItemStack(item, 1, 9))
	}
	
	@JvmStatic
	@Hook(returnCondition = ALWAYS)
	fun getIcon(block: BlockAltar, side: Int, meta: Int): IIcon =
		if (meta == 9) AlfheimBlocks.livingcobble.getIcon(0, 0) else if (meta in 1..8) ModFluffBlocks.biomeStoneA.getIcon(side, meta + 7) else Blocks.cobblestone.getIcon(side, meta)
	
	private var renderingTile = false
	
	@JvmStatic
	@Hook
	fun renderTileEntityAt(renderer: RenderTileAltar, tile: TileEntity, x: Double, y: Double, z: Double, pticks: Float) {
		val blockMeta = if (tile.blockMetadata == -1) {
			tile.worldObj?.getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord) ?: 0
		} else tile.blockMetadata
		
		if (RenderTileAltar.forceMeta == 9 || blockMeta == 9)
			renderingTile = true
	}
	
	@JvmStatic
	@Hook(returnCondition = ON_TRUE)
	fun bindTexture(tm: TextureManager, loc: ResourceLocation): Boolean {
		if (renderingTile) {
			renderingTile = false
			tm.bindTexture(LibResourceLocations.altar9)
			return true
		}
		return false
	}
	
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook(createMethod = true, returnCondition = ALWAYS)
	fun getItemIconName(block: BlockGaiaHead) = "${LibResources.PREFIX_MOD}gaiaHead"
	
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook(injectOnExit = true, isMandatory = true)
	fun renderManaBar(hh: HUDHandler?, x: Int, y: Int, color: Int, alpha: Float, mana: Int, maxMana: Int) {
		if (mana < 0 || !AlfheimConfigHandler.numericalMana || !numMana) return
		glPushMatrix()
		val f = Minecraft.getMinecraft().currentScreen == null
		var f1 = false
		
		if (AlfheimCore.NEILoaded)
			f1 = !f && Minecraft.getMinecraft().currentScreen is GuiRecipe
		
		val text = "$mana/$maxMana"
		val x = x + 51 - Minecraft.getMinecraft().fontRenderer.getStringWidth(text) / 2
		val y = if (f1) y - 9 else y - 19
		Minecraft.getMinecraft().fontRenderer.drawString(text, x, y, color, f)
		glPopMatrix()
	}
	
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook(isMandatory = true)
	fun doRenderShadowAndFire(render: Render, entity: Entity, x: Double, y: Double, z: Double, yaw: Float, partialTickTime: Float) {
		if (AlfheimCore.enableMMO) if (entity is EntityLivingBase) if (entity.isPotionActive(AlfheimRegistry.butterShield)) RenderButterflies.render(render, entity, x, y, z, Minecraft.getMinecraft().timer.renderPartialTicks)
	}
	
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook(isMandatory = true)
	fun renderOverlays(renderer: ItemRenderer, partialTicks: Float) {
		if (Minecraft.getMinecraft().thePlayer.isPotionActive(AlfheimRegistry.soulburn)) {
			glDisable(GL_ALPHA_TEST)
			PotionSoulburn.renderFireInFirstPerson(partialTicks)
			glEnable(GL_ALPHA_TEST)
		}
	}
	
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook(returnCondition = ALWAYS)
	fun setupFog(renderer: EntityRenderer, fogMode: Int, renderPartialTicks: Float) {
		val entitylivingbase = renderer.mc.renderViewEntity
		var flag = false
		
		if (entitylivingbase is EntityPlayer) {
			flag = entitylivingbase.capabilities.isCreativeMode
		}
		
		fun setFogColorBuffer(p_78469_1_: Float, p_78469_2_: Float, p_78469_3_: Float, p_78469_4_: Float): FloatBuffer {
			renderer.fogColorBuffer.clear()
			renderer.fogColorBuffer.put(p_78469_1_).put(p_78469_2_).put(p_78469_3_).put(p_78469_4_)
			renderer.fogColorBuffer.flip()
			return renderer.fogColorBuffer
		}
		
		if (fogMode == 999) {
			glFog(GL_FOG_COLOR, setFogColorBuffer(0.0f, 0.0f, 0.0f, 1.0f))
			glFogi(GL_FOG_MODE, GL_LINEAR)
			glFogf(GL_FOG_START, 0.0f)
			glFogf(GL_FOG_END, 8.0f)
			
			if (GLContext.getCapabilities().GL_NV_fog_distance) {
				glFogi(34138, 34139)
			}
			
			glFogf(GL_FOG_START, 0.0f)
		} else {
			glFog(GL_FOG_COLOR, setFogColorBuffer(renderer.fogColorRed, renderer.fogColorGreen, renderer.fogColorBlue, 1.0f))
			glNormal3f(0.0f, -1.0f, 0.0f)
			glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
			val block = ActiveRenderInfo.getBlockAtEntityViewpoint(renderer.mc.theWorld, entitylivingbase, renderPartialTicks)
			var f1: Float
			
			val event = net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity(renderer, entitylivingbase, block, renderPartialTicks.toDouble(), 0.1f)
			
			if (MinecraftForge.EVENT_BUS.post(event)) {
				glFogf(GL_FOG_DENSITY, event.density)
			} else if (entitylivingbase.isPotionActive(Potion.blindness) && !flag) {
				f1 = 5.0f
				val j = entitylivingbase.getActivePotionEffect(Potion.blindness).getDuration()
				
				if (j < 20) {
					f1 = 5.0f + (renderer.farPlaneDistance - 5.0f) * (1.0f - j.toFloat() / 20.0f)
				}
				
				glFogi(GL_FOG_MODE, GL_LINEAR)
				
				if (fogMode < 0) {
					glFogf(GL_FOG_START, 0.0f)
					glFogf(GL_FOG_END, f1 * 0.8f)
				} else {
					glFogf(GL_FOG_START, f1 * 0.25f)
					glFogf(GL_FOG_END, f1)
				}
				
				if (GLContext.getCapabilities().GL_NV_fog_distance) {
					glFogi(34138, 34139) // wtf magic numbers
				}
			} else if (renderer.cloudFog) {
				glFogi(GL_FOG_MODE, GL_EXP)
				glFogf(GL_FOG_DENSITY, 0.1f)
			} else if (block.material === Material.water) {
				glFogi(GL_FOG_MODE, GL_EXP)
				
				if (entitylivingbase.isPotionActive(Potion.waterBreathing) || (AlfheimCore.enableMMO && entitylivingbase.isPotionActive(AlfheimRegistry.noclip))) {
					glFogf(GL_FOG_DENSITY, 0.05f)
				} else {
					glFogf(GL_FOG_DENSITY, 0.1f - EnchantmentHelper.getRespiration(entitylivingbase).toFloat() * 0.03f)
				}
			} else if (block.material === Material.lava) {
				glFogi(GL_FOG_MODE, GL_EXP)
				glFogf(GL_FOG_DENSITY, if (AlfheimCore.enableMMO && entitylivingbase.isPotionActive(AlfheimRegistry.noclip)) 0.05f else 2.0f)
			} else {
				f1 = renderer.farPlaneDistance
				
				if (renderer.mc.theWorld.provider.worldHasVoidParticles && !flag) {
					var d0 = (entitylivingbase.getBrightnessForRender(renderPartialTicks) and 15728640 shr 20).toDouble() / 16.0 + (entitylivingbase.lastTickPosY + (entitylivingbase.posY - entitylivingbase.lastTickPosY) * renderPartialTicks.toDouble() + 4.0) / 32.0
					
					if (d0 < 1.0) {
						if (d0 < 0.0) {
							d0 = 0.0
						}
						
						d0 *= d0
						var f2 = 100.0f * d0.toFloat()
						
						if (f2 < 5.0f) {
							f2 = 5.0f
						}
						
						if (f1 > f2) {
							f1 = f2
						}
					}
				}
				
				glFogi(GL_FOG_MODE, GL_LINEAR)
				
				if (fogMode < 0) {
					glFogf(GL_FOG_START, 0.0f)
					glFogf(GL_FOG_END, f1)
				} else {
					glFogf(GL_FOG_START, f1 * 0.75f)
					glFogf(GL_FOG_END, f1)
				}
				
				if (GLContext.getCapabilities().GL_NV_fog_distance) {
					glFogi(34138, 34139)
				}
				
				if (renderer.mc.theWorld.provider.doesXZShowFog(entitylivingbase.posX.toInt(), entitylivingbase.posZ.toInt())) {
					glFogf(GL_FOG_START, f1 * 0.05f)
					glFogf(GL_FOG_END, min(f1, 192.0f) * 0.5f)
				}
				MinecraftForge.EVENT_BUS.post(net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent(renderer, entitylivingbase, block, renderPartialTicks.toDouble(), fogMode, f1))
			}
			
			glEnable(GL_COLOR_MATERIAL)
			glColorMaterial(GL_FRONT, GL_AMBIENT)
		}
	}
}