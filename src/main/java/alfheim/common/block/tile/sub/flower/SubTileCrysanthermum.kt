package alfheim.common.block.tile.sub.flower

import alfheim.api.ModInfo
import alfheim.common.core.util.*
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.Item
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.*
import org.lwjgl.opengl.GL11
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.subtile.*
import vazkii.botania.common.Botania
import vazkii.botania.common.block.ModFluffBlocks
import java.awt.Color
import java.util.*
import kotlin.math.*

/**
 * @author WireSegal
 * Created at 8:37 AM on 2/3/16.
 */
class SubTileCrysanthermum: SubTileGenerating() {
	
	companion object {
		const val TAG_TEMPERATURE = "flowerHeat"
		const val RANGE = 1
		
		val TYPES = arrayOf(
			2, // Forest
			1, // Plains
			-3, // Mountain
			Int.MAX_VALUE, // Mushroom, rand [-4; 4]
			-3, // Swamp
			3, // Desert
			-4, // Taiga
			4 // Mesa
		)
		
		/**
		 * Map inp from an integer range (x1<->x2) to a second (y1<->y2)
		 */
		private fun map(inp: Int, x1: Int, x2: Int, y1: Int, y2: Int): Int {
			val distance = (inp - x1).F / (x2 - x1).F
			return (distance * (y2 - y1)).I + y1
		}
	}
	
	var temp = 0
		set(temp) {
			field = max(min(temp, 8), -8)
			supertile.markDirty()
		}
	
	var deminishing = 0
	var lastBlocks = LinkedList(IntArray(8) { -1 }.toList())
	
	fun getTemp(meta: Int): Int = if (meta % 8 == 3) supertile.worldObj.rand.nextInt(9) - 4 else TYPES[meta]
	val biomeTemp
		get() = supertile.worldObj.getBiomeGenForCoordsBody(supertile.xCoord, supertile.zCoord).getFloatTemperature(supertile.xCoord, supertile.yCoord, supertile.zCoord).D.mfloor()	
	override fun onUpdate() {
		super.onUpdate()
		
		if (linkedCollector == null) return
		
		val remote = supertile.worldObj.isRemote
		val biomeStone = ModFluffBlocks.biomeStoneA.toItem()
		val items = supertile.worldObj.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB.getBoundingBox((supertile.xCoord - RANGE).D, (supertile.yCoord - RANGE).D, (supertile.zCoord - RANGE).D, (supertile.xCoord + RANGE + 1).D, (supertile.yCoord + RANGE + 1).D, (supertile.zCoord + RANGE + 1).D))
		val slowdown = slowdownFactor
		
		if (ticksExisted % 600 == 0) {
			// 30 seconds
			val bt = biomeTemp
			if (temp > bt) temp--
			else if (temp < bt) temp++
		}
		
		for (item in items) {
			if (item is EntityItem) {
				val stack = item.entityItem
				if (stack != null && stack.item === biomeStone && !item.isDead && item.age >= slowdown) {
					val meta = stack.meta % 8
					if (!remote && canGeneratePassively()) {
						deminishing = if (lastBlocks.contains(meta)) 8 else max(0, deminishing - 1)
						if (lastBlocks.size > 7) lastBlocks.removeFirst()
						lastBlocks.addLast(meta)
						temp += getTemp(meta)
						sync()
					}
					
					for (i in 0..9) {
						val m = 0.2f
						val mx = (Math.random() - 0.5).F * m
						val my = (Math.random() - 0.5).F * m
						val mz = (Math.random() - 0.5).F * m
						supertile.worldObj.spawnParticle("blockcrack_" + Item.getIdFromItem(stack.item) + "_" + meta, item.posX, item.posY, item.posZ, mx.D, my.D, mz.D)
					}
					--item.entityItem.stackSize
					if (!remote && item.entityItem.stackSize <= 0)
						item.setDead()
				}
			}
		}
		val c = Color(color)
		if (ticksExisted % 20 == 0)
			Botania.proxy.wispFX(supertile.worldObj, supertile.xCoord + 0.5, supertile.yCoord + 0.75, supertile.zCoord + 0.5, c.red / 255f, c.green / 255f, c.blue / 255f, 0.25f, -0.025f)
	}
	
	override fun getComparatorInputOverride(side: Int): Int = if (temp == -8 || temp == 8) 0 else temp + 8
	override fun getRadius(): RadiusDescriptor = RadiusDescriptor.Square(toChunkCoordinates(), RANGE)
	override fun getMaxMana() = 800
	override fun getValueForPassiveGeneration() = max(0, abs(biomeTemp - temp) / if (deminishing > 0) 2 else 1)
	override fun canGeneratePassively() = temp in -7..7
	override fun getDelayBetweenPassiveGeneration() = 5
	override fun getColor() = Color.HSBtoRGB(map(temp, -8, 8, 235, 360) / 360f, 1f, 1f)
	override fun getEntry() = null // ShadowFoxLexiconData.crysanthermum
	
	override fun renderHUD(mc: Minecraft, res: ScaledResolution) {
		super.renderHUD(mc, res)
		
		GL11.glEnable(GL11.GL_BLEND)
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
		val name = StatCollector.translateToLocal("misc.${ModInfo.MODID}:temperature.${temp + 8}")
		val width = 16 + mc.fontRenderer.getStringWidth(name) / 2
		val x = res.scaledWidth / 2 - width
		val y = res.scaledHeight / 2 + 30
		
		mc.fontRenderer.drawStringWithShadow(name, x + 20, y, color)
		
		GL11.glDisable(GL11.GL_LIGHTING)
		GL11.glDisable(GL11.GL_BLEND)
	}
	
	override fun writeToPacketNBT(nbt: NBTTagCompound) {
		super.writeToPacketNBT(nbt)
		nbt.setInteger(TAG_TEMPERATURE, temp)
		nbt.setInteger("deminishing", deminishing)
		for (i in 0..7)
			nbt.setInteger("last_$i", lastBlocks[i])
	}
	
	override fun readFromPacketNBT(nbt: NBTTagCompound) {
		super.readFromPacketNBT(nbt)
		temp = nbt.getInteger(TAG_TEMPERATURE)
		deminishing = nbt.getInteger("deminishing")
		for (i in 0..7)
			lastBlocks[i] = nbt.getInteger("last_$i")
	}
	
	override fun getIcon(): IIcon? = BotaniaAPI.getSignatureForName("crysanthermum").getIconForStack(null)
}
