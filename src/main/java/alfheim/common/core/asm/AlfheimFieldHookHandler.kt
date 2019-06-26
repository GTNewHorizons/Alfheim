package alfheim.common.core.asm

import alexsocol.asjlib.asm.HookField

class AlfheimFieldHookHandler {
	
	@HookField(targetClassName = "net.minecraft.entity.Entity")
	var canEntityUpdate: Boolean = false
	
	@HookField(targetClassName = "net.minecraft.tileentity.TileEntity")
	var canTileUpdate: Boolean = false
}
