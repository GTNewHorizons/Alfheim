package alfheim.common.block

import alfheim.api.ModInfo
import alfheim.common.block.base.BlockModRotatedPillar
import alfheim.common.item.block.ItemBlockLeavesMod
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class BlockShrinePillar: BlockModRotatedPillar(Material.rock) {
	
	init {
		setBlockName("ShrinePillar")
		setHardness(10f)
		setHarvestLevel("pickaxe", 2)
		setResistance(10000f)
		setStepSound(soundTypeStone)
	}
	
	override fun register(name: String) {
		GameRegistry.registerBlock(this, ItemBlockLeavesMod::class.java, name)
	}
	
	override fun registerBlockIcons(reg: IIconRegister) {
		iconTop = reg.registerIcon(ModInfo.MODID + ":decor/ShrinePillarTop")
		iconSide = reg.registerIcon(ModInfo.MODID + ":decor/ShrinePillar")
	}
	
	override fun getEntry(world: World?, x: Int, y: Int, z: Int, player: EntityPlayer?, stack: ItemStack?) = null
}
