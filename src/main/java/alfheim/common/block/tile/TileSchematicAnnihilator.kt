package alfheim.common.block.tile

import net.minecraft.entity.player.EntityPlayer

class TileSchematicAnnihilator : TileSchema() {
    
    override fun blockActivated(p0: EntityPlayer?) {
        if (pos_x != null && pos_y != null && pos_z != null) {
            for (x in xCoord re pos_x!!.x) {
                for (y in yCoord re pos_y!!.y) {
                    for (z in zCoord re pos_z!!.z) {
                        worldObj.setBlockToAir(x, y, z)
                    }
                }
            }
        }
    }
}
