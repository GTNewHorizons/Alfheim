package alfheim.common.item.rod

import alexsocol.asjlib.*
import alexsocol.asjlib.command.CommandDimTP
import alexsocol.asjlib.math.Vector3
import alexsocol.asjlib.security.InteractionSecurity
import alfheim.client.render.world.VisualEffectHandlerClient
import alfheim.common.core.handler.*
import alfheim.common.core.util.AlfheimTab
import alfheim.common.entity.EntityLolicorn
import alfheim.common.item.ItemMod
import alfheim.common.item.equipment.bauble.ItemPriestEmblem
import net.minecraft.block.Block
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.*
import net.minecraft.potion.PotionEffect
import net.minecraft.world.World
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.block.tile.TileBifrost
import vazkii.botania.common.core.helper.ItemNBTHelper
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.component3
import kotlin.math.abs

class ItemRodPortal: ItemMod("rodPortal") {
	
	init {
		creativeTab = AlfheimTab
		setFull3D()
		setMaxStackSize(1)
	}
	
	override fun itemInteractionForEntity(stack: ItemStack?, player: EntityPlayer?, target: EntityLivingBase?): Boolean {
		if (target is EntityLolicorn) {
			target.type = 1
			return true
		}
		
		return false
	}
	
	// [block marker] to [dimID] with [if to check Odin emblem]
	val pairs = arrayOf(Blocks.stone to 0 with false,
						Blocks.netherrack to -1 with false,
						Blocks.end_stone to 1 with true,
						ModBlocks.livingrock to AlfheimConfigHandler.dimensionIDAlfheim with false)
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		if (player.dimension == 1) return stack // no escape for the end
		
		val (x, y, z) = Vector3.fromEntity(player).mf()
		
		if (pairs.none { it.check(player, world.getBlock(x, y - 1, z)) }) return stack
		if (!isClearAllTheWayUp(world, x, y + 1, z, player)) return stack
		
		for (i in -2..2)
			for (k in -2..2) {
				if (abs(i) == 2 && abs(k) == 2)
					continue
				
				if (!isClearAllTheWayUp(world, x + i, y + 1, z + k, player))
					return stack
			}
		
		player.setItemInUse(stack, getMaxItemUseDuration(stack))
		player.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDEternity, 120))
		
		ItemNBTHelper.setInt(stack, TAG_X, x)
		ItemNBTHelper.setInt(stack, TAG_Y, y)
		ItemNBTHelper.setInt(stack, TAG_Z, z)
		
		return stack
	}
	
	fun isClearAllTheWayUp(world: World, x: Int, y: Int, z: Int, player: EntityPlayer) = InteractionSecurity.canDoSomethingHere(player, x, y, z) && world.canBlockSeeTheSky(x, y, z) && world.getPrecipitationHeight(x, z) <= y
	
	override fun getMaxItemUseDuration(stack: ItemStack?) = 120
	
	override fun getItemUseAction(stack: ItemStack?) = EnumAction.bow
	
	override fun onUsingTick(stack: ItemStack?, player: EntityPlayer, timeLeft: Int) {
		val (x, y, z) = Vector3.fromEntity(player).mf()
		
		if (ItemNBTHelper.getInt(stack, TAG_X, 0) != x || ItemNBTHelper.getInt(stack, TAG_Y, 0) != y || ItemNBTHelper.getInt(stack, TAG_Z, 0) != z) {
			player.stopUsingItem()
			return
		}
		
		val world = player.worldObj
		val count = timeLeft - 1
		
		val i = when (count % 12) {
			in 2..4 -> 2
			1, 5    -> 1
			0, 6    -> 0
			7, 11   -> -1
			else    -> -2
		}
		
		val j = count / 12
		
		val k = when (count % 12) {
			in 5..7 -> 2
			4, 8    -> 1
			3, 9    -> 0
			2, 10   -> -1
			else    -> -2
		}
		
		world.setBlock(x + i, y + j, z + k, ModBlocks.bifrost)
		(world.getTileEntity(x + i, y + j, z + k) as TileBifrost).ticks = timeLeft + 10
		
		VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.BIFROST, player.dimension, x.D, y.D, z.D)
	}
	
	override fun onPlayerStoppedUsing(stack: ItemStack?, world: World?, player: EntityPlayer, left: Int) {
		player.removePotionEffect(AlfheimConfigHandler.potionIDEternity)
	}
	
	override fun onEaten(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		val block = world.getBlock(player, y = -1)
		
		for (pair in pairs)
			if (pair.check(player, block)) {
				
				val (x, y, z) = Vector3.fromEntityCenter(player)
				VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.BIFROST_DONE, player.dimension, x, y, z, player.entityId.D)
				
				player.removePotionEffect(AlfheimConfigHandler.potionIDEternity)
				
				try {
					CommandDimTP.processCommand(player, arrayOf(pair.second.toString()))
				} catch (ignore: Throwable) {
				}
				
				break
			}
		
		return stack
	}
	
	private fun Triple<Block, Int, Boolean>.check(player: EntityPlayer, block: Block): Boolean {
		var flag = block === first && player.dimension != second
		if (third && ItemPriestEmblem.getEmblem(5, player) == null) flag = false
		return flag
	}
	
	companion object {
		
		const val TAG_X = "x"
		const val TAG_Y = "y"
		const val TAG_Z = "z"
	}
}