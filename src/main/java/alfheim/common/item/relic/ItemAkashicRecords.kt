package alfheim.common.item.relic

import alexsocol.asjlib.mc
import alexsocol.asjlib.render.ASJRenderHelper
import alfheim.api.ModInfo
import alfheim.api.item.relic.record.AkashicRecord
import alfheim.api.lib.LibResourceLocations
import alfheim.client.model.item.ModelAkashicBox
import alfheim.common.item.AlfheimItems
import alfheim.common.item.relic.AkashikModels.bookModel
import alfheim.common.item.relic.AkashikModels.boxModel
import alfheim.common.item.relic.record.AkashicRecordNewChance
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.PlayerEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.client.model.ModelBook
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.item.ItemTossEvent
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.core.helper.ItemNBTHelper.*
import vazkii.botania.common.item.relic.ItemRelic
import kotlin.math.max

class ItemAkashicRecords: ItemRelic("AkashicRecords") {
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		// ASJUtilities.chatLog("Set in use")
		player.setItemInUse(stack, getMaxItemUseDuration(stack))
		return stack
	}
	
	override fun onUsingTick(stack: ItemStack, player: EntityPlayer, left: Int) {
		// ASJUtilities.chatLog("Use tick ($left left)")
		
		// TODO play some "progress" sound
	}
	
	override fun getMaxItemUseDuration(stack: ItemStack) = 1200
	
	override fun onPlayerStoppedUsing(stack: ItemStack, world: World, player: EntityPlayer, left: Int) {
		// ASJUtilities.chatLog("Stopped. Left: $left")
		
		if (left > getMaxItemUseDuration(stack) - 20) {
			if (player.isSneaking) {
				setBoolean(stack, TAG_SWITCH, true)
			} else {
				if (isOpen(stack))
					nextRecord(player, stack)
			}
		} else Unit // TODO play some "fail" sound
	}
	
	override fun onEaten(stack: ItemStack, world: World?, player: EntityPlayer): ItemStack {
		// ASJUtilities.chatLog("Eaten!")
		
		if (isOpen(stack))
			generateRecord(player, stack)
		
		player.clearItemInUse()
		
		return stack
	}
	
	override fun onUpdate(stack: ItemStack, world: World?, entity: Entity, slot: Int, inHand: Boolean) {
		super.onUpdate(stack, world, entity, slot, inHand)
		
		if (!inHand) {
			setInt(stack, TAG_MULT, -1)
			setInt(stack, TAG_FRAME, 0)
			return
		}
		
		if (!entity.isSneaking) setFloat(stack, TAG_ROTATION, ItemFlugelSoul.getCheckingAngle(entity))
		
		var frame = (getInt(stack, TAG_FRAME, 0) + getInt(stack, TAG_MULT, -1))
		
		if (frame == 160) frame = 60
		
		if ((frame % 100 == 60 || frame == -1) && getBoolean(stack, TAG_SWITCH, false)) {
			setInt(stack, TAG_MULT, getInt(stack, TAG_MULT, -1) * -1)
			setBoolean(stack, TAG_SWITCH, false)
			setInt(stack, TAG_FRAME, if (frame != -1) 60 else 1)
		} else {
			setInt(stack, TAG_FRAME, max(0, frame))
		}
	}
	
	override fun onEntitySwing(living: EntityLivingBase, stack: ItemStack): Boolean {
		if (living is EntityPlayer && isOpen(stack))
			cast(living, stack)
		
		return true
	}
	
	override fun registerIcons(reg: IIconRegister) = Unit
	
	companion object {
		
		const val TAG_ROTATION = "rotation"
		
		const val TAG_FRAME = "frame"
		const val TAG_MULT = "mult"
		const val TAG_SWITCH = "switch"
		
		const val MANA_PER_RECORD = 1000000
		const val MAX_RECORDS = 20
		const val TAG_RECORD_PREF = "record_"
		const val TAG_RECORD_COUNT = "records"
		const val TAG_RECORD_SELECT = "record"
		
		val records = HashMap<String, AkashicRecord>()
		val recordTextures = HashMap<String, ResourceLocation>()
		
		init {
			MinecraftForge.EVENT_BUS.register(this)
			
			// registerRecord(AkashicRecordGinnungagap)
			registerRecord(AkashicRecordNewChance)
		}
		
		fun isOpen(stack: ItemStack) = getInt(stack, TAG_FRAME, 0) > 60 && getInt(stack, TAG_MULT, -1) == 1 && !getBoolean(stack, TAG_SWITCH, false)
		
		fun generateRecord(player: EntityPlayer, stack: ItemStack) {
			if (generateRecordActual(player, stack)) {
				// TODO play some "success" sound
			} else {
				// TODO play some "fail" sound
			}
		}
		
		fun generateRecordActual(player: EntityPlayer, stack: ItemStack): Boolean {
			if (records.isEmpty()) return false
			
			val contains = getInt(stack, TAG_RECORD_COUNT, 0)
			if (contains == MAX_RECORDS) return false
			if (contains > MAX_RECORDS) throw IllegalArgumentException("Records count in Akashik Records cannot be greater than $MAX_RECORDS. Holder: ${player.commandSenderName}")
			
			var record: AkashicRecord = records.values.random() // stupid IntelliJ -_-
			var tries = 32
			
			while (tries-- > 0) {
				if (record.canGet(player, stack)) break
				record = records.values.random()
			}
			
			if (tries < 0) return false
			if (!ManaItemHandler.requestManaExact(stack, player, MANA_PER_RECORD, true)) return false
			
			setInt(stack, TAG_RECORD_COUNT, contains + 1)
			
			for (i in 0 until MAX_RECORDS) {
				if (!verifyExistance(stack, "$TAG_RECORD_PREF$i")) {
					setString(stack, "$TAG_RECORD_PREF$i", record.name)
					break
				}
			}
			
			return true
		}
		
		fun nextRecord(player: EntityPlayer, stack: ItemStack) {
			val contains = getInt(stack, TAG_RECORD_COUNT, 0)
			if (contains <= 0) return
			if (contains > MAX_RECORDS)
				throw IllegalArgumentException("Records count in Akashik Records cannot be greater than $MAX_RECORDS. Holder: ${player.commandSenderName}")
			
			val startedAt = getInt(stack, TAG_RECORD_SELECT, 0)
			var cycle = startedAt
			do {
				cycle = (cycle + 1) % MAX_RECORDS
				if (verifyExistance(stack, "$TAG_RECORD_PREF$cycle")) break
			} while (cycle != startedAt)
			
			setInt(stack, TAG_RECORD_SELECT, cycle)
		}
		
		fun cast(player: EntityPlayer, stack: ItemStack) {
			records[getString(stack, "$TAG_RECORD_PREF${getInt(stack, TAG_RECORD_SELECT, 0)}", "")]?.let {
				if (it.apply(player, stack)) {
					stack.tagCompound.tagMap.remove("$TAG_RECORD_PREF${getInt(stack, TAG_RECORD_SELECT, 0)}")
					setInt(stack, TAG_RECORD_COUNT, getInt(stack, TAG_RECORD_COUNT, 0) - 1)
					nextRecord(player, stack)
				}
			}
		}
		
		fun registerRecord(rec: AkashicRecord) {
			records[rec.name] = rec
			recordTextures[rec.name] = ResourceLocation(ModInfo.MODID, "textures/model/item/record/${rec.name}.png")
		}
		
		@SubscribeEvent
		fun onDropped(e: ItemTossEvent) {
			val stack = e.entityItem?.entityItem ?: return
			
			if (stack.item === AlfheimItems.akashicRecords) {
				setInt(stack, TAG_MULT, -1)
				setInt(stack, TAG_FRAME, 0)
			}
		}
		
		@SubscribeEvent
		fun onPickup(e: PlayerEvent.ItemPickupEvent) {
			val stack = e.pickedUp?.entityItem ?: return
			
			if (stack.item === AlfheimItems.akashicRecords) {
				setInt(stack, TAG_MULT, -1)
				setInt(stack, TAG_FRAME, 0)
			}
		}
		
		@SubscribeEvent
		@SideOnly(Side.CLIENT)
		fun onRenderWorldLast(e: RenderWorldLastEvent) {
			val player = mc.thePlayer
			val stack = player.currentEquippedItem
			if (stack != null && stack.item === AlfheimItems.akashicRecords)
				render(stack, player)
		}
		
		@SideOnly(Side.CLIENT)
		fun render(stack: ItemStack, player: EntityPlayer) {
			val frame = getInt(stack, TAG_FRAME, 0)
			if (frame <= 0) return
			
			glPushMatrix()
			glEnable(GL_BLEND)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			
			ASJRenderHelper.interpolatedTranslation(player)
			glTranslated(-RenderManager.renderPosX, -RenderManager.renderPosY, -RenderManager.renderPosZ)
			glRotatef(getFloat(stack, TAG_ROTATION, 0f) + 15f, 0f, 1f, 0f)
			
			var num = 0
			
			RenderHelper.enableStandardItemLighting()
			glEnable(GL_LIGHTING)
			glEnable(GL12.GL_RESCALE_NORMAL)
			glAlphaFunc(GL_GREATER, 0f)
			for (i in 0..1) {
				glColor4f(1f, 1f, 1f, frame / 60f)
				
				glPushMatrix()
				glRotatef(60f * (i - 1), 0f, 1f, 0f)
				glTranslatef(-2f, 0f, 0f)
				glRotatef(90f, 0f, 1f, 0f)
				
				mc.renderEngine.bindTexture(LibResourceLocations.akashicBox)
				boxModel.render(0.0625f)
				
				glRotatef(90f, 0f, 1f, 0f)
				glTranslated(-0.5, 0.0, 0.5)
				
				for (b in 0 until 7) {
					glTranslatef(0f, 0f, -0.125f)
					if (!verifyExistance(stack, "$TAG_RECORD_PREF${num++}")) continue
					
					recordTextures[getString(stack, "$TAG_RECORD_PREF${num-1}", "")]?.also { mc.renderEngine.bindTexture(it) }
					bookModel.render(null, 0f, 0f, 0f, 0f, 0f, 1f / 16f)
					
					if (getInt(stack, TAG_RECORD_SELECT, 0) == num - 1) {
						glDisable(GL_TEXTURE_2D)
						glColor4f(1f, 0f, 0f, frame / 60f)
						glLineWidth(3f)
						glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)
						bookModel.render(null, 0f, 0f, 0f, 0f, 0f, 1f / 16f)
						glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)
						glColor4f(1f, 1f, 1f, frame / 60f)
						glEnable(GL_TEXTURE_2D)
					}
				}
				
				glPopMatrix()
			}
			glAlphaFunc(GL_GREATER, 0.1f)
			RenderHelper.disableStandardItemLighting()
			
			glDisable(GL_BLEND)
			glColor4f(1f, 1f, 1f, 1f)
			glPopMatrix()
		}
	}
}

// I hate those SideOnly things -_-
private object AkashikModels {
	val boxModel = ModelAkashicBox()
	val bookModel = ModelBook()
}