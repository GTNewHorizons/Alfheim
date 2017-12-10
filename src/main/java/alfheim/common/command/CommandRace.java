package alfheim.common.command;

import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alfheim.Constants;
import alfheim.common.utils.AlfheimConfig;
import alfheim.common.entity.EnumRace;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;

public class CommandRace extends CommandBase {

	@Override
	public int getRequiredPermissionLevel() {
        return 0;
    }
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return sender instanceof EntityPlayer && EnumRace.fromDouble(((EntityPlayer) sender).getEntityAttribute(Constants.RACE).getAttributeValue()) == EnumRace.HUMAN;
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
            if (EnumRace.fromDouble(((EntityPlayer) sender).getEntityAttribute(Constants.RACE).getAttributeValue()) == EnumRace.HUMAN) {
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
		if (sender instanceof EntityPlayer && EnumRace.fromDouble(((EntityPlayer) sender).getEntityAttribute(Constants.RACE).getAttributeValue()) == EnumRace.HUMAN) 
		return args.length == 1 ? getListOfStringsMatchingLastWord(args, "SALAMANDER", "SYLPH", "CAITSITH", "POOKA", "GNOME", "LEPRECHAUN", "SPRIGGAN", "UNDINE", "IMP") : null;
		return null;
    }
}
