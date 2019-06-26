package alfheim.common.spell.fire

import alexsocol.asjlib.ASJUtilities
import alfheim.api.entity.EnumRace
import alfheim.api.event.SpellCastEvent
import alfheim.api.spell.SpellBase
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import net.minecraftforge.common.MinecraftForge

class SpellIgnition: SpellBase("ignition", EnumRace.SALAMANDER, 2000, 100, 5) {
	
	override fun performCast(caster: EntityLivingBase): SpellBase.SpellCastResult {
		if (caster !is EntityPlayer) return SpellBase.SpellCastResult.NOTALLOW
		val mop = ASJUtilities.getSelectedBlock(caster, 16.0, false)
		if (mop == null || mop.typeOfHit != MovingObjectType.BLOCK || mop.sideHit == -1) return SpellBase.SpellCastResult.WRONGTGT
		
		when (mop.sideHit) {
			0 -> --mop.blockY
			1 -> ++mop.blockY
			2 -> --mop.blockZ
			3 -> ++mop.blockZ
			4 -> --mop.blockX
			5 -> ++mop.blockX
		}
		
		if (!Blocks.fire.canPlaceBlockAt(caster.worldObj, mop.blockX, mop.blockY, mop.blockZ)) return SpellBase.SpellCastResult.WRONGTGT
		if (MinecraftForge.EVENT_BUS.post(SpellCastEvent.Pre(this, caster))) return SpellBase.SpellCastResult.NOTALLOW
		val mana = caster.capabilities.isCreativeMode || SpellBase.consumeMana(caster, (manaCost * if (race == EnumRace.getRace(caster)) 1 else 1.5).toInt(), false)
		
		if (!mana) return SpellBase.SpellCastResult.NOMANA
		
		val stackToPlace = ItemStack(Blocks.fire)
		stackToPlace.tryPlaceItemIntoWorld(caster, caster.worldObj, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, mop.hitVec.xCoord.toFloat(), mop.hitVec.yCoord.toFloat(), mop.hitVec.zCoord.toFloat())
		
		if (stackToPlace.stackSize == 0) {
			SpellBase.consumeMana(caster, (manaCost * if (race == EnumRace.getRace(caster)) 1 else 1.5).toInt(), true)
			return SpellBase.SpellCastResult.OK
		}
		
		return SpellBase.SpellCastResult.OBSTRUCT
	}
}