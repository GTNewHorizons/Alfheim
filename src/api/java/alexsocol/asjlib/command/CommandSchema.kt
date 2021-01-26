package alexsocol.asjlib.command

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import com.google.gson.*
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.FMLInjectionData
import net.minecraft.block.Block
import net.minecraft.command.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import org.apache.commons.lang3.tuple.MutablePair
import java.io.*
import kotlin.math.abs

object CommandSchema: CommandBase() {
	
	val poses = HashMap<String, MutablePair<Vector3?, Vector3?>>()
	
	override fun getCommandName() = "asjschema"
	
	override fun getCommandUsage(sender: ICommandSender?) = "asjcore.commands.schema.usage"
	
	override fun processCommand(sender: ICommandSender, args: Array<String>) {
		if (sender !is EntityPlayer) return
		val name = sender.commandSenderName
		
		val pos = Vector3.fromEntity(sender)
		if (poses[name] == null)
			poses[name] = MutablePair()
		
		when (args[0]) {
			"pos1" -> poses[name]!!.left = pos
			"pos2" -> poses[name]!!.right = pos
			"save" -> {
				if (poses[name]!!.left == null) return
				if (poses[name]!!.right == null) return
				dumpFile(sender, args.getOrNull(1))
			}
		}
	}
	
	override fun addTabCompletionOptions(sender: ICommandSender?, args: Array<String>): MutableList<Any?>? {
		return when (args.size) {
			1 -> getListOfStringsMatchingLastWord(args, "pos1", "pos2", "save")
			2 -> getListOfStringsFromIterableMatchingLastWord(args, Block.blockRegistry.keys)
			else -> null
		}
	}
	
	
	
	// copy from Alfheim:
	
	fun dumpFile(player: EntityPlayer, filler: String?) {
		try {
			val e = getNewFile()
			dumpTo(e, player, filler)
			
			ASJUtilities.say(player, "Schema dumped to: ${e.path}")
		} catch (e: Exception) {
			ASJUtilities.say(player, "Error dumping schema")
			ASJUtilities.error("Error dumping schema: ${e.message}")
			e.printStackTrace()
		}
	}
	
	fun getNewFile(file: Int = 0): File {
		val e = File(FMLInjectionData.data()[6] as File, "dumps/" + getFileName("schema_dump_$file"))
		if (!e.parentFile.exists()) e.parentFile.mkdirs()
		
		if (!e.exists()) e.createNewFile()
		else return getNewFile(file + 1)
		
		return e
	}
	
	fun getFileName(prefix: String) = prefix + getFileExtension()
	
	fun getFileExtension() = ".txt"
	
	@Throws(IOException::class)
	fun dumpTo(file: File, player: EntityPlayer, filler: String?) {
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
		val world = player.worldObj
		val (mx, my, mz) = Vector3.fromEntity(player).mf()
		val (sx, sy, sz) = poses[player.commandSenderName]!!.left!!.I
		val (ex, ey, ez) = poses[player.commandSenderName]!!.right!!.I
		
		for (x in sx re ex) {
			for (y in sy re ey) {
				for (z in sz re ez) {
					val meta = world.getBlockMetadata(x, y, z)
					val key = getUniqueName(world.getBlock(x, y, z))
					var nbt: NBTTagCompound? = null
					
					world.getTileEntity(x, y, z)?.let {
						nbt = NBTTagCompound()
						it.writeToNBT(nbt)
						nbt!!.removeTag("x")
						nbt!!.removeTag("y")
						nbt!!.removeTag("z")
					}
					
					if (map.containsKey(key))
						map[key]?.add(LocationElement(mx.dif(x), my.dif(y), mz.dif(z), meta, nbt))
					else map[key] = arrayListOf(LocationElement(mx.dif(x), my.dif(y), mz.dif(z), meta, nbt))
				}
			}
		}
		
		w.print(JsonArray().apply {
			for (k in map.keys) {
				if (k != filler) {
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
		return GameRegistry.findUniqueIdentifierFor(block).toString()
	}
	
	private infix fun Int.re(to: Int): IntRange {
		var f: Int = this
		var t: Int = to
		
		if (f > t) {
			f = to
			t = this
		}
		
		return (f + 1) until t
	}
	
	private fun Int.dif(i: Int): Int = abs(this - i) * if (this < i) 1 else -1
}