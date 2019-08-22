package alfheim.common.block.tile

import alfheim.common.block.AlfheimBlocks
import cpw.mods.fml.relauncher.*
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.*
import net.minecraftforge.common.util.ForgeDirection
import org.lwjgl.opengl.GL11.glEnable
import org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL
import vazkii.botania.api.internal.VanillaPacketDispatcher
import vazkii.botania.common.block.tile.TileMod

class TileAnimatedTorch: TileMod() {
	
	var side: Int = 0
	var rotation: Double = 0.toDouble()
	var rotating: Boolean = false
	var lastTickRotation: Double = 0.toDouble()
	var nextRandomRotation: Int = 0
	var currentRandomRotation: Int = 0
	
	private var rotationTicks: Int = 0
	var anglePerTick: Double = 0.toDouble()
	
	private var torchMode = TorchMode.TOGGLE
	
	fun handRotate() {
		if (!worldObj.isRemote)
			worldObj.addBlockEvent(xCoord, yCoord, zCoord, AlfheimBlocks.animatedTorch, 0, (side + 1) % 4)
	}
	
	fun onPlace(entity: EntityLivingBase?) {
		if (!worldObj.isRemote)
			nextRandomRotation = worldObj.rand.nextInt(4)
		
		if (entity != null)
			side = MathHelper.floor_double((entity.rotationYaw * 4.0f / 360.0f).toDouble() + 0.5) and 3
		
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType())
	}
	
	fun toggle() {
		if (!worldObj.isRemote) {
			worldObj.addBlockEvent(xCoord, yCoord, zCoord, AlfheimBlocks.animatedTorch, 0, torchMode.rotate(this, side))
			nextRandomRotation = worldObj.rand.nextInt(4)
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this)
		}
	}
	
	fun onWanded() {
		val modes = TorchMode.values()
		torchMode = modes[(torchMode.ordinal + 1) % modes.size]
	}
	
	override fun receiveClientEvent(id: Int, param: Int): Boolean {
		if (id == 0) {
			rotateTo(param)
			return true
		} else {
			return super.receiveClientEvent(id, param)
		}
	}
	
	fun rotateTo(side: Int) {
		if (rotating) return
		
		currentRandomRotation = nextRandomRotation
		val finalRotation = side * 90
		
		var diff = (finalRotation - rotation % 360) % 360
		if (diff < 0)
			diff = 360 + diff
		
		rotationTicks = 5
		anglePerTick = diff / rotationTicks
		this.side = side
		rotating = true
		
		// tell neighbors that signal is off because we are rotating
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType())
	}
	
	@SideOnly(Side.CLIENT)
	fun renderHUD(mc: Minecraft, res: ScaledResolution) {
		val x = res.scaledWidth / 2 + 10
		val y = res.scaledHeight / 2 - 8
		
		RenderHelper.enableGUIStandardItemLighting()
		glEnable(GL_RESCALE_NORMAL)
		RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, ItemStack(AlfheimBlocks.animatedTorch), x, y)
		
		mc.fontRenderer.drawStringWithShadow(StatCollector.translateToLocal("tile.AnimatedTorch.desc." + torchMode.name.toLowerCase()), x + 18, y + 6, 0xFF4444)
	}
	
	override fun updateEntity() {
		if (rotating) {
			lastTickRotation = rotation
			rotation = (rotation + anglePerTick) % 360
			rotationTicks--
			
			if (rotationTicks <= 0) {
				rotating = false
				// done rotating, tell neighbors
				worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType())
			}
			
		} else
			rotation = (side * 90).toDouble()
		
		if (worldObj.isRemote) {
			val amt = if (rotating) 3 else if (Math.random() < 0.1) 1 else 0
			val x = xCoord.toDouble() + 0.5 + Math.cos((rotation + 90) / 180.0 * Math.PI) * 0.35
			val y = yCoord + 0.2
			val z = zCoord.toDouble() + 0.5 + Math.sin((rotation + 90) / 180.0 * Math.PI) * 0.35
			
			for (i in 0 until amt)
				worldObj.spawnParticle("reddust", x, y, z, 0.0, 0.0, 0.0)
		}
	}
	
	override fun writeCustomNBT(cmp: NBTTagCompound) {
		cmp.setInteger(TAG_SIDE, side)
		cmp.setBoolean(TAG_ROTATING, rotating)
		cmp.setInteger(TAG_ROTATION_TICKS, rotationTicks)
		cmp.setDouble(TAG_ANGLE_PER_TICK, anglePerTick)
		cmp.setInteger(TAG_TORCH_MODE, torchMode.ordinal)
		cmp.setInteger(TAG_NEXT_RANDOM_ROTATION, nextRandomRotation)
	}
	
	override fun readCustomNBT(cmp: NBTTagCompound) {
		side = cmp.getInteger(TAG_SIDE)
		rotating = cmp.getBoolean(TAG_ROTATING)
		if (worldObj != null && !worldObj.isRemote)
			rotationTicks = cmp.getInteger(TAG_ROTATION_TICKS)
		anglePerTick = cmp.getDouble(TAG_ANGLE_PER_TICK)
		nextRandomRotation = cmp.getInteger(TAG_NEXT_RANDOM_ROTATION)
		
		val modeOrdinal = cmp.getInteger(TAG_TORCH_MODE)
		val modes = TorchMode.values()
		torchMode = modes[modeOrdinal % modes.size]
	}
	
	enum class TorchMode {
		TOGGLE, ROTATE, RANDOM;
		
		fun rotate(tile: TileAnimatedTorch, curr: Int): Int {
			when (this) {
				TOGGLE -> return (curr + 2) % 4
				ROTATE -> return (curr + 1) % 4
				RANDOM -> return tile.currentRandomRotation
			}
			return 0
		}
	}
	
	companion object {
		
		val TAG_SIDE = "side"
		val TAG_ROTATING = "rotating"
		val TAG_ROTATION_TICKS = "rotationTicks"
		val TAG_ANGLE_PER_TICK = "anglePerTick"
		val TAG_TORCH_MODE = "torchMode"
		val TAG_NEXT_RANDOM_ROTATION = "nextRandomRotation"
		
		val SIDES = arrayOf(ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST)
	}
}