package alfheim.common.item.relic;

import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import alfheim.AlfheimCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.Achievement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ILensEffect;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword;
import vazkii.botania.common.item.relic.ItemRelic;

/**
 * This code is completely copied from 208th Botania version and fully made by Vazkii or whoever... :D<br>
 * Hope all required stuff is already done by Botania using iterfaces and stuff...
 * */
public class ItemExcaliber extends ItemManasteelSword implements IRelic, ILensEffect {
	
	private static final String TAG_ATTACKER_USERNAME = "attackerUsername";
	private static final String TAG_HOME_ID = "homeID";
	
	public static ToolMaterial toolMaterial = EnumHelper.addToolMaterial("B_EXCALIBER", 3, -1, 6.2F, 6.0F, 40);
	Achievement achievement;

	public ItemExcaliber() {
		super(toolMaterial, "Excaliber");
		this.setCreativeTab(AlfheimCore.alfheimTab);
	}

	public void onUpdate(ItemStack stack, World world, Entity entity, int slotID, boolean inHand) {
		if ((entity instanceof EntityPlayer)) {
			EntityPlayer player = (EntityPlayer) entity;
			ItemRelic.updateRelic(stack, player);
			if (ItemRelic.isRightPlayer(player, stack)) {
				PotionEffect haste = player.getActivePotionEffect(Potion.digSpeed);
				float check = haste == null ? 1.0F/6.0F : haste.getAmplifier() == 0 ? 0.4F : haste.getAmplifier() == 2 ? 1.0F/3.0F : 0.5F;
				if (!world.isRemote && inHand && player.swingProgress == check && ManaItemHandler.requestManaExact(stack, player, 100, true)) {
					EntityManaBurst burst = getBurst(player, stack);
					world.spawnEntityInWorld(burst);
					world.playSoundAtEntity(player, "botania:terraBlade", 0.4F, 1.4F);
				}
			}
		}
	}

	public void addInformation(ItemStack stack, EntityPlayer player, List infoList, boolean advTooltip) {
		ItemRelic.addBindInfo(infoList, stack, player);
	}

	public void bindToUsername(String playerName, ItemStack stack) {
		ItemRelic.bindToUsernameS(playerName, stack);
	}

	public String getSoulbindUsername(ItemStack stack) {
		return ItemRelic.getSoulbindUsernameS(stack);
	}

	public Achievement getBindAchievement() {
		return this.achievement;
	}

	public void setBindAchievement(Achievement achievement) {
		this.achievement = achievement;
	}

	public boolean usesMana(ItemStack stack) {
		return false;
	}

	public boolean isItemTool(ItemStack p_77616_1_) {
		return true;
	}

	public int getEntityLifespan(ItemStack itemStack, World world) {
		return Integer.MAX_VALUE;
	}

	public Multimap getItemAttributeModifiers() {
		Multimap multimap = HashMultimap.create();
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", 10.0, 0));
		multimap.put(SharedMonsterAttributes.movementSpeed.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", 0.3, 1));
		return multimap;
	}

	public EntityManaBurst getBurst(EntityPlayer player, ItemStack stack) {
		EntityManaBurst burst = new EntityManaBurst(player);

		float motionModifier = 7.0F;

		burst.setColor(0xFFFF20);
		burst.setMana(1);
		burst.setStartingMana(100);
		burst.setMinManaLoss(200);
		burst.setManaLossPerTick(1.0F);
		burst.setGravity(0.0F);
		burst.setMotion(burst.motionX * motionModifier, burst.motionY * motionModifier, burst.motionZ * motionModifier);

		ItemStack lens = stack.copy();
		ItemNBTHelper.setString(lens, TAG_ATTACKER_USERNAME, player.getCommandSenderName());
		burst.setSourceLens(lens);
		return burst;
	}

	public void apply(ItemStack stack, BurstProperties props) {}

	public boolean collideBurst(IManaBurst burst, MovingObjectPosition pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		return dead;
	}

	public void updateBurst(IManaBurst burst, ItemStack stack) {
		EntityThrowable entity = (EntityThrowable) burst;
		AxisAlignedBB axis = AxisAlignedBB.getBoundingBox(entity.posX, entity.posY, entity.posZ, entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).expand(1.0, 1.0, 1.0);

		String attacker = ItemNBTHelper.getString(burst.getSourceLens(), TAG_ATTACKER_USERNAME, "");
		int homeID = ItemNBTHelper.getInt(stack, TAG_HOME_ID, -1);
		if (homeID == -1) {
			AxisAlignedBB axis1 = AxisAlignedBB.getBoundingBox(entity.posX, entity.posY, entity.posZ, entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).expand(5.0, 5.0, 5.0);
			List<EntityLivingBase> entities = entity.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axis1);
			for (EntityLivingBase living : entities) {
				if ((!(living instanceof EntityPlayer)) && (!(living instanceof IBossDisplayData)) && ((living instanceof IMob)) && (living.hurtTime == 0)) {
					homeID = living.getEntityId();
					ItemNBTHelper.setInt(stack, TAG_HOME_ID, homeID);
				}
			}
		}
		List<EntityLivingBase> entities = entity.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axis);
		Entity home;
		if (homeID != -1) {
			home = entity.worldObj.getEntityByID(homeID);
			if (home != null) {
				Vector3 vecEntity = Vector3.fromEntityCenter(home);
				Vector3 vecThis = Vector3.fromEntityCenter(entity);
				Vector3 vecMotion = vecEntity.sub(vecThis);
				Vector3 vecCurrentMotion = new Vector3(entity.motionX, entity.motionY, entity.motionZ);

				vecMotion.normalize().multiply(vecCurrentMotion.mag());
				burst.setMotion(vecMotion.x, vecMotion.y, vecMotion.z);
			}
		}
		for (EntityLivingBase living : entities) {
			if ((!(living instanceof EntityPlayer)) || ((!((EntityPlayer) living).getCommandSenderName().equals(attacker)) && ((MinecraftServer.getServer() == null) || (MinecraftServer.getServer().isPVPEnabled())))) {
				if (living.hurtTime == 0) {
					int cost = 1;
					int mana = burst.getMana();
					if (mana >= cost) {
						burst.setMana(mana - cost);
						float damage = 4.0F + toolMaterial.getDamageVsEntity();
						if ((!burst.isFake()) && (!entity.worldObj.isRemote)) {
							EntityPlayer player = living.worldObj.getPlayerEntityByName(attacker);
							living.attackEntityFrom(player == null ? DamageSource.magic : DamageSource.causePlayerDamage(player), damage);
							entity.setDead();
							break;
						}
					}
				}
			}
		}
	}

	public boolean doParticles(IManaBurst burst, ItemStack stack) {
		return true;
	}

	public EnumRarity getRarity(ItemStack p_77613_1_) {
		return BotaniaAPI.rarityRelic;
	}
}