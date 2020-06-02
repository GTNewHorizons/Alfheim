package alfheim.common.core.handler

import alexsocol.asjlib.*
import alfheim.common.block.AlfheimBlocks
import alfheim.common.entity.*
import alfheim.common.item.AlfheimItems
import alfheim.common.item.material.ElvenResourcesMetas
import net.minecraft.block.BlockDispenser
import net.minecraft.dispenser.*
import net.minecraft.init.*
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntityDispenser
import net.minecraft.util.MathHelper
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.item.ModItems

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
		
		if (block.world.getBlock(x, y, z) === ModBlocks.flower) {
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

object WaterBowlDispenserHandler: BehaviorDefaultDispenseItem() {
	
	private val field_150840_b = BehaviorDefaultDispenseItem()
	
	init {
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.bowl, this)
	}
	
	override fun dispenseStack(block: IBlockSource, stack: ItemStack): ItemStack {
		val enumfacing = BlockDispenser.func_149937_b(block.blockMetadata)
		val world = block.world
		val i = block.xInt + enumfacing.frontOffsetX
		val j = block.yInt + enumfacing.frontOffsetY
		val k = block.zInt + enumfacing.frontOffsetZ
		val target = world.getBlock(i, j, k)
		val l = world.getBlockMetadata(i, j, k)
		val item = if (target === Blocks.flowing_water && l == 0) { // no need in check for static water because of block update
			ModItems.waterBowl
		} else {
			return super.dispenseStack(block, stack)
		}
		
		if (--stack.stackSize == 0) {
			stack.func_150996_a(item)
			stack.stackSize = 1
		} else if ((block.blockTileEntity as TileEntityDispenser).func_146019_a(ItemStack(item)) < 0) {
			this.field_150840_b.dispense(block, ItemStack(item))
		}
		
		return stack
	}
}