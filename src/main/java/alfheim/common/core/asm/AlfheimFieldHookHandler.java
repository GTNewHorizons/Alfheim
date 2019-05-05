package alfheim.common.core.asm;

import alexsocol.asjlib.asm.HookField;

public class AlfheimFieldHookHandler {
	
	@HookField(targetClassName = "net.minecraft.entity.Entity")
	public boolean canEntityUpdate;
	
	@HookField(targetClassName = "net.minecraft.tileentity.TileEntity")
	public boolean canTileUpdate;
}
