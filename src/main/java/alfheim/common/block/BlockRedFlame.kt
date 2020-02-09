package alfheim.common.block

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.common.core.handler.AlfheimConfigHandler
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
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.util.IIcon
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.mana.ManaItemHandler
import java.util.*

class BlockRedFlame: BlockFire(), ILexiconable {
	
	lateinit var icons: Array<IIcon>
	
	init {
		setBlockName("MuspelheimFire")
		setBlockUnbreakable()
		setCreativeTab(null)
		setLightLevel(1f)
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
		
		if (hardness < 0f) return 0f
		
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
			val soulburn = PotionEffect(AlfheimConfigHandler.potionIDSoulburn, 200)
			soulburn.curativeItems.clear()
			entity.addPotionEffect(soulburn)
			if (ASJUtilities.isServer) AlfheimCore.network.sendToAll(MessageEffect(entity.entityId, soulburn.potionID, soulburn.duration, soulburn.amplifier))
		}
		entity.setInWeb()
	}
	
	override fun updateTick(world: World, x: Int, y: Int, z: Int, rand: Random) {
		if (world.gameRules.getGameRuleBooleanValue("doFireTick")) {
			if (!canPlaceBlockAt(world, x, y, z) || world.rand.nextInt(100) == 0)
				world.setBlockToAir(x, y, z)
			
		}
	}
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack) = AlfheimLexiconData.ruling
}
