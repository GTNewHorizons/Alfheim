package alfheim.common.item.equipment.bauble

import alexsocol.asjlib.*
import alfheim.api.lib.LibResourceLocations
import baubles.api.BaubleType
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.client.Minecraft
import net.minecraft.client.model.ModelBiped
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent
import org.lwjgl.opengl.GL11
import vazkii.botania.api.item.IBaubleRender
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.item.equipment.bauble.ItemBauble
import vazkii.botania.common.item.relic.ItemInfiniteFruit

class ItemRationBelt: ItemBauble("RationBelt"), IBaubleRender {
	
	companion object {
		
		@field:SideOnly(Side.CLIENT)
		var model: ModelBiped? = null
			@SideOnly(Side.CLIENT)
			get
			@SideOnly(Side.CLIENT)
			set
		
		var captureSounds = false
		
		init {
			eventForge()
		}
		
		@SubscribeEvent
		fun onSoundPlayed(e: PlaySoundAtEntityEvent) {
			if (captureSounds) e.isCanceled = true
		}
	}
	
	override fun getBaubleType(stack: ItemStack) = BaubleType.BELT
	
	override fun onWornTick(stack: ItemStack, entity: EntityLivingBase) {
		if (entity.worldObj.isRemote) return
		
		val player = entity as? EntityPlayer ?: return
		val stats = player.foodStats
		
		if (player.ticksExisted % 80 == 0 && stats.foodLevel < 20) {
			for (i in 0..8) {
				val slot = player.inventory[i] ?: continue
				val item = slot.item
				
				if (item is ItemFood) {
					player.inventory[i] = item.onEaten(slot, player.worldObj, player)
					return
				} else if (item is ItemInfiniteFruit && ManaItemHandler.requestManaExactForTool(stack, player, 500, true)) {
					stats.addStats(1, 1f)
					return
				}
			}
		}
	}
	
	override fun onPlayerBaubleRender(stack: ItemStack, event: RenderPlayerEvent, type: IBaubleRender.RenderType) {
		if (type == IBaubleRender.RenderType.BODY) {
			
			if (model == null) {
				model = ModelBiped()
			}
			
			Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.rationBelt)
			IBaubleRender.Helper.rotateIfSneaking(event.entityPlayer)
			
			if (!event.entityPlayer.isSneaking)
				GL11.glTranslatef(0F, 0.2F, 0F)
			
			val s = 1.05F / 16F
			glScalef(s)
			
			(model as ModelBiped).bipedBody.render(1F)
		}
	}
}
