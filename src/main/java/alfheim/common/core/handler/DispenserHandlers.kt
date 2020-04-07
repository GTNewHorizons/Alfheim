package alfheim.common.core.handler

import alexsocol.asjlib.*
import alfheim.common.block.AlfheimBlocks
import alfheim.common.entity.*
import alfheim.common.item.AlfheimItems
import alfheim.common.item.material.ElvenResourcesMetas
import net.minecraft.block.BlockDispenser
import net.minecraft.dispenser.*
import net.minecraft.item.ItemStack
import net.minecraft.util.MathHelper
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.common.block.ModBlocks

/**
 * @author WireSegal
 * Created at 9:28 PM on 2/15/16.
 */
object BifrostFlowerDispenserHandler: IBehaviorDispenseItem {
	
	private val defaultBehavior = BehaviorDefaultDispenseItem()
	
	init {
		BlockDispenser.dispenseBehaviorRegistry.putObject(AlfheimItems.elvenResource, this)
	}
	
	override fun dispense(block: IBlockSource, stack: ItemStack): ItemStack? {
		if (stack.meta != ElvenResourcesMetas.RainbowDust) return defaultBehavior.dispense(block, stack)
		
		val facing = ForgeDirection.getOrientation(BlockDispenser.func_149937_b(block.blockMetadata).ordinal)
		val x = block.xInt + facing.offsetX
		val y = block.yInt + facing.offsetY
		val z = block.zInt + facing.offsetZ
		
		if (block.world.getBlock(x, y, z) == ModBlocks.flower) {
			block.world.setBlock(x, y, z, AlfheimBlocks.rainbowGrass, 2, 3)
			block.world.playSoundEffect(x.D, y.D, z.D, "botania:enchanterEnchant", 1f, 1f)
			stack.stackSize--
			return stack
		}
		return stack
	}
}

object ThrownPotionDispenserHandler: IBehaviorDispenseItem {
	
	init {
		BlockDispenser.dispenseBehaviorRegistry.putObject(AlfheimItems.splashPotion, this)
	}
	
	override fun dispense(block: IBlockSource, stack: ItemStack): ItemStack? {
		
		val facing = ForgeDirection.getOrientation(BlockDispenser.func_149937_b(block.blockMetadata).ordinal)
		
		val x = block.xInt + facing.offsetX + 0.5
		val y = block.yInt + facing.offsetY + 0.5
		val z = block.zInt + facing.offsetZ + 0.5
		
		val yaw = when (facing) {
			ForgeDirection.SOUTH -> 0f
			ForgeDirection.WEST  -> 90f
			ForgeDirection.NORTH -> 180f
			ForgeDirection.EAST  -> -90f
			else                 -> 0f
		}
		
		val pitch = when (facing) {
			ForgeDirection.UP   -> -90f
			ForgeDirection.DOWN -> 90f
			else                -> 0f
		}
		
		val potion = EntityThrownPotion(block.world, stack)
		
		--stack.stackSize
		
		potion.setLocationAndAngles(x, y, z, yaw, pitch)
		potion.posX -= (MathHelper.cos(potion.rotationYaw / 180f * Math.PI.F) * 0.16f).D
		potion.posY -= 0.10000000149011612
		potion.posZ -= (MathHelper.sin(potion.rotationYaw / 180f * Math.PI.F) * 0.16f).D
		potion.setPosition(potion.posX, potion.posY, potion.posZ)
		potion.yOffset = 0f
		val f = 0.4f
		potion.motionX = (-MathHelper.sin(potion.rotationYaw / 180f * Math.PI.F) * MathHelper.cos(potion.rotationPitch / 180f * Math.PI.F) * f).D
		potion.motionZ = (MathHelper.cos(potion.rotationYaw / 180f * Math.PI.F) * MathHelper.cos(potion.rotationPitch / 180f * Math.PI.F) * f).D
		potion.motionY = (-MathHelper.sin((potion.rotationPitch + potion.func_70183_g()) / 180f * Math.PI.F) * f).D
		potion.setThrowableHeading(potion.motionX, potion.motionY, potion.motionZ, potion.func_70182_d(), 1f)
		
		block.world.spawnEntityInWorld(potion)
		
		return stack
	}
}

object ThrownItemDispenserHandler: IBehaviorDispenseItem {
	
	init {
		BlockDispenser.dispenseBehaviorRegistry.putObject(AlfheimItems.fireGrenade, this)
	}
	
	override fun dispense(block: IBlockSource, stack: ItemStack): ItemStack? {
		
		val facing = ForgeDirection.getOrientation(BlockDispenser.func_149937_b(block.blockMetadata).ordinal)
		
		val x = block.xInt + facing.offsetX + 0.5
		val y = block.yInt + facing.offsetY + 0.5
		val z = block.zInt + facing.offsetZ + 0.5
		
		val yaw = when (facing) {
			ForgeDirection.SOUTH -> 0f
			ForgeDirection.WEST  -> 90f
			ForgeDirection.NORTH -> 180f
			ForgeDirection.EAST  -> -90f
			else                 -> 0f
		}
		
		val pitch = when (facing) {
			ForgeDirection.UP   -> -90f
			ForgeDirection.DOWN -> 90f
			else                -> 0f
		}
		
		val potion = EntityThrowableItem(block.world)
		
		--stack.stackSize
		
		potion.setLocationAndAngles(x, y, z, yaw, pitch)
		potion.posX -= (MathHelper.cos(potion.rotationYaw / 180f * Math.PI.F) * 0.16f).D
		potion.posY -= 0.10000000149011612
		potion.posZ -= (MathHelper.sin(potion.rotationYaw / 180f * Math.PI.F) * 0.16f).D
		potion.setPosition(potion.posX, potion.posY, potion.posZ)
		potion.yOffset = 0f
		val f = 0.4f
		potion.motionX = (-MathHelper.sin(potion.rotationYaw / 180f * Math.PI.F) * MathHelper.cos(potion.rotationPitch / 180f * Math.PI.F) * f).D
		potion.motionZ = (MathHelper.cos(potion.rotationYaw / 180f * Math.PI.F) * MathHelper.cos(potion.rotationPitch / 180f * Math.PI.F) * f).D
		potion.motionY = (-MathHelper.sin((potion.rotationPitch + potion.func_70183_g()) / 180f * Math.PI.F) * f).D
		potion.setThrowableHeading(potion.motionX, potion.motionY, potion.motionZ, potion.func_70182_d(), 1f)
		
		block.world.spawnEntityInWorld(potion)
		
		return stack
	}
}