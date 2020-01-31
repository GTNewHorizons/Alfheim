package alfheim.common.item

// import vazkii.botania.common.block.decor.IFloatingFlower
// import vazkii.botania.common.item.IFloatingFlowerVariant
import alfheim.api.ModInfo
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.colored.BlockAuroraDirt
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.TickEvent
import cpw.mods.fml.common.gameevent.TickEvent.Phase
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.passive.EntitySheep
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.inventory.IInventory
import net.minecraft.item.*
import net.minecraft.util.*
import net.minecraft.world.World
import vazkii.botania.api.recipe.IFlowerComponent
import vazkii.botania.common.Botania
import vazkii.botania.common.block.decor.IFloatingFlower
import vazkii.botania.common.item.IFloatingFlowerVariant
import java.awt.Color
import java.util.*

@Suppress("JoinDeclarationAndAssignment")
class ItemColorSeeds: ItemIridescent("irisSeeds"), IFlowerComponent, IFloatingFlowerVariant {
	
	private val blockSwappers = HashMap<Int, MutableList<BlockSwapper?>>()
	
	init {
		FMLCommonHandler.instance().bus().register(this)
	}
	
	companion object {
		
		const val AURORA = 17
		
		val islandTypes: Array<IFloatingFlower.IslandType>
		
		init {
			islandTypes = Array(TYPES + 1) { i ->
				IridescentIslandType("IRIDESCENT$i", ResourceLocation(ModInfo.MODID, "textures/model/block/miniIsland.png"), i)
			}
		}
		
		fun dirtFromMeta(meta: Int): Block? {
			if (meta == AURORA)
				return AlfheimBlocks.auroraDirt
			if (meta == TYPES)
				return AlfheimBlocks.rainbowDirt
			return AlfheimBlocks.irisDirt
		}
		
		class IridescentIslandType(name: String, rs: ResourceLocation, val colorIndex: Int): IFloatingFlower.IslandType(name, rs) {
			override fun getColor(): Int {
				if (colorIndex == TYPES) {
					return Color(rainbowColor()).darker().rgb
				}
				if (colorIndex >= EntitySheep.fleeceColorTable.size + 1)
					return 0xFFFFFF
				
				val color = EntitySheep.fleeceColorTable[colorIndex]
				return Color(color[0], color[1], color[2]).darker().rgb
			}
		}
	}
	
	override fun getIslandType(stack: ItemStack) = run {
		if (stack.itemDamage == AURORA) null
		else islandTypes[stack.itemDamage % (TYPES + 1)]
	}
	
	override fun canFit(stack: ItemStack, inventory: IInventory) = stack.itemDamage == TYPES
	
	override fun getParticleColor(stack: ItemStack) = rainbowColor()
	
	override fun getSubItems(par1: Item, par2: CreativeTabs?, par3: MutableList<Any?>) {
		for (i in 0..AURORA)
			par3.add(ItemStack(par1, 1, i))
	}
	
	override fun onItemUse(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, par8: Float, par9: Float, par10: Float): Boolean {
		val block = world.getBlock(x, y, z)
		val bmeta = world.getBlockMetadata(x, y, z)
		val meta = stack.itemDamage
		
		val color = Color(if (meta == AURORA) BlockAuroraDirt.getBlockColor(x, y, z) else getColorFromItemStack(stack, 0))
		val r = color.red / 255F
		val g = color.green / 255F
		val b = color.blue / 255F
		
		var px: Double
		var py: Double
		var pz: Double
		val velMul = 0.025f
		
		if ((block === Blocks.dirt || block === Blocks.grass) && bmeta == 0) {
			val swapper = addBlockSwapper(world, x, y, z, meta)
			world.setBlock(x, y, z, swapper.blockToSet, swapper.metaToSet, 3)
			if (world.getBlock(x, y + 1, z) == Blocks.tallgrass && world.getBlockMetadata(x, y + 1, z) == 1) {
				if (meta >= 16)
					world.setBlock(x, y + 1, z, AlfheimBlocks.rainbowGrass, meta - 16, 4)
				else
					world.setBlock(x, y + 1, z, AlfheimBlocks.irisGrass, swapper.metaToSet, 4)
			} else if (world.getBlock(x, y + 1, z) == Blocks.double_plant && world.getBlockMetadata(x, y + 1, z) == 2) {
				if (meta >= 16) {
					world.setBlock(x, y + 1, z, AlfheimBlocks.rainbowTallGrass, meta - 16, 2)
					world.setBlock(x, y + 2, z, AlfheimBlocks.rainbowTallGrass, meta - 8, 2)
				} else {
					if (swapper.metaToSet < 8) {
						world.setBlock(x, y + 1, z, AlfheimBlocks.irisTallGrass0, swapper.metaToSet, 2)
						world.setBlock(x, y + 2, z, AlfheimBlocks.irisTallGrass0, 8, 2)
					} else {
						world.setBlock(x, y + 1, z, AlfheimBlocks.irisTallGrass1, swapper.metaToSet - 8, 2)
						world.setBlock(x, y + 2, z, AlfheimBlocks.irisTallGrass1, 8, 2)
					}
				}
			}
			for (i in 0..49) {
				px = (Math.random() - 0.5) * 3
				py = Math.random() - 0.5 + 1
				pz = (Math.random() - 0.5) * 3
				Botania.proxy.wispFX(world, x + 0.5 + px, y + 0.5 + py, z + 0.5 + pz, r, g, b, Math.random().toFloat() * 0.15f + 0.15f, (-px).toFloat() * velMul, (-py).toFloat() * velMul, (-pz).toFloat() * velMul)
			}
			stack.stackSize--
		}
		return true
	}
	
	@SubscribeEvent
	fun onTickEnd(event: TickEvent.WorldTickEvent) {
		if (event.phase == Phase.END) {
			val dim = event.world.provider.dimensionId
			if (blockSwappers.containsKey(dim)) {
				val swappers = blockSwappers[dim] as ArrayList<BlockSwapper?>
				val swappersSafe = ArrayList(swappers)
				for (s in swappersSafe)
					if (s?.tick() != true)
						swappers.remove(s)
			}
		}
	}
	
	private fun addBlockSwapper(world: World, x: Int, y: Int, z: Int, meta: Int): BlockSwapper {
		val swapper = swapperFromMeta(world, x, y, z, meta)
		
		val dim = world.provider.dimensionId
		if (!blockSwappers.containsKey(dim))
			blockSwappers[dim] = ArrayList()
		blockSwappers[dim]?.add(swapper)
		
		return swapper
	}
	
	private fun swapperFromMeta(world: World, x: Int, y: Int, z: Int, meta: Int) =
		BlockSwapper(world, ChunkCoordinates(x, y, z), meta)
	
	private class BlockSwapper(world: World, coords: ChunkCoordinates, meta: Int) {
		
		var world: World
		var rand: Random
		var blockToSet: Block?
		var rainbow: Boolean
		var metaToSet: Int
		
		var grassBlock: Block
		var tallGrassMeta: Int
		var tallGrassBlock: Block
		
		var startCoords: ChunkCoordinates
		var ticksExisted = 0
		
		val RANGE = 3
		val TICK_RANGE = 1
		
		init {
			this.world = world
			blockToSet = dirtFromMeta(meta)
			rainbow = meta >= 16
			metaToSet = meta % 16
			val seed = coords.posX xor coords.posY xor coords.posZ
			rand = Random(seed.toLong())
			startCoords = coords
			
			grassBlock = if (rainbow) AlfheimBlocks.rainbowGrass else AlfheimBlocks.irisGrass
			tallGrassMeta = metaToSet % 8
			tallGrassBlock = if (rainbow) AlfheimBlocks.rainbowTallGrass else (if (meta > 8) AlfheimBlocks.irisTallGrass1 else AlfheimBlocks.irisTallGrass0)
		}
		
		fun tick(): Boolean {
			ticksExisted++
			for (i in -RANGE..RANGE) {
				for (j in -RANGE..RANGE) {
					val x = startCoords.posX + i
					val y = startCoords.posY
					val z = startCoords.posZ + j
					val block: Block = world.getBlock(x, y, z)
					val meta: Int = world.getBlockMetadata(x, y, z)
					
					if (block === blockToSet && meta == metaToSet) {
						// Only make changes every 20 ticks
						if (ticksExisted % 20 != 0) continue
						
						tickBlock(x, y, z)
					}
				}
			}
			
			return ticksExisted < 80
		}
		
		fun tickBlock(x: Int, y: Int, z: Int) {
			val validCoords = ArrayList<ChunkCoordinates>()
			
			for (xOffset in -TICK_RANGE..TICK_RANGE) {
				for (zOffset in -TICK_RANGE..TICK_RANGE) {
					if (xOffset == 0 && zOffset == 0) continue
					
					if (isValidSwapPosition(x + xOffset, y, z + zOffset))
						validCoords.add(ChunkCoordinates(x + xOffset, y, z + zOffset))
				}
			}
			if (validCoords.isNotEmpty() && !world.isRemote) {
				val toSwap = validCoords[rand.nextInt(validCoords.size)]
				world.setBlock(toSwap.posX, toSwap.posY, toSwap.posZ, blockToSet, metaToSet, 3)
				
				val blockAbove = world.getBlock(toSwap.posX, toSwap.posY + 1, toSwap.posZ)
				val metaAbove = world.getBlockMetadata(toSwap.posX, toSwap.posY + 1, toSwap.posZ)
				
				if (blockAbove == Blocks.tallgrass && metaAbove == 1) {
					world.setBlock(toSwap.posX, toSwap.posY + 1, toSwap.posZ, grassBlock, metaToSet, 1 or 2)
				} else if (blockAbove == Blocks.double_plant && metaAbove == 2) {
					world.setBlock(toSwap.posX, toSwap.posY + 1, toSwap.posZ, tallGrassBlock, tallGrassMeta, 2)
					world.setBlock(toSwap.posX, toSwap.posY + 2, toSwap.posZ, tallGrassBlock, 8, 2)
				}
			}
		}
		
		fun isValidSwapPosition(x: Int, y: Int, z: Int): Boolean {
			val block = world.getBlock(x, y, z)
			val meta = world.getBlockMetadata(x, y, z)
			val aboveBlock = world.getBlock(x, y + 1, z)
			
			return (block == Blocks.dirt || block == Blocks.grass)
				   && (meta == 0)
				   && (aboveBlock.getLightOpacity(world, x, y, z) <= 1)
		}
	}
}
