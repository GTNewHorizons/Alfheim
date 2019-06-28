package alfheim.common.block

import alexsocol.asjlib.render.*
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.common.core.handler.CardinalSystem.KnowledgeSystem
import alfheim.common.core.handler.CardinalSystem.KnowledgeSystem.Knowledge
import alfheim.common.core.registry.AlfheimBlocks
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.relauncher.*
import net.minecraft.block.BlockLeaves
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.*
import net.minecraft.init.*
import net.minecraft.item.*
import net.minecraft.util.IIcon
import net.minecraft.world.*
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.api.lexicon.*
import vazkii.botania.common.item.ModItems
import java.util.*

class BlockDreamLeaves: BlockLeaves(), IGlowingLayerBlock, ILexiconable {
	
	val textures = arrayOfNulls<IIcon>(3)
	
	init {
		this.setBlockName("DreamLeaves")
		this.setBlockTextureName(ModInfo.MODID + ":DreamLeaves")
		this.setCreativeTab(AlfheimCore.alfheimTab)
		this.setLightOpacity(0)
	}
	
	override// IDK whether this is good source of glowstone or not
	fun onBlockActivated(world: World?, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
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
	
	override fun isLeaves(world: IBlockAccess?, x: Int, y: Int, z: Int): Boolean {
		return false
	}
	
	override fun isOpaqueCube(): Boolean {
		return Blocks.leaves.isOpaqueCube
	}
	
	override fun getItemDropped(meta: Int, rand: Random?, p_149650_3_: Int): Item {
		return Item.getItemFromBlock(AlfheimBlocks.dreamSapling)
	}
	
	@SideOnly(Side.CLIENT)
	override fun getRenderColor(meta: Int): Int {
		return Integer.parseInt("E5FFF9", 16)
	}
	
	@SideOnly(Side.CLIENT)
	override fun colorMultiplier(world: IBlockAccess?, x: Int, y: Int, z: Int): Int {
		return Integer.parseInt("E5FFF9", 16)
	}
	
	public override fun func_150124_c(world: World?, x: Int, y: Int, z: Int, meta: Int, chance: Int) {}
	
	override fun func_150123_b(meta: Int): Int {
		return 100
	}
	
	override fun getIcon(side: Int, meta: Int): IIcon {
		return textures[if (Blocks.leaves.isOpaqueCube) 1 else 0]!!
	}
	
	override fun getGlowIcon(side: Int, meta: Int): IIcon {
		return textures[2]!!
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerBlockIcons(reg: IIconRegister) {
		textures[0] = reg.registerIcon(ModInfo.MODID + ":DreamLeaves")
		textures[1] = reg.registerIcon(ModInfo.MODID + ":DreamLeavesOpaque")
		textures[2] = reg.registerIcon(ModInfo.MODID + ":DreamSparks")
	}
	
	override fun getRenderType(): Int {
		return RenderGlowingLayerBlock.glowBlockID
	}
	
	override fun func_150125_e(): Array<String> {
		return arrayOf("dream")
	}
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack): LexiconEntry {
		return AlfheimLexiconData.worldgen
	}
}