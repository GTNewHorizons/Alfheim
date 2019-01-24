package alfheim.common.entity.boss;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.api.ModInfo;
import alfheim.common.core.registry.AlfheimAchievements;
import alfheim.common.core.registry.AlfheimItems;
import alfheim.common.core.registry.AlfheimItems.ElvenResourcesMetas;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.core.util.DamageSourceSpell;
import alfheim.common.entity.boss.ai.flugel.AIBase;
import alfheim.common.entity.boss.ai.flugel.AIChase;
import alfheim.common.entity.boss.ai.flugel.AIDeathray;
import alfheim.common.entity.boss.ai.flugel.AIEnergy;
import alfheim.common.entity.boss.ai.flugel.AIInvul;
import alfheim.common.entity.boss.ai.flugel.AILightning;
import alfheim.common.entity.boss.ai.flugel.AIRays;
import alfheim.common.entity.boss.ai.flugel.AIRegen;
import alfheim.common.entity.boss.ai.flugel.AITask;
import alfheim.common.entity.boss.ai.flugel.AITeleport;
import alfheim.common.entity.boss.ai.flugel.AIWait;
import baubles.api.BaublesApi;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import vazkii.botania.api.boss.IBotaniaBoss;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.api.lexicon.multiblock.component.MultiblockComponent;
import vazkii.botania.client.core.handler.BossBarHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.relic.ItemRelic;

public class EntityFlugel extends EntityCreature implements IBotaniaBoss { // EntityDoppleganger

	private static final String TAG_TIME_LEFT = "timeLeft"; // from vazkii.botania.common.item.equipment.bauble.ItemFlightTiara
	
	public static final int SPAWN_TICKS = 160;
	public static final int DEATHRAY_TICKS = 200;
	public static final int RANGE = 24;
	public static final float MAX_HP = 800F;

	private static final String TAG_STAGE			= "stage";
	private static final String TAG_HARDMODE		= "hardmode";
	private static final String TAG_SOURCE_X		= "sourceX";
	private static final String TAG_SOURCE_Y		= "sourceY";
	private static final String TAG_SOURCE_Z		= "sourceZ";
	private static final String TAG_PLAYER_COUNT	= "playerCount";
	private static final String TAG_AI_TASK			= "task";
	private static final String TAG_AI				= "ai";
	private static final String TAG_AI_TIMER		= "aiTime";
	private static final String TAG_SUMMONER		= "summoner";
	private static final String TAG_ATTACKED		= "attacked";

	public static final int STAGE_AGGRO				= 1;	//100%	hp
	public static final int STAGE_MAGIC				= 2;	//60%	hp
	public static final int STAGE_DEATHRAY			= 3;	//12.5%	hp

	public HashMap<String, Integer> playersWhoAttacked = new HashMap();
	private static boolean isPlayingMusic = false;
	
	public EntityFlugel(World world) {
		super(world);
		setSize(0.6F, 1.8F);
		getNavigator().setCanSwim(true);
		initAI();
		isImmuneToFire = true;
		experienceValue = 1325;
	}

	public static boolean spawn(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, boolean hard) {
		if(world.getTileEntity(x, y, z) instanceof TileEntityBeacon) {
			if (isTruePlayer(player)) {
				if(world.difficultySetting == EnumDifficulty.PEACEFUL) {
					if(!world.isRemote) ASJUtilities.say(player, "alfheimmisc.peacefulNoob");
					return false;
				}
	
				if (((TileEntityBeacon) world.getTileEntity(x, y, z)).getLevels() < 1) {
					if(!world.isRemote) ASJUtilities.say(player, "alfheimmisc.inactive");
					return false;
				}
				
				for(int[] coords : PYLON_LOCATIONS) { // TODO change structure
					int i = x + coords[0];
					int j = y + coords[1];
					int k = z + coords[2];
	
					Block blockat = world.getBlock(i, j, k);
					int meta = world.getBlockMetadata(i, j, k);
					if(blockat != ModBlocks.pylon || meta != 2) {
						if(!world.isRemote) ASJUtilities.say(player, "alfheimmisc.needsCatalysts");
						return false;
					}
					
				}
				
				if (!ModInfo.DEV) {
					if(!hasProperArena(world, x, y, z)) {
						if(!world.isRemote) ASJUtilities.say(player, "alfheimmisc.badArena");
						return false;
					}
				}
					
				if (!hard) stack.stackSize--;
				if (world.isRemote) return true;
	
				EntityFlugel e = new EntityFlugel(world);
				e.setPosition(x + 0.5, y + 3, z + 0.5);
				e.setAITask(AITask.INVUL);
				e.setAITaskTimer(0);
				do {
					e.setHealth(1F);
				} while (e.getHealth() > 1F);
				e.setSource(x, y, z);
				e.setHardMode(hard);
				e.setSummoner(player.getCommandSenderName());
				e.playersWhoAttacked.put(player.getCommandSenderName(), 1);
	
				List<EntityPlayer> players = e.getPlayersAround();
				int playerCount = 0;
				for(EntityPlayer p : players) if(isTruePlayer(p)) playerCount++;
	
				e.setPlayerCount(playerCount);
				e.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth).setBaseValue(MAX_HP * playerCount * (hard ? 2 : 1));
	
				world.playSoundAtEntity(e, "mob.enderdragon.growl", 10F, 0.1F);
				world.spawnEntityInWorld(e);
				return true;
			}
			ASJUtilities.say(player, "alfheimmisc.fakeplayer");
			return false;
		}

		ASJUtilities.say(player, "alfheimmisc.notbeacon");
		return false;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float damage) {
		Entity e = source.getEntity();
		if((source.damageType.equals("player") || source instanceof DamageSourceSpell) && e != null && isTruePlayer(e) && !isEntityInvulnerable()) {
			EntityPlayer player = (EntityPlayer) e;
			float dmg = ModInfo.DEV ? damage : Math.min(isHardMode() ? 60 : 40, damage) * (getAITaskTimer() > 0 ? 0.1F : 1F); // this is OK
			if(!playersWhoAttacked.containsKey(player.getCommandSenderName())) playersWhoAttacked.put(player.getCommandSenderName(), 1);
			else playersWhoAttacked.put(player.getCommandSenderName(), playersWhoAttacked.get(player.getCommandSenderName()) + 1);
			reUpdate();
			return super.attackEntityFrom(source, dmg);
		}
		return false;
	}

	@Override
	public void damageEntity(DamageSource source, float damage) {
		super.damageEntity(source, damage);

		Entity attacker = source.getEntity();
		if(attacker != null) {
			Vector3 motionVector = Vector3.fromEntityCenter(this).sub(Vector3.fromEntityCenter(attacker)).normalize().mul(0.75);

			if(getHealth() > 0) {
				motionX = -motionVector.x;
				motionY = 0.5;
				motionZ = -motionVector.z;
			}

			reUpdate();
		}
	}

	@Override
	public void setHealth(float hp) {
		hp = Math.max(getHealth() - (isHardMode() ? 60F : 40F), hp);
		super.setHealth(hp);
	}
	
	@Override
	public void onDeath(DamageSource source) {
		if (isAlive()) {
			//ASJUtilities.sayToAllOPs(EnumChatFormatting.DARK_RED + "Alive onDeath. Check console.");
			ASJUtilities.warn("Alive onDeath");
			ASJUtilities.printStackTrace();
			return;
		}
		super.onDeath(source);
		EntityLivingBase entitylivingbase = func_94060_bK();
		//if(entitylivingbase instanceof EntityPlayer && isHardMode()) ((EntityPlayer) entitylivingbase).triggerAchievement(AlfheimAchievements.flugelKill);
		if (isHardMode()) for (EntityPlayer player : getPlayersAround()) player.triggerAchievement(AlfheimAchievements.flugelKill);
		
		worldObj.playSoundAtEntity(this, "random.explode", 20F, (1F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
		worldObj.spawnParticle("hugeexplosion", posX, posY, posZ, 1, 0, 0);
	}

	@Override
	public void dropFewItems(boolean byPlayer, int looting) {
		if (isAlive()) {
			// ASJUtilities.sayToAllOPs(EnumChatFormatting.DARK_RED + "Alive dropFewItems. Check console.");
			ASJUtilities.warn("Alive dropFewItems");
			ASJUtilities.printStackTrace();
			return;
		}
		if (worldObj.isRemote) return;
		if(byPlayer) {
			boolean hard = isHardMode();
			boolean lot = true;
			// For everyone
			for(String name : playersWhoAttacked.keySet()) {
				if (worldObj.getPlayerEntityByName(name) == null) continue;
				boolean droppedRecord = false;

				if (hard) {
					if (name.equals(getSummoner()) && !((EntityPlayerMP) worldObj.getPlayerEntityByName(name)).func_147099_x().hasAchievementUnlocked(AlfheimAchievements.mask)) {
						ItemStack relic = new ItemStack(AlfheimItems.mask);
						worldObj.getPlayerEntityByName(name).addStat(AlfheimAchievements.mask, 1);
						ItemRelic.bindToUsernameS(name, relic);
						entityDropItem(relic, 1F);
						lot = false;
					}
					entityDropItem(new ItemStack(ModItems.ancientWill, 1, rand.nextInt(6)), 1F);
					entityDropItem(new ItemStack(AlfheimItems.elvenResource, lot ? hard ? 8 : 4 : hard ? 5 : 3, ElvenResourcesMetas.MuspelheimEssence), 1F);
					entityDropItem(new ItemStack(AlfheimItems.elvenResource, lot ? hard ? 8 : 4 : hard ? 5 : 3, ElvenResourcesMetas.NiflheimEssence), 1F);
					lot = false;
					if(Math.random() < 0.9) entityDropItem(new ItemStack(ModItems.manaResource, 16 + rand.nextInt(12)), 1F);	// Manasteel
					if(Math.random() < 0.7) entityDropItem(new ItemStack(ModItems.manaResource, 8 + rand.nextInt(6), 1), 1F);	// Manapearl
					if(Math.random() < 0.5) entityDropItem(new ItemStack(ModItems.manaResource, 4 + rand.nextInt(3), 2), 1F);	// Manadiamond
					if(Math.random() < 0.25) entityDropItem(new ItemStack(ModItems.overgrowthSeed, rand.nextInt(3) + 1), 1F);
				
					if(Math.random() < 0.5) {
						boolean voidLotus = Math.random() < 0.3F;
						entityDropItem(new ItemStack(ModItems.blackLotus, voidLotus ? 1 : rand.nextInt(3) + 1, voidLotus ? 1 : 0), 1F);
					}
				
					int runes = rand.nextInt(6) + 1;
					for(int i = 0; i < runes; i++) if(Math.random() < 0.3) entityDropItem(new ItemStack(ModItems.rune, 2 + rand.nextInt(3), rand.nextInt(16)), 1F);
					if(Math.random() < 0.2) entityDropItem(new ItemStack(ModItems.pinkinator), 1F);
					
					if(Math.random() < 0.3) {
						int i = Item.getIdFromItem(Items.record_13);
						int j = Item.getIdFromItem(Items.record_wait);
						int k = i + rand.nextInt(j - i + 1);
						entityDropItem(new ItemStack(Item.getItemById(k)), 1F);
						droppedRecord = true;
					}

					if(!droppedRecord && Math.random() < 0.2) entityDropItem(new ItemStack(AlfheimItems.flugelDisc), 1F);
				}
			}
			
			if	(ConfigHandler.relicsEnabled && !hard) {
				ItemStack relic = new ItemStack(AlfheimItems.flugelSoul);
				if (worldObj.getPlayerEntityByName(getSummoner()) != null) worldObj.getPlayerEntityByName(getSummoner()).addStat(AlfheimAchievements.flugelSoul, 1);
				ItemRelic.bindToUsernameS(getSummoner(), relic);
				entityDropItem(relic, 1F);
			}
		}
	}

	@Override
	public void setDead() {
		if (isAlive()) {
			// ASJUtilities.sayToAllOPs(EnumChatFormatting.DARK_RED + "Alive setDead. Check console");
			ASJUtilities.warn("Someone tried to force flugel to die. They failed.");
			ASJUtilities.printStackTrace();
			ASJUtilities.warn("If the server'd crashed next tick - report this to mod author, ignore otherwise.");
			return;
		}
		ChunkCoordinates source = getSource();
		Botania.proxy.playRecordClientSided(worldObj, source.posX, source.posY, source.posZ, null);
		isPlayingMusic = false;
		if (worldObj.isRemote) BossBarHandler.setCurrentBoss(null);

		super.setDead();
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		
		if(ridingEntity != null) {
			if(ridingEntity.riddenByEntity != null)
				ridingEntity.riddenByEntity = null;
			ridingEntity = null;
		}

		if(!worldObj.isRemote && worldObj.difficultySetting == EnumDifficulty.PEACEFUL) setDead();
		
		if(worldObj.isRemote && AlfheimConfig.flugelBossBar) BossBarHandler.setCurrentBoss(this);
		
		if(!worldObj.isRemote) {
			int radius = 1;
			int posXInt = MathHelper.floor_double(posX);
			int posYInt = MathHelper.floor_double(posY);
			int posZInt = MathHelper.floor_double(posZ);
			for(int i = -radius; i < radius + 1; i++)
				for(int j = -radius; j < radius + 1; j++)
					for(int k = -radius; k < radius + 1; k++) {
						int xp = posXInt + i;
						int yp = posYInt + j;
						int zp = posZInt + k;
						if(isCheatyBlock(worldObj, xp, yp, zp)) {
							Block block = worldObj.getBlock(xp, yp, zp);
							List<ItemStack> items = block.getDrops(worldObj, xp, yp, zp, 0, 0);
							for(ItemStack stack : items) {
								if(ConfigHandler.blockBreakParticles) worldObj.playAuxSFX(2001, xp, yp, zp, Block.getIdFromBlock(block) + (worldObj.getBlockMetadata(xp, yp, zp) << 12));
								worldObj.spawnEntityInWorld(new EntityItem(worldObj, xp + 0.5, yp + 0.5, zp + 0.5, stack));
							}
							worldObj.setBlockToAir(xp, yp, zp);
						}
					}
		}

		if (playersWhoAttacked.isEmpty()) playersWhoAttacked.put(getSummoner(), 1);
		ChunkCoordinates source = getSource();
		List<EntityPlayer> players = getPlayersAround();
		int playerCount = getPlayerCount();
		if (players.isEmpty() && getAITask() != AITask.NONE) dropState();
		
		if(worldObj.isRemote && !isPlayingMusic && !isDead && !players.isEmpty()) {
			Botania.proxy.playRecordClientSided(worldObj, source.posX, source.posY, source.posZ, (ItemRecord) (AlfheimItems.flugelDisc));
			isPlayingMusic = true;
		}
		
		if (ticksExisted % 20 == 0) {
			// PARTYKLZ!!!
			int mod = 10;
			for(int pitch = 0; pitch <= 180; pitch += mod)
				for(int yaw = 0; yaw < 360; yaw += mod) {
					// color
					float r = 0.5F;
					float g = 0F;
					float b = 1F;
		
					// angle in rads
					float radY = yaw * (float) Math.PI / 180F;
					float radP = pitch * (float) Math.PI / 180F;
					
					// world coords
					double X = source.posX + 0.5;
					double Y = source.posY + 0.5;
					double Z = source.posZ + 0.5;
					
					// local coords
					double x = Math.sin(radP) * Math.cos(radY) * RANGE;
					double y = Math.cos(radP) * RANGE;
					double z = Math.sin(radP) * Math.sin(radY) * RANGE;
					
					// perticle source position
					Vector3 nrm = new Vector3(x, y, z).normalize();
					
					// noraml to pos
					float radp = (pitch + 90F) * (float) Math.PI / 180F;
					double kx = Math.sin(radp) * Math.cos(radY);
					double ky = Math.cos(radp);
					double kz = Math.sin(radp) * Math.sin(radY);
					Vector3 kos = new Vector3(kx, ky, kz).normalize().rotate(Math.toRadians(Math.PI * 2 * Math.random()), nrm).mul(0.1);
					
					float motX = (float) kos.x;
					float motY = (float) kos.y;
					float motZ = (float) kos.z;
		
					Botania.proxy.wispFX(worldObj, X-x, Y-y, Z-z, r, g, b, 1F, motX, motY, motZ);
				}
		}

		for(EntityPlayer player : players) {
			// No beacon potions allowed!
			List<PotionEffect> remove = new ArrayList();
			Collection<PotionEffect> active = player.getActivePotionEffects();
			for(PotionEffect effect : active) if(effect.getDuration() < 200 && effect.getIsAmbient() && !Potion.potionTypes[effect.getPotionID()].isBadEffect) remove.add(effect);
			active.removeAll(remove);

			// remove player
			IInventory baubles = BaublesApi.getBaubles(player);
			ItemStack tiara = baubles.getStackInSlot(0);
			if (tiara != null && tiara.getItem().equals(ModItems.flightTiara) && tiara.getItemDamage() == 1)
				ItemNBTHelper.setInt(tiara, TAG_TIME_LEFT, 1200);
			else {
				if (!worldObj.isRemote) ASJUtilities.say(player, "alfheimmisc.notallowed");
				ChunkCoordinates bed = player.getBedLocation(player.dimension);
				if (bed == null) bed = player.worldObj.getSpawnPoint();
				player.setPositionAndUpdate(bed.posX, bed.posY, bed.posZ);
				continue;
			}
			
			// Get player back!
			if(vazkii.botania.common.core.helper.MathHelper.pointDistanceSpace(player.posX, player.posY, player.posZ, source.posX + 0.5, source.posY + 0.5, source.posZ + 0.5) >= RANGE) {
				Vector3 motion = new Vector3(source.posX + 0.5, source.posY + 0.5, source.posZ + 0.5).sub(Vector3.fromEntityCenter(player)).normalize();

				player.motionX = motion.x;
				player.motionY = motion.y;
				player.motionZ = motion.z;
			}
		}

		if(isDead) return;

		if (!onGround) motionY += 0.075;
		
		int invul = isEntityInvulnerable() ? getAITaskTimer() : 0;

		if(invul > 10) spawnPatyklz(false);

		if(!(invul > 0)) {
			if(vazkii.botania.common.core.helper.MathHelper.pointDistanceSpace(posX, posY, posZ, source.posX, source.posY, source.posZ) > RANGE) teleportTo(source.posX + 0.5, source.posY + 1.6, source.posZ + 0.5);
			if(isAggroed()) { 
				if(getAITask().equals(AITask.NONE)) reUpdate();
				if(getAITask() != AITask.INVUL && getHealth() / getMaxHealth() <= 0.6 && getStage() < STAGE_MAGIC) setStage(STAGE_MAGIC);
				if(isDying() && getStage() < STAGE_DEATHRAY && getAITask() != AITask.DEATHRAY) {
					setAITask(AITask.DEATHRAY);
					setAITaskTimer(0);
				}
			} else {
				int range = 3;
				players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(posX - range, posY - range, posZ - range, posX + range, posY + range, posZ + range));
				if(!players.isEmpty()) damageEntity(DamageSource.causePlayerDamage(players.get(0)), 0);
			}
		}
	}
	
	/*	================================	UTILITY STUFF	================================	*/

	private static final int[][] PYLON_LOCATIONS = new int[][] {
		{ 4, 1, 4 },
		{ 4, 1, -4 },
		{ -4, 1, 4 },
		{ -4, 1, -4 }
	};

	private static final List<String> CHEATY_BLOCKS = Arrays.asList(new String[] {
			"OpenBlocks:beartrap",
			"ThaumicTinkerer:magnet"
	});
	
	public void spawnPatyklz(boolean c) {
		ChunkCoordinates source = getSource();
		Vector3 pos = Vector3.fromEntityCenter(this).sub(0, 0.2, 0);
		for(int i = 0; i < PYLON_LOCATIONS.length; i++) {
			int[] arr = PYLON_LOCATIONS[i];
			int x = arr[0];
			int y = arr[1];
			int z = arr[2];

			Vector3 pylonPos = new Vector3(source.posX + x, source.posY + y, source.posZ + z);
			double worldTime = ticksExisted;
			worldTime /= 5;

			float rad = 0.75F + (float) Math.random() * 0.05F;
			double xp = pylonPos.x + 0.5 + Math.cos(worldTime) * rad;
			double zp = pylonPos.z + 0.5 + Math.sin(worldTime) * rad;

			Vector3 partPos = new Vector3(xp, pylonPos.y, zp);
			Vector3 mot = pos.copy().sub(partPos).mul(0.04);

			float r = (c ? 0.2F : 0.7F) + (float) Math.random() * 0.3F;
			float g = (float) Math.random() * 0.3F;
			float b = (c ? 0.7F : 0.2F) + (float) Math.random() * 0.3F;

			Botania.proxy.wispFX(worldObj, partPos.x, partPos.y, partPos.z, r, g, b, 0.25F + (float) Math.random() * 0.1F, -0.075F - (float) Math.random() * 0.015F);
			Botania.proxy.wispFX(worldObj, partPos.x, partPos.y, partPos.z, r, g, b, 0.4F, (float) mot.x, (float) mot.y, (float) mot.z);
		}
	}

	private static boolean hasProperArena(World world, int sx, int sy, int sz) {
		boolean proper = true;
		Botania.proxy.setWispFXDepthTest(false);
		for(int i = -RANGE; i < RANGE + 1; i++)
			for(int j = -RANGE; j < RANGE + 1; j++) 
				for (int k = -RANGE; k < RANGE + 1; k++) {
					if((k == -1 && i > -2 && i < 2 && j > -2 && j < 2) || (k == 1 && Math.abs(i) == 4 && Math.abs(j) == 4) || (k == 0 && i == 0 && j == 0) || vazkii.botania.common.core.helper.MathHelper.pointDistancePlane(i, j, 0, 0) > RANGE)
						continue; // Ignore pylons, beacon and out of circle
	
					int x = sx + i;
					int y = sy + k;
					int z = sz + j;
					boolean isAir = world.getBlock(x, y, z).getCollisionBoundingBoxFromPool(world, x, y, z) == null;
					if (!isAir) {
						proper = false;
						Botania.proxy.wispFX(world, x + 0.5, y + 0.5, z + 0.5, 1, 0, 0, 0.5F, 0, 10);
					}
				}
		Botania.proxy.setWispFXDepthTest(true);
		return proper;
	}

	public List<EntityPlayer> getPlayersAround() {
		ChunkCoordinates source = getSource();
		int range = RANGE + 3;
		List<EntityPlayer> players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(source.posX + 0.5 - range, source.posY + 0.5 - range, source.posZ + 0.5 - range, source.posX + 0.5 + range, source.posY + 0.5 + range, source.posZ + 0.5 + range));
		return players;
	}
	
	public static boolean isCheatyBlock(World world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		String name = Block.blockRegistry.getNameForObject(block);
		return CHEATY_BLOCKS.contains(name);
	}

	private static final Pattern FAKE_PLAYER_PATTERN = Pattern.compile("^(?:\\[.*\\])|(?:ComputerCraft)$");
	
	public static boolean isTruePlayer(Entity e) {
		if(!(e instanceof EntityPlayer)) return false;

		EntityPlayer player = (EntityPlayer) e;

		String name = player.getCommandSenderName();
		return !(player instanceof FakePlayer || FAKE_PLAYER_PATTERN.matcher(name).matches());
	}
	
	/*	================================	AI and Data STUFF	================================	*/
	
	@Override
	public void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5);
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(MAX_HP);
		getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0);
	}

	@Override
	public boolean canDespawn() {
		return false;
	}
	
	@Override
	public boolean isAIEnabled() {
		return true;
	}

	@Override
	public void entityInit() {
		super.entityInit();
		dataWatcher.addObject(21, (byte) 0);						// Stage
		dataWatcher.addObject(22, (byte) 0);						// Hard mode
		dataWatcher.addObject(23, new ChunkCoordinates(0, 0, 0));	// Source position
		dataWatcher.addObject(24, 0);								// Player count
		dataWatcher.addObject(25, 0);								// AI task timer
		dataWatcher.addObject(26, "");								// Summoner
		dataWatcher.addObject(27, 0);								// Current AI task
	}
	
	@Override
	public boolean isEntityInvulnerable() {
		return !getPlayersAround().isEmpty() && getAITask() == AITask.INVUL && getAITaskTimer() > 0;
	}

	public void initAI() {
		tasks.taskEntries.clear();
		int i = 0;
		tasks.addTask(i++, new EntityAIWatchClosest(this, EntityPlayer.class, Float.MAX_VALUE));
		tasks.addTask(i, new AITeleport(this, AITask.TP));
		tasks.addTask(i, new AIChase(this, AITask.CHASE));
		tasks.addTask(i, new AIRegen(this, AITask.REGEN));
		tasks.addTask(i, new AILightning(this, AITask.LIGHTNING));
		tasks.addTask(i, new AIRays(this, AITask.RAYS));
		tasks.addTask(i, new AIEnergy(this, AITask.DARK));
		tasks.addTask(i++, new AIDeathray(this, AITask.DEATHRAY));
		tasks.addTask(i++, new AIInvul(this, AITask.INVUL));
		tasks.addTask(i++, new AIWait(this, AITask.NONE));
	}
	
	public int getStage() {
		return dataWatcher.getWatchableObjectByte(21);
	}
	
	public boolean isHardMode() {
		return dataWatcher.getWatchableObjectByte(22) > 0;
	}
	
	public ChunkCoordinates getSource() {
		return (ChunkCoordinates)dataWatcher.getWatchedObject(23).getObject();
	}

	public int getPlayerCount() {
		return dataWatcher.getWatchableObjectInt(24);
	}
	
	public int getAITaskTimer() {
		return dataWatcher.getWatchableObjectInt(25);
	}
	
	public String getSummoner() {
		return dataWatcher.getWatchableObjectString(26);
	}
	
	public AITask getAITask() {
		return AITask.values()[dataWatcher.getWatchableObjectInt(27)]; 
	}
	
	// --------------------------------------------------------
	
	public void setStage(int stage) {
		dataWatcher.updateObject(21, (byte) stage);
	}

	public void setHardMode(boolean hard) {
		dataWatcher.updateObject(22, hard ? (byte) 1 : (byte) 0);
	}
	
	public void setSource(int x, int y, int z) {
		dataWatcher.updateObject(23, new ChunkCoordinates(x, y, z));
	}

	public void setPlayerCount(int count) {
		dataWatcher.updateObject(24, count);
	}
	
	public void setAITaskTimer(int time) {
		dataWatcher.updateObject(25, time);
	}
	
	public void setSummoner(String summoner) {
		dataWatcher.updateObject(26, summoner);
	}
	
	public void setAITask(AITask ai) {
		if (ModInfo.DEV) for (EntityPlayer player : getPlayersAround()) ASJUtilities.say(player, "Set AI command to " + ai.toString());
		dataWatcher.updateObject(27, ai.ordinal());
	}
	
	// --------------------------------------------------------
	
	public boolean isAggroed() {
		return dataWatcher.getWatchableObjectByte(21) > 0;
	}
	
	public boolean isAlive() {
		return getHealth() > 0 && worldObj.difficultySetting != EnumDifficulty.PEACEFUL && !worldObj.isRemote && ASJUtilities.isServer();
	}
	
	public boolean isDying() {
		return getAITask() != AITask.INVUL && getHealth() / getMaxHealth() <= 0.125;
	}
	
	public void dropState() {
		if (worldObj.isRemote) return;
		ChunkCoordinates source = getSource();
		teleportTo(source.posX + 0.5, source.posY + 1.6, source.posZ + 0.5);
		setStage(0);
		setHealth(getMaxHealth());
		setAITask(AITask.NONE);
		setAITaskTimer(0);
		playersWhoAttacked.clear();
		playersWhoAttacked.put(getSummoner(), 1);
	}
	
	public void reUpdate() {
		if (worldObj.isRemote) return;
		if (getStage() < 0) setStage(-getStage());
		else if (getStage() == 0) setStage(STAGE_AGGRO);
		if (getAITask() == AITask.NONE) {
			setAITaskTimer(0);
			setAITask(nextTask());
		}
	}
	
	public AITask nextTask() {
		if (getStage() < STAGE_AGGRO) return AITask.NONE;
		AITask next = AITask.values()[rand.nextInt(AITask.values().length)];
		if (next.instant && getAITask().instant && getAITask().equals(next)) return nextTask();
		if (Math.random() < next.chance) return nextTask();
		if (getStage() < next.stage) return nextTask();
		return next;
	}
	
	public boolean isCasting() {
		return getAITask().instant;
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setInteger(TAG_STAGE, getStage());
		nbt.setBoolean(TAG_HARDMODE, isHardMode());
		
		ChunkCoordinates source = getSource();
		nbt.setInteger(TAG_SOURCE_X, source.posX);
		nbt.setInteger(TAG_SOURCE_Y, source.posY);
		nbt.setInteger(TAG_SOURCE_Z, source.posZ);

		nbt.setInteger(TAG_PLAYER_COUNT, getPlayerCount());
		nbt.setInteger(TAG_AI_TASK , getAITask().ordinal());
		nbt.setInteger(TAG_AI_TIMER, getAITaskTimer());
		
		for (Object ai : tasks.executingTaskEntries) if (((EntityAITaskEntry)ai).action instanceof AIBase) {
			String[] path = ((EntityAITaskEntry)ai).action.getClass().getName().split("\\.");
			nbt.setString(TAG_AI, path[path.length - 1]);
		}
		
		nbt.setString(TAG_SUMMONER, getSummoner());
		
		NBTTagCompound map = new NBTTagCompound();
		for (Entry<String, Integer> e : playersWhoAttacked.entrySet()) map.setInteger(e.getKey(), e.getValue());
		nbt.setTag(TAG_ATTACKED, map);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		setStage(nbt.getInteger(TAG_STAGE));
		setHardMode(nbt.getBoolean(TAG_HARDMODE));
		
		int x = nbt.getInteger(TAG_SOURCE_X);
		int y = nbt.getInteger(TAG_SOURCE_Y);
		int z = nbt.getInteger(TAG_SOURCE_Z);
		setSource(x, y, z);

		if(nbt.hasKey(TAG_PLAYER_COUNT)) setPlayerCount(nbt.getInteger(TAG_PLAYER_COUNT));
		else setPlayerCount(1);
		String TAG_AI_TASK = "task";
		setAITask(AITask.values()[nbt.getInteger(TAG_AI_TASK)]);
		
		//if (ModInfo.DEV) ASJUtilities.log("Scrolling AIs for " + nbt.getString(TAG_AI));
		for (Object e : tasks.taskEntries) {
			//if (ModInfo.DEV) ASJUtilities.log("At " + ((EntityAITaskEntry) e).action.getClass().getName());
			String[] path = ((EntityAITaskEntry)e).action.getClass().getName().split("\\.");
			if (((EntityAITaskEntry) e).action instanceof AIBase && path[path.length-1].equals(nbt.getString(TAG_AI))) {
				tasks.executingTaskEntries.add(e);
			}
		}
		
		setAITaskTimer(nbt.getInteger(TAG_AI_TIMER));
		setSummoner(nbt.getString(TAG_SUMMONER));
		
		NBTTagCompound map = nbt.getCompoundTag(TAG_ATTACKED);
		for (Object o : map.func_150296_c()) playersWhoAttacked.put((String) o, map.getInteger((String) o));
	}
	
	// EntityEnderman code below ============================================================================
	
	public boolean teleportRandomly() {
		double d0 = posX + (rand.nextDouble() - 0.5) * RANGE / 2.0;
		double d1 = posY + (rand.nextInt(64) - 32);
		double d2 = posZ + (rand.nextDouble() - 0.5) * RANGE / 2.0;
		return teleportTo(d0, d1, d2);
	}
	
	public boolean teleportTo(double par1, double par3, double par5) {
		double d3 = posX;
		double d4 = posY;
		double d5 = posZ;
		posX = par1;
		posY = par3;
		posZ = par5;
		boolean flag = false;
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(posY);
		int k = MathHelper.floor_double(posZ);

		if(worldObj.blockExists(i, j, k)) {
			setPosition(posX, posY, posZ);
			motionX = motionY = motionZ = 0;
			
			if(worldObj.getCollidingBoundingBoxes(this, boundingBox).isEmpty() && !worldObj.isAnyLiquid(boundingBox)) flag = true;

			// Prevent out of bounds teleporting
			ChunkCoordinates source = getSource();
			if(vazkii.botania.common.core.helper.MathHelper.pointDistanceSpace(posX, posY, posZ, source.posX, source.posY, source.posZ) > RANGE) flag = false;
		}

		if (!flag) {
			setPosition(d3, d4, d5);
			return false;
		} else {
			short short1 = 128;

			for(int l = 0; l < short1; ++l)  {
				double d6 = l / (short1 - 1.0);
				float f = (rand.nextFloat() - 0.5F) * 0.2F;
				float f1 = (rand.nextFloat() - 0.5F) * 0.2F;
				float f2 = (rand.nextFloat() - 0.5F) * 0.2F;
				double d7 = d3 + (posX - d3) * d6 + (rand.nextDouble() - 0.5) * width * 2.0;
				double d8 = d4 + (posY - d4) * d6 + rand.nextDouble() * height;
				double d9 = d5 + (posZ - d5) * d6 + (rand.nextDouble() - 0.5) * width * 2.0;
				worldObj.spawnParticle("portal", d7, d8, d9, f, f1, f2);
			}

			worldObj.playSoundEffect(d3, d4, d5, "mob.endermen.portal", 1.0F, 1.0F);
			playSound("mob.endermen.portal", 1.0F, 1.0F);
			return true;
		}
	}
	
	// EntityFireball code below ============================================================================
	
	public void checkCollision() {
		Vec3 vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
		Vec3 vec31 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		MovingObjectPosition mop = this.worldObj.rayTraceBlocks(vec3, vec31);
		vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
		vec31 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

		if (mop != null) {
			vec31 = Vec3.createVectorHelper(mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord);
		}

		Entity entity = null;
		List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0, 1.0, 1.0));
		double d0 = 0.0;

		for (int i = 0; i < list.size(); ++i) {
			Entity entity1 = (Entity)list.get(i);

			if (entity1.canBeCollidedWith()) {
				float f = 0.3F;
				AxisAlignedBB axisalignedbb = entity1.boundingBox.expand((double)f, (double)f, (double)f);
				MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);

				if (movingobjectposition1 != null) {
					double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

					if (d1 < d0 || d0 == 0.0) {
						entity = entity1;
						d0 = d1;
					}
				}
			}
		}

		if (entity != null) {
			mop = new MovingObjectPosition(entity);
		}

		if (mop != null) {
			this.onImpact(mop);
		}
	}

	private void onImpact(MovingObjectPosition mop) {
		switch (mop.typeOfHit) {
			case BLOCK: if (onGround) motionY += 0.5; break;
			case ENTITY: {
				if (mop.entityHit != null && mop.entityHit instanceof EntityPlayer) mop.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this), isHardMode() ? 15.0F : 10.0F);
				break;
			}
			case MISS: break;
		}
	}
	
	/*	================================	HEALTHBAR STUFF	================================	*/

	@Override
	@SideOnly(Side.CLIENT)
	public ResourceLocation getBossBarTexture() {
		return BossBarHandler.defaultBossBar;
	}
	
	@SideOnly(Side.CLIENT)
	private static Rectangle barRect;
	@SideOnly(Side.CLIENT)
	private static Rectangle hpBarRect;

	@Override
	@SideOnly(Side.CLIENT)
	public Rectangle getBossBarTextureRect() {
		if(barRect == null)
			barRect = new Rectangle(0, 0, 185, 15);
		return barRect;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Rectangle getBossBarHPTextureRect() {
		if(hpBarRect == null)
			hpBarRect = new Rectangle(0, barRect.y + barRect.height, 181, 7);
		return hpBarRect;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void bossBarRenderCallback(ScaledResolution res, int x, int y) {
		GL11.glPushMatrix();
		int px = x + 160;
		int py = y + 12;

		Minecraft mc = Minecraft.getMinecraft();
		ItemStack stack = new ItemStack(Items.skull, 1, 3);
		mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);
		net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		RenderItem.getInstance().renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, stack, px, py);
		net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

		boolean unicode = mc.fontRenderer.getUnicodeFlag();
		mc.fontRenderer.setUnicodeFlag(true);
		mc.fontRenderer.drawStringWithShadow("" + getPlayerCount(), px + 15, py + 4, 0xFFFFFF);
		mc.fontRenderer.setUnicodeFlag(unicode);
		GL11.glPopMatrix();
	}
	
	/*	================================	LEXICON STUFF	================================	*/

	public static MultiblockSet makeMultiblockSet() {
		Multiblock mb = new Multiblock();

		for(int[] p : PYLON_LOCATIONS)
			mb.addComponent(p[0], p[1] + 1, p[2], ModBlocks.pylon, 2);

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
				mb.addComponent(new BeaconComponent(new ChunkCoordinates(i - 1, 0, j - 1)));

		mb.addComponent(new BeaconBeamComponent(new ChunkCoordinates(0, 1, 0)));
		mb.setRenderOffset(0, -1, 0);

		return mb.makeSet();
	}
	
	public static class BeaconComponent extends MultiblockComponent {
		public BeaconComponent(ChunkCoordinates relPos) {
			super(relPos, Blocks.iron_block, 0);
		}

		@Override
		public boolean matches(World world, int x, int y, int z) {
			return world.getBlock(x, y, z).isBeaconBase(world, x, y, z, x - relPos.posX, y - relPos.posY, z - relPos.posZ);
		};

	}

	public static class BeaconBeamComponent extends MultiblockComponent {
		public BeaconBeamComponent(ChunkCoordinates relPos) {
			super(relPos, Blocks.beacon, 0);
		}

		@Override
		public boolean matches(World world, int x, int y, int z) {
			return world.getTileEntity(x, y, z) instanceof TileEntityBeacon;
		}
	}
}