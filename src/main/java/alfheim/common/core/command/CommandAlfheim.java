package alfheim.common.core.command;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.common.core.handler.*;
import alfheim.common.core.registry.AlfheimRecipes;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.integration.thaumcraft.ThaumcraftAlfheimModule;
import alfheim.common.network.Message3d;
import alfheim.common.network.Message3d.m3d;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import vazkii.botania.common.Botania;

import java.util.*;

public class CommandAlfheim extends CommandBase {
	
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
	
	@Override
	public List getCommandAliases() {
		return Arrays.asList("alf");
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
			if (args[0].equalsIgnoreCase("ESM")) toggleESM(AlfheimCore.enableElvenStory = !AlfheimCore.enableElvenStory); else
			if (args[0].equalsIgnoreCase("MMO")) toggleMMO(AlfheimCore.enableMMO = !AlfheimCore.enableMMO); else
			throw new WrongUsageException("alfheim.commands.alfheim.wrong");
			AlfheimConfig.writeModes();
		
			ASJUtilities.sayToAllOnline(String.format(StatCollector.translateToLocal("alfheim.commands.alfheim.done"),
										sender.getCommandSenderName	(),
										AlfheimCore.enableElvenStory? EnumChatFormatting.GREEN : EnumChatFormatting.DARK_RED,
										EnumChatFormatting.RESET	,
										AlfheimCore.enableMMO		? EnumChatFormatting.GREEN : EnumChatFormatting.DARK_RED,
										EnumChatFormatting.RESET	));
			
			AlfheimCore.network.sendToAll(new Message3d(m3d.TOGGLER, args[0].equalsIgnoreCase("ESM") ? 1 : 0, ((esmOld ? 1 : 0) << 1) | (AlfheimCore.enableElvenStory ? 1 : 0), ((mmoOld ? 1 : 0) << 1) | (AlfheimCore.enableMMO ? 1 : 0)));
		} else throw new WrongUsageException("alfheim.commands.alfheim.wrong");
	}
	
	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args) {
		return getListOfStringsMatchingLastWord(args, "ESM", "MMO");
	}
	
	public static void toggleESM(boolean on) {
		if (on) {
			AlfheimConfig.initWorldCoordsForElvenStory(AlfheimCore.save);
			EventHandler.checkAddAttrs();
			if (Botania.thaumcraftLoaded) ThaumcraftAlfheimModule.addESMRecipes();
		} else {
			if (Botania.thaumcraftLoaded) ThaumcraftAlfheimModule.removeESMRecipes();
			toggleMMO(AlfheimCore.enableMMO = false);
		}
	}
	
	public static void toggleMMO(boolean on) {
		if (on) {
			CardinalSystem.load(AlfheimCore.save);
			AlfheimRecipes.addMMORecipes();
			toggleESM(AlfheimCore.enableElvenStory = true);
			for (Object o : MinecraftServer.getServer().getConfigurationManager().playerEntityList) CardinalSystem.transfer((EntityPlayerMP) o);
		} else {
			CardinalSystem.save(AlfheimCore.save);
			AlfheimRecipes.removeMMORecipes();
		}
	}
}