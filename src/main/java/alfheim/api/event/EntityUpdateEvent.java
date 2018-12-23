package alfheim.api.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.Entity;

@Cancelable
public class EntityUpdateEvent extends Event {

	public final Entity entity;
	public final boolean isRiding;
	
	public EntityUpdateEvent(Entity e) {
		entity = e;
		isRiding = e.ridingEntity != null;
	}
	
	/** Used in ASM */
	public static EntityUpdateEvent instantiate(Entity e) {
		return new EntityUpdateEvent(e);
	}
	
	/** Used in ASM */
	public static void stub() {}
}
