package alfheim.common.block.tile

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.extendables.TileItemContainer
import alexsocol.asjlib.math.Vector3
import alfheim.common.core.util.*
import alfheim.common.item.AlfheimItems
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.entity.Entity
import net.minecraft.entity.player.*
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.api.mana.*
import vazkii.botania.api.wand.IWandBindable
import vazkii.botania.common.core.handler.ConfigHandler
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.entity.EntityManaBurst
import kotlin.math.*

class TileTransferer: TileItemContainer(), IDirectioned, IManaReceiver, IWandBindable {
	
	var rotX = 0f
	override fun getRotationY() = rotX
	var rotY = 0f
	override fun getRotationX() = rotY
	
	var mana: Int = 0
	var knownMana = -1
	var redstoneLastTick = true
	var toX = -1
	var toY = -1
	var toZ = -1
	
	val burst: EntityManaBurst
		get() {
			val burst = EntityManaBurst(worldObj)
			
			burst.color = 0xCCFF00
			burst.mana = 10000
			burst.startingMana = 1
			burst.minManaLoss = 1
			burst.manaLossPerTick = 1f
			burst.gravity = 0f
			burst.setBurstSourceCoords(xCoord, yCoord, zCoord)
			setStack(burst, item!!.copy())
			item = null
			
			burst.setLocationAndAngles(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, -(rotationX + 90), rotationY)
			val f = 0.4f
			val mx = MathHelper.sin(burst.rotationYaw / 180f * PI.F) * MathHelper.cos(burst.rotationPitch / 180f * PI.F) * f / 2.0
			val mz = -(MathHelper.cos(burst.rotationYaw / 180f * PI.F) * MathHelper.cos(burst.rotationPitch / 180f * PI.F) * f) / 2.0
			val my = MathHelper.sin(burst.rotationPitch / 180f * PI.F) * f / 2.0
			burst.setMotion(mx, my, mz)
			
			return burst
		}
	
	val isBound: Boolean
		get() = !(toY == -1 && toX == -1 && toZ == -1)
	
	override fun updateEntity() {
		var redstone = false
		
		for (dir in ForgeDirection.VALID_DIRECTIONS) {
			val redstoneSide = worldObj.getIndirectPowerLevelTo(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ, dir.ordinal)
			if (redstoneSide > 0)
				redstone = true
		}
		
		if (redstone && !redstoneLastTick) tryShootBurst()
		
		ASJUtilities.dispatchTEToNearbyPlayers(worldObj, xCoord, yCoord, zCoord)
		
		redstoneLastTick = redstone
	}
	
	fun tryShootBurst() {
		if (isBound && item != null && mana == MAX_MANA) {
			val burst = burst
			if (!worldObj.isRemote) worldObj.spawnEntityInWorld(burst)
			if (!ConfigHandler.silentSpreaders) worldObj.playSoundEffect(xCoord.D, yCoord.D, zCoord.D, "botania:spreaderFire", 0.05f, 0.7f + 0.3f * Math.random().F)
		}
	}
	
	fun onWanded(player: EntityPlayer) {
		if (!player.isSneaking) {
			if (!worldObj.isRemote) {
				knownMana = mana
				val nbttagcompound = NBTTagCompound()
				writeCustomNBT(nbttagcompound)
				nbttagcompound.setInteger(TAG_KNOWN_MANA, knownMana)
				if (player is EntityPlayerMP)
					player.playerNetServerHandler.sendPacket(S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, nbttagcompound))
			}
			worldObj.playSoundAtEntity(player, "botania:ding", 0.1f, 1f)
		} else {
			val pos = raytraceFromEntity(worldObj, player, true, 5.0)
			if (pos?.hitVec != null && !worldObj.isRemote) {
				val x = pos.hitVec.xCoord - xCoord.D - 0.5
				val y = pos.hitVec.yCoord - yCoord.D - 0.5
				val z = pos.hitVec.zCoord - zCoord.D - 0.5
				
				if (pos.sideHit != 0 && pos.sideHit != 1) {
					val clickVector = Vector3(x, 0.0, z)
					val relative = Vector3(-0.5, 0.0, 0.0)
					val angle = acos(clickVector.dotProduct(relative) / (relative.length() * clickVector.length())) * 180.0 / PI
					
					rotX = angle.F + 180f
					if (clickVector.z < 0)
						rotX = 360 - rotationX
				}
				
				val angle = y * 180
				rotY = -angle.F
				
				ASJUtilities.dispatchTEToNearbyPlayers(worldObj, xCoord, yCoord, zCoord)
			}
		}
	}
	
	fun renderHUD(mc: Minecraft, res: ScaledResolution) {
		/*String name = StatCollector.translateToLocal(new ItemStack(AlfheimBlocks.transferer).getUnlocalizedName() + ".name");
		int color = 0xCCFF00;
		HUDHandler.drawSimpleManaHUD(color, knownMana, MAX_MANA, name, res);
		GL11.glColor4f(1F, 1F, 1F, 1F); BACK*/
	}
	
	override fun writeToNBT(nbt: NBTTagCompound) {
		super.writeToNBT(nbt)
		nbt.setInteger(TAG_TO_X, toX)
		nbt.setInteger(TAG_TO_Y, toY)
		nbt.setInteger(TAG_TO_Z, toZ)
		nbt.setBoolean(TAG_REDSTONE, redstoneLastTick)
	}
	
	override fun readFromNBT(nbt: NBTTagCompound) {
		super.readFromNBT(nbt)
		toX = nbt.getInteger(TAG_TO_X)
		toY = nbt.getInteger(TAG_TO_Y)
		toZ = nbt.getInteger(TAG_TO_Z)
		redstoneLastTick = nbt.getBoolean(TAG_REDSTONE)
	}
	
	override fun writeCustomNBT(nbt: NBTTagCompound) {
		super.writeCustomNBT(nbt)
		nbt.setInteger(TAG_MANA, mana)
		nbt.setInteger(TAG_KNOWN_MANA, knownMana)
		nbt.setFloat(TAG_ROTATION_X, rotationX)
		nbt.setFloat(TAG_ROTATION_Y, rotationY)
	}
	
	override fun readCustomNBT(nbt: NBTTagCompound) {
		super.readCustomNBT(nbt)
		mana = nbt.getInteger(TAG_MANA)
		knownMana = nbt.getInteger(TAG_KNOWN_MANA)
		rotX = nbt.getFloat(TAG_ROTATION_X)
		rotY = nbt.getFloat(TAG_ROTATION_Y)
	}
	
	override fun getCurrentMana() = mana
	
	override fun isFull() = mana >= MAX_MANA
	
	override fun recieveMana(mana: Int) {
		this.mana = min(this.mana + mana, MAX_MANA)
	}
	
	override fun canRecieveManaFromBursts() = true
	
	override fun getBinding() = if (!isBound) null else ChunkCoordinates(toX, toY, toZ)
	
	override fun canSelect(player: EntityPlayer, wand: ItemStack, x: Int, y: Int, z: Int, side: Int) = true
	
	override fun bindTo(player: EntityPlayer, wand: ItemStack, x: Int, y: Int, z: Int, side: Int): Boolean {
		toX = x
		toY = y
		toZ = z
		val thisVec = Vector3.fromTileEntityCenter(this)
		val blockVec = Vector3(x + 0.5, y + 0.5, z + 0.5)
		
		var axis: AxisAlignedBB? = player.worldObj.getBlock(x, y, z).getCollisionBoundingBoxFromPool(player.worldObj, x, y, z)
		if (axis == null)
			axis = AxisAlignedBB.getBoundingBox(x.D, y.D, z.D, (x + 1).D, (y + 1).D, (z + 1).D)
		
		if (!blockVec.isInside(axis!!))
			blockVec.set(axis.minX + (axis.maxX - axis.minX) / 2, axis.minY + (axis.maxY - axis.minY) / 2, axis.minZ + (axis.maxZ - axis.minZ) / 2)
		
		val diffVec = blockVec.copy().sub(thisVec)
		val rotVec = Vector3(0.0, 1.0, 0.0)
		var angle = rotVec.angle(Vector3(diffVec.x, diffVec.z, 0.0)) / PI * 180.0
		
		if (blockVec.x < thisVec.x)
			angle = -angle
		
		rotX = angle.F + 90
		
		rotVec.set(diffVec.x, 0.0, diffVec.z)
		angle = diffVec.angle(rotVec) * 180f / PI
		if (blockVec.y < thisVec.y)
			angle = -angle
		rotY = angle.F
		
		return true
	}
	
	companion object {
		
		// BlockTransferer BlockSpreader TileSpreader RenderSpreader RenderTileSpreader ItemLens
		private const val TAG_STACK = "transfering"
		
		const val MAX_MANA = 10000
		const val TAG_MANA = "mana"
		const val TAG_KNOWN_MANA = "knownMana"
		const val TAG_ROTATION_X = "rotationX"
		const val TAG_ROTATION_Y = "rotationY"
		const val TAG_TO_X = "toX"
		const val TAG_TO_Y = "toY"
		const val TAG_TO_Z = "toZ"
		const val TAG_REDSTONE = "redstone"
		
		fun setStack(burst: EntityManaBurst, stack: ItemStack) {
			val lens = ItemStack(AlfheimItems.elvenResource, 1/*, ElvenResourcesMetas.Transferer BACK*/)
			ItemNBTHelper.getNBT(lens).setTag(TAG_STACK, stack.writeToNBT(NBTTagCompound()))
			burst.sourceLens = lens
		}
		
		fun getStack(burst: EntityManaBurst) = ItemStack.loadItemStackFromNBT(ItemNBTHelper.getNBT(burst.sourceLens).getCompoundTag(TAG_STACK))
		
		fun raytraceFromEntity(world: World, player: Entity, par3: Boolean, range: Double): MovingObjectPosition? {
			val f = 1f
			val f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f
			val f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f
			val d0 = player.prevPosX + (player.posX - player.prevPosX) * f
			var d1 = player.prevPosY + (player.posY - player.prevPosY) * f
			if (!world.isRemote && player is EntityPlayer) d1 += 1.62
			val d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * f
			val vec3 = Vec3.createVectorHelper(d0, d1, d2)
			val f3 = MathHelper.cos(-f2 * 0.017453292f - PI.F)
			val f4 = MathHelper.sin(-f2 * 0.017453292f - PI.F)
			val f5 = -MathHelper.cos(-f1 * 0.017453292f)
			val f6 = MathHelper.sin(-f1 * 0.017453292f)
			val f7 = f4 * f5
			val f8 = f3 * f5
			var d3 = range
			if (player is EntityPlayerMP) d3 = player.theItemInWorldManager.blockReachDistance
			val vec31 = vec3.addVector(f7 * d3, f6 * d3, f8 * d3)
			return world.func_147447_a(vec3, vec31, par3, !par3, par3)
		}
	}
}