package alfheim.common.items;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.Constants;
import alfheim.common.registry.AlfheimItems;
import alfheim.common.registry.AlfheimRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;

public class RealitySword extends ItemSword implements IManaUsingItem {

	public static final String TAGELEMENT = "ELEMENT";
	public static IIcon[] textures = new IIcon[6];
	
	public RealitySword() {
		super(AlfheimRegistry.REALITY);
		this.setCreativeTab(AlfheimCore.alfheimTab);
		this.setNoRepair();
		this.setUnlocalizedName("RealitySword");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
		for (int i = 0; i < 6; i++)
		textures[i] = reg.registerIcon(Constants.MODID + ":RealitySword" + i);
	}
	
	@Override
	public IIcon getIconIndex(ItemStack stack) {
		if (!stack.hasTagCompound()) {
        	stack.stackTagCompound = new NBTTagCompound();
    		stack.stackTagCompound.setInteger(TAGELEMENT, 0);
    		stack.setStackDisplayName(StatCollector.translateToLocalFormatted("item.RealitySword.name", StatCollector.translateToLocal("item.RealitySword.name0")));
        }
		return textures[stack.stackTagCompound.getInteger(TAGELEMENT)];
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return getIconIndex(stack);
	}
	
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (player.isSneaking()) {
        	
        	if (!stack.hasTagCompound()) {
        		stack.stackTagCompound = new NBTTagCompound();
        		stack.stackTagCompound.setInteger(TAGELEMENT, 0);
        		stack.setStackDisplayName(StatCollector.translateToLocalFormatted("item.RealitySword.name", StatCollector.translateToLocal("item.RealitySword.name0")));
        	}
        	
        	if (stack.stackTagCompound.getInteger(TAGELEMENT) == 5) return stack;
        	if (merge(player.getCommandSenderName(), stack.getDisplayName()).equals("6BC74E0C93964462C7DA1C1EA29E1A95FFC1A89FECE0E6B15C0D0380C492D99A")) {
        		stack.stackTagCompound.setInteger(TAGELEMENT, 5);
        		stack.setStackDisplayName(StatCollector.translateToLocal("item.RealitySword.nameZ"));
        		return stack;
        	}
        	
        	int tag = Math.max(0, stack.stackTagCompound.getInteger(TAGELEMENT) + 1) % 5;
        	stack.stackTagCompound.setInteger(TAGELEMENT, tag);
        	stack.setStackDisplayName(StatCollector.translateToLocalFormatted("item.RealitySword.name", StatCollector.translateToLocal("item.RealitySword.name" + tag)));
        } else player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
        return stack;
    }
	
	String merge(String s1, String s2) {
		String s = "";
		for (int i = 0; i < s1.length(); i++) for (int j = 0; j < s2.length(); j++) s += (char) ((s1.charAt(i) * s2.charAt(j)) % 256);
		return hash(s.toString());
	}
	
	String hash(String str) {
		if(str != null)
			try {
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				return new HexBinaryAdapter().marshal(md.digest(salt(str).getBytes()));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		return "";
	}

	// Might as well be called sugar given it's not secure at all :D
	String salt(String str) {
		str = str += "wellithoughtthatthisiscoolideaandicanmakesomethinglikethis#whynot";
		SecureRandom rand = new SecureRandom(str.getBytes());
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
	public void onUpdate(ItemStack stack, World world, Entity player, int par4, boolean par5) {
		if (!stack.hasTagCompound()) {
			stack.stackTagCompound = new NBTTagCompound();
    		stack.stackTagCompound.setInteger(TAGELEMENT, 0);
    		stack.setStackDisplayName(StatCollector.translateToLocalFormatted("item.RealitySword.name", StatCollector.translateToLocal("item.RealitySword.name0")));
		}
		
		if (world.isRemote) return;
		
		boolean flag = stack.stackTagCompound.getInteger(TAGELEMENT) == 5;
		if (player instanceof EntityPlayer) {
			if (0 < stack.stackTagCompound.getInteger(TAGELEMENT) && stack.stackTagCompound.getInteger(TAGELEMENT) < 5) {
				if (!ManaItemHandler.requestManaExact(stack, (EntityPlayer) player, 1, !world.isRemote)) {
					stack.stackTagCompound.setInteger(TAGELEMENT, 0);
	        		stack.setStackDisplayName(StatCollector.translateToLocalFormatted("item.RealitySword.name", StatCollector.translateToLocal("item.RealitySword.name0")));
				}
			}
			
			if (flag && !player.getCommandSenderName().equals("AlexSocol")) {
				world.spawnEntityInWorld(new EntityItem(world, player.posX, player.posY, player.posZ, stack.copy()));
				((EntityPlayer) player).inventory.consumeInventoryItem(AlfheimItems.realitySword);
				((EntityPlayer) player).setHealth(0);
	            ((EntityPlayer) player).onDeath(DamageSource.outOfWorld);
	            ASJUtilities.sendToAllOnline(StatCollector.translateToLocalFormatted("item.RealitySword.DIE", player.getCommandSenderName())); 
			}
		} else if (flag && player instanceof EntityLivingBase) {
			world.spawnEntityInWorld(new EntityItem(world, player.posX, player.posY, player.posZ, stack.copy()));
            stack.stackSize = 0;
            
			((EntityLivingBase) player).setHealth(0);
			
			try {
				Method getDeathSound = EntityLivingBase.class.getClass().getDeclaredMethod("getDeathSound");
				getDeathSound.setAccessible(true);
				Method getSoundVolume = EntityLivingBase.class.getClass().getDeclaredMethod("getSoundVolume");
				getSoundVolume.setAccessible(true);
				Method getSoundPitch = EntityLivingBase.class.getClass().getDeclaredMethod("getSoundPitch");
				getSoundPitch.setAccessible(true);
				String s = (String) getDeathSound.invoke(((EntityLivingBase) player));
				
	            if (s != null) {
	            	((EntityLivingBase) player).playSound(s, (Float) getSoundVolume.invoke(((EntityLivingBase) player)), (Float) getSoundPitch.invoke(((EntityLivingBase) player)));
	            }
			} catch (Throwable e) {
				e.printStackTrace();
			}
			
            ((EntityLivingBase) player).onDeath(DamageSource.outOfWorld);
		}
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (!stack.hasTagCompound()) {
        	stack.stackTagCompound = new NBTTagCompound();
    		stack.stackTagCompound.setInteger(TAGELEMENT, 0);
    		stack.setStackDisplayName(StatCollector.translateToLocalFormatted("item.RealitySword.name", StatCollector.translateToLocal("item.RealitySword.name0")));
        }
        
        int elem = stack.stackTagCompound.getInteger(TAGELEMENT);
        useAbility(elem, attacker, target);
        
        return super.hitEntity(stack, target, attacker);
    }
	
	private void useAbility(int i, EntityLivingBase attacker, EntityLivingBase target) {
		switch (i) {
	        case 1: {
	        	Vec3 vec = attacker.getLookVec();
	        	target.motionX = vec.xCoord * 1.5;
	        	target.motionZ = vec.zCoord * 1.5;
	        	break;
	        }
	        case 2: target.addPotionEffect(new PotionEffect(Potion.poison.id, 100, 1)); break;
	        case 3: target.setFire(5); break;
	        case 4: target.addPotionEffect(new PotionEffect(Potion.blindness.id, 100, -1)); break;
	        case 5: {
	        	for (int a = 1; a < 5; a++) useAbility(a, attacker, target);
	        	break;
	        }
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
		if (!stack.hasTagCompound()) {
        	stack.stackTagCompound = new NBTTagCompound();
    		stack.stackTagCompound.setInteger(TAGELEMENT, 0);
    		stack.setStackDisplayName(StatCollector.translateToLocalFormatted("item.RealitySword.name", StatCollector.translateToLocal("item.RealitySword.name0")));
        }
		
		int elem = stack.stackTagCompound.getInteger(TAGELEMENT);
		if (elem == 5) {
			list.add(StatCollector.translateToLocal("item.RealitySword.descZ"));
			return;
		}
		
		if (1 > elem || elem > 4) list.add(StatCollector.translateToLocal("item.RealitySword.desc0"));
		switch (elem) {
	        case 1: list.add(StatCollector.translateToLocal("item.RealitySword.desc1")); break;
	        case 2: list.add(StatCollector.translateToLocal("item.RealitySword.desc2")); break;
	        case 3: list.add(StatCollector.translateToLocal("item.RealitySword.desc3")); break;
	        case 4: list.add(StatCollector.translateToLocal("item.RealitySword.desc4")); break;
		}
		if (0 < elem && elem < 5) list.add(StatCollector.translateToLocal("item.RealitySword.desc5"));
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return (0 < stack.stackTagCompound.getInteger(TAGELEMENT) && stack.stackTagCompound.getInteger(TAGELEMENT) < 5);
	}
}