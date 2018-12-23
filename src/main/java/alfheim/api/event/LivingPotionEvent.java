package alfheim.api.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingEvent;

public abstract class LivingPotionEvent extends LivingEvent {

	public final PotionEffect effect;
	
	public LivingPotionEvent(EntityLivingBase entity, PotionEffect pe) {
		super(entity);
		effect = pe;
	}

	public abstract static class Add extends LivingPotionEvent {
		public Add(EntityLivingBase entity, PotionEffect pe) { super(entity, pe); }

		@Cancelable
		@Deprecated
		public static class Pre extends Add {
			public Pre(EntityLivingBase entity, PotionEffect pe) { super(entity, pe); }
		}
		
		public static class Post extends Add {
			public Post(EntityLivingBase entity, PotionEffect pe) { super(entity, pe); }
		}
	}

	public abstract static class Change extends LivingPotionEvent {
		public Change(EntityLivingBase entity, PotionEffect pe) { super(entity, pe); }

		@Cancelable
		@Deprecated
		public static class Pre extends Change {
			public Pre(EntityLivingBase entity, PotionEffect pe) { super(entity, pe); }
		}
		
		public static class Post extends Change {
			public Post(EntityLivingBase entity, PotionEffect pe) { super(entity, pe); }
		}
	}

	public abstract static class Remove extends LivingPotionEvent {
		public Remove(EntityLivingBase entity, PotionEffect pe) { super(entity, pe); }

		@Cancelable
		@Deprecated
		public static class Pre extends Remove {
			public Pre(EntityLivingBase entity, PotionEffect pe) { super(entity, pe); }
		}
		
		public static class Post extends Remove {
			public Post(EntityLivingBase entity, PotionEffect pe) { super(entity, pe); }
		}
	}
}
