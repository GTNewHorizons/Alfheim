package alfheim.api.spell

import alfheim.api.entity.EnumRace
import alfheim.api.event.SpellCastEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.*
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.potion.Potion
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
	
	// public abstract String[] getWords();
	
	fun setManaCost(newVal: Int): Int {
		val temp = mana
		mana = newVal
		return temp
	}
	
	open fun getManaCost(): Int {
		return mana
	}
	
	fun setCooldown(newVal: Int): Int {
		val temp = cldn
		cldn = newVal
		return temp
	}
	
	open fun getCooldown(): Int {
		return cldn
	}
	
	fun setCastTime(newVal: Int): Int {
		val temp = cast
		cast = newVal
		return temp
	}
	
	fun getCastTime(): Int {
		return cast
	}
	
	@SideOnly(Side.CLIENT)
	open fun render(caster: EntityLivingBase) {
		// NO-OP
	}
	
	fun checkCast(caster: EntityLivingBase): SpellCastResult {
		if (MinecraftForge.EVENT_BUS.post(SpellCastEvent.Pre(this, caster))) return SpellCastResult.NOTALLOW
		val cost = MathHelper.ceiling_double_int(getManaCost() * if (caster is EntityPlayer && race == EnumRace.getRace(caster) || hard) 1.0 else 1.5)
		val mana = caster !is EntityPlayer || caster.capabilities.isCreativeMode || consumeMana(caster, cost, true)
		return if (mana) SpellCastResult.OK else SpellCastResult.NOMANA
	}
	
	fun checkCastOver(caster: EntityLivingBase): SpellCastResult {
		if (MinecraftForge.EVENT_BUS.post(SpellCastEvent.Pre(this, caster))) return SpellCastResult.NOTALLOW
		val cost = MathHelper.ceiling_float_int(over(caster, getManaCost() * if (caster is EntityPlayer && race == EnumRace.getRace(caster) || hard) 1.0 else 1.5))
		val mana = caster !is EntityPlayer || caster.capabilities.isCreativeMode || consumeMana(caster, cost, true)
		return if (mana) SpellCastResult.OK else SpellCastResult.NOMANA
	}
	
	override fun equals(other: Any?): Boolean {
		return other is SpellBase && name == other.name && race == other.race
	}
	
	override fun hashCode(): Int {
		return getManaCost() shl 16 and -0x10000 or (getCooldown() and 0xFFFF)
	}
	
	override fun toString(): String {
		return name
	}
	
	enum class SpellCastResult {
		OK, DESYNC, NOTREADY, NOTARGET, WRONGTGT, OBSTRUCT, NOMANA, NOTALLOW, NOTSEEING
	}
	
	companion object {
		
		// Will be set during preInit
		var overmag: Potion? = null
		
		fun over(caster: EntityLivingBase?, was: Double): Float {
			return try {
				(if (caster!!.isPotionActive(overmag!!)) was * 1.2 else was).toFloat()
			} catch (e: Throwable) {
				was.toFloat()
			}
		}
		
		fun consumeMana(player: EntityPlayer, mana: Int, req: Boolean): Boolean {
			return ManaItemHandler.requestManaExact(ItemStack(Blocks.stone), player, mana, req)
		}
		
		fun say(caster: EntityPlayerMP, spell: SpellBase) {
			val l = caster.worldObj.getEntitiesWithinAABB(EntityPlayerMP::class.java, AxisAlignedBB.getBoundingBox(caster.posX, caster.posY, caster.posZ, caster.posX, caster.posY, caster.posZ).expand(40.0, 40.0, 40.0)) as List<EntityPlayerMP>
			for (player in l) if (sqrt((caster.posX - player.posX).pow(2.0) + (caster.posY - player.posY).pow(2.0) + (caster.posZ - player.posZ).pow(2.0)) < 40) player.addChatMessage(ChatComponentText(EnumChatFormatting.UNDERLINE.toString() + "* " + caster.commandSenderName + ' '.toString() + StatCollector.translateToLocal("spell.cast") + EnumChatFormatting.RESET + ": " + StatCollector.translateToLocal("spell." + spell.name + ".words")))
		}
	}
}