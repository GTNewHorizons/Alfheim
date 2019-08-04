package alfheim.common.item.rod

import alfheim.AlfheimCore
import alfheim.api.ModInfo
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.*
import net.minecraft.util.MathHelper
import net.minecraft.world.World
import net.minecraftforge.oredict.OreDictionary
import vazkii.botania.api.item.IManaProficiencyArmor
import vazkii.botania.api.mana.*
import vazkii.botania.common.Botania
import java.util.*

class ItemRodGrass: Item(), IManaUsingItem {
	init {
		creativeTab = AlfheimCore.alfheimTab
		setMaxStackSize(1)
		setTextureName(ModInfo.MODID + ":grassRod")
		unlocalizedName = "grassRod"
	}
	
	override fun getItemUseAction(stack: ItemStack?): EnumAction {
		return EnumAction.bow
	}
	
	override fun getMaxItemUseDuration(stack: ItemStack?): Int {
		return 72000
	}
	
	override fun onUsingTick(stack: ItemStack?, player: EntityPlayer?, count: Int) {
		if (count != getMaxItemUseDuration(stack) && count % 5 == 0) terraform(stack, player!!.worldObj, player)
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World?, player: EntityPlayer): ItemStack {
		player.setItemInUse(stack, getMaxItemUseDuration(stack))
		return stack
	}
	
	fun terraform(stack: ItemStack?, world: World, player: EntityPlayer) {
		val range = if (IManaProficiencyArmor.Helper.hasProficiency(player)) 22 else 16
		
		val x = MathHelper.floor_double(player.posX)
		val y = MathHelper.floor_double(player.posY - if (world.isRemote) 2 else 1)
		val z = MathHelper.floor_double(player.posZ)
		
		var done = false
		for (i in -range..range) {
			for (k in -range..range) {
				for (j in -1..1) {
					if (!world.isAirBlock(x + i, y + j + 1, z + k)) continue
					for (id in OreDictionary.getOreIDs(ItemStack(world.getBlock(x + i, y + j, z + k), 1, world.getBlockMetadata(x + i, y + j, z + k))))
						if (validBlocks.contains(OreDictionary.getOreName(id)))
							if (place(stack, player, world, x + i, y + j, z + k, 1, 0.5f, 1f, 0.5f, Blocks.grass, if (world.canBlockSeeTheSky(x + i, y + j, z + k)) 30 else 50, 0f, 1f, 0f)) done = true
				}
			}
			if (done) break
		}
	}
	
	override fun usesMana(stack: ItemStack): Boolean {
		return true
	}
	
	companion object {
		
		internal val validBlocks = Arrays.asList("dirt", "mycelium", "podzol")
		
		fun place(stack: ItemStack?, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float, block: Block, cost: Int, r: Float, g: Float, b: Float): Boolean {
			if (!ManaItemHandler.requestManaExactForTool(stack, player, cost, false)) return false
			
			world.setBlock(x, y, z, block)
			ManaItemHandler.requestManaExactForTool(stack, player, cost, true)
			for (i in 0..5) Botania.proxy.sparkleFX(world, x + Math.random(), y + Math.random(), z + Math.random(), r, g, b, 1f, 5)
			
			return true
		}
	}
}