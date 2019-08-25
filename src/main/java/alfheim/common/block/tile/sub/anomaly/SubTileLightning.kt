package alfheim.common.block.tile.sub.anomaly

import alfheim.api.block.tile.SubTileEntity
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.handler.CardinalSystem.KnowledgeSystem
import alfheim.common.core.handler.CardinalSystem.KnowledgeSystem.Knowledge
import alfheim.common.core.util.DamageSourceSpell
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.entity.player.*
import net.minecraft.item.ItemStack
import net.minecraft.util.MathHelper
import net.minecraft.world.World
import vazkii.botania.common.Botania
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.core.helper.Vector3
import vazkii.botania.common.item.ModItems
import java.util.*
import kotlin.math.min

class SubTileLightning: SubTileEntity() {
	internal val vt = Vector3()
	internal val ve = Vector3()
	
	override val targets: List<Any>
		get() {
			if (inWG()) return EMPTY_LIST
			
			run {
				if (ticks % 50 == 0) {
					val e = findNearestVulnerableEntity(radius) ?: return@run
					
					worldObj.playSoundEffect(x().toDouble(), y().toDouble(), z().toDouble(), "botania:runeAltarCraft", 1f, 1f)
					
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
	
	public override fun update() {
		if (inWG()) return
		
		vt.set(x() + 0.5, y() + 0.5, z() + 0.5)
		
		if (worldObj.rand.nextInt(6000) == 0) {
			val x = x() + Math.random() * 10 - 5
			val z = z() + Math.random() * 10 - 5
			worldObj.addWeatherEffect(EntityLightningBolt(worldObj, x, worldObj.getTopSolidOrLiquidBlock(MathHelper.floor_double(x), MathHelper.floor_double(z)).toDouble(), z))
			return
		}
		
		if (AlfheimConfigHandler.lightningsSpeed > 0 && ticks % AlfheimConfigHandler.lightningsSpeed == 0) {
			ve.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize()
			vt.add(ve.x / 2.25, ve.y / 2.25, ve.z / 2.25)
			ve.multiply(1.5).add(x() + 0.5, y() + 0.5, z() + 0.5)
			Botania.proxy.lightningFX(worldObj, vt, ve, 50f, worldObj.rand.nextLong(), 0, 0xFF0000)
		}
	}
	
	override fun onActivated(stack: ItemStack?, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Boolean {
		if (player is EntityPlayerMP && stack != null && stack.item === ModItems.manaResource && stack.stackSize > 0 && stack.itemDamage == 5) {
			--stack.stackSize
			
			if (world.getBlock(x, y + 1, z) === ModBlocks.pylon && world.getBlockMetadata(x, y + 1, z) == 2)
				world.setBlock(x, y + 1, z, AlfheimBlocks.alfheimPylon, 2, 3)
			else if (world.getBlock(x, y - 1, z) === ModBlocks.pylon && world.getBlockMetadata(x, y - 1, z) == 2)
				world.setBlock(x, y - 1, z, AlfheimBlocks.alfheimPylon, 2, 3)
			else if (world.getBlock(x + 1, y, z) === ModBlocks.pylon && world.getBlockMetadata(x + 1, y, z) == 2)
				world.setBlock(x + 1, y, z, AlfheimBlocks.alfheimPylon, 2, 3)
			else if (world.getBlock(x - 1, y, z) === ModBlocks.pylon && world.getBlockMetadata(x - 1, y, z) == 2)
				world.setBlock(x - 1, y, z, AlfheimBlocks.alfheimPylon, 2, 3)
			else if (world.getBlock(x, y, z + 1) === ModBlocks.pylon && world.getBlockMetadata(x, y, z + 1) == 2)
				world.setBlock(x, y, z + 1, AlfheimBlocks.alfheimPylon, 2, 3)
			else if (world.getBlock(x, y, z - 1) === ModBlocks.pylon && world.getBlockMetadata(x, y, z - 1) == 2)
				world.setBlock(x, y, z - 1, AlfheimBlocks.alfheimPylon, 2, 3)
			
			world.playSoundEffect(x.toDouble(), y.toDouble(), z.toDouble(), "botania:runeAltarStart", 1f, 1f)
			
			for (i in 0..7) {
				ve.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize()
				vt.add(ve.x / 2.25, ve.y / 2.25, ve.z / 2.25)
				ve.multiply(1.5).add(x() + 0.5, y() + 0.5, z() + 0.5)
				Botania.proxy.lightningFX(worldObj, vt, ve, 50f, worldObj.rand.nextLong(), 0, 0xFF0000)
			}
			
			KnowledgeSystem.learn(player, Knowledge.PYLONS)
		}
		return false
	}
	
	override fun performEffect(target: Any) {
		if (ticks % 25 != 0) return
		if (target !is EntityLivingBase) return
		if (target is EntityPlayer && target.capabilities.disableDamage) return
		
		target.attackEntityFrom(DamageSourceSpell.corruption, min((Math.random() * 2 + 3) / vt.set(x() + 0.5, y() + 0.5, z() + 0.5).add(-target.posX, -target.posY, -target.posZ).mag() / 2.0, 2.5).toFloat() * 4f)
		
		vt.set(x() + 0.5, y() + 0.5, z() + 0.5)
		ve.set(target.posX, target.posY, target.posZ).normalize()
		vt.add(ve.x / 2, ve.y / 2, ve.z / 2)
		ve.set(target.posX, target.posY, target.posZ)
		
		Botania.proxy.lightningFX(worldObj, vt, ve, 1f, worldObj.rand.nextLong(), 0, 0xFF0000)
	}
	
	override fun typeBits() = HEALTH
	
	companion object {
		const val radius = 12.0
	}
}