package alfheim.common.block

import alexsocol.asjlib.D
import alfheim.AlfheimCore
import alfheim.common.block.base.BlockMod
import alfheim.common.item.AlfheimItems
import alfheim.common.lexicon.AlfheimLexiconData
import alfheim.common.network.MessageEffect
import baubles.api.BaublesApi
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.relauncher.*
import net.minecraft.block.material.Material
import net.minecraft.entity.*
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.potion.*
import net.minecraft.util.AxisAlignedBB
import net.minecraft.world.*
import net.minecraftforge.common.ForgeHooks
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.mana.ManaItemHandler
import java.util.*

class BlockPoisonIce: BlockMod(Material.packedIce), ILexiconable {
	
	init {
		val mod = 0.001f
		setBlockBounds(0 + mod, 0 + mod, 0 + mod, 1 - mod, 1 - mod, 1 - mod)
		setBlockName("NiflheimIce")
		setCreativeTab(null)
		setBlockUnbreakable()
		setHarvestLevel("pick", 2)
		setLightOpacity(0)
		setResistance(Float.MAX_VALUE)
		setStepSound(soundTypeGlass)
		tickRandomly = true
		slipperiness = 0.98f
	}
	
	override fun getPlayerRelativeBlockHardness(player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Float {
		val metadata = world.getBlockMetadata(x, y, z)
		var hardness = getBlockHardness(world, x, y, z)
		
		val bbls = PlayerHandler.getPlayerBaubles(player)
		if (bbls.getStackInSlot(0) != null && bbls.getStackInSlot(0).item === AlfheimItems.elfIcePendant && ManaItemHandler.requestManaExact(bbls.getStackInSlot(0), player, 5, true)) hardness = 2f
		
		if (hardness < 0f) return 0f
		
		return if (!ForgeHooks.canHarvestBlock(this, player, metadata))
			player.getBreakSpeed(this, true, metadata, x, y, z) / hardness / 100f
		else
			player.getBreakSpeed(this, false, metadata, x, y, z) / hardness / 30f
	}
	
	override fun isOpaqueCube() = false
	
	@SideOnly(Side.CLIENT)
	override fun getRenderBlockPass() = 1
	
	@SideOnly(Side.CLIENT)
	override fun shouldSideBeRendered(world: IBlockAccess, x: Int, y: Int, z: Int, side: Int) =
		world.getBlock(x, y, z) !== this && !world.getBlock(x, y, z).isOpaqueCube
	
	override fun quantityDropped(r: Random?) = 0
	
	override fun dropBlockAsItem(w: World, x: Int, y: Int, z: Int, s: ItemStack) = Unit
	
	override fun onEntityWalking(w: World, x: Int, y: Int, z: Int, e: Entity) {
		if (e is EntityPlayer && BaublesApi.getBaubles(e).getStackInSlot(0)?.item === AlfheimItems.elfIcePendant && ManaItemHandler.requestManaExact(BaublesApi.getBaubles(e).getStackInSlot(0), e, 50, true)) return
		
		e.setInWeb()
		if (!w.isRemote && e is EntityLivingBase) {
			if (!e.isPotionActive(Potion.poison)) {
				e.addPotionEffect(PotionEffect(Potion.poison.id, 100, 2))
				AlfheimCore.network.sendToAll(MessageEffect(e.entityId, Potion.poison.id, 100, 2))
			}
			e.addPotionEffect(PotionEffect(Potion.moveSlowdown.id, 25, 2))
			AlfheimCore.network.sendToAll(MessageEffect(e.entityId, Potion.moveSlowdown.id, 25, 2))
		}
	}
	
	override fun onEntityCollidedWithBlock(w: World, x: Int, y: Int, z: Int, e: Entity) {
		onEntityWalking(w, x, y, z, e)
	}
	
	override fun updateTick(world: World, x: Int, y: Int, z: Int, rand: Random?) {
		if (world.gameRules.getGameRuleBooleanValue("doFireTick")
			&& rand!!.nextInt(10) == 0
			&& world.getEntitiesWithinAABB(EntityMob::class.java, AxisAlignedBB.getBoundingBox(x.D, y.D, z.D, (x + 1).D, (y + 1).D, (z + 1).D).expand(5.0, 5.0, 5.0)).isEmpty()) {
			world.setBlockToAir(x, y, z)
		}
	}
	
	override fun tickRate(world: World?) = 1
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack) = AlfheimLexiconData.ruling
}