package alfheim.common.block.material

import net.minecraft.block.material.*
import vazkii.botania.common.item.equipment.tool.ToolCommons

class MaterialCustomSmeltingWood: Material(MapColor.woodColor) {
	
	companion object {
		val instance = MaterialCustomSmeltingWood()
		
		init {
			ToolCommons.materialsAxe = append(ToolCommons.materialsAxe, instance)
		}
		
		@Suppress("UNCHECKED_CAST")
		fun <T> append(arr: Array<T>, element: T): Array<T> {
			val N = arr.size
			val newarr = arr.copyOf(N + 1)
			newarr[N] = element
			return newarr as Array<T>
		}
	}
	
	init {
		setBurning()
	}
}
