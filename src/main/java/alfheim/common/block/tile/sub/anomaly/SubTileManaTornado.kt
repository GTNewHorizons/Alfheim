package alfheim.common.block.tile.sub.anomaly

import alexsocol.asjlib.math.Vector3
import alexsocol.asjlib.render.ASJRenderHelper
import alfheim.api.block.tile.SubTileEntity
import alfheim.common.core.util.*
import net.minecraft.item.ItemStack
import vazkii.botania.common.Botania
import vazkii.botania.common.entity.EntityManaBurst
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.item.lens.ItemLens
import java.util.*

class SubTileManaTornado: SubTileEntity() {
	
	internal val v = Vector3()
	
	override val targets: List<Any>
		get() {
			if (worldObj.rand.nextInt(100) == 0) {
				val l = ArrayList<Any>()
				l.add(spawnBurst())
				return l
			}
			return EMPTY_LIST
		}
	
	override val strip: Int
		get() = 2
	
	override val rarity: EnumAnomalityRarity
		get() = EnumAnomalityRarity.RARE
	
	public override fun update() {
		if (inWG()) return
		
		val c = ASJRenderHelper.colorCode[worldObj.rand.nextInt(ASJRenderHelper.colorCode.size)]
		v.rand().sub(0.5).normalize().mul(Math.random()).add(superTile!!).add(0.5)
		Botania.proxy.wispFX(worldObj, v.x, v.y, v.z, (c shr 16 and 0xFF) / 255f, (c shr 8 and 0xFF) / 255f, (c and 0xFF) / 255f, (Math.random() * 0.25 + 0.25).F, 0f, (Math.random() * 2 + 1).F)
	}
	
	fun spawnBurst(): EntityManaBurst {
		val burst = EntityManaBurst(worldObj)
		val motionModifier = 0.5f
		burst.color = ASJRenderHelper.colorCode[worldObj.rand.nextInt(ASJRenderHelper.colorCode.size)]
		burst.mana = 120
		burst.startingMana = 340
		burst.minManaLoss = 50
		burst.manaLossPerTick = 1f
		burst.gravity = 0f
		
		var meta = worldObj.rand.nextInt(ItemLens.SUBTYPES + 1)
		if (meta == ItemLens.SUBTYPES) meta = ItemLens.STORM
		
		val lens = ItemStack(ModItems.lens, 1, meta)
		burst.sourceLens = lens
		
		v.rand().sub(0.5).normalize().mul(motionModifier.D).add(0.5).add(superTile!!)
		burst.setPosition(v.x, v.y, v.z)
		v.sub(0.5).sub(superTile!!)
		burst.setMotion(v.x, v.y, v.z)
		
		return burst
	}
	
	override fun performEffect(target: Any) {
		if (target is EntityManaBurst) worldObj.spawnEntityInWorld(target)
	}
	
	override fun typeBits() = ALL
}