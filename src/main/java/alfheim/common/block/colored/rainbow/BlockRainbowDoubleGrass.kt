package alfheim.common.block.colored.rainbow

import alfheim.AlfheimCore
import alfheim.api.lib.LibRenderIDs
import alfheim.common.block.ShadowFoxBlocks
import alfheim.common.block.base.IDoublePlant
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.IIcon
import net.minecraft.world.*
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.MinecraftForge
import alfheim.common.item.block.ItemRainbowDoubleGrassMod
import alfheim.common.lexicon.ShadowFoxLexiconData
import alfheim.common.core.helper.InterpolatedIconHelper
import vazkii.botania.api.lexicon.ILexiconable
import java.util.*

class BlockRainbowDoubleGrass: BlockDoublePlant(), ILexiconable, IDoublePlant {
	
	val name = "rainbowDoubleGrass"
	var topIcon: IIcon? = null
	var bottomIcon: IIcon? = null
	
	var topFlowerIcon: IIcon? = null
	var bottomFlowerIcon: IIcon? = null
	
	init {
		setCreativeTab(AlfheimCore.baTab)
		setStepSound(Block.soundTypeGrass)
		if (FMLLaunchHandler.side().isClient)
			MinecraftForge.EVENT_BUS.register(this)
		setBlockNameSafe(name)
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun loadTextures(event: TextureStitchEvent.Pre) {
		if (event.map.textureType == 0) {
			topIcon = InterpolatedIconHelper.forBlock(event.map, this, "Top")!!
			bottomIcon = InterpolatedIconHelper.forBlock(event.map, this)!!
			topFlowerIcon = InterpolatedIconHelper.forBlock(event.map, this, "FlowerTop")!!
			bottomFlowerIcon = InterpolatedIconHelper.forBlock(event.map, this, "Flower")!!
		}
	}
	
	override fun func_149851_a(world: World, x: Int, y: Int, z: Int, isRemote: Boolean) = false
	
	override fun func_149853_b(world: World, random: Random, x: Int, y: Int, z: Int) {
	}
	
	fun isTop(meta: Int) = (meta and 8) != 0
	
	internal fun register(name: String) {
		GameRegistry.registerBlock(this, ItemRainbowDoubleGrassMod::class.java, name)
	}
	
	fun setBlockNameSafe(par1Str: String): Block {
		register(par1Str)
		return super.setBlockName(par1Str)
	}
	
	override fun setBlockName(par1Str: String) = null
	
	@SideOnly(Side.CLIENT)
	override fun getBlockColor() = 0xFFFFFF
	
	override fun getSubBlocks(item: Item?, tab: CreativeTabs?, list: MutableList<Any?>) {
		list.add(ItemStack(item))
		list.add(ItemStack(item, 1, 1))
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerBlockIcons(iconRegister: IIconRegister) = Unit
	
	@SideOnly(Side.CLIENT)
	override fun getIcon(side: Int, meta: Int) = getTopIcon(meta)
	
	@SideOnly(Side.CLIENT)
	override fun func_149888_a(top: Boolean, index: Int) = if (top) getTopIcon(index) else getBottomIcon(index)
	
	override fun getItemDropped(meta: Int, random: Random, fortune: Int) = null
	
	override fun onSheared(item: ItemStack, world: IBlockAccess, x: Int, y: Int, z: Int, fortune: Int): ArrayList<ItemStack> {
		val ret = ArrayList<ItemStack>()
		val meta = world.getBlockMetadata(x, y, z)
		if (isTop(meta)) {
			if (y > 0 && world.getBlock(x, y - 1, z) == this) {
				val downMeta = world.getBlockMetadata(x, y - 1, z)
				if (downMeta == 1)
					ret.add(ItemStack(this, 1, 1))
				else
					ret.add(ItemStack(ShadowFoxBlocks.irisGrass, 2, downMeta))
			}
		} else {
			if (meta == 1)
				ret.add(ItemStack(this, 1, 1))
			else
				ret.add(ItemStack(ShadowFoxBlocks.irisGrass, 2, meta))
		}
		return ret
	}
	
	override fun getRenderType() = LibRenderIDs.idDoubleFlower
	
	override fun isShearable(item: ItemStack, world: IBlockAccess, x: Int, y: Int, z: Int) = true
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = ShadowFoxLexiconData.pastoralSeeds
	
	override fun getBottomIcon(lowerMeta: Int) = if (lowerMeta == 0) bottomIcon else bottomFlowerIcon
	
	override fun getTopIcon(lowerMeta: Int) = if (lowerMeta == 0) topIcon else topFlowerIcon
}
