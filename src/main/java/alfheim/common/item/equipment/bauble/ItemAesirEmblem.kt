package alfheim.common.item.equipment.bauble

import alfheim.api.ModInfo
import alfheim.api.item.ColorOverrideHelper
import alfheim.common.core.helper.IconHelper
import baubles.api.BaubleType
import cpw.mods.fml.relauncher.*
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.texture.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
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
import kotlin.properties.Delegates

class ItemAesirEmblem: ItemBauble("aesirEmblem"), IBaubleRender, IManaUsingItem {
	
	val COST = 2 * ItemPriestEmblem.TYPES
	var baubleIcon: IIcon by Delegates.notNull()
	
	init {
		setHasSubtypes(true)
	}
	
	override fun addInformation(par1ItemStack: ItemStack?, par2EntityPlayer: EntityPlayer?, par3List: MutableList<Any?>?, par4: Boolean) {
		if (par1ItemStack == null) return
		this.addStringToTooltip("&7" + StatCollector.translateToLocal("misc.${ModInfo.MODID}.creative") + "&r", par3List)
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4)
	}
	
	fun addStringToTooltip(s: String, tooltip: MutableList<Any?>?) {
		tooltip!!.add(s.replace("&".toRegex(), "\u00a7"))
	}
	
	override fun getUnlocalizedNameInefficiently(par1ItemStack: ItemStack) =
		super.getUnlocalizedNameInefficiently(par1ItemStack).replace("item\\.botania:".toRegex(), "item.${ModInfo.MODID}:")
	
	override fun getItemStackDisplayName(stack: ItemStack) =
		super.getItemStackDisplayName(stack).replace("&".toRegex(), "\u00a7")
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(par1IconRegister: IIconRegister) {
		itemIcon = IconHelper.forItem(par1IconRegister, this)
		baubleIcon = IconHelper.forItem(par1IconRegister, this, "Render")
	}
	
	override fun getBaubleType(stack: ItemStack) = BaubleType.AMULET
	
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
				if (ManaItemHandler.requestManaExact(stack, player, COST, true)) ItemNBTHelper.setByte(stack, "active", 1.toByte())
				else ItemNBTHelper.setByte(stack, "active", 0.toByte())
			}
		}
	}
	
	override fun usesMana(stack: ItemStack) = true
	
	@SideOnly(Side.CLIENT)
	override fun onPlayerBaubleRender(stack: ItemStack, event: RenderPlayerEvent, type: IBaubleRender.RenderType) {
		if (type == IBaubleRender.RenderType.BODY) {
			val player = event.entityPlayer
			if (player.ticksExisted % 10 == 0) {
				val shift = getHeadOrientation(player)
				val x = player.posX + shift.x * 0.25
				val y = player.posY + shift.y * 0.25
				val z = player.posZ + shift.z * 0.25
				val xmotion = shift.x.toFloat() * 0.025f
				val ymotion = shift.y.toFloat() * 0.025f
				val zmotion = shift.z.toFloat() * 0.025f
				val color = Color(ColorOverrideHelper.getColor(player, 0xFFFFFF))
				val r = color.red.toFloat() / 255f
				val g = color.green.toFloat() / 255f
				val b = color.blue.toFloat() / 255f
				Botania.proxy.wispFX(player.worldObj, x, y, z, r, g, b, Math.random().toFloat() * 0.15f + 0.15f, xmotion, ymotion, zmotion)
			}
			
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture)
			IBaubleRender.Helper.rotateIfSneaking(player)
			val armor = player.getCurrentArmor(2) != null
			GL11.glRotatef(180F, 1F, 0F, 0F)
			GL11.glTranslatef(-0.26F, -0.4F, if (armor) 0.21F else 0.15F)
			GL11.glScalef(0.5F, 0.5F, 0.5F)
			
			ItemRenderer.renderItemIn2D(Tessellator.instance, baubleIcon.maxU, baubleIcon.minV, baubleIcon.minU, baubleIcon.maxV, baubleIcon.iconWidth, baubleIcon.iconHeight, 1F / 32F)
		}
	}
}
