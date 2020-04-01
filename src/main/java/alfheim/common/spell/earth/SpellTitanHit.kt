package alfheim.common.spell.earth

import alexsocol.asjlib.*
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.security.InteractionSecurity
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.*
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.common.Botania
import vazkii.botania.common.core.handler.ConfigHandler
import vazkii.botania.common.item.equipment.tool.ToolCommons

object SpellTitanHit: SpellBase("titanhit", EnumRace.GNOME, 1, 1, 1) {
	
	/** temp value for counting total on block breaking  */
	var tcd = 0
	var tmana = 0
	
	override var radius = 1.0
	
	override val usableParams: Array<Any>
		get() = arrayOf(radius)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		if (caster !is EntityPlayer) return SpellCastResult.WRONGTGT
		
		val dist = (caster as? EntityPlayerMP)?.theItemInWorldManager?.blockReachDistance ?: 5.0
		val mop = ASJUtilities.getSelectedBlock(caster, dist, false)
		if (mop == null || mop.typeOfHit != MovingObjectType.BLOCK || mop.sideHit == -1) return SpellCastResult.WRONGTGT
		
		tmana = removeBlocksInIteration(caster.worldObj, caster, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, false, false)
		
		val result: SpellCastResult = checkCast(caster)
		
		if (result != SpellCastResult.OK) return result
		
		removeBlocksInIteration(caster.worldObj, caster, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, true, false)
		
		return result
	}
	
	fun removeBlocksInIteration(world: World, player: EntityPlayer, x: Int, y: Int, z: Int, side: Int, remove: Boolean, draw: Boolean): Int {
		val direction = ForgeDirection.getOrientation(side)
		val xs = if (direction.offsetX == 0) -radius.I else 0
		val ys = if (direction.offsetY == 0) -radius.I else 0
		val zs = if (direction.offsetZ == 0) -radius.I else 0
		val xe = if (direction.offsetX == 0) radius.I + 1 else 1
		val ye = if (direction.offsetY == 0) radius.I + 1 else 1
		val ze = if (direction.offsetZ == 0) radius.I + 1 else 1
		var mana = 0
		if (player.isSneaking)
			mana = removeBlockWithDrops(world, player, x, y, z, remove, draw, MATERIALS)
		else
			for (x1 in xs until xe)
				for (y1 in ys until ye)
					for (z1 in zs until ze)
						mana += removeBlockWithDrops(world, player, x1 + x, y1 + y, z1 + z, remove, draw, MATERIALS)
		return mana
	}
	
	fun removeBlockWithDrops(world: World, player: EntityPlayer, x: Int, y: Int, z: Int, remove: Boolean, draw: Boolean, materialsListing: Array<Material>): Int {
		if (!world.blockExists(x, y, z)) return 0
		
		if (!InteractionSecurity.canDoSomethingHere(player, x, y, z)) return 0
		
		val block = world.getBlock(x, y, z)
		val meta = world.getBlockMetadata(x, y, z)
		var mana = 0
		
		val mat = world.getBlock(x, y, z).material
		if ((!world.isRemote || !remove && draw) && block != null && !block.isAir(world, x, y, z) && (block.getPlayerRelativeBlockHardness(player, world, x, y, z) > 0 || player.capabilities.isCreativeMode)) {
			if (!player.capabilities.isCreativeMode && (!block.canHarvestBlock(player, meta) || !ToolCommons.isRightMaterial(mat, materialsListing))) return 0
			
			var flag = false
			
			if (!player.capabilities.isCreativeMode) {
				val localMeta = world.getBlockMetadata(x, y, z)
				if (remove) block.onBlockHarvested(world, x, y, z, localMeta, player)
				if (remove) flag = block.removedByPlayer(world, player, x, y, z, true)
				if (remove && flag) {
					block.onBlockDestroyedByPlayer(world, x, y, z, localMeta)
					
					block.harvestBlock(world, player, x, y, z, localMeta)
					
					mana += (block.getBlockHardness(world, x, y, z) * 10).I
					tcd += 2
				}
				if (!remove) {
					flag = true
					mana += (block.getBlockHardness(world, x, y, z) * 10).I
				}
			} else {
				if (remove)
					world.setBlockToAir(x, y, z)
				else {
					flag = x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000 && y >= 0 && y < 256
				}
			}
			
			if (draw && flag) {
				Botania.proxy.setWispFXDepthTest(false)
				Botania.proxy.wispFX(player.worldObj, x + 0.5, y + 0.5, z + 0.5, 1f, 0f, 0f, 0.2f, 0f, 0.075f)
				Botania.proxy.setWispFXDepthTest(true)
			}
			
			if (!world.isRemote && remove && ConfigHandler.blockBreakParticles && ConfigHandler.blockBreakParticlesTool) world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (meta shl 12))
		}
		
		return mana
	}
	
	override fun getManaCost(): Int {
		try {
			return tmana * mana
		} finally {
			tmana = 0
		}
	}
	
	override fun getCooldown(): Int {
		try {
			return tcd * cldn
		} finally {
			tcd = 0
		}
	}
	
	override fun render(caster: EntityLivingBase) {
		val dist = (caster as? EntityPlayerMP)?.theItemInWorldManager?.blockReachDistance ?: 5.0
		val mop = ASJUtilities.getSelectedBlock(caster, dist, false)
		if (mop == null || mop.typeOfHit != MovingObjectType.BLOCK || mop.sideHit == -1) return
		
		removeBlocksInIteration(caster.worldObj, caster as EntityPlayer, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, false, true)
	}
	
	val MATERIALS = arrayOf(Material.rock, Material.iron, Material.ice, Material.glass, Material.piston, Material.anvil, Material.grass, Material.ground, Material.sand, Material.snow, Material.craftedSnow, Material.clay)
}