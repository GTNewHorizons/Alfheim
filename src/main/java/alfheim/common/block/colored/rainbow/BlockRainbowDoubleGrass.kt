package alfheim.common.block.colored.rainbow

import alexsocol.asjlib.*
import alfheim.api.lib.LibRenderIDs
import alfheim.client.core.helper.InterpolatedIconHelper
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.base.IDoublePlant
import alfheim.common.block.colored.BlockAuroraDirt
import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.block.ItemRainbowDoubleGrassMod
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.*
import net.minecraft.item.*
import net.minecraft.stats.StatList
import net.minecraft.util.IIcon
import net.minecraft.world.*
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.ForgeEventFactory
import vazkii.botania.api.lexicon.ILexiconable
import java.util.*

class BlockRainbowDoubleGrass: BlockDoublePlant(), ILexiconable, IDoublePlant {
	
	val name = "rainbowDoubleGrass"
	lateinit var topIcon: IIcon
	lateinit var bottomIcon: IIcon
	
	val GRASS = 0
	val AURORA = 1
	
	init {
		setBlockNameSafe(name)
		setCreativeTab(AlfheimTab)
		setStepSound(Block.soundTypeGrass)
		if (ASJUtilities.isClient)
			MinecraftForge.EVENT_BUS.register(this)
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun loadTextures(event: TextureStitchEvent.Pre) {
		if (event.map.textureType == 0) {
			topIcon = InterpolatedIconHelper.forBlock(event.map, this, "Top")!!
			bottomIcon = InterpolatedIconHelper.forBlock(event.map, this)!!
		}
	}
	
	override fun func_149851_a(world: World, x: Int, y: Int, z: Int, isRemote: Boolean) = false
	
	override fun func_149853_b(world: World, random: Random, x: Int, y: Int, z: Int) = Unit
	
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
	
	@SideOnly(Side.CLIENT)
	override fun getRenderColor(meta: Int) = 0xFFFFFF
	
	@SideOnly(Side.CLIENT)
	override fun colorMultiplier(world: IBlockAccess, x: Int, y: Int, z: Int) = run {
		when (world.getBlockMetadata(x, y, z)) {
			AURORA     -> BlockAuroraDirt.getBlockColor(x, y, z)
			AURORA + 8 -> BlockAuroraDirt.getBlockColor(x, y + 1, z)
			else       -> 0xFFFFFF
		}
	}
	
	override fun getSubBlocks(item: Item?, tab: CreativeTabs?, list: MutableList<Any?>) {
		list.add(ItemStack(item, 1, GRASS))
		list.add(ItemStack(item, 1, AURORA))
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerBlockIcons(iconRegister: IIconRegister) = Unit
	
	@SideOnly(Side.CLIENT)
	override fun getIcon(side: Int, meta: Int) = getTopIcon(meta)
	
	@SideOnly(Side.CLIENT)
	override fun func_149888_a(top: Boolean, index: Int) = if (top) getTopIcon(index) else getBottomIcon(index)
	
	override fun harvestBlock(world: World, player: EntityPlayer, x: Int, y: Int, z: Int, meta: Int) {
		if (world.isRemote || player.currentEquippedItem == null || player.currentEquippedItem.item !== Items.shears || func_149887_c(meta)/*|| !dropBlock(world, x, y, z, meta, player)*/) {
			superSuperHarvestBlock(world, player, x, y, z, meta)
		}
	}
	
	// stupid private methods
	fun superSuperHarvestBlock(world: World, player: EntityPlayer, x: Int, y: Int, z: Int, meta: Int) {
		player.addStat(StatList.mineBlockStatArray[id], 1)
		player.addExhaustion(0.025f)
		
		if (this.canSilkHarvest(world, player, x, y, z, meta) && EnchantmentHelper.getSilkTouchModifier(player)) {
			val items = ArrayList<ItemStack>()
			val itemstack = createStackedBlock(meta)
			
			if (itemstack != null)
				items.add(itemstack)
			
			ForgeEventFactory.fireBlockHarvesting(items, world, this, x, y, z, meta, 0, 1f, true, player)
			for (item in items)
				this.dropBlockAsItem(world, x, y, z, item)
		} else {
			harvesters.set(player)
			val i1 = EnchantmentHelper.getFortuneModifier(player)
			this.dropBlockAsItem(world, x, y, z, meta, i1)
			harvesters.set(null)
		}
	}
	
	override fun onBlockHarvested(world: World, x: Int, y: Int, z: Int, meta: Int, player: EntityPlayer) {
		if (func_149887_c(meta)) {
			if (world.getBlock(x, y - 1, z) === this) {
				if (!player.capabilities.isCreativeMode) {
					val i1 = world.getBlockMetadata(x, y - 1, z)
					val j1 = func_149890_d(i1)
					
					if (j1 != 3 && j1 != 2) ;
					//p_149681_1_.func_147480_a(p_149681_2_, p_149681_3_ - 1, p_149681_4_, true)
					else {
						/*if (!world.isRemote && player.currentEquippedItem?.item === Items.shears)
							dropBlock(world, x, y, z, i1, player)*/
						
						world.setBlockToAir(x, y - 1, z)
					}
				} else {
					world.setBlockToAir(x, y - 1, z)
				}
			}
		} else if (player.capabilities.isCreativeMode && world.getBlock(x, y + 1, z) === this) {
			world.setBlock(x, y + 1, z, Blocks.air, 0, 2)
		}
		
		//super.onBlockHarvested(p_149681_1_, p_149681_2_, p_149681_3_, p_149681_4_, p_149681_5_, p_149681_6_)
	}
	
	/*fun dropBlock(world: World, x: Int, y: Int, z: Int, meta: Int, player: EntityPlayer): Boolean {
		val meta = meta and 7
		//return if (func_149887_c(meta)) false
		//else {
		player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(this)], 1)
		val b0 = meta
		this.dropBlockAsItem(world, x, y, z, ItemStack(AlfheimBlocks.irisGrass, 2, b0))
		return true
		//}
	}*/
	
	override fun getItemDropped(meta: Int, random: Random, fortune: Int) = null
	
	override fun onSheared(item: ItemStack, world: IBlockAccess, x: Int, y: Int, z: Int, fortune: Int): ArrayList<ItemStack> {
		val ret = ArrayList<ItemStack>()
		val meta = world.getBlockMetadata(x, y, z)
		if (func_149887_c(meta)) {
			if (y > 0 && world.getBlock(x, y - 1, z) == this) {
				ret.add(ItemStack(AlfheimBlocks.rainbowGrass, 2, world.getBlockMetadata(x, y - 1, z)))
			}
		} else {
			ret.add(ItemStack(AlfheimBlocks.rainbowGrass, 2, meta))
		}
		return ret
	}
	
	override fun getRenderType() = LibRenderIDs.idDoubleFlower
	
	override fun isShearable(item: ItemStack, world: IBlockAccess, x: Int, y: Int, z: Int) = true
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer?, stack: ItemStack?) =
		when (world.getBlockMetadata(x, y, z)) {
			GRASS, AURORA -> AlfheimLexiconData.pastoralSeeds
			else          -> null
		}
	
	override fun getBottomIcon(lowerMeta: Int) =
		when (lowerMeta) {
			AURORA -> (AlfheimBlocks.irisTallGrass0 as IDoublePlant).getBottomIcon(0)
			else   -> bottomIcon
		}
	
	override fun getTopIcon(lowerMeta: Int) =
		when (lowerMeta) {
			AURORA -> (AlfheimBlocks.irisTallGrass0 as IDoublePlant).getTopIcon(0)
			else   -> topIcon
		}
}
