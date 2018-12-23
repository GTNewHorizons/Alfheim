package alfheim.common.spell.fire;

import alexsocol.asjlib.ASJUtilities;
import alfheim.api.entity.EnumRace;
import alfheim.api.event.SpellCastEvent;
import alfheim.api.spell.SpellBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.common.MinecraftForge;

public class SpellIgnition extends SpellBase {

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		if (!(caster instanceof EntityPlayer)) return SpellCastResult.NOTALLOW;
		EntityPlayer player = (EntityPlayer) caster;
		MovingObjectPosition mop = ASJUtilities.getSelectedBlock(caster, 16, false);
		if (mop == null || mop.typeOfHit != MovingObjectType.BLOCK || mop.sideHit == -1) return SpellCastResult.WRONGTGT;
		
	    switch(mop.sideHit) {
	    	case 0: --mop.blockY; break;
	    	case 1: ++mop.blockY; break;
	    	case 2: --mop.blockZ; break;
	    	case 3: ++mop.blockZ; break;
	    	case 4: --mop.blockX; break;
	    	case 5: ++mop.blockX; break;
    	}
		
		if (!Blocks.fire.canPlaceBlockAt(caster.worldObj, mop.blockX, mop.blockY, mop.blockZ)) return SpellCastResult.WRONGTGT;
		if (MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Pre(this, caster))) return SpellCastResult.NOTALLOW;
		boolean mana =
				player.capabilities.isCreativeMode ||
				consumeMana(player, (int) (getManaCost() * (getRace().equals(EnumRace.getRace(player)) || isHard() ? 1 : 1.5)), false);
		
		if (!mana) return SpellCastResult.NOMANA;
		
		ItemStack stackToPlace = new ItemStack(Blocks.fire);
		stackToPlace.tryPlaceItemIntoWorld(player, caster.worldObj, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, (float) mop.hitVec.xCoord, (float) mop.hitVec.yCoord, (float) mop.hitVec.zCoord);

		if(stackToPlace.stackSize == 0) {
			consumeMana(player, (int) (getManaCost() * (getRace().equals(EnumRace.getRace(player)) || isHard() ? 1 : 1.5)), true);
			return SpellCastResult.OK;
		}
		
		return SpellCastResult.OBSTRUCT;
	}

	@Override
	public EnumRace getRace() {
		return EnumRace.SALAMANDER;
	}

	@Override
	public String getName() {
		return "ignition";
	}

	@Override
	public int getManaCost() {
		return 2000;
	}

	@Override
	public int getCooldown() {
		return 100;
	}

	@Override
	public int castTime() {
		return 5;
	}

	@Override
	public boolean isHard() {
		return false;
	}

	@Override
	public void render(EntityLivingBase caster) {}
}