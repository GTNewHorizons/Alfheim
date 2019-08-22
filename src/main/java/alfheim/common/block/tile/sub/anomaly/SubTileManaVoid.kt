package alfheim.common.block.tile.sub.anomaly

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.api.block.tile.SubTileEntity
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.Botania

class SubTileManaVoid: SubTileEntity() {
	var mana: Int = 0
	internal val v = Vector3()
	
	override val targets: List<Any>
		get() = if (inWG()) EMPTY_LIST else allAroundRaw(EntityPlayer::class.java, radius.toDouble())
	
	override val strip: Int
		get() = 3
	
	override val rarity: EnumAnomalityRarity
		get() = EnumAnomalityRarity.COMMON
	
	public override fun update() {
		if (mana >= 120000) {
			for (player in allAround(EntityPlayer::class.java, radius.toDouble())) {
				radius = 50
				for (i in 0..99) performEffect(player)
			}
			
			radius = 10
			worldObj.createExplosion(null, x().toDouble(), y().toDouble(), z().toDouble(), radius.toFloat(), false)
			
			for (i in 0..127) {
				v.rand().sub(0.5).normalize().mul(Math.random() * 0.1)
				Botania.proxy.wispFX(worldObj, x() + 0.5, y() + 0.5, z() + 0.5, 0.01f, 0.75f, 1f, 0.25f, v.x.toFloat(), v.y.toFloat(), v.z.toFloat(), 2f)
			}
			
			mana = 0
		}
	}
	
	override fun performEffect(target: Any) {
		if (target !is EntityPlayer) return
		
		val m = ManaItemHandler.requestMana(ItemStack(Blocks.stone), target, 100, true)
		if (m > 0) {
			mana += m
			
			var flag = false
			if (!ASJUtilities.isServer) flag = Minecraft.getMinecraft().thePlayer !== target
			
			val l = v.set(superTile!!).add(0.5).sub(target.posX, target.posY + if (flag) 1.0 else -0.62, target.posZ).length()
			v.normalize().mul(l / 40)
			Botania.proxy.wispFX(worldObj, target.posX, target.posY + if (flag) 1.0 else -0.62, target.posZ, 0.01f, 0.75f, 1f, radius / 40f, v.x.toFloat(), v.y.toFloat(), v.z.toFloat(), 2f)
		}
	}
	
	override fun typeBits() = MANA
	
	override fun writeCustomNBT(cmp: NBTTagCompound) {
		super.writeCustomNBT(cmp)
		cmp.setInteger(TAG_MANA, mana)
	}
	
	override fun readCustomNBT(cmp: NBTTagCompound) {
		super.writeCustomNBT(cmp)
		mana = cmp.getInteger(TAG_MANA)
	}
	
	companion object {
		const val TAG_MANA = "mana"
		var radius = 10
	}
}
