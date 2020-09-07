package alfheim.common.block.colored

import alexsocol.asjlib.ASJUtilities
import alfheim.common.block.base.BlockMod
import alfheim.common.core.helper.InterpolatedIconHelper
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.common.Loader
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.world.*
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.api.lexicon.ILexiconable
import java.awt.Color
import kotlin.math.max

/**
 * @author WireSegal
 * Created at 4:44 PM on 1/12/16.
 */
class BlockColoredLamp: BlockMod(Material.redstoneLight), ILexiconable {
	
	var rainbowIcon: IIcon? = null
	
	init {
		setBlockName("irisLamp")
		setStepSound(soundTypeGlass)
		setHardness(0.3f)
		if (ASJUtilities.isClient)
			MinecraftForge.EVENT_BUS.register(this)
		
	}
	
	companion object {
		fun powerLevel(world: IBlockAccess, x: Int, y: Int, z: Int): Int {
			var ret = getBlockPowerLevel(world, x, y, z)
			for (dir in ForgeDirection.VALID_DIRECTIONS) {
				val power = getBlockPowerLevel(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ)
				ret = max(ret, power)
			}
			return ret
		}
		
		fun getBlockPowerLevel(world: IBlockAccess, x: Int, y: Int, z: Int): Int {
			val block = world.getBlock(x, y, z)
			return if (block.shouldCheckWeakPower(world, x, y, z, 0)) blockProvidingPower(world, x, y, z) else block.isProvidingWeakPower(world, x, y, z, 0)
		}
		
		fun blockProvidingPower(world: IBlockAccess, x: Int, y: Int, z: Int): Int {
			val b0 = 0
   
			var l = max(b0, world.isBlockProvidingPowerTo(x, y - 1, z, 0))
			if (l >= 15) return l
			
            l = max(l, world.isBlockProvidingPowerTo(x, y + 1, z, 1))
            if (l >= 15) return l
            
            l = max(l, world.isBlockProvidingPowerTo(x, y, z - 1, 2))
            if (l >= 15) return l
            
            l = max(l, world.isBlockProvidingPowerTo(x, y, z + 1, 3))
            if (l >= 15) return l
            
            l = max(l, world.isBlockProvidingPowerTo(x - 1, y, z, 4))
            if (l >= 15) return l
            
            l = max(l, world.isBlockProvidingPowerTo(x + 1, y, z, 5))
            return if (l >= 15) l else l
        }
		
		fun powerColor(power: Int): Int {
			if (power == 0) return 0x191616
			else if (power == 15) return 0xFFFFFF
			return Color.HSBtoRGB((power - 1) / 16F, 1F, 1F)
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	override fun loadTextures(event: TextureStitchEvent.Pre) {
		if (event.map.textureType == 0)
			rainbowIcon = InterpolatedIconHelper.forBlock(event.map, this, "RB")
	}
	
	override fun getIcon(side: Int, meta: Int) = if (meta > 14) rainbowIcon else blockIcon
	
	override fun onNeighborBlockChange(world: World, x: Int, y: Int, z: Int, block: Block) {
		super.onNeighborBlockChange(world, x, y, z, block)
		val lvl = powerLevel(world, x, y, z)
		if (world.getBlockMetadata(x, y, z) != lvl)
			world.setBlockMetadataWithNotify(x, y, z, lvl, 3)
	}
	
	override fun onBlockAdded(world: World, x: Int, y: Int, z: Int) {
		super.onBlockAdded(world, x, y, z)
		val lvl = powerLevel(world, x, y, z)
		if (world.getBlockMetadata(x, y, z) != lvl)
			world.setBlockMetadataWithNotify(x, y, z, lvl, 3)
	}
	
	override fun damageDropped(meta: Int) = 0
 
	override fun getPickBlock(target: MovingObjectPosition?, world: World, x: Int, y: Int, z: Int) = ItemStack(this, 1, 0)
 
	override fun createStackedBlock(meta: Int) = ItemStack(this, 1, 0)
	
	override fun getLightValue() = 15
 
	override fun getLightValue(world: IBlockAccess, x: Int, y: Int, z: Int): Int {
		val level = powerLevel(world, x, y, z)
		if (Loader.isModLoaded("easycoloredlights")) {
			return if (level == 15) 0xFFFFFF else powerColor(level)
		}
		return if (level > 0) 15 else 0
	}
	
	@SideOnly(Side.CLIENT)
	override fun getRenderColor(meta: Int): Int = powerColor(meta)
	
	@SideOnly(Side.CLIENT)
	override fun colorMultiplier(world: IBlockAccess, x: Int, y: Int, z: Int): Int = getRenderColor(world.getBlockMetadata(x, y, z))
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = AlfheimLexiconData.lamp
}
