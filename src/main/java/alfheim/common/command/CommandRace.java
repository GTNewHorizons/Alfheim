package alfheim.common.command;

import java.util.ArrayList;
import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alfheim.Constants;
import alfheim.common.entity.EnumRace;
import alfheim.common.utils.AlfheimConfig;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

public class CommandRace extends CommandBase {

	@Override
	public int getRequiredPermissionLevel() {
        return 0;
    }
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return sender instanceof EntityPlayer && EnumRace.fromID(((EntityPlayer) sender).getEntityAttribute(Constants.RACE).getAttributeValue()) == EnumRace.HUMAN;
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
            if (r == null || r == EnumRace.ALV || r == EnumRace.HUMAN) throw new WrongUsageException("elvenstory.commands.race.usage", new Object[0]);
            if (EnumRace.fromID(((EntityPlayer) sender).getEntityAttribute(Constants.RACE).getAttributeValue()) == EnumRace.HUMAN) {
            	((EntityPlayer) sender).getEntityAttribute(Constants.RACE).setBaseValue(r.ordinal());
            	((EntityPlayer) sender).capabilities.allowFlying = true;
            	ASJUtilities.sendToDimensionWithoutPortal((EntityPlayer) sender, AlfheimConfig.dimensionIDAlfheim, AlfheimConfig.zones[r.ordinal()].xCoord, AlfheimConfig.zones[r.ordinal()].yCoord, AlfheimConfig.zones[r.ordinal()].zCoord);
            }
        } else {
            throw new WrongUsageException("elvenstory.commands.race.usage", new Object[0]);
        }
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args) {
		if (sender instanceof EntityPlayer && args.length == 1 && EnumRace.fromID(((EntityPlayer) sender).getEntityAttribute(Constants.RACE).getAttributeValue()) == EnumRace.HUMAN) {
			String[] ss = new String[9];
			for (int i = 0; i < ss.length; i++) ss[i] = EnumRace.fromID(i + 1).localize();
			return getListOfStringsMatchingLastWord(args, ss);
		}
		return null;
    }
}
