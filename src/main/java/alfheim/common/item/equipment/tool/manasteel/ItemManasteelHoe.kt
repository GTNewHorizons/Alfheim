package alfheim.common.item.equipment.tool.manasteel

import alfheim.AlfheimCore
import alfheim.api.ModInfo
import cpw.mods.fml.common.eventhandler.Event.Result
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemHoe
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.UseHoeEvent
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.item.ISortableTool
import vazkii.botania.api.mana.IManaUsingItem
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.item.equipment.tool.ToolCommons

open class ItemManasteelHoe @JvmOverloads constructor(mat: Item.ToolMaterial = BotaniaAPI.manasteelToolMaterial, name: String = "ManasteelHoe"): ItemHoe(mat), IManaUsingItem, ISortableTool {
	
	val manaPerDamage: Int
		get() = MANA_PER_DAMAGE
	
	init {
		creativeTab = AlfheimCore.alfheimTab
		setTextureName(ModInfo.MODID + ":" + name)
		unlocalizedName = name
	}
	
	override fun hitEntity(par1ItemStack: ItemStack?, par2EntityLivingBase: EntityLivingBase?, par3EntityLivingBase: EntityLivingBase?): Boolean {
		ToolCommons.damageItem(par1ItemStack, 1, par3EntityLivingBase, manaPerDamage)
		return true
	}
	
	override fun onBlockDestroyed(stack: ItemStack?, world: World?, block: Block, x: Int, y: Int, z: Int, entity: EntityLivingBase?): Boolean {
		if (block.getBlockHardness(world, x, y, z) != 0f)
			ToolCommons.damageItem(stack, 1, entity, manaPerDamage)
		return true
	}
	
	override fun onItemUse(stack: ItemStack?, player: EntityPlayer, world: World?, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		if (!player.canPlayerEdit(x, y, z, side, stack)) {
			return false
		} else {
			val event = UseHoeEvent(player, stack, world, x, y, z)
			if (MinecraftForge.EVENT_BUS.post(event)) {
				return false
			}
			
			if (event.result == Result.ALLOW) {
				ToolCommons.damageItem(stack, 1, player, manaPerDamage)
				return true
			}
			
			val block = world!!.getBlock(x, y, z)
			
			if (side != 0 && world.isAirBlock(x, y + 1, z) && (block === Blocks.grass || block === Blocks.dirt)) {
				val block1 = Blocks.farmland
				world.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, block1.stepSound.stepResourcePath, (block1.stepSound.getVolume() + 1) * 0.5f, block1.stepSound.pitch * 0.8f)
				
				if (world.isRemote) {
					return true
				} else {
					world.setBlock(x, y, z, block1)
					ToolCommons.damageItem(stack, 1, player, manaPerDamage)
					return true
				}
			} else {
				return false
			}
		}
	}
	
	override fun onUpdate(stack: ItemStack?, world: World, player: Entity?, par4: Int, par5: Boolean) {
		if (!world.isRemote && player is EntityPlayer && stack!!.itemDamage > 0 && ManaItemHandler.requestManaExactForTool(stack, (player as EntityPlayer?)!!, manaPerDamage * 2, true))
			stack.itemDamage = stack.itemDamage - 1
	}
	
	override fun getIsRepairable(par1ItemStack: ItemStack?, par2ItemStack: ItemStack): Boolean {
		return par2ItemStack.item === ModItems.manaResource && par2ItemStack.itemDamage == 0 || super.getIsRepairable(par1ItemStack, par2ItemStack)
	}
	
	override fun usesMana(stack: ItemStack): Boolean {
		return true
	}
	
	override fun getSortingType(stack: ItemStack): ISortableTool.ToolType {
		return ISortableTool.ToolType.PICK
	}
	
	override fun getSortingPriority(stack: ItemStack): Int {
		return ToolCommons.getToolPriority(stack)
	}
	
	companion object {
		
		private val MANA_PER_DAMAGE = 60
	}
}