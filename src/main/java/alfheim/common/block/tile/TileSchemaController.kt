package alfheim.common.block.tile

import alexsocol.asjlib.ASJUtilities
import alfheim.api.ModInfo
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.util.*
import codechicken.core.CommonUtils
import com.google.gson.*
import cpw.mods.fml.common.registry.*
import cpw.mods.fml.relauncher.FMLLaunchHandler
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ChatComponentText
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.client.core.handler.ClientTickHandler
import vazkii.botania.common.Botania
import vazkii.botania.common.block.tile.TileMod
import java.awt.Color
import java.io.*
import java.util.*
import kotlin.math.abs

open class TileSchemaController: TileMod() {
	
	private var ticksAlive: Int = 0
	private var lastDump: Int = 0
	
	var pos_x: Pos? = null
	var pos_y: Pos? = null
	var pos_z: Pos? = null
	
	var mark_x: Pos? = null
	var mark_z: Pos? = null
	
	var pos_xoz: Pos? = null
	var pos_xyx: Pos? = null
	var pos_zyz: Pos? = null
	var pos_xyz: Pos? = null
	var range: Int = 64
	
	var validDir: MutableList<ForgeDirection> = ForgeDirection.VALID_DIRECTIONS.toMutableList()
	
	class Pos(val x: Int, val y: Int, val z: Int, val dir: ForgeDirection)
	
	override fun updateEntity() {
		20.tickDelay {
			for (v in arrayOf(1, 2, 3)) {
				isPosValid(v)
			}
			
			if (mark_x != null) {
				val block = worldObj.getBlock(mark_x!!.x, mark_x!!.y, mark_x!!.z)
				if (block !== AlfheimBlocks.schemaMarker) {
					mark_x = null
				}
			}
			
			if (mark_z != null) {
				val block = worldObj.getBlock(mark_z!!.x, mark_z!!.y, mark_z!!.z)
				if (block !== AlfheimBlocks.schemaMarker) {
					mark_z = null
				}
			}
		}
		
		if (pos_x != null && pos_y != null && pos_z != null) {
			if (pos_xoz == null && pos_xyx == null && pos_zyz == null && pos_xyz == null) {
				pos_xoz = Pos(pos_x!!.x, yCoord, pos_z!!.z, ForgeDirection.UNKNOWN)
				pos_xyz = Pos(pos_x!!.x, pos_y!!.y, pos_z!!.z, ForgeDirection.UNKNOWN)
				pos_xyx = Pos(pos_x!!.x, pos_y!!.y, pos_x!!.z, ForgeDirection.UNKNOWN)
				pos_zyz = Pos(pos_z!!.x, pos_y!!.y, pos_z!!.z, ForgeDirection.UNKNOWN)
				
			} else {
				20.tickDelay {
					if (mark_x == null) {
						for (x in xCoord re pos_x!!.x) {
							for (y in yCoord re pos_y!!.y) {
								val block = worldObj.getBlock(x, y, zCoord)
								
								Botania.proxy.wispFX(worldObj, x + 0.5, y + 0.5, zCoord + 0.5, 1f, 0f, 0f, 0.25f)
								
								if (block === AlfheimBlocks.schemaMarker) {
									if (mark_z == null || y == mark_z!!.y) {
										mark_x = Pos(x, y, zCoord, ForgeDirection.UNKNOWN)
									}
								}
							}
						}
					}
					if (mark_z == null) {
						for (z in zCoord re pos_z!!.z) {
							for (y in yCoord re pos_y!!.y) {
								val block = worldObj.getBlock(xCoord, y, z)
								
								Botania.proxy.wispFX(worldObj, xCoord + 0.5, y + 0.5, z + 0.5, 1f, 0f, 0f, 0.25f)
								
								if (block === AlfheimBlocks.schemaMarker) {
									if (mark_x == null || y == mark_x!!.y) {
										mark_z = Pos(xCoord, y, z, ForgeDirection.UNKNOWN)
									}
								}
							}
						}
					}
				}
				
				10.tickDelay {
					drawBoundingLine(null, pos_x!!)
					drawBoundingLine(null, pos_y!!)
					drawBoundingLine(null, pos_z!!)
					
					drawBoundingLine(pos_z!!, pos_zyz!!)
					drawBoundingLine(pos_z!!, pos_xoz!!)
					
					drawBoundingLine(pos_x!!, pos_xyx!!)
					drawBoundingLine(pos_x!!, pos_xoz!!)
					
					drawBoundingLine(pos_y!!, pos_xyx!!)
					drawBoundingLine(pos_y!!, pos_zyz!!)
					
					drawBoundingLine(pos_xyx!!, pos_xyz!!)
					drawBoundingLine(pos_xoz!!, pos_xyz!!)
					drawBoundingLine(pos_zyz!!, pos_xyz!!)
					
					if (mark_x != null) {
						drawBoundingLine(mark_x, Pos(mark_x!!.x, mark_x!!.y, pos_z!!.z, ForgeDirection.UNKNOWN))
					}
					if (mark_z != null) {
						drawBoundingLine(mark_z, Pos(pos_x!!.x, mark_z!!.y, mark_z!!.z, ForgeDirection.UNKNOWN))
					}
				}
			}
		} else {
			10.tickDelay {
				for (v in arrayOf(1, 2, 3)) {
					val pos = getPos(v)
					if (pos != null) drawBoundingLine(null, pos)
				}
				
				for (dir in validDir)
					for (i in 0..64) {
						Botania.proxy.wispFX(worldObj,
											 xCoord + (dir.offsetX * i) + .5,
											 yCoord + (dir.offsetY * i) + .5,
											 zCoord + (dir.offsetZ * i) + .5,
                                             abs(dir.offsetY).F, abs(dir.offsetX).F,
                                             abs(dir.offsetZ).F, 0.3f, -0.01f)
					}
			}
			
			30.tickDelay {
				loop@ for (dir in ForgeDirection.VALID_DIRECTIONS) {
					if (dir in validDir) {
						for (i in 1..range) {
							val x = xCoord + (dir.offsetX * i)
							val y = yCoord + (dir.offsetY * i)
							val z = zCoord + (dir.offsetZ * i)
							
							val block = worldObj.getBlock(x, y, z)
							
							if (block === AlfheimBlocks.schemaMarker) {
								setPos(getType(dir), Pos(x, y, z, dir))
								validDir.remove(dir)
								validDir.remove(dir.opposite)
								continue@loop
							}
						}
					}
				}
			}
		}
		
		ticksAlive++
	}
	
	protected infix fun Int.re(to: Int): IntRange {
		var f: Int = this
		var t: Int = to
		
		if (f > t) {
			f = to
			t = this
		}
		
		return (f + 1) until t
	}
	
	private fun isPosValid(i: Int) {
		val pos = getPos(i)
		if (pos != null) {
			val block = worldObj.getBlock(pos.x, pos.y, pos.z)
			
			if (block !== AlfheimBlocks.schemaMarker) {
				validDir.add(pos.dir)
				validDir.add(pos.dir.opposite)
				clearInferred()
				setPos(i, null)
			}
		}
	}
	
	fun getType(dir: ForgeDirection): Int {
        return when (dir) {
            ForgeDirection.UP, ForgeDirection.DOWN     -> 2
            ForgeDirection.NORTH, ForgeDirection.SOUTH -> 3
            ForgeDirection.EAST, ForgeDirection.WEST   -> 1
            else                                       -> -1
        }
	}
	
	private fun Int.dif(i: Int): Int = abs(this - i) * if (this < i) 1 else -1
	
	private fun getPos(i: Int): Pos? {
        return when (i) {
            1    -> pos_x
            2    -> pos_y
            else -> pos_z
        }
	}
	
	private fun setPos(i: Int, pos: Pos?) {
		when (i) {
			1    -> pos_x = pos
			2    -> pos_y = pos
			else -> pos_z = pos
		}
	}
	
	private fun clearInferred() {
		pos_xoz = null
		pos_xyx = null
		pos_zyz = null
		pos_xyz = null
		
		mark_x = null
		mark_z = null
	}
	
	private fun drawBoundingLine(pos1: Pos?, pos2: Pos?) {
		val dir = getDir(pos1, pos2)
		
		val x = pos1?.x ?: xCoord
		val y = pos1?.y ?: yCoord
		val z = pos1?.z ?: zCoord
		
		val x2 = pos2?.x ?: xCoord
		val y2 = pos2?.y ?: yCoord
		val z2 = pos2?.z ?: zCoord
		
		if (checkPos(x2.D, y2.D, z2.D, x.D, y.D, z.D)) {
			drawBoundingLine(pos2, pos1)
			return
		}
		
		
		loop@ for (i in 0..256) {
			coloredFlame(x + .5 + (dir.offsetX * (i * .25)),
						 y + .5 + (dir.offsetY * (i * .25)),
						 z + .5 + (dir.offsetZ * (i * .25)))
			
			if (checkPos(
					x2.D, y2.D, z2.D,
					x + (dir.offsetX * (i * .25)),
					y + (dir.offsetY * (i * .25)),
					z + (dir.offsetZ * (i * .25))))
				break@loop
			
		}
	}
	
	fun checkPos(x: Double, y: Double, z: Double, x2: Double, y2: Double, z2: Double) =
        abs(x) < abs(x2) || abs(y) < abs(y2) || abs(z) < abs(z2)
	
	private fun getDir(pos1: Pos?, pos2: Pos?): ForgeDirection {
		val x = pos1?.x ?: xCoord
		val y = pos1?.y ?: yCoord
		val z = pos1?.z ?: zCoord
		
		val x2 = pos2?.x ?: xCoord
		val y2 = pos2?.y ?: yCoord
		val z2 = pos2?.z ?: zCoord
		
		if (x > x2) return ForgeDirection.WEST
		if (x < x2) return ForgeDirection.EAST
		if (y > y2) return ForgeDirection.DOWN
		if (y < y2) return ForgeDirection.UP
		if (z > z2) return ForgeDirection.NORTH
		if (z < z2) return ForgeDirection.SOUTH
		
		return ForgeDirection.UNKNOWN
	}
	
	private fun coloredFlame(x: Double, y: Double, z: Double) {
		val v = 0.1f
		val r = (getColor() shr 16 and 255).F / 255f + (Math.random() - 0.5).F * v
		val g = (getColor() shr 8 and 255).F / 255f + (Math.random() - 0.5).F * v
		val b = (getColor() and 255).F / 255f + (Math.random() - 0.5).F * v
		
		Botania.proxy.wispFX(worldObj, x, y, z, r, g, b, 0.3f, -0.01f)
	}
	
	private fun getColor(): Int {
		if (FMLLaunchHandler.side().isServer) return 0xFFFFFF
		var time = ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks
		time += Random((xCoord xor yCoord xor zCoord).toLong()).nextInt(100000)
		return Color.HSBtoRGB(time * 0.005F, 1F, 1F)
	}
	
	open fun blockActivated(p0: EntityPlayer?) {
		if (!worldObj.isRemote && ticksAlive - lastDump > 60) {
			lastDump = ticksAlive
			if (pos_x != null && pos_y != null && pos_z != null && mark_x != null && mark_z != null) {
				dumpFile(p0)
			} else {
				p0?.addChatMessage(ChatComponentText("Missing Markers"))
			}
		}
	}
	
	fun dumpFile(p0: EntityPlayer?) {
		try {
			val e = getNewFile()
			dumpTo(e)
			
			ASJUtilities.say(p0, "Schema dumped to: ${e.path}")
		} catch (var2: Exception) {
			ASJUtilities.say(p0, "Error dumping schema")
			ASJUtilities.error("Error dumping schema: ${var2.message}")
			var2.printStackTrace()
		}
	}
	
	fun getNewFile(file: Int = 0): File {
		val e = File(CommonUtils.getMinecraftDir(), "dumps/" + getFileName("schema_dump_$file"))
		if (!e.parentFile.exists()) e.parentFile.mkdirs()
		
		if (!e.exists()) e.createNewFile()
		else return getNewFile(file + 1)
		
		return e
	}
	
	fun getFileName(prefix: String) = prefix + getFileExtension()
	
	fun getFileExtension() = ".txt"
	
	@Throws(IOException::class)
	fun dumpTo(file: File) {
		class LocationElement(val x: Int, val y: Int, val z: Int, val meta: Int, val nbt: NBTTagCompound?) {
			fun getJson(): JsonObject = JsonObject().apply {
				addProperty("x", x)
				addProperty("y", y)
				addProperty("z", z)
				addProperty("meta", meta)
				if (nbt != null)
					addProperty("nbt", nbt.toString())
			}
		}
		
		val w = PrintWriter(file)
		val map: HashMap<String, MutableList<LocationElement>> = HashMap()
		
		for (x in xCoord re pos_x!!.x) {
			for (y in yCoord re pos_y!!.y) {
				for (z in zCoord re pos_z!!.z) {
					val meta = worldObj.getBlockMetadata(x, y, z)
					val key = getUniqueName(worldObj.getBlock(x, y, z))
					var nbt: NBTTagCompound? = null
					worldObj.getTileEntity(x, y, z)?.let {
						nbt = NBTTagCompound()
						it.writeToNBT(nbt)
					}
					
					if (map.containsKey(key))
						map[key]?.add(LocationElement(mark_x!!.x.dif(x), mark_x!!.y.dif(y), mark_z!!.z.dif(z), meta, nbt))
					else map[key] = arrayListOf(LocationElement(mark_x!!.x.dif(x), mark_x!!.y.dif(y), mark_z!!.z.dif(z), meta, nbt))
				}
			}
		}
		
		w.print(JsonArray().apply {
			for (k in map.keys) {
				if (k != "${ModInfo.MODID}:schemaFiller") {
					add(JsonObject().apply {
						addProperty("block", k)
						add("location", JsonArray().apply { for (v in map[k].orEmpty()) add(v.getJson()) })
					})
				}
			}
		})
		
		w.close()
	}
	
	internal fun getUniqueName(block: Block): String {
		val name = GameData.getBlockRegistry().getNameForObject(block)
		val ui = GameRegistry.UniqueIdentifier(name)
		
		return "${ui.modId}:${ui.name}"
	}
	
	fun Int.tickDelay(lambda: () -> Any) {
		if (ticksAlive % this == 0) {
			lambda.invoke()
		}
	}
	
	override fun writeCustomNBT(nbttagcompound: NBTTagCompound) = Unit
	
	override fun readCustomNBT(nbttagcompound: NBTTagCompound) = Unit
}