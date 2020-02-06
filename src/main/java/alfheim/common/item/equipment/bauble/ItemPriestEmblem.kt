package alfheim.common.item.equipment.bauble

import alfheim.api.ModInfo
import alfheim.api.item.ColorOverrideHelper
import alfheim.client.core.util.mc
import alfheim.client.render.world.VisualEffectHandlerClient
import alfheim.common.core.handler.VisualEffectHandler
import alfheim.common.core.helper.IconHelper
import alfheim.common.core.util.*
import alfheim.common.item.AlfheimItems
import baubles.api.BaubleType
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.relauncher.*
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.texture.*
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.potion.*
import net.minecraft.util.*
import net.minecraft.util.MathHelper
import net.minecraftforge.client.event.RenderPlayerEvent
import org.lwjgl.opengl.GL11
import vazkii.botania.api.item.IBaubleRender
import vazkii.botania.api.mana.*
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.*
import vazkii.botania.common.item.equipment.bauble.ItemBauble
import java.awt.Color
import kotlin.math.min
import alexsocol.asjlib.math.Vector3 as AVector3

class ItemPriestEmblem: ItemBauble("priestEmblem"), IBaubleRender, IManaUsingItem {
	
	companion object {
		
		const val TYPES = 4
		
		fun getEmblem(meta: Int, player: EntityPlayer?): ItemStack? {
			val baubles = PlayerHandler.getPlayerBaubles(player)
			val stack = baubles.getStackInSlot(0)
			return if (stack != null && ((stack.item == AlfheimItems.emblem && stack.meta == meta) || stack.item == AlfheimItems.aesirEmblem) && isActive(stack)) stack else null
		}
		
		fun isActive(stack: ItemStack) = ItemNBTHelper.getByte(stack, "active", 0) == 1.toByte()
		
		fun setActive(stack: ItemStack, state: Boolean) {
			ItemNBTHelper.setByte(stack, "active", if (state) 1.toByte() else 0.toByte())
		}
		
		fun isDangerous(stack: ItemStack) = ItemNBTHelper.getByte(stack, "dangerous", 0) == 1.toByte()
		
		fun setDangerous(stack: ItemStack, state: Boolean) {
			ItemNBTHelper.setByte(stack, "dangerous", if (state) 1.toByte() else 0.toByte())
		}
	}
	
	val COST = 2
	var icons: Array<IIcon?> = arrayOfNulls(TYPES)
	var baubleIcons: Array<IIcon?> = arrayOfNulls(TYPES)
	
	init {
		creativeTab = AlfheimTab
		setHasSubtypes(true)
	}
	
	override fun getUnlocalizedNameInefficiently(par1ItemStack: ItemStack) =
		super.getUnlocalizedNameInefficiently(par1ItemStack).replace("item\\.botania:".toRegex(), "item.${ModInfo.MODID}:")
	
	override fun getItemStackDisplayName(stack: ItemStack) =
		super.getItemStackDisplayName(stack).replace("&".toRegex(), "\u00a7")
	
	override fun registerIcons(par1IconRegister: IIconRegister) {
		for (i in 0 until TYPES)
			icons[i] = IconHelper.forItem(par1IconRegister, this, i)
		for (i in 0 until TYPES)
			baubleIcons[i] = IconHelper.forItem(par1IconRegister, this, "Render$i")
	}
	
	override fun getSubItems(item: Item, tab: CreativeTabs?, list: MutableList<Any?>) {
		(0 until TYPES).mapTo(list) { ItemStack(item, 1, it) }
	}
	
	override fun addInformation(par1ItemStack: ItemStack?, par2EntityPlayer: EntityPlayer?, par3List: MutableList<Any?>, par4: Boolean) {
		if (par1ItemStack == null || par2EntityPlayer == null) return
		if (!GuiScreen.isShiftKeyDown() && !par2EntityPlayer.capabilities.isCreativeMode) {
			if (par2EntityPlayer.health <= 6f)
				this.addStringToTooltip(StatCollector.translateToLocal("misc.${ModInfo.MODID}.healthDanger"), par3List)
			else
				this.addStringToTooltip(StatCollector.translateToLocal("misc.${ModInfo.MODID}.healthWarning"), par3List)
		}
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4)
	}
	
	override fun addHiddenTooltip(par1ItemStack: ItemStack?, par2EntityPlayer: EntityPlayer?, par3List: MutableList<Any?>, par4: Boolean) {
		if (par1ItemStack == null || par2EntityPlayer == null) return
		if (!par2EntityPlayer.capabilities.isCreativeMode) {
			if (par2EntityPlayer.health <= 6f)
				addStringToTooltip(StatCollector.translateToLocal("misc.${ModInfo.MODID}.crisisOfFaith"), par3List)
			else
				addStringToTooltip(StatCollector.translateToLocal("misc.${ModInfo.MODID}.lackOfFaith"), par3List)
		}
		super.addHiddenTooltip(par1ItemStack, par2EntityPlayer, par3List, par4)
	}
	
	fun addStringToTooltip(s: String, tooltip: MutableList<Any?>) {
		tooltip.add(s.replace("&".toRegex(), "\u00a7"))
	}
	
	override fun getIconFromDamage(dmg: Int) = icons[min(TYPES - 1, dmg)]
	
	fun getBaubleIconFromDamage(dmg: Int) = baubleIcons[min(TYPES - 1, dmg)]
	
	override fun getBaubleType(stack: ItemStack) = BaubleType.AMULET
	
	override fun getUnlocalizedName(par1ItemStack: ItemStack) =
		super.getUnlocalizedName(par1ItemStack) + par1ItemStack.meta
	
	override fun onEquippedOrLoadedIntoWorld(stack: ItemStack, player: EntityLivingBase) {
		setDangerous(stack, false)
	}
	
	override fun onUnequipped(stack: ItemStack, player: EntityLivingBase) {
		if (!(!isDangerous(stack) || (player is EntityPlayer && player.capabilities.isCreativeMode))) {
			player.attackEntityFrom(FaithDamageSource.instance, 6f)
			player.addPotionEffect(PotionEffect(Potion.blindness.id, 150, 0))
			player.addPotionEffect(PotionEffect(Potion.confusion.id, 150, 2))
			player.addPotionEffect(PotionEffect(Potion.moveSlowdown.id, 300, 2))
			player.addPotionEffect(PotionEffect(Potion.weakness.id, 300, 2))
		}
		setDangerous(stack, false)
	}
	
	class FaithDamageSource: DamageSource("${ModInfo.MODID}.lackOfFaith") {
		companion object {
			val instance = FaithDamageSource()
		}
		
		init {
			setDamageBypassesArmor()
		}
	}
	
	fun getHeadOrientation(entity: EntityLivingBase): Vector3 {
		val f1 = MathHelper.cos(-entity.rotationYaw * 0.017453292F - Math.PI.F)
		val f2 = MathHelper.sin(-entity.rotationYaw * 0.017453292F - Math.PI.F)
		val f3 = -MathHelper.cos(-(entity.rotationPitch - 90) * 0.017453292F)
		val f4 = MathHelper.sin(-(entity.rotationPitch - 90) * 0.017453292F)
		return Vector3((f2 * f3).D, f4.D, (f1 * f3).D)
	}
	
	override fun onWornTick(stack: ItemStack, player: EntityLivingBase) {
		if (player.ticksExisted % 10 == 0) {
			
			if (player is EntityPlayer) {
				val flag: Boolean
				setActive(stack, ManaItemHandler.requestManaExact(stack, player, COST, true).also { flag = it })
				setDangerous(stack, true)
				
				VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.EMBLEM_ACTIVATION, player.dimension, player.entityId.D, if (flag) 1.0 else 0.0)
			}
		}
	}
	
	override fun usesMana(stack: ItemStack) = true
	
	@SideOnly(Side.CLIENT)
	override fun onPlayerBaubleRender(stack: ItemStack, event: RenderPlayerEvent, type: IBaubleRender.RenderType) {
		if (type == IBaubleRender.RenderType.BODY) {
			val player = event.entityPlayer
			
			doParticles(stack, player)
			
			mc.renderEngine.bindTexture(TextureMap.locationItemsTexture)
			IBaubleRender.Helper.rotateIfSneaking(player)
			val armor = player.getCurrentArmor(2) != null
			GL11.glRotatef(180F, 1F, 0F, 0F)
			GL11.glTranslatef(-0.26F, -0.4F, if (armor) 0.21F else 0.15F)
			GL11.glScalef(0.5F, 0.5F, 0.5F)
			
			val icon = getBaubleIconFromDamage(stack.meta)
			if (icon != null)
				ItemRenderer.renderItemIn2D(Tessellator.instance, icon.maxU, icon.minV, icon.minU, icon.maxV, icon.iconWidth, icon.iconHeight, 1F / 32F)
		}
	}
	
	fun doParticles(stack: ItemStack, player: EntityPlayer) {
		if (VisualEffectHandlerClient.activeAmblems.getOrDefault(player.entityId, false)) {
			when (stack.meta) {
				0 -> {
					if (player.ticksExisted % 10 == 0) {
						val playerHead = Vector3.fromEntityCenter(player).add(0.0, 0.75, 0.0)
						val playerShift = playerHead.copy().add(getHeadOrientation(player))
						val color = Color(ColorOverrideHelper.getColor(player, 0x0079C4))
						val innerColor = Color(color.rgb).brighter().brighter()
						
						spawnEmblem0(playerHead.x, playerHead.y, playerHead.z, playerShift.x, playerShift.y, playerShift.z, color.rgb.D, innerColor.rgb.D)
					}
				}
				
				1 -> {
					if (player.ticksExisted % 10 == 0) {
						for (i in 0..6) {
							val xmotion = (Math.random() - 0.5) * 0.15
							val zmotion = (Math.random() - 0.5) * 0.15
							val color = Color(ColorOverrideHelper.getColor(player, 0x964B00))
							val r = color.red.F / 255F
							val g = color.green.F / 255F
							val b = color.blue.F / 255F
							
							spawnEmblem1(player.posX, player.posY - player.yOffset, player.posZ, r.D, g.D, b.D, xmotion, zmotion)
						}
					}
				}
				
				2 -> {
					if (player.ticksExisted % 10 == 0) {
						for (i in 0..6) {
							val vec = getHeadOrientation(player).multiply(0.52)
							val color = Color(ColorOverrideHelper.getColor(player, 0x0101FF))
							val r = color.red.F / 255F
							val g = color.green.F / 255F
							val b = color.blue.F / 255F
							
							spawnEmblem2(player.posX + vec.x, player.posY + vec.y + 1.62, player.posZ + vec.z, r.D, g.D, b.D)
						}
					}
				}
				
				3 -> {
					val color = Color(ColorOverrideHelper.getColor(player, 0xF94407))
					val r = color.red / 255f
					val g = color.green / 255f
					val b = color.blue / 255f
					
					var (x, y, z) = AVector3.fromEntity(player)
					y = if (mc.thePlayer == player) y - player.yOffset else y
					spawnEmblem3(x, y, z, r.D, g.D, b.D)
				}
			}
		}
	}
	
	fun spawnEmblem0(xs: Double, ys: Double, zs: Double, xe: Double, ye: Double, ze: Double, color: Double, innerColor: Double) {
		Botania.proxy.lightningFX(mc.theWorld, Vector3(xs, ys, zs), Vector3(xe, ye, ze), 2f, color.I, innerColor.I)
	}
	
	fun spawnEmblem1(x: Double, y: Double, z: Double, r: Double, g: Double, b: Double, motionX: Double, motionZ: Double) {
		Botania.proxy.wispFX(mc.theWorld, x, y, z, r.F, g.F, b.F, Math.random().F * 0.15f + 0.15f, motionX.F, 0.0075f, motionZ.F)
	}
	
	fun spawnEmblem2(x: Double, y: Double, z: Double, r: Double, g: Double, b: Double) {
		Botania.proxy.sparkleFX(mc.theWorld, x, y, z, r.F, g.F, b.F, 1f, 5)
	}
	
	fun spawnEmblem3(x: Double, y: Double, z: Double, r: Double, g: Double, b: Double) {
		for (i in 1..9) {
			val pos = AVector3(x, y, z).add(0.0, 0.25, 0.0).add(AVector3(0.0, 0.0, 0.5).rotate((Botania.proxy.worldElapsedTicks * 5 % 360 + i * 40.0), AVector3.oY))
			Botania.proxy.sparkleFX(mc.theWorld, pos.x, pos.y, pos.z, r.F, g.F, b.F, 1f, 4)
		}
	}
}
