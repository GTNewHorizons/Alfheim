package alfheim.common.spell.earth;

import static org.lwjgl.opengl.GL11.*;

import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.api.entity.EnumRace;
import alfheim.api.lib.LibResourceLocations;
import alfheim.api.spell.SpellBase;
import alfheim.client.render.world.SpellEffectHandlerClient.Spells;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.core.handler.SpellEffectHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;

public class SpellHammerfall extends SpellBase {

	public SpellHammerfall() {
		super("hammerfall", EnumRace.GNOME, 10000, 200, 20);
	}

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		if (!caster.onGround || caster.worldObj.isAirBlock(MathHelper.floor_double(caster.posX), MathHelper.floor_double(caster.posY) - 1, MathHelper.floor_double(caster.posZ))) return SpellCastResult.WRONGTGT;
		
		SpellCastResult result = checkCast(caster);
		if (result != SpellCastResult.OK) return result;
		
		SpellEffectHandler.sendPacket(Spells.TREMORS, caster);

		List<EntityLivingBase> list = caster.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, caster.boundingBox.expand(10, 2, 10));
		list.remove(caster);
		for (EntityLivingBase living : list) {
			Block block = living.worldObj.getBlock(MathHelper.floor_double(living.posX), MathHelper.floor_double(living.posY-1), MathHelper.floor_double(living.posZ));
			if (living.onGround &&
				block.getMaterial().isSolid() &&
				block.isSideSolid(living.worldObj, MathHelper.floor_double(living.posX), MathHelper.floor_double(living.posY-1), MathHelper.floor_double(living.posZ), ForgeDirection.UP) &&
				block.getBlockHardness(living.worldObj, MathHelper.floor_double(living.posX), MathHelper.floor_double(living.posY-1), MathHelper.floor_double(living.posZ)) < 2 &&
				!PartySystem.mobsSameParty(caster, living) &&
				Vector3.entityDistancePlane(living, caster) < 10)
				living.attackEntityFrom(DamageSource.inWall, 10.0F);
		}
		return result;
	}
	
	@Override
	public void render(EntityLivingBase caster) {
		double s = 10;
		glDisable(GL_CULL_FACE);
        glAlphaFunc(GL_GREATER, 0.003921569F);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glTranslated(0, -1.61, 0);
		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.target);
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.addVertexWithUV(caster.posX - s, caster.posY, caster.posZ - s, 0, 0);
		Tessellator.instance.addVertexWithUV(caster.posX - s, caster.posY, caster.posZ + s, 0, 1);
		Tessellator.instance.addVertexWithUV(caster.posX + s, caster.posY, caster.posZ + s, 1, 1);
		Tessellator.instance.addVertexWithUV(caster.posX + s, caster.posY, caster.posZ - s, 1, 0);
		Tessellator.instance.draw();
		glDisable(GL_BLEND);
        glAlphaFunc(GL_GREATER, 0.1F);
		glEnable(GL_CULL_FACE);
	}
}