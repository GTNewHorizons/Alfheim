package alfheim.common.world.dim.alfheim.biome;

import alfheim.common.entity.EntityElf;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import ru.vamig.worldengine.WE_Biome;
import ru.vamig.worldengine.standardcustomgen.WE_BiomeLayer;
import ru.vamig.worldengine.standardcustomgen.WE_SnowGen;
import vazkii.botania.common.block.ModBlocks;

public class BiomeAlfheim extends WE_Biome {
	
	public BiomeAlfheim(int ID_FOR_ALL_WE_BIOMES, boolean r) {
		super(ID_FOR_ALL_WE_BIOMES, r);
		clearSpawn();
		setBiomeName("Alfheim");
		setColor(0xA67C00);
		waterColorMultiplier = 0x1D1D4E;
		
		createChunkGen_InXZ_List.clear();
		createChunkGen_InXYZ_List.clear();
		decorateChunkGen_List.clear();
		
		spawnableCreatureList.add(new SpawnListEntry(EntityElf.class, 1, 2, 4));
	}
	
	public BiomeAlfheim(int ID_FOR_ALL_WE_BIOMES) {
		this(ID_FOR_ALL_WE_BIOMES, false);
	}
}