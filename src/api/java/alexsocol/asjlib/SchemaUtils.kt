package alexsocol.asjlib

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.nbt.*
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

object SchemaUtils {
	
	fun generate(world: World, x: Int, y: Int, z: Int, schemaText: String) {
		val type = object: TypeToken<List<BlockElement>>() {}.type
		
		val arr = Gson().fromJson<List<BlockElement>>(schemaText, type)
		world.setBlock(x, y, z, Blocks.air, 0, 4)
		
		for (ele in arr) {
			for (loc in ele.location) {
				world.setBlock(x + loc.x, y + loc.y, z + loc.z, Block.getBlockFromName(ele.block), loc.meta, 3)
				
				if (loc.nbt != null) {
					val tile = TileEntity.createAndLoadEntity(JsonToNBT.func_150315_a(loc.nbt) as NBTTagCompound) ?: return
					tile.xCoord = x + loc.x
					tile.yCoord = y + loc.y
					tile.zCoord = z + loc.z
					world.setTileEntity(x + loc.x, y + loc.y, z + loc.z, tile)
				}
			}
		}
	}
	
	fun checkStructure(world: World, x: Int, y: Int, z: Int, structure: String, onFail: ((Int, Int, Int, Int) -> Unit)? = null): Boolean {
		val type = object: TypeToken<List<BlockElement>>() {}.type
		
		val arr = Gson().fromJson<List<BlockElement>>(structure, type)
		
		for (ele in arr) {
			for (loc in ele.location) {
				val i = x + loc.x
				val j = y + loc.y
				val k = z + loc.z
				
				fun check(): Boolean {
					if (world.getBlock(i, j, k) != Block.getBlockFromName(ele.block) || world.getBlockMetadata(i, j, k) != loc.meta)
						return false
					
					loc.nbt ?: return true
					val locNBT = JsonToNBT.func_150315_a(loc.nbt) as NBTTagCompound
					
					val tile = world.getTileEntity(i, j, k) ?: return false
					val landNBT = NBTTagCompound()
					tile.writeToNBT(landNBT)
					
					for (entry in locNBT.tagMap)
						if (entry.value != landNBT.tagMap[entry.key]) return false
					
					return true
				}
				
				if (!check()) {
					onFail?.invoke(world.provider.dimensionId, i, j, k)
					return false
				}
			}
		}
		
		return true
	}
	
	fun loadStructure(path: String): String {
		return javaClass.getResourceAsStream("/assets/$path").use {
			it.readBytes().toString(Charsets.UTF_8)
		}
	}
}

class BlockElement(val block: String, val location: List<LocationElement>)

class LocationElement(val x: Int, val y: Int, val z: Int, val meta: Int, val nbt: String?)