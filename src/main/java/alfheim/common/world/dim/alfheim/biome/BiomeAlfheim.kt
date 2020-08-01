package alfheim.common.world.dim.alfheim.biome

import alfheim.AlfheimCore
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.entity.EntityElf
import alfheim.common.world.dim.alfheim.structure.StructureDreamsTree
import net.minecraft.entity.EnumCreatureType
import net.minecraft.entity.passive.*
import net.minecraft.init.Blocks
import net.minecraft.world.biome.BiomeGenBase
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.common.BiomeDictionary.Type
import ru.vamig.worldengine.*

open class BiomeAlfheim @JvmOverloads constructor(r: Boolean = false): WE_Biome(WE_WorldProvider.we_id, r) {
	
	init {
		setBiomeName("Alfheim")
		
		BiomeDictionary.registerBiomeType(this, Type.MAGICAL)
		
		clearSpawn()
		setColor(grassColor)
		waterColorMultiplier = if (AlfheimCore.winter) 0x1D1D4E else 0x00FFFF
		temperature = if (AlfheimCore.winter) 0f else 0.5f
		
		createChunkGen_InXZ_List.clear()
		createChunkGen_InXYZ_List.clear()
		decorateChunkGen_List.clear()
		
		addEntry(EntityElf::class.java, AlfheimConfigHandler.elvesSpawn)
		addEntry(EntitySheep::class.java, AlfheimConfigHandler.sheepSpawn)
		addEntry(EntityPig::class.java, AlfheimConfigHandler.pigSpawn)
		addEntry(EntityChicken::class.java, AlfheimConfigHandler.chickSpawn)
		addEntry(EntityCow::class.java, AlfheimConfigHandler.cowSpawn)
	}
	
	override fun getSkyColorByTemp(temp: Float) = if (AlfheimCore.winter) 0x576cd9 else 0x266eff
	
	companion object {
		const val offset = -7
		
		val dreamTree = StructureDreamsTree(AlfheimBlocks.altWood1, AlfheimBlocks.altLeaves, 3, 7, 11, 15)
		val sadOak = StructureDreamsTree(Blocks.log, Blocks.leaves, 0, 4, 8, 4)
		
		fun BiomeGenBase.addEntry(clazz: Class<*>, rate: IntArray) {
			val (w, i, x) = rate
			this.getSpawnableList(EnumCreatureType.creature).add(SpawnListEntry(clazz, w, i, x))
		}
	}
}