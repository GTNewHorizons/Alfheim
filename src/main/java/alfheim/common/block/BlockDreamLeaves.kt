package alfheim.common.block

import alexsocol.asjlib.render.*
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.common.block.base.BlockLeavesMod
import alfheim.common.core.handler.CardinalSystem.KnowledgeSystem
import alfheim.common.core.handler.CardinalSystem.KnowledgeSystem.Knowledge
import alfheim.common.core.helper.IconHelper
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.*
import net.minecraft.init.*
import net.minecraft.item.*
import net.minecraft.util.IIcon
import net.minecraft.world.*
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.common.item.ModItems
import java.util.*

class BlockDreamLeaves: BlockLeavesMod(), IGlowingLayerBlock {
	
	lateinit var textures: Array<IIcon>
	
	init {
		setBlockName("DreamLeaves")
		setBlockTextureName(ModInfo.MODID + ":DreamLeaves")
		setCreativeTab(AlfheimCore.alfheimTab)
		setLightOpacity(0)
	}
	
	// IDK whether this is good source of glowstone or not
	override fun onBlockActivated(world: World?, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		if (player.currentEquippedItem != null && player.currentEquippedItem.item === ModItems.manaResource && player.currentEquippedItem.itemDamage == 9) {
			var eat = 2
			val sides = BooleanArray(6)
			
			for (dir in ForgeDirection.values()) {
				if (dir == ForgeDirection.UNKNOWN || eat <= 0) continue
				if (world!!.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) === this) {
					--eat
					sides[dir.ordinal] = true
				}
			}
			
			if (eat > 0) return false
			for (dir in ForgeDirection.values()) {
				if (dir == ForgeDirection.UNKNOWN) continue
				if (sides[dir.ordinal]) world!!.setBlockToAir(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ)
			}
			
			world!!.setBlockToAir(x, y, z)
			
			player.currentEquippedItem.stackSize--
			if (!player.inventory.addItemStackToInventory(ItemStack(Items.glowstone_dust))) {
				player.dropPlayerItemWithRandomChoice(ItemStack(Items.glowstone_dust), true)
			}
			
			if (!world.isRemote && player is EntityPlayerMP) KnowledgeSystem.learn(player, Knowledge.GLOWSTONE)
			
			return true
		}
		return false
	}
	
	override fun func_150123_b(meta: Int) = 100
	override fun quantityDropped(random: Random) = if (random.nextInt(func_150123_b(0)) == 0) 1 else 0
	override fun decayBit() = 0x8
	override fun isLeaves(world: IBlockAccess, x: Int, y: Int, z: Int) = true
	override fun isOpaqueCube() = Blocks.leaves.isOpaqueCube
	override fun getItemDropped(meta: Int, rand: Random, fortune: Int) = Item.getItemFromBlock(AlfheimBlocks.dreamSapling)!!
	@SideOnly(Side.CLIENT)
	override fun getRenderColor(meta: Int) = Integer.parseInt("E5FFF9", 16)
	@SideOnly(Side.CLIENT)
	override fun colorMultiplier(world: IBlockAccess, x: Int, y: Int, z: Int) = Integer.parseInt("E5FFF9", 16)
	override fun getIcon(side: Int, meta: Int) = textures[if (Blocks.leaves.isOpaqueCube) 1 else 0]
	override fun getGlowIcon(side: Int, meta: Int) = textures[2]
	
	@SideOnly(Side.CLIENT)
	override fun registerBlockIcons(reg: IIconRegister) {
		textures = Array(3) { IconHelper.forBlock(reg, this, it) }
	}
	
	override fun getRenderType() = RenderGlowingLayerBlock.glowBlockID
	override fun func_150125_e() = arrayOf("dream")
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack) = AlfheimLexiconData.worldgen
}