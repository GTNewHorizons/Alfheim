package alfheim.api.entity;

import alexsocol.asjlib.ASJUtilities;
import alfheim.api.AlfheimAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;

public enum EnumRace {
	HUMAN, SALAMANDER, SYLPH, CAITSITH, POOKA, GNOME, LEPRECHAUN, SPRIGGAN, UNDINE, IMP, ALV;
	
	public int getRGBColor() {
		return getRGBColor(ordinal());
	}
	
	public static int getRGBColor(double id) {
		if (id == 1) return 0xb61f24;
		if (id == 2) return 0x5ee52e;
		if (id == 3) return 0xcdb878;
		if (id == 4) return 0x99cb3b;
		if (id == 5) return 0x816b57;
		if (id == 6) return 0x6d6b7b;
		if (id == 7) return 0x282739;
		if (id == 8) return 0x40c0a4;
		if (id == 9) return 0x786a89;
		return 0xffffff;
	}
	
	public EnumChatFormatting getEnumColor() {
		return getEnumColor(ordinal());
	}
	
	public static EnumChatFormatting getEnumColor(double id) {
		if (id == 1) return EnumChatFormatting.DARK_RED;
		if (id == 2) return EnumChatFormatting.GREEN;
		if (id == 3) return EnumChatFormatting.GOLD;
		if (id == 4) return EnumChatFormatting.YELLOW;
		if (id == 5) return EnumChatFormatting.DARK_GREEN;
		if (id == 6) return EnumChatFormatting.GRAY;
		if (id == 7) return EnumChatFormatting.DARK_GRAY;
		if (id == 8) return EnumChatFormatting.AQUA;
		if (id == 9) return EnumChatFormatting.DARK_PURPLE;
		return EnumChatFormatting.WHITE;
	}
	
	public void glColor() {
		glColor(ordinal());
	}
	
	public static void glColor(double id) {
		ASJUtilities.glColor1u(ASJUtilities.addAlpha(getRGBColor(id), 255));
	}
	
	public void glColorA(double alpha) {
		glColorA(ordinal(), alpha);
	}
	
	public static void glColorA(double id, double alpha) {
		ASJUtilities.glColor1u(ASJUtilities.addAlpha(getRGBColor(id), (int) (alpha * 255)));
	}
	
	public static EnumRace getByID(double id) {
		if (0 > id || id > EnumRace.values().length) return HUMAN;
		return EnumRace.values()[MathHelper.floor_double(id)];
	}
	
	public String localize() {
		return StatCollector.translateToLocal("race." + toString() + ".name");
	}
	
	public static String unlocalize(String name) {
		return StatCollector.translateToLocal("race." + name + ".reverse");
	}
	
	public static EnumRace getRace(EntityPlayer player) {
		if (!hasRace(player)) registerRace(player);
		return getByID(player.getEntityAttribute(AlfheimAPI.RACE).getAttributeValue());
	}
	
	public static int getRaceID(EntityPlayer player) {
		if (!hasRace(player)) registerRace(player);
		return MathHelper.floor_double(player.getEntityAttribute(AlfheimAPI.RACE).getAttributeValue());
	}
	
	public static void setRace(EntityPlayer player, EnumRace race) {
		if (!hasRace(player)) registerRace(player);
		player.getEntityAttribute(AlfheimAPI.RACE).setBaseValue(race.ordinal());
	}
	
	public static void setRaceID(EntityPlayer player, double raceID) {
		if (!hasRace(player)) registerRace(player);
		player.getEntityAttribute(AlfheimAPI.RACE).setBaseValue(raceID);
	}
	
	private static boolean hasRace(EntityPlayer player) {
		return player.getAttributeMap().getAttributeInstance(AlfheimAPI.RACE) != null;
	}
	
	private static void registerRace(EntityPlayer player) {
		player.getAttributeMap().registerAttribute(AlfheimAPI.RACE);
		setRace(player, HUMAN);
	}
}; 