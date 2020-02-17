package alfheim.common.block.schema

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.nbt.*
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

object SchemaGenerator {
	
	fun generate(world: World, x: Int, y: Int, z: Int, schemaText: String) {
		val type = object: TypeToken<List<BlockElement>>() {}.type
		
		val arr = Gson().fromJson<List<BlockElement>>(schemaText, type)
		world.setBlock(x, y, z, Blocks.air, 0, 4)
		
		for (ele in arr) {
			for (loc in ele.location) {
				world.setBlock(x + loc.x, y + loc.y, z + loc.z, Block.getBlockFromName(ele.block), loc.meta, 3)
				if (loc.nbt != null) {
					val tile = TileEntity.createAndLoadEntity(JsonToNBT.func_150315_a(loc.nbt) as NBTTagCompound)
					tile.xCoord = x + loc.x
					tile.yCoord = y + loc.y
					tile.zCoord = z + loc.z
					world.setTileEntity(x + loc.x, y + loc.y, z + loc.z, tile)
				}
			}
		}
	}
}

class BlockElement(val block: String, val location: List<LocationElement>)

class LocationElement(val x: Int, val y: Int, val z: Int, val meta: Int, val nbt: String?)