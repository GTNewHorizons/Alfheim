package alfheim.common.block.tile;

import org.lwjgl.opengl.GL11;

import alexsocol.asjlib.extendables.ItemContainingTileEntity;
import alexsocol.asjlib.math.Vector3;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.core.registry.AlfheimItems;
import alfheim.common.core.registry.AlfheimItems.ElvenResourcesMetas;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IDirectioned;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.wand.IWandBindable;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.entity.EntityManaBurst;

public class TileTransferer extends ItemContainingTileEntity implements IDirectioned, IManaReceiver, IWandBindable {

	// BlockTransferer BlockSpreader TileSpreader RenderSpreader RenderTileSpreader ItemLens
	private static final String TAG_STACK = "transfering";
	
	public static final int MAX_MANA = 10000;
	public static final String TAG_MANA = "mana", TAG_KNOWN_MANA = "knownMana", TAG_ROTATION_X = "rotationX", TAG_ROTATION_Y = "rotationY", TAG_TO_X = "toX", TAG_TO_Y = "toY", TAG_TO_Z = "toZ", TAG_REDSTONE = "redstone";
	
	public float rotationX, rotationY;
	public int mana, knownMana = -1;
	public boolean redstoneLastTick = true;
	public int toX = -1, toY = -1, toZ = -1;
	
	@Override
	public void updateEntity() {
		boolean redstone = false;

		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tileAt = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);

			int redstoneSide = worldObj.getIndirectPowerLevelTo(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ, dir.ordinal());
			if(redstoneSide > 0)
				redstone = true;
		}

		if(redstone && !redstoneLastTick) tryShootBurst();

		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, xCoord, yCoord, zCoord);

		redstoneLastTick = redstone;
	}
	
	public void tryShootBurst() {
		if (isBound() && this.getItem() != null && mana == MAX_MANA) {
			EntityManaBurst burst = getBurst();
			if (!worldObj.isRemote) worldObj.spawnEntityInWorld(burst);
			if (!ConfigHandler.silentSpreaders) worldObj.playSoundEffect(xCoord, yCoord, zCoord, "botania:spreaderFire", 0.05F, 0.7F + 0.3F * (float) Math.random());
		}
	}
	
	public EntityManaBurst getBurst() {
		EntityManaBurst burst = new EntityManaBurst(worldObj);

		burst.setColor(0xCCFF00);
		burst.setMana(10000);
		burst.setStartingMana(1);
		burst.setMinManaLoss(1);
		burst.setManaLossPerTick(1);
		burst.setGravity(0);
		burst.setBurstSourceCoords(xCoord, yCoord, zCoord);
		setStack(burst, getItem().copy());
		setItem(null);
		
		burst.setLocationAndAngles(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, -(getRotationX() + 90), getRotationY());
		float f = 0.4F;
		double mx = MathHelper.sin(burst.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(burst.rotationPitch / 180.0F * (float) Math.PI) * f / 2.0;
		double mz = -(MathHelper.cos(burst.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(burst.rotationPitch / 180.0F * (float) Math.PI) * f) / 2.0;
		double my = MathHelper.sin((burst.rotationPitch) / 180.0F * (float) Math.PI) * f / 2.0;
		burst.setMotion(mx, my, mz);
		
		return burst;
	}
	
	public static void setStack(EntityManaBurst burst, ItemStack stack) {
		ItemStack lens = new ItemStack(AlfheimItems.elvenResource, 1/*, ElvenResourcesMetas.Transferer BACK*/);
		ItemNBTHelper.getNBT(lens).setTag(TAG_STACK, stack.writeToNBT(new NBTTagCompound()));
		burst.setSourceLens(lens);
	}
	
	public static ItemStack getStack(EntityManaBurst burst) {
		return ItemStack.loadItemStackFromNBT(ItemNBTHelper.getNBT(burst.getSourceLens()).getCompoundTag(TAG_STACK));
	}
	
	public void onWanded(EntityPlayer player, ItemStack wand) {
		if(player == null)
			return;

		if(!player.isSneaking()) {
			if(!worldObj.isRemote) {
				knownMana = mana;
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				writeCustomNBT(nbttagcompound);
				nbttagcompound.setInteger(TAG_KNOWN_MANA, knownMana);
				if(player instanceof EntityPlayerMP)
					((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, nbttagcompound));
			}
			worldObj.playSoundAtEntity(player, "botania:ding", 0.1F, 1F);
		} else {
			MovingObjectPosition pos = raytraceFromEntity(worldObj, player, true, 5);
			if(pos != null && pos.hitVec != null && !worldObj.isRemote) {
				double x = pos.hitVec.xCoord - xCoord - 0.5;
				double y = pos.hitVec.yCoord - yCoord - 0.5;
				double z = pos.hitVec.zCoord - zCoord - 0.5;

				if(pos.sideHit != 0 && pos.sideHit != 1) {
					Vector3 clickVector = new Vector3(x, 0, z);
					Vector3 relative = new Vector3(-0.5, 0, 0);
					double angle = Math.acos(clickVector.dotProduct(relative) / (relative.length() * clickVector.length())) * 180.0 / Math.PI;

					rotationX = (float) angle + 180F;
					if(clickVector.z < 0)
						rotationX = 360 - rotationX;
				}

				double angle = y * 180;
				rotationY = -(float) angle;

				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, xCoord, yCoord, zCoord);
			}
		}
	}
	
	public static MovingObjectPosition raytraceFromEntity(World world, Entity player, boolean par3, double range) {
		float f = 1.0F;
		float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
		float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
		double d0 = player.prevPosX + (player.posX - player.prevPosX) * f;
		double d1 = player.prevPosY + (player.posY - player.prevPosY) * f;
		if (!world.isRemote && player instanceof EntityPlayer) d1 += 1.62;
		double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * f;
		Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.017453292F);
		float f6 = MathHelper.sin(-f1 * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		double d3 = range;
		if (player instanceof EntityPlayerMP) d3 = ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance();
		Vec3 vec31 = vec3.addVector(f7 * d3, f6 * d3, f8 * d3);
		return world.func_147447_a(vec3, vec31, par3, !par3, par3);
	}

	public void renderHUD(Minecraft mc, ScaledResolution res) {
		/*String name = StatCollector.translateToLocal(new ItemStack(AlfheimBlocks.transferer).getUnlocalizedName() + ".name");
		int color = 0xCCFF00;
		HUDHandler.drawSimpleManaHUD(color, knownMana, MAX_MANA, name, res);
		GL11.glColor4f(1F, 1F, 1F, 1F); BACK*/
	}

	public boolean isBound() {
		return !(toY == -1 && toX == -1 && toZ == -1);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger(TAG_TO_X, toX);
		nbt.setInteger(TAG_TO_Y, toY);
		nbt.setInteger(TAG_TO_Z, toZ);
		nbt.setBoolean(TAG_REDSTONE, redstoneLastTick);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		toX = nbt.getInteger(TAG_TO_X);
		toY = nbt.getInteger(TAG_TO_Y);
		toZ = nbt.getInteger(TAG_TO_Z);
		redstoneLastTick = nbt.getBoolean(TAG_REDSTONE);
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound nbt) {
		super.writeCustomNBT(nbt);
		nbt.setInteger(TAG_MANA, mana);
		nbt.setInteger(TAG_KNOWN_MANA, knownMana);
		nbt.setFloat(TAG_ROTATION_X, rotationX);
		nbt.setFloat(TAG_ROTATION_Y, rotationY);
	}
	
	@Override
	public void readCustomNBT(NBTTagCompound nbt) {
		super.readCustomNBT(nbt);
		mana = nbt.getInteger(TAG_MANA);
		knownMana = nbt.getInteger(TAG_KNOWN_MANA);
		rotationX = nbt.getFloat(TAG_ROTATION_X);
		rotationY = nbt.getFloat(TAG_ROTATION_Y);
	}
	
	@Override
	public float getRotationX() {
		return rotationX;
	}

	@Override
	public float getRotationY() {
		return rotationY;
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	@Override
	public boolean isFull() {
		return mana >= MAX_MANA;
	}

	@Override
	public void recieveMana(int mana) {
		this.mana = Math.min(this.mana + mana, MAX_MANA);
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return true;
	}


	@Override
	public ChunkCoordinates getBinding() {
		if(!isBound()) return null;
		return new ChunkCoordinates(toX, toY, toZ);
	}

	@Override
	public boolean canSelect(EntityPlayer player, ItemStack wand, int x, int y, int z, int side) {
		return true;
	}

	@Override
	public boolean bindTo(EntityPlayer player, ItemStack wand, int x, int y, int z, int side) {
		this.toX = x;
		this.toY = y;
		this.toZ = z;
		Vector3 thisVec = Vector3.fromTileEntityCenter(this);
		Vector3 blockVec = new Vector3(x + 0.5, y + 0.5, z + 0.5);

		AxisAlignedBB axis = player.worldObj.getBlock(x, y, z).getCollisionBoundingBoxFromPool(player.worldObj, x, y, z);
		if(axis == null)
			axis = AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1);

		if(!blockVec.isInside(axis))
			blockVec.set(axis.minX + (axis.maxX - axis.minX) / 2, axis.minY + (axis.maxY - axis.minY) / 2, axis.minZ + (axis.maxZ - axis.minZ) / 2);

		Vector3 diffVec =  blockVec.copy().sub(thisVec);
		Vector3 rotVec = new Vector3(0, 1, 0);
		double angle = rotVec.angle(new Vector3(diffVec.x, diffVec.z, 0)) / Math.PI * 180.0;

		if(blockVec.x < thisVec.x)
			angle = -angle;

		rotationX = (float) angle + 90;

		rotVec.set(diffVec.x, 0, diffVec.z);
		angle = diffVec.angle(rotVec) * 180F / Math.PI;
		if(blockVec.y < thisVec.y)
			angle = -angle;
		rotationY = (float) angle;

		return true;
	}
}