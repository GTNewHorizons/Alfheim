package alfheim.common.block.tile;

import alfheim.common.core.registry.AlfheimBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.tile.TileMod;

public class TileAnimatedTorch extends TileMod {

	public static final String TAG_SIDE = "side";
	public static final String TAG_ROTATING = "rotating";
	public static final String TAG_ROTATION_TICKS = "rotationTicks";
	public static final String TAG_ANGLE_PER_TICK = "anglePerTick";
	public static final String TAG_TORCH_MODE = "torchMode";
	public static final String TAG_NEXT_RANDOM_ROTATION = "nextRandomRotation";

	public static final ForgeDirection[] SIDES = new ForgeDirection[] {
			ForgeDirection.NORTH,
			ForgeDirection.EAST,
			ForgeDirection.SOUTH,
			ForgeDirection.WEST
	};

	public int side;
	public double rotation;
	public boolean rotating;
	public double lastTickRotation;
	public int nextRandomRotation;
	public int currentRandomRotation;

	private int rotationTicks;
	public double anglePerTick;

	private TorchMode torchMode = TorchMode.TOGGLE;

	public void handRotate() {
		if(!worldObj.isRemote)
			worldObj.addBlockEvent(xCoord, yCoord, zCoord, AlfheimBlocks.animatedTorch, 0, (side + 1) % 4);
	}
	
	public void onPlace(EntityLivingBase entity) {
		if(!worldObj.isRemote)
			nextRandomRotation = worldObj.rand.nextInt(4);
			
		if(entity != null) 
			side = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5) & 3;
		
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());
	}

	public void toggle() {
		if(!worldObj.isRemote) {
			worldObj.addBlockEvent(xCoord, yCoord, zCoord, AlfheimBlocks.animatedTorch, 0, torchMode.rotate(this, side));
			nextRandomRotation = worldObj.rand.nextInt(4);
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
	}

	public void onWanded() {
		TorchMode[] modes = TorchMode.values();
		torchMode = modes[(torchMode.ordinal() + 1) % modes.length];
	}

	@Override
	public boolean receiveClientEvent(int id, int param) {
		if (id == 0) {
			rotateTo(param);
			return true;
		} else {
			return super.receiveClientEvent(id, param);
		}
	}

	public void rotateTo(int side) {
		if(rotating) return;

		currentRandomRotation = nextRandomRotation;
		int finalRotation = side * 90;

		double diff = (finalRotation - rotation % 360) % 360;
		if(diff < 0)
			diff = 360 + diff;

		rotationTicks = 5;
		anglePerTick = diff / rotationTicks;
		this.side = side;
		rotating = true;

		// tell neighbors that signal is off because we are rotating
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());
	}

	@SideOnly(Side.CLIENT)
	public void renderHUD(Minecraft mc, ScaledResolution res) {
		int x = res.getScaledWidth() / 2 + 10;
		int y = res.getScaledHeight() / 2 - 8;

		// RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack(Blocks.redstone_torch), x, y); // FIXME some bug
		
		mc.fontRenderer.drawStringWithShadow(StatCollector.translateToLocal("tile.AnimatedTorch.desc." + torchMode.name().toLowerCase()), x + 18, y + 6, 0xFF4444);
	}

	@Override
	public void updateEntity() {
		if(rotating) {
			lastTickRotation = rotation;
			rotation = (rotation + anglePerTick) % 360;
			rotationTicks--;

			if(rotationTicks <= 0) {
				rotating = false;
				// done rotating, tell neighbors
				worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());
			}

		} else rotation = side * 90;

		if(worldObj.isRemote) {
			int amt = rotating ? 3 : Math.random() < 0.1 ? 1 : 0;
			double x = xCoord + 0.5 + Math.cos((rotation + 90) / 180.0 * Math.PI) * 0.35;
			double y = yCoord + 0.2;
			double z = zCoord + 0.5 + Math.sin((rotation + 90) / 180.0 * Math.PI) * 0.35;

			for(int i = 0; i < amt; i++)
				worldObj.spawnParticle("reddust", x, y, z, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_SIDE, side);
		cmp.setBoolean(TAG_ROTATING, rotating);
		cmp.setInteger(TAG_ROTATION_TICKS, rotationTicks);
		cmp.setDouble(TAG_ANGLE_PER_TICK, anglePerTick);
		cmp.setInteger(TAG_TORCH_MODE, torchMode.ordinal());
		cmp.setInteger(TAG_NEXT_RANDOM_ROTATION, nextRandomRotation);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		side = cmp.getInteger(TAG_SIDE);
		rotating = cmp.getBoolean(TAG_ROTATING);
		if(worldObj != null && !worldObj.isRemote)
			rotationTicks = cmp.getInteger(TAG_ROTATION_TICKS);
		anglePerTick = cmp.getDouble(TAG_ANGLE_PER_TICK);
		nextRandomRotation = cmp.getInteger(TAG_NEXT_RANDOM_ROTATION);

		int modeOrdinal = cmp.getInteger(TAG_TORCH_MODE);
		TorchMode[] modes = TorchMode.values();
		torchMode = modes[modeOrdinal % modes.length];
	}

	public enum TorchMode {
		TOGGLE, ROTATE, RANDOM;

		public int rotate(TileAnimatedTorch tile, int curr) {
			switch (this) {
				case TOGGLE: return (curr + 2) % 4;
				case ROTATE: return (curr + 1) % 4;
				case RANDOM: return tile.currentRandomRotation;
			}
			return 0;
		}
	}
}