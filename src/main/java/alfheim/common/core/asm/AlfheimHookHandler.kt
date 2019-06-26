package alfheim.common.core.asm

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.block.IHourglassTrigger
import alfheim.api.event.*
import alfheim.client.render.entity.RenderButterflies
import alfheim.common.core.registry.*
import alfheim.common.core.util.AlfheimConfig
import alfheim.common.entity.boss.EntityFlugel
import alfheim.common.item.lens.*
import alfheim.common.potion.PotionSoulburn
import codechicken.nei.recipe.GuiRecipe
import cpw.mods.fml.relauncher.*
import gloomyfolken.hooklib.asm.Hook
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ItemRenderer
import net.minecraft.client.renderer.entity.Render
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.enchantment.*
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.*
import net.minecraft.potion.*
import net.minecraft.util.IIcon
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.api.recipe.RecipePureDaisy
import vazkii.botania.api.subtile.SubTileEntity
import vazkii.botania.client.core.handler.HUDHandler
import vazkii.botania.client.core.proxy.ClientProxy
import vazkii.botania.common.Botania
import vazkii.botania.common.block.*
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

import java.util.*

import gloomyfolken.hooklib.asm.ReturnCondition.*
import org.lwjgl.opengl.GL11.*

object AlfheimHookHandler {
	
	private var updatingTile = false
	private var updatingEntity = false
	private val TAG_TRANSFER_STACK = "transferStack"
	var numMana = true
	
	var rt = 0f
	var gt = 0f
	var bt = 0f
	
	val MESSANGER = 22
	val TRIPWIRE = 23
	
	@Hook(injectOnExit = true, isMandatory = true)
	fun onNewPotionEffect(e: EntityLivingBase, pe: PotionEffect) {
		MinecraftForge.EVENT_BUS.post(LivingPotionEvent.Add.Post(e, pe))
	}
	
	@Hook(injectOnExit = true, isMandatory = true)
	fun onChangedPotionEffect(e: EntityLivingBase, pe: PotionEffect, was: Boolean) {
		MinecraftForge.EVENT_BUS.post(LivingPotionEvent.Change.Post(e, pe, was))
	}
	
	@Hook(injectOnExit = true, isMandatory = true)
	fun onFinishedPotionEffect(e: EntityLivingBase, pe: PotionEffect) {
		MinecraftForge.EVENT_BUS.post(LivingPotionEvent.Remove.Post(e, pe))
	}
	
	@Hook(returnCondition = ALWAYS, isMandatory = true)
	fun isPotionActive(e: EntityLivingBase, p: Potion): Boolean {
		return if (p === Potion.resistance) {
			e.activePotionsMap.containsKey(Potion.resistance.id) || e.activePotionsMap.containsKey(AlfheimRegistry.tank.id)
		} else e.activePotionsMap.containsKey(p.id)
	}
	
	@Hook(returnCondition = ALWAYS, isMandatory = true)
	fun getActivePotionEffect(e: EntityLivingBase, p: Potion): PotionEffect? {
		var pe: PotionEffect? = e.activePotionsMap[p.id] as PotionEffect
		if (p === Potion.resistance)
			if (e.isPotionActive(AlfheimRegistry.tank)) {
				val tank = e.activePotionsMap[AlfheimRegistry.tank.id] as PotionEffect
				if (pe == null) pe = PotionEffect(Potion.resistance.id, tank.duration, 0)
				pe.amplifier += tank.amplifier
			}
		
		return pe
	}
	
	@Hook(returnCondition = ON_TRUE)
	fun requestManaExact(handler: ManaItemHandler, stack: ItemStack, player: EntityPlayer, manaToGet: Int, remove: Boolean): Boolean {
		return player.capabilities.isCreativeMode
	}
	
	@Hook(returnCondition = ON_TRUE, returnType = "int", returnAnotherMethod = "requestManaChecked")
	fun requestMana(handler: ManaItemHandler, stack: ItemStack, player: EntityPlayer, manaToGet: Int, remove: Boolean): Boolean {
		return player.capabilities.isCreativeMode
	}
	
	fun requestManaChecked(handler: ManaItemHandler, stack: ItemStack, player: EntityPlayer, manaToGet: Int, remove: Boolean): Int {
		return manaToGet
	}
	
	@Hook(injectOnExit = true, isMandatory = true)
	fun moveFlying(e: Entity, x: Float, y: Float, z: Float) {
		if (AlfheimCore.enableMMO && e is EntityLivingBase && e.isPotionActive(AlfheimRegistry.leftFlame)) {
			e.motionZ = 0.0
			e.motionY = e.motionZ
			e.motionX = e.motionY
		}
	}
	
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
				
				if (!e.isInvisible) {
					flag = e.worldObj.rand.nextBoolean()
				} else {
					flag = e.worldObj.rand.nextInt(15) == 0
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
	
	@Hook
	fun onLivingUpdate(e: EntityDoppleganger) {
		updatingEntity = true
	}
	
	@Hook(injectOnExit = true, targetMethod = "onLivingUpdate")
	fun onLivingUpdatePost(e: EntityDoppleganger) {
		updatingEntity = false
	}
	
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
	
	@Hook(targetMethod = "updateEntity")
	fun `TilePylon$updateEntity`(entity: TilePylon) {
		updatingTile = entity.worldObj.isRemote
	}
	
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
	
	@Hook(returnCondition = ON_TRUE)
	fun sparkleFX(proxy: ClientProxy, world: World, x: Double, y: Double, z: Double, r: Float, g: Float, b: Float, size: Float, m: Int, fake: Boolean): Boolean {
		return updatingTile
	}
	
	@Hook(returnCondition = ALWAYS)
	fun getSubBlocks(flower: BlockSpecialFlower, item: Item, tab: CreativeTabs, list: MutableList<*>) {
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
	
	@Hook(injectOnExit = true, isMandatory = true, targetMethod = "<clinit>")
	fun `ItemLens$clinit`(lens: ItemLens) {
		ItemLens.setProps(MESSANGER, 1)
		ItemLens.setProps(TRIPWIRE, 1 shl 5)
		
		ItemLens.setLens(MESSANGER, LensMessanger())
		ItemLens.setLens(TRIPWIRE, LensTripwire())
	}
	
	@Hook(injectOnExit = true)
	fun displayAllReleventItems(tab: BotaniaCreativeTab, list: List<*>) {
		AlfheimItems.thinkingHand.getSubItems(AlfheimItems.thinkingHand, tab, list)
	}
	
	@Hook
	fun onBlockPlacedBy(subtile: SubTileEntity, world: World, x: Int, y: Int, z: Int, entity: EntityLivingBase, stack: ItemStack) {
		if (subtile is SubTileDaybloom && subtile.isPrime) subtile.setPrimusPosition()
	}
	
	@Hook
	fun onBlockAdded(subtile: SubTileEntity, world: World, x: Int, y: Int, z: Int) {
		if (subtile is SubTileDaybloom && subtile.isPrime) subtile.setPrimusPosition()
	}
	
	@Hook(returnCondition = ALWAYS)
	fun getIcon(pylon: BlockPylon, side: Int, meta: Int): IIcon {
		return if (meta == 0 || meta == 1) ModBlocks.storage.getIcon(side, meta) else Blocks.diamond_block.getIcon(side, 0)
	}
	
	@Hook(returnCondition = ON_TRUE, targetMethod = "func_150000_e", isMandatory = true)
	fun onNetherPortalActivation(portal: BlockPortal, world: World, x: Int, y: Int, z: Int): Boolean {
		return MinecraftForge.EVENT_BUS.post(NetherPortalActivationEvent(world, x, y, z))
	}
	
	@Hook(returnCondition = ON_TRUE, isMandatory = true)
	fun matches(recipe: RecipePureDaisy, world: World, x: Int, y: Int, z: Int, pureDaisy: SubTileEntity, block: Block, meta: Int): Boolean {
		return recipe.output == ModBlocks.livingwood && world.provider.dimensionId == AlfheimConfig.dimensionIDAlfheim
	}
	
	@Hook(returnCondition = ON_TRUE, isMandatory = true)
	fun onItemUse(eye: ItemFlugelEye, stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		return if (player.isSneaking && world.getBlock(x, y, z) === Blocks.beacon) EntityFlugel.spawn(player, stack, world, x, y, z, false) else false
	}
	
	@Hook(returnCondition = ON_TRUE)
	fun spawn(gaia: EntityDoppleganger, player: EntityPlayer, stack: ItemStack, world: World, x: Int, y: Int, z: Int, hard: Boolean): Boolean {
		for (i in -1..1)
			for (k in -1..1)
				if (!world.getBlock(x + i, y - 1, z + k).isBeaconBase(world, x + i, y - 1, z + k, x, y, z)) {
					if (!world.isRemote) ASJUtilities.say(player, "alfheimmisc.inactive")
					return true
				}
		return false
	}
	
	@Hook(createMethod = true)
	fun onItemRightClick(item: ItemGaiaHead, stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		if (player.getCurrentArmor(3) == null) player.setCurrentItemOrArmor(4, stack.splitStack(1))
		return stack
	}
	
	@Hook(isMandatory = true, returnCondition = ALWAYS)
	fun getFortuneModifier(h: EnchantmentHelper, e: EntityLivingBase): Int {
		return EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, e.heldItem) + if (AlfheimCore.enableMMO && e.isPotionActive(AlfheimRegistry.goldRush)) 2 else 0
	}
	
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
	
	@SideOnly(Side.CLIENT)
	@Hook(injectOnExit = true, isMandatory = true)
	fun renderManaBar(hh: HUDHandler, x: Int, y: Int, color: Int, alpha: Float, mana: Int, maxMana: Int) {
		if (mana < 0 || !AlfheimConfig.numericalMana || !numMana) return
		glPushMatrix()
		val f = Minecraft.getMinecraft().currentScreen == null
		var f1 = false
		
		if (AlfheimCore.NEILoaded)
			f1 = !f && Minecraft.getMinecraft().currentScreen is GuiRecipe
		
		val text = "$mana/$maxMana"
		val X = x + 51 - Minecraft.getMinecraft().fontRenderer.getStringWidth(text) / 2
		val Y = if (f1) y - 9 else y - 19
		Minecraft.getMinecraft().fontRenderer.drawString(text, X, Y, color, f)
		glPopMatrix()
	}
	
	@SideOnly(Side.CLIENT)
	@Hook(isMandatory = true)
	fun doRenderShadowAndFire(render: Render, entity: Entity, x: Double, y: Double, z: Double, yaw: Float, partialTickTime: Float) {
		if (AlfheimCore.enableMMO) if (entity is EntityLivingBase) if (entity.isPotionActive(AlfheimRegistry.butterShield)) RenderButterflies.render(render, entity, x, y, z, Minecraft.getMinecraft().timer.renderPartialTicks)
	}
	
	@SideOnly(Side.CLIENT)
	@Hook(isMandatory = true)
	fun renderOverlays(renderer: ItemRenderer, partialTicks: Float) {
		if (Minecraft.getMinecraft().thePlayer.isPotionActive(AlfheimRegistry.soulburn)) {
			glDisable(GL_ALPHA_TEST)
			PotionSoulburn.renderFireInFirstPerson(partialTicks)
			glEnable(GL_ALPHA_TEST)
		}
	}
}