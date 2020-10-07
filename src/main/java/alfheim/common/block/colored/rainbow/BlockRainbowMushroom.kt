package alfheim.common.block.colored.rainbow

import alexsocol.asjlib.*
import alfheim.client.core.helper.InterpolatedIconHelper
import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.block.ItemBlockLeavesMod
import cpw.mods.fml.common.Optional.*
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.passive.EntitySheep
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.*
import net.minecraft.world.*
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.util.ForgeDirection
import thaumcraft.api.crafting.IInfusionStabiliser
import vazkii.botania.api.item.IHornHarvestable
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.common.Botania
import vazkii.botania.common.core.handler.ConfigHandler
import vazkii.botania.common.integration.coloredlights.ColoredLightHelper
import vazkii.botania.common.lexicon.LexiconData
import java.util.*

@Interface(modid = "Thaumcraft", iface = "thaumcraft.api.crafting.IInfusionStabiliser", striprefs = true)
class BlockRainbowMushroom: BlockMushroom(), IInfusionStabiliser, IHornHarvestable, ILexiconable {
	
	var originalLight: Int = 0
	
	init {
		setBlockName("rainbowMushroom")
		setCreativeTab(AlfheimTab)
		setLightLevel(0.2f)
		setHardness(0f)
		setStepSound(Block.soundTypeGrass)
		setBlockBounds(0.3f, 0f, 0.3f, 0.8f, 1f, 0.8f)
		tickRandomly = false
		if (ASJUtilities.isClient)
			MinecraftForge.EVENT_BUS.register(this)
	}
	
	override fun updateTick(world: World, x: Int, y: Int, z: Int, rand: Random) = Unit
	
	override fun canBlockStay(world: World, x: Int, y: Int, z: Int): Boolean {
		return if (y in 0..255) {
			val block = world.getBlock(x, y - 1, z)
			block === Blocks.mycelium || block === Blocks.dirt && world.getBlockMetadata(x, y - 1, z) == 2 || block.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this)
		} else {
			false
		}
	}
	
	override fun getSubBlocks(item: Item, tab: CreativeTabs?, list: MutableList<Any?>) {
		list.add(ItemStack(item))
	}
	
	override fun setBlockName(name: String): Block {
		GameRegistry.registerBlock(this, ItemBlockLeavesMod::class.java, name)
		return super.setBlockName(name)
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerBlockIcons(reg: IIconRegister) = Unit
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun loadTextures(event: TextureStitchEvent.Pre) {
		if (event.map.textureType == 0)
			blockIcon = InterpolatedIconHelper.forBlock(event.map, this)
	}
	
	override fun setLightLevel(lvl: Float): Block {
		originalLight = (lvl * 15).I
		return super.setLightLevel(lvl)
	}
	
	@Method(modid = "easycoloredlights")
	override fun getLightValue(world: IBlockAccess, x: Int, y: Int, z: Int) = ColoredLightHelper.getPackedColor(world.getBlockMetadata(x, y, z), originalLight)
	
	override fun getIcon(side: Int, meta: Int) = blockIcon!!
	
	override fun damageDropped(meta: Int) = meta
	
	override fun randomDisplayTick(world: World, x: Int, y: Int, z: Int, rand: Random) {
		val color = EntitySheep.fleeceColorTable[world.rand.nextInt(16)]
		if (rand.nextDouble() < ConfigHandler.flowerParticleFrequency * 0.25) {
			Botania.proxy.sparkleFX(world, x.D + 0.3 + rand.nextFloat().D * 0.5, y.D + 0.5 + rand.nextFloat().D * 0.5, z.D + 0.3 + rand.nextFloat().D * 0.5, color[0], color[1], color[2], rand.nextFloat(), 5)
		}
	}
	
	override fun canStabaliseInfusion(world: World, x: Int, y: Int, z: Int) = ConfigHandler.enableThaumcraftStablizers
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack) = LexiconData.mushrooms!!
	
	override fun canHornHarvest(world: World, x: Int, y: Int, z: Int, stack: ItemStack, hornType: IHornHarvestable.EnumHornType) = false
	
	override fun hasSpecialHornHarvest(world: World, x: Int, y: Int, z: Int, stack: ItemStack, hornType: IHornHarvestable.EnumHornType) = false
	
	override fun harvestByHorn(world: World, x: Int, y: Int, z: Int, stack: ItemStack, hornType: IHornHarvestable.EnumHornType) = Unit
}