package alfheim.common.potion

import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.DamageSourceSpell
import alfheim.common.spell.darkness.SpellSacrifice
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.ai.attributes.BaseAttributeMap
import net.minecraft.entity.boss.IBossDisplayData
import net.minecraft.util.DamageSource
import vazkii.botania.common.Botania

class PotionSacrifice: PotionAlfheim(AlfheimConfigHandler.potionIDSacrifice, "sacrifice", false, 0) {
	
	var timeQueued: Int = 0
	
	override fun isReady(time: Int, mod: Int): Boolean {
		timeQueued = time
		return AlfheimCore.enableMMO && timeQueued <= 32
	}
	
	override fun performEffect(target: EntityLivingBase, mod: Int) {
		if (!AlfheimCore.enableMMO) return
		if (timeQueued == 32)
			for (i in 0..7) target.worldObj.playSoundEffect(target.posX, target.posY, target.posZ, ModInfo.MODID + ":redexp", 10000.0f, 0.8f + target.worldObj.rand.nextFloat() * 0.2f)
		else
			particles(target, 32 - timeQueued)
	}
	
	override fun removeAttributesModifiersFromEntity(target: EntityLivingBase?, attributes: BaseAttributeMap, ampl: Int) {
		super.removeAttributesModifiersFromEntity(target, attributes, ampl)
		if (!AlfheimCore.enableMMO) return
		val l = target!!.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, target.boundingBox.copy().expand(32.0, 32.0, 32.0)) as List<EntityLivingBase>
		var dmg: DamageSource
		for (e in l) {
			if (e is IBossDisplayData) continue
			dmg = if (e === target) DamageSourceSpell.sacrifice else DamageSourceSpell.darkness(target)
			e.attackEntityFrom(dmg, SpellSacrifice.damage)
		}
	}
	
	fun particles(target: EntityLivingBase, time: Int) {
		val v = Vector3()
		for (i in 0..127) {
			v.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize().mul(time.toDouble())
			Botania.proxy.wispFX(target.worldObj, target.posX + v.x, target.posY + v.y, target.posZ + v.z, 1f, Math.random().toFloat() * 0.5f, Math.random().toFloat() * 0.075f, (Math.random() * time + 1).toFloat(), 0f, (Math.random() * 3.0 + 2).toFloat())
		}
	}
}