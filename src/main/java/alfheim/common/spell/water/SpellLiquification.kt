package alfheim.common.spell.water

import alexsocol.asjlib.*
import alfheim.api.entity.*
import alfheim.api.event.SpellCastEvent
import alfheim.api.spell.SpellBase
import alfheim.common.core.handler.AlfheimConfigHandler
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import net.minecraftforge.common.MinecraftForge
import vazkii.botania.common.block.tile.TileAltar

object SpellLiquification: SpellBase("liquification", EnumRace.UNDINE, 2000, 100, 5) {
	
	override val usableParams: Array<Any>
		get() = arrayOf(radius)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		if (caster !is EntityPlayer) return SpellCastResult.NOTALLOW
		val mop = ASJUtilities.getSelectedBlock(caster, radius, false)
		if (mop == null || mop.typeOfHit != MovingObjectType.BLOCK || mop.sideHit == -1) return SpellCastResult.WRONGTGT
		
		val te = caster.worldObj.getTileEntity(mop.blockX, mop.blockY, mop.blockZ) as? TileAltar
		
		if (te == null) when (mop.sideHit) {
			0 -> --mop.blockY
			1 -> ++mop.blockY
			2 -> --mop.blockZ
			3 -> ++mop.blockZ
			4 -> --mop.blockX
			5 -> ++mop.blockX
		}
		
		if (MinecraftForge.EVENT_BUS.post(SpellCastEvent.Pre(this, caster))) return SpellCastResult.NOTALLOW
		
		val cost = getManaCost() * if (race == caster.race) 1 else AlfheimConfigHandler.raceManaMult.I
		if (!consumeMana(caster, cost, false, this)) return SpellCastResult.NOMANA
		
		if (te != null) {
			te.hasWater = true
			ASJUtilities.dispatchTEToNearbyPlayers(te)
			return SpellCastResult.OK
		}
		
		val stackToPlace = ItemStack(Blocks.water)
		stackToPlace.tryPlaceItemIntoWorld(caster, caster.worldObj, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, mop.hitVec.xCoord.F, mop.hitVec.yCoord.F, mop.hitVec.zCoord.F)
		
		if (stackToPlace.stackSize == 0) {
			consumeMana(caster, cost, true, this)
			return SpellCastResult.OK
		}
		
		return SpellCastResult.OBSTRUCT
	}
}