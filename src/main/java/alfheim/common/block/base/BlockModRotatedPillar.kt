package alfheim.common.block.base

import alfheim.common.core.helper.*
import alfheim.common.core.util.toItem
import alfheim.common.item.block.ItemIridescentBlockMod
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.item.*
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.client.event.TextureStitchEvent
import vazkii.botania.api.lexicon.ILexiconable
import java.util.*

abstract class BlockModRotatedPillar(mat: Material): BlockMod(mat), ILexiconable {
	
	protected var iconTop: IIcon? = null
	protected var iconSide: IIcon? = null
	
	override fun setBlockName(name: String): Block {
		register(name)
		return super.setBlockName(name)
	}
	
	open fun register(name: String) {
		GameRegistry.registerBlock(this, ItemIridescentBlockMod::class.java, name)
	}
	
	override fun getItemDropped(meta: Int, random: Random, fortune: Int) = this.toItem()
	
	@SideOnly(Side.CLIENT)
	override fun getIcon(side: Int, meta: Int): IIcon? {
		val k = meta and 12
		return if (k == 0 && (side == 1 || side == 0)) getTopIcon(meta and 3) else (if (k == 4 && (side == 5 || side == 4)) getTopIcon(meta and 3) else (if (k == 8 && (side == 2 || side == 3)) getTopIcon(meta and 3) else getSideIcon(meta and 3)))
	}
	
	override fun quantityDropped(random: Random) = 1
	
	override fun shouldRegisterInNameSet() = false
	
	override fun damageDropped(meta: Int) = meta and 3
	
	override fun getRenderType() = 31
	
	@SideOnly(Side.CLIENT)
	open fun getSideIcon(meta: Int) = iconSide
	
	@SideOnly(Side.CLIENT)
	open fun getTopIcon(meta: Int) = iconTop
	
	override fun createStackedBlock(meta: Int) = ItemStack(this, 1, meta and 3)
	
	override fun onBlockPlaced(world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float, meta: Int): Int {
		val j1 = meta and 3
		var b0 = 0
		
		when (side) {
			0, 1 -> b0 = 0
			2, 3 -> b0 = 8
			4, 5 -> b0 = 4
		}
		
		return j1 or b0
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerBlockIcons(reg: IIconRegister) {
		if (!isInterpolated()) {
			iconTop = IconHelper.forBlock(reg, this, "Top")
			iconSide = IconHelper.forBlock(reg, this, "Side")
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	override fun loadTextures(event: TextureStitchEvent.Pre) {
		if (event.map.textureType == 0 && isInterpolated()) {
			iconTop = InterpolatedIconHelper.forBlock(event.map, this, "Top")
			iconSide = InterpolatedIconHelper.forBlock(event.map, this, "Side")
		}
	}
	
	override fun getPickBlock(target: MovingObjectPosition?, world: World, x: Int, y: Int, z: Int): ItemStack {
		val meta = world.getBlockMetadata(x, y, z)
		return ItemStack(this, 1, meta and 3)
	}
}
