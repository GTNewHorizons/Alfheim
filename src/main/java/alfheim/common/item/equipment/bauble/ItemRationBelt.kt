package alfheim.common.item.equipment.bauble

import alexsocol.asjlib.*
import alfheim.api.lib.LibResourceLocations
import baubles.api.BaubleType
import com.mojang.authlib.GameProfile
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.client.Minecraft
import net.minecraft.client.model.ModelBiped
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.potion.Potion
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent
import org.lwjgl.opengl.GL11
import vazkii.botania.api.item.IBaubleRender
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.item.equipment.bauble.ItemBauble

class ItemRationBelt: ItemBauble("RationBelt"), IBaubleRender {
	
	companion object {
		
		@field:SideOnly(Side.CLIENT)
		var model: ModelBiped? = null
			@SideOnly(Side.CLIENT)
			get() = field
			@SideOnly(Side.CLIENT)
			set(value) {
				field = value
			}
		
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
	
	override fun onWornTick(stack: ItemStack, player: EntityLivingBase) {
		if (player is EntityPlayer && !player.worldObj.isRemote && player.ticksExisted % 20 == 0) {
			val foods = mutableMapOf<Int, ItemStack>()
			for (i in 0..8) {
				val food = player.inventory.getStackInSlot(i) ?: continue
				if (isEdible(food, player) || (food.item == ModItems.infiniteFruit && ManaItemHandler.requestManaExact(food, player, 500, false)))
					foods[i] = food
			}
			
			val food = foods.entries.maxBy {
				when (val item = it.value.item) {
					is ItemFood            -> item.func_150906_h(it.value) * item.func_150905_g(it.value)
					ModItems.infiniteFruit -> Float.MAX_VALUE
					else                   -> 0f
				}
			} ?: return
			
			if (food.value.item is ItemFood) {
				var newFood = food.value.onFoodEaten(player.worldObj, player)
				// Not sure if this is needed anymore
				if (newFood?.let { it.stackSize <= 0} == true)
					newFood = null
				
				player.inventory.setInventorySlotContents(food.key, newFood)
			} else if (food.value.item == ModItems.infiniteFruit) {
				ManaItemHandler.requestManaExact(food.value, player, 500, true)
				for (i in 0 until 20) player.foodStats.addStats(1, 1f)
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
	
	private fun isEdible(food: ItemStack?, player: EntityPlayer): Boolean {
		if (food == null) return false
		if (!player.canEat(false)) return false
		
		var flag = false
		
		if (food.item is ItemFood) {
			
			if ((food.item as ItemFood).alwaysEdible) return false
			
			flag = true
			for (i in 0..15) {
				val fakePlayer = FakePlayerPotion(player.worldObj, GameProfile(null, "foodBeltPlayer"))
				fakePlayer.testFinishItemUse(food)
				
				var returnFlag = true
				
				if (fakePlayer.activePotionEffects.isNotEmpty())
					returnFlag = returnFlag && fakePlayer.isPotionActive(Potion.field_76443_y) && fakePlayer.activePotionEffects.size == 1
				if (fakePlayer.posX != 0.0 && fakePlayer.posY != 1000.0 && fakePlayer.posZ != 0.0)
					returnFlag = false
				flag = flag && returnFlag
				
				if (!flag) return false
			}
		}
		return flag
	}
	
	class FakePlayerPotion(world: World, profile: GameProfile) : EntityPlayer(world, profile) {
		
		init {
			setPosition(0.0, 1000.0, 0.0)
		}
		
		fun testFinishItemUse(stack: ItemStack) {
			captureSounds = true
			stack.copy().onFoodEaten(worldObj, this)
			captureSounds = false
		}
		
		override fun addChatMessage(component: IChatComponent?) = Unit
		override fun canCommandSenderUseCommand(lvl: Int, command: String?) = false
		override fun getPlayerCoordinates() = ChunkCoordinates(0, 1000, 0)
	}
}
