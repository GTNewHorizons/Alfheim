package alfheim.common.core.handler;

import static alfheim.common.network.Message2d.m2d.*;
import static alfheim.common.network.Message3d.m3d.*;

import java.io.*;
import java.util.*;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.AlfheimCore;
import alfheim.api.AlfheimAPI;
import alfheim.api.entity.EnumRace;
import alfheim.api.event.EntityUpdateEvent;
import alfheim.api.event.TileUpdateEvent;
import alfheim.api.event.TimeStopCheckEvent.TimeStopEntityCheckEvent;
import alfheim.api.event.TimeStopCheckEvent.TimeStopTileCheckEvent;
import alfheim.api.spell.ITimeStopSpecific;
import alfheim.api.spell.SpellBase;
import alfheim.common.core.handler.CardinalSystem.KnowledgeSystem.Knowledge;
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party;
import alfheim.common.core.handler.CardinalSystem.TargetingSystem.Target;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.network.*;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.ICreativeManaProvider;
import vazkii.botania.api.mana.IManaItem;

public class CardinalSystem {

	public static HashMap<String, PlayerSegment> playerSegments = new HashMap<String, PlayerSegment>();
	
	public static void load(String save) {
		File file = new File(save + "/data/Cardinal.sys");
		if (!file.exists()) {
			ASJUtilities.log("Cardinal System data file not found. Generating default values...");
			playerSegments = new HashMap<String, CardinalSystem.PlayerSegment>();
			TimeStopSystem.tsAreas = new HashMap<Integer, LinkedList<alfheim.common.core.handler.CardinalSystem.TimeStopSystem.TimeStopArea>>();
			return;
		}
		
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream oin = new ObjectInputStream(fis);
			playerSegments = (HashMap<String, PlayerSegment>) oin.readObject();
			TimeStopSystem.tsAreas = (HashMap<Integer, LinkedList<alfheim.common.core.handler.CardinalSystem.TimeStopSystem.TimeStopArea>>) oin.readObject();
			oin.close();
		} catch (Throwable e) {
			ASJUtilities.error("Unable to read whole Cardinal System data. Generating default values...");
			e.printStackTrace();
			playerSegments = new HashMap<String, CardinalSystem.PlayerSegment>();
			TimeStopSystem.tsAreas = new HashMap<Integer, LinkedList<alfheim.common.core.handler.CardinalSystem.TimeStopSystem.TimeStopArea>>();
		}
		
		backport();
	}
	
	private static void backport() {
		for (PlayerSegment segment : playerSegments.values())
			for (SpellBase spell : AlfheimAPI.spells)
				if (!segment.coolDown.containsKey(spell)) segment.coolDown.put(spell, 0);
	}
	
	public static void transfer(EntityPlayerMP player) {
		SpellCastingSystem.transfer(player);
		HotSpellsSystem.transfer(player);
		PartySystem.transfer(player);
		TimeStopSystem.transfer(player, 0);
	}
	
	public static void save(String save) {
		try {
			FileOutputStream fos = new FileOutputStream(save + "/data/Cardinal.sys");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(playerSegments);
			oos.writeObject(TimeStopSystem.tsAreas);
			oos.flush();
			oos.close();
		} catch (Throwable e) {
			ASJUtilities.error("Unable to save whole Cardinal System data. Discarding. Sorry :(");
			e.printStackTrace();
		}
	}
	
	public static boolean ensureExistance(EntityPlayer player) {
		if (!playerSegments.containsKey(player.getCommandSenderName())) {
			playerSegments.put(player.getCommandSenderName(), new PlayerSegment(player));
			return false;
		}
		return true;
	}
	
	public static PlayerSegment forPlayer(EntityPlayer player) {
		if (!ASJUtilities.isServer()) throw new RuntimeException("You shouldn't access this from client");
		ensureExistance(player);
		return playerSegments.get(player.getCommandSenderName());
	}
	
	public static class KnowledgeSystem {

		public static void learn(EntityPlayerMP player, Knowledge kn) {
			PlayerSegment seg = forPlayer(player);
			if (!seg.knowledge[kn.ordinal()]) AlfheimCore.network.sendTo(new Message1d(Message1d.m1d.KNOWLEDGE, kn.ordinal()), player);
			seg.knowledge[kn.ordinal()] = true;
		}
		
		public static boolean know(EntityPlayerMP player, Knowledge kn) {
			return forPlayer(player).knowledge[kn.ordinal()];
		}
		
		public static void transfer(EntityPlayerMP player) {
			for (Knowledge kn : Knowledge.values()) if (know(player, kn)) AlfheimCore.network.sendTo(new Message1d(Message1d.m1d.KNOWLEDGE, kn.ordinal()), player);
		}
		
		public static enum Knowledge {
			GLOWSTONE
		}
	}
	
	public static class SpellCastingSystem {
		
		public static void transfer(EntityPlayerMP player) {
			for (EnumRace affinity : EnumRace.values())
				for (SpellBase spell : AlfheimAPI.getSpellsFor(affinity))
					AlfheimCore.network.sendTo(new Message2d(COOLDOWN, ((affinity.ordinal() & 0xF) << 28) | (AlfheimAPI.getSpellID(spell) & 0xFFFFFFF), getCoolDown(player, spell)), player);
		}
		
		public static int setCoolDown(EntityPlayer caster, SpellBase spell, int cd) {
			forPlayer(caster).coolDown.put(spell, cd);
			return cd;
		}
		
		public static int getCoolDown(EntityPlayer caster, SpellBase spell) {
			try {
				return forPlayer(caster).coolDown.get(spell);
			} catch (Throwable e) {
				ASJUtilities.error(String.format("Something went wrong getting cooldown for %s. Returning 0.", spell));
				e.printStackTrace();
				return 0;
			}
		}
		
		public static void tick() {
			try {
				for (PlayerSegment segment : playerSegments.values()) {
					for (SpellBase spell : segment.coolDown.keySet()) {
						int time = segment.coolDown.get(spell);
						if (time > 0) segment.coolDown.put(spell, time - 1);
					}
					
					EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(segment.userName);
					if (player != null) {
						if (segment.init > 0) --segment.init;
						else {
							if (segment.ids != 0 && segment.castableSpell != null) {
								AlfheimCore.network.sendTo(new Message2d(COOLDOWN, segment.ids, KeyBindingHandler.cast(player, (segment.ids >> 28) & 0xF, segment.ids & 0xFFFFFFF)), player);
								segment.init = segment.ids = 0;
								segment.castableSpell = null;
							}
						}
					} else {
						segment.init = segment.ids = 0;
						segment.castableSpell = null;
					}
				}
			} catch (Throwable e) {
				ASJUtilities.error("Something went wrong ticking spells. Skipping this tick.");
				e.printStackTrace();
			}
		}
		
		public static void reset() {
			for (PlayerSegment segment : playerSegments.values()) {
				for (SpellBase spell : segment.coolDown.keySet()) {
					segment.coolDown.put(spell, 0);
				}
			}
		}
	}

	public static class ManaSystem {

		public static void handleManaChange(EntityPlayer player) {
			PartySystem.getParty(player).sendMana(player, getMana(player));
		}
		
		public static int getMana(EntityPlayer player) {
			int totalMana = 0;

			IInventory mainInv = player.inventory;
			IInventory baublesInv = BotaniaAPI.internalHandler.getBaublesInventory(player);

			int invSize = mainInv.getSizeInventory();
			int size = invSize;
			if(baublesInv != null)
				size += baublesInv.getSizeInventory();

			for(int i = 0; i < size; i++) {
				boolean useBaubles = i >= invSize;
				IInventory inv = useBaubles ? baublesInv : mainInv;
				ItemStack stack = inv.getStackInSlot(i - (useBaubles ? invSize : 0));

				if(stack != null) {
					Item item = stack.getItem();

					if(item instanceof ICreativeManaProvider && ((ICreativeManaProvider) item).isCreative(stack)) return Integer.MAX_VALUE;
					
					if(item instanceof IManaItem) 
						if(!((IManaItem) item).isNoExport(stack)) {
							if ((Integer.MAX_VALUE - ((IManaItem) item).getMana(stack)) <= totalMana) return Integer.MAX_VALUE;
							else totalMana += ((IManaItem) item).getMana(stack);
						}
				}
			}
			
			return totalMana;
		}
		
		public static int getMana(EntityLivingBase mr) {
			if (!(mr instanceof EntityPlayer)) return 0;
			return getMana((EntityPlayer) mr);
		}
		
		static {
			MinecraftForge.EVENT_BUS.register(new ManaSyncHandler());
		}
		
		public static class ManaSyncHandler {
			
			@SubscribeEvent
			public void onLivingUpdate(LivingUpdateEvent e) {
				if (AlfheimCore.enableMMO && ASJUtilities.isServer() && e.entityLiving instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) e.entityLiving;
					if (player .worldObj.getTotalWorldTime() % 20 == 0) ManaSystem.handleManaChange(player);
				}
			}
		}
	}
	
	public static class TargetingSystem {
		
		public static void setTarget(EntityPlayer player, EntityLivingBase target, boolean isParty) {
			forPlayer(player).target = new Target(target, isParty);
		}
		
		public static Target getTarget(EntityPlayer player) {
			return forPlayer(player).target;
		}
		
		public static class Target {
			
			public final EntityLivingBase target;
			public final boolean isParty;
			
			public Target(EntityLivingBase t, boolean p) {
				target = t;
				isParty = p;
			}
		}
	}

	public static class PartySystem {
		
		public static void transfer(EntityPlayerMP player) {
			AlfheimCore.network.sendTo(new MessageParty(forPlayer(player).party), player);
		}

		public static void setParty(EntityPlayer player, Party party) {
			forPlayer(player).party = party;
			party.sendChanges();
		}
		
		public static Party getParty(EntityPlayer player) {
			return forPlayer(player).party;
		}
		
		public static Party getMobParty(EntityLivingBase living) {
			for (PlayerSegment segment : playerSegments.values()) 
				if (segment.party.isMember(living)) return segment.party;
			return null;
		}
		
		public static Party getUUIDParty(UUID id) {
			for (PlayerSegment segment : playerSegments.values()) 
				if (segment.party.isMember(id)) return segment.party;
			return null;
		}

		public static boolean sameParty(EntityPlayer p1, EntityLivingBase p2) {
			return getParty(p1).isMember(p2);
		}
		
		public static boolean sameParty(UUID id, EntityLivingBase e) {
			for (PlayerSegment segment : playerSegments.values()) 
				if (segment.party.isMember(id) && segment.party.isMember(e)) return true;
			return false;
		}
		
		public static boolean mobsSameParty(EntityLivingBase e1, EntityLivingBase e2) {
			for (PlayerSegment segment : playerSegments.values()) 
				if (segment.party.isMember(e1) && segment.party.isMember(e2)) return true;
			return false;
		}
		
		public static boolean friendlyFire(EntityLivingBase entityLiving, DamageSource source) {
			if (!AlfheimCore.enableMMO || source.damageType.contains("_FF")) return false;
			
			if (!ASJUtilities.isServer()) return false;
			if (source.getEntity() != null && source.getEntity() instanceof EntityPlayer) {
				Party pt = getParty((EntityPlayer) source.getEntity());
				if (pt != null && pt.isMember(entityLiving)) {
					return true;
				}
			}
			if (entityLiving instanceof EntityPlayer && source.getEntity() != null && source.getEntity() instanceof EntityLivingBase) {
				Party pt = getParty((EntityPlayer) entityLiving);
				if (pt != null && pt.isMember((EntityLivingBase) source.getEntity())) {
					return true;
				}
			}
			if (source.getEntity() != null && source.getEntity() instanceof EntityLivingBase && mobsSameParty(entityLiving, (EntityLivingBase) source.getEntity())) {
				return true;
			}
			return false;
		}
		
		public static void notifySpawn(Entity e) {
			if (e != null && e instanceof EntityLivingBase) {
				for (PlayerSegment segment : playerSegments.values()) {
					if (segment.party.isMember((EntityLivingBase) e)) {
						for (int i = 0; i < segment.party.count; i++) {
							if (segment.party.isPlayer(i)) {
								EntityLivingBase mr = segment.party.get(i);
								if (mr != null && mr instanceof EntityPlayerMP) {
									AlfheimCore.network.sendTo(new Message2d(UUID, e.getEntityId(), segment.party.indexOf((EntityLivingBase) e)), (EntityPlayerMP) mr);
								}
							}
						}
					}
				}
			}
		}
		
		public static class Party implements Serializable, Cloneable {
			
		    private static final long serialVersionUID = 84616843168484257L;

			/** Flag for server's storing functions */
			private static transient boolean serverIO = false;
			
			private Member[] members;
			public int count;
			
			public Party() {
				members = new Member[AlfheimConfig.maxPartyMembers];
			}
			
			private Party(int i) {
				members = new Member[i];
			}
			
			public Party(EntityPlayer pl) {
				this();
				members[count++] = new Member(pl.getCommandSenderName(), pl.getUniqueID(), ManaSystem.getMana(pl), true, !pl.isEntityAlive());
			}
			
			public EntityLivingBase get(int i) {
				if (members[i].isPlayer) {
					if (ASJUtilities.isServer()) {
						return MinecraftServer.getServer().getConfigurationManager().func_152612_a(members[i].name);
					} else {
						return Minecraft.getMinecraft().theWorld.getPlayerEntityByName(members[i].name);
					}
				} else { 
					if (ASJUtilities.isServer()) {
						for (WorldServer world : MinecraftServer.getServer().worldServers) {
							for (Object entity : world.loadedEntityList) {
								if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getUniqueID().equals(members[i].uuid)) {
									return (EntityLivingBase) entity;
								}
							}
						}
					} else {
						Entity e = Minecraft.getMinecraft().theWorld.getEntityByID((int) members[i].uuid.getMostSignificantBits());
						return e != null && e instanceof EntityLivingBase ? (EntityLivingBase) e : null;
					}
				}
				return null;
			}
			
			public EntityLivingBase get(String name) {
				for (int i = 0; i < count; i++)
					
					if (members[i] != null && members[i].name.equals(name))
						return get(i);
				return null;
			}
			
			public String getName(int i) {
				return members[i] != null ? members[i].name : "";
			}
			
			public int getMana(int i) {
				return members[i] != null ? members[i].mana : 0;
			}
			
			public void setMana(int i, int mana) {
				if (members[i] != null) members[i].mana = mana;
			}
			
			public EntityPlayer getPL() {
				return (EntityPlayer) get(0);
			}
			
			public int indexOf(EntityLivingBase mr) {
				if (mr == null) return -1;
				for (int i = 0; i < count; i++)
					if (mr.getUniqueID().equals(members[i].uuid))
						return i;
				return -1;
			}
			
			public int indexOf(String name) {
				if (name == null || name.isEmpty()) return -1;
				for (int i = 0; i < count; i++)
					if (name.equals(members[i].name))
						return i;
				return -1;
			}
			
			public boolean isMember(EntityLivingBase mr) {
				if (mr != null)
					for (int i = 0; i < count; i++) {
						if (ASJUtilities.isServer()) {
							if (mr.getUniqueID().equals(members[i].uuid)) return true;
						} else {
							if (mr.getEntityId() == members[i].uuid.getMostSignificantBits()) return true;
						}
							
					}
				return false;
			}
			
			public boolean isMember(UUID uuid) {
				if (uuid != null)
					for (int i = 0; i < count; i++) {
						if (ASJUtilities.isServer()) {
							if (uuid.equals(members[i].uuid)) return true;
						} else {
							if (uuid.getMostSignificantBits() == members[i].uuid.getMostSignificantBits()) return true;
						}
							
					}
				return false;
			}
			
			public boolean isPlayer(int i) {
				return members[i].isPlayer;
			}
			
			public boolean isDead(int i) {
				return members[i].isDead;
			}
			
			public void setDead(int i, boolean d) {
				if (!ASJUtilities.isServer()) members[i].isDead = d;
			}
	
			public void setDead(EntityLivingBase mr, boolean d) {
				int i = indexOf(mr);
				if (i != -1) {
					if (mr instanceof EntityPlayer) {
						members[i].isDead = d;
						sendDead(i, d);
					} else if (d) {
						remove(mr);
						for (int j = 0; j < count; j++) {
							if (members[j].isPlayer) {
								EntityLivingBase e = get(j);
								if (e != null && e instanceof EntityPlayer) ((EntityPlayer) e).addChatMessage(new ChatComponentText(String.format(StatCollector.translateToLocal("alfheimmisc.party.memberdied"), mr.getCommandSenderName())));;
							}
						}
					}
				}
			}
			
			public void setUUID(int i, int enID) {
				if (members[i] != null) members[i].uuid = new UUID(enID, enID);
			}
			
			public boolean add(EntityLivingBase mr) {
				if (mr == null) return false;
				if (indexOf(mr) != -1) return false;
				if (count >= members.length) return false;
				members[count++] = new Member(mr.getCommandSenderName(), mr.getUniqueID(), ManaSystem.getMana(mr), mr instanceof EntityPlayer, !mr.isEntityAlive());
				sendChanges();
				return true;
			}
	
			public boolean remove(EntityLivingBase mr) {
				if (mr == null) return false;
				if (mr instanceof EntityPlayer && members[0].name.equals(mr.getCommandSenderName()))
					return removePL();
				return removeSafe(mr);
			}
			
			public boolean remove(String name) {
				if (name == null || name.isEmpty()) return false;
				if (members[0].name.equals(name))
					return removePL();
				return removeSafe(name);
			}
			
			private boolean removePL() {
				setParty(getPL(), new Party(getPL()));
				for (int i = 1; i < count; i++)
					if (members[i].isPlayer) {
						members[0] = members[i];
						--count;
						for (; i < count; i++)
							members[i] = members[i+1];
						sendChanges();
					}
				
				return true;
			}
			
			private boolean removeSafe(String name) {
				if (name == null || name.isEmpty()) return false;
				EntityLivingBase mr = get(name);
				int id = indexOf(name);
				if (mr == null && id != -1 && !isPlayer(id)) {
					--count;
					for (; id < count; id++) members[id] = members[id+1];
					members[count] = null;
					
					sendChanges();
					return true;
				}
				return removeSafe(mr);
			}
			
			private boolean removeSafe(EntityLivingBase mr) {
				if (mr == null) return false;
				int id = indexOf(mr);
				if (id == -1) return false;
				--count;
				if (mr instanceof EntityPlayer)
					setParty((EntityPlayer) mr, new Party((EntityPlayer) mr));
				for (; id < count; id++) members[id] = members[id+1];
				members[count] = null;
				
				sendChanges();
				return true;
			}
			
			private void sendChanges() {
				if (ASJUtilities.isServer())
					for (int i = 0; i < count; i++) {
						EntityLivingBase e = get(i);
						if (e != null && members[i].isPlayer && e instanceof EntityPlayerMP)
							transfer((EntityPlayerMP) e);
					}
			}
			
			private void sendMana(EntityPlayer player, int mana) {
				int index = indexOf(player);
				
				for (int i = 0; i < count; i++) {
					EntityLivingBase e = get(i);
					if (e != null && members[i].isPlayer && e instanceof EntityPlayerMP)
						AlfheimCore.network.sendTo(new Message3d(PARTY_STATUS, PartyStatus.MANA.ordinal(), index, mana), (EntityPlayerMP) e);
				}
			}
			
			private void sendDead(int id, boolean d) {
				for (int i = 0; i < count; i++) {
					EntityLivingBase e = get(i);
					if (e != null && members[i].isPlayer && e instanceof EntityPlayerMP)
						AlfheimCore.network.sendTo(new Message3d(PARTY_STATUS, PartyStatus.DEAD.ordinal(), id, d ? -10 : -100), (EntityPlayerMP) e);
				}
			}
			
			public static enum PartyStatus {
				DEAD, MANA
			}
			
			public void write(ByteBuf buf) {
				buf.writeInt(members.length);
				buf.writeInt(count);
				EntityLivingBase mr;
				for (int i = 0; i < count; i++) {
					mr = get(i);
					if (serverIO) {
						buf.writeLong(mr != null ? mr.getUniqueID().getMostSignificantBits() : 0);
						buf.writeLong(mr != null ? mr.getUniqueID().getLeastSignificantBits() : 0);
					} else {
						buf.writeInt(mr != null ? mr.getEntityId() : 0);
					}
					ByteBufUtils.writeUTF8String(buf, members[i].name);
					buf.writeInt(members[i].mana);
					buf.writeBoolean(members[i].isPlayer);
					buf.writeBoolean(members[i].isDead);
				}
			}
			
			public static Party read(ByteBuf buf) {
				int size = buf.readInt();
				int count = buf.readInt();
				Party pt = new Party(size);
				pt.count = count;
				long most, least;
				for (int i = 0; i < count; i++) {
					if (serverIO) {
						most = buf.readLong();
						least = buf.readLong();
					} else {
						most = least = buf.readInt();
					}
					pt.members[i] = new Member(ByteBufUtils.readUTF8String(buf), new UUID(most, least), buf.readInt(), buf.readBoolean(), buf.readBoolean());
				}
				return pt;
			}
			
			@Override
			public Object clone() {
				Party result = new Party();
				result.members = members.clone();
				result.count = count;
				return result;
			}
	
			private static class Member implements Serializable, Cloneable {
				
			    private static final long serialVersionUID = 8416468367146381L;
				public final String name;
				public UUID uuid;
				public int mana;
				public final boolean isPlayer;
				public boolean isDead;
				
				public Member(String n, UUID id, int m, boolean p, boolean d) {
					name = n;
					uuid = id;
					mana = m;
					isPlayer = p;
					isDead = d;
				}
				
				@Override
				public Object clone() {
					return new Member(name, uuid, mana, isPlayer, isDead);
				}
			}
		}
		
		static {
			MinecraftForge.EVENT_BUS.register(new PartyThingsListener());
		}
		
		public static class PartyThingsListener {
			
			@SubscribeEvent
			public void onClonePlayer(PlayerEvent.Clone e) {
				if (AlfheimCore.enableMMO && e.wasDeath) getParty(e.entityPlayer).setDead(e.entityPlayer, false);
			}

			@SubscribeEvent
			public void onPlayerRespawn(PlayerRespawnEvent e) {
				if (AlfheimCore.enableMMO) getParty(e.player).setDead(e.player, false);
			}
		}
	}

	public static class HotSpellsSystem {
		
		public static void transfer(EntityPlayerMP player) {
			AlfheimCore.network.sendTo(new MessageHotSpellC(forPlayer(player).hotSpells), player);
		}
		
		public static int getHotSpellID(EntityPlayer player, int slot) {
			return forPlayer(player).hotSpells[slot];
		}
		
		public static void setHotSpellID(EntityPlayer player, int slot, int id) {
			forPlayer(player).hotSpells[slot] = id;
		}
	}
	
	public static class TimeStopSystem {
		
		public static HashMap<Integer, LinkedList<TimeStopArea>> tsAreas = new HashMap<Integer, LinkedList<TimeStopArea>>();
		
		public static void transfer(EntityPlayerMP player, int fromDim) {
			if (tsAreas.containsKey(fromDim)) for (TimeStopArea tsa : tsAreas.get(fromDim)) AlfheimCore.network.sendTo(new Message1d(Message1d.m1d.TIME_STOP_REMOVE, tsa.id), player);
			if (tsAreas.containsKey(player.dimension)) for (TimeStopArea tsa : tsAreas.get(player.dimension)) AlfheimCore.network.sendTo(new MessageTimeStop(PartySystem.getUUIDParty(tsa.uuid), tsa.pos.x, tsa.pos.y, tsa.pos.z, tsa.id), player);
		}
		
		public static void stop(EntityLivingBase caster) {
			caster.worldObj.playBroadcastSound(1013, (int) caster.posX, (int) caster.posY, (int) caster.posZ, 0);
			if (!tsAreas.containsKey(caster.dimension)) tsAreas.put(caster.dimension, new LinkedList<TimeStopArea>());
			tsAreas.get(caster.dimension).addLast(new TimeStopArea(caster));
			AlfheimCore.network.sendToDimension(new MessageTimeStop(PartySystem.getMobParty(caster), caster.posX, caster.posY, caster.posZ, TimeStopArea.nextID), caster.dimension);
		}
		
		public static void tick() {
			TimeStopArea tsa = null;
			for (Integer dim : tsAreas.keySet()) {
				LinkedList tsas = tsAreas.get(dim);
				Iterator<TimeStopArea> i = tsas.iterator();
				while (i.hasNext()) {
					tsa = i.next();
					if (--tsa.life <= 0) {
						i.remove();
						AlfheimCore.network.sendToDimension(new Message1d(Message1d.m1d.TIME_STOP_REMOVE, tsa.id), dim);
					}
				}
			}
		}
		
		public static boolean affected(Entity e) {
			if (e == null) return false;
			TimeStopEntityCheckEvent ev = new TimeStopEntityCheckEvent(e);
			if (MinecraftForge.EVENT_BUS.post(ev)) return ev.result;
			if (e instanceof IBossDisplayData) return false;
			if (e instanceof ITimeStopSpecific && ((ITimeStopSpecific) e).isImmune()) return false;
			if (!tsAreas.containsKey(e.dimension)) return false;
			for (TimeStopArea tsa : tsAreas.get(e.dimension)) {
				if (Vector3.vecEntityDistance(tsa.pos, e) < 16) {
					if (e instanceof ITimeStopSpecific && ((ITimeStopSpecific) e).affectedBy(tsa.uuid)) return true;
					if (e instanceof EntityLivingBase) {
						if (!PartySystem.sameParty(tsa.uuid, (EntityLivingBase) e)) return true;
					} else {
						return true;
					}
				}
			}
			return false;
		}
		
		public static boolean affected(TileEntity te) {
			if (te == null) return false;
			TimeStopTileCheckEvent e = new TimeStopTileCheckEvent(te);
			if (MinecraftForge.EVENT_BUS.post(e)) return e.result;
			if (!te.hasWorldObj()) return false;
			if (te instanceof ITimeStopSpecific && ((ITimeStopSpecific) te).isImmune()) return false;
			if (!tsAreas.containsKey(te.getWorldObj().provider.dimensionId)) return false;
			for (TimeStopArea tsa : tsAreas.get(te.getWorldObj().provider.dimensionId))
				if (Vector3.vecTileDistance(tsa.pos, te) < 16) {
					if (te instanceof ITimeStopSpecific && ((ITimeStopSpecific) te).affectedBy(tsa.uuid)) return true;
					return true;
				}
			return false;
		}
		
		private static class TimeStopArea implements Serializable {
			
		    private static final long serialVersionUID = 4146871637815241L;
		    
		    public transient static int nextID = -1;
		    public final Vector3 pos;
		    public final UUID uuid;
		    public transient final int id;
		    public int life = 1200;
		    
		    public TimeStopArea(EntityLivingBase caster) {
		    	uuid = caster.entityUniqueID;
				pos = Vector3.fromEntity(caster);
				id = ++nextID;
			}
		}
		
		static {
			MinecraftForge.EVENT_BUS.register(new TimeStopThingsListener());
		}
		
		public static class TimeStopThingsListener {
			@SubscribeEvent
			public void onPlayerChangedDimension(PlayerChangedDimensionEvent e) {
				if (AlfheimCore.enableMMO && e.player instanceof EntityPlayerMP) transfer((EntityPlayerMP) e.player, e.fromDim);
			}
			
			@SubscribeEvent
			public void onEntityUpdate(EntityUpdateEvent e) {
				if (e.entity == null || !e.entity.isEntityAlive()) return;
				if (AlfheimCore.enableMMO && ASJUtilities.isServer() && affected(e.entity)) e.setCanceled(true);
			}
			
			@SubscribeEvent
			public void onLivingUpdate(LivingUpdateEvent e) {
				if (AlfheimCore.enableMMO && ASJUtilities.isServer() && affected(e.entity)) e.setCanceled(true);
			}
			
			@SubscribeEvent
			public void onTileUpdate(TileUpdateEvent e) {
				if (AlfheimCore.enableMMO && ASJUtilities.isServer() && affected(e.tile)) e.setCanceled(true);
			}
		}
	}
	
	public static class PlayerSegment implements Serializable {
		
	    private static final long serialVersionUID = 6871678638741684L;
	    
		public HashMap<SpellBase, Integer> coolDown = new HashMap<SpellBase, Integer>();
		public int[] hotSpells = new int[12];
		
		public Party party;
		public transient Target target;
		
		public transient SpellBase castableSpell;
		public transient int ids, init;
		
		public boolean[] knowledge;
		
		public String userName;
		
		public transient int quadStage = 0;
		
		public PlayerSegment(EntityPlayer player) {
			for (SpellBase spell : AlfheimAPI.spells) coolDown.put(spell, 0);
			target = new Target(player, true);
			party = new Party(player);
			userName = player.getCommandSenderName();
			knowledge = new boolean[Knowledge.values().length];
		}
		
		private void writeObject(ObjectOutputStream out) {
			try {
				out.writeInt(coolDown.keySet().size());
				for (SpellBase spell : coolDown.keySet()) {
					out.writeUTF(spell.name);
					out.writeInt(coolDown.get(spell));
				}
				out.writeObject(hotSpells);
				out.writeObject(party);
				out.writeObject(knowledge);
				out.writeObject(userName);
				out.writeObject(quadStage);
			} catch (IOException e) {
				ASJUtilities.error("Unable to save part of Cardinal System data. Discarding. Sorry :(");
				e.printStackTrace();
			}
		}
		
		private void readObject(ObjectInputStream in) {
			try {
				int size = in.readInt();
				coolDown = new HashMap<SpellBase, Integer>(size);
				for (; size > 0; --size) {
					coolDown.put(AlfheimAPI.getSpellInstance(in.readUTF()), in.readInt());
				}
				
				hotSpells = (int[]) in.readObject();
				party = (Party) in.readObject();
				knowledge = (boolean[]) in.readObject();
				userName = (String) in.readObject();
				quadStage = (Integer) in.readObject();
			} catch (IOException e) {
				ASJUtilities.error("Unable to read part of Cardinal System data. Skipping.");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				ASJUtilities.error("Unable to find class for part of Cardinal System data. Skipping.");
				e.printStackTrace();
			}
		}
		
		private void readObjectNoData() throws ObjectStreamException {
			for (SpellBase spell : AlfheimAPI.spells) coolDown.put(spell, 0);
		}
	}
}