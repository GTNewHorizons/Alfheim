package alfheim.common.block

import alexsocol.asjlib.F
import alfheim.common.block.base.BlockContainerMod
import alfheim.common.block.tile.TileEnderActuator
import alfheim.common.core.util.AlfheimTab
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import vazkii.botania.common.Botania

class BlockEnderActuator: BlockContainerMod(Material.iron) {
	
	init {
		setBlockName("EnderActuator")
		setCreativeTab(AlfheimTab)
		setHardness(5f)
	}
	
	override fun onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		if (!player.isSneaking) return false
		val tile = world.getTileEntity(x, y, z) as? TileEnderActuator ?: return false
		
		tile.name = player.commandSenderName
		
		if (world.isRemote) {
			for (i in 0 until 30) {
				val x1 = (x + Math.random())
				val y1 = (y + Math.random())
				val z1 = (z + Math.random())
				Botania.proxy.wispFX(world, x1, y1, z1, 0.25f + Math.random().F * 0.25f, 0f, 1f, Math.random().toFloat() * 0.5f, -0.05f + Math.random().toFloat() * 0.05f)
			}
		}
		
		return true
	}
	
	override fun createNewTileEntity(world: World?, meta: Int) = TileEnderActuator()
}
