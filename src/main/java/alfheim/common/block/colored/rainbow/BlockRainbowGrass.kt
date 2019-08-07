package alfheim.common.block.colored.rainbow

import alfheim.AlfheimCore
import alfheim.common.block.ShadowFoxBlocks
import alfheim.common.core.helper.InterpolatedIconHelper
import alfheim.common.item.block.ItemRainbowGrassMod
import alfheim.common.lexicon.ShadowFoxLexiconData
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
import vazkii.botania.api.lexicon.*
import java.util.*

class BlockRainbowGrass: BlockTallGrass(), ILexiconable {
	
	var flowerIcon: IIcon? = null
	var glowingIcon: IIcon? = null
	
	val GRASS = 0
	val FLOWER = 1
	val GLIMMER = 2
	
	init {
		setCreativeTab(AlfheimCore.baTab)
		setStepSound(Block.soundTypeGrass)
		if (FMLLaunchHandler.side().isClient)
			MinecraftForge.EVENT_BUS.register(this)
		setBlockName("rainbowGrass")
	}
	
	override fun getLightValue(world: IBlockAccess, x: Int, y: Int, z: Int) = if (world.getBlockMetadata(x, y, z) == 2) 15 else 0
	
	@SideOnly(Side.CLIENT)
	override fun getBlockColor() = 0xFFFFFF
	
	@SideOnly(Side.CLIENT)
	override fun getRenderColor(meta: Int) = 0xFFFFFF
	
	@SideOnly(Side.CLIENT)
	override fun colorMultiplier(world: IBlockAccess?, x: Int, y: Int, z: Int) = 0xFFFFFF
	
	override fun func_149851_a(world: World, x: Int, y: Int, z: Int, remote: Boolean): Boolean {
		val meta = world.getBlockMetadata(x, y, z)
		return meta == GRASS || meta == FLOWER
	}
	
	override fun func_149853_b(world: World, random: Random, x: Int, y: Int, z: Int) {
		val meta = world.getBlockMetadata(x, y, z)
		if (meta != GRASS && meta != FLOWER) return
		if (ShadowFoxBlocks.rainbowTallGrass.canPlaceBlockAt(world, x, y, z)) {
			world.setBlock(x, y, z, ShadowFoxBlocks.rainbowTallGrass, meta, 2)
			world.setBlock(x, y + 1, z, ShadowFoxBlocks.rainbowTallGrass, 8, 2)
		}
	}
	
	internal fun register(name: String) {
		GameRegistry.registerBlock(this, ItemRainbowGrassMod::class.java, name)
	}
	
	override fun setBlockName(par1Str: String): Block {
		register(par1Str)
		return super.setBlockName(par1Str)
	}
	
	override fun getDrops(world: World?, x: Int, y: Int, z: Int, meta: Int, fortune: Int): ArrayList<ItemStack>? {
		return when (meta) {
			GRASS -> super.getDrops(world, x, y, z, meta, fortune)
			else  -> arrayListOf(ItemStack(this, 1, meta))
		}
	}
	
	override fun getItemDropped(meta: Int, rand: Random?, fortune: Int) =
		if (meta == GRASS) null else Item.getItemFromBlock(this)
	
	override fun onSheared(item: ItemStack, world: IBlockAccess, x: Int, y: Int, z: Int, fortune: Int): ArrayList<ItemStack> {
		val ret = ArrayList<ItemStack>()
		ret.add(ItemStack(this))
		return ret
	}
	
	override fun isShearable(item: ItemStack?, world: IBlockAccess?, x: Int, y: Int, z: Int) = item?.itemDamage == GRASS
	
	override fun getSubBlocks(item: Item?, tab: CreativeTabs?, list: MutableList<Any?>) {
		for (i in 0..2)
			list.add(ItemStack(item, 1, i))
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerBlockIcons(iconRegister: IIconRegister) = Unit
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun loadTextures(event: TextureStitchEvent.Pre) {
		if (event.map.textureType == 0) {
			blockIcon = InterpolatedIconHelper.forBlock(event.map, this)
			flowerIcon = InterpolatedIconHelper.forBlock(event.map, this, "Flower")
			glowingIcon = InterpolatedIconHelper.forBlock(event.map, this, "FlowerGlimmer")
		}
	}
	
	@SideOnly(Side.CLIENT)
	override fun getIcon(side: Int, meta: Int): IIcon? {
		return when (meta) {
			GRASS   -> blockIcon
			FLOWER  -> flowerIcon
			GLIMMER -> glowingIcon
			else    -> null
		}
	}
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?): LexiconEntry? {
		return when (p5?.itemDamage) {
			GRASS   -> ShadowFoxLexiconData.pastoralSeeds
			FLOWER  -> null // todo
			GLIMMER -> null // todo
			else    -> null
		}
	}
}