package alfheim.common.core.command;

import java.util.Arrays;
import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.common.core.handler.CardinalSystem;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.network.Message3d;
import alfheim.common.network.Message3d.m3d;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class CommandAlfheim extends CommandBase {

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
	
	@Override
	public List getCommandAliases() {
        return Arrays.asList(new String[] {"alf"});
	}
	
	@Override
	public String getCommandName() {
		return "alfheim";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "alfheim.commands.alfheim.usage";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if(args.length == 1) {
			boolean esmOld = AlfheimCore.enableElvenStory;
			boolean mmoOld = AlfheimCore.enableMMO;
			if (args[0].equals("esm")) toggleESM(AlfheimCore.enableElvenStory = !AlfheimCore.enableElvenStory); else
			if (args[0].equals("mmo")) toggleMMO(AlfheimCore.enableMMO = !AlfheimCore.enableMMO); else
			throw new WrongUsageException("alfheim.commands.alfheim.wrong", new Object[0]);
			AlfheimConfig.writeModes();
		
			ASJUtilities.sayToAllOnline(String.format(StatCollector.translateToLocal("alfheim.commands.alfheim.done"),
										sender.getCommandSenderName	(),
										AlfheimCore.enableElvenStory? EnumChatFormatting.GREEN : EnumChatFormatting.DARK_RED,
										EnumChatFormatting.RESET	,
										AlfheimCore.enableMMO		? EnumChatFormatting.GREEN : EnumChatFormatting.DARK_RED,
										EnumChatFormatting.RESET	));
			
			AlfheimCore.network.sendToAll(new Message3d(m3d.TOGGLER, args[0].equals("esm") ? 1 : 0, ((esmOld ? 1 : 0) << 1) | (AlfheimCore.enableElvenStory ? 1 : 0), ((mmoOld ? 1 : 0) << 1) | (AlfheimCore.enableMMO ? 1 : 0)));
		} else throw new WrongUsageException("alfheim.commands.alfheim.wrong", new Object[0]);
	}
	
	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args) {
		return getListOfStringsMatchingLastWord(args, "esm", "mmo");
	}
	
	
	
	public static void toggleESM(boolean on) {
		if (on) {
			AlfheimConfig.initWorldCoordsForElvenStory(AlfheimCore.save);
		} else {
			toggleMMO(AlfheimCore.enableMMO = false);
		}
	}
	
	public static void toggleMMO(boolean on) {
		if (on) {
			CardinalSystem.load(AlfheimCore.save);
			toggleESM(AlfheimCore.enableElvenStory = true);
		} else {
			
		}
	}
}