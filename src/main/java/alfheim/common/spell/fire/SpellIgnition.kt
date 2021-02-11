package alfheim.common.spell.fire

import alexsocol.asjlib.*
import alexsocol.asjlib.security.InteractionSecurity
import alfheim.api.entity.*
import alfheim.api.event.SpellCastEvent
import alfheim.api.spell.SpellBase
import alfheim.common.core.handler.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import net.minecraftforge.common.MinecraftForge

object SpellIgnition: SpellBase("ignition", EnumRace.SALAMANDER, 2000, 100, 5) {
	
	override var efficiency = 5.0
	
	override val usableParams: Array<Any>
		get() = arrayOf(efficiency, radius)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		if (caster !is EntityPlayer) return SpellCastResult.NOTALLOW
		
		while (true) {
			val tg = CardinalSystem.TargetingSystem.getTarget(caster)
			val tgt = tg.target ?: break
			
			if (tg.isParty) return SpellCastResult.WRONGTGT
			if (!InteractionSecurity.canDoSomethingWithEntity(caster, tgt)) return SpellCastResult.NOTALLOW
			if (ASJUtilities.isNotInFieldOfVision(tgt, caster)) return SpellCastResult.NOTSEEING
			
			val result = SpellDispel.checkCastOver(caster)
			
			if (result == SpellCastResult.OK)
				tgt.setFire(over(caster, efficiency).I)
			
			return result
		}
		
		val cost = getManaCost() * if (race == caster.race) 1 else AlfheimConfigHandler.raceManaMult
		if (!consumeMana(caster, cost, false, this)) return SpellCastResult.NOMANA
		
		val mop = ASJUtilities.getSelectedBlock(caster, radius, false)
		if (mop == null || mop.typeOfHit != MovingObjectType.BLOCK || mop.sideHit == -1) return SpellCastResult.WRONGTGT
		
		when (mop.sideHit) {
			0 -> --mop.blockY
			1 -> ++mop.blockY
			2 -> --mop.blockZ
			3 -> ++mop.blockZ
			4 -> --mop.blockX
			5 -> ++mop.blockX
		}
		
		if (!Blocks.fire.canPlaceBlockAt(caster.worldObj, mop.blockX, mop.blockY, mop.blockZ)) return SpellCastResult.OBSTRUCT
		if (MinecraftForge.EVENT_BUS.post(SpellCastEvent.Pre(this, caster))) return SpellCastResult.NOTALLOW
		
		val stackToPlace = ItemStack(Blocks.fire)
		stackToPlace.tryPlaceItemIntoWorld(caster, caster.worldObj, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, mop.hitVec.xCoord.F, mop.hitVec.yCoord.F, mop.hitVec.zCoord.F)
		
		if (stackToPlace.stackSize == 0) {
			consumeMana(caster, cost, true, this)
			return SpellCastResult.OK
		}
		
		return SpellCastResult.OBSTRUCT
	}
}