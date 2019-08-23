package alfheim.common.item.equipment.bauble

import alfheim.api.ModInfo
import alfheim.api.item.ColorOverrideHelper
import alfheim.common.core.helper.IconHelper
import alfheim.common.item.AlfheimItems
import baubles.api.BaubleType
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.relauncher.*
import net.minecraft.client.Minecraft
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
import alexsocol.asjlib.math.Vector3 as ASJVec

class ItemPriestEmblem: ItemBauble("priestEmblem"), IBaubleRender, IManaUsingItem {
	
	companion object {
		
		const val TYPES = 4
		
		fun getEmblem(meta: Int, player: EntityPlayer?): ItemStack? {
			val baubles = PlayerHandler.getPlayerBaubles(player)
			val stack = baubles.getStackInSlot(0)
			return if (stack != null && ((stack.item == AlfheimItems.emblem && stack.itemDamage == meta) || stack.item == AlfheimItems.aesirEmblem) && isActive(stack)) stack else null
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
			if (par2EntityPlayer.health <= 6.0f)
				this.addStringToTooltip(StatCollector.translateToLocal("misc.${ModInfo.MODID}.healthDanger"), par3List)
			else
				this.addStringToTooltip(StatCollector.translateToLocal("misc.${ModInfo.MODID}.healthWarning"), par3List)
		}
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4)
	}
	
	override fun addHiddenTooltip(par1ItemStack: ItemStack?, par2EntityPlayer: EntityPlayer?, par3List: MutableList<Any?>, par4: Boolean) {
		if (par1ItemStack == null || par2EntityPlayer == null) return
		if (!par2EntityPlayer.capabilities.isCreativeMode) {
			if (par2EntityPlayer.health <= 6.0f)
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
		super.getUnlocalizedName(par1ItemStack) + par1ItemStack.itemDamage
	
	override fun onEquippedOrLoadedIntoWorld(stack: ItemStack, player: EntityLivingBase) {
		setDangerous(stack, false)
	}
	
	override fun onUnequipped(stack: ItemStack, player: EntityLivingBase) {
		if (!(!isDangerous(stack) || (player is EntityPlayer && player.capabilities.isCreativeMode))) {
			player.attackEntityFrom(FaithDamageSource.instance, 6.0f)
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
		val f1 = MathHelper.cos(-entity.rotationYaw * 0.017453292F - Math.PI.toFloat())
		val f2 = MathHelper.sin(-entity.rotationYaw * 0.017453292F - Math.PI.toFloat())
		val f3 = -MathHelper.cos(-(entity.rotationPitch - 90) * 0.017453292F)
		val f4 = MathHelper.sin(-(entity.rotationPitch - 90) * 0.017453292F)
		return Vector3((f2 * f3).toDouble(), f4.toDouble(), (f1 * f3).toDouble())
	}
	
	override fun onWornTick(stack: ItemStack, player: EntityLivingBase) {
		if (player.ticksExisted % 10 == 0) {
			
			if (player is EntityPlayer) {
				setActive(stack, ManaItemHandler.requestManaExact(stack, player, COST, true))
				setDangerous(stack, true)
			}
		}
	}
	
	override fun usesMana(stack: ItemStack) = true
	
	@SideOnly(Side.CLIENT)
	override fun onPlayerBaubleRender(stack: ItemStack, event: RenderPlayerEvent, type: IBaubleRender.RenderType) {
		if (type == IBaubleRender.RenderType.BODY) {
			val player = event.entityPlayer
			if (isActive(stack)) {
				when (stack.itemDamage) {
					0 -> {
						if (player.ticksExisted % 10 == 0) {
							val playerHead = Vector3.fromEntityCenter(player).add(0.0, 0.75, 0.0)
							val playerShift = playerHead.copy().add(getHeadOrientation(player))
							val color = Color(ColorOverrideHelper.getColor(player, 0x0079C4))
							val innerColor = Color(color.rgb).brighter().brighter()
							Botania.proxy.lightningFX(player.worldObj, playerHead, playerShift, 2.0f, color.rgb, innerColor.rgb)
						}
					}
					
					1 -> {
						if (player.ticksExisted % 10 == 0) {
							for (i in 0..6) {
								val xmotion = (Math.random() - 0.5).toFloat() * 0.15f
								val zmotion = (Math.random() - 0.5).toFloat() * 0.15f
								val color = Color(ColorOverrideHelper.getColor(player, 0x964B00))
								val r = color.red.toFloat() / 255F
								val g = color.green.toFloat() / 255F
								val b = color.blue.toFloat() / 255F
								Botania.proxy.wispFX(player.worldObj, player.posX, player.posY - player.yOffset, player.posZ, r, g, b, Math.random().toFloat() * 0.15f + 0.15f, xmotion, 0.0075f, zmotion)
							}
						}
					}
					
					2 -> {
						if (player.ticksExisted % 10 == 0) {
							for (i in 0..6) {
								val vec = getHeadOrientation(player).multiply(0.52)
								val color = Color(ColorOverrideHelper.getColor(player, 0x0101FF))
								val r = color.red.toFloat() / 255F
								val g = color.green.toFloat() / 255F
								val b = color.blue.toFloat() / 255F
								Botania.proxy.sparkleFX(player.worldObj, player.posX + vec.x, player.posY + vec.y, player.posZ + vec.z, r, g, b, 1.0f, 5)
							}
						}
					}
					
					3 -> {
						val color = Color(ColorOverrideHelper.getColor(player, 0xF94407))
						val r = color.red / 255f
						val g = color.green / 255f
						val b = color.blue / 255f
						
						for (i in 1..9) {
							val pos = ASJVec.fromEntity(player).add(0.0, -player.yOffset + 0.25, 0.0).add(ASJVec(0.0, 0.0, 0.5).rotate(Botania.proxy.worldElapsedTicks * 5 % 360 + i*40.0, ASJVec.oY))
							Botania.proxy.sparkleFX(player.worldObj, pos.x, pos.y, pos.z, r, g, b, 1f, 4)
						}
					}
				}
			}
			
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture)
			IBaubleRender.Helper.rotateIfSneaking(player)
			val armor = player.getCurrentArmor(2) != null
			GL11.glRotatef(180F, 1F, 0F, 0F)
			GL11.glTranslatef(-0.26F, -0.4F, if (armor) 0.21F else 0.15F)
			GL11.glScalef(0.5F, 0.5F, 0.5F)
			
			val icon = getBaubleIconFromDamage(stack.itemDamage)
			if (icon != null)
				ItemRenderer.renderItemIn2D(Tessellator.instance, icon.maxU, icon.minV, icon.minU, icon.maxV, icon.iconWidth, icon.iconHeight, 1F / 32F)
		}
	}
}
