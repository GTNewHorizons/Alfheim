package alfheim.common.item.equipment.bauble

import alfheim.AlfheimCore
import alfheim.common.core.util.AlfheimTab
import alfheim.common.integration.travellersgear.*
import baubles.api.BaubleType
import cpw.mods.fml.common.Optional
import cpw.mods.fml.relauncher.*
import net.minecraft.client.Minecraft
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderPlayerEvent
import org.lwjgl.opengl.GL11
import vazkii.botania.api.item.IBaubleRender
import vazkii.botania.common.item.equipment.bauble.ItemBauble

@Optional.Interface(modid = "TravellersGear", iface = "alfheim.common.integration.travellersgear.ITravellersGearSynced", striprefs = true)
abstract class ItemBaubleCloak(name: String): ItemBauble(name), ITravellersGearSynced, IBaubleRender {
	
	companion object {
		
		@SideOnly(Side.CLIENT)
		var model: ModelBiped? = null
	}
	
	init {
		creativeTab = AlfheimTab
	}
	
	override fun getBaubleType(stack: ItemStack) =
		if (AlfheimCore.TravellersGearLoaded) null else BaubleType.BELT
	
	override fun getSlot(stack: ItemStack) = 0
	
	override fun onTravelGearEquip(player: EntityPlayer, stack: ItemStack) {
		super.onTravelGearEquip(player, stack)
		onEquippedOrLoadedIntoWorld(stack, player)
	}
	
	override fun onTravelGearTickSynced(player: EntityPlayer, stack: ItemStack) {
		onWornTick(stack, player)
	}
	
	override fun onTravelGearUnequip(player: EntityPlayer, stack: ItemStack) {
		super.onTravelGearUnequip(player, stack)
		onUnequipped(stack, player)
	}
	
	override fun addHiddenTooltip(stack: ItemStack, player: EntityPlayer, tooltip: MutableList<Any?>, adv: Boolean) {
		TravellerBaubleTooltipHandler.addHiddenTooltip(this, stack, tooltip)
	}
	
	abstract fun getCloakTexture(stack: ItemStack): ResourceLocation
	
	open fun getCloakGlowTexture(stack: ItemStack): ResourceLocation? = null
	
	@SideOnly(Side.CLIENT)
	override fun onPlayerBaubleRender(stack: ItemStack, event: RenderPlayerEvent, type: IBaubleRender.RenderType) {
		if (type == IBaubleRender.RenderType.BODY) {
			IBaubleRender.Helper.rotateIfSneaking(event.entityPlayer)
			val armor = event.entityPlayer.getCurrentArmor(2) != null
			GL11.glTranslatef(0.0f, if (armor) -0.07f else -0.01f, 0.0f)
			val s = 0.1f
			GL11.glScalef(s, s, s)
			if (model == null)
				model = ModelBiped()
			
			Minecraft.getMinecraft().renderEngine.bindTexture(getCloakTexture(stack))
			model!!.bipedBody.render(1.0f)
			
			getCloakGlowTexture(stack)?.let { res ->
				val prevX = OpenGlHelper.lastBrightnessX
				val prevY = OpenGlHelper.lastBrightnessY
				GL11.glDisable(GL11.GL_LIGHTING)
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)
				Minecraft.getMinecraft().renderEngine.bindTexture(res)
				model!!.bipedBody.render(1.0f)
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, prevX, prevY)
				GL11.glEnable(GL11.GL_LIGHTING)
			}
		}
	}
}