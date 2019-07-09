package alexsocol.asjlib.extendables

import net.minecraft.block.Block
import net.minecraft.entity.EnumCreatureType
import net.minecraft.util.IProgressUpdate
import net.minecraft.world.*
import net.minecraft.world.chunk.*
import net.minecraft.world.chunk.storage.ExtendedBlockStorage
import net.minecraft.world.gen.*
import java.util.*

class ChunkProviderFlat(private val worldObj: World, seed: Long, genString: String): IChunkProvider {
	private val random = Random(seed)
	private val cachedBlockIDs = arrayOfNulls<Block>(256)
	private val cachedBlockMetadata = ByteArray(256)
	
	init {
		val flatWorldGenInfo = FlatGeneratorInfo.createFlatGeneratorFromString(genString)
		
		for (o in flatWorldGenInfo.flatLayers) {
			val flatlayerinfo = o as FlatLayerInfo
			for (j in flatlayerinfo.minY until flatlayerinfo.minY + flatlayerinfo.layerCount) {
				this.cachedBlockIDs[j] = flatlayerinfo.func_151536_b()
				this.cachedBlockMetadata[j] = flatlayerinfo.fillBlockMeta.toByte()
			}
		}
	}
	
	override fun loadChunk(x: Int, z: Int): Chunk {
		return this.provideChunk(x, z)
	}
	
	override fun provideChunk(x: Int, z: Int): Chunk {
		val chunk = Chunk(this.worldObj, x, z)
		var l: Int
		
		for (k in this.cachedBlockIDs.indices) {
			val block = this.cachedBlockIDs[k]
			
			if (block != null) {
				l = k shr 4
				var extendedblockstorage: ExtendedBlockStorage? = chunk.blockStorageArray[l]
				
				if (extendedblockstorage == null) {
					extendedblockstorage = ExtendedBlockStorage(k, !this.worldObj.provider.hasNoSky)
					chunk.blockStorageArray[l] = extendedblockstorage
				}
				
				for (i1 in 0..15) {
					for (j1 in 0..15) {
						extendedblockstorage.func_150818_a(i1, k and 15, j1, block)
						extendedblockstorage.setExtBlockMetadata(i1, k and 15, j1, this.cachedBlockMetadata[k].toInt())
					}
				}
			}
		}
		
		chunk.generateSkylightMap()
		val abiomegenbase = this.worldObj.worldChunkManager.loadBlockGeneratorData(null, x * 16, z * 16, 16, 16)
		val abyte = chunk.biomeArray
		
		l = 0
		while (l < abyte.size) {
			abyte[l] = abiomegenbase[l].biomeID.toByte()
			++l
		}
		
		chunk.generateSkylightMap()
		return chunk
	}
	
	override fun chunkExists(x: Int, z: Int): Boolean {
		return true
	}
	
	override fun populate(p_73153_1_: IChunkProvider, x: Int, z: Int) {
		this.random.setSeed(this.worldObj.seed)
		val i1 = this.random.nextLong() / 2L * 2L + 1L
		val j1 = this.random.nextLong() / 2L * 2L + 1L
		this.random.setSeed(x.toLong() * i1 + z.toLong() * j1 xor this.worldObj.seed)
	}
	
	override fun saveChunks(b: Boolean, progress: IProgressUpdate): Boolean {
		return true
	}
	
	override fun saveExtraData() {}
	
	override fun unloadQueuedChunks(): Boolean {
		return false
	}
	
	override fun canSave(): Boolean {
		return true
	}
	
	override fun makeString(): String {
		return "ASJFlatChunkProvider"
	}
	
	override fun getPossibleCreatures(creature: EnumCreatureType, i: Int, x: Int, z: Int): List<*> {
		return this.worldObj.getBiomeGenForCoords(x, z).getSpawnableList(creature)
	}
	
	override fun func_147416_a(world: World, structureName: String, i: Int, x: Int, z: Int): ChunkPosition? {
		return null
	}
	
	override fun getLoadedChunkCount(): Int {
		return 0
	}
	
	override fun recreateStructures(p_82695_1_: Int, p_82695_2_: Int) {}
}