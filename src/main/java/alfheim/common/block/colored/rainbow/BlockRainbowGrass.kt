package alfheim.common.block.colored.rainbow

import alexsocol.asjlib.*
import alfheim.client.core.helper.InterpolatedIconHelper
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.colored.BlockAuroraDirt
import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.*
import alfheim.common.item.block.ItemRainbowGrassMod
import alfheim.common.item.material.ElvenResourcesMetas
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.IIcon
import net.minecraft.world.*
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.MinecraftForge
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.common.Botania
import vazkii.botania.common.achievement.*
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.core.handler.ConfigHandler
import vazkii.botania.common.lexicon.LexiconData
import java.awt.Color
import java.util.*

class BlockRainbowGrass: BlockTallGrass(), ILexiconable, IPickupAchievement {
	
	var flowerIcon: IIcon? = null
	var glowingIcon: IIcon? = null
	
	companion object {
		
		const val GRASS = 0
		const val AURORA = 1
		const val FLOWER = 2
		const val GLIMMER = 3
		const val BURIED = 4
	}
	
	init {
		setBlockName("rainbowGrass")
		setCreativeTab(AlfheimTab)
		setStepSound(Block.soundTypeGrass)
		if (ASJUtilities.isClient)
			MinecraftForge.EVENT_BUS.register(this)
	}
	
	override fun setBlockBoundsBasedOnState(world: IBlockAccess, x: Int, y: Int, z: Int) {
		when (world.getBlockMetadata(x, y, z)) {
			GRASS, AURORA   -> setBlockBounds(0.1f, 0f, 0.1f, 0.9f, 0.8f, 0.9f)
			FLOWER, GLIMMER -> setBlockBounds(0.3f, 0f, 0.3f, 0.8f, 1f, 0.8f)
			BURIED          -> setBlockBounds(0f, 0f, 0f, 1f, 0.1f, 1f)
			else            -> setBlockBounds(0f, 0f, 0f, 1f, 1f, 1f)
		}
	}
	
	override fun randomDisplayTick(world: World, x: Int, y: Int, z: Int, rand: Random) {
		val meta = world.getBlockMetadata(x, y, z)
		val color = Color(ItemIridescent.rainbowColor())
		
		Botania.proxy.setSparkleFXNoClip(true)
		
		when (meta) {
			FLOWER, GLIMMER -> {
				if (rand.nextDouble() < ConfigHandler.flowerParticleFrequency)
					Botania.proxy.sparkleFX(world, x.D + 0.3 + rand.nextFloat() * 0.5, y.D + 0.5 + rand.nextFloat() * 0.5, z.D + 0.3 + rand.nextFloat() * 0.5, color.red / 255f, color.green / 255f, color.blue / 255f, rand.nextFloat(), 5)
			}
			
			BURIED          -> Botania.proxy.sparkleFX(world, x.D + 0.3 + rand.nextFloat() * 0.5, y.D + 0.1 + rand.nextFloat() * 0.1, z.D + 0.3 + rand.nextFloat() * 0.5, color.red / 255f, color.green / 255f, color.blue / 255f, rand.nextFloat(), 5)
		}
		
		Botania.proxy.setSparkleFXNoClip(false)
	}
	
	override fun getLightValue(world: IBlockAccess, x: Int, y: Int, z: Int) = when (world.getBlockMetadata(x, y, z)) {
		GLIMMER -> 15
		BURIED  -> 3
		else    -> 0
	}
	
	@SideOnly(Side.CLIENT)
	override fun getBlockColor() = 0xFFFFFF
	
	@SideOnly(Side.CLIENT)
	override fun getRenderColor(meta: Int) = 0xFFFFFF
	
	@SideOnly(Side.CLIENT)
	override fun colorMultiplier(world: IBlockAccess, x: Int, y: Int, z: Int) = if (world.getBlockMetadata(x, y, z) == AURORA) BlockAuroraDirt.getBlockColor(x, y, z) else 0xFFFFFF
	
	override fun func_149851_a(world: World, x: Int, y: Int, z: Int, remote: Boolean): Boolean {
		val meta = world.getBlockMetadata(x, y, z)
		return meta == GRASS || meta == AURORA || meta == FLOWER || meta == BURIED
	}
	
	override fun func_149853_b(world: World, random: Random, x: Int, y: Int, z: Int) {
		var meta = world.getBlockMetadata(x, y, z)
		if (meta == GRASS || meta == AURORA || meta == FLOWER || meta == BURIED) {
			var block = AlfheimBlocks.rainbowTallGrass
			
			if (meta == FLOWER || meta == BURIED) {
				block = AlfheimBlocks.rainbowTallFlower
				meta = FLOWER
			}
			
			if (block.canPlaceBlockAt(world, x, y, z)) {
				world.setBlock(x, y, z, block, meta, 2)
				world.setBlock(x, y + 1, z, block, 8, 2)
			}
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
			GRASS, AURORA -> super.getDrops(world, x, y, z, meta, fortune)
			BURIED        -> arrayListOf(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.RainbowPetal))
			else          -> arrayListOf(ItemStack(this, 1, meta))
		}
	}
	
	override fun getItemDropped(meta: Int, rand: Random?, fortune: Int) = when (meta) {
		GRASS, AURORA -> null
		BURIED        -> AlfheimItems.elvenResource
		else          -> this.toItem()
	}
	
	override fun onSheared(item: ItemStack, world: IBlockAccess, x: Int, y: Int, z: Int, fortune: Int): ArrayList<ItemStack> {
		val ret = ArrayList<ItemStack>()
		ret.add(ItemStack(this, 1, world.getBlockMetadata(x, y, z)))
		return ret
	}
	
	override fun isShearable(item: ItemStack?, world: IBlockAccess, x: Int, y: Int, z: Int) = run {
		val meta = world.getBlockMetadata(x, y, z)
		meta == GRASS || meta == AURORA
	}
	
	override fun getSubBlocks(item: Item?, tab: CreativeTabs?, list: MutableList<Any?>) {
		for (i in 0..3)
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
			AURORA  -> AlfheimBlocks.irisGrass.getIcon(side, 0)
			FLOWER  -> flowerIcon
			GLIMMER -> glowingIcon
			BURIED  -> ModBlocks.buriedPetals.getIcon(side, 0)
			else    -> blockIcon
		}
	}
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer?, stack: ItemStack?) =
		when (world.getBlockMetadata(x, y, z)) {
			GRASS, AURORA  -> AlfheimLexiconData.pastoralSeeds
			FLOWER, BURIED -> AlfheimLexiconData.rainbowFlora
			GLIMMER        -> LexiconData.shinyFlowers
			else           -> null
		}
	
	override fun getAchievementOnPickup(stack: ItemStack, player: EntityPlayer?, item: EntityItem?) = if (stack.meta == FLOWER) ModAchievements.flowerPickup else null
}