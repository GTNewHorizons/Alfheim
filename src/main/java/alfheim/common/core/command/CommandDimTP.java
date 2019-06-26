package alfheim.common.core.command;

import alexsocol.asjlib.ASJUtilities;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class CommandDimTP extends CommandBase {

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return sender instanceof EntityPlayer;
	}
	
	@Override
	public String getCommandName() {
		return "tpdim";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "alfheim.commands.tpdim.usage";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length == 1 && args[0].matches("-?\\d+") && sender instanceof EntityPlayer) {
			try {
				int id = Integer.valueOf(args[0]);
				try {
					World w = MinecraftServer.getServer().worldServerForDimension(id);
					if (w == null) {
						throw new NullPointerException("Loaded dimension is null");
					}
					
					ChunkCoordinates s = ((EntityPlayer) sender).getBedLocation(id);
					// stupid minecraft returns overworld coordinates in ANY dimension
					if (s == null) s = w.getSpawnPoint();
			   		ASJUtilities.sendToDimensionWithoutPortal((EntityPlayer) sender, id, s.posX, s.posY, s.posZ);
				} catch (Throwable e) {
					throw new WrongUsageException("alfheim.commands.tpdim.worlderr");
				}
			} catch (NumberFormatException nfe) {
				throw new WrongUsageException("alfheim.commands.tpdim.wrongid");
			}
		} else {
			throw new WrongUsageException("alfheim.commands.tpdim.wrong");
		}
	}
}