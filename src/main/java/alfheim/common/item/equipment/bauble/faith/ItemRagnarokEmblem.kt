package alfheim.common.item.equipment.bauble.faith

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alexsocol.asjlib.render.ASJRenderHelper
import alfheim.api.ModInfo
import alfheim.common.achievement.AlfheimAchievements
import alfheim.common.core.helper.IconHelper
import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.AlfheimItems
import alfheim.common.item.equipment.bauble.ItemPriestEmblem
import baubles.api.BaubleType
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.common.eventhandler.*
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.texture.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraftforge.client.event.*
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.player.PlayerDropsEvent
import org.lwjgl.opengl.GL11
import vazkii.botania.api.item.IBaubleRender
import vazkii.botania.common.core.helper.ItemNBTHelper.*
import vazkii.botania.common.item.equipment.bauble.ItemBauble

class ItemRagnarokEmblem: ItemBauble("ragnarokEmblem"), IBaubleRender {
	
	lateinit var gemIcons: Array<IIcon>
	
	init {
		creativeTab = AlfheimTab
		setHasSubtypes(true)
	}
	
	override fun canEquip(stack: ItemStack?, player: EntityLivingBase?) =
		player is EntityPlayer && player.hasAchievement(AlfheimAchievements.ragnarok)
	
	override fun canUnequip(stack: ItemStack?, player: EntityLivingBase?) =
		player !is EntityPlayer || !player.hasAchievement(AlfheimAchievements.theEND)
	
	override fun getUnlocalizedNameInefficiently(par1ItemStack: ItemStack) =
		super.getUnlocalizedNameInefficiently(par1ItemStack).replace("item\\.botania:".toRegex(), "item.${ModInfo.MODID}:")
	
	override fun getItemStackDisplayName(stack: ItemStack) =
		super.getItemStackDisplayName(stack).replace("&".toRegex(), "\u00a7")
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		itemIcon = IconHelper.forItem(reg, this)
		gemIcons = Array(2) { IconHelper.forItem(reg, this, "Render$it") }
	}
	
	override fun getIcon(stack: ItemStack, pass: Int) = if (getBoolean(stack, TAG_GEM_FLAG, false)) gemIcons.safeGet(pass) else itemIcon!!
	
	override fun requiresMultipleRenderPasses() = true
	
	override fun hasEffect(stack: ItemStack?, pass: Int) =
		pass == 0 && getBoolean(stack, TAG_BOUND, false)
	
	override fun getBaubleType(stack: ItemStack) = BaubleType.AMULET
	
	override fun onPlayerBaubleRender(stack: ItemStack?, event: RenderPlayerEvent, type: IBaubleRender.RenderType?) {
		if (type == IBaubleRender.RenderType.BODY) {
			val player = event.entityPlayer
			
			if (player != mc.thePlayer || mc.gameSettings.thirdPersonView != 0) {
				val invert = player.rng.nextBoolean()
				val (mx, my, mz) = Vector3().rand().sub(0.5).normalize().mul(0.1)
				val (x, y, z) = Vector3.fromEntity(player).add(0, player.height * 0.6875, 0)
				if (invert)
					mc.theWorld.spawnParticle("smoke", x + mx * 10, y + my * 10, z + mz * 10, -mx, -my, -mz)
				else
					mc.theWorld.spawnParticle("smoke", x, y, z, mx, my, mz)
			}
			
			mc.renderEngine.bindTexture(TextureMap.locationItemsTexture)
			IBaubleRender.Helper.rotateIfSneaking(player)
			val armor = player.getCurrentArmor(2) != null
			GL11.glTranslatef(-15 / 64f, 0f, -1 * if (armor) 0.21F else 0.15F)
			glScalef(0.5f)
			
			for ((id, baubleIcon) in gemIcons.withIndex()) {
				if (id != 0) ASJRenderHelper.setGlow()
				ItemRenderer.renderItemIn2D(Tessellator.instance, baubleIcon.maxU, baubleIcon.minV, baubleIcon.minU, baubleIcon.maxV, baubleIcon.iconWidth, baubleIcon.iconHeight, 1F / 32F)
				if (id != 0) ASJRenderHelper.discard()
			}
		}
	}
	
	companion object RagnarokHandler {
		
		const val TAG_BOUND = "ragnarok.bound"
		const val TAG_GEM_FLAG = "renderGem"
		const val TAG_CONSUMED = "consumedPowers"
		
		var ragnarok = false
		var fogFade = 1f
		
		init {
			eventForge()
		}
		
		fun beginRagnarok(player: EntityPlayer) {
			if (!player.hasAchievement(AlfheimAchievements.ragnarok)) return
			val emblem = getEmblem(player) ?: return
			setBoolean(emblem, TAG_BOUND, true)
			player.triggerAchievement(AlfheimAchievements.theEND)
			
			ragnarok = true
			// TODO the rest
		}
		
		@SubscribeEvent(priority = EventPriority.LOWEST) // let it be canceled
		fun onPlayerDied(e: LivingDeathEvent) {
			if (!ragnarok) return
			
			val ragnar = e.source.entity as? EntityPlayer ?: return
			val priest = e.entityLiving as? EntityPlayer ?: return
			
			val emblemDark = getEmblem(ragnar) ?: return
			val emblemLight = ItemPriestEmblem.getEmblem(-1, priest) ?: return
			
			if (ragnar.heldItem?.item !== AlfheimItems.soulSword) return
			
			when (emblemLight.item) {
				AlfheimItems.priestEmblem   -> {
					val arr = getByteArray(emblemDark, TAG_CONSUMED, ByteArray(6))
					if (arr[emblemLight.meta] > 0) return
					arr[emblemLight.meta] = 1
					setByteArray(emblemDark, TAG_CONSUMED, arr)
				}
				
				AlfheimItems.ragnarokEmblem -> {
					val arr = getByteArray(emblemDark, TAG_CONSUMED, ByteArray(6))
					val id = arr.indexOfFirst { it < 1 }
					if (id == -1) return
					arr[id] = 1
					setByteArray(emblemDark, TAG_CONSUMED, arr)
				}
				
				else                        -> return
			}
			
			ragnar.playSoundAtEntity("mob.enderdragon.growl", 10f, 0.1f)
			--emblemLight.stackSize
		}
		
		// technical stuff
		
		@SubscribeEvent(priority = EventPriority.LOWEST) // for Baubles to add it's contents
		fun notSoEasy(e: PlayerDropsEvent) {
			val i = e.drops.indexOfFirst { getBoolean(it.entityItem, TAG_BOUND, false) }
			if (i == -1) return
			
			val entity = e.drops.removeAt(i) ?: return
			PlayerHandler.getPlayerBaubles(e.entityPlayer)[0] = entity.entityItem?.copy() ?: return
			entity.setEntityItemStack(null)
			entity.setDead()
		}
		
		@SubscribeEvent
		fun fogColor(e: EntityViewRenderEvent.FogColors) {
			if (!ragnarok) return
			
			if (e.entity.dimension == 1) return // not sure
			
			if (fogFade > 0) fogFade -= 0.001f
			
			e.red += (1f - e.red) * (1 - fogFade)
			e.green *= fogFade
			e.blue *= fogFade
		}
		
		fun getEmblem(player: EntityPlayer?): ItemStack? {
			val baubles = PlayerHandler.getPlayerBaubles(player ?: return null)
			val stack = baubles[0] ?: return null
			return if (stack.item === AlfheimItems.ragnarokEmblem) stack else null
		}
	}
}