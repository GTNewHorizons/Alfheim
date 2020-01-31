package alfheim.common.block.alt

import alfheim.api.lib.LibOreDict.ALT_TYPES
import alfheim.common.block.base.BlockMod
import alfheim.common.block.material.MaterialCustomSmeltingWood
import alfheim.common.core.helper.*
import alfheim.common.item.block.ItemUniqueSubtypedBlockMod
import alfheim.common.lexicon.ShadowFoxLexiconData
import cpw.mods.fml.common.IFuelHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.*
import net.minecraft.world.*
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.api.lexicon.ILexiconable
import java.util.*

class BlockAltPlanks: BlockMod(MaterialCustomSmeltingWood.instance), ILexiconable, IFuelHandler {
	
	val name = "altPlanks"
	var icons: Array<IIcon?> = emptyArray()
	
	init {
		blockHardness = 2F
		setLightLevel(0f)
		stepSound = soundTypeWood
		
		setBlockName(name)
		GameRegistry.registerFuelHandler(this)
		if (FMLLaunchHandler.side().isClient)
			MinecraftForge.EVENT_BUS.register(this)
	}
	
	override fun registerBlockIcons(par1IconRegister: IIconRegister) {
		icons = Array(6) { i ->
			if (i == 3) null else IconHelper.forBlock(par1IconRegister, this, ALT_TYPES[i])
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	override fun loadTextures(event: TextureStitchEvent.Pre) {
		if (event.map.textureType == 0) {
			icons[3] = InterpolatedIconHelper.forBlock(event.map, this, ALT_TYPES[3])
		}
	}
	
	override fun getIcon(side: Int, meta: Int) = icons[meta % 6]
	
	override fun isToolEffective(type: String?, metadata: Int) = (type != null && type == "axe")
	
	override fun getHarvestTool(metadata: Int) = "axe"
	
	override fun shouldRegisterInNameSet() = false
	
	override fun damageDropped(par1: Int) = par1
	
	override fun setBlockName(name: String): Block {
		register(name)
		return super.setBlockName(name)
	}
	
	override fun quantityDropped(random: Random) = 1
	
	override fun getItemDropped(meta: Int, random: Random, fortune: Int) = Item.getItemFromBlock(this)!!
	
	override fun isFlammable(world: IBlockAccess?, x: Int, y: Int, z: Int, face: ForgeDirection?) = false
	
	override fun getFireSpreadSpeed(world: IBlockAccess?, x: Int, y: Int, z: Int, face: ForgeDirection?) = 0
	
	internal fun register(name: String) {
		GameRegistry.registerBlock(this, ItemUniqueSubtypedBlockMod::class.java, name)
	}
	
	override fun getSubBlocks(item: Item?, tab: CreativeTabs?, list: MutableList<Any?>?) {
		if (list != null && item != null)
			for (i in 0..5) {
				list.add(ItemStack(item, 1, i))
			}
	}
	
	override fun getPickBlock(target: MovingObjectPosition?, world: World, x: Int, y: Int, z: Int): ItemStack {
		val meta = world.getBlockMetadata(x, y, z)
		return ItemStack(this, 1, meta)
	}
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) =
		ShadowFoxLexiconData.irisSapling
	
	override fun getBurnTime(fuel: ItemStack) = if (fuel.item == Item.getItemFromBlock(this)) 300 else 0
}
