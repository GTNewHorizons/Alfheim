package alfheim.api.spell;

import java.io.Serializable;
import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.api.ModInfo;
import alfheim.api.entity.EnumRace;
import alfheim.api.event.SpellCastEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import vazkii.botania.api.mana.ManaItemHandler;

public abstract class SpellBase {
	
	/**
	 * Mana checking and consumption call here
	 * @return Result of cast
	 */
	public abstract SpellCastResult performCast(EntityLivingBase caster);
	
	public abstract EnumRace getRace();
	
	public abstract String getName();
	
	// public abstract String[] getWords();
	
	public abstract int getManaCost();
	
	public abstract int getCooldown();
	
	public abstract int castTime();
	
	public abstract boolean isHard();
	
	@SideOnly(Side.CLIENT)
	public abstract void render(EntityLivingBase caster);
	
	public SpellCastResult checkCast(EntityLivingBase caster) {
		if (MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Pre(this, caster))) return SpellCastResult.NOTALLOW;
		boolean mana = caster instanceof EntityPlayer ? ((EntityPlayer) caster).capabilities.isCreativeMode || consumeMana((EntityPlayer) caster, (int) (getManaCost() * (caster instanceof EntityPlayer && getRace().equals(EnumRace.getRace((EntityPlayer) caster)) || isHard() ? 1 : 1.5)), true) : true;
		return mana ? SpellCastResult.OK : SpellCastResult.NOMANA;
	}
	
	public static boolean consumeMana(EntityPlayer player, int mana, boolean req) {
		return ManaItemHandler.requestManaExact(new ItemStack(Blocks.stone), player, mana, req);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof SpellBase && getName().equals(((SpellBase) obj).getName()) && getRace().equals(((SpellBase) obj).getRace());
	}
	
	@Override
	public int hashCode() {
		return (getManaCost() << 16) & 0xFFFF0000 | (getCooldown() & 0xFFFF);
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public static enum SpellCastResult {
		OK, DESYNC, NOTREADY, NOTARGET, WRONGTGT, OBSTRUCT, NOMANA, NOTALLOW, NOTSEEING;
	}
	
	public static final void say(EntityPlayerMP caster, SpellBase spell) {
		List<EntityPlayerMP> l = caster.worldObj.getEntitiesWithinAABB(EntityPlayerMP.class, AxisAlignedBB.getBoundingBox(caster.posX, caster.posY, caster.posZ, caster.posX, caster.posY, caster.posZ).expand(40, 40, 40));
		for (EntityPlayerMP player : l) if (Vector3.entityDistance(caster, player) < 40) player.addChatMessage(new ChatComponentText(EnumChatFormatting.UNDERLINE + "* " + caster.getCommandSenderName() + ' ' + StatCollector.translateToLocal("spell.cast") + EnumChatFormatting.RESET + ": " + StatCollector.translateToLocal("spell." + spell.getName() + ".words")));
	}
}