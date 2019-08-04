package alfheim.common.item.rod

import alfheim.api.ModInfo
import alfheim.api.item.ColorOverrideHelper
import alfheim.common.core.helper.InterpolatedIconHelper
import alfheim.common.core.util.mfloor
import alfheim.common.item.ItemMod
import alfheim.common.item.equipment.bauble.ItemPriestEmblem
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.command.IEntitySelector
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.MinecraftForge
import vazkii.botania.api.internal.IManaBurst
import vazkii.botania.api.item.*
import vazkii.botania.api.mana.*
import vazkii.botania.common.Botania
import vazkii.botania.common.entity.EntityDoppleganger
import java.awt.Color
import kotlin.math.*

open class ItemInterdictionRod(name: String = "interdictionRod"): ItemMod(name), IManaUsingItem, IAvatarWieldable {
	
	private val avatarOverlay = ResourceLocation("${ModInfo.MODID}:textures/model/avatar/avatarInterdiction.png")
	
	init {
		setMaxStackSize(1)
		MinecraftForge.EVENT_BUS.register(this)
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(par1IconRegister: IIconRegister) {
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun loadTextures(event: TextureStitchEvent.Pre) {
		if (event.map.textureType == 1) {
			itemIcon = InterpolatedIconHelper.forItem(event.map, this)
		}
	}
	
	override fun getItemUseAction(par1ItemStack: ItemStack?) = EnumAction.bow
	
	override fun getMaxItemUseDuration(par1ItemStack: ItemStack?) = 72000
	
	override fun isFull3D() = true
	
	override fun usesMana(stack: ItemStack) = true
	
	override fun onItemRightClick(par1ItemStack: ItemStack, par2World: World?, par3EntityPlayer: EntityPlayer?): ItemStack {
		par3EntityPlayer!!.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack))
		return par1ItemStack
	}
	
	object PLAYER_SELECTOR: IEntitySelector {
		override fun isEntityApplicable(e: Entity) =
			(e is EntityLivingBase && e !is EntityDoppleganger) || (e is IProjectile && e !is IManaBurst)
	}
	
	object AVATAR_SELECTOR: IEntitySelector {
		override fun isEntityApplicable(e: Entity) =
			e is EntityLivingBase && e !is EntityPlayer && e !is EntityDoppleganger
	}
	
	fun particleRing(world: World, x: Int, y: Int, z: Int, range: Int, r: Float, g: Float, b: Float) {
		particleRing(world, x.toDouble(), y.toDouble(), z.toDouble(), range, r, g, b)
	}
	
	fun particleRing(world: World, x: Double, y: Double, z: Double, range: Int, r: Float, g: Float, b: Float) {
		val m = 0.15F
		val mv = 0.35F
		for (i in 0..359 step 8) {
			val rad = i.toDouble() * Math.PI / 180.0
			val dispx = x + 0.5 - cos(rad) * range.toFloat()
			val dispy = y + 0.5
			val dispz = z + 0.5 - sin(rad) * range.toFloat()
			
			Botania.proxy.wispFX(world, dispx, dispy, dispz, r, g, b, 0.2F, (Math.random() - 0.5).toFloat() * m, (Math.random() - 0.5).toFloat() * mv, (Math.random() - 0.5F).toFloat() * m)
		}
	}
	
	fun pushEntities(x: Int, y: Int, z: Int, range: Int, velocity: Double, player: EntityPlayer?, entities: List<Any?>) =
		pushEntities(x.toDouble(), y.toDouble(), z.toDouble(), range, velocity, player, entities)
	
	fun pushEntities(x: Double, y: Double, z: Double, range: Int, velocity: Double, player: EntityPlayer?, entities: List<Any?>): Boolean {
		var flag = false
		for (entity in entities) {
			if (entity is Entity && player?.canPlayerEdit(entity.posX.mfloor(), entity.posY.mfloor(), entity.posZ.mfloor(), 1, player.currentEquippedItem) == true) {
				val xDif = entity.posX - x
				val yDif = entity.posY - (y + 1)
				val zDif = entity.posZ - z
				val dist = sqrt(xDif * xDif + yDif * yDif + zDif * zDif)
				if (dist <= range) {
					entity.motionX = velocity * xDif
					entity.motionY = velocity * yDif
					entity.motionZ = velocity * zDif
					entity.fallDistance = 0f
					flag = true
				}
			}
		}
		return flag
	}
	
	override fun onUsingTick(stack: ItemStack?, player: EntityPlayer?, count: Int) {
		if (player != null) {
			val world = player.worldObj
			val x = player.posX
			val y = player.posY
			val z = player.posZ
			
			val priest = (ItemPriestEmblem.getEmblem(2, player) != null)
			val prowess = IManaProficiencyArmor.Helper.hasProficiency(player)
			
			val cost = getCost(prowess, priest)
			val range = getRange(prowess, priest)
			val velocity = getVelocity(prowess, priest)
			
			if (ManaItemHandler.requestManaExactForTool(stack, player, cost, false)) {
				val color = Color(ColorOverrideHelper.getColor(player, 0x0000FF))
				val r = color.red.toFloat() / 255f
				val g = color.green.toFloat() / 255f
				val b = color.blue.toFloat() / 255f
				if (count % 5 == 0) particleRing(world, x, y, z, range, r, g, b)
				
				val exclude: EntityLivingBase = player
				val entities = world.getEntitiesWithinAABBExcludingEntity(exclude, AxisAlignedBB.getBoundingBox(x - range, y - range, z - range, x + range, y + range, z + range), PLAYER_SELECTOR)
				
				if (pushEntities(x, y, z, range, velocity, player, entities)) {
					if (count % 3 == 0) world.playSoundAtEntity(player, "${ModInfo.MODID}:wind", 0.4F, 1F)
					ManaItemHandler.requestManaExactForTool(stack, player, cost, true)
				}
			}
		}
	}
	
	val COST = 5
	val PROWESS_COST = -1
	val PRIEST_COST = 2
	val AVATAR_COST = 4
	
	val VELOCITY = 0.05
	val PROWESS_VELOCTY = 0.02
	val PRIEST_VELOCITY = 0.07
	
	val RANGE = 5
	val PROWESS_RANGE = 0
	val PRIEST_RANGE = 2
 
	fun getCost(prowess: Boolean, priest: Boolean) =
        COST + if (prowess) PROWESS_COST else 0 + if (priest) PRIEST_COST else 0
	
	fun getVelocity(prowess: Boolean, priest: Boolean): Double {
		var vel = VELOCITY
		if (prowess) vel += PROWESS_VELOCTY
		if (priest) vel += PRIEST_VELOCITY
		return vel
	}
	
	fun getRange(prowess: Boolean, priest: Boolean) =
        RANGE + if (prowess) PROWESS_RANGE else 0 + if (priest) PRIEST_RANGE else 0
	
	override fun onAvatarUpdate(tile: IAvatarTile, stack: ItemStack) {
		val te = tile as TileEntity
		val world = te.worldObj
		val x = te.xCoord.toDouble()
		val y = te.yCoord.toDouble()
		val z = te.zCoord.toDouble()
		
		if (tile.currentMana >= AVATAR_COST) {
			if (tile.elapsedFunctionalTicks % 5 == 0) particleRing(world, x, y, z, RANGE, 0f, 0f, 1f)
			
			val entities = world.selectEntitiesWithinAABB(EntityLivingBase::class.java,
														  AxisAlignedBB.getBoundingBox(x - RANGE, y - RANGE, z - RANGE,
																					   x + RANGE, y + RANGE, z + RANGE), AVATAR_SELECTOR)
			
			if (pushEntities(x, y, z, RANGE, VELOCITY, null, entities)) {
				if (tile.elapsedFunctionalTicks % 3 == 0) world.playSoundEffect(x, y, z, "${ModInfo.MODID}:wind", 0.4F, 1F)
				tile.recieveMana(-AVATAR_COST)
			}
		}
	}
	
	override fun getOverlayResource(tile: IAvatarTile, stack: ItemStack) = avatarOverlay
}
