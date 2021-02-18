package alfheim.common.block.tile.sub.anomaly

import alexsocol.asjlib.*
import alfheim.api.block.tile.SubTileAnomalyBase
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.handler.CardinalSystem.KnowledgeSystem
import alfheim.common.core.handler.CardinalSystem.KnowledgeSystem.Knowledge
import alfheim.common.core.util.DamageSourceSpell
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.entity.player.*
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.common.Botania
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.core.helper.Vector3
import vazkii.botania.common.item.ModItems
import java.util.*
import kotlin.math.min
import alexsocol.asjlib.math.Vector3 as ASJVec3

class SubTileLightning: SubTileAnomalyBase() {
	
	internal val vt = Vector3()
	internal val ve = Vector3()
	
	override val targets: List<Any>
		get() {
			if (inWG()) return EMPTY_LIST
			
			run {
				if (ticks % 50 == 0) {
					val e = findNearestVulnerableEntity(radius) ?: return@run
					
					worldObj.playSoundEffect(x().D, y().D, z().D, "botania:runeAltarCraft", 1f, 1f)
					
					val l = ArrayList<Any>()
					l.add(e)
					return l
				}
			}
			return EMPTY_LIST
		}
	
	override val strip: Int
		get() = 1
	
	override val rarity: EnumAnomalityRarity
		get() = EnumAnomalityRarity.COMMON
	
	var transfer = 0
	var side = ForgeDirection.UNKNOWN
	
	public override fun update() {
		if (inWG()) return
		
		vt.set(x() + 0.5, y() + 0.5, z() + 0.5)
		
		if (transfer > 0) {
			--transfer
			
			if (transfer % 10 == 0) {
				
				val yOff = if (transfer % 20 == 0) 1.5 else 0.0
				
				vt.set(x() + 0.5, y() + 0.5, z() + 0.5)
				ve.set(x() + side.offsetX + 0.5, y() + side.offsetY + yOff, z() + side.offsetZ + 0.5).normalize()
				vt.add(ve.x / 2, ve.y / 2, ve.z / 2)
				ve.set(x() + side.offsetX + 0.5, y() + side.offsetY + yOff, z() + side.offsetZ + 0.5)
				
				Botania.proxy.lightningFX(worldObj, vt, ve, 1f, worldObj.rand.nextLong(), 0, 0xFF0000)
			}
			
			if (side === ForgeDirection.UNKNOWN || worldObj.getBlock(x() + side.offsetX, y() + side.offsetY, z() + side.offsetZ) !== ModBlocks.pylon || worldObj.getBlockMetadata(x() + side.offsetX, y() + side.offsetY, z() + side.offsetZ) != 2) {
				transfer = 0
				worldObj.newExplosion(null, x().D, y().D, z().D, 10f, false, true)
				return
			}
			
			if (transfer == 1) {
				worldObj.playSoundEffect(x().D, y().D, z().D, "botania:runeAltarCraft", 1f, 1f)
				
				for (i in 0..7) {
					ve.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize()
					vt.add(ve.x / 2.25, ve.y / 2.25, ve.z / 2.25)
					ve.multiply(1.5).add(x() + side.offsetX + 0.5, y() + side.offsetY + 0.5, z() + side.offsetZ + 0.5)
					Botania.proxy.lightningFX(worldObj, vt, ve, 50f, worldObj.rand.nextLong(), 0, 0xFF0000)
				}
				
				worldObj.setBlock(x() + side.offsetX, y() + side.offsetY, z() + side.offsetZ, AlfheimBlocks.alfheimPylon, 2, 3)
				worldObj.setBlockToAir(x(), y(), z())
			}
			
			return
		}
		
		if (worldObj.rand.nextInt(6000) == 0) {
			val x = x() + Math.random() * 10 - 5
			val z = z() + Math.random() * 10 - 5
			worldObj.addWeatherEffect(EntityLightningBolt(worldObj, x, worldObj.getTopSolidOrLiquidBlock(x.mfloor(), z.mfloor()).D, z))
			return
		}
		
		if (ASJUtilities.isClient) {
			if (superTile != null && ASJVec3.entityTileDistance(mc.thePlayer, superTile!!) > 32) return
			
			if (AlfheimConfigHandler.lightningsSpeed > 0 && ticks % AlfheimConfigHandler.lightningsSpeed == 0) {
				ve.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize()
				vt.add(ve.x / 2.25, ve.y / 2.25, ve.z / 2.25)
				ve.multiply(1.5).add(x() + 0.5, y() + 0.5, z() + 0.5)
				Botania.proxy.lightningFX(worldObj, vt, ve, 50f, worldObj.rand.nextLong(), 0, 0xFF0000)
			}
		}
	}
	
	override fun onActivated(stack: ItemStack?, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Boolean {
		stack ?: return false
		if (stack.item === ModItems.manaResource && stack.stackSize > 0 && stack.meta == 5) {
			--stack.stackSize
			
			for (d in ForgeDirection.VALID_DIRECTIONS) {
				if (world.getBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ) === ModBlocks.pylon && world.getBlockMetadata(x + d.offsetX, y + d.offsetY, z + d.offsetZ) == 2) {
					transfer = 200
					side = d
					break
				}
			}
			
			world.playSoundEffect(x.D, y.D, z.D, "botania:runeAltarStart", 1f, 1f)
			
			for (i in 0..7) {
				ve.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize()
				vt.add(ve.x / 2.25, ve.y / 2.25, ve.z / 2.25)
				ve.multiply(1.5).add(x() + 0.5, y() + 0.5, z() + 0.5)
				Botania.proxy.lightningFX(worldObj, vt, ve, 50f, worldObj.rand.nextLong(), 0, 0xFF0000)
			}
			
			if (player is EntityPlayerMP)
				KnowledgeSystem.learn(player, Knowledge.PYLONS)
		}
		return false
	}
	
	override fun performEffect(target: Any) {
		if (transfer > 0) return
		if (ticks % 25 != 0) return
		if (target !is EntityLivingBase) return
		if (target is EntityPlayer && target.capabilities.disableDamage) return
		
		target.attackEntityFrom(DamageSourceSpell.anomaly, min((Math.random() * 2 + 3) / vt.set(x() + 0.5, y() + 0.5, z() + 0.5).add(-target.posX, -target.posY, -target.posZ).mag() / 2.0, 2.5).F * 4f)
		
		vt.set(x() + 0.5, y() + 0.5, z() + 0.5)
		ve.set(target.posX, target.posY, target.posZ).normalize()
		vt.add(ve.x / 2, ve.y / 2, ve.z / 2)
		ve.set(target.posX, target.posY, target.posZ)
		
		Botania.proxy.lightningFX(worldObj, vt, ve, 1f, worldObj.rand.nextLong(), 0, 0xFF0000)
	}
	
	override fun typeBits() = HEALTH
	
	override fun writeCustomNBT(cmp: NBTTagCompound) {
		super.writeCustomNBT(cmp)
		
		cmp.setInteger(TAG_SIDE, side.ordinal)
		if (side != ForgeDirection.UNKNOWN)
			cmp.setInteger(TAG_TRANSFER, transfer)
	}
	
	override fun readCustomNBT(cmp: NBTTagCompound) {
		super.readCustomNBT(cmp)
		
		side = ForgeDirection.getOrientation(cmp.getInteger(TAG_SIDE))
		if (side != ForgeDirection.UNKNOWN)
			transfer = cmp.getInteger(TAG_TRANSFER)
	}
	
	companion object {
		
		const val radius = 12.0
		
		const val TAG_TRANSFER = "transfer"
		const val TAG_SIDE = "side"
	}
}