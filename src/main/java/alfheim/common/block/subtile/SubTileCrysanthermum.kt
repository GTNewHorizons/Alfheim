package alfheim.common.block.subtile

import alfheim.api.ModInfo
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
@Suppress("SameParameterValue")
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
			val distance = (inp - x1).toFloat() / (x2 - x1).toFloat()
			return (distance * (y2 - y1)).toInt() + y1
		}
		
		private val rand = Random()
		fun getTemp(meta: Int): Int = if (meta % 8 == 3) rand.nextInt(9) - 4 else TYPES[meta]
	}
	
	var temperature: Int = 0
	
	override fun onUpdate() {
		super.onUpdate()
		
		val remote = supertile.worldObj.isRemote
		val biomeStone = Item.getItemFromBlock(ModFluffBlocks.biomeStoneA)
		val items = supertile.worldObj.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB.getBoundingBox((supertile.xCoord - RANGE).toDouble(), (supertile.yCoord - RANGE).toDouble(), (supertile.zCoord - RANGE).toDouble(), (supertile.xCoord + RANGE + 1).toDouble(), (supertile.yCoord + RANGE + 1).toDouble(), (supertile.zCoord + RANGE + 1).toDouble()))
		val slowdown = slowdownFactor
		
		if (ticksExisted % 600 == 0) {
			// 30 seconds
			if (temperature > 0) temperature--
			else if (temperature < 0) temperature++
		}
		
		for (item in items) {
			if (item is EntityItem) {
				val stack = item.entityItem
				if (stack != null && stack.item === biomeStone && !item.isDead && item.age >= slowdown) {
					val meta = stack.itemDamage % 8
					if (!remote && temperature != 8 && temperature != -8) {
						// TODO mana algorithm
						setTemp(temperature + getTemp(meta))
						sync()
					}
					
					for (i in 0..9) {
						val m = 0.2f
						val mx = (Math.random() - 0.5).toFloat() * m
						val my = (Math.random() - 0.5).toFloat() * m
						val mz = (Math.random() - 0.5).toFloat() * m
						supertile.worldObj.spawnParticle("blockcrack_" + Item.getIdFromItem(stack.item) + "_" + meta, item.posX, item.posY, item.posZ, mx.toDouble(), my.toDouble(), mz.toDouble())
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
	
	override fun getComparatorInputOverride(side: Int): Int = map(temperature, -8, 8, 0, 15)
	override fun getRadius(): RadiusDescriptor = RadiusDescriptor.Square(toChunkCoordinates(), RANGE)
	override fun getMaxMana() = 8000 // TODO decide actual mana value
	override fun getColor() = Color.HSBtoRGB(map(temperature, -8, 8, 235, 360) / 360f, 1f, 1f)
	override fun getEntry() = null // LexiconRegistry.crysanthermum // TODO
	
	fun setTemp(temp: Int) {
		temperature = max(min(temp, 8), -8)
	}
	
	override fun renderHUD(mc: Minecraft, res: ScaledResolution) {
		super.renderHUD(mc, res)
		
		GL11.glEnable(GL11.GL_BLEND)
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
		val name = StatCollector.translateToLocal("misc.${ModInfo.MODID}:temperature.${temperature + 8}")
		val width = 16 + mc.fontRenderer.getStringWidth(name) / 2
		val x = res.scaledWidth / 2 - width
		val y = res.scaledHeight / 2 + 30
		
		mc.fontRenderer.drawStringWithShadow(name, x + 20, y, color)
		
		GL11.glDisable(GL11.GL_LIGHTING)
		GL11.glDisable(GL11.GL_BLEND)
	}
	
	override fun writeToPacketNBT(cmp: NBTTagCompound) {
		super.writeToPacketNBT(cmp)
		cmp.setInteger(TAG_TEMPERATURE, temperature)
	}
	
	override fun readFromPacketNBT(cmp: NBTTagCompound) {
		super.readFromPacketNBT(cmp)
		temperature = cmp.getInteger(TAG_TEMPERATURE)
	}
	
	override fun getIcon(): IIcon? = BotaniaAPI.getSignatureForName("crysanthermum").getIconForStack(null)
	
}
