package alfheim.common.block

import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.relauncher.*
import net.minecraft.block.BlockLog
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraft.world.World
import vazkii.botania.api.lexicon.*

class BlockDreamLog: BlockLog(), ILexiconable {
	
	val textures = arrayOfNulls<IIcon>(2)
	
	init {
		this.setBlockName("DreamLog")
		this.setCreativeTab(AlfheimCore.alfheimTab)
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerBlockIcons(reg: IIconRegister) {
		textures[0] = reg.registerIcon(ModInfo.MODID + ":DreamLogTop")
		textures[1] = reg.registerIcon(ModInfo.MODID + ":DreamLogSide")
	}
	
	@SideOnly(Side.CLIENT)
	override fun getSideIcon(meta: Int): IIcon {
		return textures[1]!!
	}
	
	@SideOnly(Side.CLIENT)
	override fun getTopIcon(meta: Int): IIcon {
		return textures[0]!!
	}
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack): LexiconEntry {
		return AlfheimLexiconData.worldgen
	}
}
