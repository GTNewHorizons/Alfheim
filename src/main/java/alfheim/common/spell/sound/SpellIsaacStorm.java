package alfheim.common.spell.sound;

import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.api.spell.SpellBase.SpellCastResult;
import alfheim.common.entity.spell.EntitySpellIsaacMissile;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.common.entity.EntityMagicMissile;
import vazkii.botania.common.item.rod.ItemMissileRod;

public class SpellIsaacStorm extends SpellBase {

	public SpellIsaacStorm() {
		super("isaacstorm", EnumRace.POOKA, 256000, 72000, 100, true);
	}

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		if (caster.worldObj.getEntitiesWithinAABB(IMob.class, AxisAlignedBB.getBoundingBox(caster.posX, caster.posY + 2, caster.posZ, caster.posX, caster.posY + 2, caster.posZ).expand(15, 15, 15)).isEmpty()) return SpellCastResult.NOTSEEING;
			
		SpellCastResult result = checkCast(caster);
		if (result == SpellCastResult.OK) {
			EntitySpellIsaacMissile missile;
			for (int i = 0; i < 400; i++) {
				missile = new EntitySpellIsaacMissile(caster, false);
				missile.setPosition(caster.posX + (Math.random() - 0.5) * 0.1, caster.posY + 2.4 + (Math.random() - 0.5) * 0.1, caster.posZ + (Math.random() - 0.5) * 0.1);
				caster.worldObj.spawnEntityInWorld(missile);
			}
			
			for (int i = 0; i < 4; i++) 
				caster.worldObj.playSoundEffect(caster.posX + (Math.random() - 0.5) * 0.1, caster.posY + 2.4 + (Math.random() - 0.5) * 0.1, caster.posZ + (Math.random() - 0.5) * 0.1, "botania:missile", 0.6F, 0.8F + (float) Math.random() * 0.2F);
		}
		
		return result;
	}
}
