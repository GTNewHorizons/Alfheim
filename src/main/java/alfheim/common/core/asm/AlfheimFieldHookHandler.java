package alfheim.common.core.asm;

import alexsocol.asjlib.asm.HookField;

class AlfheimFieldHookHandler {
	
	@HookField(targetClassName = "net.minecraft.entity.Entity")
	public boolean canEntityUpdate = false;
	
	@HookField(targetClassName = "net.minecraft.tileentity.TileEntity")
	public boolean canTileUpdate = false;
}
