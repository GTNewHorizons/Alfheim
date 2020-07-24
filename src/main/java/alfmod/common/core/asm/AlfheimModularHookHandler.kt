package alfmod.common.core.asm

import alfmod.common.core.handler.*
import alfmod.common.entity.*
import alfmod.common.item.AlfheimModularItems
import alfmod.common.item.equipment.armor.ItemSnowArmor
import gloomyfolken.hooklib.asm.*
import net.minecraft.block.*
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntitySnowball
import net.minecraft.init.Blocks
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World
import java.util.*

object AlfheimModularHookHandler {
	
	// Wrath of the winter hooks
	
	@JvmStatic
	@Hook(injectOnExit = true, targetMethod = "onImpact")
	fun spawnSpriteFromSnowball(ball: EntitySnowball, mop: MovingObjectPosition) {
		if (WRATH_OF_THE_WINTER) {
			if (!ball.worldObj.isRemote && mop.entityHit == null && ball.worldObj.isRaining && ball.worldObj.rand.nextInt(32) == 0) {
				val sprite = EntitySnowSprite(ball.worldObj)
				sprite.setPosition(ball.posX, ball.posY + 1, ball.posZ)
				
				if (sprite.canSpawnHere)
					ball.worldObj.spawnEntityInWorld(sprite)
			}
		}
	}
	
	@JvmStatic
	@Hook(createMethod = true, returnCondition = ReturnCondition.ALWAYS, targetMethod = "getRelativeSlipperiness")
	fun icyFloorForBoots(block: Block, requester: Entity): Float {
		return if (requester is EntityPlayer && !requester.isSneaking && (AlfheimModularItems.snowHelmet as ItemSnowArmor).hasArmorSet(requester)) 0.99f else block.slipperiness
	}
	
	// summer event hooks
	
	var replaceMelonWithMob = false
	
	@JvmStatic
	@Hook(targetMethod = "updateTick")
	fun replaceMelonWithMobPre(block: BlockStem, world: World?, x: Int, y: Int, z: Int, rand: Random?) {
		replaceMelonWithMob = SUMMER_EVENT && block === Blocks.melon_stem
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ON_TRUE, booleanReturnConstant = false, targetMethod = "setBlock")
	fun replaceMelonWithMob(world: World, x: Int, y: Int, z: Int, block: Block?): Boolean {
		if (replaceMelonWithMob && block === Blocks.melon_block && world.rand.nextInt(3) == 0) {
			replaceMelonWithMob = false
			return world.spawnEntityInWorld(EntityRollingMelon(world).apply { setPosition(x + 0.5, y + 0.5, z + 0.5); onSpawnWithEgg(null) })
		}
		
		return false
	}
	
	@JvmStatic
	@Hook(targetMethod = "updateTick", injectOnExit = true)
	fun replaceMelonWithMobPost(block: BlockStem, world: World?, x: Int, y: Int, z: Int, rand: Random?) {
		replaceMelonWithMob = false
	}
}