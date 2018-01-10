package alfheim.common.core.command;

import java.util.ArrayList;
import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alfheim.Constants;
import alfheim.common.core.utils.AlfheimConfig;
import alfheim.common.entity.EnumRace;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;

public class CommandRace extends CommandBase {

	@Override
	public int getRequiredPermissionLevel() {
        return 0;
    }
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return sender instanceof EntityPlayer && EnumRace.getRace((EntityPlayer) sender) == EnumRace.HUMAN;
	}
	
	@Override
	public String getCommandName() {
		return "race";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "elvenstory.commands.race.usage";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length == 1 && sender instanceof EntityPlayer) {
            EnumRace r = EnumRace.fromString(args[0]);
            if (r == null || r == EnumRace.ALV || r == EnumRace.HUMAN) throw new WrongUsageException(getCommandUsage(sender), new Object[0]);
            if (EnumRace.getRace((EntityPlayer) sender) == EnumRace.HUMAN) {
            	((EntityPlayer) sender).getEntityAttribute(Constants.RACE).setBaseValue(r.ordinal());
            	((EntityPlayer) sender).capabilities.allowFlying = true;
            	((EntityPlayer) sender).setSpawnChunk(new ChunkCoordinates(MathHelper.floor_double(AlfheimConfig.zones[r.ordinal()].xCoord), MathHelper.floor_double(AlfheimConfig.zones[r.ordinal()].yCoord), MathHelper.floor_double(AlfheimConfig.zones[r.ordinal()].zCoord)), true, AlfheimConfig.dimensionIDAlfheim);
            	ASJUtilities.sendToDimensionWithoutPortal((EntityPlayer) sender, AlfheimConfig.dimensionIDAlfheim, AlfheimConfig.zones[r.ordinal()].xCoord, AlfheimConfig.zones[r.ordinal()].yCoord, AlfheimConfig.zones[r.ordinal()].zCoord);
            }
        } else {
            throw new WrongUsageException(getCommandUsage(sender), new Object[0]);
        }
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args) {
		if (sender instanceof EntityPlayer && args.length == 1 && EnumRace.getRace((EntityPlayer) sender) == EnumRace.HUMAN) {
			String[] ss = new String[9];
			for (int i = 0; i < ss.length; i++) ss[i] = EnumRace.fromID(i + 1).localize();
			return getListOfStringsMatchingLastWord(args, ss);
		}
		return null;
    }
}
