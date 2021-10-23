package alexsocol.asjlib.extendables.block

import net.minecraft.nbt.NBTTagCompound

open class TileImmobile: ASJTile() {
	
	private var lock = Lock(0, -1, 0, 0)
	
	override fun updateEntity() {
		if (lock != Lock(xCoord, yCoord, zCoord, worldObj.provider.dimensionId)) worldObj.setBlockToAir(xCoord, yCoord, zCoord)
	}
	
	override fun writeCustomNBT(nbt: NBTTagCompound) {
		super.writeCustomNBT(nbt)
		
		lock.save(nbt)
	}
	
	override fun readCustomNBT(nbt: NBTTagCompound) {
		super.readCustomNBT(nbt)
		
		lock = Lock.load(nbt)
	}
	
	fun lock(x: Int, y: Int, z: Int, d: Int) {
		lock = Lock(x, y, z, d)
	}
	
	private data class Lock(val x: Int, val y: Int, val z: Int, val d: Int, val h: Int = "$x$y$z$d".hashCode()) {
		
		fun save(nbt: NBTTagCompound) {
			nbt.setInteger(TAG_B, h)
			nbt.setInteger(TAG_A, d)
			nbt.setInteger(TAG_C, x)
			nbt.setInteger(TAG_D, y)
			nbt.setInteger(TAG_E, z)
		}
		
		companion object {
			
			fun load(nbt: NBTTagCompound) = Lock(nbt.getInteger(TAG_C), nbt.getInteger(TAG_D), nbt.getInteger(TAG_E), nbt.getInteger(TAG_A), nbt.getInteger(TAG_B))
		}
	}
	
	companion object {
		
		private const val TAG_A = "a"
		private const val TAG_B = "b"
		private const val TAG_C = "c"
		private const val TAG_D = "d"
		private const val TAG_E = "e"
	}
}