package alfheim.common.block.tile.sub.flower

import alfheim.api.AlfheimAPI
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.relauncher.*
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.IIcon
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.*
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.subtile.*
import vazkii.botania.api.subtile.RadiusDescriptor.Square
import java.awt.Color

class SubTilePetronia: SubTileGenerating() {
	
	var burnTime = 0
	var powerPerTick = 0
	var coolDown = 0
	
	override fun onUpdate() {
		super.onUpdate()
		if (coolDown > 0) {
			--coolDown
		}
		if (burnTime > 0) {
			--burnTime
		}
		
		val world = supertile.worldObj
		val x = supertile.xCoord
		val y = supertile.yCoord
		val z = supertile.zCoord
		
		if (world.isRemote) {
			if (burnTime > 0 && world.rand.nextInt(10) == 0)
				world.spawnParticle("smoke", x + Math.random() * 0.25 - 0.5, y + Math.random(), z + Math.random() * 0.25, 0.0, 0.0, 0.0)
		} else {
			if (linkedCollector != null) {
				if (burnTime <= 0 && redstoneSignal == 0 && coolDown <= 0) {
					if (mana < maxMana) {
						var tries = 0
						var foundFluid = false
						
						while (tries++ < 9 && !foundFluid) {
							val i = x - 1 + tries / 3
							val j = y - 1
							val k = z - 1 + tries % 3
							
							val fluidHandler = world.getTileEntity(i, j, k) as? IFluidHandler ?: continue
							
							val grabbedFluid = fluidHandler.drain(ForgeDirection.UP, 1000, false)
							
							if (grabbedFluid?.getFluid() != null && grabbedFluid.amount == 1000) {
								val fuelInfo = AlfheimAPI.fuelMap[FluidRegistry.getFluidName(grabbedFluid.getFluid())] ?: continue
								
								foundFluid = true
								burnTime = fuelInfo.first
								powerPerTick = fuelInfo.second
								fluidHandler.drain(ForgeDirection.UP, 1000, true)
								sync()
							}
						}
						if (!foundFluid) {
							coolDown = 100
						}
					}
				}
			}
		}
	}
	
	override fun getMaxMana() = 9000
	
	override fun getValueForPassiveGeneration(): Int {
		return powerPerTick * delayBetweenPassiveGeneration
	}
	
	override fun canGeneratePassively() = burnTime > 0
	
	override fun getDelayBetweenPassiveGeneration() = 2
	
	override fun acceptsRedstone() = true
	
	@SideOnly(Side.CLIENT)
	override fun getRadius(): RadiusDescriptor {
		return Square(toChunkCoordinates().apply { --posY }, 1)
	}
	
	override fun getColor() = Color.BLACK.rgb
	
	override fun writeToPacketNBT(cmp: NBTTagCompound) {
		super.writeToPacketNBT(cmp)
		cmp.setInteger(TAG_BURN_TIME, burnTime)
		cmp.setInteger(TAG_COOLDOWN, coolDown)
		cmp.setInteger(TAG_POWER, powerPerTick)
	}
	
	override fun readFromPacketNBT(cmp: NBTTagCompound) {
		super.readFromPacketNBT(cmp)
		burnTime = cmp.getInteger(TAG_BURN_TIME)
		coolDown = cmp.getInteger(TAG_COOLDOWN)
		powerPerTick = cmp.getInteger(TAG_POWER)
	}
	
	override fun getEntry() = AlfheimLexiconData.flowerPetronia
	
	override fun getIcon(): IIcon? = BotaniaAPI.getSignatureForName("petronia").getIconForStack(null)
	
	companion object {
		
		const val TAG_BURN_TIME = "burn_time"
		const val TAG_POWER = "tick_power"
		const val TAG_COOLDOWN = "cooldown"
		
		init {
			AlfheimAPI.registerFuel("creosote", 50, 50)
			AlfheimAPI.registerFuel("oil", 100, 50)
			AlfheimAPI.registerFuel("fuel", 750, 50)
			AlfheimAPI.registerFuel("diesel", 350, 50)
			AlfheimAPI.registerFuel("biodiesel", 250, 50)
			AlfheimAPI.registerFuel("ethanol", 150, 50)
			AlfheimAPI.registerFuel("bioethanol", 250, 50)
			AlfheimAPI.registerFuel("pyrotheum", 900, 50)
		}
	}
}
