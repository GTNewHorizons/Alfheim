package alfheim.common.entity

import alexsocol.asjlib.*
import alfheim.common.core.handler.AlfheimConfigHandler
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.monster.EntityCreeper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.potion.PotionEffect
import net.minecraft.util.AxisAlignedBB
import net.minecraft.world.World
import vazkii.botania.common.item.ModItems

/**
 * All the mana is mine mahhhaahahha
 */
class EntityVoidCreeper(world: World): EntityCreeper(world) {
	
	private var lastActiveTime: Int = 0
	private var timeSinceIgnited: Int = 0
	private var range: Int = 3
	private var fuseTime = 30
	
	override fun writeEntityToNBT(tag: NBTTagCompound) {
		super.writeEntityToNBT(tag)
		
		if (dataWatcher.getWatchableObjectByte(17).I == 1) {
			tag.setBoolean("powered", true)
		}
		
		tag.setShort("Fuse", this.fuseTime.toShort())
		tag.setBoolean("ignited", func_146078_ca())
	}
	
	override fun readEntityFromNBT(tag: NBTTagCompound) {
		super.readEntityFromNBT(tag)
		dataWatcher.updateObject(17, java.lang.Byte.valueOf((if (tag.getBoolean("powered")) 1 else 0).toByte()))
		if (tag.hasKey("Fuse", 99)) fuseTime = tag.getShort("Fuse").I
		if (tag.getBoolean("ignited")) func_146079_cb()
	}
	
	override fun getDropItem() = Items.gunpowder!!
	
	override fun dropFewItems(par1: Boolean, par2: Int) {
		if (par1) {
			if (Math.random() < AlfheimConfigHandler.blackLotusDropRate) {
				entityDropItem(ItemStack(ModItems.blackLotus), 1F)
			}
		}
	}
	
	override fun onUpdate() {
		if (isEntityAlive) {
			this.lastActiveTime = this.timeSinceIgnited
			
			if (func_146078_ca()) {
				this.creeperState = 1
			}
			
			val i = this.creeperState
			
			if (i > 0 && this.timeSinceIgnited == 0) {
				playSound("creeper.primed", 1f, 0.5f)
			}
			
			this.timeSinceIgnited += i
			
			if (this.timeSinceIgnited < 0) {
				this.timeSinceIgnited = 0
			}
			
			if (this.timeSinceIgnited >= this.fuseTime) {
				this.timeSinceIgnited = this.fuseTime
				creeperGoBoom()
			}
		}
		super.onUpdate()
	}
	
	override fun fall(distance: Float) {
		super.fall(distance)
		this.timeSinceIgnited = (this.timeSinceIgnited.F + distance * 1.5f).I
		
		if (this.timeSinceIgnited > this.fuseTime - 5) {
			this.timeSinceIgnited = this.fuseTime - 5
		}
	}
	
	private fun creeperGoBoom() {
		if (!worldObj.isRemote) {
			val r = range * if (powered) 2 else 1
			
			val potential = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, AxisAlignedBB.getBoundingBox(posX - r, posY - r, posZ - r, posX + r, posY + r, posZ + r))
			
			for (p in potential) {
				if (p is EntityPlayer) {
					p.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDManaVoid, if (powered) 1200 else 120, 0))
				}
			}
			
			worldObj.createExplosion(this, posX, posY, posZ, 1f, false)
			setDead()
		}
	}
}
