package alfheim.api.spell

import alfheim.api.entity.*
import alfheim.api.event.SpellCastEvent
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.*
import cpw.mods.fml.relauncher.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.*
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraftforge.common.MinecraftForge
import vazkii.botania.api.mana.ManaItemHandler
import kotlin.math.*

abstract class SpellBase @JvmOverloads constructor(val name: String, val race: EnumRace, protected var mana: Int, protected var cldn: Int, protected var cast: Int, val hard: Boolean = false) {
	
	/**
	 * Mana checking and consumption call here
	 * @return Result of cast
	 */
	abstract fun performCast(caster: EntityLivingBase): SpellCastResult
	
	open var damage = 1f
	open var duration = 1
	open var efficiency = 0.0
	open var radius = 16.0
	
	fun setManaCost(newVal: Int): Int {
		val was = mana
		mana = newVal
		return was
	}
	
	open fun getManaCost() = mana
	
	fun setCooldown(newVal: Int): Int {
		val was = cldn
		cldn = newVal
		return was
	}
	
	open fun getCooldown() = cldn
	
	fun setCastTime(newVal: Int): Int {
		val was = cast
		cast = newVal
		return was
	}
	
	fun getCastTime() = cast
	
	@SideOnly(Side.CLIENT)
	open fun render(caster: EntityLivingBase) = Unit
	
	fun checkCast(caster: EntityLivingBase): SpellCastResult {
		if (MinecraftForge.EVENT_BUS.post(SpellCastEvent.Pre(this, caster))) return SpellCastResult.NOTALLOW
		val cost = MathHelper.ceiling_double_int((getManaCost() * if ((caster as? EntityPlayer)?.race === race || hard) 1.toByte() else AlfheimConfigHandler.raceManaMult).D)
		if (cost < 0) throw IllegalArgumentException("Manacost for $name was $cost. Check your minetweaker scripts")
		val mana = caster !is EntityPlayer || caster.capabilities.isCreativeMode || consumeMana(caster, cost, true)
		return if (mana) SpellCastResult.OK else SpellCastResult.NOMANA
	}
	
	fun checkCastOver(caster: EntityLivingBase): SpellCastResult {
		if (MinecraftForge.EVENT_BUS.post(SpellCastEvent.Pre(this, caster))) return SpellCastResult.NOTALLOW
		val cost = MathHelper.ceiling_float_int(over(caster, (getManaCost() * if ((caster as? EntityPlayer)?.race === race || hard) 1.toByte() else AlfheimConfigHandler.raceManaMult).D))
		if (cost < 0) throw IllegalArgumentException("Manacost for $name was $cost (with overmage). Check your minetweaker scripts")
		val mana = caster !is EntityPlayer || caster.capabilities.isCreativeMode || consumeMana(caster, cost, true)
		return if (mana) SpellCastResult.OK else SpellCastResult.NOMANA
	}
	
	override fun equals(other: Any?) = other is SpellBase && name == other.name && race == other.race
	
	override fun hashCode(): Int {
		var result = name.hashCode()
		result = 31 * result + race.hashCode()
		return result
	}
	
	override fun toString() = name
	
	abstract val usableParams: Array<Any>
	
	enum class SpellCastResult {
		OK, DESYNC, NOTREADY, NOTARGET, WRONGTGT, OBSTRUCT, NOMANA, NOTALLOW, NOTSEEING
	}
	
	companion object {
		
		fun over(caster: EntityLivingBase?, was: Double) =
			(if (caster?.isPotionActive(AlfheimConfigHandler.potionIDOvermage) == true) was * 1.2 else was).F
		
		fun consumeMana(player: EntityPlayer, mana: Int, req: Boolean) =
			ManaItemHandler.requestManaExact(ItemStack(Blocks.stone), player, mana, req)
		
		fun say(caster: EntityPlayerMP, spell: SpellBase) {
			val l = caster.worldObj.getEntitiesWithinAABB(EntityPlayerMP::class.java, AxisAlignedBB.getBoundingBox(caster.posX, caster.posY, caster.posZ, caster.posX, caster.posY, caster.posZ).expand(40.0, 40.0, 40.0)) as List<EntityPlayerMP>
			l
				.filter { sqrt((caster.posX - it.posX).pow(2.0) + (caster.posY - it.posY).pow(2.0) + (caster.posZ - it.posZ).pow(2.0)) < 40 }
				.forEach { it.addChatMessage(ChatComponentText(EnumChatFormatting.UNDERLINE.toString() + "* " + caster.commandSenderName + ' '.toString() + StatCollector.translateToLocal("spell.cast") + EnumChatFormatting.RESET + ": " + StatCollector.translateToLocal("spell." + spell.name + ".words"))) }
		}
	}
}