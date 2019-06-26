package alfheim.common.block

import alfheim.api.ModInfo
import alfheim.api.spell.SpellBase
import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.lexicon.AlfheimLexiconData
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.util.IIcon
import net.minecraft.world.World
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry

class BlockPowerStone: Block(Material.rock), ILexiconable {
	init {
		setBlockName("PowerStone")
		setBlockTextureName(ModInfo.MODID + ":ManaInfuserBottomDark")
		setHardness(2f)
		setResistance(6000f)
		setStepSound(Block.soundTypeStone)
	}
	
	override fun damageDropped(meta: Int): Int {
		return meta
	}
	
	override fun getIcon(side: Int, meta: Int): IIcon {
		var meta = meta
		if (meta < 0 || icons.size <= meta) meta = 0
		
		return if (side == 1) icons[meta] else icons[0]
	}
	
	override fun registerBlockIcons(reg: IIconRegister) {
		super.registerBlockIcons(reg)
		icons[0] = blockIcon
		for (i in 1 until icons.size)
			icons[i] = reg.registerIcon(ModInfo.MODID + ":PowerStone" + i)
	}
	
	override fun getSubBlocks(item: Item, tab: CreativeTabs?, subs: MutableList<*>) {
		subs.add(ItemStack(item, 1, 1)) // berserk
		subs.add(ItemStack(item, 1, 2)) // overmage
		subs.add(ItemStack(item, 1, 3)) // tank
		subs.add(ItemStack(item, 1, 4)) // ninja
	}
	
	override fun onBlockActivated(world: World?, x: Int, y: Int, z: Int, player: EntityPlayer?, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		return if (SpellBase.consumeMana(player, 10000, false) && press(world!!, x, y, z, player)) SpellBase.consumeMana(player, 10000, true) else false
	}
	
	fun press(world: World, x: Int, y: Int, z: Int, player: EntityPlayer): Boolean {
		when (world.getBlockMetadata(x, y, z)) {
			1    -> return makePlayerBerserk(player)
			2    -> return makePlayerOvermage(player)
			3    -> return makePlayerTank(player)
			4    -> return makePlayerNinja(player)
			else -> return false
		}
	}
	
	// +20% DMG, -20% HP
	private fun makePlayerBerserk(player: EntityPlayer): Boolean {
		if (!player.isPotionActive(AlfheimRegistry.overmage) && !player.isPotionActive(AlfheimRegistry.tank) && !player.isPotionActive(AlfheimRegistry.ninja)) {
			player.addPotionEffect(PotionEffect(AlfheimRegistry.berserk.id, 72000, 0))
			return true
		}
		return false
	}
	
	// +20% Spell DMG, +20% Spell Cost
	private fun makePlayerOvermage(player: EntityPlayer): Boolean {
		if (!player.isPotionActive(AlfheimRegistry.berserk) && !player.isPotionActive(AlfheimRegistry.tank) && !player.isPotionActive(AlfheimRegistry.ninja)) {
			player.addPotionEffect(PotionEffect(AlfheimRegistry.overmage.id, 72000, 0))
			return true
		}
		return false
	}
	
	// +20% Resistance, -20% Speed
	private fun makePlayerTank(player: EntityPlayer): Boolean {
		if (!player.isPotionActive(AlfheimRegistry.overmage) && !player.isPotionActive(AlfheimRegistry.berserk) && !player.isPotionActive(AlfheimRegistry.ninja)) {
			player.addPotionEffect(PotionEffect(AlfheimRegistry.tank.id, 72000, 0))
			return true
		}
		return false
	}
	
	// +20% Speed, -20% DMG
	private fun makePlayerNinja(player: EntityPlayer): Boolean {
		if (!player.isPotionActive(AlfheimRegistry.overmage) && !player.isPotionActive(AlfheimRegistry.tank) && !player.isPotionActive(AlfheimRegistry.berserk)) {
			player.addPotionEffect(PotionEffect(AlfheimRegistry.ninja.id, 72000, 0))
			return true
		}
		return false
	}
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack): LexiconEntry {
		return AlfheimLexiconData.shrines
	}
	
	companion object {
		
		val icons = arrayOfNulls<IIcon>(5)
	}
}