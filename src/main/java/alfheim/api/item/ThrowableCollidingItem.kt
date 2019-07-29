package alfheim.api.item

import net.minecraft.entity.projectile.EntityThrowable
import net.minecraft.item.ItemStack
import net.minecraft.util.MovingObjectPosition

class ThrowableCollidingItem(internal var key: String, internal var stack: ItemStack, internal var event: (EntityThrowable, MovingObjectPosition) -> Unit) {
	
	fun onImpact(throwable: EntityThrowable, movingObject: MovingObjectPosition) {
		event.invoke(throwable, movingObject)
	}
}
