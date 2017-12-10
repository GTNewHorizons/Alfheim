package alfheim.common.entity;

import net.minecraft.util.MathHelper;

public enum EnumRace {
	HUMAN, SALAMANDER, SYLPH, CAITSITH, POOKA, GNOME, LEPRECHAUN, SPRIGGAN, UNDINE, IMP, ALV;
	
	public static EnumRace fromString(String name) {
		if (name.equalsIgnoreCase("SALAMANDER")) return SALAMANDER; 
		if (name.equalsIgnoreCase("SYLPH")) return SYLPH; 
		if (name.equalsIgnoreCase("CAITSITH")) return CAITSITH; 
		if (name.equalsIgnoreCase("POOKA")) return POOKA; 
		if (name.equalsIgnoreCase("GNOME")) return GNOME; 
		if (name.equalsIgnoreCase("LEPRECHAUN")) return LEPRECHAUN; 
		if (name.equalsIgnoreCase("SPRIGGAN")) return SPRIGGAN; 
		if (name.equalsIgnoreCase("UNDINE")) return UNDINE; 
		if (name.equalsIgnoreCase("IMP")) return IMP;
		if (name.equalsIgnoreCase("ALV")) return ALV;
		if (name.equalsIgnoreCase("HUMAN")) return HUMAN;
		return null;
	}
	
	public static EnumRace fromDouble(double id) {
		if (0 > id || id > EnumRace.values().length) return HUMAN;
		return EnumRace.values()[MathHelper.floor_double(id)];
	}
	
	public String toString() {
		if (this == SALAMANDER) return "SALAMANDER";
		if (this == SYLPH) return "SYLPH";
		if (this == CAITSITH) return "CAITSITH";
		if (this == POOKA) return "POOKA";
		if (this == GNOME) return "GNOME";
		if (this == LEPRECHAUN) return "LEPRECHAUN";
		if (this == SPRIGGAN) return "SPRIGGAN";
		if (this == UNDINE) return "UNDINE";
		if (this == IMP) return "IMP";
		if (this == ALV) return "ALV";
		if (this == HUMAN) return "HUMAN";
		return "";
	}
};