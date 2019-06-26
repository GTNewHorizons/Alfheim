package alfheim.api.spell;

import alfheim.api.entity.EnumRace;
import alfheim.api.event.SpellCastEvent;
import cpw.mods.fml.relauncher.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import net.minecraftforge.common.MinecraftForge;
import vazkii.botania.api.mana.ManaItemHandler;

import java.util.List;

public abstract class SpellBase {
	
	// Will be set during preInit
	public static Potion overmag;
	public final EnumRace	race;
	public final String		name;
	protected int			mana,
							cldn,
							cast;
	public final boolean	hard;
	
	public SpellBase(String name, EnumRace race, int mana, int cldn, int cast) {
		this(name, race, mana, cldn, cast, false);
	}
	
	public SpellBase(String name, EnumRace race, int mana, int cldn, int cast, boolean hard) {
		this.race = race;
		this.name = name;
		this.mana = mana;
		this.cldn = cldn;
		this.cast = cast;
		this.hard = hard;
	}
	
	/**
	 * Mana checking and consumption call here
	 * @return Result of cast
	 */
	public abstract SpellCastResult performCast(EntityLivingBase caster);
	
	// public abstract String[] getWords();
	
	public int setManaCost(int newVal) {
		int temp = mana;
		mana = newVal;
		return temp;
	}
	
	public int getManaCost() {
		return mana;
	}
	
	public int setCooldown(int newVal) {
		int temp = cldn;
		cldn = newVal;
		return temp;
	}
	
	public int getCooldown() {
		return cldn;
	}
	
	public int setCastTime(int newVal) {
		int temp = cast;
		cast = newVal;
		return temp;
	}
	
	public int getCastTime() {
		return cast;
	}
	
	@SideOnly(Side.CLIENT)
	public void render(EntityLivingBase caster) {
		// NO-OP
	}
	
	public static float over(EntityLivingBase caster, double was) {
		try {
			return (float) (caster.isPotionActive(overmag) ? was * 1.2 : was);
		} catch (Throwable e) {
			return (float) was;
		}
	}
	
	public SpellCastResult checkCast(EntityLivingBase caster) {
		if (MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Pre(this, caster))) return SpellCastResult.NOTALLOW;
		int cost = MathHelper.ceiling_double_int(getManaCost() * (caster instanceof EntityPlayer && race.equals(EnumRace.getRace((EntityPlayer) caster)) || hard ? 1 : 1.5));
		boolean mana = !(caster instanceof EntityPlayer) || (((EntityPlayer) caster).capabilities.isCreativeMode || consumeMana((EntityPlayer) caster, cost, true));
		return mana ? SpellCastResult.OK : SpellCastResult.NOMANA;
	}
	
	public SpellCastResult checkCastOver(EntityLivingBase caster) {
		if (MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Pre(this, caster))) return SpellCastResult.NOTALLOW;
		int cost = MathHelper.ceiling_float_int(over(caster, getManaCost() * (caster instanceof EntityPlayer && race.equals(EnumRace.getRace((EntityPlayer) caster)) || hard ? 1 : 1.5)));
		boolean mana = !(caster instanceof EntityPlayer) || (((EntityPlayer) caster).capabilities.isCreativeMode || consumeMana((EntityPlayer) caster, cost, true));
		return mana ? SpellCastResult.OK : SpellCastResult.NOMANA;
	}
	
	public static boolean consumeMana(EntityPlayer player, int mana, boolean req) {
		return ManaItemHandler.requestManaExact(new ItemStack(Blocks.stone), player, mana, req);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof SpellBase && name.equals(((SpellBase) obj).name) && race.equals(((SpellBase) obj).race);
	}
	
	@Override
	public int hashCode() {
		return (getManaCost() << 16) & 0xFFFF0000 | (getCooldown() & 0xFFFF);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public enum SpellCastResult {
		OK, DESYNC, NOTREADY, NOTARGET, WRONGTGT, OBSTRUCT, NOMANA, NOTALLOW, NOTSEEING
	}
	
	public static void say(EntityPlayerMP caster, SpellBase spell) {
		List<EntityPlayerMP> l = caster.worldObj.getEntitiesWithinAABB(EntityPlayerMP.class, AxisAlignedBB.getBoundingBox(caster.posX, caster.posY, caster.posZ, caster.posX, caster.posY, caster.posZ).expand(40, 40, 40));
		for (EntityPlayerMP player : l) if (Math.sqrt(Math.pow(caster.posX - player.posX, 2) + Math.pow(caster.posY - player.posY, 2) + Math.pow(caster.posZ - player.posZ, 2)) < 40) player.addChatMessage(new ChatComponentText(EnumChatFormatting.UNDERLINE + "* " + caster.getCommandSenderName() + ' ' + StatCollector.translateToLocal("spell.cast") + EnumChatFormatting.RESET + ": " + StatCollector.translateToLocal("spell." + spell.name + ".words")));
	}
}