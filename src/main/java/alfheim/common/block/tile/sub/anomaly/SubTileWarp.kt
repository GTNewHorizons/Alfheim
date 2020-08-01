package alfheim.common.block.tile.sub.anomaly

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.api.block.tile.SubTileAnomalyBase
import alfheim.common.item.equipment.bauble.ItemSpatiotemporalRing
import net.minecraft.block.Block
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import vazkii.botania.common.Botania
import java.util.*
import kotlin.math.*

class SubTileWarp: SubTileAnomalyBase() {
	var radius = 20
	
	override val targets: List<Any>
		get() {
			if (ASJUtilities.isClient) return EMPTY_LIST
			
			var l: MutableList<Any>? = null
			
			if (ticks % 100 == 0 && !inWG()) {
				l = allAroundRaw(EntityLivingBase::class.java, radius.D)
				l.removeAll { it is EntityPlayer && it.capabilities.isCreativeMode }
				if (l.size > 0) {
					if (l.size == 1) {
						l.add(LivingCoords(l.removeAt(0) as EntityLivingBase, x().D, y().D, z().D, radius))
					} else {
						l.add(LivingPair(rand(l), rand(l)))
					}
					return l
				}
			} else if (ticks % 25 == 1) {
				var b1: Block
				var b2: Block
				val v = Vector8i()
				var tries = 50
				
				while (tries-- > 0) {
					v[x(), y(), z(), radius] = worldObj.rand
					
					b1 = worldObj.getBlock(v.x1, v.y1, v.z1)
					b2 = worldObj.getBlock(v.x2, v.y2, v.z2)
					
					if (worldObj.isAirBlock(v.x1, v.y1, v.z1) && worldObj.isAirBlock(v.x2, v.y2, v.z2) ||
						b1.getBlockHardness(worldObj, v.x1, v.y1, v.z1) < 0 ||
						b2.getBlockHardness(worldObj, v.x2, v.y2, v.z2) < 0 ||
						worldObj.getTileEntity(v.x1, v.y1, v.z1) != null ||
						worldObj.getTileEntity(v.x2, v.y2, v.z2) != null)
						continue
					
					v.m1 = worldObj.getBlockMetadata(v.x1, v.y1, v.z1)
					v.m2 = worldObj.getBlockMetadata(v.x2, v.y2, v.z2)
					l = ArrayList()
					l.add(v)
					break
				}
			}
			
			if (l == null) l = EMPTY_LIST
			return l
		}
	
	override val strip: Int
		get() = 6
	
	override val rarity: EnumAnomalityRarity
		get() = EnumAnomalityRarity.RARE
	
	public override fun update() {
		if (inWG()) return
		
		if (ASJUtilities.isServer && ticks % 600 == 0) {
			radius = worldObj.rand.nextInt(8) + 16
			ASJUtilities.dispatchTEToNearbyPlayers(superTile!!)
		}
		
		rand.setSeed((x() xor y() xor z()).toLong())
		val worldTime = (worldObj.totalWorldTime + rand.nextInt(1000)) / 5.0
		val r = 0.75f + Math.random().F * 0.05f
		
		val x = x().D + 0.5 + sin(worldTime) * r
		val y = y().D + 0.5 + sin(worldTime) * r
		val z = z().D + 0.5 + sin(worldTime) * r
		val i = x().D + 0.5 + cos(worldTime) * r
		val j = y().D + 0.5 + cos(worldTime) * r
		val k = z().D + 0.5 + cos(worldTime) * r
		
		val m = x().D + 0.5 + sin(-worldTime) * r
		val n = y().D + 0.5 + sin(-worldTime) * r
		val o = z().D + 0.5 + sin(-worldTime) * r
		
		Botania.proxy.wispFX(worldObj, x() + 0.5, j, k,
							 0.25f + Math.random().F * 0.25f, Math.random().F * 0.25f, 0.75f + Math.random().F * 0.25f,
							 0.1f + Math.random().F * 0.1f)
		
		Botania.proxy.wispFX(worldObj, x() + 0.5, n, z,
							 0.25f + Math.random().F * 0.25f, Math.random().F * 0.25f, 0.75f + Math.random().F * 0.25f,
							 0.1f + Math.random().F * 0.1f)
		
		Botania.proxy.wispFX(worldObj, i, y() + 0.5, k,
							 0.25f + Math.random().F * 0.25f, Math.random().F * 0.25f, 0.75f + Math.random().F * 0.25f,
							 0.1f + Math.random().F * 0.1f)
		
		Botania.proxy.wispFX(worldObj, x, y() + 0.5, o,
							 0.25f + Math.random().F * 0.25f, Math.random().F * 0.25f, 0.75f + Math.random().F * 0.25f,
							 0.1f + Math.random().F * 0.1f)
		
		Botania.proxy.wispFX(worldObj, i, j, z() + 0.5,
							 0.25f + Math.random().F * 0.25f, Math.random().F * 0.25f, 0.75f + Math.random().F * 0.25f,
							 0.1f + Math.random().F * 0.1f)
		
		Botania.proxy.wispFX(worldObj, m, y, z() + 0.5,
							 0.25f + Math.random().F * 0.25f, Math.random().F * 0.25f, 0.75f + Math.random().F * 0.25f,
							 0.1f + Math.random().F * 0.1f)
		
		Botania.proxy.wispFX(worldObj, x() + 0.5, y() + 0.5, k,
							 0.25f + Math.random().F * 0.25f, Math.random().F * 0.25f, 0.75f + Math.random().F * 0.25f,
							 0.1f + Math.random().F * 0.1f)
		
		Botania.proxy.wispFX(worldObj, x() + 0.5, y, z() + 0.5,
							 0.25f + Math.random().F * 0.25f, Math.random().F * 0.25f, 0.75f + Math.random().F * 0.25f,
							 0.1f + Math.random().F * 0.1f)
		
		Botania.proxy.wispFX(worldObj, i, y() + 0.5, z() + 0.5,
							 0.25f + Math.random().F * 0.25f, Math.random().F * 0.25f, 0.75f + Math.random().F * 0.25f,
							 0.1f + Math.random().F * 0.1f)
	}
	
	override fun writeCustomNBT(cmp: NBTTagCompound) {
		super.writeCustomNBT(cmp)
		cmp.setInteger(TAG_RADIUS, radius)
	}
	
	override fun readCustomNBT(cmp: NBTTagCompound) {
		super.readCustomNBT(cmp)
		radius = cmp.getInteger(TAG_RADIUS)
	}
	
	private fun rand(l: MutableList<Any>) = l.removeAt(worldObj.rand.nextInt(l.size)) as EntityLivingBase
	
	override fun performEffect(target: Any) {
		if (ASJUtilities.isClient) return
		
		if (!inWG()) {
			if (target is LivingPair) {
				if (ItemSpatiotemporalRing.hasProtection(target.e1) || ItemSpatiotemporalRing.hasProtection(target.e2)) return
				
				val x = target.e1.posX
				val y = target.e1.posY
				val z = target.e1.posZ
				
				target.e1.setPositionAndUpdate(target.e2.posX, target.e2.posY, target.e2.posZ)
				target.e2.setPositionAndUpdate(x, y, z)
				return
			} else if (target is LivingCoords) {
				if (ItemSpatiotemporalRing.hasProtection(target.e)) return
				
				target.e.setPositionAndUpdate(target.x, target.y, target.z)
				return
			}
		}
		
		if (target is Vector8i) {
			val block = worldObj.getBlock(target.x1, target.y1, target.z1)
			worldObj.setBlock(target.x1, target.y1, target.z1, worldObj.getBlock(target.x2, target.y2, target.z2), target.m2, 3)
			worldObj.setBlock(target.x2, target.y2, target.z2, block, target.m1, 3)
		}
	}
	
	override fun typeBits() = SPACE
	
	private class Vector8i {
		var x1: Int = 0
		var y1: Int = 0
		var z1: Int = 0
		var m1: Int = 0
		var x2: Int = 0
		var y2: Int = 0
		var z2: Int = 0
		var m2: Int = 0
		
		operator fun set(x: Int, y: Int, z: Int, radius: Int, rand: Random) {
			x1 = rand.nextInt(radius * 2) - radius
			y1 = rand.nextInt(radius * 2) - radius
			z1 = rand.nextInt(radius * 2) - radius
			
			x2 = rand.nextInt(radius * 2) - radius
			y2 = rand.nextInt(radius * 2) - radius
			z2 = rand.nextInt(radius * 2) - radius
			
			v.set(x1.D, y1.D, z1.D)
			if (v.length() > radius) v.shrink(v.length() - radius)
			x1 = v.x.mfloor()
			y1 = v.y.mfloor()
			z1 = v.z.mfloor()
			
			v.set(x2.D, y2.D, z2.D)
			if (v.length() > radius) v.shrink(v.length() - radius)
			x2 = v.x.mfloor()
			y2 = v.y.mfloor()
			z2 = v.z.mfloor()
			
			x1 = max(-30000000, min(x + x1, 30000000))
			y1 = max(0, min(y + y1, 255))
			z1 = max(-30000000, min(z + z1, 30000000))
			
			x2 = max(-30000000, min(x + x2, 30000000))
			y2 = max(0, min(y + y2, 255))
			z2 = max(-30000000, min(z + z2, 30000000))
		}
		
		companion object {
			
			val v = Vector3()
		}
	}
	
	private class LivingCoords(val e: EntityLivingBase, posX: Double, posY: Double, posZ: Double, radius: Int) {
		val x: Double = max(-30000000.0, min(posX + Math.random() * radius.D * 2.0 - radius, 30000000.0))
		val y: Double = max(1.0, min(posY + Math.random() * radius.D * 2.0 - radius, 255.0))
		val z: Double = max(-30000000.0, min(posZ + Math.random() * radius.D * 2.0 - radius, 30000000.0))
	}
	
	private class LivingPair(val e1: EntityLivingBase, val e2: EntityLivingBase)
	
	companion object {
		// public static final List<String> validBlocks = Arrays.asList(new String[] { "stone", "dirt", "grass", "sand", "gravel", "hardenedClay", "snowLayer", "mycelium", "podzol", "sandstone", /* Mod support: */ "blockDiorite", "stoneDiorite", "blockGranite", "stoneGranite", "blockAndesite", "stoneAndesite", "marble", "blockMarble", "limestone", "blockLimestone" });
		// maybe will change warp's behavior to swap only blocks from list above ^
		const val TAG_RADIUS = "radius"
	}
}
