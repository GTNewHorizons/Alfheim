package alfheim.common.items;

import java.util.List;

import alfheim.AlfheimCore;
import alfheim.ModInfo;
import alfheim.common.registry.AlfheimRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;

public class RealitySword extends ItemSword implements IManaUsingItem {

	public static final String TAGELEMENT = "ELEMENT";
	public static IIcon[] textures = new IIcon[5];
	
	public RealitySword() {
		super(AlfheimRegistry.REALITY);
		this.setCreativeTab(AlfheimCore.alfheimTab);
		this.setNoRepair();
		this.setUnlocalizedName("RealitySword");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
		for (int i = 0; i < 5; i++)
		textures[i] = reg.registerIcon(ModInfo.MODID + ":RealitySword" + i);
	}
	
	@Override
	public IIcon getIconIndex(ItemStack stack) {
		if (!stack.hasTagCompound()) {
        	stack.stackTagCompound = new NBTTagCompound();
    		stack.stackTagCompound.setInteger(TAGELEMENT, 0);
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
        	} else {
        		int tag = Math.max(0, stack.stackTagCompound.getInteger(TAGELEMENT) + 1);
        		if (tag > 4) tag = 0;
        		stack.stackTagCompound.setInteger(TAGELEMENT, tag);
        		stack.setStackDisplayName(StatCollector.translateToLocalFormatted("item.RealitySword.name", StatCollector.translateToLocal("item.RealitySword.name" + tag)));
        	}
        } else player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
        return stack;
    }
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity player, int par4, boolean par5) {
		if(player instanceof EntityPlayer && (0 < stack.stackTagCompound.getInteger(TAGELEMENT) && stack.stackTagCompound.getInteger(TAGELEMENT) < 5)) {
			if (!ManaItemHandler.requestManaExact(stack, (EntityPlayer) player, 1, !world.isRemote)) {
				stack.stackTagCompound.setInteger(TAGELEMENT, 0);
        		stack.setStackDisplayName(StatCollector.translateToLocalFormatted("item.RealitySword.name", StatCollector.translateToLocal("item.RealitySword.name0")));
			}
		}	
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (!stack.hasTagCompound()) {
        	stack.stackTagCompound = new NBTTagCompound();
    		stack.stackTagCompound.setInteger(TAGELEMENT, 0);
        }
        
        int elem = stack.stackTagCompound.getInteger(TAGELEMENT);
        switch (elem) {
	        case 1: {
	        	Vec3 vec = attacker.getLookVec();
	        	target.motionX = vec.xCoord * 1.5;
	        	target.motionZ = vec.zCoord * 1.5;
	        	break;
	        }
	        case 2: target.addPotionEffect(new PotionEffect(Potion.poison.id, 100, 1)); break;
	        case 3: target.setFire(5); break;
	        case 4: target.addPotionEffect(new PotionEffect(Potion.blindness.id, 100, -1)); break;
	        default: return false;
        }
        
        return super.hitEntity(stack, target, attacker);
    }
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
		if (!stack.hasTagCompound()) {
        	stack.stackTagCompound = new NBTTagCompound();
    		stack.stackTagCompound.setInteger(TAGELEMENT, 0);
        }
		int elem = stack.stackTagCompound.getInteger(TAGELEMENT);
		stack.setStackDisplayName(StatCollector.translateToLocalFormatted("item.RealitySword.name", StatCollector.translateToLocal("item.RealitySword.name" + elem)));
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