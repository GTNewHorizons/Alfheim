package alfheim.common.entity;

import alfheim.common.core.registry.AlfheimRegistry;
import net.minecraft.entity.player.EntityPlayer;

public class Flight {

	public static void register(EntityPlayer player) {
		player.getAttributeMap().registerAttribute(AlfheimRegistry.FLIGHT);
	}
	
	public static void ensureExistence(EntityPlayer player) {
		if (player.getAttributeMap().getAttributeInstance(AlfheimRegistry.FLIGHT) == null) register(player);
	}

	public static double max() {
		return AlfheimRegistry.FLIGHT.getDefaultValue();
	}
	public static double get(EntityPlayer player) {
		ensureExistence(player);
		return player.getAttributeMap().getAttributeInstance(AlfheimRegistry.FLIGHT).getBaseValue();
	}
	
	public static void set(EntityPlayer player, double val) {
		ensureExistence(player);
		player.getAttributeMap().getAttributeInstance(AlfheimRegistry.FLIGHT).setBaseValue(val);
	}
	
	public static void add(EntityPlayer player, double val) {
		set(player, get(player) + val);
	}
	
	public static void sub(EntityPlayer player, double val) {
		add(player, -val);
	}
}
