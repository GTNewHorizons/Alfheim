package alfheim.common.core.asm

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.block.IHourglassTrigger
import alfheim.api.boss.*
import alfheim.api.entity.*
import alfheim.api.event.*
import alfheim.api.lib.LibResourceLocations
import alfheim.client.core.handler.CardinalSystemClient
import alfheim.client.core.util.mc
import alfheim.client.render.entity.RenderButterflies
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.handler.*
import alfheim.common.core.util.*
import alfheim.common.entity.ai.EntityAICreeperAvoidPooka
import alfheim.common.entity.boss.EntityFlugel
import alfheim.common.item.AlfheimItems
import alfheim.common.item.lens.*
import alfheim.common.potion.PotionSoulburn
import cpw.mods.fml.relauncher.*
import gloomyfolken.hooklib.asm.Hook
import gloomyfolken.hooklib.asm.Hook.ReturnValue
import gloomyfolken.hooklib.asm.ReturnCondition.*
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.client.gui.*
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.enchantment.*
import net.minecraft.entity.*
import net.minecraft.entity.monster.EntityCreeper
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.*
import net.minecraft.potion.*
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraft.world.biome.*
import net.minecraft.world.chunk.Chunk
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.util.ForgeDirection
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GLContext
import ru.vamig.worldengine.*
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.boss.IBotaniaBoss
import vazkii.botania.api.mana.*
import vazkii.botania.api.recipe.RecipePureDaisy
import vazkii.botania.api.subtile.SubTileEntity
import vazkii.botania.client.core.handler.*
import vazkii.botania.client.core.proxy.ClientProxy
import vazkii.botania.client.fx.FXWisp
import vazkii.botania.client.lib.LibResources
import vazkii.botania.client.render.tile.RenderTileAltar
import vazkii.botania.common.Botania
import vazkii.botania.common.block.*
import vazkii.botania.common.block.decor.walls.BlockModWall
import vazkii.botania.common.block.mana.BlockSpreader
import vazkii.botania.common.block.subtile.generating.SubTileDaybloom
import vazkii.botania.common.block.tile.*
import vazkii.botania.common.block.tile.mana.TilePool
import vazkii.botania.common.core.BotaniaCreativeTab
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.core.proxy.CommonProxy
import vazkii.botania.common.entity.EntityDoppleganger
import vazkii.botania.common.item.*
import vazkii.botania.common.item.block.ItemBlockSpecialFlower
import vazkii.botania.common.item.lens.ItemLens
import vazkii.botania.common.item.relic.ItemFlugelEye
import vazkii.botania.common.lib.LibBlockNames
import java.awt.Color
import java.nio.FloatBuffer
import java.util.*
import kotlin.math.*

@Suppress("UNUSED_PARAMETER", "NAME_SHADOWING", "unused", "FunctionName")
object AlfheimHookHandler {
	
	private var updatingTile = false
	private var updatingEntity = false
	private const val TAG_TRANSFER_STACK = "transferStack"
	
	var rt = 0f
	var gt = 0f
	private var bt = 0f
	
	private const val MESSANGER = 22
	private const val TRIPWIRE = 23
	
	@JvmStatic
	@Hook(injectOnExit = true, targetMethod = "<init>")
	fun `EntityCreeper$init`(e: EntityCreeper, world: World?) {
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
			e.activePotionsMap.containsKey(Potion.resistance.id) || e.activePotionsMap.containsKey(AlfheimConfigHandler.potionIDTank)
		} else e.activePotionsMap.containsKey(p.id)
	
	@JvmStatic
	@Hook(returnCondition = ALWAYS, isMandatory = true)
	fun getActivePotionEffect(e: EntityLivingBase, p: Potion): PotionEffect? {
		var pe = e.activePotionsMap[p.id] as PotionEffect?
		if (p === Potion.resistance)
			if (e.isPotionActive(AlfheimConfigHandler.potionIDTank)) {
				val tank = e.activePotionsMap[AlfheimConfigHandler.potionIDTank] as PotionEffect
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
	fun getFullDiscountForTools(handler: ManaItemHandler?, player: EntityPlayer, @ReturnValue dis: Float): Float {
		return if (AlfheimCore.enableElvenStory && player.race === EnumRace.IMP && !ESMHandler.isAbilityDisabled(player)) dis + 0.2f
		else dis
	}
	
	@JvmStatic
	@Hook(injectOnExit = true, returnCondition = ALWAYS)
	fun getStackItemTime(tile: TileHourglass?, stack: ItemStack?, @ReturnValue time: Int) =
		if (stack != null && time == 0) {
			if (stack.item === AlfheimBlocks.elvenSand.toItem()) 600 else 0
		} else time
	
	@JvmStatic
	@Hook(injectOnExit = true, returnCondition = ALWAYS)
	fun getColor(tile: TileHourglass, @ReturnValue color: Int): Int {
		val stack = tile.getStackInSlot(0)
		return if (stack != null && color == 0) {
			if (stack.item === AlfheimBlocks.elvenSand.toItem()) 0xf7f5d9 else 0
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
					val d0 = (i shr 16 and 255).D / 255.0
					val d1 = (i shr 8 and 255).D / 255.0
					val d2 = (i and 255).D / 255.0
					e.worldObj.spawnParticle(if (flag1) "mobSpellAmbient" else "mobSpell", e.posX + (e.worldObj.rand.nextDouble() - 0.5) * e.width.D, e.posY + e.worldObj.rand.nextDouble() * e.height.D - e.yOffset.D, e.posZ + (e.worldObj.rand.nextDouble() - 0.5) * e.width.D, d0, d1, d2)
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
			rt = Math.random().F * 0.3f
			r = rt
			gt = 0.7f + Math.random().F * 0.3f
			g = gt
			bt = 0.7f + Math.random().F * 0.3f
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
	@Hook(injectOnExit = true)
	fun updateTick(grass: BlockGrass, world: World, x: Int, y: Int, z: Int, random: Random) {
		if (AlfheimCore.winter && world.provider.dimensionId == AlfheimConfigHandler.dimensionIDAlfheim && world.rand.nextInt(20) == 0 && !world.isRemote && world.canBlockSeeTheSky(x, y + 1, z)) {
			world.setBlock(x, y, z, AlfheimBlocks.snowGrass)
		}
	}
	
	@JvmStatic
	@Hook(injectOnExit = true)
	fun updateTick(grass: BlockSnow, world: World, x: Int, y: Int, z: Int, random: Random) {
		if (world.provider.dimensionId == AlfheimConfigHandler.dimensionIDAlfheim && !world.isRemote)
			if (AlfheimCore.winter) {
				world.setBlock(x, y, z, AlfheimBlocks.snowLayer)
			} else if (world.rand.nextInt(20) == 0) {
				world.setBlockToAir(x, y, z)
			}
	}
	
	@JvmStatic
	@Hook(injectOnExit = true)
	fun updateTick(grass: BlockIce, world: World, x: Int, y: Int, z: Int, random: Random) {
		if (!AlfheimCore.winter && world.provider.dimensionId == AlfheimConfigHandler.dimensionIDAlfheim && world.rand.nextInt(20) == 0 && !world.isRemote)
			world.setBlock(x, y, z, Blocks.flowing_water)
	}
	
	@JvmStatic
	@Hook(injectOnExit = true)
	fun onBlockActivated(block: BlockAvatar, world: World, x: Int, y: Int, z: Int, player: EntityPlayer, s: Int, xs: Float, ys: Float, zs: Float, @ReturnValue result: Boolean): Boolean {
		if (result) ASJUtilities.dispatchTEToNearbyPlayers(world, x, y, z)
		return result
	}
	
	@JvmStatic
	@Hook(injectOnExit = true)
	fun onBlockActivated(block: BlockSpreader, par1World: World, par2: Int, par3: Int, par4: Int, par5EntityPlayer: EntityPlayer, par6: Int, par7: Float, par8: Float, par9: Float, @ReturnValue res: Boolean): Boolean {
		if (!res) return res
		par1World.getTileEntity(par2, par3, par4)?.markDirty()
		return res
	}
	
	@JvmStatic
	@Hook(injectOnExit = true, targetMethod = "updateEntity")
	fun `TileHourglass$updateEntity`(tile: TileHourglass) {
		if (tile.blockMetadata == 1 && tile.flipTicks == 3) {
			var block: Block
			for (dir in ForgeDirection.VALID_DIRECTIONS) {
				block = tile.worldObj.getBlock(tile.xCoord + dir.offsetX, tile.yCoord + dir.offsetY, tile.zCoord + dir.offsetZ)
				if (block is IHourglassTrigger)
					(block as IHourglassTrigger).onTriggeredByHourglass(tile.worldObj, tile.xCoord + dir.offsetX, tile.yCoord + dir.offsetY, tile.zCoord + dir.offsetZ, tile)
			}
		}
	}
	
	@JvmStatic
	@Hook(targetMethod = "updateEntity")
	fun `TilePool$updateEntity`(tile: TilePool) {
		if (tile.manaCap == -1 && tile.getBlockMetadata() == 3) tile.manaCap = AlfheimConfigHandler.poolRainbowCapacity
	}
	
	@JvmStatic
	@Hook(targetMethod = "updateEntity")
	fun `TilePylon$updateEntity`(tile: TilePylon) {
		updatingTile = tile.worldObj.isRemote
	}
	
	@JvmStatic
	@Hook(injectOnExit = true, targetMethod = "updateEntity")
	fun `TilePylon$updateEntityPost`(tile: TilePylon) {
		if (tile.worldObj.isRemote) {
			updatingTile = false
			if (tile.worldObj.rand.nextBoolean()) {
				val meta = tile.getBlockMetadata()
				Botania.proxy.sparkleFX(tile.worldObj, tile.xCoord + Math.random(), tile.yCoord + Math.random() * 1.5, tile.zCoord + Math.random(), if (meta == 2) 0f else 0.5f, if (meta == 0) 0.5f else 1f, if (meta == 1) 0.5f else 1f, Math.random().F, 2)
			}
		}
	}
	
	@JvmStatic
	@Hook(returnCondition = ON_TRUE)
	fun sparkleFX(proxy: ClientProxy, world: World, x: Double, y: Double, z: Double, r: Float, g: Float, b: Float, size: Float, m: Int, fake: Boolean) = updatingTile
	
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
	fun getIcon(pylon: BlockPylon, side: Int, meta: Int) =
		(if (meta == 0 || meta == 1) ModBlocks.storage.getIcon(side, meta) else Blocks.diamond_block.getIcon(side, 0))!!
	
	@JvmStatic
	@Hook(returnCondition = ON_TRUE, targetMethod = "func_150000_e", isMandatory = true)
	fun onNetherPortalActivation(portal: BlockPortal, world: World, x: Int, y: Int, z: Int) =
		MinecraftForge.EVENT_BUS.post(NetherPortalActivationEvent(world, x, y, z))
	
	@JvmStatic
	@Hook(returnCondition = ON_TRUE, isMandatory = true, booleanReturnConstant = false)
	fun matches(recipe: RecipePureDaisy, world: World, x: Int, y: Int, z: Int, pureDaisy: SubTileEntity, block: Block, meta: Int) =
		recipe.output === ModBlocks.livingwood && world.provider.dimensionId == AlfheimConfigHandler.dimensionIDAlfheim
	
	@JvmStatic
	@Hook(returnCondition = ON_TRUE, isMandatory = true)
	fun onItemUse(eye: ItemFlugelEye, stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float) =
		// Stupid Et Futurum
		if (player.isSneaking) EntityFlugel.spawn(player, stack, world, x, y, z, false, false) else false
	
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
		EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, e.heldItem) + if (AlfheimCore.enableMMO && e.isPotionActive(AlfheimConfigHandler.potionIDGoldRush)) 2 else 0
	
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
		
		var f = b.getPlayerRelativeBlockHardness(player, world, x, y, z) > 0f
		
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
		if (player.getActivePotionEffect(Potion.nightVision.id)?.duration ?: 0 > 0) 1f else 0f
	
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
	
	@JvmStatic
	@Hook(returnCondition = ALWAYS)
	fun hasSearchBar(tab: BotaniaCreativeTab) = AlfheimConfigHandler.searchTabBotania
	
	var chunkCoors = Int.MAX_VALUE to Int.MAX_VALUE
	
	@JvmStatic
	@Hook(targetMethod = "getChunkFromBlockCoords")
	fun getChunkFromBlockCoords(world: World, x: Int, z: Int) {
		chunkCoors = x to z
	}
	
	var replace = false
	
	@JvmStatic
	@Hook
	fun getCanSpawnHere(entity: EntityAnimal): Boolean {
		replace = entity.worldObj.provider.dimensionId == AlfheimConfigHandler.dimensionIDAlfheim
		return replace
	}
	
	@JvmStatic
	@Hook(injectOnExit = true, returnCondition = ALWAYS)
	fun getBlock(world: World, x: Int, y: Int, z: Int, @ReturnValue block: Block): Block {
		if (replace && (block === AlfheimBlocks.snowGrass || block === AlfheimBlocks.snowLayer || block === Blocks.snow_layer)) {
			replace = false
			return Blocks.grass
		}
		return block
	}
	
	@JvmStatic
	@Hook(injectOnExit = true, returnCondition = ALWAYS)
	fun getBiomeGenForWorldCoords(c: Chunk, x: Int, z: Int, cm: WorldChunkManager, @ReturnValue oldBiome: BiomeGenBase): BiomeGenBase? {
		if (chunkCoors.first != Int.MAX_VALUE || chunkCoors.second != Int.MAX_VALUE) {
			val biome = WE_Biome.getBiomeAt((cm as? WE_WorldChunkManager ?: return oldBiome).cp, chunkCoors.first.toLong(), chunkCoors.second.toLong())
			chunkCoors = Int.MAX_VALUE to Int.MAX_VALUE
			return biome
		}
		else
			return oldBiome
	}
	
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook(createMethod = true, returnCondition = ALWAYS)
	fun getItemIconName(block: BlockGaiaHead) = "${LibResources.PREFIX_MOD}gaiaHead"
	
	@JvmStatic
	@Hook(returnCondition = ALWAYS)
	fun getBinding(mirror: ItemManaMirror, stack: ItemStack): ChunkCoordinates? {
		val world = mc.theWorld ?: return null
		
		if (world.provider.dimensionId != ItemNBTHelper.getInt(stack, "dim", Int.MAX_VALUE)) return null
		
		val coords = mirror.getPoolCoords(stack)
		if (coords.posY == -1)
			return null
		
		val tile = world.getTileEntity(coords.posX, coords.posY, coords.posZ)
		
		return if (tile is IManaPool) coords else null
	}
	
	var moveText = false
	
	@JvmStatic
	@Hook(isMandatory = true)
	fun drawSimpleManaHUD(hh: HUDHandler?, color: Int, mana: Int, maxMana: Int, name: String?, res: ScaledResolution) {
		moveText = mana >= 0
	}
	
	var numMana = true
	
	@JvmStatic
	@Hook(injectOnExit = true, isMandatory = true)
	fun renderManaBar(hh: HUDHandler?, x: Int, y: Int, color: Int, alpha: Float, mana: Int, maxMana: Int) {
		if (mana < 0 || !AlfheimConfigHandler.numericalMana || !numMana) return
		glPushMatrix()
		
		val text = "$mana/$maxMana"
		val x = x + 51 - mc.fontRenderer.getStringWidth(text) / 2
		val y = y - mc.fontRenderer.FONT_HEIGHT
		mc.fontRenderer.drawString(text, x, y, color, mc.currentScreen == null)
		glPopMatrix()
	}
	
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook(isMandatory = true)
	fun doRenderShadowAndFire(render: Render, entity: Entity, x: Double, y: Double, z: Double, yaw: Float, partialTickTime: Float) {
		if (AlfheimCore.enableMMO) if (entity is EntityLivingBase) {
			if (entity.isPotionActive(AlfheimConfigHandler.potionIDButterShield)) RenderButterflies.render(render, entity, x, y, z, mc.timer.renderPartialTicks)
		}
	}
	
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook(isMandatory = true)
	fun renderOverlays(renderer: ItemRenderer, partialTicks: Float) {
		if (mc.thePlayer.isPotionActive(AlfheimConfigHandler.potionIDSoulburn)) {
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
			glFog(GL_FOG_COLOR, setFogColorBuffer(0f, 0f, 0f, 1f))
			glFogi(GL_FOG_MODE, GL_LINEAR)
			glFogf(GL_FOG_START, 0f)
			glFogf(GL_FOG_END, 8f)
			
			if (GLContext.getCapabilities().GL_NV_fog_distance) {
				glFogi(34138, 34139)
			}
			
			glFogf(GL_FOG_START, 0f)
		} else {
			glFog(GL_FOG_COLOR, setFogColorBuffer(renderer.fogColorRed, renderer.fogColorGreen, renderer.fogColorBlue, 1f))
			glNormal3f(0f, -1f, 0f)
			glColor4f(1f, 1f, 1f, 1f)
			val block = ActiveRenderInfo.getBlockAtEntityViewpoint(renderer.mc.theWorld, entitylivingbase, renderPartialTicks)
			var f1: Float
			
			val event = net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity(renderer, entitylivingbase, block, renderPartialTicks.D, 0.1f)
			
			if (MinecraftForge.EVENT_BUS.post(event)) {
				glFogf(GL_FOG_DENSITY, event.density)
			} else if (entitylivingbase.isPotionActive(Potion.blindness) && !flag) {
				f1 = 5f
				val j = entitylivingbase.getActivePotionEffect(Potion.blindness.id)!!.getDuration()
				
				if (j < 20) {
					f1 = 5f + (renderer.farPlaneDistance - 5f) * (1f - j.F / 20f)
				}
				
				glFogi(GL_FOG_MODE, GL_LINEAR)
				
				if (fogMode < 0) {
					glFogf(GL_FOG_START, 0f)
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
				
				if (entitylivingbase.isPotionActive(Potion.waterBreathing) || (AlfheimCore.enableMMO && entitylivingbase.isPotionActive(AlfheimConfigHandler.potionIDNoclip))) {
					glFogf(GL_FOG_DENSITY, 0.05f)
				} else {
					glFogf(GL_FOG_DENSITY, 0.1f - EnchantmentHelper.getRespiration(entitylivingbase).F * 0.03f)
				}
			} else if (block.material === Material.lava) {
				glFogi(GL_FOG_MODE, GL_EXP)
				glFogf(GL_FOG_DENSITY, if (AlfheimCore.enableMMO && entitylivingbase.isPotionActive(AlfheimConfigHandler.potionIDNoclip)) 0.05f else 2f)
			} else {
				f1 = renderer.farPlaneDistance
				
				if (renderer.mc.theWorld.provider.worldHasVoidParticles && !flag) {
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
				
				glFogi(GL_FOG_MODE, GL_LINEAR)
				
				if (fogMode < 0) {
					glFogf(GL_FOG_START, 0f)
					glFogf(GL_FOG_END, f1)
				} else {
					glFogf(GL_FOG_START, f1 * 0.75f)
					glFogf(GL_FOG_END, f1)
				}
				
				if (GLContext.getCapabilities().GL_NV_fog_distance) {
					glFogi(34138, 34139)
				}
				
				if (renderer.mc.theWorld.provider.doesXZShowFog(entitylivingbase.posX.I, entitylivingbase.posZ.I)) {
					glFogf(GL_FOG_START, f1 * 0.05f)
					glFogf(GL_FOG_END, min(f1, 192f) * 0.5f)
				}
				MinecraftForge.EVENT_BUS.post(net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent(renderer, entitylivingbase, block, renderPartialTicks.D, fogMode, f1))
			}
			
			glEnable(GL_COLOR_MATERIAL)
			glColorMaterial(GL_FRONT, GL_AMBIENT)
		}
	}
	
	var renderingBoss = false
	
	@JvmStatic
	@Hook(returnCondition = ALWAYS)
	fun setCurrentBoss(handler: BossBarHandler?, status: IBotaniaBoss?) {
		BossBarHandler.currentBoss = if (AlfheimCore.enableMMO) null else status
	}
	
	@JvmStatic
	@Hook
	fun render(handler: BossBarHandler?, res: ScaledResolution) {
		if (BossBarHandler.currentBoss == null) return
		renderingBoss = true
	}
	
	@JvmStatic
	@Hook
	fun translateToLocal(sc: StatCollector?, text: String?): String? {
		if (text == "botaniamisc.manaUsage")
			moveText = true
			
		return text
	}
	
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook(returnCondition = ALWAYS)
	fun drawString(font: FontRenderer, string: String?, x: Int, y: Int, color: Int): Int {
		return font.drawString(string, x, y - if (moveText) font.FONT_HEIGHT + 1 else 0, color, false).also { moveText = false }
	}
	
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook(returnCondition = ALWAYS)
	fun drawStringWithShadow(font: FontRenderer, string: String?, x: Int, y: Int, color: Int): Int {
		val ny = y - if (moveText) font.FONT_HEIGHT + 1 else 0
		moveText = false
		
		val result =
			if (renderingBoss && color == 0xA2018C && (BossBarHandler.currentBoss is IBotaniaBossWithName || BossBarHandler.currentBoss is IBotaniaBossWithShaderAndName))
				if (BossBarHandler.currentBoss is IBotaniaBossWithName)
					font.drawString(string, x, ny, (BossBarHandler.currentBoss as IBotaniaBossWithName).getNameColor(), true).also { renderingBoss = false }
				else
					font.drawString(string, x, ny, (BossBarHandler.currentBoss as IBotaniaBossWithShaderAndName).getNameColor(), true).also { renderingBoss = false }
			else
				font.drawString(string, x, ny, color, true)
		
		glColor4f(1f, 1f, 1f, 1f)
		
		return result
	}
	
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook(createMethod = true, returnCondition = ALWAYS)
	fun getNameColor(gaia: EntityDoppleganger) = AlfheimConfigHandler.gaiaNameColor
	
	@JvmStatic
	@Hook // for blue line above item tooltip
	fun drawManaBar(handler: TooltipAdditionDisplayHandler?, stack: ItemStack, display: IManaTooltipDisplay, mouseX: Int, mouseY: Int, offx: Int, offy: Int, width: Int, height: Int) {
		val item = stack.item
		
		if (item is IManaItem && AlfheimConfigHandler.numericalMana) {
			glDisable(GL_DEPTH_TEST)
			mc.fontRenderer.drawStringWithShadow("${item.getMana(stack)}/${item.getMaxMana(stack)}", mouseX + offx - 1, mouseY - offy - height - 1 - mc.fontRenderer.FONT_HEIGHT, Color.HSBtoRGB(0.528f, (sin((ClientTickHandler.ticksInGame.F + ClientTickHandler.partialTicks).D * 0.2).F + 1f) * 0.3f + 0.4f, 1f))
			glEnable(GL_DEPTH_TEST)
		}
	}
	
	var wispNoclip = true
	
	@JvmStatic
	@Hook(targetMethod = "<init>", injectOnExit = true)
	fun `FXWisp$init`(fx: FXWisp, world: World?, d: Double, d1: Double, d2: Double, size: Float, red: Float, green: Float, blue: Float, distanceLimit: Boolean, depthTest: Boolean, maxAgeMul: Float) {
		fx.noClip = wispNoclip
	}
	
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook(injectOnExit = true, returnCondition = ALWAYS)
	fun isInvisibleToPlayer(player: EntityPlayer, thePlayer: EntityPlayer, @ReturnValue result: Boolean): Boolean {
		if (result && AlfheimCore.enableMMO) {
			if (CardinalSystemClient.PlayerSegmentClient.party?.isMember(player) == true) return false
		}
		
		return result
	}
	
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook(createMethod = true, returnCondition = ALWAYS)
	fun isInvisibleToPlayer(entity: EntityLivingBase, thePlayer: EntityPlayer): Boolean {
		if (AlfheimCore.enableMMO) {
			if (CardinalSystemClient.PlayerSegmentClient.party?.isMember(entity) == true) return false
		}
		
		return entity.isInvisible
	}
}