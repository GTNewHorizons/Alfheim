package alfheim.common.item.equipment.bauble

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.api.ModInfo
import alfheim.api.item.ColorOverrideHelper
import alfheim.api.item.equipment.bauble.IManaDiscountBauble
import alfheim.client.render.world.VisualEffectHandlerClient
import alfheim.common.core.handler.VisualEffectHandler
import alfheim.common.core.helper.IconHelper
import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.equipment.bauble.faith.IFaithHandler
import baubles.api.BaubleType
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.texture.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraftforge.client.event.RenderPlayerEvent
import org.lwjgl.opengl.GL11.*
import vazkii.botania.api.item.IBaubleRender
import vazkii.botania.api.mana.*
import vazkii.botania.common.Botania
import vazkii.botania.common.item.equipment.bauble.ItemBauble
import java.awt.Color

class ItemAesirEmblem: ItemBauble("aesirEmblem"), IBaubleRender, IManaUsingItem, IManaDiscountBauble {
	
	val COST = 2 * ItemPriestEmblem.TYPES
	lateinit var baubleIcon: IIcon
	
	init {
		creativeTab = AlfheimTab
		setHasSubtypes(true)
	}
	
	override fun getUnlocalizedNameInefficiently(par1ItemStack: ItemStack) =
		super.getUnlocalizedNameInefficiently(par1ItemStack).replace("item\\.botania:".toRegex(), "item.${ModInfo.MODID}:")
	
	override fun getItemStackDisplayName(stack: ItemStack) =
		super.getItemStackDisplayName(stack).replace("&".toRegex(), "\u00a7")
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		itemIcon = IconHelper.forItem(reg, this)
		baubleIcon = IconHelper.forItem(reg, this, "Render")
	}
	
	override fun getBaubleType(stack: ItemStack) = BaubleType.AMULET
	
	override fun onEquippedOrLoadedIntoWorld(stack: ItemStack, player: EntityLivingBase) {
		if (player is EntityPlayer) for (i in 0 until ItemPriestEmblem.TYPES) IFaithHandler.getFaithHandler(i).onEquipped(stack, player, IFaithHandler.FaithBauble.EMBLEM)
	}
	
	override fun onWornTick(stack: ItemStack, player: EntityLivingBase) {
		if (player is EntityPlayer) {
			if (player.ticksExisted % 10 == 0) {
				val flag: Boolean
				ItemPriestEmblem.setActive(stack, ManaItemHandler.requestManaExact(stack, player, COST, true).also { flag = it })
				VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.EMBLEM_ACTIVATION, player.dimension, player.entityId.D, if (flag) 1.0 else 0.0)
			}
			
			for (i in 0 until ItemPriestEmblem.TYPES) IFaithHandler.getFaithHandler(i).onWornTick(stack, player, IFaithHandler.FaithBauble.EMBLEM)
		}
	}
	
	override fun onUnequipped(stack: ItemStack, player: EntityLivingBase) {
		if (player is EntityPlayer) for (i in 0 until ItemPriestEmblem.TYPES) IFaithHandler.getFaithHandler(i).onUnequipped(stack, player, IFaithHandler.FaithBauble.EMBLEM)
	}
	
	override fun usesMana(stack: ItemStack) = true
	
	@SideOnly(Side.CLIENT)
	override fun onPlayerBaubleRender(stack: ItemStack, event: RenderPlayerEvent, type: IBaubleRender.RenderType) {
		if (type == IBaubleRender.RenderType.BODY) {
			val player = event.entityPlayer
			if (player.ticksExisted % 10 == 0 && !(player == mc.thePlayer && mc.gameSettings.thirdPersonView == 0)) {
				val shift = IFaithHandler.getHeadOrientation(player).mul(0.25)
				val (x, y, z) = Vector3.fromEntity(player).add(shift)
				val (mx, my, mz) = shift.mul(0.1).F
				val color = Color(ColorOverrideHelper.getColor(player, 0xFFFFFF))
				val r = color.red.F / 255f
				val g = color.green.F / 255f
				val b = color.blue.F / 255f
				Botania.proxy.wispFX(player.worldObj, x, y + 1.62, z, r, g, b, Math.random().F * 0.15f + 0.15f, mx, my, mz)
			}
			
			mc.renderEngine.bindTexture(TextureMap.locationItemsTexture)
			IBaubleRender.Helper.rotateIfSneaking(player)
			val armor = player.getCurrentArmor(2) != null
			glRotatef(180F, 1F, 0F, 0F)
			glTranslatef(-0.26F, -0.4F, if (armor) 0.21F else 0.15F)
			glScalef(0.5f)
			
			ItemRenderer.renderItemIn2D(Tessellator.instance, baubleIcon.maxU, baubleIcon.minV, baubleIcon.minU, baubleIcon.maxV, baubleIcon.iconWidth, baubleIcon.iconHeight, 1F / 32F)
		}
	}
	
	override fun getDiscount(stack: ItemStack, slot: Int, player: EntityPlayer) = 0.1f
}
