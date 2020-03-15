package alfheim.common.block

import alfheim.common.block.base.BlockModRotatedPillar
import alfheim.common.item.block.ItemBlockMod
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class BlockYggLog: BlockModRotatedPillar(Material.wood) {
	
	init {
		setBlockName("YggdrasilLog")
		setBlockUnbreakable()
		setStepSound(soundTypeWood)
	}
	
	override fun register(name: String) {
		GameRegistry.registerBlock(this, ItemBlockMod::class.java, name)
	}
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = null
}
