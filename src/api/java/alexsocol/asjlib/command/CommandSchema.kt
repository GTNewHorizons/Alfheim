package alexsocol.asjlib.command

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import com.google.gson.*
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.FMLInjectionData
import net.minecraft.block.Block
import net.minecraft.command.*
import net.minecraft.nbt.NBTTagCompound
import org.apache.commons.lang3.tuple.MutablePair
import java.io.*
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.component3
import kotlin.collections.set
import kotlin.math.abs

object CommandSchema: CommandBase() {
	
	val poses = HashMap<String, MutablePair<Vector3?, Vector3?>>()
	
	override fun getCommandAliases() = listOf("asjs")
	
	override fun getCommandName() = "asjschema"
	
	override fun getCommandUsage(sender: ICommandSender?) = "/$commandName <pos1> | <pos2> | <save> [filler] | <load> <filename>"
	
	override fun processCommand(sender: ICommandSender, args: Array<String>) {
		val name = sender.commandSenderName
		
		try {
			val (x, y, z) = sender.playerCoordinates
			val pos = Vector3(x, y, z)
			if (poses[name] == null) poses[name] = MutablePair()
			
			when (args[0]) {
				"pos1" -> {
					poses[name]!!.left = pos
					ASJUtilities.say(sender, "asjcore.commands.schema.posSet", 1, x, y, z)
				}
				"pos2" -> {
					poses[name]!!.right = pos
					ASJUtilities.say(sender, "asjcore.commands.schema.posSet", 2, x, y, z)
				}
				"save" -> {
					if (poses[name]!!.left == null) throw WrongUsageException("asjcore.commands.schema.noPosN", 1)
					if (poses[name]!!.right == null) throw WrongUsageException("asjcore.commands.schema.noPosN", 2)
					dumpFile(sender, args.getOrNull(1))
				}
				"load" -> {
					val file = File(args[1])
					if (!file.exists()) throw CommandException("asjcore.commands.schema.noFile", args[1])
					SchemaUtils.generate(sender.entityWorld, x, y, z, file.readText())
					ASJUtilities.say(sender, "asjcore.commands.schema.loadOk")
				}
				else -> {
					throw WrongUsageException(getCommandUsage(sender))
				}
			}
		} catch (e: Throwable) {
			if (e is CommandException) throw e
			throw WrongUsageException(getCommandUsage(sender), e)
		}
	}
	
	override fun addTabCompletionOptions(sender: ICommandSender?, args: Array<String>): MutableList<Any?>? {
		return when (args.size) {
			1    -> getListOfStringsMatchingLastWord(args, "pos1", "pos2", "save", "load")
			2    -> if (args[0] == "save") getListOfStringsFromIterableMatchingLastWord(args, Block.blockRegistry.keys) else null
			else -> null
		}
	}
	
	// copy from Alfheim:
	
	fun dumpFile(sender: ICommandSender, filler: String?) {
		try {
			val e = getNewFile()
			var name = filler
			
			if (name != null) try {
				val id = name.toInt()
				name = getUniqueName(Block.getBlockById(id))
			} catch (ignore: NumberFormatException) {
			}
			
			dumpTo(e, sender, name)
			
			ASJUtilities.say(sender, "asjcore.commands.schema.dumpOk", e.path)
		} catch (e: Exception) {
			ASJUtilities.say(sender, "asjcore.commands.schema.dumpNo", e.message ?: "")
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
	
	fun dumpTo(file: File, sender: ICommandSender, filler: String?) {
		class LocationElement(val x: Int, val y: Int, val z: Int, val meta: Int, val nbt: NBTTagCompound?) {
			
			fun getJson(): JsonObject = JsonObject().apply {
				if (x != 0) addProperty("x", x)
				if (y != 0) addProperty("y", y)
				if (z != 0) addProperty("z", z)
				if (meta != 0) addProperty("meta", meta)
				if (nbt != null) addProperty("nbt", nbt.toString())
			}
		}
		
		val w = PrintWriter(file)
		val map: HashMap<String, MutableList<LocationElement>> = HashMap()
		val world = sender.entityWorld
		val (mx, my, mz) = sender.playerCoordinates
		val (sx, sy, sz) = poses[sender.commandSenderName]!!.left!!.I
		val (ex, ey, ez) = poses[sender.commandSenderName]!!.right!!.I
		
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
					
					if (map.containsKey(key)) map[key]?.add(LocationElement(mx.dif(x), my.dif(y), mz.dif(z), meta, nbt))
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
		
		return f..t
	}
	
	private fun Int.dif(i: Int): Int = abs(this - i) * if (this < i) 1 else -1
}