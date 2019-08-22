package alfheim.common.block

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.item.AlfheimItems
import alfheim.common.item.block.ItemBlockMod
import alfheim.common.lexicon.AlfheimLexiconData
import alfheim.common.network.MessageEffect
import baubles.api.BaublesApi
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.util.IIcon
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.common.util.ForgeDirection.*
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.mana.ManaItemHandler
import java.util.*

class BlockRedFlame: BlockFire(), ILexiconable {
	
	lateinit var icons: Array<IIcon>
	
	init {
		setBlockName("MuspelheimFire")
		setBlockUnbreakable()
		setLightLevel(1.0f)
		setLightOpacity(0)
		setResistance(java.lang.Float.MAX_VALUE)
	}
	
	override fun setBlockName(name: String): Block {
		GameRegistry.registerBlock(this, ItemBlockMod::class.java, name)
		return super.setBlockName(name)
	}
	
	override fun getPlayerRelativeBlockHardness(player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Float {
		val metadata = world.getBlockMetadata(x, y, z)
		var hardness = getBlockHardness(world, x, y, z)
		
		val bbls = PlayerHandler.getPlayerBaubles(player)
		if (bbls.getStackInSlot(0) != null && bbls.getStackInSlot(0).item === AlfheimItems.elfFirePendant && ManaItemHandler.requestManaExact(bbls.getStackInSlot(0), player, 300, true)) hardness = 2f
		
		if (hardness < 0.0f) return 0.0f
		
		return if (!ForgeHooks.canHarvestBlock(this, player, metadata))
			player.getBreakSpeed(this, true, metadata, x, y, z) / hardness / 100f
		else
			player.getBreakSpeed(this, false, metadata, x, y, z) / hardness / 30f
	}
	
	override fun isCollidable() = false
	
	@SideOnly(Side.CLIENT)
	override fun registerBlockIcons(reg: IIconRegister) {
		icons = arrayOf(reg.registerIcon(ModInfo.MODID + ":MuspelheimFire0"), reg.registerIcon(ModInfo.MODID + ":MuspelheimFire1"))
	}
	
	@SideOnly(Side.CLIENT)
	override fun getFireIcon(i: Int) = icons[i]
	
	@SideOnly(Side.CLIENT)
	override fun getIcon(p_149691_1_: Int, p_149691_2_: Int) = icons[0]
	
	override fun onEntityCollidedWithBlock(world: World, x: Int, y: Int, z: Int, entity: Entity) {
		if (entity is EntityPlayer && BaublesApi.getBaubles(entity).getStackInSlot(0)?.item === AlfheimItems.elfFirePendant && ManaItemHandler.requestManaExact(BaublesApi.getBaubles(entity).getStackInSlot(0), entity, 50, true)) return
		
		if (entity is EntityLivingBase) {
			val soulburn = PotionEffect(AlfheimRegistry.soulburn.id, 200)
			soulburn.curativeItems.clear()
			entity.addPotionEffect(soulburn)
			if (ASJUtilities.isServer) AlfheimCore.network.sendToAll(MessageEffect(entity.entityId, soulburn.potionID, soulburn.duration, soulburn.amplifier))
		}
		entity.setInWeb()
	}
	
	fun tryCatchFire(world: World, x: Int, y: Int, z: Int, side: Int, rand: Random, meta: Int, face: ForgeDirection) {
		val j1 = world.getBlock(x, y, z).getFlammability(world, x, y, z, face)
		if (rand.nextInt(side) < j1) {
			val flag = world.getBlock(x, y, z) === Blocks.tnt
			
			var k1 = meta + rand.nextInt(5) / 4
			if (k1 > 15) {
				k1 = 15
			}
			world.setBlock(x, y, z, this, k1, 3)
			if (flag) {
				Blocks.tnt.onBlockDestroyedByPlayer(world, x, y, z, 1)
			}
		}
	}
	
	fun canNeighborBurn(world: World, x: Int, y: Int, z: Int): Boolean {
		return (canCatchFire(world, x + 1, y, z, WEST) || canCatchFire(world, x - 1, y, z, EAST)
				|| canCatchFire(world, x, y - 1, z, UP) || canCatchFire(world, x, y + 1, z, DOWN)
				|| canCatchFire(world, x, y, z - 1, SOUTH) || canCatchFire(world, x, y, z + 1, NORTH))
	}
	
	fun getChanceOfNeighborsEncouragingFire(world: World, x: Int, y: Int, z: Int): Int {
		val b0: Byte = 0
		
		return if (!world.isAirBlock(x, y, z)) {
			0
		} else {
			var l = b0.toInt()
			l = getChanceToEncourageFire(world, x + 1, y, z, l, WEST)
			l = getChanceToEncourageFire(world, x - 1, y, z, l, EAST)
			l = getChanceToEncourageFire(world, x, y - 1, z, l, UP)
			l = getChanceToEncourageFire(world, x, y + 1, z, l, DOWN)
			l = getChanceToEncourageFire(world, x, y, z - 1, l, SOUTH)
			l = getChanceToEncourageFire(world, x, y, z + 1, l, NORTH)
			l
		}
	}
	
	override fun updateTick(world: World, x: Int, y: Int, z: Int, rand: Random?) {
		if (world.gameRules.getGameRuleBooleanValue("doFireTick")) {
			if (!canPlaceBlockAt(world, x, y, z)) {
				world.setBlockToAir(x, y, z)
			}
			
			if (world.rand.nextInt(100) == 0) world.setBlockToAir(x, y, z)
			
			run {
				val l = world.getBlockMetadata(x, y, z)
				if (l < 15) {
					world.setBlockMetadataWithNotify(x, y, z, l + rand!!.nextInt(3) / 2, 4)
				}
				world.scheduleBlockUpdate(x, y, z, this, tickRate(world) + rand!!.nextInt(15))
				
				run {
					val flag1 = world.isBlockHighHumidity(x, y, z)
					var b0: Byte = 0
					if (flag1) {
						b0 = -50
					}
					tryCatchFire(world, x + 1, y, z, 300 + b0, rand, l, WEST)
					tryCatchFire(world, x - 1, y, z, 300 + b0, rand, l, EAST)
					tryCatchFire(world, x, y - 1, z, 250 + b0, rand, l, UP)
					tryCatchFire(world, x, y + 1, z, 250 + b0, rand, l, DOWN)
					tryCatchFire(world, x, y, z - 1, 300 + b0, rand, l, SOUTH)
					tryCatchFire(world, x, y, z + 1, 300 + b0, rand, l, NORTH)
					for (i1 in x - 1..x + 1) {
						for (j1 in z - 1..z + 1) {
							for (k1 in y - 1..y + 4) {
								if (i1 != x || k1 != y || j1 != z) {
									var l1 = 100
									if (k1 > y + 1) {
										l1 += (k1 - (y + 1)) * 100
									}
									val i2 = getChanceOfNeighborsEncouragingFire(world, i1, k1, j1)
									if (i2 > 0) {
										var j2 = (i2 + 40 + world.difficultySetting.difficultyId * 7) / (l + 30)
										if (flag1) {
											j2 /= 2
										}
										if (j2 > 0 && rand.nextInt(l1) <= j2
											&& (!world.isRaining || !world.canLightningStrikeAt(i1, k1, j1))
											&& !world.canLightningStrikeAt(i1 - 1, k1, z)
											&& !world.canLightningStrikeAt(i1 + 1, k1, j1)
											&& !world.canLightningStrikeAt(i1, k1, j1 - 1)
											&& !world.canLightningStrikeAt(i1, k1, j1 + 1)) {
											var k2 = l + rand.nextInt(5) / 4
											if (k2 > 15) {
												k2 = 15
											}
											world.setBlock(i1, k1, j1, this, k2, 3)
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack) = AlfheimLexiconData.ruling
}
