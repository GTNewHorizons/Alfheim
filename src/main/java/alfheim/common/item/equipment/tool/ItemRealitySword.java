package alfheim.common.item.equipment.tool;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.api.*;
import alfheim.client.render.world.SpellEffectHandlerClient.Spells;
import alfheim.common.core.handler.SpellEffectHandler;
import alfheim.common.core.registry.AlfheimItems;
import cpw.mods.fml.relauncher.*;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraft.world.World;
import vazkii.botania.api.mana.*;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.nio.charset.Charset;
import java.security.*;
import java.util.List;

import static vazkii.botania.common.core.helper.ItemNBTHelper.*;

public class ItemRealitySword extends ItemSword implements IManaUsingItem {

	public static final String TAG_ELEMENT = "element";
	public static final IIcon[] textures = new IIcon[6];
	
	public ItemRealitySword() {
		super(AlfheimAPI.REALITY);
		setCreativeTab(AlfheimCore.alfheimTab);
		setNoRepair();
		setUnlocalizedName("RealitySword");
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "item.RealitySword" + getInt(stack, TAG_ELEMENT, 0);
	}
	
	@Override
	public void registerIcons(IIconRegister reg) {
		for (int i = 0; i < textures.length; i++)
			textures[i] = reg.registerIcon(ModInfo.MODID + ":RealitySword" + i);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconIndex(ItemStack stack) {
		return textures[getInt(stack, TAG_ELEMENT, 0)];
	}
	
	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return getIconIndex(stack);
	}
	
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (player.isSneaking()) {
			if (getInt(stack, TAG_ELEMENT, 0) == 5) return stack;
			if (merge(player.getCommandSenderName(), stack.getDisplayName()).equals("35E07445CBB8B10F7173F6AD6C1E29E9A66565F86AFF61ACADA750D443BFF7B0")) {
				setInt(stack, TAG_ELEMENT, 5);
				stack.getTagCompound().removeTag("display");
				return stack;
			}
			
			if (!ManaItemHandler.requestManaExact(stack, player, 1, !world.isRemote)) return stack;
			setInt(stack, TAG_ELEMENT, Math.max(0, getInt(stack, TAG_ELEMENT, 0) + 1) % 5);
		} else player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
		return stack;
	}
	
	String merge(String s1, String s2) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < s1.length(); i++) for (int j = 0; j < s2.length(); j++) s.append((char) ((s1.charAt(i) * s2.charAt(j)) % 256));
		return hash(s.toString());
	}
	
	String hash(String str) {
		if(str != null)
			try {
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				return new HexBinaryAdapter().marshal(md.digest(salt(str).getBytes(Charset.forName("UTF-8"))));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		return "";
	}
	
	// Might as well be called sugar given it's not secure at all :D
	String salt(String str) {
		SecureRandom rand = new SecureRandom(str.getBytes(Charset.forName("UTF-8")));
		int l = str.length();
		int steps = rand.nextInt(l);
		char[] chrs = str.toCharArray();
		for(int i = 0; i < steps; i++) {
			int indA = rand.nextInt(l);
			int indB;
			do {
				indB = rand.nextInt(l);
			} while(indB == indA);
			char c = (char) (chrs[indA] ^ chrs[indB]);
			chrs[indA] = c;
		}
		
		return String.copyValueOf(chrs);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slotID, boolean inHand) {
		if (world.isRemote) return;
		
		boolean flag = getInt(stack, TAG_ELEMENT, 0) == 5;
		if (entity instanceof EntityPlayer) {
			if (!flag && 0 < getInt(stack, TAG_ELEMENT, 0) && getInt(stack, TAG_ELEMENT, 0) < 5) 
				if (!ManaItemHandler.requestManaExact(stack, (EntityPlayer) entity, 1, !world.isRemote)) 
					setInt(stack, TAG_ELEMENT, 0);
			
			if (flag && !entity.getCommandSenderName().equals("AlexSocol")) {
				EntityPlayer player = ((EntityPlayer) entity);
				world.spawnEntityInWorld(new EntityItem(world, player.posX, player.posY, player.posZ, stack.copy()));
				player.inventory.consumeInventoryItem(AlfheimItems.realitySword);
				player.setHealth(0);
				player.onDeath(DamageSource.outOfWorld);
				ASJUtilities.sayToAllOnline(StatCollector.translateToLocalFormatted("item.RealitySword.DIE", player.getCommandSenderName())); 
			}
		} else if (flag && entity instanceof EntityLivingBase) {
			EntityLivingBase living = ((EntityLivingBase) entity);
			world.spawnEntityInWorld(new EntityItem(world, living.posX, living.posY, living.posZ, stack.copy()));
			stack.stackSize = 0;
			living.setHealth(0);
			living.onDeath(DamageSource.outOfWorld);
		} else if (flag) {
			world.spawnEntityInWorld(new EntityItem(world, 0, 666, 0, stack.copy()));
			stack.stackSize = 0;
		}
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		if (attacker instanceof EntityPlayer) {
			int elem = getInt(stack, TAG_ELEMENT, 0);
			if (elem != 0 && (elem == 5 || ManaItemHandler.requestManaExact(stack, (EntityPlayer) attacker, 1000, !attacker.worldObj.isRemote))) useAbility(elem, attacker, target);
		}
		return super.hitEntity(stack, target, attacker);
	}
	
	private void useAbility(int i, EntityLivingBase attacker, EntityLivingBase target) {
		switch (i) {
			case 1: {
				target.addPotionEffect(new PotionEffect(Potion.blindness.id, 100, -1));
				Vec3 vec = attacker.getLookVec();
				target.motionX = vec.xCoord * 1.5;
				target.motionZ = vec.zCoord * 1.5;
				break;
			}
			case 2: target.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 100, 1)); break;
			case 3: target.setFire(6); break;
			case 4: {
				target.motionY += 0.825;
				SpellEffectHandler.sendPacket(Spells.SPLASH, target);
				break;
			}
			case 5: {
				for (int a = 1; a < 5; a++) useAbility(a, attacker, target);
				break;
			}
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
		int elem = getInt(stack, TAG_ELEMENT, 0);
		if (elem == 5) {
			list.add(StatCollector.translateToLocal("item.RealitySword.descZ"));
			return;
		}
		
		if (0 < elem && elem < 5) {
			list.add(StatCollector.translateToLocal("item.RealitySword.desc" + elem));
			list.add(StatCollector.translateToLocal("item.RealitySword.desc5"));
		} else
			list.add(StatCollector.translateToLocal("item.RealitySword.desc0"));
	}
	
	@Override
	public boolean usesMana(ItemStack stack) {
		return (0 < getInt(stack, TAG_ELEMENT, 0) && getInt(stack, TAG_ELEMENT, 0) < 5);
	}
}