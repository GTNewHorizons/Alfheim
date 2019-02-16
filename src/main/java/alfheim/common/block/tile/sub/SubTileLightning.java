package alfheim.common.block.tile.sub;

import java.util.ArrayList;
import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alfheim.api.block.tile.SubTileEntity;
import alfheim.api.block.tile.SubTileEntity.EnumAnomalityRarity;
import alfheim.common.core.handler.CardinalSystem.KnowledgeSystem;
import alfheim.common.core.handler.CardinalSystem.KnowledgeSystem.Knowledge;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.core.util.DamageSourceSpell;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileRuneAltar;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;

public class SubTileLightning extends SubTileEntity {
	
	public static final double radius = 12;
	Vector3 vt = new Vector3();
	Vector3 ve = new Vector3();
	
	@Override
	public void update() {
		if (inWG()) return;
		
		vt.set(x() + 0.5, y() + 0.5, z() + 0.5);
		
		if (worldObj().rand.nextInt(6000) == 0) {
			double x = x() + Math.random() * 10 - 5, z = z() + Math.random() * 10 - 5;
			worldObj().addWeatherEffect(new EntityLightningBolt(worldObj(), x, worldObj().getTopSolidOrLiquidBlock(MathHelper.floor_double(x), MathHelper.floor_double(z)), z));
			return;
		}
		
		if (AlfheimConfig.lightningsSpeed > 0 && ticks % AlfheimConfig.lightningsSpeed == 0) {
			ve.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize();
			vt.add(ve.x / 2.25, ve.y / 2.25, ve.z / 2.25);
			ve.multiply(1.5).add(x() + 0.5, y() + 0.5, z() + 0.5);
			Botania.proxy.lightningFX(worldObj(), vt, ve, 50, worldObj().rand.nextLong(), 0, 0xFF0000);
		}
	}
	
	@Override
	public boolean onActivated(ItemStack stack, EntityPlayer player, World world, int x, int y, int z) {
		if (player instanceof EntityPlayerMP && stack != null && stack.getItem() == ModItems.manaResource && stack.stackSize > 0 && stack.getItemDamage() == 5 && world.getBlock(x, y - 1, z) == ModBlocks.pylon && world.getBlockMetadata(x, y - 1, z) == 2) {
			--stack.stackSize;
			world.setBlock(x, y - 1, z, AlfheimBlocks.alfheimPylons, 2, 3);
			
			world.playSoundEffect(x, y, z, "botania:runeAltarStart", 1F, 1F);
			
			for (int i = 0; i < 8; i++) {
				ve.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize();
				vt.add(ve.x / 2.25, ve.y / 2.25, ve.z / 2.25);
				ve.multiply(1.5).add(x() + 0.5, y() + 0.5, z() + 0.5);
				Botania.proxy.lightningFX(worldObj(), vt, ve, 50, worldObj().rand.nextLong(), 0, 0xFF0000);
			}
			
			KnowledgeSystem.learn((EntityPlayerMP) player, Knowledge.PYLONS);
		}
		return false;
	}
	
	@Override
	public List<Object> getTargets() {
		if (inWG()) return EMPTY_LIST;
		
		f: if (ticks % 50 == 0) {
			EntityLivingBase e =  findNearestVulnerableEntity(radius);
			if (e == null) break f;
			
			worldObj().playSoundEffect(x(), y(), z(), "botania:runeAltarCraft", 1F, 1F);
			
			List l = new ArrayList<Object>();
			l.add(e);
			return l;
		}
		return EMPTY_LIST;
	}
	
	@Override
	public void performEffect(Object target) {
		if (ticks % 25 != 0) return;
		if (target == null || !(target instanceof EntityLivingBase)) return;
		if (target instanceof EntityPlayer && ((EntityPlayer) target).capabilities.disableDamage) return;
		
		EntityLivingBase entity = (EntityLivingBase) target;
		
		entity.attackEntityFrom(DamageSourceSpell.corruption, (float) Math.min((Math.random() * 2 + 3) / vt.set(x() + 0.5, y() + 0.5, z() + 0.5).add(-entity.posX, -entity.posY, -entity.posZ).mag() / 2, 2.5) * 4F);
		
		vt.set(x() + 0.5, y() + 0.5, z() + 0.5);
		ve.set(entity.posX, entity.posY, entity.posZ).normalize();
		vt.add(ve.x / 2, ve.y / 2, ve.z / 2);
		ve.set(entity.posX, entity.posY, entity.posZ);
		
		Botania.proxy.lightningFX(worldObj(), vt, ve, 1, worldObj().rand.nextLong(), 0, 0xFF0000);
	}
	
	@Override
	public int typeBits() {
		return HEALTH;
	}
	
	@Override
	public int getStrip() {
		return 1;
	}
	
	@Override
	public EnumAnomalityRarity getRarity() {
		return EnumAnomalityRarity.COMMON;
	}
}