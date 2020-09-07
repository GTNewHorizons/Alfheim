package alfheim.common.block.colored

import alexsocol.asjlib.*
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.base.BlockMod
import alfheim.common.item.block.ItemBlockAurora
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.*
import net.minecraftforge.common.IPlantable
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.api.lexicon.ILexiconable
import java.util.*
import kotlin.math.roundToInt

class BlockAuroraDirt : BlockMod(Material.ground), IGrowable, ILexiconable {
	
	companion object {
		private data class Vec3i(var x: Int, var y: Int, var z: Int)
		
		private data class Vec3d(var x: Double, var y: Double, var z: Double) {
			operator fun plus(v: Vec3d): Vec3d {
				x += v.x
				y += v.y
				z += v.z
				return this
			}
			operator fun times(d: Double): Vec3d {
				x *= d
				y *= d
				z *= d
				return this
			}
			constructor(v: Vec3i): this(v.x.D, v.y.D, v.z.D)
		}
		
		private fun fromVec(vec: Vec3i): Int = (((vec.x + 256) % 256) shl 16) or (((vec.y + 256) % 256) shl 8) or ((vec.z + 256) % 256)
		
		private fun fromPos(x: Double, y: Double, z: Double) = fromVec(trilinearInterp(x, y, z))
		
		private fun fromPos(x: Int, y: Int, z: Int): Vec3i {
			var red = x * 32 + y * 16
			if (red and 256 != 0) {
				red = 255 - (red and 255)
			}
			red = red and 255
			
			var blue = y * 32 + z * 16
			if (blue and 256 != 0) {
				blue = 255 - (blue and 255)
			}
			blue = (blue xor 255) % 256
			
			var green = x * 16 + z * 32
			if (green and 256 != 0) {
				green = 255 - (green and 255)
			}
			green = green and 255
			
			return Vec3i(red, blue, green)
		}
		
		private fun trilinearInterp(x: Double, y: Double, z: Double): Vec3i {
			val x0 = (x - 0.5).roundToInt()
			val y0 = (y - 0.5).roundToInt()
			val z0 = (z - 0.5).roundToInt()
			val x1 = (x + 0.5).roundToInt()
			val y1 = (y + 0.5).roundToInt()
			val z1 = (z + 0.5).roundToInt()
			
			val xd = x1 - x
			val xl = x1 - x0
			val yd = y1 - y
			val yl = y1 - y0
			val zd = z1 - z
			val zl = z1 - z0
			
			val c000 = Vec3d(fromPos(x0, y0, z0))
			val c001 = Vec3d(fromPos(x0, y0, z1))
			val c010 = Vec3d(fromPos(x0, y1, z0))
			val c011 = Vec3d(fromPos(x0, y1, z1))
			val c100 = Vec3d(fromPos(x1, y0, z0))
			val c101 = Vec3d(fromPos(x1, y0, z1))
			val c110 = Vec3d(fromPos(x1, y1, z0))
			val c111 = Vec3d(fromPos(x1, y1, z1))
			
			val c00 = (c000 * xd) + (c100 * (xl - xd))
			val c01 = (c001 * xd) + (c101 * (xl - xd))
			val c10 = (c010 * xd) + (c110 * (xl - xd))
			val c11 = (c011 * xd) + (c111 * (xl - xd))
			
			val c0 = (c00 * yd) + (c10 * (yl - yd))
			val c1 = (c01 * yd) + (c11 * (yl - yd))
			
			val c = (c0 * zd) + (c1 * (zl - zd))
			
			return Vec3i(c.x.mfloor(), c.y.mfloor(), c.z.mfloor())
		}
		
		fun getBlockColor(x: Int, y: Int, z: Int) = fromVec(fromPos(x, y, z))
		
		fun getItemColor() = fromPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)
	}
	
	init {
		stepSound = soundTypeGravel
		blockHardness = 0.5f
		
		setBlockName("auroraDirt")
	}
	
//	override fun addInformation(stack: ItemStack, player: World?, tooltip: MutableList<String>, advanced: ITooltipFlag) {
//		addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.color.aurora")
//	}
	
	override fun shouldRegisterInNameSet() = false
	
	override fun setBlockName(name: String): Block {
		GameRegistry.registerBlock(this, ItemBlockAurora::class.java, name)
		return super.setBlockName(name)
	}
	
	override fun colorMultiplier(world: IBlockAccess, x: Int, y: Int, z: Int) = getBlockColor(x, y, z)
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = AlfheimLexiconData.aurora
	
	override fun isToolEffective(type: String?, metadata: Int) = (type != null && type == "shovel")
	
	override fun getHarvestTool(metadata: Int) = "shovel"
	
	override fun canSustainPlant(world: IBlockAccess?, x: Int, y: Int, z: Int, direction: ForgeDirection?, plantable: IPlantable?) = true
	
	override fun func_149851_a(world: World, x: Int, y: Int, z: Int, remote: Boolean) = true
	
	override fun func_149852_a(world: World, random: Random, x: Int, y: Int, z: Int) = true
	
	override fun func_149853_b(world: World, random: Random, x: Int, y: Int, z: Int) {
		var l = 0
		
		while (l < 128) {
			var i1 = x
			var j1 = y + 1
			var k1 = z
			var l1 = 0
			
			while (true) {
				if (l1 < l / 16) {
					i1 += random.nextInt(3) - 1
					j1 += (random.nextInt(3) - 1) * random.nextInt(3) / 2
					k1 += random.nextInt(3) - 1
					
					if ((world.getBlock(i1, j1 - 1, k1) == this || world.getBlock(i1, j1 - 1, k1) == AlfheimBlocks.irisDirt || world.getBlock(i1, j1 - 1, k1) == AlfheimBlocks.rainbowDirt) && !world.getBlock(i1, j1, k1).isNormalCube) {
						++l1
						continue
					}
				} else if (world.getBlock(i1, j1, k1).isAir(world, i1, j1, k1)) {
					if (random.nextInt(8) != 0) {
						if (AlfheimBlocks.rainbowGrass.canBlockStay(world, i1, j1, k1)) {
							world.setBlock(i1, j1, k1, AlfheimBlocks.rainbowGrass, 1, 3)
						}
					} else {
						world.getBiomeGenForCoords(i1, k1).plantFlower(world, random, i1, j1, k1)
					}
				}
				
				++l
				break
			}
		}
	}
}