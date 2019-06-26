package alfheim.client.core.handler;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.api.AlfheimAPI;
import alfheim.api.spell.SpellBase;
import alfheim.client.render.world.SpellVisualizations;
import alfheim.common.core.handler.CardinalSystem.KnowledgeSystem.Knowledge;
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

import java.io.Serializable;
import java.util.*;

public class CardinalSystemClient {
	
	private static final Minecraft mc = Minecraft.getMinecraft();
	public static PlayerSegmentClient segment;
	
	public static PlayerSegmentClient segment() {
		if (segment == null) segment = new PlayerSegmentClient();
		return segment;
	}
	
	public static class SpellCastingSystemClient {
		
		public static void setCoolDown(SpellBase spell, int cd) {
			if (spell == null) return;
			segment().coolDown.put(spell, cd);
		}
		
		public static int getCoolDown(SpellBase spell) {
			try {
				if (spell == null) return 0;
				return segment().coolDown.get(spell);
			} catch (Throwable e) {
				System.err.println(String.format("Something went wrong getting cooldown for %s. Returning 0.", spell));
				e.printStackTrace();
				return 0;
			}
		}
		
		public static void tick() {
			try {
				for (SpellBase spell : segment().coolDown.keySet()) {
					int time = segment.coolDown.get(spell);
					if (time > 0) segment().coolDown.put(spell, time - 1);
				}
				if (segment.init > 0) --segment.init;
			} catch (Throwable e) {
				System.err.println("Something went wrong ticking spells. Skipping this tick.");
				e.printStackTrace();
			}
		}
		
		public static void reset() {
			for (SpellBase spell : segment().coolDown.keySet()) {
				segment().coolDown.put(spell, 0);
			}
		}
	}
	
	public static class TargetingSystemClient {
		
		public static boolean selectMob() {
			if (mc == null || mc.thePlayer == null) return false;
			if (segment().party == null) segment.party = new Party(mc.thePlayer);
			MovingObjectPosition mop = ASJUtilities.getMouseOver(mc.thePlayer, 128, true);
			if (mop != null && mop.typeOfHit == MovingObjectType.ENTITY && mop.entityHit instanceof EntityLivingBase) {
				if (!segment.party.isMember((EntityLivingBase) mop.entityHit)) {
					boolean invis = ((EntityLivingBase) mop.entityHit).isPotionActive(Potion.invisibility) || mop.entityHit.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer);
					if (Vector3.entityDistance(mop.entityHit, mc.thePlayer) < (mop.entityHit instanceof IBossDisplayData ? 128 : 32) && !invis) {
						segment.target = (EntityLivingBase) mop.entityHit;
						segment.isParty = false;
					}
				} else ASJUtilities.say(mc.thePlayer, "alfheimmisc.teamnotmob");
			}
			return segment.target != null && !segment.isParty;
		}
		
		public static boolean selectTeam() {
			if (mc == null || mc.thePlayer == null) return false;
			segment().isParty = true;
			if (segment.party == null) segment.party = new Party(mc.thePlayer);
			while (true) {
				EntityLivingBase team = segment.party.get(PlayerSegmentClient.partyIndex = ((++PlayerSegmentClient.partyIndex) % segment.party.count));
				if (team != null && Vector3.entityDistancePlane(mc.thePlayer, segment.target = team) < (team instanceof IBossDisplayData ? 128 : 32)) break;
			}
			return segment.isParty;
		}
	}
	
	public static class TimeStopSystemClient {
		
		public static final LinkedList<TimeStopAreaClient> tsAreas = new LinkedList<TimeStopAreaClient>();
		
		public static void stop(double x, double y, double z, Party pt, int id) {
			tsAreas.add(new TimeStopAreaClient(x, y, z, pt, id));
		}
		
		public static boolean affected(Entity e) {
			if (e == null || e instanceof IBossDisplayData) return false;
			for (TimeStopAreaClient tsa : tsAreas) {
				if (Vector3.vecEntityDistance(tsa.pos, e) < 16) {
					if (e instanceof EntityLivingBase) {
						if (!tsa.cPt.isMember((EntityLivingBase) e)) return true;
					} else {
						return true;
					}
				}
			}
			return false;
		}
		
		public static boolean affected(TileEntity te) {
			if (te == null || !te.hasWorldObj()) return false;
			for (TimeStopAreaClient tsa : tsAreas)
				if (Vector3.vecTileDistance(tsa.pos, te) < 16) return true;
			return false;
		}
		
		public static boolean inside(EntityPlayer pl) {
			for (TimeStopAreaClient tsa : tsAreas) if (Vector3.vecEntityDistance(tsa.pos, pl) < 16.5) return true;
			return false;
		}
		
		public static void render() {
			for (TimeStopAreaClient tsa : tsAreas) SpellVisualizations.redSphere(tsa.pos.x, tsa.pos.y, tsa.pos.z);
		}
		
		public static void remove(int i) {
			tsAreas.remove(new TimeStopAreaClient(0, 0, 0, null, i));
		}
		
		private static class TimeStopAreaClient {
			
			public final Vector3 pos;
			public final Party cPt;
			public final int id;
			
			public TimeStopAreaClient(double x, double y, double z, Party pt, int i) {
				pos = new Vector3(x, y, z);
				cPt = pt;
				id = i;
			}
			
			@Override
			public boolean equals(Object obj) {
				if (!(obj instanceof TimeStopAreaClient)) return false;
				return ((TimeStopAreaClient) obj).id == id;
			}
		}
	}
	
	public static class PlayerSegmentClient implements Serializable {
		
		private static final long serialVersionUID = 6871678638741684L;
		
		public final HashMap<SpellBase, Integer> coolDown = new HashMap<SpellBase, Integer>();
		public int[] hotSpells = new int[12];
		// current and max spell init time (for blue bar)
		public int init, initM;
		
		public Party party;
		public static int partyIndex;
		public EntityLivingBase target;
		public boolean isParty;
		
		public static boolean[] knowledge;
		
		public final String userName;
		
		public PlayerSegmentClient() {
			for (SpellBase spell : AlfheimAPI.spells) coolDown.put(spell, 0);
			party = new Party(mc.thePlayer);
			userName = mc.thePlayer.getCommandSenderName();
			knowledge = new boolean[Knowledge.values().length];
		}
	}
}