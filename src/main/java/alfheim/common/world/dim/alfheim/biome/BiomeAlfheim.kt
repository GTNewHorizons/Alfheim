package alfheim.common.world.dim.alfheim.biome

import alfheim.AlfheimCore
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.entity.EntityElf
import net.minecraft.entity.passive.*
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.common.BiomeDictionary.Type
import ru.vamig.worldengine.*

open class BiomeAlfheim @JvmOverloads constructor(r: Boolean = false): WE_Biome(WE_WorldProvider.we_id, r) {
	
	init {
		setBiomeName("Alfheim")
		
		BiomeDictionary.registerBiomeType(this, Type.MAGICAL)
		
		clearSpawn()
		setColor(0xA67C00)
		waterColorMultiplier = 0x1D1D4E
		
		createChunkGen_InXZ_List.clear()
		createChunkGen_InXYZ_List.clear()
		decorateChunkGen_List.clear()
		
		addEntry(EntityElf::class.java, AlfheimConfigHandler.pixieSpawn)
		addEntry(EntitySheep::class.java, AlfheimConfigHandler.sheepSpawn)
		addEntry(EntityPig::class.java, AlfheimConfigHandler.pigSpawn)
		addEntry(EntityChicken::class.java, AlfheimConfigHandler.chickSpawn)
		addEntry(EntityCow::class.java, AlfheimConfigHandler.cowSpawn)
	}
	
	fun addEntry(clazz: Class<*>, rate: IntArray) {
		val (w, i, x) = rate
		spawnableCreatureList.add(SpawnListEntry(clazz, w, i, x))
	}
	
	override fun getFloatTemperature(x: Int, y: Int, z: Int): Float {
		return if (AlfheimCore.winter) 0f else 0.5f
	}
}