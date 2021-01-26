package alexsocol.arctitles

import alexsocol.asjlib.*
import alexsocol.asjlib.extendables.ASJConfigHandler
import alexsocol.asjlib.math.Vector3
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.client.renderer.*
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.potion.*
import net.minecraft.util.*
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.common.config.Configuration
import org.lwjgl.opengl.GL11
import travellersgear.TravellersGear
import travellersgear.api.*
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.ItemNBTHelper
import java.awt.Color

const val MODID = "arctitles"

@cpw.mods.fml.common.Mod(modid = MODID, name = "Arc Titles", version = "12", useMetadata = true)
class ArcTitlesMain {
	
	companion object {
		lateinit var title: Item
	}
	
	@Mod.EventHandler
	fun preInit(e: FMLPreInitializationEvent) {
		ArcTitlesConfigHandler.loadConfig(e.suggestedConfigurationFile)
		if (ASJUtilities.isClient) RenderArcTitle.eventForge()
		title = ItemArcTitle("arctitle")
	}
}

object ArcTitlesConfigHandler: ASJConfigHandler() {
	
	lateinit var colors: Array<Pair<Int, Int>>
	var names = Array(1) { "HelloWorld" }
	lateinit var particles: IntArray
	lateinit var potions: Array<ArrayList<Array<Int>>> // [meta][potion][params]
	
	const val CATEGORY_COLORS = Configuration.CATEGORY_GENERAL + Configuration.CATEGORY_SPLITTER + "colors"
	const val CATEGORY_PARTICLES = Configuration.CATEGORY_GENERAL + Configuration.CATEGORY_SPLITTER + "particles"
	const val CATEGORY_POTIONS = Configuration.CATEGORY_GENERAL + Configuration.CATEGORY_SPLITTER + "potions"
	
	override fun addCategories() {
		addCategory(CATEGORY_COLORS, "Hex color pairs [hex1 hex2]")
		addCategory(CATEGORY_PARTICLES, "Particle types (0 - none, 1 - glow, 2 - nimbus)")
		addCategory(CATEGORY_POTIONS, "Potion lists [id duration power once-per-N-ticks]")
	}
	
	override fun readProperties() {
		names = loadProp(Configuration.CATEGORY_GENERAL, "names", names, true, "Title code names (only A-z)")
		
		colors = Array(names.size) { 0 to 0 }
		particles = IntArray(names.size)
		potions = Array(names.size) { ArrayList<Array<Int>>() }
		
		for (i in names.indices) {
			loadProp(CATEGORY_COLORS, "color_${names[i]}", "FFFF00 00AAFF", true, "Color pair for ${names[i]}")
				.split(" ").also { colors[i] = it[0].toInt(16) to it[1].toInt(16) }
			
			loadProp(CATEGORY_PARTICLES, "particle_${names[i]}", 1, true, "Particle type for ${names[i]}", 0, 2)
				.also { particles[i] = it }
			
			loadProp(CATEGORY_POTIONS, "potionlist_${names[i]}", arrayOf("1 600 2 1"), true, "Potion list for ${names[i]}").forEach { s ->
				s.split(" ").map { it.toInt() }.toTypedArray().also { potions[i].add(it) }
			}
		}
	}
}

class ItemArcTitle(name: String): Item(), ITravellersGear {
	
	companion object {
		
		const val TAG_TITLE = "title"
		const val TITLE_PREFIX = "TG.personaltitle.arc."
	}
	
	init {
		creativeTab = TravellersGear.creativeTab
		hasSubtypes = true
		maxStackSize = 1
		setTextureName("$MODID:$name")
		unlocalizedName = name
		
		GameRegistry.registerItem(this, name)
	}
	
	override fun getSubItems(item: Item?, tab: CreativeTabs?, list: MutableList<Any?>) {
		ArcTitlesConfigHandler.names.forEachIndexed { meta, title -> list.add(ItemStack(item, 1, meta).also { stack -> ItemNBTHelper.setString(stack, TAG_TITLE, "$TITLE_PREFIX$title") }) }
	}
	
	override fun addInformation(stack: ItemStack?, player: EntityPlayer?, list: MutableList<Any?>, adv: Boolean) {
		list.add(StatCollector.translateToLocal(ItemNBTHelper.getString(stack, TAG_TITLE, "")))
	}
	
	override fun onTravelGearTick(player: EntityPlayer, stack: ItemStack) {
		val potions = ArcTitlesConfigHandler.potions[stack.meta]
		
		for (data in potions) {
			if (player.ticksExisted % data[3] == 0) {
				var pe = player.getActivePotionEffect(data[0])
				
				if (pe == null) {
					pe = PotionEffect(data[0], data[1], data[2])
					player.addPotionEffect(pe)
				} else {
					pe.duration = data[1]
					pe.amplifier = data[2]
				}
				
				player.addPotionEffect(PotionEffect(data[0], data[1], data[2]))
			}
		}
	}
	
	override fun getSlot(stack: ItemStack?) = 3
	
	override fun onTravelGearEquip(player: EntityPlayer?, stack: ItemStack?) = Unit
	override fun onTravelGearUnequip(player: EntityPlayer?, stack: ItemStack?) = Unit
}

object RenderArcTitle {
	
	val texture = Array(ArcTitlesConfigHandler.names.size) { ResourceLocation(MODID, "textures/model/entity/title/title${ArcTitlesConfigHandler.names[it]}.png") }
	
	@SubscribeEvent
	fun onPlayerRender(e: RenderPlayerEvent.Specials.Post) {
		val player = e.entityPlayer
		if (player.isInvisible || player.isInvisibleToPlayer(mc.thePlayer) || player.isPotionActive(Potion.invisibility)) return
		if (player == mc.thePlayer && mc.gameSettings.thirdPersonView == 0) return
		
		val stack = TravellersGearAPI.getExtendedInventory(player)[3] ?: return
		if (stack.item !== ArcTitlesMain.title) return
		
		val id = stack.meta
		val type = ArcTitlesConfigHandler.particles[id]
		
		val delay = if (type == 2) 1 else 10
		
		if (player.ticksExisted % delay == 0 && !mc.isGamePaused) {
			val world = player.worldObj
			
			when (type) {
				0 -> return
				
				1 -> {
					val v = Vector3()
					val yOff = if (player === mc.thePlayer) -1.6 else 0.0
					
					val c1 = Color(ArcTitlesConfigHandler.colors[id].first)
					val r1 = c1.red / 255f
					val g1 = c1.green / 255f
					val b1 = c1.blue / 255f
					
					val c2 = Color(ArcTitlesConfigHandler.colors[id].second)
					val r2 = c2.red / 255f
					val g2 = c2.green / 255f
					val b2 = c2.blue / 255f
					
					v.rand().sub(0.5).normalize().mul(player.width, player.height, player.width).mul(Math.random()).extend(0.5).add(player).add(0, yOff + player.height / 2, 0)
					Botania.proxy.wispFX(world, v.x, v.y, v.z, r1, g1, b1, 0.3f)
					
					v.rand().sub(0.5).normalize().mul(player.width, player.height, player.width).mul(Math.random()).add(player).add(0, yOff + player.height / 2, 0)
					Botania.proxy.wispFX(world, v.x, v.y, v.z, r2, g2, b2, 0.3f)
				}
				
				2 -> {
					val color = Color(ArcTitlesConfigHandler.colors[id].first)
					val r = color.red / 255f
					val g = color.green / 255f
					val b = color.blue / 255f
					
					val (x, y, z) = Vector3.fromEntity(player)
					
					for (i in 1..9) {
						val pos = Vector3(x, y + 1.9, z).add(Vector3(0.0, 0.0, 0.5).rotate(Botania.proxy.worldElapsedTicks * 5 % 360 + i * 40.0, Vector3.oY))
						Botania.proxy.sparkleFX(mc.theWorld, pos.x, pos.y, pos.z, r, g, b, 1f, 4)
					}
				}
			}
		}
		
		GL11.glPushMatrix()
		GL11.glEnable(GL11.GL_BLEND)
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
		GL11.glDisable(GL11.GL_CULL_FACE)
		GL11.glDisable(GL11.GL_LIGHTING)
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)
		GL11.glShadeModel(GL11.GL_SMOOTH)
		
		GL11.glRotated(mc.theWorld.totalWorldTime / 2.0 + mc.timer.renderPartialTicks, 0.0, 1.0, 0.0)
		
		GL11.glTranslated(0.0, 1.35, 0.0)
		GL11.glColor4f(1f, 1f, 1f, 1f)
		
		mc.renderEngine.bindTexture(texture[id])
		val tes = Tessellator.instance
		tes.startDrawingQuads()
		tes.addVertexWithUV(-1.0, 0.0, -1.0, 0.0, 0.0)
		tes.addVertexWithUV(-1.0, 0.0, 1.0, 0.0, 1.0)
		tes.addVertexWithUV(1.0, 0.0, 1.0, 1.0, 1.0)
		tes.addVertexWithUV(1.0, 0.0, -1.0, 1.0, 0.0)
		tes.draw()
		GL11.glColor4f(1f, 1f, 1f, 1f)
		
		GL11.glShadeModel(GL11.GL_FLAT)
		GL11.glEnable(GL11.GL_LIGHTING)
		GL11.glEnable(GL11.GL_CULL_FACE)
		GL11.glDisable(GL11.GL_BLEND)
		GL11.glPopMatrix()
	}
}