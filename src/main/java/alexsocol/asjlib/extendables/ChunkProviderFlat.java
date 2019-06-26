package alexsocol.asjlib.extendables;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.*;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.*;

import java.util.*;

public class ChunkProviderFlat implements IChunkProvider {
	private final World worldObj;
	private final Random random;
	private final Block[] cachedBlockIDs = new Block[256];
	private final byte[] cachedBlockMetadata = new byte[256];
	
	public ChunkProviderFlat(World world, long seed, String genString) {
		this.worldObj = world;
		this.random = new Random(seed);
		FlatGeneratorInfo flatWorldGenInfo = FlatGeneratorInfo.createFlatGeneratorFromString(genString);
		
		
		for (Object o : flatWorldGenInfo.getFlatLayers()) {
			FlatLayerInfo flatlayerinfo = (FlatLayerInfo) o;
			for (int j = flatlayerinfo.getMinY(); j < flatlayerinfo.getMinY() + flatlayerinfo.getLayerCount(); ++j) {
				this.cachedBlockIDs[j] = flatlayerinfo.func_151536_b();
				this.cachedBlockMetadata[j] = (byte)flatlayerinfo.getFillBlockMeta();
			}
		}
	}
	
	@Override
	public Chunk loadChunk(int x, int z) {
		return this.provideChunk(x, z);
	}
	
	@Override
	public Chunk provideChunk(int x, int z) {
		Chunk chunk = new Chunk(this.worldObj, x, z);
		int l;
		
		for (int k = 0; k < this.cachedBlockIDs.length; ++k) {
			Block block = this.cachedBlockIDs[k];
			
			if (block != null) {
				l = k >> 4;
				ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[l];
				
				if (extendedblockstorage == null) {
					extendedblockstorage = new ExtendedBlockStorage(k, !this.worldObj.provider.hasNoSky);
					chunk.getBlockStorageArray()[l] = extendedblockstorage;
				}
				
				for (int i1 = 0; i1 < 16; ++i1) {
					for (int j1 = 0; j1 < 16; ++j1) {
						extendedblockstorage.func_150818_a(i1, k & 15, j1, block);
						extendedblockstorage.setExtBlockMetadata(i1, k & 15, j1, this.cachedBlockMetadata[k]);
					}
				}
			}
		}
		
		chunk.generateSkylightMap();
		BiomeGenBase[] abiomegenbase = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(null, x * 16, z * 16, 16, 16);
		byte[] abyte = chunk.getBiomeArray();
		
		for (l = 0; l < abyte.length; ++l) {
			abyte[l] = (byte)abiomegenbase[l].biomeID;
		}
		
		chunk.generateSkylightMap();
		return chunk;
	}
	
	@Override
	public boolean chunkExists(int x, int z) {
		return true;
	}
	
	@Override
	public void populate(IChunkProvider p_73153_1_, int x, int z) {
		int k = x * 16;
		int l = z * 16;
		BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(k + 16, l + 16);
		boolean flag = false;
		this.random.setSeed(this.worldObj.getSeed());
		long i1 = this.random.nextLong() / 2L * 2L + 1L;
		long j1 = this.random.nextLong() / 2L * 2L + 1L;
		this.random.setSeed((long)x * i1 + (long)z * j1 ^ this.worldObj.getSeed());
	}
	
	@Override
	public boolean saveChunks(boolean b, IProgressUpdate progress) {
		return true;
	}
	
	@Override
	public void saveExtraData() {}
	
	@Override
	public boolean unloadQueuedChunks() {
		return false;
	}
	
	@Override
	public boolean canSave() {
		return true;
	}
	
	@Override
	public String makeString() {
		return "ASJFlatChunkProvider";
	}
	
	@Override
	public List getPossibleCreatures(EnumCreatureType creature, int i, int x, int z) {
		return this.worldObj.getBiomeGenForCoords(x, z).getSpawnableList(creature);
	}
	
	@Override
	public ChunkPosition func_147416_a(World world, String structureName, int i, int x, int z) {
		return null;
	}
	
	@Override
	public int getLoadedChunkCount() {
		return 0;
	}
	
	@Override
	public void recreateStructures(int p_82695_1_, int p_82695_2_) { }
}