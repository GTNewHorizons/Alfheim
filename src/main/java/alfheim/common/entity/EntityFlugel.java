package alfheim.common.entity;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import alexsocol.asjlib.ASJUtilities;
import alfheim.common.core.registry.AlfheimItems;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import vazkii.botania.api.boss.IBotaniaBoss;
import vazkii.botania.api.internal.ShaderCallback;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.api.lexicon.multiblock.component.MultiblockComponent;
import vazkii.botania.client.core.handler.BossBarHandler;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.common.Botania;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.entity.EntityMagicLandmine;
import vazkii.botania.common.entity.EntityMagicMissile;
import vazkii.botania.common.entity.EntityPixie;
import vazkii.botania.common.entity.EntityDoppleganger.BeaconBeamComponent;
import vazkii.botania.common.entity.EntityDoppleganger.BeaconComponent;
import vazkii.botania.common.entity.EntityFallingStar;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ItemStarSword;
import vazkii.botania.common.item.relic.ItemRelic;
import vazkii.botania.common.lib.LibObfuscation;

public class EntityFlugel extends EntityCreature implements IBotaniaBoss { // EntityDoppleganger

	public static final int SPAWN_TICKS = 160;
	public static final int DEATHRAY_TICKS = 600;
	private static final float RANGE = 12F;
	private static final float MAX_HP = 800F;

	private static final String TAG_INVUL_TIME = "invulTime";
	private static final String TAG_AGGRO = "aggro";
	private static final String TAG_SOURCE_X = "sourceX";
	private static final String TAG_SOURCE_Y = "sourceY";
	private static final String TAG_SOURCE_Z = "sourcesZ";
	private static final String TAG_MOB_SPAWN_TICKS = "mobSpawnTicks";
	private static final String TAG_PLAYER_COUNT = "playerCount";
	private static final String TAG_DEATHRAY = "deathray";

	List<String> playersWhoAttacked = new ArrayList();
	private static boolean isPlayingMusic = false;

	public EntityFlugel(World par1World) {
		super(par1World);
		setSize(0.6F, 1.8F);
		getNavigator().setCanSwim(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, Float.MAX_VALUE));
		isImmuneToFire = true;
		experienceValue = 1325;
	}

	public static boolean spawn(EntityPlayer player, ItemStack stack, World world, int x, int y, int z/*, boolean hard*/) {
		if(world.getTileEntity(x, y, z) instanceof TileEntityBeacon && isTruePlayer(player)) {
			if(world.difficultySetting == EnumDifficulty.PEACEFUL) {
				if(!world.isRemote) ASJUtilities.say(player, "alfheimmisc.peacefulNoob");
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

			if(!hasProperArena(world, x, y, z)) {
				if(!world.isRemote) ASJUtilities.say(player, "alfheimmisc.badArena");
				return false;
			}

			stack.stackSize--;
			if(world.isRemote) return true;

			EntityFlugel e = new EntityFlugel(world);
			e.setPosition(x + 0.5, y + 3, z + 0.5);
			e.setInvulTime(SPAWN_TICKS);
			e.setHealth(1F);
			e.setSource(x, y, z);
			e.playersWhoAttacked.add(player.getCommandSenderName());

			List<EntityPlayer> players = e.getPlayersAround();
			int playerCount = 0;
			for(EntityPlayer p : players) if(isTruePlayer(p)) playerCount++;

			e.setPlayerCount(playerCount);
			e.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth).setBaseValue(MAX_HP * playerCount);

			world.playSoundAtEntity(e, "mob.enderdragon.growl", 10F, 0.1F);
			world.spawnEntityInWorld(e);
			return true;
		}

		return false;
	}

	// TODO Check for pixies
	@Override
	public boolean attackEntityFrom(DamageSource source, float damage) {
		Entity e = source.getEntity();
		if((source.damageType.equals("player") || e instanceof EntityPixie) && e != null && isTruePlayer(e) && getInvulTime() == 0) {
			EntityPlayer player = (EntityPlayer) e;
			if(!playersWhoAttacked.contains(player.getCommandSenderName())) playersWhoAttacked.add(player.getCommandSenderName());

			boolean crit = false;
			if(e instanceof EntityPlayer) {
				EntityPlayer p = (EntityPlayer) e;
				crit = p.fallDistance > 0.0F && !p.onGround && !p.isOnLadder() && !p.isInWater() && !p.isPotionActive(Potion.blindness) && p.ridingEntity == null;
			}

			return super.attackEntityFrom(source, Math.min(crit ? 60 : 40, damage) * (getDeathrayTime() > 0 ? 0.5F : 1F));
		}
		return false;
	}

	@Override
	protected void damageEntity(DamageSource source, float damage) {
		super.damageEntity(source, damage);

		Entity attacker = source.getEntity();
		if(attacker != null) {
			Vector3 thisVector = Vector3.fromEntityCenter(this);
			Vector3 playerVector = Vector3.fromEntityCenter(attacker);
			Vector3 motionVector = thisVector.copy().sub(playerVector).copy().normalize().multiply(0.75);

			if(getHealth() > 0) {
				motionX = -motionVector.x;
				motionY = 0.5;
				motionZ = -motionVector.z;
				setTPDelay(4);
			}

			setAggroed(true);
		}
	}

	@Override
	public void onDeath(DamageSource source) {
		super.onDeath(source);
		EntityLivingBase entitylivingbase = func_94060_bK();
		// if(entitylivingbase instanceof EntityPlayer) ((EntityPlayer) entitylivingbase).addStat(ModAchievements.gaiaGuardianKill, 1); TODO new achievement

		worldObj.playSoundAtEntity(this, "random.explode", 20F, (1F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
		worldObj.spawnParticle("hugeexplosion", posX, posY, posZ, 1D, 0D, 0D);
	}

	@Override
	protected void dropFewItems(boolean byPlayer, int looting) {
		if(byPlayer) {
			for(int pl = 0; pl < playersWhoAttacked.size(); pl++) {
				// boolean hard = isHardMode();
				// entityDropItem(new ItemStack(ModItems.manaResource, pl == 0 ? /*hard ? 16 :*/ 8 : /*hard ? 10 :*/ 6, 5), 1F); TODO change special drop
				boolean droppedRecord = false;

				entityDropItem(new ItemStack(ModItems.ancientWill, 1, rand.nextInt(6)), 1F);
				
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

				// if(!droppedRecord && Math.random() < 0.2) entityDropItem(new ItemStack(hard ? ModItems.recordGaia2 : ModItems.recordGaia1), 1F); TODO add disk
			}
			
			if(ConfigHandler.relicsEnabled) {
				ItemStack dice = new ItemStack(AlfheimItems.flugelSoul);
				ItemRelic.bindToUsernameS(playersWhoAttacked.get(0), dice);
				entityDropItem(dice, 1F);
			}
		}
	}

	@Override
	public void setDead() {
		ChunkCoordinates source = getSource();
		Botania.proxy.playRecordClientSided(worldObj, source.posX, source.posY, source.posZ, null);
		isPlayingMusic = false;
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

		ChunkCoordinates source = getSource();
		float range = RANGE + 3F;
		List<EntityPlayer> players = getPlayersAround();
		int playerCount = getPlayerCount();

		if(worldObj.isRemote && !isPlayingMusic && !isDead && !players.isEmpty()) {
			Botania.proxy.playRecordClientSided(worldObj, source.posX, source.posY, source.posZ, (ItemRecord) (ModItems.recordGaia2));// TODO change music
			isPlayingMusic = true;
		}
		
		range = RANGE;
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
					double x = Math.sin(radP) * Math.cos(radY) * range;
					double y = Math.cos(radP) * range;
					double z = Math.sin(radP) * Math.sin(radY) * range;
					
					// perticle source position
					Vector3 nrm = new Vector3(x, y, z).normalize();
					
					// noraml to pos
					float radp = (pitch + 90F) * (float) Math.PI / 180F;
					double kx = Math.sin(radp) * Math.cos(radY);
					double ky = Math.cos(radp);
					double kz = Math.sin(radp) * Math.sin(radY);
					Vector3 kos = new Vector3(kx, ky, kz).normalize().rotate(Math.PI * 2 * Math.random(), nrm).multiply(0.1);
					
					float motX = (float) kos.x;
					float motY = (float) kos.y;
					float motZ = (float) kos.z;
		
					Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, X-x, Y-y, Z-z, r, g, b, 1F, motX, motY, motZ);
				}
		}

		for(EntityPlayer player : players) {
			// No beacon potions allowed!
			List<PotionEffect> remove = new ArrayList();
			Collection<PotionEffect> active = player.getActivePotionEffects();
			for(PotionEffect effect : active) if(effect.getDuration() < 200 && effect.getIsAmbient() && !ASJUtilities.isBadPotion(Potion.potionTypes[effect.getPotionID()])) remove.add(effect);
			active.removeAll(remove);

			// Get player back!
			if(vazkii.botania.common.core.helper.MathHelper.pointDistanceSpace(player.posX, player.posY, player.posZ, source.posX + 0.5, source.posY + 0.5, source.posZ + 0.5) >= RANGE) {
				ASJUtilities.say(player, "alfheimmisc.getback!");
				Vector3 sourceVector = new Vector3(source.posX + 0.5, source.posY + 0.5, source.posZ + 0.5);
				Vector3 playerVector = Vector3.fromEntityCenter(player);
				Vector3 motion = sourceVector.copy().sub(playerVector).copy().normalize();

				player.motionX = motion.x;
				player.motionY = motion.y;
				player.motionZ = motion.z;
			}
		}

		if(isDead) return;

		if (!onGround) motionY += 0.075;
		
		int invul = getInvulTime();

		if(invul > 10) spawnPatyklz(false);

		if(invul > 0) {
			if(!worldObj.isRemote) {
				setHealth(getHealth() + (getMaxHealth() - 1F) / SPAWN_TICKS);
				setInvulTime(invul - 1);
			}
			motionY = 0;
		} else {
			if(isAggroed()) { 
				boolean dying = getHealth() / getMaxHealth() <= 0.125;
				int deathray = getDeathrayTime();
				if(dying && deathray != -1) {
					if (!worldObj.isRemote) ASJUtilities.chatLog("Deathray in " + deathray);
					setPosition(source.posX + 0.5, source.posY + 3, source.posZ + 0.5);
					motionX = 0;
					motionY = 0;
					motionZ = 0;
					if (deathray > 10) spawnPatyklz(true);
					
					if (deathray == 0) setDeathrayTime(DEATHRAY_TICKS);
					else if (deathray > 0) setDeathrayTime(deathray - 1);
					
					if (deathray == 1) {
						List<EntityFallingStar> stars = new ArrayList<EntityFallingStar>(16);
						int rang = (int) Math.ceil(RANGE);
						for (int l = 0; l < 4; l++)
							for (int i = 0; i < 16;) {
								int x = rand.nextInt(rang * 2 + 1) - rang;
								int z = rand.nextInt(rang * 2 + 1) - rang;
								if (vazkii.botania.common.core.helper.MathHelper.pointDistancePlane(x, z, 0, 0) <= RANGE) {
									Vector3 posVec = new Vector3(source.posX + x, source.posY + l * 10 * 2 + 10, source.posZ + z);
									Vector3 motVec = new Vector3((Math.random() - 0.5) * 18, 24, (Math.random() - 0.5) * 18);
									posVec.add(motVec);
									motVec.normalize().negate().multiply(1.5);
										
									EntityFallingStar star = new EntityFallingStar(worldObj, this);
									star.setPosition(posVec.x, posVec.y, posVec.z);
									star.motionX = motVec.x;
									star.motionY = motVec.y;
									star.motionZ = motVec.z;
									stars.add(star);
									i++;
								}
							}
						for (EntityFallingStar star : stars) worldObj.spawnEntityInWorld(star);
						if (worldObj.isRemote) {
							for (int i = 0; i < 360; i++) {
								float r = 0.2F + (float) Math.random() * 0.3F;
								float g = (float) Math.random() * 0.3F;
								float b = 0.2F + (float) Math.random() * 0.3F;
								Botania.proxy.wispFX(worldObj, posX, posY + 1, posZ, r, g, b, 0.5F, (float) Math.cos(i) * 0.4F, 0, (float) Math.sin(i) * 0.4F);
								Botania.proxy.wispFX(worldObj, posX, posY + 1, posZ, r, g, b, 0.5F, (float) Math.cos(i) * 0.3F, 0, (float) Math.sin(i) * 0.3F);
								Botania.proxy.wispFX(worldObj, posX, posY + 1, posZ, r, g, b, 0.5F, (float) Math.cos(i) * 0.2F, 0, (float) Math.sin(i) * 0.2F);
								Botania.proxy.wispFX(worldObj, posX, posY + 1, posZ, r, g, b, 0.5F, (float) Math.cos(i) * 0.1F, 0, (float) Math.sin(i) * 0.1F);
							}
						}
						setDeathrayTime(-1);
					}
				} else if(getTPDelay() > 0 && !worldObj.isRemote) {
					if(invul > 0) setInvulTime(invul - 1);

					setTPDelay(getTPDelay() - 1);
					if(getTPDelay() == 0 && getHealth() > 0) {
						int tries = 0;
						while(!teleportRandomly() && tries < 50) tries++;
						if(tries >= 50) teleportTo(source.posX + 0.5, source.posY + 1.6, source.posZ + 0.5);

						/*if(spawnLandmines) {
							int count = dying && hard ? 7 : 6;
							for(int i = 0; i < count; i++) {
								int x = source.posX - 10 + rand.nextInt(20);
								int z = source.posZ - 10 + rand.nextInt(20);
								int y = worldObj.getTopSolidOrLiquidBlock(x, z);

								EntityMagicLandmine landmine = new EntityMagicLandmine(worldObj);
								landmine.setPosition(x + 0.5, y, z + 0.5);
								landmine.summoner = this;
								worldObj.spawnEntityInWorld(landmine);
							}

						}*/
							
						/*if(!players.isEmpty())
							for(int pl = 0; pl < playerCount; pl++)
								for(int i = 0; i < (spawnPixies ? worldObj.rand.nextInt(hard ? 6 : 3) : 1); i++) {
									EntityPixie pixie = new EntityPixie(worldObj);
									pixie.setProps(players.get(rand.nextInt(players.size())), this, 1, 8);
									pixie.setPosition(posX + width / 2, posY + 2, posZ + width / 2);
									worldObj.spawnEntityInWorld(pixie);
								}*/

						setTPDelay(/*hard ? (dying ? 35 : 45) :*/ (dying ? 40 : 60));
						/*spawnLandmines = true;
						spawnPixies = false;*/
					}
				}

				// if(spawnMissiles) spawnMissile();
			} else {
				range = 3F;
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
	
	private void spawnPatyklz(boolean c) {
		ChunkCoordinates source = getSource();
		Vector3 pos = Vector3.fromEntityCenter(this).subtract(new Vector3(0, 0.2, 0));
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
			Vector3 mot = pos.copy().sub(partPos).multiply(0.04);

			float r = (c ? 0.2F : 0.7F) + (float) Math.random() * 0.3F;
			float g = (float) Math.random() * 0.3F;
			float b = (c ? 0.7F : 0.2F) + (float) Math.random() * 0.3F;

			Botania.proxy.wispFX(worldObj, partPos.x, partPos.y, partPos.z, r, g, b, 0.25F + (float) Math.random() * 0.1F, -0.075F - (float) Math.random() * 0.015F);
			Botania.proxy.wispFX(worldObj, partPos.x, partPos.y, partPos.z, r, g, b, 0.4F, (float) mot.x, (float) mot.y, (float) mot.z);
		}
	}

	private static boolean hasProperArena(World world, int sx, int sy, int sz) {
		int range = (int) Math.ceil(RANGE);
		boolean proper = true;
		Botania.proxy.setWispFXDepthTest(false);
		for(int i = -range; i < range + 1; i++)
			for(int j = -range; j < range + 1; j++) 
				for (int k = -range; k < range + 1; k++) {
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
		float range = 15F;
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
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5);
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(MAX_HP);
		getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0);
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}
	
	@Override
	protected boolean isAIEnabled() {
		return true;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(20, 0);			// Invul Time
		dataWatcher.addObject(21, (byte) 0);	// Aggro
		dataWatcher.addObject(22, 0);			// TP Delay
		dataWatcher.addObject(23, 0);			// Source X
		dataWatcher.addObject(24, 0);			// Source Y
		dataWatcher.addObject(25, 0);			// Source Z
		dataWatcher.addObject(26, 0);			// Player count
		dataWatcher.addObject(27, 0);			// Deathray Cast
	}

	public int getInvulTime() {
		return dataWatcher.getWatchableObjectInt(20);
	}

	public boolean isAggroed() {
		return dataWatcher.getWatchableObjectByte(21) == 1;
	}

	public int getTPDelay() {
		return dataWatcher.getWatchableObjectInt(22);
	}

	public ChunkCoordinates getSource() {
		int x = dataWatcher.getWatchableObjectInt(23);
		int y = dataWatcher.getWatchableObjectInt(24);
		int z = dataWatcher.getWatchableObjectInt(25);
		return new ChunkCoordinates(x, y, z);
	}

	public int getPlayerCount() {
		return dataWatcher.getWatchableObjectInt(26);
	}
	
	public int getDeathrayTime() {
		return dataWatcher.getWatchableObjectInt(27);
	}

	public void setInvulTime(int time) {
		dataWatcher.updateObject(20, time);
	}

	public void setAggroed(boolean aggored) {
		dataWatcher.updateObject(21, (byte) (aggored ? 1 : 0));
	}

	public void setTPDelay(int delay) {
		dataWatcher.updateObject(22, delay);
	}

	public void setSource(int x, int y, int z) {
		dataWatcher.updateObject(23, x);
		dataWatcher.updateObject(24, y);
		dataWatcher.updateObject(25, z);
	}

	public void setPlayerCount(int count) {
		dataWatcher.updateObject(26, count);
	}
	
	public void setDeathrayTime(int time) {
		dataWatcher.updateObject(27, time);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setInteger(TAG_INVUL_TIME, getInvulTime());
		nbt.setBoolean(TAG_AGGRO, isAggroed());

		ChunkCoordinates source = getSource();
		nbt.setInteger(TAG_SOURCE_X, source.posX);
		nbt.setInteger(TAG_SOURCE_Y, source.posY);
		nbt.setInteger(TAG_SOURCE_Z, source.posZ);

		nbt.setInteger(TAG_PLAYER_COUNT, getPlayerCount());
		nbt.setInteger(TAG_DEATHRAY, getDeathrayTime());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		setInvulTime(nbt.getInteger(TAG_INVUL_TIME));
		setAggroed(nbt.getBoolean(TAG_AGGRO));

		int x = nbt.getInteger(TAG_SOURCE_X);
		int y = nbt.getInteger(TAG_SOURCE_Y);
		int z = nbt.getInteger(TAG_SOURCE_Z);
		setSource(x, y, z);

		if(nbt.hasKey(TAG_PLAYER_COUNT)) setPlayerCount(nbt.getInteger(TAG_PLAYER_COUNT));
		else setPlayerCount(1);
		setDeathrayTime(nbt.getInteger(TAG_DEATHRAY));
	}
	
	// EntityEnderman code below ============================================================================

	protected boolean teleportRandomly() {
		double d0 = posX + (rand.nextDouble() - 0.5D) * 64.0D;
		double d1 = posY + (rand.nextInt(64) - 32);
		double d2 = posZ + (rand.nextDouble() - 0.5D) * 64.0D;
		return teleportTo(d0, d1, d2);
	}

	// TODO remove checking whether entity can stay
	protected boolean teleportTo(double par1, double par3, double par5) {
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
			boolean flag1 = false;

			while(!flag1 && j > 0) {
				Block block = worldObj.getBlock(i, j - 1, k);

				if(block.getMaterial().blocksMovement())
					flag1 = true;
				else {
					--posY;
					--j;
				}
			}

			if(flag1) {
				setPosition(posX, posY, posZ);

				if(worldObj.getCollidingBoundingBoxes(this, boundingBox).isEmpty() && !worldObj.isAnyLiquid(boundingBox))
					flag = true;

				// Prevent out of bounds teleporting
				ChunkCoordinates source = getSource();
				if(vazkii.botania.common.core.helper.MathHelper.pointDistanceSpace(posX, posY, posZ, source.posX, source.posY, source.posZ) > 12)
					flag = false;
			}
		}

		if (!flag) {
			setPosition(d3, d4, d5);
			return false;
		} else  {
			short short1 = 128;

			for(int l = 0; l < short1; ++l)  {
				double d6 = l / (short1 - 1.0D);
				float f = (rand.nextFloat() - 0.5F) * 0.2F;
				float f1 = (rand.nextFloat() - 0.5F) * 0.2F;
				float f2 = (rand.nextFloat() - 0.5F) * 0.2F;
				double d7 = d3 + (posX - d3) * d6 + (rand.nextDouble() - 0.5D) * width * 2.0D;
				double d8 = d4 + (posY - d4) * d6 + rand.nextDouble() * height;
				double d9 = d5 + (posZ - d5) * d6 + (rand.nextDouble() - 0.5D) * width * 2.0D;
				worldObj.spawnParticle("portal", d7, d8, d9, f, f1, f2);
			}

			worldObj.playSoundEffect(d3, d4, d5, "mob.endermen.portal", 1.0F, 1.0F);
			playSound("mob.endermen.portal", 1.0F, 1.0F);
			return true;
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