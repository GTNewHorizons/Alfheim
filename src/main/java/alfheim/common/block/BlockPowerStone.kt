package alfheim.common.block

import alexsocol.asjlib.extendables.block.BlockModMeta
import alfheim.api.ModInfo
import alfheim.api.spell.SpellBase
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.core.util.AlfheimTab
import alfheim.common.lexicon.AlfheimLexiconData
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.world.World
import vazkii.botania.api.lexicon.ILexiconable

class BlockPowerStone: BlockModMeta(Material.rock, 5, ModInfo.MODID, "PowerStone", AlfheimTab, 2f, resist = 6000f), ILexiconable {
	
	override fun getIcon(side: Int, meta: Int) = if (side != 1) texture[0] else super.getIcon(side, meta)
	
	override fun onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, hitX: Float, hitY: Float, hitZ: Float) =
		if (SpellBase.consumeMana(player, 10000, false) && press(world, x, y, z, player)) SpellBase.consumeMana(player, 10000, true) else false
	
	fun press(world: World, x: Int, y: Int, z: Int, player: EntityPlayer): Boolean {
		return when (world.getBlockMetadata(x, y, z)) {
			1    -> makePlayerBerserk(player)
			2    -> makePlayerOvermage(player)
			3    -> makePlayerTank(player)
			4    -> makePlayerNinja(player)
			else -> false
		}
	}
	
	// +20% DMG, -20% HP
	private fun makePlayerBerserk(player: EntityPlayer): Boolean {
		if (!player.isPotionActive(AlfheimConfigHandler.potionIDOvermage) && !player.isPotionActive(AlfheimConfigHandler.potionIDTank) && !player.isPotionActive(AlfheimConfigHandler.potionIDNinja)) {
			player.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDBerserk, 72000, 0))
			return true
		}
		return false
	}
	
	// +20% Spell DMG, +20% Spell Cost
	private fun makePlayerOvermage(player: EntityPlayer): Boolean {
		if (!player.isPotionActive(AlfheimConfigHandler.potionIDBerserk) && !player.isPotionActive(AlfheimConfigHandler.potionIDTank) && !player.isPotionActive(AlfheimConfigHandler.potionIDNinja)) {
			player.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDOvermage, 72000, 0))
			return true
		}
		return false
	}
	
	// +20% Resistance, -20% Speed
	private fun makePlayerTank(player: EntityPlayer): Boolean {
		if (!player.isPotionActive(AlfheimConfigHandler.potionIDOvermage) && !player.isPotionActive(AlfheimConfigHandler.potionIDBerserk) && !player.isPotionActive(AlfheimConfigHandler.potionIDNinja)) {
			player.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDTank, 72000, 0))
			return true
		}
		return false
	}
	
	// +20% Speed, -20% DMG
	private fun makePlayerNinja(player: EntityPlayer): Boolean {
		if (!player.isPotionActive(AlfheimConfigHandler.potionIDOvermage) && !player.isPotionActive(AlfheimConfigHandler.potionIDTank) && !player.isPotionActive(AlfheimConfigHandler.potionIDBerserk)) {
			player.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDNinja, 72000, 0))
			return true
		}
		return false
	}
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack) = AlfheimLexiconData.shrines
}