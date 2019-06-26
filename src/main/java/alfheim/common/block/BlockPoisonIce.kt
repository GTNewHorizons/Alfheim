package alfheim.common.block

import alfheim.api.ModInfo
import alfheim.common.core.registry.AlfheimItems
import alfheim.common.lexicon.AlfheimLexiconData
import baubles.api.BaublesApi
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.*
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.potion.*
import net.minecraft.util.AxisAlignedBB
import net.minecraft.world.*
import net.minecraftforge.common.ForgeHooks
import vazkii.botania.api.lexicon.*
import vazkii.botania.api.mana.ManaItemHandler

import java.util.Random

class BlockPoisonIce: Block(Material.packedIce), ILexiconable {
	init {
		val mod = 0.001f
		setBlockBounds(0 + mod, 0 + mod, 0 + mod, 1 - mod, 1 - mod, 1 - mod)
		setBlockName("NiflheimIce")
		setBlockTextureName(ModInfo.MODID + ":NiflheimIce")
		setBlockUnbreakable()
		setHarvestLevel("pick", 2)
		setLightOpacity(0)
		setResistance(java.lang.Float.MAX_VALUE)
		setStepSound(Block.soundTypeGlass)
		tickRandomly = true
		slipperiness = 0.98f
	}
	
	override fun getPlayerRelativeBlockHardness(player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Float {
		val metadata = world.getBlockMetadata(x, y, z)
		var hardness = getBlockHardness(world, x, y, z)
		
		val bbls = PlayerHandler.getPlayerBaubles(player)
		if (bbls.getStackInSlot(0) != null && bbls.getStackInSlot(0).item === AlfheimItems.elfIcePendant && ManaItemHandler.requestManaExact(bbls.getStackInSlot(0), player, 5, true)) hardness = 2f
		
		if (hardness < 0.0f) return 0.0f
		
		return if (!ForgeHooks.canHarvestBlock(this, player, metadata))
			player.getBreakSpeed(this, true, metadata, x, y, z) / hardness / 100f
		else
			player.getBreakSpeed(this, false, metadata, x, y, z) / hardness / 30f
	}
	
	override fun isOpaqueCube(): Boolean {
		return false
	}
	
	@SideOnly(Side.CLIENT)
	override fun getRenderBlockPass(): Int {
		return 1
	}
	
	@SideOnly(Side.CLIENT)
	override fun shouldSideBeRendered(world: IBlockAccess, x: Int, y: Int, z: Int, side: Int): Boolean {
		return world.getBlock(x, y, z) !== this && !world.getBlock(x, y, z).isOpaqueCube
	}
	
	override fun quantityDropped(r: Random?): Int {
		return 0
	}
	
	public override fun dropBlockAsItem(w: World, x: Int, y: Int, z: Int, s: ItemStack) {}
	
	override fun onEntityWalking(w: World?, x: Int, y: Int, z: Int, e: Entity?) {
		if (e is EntityPlayer) {
			val player = e as EntityPlayer?
			if (PlayerHandler.getPlayerBaubles(player!!).getStackInSlot(0) != null && BaublesApi.getBaubles(player).getStackInSlot(0).item === AlfheimItems.elfIcePendant) return
		}
		e!!.setInWeb()
		if (!w!!.isRemote && e is EntityLivingBase) {
			val l = e as EntityLivingBase?
			if (!l!!.isPotionActive(Potion.poison)) l.addPotionEffect(PotionEffect(Potion.poison.id, 100, 2))
			l.addPotionEffect(PotionEffect(Potion.moveSlowdown.id, 25, 2))
		}
	}
	
	override fun onEntityCollidedWithBlock(w: World?, x: Int, y: Int, z: Int, e: Entity?) {
		this.onEntityWalking(w, x, y, z, e)
	}
	
	override fun updateTick(world: World, x: Int, y: Int, z: Int, rand: Random?) {
		if (world.gameRules.getGameRuleBooleanValue("doFireTick")
			&& rand!!.nextInt(10) == 0
			&& world.getEntitiesWithinAABB(EntityMob::class.java, AxisAlignedBB.getBoundingBox(x.toDouble(), y.toDouble(), z.toDouble(), (x + 1).toDouble(), (y + 1).toDouble(), (z + 1).toDouble()).expand(5.0, 5.0, 5.0)).isEmpty()) {
			world.setBlockToAir(x, y, z)
		}
	}
	
	override fun tickRate(world: World?): Int {
		return 1
	}
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack): LexiconEntry {
		return AlfheimLexiconData.ruling
	}
}